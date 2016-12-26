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

import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.bm.metadata.sysmodel.model.CapPackageVO;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * 异常代码生成器
 * 
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class ExceptionGenerator extends AbstractGenerator<ExceptionVO> {

	/** 用于服务见面代码生成业务层次 ：包括facade */
    public static final Set<String> EX_LAYERS = new HashSet<String>();
    
    static {
    	EX_LAYERS.add("exception");
    }
    
    /**
     * 构造函数
     */
    public ExceptionGenerator() {
        super();
    }

	@Override
	public List<ExceptionVO> generateByPackageName(String packageName) {
		return null;
	}

	@Override
	public ExceptionVO generateById(String id) {
		return null;
	}

	@Override
	public List<ExceptionVO> generateByIdList(List<String> ids) {
		return null;
	}

	@Override
	public List<String> getBuilderSourcePath() {
		List<String> lstPath = new ArrayList<String>();
		lstPath.add("exception");
		return lstPath;
	}

	@Override
	protected List<LayerConfig> filerDefultLayerConfig(List<LayerConfig> lstDefaultConfig) {
		List<LayerConfig> lstResult = new ArrayList<LayerConfig>();
		for (LayerConfig layerConfig : lstDefaultConfig) {
			if (EX_LAYERS.contains(layerConfig.getId())) {
				lstResult.add(layerConfig);
			}
		}
		return lstResult;
	}

	@Override
	public boolean isGenerateCodeOnLayer(ExceptionVO data, LayerConfig layer) {
		return true;
	}
	
	/** 
	 * 
	 * @param data 异常信息
	 * @return 代码生成路径
	 */
	@Override
	public String getProjectDir(ExceptionVO data) {
		CapPackageVO capPackageVO = data.getPkg();

		if (capPackageVO == null
				|| StringUtil.isBlank(capPackageVO.getJavaCodePath())) {
			return super.getProjectDir(data);
		}

		return capPackageVO.getJavaCodePath();
	}
	
	/**
	 * 是否生成代码模块化
	 * 
	 * @param data
	 *            异常对象
	 * @return 是否生成代码模块化
	 */
	@Override
	public boolean isGenerateCodeModule(ExceptionVO data) {
		CapPackageVO capPackageVO = data.getPkg();

		if (capPackageVO == null
				|| StringUtil.isBlank(capPackageVO.getJavaCodePath())) {
			return false;
		}

		return true;
	}

}
