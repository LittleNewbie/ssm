package com.svili.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/***
 * RSA加解密</br>
 * <ul>
 * RSA数据加解密方式
 * <li>1.明文-->公钥加密-->密文-->私钥解密-->明文
 * <li>2.明文-->私钥加密-->密文-->公钥解密-->明文
 * <ul>
 * SRA密钥说明
 * <li>相关参数:公钥指数publicExponent,私钥指数privateExponent,模值modulus
 * <li>key = fun(modulus,exponent) {@link #getPublicKey(BigInteger, BigInteger)}
 * <li>key = fun(encodedKey) {@link #getPublicKey(byte[])}
 *
 * @author svili
 * @date 2017年9月8日
 *
 */
public class RSAUtil {

	/** RSA密文长度:128byte(字节) */
	private final static int DECRYPT_BLOCK_CAPACITY = 2 << 6;

	/** RSA明文长度:117byte(字节),padding=11byte(字节) */
	private final static int ENCRYPT_BLOCK_CAPACITY = (2 << 6) - 11;

	private static class GeneratorHolder {

		/** RSA秘钥生成器 */
		private static KeyPairGenerator keyPairGenerator;

		static {
			// 静态内部类实现单例模式
			try {
				keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			} catch (NoSuchAlgorithmException e) {
				keyPairGenerator = null;
				LogUtil.error("KeyGenerator fro RSA init error.", e);
			}
			// 模值长度
			keyPairGenerator.initialize(1024);
		}
	}

	public static KeyPairGenerator getGenerator() {
		return GeneratorHolder.keyPairGenerator;
	}

	public static KeyPair generateKeyPair() {
		// 生成密钥对
		return getGenerator().generateKeyPair();
	}

	public static Cipher getCipher() {
		Cipher cipher = null;
		try {
			// 算法/模式/填充
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {

		} catch (NoSuchPaddingException e) {

		}
		return cipher;
	}

	/***
	 * 加密/解密算法 </br>
	 * <ul>
	 * RSA算法中,对明文/密文的字节长度有所限制,超过最大长度需要分段处理.
	 * <li>密文最大字节长度为:{@value #DECRYPT_BLOCK_CAPACITY}
	 * <li>明文最大字节长度为:{@value #ENCRYPT_BLOCK_CAPACITY}
	 * 
	 * @param blockCapacity
	 *            块的容量(最大字节长度)
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static byte[] doFinalInternal(byte[] data, Cipher cipher, int blockCapacity)
			throws IllegalBlockSizeException, BadPaddingException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2 << 6);

		int offset = 0;
		int waitResolveLength = 0;

		while ((waitResolveLength = data.length - offset) > 0) {
			byte[] resolved;
			if (waitResolveLength < blockCapacity) {
				resolved = cipher.doFinal(data, offset, waitResolveLength);
			} else {
				resolved = cipher.doFinal(data, offset, blockCapacity);
			}

			try {
				outputStream.write(resolved);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			offset += blockCapacity;
		}

		return outputStream.toByteArray();
	}

	/**
	 * 数据加密
	 * 
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * 
	 */
	public static byte[] encrypt(byte[] data, Key key)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = getCipher();
		// 加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 执行加密算法
		return doFinalInternal(data, cipher, ENCRYPT_BLOCK_CAPACITY);

	}

	/**
	 * 数据解密
	 * 
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * 
	 */
	public static byte[] decrypt(byte[] data, Key key)
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		Cipher cipher = getCipher();
		cipher.init(Cipher.DECRYPT_MODE, key);

		return doFinalInternal(data, cipher, DECRYPT_BLOCK_CAPACITY);

	}

	public static PublicKey getPublicKey(BigInteger modulus, BigInteger exponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	public static PrivateKey getPrivateKey(BigInteger modulus, BigInteger exponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	public static PublicKey getPublicKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// X509
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	public static PrivateKey getPrivateKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// PKCS8
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

}
