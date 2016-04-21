package com.xlongwei.archetypes.mydubbox;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:provider.xml" })
public class Provider{
	@Test
	public void provide() throws IOException {
		System.out.println("mydubbox server started!");
		System.in.read();
		System.out.println("Provider Exit");
	}
}
