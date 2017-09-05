package com.svili.util.http;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/***
 * HttpClient 工具类.</br>
 * build HttpClient.</br>
 * 
 * @author lishiwei
 * @data 2017年8月7日
 *
 */
public class HttpClientFactory {

	/** create HttpClient */
	public static CloseableHttpClient create(boolean ssl) {
		return ssl ? http() : https();
	}

	/**
	 * create HttpClient</br>
	 * protocol : http
	 * 
	 */
	public static CloseableHttpClient http() {
		return HttpClients.createDefault();
	}

	/**
	 * create HttpClient</br>
	 * protocol : https</br>
	 * SSL 绕过验证
	 * 
	 */
	public static CloseableHttpClient https() {

		X509TrustManager trustManager = DefaultTrustManager.getInstance();

		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSLv3");
			sslContext.init(null, new TrustManager[] { trustManager }, null);
		} catch (NoSuchAlgorithmException e) {
			// getInstance error.
			throw new RuntimeException(e);
		} catch (KeyManagementException e) {
			// init error.
			throw new RuntimeException(e);
		}

		// 设置协议http和https对应的处理socket链接工厂的对象
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", new SSLConnectionSocketFactory(sslContext)).build();
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		// 创建自定义的http client对象
		return HttpClients.custom().setConnectionManager(connManager).build();
	}

}
