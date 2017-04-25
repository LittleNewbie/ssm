package com.svili.listener;

import org.springframework.context.ApplicationListener;

/**
 * Custom事件监听</br>
 * ApplicationContext(Spring上下文)监听</br>
 * ApplicationContext初始化时装载Bean(@Component)
 * 
 * @author lishiwei
 * @data 2017年4月25日
 *
 */
public class CustomEventListener implements ApplicationListener<CustomEvent> {

	@Override
	public void onApplicationEvent(CustomEvent event) {
		//deal with CustomEvent
	}

}
