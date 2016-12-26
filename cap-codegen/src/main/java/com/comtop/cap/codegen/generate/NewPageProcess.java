/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.page.desinger.facade.PageFacade;
import com.comtop.cap.bm.metadata.page.desinger.model.PageVO;
import com.comtop.cap.bm.metadata.sysmodel.model.CapPackageVO;
import com.comtop.cap.bm.metadata.sysmodel.utils.CapSystemModelUtil;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.cap.codegen.util.PageTypeConstant;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.top.core.jodd.AppContext;

/**
 * JSP代码生成处理类
 * 
 * @author 郑重
 * @version 2015-6-22 郑重
 * @param <T> 泛型
 */
public class NewPageProcess<T extends PageVO> extends AbstractProcess<PageVO> {
	
	/***/
	private final List<String> layerName = new ArrayList<String>();
	
	/**生成代码范围*/
    private int genType;
	
	/**
	 * @param genType the genType to set
	 */
	public void setGenType(int genType) {
		this.genType = genType;
	}

	/**
	 * 
	 */
	public NewPageProcess(){
		layerName.add("action");
		layerName.add("newPages");
	}
    
    /**
     * 页面/菜单 facade
     */
    protected PageFacade pageFacade = AppContext.getBean(PageFacade.class);
    
    @Override
    public List<PageVO> generateByPackageName(String packageName) {
        try {
            List<PageVO> lstPageVOs = pageFacade.queryPageListAndAllDepend(packageName);
            List<PageVO> lstResult = new ArrayList<PageVO>(lstPageVOs.size());
            for (int i = 0; i < lstPageVOs.size(); i++) {
                PageVO objPageVO = lstPageVOs.get(i);
				// 自定义页面不生成代码
				if (objPageVO.getPageType() == PageTypeConstant.CUSTOM_PAGE){
					continue;
				}
				lstResult.add(objPageVO);
            }
            return lstResult;
        } catch (OperateException e) {
            LOGGER.error("根据packageName生成页面文件失败，packageName：" + packageName, e);
        }
        return new ArrayList<PageVO>(0);
    }
    
    @Override
    public PageVO generateById(String id) {
        try {
            PageVO objPageVO = pageFacade.loadModel(id, null);
            if (objPageVO.getPageType() == PageTypeConstant.CUSTOM_PAGE){
            	return null;
            }
            return objPageVO;
        } catch (OperateException e) {
            LOGGER.error("根据pageId生成页面文件失败，id：" + id, e);
        }
        return null;
    }
    
    @Override
    public List<PageVO> generateByIdList(List<String> ids) {
        try {
        	List<PageVO> lstResult = new ArrayList<PageVO>(ids.size());
            for (int i = 0; i < ids.size(); i++) {
                PageVO objPageVO = pageFacade.loadModel(ids.get(i), null);
                if (objPageVO.getPageType() == PageTypeConstant.CUSTOM_PAGE){
					continue;
				}
				lstResult.add(objPageVO);
            }
            return lstResult;
        } catch (OperateException e) {
            LOGGER.error("根据pageId生成页面文件失败，ids：" + ids, e);
        }
        return new ArrayList<PageVO>(0);
    }

	@Override
	protected List<LayerConfig> filerDefultLayerConfig(
			List<LayerConfig> lstDefaultConfig) {
		List<LayerConfig> lstResult = new ArrayList<LayerConfig>();
		for (Iterator<LayerConfig> iterator = lstDefaultConfig.iterator(); iterator
				.hasNext();) {
			LayerConfig layerConfig = iterator.next();
			if(layerName.contains(layerConfig.getName())){
				lstResult.add(layerConfig);
			}
		}
		return lstResult;
	}

	@Override
	public List<String> getBuilderSourcePath() {
		List<String> lstPath = new ArrayList<String>();
		lstPath.add("action");
		return lstPath;
	}

	@Override
	public boolean isGenerateCodeOnLayer(PageVO t, LayerConfig layer) {
		if(genType == 1){
			return "newPages".equals(layer.getName());
		}
		return true;
	}
	
	/**
	 * 页面代码生成路径 <br>
	 * 首先取应用信息中的页面工程路径 <br>
	 * 如果页面工程路径没有则取首选项中的工程路径
	 * 
	 * @return 页面代码生成路径
	 */
	@Override
	public String getProjectDir(PageVO pageVO) {
		CapPackageVO capPackageVO = CapSystemModelUtil.queryCapPackageBypage(pageVO);

		if (capPackageVO == null
				|| StringUtil.isBlank(capPackageVO.getPagePath())) {
			return super.getProjectDir(pageVO);
		}

		return capPackageVO.getPagePath();
	}
	
	/**
	 * 查询模块配置是否生成代码模块化
	 * 
	 * @param pageVO
	 *            页面对象
	 * @return 是否生成代码模块化
	 */
	@Override
	public boolean isGenerateCodeModule(PageVO pageVO) {
		CapPackageVO capPackageVO = CapSystemModelUtil.queryCapPackageBypage(pageVO);

		if (capPackageVO == null
				|| StringUtil.isBlank(capPackageVO.getPagePath())) {
			return false;
		}

		return true;
	}
	
}
