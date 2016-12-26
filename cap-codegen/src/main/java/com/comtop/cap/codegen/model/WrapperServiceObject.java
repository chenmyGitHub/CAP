/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cap.codegen.model.WrapperEntity.ImportNotifyArgs;
import com.comtop.cap.codegen.util.CapCodegenUtils;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 服务包装类
 * 
 * 
 * @author 林玉千
 * @since 1.0
 * @version 2016-6-2 林玉千
 */
public final class WrapperServiceObject implements Observer {
    
    /** 实体 */
    private final ServiceObjectVO service;
    
    /** VO名称格式 */
    private final String format;
    
    /** 其他层次的导入类 */
    private final Set<String> imports;
    
    /** facade实现层的导入类 */
    private final Set<String> impFacadeImports;
    
    /** facade抽象层的导入类 */
    private final Set<String> absFacadeImports;
    
    /** 异常 */
    private final Set<WrapperException> exceptions;
    
    /** FIXME */
    private final List<WrapperMethod> wrapperMethods;
    
    /** 实体VO全类名 */
    private final String serviceVOClassFullName;
    
    /**
     * 构造函数
     * 
     * @param service 服务对象
     * @param format VO名称格式
     */
    public WrapperServiceObject(ServiceObjectVO service, String format) {
        this.service = service;
        this.format = format;
        this.absFacadeImports = new HashSet<String>();
        this.impFacadeImports = new HashSet<String>();
        this.serviceVOClassFullName = CapCodegenUtils.getServiceVOClassName(service, format);
        this.imports = new HashSet<String>();
        this.exceptions = new HashSet<WrapperException>();
        this.wrapperMethods = wrapperMethod(service.getMethods());
    }
    
    /**
     * 包装方法集合
     * 
     * 
     * @param methods 方法集合
     * @return 包装后的方法集合
     */
    public List<WrapperMethod> wrapperMethod(List<MethodVO> methods) {
        // 包装实体方法
        List<WrapperMethod> lstWrapperMethod = new ArrayList<WrapperMethod>();
        if (methods != null) {
            for (MethodVO objMethod : methods) {
                WrapperMethod objWrapperMethod = new WrapperMethod(objMethod, new ArrayList<WrapperAttribute>(), this,
                    null);
                lstWrapperMethod.add(objWrapperMethod);
            }
        }
        return lstWrapperMethod;
    }
    
    /**
     * 获取包路径
     * 
     * @return 包路径
     */
    public String getPackagePath() {
        if (this.service.getPackageVO() == null) {
            return null;
        }
        return this.service.getPackageVO().getFullPath();
    }
    
    /**
     * 获取类名称
     * 
     * @return 类名称
     */
    public String getClassName() {
        return MessageFormat.format(format, getServiceName());
    }
    
    /**
     * 获取类名称
     * 
     * @return 类名称
     */
    public String getAliasName() {
        return this.service.getServiceAlias();
    }
    
    /**
     * @return 获取 entityVOClassFullName属性值
     */
    public String getServiceClassFullName() {
        return this.serviceVOClassFullName;
    }
    
    /**
     * 获取实体名称
     * 
     * @return 类名称
     */
    public String getServiceName() {
        String strEnglishName = this.service.getEnglishName();
        String strServiceName = strEnglishName.substring(0, 1).toUpperCase() + strEnglishName.substring(1);
        return strServiceName;
    }
    
    /**
     * 获取注释
     * 
     * @return 注释
     */
    public String getComment() {
        String strComment = StringUtil.isBlank(this.service.getDescription()) ? this.service.getChineseName()
            : this.service.getDescription();
        return StringUtil.isBlank(strComment) ? this.service.getEnglishName() : strComment;
    }
    
    /**
     * @return 获取 entityVO属性值
     */
    public ServiceObjectVO getServiceObject() {
        return service;
    }
    
    /**
     * @return 获取 wrapperMethods属性值
     */
    public List<WrapperMethod> getWrapperMethods() {
        return this.wrapperMethods;
    }
    
    /**
     * @return 获取 voImports属性值
     */
    public Set<String> getImports() {
        return imports;
    }
    
    /**
     * @return 获取 exceptions属性值
     */
    public Set<WrapperException> getExceptions() {
        return exceptions;
    }
    
    /**
     * 更新服务信息
     * 
     * @param o observable 对象。
     * @param arg 传递给 notifyObservers方法的参数
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof ImportNotifyArgs) { // 导入
            ImportNotifyArgs objArg = (ImportNotifyArgs) arg;
            String strClassName = objArg.getClassName();
            switch (objArg.getImportType()) {
                case ImportNotifyArgs.ABS_FACADE_IMPORT:
                    if (!serviceVOClassFullName.equals(strClassName)) {
                        absFacadeImports.add(strClassName);
                    }
                    break;
                case ImportNotifyArgs.IMP_FACADE_IMPORT:
                    if (!serviceVOClassFullName.equals(strClassName)) {
                        impFacadeImports.add(strClassName);
                    }
                    break;
                default:
                    break;
            }
            
        } else if (arg instanceof WrapperException) { // 异常
            this.exceptions.add((WrapperException) arg);
        }
    }
    
    /**
     * 获取service类名
     * 
     * @param service 服务对象
     * @param format 格式
     * @return service类名
     */
    public static String getServiceVOClassName(ServiceObjectVO service, String format) {
        String strPkg = "";
        String strClassName = StringUtil.capitalize(service.getEnglishName());
        if (service.getPackageVO() != null) {
            strPkg = service.getPackageVO().getFullPath();
        }
        if (StringUtil.isNotBlank(strPkg)) {
            strClassName = strPkg + ".model." + MessageFormat.format(format, service.getEnglishName());
        }
        return strClassName;
    }
    
    /**
     * 是否可以忽略的引入（java.lang.*是可以忽略引入的）
     * 
     * @param className 类的全路径
     * @return true表示可忽略；false表示不可忽略。
     */
    // private static boolean canIgnoreImport(String className) {
    // Pattern pattern = Pattern.compile("^java\\.lang\\..+");
    // Matcher matcher = pattern.matcher(className);
    // if (matcher.matches()) {
    // return true;
    // }
    // return false;
    // }
    
    /**
     * @return 获取absFacadeImports属性值
     */
    public Set<String> getAbsFacadeImports() {
        return absFacadeImports;
    }
    
    /**
     * @return 获取impFacadeImports属性值
     */
    public Set<String> getImpFacadeImports() {
        return impFacadeImports;
    }
}
