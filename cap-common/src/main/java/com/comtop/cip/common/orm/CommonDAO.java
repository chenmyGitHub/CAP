/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.orm;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.corm.io.Resources;
import com.comtop.corm.session.RowBounds;
import com.comtop.corm.session.SqlSession;
import com.comtop.corm.session.SqlSessionFactory;
import com.comtop.corm.session.SqlSessionFactoryBuilder;

/**
 * 通用DAO
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-14 冯展 新建
 * @param <T> Model类
 */
public class CommonDAO<T> {
    
    /** 日志 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CommonDAO.class);
    
    /**
     * Session工厂
     */
    private static SqlSessionFactory factory;
    
    /**
     * 构造函数
     */
    public CommonDAO() {
        Reader objReader = null;
        if (factory == null) {
            try {
                String strFileName = "mybatis-config.xml";
                objReader = Resources.getResourceAsReader(strFileName);
                SqlSessionFactoryBuilder objBuilder = new SqlSessionFactoryBuilder();
                factory = objBuilder.build(objReader);
                
            } catch (Exception e) {
                LOGGER.error("commonDao初始化出错", e);
            } finally {
                if (objReader != null) {
                    try {
                        objReader.close();
                    } catch (IOException e) {
                        LOGGER.error("commonDao初始化出错", e);
                    }
                }
            }
        }
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
        boolean bIsAdd = (objModelId == null);
        return bIsAdd ? insert(model, mappingTables) : update(model, mappingTables);
    }
    
    /**
     * 
     * 新增Model
     * 
     * @param models 代添加的model
     * @return 成功的数目
     */
    public int insert(List<T> models) {
        if (models == null) {
            return -1;
        }
        
        int iCount = 0;
        for (T objModel : models) {
            insert(objModel, new String[] {});
            iCount++;
        }
        return iCount;
    }
    
    /**
     * 
     * 新增Model
     * 
     * @param model 代添加的model
     * @param mappingTables model所映射的表名
     * @return 新增的id
     */
    public Object insert(T model, String... mappingTables) {
        SqlSession objSession = null;
        Object objId = getId(model);
        try {
            if (objId == null) {
                objId = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                setId(model, (String) objId);
            }
            
            objSession = factory.openSession();
            
            String strOperation = "insert";
            
            if (mappingTables == null || mappingTables.length == 0) {
                objSession.insert(getStatementId(model, strOperation, null), model);
            } else {
                for (String strMappingTable : mappingTables) {
                    objSession.insert(getStatementId(model, strOperation, strMappingTable), model);
                }
            }
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        
        return objId;
    }
    
    /**
     * 
     * 修改model列表
     * 
     * @param models 待修改的model列表
     * @return 成功修改的个数
     */
    public int update(List<T> models) {
        if (models == null) {
            return 0;
        }
        
        int iCount = 0;
        for (T objModel : models) {
            update(objModel, new String[] {});
            iCount++;
        }
        return iCount;
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
    public boolean update(T model, String... mappingTables) {
        SqlSession objSession = null;
        try {
            objSession = factory.openSession();
            
            String strOperation = "update";
            
            if (mappingTables == null || mappingTables.length == 0) {
                objSession.update(getStatementId(model, strOperation, null), model);
            } else {
                for (String strMappingTable : mappingTables) {
                    objSession.update(getStatementId(model, strOperation, strMappingTable), model);
                }
            }
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        
        return true;
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
    public Object selectOne(String statementId, Object queryParamObject) {
        SqlSession objSession = null;
        Object objRet;
        try {
            objSession = factory.openSession();
            
            objRet = objSession.selectOne(statementId, queryParamObject);
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        return objRet;
    }
    
    /**
     * 
     * 查询列表记录
     * 
     * 
     * @param statementId statement id
     * @param queryParamObject 查询参数
     * @return 查询结果
     */
    public List<?> queryList(String statementId, Object queryParamObject) {
        List<?> lstRet;
        SqlSession objSession = null;
        try {
            objSession = factory.openSession();
            
            lstRet = objSession.selectList(statementId, queryParamObject);
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        
        return lstRet;
    }
    
    /**
     * 
     * 查询分页结果
     * 
     * 
     * @param statementId statement id
     * @param queryParamObject 查询参数
     * @param pageNo 页码
     * @param pageSize 页面大小
     * @return 分页结果
     */
    public List<?> queryList(String statementId, Object queryParamObject, int pageNo, int pageSize) {
        List<?> lstRet;
        SqlSession objSession = null;
        try {
            objSession = factory.openSession();
            
            int iOffset = (pageNo - 1) * pageSize;
            RowBounds objRowBounds = new RowBounds(iOffset, pageSize);
            lstRet = objSession.selectList(statementId, queryParamObject, objRowBounds);
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        
        return lstRet;
    }
    
    /**
     * 
     * 删除列表记录
     * 
     * 
     * @param list 列表记录
     */
    public void delete(List<T> list) {
        for (T objModel : list) {
            delete(objModel);
        }
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
    public boolean delete(T model, String... mappingTables) {
        int iRet;
        SqlSession objSession = null;
        try {
            objSession = factory.openSession();
            
            if (mappingTables == null || mappingTables.length == 0) {
                iRet = objSession.delete(getStatementId(model, "delete", null), model);
            } else {
                iRet = 0;
                for (String strMappingTable : mappingTables) {
                    iRet = objSession.delete(getStatementId(model, "delete", strMappingTable), model);
                }
            }
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        return iRet == 0;
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
        List<T> lstResults = new ArrayList<T>(models.size());
        for (T objModel : models) {
            T objDbModel = load(objModel);
            if (objDbModel != null) {
                lstResults.add(objDbModel);
            }
        }
        return lstResults;
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
    public T load(T model, String... mappingTables) {
        SqlSession objSession = null;
        T objRet;
        try {
            objSession = factory.openSession();
            
            if (mappingTables == null || mappingTables.length == 0) {
                objRet = (T) objSession.selectOne(getStatementId(model, "read", null), model);
            } else {
                objRet = null;
                for (String strMappingTable : mappingTables) {
                    objRet = (T) objSession.selectOne(getStatementId(model, "read", strMappingTable), model);
                }
            }
            
            objSession.commit();
        } finally {
            if (objSession != null) {
                objSession.close();
            }
        }
        return objRet;
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
            LOGGER.error("获取ID出错", ex);
            return null;
        }
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param model xx
     * @param uuid xx
     */
    private void setId(T model, String uuid) {
        try {
            Method objSetIdMethod = model.getClass().getMethod("setId", String.class);
            objSetIdMethod.invoke(model, uuid);
        } catch (Exception ex) {
            LOGGER.error("设置ID出错", ex);
        }
    }
}
