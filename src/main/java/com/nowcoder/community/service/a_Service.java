package com.nowcoder.community.service;

import com.nowcoder.community.dao.A_Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class a_Service {

	@Autowired
	private A_Dao a_dao;

	public a_Service(){
		System.out.println("实例化A_Service");
	}

	@PostConstruct
	public void init(){
		System.out.println("初始化A_Service");
	}

	@PreDestroy
	public void destory(){
		System.out.println("销毁A_Service");
	}

	public String find(){
		return a_dao.select();
	}

}
