
package com.comtop.cap.runtime.base.json;

import java.lang.reflect.Type;

import com.comtop.cip.json.parser.DefaultJSONParser;
import com.comtop.cip.json.parser.deserializer.TimestampDeserializer;

/**
 * 重写FastJSON的TimastampDeserializer
 *
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2015年11月25日 凌晨
 */
public class ExtTimestampDeserializer extends TimestampDeserializer {
    
    /** deserializer实例 */
    public final static ExtTimestampDeserializer EXT_TIMESTAMP_INSTANCE = new ExtTimestampDeserializer();
    
    /**
     * 拦截日期的字符串，进行转换成java.sql.Timestamp，转换出错则调用父类方法进行处理
     * 非String类型，直接交给父类进行处理
     * 
     * @see com.comtop.cip.json.parser.deserializer.TimestampDeserializer#cast(com.comtop.cip.json.parser.DefaultJSONParser,
     *      java.lang.reflect.Type, java.lang.Object, java.lang.Object)
     */
    @Override
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        if (val == null) {
            return null;
        }
        
        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            long times = DatePatterner.parse(strVal);
            if (times != -1L) {
                return (T) new java.sql.Timestamp(times);
            }
            
        }
        return (T) super.cast(parser, clazz, fieldName, val);
    }
}
