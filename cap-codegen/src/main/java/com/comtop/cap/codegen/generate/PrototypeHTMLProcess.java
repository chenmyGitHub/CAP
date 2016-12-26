/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.req.prototype.design.facade.PrototypeFacade;
import com.comtop.cap.bm.req.prototype.design.model.PrototypeVO;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.top.core.jodd.AppContext;

/**
 * 需求建模中的界面原型，生成代码的处理器
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年10月31日 凌晨
 * @param <T> 泛型
 */
public class PrototypeHTMLProcess<T extends PrototypeVO> extends AbstractProcess<PrototypeVO> {
    
    /**
     * 界面原型 facade
     */
    protected com.comtop.cap.bm.req.prototype.design.facade.PrototypeFacade prototypeFacade = AppContext
        .getBean(PrototypeFacade.class);
    
    /**  */
    private final List<String> layerName = new ArrayList<String>();
    
    /**
     * 构造函数
     */
    public PrototypeHTMLProcess() {
        layerName.add("prototypePages");
    }
    
    @Override
    public List<PrototypeVO> generateByPackageName(String packageName) {
        try {
            return prototypeFacade.queryPrototypesByModelPackage(packageName);
        } catch (OperateException e) {
            LOGGER.error("根据packageName生成界面原型html失败，packageName：" + packageName, e);
        }
        return new ArrayList<PrototypeVO>(0);
    }
    
    @Override
    public PrototypeVO generateById(String id) {
        
        try {
            return prototypeFacade.loadModel(id, null);
        } catch (OperateException e) {
            LOGGER.error("根据界面原型Id生成界面原型HTML失败，id：" + id, e);
        }
        return null;
    }
    
    @Override
    public List<PrototypeVO> generateByIdList(List<String> ids) {
        List<PrototypeVO> lstResult = new ArrayList<PrototypeVO>(ids.size());
        try {
            for (int i = 0, len = ids.size(); i < len; i++) {
                lstResult.add(prototypeFacade.loadModel(ids.get(i), null));
            }
        } catch (OperateException e) {
            LOGGER.error("根据界面原型Id集合生成界面原型HTML失败，ids：" + ids, e);
        }
        return lstResult;
    }
    
    @Override
    public List<String> getBuilderSourcePath() {
        return new ArrayList<String>();
    }
    
    @Override
    public boolean isGenerateCodeOnLayer(PrototypeVO data, LayerConfig layer) {
        return true;
    }
    
    @Override
    protected List<LayerConfig> filerDefultLayerConfig(List<LayerConfig> lstDefaultConfig) {
        List<LayerConfig> lstResult = new ArrayList<LayerConfig>();
        for (Iterator<LayerConfig> iterator = lstDefaultConfig.iterator(); iterator.hasNext();) {
            LayerConfig layerConfig = iterator.next();
            if (layerName.contains(layerConfig.getName())) {
                lstResult.add(layerConfig);
            }
        }
        return lstResult;
    }
    
}
