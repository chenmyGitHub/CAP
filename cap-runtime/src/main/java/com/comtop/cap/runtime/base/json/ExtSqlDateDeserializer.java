/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.json;

import java.lang.reflect.Type;

import com.comtop.cip.json.parser.DefaultJSONParser;
import com.comtop.cip.json.parser.deserializer.SqlDateDeserializer;

/**
 * 重写FastJSON的SqlDateDeserializer
 *
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2015年11月25日 凌晨
 */
public class ExtSqlDateDeserializer extends SqlDateDeserializer {
    
    /** deserializer实例 */
    public final static ExtSqlDateDeserializer EXT_SQLDATE_INSTANCE = new ExtSqlDateDeserializer();
    
    /**
     * 拦截日期的字符串，进行转换成java.sql.Date，转换出错则调用父类方法进行处理
     * 非String类型，直接交给父类进行处理
     * 
     * @see com.comtop.cip.json.parser.deserializer.SqlDateDeserializer#cast(com.comtop.cip.json.parser.DefaultJSONParser,
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
                return (T) new java.sql.Date(times);
            }
        }
        return (T) super.cast(parser, clazz, fieldName, val);
    }
}
