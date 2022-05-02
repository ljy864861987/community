package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommunityConstant;
import com.nowcoder.community.service.DiscussPostsService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class HomeController implements CommunityConstant {

	@Autowired
	private DiscussPostsService discussPostsService;

	@Autowired
	private UserService userService;

	@Autowired
	private LikeService likeService;

	@RequestMapping(path = "/index", method = RequestMethod.GET)
	public String getIndexPage(Model model, Page page) {
		//方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model.
		//所以，在thymeleaf中可以直接访问Page对象中的数据.
		page.setRows(discussPostsService.findDiscussPostsRows(0));
		page.setPath("/index");

		List<DiscussPost> list = discussPostsService.findDiscussPosts(0, page.getOffset(), page.getLimit());
		List<Map<String, Object>> discussPosts = new ArrayList<>();
		if (list != null) {
			for (DiscussPost posts : list) {
				Map<String, Object> map = new HashMap<>();
				map.put("post", posts);
				User user = userService.findUserById(posts.getUserId());
				map.put("user", user);

				// 查询赞的数量
				long likeCount = likeService.findElementLikeCount(ENTITY_TYPE_POST, posts.getId());
				map.put("likeCount", likeCount);

				discussPosts.add(map);
			}
		}
		model.addAttribute("discussPosts", discussPosts);
		return "/index";
	}

	@RequestMapping(path = "/error", method = RequestMethod.GET)
	public String getErrorPage() {
		return "/error/500";
	}

	// 没有访问权限时
	@RequestMapping(path = "/denied", method = RequestMethod.GET)
	public String getDeniedPage() {
		return "/error/404";
	}

}
