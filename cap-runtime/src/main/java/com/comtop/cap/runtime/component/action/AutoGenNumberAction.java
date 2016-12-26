/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.action;

import java.util.HashMap;
import java.util.Map;

import com.comtop.cap.runtime.component.facade.AutoGenNumberFacade;
import com.comtop.cip.jodd.madvoc.meta.Action;
import com.comtop.cip.jodd.madvoc.meta.In;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;

/**
 * 自动生成业务编码Action
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-28 李忠文
 */
@MadvocAction
public class AutoGenNumberAction {
    
    /** 自动生成编码服务 */
    protected AutoGenNumberFacade service = AppBeanUtil.getBean(AutoGenNumberFacade.class);
    
    /** 表达式 */
    @In
    String expression;
    
    /** 扩展参数 */
    @In
    Map params;
    
    /**
     * 生成编码
     * 
     * @return 编码
     */
    @Action(value = "/cip/genNumber.ac", result = "autoGenNumberResult")
    public Map<String, String> genNumber() {
        Map<String, String> objResult = new HashMap<String, String>();
        objResult.put("code", "204");
        objResult.put("message", "自动生成编码失败！");
        if (StringUtil.isEmpty(expression)) {
            return objResult;
        }
        String objNumber = service.genNumber(expression, params);
        objResult.put("code", "200");
        objResult.put("message", "自动生成编码成功！");
        objResult.put("number", objNumber);
        return objResult;
    }
}
