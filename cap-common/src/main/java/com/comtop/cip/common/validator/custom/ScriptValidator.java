/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.validator.custom;

import com.comtop.cip.script.org.mozilla.javascript.Context;
import com.comtop.cip.script.org.mozilla.javascript.Scriptable;
import com.comtop.cip.validator.javax.validation.ConstraintValidator;
import com.comtop.cip.validator.javax.validation.ConstraintValidatorContext;

/**
 * javascript动态脚本校验
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-1-23 郑重 新建
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
    
    /**
     * 初始化
     * 
     * @see com.comtop.cip.validator.javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
     * 
     * @param st 参数
     */
    @Override
    public void initialize(Script st) {
        this.script = st.script();
        this.message = st.message();
    }
    
    /**
     * 校验是否有效
     * 
     * @see com.comtop.cip.validator.javax.validation.ConstraintValidator#isValid(java.lang.Object,
     *      com.comtop.cip.validator.javax.validation.ConstraintValidatorContext)
     * 
     * @param value 参数
     * 
     * @param cv 参数
     * @return bResult
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext cv) {
        boolean bResult = true;
        Context objCx = Context.enter();
        try {
            Scriptable objScope = objCx.initStandardObjects();
            objScope.put("input", objScope, value);
            Object objResult = objCx.evaluateString(objScope, getValidateScript(), null, 1, null);
            bResult = Context.toBoolean(objResult);
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
     * 包装javascript脚本
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
