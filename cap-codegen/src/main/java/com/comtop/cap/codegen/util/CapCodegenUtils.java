/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.util;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.CacheOperator;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.ParamCategory;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.bm.metadata.pkg.model.ProjectVO;
import com.comtop.cap.runtime.core.CipCompileUtil;

/**
 * CAP代码生成工具类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月21日 龚斌
 */
public final class CapCodegenUtils {
    
    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(CapCodegenUtils.class);
    
    /**
     * 构造函数
     */
    private CapCodegenUtils() {
    }
    
    /**
     * 
     * 根据实体id，拼装实体的全路径名
     * 如entityId为com.comtop.entity.Project,则FullClassName为com.comtop.model.ProjectVO
     * 
     * @param entityId 实体id
     * @return 类型全路径名
     */
    public static String getFullClassNameByEntityId(String entityId) {
        String targetStr = ".entity.";
        int lastIndex = entityId.lastIndexOf(targetStr);
        if (lastIndex == -1) {
            throw new RuntimeException("非法的实体id：" + entityId);
        }
        String entityClassName = entityId.substring(0, lastIndex) + ".model."
            + entityId.substring(lastIndex + targetStr.length());
        if (!entityClassName.endsWith("VO")) {
            entityClassName += "VO";
        }
        return entityClassName;
    }
    
    /**
     * 
     * 根据实体id，拼装实体的类名
     * 如entityId为com.comtop.entity.Project,则ClassName为ProjectVO
     * 
     * @param entityId 实体id
     * @return 类型名称
     */
    public static String getClassNameByEntityId(String entityId) {
        int lastIndex = entityId.lastIndexOf(".");
        return entityId.substring(lastIndex + 1) + "VO";
    }
    
    /**
     * 
     * 根据实体id，获取实体名称（首字母大写）
     * 
     * @param entityId 实体id
     * @return 类型名称
     */
    public static String getEntityNameByEntityId(String entityId) {
        int lastIndex = entityId.lastIndexOf(".");
        return entityId.substring(lastIndex + 1);
    }
    
    /**
     * 
     * 根据实体id，获取实体名称（首字母大写）
     * 
     * @param entityId 实体id
     * @return 类型名称
     */
    public static String getEntityAliasNameByEntityId(String entityId) {
        EntityVO entity = (EntityVO) CacheOperator.readById(entityId);
        return entity != null ? (StringUtil.isNotBlank(entity.getAliasName()) ? entity.getAliasName()
            : getEntityNameByEntityId(entityId)) : getEntityNameByEntityId(entityId);
    }
    
    /**
     * 
     * 首字母大写
     * 
     * @param str str
     * @return 首字母大写
     */
    public static String firstLetterToUpper(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    /**
     * 
     * 首字母小写
     * 
     * @param str String
     * @return 首字母小写
     */
    public static String firstLetterToLower(String str) {
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
    
    /**
     * 构建编译代码
     * 
     * @param projectPath 工程路径
     * @param packagePath 包路径
     */
    public static void executeBuilderProject(final String projectPath, final String packagePath) {
        // 执行批处理文件
        LOGGER.info("编译构建代码开始！");
        try {
            CipCompileUtil.getInstance().compileModule(projectPath, packagePath);
            LOGGER.info("编译构建代码成功！");
        } catch (Exception e) {
            LOGGER.info("编译构建代码失败！");
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 构建编译代码
     * 
     * @param projectPath
     *            工程路径
     * @param packagePath
     *            包路径
     * @param isGenerateCodeModule
     *            是否生成代码模块化
     */
    public static void executeBuilderProject(final String projectPath, final String packagePath,
        final boolean isGenerateCodeModule) {
        // 执行批处理文件
        LOGGER.info("编译构建代码开始！");
        try {
            CipCompileUtil.getInstance().compileModule(projectPath, packagePath, isGenerateCodeModule);
            LOGGER.info("编译构建代码成功！");
        } catch (Exception e) {
            LOGGER.info("编译构建代码失败！");
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 获取实体所在项目
     * 
     * 
     * @return 项目目录
     */
    public static ProjectVO getProject() {
        ProjectVO objProject = new ProjectVO();
        objProject.setSrcDir(PreferenceConfigQueryUtil.getJavaCodePath());
        objProject.setSrcResourceDir(PreferenceConfigQueryUtil.getResourceFilePath());
        objProject.setWebDir(PreferenceConfigQueryUtil.getPageFilePath());
        return objProject;
    }
    
    /**
     * 修正路径(将路径中双斜杠转换为反斜杠，并在目录末尾加上反斜杠)
     * 
     * @param path 原路径
     * @param toDir 是否转为目录路径
     * @return 修正后的路径
     */
    public static String fixPath(final String path, final boolean toDir) {
        String strTempPath = path.replace('\\', '/');
        strTempPath = strTempPath.startsWith("/") ? strTempPath.substring(1) : strTempPath;
        if (toDir && !strTempPath.endsWith("/")) {
            return strTempPath + "/";
        }
        return strTempPath;
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
     * 将数据对象类型转换为java枚举类型
     * 
     * @param ParameterType 数据对象类型
     * @return 枚举类型
     */
    public static String getParamCategoryString(String ParameterType) {
        if (ParameterType == null) {
            return "";
        }
        if (ParamCategory.IN.getValue().equals(ParameterType)) {
            return "IN";
        }
        if (ParamCategory.OUT.getValue().equals(ParameterType) || ParamCategory.RETURN.getValue().equals(ParameterType)) {
            return "OUT";
        }
        return ParameterType;
    }
    
    /**
     * 将数据对象类型转换为java类型
     * 
     * @param databaseType 数据对象类型
     * @return java类型
     */
    public static String tranfJavaTypeToDatabaseType(String databaseType) {
        if (databaseType == null) {
            return "";
        }
        if ("String".equals(databaseType)) {
            return "VARCHAR";
        }
        if ("Date".equals(databaseType)) {
            return "DATE";
        }
        if ("long".equals(databaseType)) {
            return "LONG";
        }
        if ("int".equals(databaseType)) {
            return "INTEGER";
        }
        return databaseType;
        
    }
    
}
