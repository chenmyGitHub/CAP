/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.info.action;

import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * FIXME 类注释信息
 *
 * @author lizhiyong
 * @since jdk1.6
 * @version 2015年11月26日 lizhiyong
 */
@DataTransferObject
public class ImportResult {
    
    /** 消息 */
    private String message;
    
    /** 导入结果代码 */
    private String code;
    
    /**
     * @return 获取 message属性值
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @param message 设置 message 属性值为参数值 message
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * @return 获取 code属性值
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @param code 设置 code 属性值为参数值 code
     */
    public void setCode(String code) {
        this.code = code;
    }
    
}
