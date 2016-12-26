/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cap.runtime.base.model.SelectorItemCollection;
import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.corm.session.ExecutorType;
import com.comtop.corm.session.SqlSession;
import com.comtop.top.core.base.dao.CoreDAO;
import com.comtop.top.core.jodd.executor.IBizExecutor;
import com.comtop.top.core.util.DBUtil;
import com.comtop.top.core.util.constant.NumberConstant;

/**
 * CAP公共基本DAO
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月15日 龚斌
 * @version 2015年3月31日 许畅
 * @param <T> 继承
 */
@PetiteBean
public class CapBaseCommonDAO<T extends CapBaseVO> extends CoreDAO {
    
    /** 日志 */
    protected final static Logger LOGGER = LoggerFactory.getLogger(CapBaseCommonDAO.class);
    
    /**
     * 批量提交大小
     */
    protected final static int BATCH_SIZE = 1000;
    
    /**
     * 
     * 新增Model
     * 
     * @param models 代添加的model
     * @return 成功的数目
     */
    @Override
    public int insert(final List models) {
        if (models == null || models.isEmpty()) {
            return NumberConstant.ZERO;
        }
        return (Integer) execute(new IBizExecutor() {
            
            @Override
            public Object execute(Connection connection) {
                int iCount = NumberConstant.ZERO;
                String strOperation = "insert";
                SqlSession objSession = factory.openSession(ExecutorType.BATCH, connection);
                String strSQL = getStatementId(models.get(NumberConstant.ZERO), strOperation, null);
                for (Object o : models) {
                    T objModel = (T) o;
                    if (StringUtil.isBlank((String) getId(objModel))) {
                        String objId = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                        setId(objModel, objId);
                    }
                    objSession.insert(strSQL, objModel);
                    iCount++;
                    if (iCount % BATCH_SIZE == NumberConstant.ZERO) {
                        objSession.commit(true);
                    }
                }
                objSession.commit(true);
                return iCount;
            }
        });
    }
    
    /**
     * 
     * 修改model列表
     * 
     * @param models 待修改的model列表
     * @return 成功修改的个数
     */
    @Override
    public int update(final List models) {
        if (models == null || models.isEmpty()) {
            return NumberConstant.ZERO;
        }
        return (Integer) execute(new IBizExecutor() {
            
            @Override
            public Object execute(Connection connection) {
                int iCount = NumberConstant.ZERO;
                String strOperation = "update";
                SqlSession objSession = factory.openSession(ExecutorType.BATCH, connection);
                String strSQL = getStatementId(models.get(NumberConstant.ZERO), strOperation, null);
                for (Object o : models) {
                    T objModel = (T) o;
                    objSession.update(strSQL, objModel);
                    iCount++;
                    if (iCount % BATCH_SIZE == NumberConstant.ZERO) {
                        objSession.commit(true);
                    }
                }
                objSession.commit(true);
                return iCount;
            }
        });
    }
    
    /**
     * 批量更新指定列方法
     * 
     * @param models
     *            待修改的model list集合
     * @param selector
     *            需要更新的列(将需要更新的vo值add进去即可)
     * @return count 成功修改的个数
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected int batchUpdate(final List models, SelectorItemCollection selector) {
        if (models == null || models.isEmpty()) {
            return NumberConstant.ZERO;
        }
        
        int count = NumberConstant.ZERO;
        List<String> sqls = new ArrayList<String>();
        
        for (Object o : models) {
            T model = (T) o;
            if (count > 0 && count % BATCH_SIZE == NumberConstant.ZERO) {
                // 每一千条执行一次批量提交
                executeBatch(sqls);
                sqls = new ArrayList<String>();
            }
            String sql = selector.toUpdateSQL(model);
            if (StringUtil.isNotEmpty(sql)) {
                sqls.add(sql);
                count++;
            }
        }
        
        // 将1000的余数批量提交
        if (sqls.size() <= BATCH_SIZE) {
            executeBatch(sqls);
        }
        
        return count;
    }
    
    /**
     * 批量更新指定列方法
     * 
     * @param models
     *            待修改的model list集合
     * @param attributes
     *            需要更新的VO属性值
     * @return 成功修改的个数
     */
    @SuppressWarnings("rawtypes")
    public int batchUpdate(final List models, String[] attributes) {
        if (models == null || models.isEmpty()) {
            return NumberConstant.ZERO;
        }
        
        if (attributes == null || attributes.length <= 0) {
            return NumberConstant.ZERO;
        }
        
        SelectorItemCollection selector = new SelectorItemCollection();
        for (String attribute : attributes) {
            selector.add(attribute);
        }
        
        return this.batchUpdate(models, selector);
    }
    
    /**
     * 
     * 批量删除列表记录的接口
     * 
     * @param models 列表记录
     */
    @Override
    public void delete(final List models) {
        if (models == null || models.isEmpty()) {
            return;
        }
        execute(new IBizExecutor() {
            
            @Override
            public Object execute(Connection connection) {
                int iCount = NumberConstant.ZERO;
                String strOperation = "delete";
                SqlSession objSqlSession = factory.openSession(connection);
                String strSQL = getStatementId(models.get(NumberConstant.ZERO), strOperation, null);
                for (Object o : models) {
                    T objModel = (T) o;
                    objSqlSession.delete(strSQL, objModel);
                    iCount++;
                    if (iCount % BATCH_SIZE == NumberConstant.ZERO) {
                        objSqlSession.commit(true);
                    }
                }
                return iCount;
            }
        });
    }
    
    /**
     * 
     * 按mybatis的statement查询
     * 
     * 
     * @param model model名
     * @param operation 操作名
     * @param tableName 表名
     * @return sql
     */
    protected String getStatementId(Object model, String operation, String tableName) {
        String strSimpleName = model.getClass().getSimpleName();
        if (strSimpleName.endsWith("VO")) {
            strSimpleName = strSimpleName.substring(NumberConstant.ZERO, strSimpleName.length() - NumberConstant.TWO);
        }
        
        return model.getClass().getPackage().getName() + '.' + operation + strSimpleName
            + (tableName == null ? "" : "_" + tableName.toUpperCase());
    }
    
    /**
     * 
     * 根据主键id获取model
     * 
     * 
     * @param id model主键值
     * @param voClass model的class类型
     * @return id
     */
    public T getModelById(String id, Class voClass) {
        T instance = null;
        try {
            instance = (T) voClass.newInstance();
            CapRuntimeUtils.setId(instance, id);
            instance = (T) load(instance);
            return instance;
        } catch (InstantiationException e) {
            LOGGER.error("[信息]" + voClass.getName() + "无法创建实例.", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("[信息]" + voClass.getName() + "无法创建实例.", e);
        }
        return instance;
    }
    
    /****
     * 执行存储过程SQL集
     * 
     * @param procedures
     *            需要执行SOA存储过程
     * @return 执行失败的内容，如果执行成功，则返回结果为空
     */
    public String execProcedureSqlList(final List<String> procedures) {
        
        Object msg = execute(new IBizExecutor() {
            
            @Override
            public Object execute(Connection objConn) {
                boolean bHasDBError = false;
                String strResult;
                String strTempSQL;
                for (String strSql : procedures) {
                    strTempSQL = strSql;
                    if (StringUtil.isBlank(strTempSQL)) { // 过滤/n/t等符号
                        continue;
                    }
                    strTempSQL = strTempSQL.trim();
                    strResult = execSingleProcedure(strTempSQL, objConn);
                    if (StringUtil.isNotBlank(strResult)) {
                        LOGGER.error("执行存储过程：{} 失败，\n 失败原因： {}", new Object[] { strTempSQL, strResult });
                        bHasDBError = true;
                    }
                }
                return bHasDBError ? "执行注册服务SQL失败" : "";
            }
        });
        return (String) msg;
    }
    
    /***
     * 执行单条注册SQL
     * 
     * @param procedure 需要执行SOA存储过程
     * @param conn 当前连接对象
     * @return 执行失败的内容，如果执行成功，则返回结果为空
     */
    private String execSingleProcedure(String procedure, Connection conn) {
        Statement objStmt = null;
        try {
            objStmt = conn.createStatement();
            objStmt.execute(procedure);
            return "";
        } catch (SQLException ex) {
            return ex.toString();
        } finally {
            DBUtil.closeConnection(null, objStmt, null);
        }
    }
}
