/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.facade.CapBaseFacade;
import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cip.jodd.introspector.ClassDescriptor;
import com.comtop.cip.jodd.introspector.ClassIntrospector;
import com.comtop.cip.jodd.introspector.FieldDescriptor;
import com.comtop.top.core.util.constant.NumberConstant;

/**
 * CAP工具类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月21日 龚斌
 * @version 2016年3月31日 许畅
 */
public final class CapRuntimeUtils {

	/** 日志 */
	private final static Logger LOGGER = LoggerFactory
			.getLogger(CapRuntimeUtils.class);

	/**
	 * 构造函数
	 */
	private CapRuntimeUtils() {
	}

	/**
	 * 
	 * 根据名称获取业务facade
	 * 
	 * @param facadeName
	 *            facadeName
	 * @return BaseFacade
	 */
	public static CapBaseFacade<?> getBusinessFacade(String facadeName) {
		if (facadeName == null || "".equals(facadeName)) {
			return null;
		}
		return (CapBaseFacade) BeanContextUtil.getBean(facadeName);
	}

	/**
	 * 
	 * 根据VO对象获取相对应的业务facade
	 * 
	 * @param model
	 *            业务对象
	 * @return BaseFacade
	 */
	public static CapBaseFacade<?> getBusinessFacade(CapBaseVO model) {
		String strSimpleName = model.getClass().getSimpleName();
		if (strSimpleName.endsWith("VO")) {
			strSimpleName = strSimpleName.substring(NumberConstant.ZERO,
					strSimpleName.length() - NumberConstant.TWO);
		}
		return getBusinessFacade(CapRuntimeUtils
				.firstLetterToLower(strSimpleName) + "Facade");
	}

	/**
	 * 
	 * 根据实体id获取实体对应的class名称
	 * 
	 * @param entityId
	 *            实体id
	 * @return class名
	 */
	public static String getFullClassNameByEntityId(String entityId) {
		String entityClassName = entityId.replaceAll(".entity.", ".model.");
		if (!entityClassName.endsWith("VO")) {
			entityClassName += "VO";
		}
		return entityClassName;
	}

	/**
	 * 
	 * 根据实体id，获取实体名称（首字母大写）
	 * 
	 * @param entityId
	 *            实体id
	 * @return 类型名称
	 */
	public static String getEntityNameByEntityId(String entityId) {
		int lastIndex = entityId.lastIndexOf(".");
		return entityId.substring(lastIndex + 1);
	}

	/**
	 * 根据实体获取相对应的sqlKey
	 * 
	 * @param baseVO
	 *            业务对象
	 * @param prefix
	 *            key前缀
	 * @param suffix
	 *            key后缀
	 * @return 返回sqlKey
	 */
	public static String getSqlKey(CapBaseVO baseVO, String prefix,
			String suffix) {
		String pkgName = baseVO.getClass().getPackage().getName();
		String strSimpleName = baseVO.getClass().getSimpleName();
		if (strSimpleName.endsWith("VO")) {
			strSimpleName = strSimpleName.substring(NumberConstant.ZERO,
					strSimpleName.length() - NumberConstant.TWO);
		}
		StringBuffer bfStatementId = new StringBuffer();
		bfStatementId.append(pkgName).append(".").append(prefix)
				.append(strSimpleName).append(suffix);
		return bfStatementId.toString();
	}

	/**
	 * 
	 * 首字母大写
	 * 
	 * @param str
	 *            str
	 * @return 首字母大写
	 */
	public static String firstLetterToUpper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 
	 * 首字母小写
	 * 
	 * @param str
	 *            String
	 * @return 首字母小写
	 */
	public static String firstLetterToLower(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 
	 * 获得model的id
	 * 
	 * 
	 * @param model
	 *            model
	 * @return id
	 */
	public static String getId(Object model) {
		Object objResult = null;
		try {
			Method objGetIdMethod = model.getClass().getMethod("getId");
			objResult = objGetIdMethod.invoke(model);
			return objResult != null ? String.valueOf(objResult) : null;
		} catch (Exception ex) {
			LOGGER.debug("[信息]" + model.getClass().getName() + "无getId方法.", ex);
		}
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(model
				.getClass());
		FieldDescriptor[] objArrFieldDescriptors = objClassDescriptor
				.getAllFieldDescriptors();
		for (FieldDescriptor objFieldDescriptor : objArrFieldDescriptors) {
			Id objId = objFieldDescriptor.getField().getAnnotation(Id.class);
			if (objId != null) {
				try {
					objResult = objFieldDescriptor.invokeGetter(model);
				} catch (InvocationTargetException e) {
					LOGGER.error("[错误]" + model.getClass().getName() + "->"
							+ objFieldDescriptor.getField().getName()
							+ "无getter方法.", e);
				} catch (IllegalAccessException e) {
					LOGGER.error("[错误]" + model.getClass().getName() + "->"
							+ objFieldDescriptor.getField().getName()
							+ "无getter方法.", e);
				}
				break;
			}
		}
		return objResult != null ? String.valueOf(objResult) : null;
	}

	/**
	 * 将map转为bean
	 * 
	 * @param bean
	 *            bean
	 * @return map
	 */
	public static Map<String, Object> beanConvertToMap(Object bean) {
		Class<?> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					
					returnMap.put(propertyName, result);
				}
			}
		} catch (Exception e) {
			LOGGER.error("bean转换为map失败:" + e.getMessage(), e);
		}

		return returnMap;
	}

	/**
	 * 根据VO属性名获取对应的数据库字段名
	 * 
	 * @param model
	 *            VO对象
	 * @param attribute
	 *            VO属性名
	 * @return columnName
	 */
	public static String getColumnByAttribute(Object model, String attribute) {
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(model
				.getClass());
		FieldDescriptor[] objArrFieldDescriptors = objClassDescriptor
				.getAllFieldDescriptors();
		for (FieldDescriptor objFieldDescriptor : objArrFieldDescriptors) {
			Field field = objFieldDescriptor.getField();
			if (field != null && field.getName().equals(attribute)) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					return column.name();
				}
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取VO的主键列名
	 * 
	 * @param model
	 *            VO对象
	 * 
	 * @return columnPrimaryKey 表的主键
	 */
	public static String getColumnPrimaryKey(Object model) {
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(model
				.getClass());
		FieldDescriptor[] objArrFieldDescriptors = objClassDescriptor
				.getAllFieldDescriptors();
		for (FieldDescriptor objFieldDescriptor : objArrFieldDescriptors) {
			Field field = objFieldDescriptor.getField();
			if (field != null) {
				if (field.isAnnotationPresent(Id.class)) {
					Column column = field.getAnnotation(Column.class);
					return column.name();
				}
			}
		}
		return null;
	}

	/**
	 * 调用VO属性的getter方法获取返回值
	 * 
	 * @param model
	 *            VO对象
	 * @param attribute
	 *            VO属性名
	 * @return getter返回值
	 */
	public static Object invokeAttributeGetter(Object model, String attribute) {
		String result = "";
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(model
				.getClass());
		FieldDescriptor[] objArrFieldDescriptors = objClassDescriptor
				.getAllFieldDescriptors();
		for (FieldDescriptor objFieldDescriptor : objArrFieldDescriptors) {
			Field field = objFieldDescriptor.getField();
			if (field != null && field.getName().equals(attribute)) {
				try {
					Object objResult = objFieldDescriptor.invokeGetter(model);
					return objResult == null ? result : objResult;
				} catch (InvocationTargetException e) {
					LOGGER.error("[错误]" + model.getClass().getName() + "->"
							+ objFieldDescriptor.getField().getName()
							+ "无getter方法.", e);
				} catch (IllegalAccessException e) {
					LOGGER.error("[错误]" + model.getClass().getName() + "->"
							+ objFieldDescriptor.getField().getName()
							+ "无getter方法.", e);
				}
			}
		}
		return result;
	}

	/**
	 * 根据VO获取表名
	 * 
	 * @param model
	 *            VO对象
	 * @return table表名
	 */
	@SuppressWarnings("unchecked")
	public static String getTableByVO(Object model) {
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(model
				.getClass());
		if (objClassDescriptor.getType().isAnnotationPresent(Table.class)) {
			Table table = (Table) objClassDescriptor.getType().getAnnotation(
					Table.class);
			return table.name();
		}
		return null;
	}

	/**
	 * 将集合转为需要的sql in条件
	 * 
	 * @param collection
	 *            java集合
	 * @return sql中的in条件
	 */
	@SuppressWarnings("rawtypes")
	public static String toInSqlString(Collection collection) {
		if (collection == null || collection.size() <= 0)
			return null;

		StringBuffer inSql = new StringBuffer();
		inSql.append("(");
		int count = 0;
		for (Object o : collection) {
			if (count == 0) {
				inSql.append(o);
			} else {
				inSql.append("," + o);
			}
			count++;
		}
		inSql.append(")");
		return inSql.toString();
	}

	/**
	 * 设置VO的ID
	 * 
	 * @param model
	 *            model
	 * @param uuid
	 *            id
	 */
	public static void setId(Object model, String uuid) {
		try {
			Method objSetIdMethod = model.getClass().getMethod("setId",
					String.class);
			objSetIdMethod.invoke(model, uuid);
			return;
		} catch (Exception ex) {
			LOGGER.debug("[信息]" + model.getClass().getName() + "无setId方法.", ex);
		}
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(model
				.getClass());
		FieldDescriptor[] objArrFieldDescriptors = objClassDescriptor
				.getAllFieldDescriptors();
		for (FieldDescriptor objFieldDescriptor : objArrFieldDescriptors) {
			Id objId = objFieldDescriptor.getField().getAnnotation(Id.class);
			if (objId != null) {
				try {
					objFieldDescriptor.invokeSetter(model, uuid);
				} catch (IllegalAccessException e) {
					LOGGER.error("[错误]" + model.getClass().getName() + "->"
							+ objFieldDescriptor.getField().getName()
							+ "无setter方法.", e);
				}
				break;
			}
		}
	}

}
