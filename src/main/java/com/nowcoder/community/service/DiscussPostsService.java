package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPosts;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostsService {

	@Autowired
	private DiscussPostMapper discussPostMapper;

	@Autowired
	private SensitiveFilter sensitiveFilter;

	public List<DiscussPosts> findDiscussPosts(int userId, int offset, int limit) {
		return discussPostMapper.selectDiscussPosts(userId, offset, limit);
	}

	public int findDiscussPostsRows(int userId) {
		return discussPostMapper.selectDiscussPostsRows(userId);
	}

	public int addDiscussPost(DiscussPosts discussPosts) {
		if (discussPosts == null) {
			throw new IllegalArgumentException("参数不能为空！");
		}
		//转义Html
		discussPosts.setTitle(HtmlUtils.htmlEscape(discussPosts.getTitle()));
		discussPosts.setContent(HtmlUtils.htmlEscape(discussPosts.getContent()));
		//过滤敏感词
		discussPosts.setTitle(sensitiveFilter.filter(discussPosts.getTitle()));
		discussPosts.setContent(sensitiveFilter.filter(discussPosts.getContent()));

		return discussPostMapper.insertDiscussPost(discussPosts);
	}

	public DiscussPosts finfDiscussPostById(int id) {
		return discussPostMapper.selectDiscussPostById(id);
	}

	public int updateCommentCount(int id, int commentCount) {
		return discussPostMapper.updateCommentCount(id, commentCount);
	}

}
