/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.aop;

import com.comtop.cip.jodd.proxetta.MethodInfo;
import com.comtop.cip.jodd.proxetta.pointcuts.ProxyPointcutSupport;

/**
 * 日志切面 切入点
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class LogProxyPointcut extends ProxyPointcutSupport {
    
    /**
     * 查找所有添加了@Log注解的方法
     * 
     * @see com.comtop.cip.jodd.proxetta.ProxyPointcut#apply(com.comtop.cip.jodd.proxetta.MethodInfo)
     */
    @Override
    public boolean apply(MethodInfo methodInfo) {
        return hasAnnotation(methodInfo, Log.class);
    }
    
}
