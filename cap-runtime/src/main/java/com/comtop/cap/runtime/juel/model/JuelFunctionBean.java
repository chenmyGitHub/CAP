/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel.model;

import java.lang.reflect.Method;

/**
 * 业务序号
 * 
 * 
 * @author Juel方法扩展bean
 * @since 1.0
 * @version 2016-11-9 李小强
 */
public class JuelFunctionBean {
	/**前缀,可为空*/
	private String prefix="";
	/**本地命名,不可为空，不可重复*/
	private String localName="";
	/**方法method对象，要求必须是静态可以直接调用的方法*/
	private Method method;
	
	/** 关键参数构造器
	 * @param localName 本地命名,不可为空，不可重复
	 * @param method 方法method对象，要求必须是静态可以直接调用的方法
	 */
	public JuelFunctionBean(String localName, Method method) {
		super();
		this.localName = localName;
		this.method = method;
	}
	/** 全参构造器
	 * @param prefix 前缀,可为空
	 * @param localName 本地命名,不可为空，不可重复
	 * @param method 方法method对象，要求必须是静态可以直接调用的方法
	 */
	public JuelFunctionBean(String prefix, String localName, Method method) {
		super();
		this.prefix = prefix;
		this.localName = localName;
		this.method = method;
	}
	/**
	 * @return 前缀,可为空
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * 设置：前缀,可为空
	 * @param prefix 前缀,可为空
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return 本地命名,不可为空，不可重复
	 */
	public String getLocalName() {
		return localName;
	}
	/***
	 * 设置：本地命名,不可为空，不可重复
	 * @param localName 本地命名,不可为空，不可重复
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	/***
	 * @return 方法method对象，要求必须是静态可以直接调用的方法
	 */
	public Method getMethod() {
		return method;
	}
	/***
	 * 设置：方法method对象，要求必须是静态可以直接调用的方法
	 * @param method 方法method对象，要求必须是静态可以直接调用的方法
	 */
	public void setMethod(Method method) {
		this.method = method;
	}
	@Override
	public String toString() {
		return "JuelFunctionBean [prefix=" + prefix + ", localName="
				+ localName + ", method=" + method.getName() + "]";
	}
	
}
