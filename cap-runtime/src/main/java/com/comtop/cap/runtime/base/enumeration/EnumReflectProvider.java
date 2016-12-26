/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.enumeration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 通过枚举类路径反射获取数据字典，枚举类必须实现<code>com.comtop.cap.bm.common.IEum</code>接口。
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年3月4日 凌晨
 */
@DwrProxy
public class EnumReflectProvider {
    
    /**
     * 反射获取枚举类的键值对
     * 
     * @param classpath 需要反射的枚举类的全路径
     * @return 数据值集合
     * @throws ClassNotFoundException 反射异常
     */
    @SuppressWarnings("unchecked")
    @RemoteMethod
    public static List<Map<String, String>> invoke(String classpath) throws ClassNotFoundException {
        List<Map<String, String>> datasource = new ArrayList<Map<String, String>>();
        Class<IEnum> clazz = (Class<IEnum>) Class.forName(classpath);
        Object[] objArray = clazz.getEnumConstants();
        if (objArray == null) {
            throw new RuntimeException(classpath + "不是枚举类,无法解析成数据字典。");
        }
        for (Object item : objArray) {
            IEnum _item = (IEnum) item;
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", _item.getKey());
            map.put("value", _item.getKey());
            map.put("text", _item.getValue());
            map.put("name", _item.getName());
            datasource.add(map);
        }
        return datasource;
    }
    
    /**
     * 通过类路径的数据，一次性获取多个枚举类中的数据集合
     * 
     * @param classpathes 枚举类的类路径数组
     * @return 多个枚举类中的数据集合（返回的map的key是路径）
     * @throws ClassNotFoundException 反射异常
     */
    public static Map<String, List<Map<String, String>>> invoke(String[] classpathes) throws ClassNotFoundException {
        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
        if (classpathes != null && classpathes.length > 0) {
            for (String classpath : classpathes) {
                map.put(classpath, invoke(classpath));
            }
        }
        
        return map;
    }
}
