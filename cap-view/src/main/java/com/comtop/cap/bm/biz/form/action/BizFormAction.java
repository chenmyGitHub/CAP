/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用
 * 复制、修改或发布本软
 *****************************************************************************/

package com.comtop.cap.bm.biz.form.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.form.facade.BizFormFacade;
import com.comtop.cap.bm.biz.form.model.BizFormVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务表单抽象Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-11-11 姜子豪
 */
@DwrProxy
public class BizFormAction extends BaseAction {
    
    /** 业务表单Facade */
    protected final BizFormFacade bizFormFacade = AppBeanUtil.getBean(BizFormFacade.class);
    
    /**
     * 通过业务域ID查询业务表单
     * 
     * @param domainId 业务域ID
     * @param bizForm 业务表单
     * @return 业务表单对象
     */
    @RemoteMethod
    public List<BizFormVO> queryFormListByDomainId(final String domainId, final BizFormVO bizForm) {
        return bizFormFacade.queryFormListByDomainId(domainId, bizForm);
        
    }
    
    /**
     * 根据业务表单ID查询业务表单对象
     * 
     * @param formId 业务表单ID
     * @return 业务表单对象
     */
    @RemoteMethod
    public BizFormVO queryFormById(final String formId) {
        return bizFormFacade.queryFormById(formId);
    }
    
    /**
     * 保存业务表单信息
     * 
     * @param bizForm 业务表单信息
     * @return 业务域业务表单ID
     */
    @RemoteMethod
    public String saveForm(final BizFormVO bizForm) {
        if (bizForm.getId() == null) {
            String strId = bizFormFacade.insertForm(bizForm);
            bizForm.setId(strId);
        } else {
            bizFormFacade.updateForm(bizForm);
            bizFormFacade.updateCodeAndSortNo();
        }
        return bizForm.getId();
    }
    
    /**
     * 删除业务表单信息
     * 
     * @param formId 业务表单Id
     */
    @RemoteMethod
    public void deleteForm(final String formId) {
        bizFormFacade.deleteForm(formId);
    }
    
    /**
     * 查询业务表单
     * 
     * @param bizForm 业务表单信息
     * @return 业务表单对象
     */
    @RemoteMethod
    public Map<String, Object> queryFormList(final BizFormVO bizForm) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizFormFacade.queryFormCount(bizForm);
        List<BizFormVO> bizFormVOList = null;
        if (count > 0) {
            bizFormVOList = bizFormFacade.queryFormList(bizForm);
        }
        ret.put("list", bizFormVOList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 查询业务表单
     * 
     * @param bizForm 业务表单信息
     * @return 业务表单对象
     */
    @RemoteMethod
    public Map<String, Object> queryFormListByDomainIdList(BizFormVO bizForm) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizFormFacade.queryFormCountByDomainIdList(bizForm);
        List<BizFormVO> bizFormVOList = null;
        if (count > 0) {
            bizFormVOList = bizFormFacade.queryFormListByDomainIdList(bizForm);
        }
        ret.put("list", bizFormVOList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 查询业务表单是否被引用
     * 
     * @param bizFormId 业务表单ID
     * @return 结果
     */
    @RemoteMethod
    public int checkFormIsUse(final String bizFormId) {
        return bizFormFacade.checkFormIsUse(bizFormId);
    }
    
    /**
     * 查询业务表单编码是否重复
     * 
     * @param bizForm 业务表单
     * @return 结果
     */
    @RemoteMethod
    public int checkFormCode(final BizFormVO bizForm) {
        return bizFormFacade.checkFormCode(bizForm);
    }
    
}
