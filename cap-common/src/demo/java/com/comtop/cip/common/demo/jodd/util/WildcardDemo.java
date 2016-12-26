/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import com.comtop.cip.jodd.util.Wildcard;

/**
 * 
 * jodd 字符串正则匹配工具类
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-19 柯尚福
 */
public class WildcardDemo {
    
    /**
     * 判断字符串是否匹配某个正则表达式
     * 
     */
    public static void matchString() {
        Wildcard.match("CfgOptions.class", "*C*g*cl*"); // true
        Wildcard.match("CfgOptions.class", "*g*c**s"); // true!
        Wildcard.match("CfgOptions.class", "??gOpti*c?ass"); // true
        Wildcard.match("CfgOpti*class", "*gOpti\\*class"); // true
        Wildcard.match("CfgOptions.class", "C*ti*c?a?*"); // true
    }
    
    /**
     * 判断路径是否匹配某个表达式
     * 
     */
    public static void matchPath() {
        Wildcard.matchPath("/foo/soo/doo/boo", "/**/bo*"); // true
        Wildcard.matchPath("/foo/one/two/three/boo", "**/t?o/**"); // true
    }
    
    /**
     * jodd 字符串正则匹配 工具类 例子
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        
        WildcardDemo.matchString();
        WildcardDemo.matchPath();
        
    }
}
