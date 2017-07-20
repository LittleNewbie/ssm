package com.svili.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数学计算工具
 * 
 * @author lishiwei
 * @data 2017年6月14日
 *
 */
public class MathUtil {

	/***
	 * 除法
	 * 
	 * @param numerator
	 *            除数
	 * @param denominator
	 *            被除数
	 * @param fractionLength
	 *            小数位长度
	 * @exception {@see
	 *                ArithmeticException}</br>
	 *                when numerator = null or denominator = null or
	 *                denominator=0.
	 */
	public static double division(Number numerator, Number denominator, int fractionLength) {
		if (numerator == null) {
			throw new java.lang.ArithmeticException("numerator can not allowed null");
		}
		if (denominator == null) {
			throw new java.lang.ArithmeticException("denominator can not allowed null");
		}

		// 设置小数位长度
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(fractionLength);
		if (numerator.doubleValue() == 0D) {
			return 0D;
		}
		if (denominator.equals(0)) {
			throw new java.lang.ArithmeticException("denominator can not allowed by 0");
		}

		return Double.parseDouble(numberFormat.format(numerator.doubleValue() / denominator.doubleValue()));
	}

	/**
	 * 数值格式化
	 * 
	 * @param fractionLength
	 *            小数位长度
	 */
	public static double format(Number number, int fractionLength) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(fractionLength);
		return Double.parseDouble(numberFormat.format(number));
	}

	/** 将数值转化为百分数,并加以百分号"%" */
	public static String toPercentage(Number number, int fractionLength) {
		StringBuilder pattern = new StringBuilder("###.");
		for (int i = 0; i < fractionLength; ++i) {
			pattern.append("#");
		}
		pattern.append("%");
		DecimalFormat numberFormat = new DecimalFormat(pattern.toString());
		return numberFormat.format(number);
	}

}
