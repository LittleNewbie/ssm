package com.svili.exception;

/**
 * 自定义应用异常(applicationException)
 * 
 * @author svili
 * @data 2017年4月20日
 *
 */
public class CustomAppException extends RuntimeException {

	private static final long serialVersionUID = 3276319868132039839L;
	
	private static final String DEFAULT_CODE = "0001";
	private static final String DEFAULT_MSG = "system erro";

	/** 异常代码 */
	private String customCode;

	/** 自定义日志信息 */
	private String customMsg;

	public CustomAppException() {
		this(DEFAULT_CODE, DEFAULT_MSG);
	}

	public CustomAppException(Throwable cause) {
		this(DEFAULT_CODE, DEFAULT_MSG,cause);
	}

	public CustomAppException(String customCode, String customMsg) {
		super(customMsg);
		this.customCode = customCode;
		this.customMsg = customMsg;
	}
	
	public CustomAppException(String customCode, String customMsg,Throwable cause) {
		super(customMsg,cause);
		this.customCode = customCode;
		this.customMsg = customMsg;
	}

	public String getCustomCode() {
		return customCode;
	}

	public void setCustomCode(String customCode) {
		this.customCode = customCode;
	}

	public String getCustomMsg() {
		return customMsg;
	}

	public void setCustomMsg(String customMsg) {
		this.customMsg = customMsg;
	}

}
