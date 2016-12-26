/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.validator.custom;

import com.comtop.cip.validator.javax.validation.ConstraintValidator;
import com.comtop.cip.validator.javax.validation.ConstraintValidatorContext;

/**
 * 字符长度校验注解，汉字算2个字符，英文算1个字符
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-1-23 郑重
 */
public class StringLengthValidator implements ConstraintValidator<StringLength, String> {
    
    /**
     * 最小长度
     */
    private int min;
    
    /**
     * 最大长度
     */
    private int max;
    
    /**
     * 错误消息模板
     */
    private String message;
    
    /**
     * (non-Javadoc)
     * 
     * @see com.comtop.cip.validator.javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     * 
     * @param stringLength stringLength
     */
    @Override
    public void initialize(StringLength stringLength) {
        this.message = stringLength.message();
        this.min = stringLength.min();
        this.max = stringLength.max();
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see com.comtop.cip.validator.javax.validation.ConstraintValidator#isValid(java.lang.Object,
     *      com.comtop.cip.validator.javax.validation.ConstraintValidatorContext)
     * 
     * @param value value
     * @param cv ConstraintValidatorContext
     * @return bResult bResult
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext cv) {
        boolean bResult = true;
        int iCount = 0;
        if (value == null) {
            bResult = true;
        } else {
            char[] chC = value.toCharArray();
            for (int i = 0; i < chC.length; i++) {
                if (chC[i] < 299) {
                    iCount++;
                } else {
                    iCount += 2;
                }
            }
            if (max > 0) {
                bResult = iCount <= max && iCount >= min;
            } else {
                bResult = iCount >= min;
            }
        }
        if (!bResult) {
            // 使用具体值替换错误模板中的{value}
            cv.disableDefaultConstraintViolation();
            cv.buildConstraintViolationWithTemplate(message.replace("{value}", value)).addConstraintViolation();
        }
        
        return bResult;
    }
}
