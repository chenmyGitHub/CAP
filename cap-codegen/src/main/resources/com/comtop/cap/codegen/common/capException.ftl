<#include "/common/capCopyright.ftl">

package ${exception.packagePath}.exception;

import com.comtop.cap.runtime.base.exception.CapBaseException;
import java.text.MessageFormat;

/**
 * ${exception.comment}
 * 
 * @author ${exception.author}
 * @since 1.0
 * @version ${.now?date} ${exception.author}
 */
public class ${exception.exceptionType} extends CapBaseException {
    
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
