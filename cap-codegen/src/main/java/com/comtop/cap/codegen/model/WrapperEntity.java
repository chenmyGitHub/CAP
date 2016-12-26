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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.comtop.bpms.client.ClientFactory;
import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.cap.bm.metadata.common.storage.CacheOperator;
import com.comtop.cap.bm.metadata.entity.model.EntityAttributeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityRelationshipVO;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.MethodType;
import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.codegen.util.CapCodegenUtils;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.corm.resource.util.CollectionUtils;

/**
 * 
 * 实体包装类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌 新建
 * @version 2016年06月06日 许畅 修改
 */
public final class WrapperEntity implements Observer {
    
    /** 实体 */
    private final EntityVO entity;
    
    /** VO名称格式 */
    private final String format;
    
    /** 实体VO全类名 */
    private final String entityVOClassFullName;
    
    /** 实体属性包装属性对象集合 */
    private final List<WrapperAttribute> wrapperAttributes;
    
    /** 实体方法包装方法对象集合 */
    private final List<WrapperMethod> wrapperMethods;
    
    /** 包装重写方法 */
    private final List<WrapperOverrideMethod> wrapperOverrideMethods;
    
    /** VO的导入类 */
    private final Set<String> voImports;
    
    /** facade实现层的导入类 */
    private final Set<String> impFacadeImports;
    
    /** facade抽象层的导入类 */
    private final Set<String> absFacadeImports;
    
    /** service抽象层的导入类 */
    private final Set<String> absServiceImports;
    
    /** service实现层的导入类 */
    private final Set<String> impServiceImports;
    
    /** 异常 */
    private final Set<WrapperException> exceptions;
    
    /** 实体的主键属性名称 */
    private String primaryKeyName;
    
    /**
     * @return the primaryKeyName
     */
    public String getPrimaryKeyName() {
        return primaryKeyName;
    }
    
    /** 默认已经引入的vo类 */
    private final List<String> defaultVOImport = new ArrayList<String>(2);
    
    /**
     * 构造函数
     * 
     * @param entity
     *            实体对象
     * @param format
     *            VO名称格式
     */
    public WrapperEntity(EntityVO entity, String format) {
        this.entity = entity;
        // 这两个引入文件已经在ftl模板中默认引入了
        String strParentVOFullName = CapCodegenUtils.getFullClassNameByEntityId(this.entity.getParentEntityId());
        defaultVOImport.add(strParentVOFullName);
        defaultVOImport.add("comtop.org.directwebremoting.annotations.DataTransferObject");
        //
        this.format = format;
        this.entityVOClassFullName = CapCodegenUtils.getFullClassNameByEntityId(this.entity.getModelId());
        this.voImports = new HashSet<String>();
        this.absFacadeImports = new HashSet<String>();
        this.impFacadeImports = new HashSet<String>();
        this.absServiceImports = new HashSet<String>();
        this.impServiceImports = new HashSet<String>();
        this.exceptions = new HashSet<WrapperException>();
        this.wrapperAttributes = wrapperAttribute(entity.getAttributes(), this.entity.getLstRelation());
        this.wrapperMethods = wrapperMethod(entity.getMethods());
        this.wrapperOverrideMethods = wrapperOverrideMethods(entity.getMethods());
    }
    
    /**
     * 包装实体的属性
     * 
     * @param attributes
     *            实体属性
     * @param lstRelation
     *            实体属性关联关系
     * @return 包装后的实体属性
     */
    private List<WrapperAttribute> wrapperAttribute(final List<EntityAttributeVO> attributes,
        final List<EntityRelationshipVO> lstRelation) {
        List<WrapperAttribute> lstWrapperAttr = new ArrayList<WrapperAttribute>();
        // 包装实体属性
        for (EntityAttributeVO objAttribute : attributes) {
            lstWrapperAttr.add(new WrapperAttribute(objAttribute, lstRelation, this, isProcessable()));
            if (objAttribute.isPrimaryKey()) {
                primaryKeyName = objAttribute.getEngName();
            }
        }
        return lstWrapperAttr;
    }
    
    /**
     * 包装方法集合
     * 
     * 
     * @param methods
     *            方法集合
     * @return 包装后的方法集合
     */
    private List<WrapperMethod> wrapperMethod(final List<MethodVO> methods) {
        // 包装实体方法
        List<WrapperMethod> lstWrapperMethod = new ArrayList<WrapperMethod>();
        if (methods != null && methods.size() > 0) {
            for (MethodVO objMethod : methods) {
                WrapperMethod objWrapperMethod = new WrapperMethod(objMethod, this.wrapperAttributes, this, this.entity);
                
                lstWrapperMethod.add(objWrapperMethod);
            }
        }
        return lstWrapperMethod;
    }
    
    /**
     * 包装查询重写方法集合
     * 
     * @param methods
     *            方法集合
     * @return 包装后的查询重写方法
     */
    private List<WrapperOverrideMethod> wrapperOverrideMethods(final List<MethodVO> methods) {
        List<WrapperOverrideMethod> overrideMethods = new ArrayList<WrapperOverrideMethod>();
        if (methods != null && methods.size() > 0) {
            for (MethodVO objMethod : methods) {
                if (MethodType.QUERY_EXTEND.getValue().equals(objMethod.getMethodType())) {
                    WrapperOverrideMethod overrideMethod = new WrapperOverrideMethod(objMethod, this);
                    overrideMethods.add(overrideMethod);
                    
                    MethodVO countMethodVO = this.wrapperCountMethod(objMethod);
                    if (countMethodVO != null) {
                        WrapperOverrideMethod overrideCountMethod = new WrapperOverrideMethod(countMethodVO, this);
                        overrideMethods.add(overrideCountMethod);
                    }
                }
            }
        }
        // 包装父类方法包括CapWorkflow和CapBase两个实体
        this.wrapperParentMethods(overrideMethods);
        
        return overrideMethods;
    }
    
    /**
     * 包装父类方法,包括CapWorkflow和CapBase两个实体
     * 
     * @param overrideMethods
     *            重写的方法集合
     */
    private void wrapperParentMethods(List<WrapperOverrideMethod> overrideMethods) {
        EntityVO parentVO = getEntity();
        while (StringUtil.isNotEmpty(parentVO.getParentEntityId())) {
            // 递归查找父类实体直至没有父类实体为止
            String parentEntityId = parentVO.getParentEntityId();
            parentVO = (EntityVO) CacheOperator.readById(parentEntityId);
            if (parentVO == null)
                break;
            
            List<MethodVO> methods = parentVO.getMethods();
            addOverrideMethods(overrideMethods, methods);
        }
    }
    
    /**
     * 添加父类实体的sql方法到 overrideMethods集合中
     * 
     * @param overrideMethods 查询重写方法集合(包括了父实体的sql方法)
     * @param methods 父实体方法集合
     */
    private void addOverrideMethods(List<WrapperOverrideMethod> overrideMethods, List<MethodVO> methods) {
    	if(CollectionUtils.isEmpty(methods))
    		return;
    	
        for (MethodVO targetMethodVO : methods) {
            // 如果该方法已重写则不用父类方法
            if (WrapperOverrideMethod.hasOverride(overrideMethods, targetMethodVO)) {
                continue;
            }
            if (targetMethodVO.getQueryExtend() != null) {
                overrideMethods.add(new WrapperOverrideMethod(targetMethodVO, this));
            }
        }
    }
    
    /**
     * 包装查询数量方法
     * 
     * @param objMethod 源方法
     * @return 查询数量方法
     */
    private MethodVO wrapperCountMethod(MethodVO objMethod) {
        List<WrapperOverrideMethod> pMethods = new ArrayList<WrapperOverrideMethod>();
        wrapperParentMethods(pMethods);
        
        // 根据唯一的别名查找方法
        if (StringUtil.isNotEmpty(objMethod.getAliasName())) {
            // 当前实现类的方法别名
            String overrideAliasName = objMethod.getAliasName();
            // 父类的方法别名
            String aliasName = overrideAliasName.substring("override_".length(), overrideAliasName.length());
            MethodVO countMethodVO = WrapperOverrideMethod.getMethod(aliasName, pMethods);
            if (countMethodVO != null) {
                countMethodVO.getQueryExtend().setMybatisSQL(
                    "SELECT COUNT(1) FROM \n (" + objMethod.getQueryExtend().getMybatisSQL() + ")");
                countMethodVO.getQueryExtend().setMethodId(UUID.randomUUID().toString().replaceAll("-", ""));
                countMethodVO.setMethodId(UUID.randomUUID().toString().replaceAll("-", ""));
            }
            return countMethodVO;
        }
        
        return null;
    }
    
    /**
     * 获取包路径
     * 
     * @return 包路径
     */
    public String getPackagePath() {
        return this.entity.getModelPackage();
    }
    
    /**
     * 获取类模式
     * 
     * @return 类模式
     */
    public String getClassPattern() {
        return this.entity.getClassPattern();
    }
    
    /**
     * 获取表名称
     * 
     * @return 表名称
     */
    public String getTable() {
        return this.entity.getDbObjectName();
    }
    
    /**
     * 获取类名称
     * 
     * @return 类名称
     */
    public String getClassName() {
        return MessageFormat.format(format, getEntityName());
    }
    
    /**
     * 获取自定义默认查询条件
     * 
     * @return 获取自定义查询条件
     */
    public String getCustomSqlCondition() {
        return entity.getCustomSqlCondition();
    }
    
    /**
     * @return 是否启用自定义默认查询条件
     */
    public boolean isCustomSqlConditionEnable() {
        return entity.isCustomSqlConditionEnable();
    }
    
    /**
     * 获取实体名称
     * 
     * @return 类名称
     */
    public String getEntityName() {
        return CapCodegenUtils.firstLetterToUpper(this.entity.getEngName());
    }
    
    /**
     * 实体VO全类名
     * 
     * @return 实体VO全类名
     */
    public String getEntityVOClassFullName() {
        return this.entityVOClassFullName;
    }
    
    /**
     * 获取注释
     * 
     * @return 注释
     */
    public String getComment() {
        String strComment = StringUtil.isBlank(this.entity.getDescription()) ? this.entity.getChName() : this.entity
            .getDescription();
        return StringUtil.isBlank(strComment) ? this.entity.getEngName() : strComment;
    }
    
    /**
     * @return 获取 processId属性值
     */
    public String getProcessId() {
        if (this.entity.getProcessId() == null) {
            return "";
        }
        return this.entity.getProcessId();
    }
    
    /**
     * @return 是否启用流程
     */
    public boolean isProcessable() {
        return StringUtil.isNotBlank(getProcessId());
    }
    
    /**
     * 是否增加实体别名注解
     * 
     * @return 是否增加实体别名
     */
    public boolean isEntityAlias() {
        if (StringUtil.isBlank(entity.getAliasName())) {
            return false;
        }
        
		if ("abstract".equals(entity.getClassPattern())) {
			return false;
		}
        
        if (!entity.getAliasName().equalsIgnoreCase(entity.getEngName()))
            return true;
        
        return false;
    }
    
    /**
     * @return 获取待办表名
     * @throws AbstractBpmsException
     *             工作流异常
     */
    public String getTodoTableName() throws AbstractBpmsException {
        return ClientFactory.getBPMSConfigService().readToDoTaskTableName(getProcessId());
    }
    
    /**
     * @return 获取已办表名
     * @throws AbstractBpmsException
     *             工作流异常
     */
    public String getDoneTableName() throws AbstractBpmsException {
        return ClientFactory.getBPMSConfigService().readDoneTaskTableName(getProcessId());
    }
    
    /**
     * @return 获取 entityVO属性值
     */
    public EntityVO getEntity() {
        return entity;
    }
    
    /**
     * @return 获取 entityVO属性值
     */
    public EntityVO getParentEntity() {
        return entity.getParentEntity();
    }
    
    /**
     * @return 获取 entityVO属性值
     */
    public String getParentEntityId() {
        return entity.getParentEntityId();
    }
    
    /**
     * @return 获取 wrapperAttributes属性值
     */
    public List<WrapperAttribute> getWrapperAttributes() {
        return this.wrapperAttributes;
    }
    
    /**
     * @return 获取 wrapperMethods属性值
     */
    public List<WrapperMethod> getWrapperMethods() {
        return this.wrapperMethods;
    }
    
    /**
     * @return 获取voImports属性值
     */
    public Set<String> getVoImports() {
        return voImports;
    }
    
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
    
    /**
     * @return 获取absServiceImports属性值
     */
    public Set<String> getAbsServiceImports() {
        return absServiceImports;
    }
    
    /**
     * @return 获取impServiceImports属性值
     */
    public Set<String> getImpServiceImports() {
        return impServiceImports;
    }
    
    /**
     * 获取实体类型
     * 
     * @return 实体类型
     */
    public String getEntityType() {
        return this.entity.getEntityType();
    }
    
    /**
     * @return 获取 exceptions属性值
     */
    public Set<WrapperException> getExceptions() {
        return exceptions;
    }
    
    /**
     * 更新实体信息（主要为导入类）
     * 
     * @param o
     *            observable 对象。
     * @param arg
     *            传递给 notifyObservers方法的参数
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof ImportNotifyArgs) { // 导入
            ImportNotifyArgs objArg = (ImportNotifyArgs) arg;
            String strClassName = objArg.getClassName();
            boolean canIgnore = canIgnoreImport(strClassName);
            if (canIgnore) {
                return;
            }
            switch (objArg.getImportType()) {
                case ImportNotifyArgs.VO_IMPORT:
                    if (!(entityVOClassFullName.equals(strClassName) || defaultVOImport.contains(strClassName))) {
                        voImports.add(strClassName);
                    }
                    break;
                case ImportNotifyArgs.ABS_FACADE_IMPORT:
                    if (!entityVOClassFullName.equals(strClassName)) {
                        absFacadeImports.add(strClassName);
                    }
                    break;
                case ImportNotifyArgs.IMP_FACADE_IMPORT:
                    if (!entityVOClassFullName.equals(strClassName)) {
                        impFacadeImports.add(strClassName);
                    }
                    break;
                case ImportNotifyArgs.ABS_SERVICE_IMPORT:
                    if (!entityVOClassFullName.equals(strClassName)) {
                        absServiceImports.add(strClassName);
                    }
                    break;
                case ImportNotifyArgs.IMP_SERVICE_IMPORT:
                    if (!entityVOClassFullName.equals(strClassName)) {
                        impServiceImports.add(strClassName);
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
     * 是否可以忽略的引入（java.lang.*是可以忽略引入的）
     * 
     * @param className
     *            类的全路径
     * @return true表示可忽略；false表示不可忽略。
     */
    private static boolean canIgnoreImport(String className) {
        Pattern pattern = Pattern.compile("^java\\.lang\\..+");
        Matcher matcher = pattern.matcher(className);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    
    /**
     * @return the wrapperOverrideMethods
     */
    public List<WrapperOverrideMethod> getWrapperOverrideMethods() {
        return wrapperOverrideMethods;
    }
    
    /**
     * 
     * 引入包更新参数
     *
     * @author 龚斌
     * @since 1.0
     * @version 2015年10月19日 龚斌
     */
    static class ImportNotifyArgs {
        
        /** 更新VO层的导入包 */
        public static final int VO_IMPORT = 0;
        
        /** 更新Facade抽象层的导入包 */
        public static final int IMP_FACADE_IMPORT = 1;
        
        /** 更新Facade实现层的导入包 */
        public static final int ABS_FACADE_IMPORT = 2;
        
        /** 更新service抽象层的导入包 */
        public static final int ABS_SERVICE_IMPORT = 3;
        
        /** 更新service实现层的导入包 */
        public static final int IMP_SERVICE_IMPORT = 4;
        
        /** 导入类型 */
        private final int importType;
        
        /** 类名称 */
        private final String className;
        
        /** 源 */
        private final Object source;
        
        /**
         * 构造函数
         * 
         * @param importType
         *            导入类型
         * @param className
         *            类名称
         * @param source
         *            源
         */
        ImportNotifyArgs(final int importType, final String className, final Object source) {
            super();
            this.importType = importType;
            this.className = className;
            this.source = source;
        }
        
        /**
         * @return 获取 importType属性值
         */
        public int getImportType() {
            return importType;
        }
        
        /**
         * @return 获取 className属性值
         */
        public String getClassName() {
            return className;
        }
        
        /**
         * @return 获取 source属性值
         */
        public Object getSource() {
            return source;
        }
    }
}
