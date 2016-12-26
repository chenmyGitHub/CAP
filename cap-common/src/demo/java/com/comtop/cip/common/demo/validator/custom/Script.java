/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.validator.custom;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.comtop.cip.validator.javax.validation.Constraint;
import com.comtop.cip.validator.javax.validation.Payload;

/**
 * 脚本校验
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-23 郑重
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = ScriptValidator.class)
@Documented
public @interface Script {
    
    /***/
    public String message() default "脚本校验失败";
    
    /***/
    public Class<?>[] groups() default {};
    
    /***/
    public Class<? extends Payload>[] payload() default {};
    
    /***/
    public String script();
    
    /***/
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, TYPE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        
        /***/
        StringLength[] value();
    }
}
