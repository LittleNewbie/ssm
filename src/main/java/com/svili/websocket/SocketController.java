package com.svili.websocket;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svili.model.vo.JsonModel;

@RestController
@RequestMapping(value = "/websocket")
public class SocketController {

	@RequestMapping(value = "/subject/notify", method = RequestMethod.POST)
	public JsonModel push(@RequestBody String jsonText) {
		JSONObject json = JSON.parseObject(jsonText);
		String principal = json.getString("principal");
		String type = json.getString("type");
		String data = json.getString("data");

		Assert.hasText(principal);
		Assert.hasText(type);
		Assert.hasText(data);

		WebSocketSubject subject = WebSocketSubject.Holder.getSubject(principal);
		subject.notify(type, data);

		return JsonModel.success(Boolean.TRUE);
	}

}
