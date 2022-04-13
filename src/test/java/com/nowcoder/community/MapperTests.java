package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPosts;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.xmlunit.util.Mapper;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DiscussPostMapper discussPostMapper;

	@Autowired
	private LoginTicketMapper loginTicketMapper;

	@Test
	public void testSelectUser() {
		System.out.println(userMapper.selectById(101));

		System.out.println(userMapper.selectByName("liubei"));

		System.out.println(userMapper.selectByEmail("nowcoder101@sina.com"));
	}

	@Test
	public void testInsertUser() {
		User user = new User();
		user.setUsername("Test");
		user.setPassword("123456");
		user.setSalt("lbw");
		user.setEmail("lbwt@QWE.com");
		user.setHeaderUrl("http://www.nowcoder.com/182.png");
		user.setCreateTime(new Date());

		int rows = userMapper.insertUser(user);
		System.out.println(rows);
		System.out.println(user.getId());
	}

	@Test
	public void updateUser() {
		int rows = userMapper.updateStatus(150, 1);
		System.out.println(rows);

		rows = userMapper.updateHeader(150, "http://www.nowcoder.com/188.png");
		System.out.println(rows);

		rows = userMapper.updatePassword(150, "864861");
		System.out.println(rows);
	}

	@Test
	public void setsSelectPosts() {
		List<DiscussPosts> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
		for (DiscussPosts post : list) {
			System.out.println(post);
		}
		int Rows = discussPostMapper.selectDiscussPostsRows(149);
		System.out.println(Rows);
	}

	@Test
	public void insertLoginTicket() {
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(101);
		loginTicket.setTicket("abc");
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

		loginTicketMapper.insertLoginTicket(loginTicket);
	}

	@Test
	public void testSelectLoginTicket() {
		LoginTicket loginTicket = loginTicketMapper.selectByLoginTicket("abc");
		System.out.println(loginTicket);
		loginTicketMapper.updateStatus("abc", 1);
	}

}
