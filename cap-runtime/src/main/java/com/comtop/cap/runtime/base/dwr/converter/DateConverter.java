/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.dwr.converter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.comtop.top.core.util.StringUtil;
import comtop.org.directwebremoting.ConversionException;
import comtop.org.directwebremoting.extend.InboundVariable;

/**
 * DWR时间转换器
 * 
 * @author lizhiyong
 * @since jdk1.6
 * @version 2015年6月2日 lizhiyong
 */
public class DateConverter extends comtop.org.directwebremoting.convert.DateConverter {
    
    /** 格式化 */
    private final Map<Pattern, String> patternMap;
    
    /** 格式化 */
    private final List<Pattern> patternList = new ArrayList<Pattern>(5);
    
    /**
     * 构造函数
     */
    public DateConverter() {
        patternMap = new ConcurrentHashMap<Pattern, String>();
        
        Pattern pattern1 = Pattern.compile("\\d{4}");
        patternMap.put(pattern1, "yyyy");
        
        Pattern pattern2 = Pattern.compile("\\d{4}-\\d{1,2}");
        patternMap.put(pattern2, "yyyy-MM");
        
        Pattern pattern3 = Pattern.compile("(\\d{4}\\-\\d{1,2}\\-\\d{1,2})");
        patternMap.put(pattern3, "yyyy-MM-dd");
        
        Pattern pattern4 = Pattern.compile("(\\d{4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2})");
        patternMap.put(pattern4, "yyyy-MM-dd hh:mm");
        
        Pattern pattern5 = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}");
        patternMap.put(pattern5, "yyyy-MM-dd HH:mm:ss");
        
        Pattern pattern6 = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d+");
        patternMap.put(pattern6, "yyyy-MM-dd HH:mm:ss.SSS");
        
        Pattern pattern7 = Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}");
        patternMap.put(pattern7, "yyyy/MM/dd");
        
        patternList.add(pattern1);
        patternList.add(pattern2);
        patternList.add(pattern3);
        patternList.add(pattern4);
        patternList.add(pattern5);
        patternList.add(pattern6);
        patternList.add(pattern7);
    }
    
    @Override
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException {
        if (data.isNull()) {
            return null;
        }
        String value = data.getValue();
        if (value == null || "".equals(value.trim()) || "null".equalsIgnoreCase(value.trim())) {
            return null;
        }
        // 解决字符串被自动转码导致的问题，在此将转码后的字符串还原。
        if (value.indexOf('%') >= 0) {
            try {
                value = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        // if (StringUtil.isNumeric(value)) {
        // return super.convertInbound(paramType, data);
        // }
        
        String format = getMatchFormat(value);
        if (format == null) {
            if (StringUtil.isNumeric(value)) {
                return super.convertInbound(paramType, data);
            }
            throw new ConversionException(paramType, "不支持的时间格式:" + value);
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(value);
            if (paramType == Date.class) {
                return date;
            } else if (paramType == java.sql.Date.class) {
                return new java.sql.Date(date.getTime());
            } else if (paramType == Time.class) {
                return new Time(date.getTime());
            } else if (paramType == Timestamp.class) {
                return new Timestamp(date.getTime());
            } else if (paramType == Calendar.class) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal;
            }
            throw new ConversionException(paramType, "不支持的时间类型:" + paramType.getName());
        } catch (ParseException e) {
            throw new ConversionException(paramType, "不支持的时间格式:" + value, e);
        }
        
    }
    
    /**
     * 根据值获取合适的格式
     *
     * 
     * @param value 数据
     * @return 格式
     */
    private String getMatchFormat(final String value) {
        Pattern pattern = null;
        for (Iterator<Pattern> iterator = patternList.iterator(); iterator.hasNext();) {
            pattern = iterator.next();
            Matcher matcher = pattern.matcher(value);
            boolean isMatch = matcher.matches();
            if (isMatch) {
                return patternMap.get(pattern);
            }
        }
        return null;
    }
    
}
