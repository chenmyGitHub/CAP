/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
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
 * 用于实体对象insert时，获取默认值进行保存。
 * 
 * 此注解的value值设置要符合JSON字符串格式。
 * 
 * 特别说明：该注解用于基础类型（如<code>int、double、float、long、boolean、char、byte、short</code>）的属性上无效。
 * 若需使用请把属性声明为基础类型的包装类型。
 * 
 * 
 * <pre>
 * 时间的设置：
 * 1)固定时间：<code>@DefaultValue(value="'1970-01-01 00:00:00'")</code> 
 * 2)系统时间：<code>@DefaultValue(value="sysdate")</code>
 * </pre>
 * 
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年3月25日 凌晨
 */
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface DefaultValue {
    
    /** 值 */
    String value();
}
