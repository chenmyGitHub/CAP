/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.json;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于匹配日期格式
 *
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2015年12月11日 凌晨
 */
public class DatePatterner {
	
	/** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(DatePatterner.class);
    
    /** yyyy */
    private static final Pattern PATTERN_Y = Pattern.compile("\\d{4}");
    
    /** yyyy-MM */
    private static final Pattern PATTERN_YM = Pattern.compile("\\d{4}-\\d{1,2}");
    
    /** yyyy-MM-dd */
    private static final Pattern PATTERN_YMD = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
    
    /** yyyy-MM-dd HH:mm */
    private static final Pattern PATTERN_YMD_HM = Pattern.compile("(\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2})");
    
    /** yyyy-MM-dd HH:mm:ss */
    private static final Pattern PATTERN_YMD_HMS = Pattern
        .compile("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}");
    
    /** yyyy-MM-dd HH:mm:ss.SSS */
    private static final Pattern PATTERN_YMD_HMS_S = Pattern
        .compile("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d+");
    
    /** yyyy/MM/dd */
    private static final Pattern PATTERN_YMD_REV = Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}");
    
    /** 存储所有支持的日期格式 */
    private static final Map<Pattern, String> PATTERN_MAP = new HashMap<Pattern, String>();
    
    // 注册所有支持的日期格式
    static {
        
        PATTERN_MAP.put(PATTERN_Y, "yyyy");
        PATTERN_MAP.put(PATTERN_YM, "yyyy-MM");
        PATTERN_MAP.put(PATTERN_YMD, "yyyy-MM-dd");
        PATTERN_MAP.put(PATTERN_YMD_HM, "yyyy-MM-dd HH:mm");
        PATTERN_MAP.put(PATTERN_YMD_HMS, "yyyy-MM-dd HH:mm:ss");
        PATTERN_MAP.put(PATTERN_YMD_HMS_S, "yyyy-MM-dd HH:mm:ss.SSS");
        PATTERN_MAP.put(PATTERN_YMD_REV, "yyyy/MM/dd");
    }
    
    /**
     * 
     * 通过传进来的日期字符串，自动在所有支持的匹配格式中进行查找。
     *
     * @param value 传过来的日期字符串
     * @return 合适的formatter。若找不到合适的formatter则返回null
     */
    public static DateFormat findMatchedFormatter(String value) {
        if (value == null) {
            return null;
        }
        DateFormat formatter = null;
        Matcher matcher = null;
        for (Pattern pattern : PATTERN_MAP.keySet()) {
            matcher = pattern.matcher(value);
            if (matcher.matches()) {
                formatter = new SimpleDateFormat(PATTERN_MAP.get(pattern));
                formatter.setTimeZone(TimeZone.getDefault());
                return formatter;
            }
        }
        return null;
    }
    
    /**
     * 通过传过来的日期字符串，在全局PATTERN_MAP中查找合适的匹配器，并把日期字符串解析成long类型的毫秒数
     *
     * 
     * @param value 传过来的日期字符串
     * @return 日期的毫秒数。若解析发生异常则返回-1L
     */
    public static long parse(String value) {
        DateFormat formatter = findMatchedFormatter(value);
        if (formatter != null) {
            try {
                java.util.Date date = formatter.parse(value);
                return date.getTime();
            } catch (Exception e) {
            	LOGGER.debug("error", e);
                return -1L;
            }
        }
        try{
        	return Long.parseLong(value);
        }catch(Exception e){
        	LOGGER.debug("error", e);
        }
        return -1L;
    }
    
    /**
     * 
     * 注册日期的格式化。
     *
     * <pre>
     * regex = Pattern.compile(&quot;\\d{4}年\\d{1,2}月\\d{1,2}日&quot;);
     * pattern = &quot;yyyy年MM月dd日&quot;;
     * </pre>
     * 
     * @param regex 日期字符串的匹配的正则表达式
     * @param pattern 描述日期和时间格式的模式
     */
    public static void registFormatter(Pattern regex, String pattern) {
        PATTERN_MAP.put(regex, pattern);
    }
}
