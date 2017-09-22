package com.svili.util.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

/***
 * HttpClient 工具类.</br>
 * build <code>HttpUriRequest</code>.
 * 
 * @author svili
 * @data 2017年8月7日
 *
 */
public class HttpClientRequestBuilder {

	/**
	 * httpGet</br>
	 * 
	 * if the value is null,it will be cast to String with "null".
	 * 
	 * @param uri
	 *            e.g.http://www.baidu.com or
	 *            http://www.baidu.com?param_1=value... </br>
	 * 
	 */
	public static HttpUriRequest get(String uri, Map<String, String> headers) {

		HttpGet httpGet = new HttpGet(uri);

		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), entry.getValue());
			}
		}

		return httpGet;
	}

	/**
	 * post
	 * 
	 * @param methodName
	 *            restful:"POST","PUT","PATCH"
	 * @param uri
	 *            e.g.https://www.baidu.com or
	 *            https://www.baidu.com?param_1=value... </br>
	 */
	public static HttpEntityEnclosingRequestBase post(String methodName, String uri, Map<String, String> headers) {
		HttpEntityEnclosingRequestBase request = null;
		if (StringUtils.isBlank(methodName)) {
			// 默认post
			methodName = "POST";
		}
		switch (methodName) {
		case "PATCH":
			request = new HttpPatch(uri);
			break;
		case "PUT":
			request = new HttpPut(uri);
			break;
		case "POST":
			request = new HttpPost(uri);
			break;
		default:
			request = new HttpPost(uri);
			break;
		}

		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return request;
	}

	/**
	 * post form.
	 */
	public static HttpUriRequest postForm(String methodName, String uri, Map<String, String> formParams,
			Map<String, String> headers) {
		HttpEntityEnclosingRequestBase request = post(methodName, uri, headers);

		if (formParams != null && !formParams.isEmpty()) {
			// 创建参数列表
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(formParams.size());
			for (Entry<String, String> entry : formParams.entrySet()) {
				String name = entry.getKey();
				String value = entry.getValue();
				if (!StringUtils.isBlank(name)) {
					if (!StringUtils.isBlank(value)) {
						nameValuePairs.add(new BasicNameValuePair(name, value));
					} else {
						nameValuePairs.add(new BasicNameValuePair(name, ""));
					}
				}
			}
			try {
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		return request;
	}

	/***
	 * post body
	 */
	public static HttpUriRequest postBody(String methodName, String uri, String body, Map<String, String> headers) {
		HttpEntityEnclosingRequestBase request = post(methodName, uri, headers);

		if (!StringUtils.isEmpty(body)) {
			request.setEntity(new StringEntity(body, "UTF-8"));
		}

		// default application/json
		if (!request.containsHeader("Content-Type")) {
			request.addHeader("Content-Type", "application/json");
		}

		return request;
	}

}
