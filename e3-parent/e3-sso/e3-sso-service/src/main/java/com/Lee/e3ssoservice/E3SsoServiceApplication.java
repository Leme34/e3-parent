package com.Lee.e3ssoservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.Lee.dao")
@SpringBootApplication
public class E3SsoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3SsoServiceApplication.class, args);
	}
}
