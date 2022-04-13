package com.nowcoder.community.controller.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class A_Intercepter implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(A_Intercepter.class);

	//在Controller之前执行
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("preHandle" + handler.toString());
		return true;
	}

	//在Controller之后,TemplateEngine之前执行
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("postHandle:" + handler.toString());
	}

	//在TemplateEngine之后执行
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logger.debug("afterCompletion:" + handler.toString());
	}
}
