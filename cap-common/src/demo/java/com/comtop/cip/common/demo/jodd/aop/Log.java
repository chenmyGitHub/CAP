/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * 注解类，添加了改注解的类将被记录日志
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */

@Target(METHOD)
@Retention(RUNTIME)
public @interface Log {
    
    /** 提示信息 */
    public String message();
}
