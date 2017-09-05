package com.svili.util;

import java.util.regex.Pattern;

/***
 * 正则工具类
 *
 * @author lishiwei
 * @date 2017年9月1日
 *
 */
public class RegexUtil {

	/** 手机号码正则 */
	public static final Pattern MOBILE_PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

	/** 邮箱正则 */
	public static final Pattern EMAIL_PATTERN = Pattern
			.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");

	public static boolean isMobilePhone(CharSequence input) {
		return MOBILE_PHONE_PATTERN.matcher(input).matches();
	}

	public static boolean isEmail(CharSequence input) {
		return EMAIL_PATTERN.matcher(input).matches();
	}

}
