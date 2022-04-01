package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

	@Autowired
	private MailClient mailClient;

	@Autowired
	private TemplateEngine templateEngine;

	@Test
	public void testTextMail(){
		mailClient.sendMail("864861987@qq.com", "TEXTS", "this is a test.");
	}

	@Test
	public void testHtmlMail(){
		Context context = new Context();
		context.setVariable("username", "LJY");

		String content = templateEngine.process("/mail/demo", context);
		System.out.println(content);

		mailClient.sendMail("ljy864861987@163.com", "TESThtml", content);
	}

}
