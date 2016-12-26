/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.config;

/**
 * 
 * 代码层次配置
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class LayerConfig {
    
    /** Java类型 */
    public static final int JAVA_TYPE = 0;
    
    /** 资源文件类型 */
    public static final int RES_TYPE = 1;
    
    /** web页面类型 */
    public static final int WEB_TYPE = 2;
    
    /** Java类型 */
    public static final String JAVA_TYPE_VALUE = "java";
    
    /** 资源文件类型 */
    public static final String RES_TYPE_VALUE = "resource";
    
    /** web页面类型 */
    public static final String WEB_TYPE_VALUE = "web";
    
    /** 层次名称 */
    private String name;
    
    /** 层次类型 */
    private int sourceType;
    
    /** 源文件名称模式 */
    private String[] sourceNamePattern;
    
    /** 代码模板名称 */
    private String[] ftlName;
    
    /** 可重新生产的 */
    private Boolean[] rewritable;
    
    /*** id */
    private String id;
    
    /**ftl文件路径**/
    private String ftlFilePath;
    
    /**
	 * @return the ftlFilePath
	 */
	public String getFtlFilePath() {
		return ftlFilePath;
	}

	/**
	 * @param ftlFilePath the ftlFilePath to set
	 */
	public void setFtlFilePath(String ftlFilePath) {
		this.ftlFilePath = ftlFilePath;
	}

	/*** 处理实现类 */
    private String processClass;
    
    /**对象封装类*/
    private String layerWrapper;
    
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
     * @return 获取 sourceType属性值
     */
    public int getSourceType() {
        return sourceType;
    }
    
    /**
     * @param sourceType 设置 sourceType 属性值为参数值 sourceType
     */
    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }
    
    /**
     * @return 获取 sourceNamePattern属性值
     */
    public String[] getSourceNamePattern() {
        return sourceNamePattern;
    }
    
    /**
     * @param sourceNamePattern 设置 sourceNamePattern 属性值为参数值 sourceNamePattern
     */
    public void setSourceNamePattern(String[] sourceNamePattern) {
        this.sourceNamePattern = sourceNamePattern;
    }
    
    /**
     * @return 获取 ftlName属性值
     */
    public String[] getFtlName() {
        return ftlName;
    }
    
    /**
     * @param ftlName 设置 ftlName 属性值为参数值 ftlName
     */
    public void setFtlName(String[] ftlName) {
        this.ftlName = ftlName;
    }
    
    /**
     * @param rewritable 设置 rewritable 属性值为参数值 rewritable
     */
    public void setRewritable(Boolean[] rewritable) {
        this.rewritable = rewritable;
    }
    
    /**
     * @return 获取 rewritable属性值
     */
    public Boolean[] getRewritable() {
        return rewritable;
    }
    
    /**
     * 返回ftl文件的加载路径
     * @return ftl文件加载路径
     */
    public String[] getFtlLoadPath(){
    	if(this.ftlName == null){
    		return new String[0];
    	}
    	String[] ftlLoadPath = new String[this.ftlName.length];
    	for (int i = 0; i < ftlLoadPath.length; i++) {
    		ftlLoadPath[i] = this.ftlFilePath+"/"+this.ftlName[i];
		}
    	return ftlLoadPath;
    }
    
    /**
     * @param specialftlName 获取指定flt模板对应的文件能否重写生成
     * @return 获取 rewritable属性值
     */
    public Boolean getRewritable(String specialftlName) {
    	if(this.rewritable == null){
    		return true;
    	}
    	String[] ftlLoadPath = getFtlLoadPath();
    	for (int i = 0; i < ftlLoadPath.length; i++) {
			if( ftlLoadPath[i].equals(specialftlName)){
				if(i >= this.rewritable.length){
					return true;
				}
				return this.rewritable[i];
			}
		}
        return true;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return the processClass
     */
    public String getProcessClass() {
        return processClass;
    }
    
    /**
     * @param processClass the processClass to set
     */
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

	/**
	 * @return the layerWrapper
	 */
	public String getLayerWrapper() {
		return layerWrapper;
	}

	/**
	 * @param layerWrapper the layerWrapper to set
	 */
	public void setLayerWrapper(String layerWrapper) {
		this.layerWrapper = layerWrapper;
	}
}
