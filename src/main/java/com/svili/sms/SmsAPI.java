package com.svili.sms;

public class SmsAPI {

	public Boolean send(String phone, String content) {
		String message = "Send sms success. {'phone':'#phone#','msg':'#msg#'}";
		System.out.println(message.replace("#phone#", phone).replace("#msg#", content));
		return Boolean.TRUE;
	}

}
