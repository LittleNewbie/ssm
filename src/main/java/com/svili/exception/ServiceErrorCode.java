package com.svili.exception;

/***
 * 系统定义的错误代码.
 * 
 * @author svili
 * @data 2017年7月18日
 *
 */
public interface ServiceErrorCode {

	/** 未知错误 */
	String UNKNOWN = "1";

	/** 非法参数 */
	String ILLEGAL_ARGUMENT = "101";

	/** 数据缺失 */
	String LACK_OF_DATA = "201";

	/** 非法操作 */
	String ILLEGAL_OPERATION = "301";

	/** 未限定的异常 */
	String UNDEFINED = "999";

}
