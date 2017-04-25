package com.svili.model;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * SpringMVC response for JSON
 * 
 * @author svili
 * @data 2017年4月20日
 *
 */
@JSONType(orders = {"code", "message", "result"})
public class JsonResponse {
	/** 响应代码 */
	private String code;
	
	/** 消息描述 */
	private String message;
	
	/** 数据对象 */
	private Object result;
	
	public JsonResponse(Object result) {
		this("0", "success", result);
	}
	
	public JsonResponse(String code, String message) {
		this(code, message, "");
	}
	
	private JsonResponse(String code, String message, Object result) {
		this.code = code;
		this.message  = message;
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	
}
