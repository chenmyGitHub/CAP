/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.comtop.cip.jodd.madvoc.ActionRequest;
import com.comtop.cip.jodd.madvoc.result.ActionResult;
import com.comtop.top.core.util.JsonUtil;

/**
 * FIXME 类注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-28 李忠文
 */
public class AutoGenNumberResult extends ActionResult {
    
    /**
     * result mapping name
     */
    public static final String NAME = "autoGenNumberResult";
    
    /**
     * 构造函数
     */
    public AutoGenNumberResult() {
        super(NAME);
    }
    
    /**
     * 
     * @see com.comtop.cip.jodd.madvoc.result.ActionResult#render(com.comtop.cip.jodd.madvoc.ActionRequest,
     *      java.lang.Object, java.lang.String, java.lang.String)
     */
    @Override
    public void render(ActionRequest actionRequest, Object resultObject, String resultValue, String resultPath)
        throws Exception {
        Map<String, String> objResult = (Map<String, String>) resultObject;
        String strJson = "";
        if (objResult != null && !objResult.isEmpty()) {
            strJson = JsonUtil.objectToJson(objResult);
        }
        HttpServletResponse objResponse = actionRequest.getHttpServletResponse();
        objResponse.setContentType("text/html;charset=UTF-8");
        PrintWriter objWrite = null;
        try {
            objWrite = objResponse.getWriter();
            objWrite.write(strJson);
            objWrite.flush();
        } catch (IOException e) {
            throw new RuntimeException("响应失败。", e);
        } finally {
            if (objWrite != null) {
                objWrite.close();
            }
        }
    }
}
