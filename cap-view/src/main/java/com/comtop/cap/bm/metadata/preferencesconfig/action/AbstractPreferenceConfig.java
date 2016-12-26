/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.bm.metadata.preferencesconfig.action;

import com.comtop.cap.bm.metadata.common.storage.CacheOperator;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfig;
import com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade;
import com.comtop.cap.bm.metadata.preferencesconfig.model.PreferencesFileVO;
import com.comtop.top.core.jodd.AppContext;

/**
 * 首选项配置抽象模板类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月27日 许畅 新建
 */
public abstract class AbstractPreferenceConfig implements PreferenceConfig {

	/** 首选项默认配置XML modelId */
	public static final String DEFAULT_PERERENCE_MODEL_ID = PreferencesFileVO.getDefaultPreferModelId();

	/**
	 * 获取默认首选项配置信息
	 * 
	 * @return 获取默认首选项配置信息
	 */
	protected PreferencesFileVO getDefaultPreferencesFileVO() {
		return (PreferencesFileVO) CacheOperator
				.readById(DEFAULT_PERERENCE_MODEL_ID);
	}

	/**
	 * 获取首选项自定义配置信息
	 * 
	 * @return 获取首选项自定义配置信息
	 */
	protected PreferencesFileVO getCustomPreferencesFileVO() {
		PreferencesFacade preferencesFacade = AppContext
				.getBean(PreferencesFacade.class);
		if (preferencesFacade == null) {
			preferencesFacade = new PreferencesFacade();
		}
		return preferencesFacade.getCustomPreferencesFileVO();
	}
}
