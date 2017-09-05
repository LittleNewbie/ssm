package com.svili.util.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/***
 * 默认的证书信任管理器,绕过SSL验证</br>
 * 静态内部类实现单例模式.
 * 
 * @author lishiwei
 * @data 2017年8月22日
 *
 */
public class DefaultTrustManager implements X509TrustManager {

	/** 私有化构造方法 */
	private DefaultTrustManager() {
	}

	public static DefaultTrustManager getInstance() {
		return Holder.trustManager;
	}

	public static class Holder {
		private static DefaultTrustManager trustManager = new DefaultTrustManager();
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}
