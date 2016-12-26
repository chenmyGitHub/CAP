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

import com.comtop.cap.bm.biz.flow.facade.BizFormNodeRelFacade;
import com.comtop.cap.bm.biz.flow.model.BizFormNodeRelVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务表单和业务流程节点关系表抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-24 CAP
 */
@DwrProxy
public class BizFormNodeRelAction extends BaseAction {
    
    /** 业务表单和业务流程节点关系表Facade */
    protected final BizFormNodeRelFacade bizFormNodeRelFacade = AppBeanUtil.getBean(BizFormNodeRelFacade.class);
    
    /**
     * 通过业务表单和业务流程节点关系表ID查询业务表单和业务流程节点关系表
     * 
     * @param bizFormNodeRelId 业务表单和业务流程节点关系表ID
     * @return 业务表单和业务流程节点关系表对象
     */
    @RemoteMethod
    public BizFormNodeRelVO queryBizFormNodeRelById(final String bizFormNodeRelId) {
        BizFormNodeRelVO objBizFormNodeRel = bizFormNodeRelFacade.loadBizFormNodeRelById(bizFormNodeRelId);
        if (objBizFormNodeRel == null) {
            objBizFormNodeRel = new BizFormNodeRelVO();
        }
        return objBizFormNodeRel;
    }
    
    /**
     * 通过业务表单和业务流程节点关系表ID查询业务表单和业务流程节点关系表
     * 
     * @param bizFormNodeRel 业务表单和业务流程节点关系表ID
     * @return 业务表单和业务流程节点关系表对象
     */
    @RemoteMethod
    public String saveBizFormNodeRel(final BizFormNodeRelVO bizFormNodeRel) {
        if (bizFormNodeRel.getId() == null) {
            String strId = (String) bizFormNodeRelFacade.insertBizFormNodeRel(bizFormNodeRel);
            bizFormNodeRel.setId(strId);
        } else {
            bizFormNodeRelFacade.updateBizFormNodeRel(bizFormNodeRel);
        }
        return bizFormNodeRel.getId();
    }
    
    /**
     * 通过业务表单和业务流程节点关系表ID查询业务表单和业务流程节点关系表
     * 
     * @param bizFormNodeRelVOLst 业务表单和业务流程节点关系表ID
     * @return 业务表单和业务流程节点关系表对象
     */
    @RemoteMethod
    public boolean saveBizFormNodeReltList(final List<BizFormNodeRelVO> bizFormNodeRelVOLst) {
        boolean flag = false;
        for (BizFormNodeRelVO bizFormNodeRelVO : bizFormNodeRelVOLst) {
            if (StringUtil.isNotBlank(bizFormNodeRelVO.getId())) {
                bizFormNodeRelFacade.updateBizFormNodeRel(bizFormNodeRelVO);
                flag = true;
            } else {
                String ID = (String) bizFormNodeRelFacade.insertBizFormNodeRel(bizFormNodeRelVO);
                bizFormNodeRelVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 通过业务表单和业务流程节点关系表ID查询业务表单和业务流程节点关系表
     * 
     * @param bizFormNodeRel 业务表单和业务流程节点关系表
     * @return 业务表单和业务流程节点关系表map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizFormNodeRelList(final BizFormNodeRelVO bizFormNodeRel) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizFormNodeRelFacade.queryBizFormNodeRelCount(bizFormNodeRel);
        List<BizFormNodeRelVO> bizFormNodeRelList = null;
        if (count > 0) {
            bizFormNodeRelList = bizFormNodeRelFacade.queryBizFormNodeRelList(bizFormNodeRel);
            if(bizFormNodeRelList == null || bizFormNodeRelList.size()==0){
            	bizFormNodeRel.setPageNo(1);
            	bizFormNodeRelList = bizFormNodeRelFacade.queryBizFormNodeRelList(bizFormNodeRel);
            }
        }
        ret.put("list", bizFormNodeRelList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除业务表单和业务流程节点关系表
     * 
     * @param bizFormNodeRelList 业务表单和业务流程节点关系表集合
     */
    @RemoteMethod
    public void deleteBizFormNodeRelList(final List<BizFormNodeRelVO> bizFormNodeRelList) {
        bizFormNodeRelFacade.deleteBizFormNodeRelList(bizFormNodeRelList);
    }
    
}
