package com.svili.listener;

import org.springframework.context.ApplicationEvent;

/**
 * Application事件
 * 
 * @author lishiwei
 * @data 2017年4月25日
 *
 */
public class CustomEvent extends ApplicationEvent {

	private static final long serialVersionUID = 6610776168893186680L;

	public CustomEvent(Object source) {
		super(source);
	}

}
