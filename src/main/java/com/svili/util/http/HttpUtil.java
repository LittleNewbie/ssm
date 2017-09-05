package com.svili.util.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;

/***
 * HTPP工具类</br>
 * 基于{@link java.net.HttpURLConnection}
 * 
 * @author lishiwei
 * @data 2017年7月26日
 *
 */
public class HttpUtil {

	private static final String CRLF_STR = "\r\n";

	private static final String BOUNDARY_STR = "--------yktUploadBoundary------";

	public static String toBinary(String fileName, InputStream file) {
		if (StringUtils.isBlank(fileName)) {
			// 文件名为空,默认填充uuid
			fileName = UUID.randomUUID().toString();
		}

		Map<String, InputStream> files = new HashMap<>();
		files.put(fileName, file);
		return toBinary(null, files);
	}

	public static String toBinary(Map<String, InputStream> files) {
		return toBinary(null, files);
	}

	/**
	 * 将post表单参数转化为http binary</br>
	 * 
	 * @param textPairs
	 *            字符参数 Key-Value Pair
	 * @param files
	 *            文件参数 filename,file
	 * @return binary
	 * @throws IOException
	 *             {@link BufferedReader#readLine()}
	 */
	public static String toBinary(Map<String, String> textPairs, Map<String, InputStream> files) {
		StringBuilder binary = new StringBuilder();
		if (textPairs != null && !textPairs.isEmpty()) {
			for (Entry<String, String> entry : textPairs.entrySet()) {
				binary.append(CRLF_STR).append("--").append(BOUNDARY_STR).append(CRLF_STR);
				binary.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"");
				binary.append(CRLF_STR).append(CRLF_STR);
				binary.append(entry.getValue());
			}
		}

		if (files != null && !files.isEmpty()) {
			for (Entry<String, InputStream> entry : files.entrySet()) {
				// fileName
				binary.append(CRLF_STR).append("--").append(BOUNDARY_STR).append(CRLF_STR);
				binary.append("Content-Disposition: form-data; name=\"file\";filename=\"").append(entry.getKey())
						.append("\"");
				binary.append(CRLF_STR).append("Content-Type: application/octet-stream");
				binary.append(CRLF_STR).append(CRLF_STR);
				// file
				Charset charset = Charset.forName("utf-8");
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entry.getValue(), charset));
				// 读行
				String line = null;
				try {
					while ((line = bufferedReader.readLine()) != null) {
						binary.append(line);
					}
				} catch (IOException e) {
					throw new RuntimeException("read InputStream erro.", e);
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						throw new RuntimeException("close InputStream erro.", e);
					}
				}
			}
		}

		// end binary
		binary.append(("\r\n--" + BOUNDARY_STR + "--\r\n"));

		return binary.toString();
	}

	/***
	 * post 文件上传
	 * 
	 * @param uri
	 *            请求地址
	 * @param headers
	 *            字符参数 Key-Value Pair
	 * @param binary
	 *            文件参数
	 * @return response.content
	 */
	public static String postBinary(String uri, String binary, Map<String, String> headers) {

		// 创建连接对象
		HttpURLConnection conn = ConnectionBuilder.openConnection(uri);
		try {
			conn.setRequestMethod("POST");
		} catch (ProtocolException e) {
			throw new RuntimeException(e);
		}

		// post请求(比如：文件上传)需要往服务区传输大量的数据，这些数据是放在http的body里面的，因此需要在建立连接以后，往服务端写数据。
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);

		// 设置请求头 header
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY_STR);
		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> header : headers.entrySet()) {
				conn.setRequestProperty(header.getKey(), header.getValue());
			}
		}

		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);

		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			bufferedWriter.write(binary);
			bufferedWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException("write body erro.", e);
		} finally {
			if (bufferedWriter != null) {
				try {
					// 关闭流
					bufferedWriter.close();
				} catch (IOException e) {
					throw new RuntimeException("close OutputStream erro.", e);
				}
			}
		}

		// 接收返回值
		StringBuilder response = new StringBuilder();

		// 读取返回的输入流 并设置字符编码
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}
		} catch (IOException e) {
			throw new RuntimeException("read response erro.", e);
		} finally {
			if (bufferedReader != null) {
				try {
					// 关闭流
					bufferedReader.close();
				} catch (IOException e) {
					throw new RuntimeException("close InputStream erro.", e);
				}

			}
		}
		return response.toString();
	}

	private static class ConnectionBuilder {

		public static HttpURLConnection openConnection(String uri) {
			if (uri.startsWith("https")) {
				return https(uri);
			}
			return http(uri);
		}

		public static HttpURLConnection http(String uri) {
			// 创建连接对象
			HttpURLConnection conn = null;
			try {
				// 创建请求对象URL
				URL httpUrl = new URL(uri);
				// 建立通信链接
				conn = (HttpURLConnection) httpUrl.openConnection();
			} catch (IOException e) {
				throw new RuntimeException("create connection erro.", e);
			}
			return conn;
		}

		public static HttpURLConnection https(String uri) {
			// 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
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
			SSLSocketFactory sslFactory = sslContext.getSocketFactory();
			// 创建连接对象
			HttpsURLConnection conn = null;
			try {
				// 创建请求对象URL
				URL httpUrl = new URL(uri);
				// 建立通信链接
				conn = (HttpsURLConnection) httpUrl.openConnection();
			} catch (IOException e) {
				throw new RuntimeException("create connection erro.", e);
			}
			conn.setSSLSocketFactory(sslFactory);
			return conn;
		}
	}

}
