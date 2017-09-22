package com.svili.util.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;

/***
 * Http文件上传工具类
 *
 * @author svili
 * @date 2017年9月22日
 *
 */
public class HttpBinaryUtil {

	private static final String CRLF_STR = "\r\n";

	private static final String BOUNDARY_STR = "----yktUploadBoundary";

	public static class BinaryBuilder {

		private ByteArrayOutputStream binary;

		public BinaryBuilder() {
			binary = new ByteArrayOutputStream();
		}

		public BinaryBuilder addTextPair(String name, String value) {
			String str = "--" + BOUNDARY_STR + CRLF_STR;
			str = str + "Content-Disposition: form-data; name=\"" + name + "\"" + CRLF_STR + CRLF_STR;
			str = str + value + CRLF_STR;
			try {
				binary.write(str.getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return this;
		}

		public BinaryBuilder addTextPairs(Map<String, String> textPairs) {
			if (textPairs == null || textPairs.isEmpty()) {
				return this;
			}
			for (Entry<String, String> entry : textPairs.entrySet()) {
				addTextPair(entry.getKey(), entry.getValue());
			}
			return this;
		}

		public BinaryBuilder addFile(String name, String fileName, InputStream input) {
			if (StringUtils.isBlank(name)) {
				// 表单项为空,默认填充file
				name = "file";
			}
			if (StringUtils.isBlank(fileName)) {
				// 文件名为空,默认填充uuid
				fileName = UUID.randomUUID().toString();
			}
			String str = "--" + BOUNDARY_STR + CRLF_STR;
			str = str + "Content-Disposition: form-data; name=\"" + name + "\" ;filename=\"" + fileName + "\""
					+ CRLF_STR;
			str = str + resolveFileType(fileName) + CRLF_STR + CRLF_STR;
			try {
				binary.write(str.getBytes(Charset.forName("UTF-8")));
				int offset = 0;
				while (offset != -1) {
					byte[] buff = new byte[1024];
					offset = input.read(buff);
					binary.write(buff);
				}
				binary.write(CRLF_STR.getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return this;
		}

		private String resolveFileType(String fileName) {
			// image
			if (fileName.endsWith(".gif") || fileName.endsWith(".jpeg") || fileName.endsWith(".jpg")
					|| fileName.endsWith(".png")) {
				return "Content-Type: image/jpeg";
			}
			if (fileName.endsWith(".xlsx") || fileName.endsWith(".docx") || fileName.endsWith(".doc")) {
				return "Content-Type: application/vnd.openxmlformats-officedocument.wordprocessingml.document";
			}
			return "Content-Type: application/octet-stream";
		}

		public byte[] toBinary() {
			try {
				binary.write(("--" + BOUNDARY_STR + "--").getBytes(Charset.forName("UTF-8")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return binary.toByteArray();
		}

		public byte[] build() {
			return toBinary();
		}

	}

	/***
	 * post 文件上传
	 * 
	 * @param uri
	 *            请求地址
	 * @param headers
	 *            字符参数 Key-Value Pair
	 * @param binary
	 *            二进制报文
	 * @return response.content
	 */
	public static String postBinary(String uri, byte[] binary, Map<String, String> headers) {

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

		conn.setRequestProperty("charset", "UTF-8");

		// 设置请求头 header
		conn.setRequestProperty("connection", "keep-alive");
		// Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)
		conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY_STR);
		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> header : headers.entrySet()) {
				conn.setRequestProperty(header.getKey(), header.getValue());
			}
		}

		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		OutputStream out = null;
		try {
			out = conn.getOutputStream();
			out.write(binary);
			out.flush();

		} catch (IOException e) {
			throw new RuntimeException("write body erro.", e);
		} finally {
			if (out != null) {
				try {
					// 关闭流
					out.close();
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
			bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
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
