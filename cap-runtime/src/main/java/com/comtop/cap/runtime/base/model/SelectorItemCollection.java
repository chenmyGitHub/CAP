/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 需要更新的指定列集合
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年3月31日 许畅 新建
 */
public class SelectorItemCollection {

	/**
	 * 公共构造方法
	 */
	public SelectorItemCollection() {

	}

	/** 日志 */
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SelectorItemCollection.class);

	/**
	 * 列名
	 */
	private List<String> attributes = new ArrayList<String>();

	/**
	 * ids集合(可使用批量in来更新)
	 */
	private Set<String> ids = new HashSet<String>();

	/**
	 * 添加vo属性方法
	 * 
	 * @param attribute
	 *            vo中的属性值
	 */
	public void add(String attribute) {
		attributes.add(attribute);
	}

	/**
	 * @return 列数
	 */
	public int size() {
		return attributes.size();
	}

	/**
	 * 获取VO属性值
	 * 
	 * @return VO属性值
	 */
	public List<String> getAttributes() {
		return attributes;
	}

	/**
	 * 拼装成update sql语句
	 * 
	 * @param model
	 *            VO对象
	 * 
	 * @return update sql
	 */
	public String toUpdateSQL(CapBaseVO model) {
		if (attributes.size() <= 0) {
			LOGGER.error(this.getClass().getName() + "实例化时未调用add方法...");
			return null;
		}

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		// 表名
		sql.append(CapRuntimeUtils.getTableByVO(model) + " ");
		sql.append("SET ");

		// 列名
		int count = 0;
		for (String attribute : attributes) {
			String column = CapRuntimeUtils.getColumnByAttribute(model,
					attribute);
			if (StringUtil.isEmpty(column))
				continue;

			Object columnVal = CapRuntimeUtils.invokeAttributeGetter(model,
					attribute);

			if (count == 0) {
				sql.append(column);

				if (columnVal instanceof java.util.Date) {
					sql.append("=" + converToDate(columnVal));
				} else {
					sql.append("='" + columnVal + "'");
				}
			} else {
				sql.append(",");
				sql.append(column);

				if (columnVal instanceof java.util.Date) {
					sql.append("=" + converToDate(columnVal));
				} else {
					sql.append("='" + columnVal + "'");
				}

			}
			count++;
		}
		if (StringUtil.isEmpty(model.getPrimaryValue())) {
			LOGGER.error(model.getClass().getName() + "的VO主键为空...");
			return null;
		}
		// 条件
		sql.append("\n WHERE ");
		sql.append(CapRuntimeUtils.getColumnPrimaryKey(model) + " =");
		sql.append("'" + model.getPrimaryValue() + "'");
		return sql.toString();
	}

	/**
	 * @param columnVal
	 *            列值
	 * @return 日期格式
	 */
	private String converToDate(Object columnVal) {
		if (columnVal instanceof java.util.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return "to_date('" + sdf.format((java.util.Date) columnVal)
					+ "','yyyy-MM-dd HH24:mi:ss')";
		}
		return "";
	}

	/**
	 * @return the ids
	 */
	public Set<String> getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(Set<String> ids) {
		this.ids = ids;
	}
}
