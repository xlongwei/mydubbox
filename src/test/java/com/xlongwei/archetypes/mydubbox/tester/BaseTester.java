package com.xlongwei.archetypes.mydubbox.tester;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.xlongwei.archetypes.mydubbox.util.Result;

public class BaseTester {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected String url = "http://127.0.0.1:8080";
	private Client client = ClientBuilder.newClient();//使用Client通过http请求时，可避免@Path等注解污染接口
	protected String method(String method, String path) {
		return client.target(url+path).request(MediaType.APPLICATION_JSON).method(method).readEntity(String.class);
	}
	/** 支持entity参数提交 */
	protected <T> String entity(String method, String path, T entity) {
		return client.target(url+path).request().method(method, Entity.json(entity)).readEntity(String.class);
	}
	/** 支持对象和原始类型返回值 */
	protected <T,E> T data(String method, String path, E entity, Class<T> data) {
		Builder builder = client.target(url+path).request(MediaType.APPLICATION_JSON);
		return entity==null ? builder.method(method, data) : builder.method(method, Entity.json(entity), data);
	}
	/** 支持Result类型返回值 */
	@SuppressWarnings("unchecked")
	protected <T,E> Result<T> result(String method, String path, E entity, Class<T> data) {
		return data(method, path, entity, Result.class);
	}
	/** <dubbo:reference interface="com.xlongwei.archetypes.mydubbox.UserService" protocol="dubbo" /> */
	protected <T> T refer(Class<T> clazz, String protocol, String port) {
		ReferenceConfig<T> refer = new ReferenceConfig<>();
		refer.setApplication(new ApplicationConfig("mydubbox-consumer"));
		refer.setProtocol(protocol);
		//无论是访问registry的rest服务，还是访问外部rest接口，都需要在接口clazz注解@Path等请求路径（污染了接口），以便框架生成正确的http请求
		refer.setRegistry(new RegistryConfig("redis://127.0.0.1:6379"));//使用了tester包里的UserService，和registry里面的不匹配
		refer.setUrl(url.replace("http", protocol).replace("8080", port)+"/"); //访问外部rest接口时直接提供rest://host:port/context/地址
		refer.setInterface(clazz);
		return refer.get();
	}
}
