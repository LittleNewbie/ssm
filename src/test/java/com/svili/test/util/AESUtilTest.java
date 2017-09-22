package com.svili.test.util;

import com.svili.constant.CharsetConstants;
import com.svili.util.AESUtil;

public class AESUtilTest {

	public static void main(String[] args) throws Exception {
		byte[] key = AESUtil.generateKey();

		// 明文
		String text = "hello , AES";
		// 密文
		byte[] encrypted = AESUtil.encrypt(text.getBytes(), key);

		System.out.println(new String(encrypted, CharsetConstants.UTF_8));

		// 解密密文-->明文
		byte[] decrypted = AESUtil.decrypt(encrypted, key);

		System.out.println(new String(decrypted, CharsetConstants.UTF_8));
	}

}
