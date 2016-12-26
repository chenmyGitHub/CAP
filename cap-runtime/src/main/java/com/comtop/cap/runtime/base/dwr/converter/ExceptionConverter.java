/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.base.dwr.converter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import comtop.org.directwebremoting.ConversionException;
import comtop.org.directwebremoting.extend.PlainProperty;
import comtop.org.directwebremoting.extend.Property;
import comtop.org.directwebremoting.extend.PropertyDescriptorProperty;
import comtop.org.directwebremoting.util.LocalUtil;

/**
 * DWR异常处理信息转换器
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年4月20日 许畅 新建
 */
public class ExceptionConverter extends
		comtop.org.directwebremoting.convert.ExceptionConverter {

	/**
	 * 对dwr请求异常信息处理
	 * 
	 * @param type
	 *            异常class
	 * @param readRequired
	 *            get方法
	 * @param writeRequired
	 *            set方法
	 * @return map
	 * @throws ConversionException
	 *             dwr转换异常
	 */
	@Override
	public Map<String, Property> getPropertyMapFromClass(Class<?> type,boolean readRequired, boolean writeRequired)throws ConversionException {
		Map<String, Property> descriptors = this.getPropertyMapFromCls(type,
				readRequired, writeRequired);
		descriptors.put("javaClassName", new PlainProperty("javaClassName",
				type.getName()));

		try {
			fixMissingThrowableProperty(descriptors, "message", "getMessage");
			fixMissingThrowableProperty(descriptors, "cause", "getCause");
		} catch (IntrospectionException ex) {
			throw new ConversionException(type, ex);
		}

		return descriptors;
	}

	/**
	 * 通过class反射取得异常信息
	 *
	 * @param type
	 *            异常class
	 * @param readRequired
	 *            boolean
	 * @param writeRequired
	 *            boolean
	 * @return map
	 * @throws ConversionException
	 *             dwr转换异常
	 */
	public Map<String, Property> getPropertyMapFromCls(Class<?> type,
			boolean readRequired, boolean writeRequired)
			throws ConversionException {
		try {
			BeanInfo info = Introspector.getBeanInfo(type);
			PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

			Map<String, Property> properties = new HashMap<String, Property>();

			for (PropertyDescriptor descriptor : descriptors) {
				String name = descriptor.getName();
				if ("class".equals(name)) {
					continue;
				}
				if (!(isAllowedByIncludeExcludeRules(name))) {
					continue;
				}
				if ((readRequired) && (descriptor.getReadMethod() == null)) {
					continue;
				}
				if ((writeRequired)
						&& (LocalUtil.getWriteMethod(type, descriptor) == null)) {
					continue;
				}
				properties
						.put(name, new PropertyDescriptorProperty(descriptor));
			}
			return properties;
		} catch (IntrospectionException ex) {
			throw new ConversionException(type, ex);
		}
	}

	/**
	 * 如果没有取到堆栈信息,通过反射取堆栈信息
	 *
	 * @param descriptors
	 *            异常描述信息
	 * @param name
	 *            map的key
	 * @param readMethodName
	 *            get方法
	 * @throws IntrospectionException
	 *             IntrospectionException
	 */
	@Override
	protected void fixMissingThrowableProperty(Map<String, Property> descriptors, String name,String readMethodName) throws IntrospectionException {
		if ((descriptors.containsKey(name))
				|| (!(isAllowedByIncludeExcludeRules(name))))
			return;

		PropertyDescriptor descriptor = new PropertyDescriptor(name,
				Throwable.class, readMethodName, null);
		descriptors.put(name, new PropertyDescriptorProperty(descriptor));
	}
}
