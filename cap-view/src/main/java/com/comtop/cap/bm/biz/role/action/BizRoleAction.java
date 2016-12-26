/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.biz.role.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.role.facade.BizRoleFacade;
import com.comtop.cap.bm.biz.role.model.BizRoleVO;
import com.comtop.cap.bm.common.BizLevel;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 角色表,用于文档管理中角色信息管理抽象Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-11-11 姜子豪
 */
@DwrProxy
public class BizRoleAction extends BaseAction {
    
    /** 角色信息管理Facade */
    protected final BizRoleFacade roleFacade = AppBeanUtil.getBean(BizRoleFacade.class);
    
    /**
     * 通过角色信息ID查询角色表
     * 
     * @param roleId 角色表,用于文档管理中角色信息管理ID
     * @return 角色表,用于文档管理中角色信息管理对象
     */
    @RemoteMethod
    public BizRoleVO queryRoleById(final String roleId) {
        BizRoleVO objRole = roleFacade.loadRoleById(roleId);
        if (objRole == null) {
            objRole = new BizRoleVO();
        }
        return objRole;
    }
    
    /**
     * 保存角色信息
     * 
     * @param roleList 角色信息
     * @return 保存结果
     */
    @RemoteMethod
    public boolean saveRole(final List<BizRoleVO> roleList) {
        boolean flag = false;
        for (BizRoleVO role : roleList) {
            if (StringUtil.isNotBlank(role.getId())) {
                roleFacade.updateRole(role);
                flag = true;
            } else {
                String ID = (String) roleFacade.insertRole(role);
                role.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 通过业务域ID查询角色列表
     * 
     * @param domainId 业务域ID
     * @return 角色列表
     */
    @RemoteMethod
    public List<BizRoleVO> queryRoleByDomainId(final String domainId) {
        return roleFacade.queryRoleByDomainId(domainId);
    }
    
    /**
     * 查询角色列表
     * 
     * @param bizRoleVO 业务流程节点
     * @return 业务流程节点map对象
     */
    @RemoteMethod
    public Map<String, Object> queryRoleList(final BizRoleVO bizRoleVO) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = roleFacade.queryRoleCount(bizRoleVO);
        List<BizRoleVO> bizRoleList = null;
        if (count > 0) {
            bizRoleList = roleFacade.queryRoleList(bizRoleVO);
        }
        ret.put("list", bizRoleList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除角色信息
     * 
     * @param roleList 角色信息集合
     */
    @RemoteMethod
    public void deleteRoleList(final List<BizRoleVO> roleList) {
        roleFacade.deleteRoleList(roleList);
    }
    
    /**
     * 查询角色列表
     * 
     * @param role 角色
     * @return 业务角色对象
     */
    @RemoteMethod
    public List<BizRoleVO> queryRoleListNopage(final BizRoleVO role) {
        return roleFacade.queryRoleListNopage(role);
    }
    
    /**
     * 查询角色是否被引用
     * 
     * @param role 角色
     * @return 结果
     */
    @RemoteMethod
    public int checkRoleIsUse(final BizRoleVO role) {
        return roleFacade.checkRoleIsUse(role);
    }
    
    /**
     * 获取角色层级
     * 
     * @return 结果
     */
    @RemoteMethod
    public String getRoleLevel() {
        String roleLevel = BizLevel.getBizLevelDicByDicKey("DIC_3");
        return roleLevel;
    }
}
