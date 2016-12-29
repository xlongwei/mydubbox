package com.xlongwei.archetypes.mydubbox.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.alibaba.fastjson.JSON;
import com.xlongwei.archetypes.mydubbox.UserService;
import com.xlongwei.archetypes.mydubbox.model.User;
import com.xlongwei.archetypes.mydubbox.util.Result;

@Path("user")
@Service(filter={"validationException"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(ContentType.APPLICATION_JSON_UTF_8)
public class UserServiceImpl implements UserService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @GET
    @Path("{id : \\d+}")
	public User getUser(@PathParam("id") Long id) {
    	logger.info("get user, id: "+id);
    	User user = new User();
    	user.setId(id);
		return user;
	}

    @PUT
    @Path("{id : \\d+}")
    public Result<Boolean> updateUser(@PathParam("id") Long id, String status) {
    	logger.info("update user, id: "+id+", status: "+status);
    	return Result.newSuccess(true);
    }
    
    @DELETE
    @Path("{id : \\d+}")
    public boolean deleteUser(@PathParam("id") Long id) {
    	logger.info("delete user, id: "+id);
    	return true;
    }
    
    @POST
    @Path("register")
	public Long registerUser(User user) {
    	logger.info("register user: "+JSON.toJSONString(user));
		return 1L;
	}
}
