/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.aop;

import com.comtop.cip.jodd.proxetta.ProxyAdvice;
import com.comtop.cip.jodd.proxetta.ProxyTarget;

/**
 * 
 * 日志切面通知类
 * 需要实现ProxyAdvice 这个接口
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-14 柯尚福
 */
public class LogProxyAdvice implements ProxyAdvice {
    
    @Override
    public Object execute() {
        
        // 执行业务方法之前的操作——》记录日志
        int totalArgs = ProxyTarget.argumentsCount();
        Class target = ProxyTarget.targetClass();
        String methodName = ProxyTarget.targetMethodName();
        System.out.println(">>>" + target.getSimpleName() + '#' + methodName + ':' + totalArgs);
        
        // 真正执行的业务方法
        Object result = ProxyTarget.invoke();
        
        // 执行业务方法之后的操作
        System.out.println("<<<" + result);
        return result;
    }
}
