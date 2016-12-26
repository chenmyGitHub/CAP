/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.codegen.util;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.CacheOperator;
import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.consistency.entity.util.EntityConsistencyUtil;
import com.comtop.cap.bm.metadata.entity.model.DataTypeVO;
import com.comtop.cap.bm.metadata.entity.model.query.WhereCondition;

/**
 * 查询建模工具类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年8月4日 许畅 新建
 */
public final class QueryModelUtil {

	/** 日志记录 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QueryModelUtil.class);

	/**
	 * 构造方法
	 */
	private QueryModelUtil() {

	}

	/**
	 * 获取集合中的分隔符
	 * 
	 * @param collection
	 *            集合
	 * @param index
	 *            集合下标
	 * @return 分隔符
	 */
	public static String getCollectionSeparator(Collection<?> collection,
			int index) {
		return (index == (collection.size() - 1)) ? "" : ",";
	}

	/**
	 * 获取数组分隔符
	 * 
	 * @param arrays
	 *            数组
	 * @param index
	 *            集合下标
	 * @return 分隔符
	 */
	public static String getArraySeparator(Object[] arrays, int index) {
		return (index == (arrays.length - 1)) ? "" : ",";
	}

	/**
	 * 实体id转换为实体全名
	 * 
	 * @param entityId
	 *            实体id
	 * @return 实体全名
	 */
	public static String convertToEntityFullName(String entityId) {

		return entityId.replace(".entity.", ".model.") + "VO";
	}

	/**
	 * list集合sql条件模板 <br>
	 * <if test="firstNodeIds != null and firstNodeIds.size>0 "> AND
	 * T1.CUR_NODE_ID NOT IN <foreach item="item" index="index"
	 * collection="firstNodeIds" open="(" separator="," close=")"> #{item}
	 * </foreach> </if>
	 * 
	 * @return 集合sql条件模板
	 */
	public static String getListTemplate() {
		StringBuffer template = new StringBuffer();
		template.append("\n");
		template.append("\t <foreach item=\"item\" index=\"index\" collection=\"{0}\" open=\"(\" separator=\",\" close=\")\"> \n");
		template.append("\t   #{item} \n");
		template.append("\t </foreach> \n");
		return template.toString();
	}

	/**
	 * 转换为sql in条件字符串
	 * 
	 * @param str
	 *            形式如 a,b,c 的字符串
	 * @return 转换成 'a','b','c' sql in条件的字符串
	 */
	public static String convertToInString(String str) {
		StringBuffer sb = new StringBuffer();

		String[] strs = str.split(",");
		for (int i = 0; i < strs.length; i++) {
			// 分隔符
			String separator = getArraySeparator(strs, i);
			sb.append("'");
			sb.append(strs[i]);
			sb.append("'");
			sb.append(separator);
		}
		return sb.toString();
	}

	/**
	 * 解析表达式
	 * 
	 * @param expression
	 *            表达式
	 * @param params
	 *            参数对象
	 * @return 解析表达式
	 */
	public static String parsingExpression(String expression, String... params) {

		return EntityConsistencyUtil.parsingExpression(expression, params);
	}

	/**
	 * 判断查询条件是否存在非String类型
	 * 
	 * @param condition
	 *            WhereCondition
	 * @return boolean
	 */
	public static boolean specialType(WhereCondition condition) {
		String entityId = condition.getConditionAttribute().getEntityId();
		String dbField = condition.getConditionAttribute().getColumnName();
		if (StringUtils.isNotEmpty(entityId)) {
			String expression = "entity[@modelId='{1}']/attributes[@dbFieldId='{0}']/attributeType";
			try {
				List<DataTypeVO> lst = CacheOperator.queryList(
						parsingExpression(expression, dbField, entityId),
						DataTypeVO.class);
				if (lst.size() > 0) {
					DataTypeVO dataType = lst.get(0);
					if (!"String".equals(dataType.getType())) {
						return true;
					}
				}
			} catch (OperateException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		return false;
	}

}
