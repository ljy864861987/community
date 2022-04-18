package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

	@Autowired
	private SensitiveFilter sensitiveFilter;

	@Test
	public void testSensitiveTest(){
		String text = "this way 赌博 开;票 you c 嫖赌博娼 take it easy!";
		System.out.println(sensitiveFilter.filter(text));
	}

}
