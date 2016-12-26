/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

/**
 * 范围对象
 * </pre>
 *
 * @author 李志勇
 * @since jdk1.6
 * @version 2015年5月14日 李志勇
 * @param <T> 泛型参数
 */
public class Range<T> {
    
    /** 原始值 */
    private T[] values;
    
    /** 开始点 */
    private T start;
    
    /** 结束点 */
    private T end;
    
    /**
     * @return 获取原始值
     */
    public T[] getValues() {
        return values;
    }
    
    /**
     * @param values 设置原始值
     */
    public void setValues(T[] values) {
        this.values = values;
        if (values == null || values.length == 0) {
            return;
        }
        start = values[0];
        end = values.length < 2 ? null : values[1];
    }

    
    /**
     * @return 获取 start属性值
     */
    public T getStart() {
        return start;
    }

    
    /**
     * @param start 设置 start 属性值为参数值 start
     */
    public void setStart(T start) {
        this.start = start;
    }

    
    /**
     * @return 获取 end属性值
     */
    public T getEnd() {
        return end;
    }

    
    /**
     * @param end 设置 end 属性值为参数值 end
     */
    public void setEnd(T end) {
        this.end = end;
    }
}
