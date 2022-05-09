package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testStrings() {
		String redisKey = "test:user";

		redisTemplate.opsForHash().put(redisKey, "id", 1);
		redisTemplate.opsForHash().put(redisKey, "username", "zhangsan");
		System.out.println(redisTemplate.opsForHash().get(redisKey, "id"));
		System.out.println(redisTemplate.opsForHash().get(redisKey, "username"));

	}

	// 多次访问同一个key
	@Test
	public void testBoundOperations() {
		String redisKey = "test:count";
		BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
		operations.set(88);
		System.out.println(operations.get());
	}

	// 编程式事务
	@Test
	public void testTransactional() {
		Object obj = redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String redisKey = "test:tx";
				operations.multi();// 启用事务

				operations.opsForSet().add(redisKey, "zhangsan");
				operations.opsForSet().add(redisKey, "lisi");
				operations.opsForSet().add(redisKey, "wangwu");

				System.out.println(operations.opsForSet().members(redisKey));

				return operations.exec();// 提交事务
			}
		});
		System.out.println(obj);
	}

	// 统计20w个重复数据的独立总数
	@Test
	public void testHyperLogLog() {
		String redisKey = "test:hll:01";

		for (int i = 0; i <= 100000; i++) {
			redisTemplate.opsForHyperLogLog().add(redisKey, i);
		}

		for (int i = 1; i <= 100000; i++) {
			int r = (int) (Math.random() * 100000 + 1);
			redisTemplate.opsForHyperLogLog().add(redisKey, r);
		}

		long size = redisTemplate.opsForHyperLogLog().size(redisKey);

		System.out.println(size);
	}


}
