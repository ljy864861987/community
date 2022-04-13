package com.nowcoder.community.config;

import com.nowcoder.community.controller.intercepter.A_Intercepter;
import com.nowcoder.community.controller.intercepter.LoginRequiredInterceptor;
import com.nowcoder.community.controller.intercepter.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private A_Intercepter a_intercepter;

	@Autowired
	private LoginTicketInterceptor loginTicketInterceptor;

	@Autowired
	private LoginRequiredInterceptor loginRequiredInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(a_intercepter)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg")
				.addPathPatterns("/register", "/login");

		registry.addInterceptor(loginTicketInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");

		registry.addInterceptor(loginRequiredInterceptor)
				.excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.png", "/**/*.jpeg");
	}
}
