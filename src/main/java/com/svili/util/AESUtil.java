package com.svili.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/***
 * AES加解密
 *
 * @author svili
 * @date 2017年9月8日
 *
 */
public class AESUtil {

	private static class GeneratorHolder {

		/** AES秘钥生成器 */
		private static KeyGenerator keyGenerator;

		static {
			// 静态内部类实现单例模式
			try {
				keyGenerator = KeyGenerator.getInstance("AES");
			} catch (NoSuchAlgorithmException e) {
				LogUtil.error("KeyGenerator fro AES init error.", e);
			}
			// AES秘钥长度:128bit(位)=16byte(字节)
			keyGenerator.init(128);
		}
	}

	public static KeyGenerator getGenerator() {
		return GeneratorHolder.keyGenerator;
	}

	public static byte[] generateKey() {
		SecretKey secretKey = getGenerator().generateKey();
		return secretKey.getEncoded();
	}

	public static byte[] encrypt(byte[] data, byte[] encodedKey)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// 初始化加密算法
		SecretKeySpec keySpec = new SecretKeySpec(encodedKey, "AES");
		Cipher cipher = getCipher();

		cipher.init(Cipher.ENCRYPT_MODE, keySpec);

		// 加密
		return cipher.doFinal(data);

	}

	public static byte[] decrypt(byte[] data, byte[] encodedKey)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// 初始化解密算法
		SecretKeySpec keySpec = new SecretKeySpec(encodedKey, "AES");
		Cipher cipher = getCipher();

		cipher.init(Cipher.DECRYPT_MODE, keySpec);
		// 解密
		return cipher.doFinal(data);
	}

	public static Cipher getCipher() {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {

		} catch (NoSuchPaddingException e) {

		}
		return cipher;
	}

}
