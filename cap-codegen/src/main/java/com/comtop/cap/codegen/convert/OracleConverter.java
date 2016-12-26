/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.convert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.comtop.cap.bm.metadata.entity.model.Wildcard;

/**
 * ORACLE语法转换器
 * 
 * @author 许畅
 * @since JDK1.7
 * @version 2016年12月5日 许畅 新建
 */
public class OracleConverter implements IDataBaseConvert {

	/**
	 * 私有化构造方法
	 */
	private OracleConverter() {
		super();
	}

	/** 延迟加载 */
	private static OracleConverter instance;

	/**
	 * @return 获取该实例
	 */
	public static OracleConverter getInstance() {
		if (instance == null) {
			synchronized (OracleConverter.class) {
				if (instance == null)
					instance = new OracleConverter();
			}
		}
		return instance;
	}

	/** ORACLE语法 */
	private static final Map<Wildcard, String> grammar = new HashMap<Wildcard, String>();

	static {
		grammar.put(Wildcard.ALL_LIKE, "'%'||#{{0}}||'%' ");
		grammar.put(Wildcard.LEFT_LIKE, "'%'||#{{0}} ");
		grammar.put(Wildcard.RIGHT_LIKE, "#{{0}}||'%' ");
	}

	/**
	 * 转换为对应数据库类型语法
	 * 
	 * @param wildcard
	 *            通配符
	 * @return 转换为对应数据库类型语法
	 *
	 * @see com.comtop.cap.codegen.convert.IDataBaseConvert#convert(com.comtop.cap.bm.metadata.entity.model.Wildcard)
	 */
	@Override
	public String convert(Wildcard wildcard) {
		return grammar.get(wildcard);
	}

	/**
	 * 转换为对应数据库类型语法
	 *
	 * @param wildcard
	 *            通配符
	 * @return 转换为对应数据库类型语法
	 *
	 * @see com.comtop.cap.codegen.convert.IDataBaseConvert#convert(java.lang.String)
	 */
	@Override
	public String convert(String wildcard) {
		Set<Wildcard> keys = grammar.keySet();
		for (Wildcard key : keys) {
			if (key.getValue().equals(wildcard))
				return grammar.get(key);
		}

		return null;
	}

}
