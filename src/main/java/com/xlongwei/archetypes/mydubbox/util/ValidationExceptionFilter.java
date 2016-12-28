package com.xlongwei.archetypes.mydubbox.util;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.support.RpcUtils;
import com.alibaba.dubbo.validation.Validation;
import com.alibaba.dubbo.validation.Validator;
import com.alibaba.dubbo.validation.support.jvalidation.JValidation;

@Activate(group = { Constants.CONSUMER, Constants.PROVIDER }, value = Constants.VALIDATION_KEY, order = 10000)
public class ValidationExceptionFilter implements Filter {
    private Validation validation = new JValidation();
    private Logger logger = LoggerFactory.getLogger(getClass());

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (validation != null && ! invocation.getMethodName().startsWith("$")) {
            try {
                Validator validator = validation.getValidator(invoker.getUrl());
                if (validator != null) {
                    validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
                }
            } catch (ConstraintViolationException e) {
            	String string = ValidationExceptionMapper.toString(e);
            	logger.warn(string);
            	Class<?> returnType = RpcUtils.getReturnType(invocation), resultType = com.xlongwei.archetypes.mydubbox.util.Result.class;
            	Object returnValue = resultType==returnType ? com.xlongwei.archetypes.mydubbox.util.Result.newFailure(-1, string) : ReflectUtils.getEmptyObject(returnType);
                return new RpcResult(returnValue);
            } catch (RpcException e) {
            	throw e;
            } catch (Throwable t) {
                throw new RpcException(t.getMessage(), t);
            }
        }
        return invoker.invoke(invocation);
    }
}
