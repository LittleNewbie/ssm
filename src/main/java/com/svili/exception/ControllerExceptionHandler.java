package com.svili.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;
import com.svili.common.LogUtil;
import com.svili.model.vo.JsonModel;

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
	public JsonModel handleException(Exception exception, HttpServletRequest request) {

		// 打印异常日志
		printLog(exception, request);

		// 返回异常信息
		return JsonModel.erro("0001", "system erro");
	}

	/**
	 * 打印异常日志 </br>
	 * 日志格式：{method} {uri} parameters={parameters}</br>
	 * e.g. GET /ykt_mobileapi/index args =
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
