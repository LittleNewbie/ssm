package com.svili.captcha;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.svili.sms.SmsAPI;
import com.svili.util.RegexUtil;

/**
 * 短信验证码
 * 
 * @author lishiwei
 * @data 2017年8月30日
 *
 */
@Service
public class SmsCaptcha implements Captcha {

	@Resource
	private SmsAPI smsAPI;

	@Resource
	private CaptchaManagement captcha;

	@Override
	public boolean send(String target) {
		if (!RegexUtil.isMobilePhone(target)) {
			throw new IllegalArgumentException("手机号码格式不正确,target :" + target);
		}
		return smsAPI.send(target, captcha.put(target));
	}

	@Override
	public boolean verify(String target, String code) {
		if (StringUtils.isBlank(code)) {
			return false;
		}
		// 从缓存中读取code
		String cache = captcha.get(target);
		return code.equals(cache);
	}

}
