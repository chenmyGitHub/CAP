/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.func.action;

import java.util.List;

import com.comtop.cap.bm.req.func.facade.ReqFunctionRelFacade;
import com.comtop.cap.bm.req.func.model.ReqFunctionRelVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 功能项关系 Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-25 CAP
 */
@DwrProxy
public class ReqFunctionRelAction extends BaseAction {
    
    /** Facade */
    protected final ReqFunctionRelFacade reqFunctionRelFacade = AppBeanUtil.getBean(ReqFunctionRelFacade.class);
    
    /**
     * 
     * 功能项关系
     *
     * @param reqFunctionRel 功能项关系
     * @return 功能项关系
     */
    @RemoteMethod
    public List<ReqFunctionRelVO> queryFunctionRel(final ReqFunctionRelVO reqFunctionRel) {
        return reqFunctionRelFacade.queryFunctionRel(reqFunctionRel);
    }
    
    /**
     * 
     * 保存功能项关系
     *
     * @param reqFunctionRel 功能项关系
     */
    @RemoteMethod
    public void saveFunctionRel(final ReqFunctionRelVO reqFunctionRel) {
        if (StringUtil.isNotBlank(reqFunctionRel.getId())) {
            reqFunctionRelFacade.updateFunctionRel(reqFunctionRel);
        } else {
            String ID = reqFunctionRelFacade.insertFunctionRel(reqFunctionRel);
            reqFunctionRel.setId(ID);
        }
    }
    
    /**
     * 
     * 删除功能项关系
     * 
     * @param reqFunctionRelList 功能项关系
     */
    @RemoteMethod
    public void deleteFunctionRel(final List<ReqFunctionRelVO> reqFunctionRelList) {
        reqFunctionRelFacade.deleteFunctionRel(reqFunctionRelList);
    }
    
    /**
     * 
     * 检查是否重复关联
     * 
     * @param reqFunctionRelVO 功能项
     * @return 结果
     */
    @RemoteMethod
    public boolean checkRelFunctionItemId(final ReqFunctionRelVO reqFunctionRelVO) {
        return reqFunctionRelFacade.checkRelFunctionItemId(reqFunctionRelVO);
    }
    
    /**
     * 
     * 功能项关系
     *
     * @param funcitemRelId 功能项关系Id
     * @return 功能项关系
     */
    @RemoteMethod
    public ReqFunctionRelVO queryFunctionRelById(String funcitemRelId) {
        return reqFunctionRelFacade.queryFunctionRelById(funcitemRelId);
    }
}
