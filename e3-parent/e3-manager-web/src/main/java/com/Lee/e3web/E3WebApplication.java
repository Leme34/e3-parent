package com.Lee.e3web;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class)
@SpringBootApplication
public class E3WebApplication {

	public static void main(String[] args) {
		SpringApplication.run(E3WebApplication.class, args);
	}
}
