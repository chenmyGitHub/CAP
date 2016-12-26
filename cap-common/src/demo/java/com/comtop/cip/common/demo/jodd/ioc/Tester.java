/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.ioc;

import com.comtop.cip.jodd.petite.PetiteContainer;

/**
 * 
 * ioc 例子测试
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-18 柯尚福
 */
public class Tester {
    
    /**
     * 测试 jodd 的ioc容器 使用例子
     * 
     * 通过手工的方式注册和使用bean
     */
    public static void testManualIOC() {
        
        IOCByManual iocByManual = new IOCByManual();
        
        // 注册bean
        iocByManual.registBeanByShortType();
        iocByManual.registBeanByFullType();
        iocByManual.registBeanWithScope();
        
        // 使用bean
        iocByManual.getBeanByType();
        iocByManual.getBeanByTypeName();
        iocByManual.getBeanByFullTypeName();
    }
    
    /**
     * 测试 jodd 的ioc容器 使用例子
     * 
     * 通过扫描注解的方式注册和使用bean
     * 
     */
    public static void testAutomaticIOC() {
        // 创建ioc 容器
        PetiteContainer petite = new PetiteContainer();
        
        // 通过扫描注解，注册bean
        IOCAutomatic.configPetiteContainer(petite);
        
        // 使用bean
        Foo foo = petite.getBean(Foo.class);
        foo.foo();
        
    }
    
    /**
     * 
     * 测试ioc例子 主方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        Tester.testManualIOC();
        Tester.testAutomaticIOC();
    }
    
}
