/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.json;

import java.util.Date;
import java.util.Random;

import com.comtop.cip.json.JSON;
import com.comtop.cip.json.serializer.SerializeConfig;
import com.comtop.cip.json.serializer.SimpleDateFormatSerializer;

/**
 * JSON 测试VO
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-14 郑重
 */
public class BarVO{
    
    /**
     * 序列化配置
     */
    public static SerializeConfig mapping = new SerializeConfig();
    
    /**
     * 
     */
    private String barName;
    
    /**
     * 
     */
    private int barAge;
    
    /**
     * 
     */
    private Date barDate = new Date();
    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
    }
    {
        Random r = new Random();
        barName = "sss_" + String.valueOf(r.nextFloat());
        barAge = r.nextInt();
    }
    
    /**
     * @param args 数组
     * 
     */
    public static void main(String[] args) {
        Object obj = JSON.toJSON(new BarVO());
        System.out.println(obj);
        String x1 = JSON.toJSONString(new BarVO(), true);
        System.out.println(x1);
        String x2 = JSON.toJSONString(new BarVO(), mapping);
        System.out.println(x2);
    }
    
    /**
     * @return 获取 barName属性值
     */
    public String getBarName() {
        return barName;
    }
    
    /**
     * @param barName 设置 barName 属性值为参数值 barName
     */
    public void setBarName(String barName) {
        this.barName = barName;
    }
    
    /**
     * @return 获取 barAge属性值
     */
    public int getBarAge() {
        return barAge;
    }
    
    /**
     * @param barAge 设置 barAge 属性值为参数值 barAge
     */
    public void setBarAge(int barAge) {
        this.barAge = barAge;
    }
    
    /**
     * @return 获取 barDate属性值
     */
    public Date getBarDate() {
        return barDate;
    }
    
    /**
     * @param barDate 设置 barDate 属性值为参数值 barDate
     */
    public void setBarDate(Date barDate) {
        this.barDate = barDate;
    }
    
    @Override
    public String toString() {
        return "Bar{" + "barName='" + barName + '\'' + ", barAge=" + barAge + ", barDate=" + barDate + '}';
    }
}
