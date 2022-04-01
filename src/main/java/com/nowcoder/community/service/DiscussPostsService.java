package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostsService {

	@Autowired
	private DiscussPostMapper discussPostMapper;

	public List<DiscussPosts> findDiscussPosts(int userId, int offset, int limit){
		return discussPostMapper.selectDiscussPosts(userId, offset, limit);
	}

	public int findDiscussPostsRows(int userId){
		return discussPostMapper.selectDiscussPostsRows(userId);
	}

}
