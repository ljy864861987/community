package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CaffeineTests {

	@Autowired
	private DiscussPostsService postsService;

	@Test
	public void testCache() {
		System.out.println(postsService.findDiscussPosts(0, 0, 10, 1));
		System.out.println(postsService.findDiscussPosts(0, 0, 10, 1));
		System.out.println(postsService.findDiscussPosts(0, 0, 10, 1));
		System.out.println(postsService.findDiscussPosts(0, 0, 10, 0));
	}

	@Test
	public void insert() {
		DiscussPost post = new DiscussPost();
		post.setTitle("压力测试");
		post.setContent("这是压力测试帖子");
		post.setCreateTime(new Date());
		post.setStatus(0);
		for (int i = 0; i < 300000; i++) {
			postsService.addDiscussPost(post);
		}
	}

}
