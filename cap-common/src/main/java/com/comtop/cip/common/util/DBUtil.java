/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库工具类，获取数据库连接，关闭连接等方法
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-3-4 柯尚福
 */
public final class DBUtil {
    
    /** 日志记录器 */
    private static final Logger OBJLOGGER = LoggerFactory.getLogger(DBUtil.class);
    
    /** 私有构造器 **/
    private DBUtil() {
        
    }
    
    /**
     * 获取数据库连接
     * 
     * @param driver 三个月会议决议驱动
     * @param url 连接url
     * @param user 用户名
     * @param password 密码
     * @return 一个连接
     * @throws ClassNotFoundException 类没找到异常
     * @throws SQLException sql异常
     */
    public static Connection getConnection(String driver, String url, String user, String password)
        throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection objConnection = DriverManager.getConnection(url, user, password);
        return objConnection;
    }
    /**
     * 获取数据库连接
     * 
     * @param driver 数据库驱动
     * @param url 连接url
     * @param properties 其它属性
     * 
     * @return 一个连接
     * @throws ClassNotFoundException 类没找到异常
     * @throws SQLException sql异常
     */
    public static Connection getConnection(String driver, String url, Properties properties)
        throws ClassNotFoundException, SQLException {
        
        Class.forName(driver);
        Connection objConnection = DriverManager.getConnection(url, properties);
        
        return objConnection;
        
    }
    
    /**
     * 关闭数据库连接 statement和ResultSet
     * 
     * @param connection 连接
     * @param stmt 语句
     * @param rs 结果集
     */
    public static void closeConnection(Connection connection, Statement stmt, ResultSet rs) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                OBJLOGGER.error("关闭数据库连接出错：", e);
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                OBJLOGGER.error("关闭statement出错：", e);
            }
        }
        
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                OBJLOGGER.error("关闭游标池出错：", e);
            }
        }
    }
    
   
    /***
     * 提交当前连接中的所有事务
     * @param conn 当前连接
     */
    public static void commitTX(Connection conn){
    	if(conn!=null){
    		try {
    			if(conn.isClosed()){
    				OBJLOGGER.error("提交事务出错,当前连接已被关闭");
    			}
    			if(conn.isReadOnly()){
    				OBJLOGGER.error("提交事务出错,当前为只读操作");
    			}
				conn.commit();
			} catch (SQLException e) {
				 OBJLOGGER.error("提交事务出错", e);
			}
    	}
    }
}
