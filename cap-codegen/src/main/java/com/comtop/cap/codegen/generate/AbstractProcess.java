/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.base.model.BaseModel;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.codegen.config.ConfigFactory;
import com.comtop.cap.codegen.config.GeneratorConfig;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.cap.runtime.base.exception.CapRuntimeException;
import com.comtop.cip.jodd.util.StringUtil;

import freemarker.template.Configuration;

/**
 * 抽象模板处理器
 * 
 * @author 郑重
 * @version 2015-6-18 郑重
 * @param <T> 泛型
 */
public abstract class AbstractProcess<T extends BaseModel> implements ICodeGenerateConfig<T>{
    
    /** 日志 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractProcess.class);
    
    /***/
    protected GeneratorConfig config = ConfigFactory.getInstance().getDefaultConfig();

    /***/
    protected Configuration objTemplateConfig;
    
    /**
     * 
     */
    public AbstractProcess(){
    	 objTemplateConfig = new Configuration();
         objTemplateConfig.setClassForTemplateLoading(ICodeGenerateConfig.class, config.getFtlRoot());
         objTemplateConfig.setDefaultEncoding("UTF-8"); // 这个一定要设置，不然在生成的页面中 会乱码
    }

	@Override
	public List<LayerConfig> getLayerConfig() {
		List<LayerConfig> lstDefaultConfig = config.getLayers();
		return filerDefultLayerConfig(new ArrayList<LayerConfig>(lstDefaultConfig));
	}

	@Override
	public String getProjectDir(T data) {
		return getCodePath();
	}
	
	/**
   	 * 通过首选项配置xml获取代码路径
   	 * 
   	 * @return 代码工程路径
   	 */
   	private static String getCodePath() {
   		String codePath = PreferenceConfigQueryUtil.getCodePath();
   		if (StringUtil.isEmpty(codePath)){
   			throw new CapRuntimeException("请在首选项中配置项目工程路径");
   		}
   		return codePath;
   	}
   	
	/**
	 * 是否生成代码模块化
	 * 
	 * @return 是否生成代码模块化
	 */
	@Override
	public boolean isGenerateCodeModule(T data) {
		return false;
	}
   	
	@Override
	public Configuration getTemplateConfig() {
		return objTemplateConfig;
	}
    
	/**
	 * 过来代码层级
	 * @param lstDefaultConfig 默认配置的代码层级
	 * @return 要生成的代码层级
	 */
    abstract protected List<LayerConfig> filerDefultLayerConfig(List<LayerConfig> lstDefaultConfig);
}
