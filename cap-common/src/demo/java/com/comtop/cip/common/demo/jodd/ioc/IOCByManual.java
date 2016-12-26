/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.ioc;

import com.comtop.cip.jodd.petite.PetiteContainer;
import com.comtop.cip.jodd.petite.scope.ProtoScope;
import com.comtop.cip.jodd.petite.scope.SingletonScope;

/**
 *
 * 通过手工的方式注册和使用bean
 *
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-14 柯尚福
 */
public class IOCByManual {

	/**
	 *
	 * 通过类型方式注册bean
	 *
	 */
	public void registBeanByShortType() {
		// 创建ioc容器
		PetiteContainer petite = new PetiteContainer();

		// 在容器中注册bean
		petite.registerPetiteBean(MyBean.class, null, null, null, false);

	}

	/**
	 *
	 * 通过类全路径的方式注册bean
	 *
	 */
	public void registBeanByFullType() {
		// 创建ioc容器
		PetiteContainer petite = new PetiteContainer();

		// 设置使用类全路径注册
		petite.getConfig().setUseFullTypeNames(true);

		// 在容器中注册bean
		petite.registerPetiteBean(MyBean.class, null, null, null, false);

	}

	/**
	 *
	 * 注册bean，并指定bean的scope（单例或多实例）
	 * ProtoScope.class 多例
	 * SingletonScope.class 单例
	 *
	 */
	public void registBeanWithScope() {
		// 创建ioc容器
		PetiteContainer petite = new PetiteContainer();

		// 在容器中注册bean,并设定bean的创建方式为多实例
		petite.registerPetiteBean(MyBean.class, null, ProtoScope.class, null, false);

		// 在容器中注册bean,并设定bean的创建方式为单实例
		petite.registerPetiteBean(MyBean.class, null, SingletonScope.class, null, false);

	}

	/**
	 *
	 * 通过类型方式获取bean
	 *
	 */
	public void getBeanByType() {
		// 创建ioc容器
		PetiteContainer petite = new PetiteContainer();
		petite.getConfig().setUseFullTypeNames(true);
		// 在容器中注册bean
		petite.registerPetiteBean(MyBean.class, null, null, null, false);
		petite.registerPetiteBean(com.comtop.cip.common.demo.jodd.util.MyBean.class, null, null, null, false);

		// 通过类型获取bean
		MyBean myBean = petite.getBean(MyBean.class);
		myBean.hello();

	}

	/**
	 *
	 * 通过类名称获取bean
	 *
	 */
	public void getBeanByTypeName() {
		// 创建ioc容器
		PetiteContainer petite = new PetiteContainer();
		// 在容器中注册bean
		petite.registerPetiteBean(MyBean.class, null, null, null, false);

		// 通过类型获取bean
		MyBean myBean = (MyBean) petite.getBean("myBean");
		myBean.hello();

	}

	/**
	 *
	 * 通过类名全路径的方式获取bean
	 *
	 */
	public void getBeanByFullTypeName() {
		// 创建ioc容器
		PetiteContainer petite = new PetiteContainer();

		// 设置使用类全路径注册
		petite.getConfig().setUseFullTypeNames(true);

		// 在容器中注册bean
		petite.registerPetiteBean(MyBean.class, null, null, null, false);

		// 通过类名全路径的方式获取bean
		MyBean myBean = (MyBean) petite.getBean("com.comtop.cip.common.demo.jodd.ioc.MyBean");
		myBean.hello();

	}

	/**
	 *
	 * bean的注册和使用例子
	 *
	 * @param args 参数
	 */
	public static void main(String[] args) {

		IOCByManual iocByManual = new IOCByManual();

		iocByManual.registBeanByShortType();
		iocByManual.registBeanByFullType();
		iocByManual.registBeanWithScope();

		iocByManual.getBeanByType();
		iocByManual.getBeanByTypeName();
		iocByManual.getBeanByFullTypeName();
	}

}
