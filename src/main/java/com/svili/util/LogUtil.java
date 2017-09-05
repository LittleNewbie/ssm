package com.svili.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类，封装了slf4j的常见用法
 * 
 * @author svili
 * @data 2017年4月20日
 *
 */
public final class LogUtil {
	private final static Logger logger = LoggerFactory.getLogger(LogUtil.class);

	public static void info(String msg) {
		if (logger.isInfoEnabled()) {
			logger.info(msg);
		}
	}

	public static void info(String msg, Throwable t) {
		if (logger.isInfoEnabled()) {
			logger.info(msg, t);
		}
	}

	public static void info(String format, Object... arguments) {
		logger.info(format, arguments);
	}

	public static void debug(String msg) {
		if (logger.isDebugEnabled()) {
			logger.debug(msg);
		}
	}

	public static void debug(String msg, Throwable t) {
		if (logger.isDebugEnabled()) {
			logger.debug(msg, t);
		}
	}

	public static void debug(String format, Object... arguments) {
		logger.debug(format, arguments);
	}

	public static void warn(String msg) {
		if (logger.isWarnEnabled()) {
			logger.warn(msg);
		}
	}

	public static void warn(String msg, Throwable t) {
		if (logger.isWarnEnabled()) {
			logger.warn(msg, t);
		}
	}

	public static void warn(String format, Object... arguments) {
		logger.warn(format, arguments);
	}

	public static void error(String msg) {
		if (logger.isErrorEnabled()) {
			logger.error(msg);
		}
	}

	public static void error(String msg, Throwable t) {
		if (logger.isErrorEnabled()) {
			logger.error(msg, t);
		}
	}

	public static void error(String format, Object... arguments) {
		logger.error(format, arguments);
	}

	public static void trace(String msg) {
		if (logger.isTraceEnabled()) {
			logger.trace(msg);
		}
	}

	public static void trace(String msg, Throwable t) {
		if (logger.isTraceEnabled()) {
			logger.trace(msg, t);
		}
	}

	public static void trace(String format, Object... arguments) {
		logger.trace(format, arguments);
	}
}
