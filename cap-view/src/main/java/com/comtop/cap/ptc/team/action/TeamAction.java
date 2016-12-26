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

import com.comtop.cap.ptc.team.facade.TeamAndEmployeeRelFacade;
import com.comtop.cap.ptc.team.facade.TeamFacade;
import com.comtop.cap.ptc.team.model.CapEmployeeVO;
import com.comtop.cap.ptc.team.model.TeamAndEmployeeRelVO;
import com.comtop.cap.ptc.team.model.TeamVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.top.core.base.action.BaseAction;
import com.comtop.top.core.jodd.AppContext;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 项目团队基本信息Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-9-9 CAP
 */
@DwrProxy
public class TeamAction extends BaseAction {
    
    /** 团队服务对象 Facade */
    protected TeamFacade teamFacade = AppContext.getBean(TeamFacade.class);
    
    /** 团队、成员关系服务对象 Facade */
    protected TeamAndEmployeeRelFacade teamAndEmployeeRelFacade = AppContext.getBean(TeamAndEmployeeRelFacade.class);
    
    /**
     * 查询项目团队基本信息
     * 
     * @return 项目团队基本信息map对象
     */
    @RemoteMethod
    public Map<String, Object> queryTeamList() {
        final Map<String, Object> ret = new HashMap<String, Object>(1);
        List<TeamVO> teamList = null;
        teamList = teamFacade.queryTeamList();
        ret.put("list", teamList);
        return ret;
    }
    
    /**
     * 查询测试团队基本信息
     * 
     * @return 项目团队基本信息map对象
     */
    @RemoteMethod
    public Map<String, Object> queryTestTeamList() {
        final Map<String, Object> ret = new HashMap<String, Object>(1);
        List<TeamVO> teamList = null;
        teamList = teamFacade.queryTestTeamList();
        ret.put("list", teamList);
        return ret;
    }
    
    /**
     * 根据团队ID查询团队对象列表
     * 
     * @param teamId 团队ID
     * @return 团队对象集合
     */
    @RemoteMethod
    public TeamVO queryTeamVOByTeamId(String teamId) {
        TeamVO lstteam = teamFacade.queryTeamVOByTeamId(teamId);
        return lstteam;
    }
    
    /**
     * 根据团队ID查询团队成员
     * 
     * @param teamId 团队ID
     * @return 团队成员集合
     */
    @RemoteMethod
    public List<CapEmployeeVO> queryEmployeeByTeamId(String teamId) {
        List<CapEmployeeVO> lstEmployee = teamAndEmployeeRelFacade.queryEmployeeByTeamId(teamId);
        return lstEmployee;
    }
    
    /**
     * 保存项目团队基本信息
     * 
     * @param team 项目团队基本信息
     * @return 项目团队ID
     */
    @RemoteMethod
    public String saveTeam(final TeamVO team) {
        if (team.getId() == null) {
            String strId = teamFacade.insertTeam(team);
            team.setId(strId);
        } else {
            teamFacade.updateTeam(team);
        }
        return team.getId();
    }
    
    /**
     * 删除项目团队
     * 
     * @param teamList 项目团队
     */
    @RemoteMethod
    public void deleteTeamList(final List<TeamVO> teamList) {
        teamFacade.deleteTeamList(teamList);
        for (TeamVO team : teamList) {
            teamAndEmployeeRelFacade.deleteFromTeam(team.getId());
        }
    }
    
    /**
     * 新增相应团队成员
     * 
     * @param employeeVOList 项目团队成员基本信息
     * @return 保存结果
     */
    @RemoteMethod
    public boolean saveEmployeeToTeam(final List<TeamAndEmployeeRelVO> employeeVOList) {
        boolean flag = false;
        for (TeamAndEmployeeRelVO employeeVO : employeeVOList) {
            if (StringUtil.isNotBlank(employeeVO.getId())) {
                teamAndEmployeeRelFacade.updateEmployeeToTeam(employeeVO);
                flag = true;
            } else {
                String ID = teamAndEmployeeRelFacade.insertEmployeeToTeam(employeeVO);
                employeeVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 删除项目团队成员
     * 
     * @param employeeVOList 项目团队成员
     */
    @RemoteMethod
    public void deleteTeamEmployee(final List<TeamAndEmployeeRelVO> employeeVOList) {
        teamAndEmployeeRelFacade.deleteTeamEmployee(employeeVOList);
    }
    
}
