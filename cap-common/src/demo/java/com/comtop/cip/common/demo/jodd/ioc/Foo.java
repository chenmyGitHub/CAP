/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.ioc;

import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInitMethod;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.petite.scope.ProtoScope;

/**
 * 
 * 一个简单的bean
 * 通过添加@PetiteBean注解来注册成为ioc容器中的bean
 * 通过指定scope来指定bean的创建方式 （单例或多例），
 * ProtoScope.class 为多实例 
 * SingletonScope.class 为单例
 * 
 * 
 * 通过 @PetiteInject 注解来 注入依赖的bean
 * 通过 @PetiteInitMethod 注解来指定bean被创建时的初始化动作
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-14 柯尚福
 */

@PetiteBean(scope = ProtoScope.class)
public class Foo {
    
    /** 通过 @PetiteInject 注解来 注入依赖的bean */
    @PetiteInject
    Boo boo;
    
    /** 通过 @PetiteInitMethod 注解来 指定bean被创建后的初始化操作（可选） */
    @PetiteInitMethod
    public void init() {
        System.out.println("foo is inited");
    }
    
    /**
     * 
     * foo方法
     * 
     */
    public void foo() {
        System.out.println("foo");
        boo.boo();
    }
}
