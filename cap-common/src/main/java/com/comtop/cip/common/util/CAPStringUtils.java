/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * 字符串处理工具类 从common-lang-2.6.jar中迁移
 * 
 * @author 李小强
 * @since 1.0
 * @version 2015-7-2 李小强
 */
public final class CAPStringUtils {
    
    /**
     * <p>
     * <code>SOAStringUtils</code> 类中除了构造函数其他的都是静态方法 所以在程序中不应创建这个类的实例 应象这样使用: <code>SOAStringUtils.trim(" foo ");</code> .
     * </p>
     * <p>
     * 默认的构造方法是为了 JavaBean 的操作。
     * </p>
     */
    private CAPStringUtils() {
        super();
    }
    
    /**
     * <p>
     * 检查string是否是空白或者string为null.
     * </p>
     * 
     * <pre>
     * SOAStringUtils.isBlank(null)      = true
     * SOAStringUtils.isBlank(&quot;&quot;)        = true
     * SOAStringUtils.isBlank(&quot; &quot;)       = true
     * SOAStringUtils.isBlank(&quot;bob&quot;)     = false
     * SOAStringUtils.isBlank(&quot;  bob  &quot;) = false
     * </pre>
     * 
     * @param string 需要检查的string, 可能为null
     * @return <code>true</code> 如果string为null或者string中数据为空白
     * @since 2.0
     */
    public static boolean isBlank(String string) {
        int strLen;
        if (string == null || (strLen = string.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * <p>
     * 是否string不为空白，string的长度不为0，string不为null.
     * </p>
     * 
     * <pre>
     * SOAStringUtils.isNotBlank(null)      = false
     * SOAStringUtils.isNotBlank(&quot;&quot;)        = false
     * SOAStringUtils.isNotBlank(&quot; &quot;)       = false
     * SOAStringUtils.isNotBlank(&quot;bob&quot;)     = true
     * SOAStringUtils.isNotBlank(&quot;  bob  &quot;) = true
     * </pre>
     * 
     * @param string 需要检查的string, 可能为null
     * @return <code>true</code> 如果string不为null并且 string不为空白，string的长度不为0.
     * @since 2.0
     */
    public static boolean isNotBlank(String string) {
        return !CAPStringUtils.isBlank(string);
    }
    
    /**
     * <p>
     * 查看字符串中是否只有unicode的数字. 小数点不认为是unicode的数字.
     * </p>
     * <p>
     * <code>null</code> 返回 <code>false</code>. 空的字符串("") 返回 <code>true</code>.
     * </p>
     * 
     * <pre>
     * SOAStringUtils.isNumeric(null)   = false
     * SOAStringUtils.isNumeric(&quot;&quot;)     = true
     * SOAStringUtils.isNumeric(&quot;  &quot;)   = false
     * SOAStringUtils.isNumeric(&quot;123&quot;)  = true
     * SOAStringUtils.isNumeric(&quot;12 3&quot;) = false
     * SOAStringUtils.isNumeric(&quot;ab2c&quot;) = false
     * SOAStringUtils.isNumeric(&quot;12-3&quot;) = false
     * SOAStringUtils.isNumeric(&quot;12.3&quot;) = false
     * </pre>
     * 
     * @param str 要检查的字符串, 可能为null
     * @return <code>true</code> 只有unicode的数字,而且输入不为null
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int iLength = str.length();
        for (int i = 0; i < iLength; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取格式化值,如果为空则返回""，否则返回str.trim()
     * 
     * @param str 源值
     * @return 格式化后的值
     */
    public static String getFormatValue(String str) {
        return isBlank(str) ? "" : str.trim();
    }
    
    /**
     * 获得一个UUID
     * 
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }
    
    /**
     * /**
     * 
     * @description 字符串首字母小写
     * @param str 传入的字符串
     * @return 字符串首字母小写
     */
    public static String uncapitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.toLowerCase().charAt(0) + str.substring(1);
    }
    
    /**
     * 
     * @description 字符串首字母大写
     * @param str 传入的字符串
     * @return 字符串首字母大写
     */
    public static String initials(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.toUpperCase().charAt(0) + str.substring(1);
    }
    
    /**
     * 获取数据主键，生成规则：UUID+4位随机数,32+4共36位
     * 
     * @return UUID+4位随机数
     */
    
    public static String createDataPkId() {
        return getUUID() + String.format("%04d", new Random().nextInt(10000) + 1);
    }
}
