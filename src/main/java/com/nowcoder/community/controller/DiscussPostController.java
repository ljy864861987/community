package com.nowcoder.community.controller;

import com.nowcoder.community.entity.*;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.*;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/discuss")
public class DiscussPostController implements CommunityConstant {

	@Autowired
	private DiscussPostsService discussPostsService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private LikeService likeService;

	@Autowired
	private EventProducer eventProducer;

	@Autowired
	private RedisTemplate redisTemplate;

	@RequestMapping(path = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String addDiscussPost(String title, String content) {
		if (StringUtils.isBlank(title)) {
			return CommunityUtil.getJSONString(-1, "标题不能为空!");
		}
		if (StringUtils.isBlank(content)) {
			return CommunityUtil.getJSONString(-1, "正文不能为空!");
		}
		User user = hostHolder.getUser();
		if (user == null) {
			return CommunityUtil.getJSONString(401, "还没登录，蠢蛋。");
		}
		DiscussPost discussPost = new DiscussPost();
		discussPost.setTitle(title);
		discussPost.setContent(content);
		discussPost.setCreateTime(new Date());
		discussPost.setUserId(user.getId());

		discussPostsService.addDiscussPost(discussPost);

		// 触发发帖事件
		Event event = new Event()
				.setTopic(TOPIC_PUBLISH)
				.setUserId(user.getId())
				.setEntityType(ENTITY_TYPE_POST)
				.setEntityId(discussPost.getId());
		eventProducer.fireEvent(event);

		// 计算帖子分数
		String redisKey = RedisKeyUtil.getPostScoreKey();
		redisTemplate.opsForSet().add(redisKey, discussPost.getId());

		//报错的情况将来统一处理.
		return CommunityUtil.getJSONString(0, "发布成功！");
	}

	@RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
	public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
		// 帖子
		DiscussPost post = discussPostsService.finfDiscussPostById(discussPostId);
		model.addAttribute("post", post);
		// 作者
		User user = userService.findUserById(post.getUserId());
		model.addAttribute("user", user);
		// 帖子赞数量
		long likeCount = likeService.findElementLikeCount(ENTITY_TYPE_POST, discussPostId);
		model.addAttribute("likeCount", likeCount);
		// 点赞状态
		int likeStatus = hostHolder.getUser() == null ? 0 :
				likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
		model.addAttribute("likeStatus", likeStatus);

		// 评论分页信息
		page.setLimit(5);
		page.setPath("/discuss/detail/" + discussPostId);
		page.setRows(post.getCommentCount());

		// 评论：给帖子的评论
		// 回复：给评论的评论
		// 评论列表
		List<Comment> commentList = commentService.findCommentsByEntity(
				ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
		// 评论VO列表
		List<Map<String, Object>> commentVoList = new ArrayList<>();
		if (commentList != null) {
			for (Comment comment : commentList) {
				// 评论VO
				Map<String, Object> commentVo = new HashMap<>();
				// 评论
				commentVo.put("comment", comment);
				// 作者
				commentVo.put("user", userService.findUserById(comment.getUserId()));
				// 评论赞数量
				likeCount = likeService.findElementLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
				commentVo.put("likeCount", likeCount);
				// 评论点赞状态
				likeStatus = hostHolder.getUser() == null ? 0 :
						likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
				commentVo.put("likeStatus", likeStatus);
				//回复列表
				List<Comment> replyList = commentService.findCommentsByEntity(
						ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
				// 回复的VO列表
				List<Map<String, Object>> replyVoList = new ArrayList<>();
				if (replyList != null) {
					for (Comment reply : replyList) {
						Map<String, Object> replyVo = new HashMap<>();
						// 回复
						replyVo.put("reply", reply);
						// 作者
						replyVo.put("user", userService.findUserById(reply.getUserId()));
						// 回复赞数量
						likeCount = likeService.findElementLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
						replyVo.put("likeCount", likeCount);
						// 回复点赞状态
						likeStatus = hostHolder.getUser() == null ? 0 :
								likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
						replyVo.put("likeStatus", likeStatus);
						// 回复目标
						User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
						replyVo.put("target", target);

						replyVoList.add(replyVo);
					}
				}
				commentVo.put("replys", replyVoList);

				//回复数量
				int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
				commentVo.put("replyCount", replyCount);

				commentVoList.add(commentVo);
			}
		}

		model.addAttribute("comments", commentVoList);

		return "/site/discuss-detail";
	}

	// 置顶
	@RequestMapping(path = "/top", method = RequestMethod.POST)
	@ResponseBody
	public String setTop(int id) {
		int type = discussPostsService.finfDiscussPostById(id).getType();
		if (type == 0) {
			discussPostsService.updateType(id, 1);
		} else {
			discussPostsService.updateType(id, 0);
		}

		// 触发发帖事件
		Event event = new Event()
				.setTopic(TOPIC_PUBLISH)
				.setUserId(hostHolder.getUser().getId())
				.setEntityType(ENTITY_TYPE_POST)
				.setEntityId(id);
		eventProducer.fireEvent(event);

		return CommunityUtil.getJSONString(0);
	}

	// 加精
	@RequestMapping(path = "/wonderful", method = RequestMethod.POST)
	@ResponseBody
	public String setWonderful(int id) {
		int status = discussPostsService.finfDiscussPostById(id).getStatus();
		if (status == 0) {
			discussPostsService.updateStatus(id, 1);
		} else if (status == 1) {
			discussPostsService.updateStatus(id, 0);
		} else {
			return CommunityUtil.getJSONString(500, "参数错误！");
		}

		// 触发发帖事件
		Event event = new Event()
				.setTopic(TOPIC_PUBLISH)
				.setUserId(hostHolder.getUser().getId())
				.setEntityType(ENTITY_TYPE_POST)
				.setEntityId(id);
		eventProducer.fireEvent(event);

		// 计算帖子分数
		String redisKey = RedisKeyUtil.getPostScoreKey();
		redisTemplate.opsForSet().add(redisKey, id);

		return CommunityUtil.getJSONString(0);
	}

	// 删除
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String setDelete(int id) {
		discussPostsService.updateStatus(id, 2);

		// 触发删帖事件
		Event event = new Event()
				.setTopic(TOPIC_DELETE)
				.setUserId(hostHolder.getUser().getId())
				.setEntityType(ENTITY_TYPE_POST)
				.setEntityId(id);
		eventProducer.fireEvent(event);

		return CommunityUtil.getJSONString(0);
	}

}
