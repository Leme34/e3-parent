package com.Lee.e3ssoweb.controller;

import com.Lee.dto.E3Result;
import com.Lee.sso.service.TokenService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 根据请求中的token查询用户信息Controller
 */
@Controller
public class TokenController {

	@Reference
	private TokenService tokenService;
	
	/*@RequestMapping(value="/user/token/{token}", 
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE"application/json;charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		E3Result result = tokenService.getUserByToken(token);
		//响应结果之前，判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			//把结果封装成一个js语句响应
			return callback + "(" + JsonUtils.objectToJson(result)  + ");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {  //传入callback参数实现跨域访问
		//判断用户是否过期
		E3Result result = tokenService.getUserByToken(token);
		//响应结果之前，判断是否为jsonp跨域请求
		if (!StringUtils.isEmpty(callback)) {
			//jsonp内部原理还是利用jackson的支持，把数据渲染成js字符串(不用自己拼串)形式返回给客户端
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}