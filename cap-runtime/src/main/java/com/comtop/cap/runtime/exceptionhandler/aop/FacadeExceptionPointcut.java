/******************************************************************************
 * Copyright (C) 2014  ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.exceptionhandler.aop;

import com.comtop.cip.jodd.proxetta.MethodInfo;
import com.comtop.cip.jodd.proxetta.pointcuts.ProxyPointcutSupport;
import com.comtop.cip.jodd.util.Wildcard;

/**
 * 
 * facade层异常切面
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-25 柯尚福
 */
public class FacadeExceptionPointcut extends ProxyPointcutSupport {
    
    /**
     * 判断方法是否应该被拦截
     * 
     * @see com.comtop.cip.jodd.proxetta.ProxyPointcut#apply(com.comtop.cip.jodd.proxetta.MethodInfo)
     * @param methodInfo MethodInfo
     * @return boolean
     */
    @Override
    public boolean apply(MethodInfo methodInfo) {
        return isPublic(methodInfo) && isTopLevelMethod(methodInfo)
            && matchPackageName(methodInfo, "com.comtop.*.facade") && matchClassName(methodInfo, "*Facade");
    }
    
    /**
     * 
     * 判断包名是否匹配
     * 
     * @param methodInfo 方法的详细信息
     * @param pattern 匹配表达式
     * @return 匹配结果
     */
    public boolean matchPackageName(MethodInfo methodInfo, String pattern) {
        String strPackageName = methodInfo.getClassInfo().getPackage();
        return Wildcard.match(strPackageName, pattern);
    }
    
}
