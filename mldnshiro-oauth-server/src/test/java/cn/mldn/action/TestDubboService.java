package cn.mldn.action;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.mldn.service.IClientService;
import cn.mldn.service.IMemberAuthorizationService;

@ContextConfiguration(locations = { "classpath:spring-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestDubboService {
	@Reference
	private IClientService clientService ;
	@Test
	public void test() {
		System.out.println(this.clientService.getByClientId("mldn_client"));
	}
	
}
