/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.json;

import java.io.IOException;
import java.lang.reflect.Type;

import com.comtop.cip.json.serializer.JSONSerializer;
import com.comtop.cip.json.serializer.ObjectSerializer;

/**
 * 用户自定义序列化DEMO,把boolean类型序列化为0,1
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-14 郑重
 */
public class CustomFormatSerializer implements ObjectSerializer {
    
    /**
     * 自定义格式转换需要的参数
     */
    private final String pattern;
    
    /**
     * 
     * 构造函数
     * 
     * @param pattern 格式化参数
     */
    public CustomFormatSerializer(String pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.getWriter().writeNull();
            return;
        }
        
        Boolean objBool = (Boolean) object;
        if (objBool) {
            serializer.write("1" + pattern);
        } else {
            serializer.write("0" + pattern);
        }
    }
}
