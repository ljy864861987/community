package com.nowcoder.community.controller;

import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPosts;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.CommunityConstant;
import com.nowcoder.community.service.DiscussPostsService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
			return CommunityUtil.getJSONString(403, "还没登录，蠢蛋。");
		}
		DiscussPosts discussPosts = new DiscussPosts();
		discussPosts.setTitle(title);
		discussPosts.setContent(content);
		discussPosts.setCreateTime(new Date());
		discussPosts.setUserId(user.getId());

		discussPostsService.addDiscussPost(discussPosts);
		//报错的情况将来统一处理.
		return CommunityUtil.getJSONString(0, "发布成功！");
	}

	@RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
	public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
		// 帖子
		DiscussPosts post = discussPostsService.finfDiscussPostById(discussPostId);
		model.addAttribute("post", post);
		// 作者
		User user = userService.findUserById(post.getUserId());
		model.addAttribute("user", user);

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

}
