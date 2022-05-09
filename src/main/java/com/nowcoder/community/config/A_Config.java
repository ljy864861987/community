package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

//@Configuration
public class A_Config {

	@Bean
	public SimpleDateFormat simpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

}
