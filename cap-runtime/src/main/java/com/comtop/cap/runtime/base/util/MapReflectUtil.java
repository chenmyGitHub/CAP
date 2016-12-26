/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Map反射工具类
 * 
 * 
 * @author 李小芬
 * @since 1.0
 * @version 2015-6-1 李小芬
 */
public class MapReflectUtil {
    
	/** 日志 */
     private final static Logger LOGGER = LoggerFactory.getLogger(MapReflectUtil.class);
	
    /**
     * 将VO反射成map的键值对
     *
     * @param object vo对象
     * @return map对象
     */
    public static Map<String, Object> getVoReflectToMap(Object object) {
        Map<String,Object> params = new HashMap<String,Object>();
        Class objClass = object.getClass();  
        setAllFieldToMap(params,objClass,object);
        return params;
    }

    /**
     * 将VO及继承的VO的属性，属性值设置到Map中
     * @param param 使用的参数Map
     * @param objClass 反射类
     * @param object 实体VO
     */
    @SuppressWarnings("rawtypes")
    public static void setAllFieldToMap(Map<String, Object> param,Class objClass,Object object) {
        if(!objClass.getSuperclass().equals(Object.class)){
            Field[] fields  = objClass.getDeclaredFields();  
            for (Field objField : fields) {
                Object objValue;
                try {
                    objValue = invokeMethod(object, objField.getName());
                    param.put(objField.getName(), objValue);
                    //System.out.println(objField.getName() + "\t" + objValue + "\t" + objField.getType());  
                } catch (Exception e) {
                    e.printStackTrace();
                }  
            }
            setAllFieldToMap(param,objClass.getSuperclass(),object);  
         }  
    }
    
    /** 
     * 获得对象属性的值 
     * @param owner  对象本身
     * @param attriName  属性名称
     * @return Object 属性值
     * @throws Exception  转换异常
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    private static Object invokeMethod(Object owner, String attriName) throws Exception {  
        Class ownerClass = owner.getClass();  
        String strMethodName = attriName.substring(0, 1).toUpperCase()  
            + attriName.substring(1);  
        Method method = null;  
        try {  
            method = ownerClass.getMethod("get" + strMethodName);  
        } catch (NoSuchMethodException e) {  
        	LOGGER.debug("No such method.", e);
            return "找不到'get" + strMethodName + "' 方法";  
        }  
        return method.invoke(owner);  
    }
    
}
