package com.nowcoder.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class A_DaoMyBatisImpl implements A_Dao{
	@Override
	public String select() {
		return "MyBatis";
	}
}
