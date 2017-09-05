package com.svili.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化工具类
 * 
 * @author lishiwei
 * @data 2017年5月26日
 *
 */
public class DateUtil {

	public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DateFormat SIMPLE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final DateFormat NO_SECOND_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static final DateFormat HOUR_AND_MINUTE_FORMAT = new SimpleDateFormat(
			"HH:mm");

	/* 普通格式化：yyyy-MM-dd */
	public static String formatSimple() {
		return formatSimple(System.currentTimeMillis());
	}

	/* 普通格式化：yyyy-MM-dd */
	public static String formatSimple(Date date) {
		return formatSimple(date.getTime());
	}

	/* 普通格式化：yyyy-MM-dd */
	public static String formatSimple(long time) {
		return SIMPLE_FORMAT.format(time);
	}

	/* 默认格式化：yyyy-MM-dd HH:mm:ss */
	public static String format() {
		return format(System.currentTimeMillis());
	}

	/* 默认格式化：yyyy-MM-dd HH:mm:ss */
	public static String format(Date date) {
		return format(date.getTime());
	}

	/* 默认格式化：yyyy-MM-dd HH:mm:ss */
	public static String format(long time) {
		return DEFAULT_FORMAT.format(time);
	}

	/* 自定义格式化：pattern */
	public static String format(String pattern) {
		return format(System.currentTimeMillis(), pattern);
	}

	/* 自定义格式化：pattern */
	public static String format(Date date, String pattern) {
		return format(date.getTime(), pattern);
	}

	/* 自定义格式化：pattern */
	public static String format(long time, String pattern) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(time);
	}

	/* 获取日期 */
	public static int getDay() {
		return getDay(System.currentTimeMillis());
	}

	/* 获取日期 */
	public static int getDay(Date date) {
		return getDay(date.getTime());
	}

	/* 获取日期 */
	public static int getDay(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.DATE);
	}

	/* 月份 */
	public static int getMonth() {
		return getMonth(System.currentTimeMillis());
	}

	public static int getMonth(Date date) {
		return getMonth(date.getTime());
	}

	public static int getMonth(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.MONTH) + 1;
	}

	/* 年份 */
	public static int getYear() {
		return getYear(System.currentTimeMillis());
	}

	public static int getYear(Date date) {
		return getYear(date.getTime());
	}

	public static int getYear(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.get(Calendar.YEAR);
	}

	/* 月份第一天 */
	public static String getFirstDay(Date month) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(month);
		// 获取当月的第一天
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		return SIMPLE_FORMAT.format(cale.getTime());
	}

	/* 月份最后一天 */
	public static String getLastDay(Date month) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(month);
		// 获取当月的最后一天 即,下个月月初的前一天
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		return SIMPLE_FORMAT.format(cale.getTime());
	}

	/* 星期 */
	public static int getWeek(Date day) {
		Calendar cale = Calendar.getInstance();
		cale.setTime(day);
		// 在美国 星期日是一个星期的第一天
		if (cale.get(Calendar.DAY_OF_WEEK) == 1) {
			return 7;
		}
		return cale.get(Calendar.DAY_OF_WEEK) - 1;
	}

	public static int getWeekDay() {
		Calendar calendar = Calendar.getInstance();
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		weekday = weekday == 1 ? 7 : weekday - 1;
		return weekday;
	}

}