/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.test.robot;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2016年6月30日 lizhongwen
 */
public class ResponseData implements Serializable {
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 结果类型 */
    private ResultType type;
    
    /** 消息 */
    private String message;
    
    /** 详细消息 */
    private String detailMessage;
    
    /** 数据 */
    private Object data;
    
    /**
     * 构造函数
     */
    public ResponseData() {
        
    }
    
    /**
     * 构造函数
     * 
     * @param type 结果类型
     */
    public ResponseData(ResultType type) {
        this.type = type;
    }
    
    /**
     * @return 获取 type属性值
     */
    public ResultType getType() {
        return type;
    }
    
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
     * @return 获取 detailMessage属性值
     */
    public String getDetailMessage() {
        return detailMessage;
    }
    
    /**
     * @param detailMessage 设置 detailMessage 属性值为参数值 detailMessage
     */
    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
    
    /**
     * @param type 设置 type 属性值为参数值 type
     */
    public void setType(ResultType type) {
        this.type = type;
    }
    
    /**
     * @return 获取 data属性值
     */
    public Object getData() {
        return data;
    }
    
    /**
     * @param data 设置 data 属性值为参数值 data
     */
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * 结果类型
     *
     * @author lizhongwen
     * @since jdk1.6
     * @version 2016年6月30日 lizhongwen
     */
    public static enum ResultType {
        /** 失败 */
        FAIL,
        /** 成功 */
        SUCCESS;
    }
    
}
