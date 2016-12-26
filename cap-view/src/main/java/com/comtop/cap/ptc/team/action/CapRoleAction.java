/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.ptc.team.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.ptc.team.facade.CapRoleFacade;
import com.comtop.cap.ptc.team.model.CapRoleVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 角色管理基本信息Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-9-9 CAP
 */
@DwrProxy
public class CapRoleAction extends BaseAction {
    
    /** 角色管理基本信息Facade */
    protected final CapRoleFacade capRoleFacade = AppBeanUtil.getBean(CapRoleFacade.class);
    
    /**
     * 通过角色管理基本信息ID查询角色管理基本信息
     * 
     * @param capRoleId 角色管理基本信息ID
     * @return 角色管理基本信息对象
     */
    @RemoteMethod
    public CapRoleVO queryCapRoleById(final String capRoleId) {
        CapRoleVO objCapRole = capRoleFacade.loadCapRoleById(capRoleId);
        if (objCapRole == null) {
            objCapRole = new CapRoleVO();
        }
        return objCapRole;
    }
    
    /**
     * 保存角色基本信息
     * 
     * @param capRole 角色管理基本信息
     * @return 角色Id
     */
    @RemoteMethod
    public String saveCapRole(final CapRoleVO capRole) {
        if (capRole.getId() == null) {
            String strId = (String) capRoleFacade.insertCapRole(capRole);
            capRole.setId(strId);
        } else {
            capRoleFacade.updateCapRole(capRole);
        }
        return capRole.getId();
    }
    
    /**
     * 查询角色管理基本信息
     * 
     * @param capRole 角色管理基本信息
     * @return 角色管理基本信息map对象
     */
    @RemoteMethod
    public Map<String, Object> queryCapRoleList(final CapRoleVO capRole) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = capRoleFacade.queryCapRoleCount(capRole);
        List<CapRoleVO> capRoleList = null;
        if (count > 0) {
            capRoleList = capRoleFacade.queryCapRoleList(capRole);
        }
        ret.put("list", capRoleList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除角色管理基本信息
     * 
     * @param capRoleList 角色管理基本信息集合
     */
    @RemoteMethod
    public void deleteCapRoleList(final List<CapRoleVO> capRoleList) {
        capRoleFacade.deleteCapRoleList(capRoleList);
    }
    
}
