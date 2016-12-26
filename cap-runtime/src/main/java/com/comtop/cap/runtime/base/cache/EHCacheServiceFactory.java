/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.cache;

/**
 * EHCache缓存服务类工厂类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月20日 许畅 新建
 */
public class EHCacheServiceFactory {

	/**
	 * 构造方法
	 */
	private EHCacheServiceFactory() {

	}

	/**
	 * 获取EHCache缓存服务实例对象
	 * 
	 * @return 获取EHCacheService实例对象
	 */
	public static IEHCacheService getInstance() {
		
		return new EHCacheService();
	}

}
