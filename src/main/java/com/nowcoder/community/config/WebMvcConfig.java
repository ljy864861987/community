package com.nowcoder.community.config;

import com.nowcoder.community.controller.intercepter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//	@Autowired
//	private A_Intercepter a_intercepter;

	@Autowired
	private LoginTicketInterceptor loginTicketInterceptor;

//	@Autowired
//	private LoginRequiredInterceptor loginRequiredInterceptor;

	@Autowired
	private MessageInterceptor messageInterceptor;

	@Autowired
	private DataInterceptor dataInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(a_intercepter)
//				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg")
//				.addPathPatterns("/register", "/login");

		registry.addInterceptor(loginTicketInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");

//		registry.addInterceptor(loginRequiredInterceptor)
//				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");

		registry.addInterceptor(messageInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");

		registry.addInterceptor(dataInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");
	}
}
