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

import com.comtop.cap.bm.biz.flow.facade.BizProcessInfoFacade;
import com.comtop.cap.bm.biz.flow.model.BizProcessInfoVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务流程Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-13 CAP
 */
@DwrProxy
public class BizProcessInfoAction extends BaseAction {
    
    /** 业务流程Facade */
    protected final BizProcessInfoFacade bizProcessInfoFacade = AppBeanUtil.getBean(BizProcessInfoFacade.class);
    
    /**
     * 通过业务流程ID查询业务流程
     * 
     * @param bizProcessInfoId 业务流程ID
     * @return 业务流程对象
     */
    @RemoteMethod
    public BizProcessInfoVO queryBizProcessInfoById(final String bizProcessInfoId) {
        BizProcessInfoVO objBizProcessInfo = bizProcessInfoFacade.loadBizProcessInfoById(bizProcessInfoId);
        if (objBizProcessInfo == null) {
            objBizProcessInfo = new BizProcessInfoVO();
        }
        if (StringUtil.isNotBlank(objBizProcessInfo.getFlowChartId())) {
            objBizProcessInfo.setFlowChartId(objBizProcessInfo.getFlowChartId().replaceAll("\\.emf", ".png"));
        }
        return objBizProcessInfo;
    }
    
    /**
     * 通过业务流程ID查询业务流程
     * 
     * @param bizProcessInfo 业务流程ID
     * @return 业务流程对象
     */
    @RemoteMethod
    public String saveBizProcessInfo(final BizProcessInfoVO bizProcessInfo) {
        if (bizProcessInfo.getId() == null) {
            int count = 1;
            count += bizProcessInfoFacade.queryBizProcessInfoCount(bizProcessInfo);
            bizProcessInfo.setSortNo(count);
            String strId = (String) bizProcessInfoFacade.insertBizProcessInfo(bizProcessInfo);
            bizProcessInfo.setId(strId);
        } else {
            bizProcessInfoFacade.updateBizProcessInfo(bizProcessInfo);
        }
        return bizProcessInfo.getId();
    }
    
    /**
     * 通过业务流程ID查询业务流程集合
     * 
     * @param bizProcessInfoLst 业务流程集合
     * @return 业务流程对象
     */
    @RemoteMethod
    public boolean saveBizProcessInfoList(final List<BizProcessInfoVO> bizProcessInfoLst) {
        boolean flag = false;
        for (BizProcessInfoVO bizProcessInfoVO : bizProcessInfoLst) {
            if (StringUtil.isNotBlank(bizProcessInfoVO.getId())) {
                bizProcessInfoFacade.updateBizProcessInfo(bizProcessInfoVO);
                flag = true;
            } else {
                String ID = (String) bizProcessInfoFacade.insertBizProcessInfo(bizProcessInfoVO);
                bizProcessInfoVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 通过业务流程ID查询业务流程
     * 
     * @param bizProcessInfo 业务流程
     * @return 业务流程map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizProcessInfoList(final BizProcessInfoVO bizProcessInfo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizProcessInfoFacade.queryBizProcessInfoCount(bizProcessInfo);
        List<BizProcessInfoVO> bizProcessInfoList = null;
        if (count > 0) {
            bizProcessInfoList = bizProcessInfoFacade.queryBizProcessInfoList(bizProcessInfo);
            if (bizProcessInfoList == null || bizProcessInfoList.size() == 0) {
                bizProcessInfo.setPageNo(1);
                bizProcessInfoList = bizProcessInfoFacade.queryBizProcessInfoList(bizProcessInfo);
            }
        }
        ret.put("list", bizProcessInfoList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 通过业务流程查询条数
     * 
     * @param bizProcessInfo 查询条件
     * @return 条数
     */
    @RemoteMethod
    public int queryBizProcessInfoCount(final BizProcessInfoVO bizProcessInfo) {
        return bizProcessInfoFacade.queryBizProcessInfoCount(bizProcessInfo);
    }
    
    /**
     * 删除业务流程
     * 
     * @param bizProcessInfoList 业务流程集合
     */
    @RemoteMethod
    public void deleteBizProcessInfoList(final List<BizProcessInfoVO> bizProcessInfoList) {
        bizProcessInfoFacade.deleteBizProcessInfoList(bizProcessInfoList);
    }
    
    /**
     * 查询存在流程节点数量
     * 
     * @param bizProcessInfo 业务流程
     * @return 流程节点数量
     */
    @RemoteMethod
    public int queryProcessNodeCount(final BizProcessInfoVO bizProcessInfo) {
        return bizProcessInfoFacade.queryProcessNodeCount(bizProcessInfo);
    }
}
