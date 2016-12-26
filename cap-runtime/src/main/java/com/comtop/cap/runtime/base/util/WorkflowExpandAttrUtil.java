/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 非CAP定义工作流扩展属性工具类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年2月16日 许畅 新建
 */
public class WorkflowExpandAttrUtil {

	/**
	 * 构造方法
	 */
	private WorkflowExpandAttrUtil() {

	}

	/**
	 * 属性关系保存扩展属性与全局扩展属性对应的一对一关系
	 */
	private static Map<String, String> attrRelation = new HashMap<String, String>();

	/**
	 * 属性值value映射关系
	 */
	private static Map<String, String> valueRelation = new HashMap<String, String>();

	/**
	 * 非CAP定义的工作流所有的扩展属性key集合
	 */
	private static List<String> WORKFLOW_EXPAND_ATTRS = new ArrayList<String>();

	/**
	 * 开始属性值value映射(两个映射的分水岭标识)
	 */
	private static final String START_VALUE_MAPPING = "startValueMapping";

	/**
	 * 配置文件名称
	 */
	private static final String MAPPING_PROPERTIES = "workflowExpAttrMapping.properties";

	/** 日志 */
	private final static Logger LOGGER = LoggerFactory
			.getLogger(WorkflowExpandAttrUtil.class);

	/**
	 * 静态代码块初始化用户定义的属性与cap定义的共同一对一关系
	 */
	static {
		// 判断配置文件是否存在
		URL url = WorkflowExpandAttrUtil.class.getClassLoader().getResource(
				MAPPING_PROPERTIES);
		if (url != null) {
			// 从workflowExpAttrMapping.properties配置文件中读取配置信息
			InputStream in = WorkflowExpandAttrUtil.class.getClassLoader()
					.getResourceAsStream(MAPPING_PROPERTIES);
			OrderedProperties p = new OrderedProperties();
			try {
				// 加载输入流
				p.load(in);

				// 解析配置文件上的key,value键值对
				List<Object> keys = p.getKeys();
				// 将键值对put到attrRelation中
				for (Object ky : keys) {
					String key = String.valueOf(ky);
					String value = p.getProperty(key);

					if (START_VALUE_MAPPING.equals(key)) {
						break;
					}

					attrRelation.put(key, value);
					WORKFLOW_EXPAND_ATTRS.add(key);
				}

				// 配置文件中属性值value
				Set<Object> all = p.keySet();
				all.removeAll(WORKFLOW_EXPAND_ATTRS);// 去除扩展属性key
				all.remove(START_VALUE_MAPPING);// 去除标识key
				for (Object obj : all) {
					String key = String.valueOf(obj);
					String value = p.getProperty(key);
					valueRelation.put(key, value);
				}

			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}

	}

	/**
	 * 将value转换成cap定义的value
	 * 
	 * @param value
	 *            非cap定义的扩展属性xml的value
	 * @return cap定义的value
	 */
	public static String getRealAttrMapVal(String value) {
		if (valueRelation.size() <= 0)
			return String.valueOf(false);

		Set<Entry<String, String>> set = valueRelation.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();

		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			// 配置文件中的true/false
			String entryKey = entry.getKey();
			// 配置文件中定义的非CAP扩展属性值
			String entryValues = entry.getValue();
			if (entryValues != null) {
				String[] values = entryValues.split(",");
				for (String entryValue : values) {
					if (entryValue.equals(value))
						return entryKey;
				}
			}
		}

		return String.valueOf(false);
	}

	/**
	 * 设置扩展属性值(将非CAP定义的xml属性值放入CAP map中)
	 * 
	 * @param objMap
	 *            cap定义的扩展属性
	 */
	public static void putExpandAttribute(Map<String, String> objMap) {
		// 非CAP定义的扩展属性Map
		Map<String, String> otherAttrMap = new HashMap<String, String>();
		// 设置7个扩展属性值
		for (String valueAttr : WORKFLOW_EXPAND_ATTRS) {
			if (objMap.containsKey(valueAttr)) {
				// 非CAP定义的扩展属性xml的value
				String value = objMap.get(valueAttr);
				// CAP定义的xml value; 不填和非必填作为:意见是否必填的false 其他为 true
				String realValue = WorkflowExpandAttrUtil
						.getRealAttrMapVal(value);

				// 处理: 意见是否必填,回退意见是否必填,是否需要发送邮件,是否需要发送短信,是否指定回退 这5个CAP定义的扩展属性
				if (realValue != null && attrRelation.containsKey(valueAttr))
					otherAttrMap.put(attrRelation.get(valueAttr), realValue);
			}
		}

		// 将otherAttrMap覆盖到objMap中去
		objMap.putAll(otherAttrMap);
	}

}
