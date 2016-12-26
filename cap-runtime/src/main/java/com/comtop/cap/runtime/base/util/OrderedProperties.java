/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * 有排序的Properties类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年2月29日 许畅 新建
 */
public class OrderedProperties extends Properties {

	/** 默认序列 */
	private static final long serialVersionUID = 1L;

	/**
	 * keyList
	 */
	private List<Object> keyList = new ArrayList<Object>();

	/**
	 * 重写put方法，按照property的存入顺序保存key到keyList，遇到重复的后者将覆盖前者。
	 */
	@Override
	public synchronized Object put(Object key, Object value) {
		this.removeKeyIfExists(key);
		keyList.add(key);
		return super.put(key, value);
	}

	/**
	 * @return keyList
	 */
	public List<Object> getKeys() {
		return keyList;
	}

	/**
	 * 重写remove方法，删除属性时清除keyList中对应的key。
	 */
	@Override
	public synchronized Object remove(Object key) {
		this.removeKeyIfExists(key);
		return super.remove(key);
	}

	/**
	 * keyList中存在指定的key时则将其删除
	 * 
	 * @param key
	 *            k
	 */
	private void removeKeyIfExists(Object key) {
		keyList.remove(key);
	}

	/**
	 * 获取Properties中key的有序集合
	 * 
	 * @return keyList
	 */
	public List<Object> getKeyList() {
		return keyList;
	}

	/**
	 * 重写keys方法，返回根据keyList适配的Enumeration，且保持HashTable keys()方法的原有语义，
	 * 每次都调用返回一个新的Enumeration对象，且和之前的不产生冲突
	 */
	@Override
	public synchronized Enumeration<Object> keys() {
		return new EnumerationAdapter<Object>(keyList);
	}

	/**
	 * List到Enumeration的适配器
	 * 
	 * @param <T>
	 *            t
	 */
	private class EnumerationAdapter<T> implements Enumeration<T> {
		/**
		 * 
		 */
		private int index = 0;

		/**
		 * 
		 */
		private final List<T> list;

		/**
		 * 
		 */
		private final boolean isEmpty;

		/**
		 * @param list
		 *            ls
		 */
		public EnumerationAdapter(List<T> list) {
			this.list = list;
			this.isEmpty = list.isEmpty();
		}
		@Override
		public boolean hasMoreElements() {
			// isEmpty的引入是为了更贴近HashTable原有的语义，在HashTable中添加元素前调用其keys()方法获得一个Enumeration的引用，
			// 之后往HashTable中添加数据后，调用之前获取到的Enumeration的hasMoreElements()将返回false，但如果此时重新获取一个
			// Enumeration的引用，则新Enumeration的hasMoreElements()将返回true，而且之后对HashTable数据的增、删、改都是可以在
			// nextElement中获取到的。
			return !isEmpty && index < list.size();
		}

		@Override
		public T nextElement() {
			if (this.hasMoreElements()) {
				return list.get(index++);
			}
			return null;
		}

	}

}
