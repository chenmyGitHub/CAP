/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.util.CapRtClazzScaner;
import com.comtop.cap.runtime.juel.model.JuelFunctionBean;
import com.comtop.cap.runtime.juel.scan.JuelMethodAnnParser;

/**
 * juel方法容器的初始化器
 * 
 * 
 * @author Juel方法扩展bean
 * @since 1.0
 * @version 2016-11-9 李小强
 */
public class JuelContainerIniter {
	/** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(JuelContainerIniter.class);
    /** juel 扩展函数包路径 */
    private static final String JUEL_EXT_METHOD_PACKAGE_PATH="com.comtop.cap.runtime.juel.ext";//com.comtop.cap.runtime.juel.ext
    /**是否已进行了容器初始化*/
	private static boolean isInited=false;
    /**
     * 初始化
     */
    public static void init() {
    	//已经初始化过就不需再初始化了
    	if(isInited){
    		return;
    	}
    	try {
    		//按规则扫描指定包、子包下的包有类
			 Set<Class<?>>  clazzSet=CapRtClazzScaner.getClasses(JUEL_EXT_METHOD_PACKAGE_PATH);
			 List<JuelFunctionBean> lstMethod=JuelMethodAnnParser.parser(clazzSet);
			 for(JuelFunctionBean bean:lstMethod){
				 JuelMethodExtContainer.push(bean);
			 }
			 isInited=true;
		} catch (SecurityException e) {
			LOGGER.error("注册JUEL扩展方法出错,错误详情："+e.getMessage(),e);
		} 
    }
	
}
