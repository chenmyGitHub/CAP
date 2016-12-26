/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * baseVO
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-21 柯尚福
 */
public abstract class BaseVO implements Serializable {
    
    /** 基本类型集合 */
    final static Map<String, Class<?>> basicTypeMap = new ConcurrentHashMap<String, Class<?>>(15);
    
    // 初始化基本类型集合
    static {
        basicTypeMap.put("java.lang.String", String.class);
        basicTypeMap.put("java.lang.Boolean", Boolean.class);
        basicTypeMap.put("java.lang.Byte", Byte.class);
        basicTypeMap.put("java.lang.Short", Short.class);
        basicTypeMap.put("java.lang.Integer", Integer.class);
        basicTypeMap.put("java.lang.Long", Long.class);
        basicTypeMap.put("java.lang.Character", Character.class);
        basicTypeMap.put("java.lang.Float", Float.class);
        basicTypeMap.put("java.lang.Double", Double.class);
        basicTypeMap.put("java.util.Date", java.util.Date.class);
        basicTypeMap.put("java.util.Calendar", java.util.Calendar.class);
        basicTypeMap.put("java.sql.Date", java.sql.Date.class);
        basicTypeMap.put("java.sql.Timestamp", java.sql.Timestamp.class);
        basicTypeMap.put("java.sql.Time", java.sql.Time.class);
        
    }
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 页码 */
    private Integer pageNo;
    
    /** 单页大小 */
    private Integer pageSize;
    
    /**
     * @return 获取 id属性值
     */
    public abstract String getId();
    
    /**
     * @param id 设置 id 属性值为参数值 id
     */
    public abstract void setId(String id);
    
    /**
     * @return 获取 pageNo属性值
     */
    public Integer getPageNo() {
        return pageNo;
    }
    
    /**
     * @param pageNo 设置 pageNo 属性值为参数值 pageNo
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    
    /**
     * @return 获取 pageSize属性值
     */
    public Integer getPageSize() {
        return pageSize;
    }
    
    /**
     * @param pageSize 设置 pageSize 属性值为参数值 pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * 通用toString
     * 
     * @return 类信息
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(256);
        sb.append(getClass().getName());
        sb.append("{");
        Field[] fields = getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            String typeName = fields[i].getClass().getName();
            sb.append(fields[i].getName());
            sb.append('=');
            try {
                Object value = fields[i].get(this);
                if (value == null || basicTypeMap.get(typeName) != null) {
                    sb.append(value);
                } else {
                    sb.append(typeName).append("@").append(Integer.toHexString(hashCode()));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        char last = sb.charAt(sb.length() - 1);
        if (last == ',') {
            sb.deleteCharAt(last);
        }
        sb.append("}");
        return sb.toString();
        
    }
}
