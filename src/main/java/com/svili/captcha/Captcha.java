package com.svili.captcha;

/***
 * 验证码接口
 * 
 * @author lishiwei
 * @data 2017年8月30日
 *
 */
public interface Captcha {

	/***
	 * 发送验证码
	 * 
	 * @param target
	 *            手机号,邮箱,sessionId
	 * @return 成功-true,失败-false
	 */
	boolean send(String target);

	/***
	 * 匹配校验
	 * 
	 * @param target
	 *            手机号,邮箱,sessionId
	 * @param code
	 *            验证码
	 * @return 正确-true
	 */
	boolean verify(String target, String code);

}
