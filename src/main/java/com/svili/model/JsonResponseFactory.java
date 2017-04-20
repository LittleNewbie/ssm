package com.svili.model;

public class JsonResponseFactory {
	
	public static JsonResponse createSuccessResponse(Object result){
		return new JsonResponse(result);
	}
	
	public static JsonResponse createErroResponse(String code,String message){
		return new JsonResponse(code,message);
	}

}
