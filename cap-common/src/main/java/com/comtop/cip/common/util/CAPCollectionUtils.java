/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cip.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CAP文档管理，表达式函数，集合处理工具类
 *
 * @author 李小强
 * @since jdk1.6
 * @version 2015年11月19日 李小强
 */
public class CAPCollectionUtils {
	
	 /**
     * Return <code>true</code> if the supplied Collection is <code>null</code> or empty. Otherwise, return <code>false</code>.
     * 
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty() || collection.size() == 0;
    }
    
    /**
     * Return <code>false</code> if the supplied Collection is <code>null</code> or empty. Otherwise, return <code>true</code>.
     * 
     * @param collection the Collection to check
     * @return whether the given Collection is not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
    
    /**
     * 判断对象数组是否为空，
     * 
     * @param objs 对象数组
     * @return 如果是空（null或 数组的数据为空）,返回true;否则返回false
     */
    public static boolean isEmpty(Object[] objs) {
        if (objs == null || objs.length == 0) {
            return true;
        }
        return false;
    }
    
	  /**
     * 获取两个集合的不同元素
     * 
     * @param collmax
     *            大集合
     * @param collmin
     *            小集合
     * @return 两个集合的不同元素
     */
    public static List<Object> getDiffent(List<Object> collmax, List<Object> collmin) {
        List<Object> csReturn = new ArrayList<Object>();
        Collection<Object> max = collmax;
        Collection<Object> min = collmin;
        // 先比较大小,这样会减少后续map的if判断次数
        if (collmax.size() < collmin.size()) {
            max = collmin;
            min = collmax;
        }
        // 直接指定大小,防止再散列
        Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
        for (Object object : max) {
            map.put(object, 1);
        }
        for (Object object : min) {
            if (map.get(object) != null) {// 如果存在，则作一个标识--2
                map.put(object, 2);
                continue;
            }
            csReturn.add(object);
        }
        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                csReturn.add(entry.getKey());
            }
        }
        return csReturn;
    }
    
    /**
     * 获取两个List的相同元素
     * 
     * @param list1
     *            集合1
     * @param list2
     *            集合2
     * @return 相同元素
     */
    public static List<String> getSameElement2(List<String> list1, List<String> list2) {
        list1.retainAll(list2);
        return list1;
    }
    
    
    /**
     *检查指定的字符串集合中否是有指定的字符串
     * 
     * @param list
     *            定的字符串集合
     * @param str
     *            指定的字符串
     * @return  存在时返回true，否则返回false;当集合或指定的字符串为空时返回false
     */
    public static boolean checkCollectionContains(List<String> list,String str) {
    	if(isEmpty(list)||str==null||str.trim().equals("")){
    		return false;
    	}
    	return list.contains(str);
    }
    
}
