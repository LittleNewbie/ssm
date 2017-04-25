package com.svili.listener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Custom事件发布者</br>
 * 携带pring上下文</br>
 * ApplicationContext初始化时装载Bean(@Component)
 * 
 * @author lishiwei
 * @data 2017年4月25日
 *
 */
public class CustomEventPublisher implements ApplicationEventPublisher, ApplicationContextAware {
	
	/* Spring上下文 */
	private ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//获取Spring上下文
		this.context = applicationContext;
	}

	@Override
	public void publishEvent(ApplicationEvent event) {
		//事件发布
		if (event instanceof CustomEvent) {
			this.context.publishEvent(event);
		}

	}

	@Override
	public void publishEvent(Object event) {
		//其他事件发布,不建议使用
	}

}
