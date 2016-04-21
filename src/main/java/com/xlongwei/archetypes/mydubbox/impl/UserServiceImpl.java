package com.xlongwei.archetypes.mydubbox.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.xlongwei.archetypes.mydubbox.UserService;
import com.xlongwei.archetypes.mydubbox.model.User;
import com.xlongwei.archetypes.mydubbox.util.Result;

@Path("user")
@Service(validation="true")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public class UserServiceImpl implements UserService {
    @GET
    @Path("{id : \\d+}")
	public Result<User> getUser(@PathParam("id") Long id) {
    	User user = new User();
    	user.setId(id);
    	user.setName("test:"+id);
		return Result.newSuccess(user);
	}
    
    @POST
    @Path("register")
	public Result<Boolean> registerUser(User user) {
		return Result.newSuccess(true);
	}
}
