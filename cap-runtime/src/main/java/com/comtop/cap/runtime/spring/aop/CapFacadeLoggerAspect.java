/******************************************************************************
 * Copyright (C) 2011 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.spring.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.exception.CapRuntimeException;

/**
 * @author luozhenming
 *
 */
public class CapFacadeLoggerAspect implements org.aopalliance.intercept.MethodInterceptor{
	
	/** 日志对象 */
    private final Logger LOG = LoggerFactory.getLogger(CapFacadeLoggerAspect.class);
	
	/**
	 * 此方法在使用tx切面时，生效
	 * 
	 * @param p 方法切面
	 * @return 方法调用返回值
	 * 
	 **/
	public Object loggerRecoder(ProceedingJoinPoint p){
		try {
			Object obj = p.proceed();
			return obj;
		} catch (Throwable e) {
			LOG.error(e.getMessage(),e);
			throw new CapRuntimeException(e);
		}
	}

	/**
	 * 此方法在使用beanName代理连接器时生效
	 */
	@Override
	public Object invoke(MethodInvocation invoke) throws Throwable {
		try {
			Object obj = invoke.proceed();
			return obj;
		} catch (Throwable e) {
			System.out.println("org.aopalliance.intercept.MethodInterceptor invoke");
			LOG.error(e.getMessage(),e);
			throw new CapRuntimeException(e);
		}
	}
}
