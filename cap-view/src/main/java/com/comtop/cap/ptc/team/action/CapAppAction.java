/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.ptc.team.action;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.ptc.team.facade.CapAppFacade;
import com.comtop.cap.ptc.team.model.CapAppVO;
import com.comtop.cap.ptc.team.model.CapEmployeeVO;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cap.runtime.core.AppBeanUtil;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 人员基本信息Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-9-18 CAP
 */
@DwrProxy
@MadvocAction
public class CapAppAction {
    
    /** 人员基本信息Facade */
    private final CapAppFacade capAppFacade = AppBeanUtil.getBean(CapAppFacade.class);
    
    /**
     * 根据ID查询VO
     *
     * @param id ID
     * @return VO
     */
    @RemoteMethod
    public CapAppVO queryById(String id) {
        return capAppFacade.queryById(id);
    }
    
    /**
     * 根据登录人的应用收藏信息
     *
     * @param capAppVO 应用ID,人员ID
     * @return 应用收藏信息
     */
    @RemoteMethod
    public CapAppVO queryStoreApp(final CapAppVO capAppVO) {
        return capAppFacade.queryStoreApp(capAppVO);
    }
    
    /**
     * 根据应用查询分配的人员信息
     *
     * @param appId 应用ID
     * @param teamId 团队ID
     * @return 人员信息
     */
    @RemoteMethod
    public List<CapEmployeeVO> queryEmployeeListByAppId(final String appId, final String teamId) {
        return capAppFacade.queryEmployeeListByAppId(appId, teamId);
    }
    
    /**
     * 收藏应用
     *
     * @param capAppVO 应用VO
     * @return 成功标志
     */
    @RemoteMethod
    public String storeUpApp(final CapAppVO capAppVO) {
        return capAppFacade.storeUpApp(capAppVO);
    }
    
    /**
     * 取消收藏
     *
     * @param capAppVO 应用VO
     * @return 成功标志
     */
    @RemoteMethod
    public boolean cancelAppStore(final CapAppVO capAppVO) {
        return capAppFacade.cancelAppStore(capAppVO);
    }
    
    /**
     * 分配应用
     *
     * @param lstCapEmployee 分配人
     * @param appId 应用ID
     * @param teamId 团队ID
     * @return 成功标志
     */
    @RemoteMethod
    public int assignApp(final List<CapEmployeeVO> lstCapEmployee, final String appId, final String teamId) {
        List<CapAppVO> lstInsertCapAppVO = new ArrayList<CapAppVO>(10);
        CapAppVO objDelCapAppVO = new CapAppVO();
        objDelCapAppVO.setAppId(appId);
        objDelCapAppVO.setTeamId(teamId);
        CapAppVO objCapAppVO;
        for (CapEmployeeVO capEmployeeVO : lstCapEmployee) {
            objCapAppVO = new CapAppVO();
            objCapAppVO.setEmployeeId(capEmployeeVO.getId());
            objCapAppVO.setAppId(appId);
            objCapAppVO.setAppType(1);
            lstInsertCapAppVO.add(objCapAppVO);
        }
        capAppFacade.assignApp(lstInsertCapAppVO, objDelCapAppVO);
        return 1;
    }
    
    /**
     * 获取我的应用
     * 
     * @param userId 用户ID信息
     * @param cascadeCollect 是否级联查询收藏的应用
     * @return 返回值
     */
    @RemoteMethod
    public List<CapAppVO> queryMyApp(final String userId, boolean cascadeCollect) {
        return capAppFacade.queryMyApp(userId, cascadeCollect);
    }
    
}
