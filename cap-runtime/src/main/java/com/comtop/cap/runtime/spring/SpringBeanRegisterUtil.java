/******************************************************************************
 * Copyright (C) 2011 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.spring;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 动态注入Spring，bean工具类
 * 
 * @author luozhenming
 *
 */
public class SpringBeanRegisterUtil {
	
	/***/
	private static final Pattern facadePattern = Pattern.compile("^com\\.comtop\\..*\\.facade\\..*Facade$");
	
	 /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CapBaseAppService.class);
	
			     
	/** register the bean 
	 * 
	 * 
	 * @param beanId beanId
	 * @param className 类名
	 * 
	 * */
	public static void registerBean(String beanId,String className) {
		
		BeanDefinitionRegistry beanDefinitionRegistry = getSpringBeanRegistry(); 
		
		// get the BeanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(className);
		// get the BeanDefinition
		BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
		
		registerBean(beanId, beanDefinitionRegistry, beanDefinition);  
	}

	/**
	 * @param beanId beanId
	 * @param beanDefinitionRegistry 注册对项
	 * @param beanDefinition bean定义对象
	 */
	private static void registerBean(String beanId,
			BeanDefinitionRegistry beanDefinitionRegistry,
			BeanDefinition beanDefinition) {
		
		unregisterBean(beanId);
	
		// register the bean
		beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
	}

	/**
	 * @return BeanDefinitionRegistry
	 */
	private static BeanDefinitionRegistry getSpringBeanRegistry() {
		ApplicationContext context=BeanContextUtil.getSpringContext();
		@SuppressWarnings("resource")
		ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
		BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
		//Spring的容器对象，不能关闭。关闭后，从容器中获取不了对象
		//configurableContext.close();
		return beanDefinitionRegistry;
	}
	
	/** register the bean 
	 * 
	 * 
	 * @param beanType 类型
	 * 
	 * */
	public static void registerBean(Class<?> beanType) {
		
		BeanDefinitionRegistry beanDefinitionRegistry = getSpringBeanRegistry(); 
		// get the BeanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanType); 
		
		BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
		
		String beanId = getServiceBeanId(beanType);
		
		registerBean(beanId, beanDefinitionRegistry, beanDefinition);  
	}
	
	/** register the bean 
	 * 
	 * 
	 * @param beanType 类型
	 * 
	 * */
	public static void registerSpringBean(Class<?> beanType) {
		
		BeanDefinitionRegistry beanDefinitionRegistry = getSpringBeanRegistry(); 
		// get the BeanDefinitionBuilder
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanType); 
		
		BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
		
		String beanId = getSpringBeanId(beanType);
		
		if(beanId == null){
			LOGGER.error("class:"+beanType.getName()+" is not a spring bean type");
			return;
		}
		
		registerBean(beanId, beanDefinitionRegistry, beanDefinition);  
	}
	
	/**
	 * 
	 * @param beanType bean类型
	 * @return beaId
	 */
	private static String getSpringBeanId(Class<?> beanType) {
		Service objService = beanType.getAnnotation(Service.class);
		if(objService != null){
			return getServiceBeanId(beanType);
		}
		Controller objController = beanType.getAnnotation(Controller.class);
		if(objController != null){
			return getControllerBeanId(beanType);
		}
		Component objComponent = beanType.getAnnotation(Component.class);
		if(objComponent != null){
			return getComponentBeanId(beanType);
		}
		Repository objRepository = beanType.getAnnotation(Repository.class);
		if(objRepository != null){
			return getRepositoryBeanId(beanType);
		}
		return null;
	}

	/**
	 * 
	 * @param beanType bean类型
	 * @return beanId
	 */
	private static String getRepositoryBeanId(Class<?> beanType) {
		String beanId = "";
		Repository objService = beanType.getAnnotation(Repository.class);
		if(StringUtil.isBlank(objService.value())){
			beanId = StringUtil.uncapitalize(beanType.getSimpleName());
		}else{
			beanId = objService.value();
		}
		return beanId;
	}

	/**
	 * 
	 * @param beanType bean类型
	 * @return beanId
	 */
	private static String getComponentBeanId(Class<?> beanType) {
		String beanId = "";
		Component objService = beanType.getAnnotation(Component.class);
		if(StringUtil.isBlank(objService.value())){
			beanId = StringUtil.uncapitalize(beanType.getSimpleName());
		}else{
			beanId = objService.value();
		}
		return beanId;
	}

	/**
	 * 
	 * @param beanType bean类型
	 * @return beanId
	 */
	private static String getControllerBeanId(Class<?> beanType){
		String beanId = "";
		Controller objService = beanType.getAnnotation(Controller.class);
		if(StringUtil.isBlank(objService.value())){
			beanId = StringUtil.uncapitalize(beanType.getSimpleName());
		}else{
			beanId = objService.value();
		}
		return beanId;
	}
	
	/**
	 * 
	 * @param beanType bean类型
	 * @return beanId
	 */
	private static String getServiceBeanId(Class<?> beanType){
		String beanId = "";
		Service objService = beanType.getAnnotation(Service.class);
		if(StringUtil.isBlank(objService.value())){
			beanId = StringUtil.uncapitalize(beanType.getSimpleName());
		}else{
			beanId = objService.value();
		}
		return beanId;
	}
	
	/** unregister the bean 
	 * @param beanType beanId
	 * */
	public static void unregisterBean(Class<?> beanType){
		String beanId = getSpringBeanId(beanType);
		unregisterBean(beanId);
	}
	
	/** unregister the bean 
	 * @param beanId beanId
	 * */
	public static void unregisterBean(String beanId){
		BeanDefinitionRegistry beanDefinitionRegistry = getSpringBeanRegistry(); 
		if(beanDefinitionRegistry.containsBeanDefinition(beanId)){
			beanDefinitionRegistry.removeBeanDefinition(beanId);
		}
	}
	
	/**
	 * 是否为Spirng的bean
	 * @param beanType 类型
	 * @return true 是，false 不是
	 */
	public static boolean isSpringFacadeBean(Class<?> beanType){
		Matcher objMatch =facadePattern.matcher(beanType.getName());
		if(objMatch.find()){
			Service objService = beanType.getAnnotation(Service.class);
			if(objService == null){
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 是否为Spirng的Controller
	 * @param beanType 类型
	 * @return true 是，false 不是
	 */
	public static boolean isSpringBean(Class<?> beanType){
		Controller objController = beanType.getAnnotation(Controller.class);
		if(objController != null){
				return true;
		}
		Service objService = beanType.getAnnotation(Service.class);
		if(objService != null){
				return true;
		}
		Component objComponent = beanType.getAnnotation(Component.class);
		if(objComponent != null){
				return true;
		}
		Repository objRepository = beanType.getAnnotation(Repository.class);
		if(objRepository != null){
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param args xx
	 *//*
	public static void main(String[] args) {
		String str = CapWorkflowFacade.class.getName();
		Matcher objMatch = facadePattern.matcher(str);
		System.out.println(str);
		if(objMatch.matches()){
			System.out.println("find "+objMatch.group());
		}
		
		str = AbstractTestFacade.class.getName();
		Matcher objMatch1 = facadePattern.matcher(str);
		System.out.println(str);
		if(objMatch1.find()){
			System.out.println("find "+objMatch1.group());
		}
		
		//String sss = "com.comtop.lzm.testproject.facade.LzmProjectFacade$$EnhancerByCGLIB$$da7d8c1b";
		
		String sss = "com.comtop.lzm.testproject.LzmProjectFacade";
		
		Matcher objMatch2 = facadePattern.matcher(sss);
		System.out.println(sss);
		if(objMatch2.find()){
			System.out.println("find "+objMatch2.group());
		}
		
		System.out.println(StringUtil.uncapitalize("Abcedd"));
		System.out.println(StringUtil.uncapitalize("abcedd"));
	}*/
}
