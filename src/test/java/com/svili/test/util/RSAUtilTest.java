package com.svili.test.util;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.svili.constant.CharsetConstants;
import com.svili.util.RSAUtil;

public class RSAUtilTest {

	public static void main(String[] args) throws Exception {

		test1();
		test2();
		test3();
	}

	public static void test1() throws Exception {
		KeyPair keyPair = RSAUtil.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 明文
		String text = "hello , AES";
		// 公钥加密明文-->密文
		byte[] encrypted = RSAUtil.encrypt(text.getBytes(), publicKey);

		// 私钥解密密文-->明文
		byte[] decrypted = RSAUtil.decrypt(encrypted, privateKey);

		System.out.println(new String(decrypted, CharsetConstants.UTF_8));
	}

	public static void test2() throws Exception {
		KeyPair keyPair = RSAUtil.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 明文
		String text = "hello , AES";

		PublicKey publicKey2 = RSAUtil.getPublicKey(publicKey.getModulus(), publicKey.getPublicExponent());
		// 公钥加密明文-->密文
		byte[] encrypted = RSAUtil.encrypt(text.getBytes(), publicKey2);

		PrivateKey privateKey2 = RSAUtil.getPrivateKey(privateKey.getModulus(), privateKey.getPrivateExponent());

		// 私钥解密密文-->明文
		byte[] decrypted = RSAUtil.decrypt(encrypted, privateKey2);

		System.out.println(new String(decrypted, CharsetConstants.UTF_8));
	}

	public static void test3() throws Exception {
		KeyPair keyPair = RSAUtil.generateKeyPair();
		PublicKey publicKey = RSAUtil.getPublicKey(keyPair.getPublic().getEncoded());
		PrivateKey privateKey = RSAUtil.getPrivateKey(keyPair.getPrivate().getEncoded());
		// 明文
		String text = "hello , AES";

		// 公钥加密明文-->密文
		byte[] encrypted = RSAUtil.encrypt(text.getBytes(), publicKey);

		// 私钥解密密文-->明文
		byte[] decrypted = RSAUtil.decrypt(encrypted, privateKey);

		System.out.println(new String(decrypted, CharsetConstants.UTF_8));
	}

}
