
package com.comtop.cap.runtime.base.exception;

import java.text.MessageFormat;

/**
 * 元数据异常
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2014-4-11 李忠文
 */
public class CapMetaDataException extends CapBaseException {
    
    /** 标识 */
    private static final long serialVersionUID = 1L;
    
    /**
     * 构造函数
     * 
     * @param message 消息
     * @param params 参数
     */
    public CapMetaDataException(String message, Object... params) {
        this(message, null, params);
    }
    
    /**
     * 异常构造函数
     * 
     * @param message 异常错误消息
     * @param throwable 引发的异常类
     * @param params 异常信息参数
     */
    public CapMetaDataException(String message, Throwable throwable, Object... params) {
        super(MessageFormat.format(message, params), throwable);
    }
    
}
