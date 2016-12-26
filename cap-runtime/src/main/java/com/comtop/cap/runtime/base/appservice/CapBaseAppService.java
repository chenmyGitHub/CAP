/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.appservice;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.annotation.DefaultValue;
import com.comtop.cap.runtime.base.dao.CapBaseCommonDAO;
import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.JSON;

/**
 * 
 * 服务类基类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月23日 龚斌
 * @param <T> 泛型
 */
public class CapBaseAppService<T extends CapBaseVO> {
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CapBaseAppService.class);
    
    /**
     * CapBaseCommonDAO对象 capBaseCommonDAO
     */
    protected CapBaseCommonDAO<T> capBaseCommonDAO = BeanContextUtil.getBeanFromJoddContext(CapBaseCommonDAO.class);
    
    /**
     * 新增vo
     * 
     * @param vo vo对象
     * @return Id
     */
    public String insert(T vo) {
        this.processDefaultValue(vo);
        return (String) capBaseCommonDAO.insert(vo);
    }
    
    /**
     * 处理实体VO对象中的属性的默认值。
     * 
     * 属性未打默认值注解，则不处理；
     * 有打默认值注解，进一步判断属性是否有值，有值则不处理，无值（null），则取注解中的默认值。
     *
     * @param vo 实体对象
     */
    private void processDefaultValue(T vo) {
        // 取实体vo中的所有声明的属性集合
        Field[] fields = vo.getClass().getDeclaredFields();
        DefaultValue _default = null;
        String fieldType = null;
        
        for (Field field : fields) {
            // 如果是基础类型（int、double、float、long、boolean、char、byte、short）直接跳过
            fieldType = field.getType().getName();
            if ("int".equals(fieldType) || "double".equals(fieldType) || "float".equals(fieldType)
                || "long".equals(fieldType) || "boolean".equals(fieldType) || "char".equals(fieldType)
                || "byte".equals(fieldType) || "short".equals(fieldType)) {
                continue;
            }
            // 取属性的默认值注解（DefaultValue注解）
            _default = field.getAnnotation(DefaultValue.class);
            // 如果注解存在
            if (null != _default) {
                // 取得属性名称
                String fname = field.getName();
                // 把属性名称首字母转换成大写
                String methodTail = fname.substring(0, 1).toUpperCase() + fname.substring(1, fname.length());
                try {
                    String getMethodName = "get" + methodTail;
                    Method getMethod = vo.getClass().getMethod(getMethodName);
                    Object objValue = getMethod.invoke(vo);
                    // 调用getXX()方法，返回有值，则不再调用setXX()方法去设置默认值,执行下次循环
                    if (null != objValue) {
                        continue;
                    }
                    
                    // 调用setXX()方法去设置注解中设置的默认值
                    String setMethodName = "set" + methodTail;
                    Method setMethod = vo.getClass().getMethod(setMethodName, new Class[] { field.getType() });
                    // 根据数据类型转换成对应类型的数据对象
                    Object o = this.convertData(_default.value(), field.getType());
                    // 把默认值设置到vo中
                    setMethod.invoke(vo, new Object[] { o });
                } catch (NoSuchMethodException e) {
                    LOGGER.error("[错误]" + vo.getClass().getName() + "->" + fname + "无setter或getter方法.", e);
                } catch (IllegalAccessException e) {
                    LOGGER.error("[错误]" + vo.getClass().getName() + "->" + fname + "无setter或getter方法.", e);
                } catch (IllegalArgumentException e) {
                    LOGGER.error("[错误]" + vo.getClass().getName() + "->" + fname + "的setter或getter方法调用失败.", e);
                } catch (InvocationTargetException e) {
                    LOGGER.error("[错误]" + vo.getClass().getName() + "->" + fname + "的setter或getter方法调用失败.", e);
                }
            }
        }
    }
    
    /**
     * 根据不同数据类型转换成相应的数据类型的对象
     * 
     * @param value 数据的字符串表示
     * @param clazz 数据的类型
     * @return 转换后的数据对象
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object convertData(String value, Class clazz) {
        Object o = null;
        // 转换特殊字符sysdate为当前系统事件
        if ("sysdate".equalsIgnoreCase(value) && "java.sql.Timestamp".equals(clazz.getName())) {
            o = new java.sql.Timestamp(System.currentTimeMillis());
        } else if ("sysdate".equalsIgnoreCase(value) && "java.sql.Date".equals(clazz.getName())) {
            o = new java.sql.Date(System.currentTimeMillis());
        } else if ("sysdate".equalsIgnoreCase(value) && "java.util.Date".equals(clazz.getName())) {
            o = new java.util.Date(System.currentTimeMillis());
        } else {
            o = JSON.parseObject(value, clazz);
        }
        return o;
    }
    
    /**
     * 新增vo集合
     * 
     * @param voList vo集合
     * @return 新增集合个数
     */
    public int insert(List<T> voList) {
        return capBaseCommonDAO.insert(voList);
    }
    
    /**
     * 更新 vo
     * 
     * @param vo vo对象
     * @return 更新成功与否
     */
    public boolean update(T vo) {
        return capBaseCommonDAO.update(vo);
    }
    
    /**
     * 更新vo集合
     * 
     * @param voList vo集合
     * @return 更新集合个数
     */
    public int update(List<T> voList) {
        return capBaseCommonDAO.update(voList);
    }
    
	/**
	 * 批量更新指定列值方法
	 * 
	 * @param voList
	 *            待修改的model list集合
	 * @param attributes
	 *            需要更新的VO属性值
	 * @return 成功修改的个数
	 */
	public int batchUpdate(List<T> voList, String[] attributes) {
		return capBaseCommonDAO.batchUpdate(voList, attributes);
	}
    
    /**
     * 保存或更新VO，根据ID是否为空
     * 
     * @param vo VO对象
     * @return VO保存后的主键ID
     */
    public String save(T vo) {
        if (StringUtil.isEmpty(CapRuntimeUtils.getId(vo))) {
            String strId = this.insert(vo);
            CapRuntimeUtils.setId(vo, strId);
        } else {
            this.update(vo);
        }
        return CapRuntimeUtils.getId(vo);
    }
    
    /**
     * 删除 vo
     * 
     * @param vo vo对象
     * @return 删除成功与否
     */
    public boolean delete(T vo) {
        return capBaseCommonDAO.delete(vo);
    }
    
    /**
     * 删除vo对象集合
     * 
     * @param voList vo对象集合
     * @return 删除成功与否
     */
    public boolean deleteList(List<T> voList) {
        capBaseCommonDAO.delete(voList);
        return true;
    }
    
    /**
     * 读取VO
     * 
     * @param vo vo对象
     * @return vo
     */
    public T load(T vo) {
        return (T) capBaseCommonDAO.load(vo);
    }
    
    /**
     * 根据vo主键读取vo
     * 
     * @param id vo主键
     * @return vo
     */
    public T loadVOById(String id) {
        return capBaseCommonDAO.getModelById(id, getGenericClass());
    }
    
    /**
     * 获取泛型class
     * 
     * @return class类型
     */
    protected Class getGenericClass() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class clazz = (Class) pt.getActualTypeArguments()[0];
        return clazz;
    }
    
    /**
     * 读取VO列表
     * 
     * @param condition 查询条件
     * @return vo列表
     */
    public List<T> queryVOList(T condition) {
        return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(condition, "query", "List"), condition,
            condition.getPageNo(), condition.getPageSize());
    }
    
    /**
     * 读取VO数据条数
     * 
     * @param condition 查询条件
     * @return vo数据条数
     */
    public int queryVOCount(T condition) {
        return ((Integer) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(condition, "query", "Count"), condition))
            .intValue();
    }
    
    /**
     * 读取VO列表
     * 
     * @param condition 查询条件
     * @return vo列表
     */
    public List<T> queryVOListByCondition(T condition) {
        return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(condition, "query", "ListByCondition"), condition);
    }
    
	/**
	 * 查询VO列表数据不分页查询
	 * 
	 * @param condition
	 *            查询条件
	 * @return vo列表
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryVOListNoPaging(T condition) {
		return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(condition, "query", "ListNoPaging"), condition);
	}
}
