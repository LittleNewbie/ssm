package com.svili.model.vo;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * SpringMVC ResponseBody for JSON Object.</br>
 * 数据接口必须使用此类作为返回类型.</br>
 * 
 * @attetion 该类的构造方法私有化,对外提供构建对象的方法如下:</br>
 * 
 * @attetion for success: {@link #success(Object)}</br>
 * @attetion for erro: {@link #erro(String,String)}</br>
 * 
 * @author svili
 * @data 2017年7月7日
 *
 */
@JSONType(orders = { "code", "message", "result" })
public class JsonModel {

	/** 响应代码 */
	private String code;

	/** 消息描述 */
	private String message;

	/** 数据对象 */
	private Object result;

	private JsonModel(String code, String message, Object result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

	/**
	 * 请求成功</br>
	 * 
	 * @param result
	 *            返回对象
	 * @return json format response with
	 *         {"code":"0","message":"success","result":}
	 */
	public static JsonModel success(Object result) {
		return new JsonModel("0", "success", result);
	}

	/**
	 * 异常响应</br>
	 * 
	 * @param code
	 *            异常代码
	 * @param message
	 *            异常信息
	 * @return json format response with {"code":"","message":""}
	 */
	public static JsonModel erro(String code, String message) {
		return new JsonModel(code, message, null);
	}

	/* getter and setter */

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
