/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.bpmsext;

import com.comtop.cap.runtime.base.util.BeanContextUtil;

/**
 * CAP工作流facade工厂类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月5日 许畅 新建
 */
public class CapWorkflowFacadeFactory {

	/**
	 * 构造方法
	 */
	private CapWorkflowFacadeFactory() {

	}

	/**
	 * @return 获取capWorkflowFacade实例
	 */
	public static ICapWorkflowExtFacade getInstance() {
		return (ICapWorkflowExtFacade) BeanContextUtil.getBean("capWorkflowExtFacade");
	}

	/**
	 * @param <T> Class
	 * @param cls
	 *            获取指定facade实例
	 * @return T
	 */
	public static <T> T getInstance(Class<T> cls) {
		return BeanContextUtil.getBean(cls);
	}
}
