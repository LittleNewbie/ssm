package com.svili.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.svili.common.LogUtil;
import com.svili.model.JsonResponse;
import com.svili.model.JsonResponseFactory;

/**
 * SpringMVC异常处理
 * 
 * @author svili
 * @data 2017年4月20日
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JsonResponse handleException(Exception exception, HttpServletRequest request) {

		// 打印异常日志
		printLog(exception, request);

		// 返回异常响应
		return JsonResponseFactory.createErroResponse("0001", "system erro");
	}

	/**
	 * 打印异常日志 </br>
	 * 日志格式：{method} {uri}
	 */
	private void printLog(Exception exception, HttpServletRequest request) {
		String format = "{} {}";

		List<Object> arguments = new ArrayList<Object>();
		arguments.add(request.getMethod());
		arguments.add(request.getRequestURI());

		// 打印异常堆栈
		if (exception.getCause() != null) {
			arguments.add(exception.getCause());
		} else {
			arguments.add(exception);
		}

		LogUtil.error(format, arguments.toArray());
	}
}
