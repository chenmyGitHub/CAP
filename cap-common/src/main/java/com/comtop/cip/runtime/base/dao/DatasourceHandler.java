/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.runtime.base.dao;

/**
 * 元数据版本化数据同步DB切换处理类
 * 
 * 
 * @author 沈康
 * @since 1.0
 * @version 2014-6-17 沈康
 */
public class DatasourceHandler {
    
    /**
     * 
     * 构造函数
     */
    private DatasourceHandler() {
        
    }
    
    /** DB类型 */
    private static ThreadLocal<String> dataSourceType = new ThreadLocal<String>();
    
    /**
     * 切换为中心数据库 ORACLE
     */
    public static void useCenterDB() {
        dataSourceType.set("centerDB");
    }
    
    /**
     * 切换为本地MySQL
     */
    public static void useLocalDB() {
        dataSourceType.set("localDB");
    }
    
    /**
     * 获取数据库源类型
     * 
     * @return 当前数据库源类型
     */
    public static Object getDataSourceType() {
        return dataSourceType.get();
    }
}
