package com.xlongwei.archetypes.mydubbox.util;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.protocol.rest.RpcExceptionMapper;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

public class ValidationExceptionMapper extends RpcExceptionMapper {
	public Response toResponse(RpcException e) {
		if (e.getCause() instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) e.getCause());
        }
		return Response.status(Response.Status.OK).entity(Result.newFailure(-1, e.getMessage())).type(ContentType.APPLICATION_JSON_UTF_8).build();
	}
	protected Response handleConstraintViolationException(ConstraintViolationException cve) {
    	StringBuilder err = new StringBuilder();
        for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
        	err.append(cv.getPropertyPath().toString());
        	err.append(cv.getMessage());
        }
        return Response.status(Response.Status.OK).entity(Result.newFailure(-1, err.toString())).type(ContentType.APPLICATION_JSON_UTF_8).build();
    }
}