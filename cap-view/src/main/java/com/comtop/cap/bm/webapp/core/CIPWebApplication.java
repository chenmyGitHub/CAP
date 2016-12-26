/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.core;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.comtop.cap.runtime.base.appservice.CapWorkflowCallbackExtend;
import com.comtop.cap.runtime.base.appservice.CapWorkflowPlugin;
import com.comtop.cap.runtime.base.appservice.CapWorkflowPlugins;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cap.runtime.spring.SpringBeanRegisterUtil;
import com.comtop.cip.jodd.introspector.ClassDescriptor;
import com.comtop.cip.jodd.introspector.ClassIntrospector;
import com.comtop.cip.jodd.introspector.MethodDescriptor;
import com.comtop.cip.jodd.madvoc.component.ActionsManager;
import com.comtop.cip.jodd.madvoc.component.MadvocConfig;
import com.comtop.cip.jodd.madvoc.component.ResultsManager;
import com.comtop.cip.jodd.madvoc.meta.ActionAnnotation;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cip.jodd.madvoc.result.ActionResult;
import com.comtop.cip.jodd.petite.PetiteContainer;
import com.comtop.cip.jodd.proxetta.Proxetta;
import com.comtop.cip.jodd.util.ClassLoaderUtil;
import com.comtop.cip.jodd.util.ReflectUtil;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.cap.runtime.core.CAPAnnotatoinScanner;
import com.comtop.cap.runtime.core.CipCompileUtil;
import com.comtop.cap.runtime.core.InitBean;
import com.comtop.corm.builder.xml.XMLMapperBuilder;
import com.comtop.corm.extend.xml.XMLMapperReload;
import com.comtop.corm.io.Resources;
import com.comtop.corm.resource.core.io.ClassPathResource;
import com.comtop.corm.resource.core.io.Resource;
import com.comtop.corm.session.Configuration;
import com.comtop.top.component.common.aop.init.TransactionMethodUtil;
import com.comtop.top.component.common.systeminit.TopInitHelper;
import com.comtop.top.component.common.systeminit.WebApplication;
import com.comtop.top.core.jodd.AnnotatoinScanner;
import com.comtop.top.core.util.IOUtil;
import comtop.org.directwebremoting.Container;
import comtop.org.directwebremoting.ServerContextFactory;
import comtop.org.directwebremoting.annotations.AnnotationsConfigurator;
import comtop.org.directwebremoting.annotations.DataTransferObject;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.extend.AccessControl;
import comtop.org.directwebremoting.extend.Creator;
import comtop.org.directwebremoting.extend.CreatorManager;
import comtop.org.directwebremoting.extend.Handler;
import comtop.org.directwebremoting.impl.DefaultAccessControl;
import comtop.org.directwebremoting.impl.DefaultContainer;
import comtop.org.directwebremoting.impl.DefaultCreatorManager;
import comtop.org.directwebremoting.servlet.InterfaceHandler;
import comtop.org.directwebremoting.servlet.UrlProcessor;

/**
 * CIPWeb工程
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-12-3 冯展
 */
public class CIPWebApplication extends WebApplication {
    
    /** 日志 */
    private static Logger log = LoggerFactory.getLogger(CIPWebApplication.class);
    
    /** reloadList */
    private final Map<String, Object[]> reloadList = new ConcurrentHashMap<String, Object[]>();
    
    /** CIP容器 */
    private final PetiteContainer cwapc;
    
    /** 动态类加载器 */
    private DynamicClassLoader loader;
    
    /** 是否刷新Spring容器标识 */
    private volatile boolean refresh;
    
    /** ServletContext */
    private ServletContext scontext;
    
    // static {
    // ParserConfig config = ParserConfig.getGlobalInstance();
    // // 注册重写的FastJSON TimestampDeserializer
    // config.putDeserializer(java.sql.Timestamp.class, ExtTimestampDeserializer.EXT_TIMESTAMP_INSTANCE);
    // // 注册重写的FastJSON SqlDateDeserializer
    // config.putDeserializer(java.sql.Date.class, ExtSqlDateDeserializer.EXT_SQLDATE_INSTANCE);
    //
    // // 给soa的jackson处理类，设置timestamp的格式化处理类
    // SimpleModule module = new SimpleModule();
    // module.addDeserializer(Timestamp.class, new CapTimeStampDeserise());
    // module.addDeserializer(Date.class, new CapSqlDateDeserise());
    // SOAJsonUtil.getMapper().registerModule(module);
    //
    // }
    
    /**
     * 构造函数
     */
    public CIPWebApplication() {
        TopInitHelper.initJoddNoDB();
        cwapc = super.providePetiteContainer();
    }
    
    /**
     * 初始化
     * 
     * @see com.comtop.top.component.common.systeminit.WebApplication#init(com.comtop.cip.jodd.madvoc.component.MadvocConfig,
     *      javax.servlet.ServletContext)
     */
    @Override
    public void init(MadvocConfig madvocConfig, final ServletContext servletContext) {
        super.init(madvocConfig, servletContext);
        // init cip compile util;
        CipCompileUtil.init(servletContext);
        
        initWorkflowPlugins();
        this.scontext = servletContext;
        initExt();
        // is debug;
        boolean isDebug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;
        if (!isDebug) {
            return;
        }
        
        Thread traceThread = new Thread(new Runnable() {
            
            @Override
            public void run() {
                traceFileChange(servletContext);
                
                while (true) {
                    try {
                        Thread.sleep(1000 * 1000);
                    } catch (Exception e) {
                        // 休眠被唤醒时，该捕获异常，不该额外处理，继续后续操作
                        log.error("主线程被打断", e);
                    }
                }
            }
        });
        traceThread.setDaemon(true);
        traceThread.setName("trace file");
        traceThread.start();
        
        Thread checker = new Thread(new Runnable() {
            
            @Override
            public void run() {
                while (true) {
                    checkFileChange();
                    
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        // 休眠被唤醒时，该捕获异常，不该额外处理，继续后续操作
                        log.error("主线程被打断", e);
                    }
                }
            }
        });
        checker.setDaemon(true);
        checker.setName("checker");
        checker.start();
    }
    
    /**
     * 扩展初始化
     *
     */
    private void initExt() {
        List<Class> annotations = new ArrayList<Class>();
        annotations.add(InitBean.class);
        CAPAnnotatoinScanner scanner = new CAPAnnotatoinScanner(annotations);
        scanner.setIncludedEntries("com.comtop.*");
        String names = scanner.getClassNames();
        if (StringUtil.isNotBlank(names)) {
            String[] classNames = names.split(",");
            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.newInstance();
                    Method method = clazz.getMethod("init", ServletContext.class);
                    method.invoke(instance, this.scontext);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * 初始化工作流插件
     */
    private void initWorkflowPlugins() {
        List<Class> annotations = new ArrayList<Class>();
        annotations.add(CapWorkflowPlugin.class);
        AnnotatoinScanner scanner = new AnnotatoinScanner(annotations);
        scanner.setIncludedEntries("com.comtop.*");
        String names = scanner.getClassNames();
        if (StringUtil.isNotBlank(names)) {
            String[] classNames = names.split(",");
            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Object ins = AppBeanUtil.getBean(clazz);
                    if (ins != null && ins instanceof CapWorkflowCallbackExtend) {
                        CapWorkflowPlugins.register((CapWorkflowCallbackExtend) ins);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * 跟踪文件变化
     * 
     * @param servletContext context
     * 
     */
    private void traceFileChange(ServletContext servletContext) {
        String paths = "";
        try {
            // 因为runtime工程不能依赖metadata工程，所以暂时使用反射
            Class facadeClazz = Class.forName("com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade");
            Method method = facadeClazz.getDeclaredMethod("getConfig", String.class);
            method.setAccessible(true);
            Object obj = BeanContextUtil.getBean("preferencesFacade");
            Object preferenceConfigVO = method.invoke(obj, "compileThermalPath");
            Class voClazz = Class.forName("com.comtop.cap.bm.metadata.preferencesconfig.model.PreferenceConfigVO");
            Method voMethod = voClazz.getDeclaredMethod("getConfigValue");
            voMethod.setAccessible(true);
            paths = (String) voMethod.invoke(preferenceConfigVO);
        } catch (Exception e) {
            log.error("load  thermal rootPath failed .", e);
        }
        String[] path = null;
        
        if (StringUtil.isNotBlank(paths) && paths.split(";") != null) {
            path = paths.split(";");
        }
        
        if (path == null) {
            String p = servletContext.getRealPath("/WEB-INF/classes");
            path = new String[] { p };
        }
        
        int mask = JNotify.FILE_ANY;
        
        boolean watchSubtree = true;
        
        JNotifyListener listener = new JNotifyListener() {
            
            @Override
            public void fileCreated(int wd, String rootPath, String name) {
                toReload(JNotify.FILE_CREATED, rootPath, name);
                log.info("created " + rootPath + " : " + name);
            }
            
            @Override
            public void fileDeleted(int wd, String rootPath, String name) {
                toReload(JNotify.FILE_DELETED, rootPath, name);
                log.info("deleted " + rootPath + " : " + name);
            }
            
            @Override
            public void fileModified(int wd, String rootPath, String name) {
                toReload(JNotify.FILE_MODIFIED, rootPath, name);
                log.info("deleted " + rootPath + " : " + name);
            }
            
            @Override
            public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
                toReload(JNotify.FILE_RENAMED, rootPath, oldName, newName);
                log.info("renamed " + rootPath + " : " + oldName + " -> " + newName);
            }
            
        };
        
        log.info("Initializing CIP web application");
        log.info("Initializing tomcat thermal loading...");
        log.info("Initializing tomcat thermal watching path {}", Arrays.toString(path));
        try {
            for (String p : path) {
                JNotify.addWatch(p, mask, watchSubtree, listener);
            }
        } catch (JNotifyException e) {
            log.error("Initializing file watch failed!", e);
        }
    }
    
    /**
     * 检查文件变化
     */
    private void checkFileChange() {
        if (reloadList.isEmpty()) {
            return;
        }
        
        List<Object[]> list;
        synchronized (reloadList) {
            if (reloadList.isEmpty()) {
                return;
            }
            
            list = new ArrayList<Object[]>(reloadList.values());
            reloadList.clear();
        }
        refresh = false;
        for (Object[] param : list) {
            reload((Integer) param[0], (String) param[1], (String[]) param[2]);
        }
        if (refresh) {
            WebApplicationContext spring = WebApplicationContextUtils.getWebApplicationContext(scontext);
            if (spring != null && spring instanceof AbstractRefreshableWebApplicationContext) {
                AbstractRefreshableWebApplicationContext app = (AbstractRefreshableWebApplicationContext) spring;
                app.refresh();
                app.close();
            }
        }
    }
    
    /**
     * 重新加载文件
     * 
     * @param opt opt
     * @param rootPath rootPath
     * @param names names
     */
    private void toReload(final int opt, final String rootPath, final String... names) {
        String oldName = names[0];
        reloadList.put(oldName, new Object[] { opt, rootPath, names });
    }
    
    /**
     * 重新加载，并注入JODD容器
     * 
     * @param opt 操作
     * @param rootPath 根路径
     * @param names 文件名称
     */
    private void reload(final int opt, final String rootPath, final String... names) {
        MadvocConfig madvocConfig = cwapc.getBean(MadvocConfig.class);
        if (madvocConfig == null) {
            madvocConfig = this.getComponent(MadvocConfig.class);
        }
        
        madvocConfig.setDetectDuplicatePathsEnabled(false);
        cwapc.getConfig().setDetectDuplicatedBeanNames(false);
        int argLength = names.length;
        String fileName;
        String oldFileName = null;
        if (argLength == 1) {
            fileName = names[0];
        } else {
            oldFileName = names[0];
            fileName = names[1];
        }
        if (fileName.endsWith(".class")) {
            thermalLoadClass(opt, fileName, oldFileName);
        } else if (fileName.endsWith("SQL.xml")) {
            thermalDBConfig(opt, fileName, oldFileName);
        }
    }
    
    /**
     * 热加载数据库配置文件
     * 
     * 
     * @param opt 操作
     * @param fileName 文件
     * @param oldFileName 旧文件
     */
    private void thermalDBConfig(final int opt, String fileName, String oldFileName) {
        InputStream input = null;
        try {
            // Configuration config = coreDAO.getFactory().getConfiguration();
            Field field = XMLMapperReload.class.getDeclaredField("configuration");
            field.setAccessible(true);
            Configuration config = (Configuration) field.get(null);
            
            if (opt != JNotify.FILE_DELETED) {
                if (opt == JNotify.FILE_RENAMED) {
                    config.removeResourceLoaded(oldFileName);
                }
                input = Resources.getResourceAsStream(fileName);
                
                config.removeResourceLoaded(fileName);
                Resource resource = new ClassPathResource(fileName);
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(input, config, fileName,
                    config.getSqlFragments(), resource);
                xmlMapperBuilder.parse();
            } else {
                config.removeResourceLoaded(oldFileName);
            }
        } catch (Exception e) {
            log.error("tomcat thermal loading failed!", e);
        } finally {
            IOUtil.closeQuietly(input);
        }
    }
    
    /**
     * 热加载Class
     * 
     * 
     * @param opt 操作
     * @param fileName 文件名
     * @param oldFileName 原文件 名
     */
    private void thermalLoadClass(final int opt, String fileName, String oldFileName) {
        String name;
        String oldClassName;
        String className = fileName.replace('\\', '.').replace('/', '.').replace(".class", "");
        try {
            loader = new DynamicClassLoader();
            ClassLoaderUtil.setContextClassLoader(loader);
            Class<?> component = ClassLoaderUtil.loadClass(className, loader);
            Class<?> impl = null;
            if (Modifier.isAbstract(component.getModifiers()) && component.getName().contains("abs.Abstract")) { // 当前类是抽象类
                String implName = component.getName().replace("abs.Abstract", "");
                try {
                    impl = ClassLoaderUtil.loadClass(implName, loader);
                } catch (Exception e) {
                    log.error("not found impl!", e);
                }
            }
            ActionsManager objActionsManager = this.getComponent(ActionsManager.class);
            ResultsManager resultsManager = this.getComponent(ResultsManager.class);
            switch (opt) {
                case JNotify.FILE_CREATED:
                    this.dwrThermalLoad(component, null);
                    this.registerCipComponent(component);
                    // checkSpingAnnotation(component);
                    if (impl != null) {
                        name = this.cwapc.resolveBeanName(impl);
                        cwapc.removeBean(name);
                        this.registerCipComponent(impl);
                        // checkSpingAnnotation(impl);
                    }
                    onActionClass(className, objActionsManager);
                    onResultClass(className, resultsManager);
                    break;
                case JNotify.FILE_MODIFIED:
                    this.dwrThermalLoad(component, null);
                    name = this.cwapc.resolveBeanName(component);
                    cwapc.removeBean(name);
                    this.registerCipComponent(component);
                    // checkSpingAnnotation(component);
                    if (impl != null) {
                        name = this.cwapc.resolveBeanName(impl);
                        cwapc.removeBean(name);
                        this.registerCipComponent(impl);
                        // checkSpingAnnotation(component);
                    }
                    onActionClass(className, objActionsManager);
                    onResultClass(className, resultsManager);
                    break;
                case JNotify.FILE_DELETED:
                    try {
                        this.dwrThermalLoad(null, className);
                        cwapc.removeBean(component);
                        removeSpringBean(component);
                        // checkSpingAnnotation(component);
                    } catch (Exception e) {
                        log.debug("error.", e);
                        int index = className.lastIndexOf('.');
                        String simpleName = StringUtil.uncapitalize(className.substring(index));
                        cwapc.removeBean(simpleName);
                    }
                    break;
                case JNotify.FILE_RENAMED:
                    if (oldFileName != null) {
                        oldClassName = oldFileName.replace('\\', '.').replace('/', '.').replace(".class", "");
                        this.dwrThermalLoad(component, oldClassName);
                        // checkSpingAnnotation(component);
                        Class<?> oldComponent;
                        try {
                            oldComponent = ClassLoaderUtil.loadClass(oldClassName);
                            cwapc.removeBean(oldComponent);
                            // checkSpingAnnotation(oldComponent);
                            removeSpringBean(oldComponent);
                        } catch (Exception e) {
                            log.debug("error.", e);
                            int index = oldClassName.lastIndexOf('.');
                            String simpleName = StringUtil.uncapitalize(className.substring(index));
                            cwapc.removeBean(simpleName);
                        }
                    }
                    this.registerCipComponent(component);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("tomcat thermal loading failed!", e);
        }
    }
    
    /**
     * 移除Spring容器中的Bean
     * 
     * @param component 类
     */
    private void removeSpringBean(Class<?> component) {
        if (SpringBeanRegisterUtil.isSpringBean(component)) {
            SpringBeanRegisterUtil.unregisterBean(component);
        }
    }
    
    /**
     * 检查是否含有Sping注解
     * 
     * 
     * @param component 组件
     */
    /*
     * private void checkSpingAnnotation(Class<?> component) {
     * refresh = component != null && component.getAnnotation(Controller.class) != null;
     * }
     */
    
    /**
     * 注册组件
     * 
     * @param component 组件
     */
    public void registerCipComponent(Class<?> component) {
        if (component.isInterface()) {
            return;
        }
        if (SpringBeanRegisterUtil.isSpringFacadeBean(component)) {
            SpringBeanRegisterUtil.registerBean(component);
        } else if (SpringBeanRegisterUtil.isSpringBean(component)) {
            SpringBeanRegisterUtil.registerSpringBean(component);
        } else {
            String name = this.cwapc.resolveBeanName(component);
            this.registerCipComponent(name, component);
        }
    }
    
    /**
     * 注册组件
     * 
     * 
     * @param name 名称
     * @param component 组件
     */
    public void registerCipComponent(final String name, final Class<?> component) {
        Proxetta<?> proxetta = null;
        try {
            Field field = getDeclaredField(this.appCore, "proxetta");
            proxetta = (Proxetta<?>) field.get(this.appCore);
        } catch (Exception e) {
            log.error("注册组件失败", e);
        }
        if (proxetta != null && loader != null) {
            proxetta.setClassLoader(loader);
        }
        if (log.isDebugEnabled()) {
            log.debug("Registering component '" + name + "' of type " + component.getName());
        }
        cwapc.removeBean(name);
        cwapc.removeBean(component);
        cwapc.registerPetiteBean(component, name, null, null, false);
        if (!Modifier.isAbstract(component.getModifiers()) && !component.getName().contains("abs.Abstract")) {
            TransactionMethodUtil.reloadProxy(component.getName());
        }
        
    }
    
    /**
     * 热加载DWR
     * 
     * 
     * @param component 需要热加载的组件
     * @param oldClassName 原Class名称（只有在修改类名或者删除是有效）
     */
    private void dwrThermalLoad(Class<?> component, String oldClassName) {
        if (ServerContextFactory.get() == null) {
            return;
        }
        DefaultContainer container = (DefaultContainer) ServerContextFactory.get().getContainer();
        if (container == null) {
            return;
        }
        try {
            if (component != null) {
                Class<?> impl = component;
                if (Modifier.isAbstract(component.getModifiers()) && component.getName().contains("abs.Abstract")) { // 当前类是抽象类
                    String implName = component.getName().replace("abs.Abstract", "");
                    impl = ClassLoaderUtil.loadClass(implName);
                }
                if (impl.isAnnotationPresent(DwrProxy.class) || impl.isAnnotationPresent(DataTransferObject.class)) { // 存在DWR注解
                    removeComponentFromDwrContainer(impl.getName(), container, impl.isAnnotationPresent(DwrProxy.class));
                    AnnotationsConfigurator configurator = new AnnotationsConfigurator();
                    Method method = AnnotationsConfigurator.class.getDeclaredMethod("processClass", Class.class,
                        Container.class);
                    if (method != null) {
                        method.setAccessible(true);
                        method.invoke(configurator, impl, container);
                    }
                }
            }
            if (StringUtil.isNotBlank(oldClassName)) {
                removeComponentFromDwrContainer(oldClassName, container, true);
            }
        } catch (Exception e) {
            log.error("dwr thermal loading failed!", e);
        }
        
    }
    
    /**
     * 将组件从DWR容器中移除
     * 
     * 
     * @param fullName 全类名称
     * @param container 容器
     * @param isProxy 是否为DWR代理
     */
    private void removeComponentFromDwrContainer(String fullName, DefaultContainer container, boolean isProxy) {
        String simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
        Collection<String> coll = new LinkedList<String>();
        coll.add(fullName);
        coll.add(simpleName);
        DefaultCreatorManager creatorManager;
        DefaultAccessControl control;
        UrlProcessor processor;
        try {
            container.servletDestroyed(coll);
            if (container.getBean(simpleName) != null) {
                Field field = DefaultContainer.class.getDeclaredField("beans");
                if (field != null) {
                    field.setAccessible(true);
                    @SuppressWarnings("rawtypes")
                    Map beans = (Map) field.get(container);
                    beans.remove(fullName);
                }
            }
            
            creatorManager = (DefaultCreatorManager) container.getBean(CreatorManager.class);
            if (creatorManager != null) {
                Field field = DefaultCreatorManager.class.getDeclaredField("creators");
                if (field != null) {
                    field.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    Map<String, Creator> creators = (Map<String, Creator>) field.get(creatorManager);
                    creators.remove(simpleName);
                }
            }
            control = (DefaultAccessControl) container.getBean(AccessControl.class);
            if (control != null) {
                Field field = DefaultAccessControl.class.getDeclaredField("policyMap");
                if (field != null) {
                    field.setAccessible(true);
                    @SuppressWarnings("rawtypes")
                    Map policys = (Map) field.get(control);
                    policys.remove(simpleName);
                }
            }
            
            if (!isProxy) {
                return;
            }
            
            processor = container.getBean(UrlProcessor.class);
            if (processor != null) {
                Field field = UrlProcessor.class.getDeclaredField("urlMapping");
                if (field != null) {
                    field.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    Map<String, Handler> handlers = (Map<String, Handler>) field.get(processor);
                    InterfaceHandler handler = (InterfaceHandler) handlers.get("/interface/");
                    if (handler != null) {
                        Field fieldCache = getDeclaredField(handler, "scriptCache");
                        Field fieldURL = getDeclaredField(handler, "interfaceHandlerUrl");
                        if (fieldCache != null && fieldURL != null) {
                            @SuppressWarnings("rawtypes")
                            Map scripts = (Map) fieldCache.get(handler);
                            String url = (String) fieldURL.get(handler);
                            String jsName = url + simpleName + ".js";
                            Set<String> keys = new HashSet<String>();
                            if (scripts != null) {
                                for (Object key : scripts.keySet()) {
                                    if (((String) key).endsWith(jsName)) {
                                        keys.add((String) key);
                                    }
                                }
                                for (String key : keys) {
                                    scripts.remove(key);
                                }
                            }
                            
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("remove component [" + simpleName + "] from dwr container field!", e);
        }
    }
    
    /**
     * 获取字段
     * 
     * 
     * @param object 对象
     * @param fieldName 字段名
     * @return 字段
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        Field field = null;
        
        Class<?> clazz = object.getClass();
        
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                /* 去除final修饰符的影响，将字段设为可修改的 */
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                return field;
            } catch (Exception e) {
                log.debug("error.", e);
                // 这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                // 如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
                
            }
        }
        
        return null;
    }
    
    /**
     * JODD MVC热加载
     * 
     * @param className 类名称（带包路径）
     * @param actionsManager action管理器
     * @throws ClassNotFoundException 类不存在
     */
    public void onActionClass(String className, ActionsManager actionsManager) throws ClassNotFoundException {
        Class<?> actionClass = ClassLoaderUtil.loadClass(className);
        
        if (checkClass(actionClass) == false) {
            return;
        }
        
        if (actionClass.getAnnotation(MadvocAction.class) == null) {
            return;
        }
        
        ClassDescriptor cd = ClassIntrospector.register(actionClass);
        
        MethodDescriptor[] allMethodDescriptors = cd.getAllMethodDescriptors();
        for (MethodDescriptor methodDescriptor : allMethodDescriptors) {
            if (!methodDescriptor.isPublic()) {
                continue;
            }
            // just public methods
            Method method = methodDescriptor.getMethod();
            MadvocConfig madvocConfig = new MadvocConfig();
            boolean hasAnnotation = false;
            for (ActionAnnotation<?> actionAnnotation : madvocConfig.getActionAnnotationInstances()) {
                if (actionAnnotation.hasAnnotation(method)) {
                    hasAnnotation = true;
                    break;
                }
            }
            if (hasAnnotation == false) {
                continue;
            }
            actionsManager.register(actionClass, method);
        }
    }
    
    /**
     * JODD MVC热加载
     * 
     * @param className 类名
     * @param resultsManager ResultsManager
     * @throws ClassNotFoundException 异常
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void onResultClass(String className, ResultsManager resultsManager) throws ClassNotFoundException {
        Class resultClass = ClassLoaderUtil.loadClass(className);
        if (checkClass(resultClass) == false) {
            return;
        }
        if (ReflectUtil.isSubclass(resultClass, ActionResult.class) == true) {
            resultsManager.register(resultClass);
        }
    }
    
    /**
     * Determines if class should be examined for Madvoc annotations. Array,
     * anonymous, primitive, interfaces and so on should be ignored. Sometimes,
     * checking may fail due to e.g. <code>NoClassDefFoundError</code>; we
     * should continue searching anyway.
     * 
     * @param clazz Calss对象
     * 
     * @return 检查结果
     */
    @SuppressWarnings("rawtypes")
    public boolean checkClass(Class clazz) {
        try {
            if (clazz.isAnonymousClass()) {
                return false;
            }
            if (clazz.isArray() || clazz.isEnum()) {
                return false;
            }
            if (clazz.isInterface()) {
                return false;
            }
            if (clazz.isLocalClass()) {
                return false;
            }
            if ((clazz.isMemberClass() ^ Modifier.isStatic(clazz.getModifiers()))) {
                return false;
            }
            if (clazz.isPrimitive()) {
                return false;
            }
            int modifiers = clazz.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                return false;
            }
            return true;
        } catch (Throwable ignore) {
            log.debug("error.", ignore);
            return false;
        }
    }
    
}
