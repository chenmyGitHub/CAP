/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.util;

import com.comtop.corm.annotations.Param;
import com.comtop.corm.mapping.BoundSql;
import com.comtop.corm.mapping.MappedStatement;
import com.comtop.corm.mapping.ParameterMapping;
import com.comtop.corm.mapping.ParameterMode;
import com.comtop.corm.reflection.MetaObject;

import com.comtop.corm.reflection.factory.DefaultObjectFactory;
import com.comtop.corm.reflection.factory.ObjectFactory;
import com.comtop.corm.reflection.wrapper.DefaultObjectWrapperFactory;
import com.comtop.corm.reflection.wrapper.ObjectWrapperFactory;
import com.comtop.corm.session.Configuration;
import com.comtop.corm.session.ResultHandler;
import com.comtop.corm.session.RowBounds;
import com.comtop.corm.session.SqlSession;
import com.comtop.corm.type.JdbcType;
import com.comtop.corm.type.TypeHandlerRegistry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * myBatis SqlHelper
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年3月4日 许畅 新建
 */
public class MyBatisSqlHelper {
	/**
	 * DEFAULT_OBJECT_FACTORY
	 */
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();

	/**
	 * DEFAULT_OBJECT_WRAPPER_FACTORY
	 */
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

	/**
	 * 通过命名空间方式获取sql
	 * 
	 * @param session
	 *            s
	 * @param namespace
	 *            s
	 * @param params
	 *            s
	 * @return s
	 */
	public static String getSqlByNamespace(SqlSession session, String namespace,
			Object params) {
		params = wrapCollection(params);
		Configuration configuration = session.getConfiguration();
		MappedStatement mappedStatement = configuration
				.getMappedStatement(namespace);
		TypeHandlerRegistry typeHandlerRegistry = mappedStatement
				.getConfiguration().getTypeHandlerRegistry();
		BoundSql boundSql = mappedStatement.getBoundSql(params);
		List<ParameterMapping> parameterMappings = boundSql
				.getParameterMappings();
		String sql = boundSql.getSql();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (params == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(params
							.getClass())) {
						value = params;
					} else {
						MetaObject metaObject = configuration
								.newMetaObject(params);
						value = metaObject.getValue(propertyName);
					}
					JdbcType jdbcType = parameterMapping.getJdbcType();
					if (value == null && jdbcType == null)
						jdbcType = configuration.getJdbcTypeForNull();
					sql = replaceParameter(sql, value, jdbcType,
							parameterMapping.getJavaType());
				}
			}
		}
		return sql;
	}
	
	/**
	 * 
	 * 通过Mapper方法名获取sql
	 * 
	 * @param session
	 *            SqlSession
	 * @param fullMapperMethodName
	 *            fullMapperMethodName
	 * @param args
	 *            ar
	 * @return str
	 */
	public static String getMapperSql(SqlSession session,
			String fullMapperMethodName, Object... args) {
		if (args == null || args.length == 0) {
			return getSqlByNamespace(session, fullMapperMethodName, null);
		}
		String methodName = fullMapperMethodName.substring(fullMapperMethodName
				.lastIndexOf('.') + 1);
		Class mapperInterface = null;
		try {
			mapperInterface = Class.forName(fullMapperMethodName.substring(0,
					fullMapperMethodName.lastIndexOf('.')));
			return getMapperSql(session, mapperInterface, methodName, args);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("参数" + fullMapperMethodName
					+ "无效！", e);
		}
	}

	/**
	 * 
	 * 通过Mapper接口和方法名
	 * 
	 * @param session
	 *            session
	 * @param mapperInterface
	 *            mapperInterface
	 * @param methodName
	 *            methodName
	 * @param args
	 *            args
	 * @return str
	 */
	public static String getMapperSql(SqlSession session,
			Class mapperInterface, String methodName, Object... args) {
		String fullMapperMethodName = mapperInterface.getCanonicalName() + "."
				+ methodName;
		if (args == null || args.length == 0) {
			return getSqlByNamespace(session, fullMapperMethodName, null);
		}
		Method method = getDeclaredMethods(mapperInterface, methodName);
		Map params = new HashMap();
		final Class<?>[] argTypes = method.getParameterTypes();
		for (int i = 0; i < argTypes.length; i++) {
			if (!RowBounds.class.isAssignableFrom(argTypes[i])
					&& !ResultHandler.class.isAssignableFrom(argTypes[i])) {
				String paramName = "param" + String.valueOf(params.size() + 1);
				paramName = getParamNameFromAnnotation(method, i, paramName);
				params.put(paramName, i >= args.length ? null : args[i]);
			}
		}
		if (args != null && args.length == 1) {
			Object _params = wrapCollection(args[0]);
			if (_params instanceof Map) {
				params.putAll((Map) _params);
			}
		}
		return getSqlByNamespace(session, fullMapperMethodName, params);
	}

	/**
	 * 通过命名空间方式获取sql
	 * 
	 * @param session
	 *            SqlSession
	 * @param namespace
	 *            n
	 * @return str
	 */
	public static String getNamespaceSql(SqlSession session, String namespace) {
		return getSqlByNamespace(session, namespace, null);
	}

	/**
	 * 根据类型替换参数 仅作为数字和字符串两种类型进行处理，需要特殊处理的可以继续完善这里
	 * 
	 * @param sql
	 *            s
	 * @param value
	 *            v
	 * @param jdbcType
	 *            j
	 * @param javaType
	 *            j
	 * @return str
	 */
	private static String replaceParameter(String sql, Object value,
			JdbcType jdbcType, Class javaType) {
		String strValue = String.valueOf(value);
		if (jdbcType != null) {
			switch (jdbcType) {
			// 数字
			case BIT:
			case TINYINT:
			case SMALLINT:
			case INTEGER:
			case BIGINT:
			case FLOAT:
			case REAL:
			case DOUBLE:
			case NUMERIC:
			case DECIMAL:
				break;
			// 日期
			case DATE:
			case TIME:
			case TIMESTAMP:
				// 其他，包含字符串和其他特殊类型
			default:
				strValue = "'" + strValue + "'";
			}
		} else if (Number.class.isAssignableFrom(javaType)) {
			// 不加单引号

		} else {
			strValue = "'" + strValue + "'";
		}
		return sql.replaceFirst("\\?", strValue);
	}

	/**
	 * 根据类型替换参数 仅作为数字和字符串两种类型进行处理，需要特殊处理的可以继续完善这里
	 * 
	 * @param clazz
	 *            c
	 * @param methodName
	 *            m
	 * @return m
	 */
	private static Method getDeclaredMethods(Class clazz, String methodName) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		throw new IllegalArgumentException("方法" + methodName + "不存在！");
	}

	/**
	 * 获取参数注解名
	 *
	 * @param method
	 *            m
	 * @param i
	 *            i
	 * @param paramName
	 *            p
	 * @return s
	 */
	private static String getParamNameFromAnnotation(Method method, int i,
			String paramName) {
		final Object[] paramAnnos = method.getParameterAnnotations()[i];
		for (Object paramAnno : paramAnnos) {
			if (paramAnno instanceof Param) {
				paramName = ((Param) paramAnno).value();
			}
		}
		return paramName;
	}

	/**
	 * 简单包装参数
	 * 
	 * @param object
	 *            o
	 * 
	 * @return r
	 */
	private static Object wrapCollection(final Object object) {
		if (object instanceof List) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", object);
			return map;
		} else if (object != null && object.getClass().isArray()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("array", object);
			return map;
		}
		return object;
	}

	/**
	 * @return the defaultObjectFactory
	 */
	public static ObjectFactory getDefaultObjectFactory() {
		return DEFAULT_OBJECT_FACTORY;
	}

	/**
	 * @return the defaultObjectWrapperFactory
	 */
	public static ObjectWrapperFactory getDefaultObjectWrapperFactory() {
		return DEFAULT_OBJECT_WRAPPER_FACTORY;
	}

}
