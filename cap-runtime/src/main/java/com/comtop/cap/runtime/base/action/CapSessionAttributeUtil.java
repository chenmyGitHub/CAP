/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.comtop.cip.json.JSON;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.sys.login.util.LoginConstant;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * CAP 代码生成Action基类
 * 
 * @author 罗珍明
 * @version jdk1.6
 * @version 2016-3-24 罗珍明
 */
@DwrProxy
public class CapSessionAttributeUtil {
    
    /**
     * 
     * 将session中的attribute属性转换成json对象
     * 
     * @return 返回的json对象
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static String sessionAttributeToJson() {
        
        Enumeration<String> objEnumAttr = TopServletListener.getSession().getAttributeNames();
        
        Map<String, String> map = new HashMap<String, String>();
        
        while (objEnumAttr.hasMoreElements()) {
            String string = objEnumAttr.nextElement();
            if (LoginConstant.SECURITY_CURR_USER_INFO_KEY.equals(string)) {
                continue;
            }
            Object obj = TopServletListener.getSession().getAttribute(string); // 此方法有可能取到的obj不能使用JSON.toJSONString(obj)而导致报错。
            map.put(string, JSON.toJSONString(obj));
        }
        return JSON.toJSONString(map);
    }
    
    /**
     * 
     * 将session中的attribute属性转换成json对象
     * 
     * @param keys 需要从session中取的keys
     * @return 返回的json对象
     */
    @SuppressWarnings("rawtypes")
    @RemoteMethod
    public static Map sessionAttributeToJson(String keys) {
        Map<String, Object> map = new HashMap<String, Object>();
        
        if (StringUtil.isBlank(keys)) {
            return map;
        }
        // 转换成String[]
        String[] keysArray = keys.split(",");
        
        for (String key : keysArray) {
            Object obj = TopServletListener.getSession().getAttribute(key);
            map.put(key, obj);
        }
        
        return map;
    }
    
}
