package com.Lee.e3cartservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.Lee.dao")
@SpringBootApplication
public class E3CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3CartServiceApplication.class, args);
	}
}
