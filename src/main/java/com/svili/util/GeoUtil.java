package com.svili.util;

/***
 * 地图工具
 * 
 * @author chenjiayu
 * @data 2017年7月13日
 *
 */
public class GeoUtil {

	/** 地球半径 ,单位:km */
	private static final double EARTH_RADIUS = 6378.137D;

	/**
	 * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多,相差范围在0.2米以下</br>
	 * 
	 * @param lng1
	 *            经度1,e.g. ddd.dddddd
	 * @param lat1
	 *            纬度1,e.g. dd.dddddd
	 * @param lng2
	 *            经度2
	 * @param lat2
	 *            纬度2
	 * @return 单位m
	 */
	public static double distance(double lng1, double lat1, double lng2, double lat2) {
		/** 角度转为弧度 */
		double radLat1 = Math.toRadians(lat1);
		double radLat2 = Math.toRadians(lat2);
		// 纬度差
		double a = radLat1 - radLat2;
		// 经度差
		double b = Math.toRadians(lng1) - Math.toRadians(lng2);
		// 计算公式
		double distance = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));

		distance = distance * EARTH_RADIUS * 1000;
		return distance;
	}

}
