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
 * 测试加入了日志切面的ioc容器
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class ProxyPetiteTest {
    
    /**
     * 
     * 测试主方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        
        // 定义日志切面
        ProxyAspect aspect = new ProxyAspect(LogProxyAdvice.class, new LogProxyPointcut());
        ProxyProxetta proxetta = ProxyProxetta.withAspects(aspect);
        
        // 创建ioc容器
        ProxyPetiteContainer petite = new ProxyPetiteContainer(proxetta);
        
        // 注册bean
        petite.registerPetiteBean(SomeService.class, null, null, null, false);
        
        // 使用bean
        SomeService someService = petite.getBean(SomeService.class);
        someService.doSomething();
        
    }
}
