package com.svili.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.svili.exception.CustomInnerException;

/**
 * HttpClient工具类</br>
 * HTTP GET/POST
 * 
 * @author svili
 * @data 2017年4月22日
 *
 */
public class HttpClientUtil {

	/**
	 * create HttpClient
	 * 
	 */
	private static CloseableHttpClient buildHttpClient() {
		return HttpClients.createDefault();
	}

	/**
	 * httpGet
	 * 
	 * @param uri
	 *            for example,http://www.baidu.com or
	 *            http://www.baidu.com?param_1=value...
	 * @return response
	 */
	public static String sendGet(String uri) {
		return sendGet(uri, null, null);
	}

	/**
	 * httpGet
	 */
	public static String sendGet(String url, Map<String, Object> params) {
		return sendGet(url, params, null);
	}

	/**
	 * 
	 * params.key:param name,if key is null or key equals(""),this key and value
	 * will not append to request uri</br>
	 * params.value:param value,if value is null, the value will not append to
	 * request uri;if value is not null,append to uri with value.toString()</br>
	 * header.value:if the value is null,it will be cast to String with
	 * "null".</br>
	 * 
	 */
	public static String sendGet(String url, Map<String, Object> params, Map<String, Object> headers) {
		String uri = url;
		if (params != null && !params.isEmpty()) {
			StringBuilder paramURL = new StringBuilder();
			for (Entry<String, Object> entry : params.entrySet()) {
				String name = entry.getKey();
				Object value = entry.getValue();
				if (name != null && !name.equals("")) {
					paramURL.append("&");
					paramURL.append(name).append("=");
					if (value != null) {
						try {
							paramURL.append(URLEncoder.encode(value.toString(), "UTF-8"));
						} catch (UnsupportedEncodingException e) {
							throw new CustomInnerException(e);
						}
					}
				}
			}
			uri = url + "?" + paramURL.substring(1);
		}

		HttpGet httpGet = new HttpGet(uri);

		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, Object> entry : headers.entrySet()) {
				httpGet.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}

		return doRequest(httpGet);
	}

	public static String sendPost(String uri) {
		return sendPost(uri, null, null);
	}

	public static String sendPost(String url, Map<String, Object> params) {
		return sendPost(url, params, null);
	}

	/**
	 * header.value:if the value is null,it will be cast to String with
	 * "null".</br>
	 */
	public static String sendPost(String url, Map<String, Object> params, Map<String, Object> headers) {
		HttpPost httpPost = new HttpPost(url);
		if (params != null && !params.isEmpty()) {
			// 创建参数列表
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.size());
			for (Entry<String, Object> entry : params.entrySet()) {
				String name = entry.getKey();
				Object value = entry.getValue();
				if (name != null && !name.equals("")) {
					if (value != null) {
						nameValuePairs.add(new BasicNameValuePair(name, value.toString()));
					} else {
						nameValuePairs.add(new BasicNameValuePair(name, null));
					}
				}
			}
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new CustomInnerException(e);
			}
		}

		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, Object> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}

		return doRequest(httpPost);
	}

	private static String doRequest(HttpUriRequest request) {
		CloseableHttpClient httpClient = buildHttpClient();
		CloseableHttpResponse response = null;

		try {
			response = httpClient.execute(request);
		} catch (IOException e) {
			throw new CustomInnerException(e);
		} finally {
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					new CustomInnerException(e);
				}
			}
		}
		// 状态码
		// int statusCode = response.getStatusLine().getStatusCode();
		String result = "";

		try {
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ParseException | IOException e) {
			throw new CustomInnerException(e);
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					new CustomInnerException(e);
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
