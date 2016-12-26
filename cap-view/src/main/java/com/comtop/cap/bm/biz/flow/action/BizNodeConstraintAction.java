/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.biz.flow.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.flow.facade.BizNodeConstraintFacade;
import com.comtop.cap.bm.biz.flow.model.BizNodeConstraintVO;
import com.comtop.cap.bm.biz.info.facade.BizObjInfoFacade;
import com.comtop.cap.bm.biz.info.model.BizObjInfoVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 流程节点数据项约束Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-20 CAP
 */
@DwrProxy
public class BizNodeConstraintAction extends BaseAction {
    
    /** 流程节点数据项约束Facade */
    protected final BizNodeConstraintFacade bizNodeConstraintFacade = AppBeanUtil
        .getBean(BizNodeConstraintFacade.class);
    
    /** 数据对象Facade */
    protected final BizObjInfoFacade bizObjInfoFacade = AppBeanUtil.getBean(BizObjInfoFacade.class);
    
    /**
     * 通过流程节点数据项约束ID查询流程节点数据项约束
     * 
     * @param bizNodeConstraintId 流程节点数据项约束ID
     * @return 流程节点数据项约束对象
     */
    @RemoteMethod
    public BizNodeConstraintVO queryBizNodeConstraintById(final String bizNodeConstraintId) {
        BizNodeConstraintVO objBizNodeConstraint = bizNodeConstraintFacade
            .loadBizNodeConstraintById(bizNodeConstraintId);
        if (objBizNodeConstraint == null) {
            objBizNodeConstraint = new BizNodeConstraintVO();
        }
        return objBizNodeConstraint;
    }
    
    /**
     * 通过流程节点数据项约束ID查询流程节点数据项约束
     * 
     * @param bizNodeConstraint 流程节点数据项约束ID
     * @return 流程节点数据项约束对象
     */
    @RemoteMethod
    public String saveBizNodeConstraint(final BizNodeConstraintVO bizNodeConstraint) {
        if (bizNodeConstraint.getId() == null) {
            String strId = (String) bizNodeConstraintFacade.insertBizNodeConstraint(bizNodeConstraint);
            bizNodeConstraint.setId(strId);
        } else {
            bizNodeConstraintFacade.updateBizNodeConstraint(bizNodeConstraint);
        }
        return bizNodeConstraint.getId();
    }
    
    /**
     * 保存对象元素集合
     * 
     * @param bizNodeConstraintVOLst 需求附件元素集合
     * @return 需求附件元素ID
     */
    @RemoteMethod
    public boolean saveBizNodeConstraintList(final List<BizNodeConstraintVO> bizNodeConstraintVOLst) {
        boolean flag = false;
        for (BizNodeConstraintVO constraintVO : bizNodeConstraintVOLst) {
            if (StringUtil.isNotBlank(constraintVO.getId())) {
                bizNodeConstraintFacade.updateBizNodeConstraint(constraintVO);
                flag = true;
            } else {
                String ID = (String) bizNodeConstraintFacade.insertBizNodeConstraint(constraintVO);
                constraintVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 通过流程节点数据项约束ID查询流程节点数据项约束
     * 
     * @param bizNodeConstraint 流程节点数据项约束
     * @return 流程节点数据项约束map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizNodeConstraintList(final BizNodeConstraintVO bizNodeConstraint) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizNodeConstraintFacade.queryBizNodeConstraintCount(bizNodeConstraint);
        List<BizNodeConstraintVO> bizNodeConstraintList = null;
        if (count > 0) {
            bizNodeConstraintList = bizNodeConstraintFacade.queryBizNodeConstraintList(bizNodeConstraint);
            if(bizNodeConstraintList == null || bizNodeConstraintList.size()==0){
            	bizNodeConstraint.setPageNo(1);
            	bizNodeConstraintList = bizNodeConstraintFacade.queryBizNodeConstraintList(bizNodeConstraint);
            }
        }
        ret.put("list", bizNodeConstraintList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除流程节点数据项约束
     * 
     * @param bizNodeConstraintList 流程节点数据项约束集合
     */
    @RemoteMethod
    public void deleteBizNodeConstraintList(final List<BizNodeConstraintVO> bizNodeConstraintList) {
        bizNodeConstraintFacade.deleteBizNodeConstraintList(bizNodeConstraintList);
    }
    
    /**
     * 删除流程节点数据项约束
     * 
     * @param bizNodeConstraintVO 流程节点数据项约束集合
     */
    @RemoteMethod
    public void deleteBizNodeConstraintByObjId(final BizNodeConstraintVO bizNodeConstraintVO) {
        bizNodeConstraintFacade.deleteBizNodeConstraintByObjId(bizNodeConstraintVO);
    }
    
    /**
     * 通过节点id查询数据对象集合
     * 
     * @param bizNodeConstraint 流程节点数据项约束集合
     * @return 数据对象集合
     */
    @RemoteMethod
    public Map<String, Object> queryBizNodeConstraintGroupObjId(final BizNodeConstraintVO bizNodeConstraint) {
        final Map<String, Object> ret = new HashMap<String, Object>(1);
        List<BizObjInfoVO> bizObjInfoLst = new ArrayList<BizObjInfoVO>();
        List<BizNodeConstraintVO> BizNodeConstraintLst = bizNodeConstraintFacade
            .queryBizNodeConstraintGroupObjId(bizNodeConstraint);
        if (BizNodeConstraintLst.size() > 0) {
            BizObjInfoVO condition = new BizObjInfoVO();
            String[] idList = new String[BizNodeConstraintLst.size()];
            for (int i = 0; i < BizNodeConstraintLst.size(); i++) {
                BizNodeConstraintVO bizNodeConstraintVO = BizNodeConstraintLst.get(i);
                idList[i] = bizNodeConstraintVO.getBizObjId();
            }
            condition.setIdList(idList);
            bizObjInfoLst = bizObjInfoFacade.queryBizObjInfoList(condition);
        }
        ret.put("list", bizObjInfoLst);
        return ret;
    }
}
