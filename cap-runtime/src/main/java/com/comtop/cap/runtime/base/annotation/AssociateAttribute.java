/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联属性注解
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月29日 龚斌
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface AssociateAttribute {
    
    /** 关联类型，如一对多、一对一、多对多等 */
    String multiple();
    
    /** 关联字段名 */
    String associateFieldName();
    
    /** 中间关联实体的别名,只在多对多关联时有效 */
    String associateEntityAliasName() default "";
    
    /** 中间关联实体id,只在多对多关联时有效 */
    String associateEntityId() default "";
}
