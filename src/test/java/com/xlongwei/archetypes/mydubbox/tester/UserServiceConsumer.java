package com.xlongwei.archetypes.mydubbox.tester;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.xlongwei.archetypes.mydubbox.UserService;
import com.xlongwei.archetypes.mydubbox.model.User;

public class UserServiceConsumer extends BaseTester {
	@Test public void webservice() {
		testProtocol("webservice", "8081");
	}
	@Test public void dubbo() {
		testProtocol("dubbo", "20880");
	}
	@Test public void rmi() {
		testProtocol("rmi", "1099");
	}
	@Test public void hessian() {
		testProtocol("hessian", "8083");
	}
	
	@Test public void protocols() {
		testProtocol("dubbo", "20880");
		testProtocol("rmi", "1099");
		testProtocol("webservice", "8081");
		testProtocol("hessian", "8083");
	}
	
	private void testProtocol(String protocol, String port) {
		UserService userService = refer(UserService.class, protocol, port);
		testService(userService);
	}
	private void testService(UserService userService) {
		logger.info(JSON.toJSONString(userService.getUser(1L)));
//		logger.info(JSON.toJSONString(userService.getUser(0L)));
		
		User user = new User();
//		logger.info(JSON.toJSONString(userService.registerUser(user)));
		user.setName("testUser");
		logger.info(JSON.toJSONString(userService.registerUser(user)));
//		user.setName("te");
//		logger.info(JSON.toJSONString(userService.registerUser(user)));
	}
}
