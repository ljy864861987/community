package com.nowcoder.community;

import com.nowcoder.community.dao.A_Dao;
import com.nowcoder.community.service.A_Service;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);

		A_Dao a_Dao = applicationContext.getBean(com.nowcoder.community.dao.A_Dao.class);
		System.out.println(a_Dao.select());

		a_Dao = applicationContext.getBean("A_Hibernate", com.nowcoder.community.dao.A_Dao.class);
		System.out.println(a_Dao.select());

	}

	@Test
	public void testBeanManagement(){
		A_Service a_service = applicationContext.getBean(A_Service.class);
		System.out.println(a_service);
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	private SimpleDateFormat simpleDateFormat;
	@Autowired
	@Qualifier("a_Hibernate")
	private A_Dao a_dao;

	@Test
	public void testDI(){
		System.out.println(a_dao.select());
		System.out.println(simpleDateFormat.format(new Date()));
	}

}
