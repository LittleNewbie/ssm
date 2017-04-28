package com.svili.common;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.svili.exception.CustomAppException;
import com.svili.exception.CustomInnerException;

/**
 * 安全工具类。包括加密、解密等各个算法。
 * 字符与字节的转换统一使用UTF-8。
 * 
 * @author svili
 * @since  2017-04-25
 */
public final class SecurityUtil {
	/** AES密钥长度 */
	public final static int AES_KEY_LENGTH = 16;
	
	/** RSA算法每次加密的字节不能超过117，超过需要分段加密 */
	private final static int RSA_MAX_ENCRYPT_BLOCK = 117;
	
	/** RSA算法每次解密的字节不能超过128，超过需要分段解密 */
	private final static int RSA_MAX_DECRYPT_BLOCK = 128;
	
	/** AES算法对称密钥生成器，线程安全 */
	private static KeyGenerator keyGenForAES;
	
	static {
		try {
			// 预先初始化密钥生成器，会大大提高加密和解密的过程
			keyGenForAES = KeyGenerator.getInstance("AES");
			keyGenForAES.init(128);
		} catch (NoSuchAlgorithmException e) {
			LogUtil.error("初始化AES密钥生成器失败", e);
		}
	}
	
	/**
	 * 生成非对称密钥对
	 * 
	 * @return 返回密钥对的map<br>
	 *         私钥=map.get("privateKey")<br>
	 *         公钥=map.get("publicKey")
	 */
	public static Map<String, String> genRSAKeyPair() {
		//try {
			Map<String, String> result = new HashMap<String, String>();
			
			// 初始化RSA算法
			KeyPairGenerator generator = null;
			try {
				generator = KeyPairGenerator.getInstance("RSA");
				generator.initialize(1024);
			} catch (NoSuchAlgorithmException e) {
				throw new CustomInnerException("生成RSA密钥对出错",e);
				//LogUtil.error("生成RSA密钥对出错", e);
			}
			
			// 生成密钥对
			KeyPair keyPair = generator.generateKeyPair();
			
			// 获取密钥对字符串表示形式
			result.put("privateKey", encryptBASE64(keyPair.getPrivate().getEncoded()));
			result.put("publicKey", encryptBASE64(keyPair.getPublic().getEncoded()));
			
			return result;
	}
	
	/**
	 * RSA每次加密数据不能超过117字节，超过要分段处理
	 * 
	 * @param cipher
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptRSA(Cipher cipher, byte[] input) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(RSA_MAX_ENCRYPT_BLOCK);
		int offset = 0;
		int len = 0;
		
		while (input.length - offset > 0) {
			if (input.length - offset >= RSA_MAX_ENCRYPT_BLOCK) {
				len = RSA_MAX_ENCRYPT_BLOCK;
			} else {
				len = input.length - offset;
			}
			
			bos.write(cipher.doFinal(input, offset, len));
			
			offset = offset + len;
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * RSA每次解密数据不能超过128字节，超过要分段处理
	 * 
	 * @param cipher
	 * @param input
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptRSA(Cipher cipher, byte[] input) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(RSA_MAX_DECRYPT_BLOCK);
		int offset = 0;
		int len = 0;
		
		while (input.length - offset > 0) {
			if (input.length - offset >= RSA_MAX_DECRYPT_BLOCK) {
				len = RSA_MAX_DECRYPT_BLOCK;
			} else {
				len = input.length - offset;
			}
			
			bos.write(cipher.doFinal(input, offset, len));
			
			offset = offset + len;
		}
		
		return bos.toByteArray();
	}
	
	/**
	 * 使用私钥对数据加密
	 * 
	 * @param data 需要加密的数据
	 * @return 加密后的字节流使用BASE64再次加密
	 */
	public static String encryptRSAByPrivateKey(String data,String StringPrivateKey) {
		try {
			// 获取私钥的字节表示
			//MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANcrToapw/5rb5DIixqgt7jVmFpFb/YtB2frmm+hQhL5+Hzm63Yq2v6zyD6OFrnyu0w7spNAsA4Ialw6h6+abMtUnXpZRca7eTonOBeE3edF6Db10Asak77XTiZwzxwA1N5+Czvc06BiLO5n8TxhPZ4j6yB3VJxt/ftD87+0CLHhAgMBAAECgYEArXWzI2p+n386/Iw/X+MK2U4QOU3bGalpXASahr2Ih8XO5do/Hg8kZsrGdm+TsKoXBjjmGEHA7D8qh1mEAzPJ2ZKIVMpttNS44CLmZUMguONklDY14bHFSKKz02NLXK45px55E5UYA+miN9W0HK6HjIq488T0IHETe6A8ZBQFDoUCQQD00RjTlhpVblxtLxLCQDLg/xfecJV1pQwreoNFEdqYu2pFDySSxciuVznK+/gJP7ONKTix6NNLarZXT9q4376HAkEA4P+CNfihVZfydiSZICSi0bQSCeWwWAQHz49JdObr9ERlyZE+WQEW3V1T3AMjPIgTE4LR6e1dpbaLrJuAJ6P+VwJAOoN/m5LU2HZ4QOBva15MytotqkzebC/2qK2vkmKiDL8+tyLXav066fCPL6Tps8w17hdcJamwVuY6jsPXUzinIQJAK3CJRjxLgcgFUtP8EF2iZdRbSzfiVQvqKiOi7gQLeCygbksxe2Ofc3uqTib47Z3j+Pdf6ccfNnhI/TlEg+LAnwJAZ+wzwvZa/wZR/nJYWWH98VKhs6IZKKchSyX/qTD9S//My4Nb/3qskCmkhtx4xTCa+3Hdw4njrbAzp9geR7D6Tg==
			byte[] encodedKey = decryptBASE64(StringPrivateKey);
			
			// 初始化私钥
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key privateKey = keyFactory.generatePrivate(keySpec);
			
			// 使用私钥加密数据
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			
			// 私钥加密后的字节流，用BASE64再次加密
			return encryptBASE64(encryptRSA(cipher, toUTF8Bytes(data)));
		} catch(Exception e) {
			throw new CustomInnerException("RSA私钥加密明文：" + data, e);
		}
	}
	
	/**
	 * 使用私钥对数据解密
	 * 
	 * @param data 源数据使用公钥加密后，再用BASE64加密
	 * @return 返回明文数据
	 */
	public static String decryptRSAByPrivateKey(String data,String StringPrivateKey) {
		try {
			// 获取私钥的字节表示
			byte[] encodedKey = decryptBASE64(StringPrivateKey);
			
			// 初始化私钥
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key privateKey = keyFactory.generatePrivate(keySpec);
			
			// 使用私钥解密数据
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			// 使用BASE64先解密，然后用私钥再次解密
			return toUTF8String(decryptRSA(cipher, decryptBASE64(data)));
		} catch(Exception e) {
			throw new CustomInnerException("RSA私钥解密密文：" + data, e);
		}
	}
	
	/**
	 * 使用公钥对数据加密
	 * 
	 * @param data 需要加密的数据
	 * @return 加密后的字节流使用BASE64再次加密
	 */
	public static String encryptRSAByPublicKey(String data,String StringPublicKey) {
		try {
			// 获取公钥的字节表示
			byte[] encodedKey = decryptBASE64(StringPublicKey);
			
			// 初始化公钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key publicKey = keyFactory.generatePublic(keySpec);
			
			// 使用公钥加密数据
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			// 使用公钥加密后，再用BASE64加密
			return encryptBASE64(encryptRSA(cipher, toUTF8Bytes(data)));
		} catch(Exception e) {
			throw new CustomInnerException("RSA公钥加密明文：" + data, e);
		}
	}
	
	/**
	 * 使用公钥对数据解密
	 * 
	 * @param data 源数据使用私钥钥加密后，再用BASE64加密
	 * @return 返回明文数据
	 */
	public static String decryptRSAByPublicKey(String data,String StringPublicKey) {
		try {
			// 获取公钥的字节表示
			byte[] encodedKey = decryptBASE64(StringPublicKey);
			
			// 初始化公钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key publicKey = keyFactory.generatePublic(keySpec);
			
			// 使用公钥解密数据
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			
			// 使用BASE64先解密，然后用公钥再次解密
			return toUTF8String(decryptRSA(cipher, decryptBASE64(data)));
		} catch(Exception e) {
			throw new CustomInnerException("RSA公钥解密密文：" + data, e);
		}
	}
	
	/**
	 * 生成128位对称密钥
	 * 
	 * @return 返回经过BASE64编码的密钥
	 */
	public static String genAESKey() {
		try {
			// 生成AES密钥
			SecretKey secretKey = keyGenForAES.generateKey();

	        // 将密钥用BASE64编码
	        return encryptBASE64(secretKey.getEncoded());
		} catch (Exception e) {
			throw new CustomInnerException("生成AES对称秘钥失败", e);
		}
	}
	
	/**
	 * 对称算法AES加密数据
	 * 
	 * @param data 明文数据
	 * @param key BASE64加密后的对称密钥
	 * @return 加密后的数据，用BASE64再次加密返回
	 */
	public static String encryptAES(String data, String key) {
		try {
			// 128位密钥
			byte[] encodedKey = decryptBASE64(key);
			
			// 初始化加密算法
			SecretKeySpec keySpec = new SecretKeySpec(encodedKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			
			// 加密后，再用BASE64加密
			return encryptBASE64(cipher.doFinal(toUTF8Bytes(data)));
		} catch (Exception e) {
			throw new CustomInnerException("AES加密明文：" + data, e);
		}
	}
	
	/**
	 * 对称算法AES解密数据
	 * 
	 * @param data 源数据使用AES加密后，再用BASE64加密
	 * @param key BASE64加密后的对称密钥
	 * @return
	 */
	public static String decryptAES(String data, String key) {
		try {
			// 128位密钥
			byte[] encodedKey = decryptBASE64(key);
	
			// 初始化解密算法
			SecretKeySpec keySpec = new SecretKeySpec(encodedKey, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			
			// 使用BASE64先解密，然后用对称密钥再次解密
			return toUTF8String(cipher.doFinal(decryptBASE64(data)));
		} catch (Exception e) {
			throw new CustomInnerException("AES解密密文：" + data, e);
		}
	}
	
	/**
	 * BASE64加密算法
	 * 
	 * @param data 需要加密的数据
	 * @return 返回加密后的字符串
	 */
	public static String encryptBASE64(byte[] data) {
		return new Base64().encodeToString(data);
	}
	
	/**
	 * BASE64解密算法
	 * 
	 * @param data 需要解密的字符串
	 * @return 返回解密后的字节数组
	 */
	public static byte[] decryptBASE64(String data) {
		return new Base64().decode(data);
	}
	
	/**
	 * 把字符串转换成UTF8编码的字节数组
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] toUTF8Bytes(String data) {
		try {
			return data.getBytes("UTF-8");
		} catch (Exception e) {
			throw new CustomAppException(e);
		}
	}
	
	/**
	 * 把UTF8编码的字节数组转换为字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String toUTF8String(byte[] data) {
		try {
			return new String(data, "UTF-8");
		} catch (Exception e) {
			throw new CustomInnerException("",e);
		}
	}
	
	/**
	 * SHA1加密
	 * 
	 * @param data
	 * @return
	 */
	public static String encryptSHA1(String data) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(toUTF8Bytes(data));
			return byteToHex(messageDigest.digest());
		} catch(Exception e) {
			throw new CustomInnerException("SHA1加密明文：" + data, e);
		}
	}
	
	/**
	 * 字节数组转换为大写十六进制字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteToHex(byte[] data) {
		StringBuilder hexString = new StringBuilder();
		
		for (int i = 0; i < data.length; i++) {
			String hex = Integer.toHexString(data[i] & 0xFF);

			if (hex.length() < 2) {
				hexString.append('0');  
			}

			hexString.append(hex);
		}
		
		return hexString.toString().toUpperCase();
	}
	
	/**
	 * 验证SHA1签名
	 * 
	 * @param data 源数据
	 * @param signature SHA1签名
	 * @return true：签名一致 <br> false：签名不一致
	 */
	public static boolean verifySHA1Signature(String data, String signature) {
		if(data == null || signature == null) {
			return false;
		}
		
		if(signature.equals(encryptSHA1(data))) {
			return true;
		}
		
		return false;
	}
}
