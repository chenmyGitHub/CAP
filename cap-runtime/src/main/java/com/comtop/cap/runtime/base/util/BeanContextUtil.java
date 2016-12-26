/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.top.component.common.systeminit.TopBeanRegister;
import com.comtop.top.core.jodd.AppContext;

/**
 * bean容器工具类
 * 
 * @author 罗珍明
 *
 */
public final class BeanContextUtil {
    
    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(BeanContextUtil.class);
    
    /**
     * 获取bean对象，先从Spring容器中获取，若不为空则返回Spring容器中的对象。
     * 若Spring容器中对象为空，则返回jodd容器中的bean对象。
     * 
     * @param beanName bean名称
     * @return bean对象
     */
    public static Object getBean(String beanName) {
        Object objBean = getBeanFromSpringContext(beanName);
        if (objBean == null) {
            return getBeanFromJoddContext(beanName);
        }
        return objBean;
    }
    
    /**
     * 获取bean对象，若class有spring注解，则从spring容器中获取，否则从jodd容器中获取。
     * 如果jodd中取不到，则表示可能是通过spring配置文件注入的，尝试再从spring容器中取。
     * 如果仍然取不到，则返回null。
     * 
     * @param <T> Class
     * 
     * @param beanType bean类型
     * @return bean对象
     */
    public static <T> T getBean(Class<T> beanType) {
        if (isClassWithSpringAnnotion(beanType)) {
            return getBeanFromSpringContext(beanType);
        }
        T objBean = getBeanFromJoddContext(beanType);
        if (objBean == null) {
            // 没有spring的注解，也不在jodd容器中。可能是通过spring配置文件注入的bean。
            return getBeanFromSpringContext(beanType);
        }
        return objBean;
    }
    
    /**
     * 判断类型是否带有spring的注解
     * 
     * @param beanType bean类型
     * @return bean对象
     */
    private static boolean isClassWithSpringAnnotion(Class<?> beanType) {
        Annotation[] arrAnno = beanType.getAnnotations();
        boolean isBean = false;
        for (Annotation annotation : arrAnno) {
            if (annotation instanceof Component) {
                isBean = true;
                break;
            }
            if (annotation instanceof Service) {
                isBean = true;
                break;
            }
            if (annotation instanceof Controller) {
                isBean = true;
                break;
            }
            if (annotation instanceof Repository) {
                isBean = true;
                break;
            }
        }
        return isBean;
    }
    
    /**
     * 
     * @param beanName bean名称
     * @return bean对象
     */
    public static Object getBeanFromJoddContext(String beanName) {
        return AppContext.getBean(beanName);
    }
    
    /**
     * 
     * @param <T> Class
     * @param beanType bean的类型
     * @return bean对象
     */
    public static <T> T getBeanFromJoddContext(Class<T> beanType) {
        return AppContext.getBean(beanType);
    }
    
    /**
     * 
     * @return 所有容器中bean的名称
     */
    public static Set<String> getBeanNamesFromJoddContext() {
        return AppContext.getBeanNames();
    }
    
    /**
     * 
     * @param beanName bean名称
     * @return bean对象
     */
    public static Object getBeanFromSpringContext(String beanName) {
        if (getSpringContext() == null) {
            return null;
        }
        try {
            return getSpringContext().getBean(beanName);
        } catch (Exception e) {
            LOGGER.debug("error when getBean(String) from spring context with beanName:" + beanName, e);
        }
        return null;
    }
    
    /**
     * 
     * @param beanType bean的类型
     * @param <T> Class
     * @return bean对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanFromSpringContext(Class<T> beanType) {
        if (getSpringContext() == null) {
            return null;
        }
        T objBean = null;
        try {
            objBean = getSpringContext().getBean(beanType);
        } catch (Exception e) {
            LOGGER.error("can not getBean(Class) from spring context with type:" + beanType.getName(), e);
        }
        if (objBean != null) {
            return objBean;
        }
        LOGGER.error("try to getBean(String) from spring context with beanName for Type:" + beanType.getName());
        Annotation[] arrAnno = beanType.getAnnotations();
        String beanName = null;
        boolean isBean = false;
        for (Annotation annotation : arrAnno) {
            if (annotation instanceof Component) {
                Component objComponent = (Component) annotation;
                beanName = objComponent.value();
                isBean = true;
                break;
            }
            if (annotation instanceof Service) {
                Service objService = (Service) annotation;
                beanName = objService.value();
                isBean = true;
                break;
            }
            if (annotation instanceof Controller) {
                Controller objController = (Controller) annotation;
                beanName = objController.value();
                isBean = true;
                break;
            }
            if (annotation instanceof Repository) {
                Repository objRepository = (Repository) annotation;
                beanName = objRepository.value();
                isBean = true;
                break;
            }
        }
        if (isBean) {
            if (StringUtil.isEmpty(beanName)) {
                beanName = StringUtil.uncapitalize(beanType.getSimpleName());
            }
        } else {
            LOGGER.error("class:" + beanType.getName() + " is not a bean of Spring");
            return null;
        }
        Object obj = getBeanFromSpringContext(beanName);
        if (obj == null) {
            LOGGER.error("can not getBean from spring context with beanName:" + beanName);
            return null;
        }
        if (beanType.isInstance(obj)) {
            return (T) obj;
        }
        LOGGER.error("the type of bean named '" + beanName + "' is " + obj.getClass().getName()
            + ",but the required Type is " + beanType.getName());
        return null;
    }
    
    /**
     * 
     * @param beanName bean名称
     * @param <T> bean类型
     * @param beanType bean的类型
     * @return bean对象
     */
    public static <T> T getBeanFromSpringContext(String beanName, Class<T> beanType) {
        if (getSpringContext() == null) {
            return null;
        }
        return getSpringContext().getBean(beanName, beanType);
    }
    
    /**
     * 
     * @return bean容器
     */
    public static ApplicationContext getSpringContext() {
        return TopBeanRegister.getContext();
    }
    
}
