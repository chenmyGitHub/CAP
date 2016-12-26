/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.test.robot;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 请求数据
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2016年6月30日 lizhongwen
 */
public class RequestData implements Serializable {
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 类名称 */
    private String clazz;
    
    /** 方法名称 */
    private String methodName;
    
    /** 参数类型 */
    private List<String> params;
    
    /** 参数值 */
    private List<Object> datas;
    
    /**
     * @return 获取 clazz属性值
     */
    public String getClazz() {
        return clazz;
    }
    
    /**
     * @param clazz 设置 clazz 属性值为参数值 clazz
     */
    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
    
    /**
     * @return 获取 methodName属性值
     */
    public String getMethodName() {
        return methodName;
    }
    
    /**
     * @param methodName 设置 methodName 属性值为参数值 methodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    /**
     * @return 获取 params属性值
     */
    public List<String> getParams() {
        return params;
    }
    
    /**
     * @param params 设置 params 属性值为参数值 params
     */
    public void setParams(List<String> params) {
        this.params = params;
    }
    
    /**
     * @param param 参数名
     */
    public void addParam(String param) {
        if (this.params == null) {
            this.params = new LinkedList<String>();
        }
        this.params.add(param);
    }
    
    /**
     * @return 获取 datas属性值
     */
    public List<Object> getDatas() {
        return datas;
    }
    
    /**
     * @param datas 设置 datas 属性值为参数值 datas
     */
    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
    
    /**
     * @param data 添加参数值
     */
    public void addData(Object data) {
        if (this.datas == null) {
            this.datas = new LinkedList<Object>();
        }
        this.datas.add(data);
    }
}
