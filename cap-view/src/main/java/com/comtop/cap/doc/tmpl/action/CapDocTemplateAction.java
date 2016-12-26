/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.tmpl.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.doc.tmpl.facade.CapDocTemplateFacade;
import com.comtop.cap.doc.tmpl.model.CapDocTemplateVO;
import com.comtop.cip.common.constant.CapNumberConstant;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 文档模板抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-9 CAP
 */
@DwrProxy
public class CapDocTemplateAction extends BaseAction {
    
    /** 文档模板Facade */
    protected final CapDocTemplateFacade capDocTemplateFacade = AppBeanUtil.getBean(CapDocTemplateFacade.class);
    
    /**
     * 通过文档模板ID查询文档模板
     * 
     * @param capDocTemplateId 文档模板ID
     * @return 文档模板对象
     */
    @RemoteMethod
    public CapDocTemplateVO queryCapDocTemplateById(final String capDocTemplateId) {
        CapDocTemplateVO objCapDocTemplate = capDocTemplateFacade.loadCapDocTemplateById(capDocTemplateId);
        if (objCapDocTemplate == null) {
            objCapDocTemplate = new CapDocTemplateVO();
        }
        return objCapDocTemplate;
    }
    
    /**
     * 
     * 查询条数
     * 
     * @param capDocTemplate 文档模板ID
     * @return 文档模板对象
     */
    @RemoteMethod
    public int queryCapDocTemplateCount(final CapDocTemplateVO capDocTemplate) {
        return capDocTemplateFacade.queryCapDocTemplateCount(capDocTemplate);
    }
    
    /**
     * 通过文档模板ID查询文档模板
     * 
     * @param capDocTemplate 文档模板ID
     * @return 文档模板对象
     */
    @RemoteMethod
    public String saveCapDocTemplate(final CapDocTemplateVO capDocTemplate) {
        if (capDocTemplate.getId() == null) {
            String strId = (String) capDocTemplateFacade.insertCapDocTemplate(capDocTemplate);
            capDocTemplate.setId(strId);
        } else {
            capDocTemplateFacade.updateCapDocTemplate(capDocTemplate);
        }
        return capDocTemplate.getId();
    }
    
    /**
     * 通过文档模板ID查询文档模板
     * 
     * @param capDocTemplate 文档模板
     * @return 文档模板map对象
     */
    @RemoteMethod
    public Map<String, Object> queryCapDocTemplateList(final CapDocTemplateVO capDocTemplate) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        List<CapDocTemplateVO> capDocTemplateList = capDocTemplateFacade.queryCapDocTemplateList(capDocTemplate);
        int count = CapNumberConstant.NUMBER_INT_ZERO;
        if (capDocTemplateList != null) {
            count = capDocTemplateFacade.queryCapDocTemplateCount(capDocTemplate);
        }
        ret.put("list", capDocTemplateList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除文档模板
     * 
     * @param capDocTemplateList 文档模板集合
     */
    @RemoteMethod
    public void deleteCapDocTemplateList(final List<CapDocTemplateVO> capDocTemplateList) {
        capDocTemplateFacade.deleteCapDocTemplateList(capDocTemplateList);
    }
    
}
