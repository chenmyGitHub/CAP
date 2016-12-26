/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel.scan;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.comtop.cap.runtime.juel.annotation.JuelExtMethod;
import com.comtop.cap.runtime.juel.model.JuelFunctionBean;
import com.comtop.soa.common.util.SOAReflectionUtils;

/**
 * Juel方法注解解析器，无任何第三方jar依赖，保障运行时jar包最小化
 * @author 李小强
 * @since 1.0
 * @version 2016-11-9 李小强
 */
public class JuelMethodAnnParser {

/**
 * 解析指定集合clazz中的juel方法注解
 * @param clazzSet  指定集合clazz
 * @return 指定集合clazz中的juel方法注解 的方法集中
 */
	public static List<JuelFunctionBean> parser(Set<Class<?>> clazzSet) {
		List<JuelFunctionBean> lstMethod = new ArrayList<JuelFunctionBean>();
		if (clazzSet == null || clazzSet.isEmpty()) {
			return lstMethod;
		}
		Iterator<Class<?>> it = clazzSet.iterator();
		while (it.hasNext()) {
			Class<?> clazz = it.next();
			List<Method> clazzMethods = SOAReflectionUtils.getClassMethodWithOutObjMethod(clazz);
			if (clazzMethods==null||clazzMethods.isEmpty()) {
				continue;
			}
			for (Method objMethod : clazzMethods) {
				JuelExtMethod ann = objMethod.getAnnotation(JuelExtMethod.class);
				if (ann == null||ann.localName() == null||ann.localName().trim().equals("")) {
					continue;
				}
				lstMethod.add(new JuelFunctionBean(ann.prefix(), ann.localName(), objMethod));
			}
		}
		return lstMethod;
	}

}
