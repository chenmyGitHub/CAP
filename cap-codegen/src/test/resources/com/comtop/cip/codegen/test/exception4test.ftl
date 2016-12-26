<#include "/copyright.ftl">

package ${exception.packagePath}.exception;

import com.comtop.cip.runtime.base.exception.BaseException;
import java.text.MessageFormat;

/**
 * ${exception.comment}
 * 
 * @author CIP
 * @since 1.0
 * @version ${.now?date} CIP
 */
public class ${exception.exceptionType} extends BaseException {
    
    /**
     * 构造函数
     * 
     * @param message 消息
     * @param params 参数
     */
    public ${exception.exceptionType}(String message, Object... params) {
        this(message, null, params);
    }
    
    /**
     * 异常构造函数
     * 
     * @param message 异常错误消息
     * @param throwable 引发的异常类
     * @param params 异常信息参数
     */
    public ${exception.exceptionType}(String message, Throwable throwable, Object... params) {
        super(MessageFormat.format(message, params), throwable);
    }
}
