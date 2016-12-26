/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体别名注解
 * <br>用于工作流业务实体实现类VO
 * <br>如果当前实体名和别名不同则会出现在VO中
 * <br>如果实体名和别名相同则不会出现该注解
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年7月15日 许畅 新建
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EntityAlias {

	/**
	 * 实体别名
	 * 
	 * @return 实体别名
	 */
	String value() default "";

}
