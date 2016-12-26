/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import com.comtop.cip.jodd.datetime.JDateTime;
import com.comtop.cip.jodd.datetime.Period;

/**
 * jodd 时间日期工具类 例子
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class JDateTimeDemo {
    
    /**
     * 
     * 创建时间日期对象
     * 
     * 各种形式的创建方法
     * 
     */
    public static void createDateTime() {
        // current date and time
        JDateTime jdt = new JDateTime();
        System.out.println(jdt);
        
        // 21st December 2012, midnight
        jdt = new JDateTime(2012, 12, 21);
        System.out.println(jdt);
        
        // current date and time
        jdt = new JDateTime(System.currentTimeMillis());
        System.out.println(jdt);
        
        // 21st Dec. 2012,11:54:22.124
        jdt = new JDateTime(2012, 12, 21, 11, 54, 22, 124);
        System.out.println(jdt);
        
        // -//-
        jdt = new JDateTime("2012-12-21 11:54:22.124");
        System.out.println(jdt);
        
        // 21st Dec. 2012, midnight
        jdt = new JDateTime("12/21/2012", "MM/DD/YYYY");
        System.out.println(jdt);
    }
    
    /**
     * 修改时间日期
     * 
     */
    public static void setDateTime() {
        JDateTime jdt = new JDateTime(); // current date and time
        jdt.set(2012, 12, 21, 11, 54, 22, 124); // 21st December 2012, 11:54:22.124
        jdt.set(2012, 12, 21); // 21st December 2012, midnight
        jdt.setDate(2012, 12, 21); // change just date to 21st Dec. 2012
        jdt.setCurrentTime(); // set current date and time
        jdt.setYear(1973); // change the year
        jdt.setHour(22); // change the hour
        jdt.setTime(18, 00, 12, 853); // change just time
    }
    
    /**
     * 读取时间日期
     * 
     */
    public static void readDateTime() {
        JDateTime jdt = new JDateTime(); // current date and time
        jdt.getDateTimeStamp();
        jdt.getDay();
        jdt.getDayOfMonth();
        jdt.getDayOfWeek();
        jdt.getDayOfYear();
        jdt.getHour();
        
    }
    
    /**
     * 时间日期 加减运算
     * 
     */
    public static void travelingDateTime() {
        JDateTime jdt = new JDateTime(); // current date and time
        jdt.add(1, 2, 3, 4, 5, 6, 7); // add 1 year, 2 months, 3 days, 4 hours...
        jdt.add(4, 2, 0); // add 4 years and 2 months
        jdt.addMonth(-120); // go back 120 months
        jdt.subYear(1); // go back one year
        jdt.addHour(1234); // add 1234 hours
        
    }
    
    /**
     * 比较两个日期之间的时间差
     * 
     */
    public static void compareDateTime() {
        JDateTime jdt1 = new JDateTime(); //
        JDateTime jdt2 = new JDateTime(2015, 01, 01);
        
        Period period = new Period(jdt1, jdt2);
        long days = period.getDays(); // 在天数上的相差
        int hours = period.getHours(); // 在小时数上的相差
        int minutes = period.getMinutes(); // 在分钟数上的相差
        int seconds = period.getMilliseconds(); // 在秒数上的相差
        
        System.out.println("两个时间相差" + days + "天；相差" + hours + "小时;" + "相差" + minutes + "分钟；" + "相差" + seconds + "秒；");
        
    }
    
    /**
     * JDateTime 对象可以轻易地转换为
     * 
     * GregorianCalendar,
     * java.util.Date,
     * java.sql.Date,
     * Timestamp,
     * DateTimeStamp
     * 
     * 等对象
     * 
     */
    public static void convert() {
        JDateTime jdt = new JDateTime();
        jdt.convertToCalendar();
        jdt.convertToDate();
        jdt.convertToSqlDate();
        jdt.convertToSqlTime();
        jdt.convertToSqlTimestamp();
    }
    
    /***
     * 字符串和JDateTime的互相转换
     * 
     */
    public static void format() {
        
        // 转换为字符串
        JDateTime jdt = new JDateTime(1975, 1, 1);
        jdt.toString(); // "1975-01-01 00:00:00.000"
        jdt.toString("YYYY.MM.DD"); // "1975.01.01"
        jdt.toString("MM: MML (MMS)"); // "01: January (Jan)"
        jdt.toString("DD is D: DL (DS)"); // "01 is 3: Wednesday (Wed)"
        
        // 还可以将字符串里的一些特殊字符替换为时间
        JDateTime jdt2 = new JDateTime(1968, 9, 30);
        jdt2.toString("'''' is a sign, W is a week number and 'W' is a letter");
        // "' is a sign, 5 is a week number and W is a letter"
        
        // 字符串转换为日期时间
        JDateTime jdt3 = new JDateTime();
        jdt3.parse("2003-11-24 23:18:38.173");
        jdt3.parse("2003-11-23"); // 2003-11-23 00:00:00.000
        jdt3.parse("01.01.1975", "DD.MM.YYYY"); // 1975-01-01
        jdt3.parse("2001-01-31", "YYYY-MM-***"); // 2001-01-01, since day is not parsed
        
        // 还可以通过实现JdtFormatter接口配合JdtFormat类实现一些自定义转换。
    }
    
    /**
     * 
     * 测试主方法
     * 
     * @param args 参数列表
     */
    public static void main(String[] args) {
        JDateTimeDemo.createDateTime();
        JDateTimeDemo.setDateTime();
        JDateTimeDemo.readDateTime();
        JDateTimeDemo.travelingDateTime();
        JDateTimeDemo.compareDateTime();
        JDateTimeDemo.convert();
        JDateTimeDemo.format();
    }
}
