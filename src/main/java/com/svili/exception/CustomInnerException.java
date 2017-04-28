package com.svili.exception;

/**
 * 自定义系统内部异常
 * 
 * @author svili
 * @data 2017年4月25日
 *
 */
public class CustomInnerException extends java.lang.RuntimeException {

	private static final long serialVersionUID = 9030107012846801196L;

	public CustomInnerException(String message) {
		super(message);
	}

	public CustomInnerException(Throwable cause) {
		super(cause);
	}

	public CustomInnerException(String message, Throwable cause) {
		super(message, cause);
	}

}
