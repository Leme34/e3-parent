package com.Lee.e3portalweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class E3PortalWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3PortalWebApplication.class, args);
	}

	/**
	 * 自定义dispatchServlet的访问路径
	 */
//	@Bean
//	public ServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet){
//		System.out.println("修改urlMapping");
//		ServletRegistrationBean registrationBean = new ServletRegistrationBean(dispatcherServlet);
//		//容器在应用启动时就加载这个servlet
//		registrationBean.setLoadOnStartup(1);
//		//清除请求拦截器
//		registrationBean.getUrlMappings().clear();
//		//添加需要拦截的urlMapping
//		registrationBean.addUrlMappings("*.html");
//		return registrationBean;
//	}

}
