/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.juel.model.JuelFunctionBean;

import de.odysseus.el.util.SimpleContext;

/**
 * Juel函数扩展容器,用于存放用户扩展的Juel函数
 * @author 李小强
 * @since 1.0
 * @version 2016-11-9 李小强
 */
public class JuelMethodExtContainer {
	/** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(JuelMethodExtContainer.class);
	/**juel 扩展函数Map**/
	private static Map<String,JuelFunctionBean> funcMap = new HashMap<String,JuelFunctionBean>();
	
	/***
	 * 私有化构造器
	 */
	private JuelMethodExtContainer(){
		
	}
	/**
	 * 添加扩展方法
	 * @param bean Juel方法扩展bean;	其中：localName本地命名,不可为空，不可重复 ;  method:方法method对象,不可为空，要求必须是静态可以直接调用的方法;
	 * @return 扩展成功返回true，否则返回false
	 */
	public static boolean push(JuelFunctionBean bean){
		if(bean==null||bean.getLocalName()==null||bean.getLocalName().trim().equals("")||bean.getMethod()==null){
			return false;
		}
		funcMap.put(bean.getLocalName(), bean);
		return true;
	}
	
	/**
	 * @return juel 扩展函数Map
	 */
	public static Map<String,JuelFunctionBean> getFuncMap(){
		return funcMap;
	}
	
	/**
	 * 注入用户扩展的函数注入到juel上下文中
	 * @param context juel上下文中
	 */
	public static void injectFunction2JuelContext(SimpleContext context){
		JuelContainerIniter.init();
		try {
			for(Map.Entry<String, JuelFunctionBean> entry:funcMap.entrySet()){    
				context.setFunction(entry.getValue().getPrefix(), entry.getValue().getLocalName(), entry.getValue().getMethod());
			}
		} catch (SecurityException e) {
			LOGGER.error("访问获取序列编号方法getCodeSuffixBySeq出错:" + e);
	        throw new RuntimeException("访问获取序列编号方法getCodeSuffixBySeq出错:", e);
		} catch (Exception e) {
			LOGGER.error("未找到方法：getCodeSuffixBySeq" + e);
	        throw new RuntimeException("未找到方法：getCodeSuffixBySeq", e);
		}
	}
}
