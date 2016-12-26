/******************************************************************************
 * Copyright (C) 2014  ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.exceptionhandler.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cip.jodd.proxetta.ProxyAdvice;
import com.comtop.cip.jodd.proxetta.ProxyTarget;

/**
 * 
 * facade层异常通知类
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-25 柯尚福
 */
public class FacadeExceptionAdvice implements ProxyAdvice {
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(FacadeExceptionAdvice.class);
    
    /**
     * 拦截到后执行的方法
     * 
     * @see com.comtop.cip.jodd.proxetta.ProxyAdvice#execute()
     * @throws Exception 业务异常
     * @return Object result
     */
    @Override
    public Object execute() throws Exception {
        try {
            Object objResult = ProxyTarget.invoke(); // 真正的业务方法
            return objResult;
            
        } catch (Exception ex) {
            LOGGER.error("执行业务方法出错:" + ProxyTarget.targetMethodName(), ex);
            throw ex;
        }
    }
    
}
