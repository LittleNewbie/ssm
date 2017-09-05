package com.svili.util.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/***
 * httpClient工具类.</br>
 * 执行器
 * 
 * @author lishiwei
 * @data 2017年8月22日
 *
 */
public class HttpClientExecutor {

	/***
	 * HttpClient 执行入口
	 */
	public static String execute(CloseableHttpClient httpClient, HttpUriRequest request) {
		CloseableHttpResponse response = null;
		// 状态码
		// int statusCode = response.getStatusLine().getStatusCode();
		String result = "";

		try {
			response = httpClient.execute(request);
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return result;
	}

	/**
	 * 解析Http Response响应头
	 * 
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Map<String, String> parseHeader(HttpResponse response) {
		Header[] headers = response.getAllHeaders();

		if (headers != null) {
			Map<String, String> map = new HashMap<String, String>(headers.length);

			for (Header header : headers) {
				map.put(header.getName(), header.getValue());
			}

			// 不包括Cookie
			map.remove("Set-Cookie");

			return map;
		}

		return null;
	}

	/**
	 * 解析Http Response响应Cookie
	 * 
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Map<String, String> parseCookie(HttpResponse response) {
		Header[] cookies = response.getHeaders("Set-Cookie");

		if (cookies != null) {
			Map<String, String> map = new HashMap<String, String>(cookies.length);

			for (Header header : cookies) {
				String cookie = header.getValue().substring(0, header.getValue().indexOf(";"));
				String[] kv = cookie.split("=");

				if (kv.length == 2) {
					map.put(kv[0].trim(), kv[1].trim());
				} else {
					map.put(kv[0].trim(), "");
				}
			}

			return map;
		}

		return null;
	}

}
