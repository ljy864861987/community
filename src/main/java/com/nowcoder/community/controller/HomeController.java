package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPosts;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostsService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class HomeController {

	@Autowired
	private DiscussPostsService discussPostsService;

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/index", method = RequestMethod.GET)
	public String getIndexPage(Model model, Page page){
		//方法调用前，SpringMVC会自动实例化Model和Page，并将Page注入Model.
		//所以，在thymeleaf中可以直接访问Page对象中的数据.
		page.setRows(discussPostsService.findDiscussPostsRows(0));
		page.setPath("/index");

		List<DiscussPosts> list = discussPostsService.findDiscussPosts(0, page.getOffset(), page.getLimit());
		List<Map<String, Object>> discussPosts = new ArrayList<>();
		if(list != null){
			for(DiscussPosts posts : list){
				Map<String, Object> map = new HashMap<>();
				map.put("post", posts);
				User user = userService.findUserById(posts.getUserId());
				map.put("user", user);
				discussPosts.add(map);
			}
		}
		model.addAttribute("discussPosts", discussPosts);
		return "/index";
	}

}
