/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.orm;

/**
 * 异常基类
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-14 冯展
 */
public class BaseException extends RuntimeException {
    
    /** 标识 */
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param desc 描述
     * @param ex 基础异常
     */
    public BaseException(String desc, Exception ex) {
        super(ex);
    }
}
