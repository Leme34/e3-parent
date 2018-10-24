package com.Lee.e3ssoservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@MapperScan("com.Lee.dao")
@RunWith(SpringRunner.class)
@SpringBootTest
public class E3SsoServiceApplicationTests {

	@Test
	public void contextLoads() {
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
