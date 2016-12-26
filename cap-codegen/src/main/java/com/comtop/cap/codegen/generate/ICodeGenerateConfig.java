/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.List;

import com.comtop.cap.bm.metadata.base.model.BaseModel;
import com.comtop.cap.codegen.config.LayerConfig;

import freemarker.template.Configuration;

/**
 * @author 代码生成接口
 * @param <T> 泛型
 *
 */
public interface ICodeGenerateConfig<T extends BaseModel> {
	 
    /**
     * 生成当前包下的所有代码
     * 
     * @param packageName 包名
     * @return 返回baseModel
     */
     List<T> generateByPackageName(String packageName);
    
    /**
     * 根据元数据ID生成layerId的代码
     * 
     * @param id 模板ID
     * @return 返回baseModel
     */
    T generateById(String id);
    
    /**
     * 根据元数据ID数组生成layerId的代码
     * 
     * @param ids 模板IDw集
     * @return 返回baseModel
     */
    List<T> generateByIdList(List<String> ids);

    /**
     * 
     * @return 当前生成代码对应的层次配置
     */
	List<LayerConfig> getLayerConfig();
	
	/**
	 * 
	 * @return 需要编译的源码的路径
	 */
	List<String> getBuilderSourcePath();
	
	/**
	 * 
	 * @param data 元数据
	 * @param layer 层
	 * @return true，元数据在此层上生成代码，false不生成代码
	 */
	boolean isGenerateCodeOnLayer(T data, LayerConfig layer);

	/**
	 * 获取代码生成工程路径
	 * 
	 * @param data
	 *            元数据
	 * @return 返回工程路径
	 */
	String getProjectDir(T data);
	
	
	/**
	 * 查询模块配置是否生成代码模块化(默认为false)
	 * 
	 * @param data
	 *            元数据
	 * 
	 * @return 是否生成代码模块化
	 */
	boolean isGenerateCodeModule(T data);

	/**
	 * 
	 * @return 返回ftl的模板信息
	 */
	Configuration getTemplateConfig();
	
}
