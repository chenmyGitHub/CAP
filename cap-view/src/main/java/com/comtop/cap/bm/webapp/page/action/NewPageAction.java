/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.page.action;

import java.util.List;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.common.storage.exception.ValidateException;
import com.comtop.cap.bm.metadata.page.desinger.facade.PageFacade;
import com.comtop.cap.bm.metadata.page.desinger.model.PageVO;
import com.comtop.cap.codegen.generate.PageActionGenerator;
import com.comtop.top.core.jodd.AppContext;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2015-6-25 郑重
 */
@DwrProxy
public class NewPageAction {
    
    /**
     * 页面/菜单 facade
     */
    protected PageFacade pageFacade = AppContext.getBean(PageFacade.class);
    
    /**
     * 生成模块下所有代码
     * 
     * @param modelPackage 模块包路径
     */
    @RemoteMethod
    public void generateByPackageName(String modelPackage) {
        PageActionGenerator.generateByPackageNameAndLayerName(modelPackage);
    }
    
    /**
     * 根据pageId生成代码
     * 
     * @param pageVO 页面对象
     * @param genType 代码生成模式，0：生成所有代码;1：仅生成前端代码;
     * @return 页面对象
     * @throws ValidateException 验证失败
     * @throws OperateException 操作异常
     */
    @RemoteMethod
    public PageVO generateById(PageVO pageVO, String genType) throws ValidateException, OperateException {
        PageVO objPageVO = pageFacade.saveModel(pageVO);
        PageActionGenerator.generateByIdAndLayerName(objPageVO.getModelPackage(), objPageVO.getModelId(),genType);
        return objPageVO;
    }
    
    /**
     * 根据pageId List生成代码
     * 
     * @param ids 模板ID集
     * @param modelPackage 模块包路径
     */
    @RemoteMethod
    public void generateByIdList(List<String> ids, String modelPackage) {
        PageActionGenerator.generateByIdListAndLayerName(modelPackage, ids);
    }
}
