/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.exception;

import java.text.MessageFormat;

/**
 * 
 * 异常基类
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-21 柯尚福
 * @version 2016-4-22 许畅 修改
 */
public class CapBaseException extends RuntimeException {
    
    /** 标识 */
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message 异常信息
     * @param params 异常信息参数
     */
    public CapBaseException(String message, String... params) {
        this(message, null, params);
    }
    
    /**
     * 异常构造函数
     * 
     * @param message 异常错误消息
     * @param throwable 引发的异常类
     * @param params 异常信息参数
     */
    public CapBaseException(String message, Throwable throwable, String... params) {
        super(message==null ? null:MessageFormat.format(message, (Object[]) params), throwable);
    }
    
}
