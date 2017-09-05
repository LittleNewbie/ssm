package com.svili.websocket;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svili.exception.ServiceExceptionFactory;

/***
 * Web Scoket服务.</br>
 * URL expression as ws://ip:port/ykt_mobileapi/websocket/{principal}</br>
 * 观察者模式
 * 
 * 
 * @author lishiwei
 * @data 2017年7月12日
 *
 */
@ServerEndpoint(value = "/websocket/{principal}")
public class DefaultWebSocket {

	/** Web Socket连接建立成功的回调方法 */
	@OnOpen
	public void onOpen(@PathParam("principal") String principal, Session session) {

		// create observer
		WebSocketObserver observer = new WebSocketObserver(session);
		// get subject
		WebSocketSubject subject = WebSocketSubject.Holder.getSubject(principal);
		// register observer into subject
		subject.addObserver(observer);
	}

	/** 客户端发来消息 */
	@OnMessage
	public void onMessage(@PathParam("principal") String principal, String message, Session session) {

		// message EX:{"type":"","data":""}
		JSONObject json = JSON.parseObject(message);
		String type = json.getString("type");
		String data = json.getString("data");
		
		// create subject
		WebSocketSubject subject = WebSocketSubject.Holder.getSubject(principal);
		// notify
		subject.notify(type, data);
	}

	@OnClose
	public void onClose(@PathParam("principal") String principal, Session session) {

		// get subject
		WebSocketSubject subject = WebSocketSubject.Holder.getSubject(principal);

		// get observer
		WebSocketObserver observer = new WebSocketObserver(session);
		// delete observer from subject
		subject.deleteObserver(observer);

		// close session and close Web Socket
		try {
			if (session.isOpen()) {
				session.close();
			}
		} catch (IOException e) {
			throw ServiceExceptionFactory.undefined("close web socket session error.", e);
		}

	}

	@OnError
	public void onError(@PathParam("principal") String principal, Session session, Throwable error) {
		throw ServiceExceptionFactory.undefined("web socket error.", error);
	}

}
