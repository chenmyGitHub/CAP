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

import org.apache.commons.lang.StringUtils;

import com.comtop.cap.bm.biz.flow.facade.BizProcessNodeFacade;
import com.comtop.cap.bm.biz.flow.facade.BizProcessNodeRoleFacade;
import com.comtop.cap.bm.biz.flow.model.BizProcessNodeRoleVO;
import com.comtop.cap.bm.biz.flow.model.BizProcessNodeVO;
import com.comtop.cap.bm.biz.role.facade.BizRoleFacade;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务流程节点抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-16 CAP
 */
@DwrProxy
public class BizProcessNodeAction extends BaseAction {
    
    /** 业务流程节点Facade */
    protected final BizProcessNodeFacade bizProcessNodeFacade = AppBeanUtil.getBean(BizProcessNodeFacade.class);
    
    /** 角色Facade */
    protected final BizRoleFacade bizRoleFacade = AppBeanUtil.getBean(BizRoleFacade.class);
    
    /** 节点 */
    protected final BizProcessNodeRoleFacade bizProcessNodeRoleFacade = AppBeanUtil
        .getBean(BizProcessNodeRoleFacade.class);
    
    /**
     * 通过业务流程节点ID查询业务流程节点
     * 
     * @param bizProcessNodeId 业务流程节点ID
     * @return 业务流程节点对象
     */
    @RemoteMethod
    public BizProcessNodeVO queryBizProcessNodeById(final String bizProcessNodeId) {
        BizProcessNodeVO objBizProcessNode = bizProcessNodeFacade.loadBizProcessNodeById(bizProcessNodeId);
        BizProcessNodeRoleVO bizProcessNodeRoleVO = bizProcessNodeRoleFacade
            .queryBizProcessNodeRoleByNodeId(bizProcessNodeId);
        if (bizProcessNodeRoleVO != null) {
            objBizProcessNode.setRoleIds(bizProcessNodeRoleVO.getRoleId());
            objBizProcessNode.setRoleNames(bizProcessNodeRoleVO.getRoleName());
        }
        return objBizProcessNode;
    }
    
    /**
     * 通过业务流程节点ID查询业务流程节点
     * 
     * @param bizProcessNode 业务流程节点ID
     * @return 业务流程节点对象
     */
    @RemoteMethod
    public String saveBizProcessNode(final BizProcessNodeVO bizProcessNode) {
        if (bizProcessNode.getId() == null) {
            String strId = (String) bizProcessNodeFacade.insertBizProcessNode(bizProcessNode);
            bizProcessNode.setId(strId);
        } else {
            bizProcessNodeFacade.updateBizProcessNode(bizProcessNode);
        }
        // 删除原来的数据
        bizProcessNodeRoleFacade.deleteProcessNodeRoleFacadeByNodeId(bizProcessNode.getId());
        if (StringUtils.isNotBlank(bizProcessNode.getRoleIds())) {
            String[] roleIds = bizProcessNode.getRoleIds().split(",");
            List<BizProcessNodeRoleVO> bizProcessNodeRoleVOs = new ArrayList<BizProcessNodeRoleVO>();
            for (String id : roleIds) {
                BizProcessNodeRoleVO bizProcessNodeRoleVO = new BizProcessNodeRoleVO();
                bizProcessNodeRoleVO.setRoleId(id);
                bizProcessNodeRoleVO.setNodeId(bizProcessNode.getId());
                bizProcessNodeRoleVOs.add(bizProcessNodeRoleVO);
            }
            bizProcessNodeRoleFacade.saveBizProcessNodeRoleVOs(bizProcessNodeRoleVOs);
        }
        return bizProcessNode.getId();
    }
    
    /**
     * 通过业务流程节点ID查询业务流程节点
     * 
     * @param bizProcessNode 业务流程节点
     * @return 业务流程节点map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizProcessNodeList(final BizProcessNodeVO bizProcessNode) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizProcessNodeFacade.queryBizProcessNodeCount(bizProcessNode);
        List<BizProcessNodeVO> bizProcessNodeList = null;
        if (count > 0) {
            bizProcessNodeList = bizProcessNodeFacade.queryBizProcessNodeList(bizProcessNode);
        }
        ret.put("list", bizProcessNodeList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 查询业务流程节点列表（不分页）
     * 
     * @param bizProcessNode 业务流程节点
     * @return 业务流程节点map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizNodeList(final BizProcessNodeVO bizProcessNode) {
        final Map<String, Object> ret = new HashMap<String, Object>(1);
        List<BizProcessNodeVO> bizProcessNodeList = null;
        bizProcessNodeList = bizProcessNodeFacade.queryBizProcessNodeList(bizProcessNode);
        ret.put("list", bizProcessNodeList);
        return ret;
    }
    
    /**
     * 删除业务流程节点
     * 
     * @param bizProcessNodeList 业务流程节点集合
     */
    @RemoteMethod
    public void deleteBizProcessNodeList(final List<BizProcessNodeVO> bizProcessNodeList) {
        bizProcessNodeFacade.deleteBizProcessNodeList(bizProcessNodeList);
    }
    
    /**
     * 查询流程节点(提供选择界面)
     * 
     * @param bizProcessNode 业务流程节点
     * @return 业务流程节点对象
     */
    @RemoteMethod
    public List<BizProcessNodeVO> queryNodeListForChoose(final BizProcessNodeVO bizProcessNode) {
        return bizProcessNodeFacade.queryNodeListForChoose(bizProcessNode);
    }
    
    /**
     * 查询流程节点(提供选择界面)
     * 
     * @param bizProcessNode 业务流程节点
     * @return 业务流程节点对象
     */
    @RemoteMethod
    public BizProcessNodeVO queryNodeInfoById(final BizProcessNodeVO bizProcessNode) {
        return bizProcessNodeFacade.queryNodeInfoById(bizProcessNode).get(0);
    }
    
    /**
     * 
     * 查询流程节点使用条数
     * 
     * @param bizProcessNode 业务流程节点对象
     * @return 业务流程节点对象
     */
    @RemoteMethod
    public int queryUseBizProcessNodeCount(final BizProcessNodeVO bizProcessNode) {
        return bizProcessNodeFacade.queryUseBizProcessNodeCount(bizProcessNode);
    }
}
