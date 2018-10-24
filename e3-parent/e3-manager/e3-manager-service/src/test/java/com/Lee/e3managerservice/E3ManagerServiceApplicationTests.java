package com.Lee.e3managerservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.Lee.dao")
public class E3ManagerServiceApplicationTests {

	@Test
	public void contextLoads() {
		//只初始化容器以注册服务，但不启动tomcat
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
