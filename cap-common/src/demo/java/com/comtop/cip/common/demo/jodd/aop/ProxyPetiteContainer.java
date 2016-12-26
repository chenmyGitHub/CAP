/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.aop;

import com.comtop.cip.jodd.petite.BeanDefinition;
import com.comtop.cip.jodd.petite.PetiteContainer;
import com.comtop.cip.jodd.petite.PetiteUtil;
import com.comtop.cip.jodd.petite.WiringMode;
import com.comtop.cip.jodd.petite.scope.Scope;
import com.comtop.cip.jodd.proxetta.impl.ProxyProxetta;
import com.comtop.cip.jodd.proxetta.impl.ProxyProxettaBuilder;

/**
 * 
 * 自定义ioc容器，为ioc容器配置切面
 * 
 * 本例子是一个添加了日志切面的ioc容器
 * 该容器在初始化时为 符合切入点条件的类生成代理类
 * 所以从容器中获取到的类实例都是代理之后拥有日志功能的代理类
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class ProxyPetiteContainer extends PetiteContainer {
    
    /** 代理工具类,用于生成代理类 **/
    protected final ProxyProxetta proxetta;
    
    /**
     * 
     * 构造函数
     * 
     * @param proxetta 代理工具类
     */
    public ProxyPetiteContainer(ProxyProxetta proxetta) {
        this.proxetta = proxetta;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param name bean的名称
     * @param type bean的类型
     * @param scopeType 创建实例的方式（单例或多例）
     * @param wiringMode wiringMode
     * @return bean
     */
    @Override
    public BeanDefinition registerPetiteBean(Class type, String name, Class<? extends Scope> scopeType,
        WiringMode wiringMode, boolean define) {
        
        if (name == null) {
            name = PetiteUtil.resolveBeanName(type, false);
        }
        
        ProxyProxettaBuilder builder = proxetta.builder();
        builder.setTarget(type);
        type = builder.define();
        
        return super.registerPetiteBean(type, name, scopeType, wiringMode, false);
    }
}
