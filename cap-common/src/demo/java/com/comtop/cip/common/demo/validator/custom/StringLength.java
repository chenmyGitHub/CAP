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
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.comtop.cip.validator.javax.validation.Constraint;
import com.comtop.cip.validator.javax.validation.Payload;

/**
 * 字符长度校验注解，汉字算2个字符，英文算1个字符
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-23 郑重
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = StringLengthValidator.class)
@Documented
public @interface StringLength {
    
    /**
     * {value}写法会在校验实现类中被替换成具体值
     * 
     * @return str
     */
    public String message() default "字段值{{value}}字符长度应该在{min}到{max}之间";
    
    /****/
    public Class<?>[] groups() default {};
    
    /****/
    public Class<? extends Payload>[] payload() default {};
    
    /****/
    public int min() default 0;
    
    /****/
    public int max() default 0;
    
    /****/
    @Target({ METHOD, FIELD, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        
        /****/
        StringLength[] value();
    }
}
