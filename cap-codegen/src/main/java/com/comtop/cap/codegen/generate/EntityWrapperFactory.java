/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.codegen.generate;

import com.comtop.cap.codegen.config.ConfigFactory;
import com.comtop.cap.codegen.config.GeneratorConfig;
import com.comtop.cap.runtime.base.exception.CapMetaDataException;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 实体包装工厂类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月11日 许畅 新建
 */
public class EntityWrapperFactory {

	/**
	 * 构造方法
	 */
	private EntityWrapperFactory() {
		super();
	}

	/**
	 * 获取生成实体默认包装类 DefaultWrapper
	 * 
	 * @return 默认实体包装类
	 * @throws Exception
	 *             异常
	 */
	public static IWrapper getDefaultWrapper() throws Exception {
		GeneratorConfig objConfig = getGeneratorConfig();

		if (objConfig == null) {
			throw new CapMetaDataException("找不到代码生成配置。");
		}
		String strWrapper = objConfig.getWrapper();
		if (StringUtil.isBlank(strWrapper)) {
			throw new CapMetaDataException("找不到数据包装类。");
		}
		Class<?> objWrapperClass = Class.forName(strWrapper);
		Object objInstance = objWrapperClass.newInstance();
		if (!(objInstance instanceof IWrapper)) {
			throw new CapMetaDataException("错误的数据包装类。");
		}

		return (IWrapper) objInstance;
	}

	/**
	 * @return 获取实体代码生成器
	 */
	public static GeneratorConfig getGeneratorConfig() {
		return ConfigFactory.getInstance().getDefaultConfig();
	}
}
