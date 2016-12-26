/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.config;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.codegen.config.LayerConfig;

/**
 * 
 * 代码生成器配置>
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class GeneratorConfig {
    
    /** 代码生成器配置唯一标示 */
    private String id;
    
    /** 代码生成器配置名称 */
    private String name;
    
    /** 数据包装类名称 */
    private String wrapper;
    
    /** 模板上级路径 */
    private String ftlParentDir;
    
    /** 模板顶层路径 */
    private String ftlRoot;
    
    /** 是否默认 */
    private boolean isDefault;
    
    /** 代码层次配置 */
    private List<LayerConfig> layers;
    
    /**父GenerateConfig的Id*/
    private String parentConfig;
//    
//    /**编译顺序配置***/
//    private List<CompileSortConfig> compileConfig;
//    
//    /**编译路径**/
//    private List<String> compilePath;
//
//	/**
//	 * @return the compileConfig
//	 */
//	public List<CompileSortConfig> getCompileConfig() {
//		return compileConfig;
//	}
//
//	/**
//	 * @param compileConfig the compileConfig to set
//	 */
//	public void setCompileConfig(List<CompileSortConfig> compileConfig) {
//		this.compileConfig = compileConfig;
//	}
//
//	/**
//	 * @return the compilePath
//	 */
//	public List<String> getCompilePath() {
//		return compilePath;
//	}
//
//	/**
//	 * @param compilePath the compilePath to set
//	 */
//	public void setCompilePath(List<String> compilePath) {
//		this.compilePath = compilePath;
//	}

	/**
	 * @return the parentConfig
	 */
	public String getParentConfig() {
		return parentConfig;
	}

	/**
	 * @param parentConfig the parentConfig to set
	 */
	public void setParentConfig(String parentConfig) {
		this.parentConfig = parentConfig;
	}

	/**
	 * @return the ftlRoot
	 */
	public String getFtlRoot() {
		return ftlRoot;
	}

	/**
	 * @param ftlRoot the ftlRoot to set
	 */
	public void setFtlRoot(String ftlRoot) {
		this.ftlRoot = ftlRoot;
	}

	/**
     * @return 获取 id属性值
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id 设置 id 属性值为参数值 id
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return 获取 name属性值
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name 设置 name 属性值为参数值 name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return 获取 wrapper属性值
     */
    public String getWrapper() {
        return wrapper;
    }
    
    /**
     * @param wrapper 设置 wrapper 属性值为参数值 wrapper
     */
    public void setWrapper(String wrapper) {
        this.wrapper = wrapper;
    }
    
    /**
     * @return 获取 ftlParentDir属性值
     */
    public String getFtlParentDir() {
        return ftlParentDir;
    }
    
    /**
     * @param ftlParentDir 设置 ftlParentDir 属性值为参数值 ftlParentDir
     */
    public void setFtlParentDir(String ftlParentDir) {
        this.ftlParentDir = ftlParentDir;
    }
    
    /**
     * @return 获取 layers属性值
     */
    public List<LayerConfig> getLayers() {
        return layers;
    }
    
    /**
     * @param layers 设置 layers 属性值为参数值 layers
     */
    public void setLayers(List<LayerConfig> layers) {
        this.layers = layers;
    }
    
    /**
     * 添加代码层次配置
     * 
     * @param layer 代码层次配置
     */
    public void addLayer(LayerConfig layer) {
        if (this.layers == null) {
            this.layers = new ArrayList<LayerConfig>();
        }
        this.layers.add(layer);
    }
    
    /**
     * @return 获取 isDefault属性值
     */
    public boolean isDefault() {
        return isDefault;
    }
    
    /**
     * @param isDefault 设置 isDefault 属性值为参数值 isDefault
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}
