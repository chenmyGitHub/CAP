/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.ce;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.runtime.base.annotation.AssociateAttribute;
import com.comtop.cap.runtime.base.exception.CapRuntimeException;
import com.comtop.cap.runtime.base.facade.CapBaseFacade;
import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cap.runtime.base.model.CascadeVO;
import com.comtop.cap.runtime.base.util.CapRuntimeConstant;
import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 级联操作执行器
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月29日 龚斌
 */
public class EntityExecutor implements IExecutor {
    
    /** 日志 */
    // private final static Logger LOGGER = LoggerFactory.getLogger(EntityExecutor.class);
    
    /**
     * 级联链表
     */
    private CascadeVO cascadeVO;
    
    /**
     * 实体vo对象
     */
    private CapBaseVO vo;
    
    /**
     * 构造函数
     * 
     * @param cascadeVO cascadeVO
     * @param vo CapBaseVO
     */
    public EntityExecutor(CascadeVO cascadeVO, CapBaseVO vo) {
        super();
        this.cascadeVO = cascadeVO;
        this.vo = vo;
    }
    
    @Override
    public void insert() {
        try {
            Field[] fields = vo.getClass().getDeclaredFields();
            for (Field f : fields) {
                String fieldName = f.getName();
                AssociateAttribute aa = f.getAnnotation(AssociateAttribute.class);
                // 遍历实体属性，找到关联的属性
                if (fieldName.equals(cascadeVO.getEntityAttName()) && aa != null) {
                    // 根据关联属性名，获取关联的实例变量的属性值（可能为VO，也可能是List）
                    Method mGetVO;
                    mGetVO = vo.getClass().getDeclaredMethod("get" + CapRuntimeUtils.firstLetterToUpper(fieldName));
                    mGetVO.setAccessible(true);
                    Object res = mGetVO.invoke(vo);
                    if (res == null) {
                        return;
                    }
                    
                    CapBaseFacade objBaseFacade = null;
                    String tarFiledName = aa.associateFieldName();
                    String strMultiple = aa.multiple();
                    // 继续递归插入
                    List<CascadeVO> lstCascade = cascadeVO.getLstCascadeVO();
                    
                    // 获取级联的目标实体的别名
                    String facadeBeanAliasName = StringUtil.isNotBlank(cascadeVO.getTargetEntityAliasName())
                        ? cascadeVO.getTargetEntityAliasName() : cascadeVO.getEntityName();
                    
                    if (CapRuntimeConstant.ONE_TO_MANY_ASS_TYPE.equals(strMultiple)) { // 1对多
                        List lst = (ArrayList) res;
                        for (Object obj : lst) {
                            // insertEntity(tarFiledName, (CapBaseVO) o);
                            // 获取并设置子实体的关联字段id
                            setAssociateFieldValue(CapRuntimeUtils.getId(vo), tarFiledName, obj);
                            // 递归级联插入
                            objBaseFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils
                                .firstLetterToLower(facadeBeanAliasName) + "Facade");
                            // objBaseFacade = CapRuntimeUtils.getBusinessFacade((CapBaseVO) obj);
                            objBaseFacade.insertCascadeVO((CapBaseVO) obj, lstCascade);
                        }
                    } else if (CapRuntimeConstant.ONE_TO_ONE_ASS_TYPE.equals(strMultiple)) { // 1对1
                        // 递归级联插入
                        // objBaseFacade = CapRuntimeUtils.getBusinessFacade((CapBaseVO) res);
                        objBaseFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils
                            .firstLetterToLower(facadeBeanAliasName) + "Facade");
                        String id = objBaseFacade.insertCascadeVO((CapBaseVO) res, lstCascade);
                        CapRuntimeUtils.setId(res, id);
                        setAssociateFieldValue(id, tarFiledName, vo);
                        
                        // 获取本体的facade
                        if (StringUtil.isNotBlank(cascadeVO.getSourceEntityAliasName())) {
                            objBaseFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils
                                .firstLetterToLower(cascadeVO.getSourceEntityAliasName()) + "Facade");
                        } else {
                            objBaseFacade = CapRuntimeUtils.getBusinessFacade(vo);
                        }
                        // 更新本体VO
                        objBaseFacade.update(vo);
                    } else if (CapRuntimeConstant.MANY_TO_MANY_ASS_TYPE.equals(strMultiple)) { // 多对多的插入
                        String entityId = aa.associateEntityId();
                        String assEntityFullClassName = CapRuntimeUtils.getFullClassNameByEntityId(entityId);
                        
                        List lst = (ArrayList) res;
                        for (Object obj : lst) {
                            CapBaseVO objAssEntityVO;
                            try {
                                objAssEntityVO = (CapBaseVO) Class.forName(assEntityFullClassName).newInstance();
                            } catch (InstantiationException e) {
                                throw new CapRuntimeException("实例化关联类时出错！", e);
                            } catch (ClassNotFoundException e) {
                                throw new CapRuntimeException("实例化关联类时出错！", e);
                            }
                            String[] arrAssFiedName = tarFiledName.split(":");
                            setAssociateFieldValue(CapRuntimeUtils.getId(vo), arrAssFiedName[0], objAssEntityVO);
                            setAssociateFieldValue(CapRuntimeUtils.getId(obj), arrAssFiedName[1], objAssEntityVO);
                            
                            // 多对多级联插入，不再递归
                            String assoFacadeBeanName = StringUtil.isNotBlank(aa.associateEntityAliasName()) ? aa
                                .associateEntityAliasName() : CapRuntimeUtils.getEntityNameByEntityId(entityId);
                            
                            objBaseFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils
                                .firstLetterToLower(assoFacadeBeanName) + "Facade");
                            
                            // 多对多级联插入，不再递归
                            // objBaseFacade = CapRuntimeUtils.getBusinessFacade(objAssEntityVO);
                            objBaseFacade.insert(objAssEntityVO);
                        }
                    } else {
                        throw new CapRuntimeException("无法识别或暂不支持的关联插入类型");
                    }
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (IllegalAccessException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (InvocationTargetException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (SecurityException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (NoSuchMethodException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        }
    }
    
    /**
     * 反射设置VO关联字段id
     * 
     * @param voId voId
     * @param fieldName fieldName
     * @param o o
     * @throws NoSuchMethodException 方法异常
     * @throws IllegalAccessException 访问异常
     * @throws InvocationTargetException 调用异常
     */
    private void setAssociateFieldValue(Object voId, String fieldName, Object o) throws NoSuchMethodException,
        IllegalAccessException, InvocationTargetException {
        Method mSetVO;
        // 反射设置关联字段id（如projectId）
        Class<?>[] args1 = { String.class };
        mSetVO = o.getClass().getDeclaredMethod("set" + CapRuntimeUtils.firstLetterToUpper(fieldName), args1);
        mSetVO.setAccessible(true);
        mSetVO.invoke(o, voId);
    }
    
    @Override
    public void loadById() {
        String entityAliasName = StringUtil.isNotBlank(cascadeVO.getTargetEntityAliasName()) ? cascadeVO
            .getTargetEntityAliasName() : cascadeVO.getEntityName();
        CapBaseFacade objFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils.firstLetterToLower(entityAliasName)
            + "Facade");
        try {
            Field[] fields = vo.getClass().getDeclaredFields();
            for (Field f : fields) {
                String fieldName = f.getName();
                AssociateAttribute aa = f.getAnnotation(AssociateAttribute.class);
                // 遍历实体属性，找到关联的属性
                if (fieldName.equals(cascadeVO.getEntityAttName()) && aa != null) {
                    List<CascadeVO> lstCascade = cascadeVO.getLstCascadeVO();
                    String strMultiple = aa.multiple();
                    String tarFiledName = CapRuntimeUtils.firstLetterToUpper(aa.associateFieldName());
                    if (CapRuntimeConstant.ONE_TO_MANY_ASS_TYPE.equals(strMultiple)) { // 1对多
                        String voId = CapRuntimeUtils.getId(vo);
                        // 获取list的泛型class
                        Type objType = f.getGenericType();
                        ParameterizedType pt = (ParameterizedType) objType;
                        Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                        CapBaseVO objSubVO = null;
                        try {
                            objSubVO = (CapBaseVO) genericClazz.newInstance();
                        } catch (InstantiationException e) {
                            throw new CapRuntimeException("实例化类时出错！", e);
                        }
                        setAssociateFieldValue(voId, tarFiledName, objSubVO);
                        
                        // 调用万能查询方法
                        List res = objFacade.queryVOListByCondition(objSubVO);
                        
                        // 反射设置关联属性的值
                        Method mSetVO;
                        mSetVO = vo.getClass().getDeclaredMethod("set" + CapRuntimeUtils.firstLetterToUpper(fieldName),
                            List.class);
                        mSetVO.setAccessible(true);
                        mSetVO.invoke(vo, res);
                        
                        // 继续递归查询
                        List lst = res;
                        for (Object obj : lst) {
                            // 递归级联查詢
                            objFacade.loadCascadeById((CapBaseVO) obj, lstCascade);
                        }
                    } else if (CapRuntimeConstant.ONE_TO_ONE_ASS_TYPE.equals(strMultiple)
                        || CapRuntimeConstant.MANY_TO_ONE_ASS_TYPE.equals(strMultiple)) { // 1对1或多对1的查询
                        Method mGetVO;
                        // 获取关联字段属性值
                        mGetVO = vo.getClass().getDeclaredMethod(
                            "get" + CapRuntimeUtils.firstLetterToUpper(tarFiledName));
                        mGetVO.setAccessible(true);
                        Object subVOId = mGetVO.invoke(vo);
                        
                        if (subVOId != null) {
                            // 递归级联查詢
                            Object model = objFacade.loadById((String) subVOId);
                            
                            if (model != null) {
                                Method mSetVO;
                                // 反射设置关联属性
                                mSetVO = vo.getClass().getDeclaredMethod(
                                    "set" + CapRuntimeUtils.firstLetterToUpper(fieldName), model.getClass());
                                mSetVO.setAccessible(true);
                                mSetVO.invoke(vo, model);
                                // 继续递归查询
                                objFacade.loadCascadeById((CapBaseVO) model, lstCascade);
                            }
                            
                        }
                    } else if (CapRuntimeConstant.MANY_TO_MANY_ASS_TYPE.equals(strMultiple)) { // 多对多的查询
                        String[] arrAssFiedName = tarFiledName.split(":");
                        // 获取关联中间实体的服务类及VO集合
                        String assEntityId = aa.associateEntityId();
                        Class assEntityClass = null;
                        try {
                            assEntityClass = Class.forName(CapRuntimeUtils.getFullClassNameByEntityId(assEntityId));
                        } catch (ClassNotFoundException e) {
                            throw new CapRuntimeException("找不到中间关联类的class", e);
                        }
                        if (assEntityClass == null) {
                            throw new CapRuntimeException("找不到中间关联类的class");
                        }
                        CapBaseVO objAssVO = null;
                        try {
                            objAssVO = (CapBaseVO) assEntityClass.newInstance();
                        } catch (InstantiationException e) {
                            throw new CapRuntimeException("实例化中间关联类时出错！", e);
                        }
                        setAssociateFieldValue(CapRuntimeUtils.getId(vo), arrAssFiedName[0], objAssVO);
                        
                        // 获取中间关联实体的facade，并查询实体对象集合
                        String assoFacadeBeanName = StringUtil.isNotBlank(aa.associateEntityAliasName()) ? aa
                            .associateEntityAliasName() : CapRuntimeUtils.getEntityNameByEntityId(assEntityId);
                        
                        CapBaseFacade objAssFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils
                            .firstLetterToLower(assoFacadeBeanName) + "Facade");
                        
                        List lstAssRes = objAssFacade.queryVOListByCondition(objAssVO);
                        if (lstAssRes == null) {
                            return;
                        }
                        List<CapBaseVO> lstRes = new ArrayList<CapBaseVO>();
                        Method mGetVO;
                        // 获取关联字段属性值
                        mGetVO = assEntityClass.getDeclaredMethod("get"
                            + CapRuntimeUtils.firstLetterToUpper(arrAssFiedName[1]));
                        mGetVO.setAccessible(true);
                        for (Object obj : lstAssRes) {
                            String assVOId = (String) mGetVO.invoke(obj);
                            CapBaseVO objRes = objFacade.loadById(assVOId);
                            lstRes.add(objRes);
                        }
                        
                        Method mSetVO;
                        // 反射设置关联字段id
                        mSetVO = vo.getClass().getDeclaredMethod("set" + CapRuntimeUtils.firstLetterToUpper(fieldName),
                            List.class);
                        mSetVO.setAccessible(true);
                        mSetVO.invoke(vo, lstRes);
                        
                        // 继续递归级联查询
                        for (CapBaseVO objSubVO : lstRes) {
                            objFacade.loadCascadeById(objSubVO, lstCascade);
                        }
                    } else {
                        throw new CapRuntimeException("无法识别或暂不支持的关联查询类型");
                    }
                    
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (IllegalAccessException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (InvocationTargetException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (SecurityException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (NoSuchMethodException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        }
    }
    
    @Override
    public void delete() {
        String entityAliasName = StringUtil.isNotBlank(cascadeVO.getTargetEntityAliasName()) ? cascadeVO
            .getTargetEntityAliasName() : cascadeVO.getEntityName();
        CapBaseFacade objFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils.firstLetterToLower(entityAliasName)
            + "Facade");
        try {
            Field[] fields = vo.getClass().getDeclaredFields();
            for (Field f : fields) { // 遍历VO的属性
                String fieldName = f.getName();
                AssociateAttribute aa = f.getAnnotation(AssociateAttribute.class);
                // 遍历实体属性，找到关联的属性
                if (fieldName.equals(cascadeVO.getEntityAttName()) && aa != null) {
                    List<CascadeVO> lstCascade = cascadeVO.getLstCascadeVO();
                    String strMultiple = aa.multiple();
                    String tarFiledName = aa.associateFieldName();
                    if (CapRuntimeConstant.ONE_TO_MANY_ASS_TYPE.equals(strMultiple)) { // 1对多
                        String voId = CapRuntimeUtils.getId(vo);
                        // 获取list的泛型class
                        Type objType = f.getGenericType();
                        ParameterizedType pt = (ParameterizedType) objType;
                        Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                        CapBaseVO objSubVO = null;
                        try {
                            objSubVO = (CapBaseVO) genericClazz.newInstance();
                        } catch (InstantiationException e) {
                            throw new CapRuntimeException("实例化类时出错！", e);
                        }
                        setAssociateFieldValue(voId, tarFiledName, objSubVO);
                        // 调用万能查询方法
                        List lstRes = objFacade.queryVOListByCondition(objSubVO);
                        
                        if (lstRes == null) {
                            return;
                        }
                        // objFacade.deleteCascadeList((CapBaseVO[]) lst.toArray(), lstCascade);
                        objFacade.deleteCascadeList(lstRes, lstCascade);
                        
                    } else if (CapRuntimeConstant.ONE_TO_ONE_ASS_TYPE.equals(strMultiple)) { // 1对1
                        Method mGetVO;
                        // 获取关联字段属性值
                        mGetVO = vo.getClass().getDeclaredMethod(
                            "get" + CapRuntimeUtils.firstLetterToUpper(tarFiledName));
                        mGetVO.setAccessible(true);
                        Object subVOId = mGetVO.invoke(vo);
                        
                        if (subVOId != null) {
                            // 递归级联查詢
                            Object model = objFacade.loadById((String) subVOId);
                            if (model != null) {
                                // 继续递归删除
                                objFacade.deleteCascadeVO((CapBaseVO) model, lstCascade);
                            }
                        }
                    } else if (CapRuntimeConstant.MANY_TO_MANY_ASS_TYPE.equals(strMultiple)) { // 多对多
                        // 获取关联中间实体的服务类及VO集合
                        String assEntityId = aa.associateEntityId();
                        Class assEntityClass = null;
                        try {
                            assEntityClass = Class.forName(CapRuntimeUtils.getFullClassNameByEntityId(assEntityId));
                        } catch (ClassNotFoundException e) {
                            throw new CapRuntimeException("找不到中间关联类的class", e);
                        }
                        if (assEntityClass == null) {
                            throw new CapRuntimeException("找不到中间关联类的class");
                        }
                        CapBaseVO objAssVO = null;
                        try {
                            objAssVO = (CapBaseVO) assEntityClass.newInstance();
                        } catch (InstantiationException e) {
                            throw new CapRuntimeException("实例化中间关联类时出错！", e);
                        }
                        String[] arrAssFiedName = tarFiledName.split(":");
                        setAssociateFieldValue(CapRuntimeUtils.getId(vo), arrAssFiedName[0], objAssVO);
                        
                        // 获取中间关联实体的facade，并查询实体对象集合
                        String assoFacadeBeanName = StringUtil.isNotBlank(aa.associateEntityAliasName()) ? aa
                            .associateEntityAliasName() : CapRuntimeUtils.getEntityNameByEntityId(assEntityId);
                        
                        CapBaseFacade objAssFacade = CapRuntimeUtils.getBusinessFacade(CapRuntimeUtils
                            .firstLetterToLower(assoFacadeBeanName) + "Facade");
                        
                        List lstAssRes = objAssFacade.queryVOListByCondition(objAssVO);
                        if (lstAssRes == null) {
                            return;
                        }
                        
                        // 多对多的删除，不再级联往下删除
                        objAssFacade.deleteList(lstAssRes);
                    } else {
                        throw new CapRuntimeException("无法识别或暂不支持的关联删除类型！");
                    }
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (IllegalAccessException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (InvocationTargetException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (SecurityException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        } catch (NoSuchMethodException e) {
            throw new CapRuntimeException("级联操作时发生异常", e);
        }
    }
    
}
