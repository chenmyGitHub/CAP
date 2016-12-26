/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.util;

/**
 * 元数据首选项信息存储类
 * 
 * 
 * @author 沈康
 * @since 1.0
 * @version 2014-7-3 沈康
 */
public final class MetadataPreferences {
    
    /** 元数据连接中心数据库初始化 */
    private static MetadataConnection metadataConnection;
    
    /** 本地数据库连接初始化 */
    private static MetadataConnection metadataLocalConnection;
    
    /** SVN连接初始化 */
    private static MetadataSVNModel metadataSVNModel;
    
    /**
     * @return 获取 metadataSVNModel属性值
     */
    public static MetadataSVNModel getMetadataSVNModel() {
        return metadataSVNModel;
    }
    
    /**
     * @param metadataSVNModel 设置 metadataSVNModel 属性值为参数值 metadataSVNModel
     */
    public static void setMetadataSVNModel(MetadataSVNModel metadataSVNModel) {
        MetadataPreferences.metadataSVNModel = metadataSVNModel;
    }
    
    /**
     * @return 获取 metadataLocalConnection属性值
     */
    public static MetadataConnection getMetadataLocalConnection() {
        return metadataLocalConnection;
    }
    
    /**
     * @param metadataLocalConnection 设置 metadataLocalConnection 属性值为参数值 metadataLocalConnection
     */
    public static void setMetadataLocalConnection(MetadataConnection metadataLocalConnection) {
        MetadataPreferences.metadataLocalConnection = metadataLocalConnection;
    }
    
    /**
     * @return 获取 metadataConnection属性值
     */
    public static MetadataConnection getMetadataConnection() {
        return metadataConnection;
    }
    
    /**
     * 设置MetadataConnection
     * 
     * @param metadataConn metadataConnection
     */
    public static void setMetadataConnection(MetadataConnection metadataConn) {
        MetadataPreferences.metadataConnection = metadataConn;
    }
}
