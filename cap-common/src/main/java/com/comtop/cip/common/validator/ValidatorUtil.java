/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.validator;

import java.util.Set;

import com.comtop.cip.validator.javax.validation.ConstraintViolation;
import com.comtop.cip.validator.javax.validation.Validation;
import com.comtop.cip.validator.javax.validation.Validator;
import com.comtop.cip.validator.javax.validation.ValidatorFactory;

/**
 * VO校验工具类
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-18 郑重 新建
 */
public final class ValidatorUtil {
    
    /**
     * 校验对象
     */
    private static Validator validator;
    
    static {
        ValidatorFactory objFactory = Validation.buildDefaultValidatorFactory();
        validator = objFactory.getValidator();
    }
    
    /**
     * 构造函数
     */
    private ValidatorUtil() {
        
    }
    
    /**
     * 验证VO对象
     * 
     * @param vo 被校验对象
     * @param <T> 对象类型
     * @return 结果集
     * 
     */
    public static <T> ValidateResult<T> validate(T vo) {
        Set<ConstraintViolation<T>> objConstraintViolations = validator.validate(vo);
        return new ValidateResult<T>(objConstraintViolations);
        
    }
    
    /**
     * 验证VO对象字段值
     * 
     * @param vo 被校验对象
     * @param <T> 对象类型
     * @param name 字段名称
     * @return 结果集
     */
    public static <T> ValidateResult<T> validateFiledValue(T vo, String name) {
        Set<ConstraintViolation<T>> objConstraintViolations = validator.validateProperty(vo, name);
        return new ValidateResult<T>(objConstraintViolations);
    }
    
    /**
     * 根据VO类注解信息验证字段值
     * 
     * @param <T> 对象类型
     * @param cls 被校验类
     * @param name 字段名称
     * @param value 字段值
     * @return 结果集
     */
    public static <T> ValidateResult<T> validateClassFiledValue(Class<T> cls, String name, Object value) {
        Set<ConstraintViolation<T>> objConstraintViolations = validator.validateValue(cls, name, value);
        return new ValidateResult<T>(objConstraintViolations);
    }
}
