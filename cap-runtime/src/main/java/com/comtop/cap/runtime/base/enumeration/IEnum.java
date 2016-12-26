/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.enumeration;

/**
 * 枚举类基本接口。
 * 从枚举类中读取数据字典，必须实现该接口。
 * 
 * <pre>
 * e.g.:
 * SEX_MALE(0, &quot;男&quot;),
 * SEX_FEMALE(1, &quot;女&quot;);
 * </pre>
 * 
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年3月4日 凌晨
 */
public interface IEnum {
    
    /**
     * 获取枚举对象的key属性值
     * 
     * @return key
     */
    String getKey();
    
    /**
     * 获取枚举对象的value属性值
     *
     * @return 枚举对象value
     */
    String getValue();
    
    /**
     * 获取枚举对象的name
     *
     * @return 枚举对象name
     */
    String getName();
    
}
