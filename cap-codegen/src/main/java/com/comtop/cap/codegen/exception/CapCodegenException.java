/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.exception;

/**
 * 
 * CAP代码生成异常
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年11月17日 龚斌
 */
public class CapCodegenException extends RuntimeException {
    
    /** 版本号 */
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param e bpms异常
     */
    public CapCodegenException(Throwable e) {
        super(e.getMessage(), e);
    }
    
    /**
     * 构造函数
     * 
     * @param msg 消息体
     */
    public CapCodegenException(String msg) {
        super(msg);
    }
    
    /**
     * 构造函数
     * 
     * @param msg 消息体
     * @param e 异常
     */
    public CapCodegenException(String msg, Throwable e) {
        super(msg, e);
    }
}
