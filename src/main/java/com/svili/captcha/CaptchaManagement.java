package com.svili.captcha;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/***
 * 验证码缓存
 * 
 * @author lishiwei
 * @data 2017年8月30日
 *
 */
@Component
@CacheConfig(cacheNames = { "ssm_frame.captcha" })
public class CaptchaManagement {

	/** 获取缓存中的验证码 */
	@Cacheable(key = "#target")
	public String get(String target) {
		return null;
	}

	/** 生成验证码,并写入缓存 */
	@CachePut(key = "#target")
	public String put(String target) {
		return RandomStringUtils.randomNumeric(6);
	}

	/** 清除缓存 */
	@CacheEvict(key = "#target")
	public boolean evict(String target) {
		return Boolean.TRUE;
	}

}
