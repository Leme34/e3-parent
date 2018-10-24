package com.Lee.e3portalweb;

import com.Lee.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class E3PortalWebApplicationTests {

	@Reference
	private ContentService contentService;

	@Test
	public void contextLoads() {
		contentService.getContentListByCid(89);
	}

}
