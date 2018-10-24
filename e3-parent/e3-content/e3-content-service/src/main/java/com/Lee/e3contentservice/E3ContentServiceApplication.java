package com.Lee.e3contentservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.Lee.dao")
@SpringBootApplication
public class E3ContentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3ContentServiceApplication.class, args);
	}
}
