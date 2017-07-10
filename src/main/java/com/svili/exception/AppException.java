package com.svili.exception;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 4523986064657839228L;

	private static final String DEFAULT_CODE = "0001";
	private static final String DEFAULT_MESSAGE = "System erro";

	/** 错误码 */
	private String code;

	public AppException() {
		super(DEFAULT_MESSAGE);
		this.code = DEFAULT_CODE;
	}

	public AppException(Throwable cause) {
		this(DEFAULT_CODE, DEFAULT_MESSAGE, cause);
	}

	public AppException(String code, String message) {
		super(message);
		this.code = code;
	}

	public AppException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	// getter

	public String getCode() {
		return code;
	}

}
