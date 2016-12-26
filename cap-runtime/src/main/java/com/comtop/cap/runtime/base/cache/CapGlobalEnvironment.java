/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Cap系统全局性环境变量,主要用于存放系统配置类（非模型元数据）配置
 * 
 * @author 李小强
 * @version jdk1.5
 * @version 2016-5-20 李小强
 */
public class CapGlobalEnvironment {
	/**
	 * 系统全局性环境变量-非模型元数据
	 */
	private final static Map<String, Object> GLOBAL_VAR_MAP = new HashMap<String, Object>();

	/**
	 * 置入全局变量及值
	 * 
	 * @param varKey
	 *            变量key
	 * @param varValue
	 *            变量值
	 */
	public static void putVar(String varKey, Object varValue) {
		GLOBAL_VAR_MAP.put(varKey, varValue);
	}

	/**
	 * @return the globalVarMap
	 */
	public static Map<String, Object> getGlobalVarMap() {
		return GLOBAL_VAR_MAP;
	}

	/**
	 * 获取全局变量值
	 * 
	 * @param varKey
	 *            变量key
	 * @return 变量值
	 */
	public static Object getVar(String varKey) {
		return GLOBAL_VAR_MAP.get(varKey);
	}
}
