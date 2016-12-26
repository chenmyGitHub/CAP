/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.runtime.base.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cip.jodd.db.DbManager;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.corm.session.ExecutorType;
import com.comtop.corm.session.RowBounds;
import com.comtop.corm.session.SqlSession;
import com.comtop.corm.session.SqlSessionFactory;
import com.comtop.top.core.jodd.AppCore;

/**
 * DAO 基类
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-27 冯展
 * @param <T> Model类
 */
public class BaseDAO<T> {
    
    /** 日誌 */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDAO.class);
    
    /**
     * Session工厂
     */
    private static SqlSessionFactory factory;
    
    /**
     * 批量提交大小
     */
    final static int BATCH_SIZE = 1000;
    
    /**
     * grid list常量
     */
    public final static String PAGE_LIST = "list";
    
    /**
     * 总记录数
     */
    public final static String TOTAL_COUNT = "count";
    
    /**
     * 构造函数
     */
    public BaseDAO() {
        factory = AppCore.getInstance().getSqlFactory();
    }
    
    /**
     * 业务回调接口
     * 
     * 
     * @author 冯展
     * @since 1.0
     * @version 2014-4-10 冯展
     */
    private interface BizExecutor {
        
        /**
         * 执行
         * 
         * @param sqlSession SQL session
         * @return result
         */
        Object execute(SqlSession sqlSession);
    }
    
    /**
     * 
     * 获得元数据中的查询定义
     * 
     * 
     * @param sqlName 元数据名称
     * @return 查询定义
     */
    public String loadQuery(String sqlName) {
        // TODO: 等待metadata的相关api定稿
        return null;
    }
    
    /**
     * 
     * 保存或合并记录
     * 
     * @param model model对像
     * @param mappingTables model所映射的表名
     * @return ID
     */
    public Object merge(T model, String... mappingTables) {
        Object objModelId = getId(model);
        boolean bAdd = (objModelId == null);
        return bAdd ? insert(model, mappingTables) : update(model, mappingTables);
    }
    
    /**
     * 
     * 新增Model
     * 
     * @param models 代添加的model
     * @return 成功的数目
     */
    public int insert(final List<T> models) {
        if (models == null) {
            return -1;
        }
        
        if (models.isEmpty()) {
            return 0;
        }
        
        return (Integer) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSession) {
                int iCount = 0;
                String strOperation = "insert";
                String strSQL = getStatementId(models.get(0), strOperation, null);
                for (T objModel : models) {
                    if (StringUtil.isBlank((String) getId(objModel))) {
                        String objId = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                        setId(objModel, objId);
                    }
                    objSession.insert(strSQL, objModel);
                    iCount++;
                    if (iCount % BATCH_SIZE == 0) {
                        objSession.commit(true);
                    }
                }
                objSession.commit(true);
                return iCount;
            }
        }, Boolean.TRUE);
    }
    
    /**
     * 
     * 新增Model
     * 
     * @param model 代添加的model
     * @param mappingTables model所映射的表名
     * @return 新增的id
     */
    public Object insert(final T model, final String... mappingTables) {
        String objId = (String) getId(model);
        
        if (StringUtil.isBlank(objId)) {
            objId = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            setId(model, objId);
        }
        
        execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSession) {
                String strOperation = "insert";
                
                if (mappingTables == null || mappingTables.length == 0) {
                    objSession.insert(getStatementId(model, strOperation, null), model);
                } else {
                    for (String strMappingTable : mappingTables) {
                        objSession.insert(getStatementId(model, strOperation, strMappingTable), model);
                    }
                }
                return null;
            }
        }, Boolean.FALSE);
        return objId;
    }
    
    /**
     * 
     * 自定义新增
     * 
     * @param statementId statement id
     * @param parameter 参数
     * @return 实际新増的行数
     */
    public int insert(final String statementId, final Object parameter) {
        return (Integer) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession sqlSession) {
                return sqlSession.insert(statementId, parameter);
            }
        }, Boolean.FALSE);
    }
    
    /**
     * 
     * 修改model列表
     * 
     * @param models 待修改的model列表
     * @return 成功修改的个数
     */
    public int update(final List<T> models) {
        if (models == null || models.isEmpty()) {
            return 0;
        }
        
        return (Integer) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSession) {
                int iCount = 0;
                String strOperation = "update";
                String strSQL = getStatementId(models.get(0), strOperation, null);
                for (T objModel : models) {
                    objSession.update(strSQL, objModel);
                    iCount++;
                    if (iCount % BATCH_SIZE == 0) {
                        objSession.commit(true);
                    }
                }
                objSession.commit(true);
                return iCount;
            }
        }, Boolean.TRUE);
    }
    
    /**
     * 
     * 修改model
     * 
     * 
     * @param model 待修改的model
     * @param mappingTables model映射的表名
     * @return 成功与否
     */
    public boolean update(final T model, final String... mappingTables) {
        execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                String strOperation = "update";
                
                if (mappingTables == null || mappingTables.length == 0) {
                    objSqlSession.update(getStatementId(model, strOperation, null), model);
                } else {
                    for (String strMappingTable : mappingTables) {
                        objSqlSession.update(getStatementId(model, strOperation, strMappingTable), model);
                    }
                }
                return null;
            }
        }, Boolean.FALSE);
        return true;
    }
    
    /**
     * 
     * 自定义修改
     * 
     * @param statementId statement id
     * @param parameter 参数
     * @return 实际更新的行数
     * 
     */
    public int update(final String statementId, final Object parameter) {
        return (Integer) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                return objSqlSession.update(statementId, parameter);
            }
        }, Boolean.FALSE);
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
    private String getStatementId(Object model, String operation, String tableName) {
        String strSimpleName = model.getClass().getSimpleName();
        if (strSimpleName.endsWith("VO")) {
            strSimpleName = strSimpleName.substring(0, strSimpleName.length() - 2);
        }
        
        return model.getClass().getPackage().getName() + '.' + operation + strSimpleName
            + (tableName == null ? "" : "_" + tableName.toUpperCase());
    }
    
    /**
     * 
     * 保存或修改model列表
     * 
     * 
     * @param modelList model列表
     * @return 修改成功个数
     */
    public int merge(List<T> modelList) {
        for (T objModel : modelList) {
            merge(objModel);
        }
        return modelList.size();
    }
    
    /**
     * 
     * 查询一条model结果
     * 
     * 
     * @param statementId statement id
     * @param queryParamObject 查询参数
     * @return model结果
     */
    public Object selectOne(final String statementId, final Object queryParamObject) {
        return execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                
                return objSqlSession.selectOne(statementId, queryParamObject);
            }
        }, Boolean.FALSE);
    }
    
    /**
     * 
     * 查询列表记录
     * 
     * @param <B> 泛型
     * @param statementId statement id
     * @param queryParamObject 查询参数
     * @return 查询结果
     */
    public <B> List<B> queryList(final String statementId, final Object queryParamObject) {
        return (List<B>) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                return objSqlSession.selectList(statementId, queryParamObject);
            }
        }, Boolean.FALSE);
    }
    
    /**
     * 
     * 查询分页结果
     * 
     * @param <B> 泛型
     * @param statementId statement id
     * @param queryParamObject 查询参数
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @return 分页结果
     */
    public <B> List<B> queryList(final String statementId, final Object queryParamObject, final int pageNo,
        final int pageSize) {
        return (List<B>) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                
                int iOffset = (pageNo - 1) * pageSize;
                RowBounds objRowBounds = new RowBounds(iOffset, pageSize);
                return objSqlSession.selectList(statementId, queryParamObject, objRowBounds);
            }
        }, Boolean.FALSE);
    }
    
    /**
     * 
     * 删除列表记录
     * 
     * 
     * @param models 列表记录
     */
    public void delete(final List<T> models) {
        if (models == null || models.isEmpty()) {
            return;
        }
        execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSession) {
                int iCount = 0;
                String strOperation = "delete";
                String strSQL = getStatementId(models.get(0), strOperation, null);
                for (T objModel : models) {
                    objSession.delete(strSQL, objModel);
                    iCount++;
                    if (iCount % BATCH_SIZE == 0) {
                        objSession.commit(true);
                    }
                }
                objSession.commit(true);
                return iCount;
            }
        }, Boolean.TRUE);
    }
    
    /**
     * 
     * 自定义删除
     * 
     * @param statementId statement id
     * @param parameter 参数
     * 
     */
    public void delete(final String statementId, final Object parameter) {
        execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                return objSqlSession.delete(statementId, parameter);
            }
        }, Boolean.FALSE);
    }
    
    /**
     * 
     * 删除单列model
     * 
     * 
     * @param model 待删除model
     * @param mappingTables model所映射的表名
     * @return 成功与否
     */
    public boolean delete(final T model, final String... mappingTables) {
        int count = (Integer) execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                int iRet;
                if (mappingTables == null || mappingTables.length == 0) {
                    iRet = objSqlSession.delete(getStatementId(model, "delete", null), model);
                } else {
                    iRet = 0;
                    for (String strMappingTable : mappingTables) {
                        iRet = objSqlSession.delete(getStatementId(model, "delete", strMappingTable), model);
                    }
                }
                return iRet;
            }
        }, Boolean.FALSE);
        return count == 0;
    }
    
    /**
     * 
     * 按id加载model列表
     * 
     * 
     * @param models model列表
     * @return model列表
     */
    public List<T> load(List<T> models) {
        List<T> lstResult = new ArrayList<T>(models.size());
        for (T objModel : models) {
            T dbModel = load(objModel);
            if (dbModel != null) {
                lstResult.add(dbModel);
            }
        }
        return lstResult;
    }
    
    /**
     * 
     * 加载单列model
     * 
     * 
     * @param model 待加载model
     * @param mappingTables model映射的表名
     * @return 单条model
     */
    @SuppressWarnings("unchecked")
    public T load(final T model, final String... mappingTables) {
        Object result = execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                Object objRet;
                if (mappingTables == null || mappingTables.length == 0) {
                    objRet = objSqlSession.selectOne(getStatementId(model, "read", null), model);
                } else {
                    objRet = null;
                    for (String strMappingTable : mappingTables) {
                        objRet = objSqlSession.selectOne(getStatementId(model, "read", strMappingTable), model);
                    }
                }
                return objRet;
            }
        }, Boolean.FALSE);
        return (T) result;
    }
    
    /**
     * 
     * 在不同的事务环境中运行业务
     * 
     * @param bizExecutor 业务函数
     * @param isBatch 是否批量处理
     * @return 业务结果
     */
    private Object execute(BizExecutor bizExecutor, boolean isBatch) {
        SqlSession objSqlSession = null;
        boolean enableTx = false;
        Object result;
        Connection conn = getConnection();
        // 元数据版本化，是否切换到中心数据库
        try {
            if (hasJoddTx()) {
                if (hasTransaction()) {
                    objSqlSession = factory.openSession(isBatch ? ExecutorType.BATCH : ExecutorType.SIMPLE, conn);
                    enableTx = true;
                } else {
                    // 未激活Jodd事务, 转入Mybatis事务
                    objSqlSession = factory.openSession(isBatch ? ExecutorType.BATCH : ExecutorType.SIMPLE, conn);
                    enableTx = false;
                }
            } else {
                objSqlSession = factory.openSession(isBatch ? ExecutorType.BATCH : ExecutorType.SIMPLE, conn);
                enableTx = false;
            }
            
            result = bizExecutor.execute(objSqlSession);
            
            // 事务激活时, 由容器处理提交
            if (!enableTx && objSqlSession != null) {
                objSqlSession.commit();
            }
            
            return result;
        } finally {
            // 事务激活时, 由容器处理关闭
            if (!enableTx && objSqlSession != null) {
                objSqlSession.close();
            }
            close(conn, null);
        }
    }
    
    /**
     * 
     * 关闭数据库资源
     * 
     * @param conn 数据库连接
     * @param objStmt objStmt
     *
     */
    private void close(Connection conn, Statement objStmt) {
        try {
            if (null != conn && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            LOGGER.error("关闭连接出错", e);
        }
        try {
            if (null != objStmt && !objStmt.isClosed()) {
                objStmt.close();
                objStmt = null;
            }
        } catch (SQLException e) {
            LOGGER.error("关闭Statement出错", e);
        }
    }
    
    /**
     * 获取数据库连接
     * 
     * @param url 数据库地址
     * @param username 用户名
     * @param passwd 密码
     * @param dirver 驱动
     * @return 数据库连接
     */
    public Connection getConnection(String url, String username, String passwd, String dirver) {
        // 创建数据库连接
        Connection objConnection = null;
        try {
            Class.forName(dirver).newInstance();
            objConnection = DriverManager.getConnection(url, username, passwd);
        } catch (InstantiationException e) {
            LOGGER.error("获取连接出错", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("获取连接出错", e);
        } catch (ClassNotFoundException e) {
            LOGGER.error("获取连接出错", e);
        } catch (SQLException e) {
            LOGGER.error("获取连接出错", e);
        }
        return objConnection;
    }
    
    /**
     * 
     * 获得model的id
     * 
     * 
     * @param model model
     * @return id
     */
    private Object getId(Object model) {
        try {
            Method objGetIdMethod = model.getClass().getMethod("getId");
            return objGetIdMethod.invoke(model);
        } catch (Exception ex) {
            LOGGER.error("获取ID属性时出错", ex);
            return null;
        }
    }
    
    /**
     * 设置VOID
     * 
     * @param model model
     * @param uuid id
     */
    private void setId(T model, String uuid) {
        try {
            Method objSetIdMethod = model.getClass().getMethod("setId", String.class);
            objSetIdMethod.invoke(model, uuid);
        } catch (Exception ex) {
            LOGGER.error("设置ID属性时出错", ex);
        }
    }
    
    /**
     * 执行SQL语句
     * 
     * @param sql sql语句
     * @return SQL执行是否成功
     */
    public boolean execute(final String sql) {
        execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                Connection objConn = objSqlSession.getConnection();
                Statement objStmt = null;
                try {
                    objStmt = objConn.createStatement();
                    return objStmt.execute(sql);
                } catch (Exception ex) {
                    LOGGER.debug("error", ex);
                    return null;
                } finally {
                    close(objConn, null);
                    close(null, objStmt);
                    objSqlSession.close();
                }
            }
        }, Boolean.FALSE);
        return true;
    }
    
    /**
     * 执行SQL语句
     * 
     * @param sqls sql语句
     */
    public void executeBatch(final List<String> sqls) {
        execute(new BizExecutor() {
            
            @Override
            public Object execute(SqlSession objSqlSession) {
                Connection objConn = objSqlSession.getConnection();
                Statement objStmt = null;
                try {
                    objStmt = objConn.createStatement();
                    for (String sql : sqls) {
                        objStmt.addBatch(sql);
                    }
                    objStmt.executeBatch();
                } catch (Exception ex) {
                    LOGGER.error("批量执行SQL语句时出错", ex);
                } finally {
                    close(objConn, null);
                    close(null, objStmt);
                    objSqlSession.close();
                }
                return null;
            }
        }, Boolean.TRUE);
    }
    
    /**
     * 获取数据库连接
     * 
     * @return 数据库连接
     */
    public Connection getConnection() {
        Connection objConnection = null;
        if (hasTransaction()) {
            objConnection = DbManager.getInstance().getSessionProvider().getDbSession().getConnection();
        } else {
            if (DbManager.getInstance().getConnectionProvider() != null) {
                objConnection = DbManager.getInstance().getConnectionProvider().getConnection();
            } else {
                try {
                    objConnection = factory.getConfiguration().getEnvironment().getDataSource().getConnection();
                } catch (Exception e) {
                    throw new RuntimeException("Get connection failed. ", e);
                }
            }
            
        }
        return objConnection;
    }
    
    /**
     * 是否启动Jodd事务
     * 
     * @return 是否启动Jodd事务
     */
    private boolean hasJoddTx() {
        // return AppCore.getInstance().isEnableJoddTx()
        // || (com.comtop.top.core.jodd.AppCore.getInstance().getTxAdviceManager() != null &&
        // com.comtop.top.core.jodd.AppCore
        // .getInstance().getTxAdviceManager().getTransaction() != null);
        return (com.comtop.top.core.jodd.AppCore.getInstance().getTxAdviceManager() != null && com.comtop.top.core.jodd.AppCore
            .getInstance().getTxAdviceManager().getTransaction() != null);
    }
    
    /**
     * 是否支持事务
     * 
     * @return 是否支持事务
     */
    private boolean hasTransaction() {
        // boolean bCip = (AppCore.getInstance().getJtxManager() != null && AppCore.getInstance().getJtxManager()
        // .getTransaction() != null) ? true : false;
        // boolean bTop = (com.comtop.top.core.jodd.AppCore.getInstance().getTxAdviceManager() != null &&
        // com.comtop.top.core.jodd.AppCore
        // .getInstance().getTxAdviceManager().getTransaction() != null) ? true : false;
        // return bCip || bTop;
        boolean bTop = (com.comtop.top.core.jodd.AppCore.getInstance().getTxAdviceManager() != null && com.comtop.top.core.jodd.AppCore
            .getInstance().getTxAdviceManager().getTransaction() != null) ? true : false;
        return bTop;
    }
    
    /**
     * 获取数据库类型字符串
     * 
     * @return 数据库类型字符串
     */
    public String getDataBaseType() {
        return factory.getConfiguration().getVariables().getProperty("dialect");
    }
}
