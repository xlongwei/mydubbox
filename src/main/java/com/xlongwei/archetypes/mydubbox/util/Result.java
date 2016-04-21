package com.xlongwei.archetypes.mydubbox.util;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@SuppressWarnings("serial")
@JsonSerialize(include=Inclusion.NON_NULL)
public class Result<T> implements Serializable {
	public static <T> Result<T> newSuccess(T data) {
		Result<T> result = new Result<>();
		result.setData(data);
		return result;
	}
	public static <T> Result<T> newFailure(int code, String err){
		Result<T> result = new Result<>();
		result.setCode(code!=0 ? code : -1);
		result.setErr(err);
		return result;
	}
	public static <T> Result<T> newFailure(Result<?> failure){
		Result<T> result = new Result<>();
		result.setCode(failure.getCode()!=0 ? failure.getCode() : -1);
		result.setErr(failure.getErr());
		return result;
	}
	public boolean success() { return code == 0; }
	public boolean hasData() { return code==0 && data!=null; }	
	
	private int code;
	private T data;
	private String err;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
}
