package com.svili.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.svili.model.vo.JsonModel;
import com.svili.util.LogUtil;

/**
 * SpringMVC异常处理
 * 
 * @author svili
 * @data 2017年4月20日
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	/** 内置异常类型 */
	@ExceptionHandler(value = ServiceException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JsonModel handleServiceException(ServiceException exception) {
		// 返回异常信息
		return JsonModel.erro(exception.getErrorCode(), exception.getErrorMessage());
	}

	/** 非法参数 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JsonModel handleArgumentException(IllegalArgumentException exception) {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		// 打印异常日志
		printLog(exception, request);

		// 返回异常信息
		return JsonModel.erro(ServiceErrorCode.ILLEGAL_ARGUMENT, exception.getMessage());
	}

	/** 身份认证 */
	@ExceptionHandler(value = AuthenticationException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JsonModel handleAuthenticationException(AuthenticationException exception) {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		// 打印异常日志
		printLog(exception, request);

		// 返回异常信息
		return JsonModel.erro(ServiceErrorCode.ILLEGAL_OPERATION, "authentication error." + exception.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JsonModel handleException(Exception exception) {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		// 打印异常日志
		printLog(exception, request);

		// 返回异常信息
		return JsonModel.erro(ServiceErrorCode.UNKNOWN, "System error.");
	}

	/**
	 * 打印异常日志 </br>
	 * 日志格式：{method} {uri} parameters={parameters}</br>
	 * e.g. GET /ykt_mobileapi/calendar/14111/encapsulation parameters={"time":[
	 * "2017-07-18 09:57:46"]}
	 */
	private void printLog(Exception exception, HttpServletRequest request) {
		String msg = "{method} {uri} parameters={parameters}";
		msg = msg.replace("{method}", request.getMethod());
		msg = msg.replace("{uri}", request.getRequestURI());
		Map<String, String[]> parameters = request.getParameterMap();
		msg = msg.replace("{parameters}", JSON.toJSONString(parameters));
		LogUtil.error(msg, exception);
	}

}