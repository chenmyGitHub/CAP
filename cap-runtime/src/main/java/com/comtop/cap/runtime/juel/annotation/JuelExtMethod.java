/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动生成变量的用户扩展函数注解类
 * @author 李小强
 * @since 1.0
 * @version 2016-11-9 李小强
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.METHOD })
public @interface JuelExtMethod {
	/**前缀,可为空*/
	 String prefix() default "";
	/**本地命名,不可为空，不可重复*/
	 String localName() ;
	
}
