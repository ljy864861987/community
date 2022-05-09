package com.nowcoder.community;

import com.nowcoder.community.service.A_Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TreadPoolTests {

	private static final Logger logger = LoggerFactory.getLogger(TreadPoolTests.class);

	// JDK普通线程池
	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	// JDK可执行定时任务的线程池
	private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

	// Spring普通线程池
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	// Spring可执行定时任务的线程池
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	private A_Service a_service;


	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// 1.JDK普通线程池
	@Test
	public void testExecutorService() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				logger.debug("Hello ExecutorService");
			}
		};

		for (int i = 0; i < 10; i++) {
			executorService.submit(task);
		}

		sleep(10000);
	}

	// 2.JDK定时任务线程池
	@Test
	public void testScheduledExecutorService() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				logger.debug("Hello ScheduledExecutorService");
			}
		};

		scheduledExecutorService.scheduleAtFixedRate(task, 2000, 2000, TimeUnit.MILLISECONDS);

		sleep(30000);
	}

	// 3.Spring普通线程池
	@Test
	public void testThreadPoolTaskExecutor() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				logger.debug("Hello ThreadPoolTaskExecutor");
			}
		};
		for (int i = 0; i < 10; i++) {
			threadPoolTaskExecutor.submit(task);
		}

		sleep(10000);
	}

	// 4.Spring定时任务线程池
	@Test
	public void testThreadPoolTaskScheduler() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				logger.debug("Hello ThreadPoolTaskScheduler");
			}
		};
		Date startTime = new Date(System.currentTimeMillis() + 5000);
		threadPoolTaskScheduler.scheduleAtFixedRate(task, startTime, 1000);

		sleep(300000);
	}

	// 5.Spring普通线程池（简化）
	@Test
	public void testThreadPoolTaskExecutorSimple() {
		for (int i = 0; i < 10; i++) {
			a_service.execute1();
		}

		sleep(10000);
	}

	// 6.Spring定时任务线程池（简化）
	@Test
	public void testThreadPoolTaskSchedulerSimple() {
		sleep(30000);
	}


}
