package com.Lee.e3ssoweb.controller;

import com.Lee.dto.E3Result;
import com.Lee.pojo.TbUser;
import com.Lee.sso.service.RegisterService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 注册功能Controller
 */
@Controller
public class RegitsterController {
	
	@Reference
	private RegisterService registerService;

	@RequestMapping("/page/login")
	public String showLogin() {
		return "login";
	}


	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
		E3Result e3Result = registerService.checkData(param, type);
		return e3Result;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user) {
		E3Result e3Result = registerService.register(user);
		return e3Result;
	}
}
