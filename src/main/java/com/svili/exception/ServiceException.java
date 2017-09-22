package com.svili.exception;

/**
 * 接口服务返回的错误消息.</br>
 * 该类的构造方法被设置为包访问,构建对象请使用{@link ServiceExceptionFactory}.
 * 
 * @author svili
 * @data 2017年7月18日
 *
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -7494861938541249085L;

	/** 错误代码 */
	private String errorCode;

	/** 错误信息 */
	private String errorMessage;

	ServiceException(String errorMessage) {
		this(null, errorMessage);
	}

	ServiceException(String errorCode, String errorMessage) {
		super((String) null);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	ServiceException(String errorMessage, Throwable cause) {
		this(null, errorMessage, cause);
	}

	ServiceException(String errorCode, String errorMessage, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	// getter
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
