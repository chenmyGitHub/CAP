/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.base.model.BaseModel;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.cap.codegen.util.CapCodegenUtils;
import com.comtop.cap.runtime.base.exception.CapMetaDataException;
import com.comtop.cip.jodd.io.FileUtil;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.util.IOUtils;
import com.comtop.cap.bm.metadata.pkg.model.ProjectVO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 数据包装接口
 *
 * @author 罗珍明
 * @since 1.0
 * @version 2016年6月8日 罗珍明
 */
public class GenerateCode {
    
    /** 日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateCode.class);
    
    /**
     * @param packageName 包名
     * @param codeg 代码生成配置
     * @param <T> 泛型
     * 
     */
    public static <T extends BaseModel> void generateByPackageName(String packageName, ICodeGenerateConfig<T> codeg) {
        List<T> lstData = codeg.generateByPackageName(packageName);
        generateCode(packageName, codeg, lstData);
    }
    
    /**
     * @param <T> 泛型
     * @param packageName 包名
     * @param codeg 代码生成配置
     * @param lstData 生成代码的元数据
     */
    public static <T extends BaseModel> void generateCode(String packageName, ICodeGenerateConfig<T> codeg,
        List<T> lstData) {
        if (lstData == null || lstData.isEmpty()) {
            return;
        }
        List<LayerConfig> lstLayers = codeg.getLayerConfig();
        LOGGER.info("开始生成代码...");
        ProjectVO objProject = CapCodegenUtils.getProject();
        String codePath = codeg.getProjectDir(lstData.get(0));
        for (T t : lstData) {
            Map<String, Map<String, Object>> wrapperDataMap = new HashMap<String, Map<String, Object>>();
            for (LayerConfig layer : lstLayers) {
                if (!codeg.isGenerateCodeOnLayer(t, layer)) {
                    continue;
                }
                try {
                    String[] strFltNames = layer.getFtlLoadPath();
                    Map<String, Object> objWrapObj = wrapperDataMap.get(layer.getLayerWrapper());
                    if (objWrapObj == null) {
                        IWrapper<T> objWrapper = createLayerWrapper(layer.getLayerWrapper());
                        objWrapObj = objWrapper.wrapper(t);
                        wrapperDataMap.put(layer.getLayerWrapper(), objWrapObj);
                    }
                    for (int i = 0; i < strFltNames.length; i++) {
                        String strSourceNamePattern = layer.getSourceNamePattern()[i];
                        
                        String strDir = getDirectory(layer, t.getModelPackage(), objProject, codePath);
                        
                        String strFileName = strDir + getSourceFileName(strSourceNamePattern, t.getModelName());
                        File objSourceFile = new File(strFileName);
                        // 如果action已经存在则不生成
                        if (!layer.getRewritable(strFltNames[i]) && objSourceFile.exists()) {
                            continue;
                        }
                        generateCode(objWrapObj, codeg.getTemplateConfig(), strFltNames[i], objSourceFile);
                    }
                } catch (Throwable e) {
                    LOGGER.error("生成失败：" + t.getModelId(), e);
                }
            }
        }
        LOGGER.info("生成代码成功！");
        List<String> lstPath = codeg.getBuilderSourcePath();
		for (String strPath : lstPath) {
			// 代码编译
			CapCodegenUtils.executeBuilderProject(codePath, packageName + "/" + strPath);
		}
    }
    
    /**
     * 获取目标文件的目录
     * 
     * 
     * @param layer 层次配置
     * @param pkgFullPath 包路径
     * @param project 项目
     * @param projectDir 项目所在目录
     * @return 目标文件所在目录
     */
    private static String getDirectory(final LayerConfig layer, final String pkgFullPath, final ProjectVO project,
        final String projectDir) {
        int iType = layer.getSourceType();
        String strLayer = layer.getName();
        String strPrjDir = CapCodegenUtils.fixPath(projectDir, true);
        String strPkgPath = pkgFullPath.replace(".", "/");
        String strDirPath = strPrjDir;
        
        switch (iType) {
            case LayerConfig.RES_TYPE:
                strDirPath += CapCodegenUtils.fixPath(project.getSrcResourceDir(), true);
                strDirPath += strPkgPath;
                break;
            case LayerConfig.WEB_TYPE:
                strDirPath += CapCodegenUtils.fixPath(project.getWebDir(), true);
                strDirPath += getWebRetraivalPath(pkgFullPath);
                break;
            default:
                strDirPath += CapCodegenUtils.fixPath(project.getSrcDir(), true);
                strDirPath += strPkgPath;
                break;
        }
        strDirPath = CapCodegenUtils.fixPath(strDirPath, true)
            + (LayerConfig.WEB_TYPE == iType ? "" : (strLayer.replace('.', '/') + "/"));
        return strDirPath;
    }
    
    /**
     * 获取web项目相对路径
     * 
     * 
     * @param pkgFullPath 项目所在目录
     * @return web项目相对路径
     */
    protected static String getWebRetraivalPath(final String pkgFullPath) {
        String strWebPath = pkgFullPath.replaceFirst(PreferenceConfigQueryUtil.getPageUrlPrefix(), "");
        strWebPath = strWebPath.replace('.', '/');
        return strWebPath;
    }
    
    /**
     * 获取源文件名称
     * 
     * 
     * @param sourceNamePattern 源文件命名模式
     * @param modelName 实体名称
     * @return 源文件名称
     */
    private static String getSourceFileName(final String sourceNamePattern, final String modelName) {
        return MessageFormat.format(sourceNamePattern, StringUtil.capitalize(modelName));
    }
    
    /**
     * 创建layer的数据转换类
     * 
     * @param <T> 泛型
     * 
     * @param layerWrapperClassName layer转换数据类名
     * @return layer的数据封装类实例
     */
    private static <T> IWrapper<T> createLayerWrapper(String layerWrapperClassName) {
        Exception ex = null;
        try {
            Class<?> clazz = Class.forName(layerWrapperClassName);
            Object obj = clazz.newInstance();
            if (!(obj instanceof IWrapper)) {
                throw new CapMetaDataException(layerWrapperClassName + "未实现:" + IWrapper.class.getName());
            }
            return (IWrapper<T>) obj;
        } catch (InstantiationException e) {
            ex = e;
        } catch (IllegalAccessException e) {
            ex = e;
        } catch (ClassNotFoundException e) {
            ex = e;
        }
        throw new CapMetaDataException("生成代码出错。", ex);
    }
    
    /**
     * 生成代码
     * 
     * 
     * @param param 数据
     * @param templateConfig 模板配置
     * @param ftlName 模板文件名称
     * @param srcFile 源文件
     */
    public static void generateCode(final Object param, final Configuration templateConfig, final String ftlName,
        final File srcFile) {
        OutputStream objOS = null;
        OutputStreamWriter objOSW = null;
        Template objCodeTemplate = null;
        Exception ex = null;
        try {
            FileUtil.mkdirs(srcFile.getParentFile().getPath());
            // 获取模版
            objCodeTemplate = templateConfig.getTemplate(ftlName);
            objOS = new FileOutputStream(srcFile);
            objOSW = new OutputStreamWriter(objOS, "UTF-8");
            objCodeTemplate.process(param, objOSW);
        } catch (FileNotFoundException e) {
            ex = e;
        } catch (UnsupportedEncodingException e) {
            ex = e;
        } catch (IOException e) {
            ex = e;
        } catch (TemplateException e) {
            ex = e;
        } finally {
            IOUtils.close(objOS);
            IOUtils.close(objOSW);
        }
        if (ex != null) {
            LOGGER.error("生成代码出错.", ex);
            throw new CapMetaDataException("生成代码出错。", ex);
        }
    }
    
	/**
	 * 根据StringWriter获取ftl模板中内容
	 * 
	 * @param param
	 *            模版参数
	 * @param templateConfig
	 *            模版对象
	 * @param ftlName
	 *            模版名
	 * @return 生成的代码
	 */
	public static String getFtlContent(Object param,
			Configuration templateConfig, String ftlName) {
		Template objCodeTemplate = null;
		Writer objWriter = null;
		Exception ex = null;
		try {
			objCodeTemplate = templateConfig.getTemplate(ftlName);
			objWriter = new StringWriter();
			objCodeTemplate.process(param, objWriter);
		} catch (IOException e) {
			LOGGER.error("生成代码出错." + e.getMessage(), e);
			ex = e;
		} catch (TemplateException e) {
			LOGGER.error("生成代码出错." + e.getMessage(), e);
			ex = e;
		}
		if (ex != null) {
			throw new CapMetaDataException("生成代码出错。", ex);
		}
		return objWriter != null ? objWriter.toString() : null;
	}
    
    /**
     * 根据元数据ID生成layerName的代码
     * 
     * @param <T> 泛型
     * @param packageName 包名
     * @param id 元数据主键
     * @param codeg 代码生成配置
     */
    public static <T extends BaseModel> void generateByIdAndLayerName(String packageName, String id,
        ICodeGenerateConfig<T> codeg) {
        T obj = codeg.generateById(id);
        if (obj == null) {
            return;
        }
        List<T> lst = new ArrayList<T>();
        lst.add(obj);
        generateCode(packageName, codeg, lst);
    }
    
    /**
     * 根据元数据ID List生成layerName的代码
     * 
     * @param <T> 泛型
     * @param packageName 包名
     * @param ids 元数据主键集合
     * @param codeg 代码生成配置
     */
    public static <T extends BaseModel> void generateByIdListAndLayerName(String packageName, List<String> ids,
        ICodeGenerateConfig<T> codeg) {
        List<T> lst = codeg.generateByIdList(ids);
        generateCode(packageName, codeg, lst);
    }
    
}
