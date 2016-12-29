package com.xlongwei.archetypes.mydubbox.tester;

import java.net.URL;

import javax.jws.WebService;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Test;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.xlongwei.archetypes.mydubbox.model.User;
import com.xlongwei.archetypes.mydubbox.util.Result;

public class UserServiceTester extends BaseTester {
	/** 通常以http或webservice方式向外开放接口 */
	@Test public void rest_http() {
		logger.info(method(HttpMethod.GET, "/user/1"));
		logger.info(method(HttpMethod.GET, "/user/0"));//@Min(1L)
		logger.info(JSON.toJSONString(data(HttpMethod.GET, "/user/1", null, User.class)));
		logger.info(method(HttpMethod.DELETE, "/user/1"));
		logger.info(method(HttpMethod.DELETE, "/user/0"));
		logger.info(entity(HttpMethod.PUT, "/user/1", "A"));
		logger.info(JSON.toJSONString(result(HttpMethod.PUT, "/user/1", "A", Boolean.class)));
		logger.info(entity(HttpMethod.PUT, "/user/0", "I"));
		User user = new User();//@NotNull
		logger.info(entity(HttpMethod.POST, "/user/register", user));//返回false
		user.setName("testuser");
		logger.info(entity(HttpMethod.POST, "/user/register", user));
		user.setName("te");//@Size(min = 3, max = 50)
		logger.info(entity(HttpMethod.POST, "/user/register", user));
	}
	
	/** 使用dubbo访问rest服务，需要接口有JAX-RS注解，而在接口设计上最好不要被注解污染，因此这里另外写带有注解的接口 */
	@Path("user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(ContentType.APPLICATION_JSON_UTF_8)
	public static interface RsUserService {
	    @GET @Path("{id : \\d+}")
		User getUser(@Min(1L) @PathParam("id") Long id);
	    
	    @PUT  @Path("{id : \\d+}")
	    Result<Boolean> updateUser(@Min(1L) @PathParam("id") Long id, String status);
	    
	    @DELETE @Path("{id : \\d+}")
	    boolean deleteUser(@Min(1L) @PathParam("id") Long id);
	    
	    @POST @Path("register") 
		Long registerUser(User user);
	}
	/** dubbo方式可以供内部模块之间调用（这里用rest协议必须重新定义接口RsUserService并使用JAX-RS注解，其他协议入dubbo等都可以直接用UserService接口） */
	@Test public void rest_dubbo() {
		RsUserService userService = refer(RsUserService.class, "rest", "8080");
		logger.info(JSON.toJSONString(userService.getUser(1L)));
		logger.info(JSON.toJSONString(userService.getUser(0L)));//ValidationExceptionMapper.toResponse覆盖时返回错误提示，否则报异常
		logger.info(JSON.toJSONString(userService.updateUser(1L, "A")));
		logger.info(String.valueOf(userService.deleteUser(1L)));
		
		User user = new User();
		logger.info(JSON.toJSONString(userService.registerUser(user)));
		user.setName("testUser");
		logger.info(JSON.toJSONString(userService.registerUser(user)));
		user.setName("te");
		logger.info(JSON.toJSONString(userService.registerUser(user)));
	}
	
	/** 访问WebService时需要接口有@WebService注解，targetNamespace默认为包名倒序这里需要手动指定否则报错，可以扩展原接口也可以复制所有方法。这里单独定义接口方法，然后可以封装成SDK供客户下载使用，优点是接口参数明确调用方便*/
	@WebService(targetNamespace="http://mydubbox.archetypes.xlongwei.com/")
	public static interface WsUserService {
		User getUser(@Min(1L) @PathParam("id") Long id);
		Result<Boolean> updateUser(@Min(1L) @PathParam("id") Long id, String status);
		boolean deleteUser(@Min(1L) @PathParam("id") Long id);
		Long registerUser(User user);
	}
	//扩展原接口的弊端：可能不方便开放基础接口UserService，WsUserService仅需要与UserService保持方法签名一致，并不需要继承关系
//	public static interface WsUserService extends com.xlongwei.archetypes.mydubbox.UserService { }
	/** 通常以http或webservice方式向外开放接口 */
	@Test public void webservice() throws Exception {
		String wsdlUrl = "http://127.0.0.1:8081/com.xlongwei.archetypes.mydubbox.UserService?wsdl";
		String nameSpaceUri = "http://mydubbox.archetypes.xlongwei.com/";
		String serviceName = "UserService", portName = "UserServicePort";
		
		//这里两个QName都要带上nameSpaceUri否则报错
		Service service = Service.create(new URL(wsdlUrl), new QName(nameSpaceUri, serviceName));
		WsUserService userService = service.getPort(new QName(nameSpaceUri, portName), WsUserService.class);
		logger.info(JSON.toJSONString(userService.getUser(1L)));
		logger.info(JSON.toJSONString(userService.getUser(0L)));//数据校验失败，由ValidationExceptionFilter返回失败结果
		
		User user = new User();
		logger.info(JSON.toJSONString(userService.registerUser(user)));//数据校验失败，由ValidationExceptionFilter返回失败结果
		user.setName("testUser");
		logger.info(JSON.toJSONString(userService.registerUser(user)));
		user.setName("te");
		logger.info(JSON.toJSONString(userService.registerUser(user)));//数据校验失败，由ValidationExceptionFilter返回失败结果
	}
}
