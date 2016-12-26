/******************************************************************************
 * Copyright (C) 2011 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.spring;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jca.cci.connection.CciLocalTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jdo.JdoTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.jta.WebSphereUowTransactionManager;

import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.top.core.jodd.executor.IBizExecutor;
import com.comtop.top.core.jodd.executor.IDataBaseExecutor;

/**
 * 基于Spring事务连接的数据库操作执行器
 * 
 * @author 罗珍明
 * @since 1.0
 * @version 2014-10-9 罗珍明
 */
public class DataBaseExecutorImpl4Spring implements IDataBaseExecutor {
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseExecutorImpl4Spring.class);
    
    /**
     * springContext
     */
    private ApplicationContext springContext = null;
    
    /**
     * 构造函数
     */
    public DataBaseExecutorImpl4Spring() {
        springContext = BeanContextUtil.getSpringContext();
    }
    
    @Override
    public Object execute(IBizExecutor bizExecutor) {
        return executeSpring(bizExecutor);
        
    }
    
    /**
     * @param bizExecutor 执行器
     * @return 执行结果
     */
    protected Object executeSpring(final IBizExecutor bizExecutor) {
    	DataSource objDataSource = getDataSource();
        Connection objConnection = DataSourceUtils.getConnection(objDataSource);
        try{
        return bizExecutor.execute(objConnection);
        }finally{
        	DataSourceUtils.releaseConnection(objConnection, objDataSource);
        }
        
    }
    
    /**
     * 当前数据执行器是否有事务
     * 
     * @return 是否有事务
     */
    @Override
    public boolean hasTransaction() {
        try {
            Map<String, PlatformTransactionManager> objMmPM = springContext
                .getBeansOfType(PlatformTransactionManager.class);
            if (objMmPM.containsKey("txManager")) {
                return hasTransaction(objMmPM.get("txManager"));
            }
            for (PlatformTransactionManager objMP : objMmPM.values()) {
                if (hasTransaction(objMP)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            LOGGER.error("未开启第三方事务", e);
            return false;
        }
    }
    
    /**
     * 应该事务管理器是否开启了当前事务
     * 
     * @param objMP 参数
     * @return 返回
     */
    private boolean hasTransaction(PlatformTransactionManager objMP) {
        try {
            if (objMP instanceof JtaTransactionManager) {
                return ((JtaTransactionManager) objMP).getUserTransaction().getStatus() != Status.STATUS_NO_TRANSACTION;
            } else if (objMP instanceof org.springframework.orm.hibernate4.HibernateTransactionManager) {
                return ((org.springframework.orm.hibernate4.HibernateTransactionManager) objMP)
                    .isValidateExistingTransaction();
            } else if (objMP instanceof org.springframework.orm.hibernate3.HibernateTransactionManager) {
                return ((org.springframework.orm.hibernate3.HibernateTransactionManager) objMP)
                    .isValidateExistingTransaction();
            } else if (objMP instanceof DataSourceTransactionManager) {
                return ((DataSourceTransactionManager) objMP).isValidateExistingTransaction();
            } else if (objMP instanceof JpaTransactionManager) {
                return ((JpaTransactionManager) objMP).isValidateExistingTransaction();
            } else if (objMP instanceof CciLocalTransactionManager) {
                return ((CciLocalTransactionManager) objMP).isValidateExistingTransaction();
            } else if (objMP instanceof WebSphereUowTransactionManager) {
                return ((WebSphereUowTransactionManager) objMP).getUserTransaction().getStatus() != Status.STATUS_NO_TRANSACTION;
            } else if (objMP instanceof JdoTransactionManager) {
                return ((JdoTransactionManager) objMP).isValidateExistingTransaction();
            } else {
                LOGGER.error("未知的第三方事务管理器");
            }
        } catch (Exception e) {
            LOGGER.error("未开启第三方事务", e);
        }
        return false;
    }
    
    /**
     * 获取数据源
     * 
     * @return 返回数据源
     */
    @Override
    public DataSource getDataSource() {
        return springContext.getBean("dataSource", DataSource.class);
    }
}
