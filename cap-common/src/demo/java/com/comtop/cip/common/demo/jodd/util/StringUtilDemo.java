/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * jodd StringUtil 例子
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class StringUtilDemo {
    
    /**
     * replace（）方法 例子
     * 
     * 替换字符子串
     */
    public static void testReplace() {
        
        String str = "my name is KeShangFu";
        
        // 替换子字符串
        String str1 = StringUtil.replace(str, "KeShangFu", "ksf");
        System.out.println("替换字符子串后：" + str1);
        
        // 同时替换数个字符串
        String str2 = StringUtil.replace(str, new String[] { "is", "KeShangFu" }, new String[] { "are", "ksf" });
        System.out.println("替换数个字符串后：" + str2);
        
        // 替换首个匹配字符串：
        String str3 = StringUtil.replaceFirst(str, "s", "a");
        System.out.println("替换首个匹配字符串后：" + str3);
        
        // 替换最后一个匹配字符串：
        String str4 = StringUtil.replaceLast(str, "m", "a");
        System.out.println("替换最后一个匹配字符串后：" + str4);
        
        // 替换某个字符：
        String str5 = StringUtil.replaceChar(str, 'm', 'n');
        System.out.println("替换某个匹配字符后：" + str5);
        
        // 替换某个字符串，忽略大小写：
        StringUtil.replaceIgnoreCase(str, new String[] { "keshangfu" }, new String[] { "ksf" });
        System.out.println("替换忽略大小写的字符串后：" + str5);
        
        System.out.println("\n");
    }
    
    /**
     * remove（）方法 例子
     * 
     * 删除某些字符串
     * 
     */
    public static void testRemove() {
        String str = " i don't known who you are";
        
        // 删除某个字符子串
        String str1 = StringUtil.remove(str, "who you are");
        System.out.println(str1);
        
        // 删除某个字符
        String str2 = StringUtil.remove(str, 'i');
        System.out.println(str2);
        
        System.out.println("\n");
    }
    
    /**
     * isEmpty（） isNotEmpty（）
     * isBlank() isNotBlank()
     * 
     * 方法 例子
     * 
     * 判断是否为空字符串或空白字符
     * 
     */
    public static void testIsEmpty() {
        String str1 = null;
        String str2 = "";
        String str3 = "  ";
        String str4 = "\t\n";
        
        System.out.println(StringUtil.isEmpty(str1));
        System.out.println(StringUtil.isEmpty(str2));
        System.out.println(StringUtil.isEmpty(str3));
        System.out.println(StringUtil.isEmpty(str4));
        System.out.println("\t\n");
        
        System.out.println(StringUtil.isNotEmpty(str1));
        System.out.println(StringUtil.isNotEmpty(str2));
        System.out.println(StringUtil.isNotEmpty(str3));
        System.out.println(StringUtil.isNotEmpty(str4));
        System.out.println("\t\n");
        
        System.out.println(StringUtil.isBlank(str1));
        System.out.println(StringUtil.isBlank(str2));
        System.out.println(StringUtil.isBlank(str3));
        System.out.println(StringUtil.isBlank(str4));
        System.out.println("\t\n");
        
        System.out.println(StringUtil.isNotBlank(str1));
        System.out.println(StringUtil.isNotBlank(str2));
        System.out.println(StringUtil.isNotBlank(str3));
        System.out.println(StringUtil.isNotBlank(str4));
        System.out.println("\t\n");
        
    }
    
    /**
     * 安全的equals（）方法， null也不会包空指针异常
     * 
     * 还可以比较两个字符串数组是否相等
     * 
     */
    public static void testEquals() {
        // 与null比较
        String str1 = null;
        String str2 = "hello";
        System.out.println(StringUtil.equals(str1, str2));
        
        // 字符串数组比较
        String[] a1 = { "aa", "bb", "cc" };
        String[] a2 = { "aa", "bb", "cc" };
        System.out.println("字符串数组a1与a2是否相等:" + StringUtil.equals(a1, a2));
        
        System.out.println("\t\n");
    }
    
    /**
     * capitalize() 和 uncapitalize()方法
     * 
     * 将字符串的首字母变为大写字母
     * 
     */
    public static void testCapitalize() {
        
        String str = "hello";
        
        // 首字母变为大写字母
        String str1 = StringUtil.capitalize(str);
        System.out.println("首字母变为大写字母后：" + str1);
        
        // 首字母变为小写字母
        String str2 = StringUtil.uncapitalize(str1);
        System.out.println("首字母变为小写字母后：" + str2);
        
        System.out.println("\t\n");
    }
    
    /**
     * split() 和 splitc()方法
     * 
     * 将字符串依照分隔符分割为字符数组
     * 该方法比jdk的按正则分割速度快
     * 
     */
    public static void testSplit() {
        
        String str = "what is you name";
        String[] result = StringUtil.split(str, " ");
        
        for (String s : result) {
            System.out.println(s);
        }
        
        System.out.println("\t\n");
        
        String str2 = "my,name,is,ksf";
        String[] result2 = StringUtil.splitc(str2, ',');
        for (String s : result2) {
            System.out.println(s);
        }
        
        System.out.println("\t\n");
    }
    
    /**
     * indexOf（）方法
     * 查找某个给定字符串是否包含 字符串数组中的其中一个字符串
     * 返回一个int数组
     * int[0],返回包含的字符串在字符数组的位置。
     * int[1],返回包含的字符在给定字符串中的位置。
     * 
     */
    public static void testIndexOf() {
        
        String[] strArr = { "zz", "ksf", "LZW", "wxb" };
        
        // 查找某个给定字符串是否包含 字符串数组中的其中一个字符串
        int[] indexs = StringUtil.indexOf("i am ksf", strArr);
        if (indexs != null) {
            for (int i : indexs) {
                System.out.println(i);
            }
        }
        System.out.println("\t\n");
        
        // 忽略大小写
        int[] indexs2 = StringUtil.indexOfIgnoreCase("i am lzw", strArr);
        if (indexs2 != null) {
            for (int i : indexs2) {
                System.out.println(i);
            }
        }
        
        System.out.println("\t\n");
        
    }
    
    /**
     * trim（）方法
     * 
     * 截取字符串两边的空格
     * 
     */
    public static void testTrim() {
        
        String str = " hello ";
        // 截取左边空格
        System.out.println(StringUtil.trimLeft(str));
        // 截取右边空格
        System.out.println(StringUtil.trimRight(str));
        
        // 一次性截取字符数组里的所有字符
        String[] strArr = { " zz ", " ksf ", "LZW ", " wxb " };
        StringUtil.trimAll(strArr);
        for (String s : strArr) {
            System.out.println(s);
        }
        
        System.out.println("\t\n");
        
    }
    
    /**
     * cut 一系列方法
     * 
     * 根据某些条件截取字符子串
     * 
     */
    public static void testCut() {
        
        String str = "hello world";
        
        System.out.println(StringUtil.cutFromIndexOf(str, 'e'));
        System.out.println(StringUtil.cutFromIndexOf(str, "ll"));
        System.out.println(StringUtil.cutPrefix(str, "hello"));
        System.out.println(StringUtil.cutSuffix(str, "world"));
        System.out.println(StringUtil.cutSurrounding(str, "he", "ld"));
        
        System.out.println("\t\n");
        
    }
    
    /**
     * strip 一系列方法
     * 
     * 删除某些字符
     * 
     */
    public static void testStrip() {
        
        String str = "hello world";
        
        System.out.println(StringUtil.stripChar(str, 'h'));
        System.out.println(StringUtil.stripFromChar(str, 'h'));
        
        System.out.println("\t\n");
        
    }
    
    /**
     * 测试主方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        StringUtilDemo.testReplace();
        StringUtilDemo.testRemove();
        StringUtilDemo.testIsEmpty();
        StringUtilDemo.testEquals();
        StringUtilDemo.testCapitalize();
        StringUtilDemo.testSplit();
        StringUtilDemo.testIndexOf();
        StringUtilDemo.testTrim();
        StringUtilDemo.testCut();
        StringUtilDemo.testStrip();
    }
}
