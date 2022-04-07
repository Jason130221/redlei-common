package com.redlei.common;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @remark
 */
//@SpringBootConfiguration
//@EnableAutoConfiguration
@Component
//@ComponentScan
public class ApplicationContextHolder implements ApplicationContextAware {
	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * spring bean 工厂获取指定名字的bean
	 *
	 * @param name
	 * @param <T>
	 * @return
	 */
	public static <T> T getBean(String name) {
		Object bean = context.getBean(name);
		if (bean == null) {
			return null;
		}

		return (T) bean;
	}

	public static String getDictLabel(String value, String type, String defaultValue) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
			Map<String, String> map = getBean(type);

			return map.get(value);
		}
		return defaultValue;
	}

	public static String getDictValue(String label, String type, String defaultLabel) {
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
			String key;
			String value;
			Map<String, String> map = getBean(type);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				key = entry.getKey().toString();
				value = entry.getValue().toString();
				if (label.equals(value)) {

					return key;
				}
			}
		}
		return defaultLabel;
	}
}
