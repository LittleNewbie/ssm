package com.svili.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/***
 * SpringMVC jsonp 响应体处理.</br>
 * 基于alibaba fastJson.
 * 
 * @author svili
 * @data 2017年7月19日
 *
 */
@ControllerAdvice
public class JsonpAdvice implements ResponseBodyAdvice<Object> {

	// jsonp请求回调函数名
	private final String[] jsonpQueryParamNames = { "callback", "jsonp" };

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return FastJsonHttpMessageConverter.class.isAssignableFrom(converterType);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		for (String name : this.jsonpQueryParamNames) {
			String value = servletRequest.getParameter(name);
			if (value != null) {
				MediaType contentTypeToUse = new MediaType("application", "javascript");
				response.getHeaders().setContentType(contentTypeToUse);
				JSONPObject container = getOrCreateContainer(body);
				container.setFunction(value);
				return container;
			}
		}
		return body;
	}

	/**
	 * Wrap the body in a {@link JSONPObject} value container (for providing
	 * additional serialization instructions) or simply cast it if already
	 * wrapped.
	 */
	protected JSONPObject getOrCreateContainer(Object body) {
		if (body instanceof JSONPObject) {
			return (JSONPObject) body;
		}
		JSONPObject container = new JSONPObject();
		container.addParameter(body);
		return container;
	}

}
