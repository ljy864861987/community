package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

//@Repository("a_Hibernate")
public class A_DaoHibernateImpl implements A_Dao {
	@Override
	public String select() {
		return "Hibernate";
	}
}
