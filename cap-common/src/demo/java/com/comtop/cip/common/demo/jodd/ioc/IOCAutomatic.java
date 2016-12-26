/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.ioc;

import com.comtop.cip.jodd.petite.PetiteContainer;
import com.comtop.cip.jodd.petite.config.AutomagicPetiteConfigurator;

/**
 * 
 * ioc 通过扫描添加了注解的类，自动注册bean
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-14 柯尚福
 */
public class IOCAutomatic {
    
    /**
     * 配置ioc 容器
     * 
     * 扫描classpath下的所有类，将加了@PetiteBean注解的类注册到 ioc容器
     * 
     * @param petite ioc容器
     * 
     * 
     */
    public static void configPetiteContainer(PetiteContainer petite) {
        // 下面配置，所有类路径下的类将被扫描，添加了@PetiteBean注解的bean将被注册到ioc容器
        AutomagicPetiteConfigurator petiteConfigurator = new AutomagicPetiteConfigurator();
        petiteConfigurator.configure(petite);
    }
    
}
