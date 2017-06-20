package com.svili.model.vo;

/**
 * JSON Response Factory
 * 
 * @author svili
 * @data 2017年4月25日
 *
 */
public class JsonResponseFactory {

	/**
	 * 正确响应</br>
	 * 
	 * @param result
	 *            返回对象
	 * @return json format response with
	 *         {"code":"0","message":"success","result":""}
	 */
	public static JsonResponse createSuccessResponse(Object result) {
		return new JsonResponse(result);
	}

	/**
	 * 异常响应</br>
	 * 
	 * @param code
	 *            异常代码
	 * @param message
	 *            异常信息
	 * @return json format response with {"code":"","message":"","result":""}
	 */
	public static JsonResponse createErroResponse(String code, String message) {
		return new JsonResponse(code, message);
	}

}
