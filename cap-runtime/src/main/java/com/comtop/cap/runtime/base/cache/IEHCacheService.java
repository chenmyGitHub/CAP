/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.cache;

/**
 * EHCache缓存操作接口
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月20日 许畅 新建
 */
public interface IEHCacheService {
	/**
	 * 获取EHCache中的缓存对象
	 * 
	 * @param cacheKey
	 *            缓存key
	 * @return 缓存里的对象
	 * @throws Exception
	 *             对象
	 */
	public Object getCacheElement(String cacheKey) throws Exception;

	/**
	 * 将类或者对象添加进缓存,如果缓存存在则更新覆盖
	 * 
	 * @param cacheKey
	 *            缓存key
	 * @param result
	 *            需要缓存的对象
	 * @throws Exception
	 *             异常
	 */
	public void saveCache(String cacheKey, Object result) throws Exception;

	/**
	 * 根据缓存key删除缓存对象
	 * 
	 * @param cacheKey
	 *            缓存key
	 */
	public void removeCacheElement(String cacheKey);
}
