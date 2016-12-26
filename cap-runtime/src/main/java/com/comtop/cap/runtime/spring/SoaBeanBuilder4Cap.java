/******************************************************************************
 * Copyright (C) 2011 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.base.exception.CapRuntimeException;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.soa.bus.IBeanBuilder;

/**
 * Bean对象获取实现类 实现 soa接口
 * 
 * @author 罗珍明
 *
 */
public class SoaBeanBuilder4Cap implements IBeanBuilder{
	 
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CapBaseAppService.class);
	
	@Override
	public Object getBean(String className) {
		try {
			Class<?> beanType = Class.forName(className);
			return BeanContextUtil.getBean(beanType);
		} catch (ClassNotFoundException e) {
			LOGGER.error("class is not exist in spring context:"+className,e);
			throw new CapRuntimeException("class is not exist in spring context:"+className,e);
		}
	}
}
