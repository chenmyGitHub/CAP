/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.validator.custom;

import com.comtop.cip.script.org.mozilla.javascript.Context;
import com.comtop.cip.script.org.mozilla.javascript.Scriptable;
import com.comtop.cip.validator.javax.validation.ConstraintValidator;
import com.comtop.cip.validator.javax.validation.ConstraintValidatorContext;

/**
 * 
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-23 郑重
 */
public class ScriptValidator implements ConstraintValidator<Script, Object> {
    
    /**
     * 脚本
     */
    private String script;
    
    /**
     * 错误消息模板
     */
    private String message;
    
    /*
     * (non-Javadoc)
     * 
     * @see com.comtop.cip.validator.javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(Script st) {
        this.script = st.script();
        this.message = st.message();
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.comtop.cip.validator.javax.validation.ConstraintValidator#isValid(java.lang.Object,
     * com.comtop.cip.validator.javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cv) {
        boolean bResult = true;
        Context cx = Context.enter();
        try {
            Scriptable scope = cx.initStandardObjects();
            scope.put("input", scope, value);
            Object result = cx.evaluateString(scope, getValidateScript(), null, 1, null);
            bResult = Context.toBoolean(result);
        } finally {
            Context.exit();
        }
        if (!bResult) {
            // 使用具体值替换错误模板中的{value}
            cv.disableDefaultConstraintViolation();
            cv.buildConstraintViolationWithTemplate(message.replace("{value}", value.toString()))
                .addConstraintViolation();
        }
        return bResult;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @return string
     */
    private String getValidateScript() {
        StringBuilder objScript = new StringBuilder();
        objScript.append("function validate (){ ");
        objScript.append(script);
        objScript.append("}");
        objScript.append("validate();");
        return objScript.toString();
    }
    
}
