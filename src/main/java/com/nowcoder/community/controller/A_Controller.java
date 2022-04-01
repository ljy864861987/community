package com.nowcoder.community.controller;

import com.nowcoder.community.service.a_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/a")
public class A_Controller {

	@Autowired
	private a_Service a_service;

	@RequestMapping("/hello")
	@ResponseBody
	public String say(){
		return "Hello Offer.";
	}

	@RequestMapping("/data")
	@ResponseBody
	public String getData(){
		return a_service.find();
	}

	@RequestMapping("/http")
	public void http(HttpServletRequest request, HttpServletResponse response){
		//获取请求数据
		System.out.println(request.getMethod());
		System.out.println(request.getServletPath());
		Enumeration<String> enumeration = request.getHeaderNames();
		while(enumeration.hasMoreElements()){
			String name = enumeration.nextElement();
			String value = request.getHeader(name);
			System.out.println(name + ":" + value);
		}
		System.out.println(request.getParameter("code"));

		//返回相应数据
		response.setContentType("text/html;charset=utf-8");
		try(
				PrintWriter writer = response.getWriter();
		) {

			writer.write("<h1>牛客网</h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//GET请求

	// /students?current=1&limit=20
	@RequestMapping(path="/students", method = RequestMethod.GET)
	@ResponseBody
	public String getStudents(
			@RequestParam(name = "current", required = false, defaultValue = "1") int current,
			@RequestParam(name = "limit", required = false, defaultValue = "20") int limit
	){
		System.out.println(current);
		System.out.println(limit);
		return "some students";
	}

	// student/123
	@RequestMapping(path = "student/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getStudent(@PathVariable("id") int id){
		System.out.println(id);
		return "a student";
	}

	//POST请求
	@RequestMapping(path = "/student", method = RequestMethod.POST)
	@ResponseBody
	public String saveStudent(String name, int age){
		System.out.println(name);
		System.out.println(age);
		return "success";
	}

	//相应HTML数据

	@RequestMapping(path = "/teacher", method = RequestMethod.GET)
	public ModelAndView getTeacher(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("name","张三");
		mav.addObject("age", "20");
		mav.setViewName("/demo/view");
		return mav;
	}

	@RequestMapping(path = "/school", method = RequestMethod.GET)
	public String getSchool(Model model){
		model.addAttribute("name", "大连理工大学");
		model.addAttribute("age", 73);
		return "/demo/view";
	}

	//响应JSON数据(异步请求)
	//Java对象 ——》JSON对象 ——》JS对象

	@RequestMapping(path = "/emp", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getEmp(){
		Map<String, Object> emp = new HashMap<>();
		emp.put("name", "张三");
		emp.put("age", 23);
		emp.put("salary", 8000.00);
		return emp;
	}

	@RequestMapping(path = "/emps", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getEmps(){
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> emp = new HashMap<>();
		emp.put("name", "张三");
		emp.put("age", 23);
		emp.put("salary", 8000.00);
		list.add(emp);

		emp.put("name", "里斯");
		emp.put("age", 26);
		emp.put("salary", 9000.00);
		list.add(emp);

		emp.put("name", "槐五");
		emp.put("age", 28);
		emp.put("salary", 18000.00);
		list.add(emp);

		return list;
	}

}
