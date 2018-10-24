package com.Lee.e3cartweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class E3CartWebApplication {

	/**
	 * 自定义dispatcherServlet的拦截路径
	 */
	@Bean
	ServletRegistrationBean dispatcherServletServletRegistrationBean(DispatcherServlet dispatcherServlet){
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(dispatcherServlet);
		//先清空
		registrationBean.getUrlMappings().clear();
		//添加拦截路径
		registrationBean.addUrlMappings("*.html");
		registrationBean.addUrlMappings("*.action");  //解决*.html请求返回json数据时报406错误
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(E3CartWebApplication.class, args);
	}
}
