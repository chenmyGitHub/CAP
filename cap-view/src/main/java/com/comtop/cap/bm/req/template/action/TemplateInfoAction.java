/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.template.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.template.facade.TemplateInfoFacade;
import com.comtop.cap.bm.req.template.model.TemplateInfoVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 需求模板明细Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-9-22 姜子豪
 */
@DwrProxy
public class TemplateInfoAction extends BaseAction {
    
    /** 需求模板明细Facade */
    protected final TemplateInfoFacade templateInfoFacade = AppBeanUtil.getBean(TemplateInfoFacade.class);
    
    /**
     * 通过需求模板明细ID查询需求模板明细
     * 
     * @param templateInfoId 需求模板明细ID
     * @return 需求模板明细对象
     */
    @RemoteMethod
    public TemplateInfoVO queryTemplateInfoById(final String templateInfoId) {
        TemplateInfoVO objTemplateInfo = templateInfoFacade.loadTemplateInfoById(templateInfoId);
        if (objTemplateInfo == null) {
            objTemplateInfo = new TemplateInfoVO();
        }
        return objTemplateInfo;
    }
    
    /**
     * 保存需求模板明细
     * 
     * @param templateInfo 需求模板明细对象
     * @return 需求模板明细ID
     */
    @RemoteMethod
    public String saveTemplateInfo(final TemplateInfoVO templateInfo) {
        if (templateInfo.getId() == null) {
            String strId = (String) templateInfoFacade.insertTemplateInfo(templateInfo);
            templateInfo.setId(strId);
        } else {
            templateInfoFacade.updateTemplateInfo(templateInfo);
        }
        return templateInfo.getId();
    }
    
    /**
     * 通过需求模板类型ID查询需求模板明细
     * 
     * @param templateInfo 需求模板明细
     * @param templateTypeId 需求模板id
     * @return 需求模板明细map对象
     */
    @RemoteMethod
    public Map<String, Object> queryTemplateInfoList(final TemplateInfoVO templateInfo, String templateTypeId) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int iCount = templateInfoFacade.queryTemplateInfoCount(templateInfo, templateTypeId);
        List<TemplateInfoVO> templateInfoList = new ArrayList<TemplateInfoVO>();
        if (iCount > 0) {
            templateInfoList = templateInfoFacade.queryTemplateInfoList(templateInfo, templateTypeId);
            if (templateInfoList.isEmpty() || templateInfoList == null) {
                int strPageNo = templateInfo.getPageNo();
                if (strPageNo > 1) {
                    templateInfo.setPageNo(strPageNo - 1);
                } else {
                    templateInfo.setPageNo(1);
                }
                templateInfoList = templateInfoFacade.queryTemplateInfoList(templateInfo, templateTypeId);
            }
        }
        ret.put("list", templateInfoList);
        ret.put("count", iCount);
        return ret;
    }
    
    /**
     * 删除需求模板明细
     * 
     * @param templateInfoList 需求模板明细集合
     */
    @RemoteMethod
    public void deleteTemplateInfoList(final List<TemplateInfoVO> templateInfoList) {
        templateInfoFacade.deleteTemplateInfoList(templateInfoList);
    }
    
}
