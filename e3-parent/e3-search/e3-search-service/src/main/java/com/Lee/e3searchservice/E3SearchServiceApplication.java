package com.Lee.e3searchservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.Lee.dao")
@ComponentScan(basePackages = {"com.Lee.e3mall.search.dao","com.Lee.e3searchservice.conf","com.Lee.e3mall.search.message"})
@SpringBootApplication
public class E3SearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3SearchServiceApplication.class, args);
	}
}
