/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.template.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.template.facade.TemplateTypeFacade;
import com.comtop.cap.bm.req.template.model.TemplateTypeVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 需求模板类型Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-9-22 姜子豪
 */
@DwrProxy
public class TemplateTypeAction extends BaseAction {
    
    /** 需求模板类型Facade */
    protected final TemplateTypeFacade templateTypeFacade = AppBeanUtil.getBean(TemplateTypeFacade.class);
    
    /**
     * 获取需求模板类型对象
     * 
     * @return 需求模板类型对象
     */
    @RemoteMethod
    public Map<String, Object> reqTemplateTypeIDLst() {
        Map<String, Object> res = new HashMap<String, Object>(1);
        List<TemplateTypeVO> lstReq = templateTypeFacade.reqTemplateTypeIDLst();
        res.put("list", lstReq);// 集合
        return res;
    }
    
    /**
     * 
     * 新增需求模板类型对象
     *
     * @param templateTypeVO 需求模板类型对象
     * @return 需求模板类型ID
     */
    @RemoteMethod
    public String insetTemplateType(TemplateTypeVO templateTypeVO) {
        return templateTypeFacade.insetTemplateType(templateTypeVO);
    }
    
    /**
     * 
     * 删除需求模板类型对象
     * 
     * @param templateTypeVOLst 需求模板类型对象
     * @return 删除结果
     */
    @RemoteMethod
    public boolean deleteTemplateType(List<TemplateTypeVO> templateTypeVOLst) {
        boolean flag = true;
        for (TemplateTypeVO templateTypeVO : templateTypeVOLst) {
            if (templateTypeFacade.deleteTemplateType(templateTypeVO)) {
                flag = false;
            }
        }
        return flag;
    }
    
    /**
     * 
     * 修改需求模板类型对象
     *
     * @param templateTypeVO 需求模板类型对象
     * @return 需求模板类型ID
     */
    @RemoteMethod
    public boolean updateTemplateType(TemplateTypeVO templateTypeVO) {
        return templateTypeFacade.updateTemplateType(templateTypeVO);
    }
}
