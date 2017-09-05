package com.svili.exception;

/**
 * factory.
 * 
 * @author lishiwei
 * @data 2017年7月19日
 *
 */
public class ServiceExceptionFactory {

	public static ServiceException undefined(String errorMessage) {
		return newInstance(ServiceErrorCode.UNDEFINED, errorMessage);
	}

	public static ServiceException undefined(String errorMessage, Throwable cause) {
		return newInstance(ServiceErrorCode.UNDEFINED, errorMessage, cause);
	}

	public static ServiceException lackOfData(String errorMessage) {
		return newInstance(ServiceErrorCode.LACK_OF_DATA, errorMessage);
	}

	public static ServiceException lackOfData(String errorMessage, Throwable cause) {
		return newInstance(ServiceErrorCode.LACK_OF_DATA, errorMessage, cause);
	}

	public static ServiceException illegalOperation(String errorMessage) {
		return newInstance(ServiceErrorCode.ILLEGAL_OPERATION, errorMessage);
	}

	public static ServiceException illegalOperation(String errorMessage, Throwable cause) {
		return newInstance(ServiceErrorCode.ILLEGAL_OPERATION, errorMessage, cause);
	}

	private static ServiceException newInstance(String errorCode, String errorMessage) {
		return new ServiceException(errorCode, errorMessage);
	}

	private static ServiceException newInstance(String errorCode, String errorMessage, Throwable cause) {
		return new ServiceException(errorCode, errorMessage, cause);
	}

}
