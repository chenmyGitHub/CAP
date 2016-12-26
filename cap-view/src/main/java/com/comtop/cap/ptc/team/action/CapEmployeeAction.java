/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用
 * 复制、修改或发布本软件
 *****************************************************************************/

package com.comtop.cap.ptc.team.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.ptc.team.facade.CapEmployeeFacade;
import com.comtop.cap.ptc.team.facade.TeamAndEmployeeRelFacade;
import com.comtop.cap.ptc.team.facade.TeamFacade;
import com.comtop.cap.ptc.team.model.CapEmployeeVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import com.comtop.top.core.jodd.AppContext;
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
public class CapEmployeeAction extends BaseAction {
    
    /** 服务对象 Facade */
    protected final TeamFacade objTeamFacade = AppContext.getBean(TeamFacade.class);
    
    /** 人员基本信息Facade */
    protected CapEmployeeFacade CapEmployeeFacade = AppBeanUtil.getBean(CapEmployeeFacade.class);
    
    /** 团队、人员关系Facade */
    protected TeamAndEmployeeRelFacade teamAndEmployeeRelFacade = AppBeanUtil.getBean(TeamAndEmployeeRelFacade.class);
    
    /**
     * 查询用户列表
     * 
     * @param capEmployee 人员基本信息
     * @return 人员基本信息
     */
    @RemoteMethod
    public Map<String, Object> queryEmployeeList(final CapEmployeeVO capEmployee) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<CapEmployeeVO> capEmployeeList = new ArrayList<CapEmployeeVO>();
        int count = CapEmployeeFacade.queryEmployeeCount(capEmployee);
        if (count > 0) {
            capEmployeeList = CapEmployeeFacade.queryEmployeeList(capEmployee);
            if (capEmployeeList.isEmpty() || capEmployeeList == null) {
                int strPageNo = capEmployee.getPageNo();
                if (strPageNo > 1) {
                    capEmployee.setPageNo(capEmployee.getPageNo() - 1);
                } else {
                    capEmployee.setPageNo(1);
                }
                capEmployeeList = CapEmployeeFacade.queryEmployeeList(capEmployee);
            }
        }
        
        mapResult.put("list", capEmployeeList);
        mapResult.put("count", count);
        return mapResult;
    }
    
    /**
     * 查询用户列表(不分页)
     * 
     * @param capEmployee 人员基本信息
     * @return 人员基本信息
     */
    @RemoteMethod
    public List<CapEmployeeVO> queryEmployeeListNoPage(CapEmployeeVO capEmployee) {
        return CapEmployeeFacade.queryEmployeeListNoPage(capEmployee);
    }
    
    /**
     * 查询测试用户列表(不分页)
     * 
     * @param capEmployee 测试人员基本信息
     * @return 人员基本信息
     */
    @RemoteMethod
    public List<CapEmployeeVO> queryTestEmployeeListNoPage(CapEmployeeVO capEmployee) {
        return CapEmployeeFacade.queryTestEmployeeListNoPage(capEmployee);
    }
    
    /**
     * 通过人员基本信息ID查询人员基本信息
     * 
     * @param CapEmployeeId 人员基本信息ID
     * @return 人员基本信息对象
     */
    @RemoteMethod
    public CapEmployeeVO queryCapEmployeeById(final String CapEmployeeId) {
        CapEmployeeVO objCapEmployee = CapEmployeeFacade.queryCapEmployeeById(CapEmployeeId);
        if (objCapEmployee == null) {
            objCapEmployee = new CapEmployeeVO();
        }
        return objCapEmployee;
    }
    
    /**
     * 保存人员对象
     * 
     * @param CapEmployee 人员对象
     * @return 操作结果
     */
    @RemoteMethod
    public String saveEmployee(final CapEmployeeVO CapEmployee) {
        if (CapEmployee.getId() == null) {
            String strId = CapEmployeeFacade.insertEmployee(CapEmployee);
            CapEmployee.setId(strId);
        } else {
            CapEmployeeFacade.updateEmployee(CapEmployee);
        }
        return CapEmployee.getId();
    }
    
    /**
     * 删除人员对象
     * 
     * @param capEmployeeList 人员对象
     */
    @RemoteMethod
    public void deleteEmployeeList(final List<CapEmployeeVO> capEmployeeList) {
        CapEmployeeFacade.deleteEmployeeList(capEmployeeList);
        for (CapEmployeeVO CapEmployee : capEmployeeList) {
            teamAndEmployeeRelFacade.deleteRelation(CapEmployee.getId());
        }
    }
    
}
