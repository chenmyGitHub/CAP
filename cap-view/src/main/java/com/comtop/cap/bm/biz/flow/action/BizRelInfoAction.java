/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.biz.flow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.flow.facade.BizRelInfoFacade;
import com.comtop.cap.bm.biz.flow.model.BizRelInfoVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务关联抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-25 CAP
 */
@DwrProxy
public class BizRelInfoAction extends BaseAction {
    
    /** 业务关联Facade */
    protected final BizRelInfoFacade bizRelInfoFacade = AppBeanUtil.getBean(BizRelInfoFacade.class);
    
    /**
     * 通过业务关联ID查询业务关联
     * 
     * @param bizRelInfoId 业务关联ID
     * @return 业务关联对象
     */
    @RemoteMethod
    public BizRelInfoVO queryBizRelInfoById(final String bizRelInfoId) {
        BizRelInfoVO objBizRelInfo = bizRelInfoFacade.loadBizRelInfoById(bizRelInfoId);
        if (objBizRelInfo == null) {
            objBizRelInfo = new BizRelInfoVO();
        }
        return objBizRelInfo;
    }
    
    /**
     * 通过业务关联ID查询业务关联
     * 
     * @param bizRelInfo 业务关联ID
     * @return 业务关联对象
     */
    @RemoteMethod
    public String saveBizRelInfo(final BizRelInfoVO bizRelInfo) {
        if (bizRelInfo.getId() == null) {
            String strId = (String) bizRelInfoFacade.insertBizRelInfo(bizRelInfo);
            bizRelInfo.setId(strId);
        } else {
            bizRelInfoFacade.updateBizRelInfo(bizRelInfo);
        }
        return bizRelInfo.getId();
    }
    
    /**
     * 通过业务关联ID查询业务关联
     * 
     * @param bizRelInfo 业务关联
     * @return 业务关联map对象 
     */
    @RemoteMethod
    public Map<String, Object> queryBizRelInfoList(final BizRelInfoVO bizRelInfo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizRelInfoFacade.queryBizRelInfoCount(bizRelInfo);
        List<BizRelInfoVO> bizRelInfoList = null;
        if (count > 0) {
            bizRelInfoList = bizRelInfoFacade.queryBizRelInfoList(bizRelInfo);
        }
        ret.put("list", bizRelInfoList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除业务关联
     * 
     * @param bizRelInfoList 业务关联集合
     */
    @RemoteMethod
    public void deleteBizRelInfoList(final List<BizRelInfoVO> bizRelInfoList) {
        bizRelInfoFacade.deleteBizRelInfoList(bizRelInfoList);
    }
    
    /**
     * 删除业务关联
     * 
     * @param bizRelInfo 业务关联
     */
    @RemoteMethod
    public void deleteBizRelInfo(final BizRelInfoVO bizRelInfo) {
        bizRelInfoFacade.deleteBizRelInfo(bizRelInfo);
    }
}
