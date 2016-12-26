/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cap.codegen.config.LayerConfig;

/**
 * 
 * 代码生成器
 * 
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class ServiceFacadeGenerator extends AbstractGenerator<ServiceObjectVO> {

	/** 用于服务见面代码生成业务层次 ：包括facade */
    public static final Set<String> SERVICE_BIZ_LAYERS_ID = new HashSet<String>();
    
    static {
    	SERVICE_BIZ_LAYERS_ID.add("abs_servicefacade");
    	SERVICE_BIZ_LAYERS_ID.add("servicefacade");
    }
    
    /**
     * 构造函数
     */
    public ServiceFacadeGenerator() {
        super();
    }
    

	@Override
	public List<ServiceObjectVO> generateByPackageName(String packageName) {
		return null;
	}

	@Override
	public ServiceObjectVO generateById(String id) {
		return null;
	}

	@Override
	public List<ServiceObjectVO> generateByIdList(List ids) {
		return null;
	}

	@Override
	public List<String> getBuilderSourcePath() {
		List<String> lstPath = new ArrayList<String>();
		lstPath.add("facade");
		return lstPath;
	}

	@Override
	protected List<LayerConfig> filerDefultLayerConfig(List<LayerConfig> lstDefaultConfig) {
		List<LayerConfig> lstResult = new ArrayList<LayerConfig>();
		for (LayerConfig layerConfig : lstDefaultConfig) {
			if (SERVICE_BIZ_LAYERS_ID.contains(layerConfig.getId())) {
				lstResult.add(layerConfig);
			}
		}
		return lstResult;
	}

	@Override
	public boolean isGenerateCodeOnLayer(ServiceObjectVO data, LayerConfig layer) {
		return true;
	}
    
}
