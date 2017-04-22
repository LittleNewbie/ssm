package com.svili.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * body开始:--boundary </br>
 * body结束:--boundary--</br>
 * boundary分隔符,避免和上传文本内容重复
 * 
 * @author lishiwei
 * @data 2017年4月1日
 *
 */
public class HttpUploadUtil {

	static final String CRLF_STR = "\r\n";
	static final byte[] CRLF = "\r\n".getBytes();

	static final String BOUNDARY_STR = "--------yktUploadBoundary------";
	static final byte[] BOUNDARY = "--------yktUploadBoundary------".getBytes();

	/**
	 * 将请求参数转化为http form-body</br>
	 * 
	 * @param textParams
	 *            字符参数
	 * @param fileName
	 *            文件名
	 * @param file
	 *            字节文件
	 * @return response.content
	 */
	public static byte[] toFormBody(Map<String, String> textParams, String fileName, byte[] file) {
		int capacity = 500;

		if (file != null) {
			capacity = capacity + 500;
		}

		ByteBuffer buffer = ByteBuffer.allocate(capacity);

		if (textParams != null && !textParams.isEmpty()) {
			for (Entry<String, String> entry : textParams.entrySet()) {
				buffer.put(CRLF).put("--".getBytes()).put(BOUNDARY).put(CRLF);
				buffer.put("Content-Disposition: form-data; name=\"".getBytes()).put(entry.getKey().getBytes())
						.put("\"".getBytes());
				buffer.put(CRLF).put(CRLF);
				buffer.put(entry.getValue().getBytes());
			}
		}

		if (file != null) {
			buffer.put(CRLF).put("--".getBytes()).put(BOUNDARY).put(CRLF);
			buffer.put("Content-Disposition: form-data; name=\"file\";filename=\"".getBytes()).put(fileName.getBytes())
					.put("\"".getBytes());
			buffer.put(CRLF);
			buffer.put("Content-Type: application/octet-stream".getBytes());
			buffer.put(CRLF).put(CRLF);
			buffer.put(file);
		}

		// end body
		buffer.put(CRLF).put("--".getBytes()).put(BOUNDARY).put("--".getBytes()).put(CRLF);
		return buffer.array();
	}

	/**
	 * httpClient post 单文件上传</br>
	 * 多文件上传请使用httpUpload</br>
	 * <p>
	 * 文件上传 不建议使用HttpClient</br>
	 * 效率低,耗时约为URLConnection的10倍
	 * </p>
	 * 
	 * <p>
	 * 原因正在分析,初步认为在 byteBuffer 和 ByteArrayEntity
	 * </p>
	 * 
	 * @param url
	 *            请求地址
	 * @param headers
	 * @param textParams
	 *            字符参数对
	 * @param fileName
	 *            文件名
	 * @param file
	 *            字节文件
	 * @return response.content
	 */
	@Deprecated
	public static String httpClientUpload(String url, Map<String, String> headers, Map<String, String> textParams,
			String fileName, byte[] file) throws Exception {
		ByteArrayEntity entity = new ByteArrayEntity(toFormBody(textParams, fileName, file));
		return httpClientUpload(url, headers, entity);
	}

	/**
	 * httpClient post 单文件上传</br>
	 * 多文件上传请使用httpUpload</br>
	 * <p>
	 * 文件上传 不建议使用HttpClient</br>
	 * 效率低,耗时约为URLConnection的10倍
	 * </p>
	 */
	@Deprecated
	public static String httpClientUpload(String url, Map<String, String> headers, HttpEntity entity) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		httpPost.setHeader("Content-Type", "multipart/form-data; boundary=" + BOUNDARY_STR);
		httpPost.setEntity(entity);

		CloseableHttpResponse response = null;

		try {
			response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} finally {
			if (response != null)
				response.close();
			if (httpClient != null)
				httpClient.close();
		}
		return null;
	}

	/**
	 * http URLConnection 文件上传
	 * 
	 * @param url
	 *            请求地址
	 * @param textParams
	 *            字符参数 key-value
	 * @param fileParams
	 *            文件参数 filename-byte[] file
	 * @return response.content
	 */
	public static String httpUpload(String url, Map<String, String> textParams, Map<String, byte[]> fileParams) {
		// 接收返回值
		StringBuffer response = new StringBuffer();
		// 创建连接对象
		HttpURLConnection conn = null;
		try {

			// 创建请求对象URL
			URL httpUrl = new URL(url);

			conn = (HttpURLConnection) httpUrl.openConnection();

			conn.setRequestMethod("POST");

			// get请求用不到conn.getOutputStream()，因为参数直接追加在地址后面，因此默认是false。
			// post请求(比如：文件上传)需要往服务区传输大量的数据，这些数据是放在http的body里面的，因此需要在建立连接以后，往服务端写数据。
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			// 设置请求属性
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY_STR);

			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			// 建立通信链接
			// conn.connect();

			OutputStream out = new DataOutputStream(conn.getOutputStream());

			if (textParams != null && !textParams.isEmpty()) {
				StringBuffer buffer = new StringBuffer();
				for (Entry<String, String> entry : textParams.entrySet()) {
					buffer.append(CRLF_STR).append("--").append(BOUNDARY_STR).append(CRLF_STR);
					buffer.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"");
					buffer.append(CRLF_STR).append(CRLF_STR);
					buffer.append(entry.getValue());
				}
				out.write(buffer.toString().getBytes());
			}

			if (fileParams != null && !fileParams.isEmpty()) {
				for (Entry<String, byte[]> entry : fileParams.entrySet()) {
					// fileName
					StringBuffer buffer = new StringBuffer();
					buffer.append(CRLF_STR).append("--").append(BOUNDARY_STR).append(CRLF_STR);
					buffer.append("Content-Disposition: form-data; name=\"file\";filename=\"").append(entry.getKey())
							.append("\"");
					buffer.append(CRLF_STR).append("Content-Type: application/octet-stream");
					buffer.append(CRLF_STR).append(CRLF_STR);
					out.write(buffer.toString().getBytes());
					// file
					DataInputStream in = new DataInputStream(new ByteArrayInputStream(entry.getValue()));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY_STR + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回的输入流 并设置字符编码
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			// 读行
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				response.append(line);
			}
			// 关闭流
			bufferedReader.close();
		} catch (Exception e) {

		} finally {
			
		}
		return response.toString();
	}

}
