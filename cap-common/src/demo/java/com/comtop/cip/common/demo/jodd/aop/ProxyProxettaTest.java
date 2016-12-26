/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.aop;

import com.comtop.cip.jodd.proxetta.ProxyAspect;
import com.comtop.cip.jodd.proxetta.impl.ProxyProxetta;

/**
 * 
 * 测试AOP
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class ProxyProxettaTest {
    
    /**
     * 
     * 测试aop
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        // 1. 定义日志切面（切入点和通知的集合）
        ProxyAspect aspect = new ProxyAspect(LogProxyAdvice.class, new LogProxyPointcut());
        
        // 2. 配置用于生成代理类的工具类（ProxyProxetta）
        ProxyProxetta proxetta = ProxyProxetta.withAspects(aspect);
        
        // 3. 生成代理类
        // Class someServiceClass = proxetta.builder(SomeService.class).define();
        SomeService someService = (SomeService) proxetta.builder(SomeService.class).newInstance();
        
        // 4. 测试代理之后的方法
        someService.doSomething();
        
    }
    
}
