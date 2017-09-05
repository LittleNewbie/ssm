package com.svili.util.http;

import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

/***
 * HttpClient 工具类.</br>
 * method:GET,POST,PUT,PATCH.
 * 
 * @author lishiwei
 * @data 2017年8月22日
 *
 */
public class HttpClientUtils {

	public static class Get {
		public static String get(String uri) {
			return get(uri, null);
		}

		public static String get(String uri, Map<String, String> headers) {
			HttpUriRequest request = HttpClientRequestBuilder.get(uri, headers);

			CloseableHttpClient httpClient = HttpClientFactory.create(isSSL(uri));

			return HttpClientExecutor.execute(httpClient, request);
		}
	}

	public static class Post {
		public static String post(String uri) {
			return HttpClientUtils.post("POST", uri, null);
		}

		public static String post(String uri, Map<String, String> headers) {
			return HttpClientUtils.post("POST", uri, null);
		}

		public static String postForm(String uri, Map<String, String> formParams) {
			return HttpClientUtils.postForm("POST", uri, formParams, null);
		}

		public static String postForm(String uri, Map<String, String> formParams, Map<String, String> headers) {
			return HttpClientUtils.postForm("POST", uri, formParams, headers);
		}

		public static String postBody(String uri, String body) {
			return HttpClientUtils.postBody("POST", uri, body, null);
		}

		public static String postBody(String uri, String body, Map<String, String> headers) {
			return HttpClientUtils.postBody("POST", uri, body, headers);
		}
	}

	public static class Put {
		public static String put(String uri) {
			return post("PUT", uri, null);
		}

		public static String put(String uri, Map<String, String> headers) {
			return post("PUT", uri, headers);
		}

		public static String putForm(String uri, Map<String, String> formParams) {
			return postForm("PUT", uri, formParams, null);
		}

		public static String putForm(String uri, Map<String, String> formParams, Map<String, String> headers) {
			return postForm("PUT", uri, formParams, headers);
		}

		public static String putBody(String uri, String body) {
			return postBody("PUT", uri, body, null);
		}

		public static String putBody(String uri, String body, Map<String, String> headers) {
			return postBody("PUT", uri, body, headers);
		}
	}

	public static class Patch {
		public static String patch(String uri) {
			return post("PATCH", uri, null);
		}

		public static String patch(String uri, Map<String, String> headers) {
			return post("PATCH", uri, headers);
		}

		public static String patchForm(String uri, Map<String, String> formParams) {
			return postForm("PATCH", uri, formParams, null);
		}

		public static String patchForm(String uri, Map<String, String> formParams, Map<String, String> headers) {
			return postForm("PATCH", uri, formParams, headers);
		}

		public static String patchBody(String uri, String body) {
			return postBody("PATCH", uri, body, null);
		}

		public static String patchBody(String uri, String body, Map<String, String> headers) {
			return postBody("PATCH", uri, body, headers);
		}
	}

	/**
	 * post
	 * 
	 * @param methodName
	 *            restful:"POST","PUT","PATCH"
	 */
	public static String post(String methodName, String uri, Map<String, String> headers) {
		HttpUriRequest request = HttpClientRequestBuilder.post(methodName, uri, headers);

		CloseableHttpClient httpClient = HttpClientFactory.create(isSSL(uri));

		return HttpClientExecutor.execute(httpClient, request);
	}

	/**
	 * post form.</br>
	 * 
	 * @param methodName
	 *            restful:"POST","PUT","PATCH"
	 * 
	 */
	private static String postForm(String methodName, String uri, Map<String, String> formParams,
			Map<String, String> headers) {
		HttpUriRequest request = HttpClientRequestBuilder.postForm(methodName, uri, formParams, headers);

		CloseableHttpClient httpClient = HttpClientFactory.create(isSSL(uri));

		return HttpClientExecutor.execute(httpClient, request);
	}

	/***
	 * post body
	 * 
	 * @param methodName
	 *            restful:"POST","PUT","PATCH"
	 */
	public static String postBody(String methodName, String uri, String body, Map<String, String> headers) {
		HttpUriRequest request = HttpClientRequestBuilder.postBody(methodName, uri, body, headers);

		CloseableHttpClient httpClient = HttpClientFactory.create(isSSL(uri));

		return HttpClientExecutor.execute(httpClient, request);
	}

	private static boolean isSSL(String uri) {
		return uri.startsWith("https://");
	}

}
