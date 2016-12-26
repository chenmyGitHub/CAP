/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.exception;


/**
 * 
 * CAP运行时异常
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月22日 龚斌
 */
public class CapRuntimeException extends CapBaseException {
    
    /** 版本标识 */
    private static final long serialVersionUID = 1207141553210231079L;
    
    /**
     * 构造函数
     * 
     * @param e bpms异常
     */
    public CapRuntimeException(Throwable e) {
        super(e.getMessage(), e);
    }
    
    /**
     * 构造函数
     * 
     * @param msg 消息体
     */
    public CapRuntimeException(String msg) {
        super(msg);
    }
    
    /**
     * 构造函数
     * 
     * @param msg 消息体
     * @param e 异常
     */
    public CapRuntimeException(String msg, Throwable e) {
        super(msg, e);
    }
}
