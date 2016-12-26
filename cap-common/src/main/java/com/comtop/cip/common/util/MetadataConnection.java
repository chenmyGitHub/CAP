/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库连接元数据
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2014-3-26 李忠文
 */
public class MetadataConnection {
    
    /** 日志记录 */
    private static final Logger LOGGER = LoggerFactory.getLogger(MetadataConnection.class);
    
    /** 数据库服务器 */
    private String hostName;
    
    /** 数据库端口 */
    private int port;
    
    /** 数据库名称,Oracle数据库时，为SID */
    private String dbName;
    
    /** 用户名 */
    private String userName;
    
    /** 密码 */
    private String password;
    
    /** 数据库类型 ；0：oracle,1:MySQL */
    private int dbType;
    
    /**
     * @return 获取 hostName属性值
     */
    public String getHostName() {
        return hostName;
    }
    
    /**
     * @param hostName 设置 hostName 属性值为参数值 hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    
    /**
     * @return 获取 port属性值
     */
    public int getPort() {
        return port;
    }
    
    /**
     * @param port 设置 port 属性值为参数值 port
     */
    public void setPort(int port) {
        this.port = port;
    }
    
    /**
     * @return 获取 dbName属性值
     */
    public String getDbName() {
        return dbName;
    }
    
    /**
     * @param dbName 设置 dbName 属性值为参数值 dbName
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
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
     * @return 获取 dbType属性值
     */
    public int getDbType() {
        return dbType;
    }
    
    /**
     * @param dbType 设置 dbType 属性值为参数值 dbType
     */
    public void setDbType(int dbType) {
        this.dbType = dbType;
    }
    
    /**
     * 获取数据库Schema
     * 
     * @return 数据库Schema
     */
    public String getSchema() {
        switch (this.dbType) {
            case DatabaseType.TYPE_ORACLE:
                return this.userName;
            case DatabaseType.TYPE_MYSQL: // MYSQL 数据库
                return this.dbName;
            default:
                break;
        }
        return this.userName;
    }
    
    /**
     * 
     * 获取连接URL
     * 
     * @return 数据库连接URL
     */
    public String getURL() {
        switch (dbType) {
            case DatabaseType.TYPE_ORACLE: // oracle 数据库
                return "jdbc:oracle:thin:@" + hostName + ":" + port + ":" + dbName;
            case DatabaseType.TYPE_MYSQL: // MYSQL 数据库
                return "jdbc:mysql://" + hostName + ":" + port + "/" + dbName;
            default:
                return "jdbc:oracle:thin:@" + hostName + ":" + port + ":" + dbName;
        }
    }
    
    /**
     * 获取数据库驱动类名称
     * 
     * @return 数据库驱动类名称
     */
    public String getDriverClassName() {
        switch (dbType) {
            case DatabaseType.TYPE_ORACLE: // oracle 数据库
                return "oracle.jdbc.OracleDriver";
            case DatabaseType.TYPE_MYSQL: // MYSQL 数据库
                return "com.mysql.jdbc.Driver";
            default:
                return "oracle.jdbc.OracleDriver";
        }
    }
    
    /**
     * 获取数据库连接
     * 
     * @return 数据库连接
     */
    public Connection getConnection() {
        Properties objProp = new Properties();
        objProp.put("user", this.userName);
        objProp.put("password", this.password);
        objProp.put("remarksReporting", "true");
        String strURL = this.getURL();
        String strDriver = this.getDriverClassName();
        Connection objConn = null;
        try {
            // 加载数据库驱动
            Class.forName(strDriver);
            objConn = DriverManager.getConnection(strURL, objProp);
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return objConn;
    }
    
}
