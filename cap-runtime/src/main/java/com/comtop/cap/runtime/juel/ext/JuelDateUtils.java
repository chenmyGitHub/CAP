/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel.ext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.comtop.cap.runtime.juel.annotation.JuelExtMethod;

/**
 * juel扩展序列号管理工具类
 * @author 李小强
 * @since 1.0
 * @version 2016-11-9 李小强
 */
public class JuelDateUtils {
    
    /**
     * 日期格式包含年月日，如：20031001
     * 
     */
    public final static String YEARMOUTHDAY = "yyyyMMdd";
    /**
     * 获取当前时间字符串值
     * 紧凑型日期格式包含年、月、日、小时、分钟、秒,如：20031001102015
     * @param type 输出类型
     * @author 李小强
     * @return 当前时间字符串值,格式：20031001102015
     */
    @JuelExtMethod(localName="now")
    public static String getCurrDateTimeStr(String type) {
    	if(type==null||type.trim().equals("")){
    		type = YEARMOUTHDAY;
    	}
        return dateTimeToString(new Date(), type);
    }
    /**
     * 把包含日期值转换为字符串
     * 
     * @param date 日期（日期+时间）
     * @param type 输出类型
     * @return 字符串
     */
    public static String dateTimeToString(java.util.Date date, String type) {
        String dateString = "";
        if (date == null) {
            dateString = "";
        } else {
            SimpleDateFormat objFormatDate = new SimpleDateFormat(type, Locale.getDefault());
            dateString = objFormatDate.format(date);
        }
        return dateString;
    }
}
