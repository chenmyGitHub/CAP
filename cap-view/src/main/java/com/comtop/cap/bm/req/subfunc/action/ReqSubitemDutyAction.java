/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.subfunc.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.subfunc.facade.ReqSubitemDutyFacade;
import com.comtop.cap.bm.req.subfunc.model.ReqSubitemDutyVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 功能子项职责表Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-12-16 CAP
 */
@DwrProxy
public class ReqSubitemDutyAction extends BaseAction {
    
    /** 功能子项职责表Facade */
    protected final ReqSubitemDutyFacade reqSubitemDutyFacade = AppBeanUtil.getBean(ReqSubitemDutyFacade.class);
    
    /**
     * 通过功能子项职责表ID查询功能子项职责表
     * 
     * @param reqSubitemDutyId 功能子项职责表ID
     * @return 功能子项职责表对象
     */
    @RemoteMethod
    public ReqSubitemDutyVO queryReqSubitemDutyById(final String reqSubitemDutyId) {
        ReqSubitemDutyVO objReqSubitemDuty = reqSubitemDutyFacade.loadReqSubitemDutyById(reqSubitemDutyId);
        if (objReqSubitemDuty == null) {
            objReqSubitemDuty = new ReqSubitemDutyVO();
        }
        return objReqSubitemDuty;
    }
    
    /**
     * 通过功能子项职责表ID查询功能子项职责表
     * 
     * @param reqSubitemDuty 功能子项职责表ID
     * @return 功能子项职责表对象
     */
    @RemoteMethod
    public String saveReqSubitemDuty(final ReqSubitemDutyVO reqSubitemDuty) {
        if (reqSubitemDuty.getId() == null) {
            String strId = (String) reqSubitemDutyFacade.insertReqSubitemDuty(reqSubitemDuty);
            reqSubitemDuty.setId(strId);
        } else {
            reqSubitemDutyFacade.updateReqSubitemDuty(reqSubitemDuty);
        }
        return reqSubitemDuty.getId();
    }
    
    /**
     * 通过功能子项职责表ID查询功能子项职责表(集合)
     * 
     * @param reqSubitemDutyVOLst 功能子项职责表ID
     * @return 功能子项职责表对象
     */
    @RemoteMethod
    public boolean saveReqSubitemDutyList(final List<ReqSubitemDutyVO> reqSubitemDutyVOLst) {
        boolean flag = false;
        for (ReqSubitemDutyVO constraintVO : reqSubitemDutyVOLst) {
            if (StringUtil.isNotBlank(constraintVO.getId())) {
                reqSubitemDutyFacade.updateReqSubitemDuty(constraintVO);
                flag = true;
            } else {
                String ID = (String) reqSubitemDutyFacade.insertReqSubitemDuty(constraintVO);
                constraintVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 通过功能子项职责表ID查询功能子项职责表
     * 
     * @param reqSubitemDuty 功能子项职责表
     * @return 功能子项职责表map对象
     */
    @RemoteMethod
    public Map<String, Object> queryReqSubitemDutyList(final ReqSubitemDutyVO reqSubitemDuty) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = reqSubitemDutyFacade.queryReqSubitemDutyCount(reqSubitemDuty);
        List<ReqSubitemDutyVO> reqSubitemDutyList = null;
        String roleIds = null;
        if (count > 0) {
            reqSubitemDutyList = reqSubitemDutyFacade.queryReqSubitemDutyList(reqSubitemDuty);
            roleIds = reqSubitemDutyFacade.queryBizRolesBySubitemId(reqSubitemDuty.getSubitemId());
        }
        ret.put("list", reqSubitemDutyList);
        ret.put("count", count);
        ret.put("roleIds", roleIds);
        return ret;
    }
    
    /**
     * 删除功能子项职责表
     * 
     * @param reqSubitemDutyList 功能子项职责表集合
     */
    @RemoteMethod
    public void deleteReqSubitemDutyList(final List<ReqSubitemDutyVO> reqSubitemDutyList) {
        reqSubitemDutyFacade.deleteReqSubitemDutyList(reqSubitemDutyList);
    }
}
