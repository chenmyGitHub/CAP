/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.validator;

import java.util.Iterator;
import java.util.Set;

import com.comtop.cip.validator.javax.validation.ConstraintViolation;

/**
 * 校验结果集合
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-18 郑重 新建
 * @param <T> 被校验对象
 */
public class ValidateResult<T> {
    
    /**
     * 原始校验结果集
     */
    private final Set<ConstraintViolation<T>> constraintViolation;
    
    /**
     * 
     * @return 返回原始结果集
     */
    public Set<ConstraintViolation<T>> getConstraintViolation() {
        return constraintViolation;
    }
    
    /**
     * 
     * 构造函数
     * 
     * @param constraintViolation 原始结果集
     */
    public ValidateResult(Set<ConstraintViolation<T>> constraintViolation) {
        this.constraintViolation = constraintViolation;
    }
    
    /**
     * 
     * @return 是否校验成功
     */
    public boolean isOK() {
        return constraintViolation.size() == 0;
    }
    
    /**
     * @return 校验错误提示字符串
     */
    public String getMessageString() {
        StringBuilder objResult = new StringBuilder();
        Iterator<ConstraintViolation<T>> objIterator = constraintViolation.iterator();
        while (objIterator.hasNext()) {
            ConstraintViolation<T> objType = objIterator.next();
            objResult.append(objType.getMessage());
            objResult.append('\n');
        }
        return objResult.toString();
    }
}
