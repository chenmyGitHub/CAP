/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.util;

/**
 * SVN连接元数据
 * 
 * 
 * @author 沈康
 * @since 1.0
 * @version 2014-7-21 沈康
 */
public class MetadataSVNModel {
    
    /** SVN访问地址 */
    private String url;
    
    /** SVN帐号 */
    private String userName;
    
    /** SVN密码 */
    private String password;
    
    /** 临时工作空间 */
    private String tempWorkSpace;
    
    /**
     * @return 获取 url属性值
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * @param url 设置 url 属性值为参数值 url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * @return 获取 userName属性值
     */
    public String getUserName() {
        return userName;
    }
    
    /**
     * @param userName 设置 userName 属性值为参数值 userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /**
     * @return 获取 password属性值
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password 设置 password 属性值为参数值 password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return 获取 tempWorkSpace属性值
     */
    public String getTempWorkSpace() {
        return tempWorkSpace;
    }
    
    /**
     * @param tempWorkSpace 设置 tempWorkSpace 属性值为参数值 tempWorkSpace
     */
    public void setTempWorkSpace(String tempWorkSpace) {
        this.tempWorkSpace = tempWorkSpace;
    }
    
}
