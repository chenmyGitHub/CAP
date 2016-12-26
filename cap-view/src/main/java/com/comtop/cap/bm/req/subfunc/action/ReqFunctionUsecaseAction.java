/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.subfunc.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.comtop.cap.bm.req.subfunc.facade.ReqFunctionUsecaseFacade;
import com.comtop.cap.bm.req.subfunc.facade.ReqUsecaseRelFormFacade;
import com.comtop.cap.bm.req.subfunc.model.ReqFunctionUsecaseVO;
import com.comtop.cap.bm.req.subfunc.model.ReqUsecaseRelFormVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 *  功能用例Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-12-22 CAP
 */
@DwrProxy
public class ReqFunctionUsecaseAction extends BaseAction {
    
    /** 功能用例Facade */
    protected final ReqFunctionUsecaseFacade reqFunctionUsecaseFacade = AppBeanUtil.getBean(ReqFunctionUsecaseFacade.class);
    
    /** 功能用例关联业务表单 Facade */
    protected final ReqUsecaseRelFormFacade reqUsecaseRelFormFacade = AppBeanUtil.getBean(ReqUsecaseRelFormFacade.class);
    
    /**
     * 通过功能用例ID查询功能用例
     * 
     * @param reqFunctionUsecaseId 功能用例ID
     * @return 功能用例对象
     */
    @RemoteMethod
    public ReqFunctionUsecaseVO queryReqFunctionUsecaseById(final String reqFunctionUsecaseId) {
        ReqFunctionUsecaseVO objReqFunctionUsecase = reqFunctionUsecaseFacade.loadReqFunctionUsecaseById(reqFunctionUsecaseId);
        if (objReqFunctionUsecase == null) {
            objReqFunctionUsecase = new ReqFunctionUsecaseVO();
        }
        return objReqFunctionUsecase;
    }
    
    /**
     * 通过功能用例ID查询功能用例
     * 
     * @param reqFunctionUsecase 功能用例ID
     * @return 功能用例对象
     */
    @RemoteMethod
    public String saveReqFunctionUsecase(final ReqFunctionUsecaseVO reqFunctionUsecase) {
        if (reqFunctionUsecase.getId() == null) {
            String strId = (String) reqFunctionUsecaseFacade.insertReqFunctionUsecase(reqFunctionUsecase);
            reqFunctionUsecase.setId(strId);
        } else {
            reqFunctionUsecaseFacade.updateReqFunctionUsecase(reqFunctionUsecase);
	        //删除原有的功能用例关联业务表单
	        reqUsecaseRelFormFacade.deleteReqUsecaseRelFormByUsecaseId(reqFunctionUsecase.getId());
	        //保存功能用例关联业务表单
	        if(StringUtils.isNotBlank(reqFunctionUsecase.getBizFormIds())){
	        	String[] bizFormIds= reqFunctionUsecase.getBizFormIds().split(",");
	        	 List<ReqUsecaseRelFormVO> formVOs = new ArrayList<ReqUsecaseRelFormVO>();
	        	 for(int i=0; i<bizFormIds.length;i++){
	        		 ReqUsecaseRelFormVO formVO = new ReqUsecaseRelFormVO();
	        		 formVO.setUsecaseId(reqFunctionUsecase.getId());
	        		 formVO.setBizFormId(bizFormIds[i]);
	        		 formVOs.add(formVO);
	        	 }
	        	 reqUsecaseRelFormFacade.saveBizFormNodeRelList(formVOs);
	        }
        }
        return reqFunctionUsecase.getId();
    }
    
    /**
     * 通过功能用例ID查询功能用例
     * 
     * @param reqFunctionUsecase 功能用例
     * @return 功能用例map对象
     */
    @RemoteMethod
    public Map<String, Object> queryReqFunctionUsecaseList(final ReqFunctionUsecaseVO reqFunctionUsecase) {
    	final Map<String, Object> ret = new HashMap<String, Object>(2);
    	int count = reqFunctionUsecaseFacade.queryReqFunctionUsecaseCount(reqFunctionUsecase);
    	List<ReqFunctionUsecaseVO> reqFunctionUsecaseList = null;
    	if(count > 0){
            reqFunctionUsecaseList = reqFunctionUsecaseFacade.queryReqFunctionUsecaseList(reqFunctionUsecase);
        }
        ret.put("list", reqFunctionUsecaseList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 获取功能用例关联业务表单
     * 
     * @param subitemId 功能用例ID
     * @return 功能用例关联业务表单
     */
    @RemoteMethod
    public  ReqUsecaseRelFormVO queryReqUsecaseRelFormBysubitemId(final String subitemId) {
        return reqUsecaseRelFormFacade.queryReqUsecaseRelFormBysubitemId(subitemId);
    }
}
