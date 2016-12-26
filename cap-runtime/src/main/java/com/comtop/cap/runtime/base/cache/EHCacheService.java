/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * EHCache缓存服务类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月20日 许畅 新建
 */
public class EHCacheService implements IEHCacheService {

	/**
	 * EHCache缓存管理器
	 */
	private static CacheManager CACHEMANAGER = null;

	/**
	 * EHCache缓存对象
	 */
	private Cache cache = null;

	static {
		// 设置服务器重启后依然可以读取到缓存文件
		System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
		CACHEMANAGER = CacheManager.getInstance();
	}

	/**
	 * 构造方法
	 */
	public EHCacheService() {
		this.cache = getInstance().getCache("CapGlobalEnvironment_Cache");
	}

	/**
	 * @return 获取缓存管理器实例
	 */
	protected CacheManager getInstance() {
		if (CACHEMANAGER == null) {
			System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
			CACHEMANAGER = CacheManager.getInstance();
		}
		return CACHEMANAGER;
	}

	/**
	 * 获取EHCache中的缓存对象(如果服务器重启EHCache缓存会中磁盘中找到上次的缓存文件放入到内存中)
	 * 
	 * @param cacheKey
	 *            缓存key
	 * @return 缓存里的对象
	 * @throws Exception
	 *             对象
	 */
	@Override
	public Object getCacheElement(String cacheKey) throws Exception {
		if (cache == null)
			return null;

		Element e = cache.get(cacheKey);
		cache.flush();
		if (e == null) {
			return null;
		}
		return e.getValue();
	}

	/**
	 * 将类或者对象添加进缓存,如果缓存存在则更新覆盖
	 * 
	 * @param cacheKey
	 *            缓存key
	 * 
	 * @param result
	 *            需要缓存的对象
	 * @throws Exception
	 *             异常
	 */
	@Override
	public void saveCache(String cacheKey, Object result) throws Exception {
		Element element = new Element(cacheKey, result);
		cache.put(element);
	}

	/**
	 * 根据缓存key删除缓存对象
	 * 
	 * @param cacheKey
	 *            缓存key
	 */
	@Override
	public void removeCacheElement(String cacheKey) {
		Element e = cache.get(cacheKey);
		if (e != null) {
			cache.remove(e);
		}
	}

	/**
	 * @return cache
	 */
	public Cache getCache() {
		return cache;
	}

	/**
	 * @param cache
	 *            设置EHCache
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}
}
