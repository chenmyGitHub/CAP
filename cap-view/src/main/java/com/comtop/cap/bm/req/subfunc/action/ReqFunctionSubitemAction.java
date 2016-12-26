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

import com.comtop.cap.bm.biz.domain.facede.BizDomainFacade;
import com.comtop.cap.bm.biz.domain.model.BizDomainVO;
import com.comtop.cap.bm.req.subfunc.appservice.ReqFunctionPageAppService;
import com.comtop.cap.bm.req.subfunc.facade.ReqFunctionSubitemFacade;
import com.comtop.cap.bm.req.subfunc.facade.ReqFunctionUsecaseFacade;
import com.comtop.cap.bm.req.subfunc.facade.ReqSubitemDutyFacade;
import com.comtop.cap.bm.req.subfunc.model.ReqFunctionSubitemVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 功能子项,建在功能项下面Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-25 CAP
 */
@DwrProxy
public class ReqFunctionSubitemAction extends BaseAction {
    
    /** 功能子项,建在功能项下面Facade */
    protected final ReqFunctionSubitemFacade reqFunctionSubitemFacade = AppBeanUtil.getBean(ReqFunctionSubitemFacade.class);
    
    /** 业务域业务逻辑处理类面Facade */
    protected final BizDomainFacade bizDomainFacade = AppBeanUtil.getBean(BizDomainFacade.class);
    
    /** 功能用例,建在功能项下面Facade */
    protected final ReqFunctionUsecaseFacade reqFunctionUsecaseFacade = AppBeanUtil.getBean(ReqFunctionUsecaseFacade.class);
    
    /** 功能子项角色职责项,建在功能项下面Facade */
    protected final ReqSubitemDutyFacade reqSubitemDutyFacade = AppBeanUtil.getBean(ReqSubitemDutyFacade.class);
    
    /** 功能子项界面原型,建在功能项下面Facade */
    protected final ReqFunctionPageAppService reqFunctionPageAppService = AppBeanUtil.getBean(ReqFunctionPageAppService.class);
    
    /**
     * 通过功能子项,建在功能项下面ID查询功能子项,建在功能项下面（只获取自身数据）
     * 
     * @param reqFunctionSubitemId 功能子项,建在功能项下面ID
     * @return 功能子项,建在功能项下面对象
     */
    @RemoteMethod
    public ReqFunctionSubitemVO queryById(String reqFunctionSubitemId) {
        ReqFunctionSubitemVO objReqFunctionSubitem = reqFunctionSubitemFacade.loadById(reqFunctionSubitemId);
        if (objReqFunctionSubitem == null) {
            objReqFunctionSubitem = new ReqFunctionSubitemVO();
        }
        return objReqFunctionSubitem;
    }
    
    /**
     * 通过功能子项,建在功能项下面ID查询功能子项,建在功能项下面
     * 
     * @param reqFunctionSubitemId 功能子项,建在功能项下面ID
     * @return 功能子项,建在功能项下面对象
     */
    @RemoteMethod
    public ReqFunctionSubitemVO queryReqFunctionSubitemById(final String reqFunctionSubitemId) {
        ReqFunctionSubitemVO objReqFunctionSubitem = reqFunctionSubitemFacade.loadReqFunctionSubitemById(reqFunctionSubitemId);
        if (objReqFunctionSubitem == null) {
            objReqFunctionSubitem = new ReqFunctionSubitemVO();
        }
        return objReqFunctionSubitem;
    }
    
    /**
     * 通过功能子项,建在功能项下面ID查询功能子项,建在功能项下面
     * 
     * @param reqFunctionSubitem 功能子项,建在功能项下面ID
     * @return 功能子项,建在功能项下面对象
     */
    @RemoteMethod
    public String saveReqFunctionSubitem(final ReqFunctionSubitemVO reqFunctionSubitem) {
        if (reqFunctionSubitem.getId() == null) {
            String strId = (String) reqFunctionSubitemFacade.insertReqFunctionSubitem(reqFunctionSubitem);
            reqFunctionSubitem.setId(strId);
        } else {
            reqFunctionSubitemFacade.updateReqFunctionSubitem(reqFunctionSubitem);
        }
        return reqFunctionSubitem.getId();
    }
    
    /**
     * 通过功能子项,建在功能项下面ID查询功能子项,建在功能项下面
     * 
     * @param reqFunctionSubitem 功能子项,建在功能项下面
     * @return 功能子项,建在功能项下面map对象
     */
    @RemoteMethod
    public Map<String, Object> queryReqFunctionSubitemList(final ReqFunctionSubitemVO reqFunctionSubitem) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = reqFunctionSubitemFacade.queryReqFunctionSubitemCount(reqFunctionSubitem);
        List<ReqFunctionSubitemVO> reqFunctionSubitemList = null;
        if (count > 0) {
            reqFunctionSubitemList = reqFunctionSubitemFacade.queryReqFunctionSubitemList(reqFunctionSubitem);
        }
        ret.put("list", reqFunctionSubitemList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除功能子项,建在功能项下面
     * 
     * @param reqFunctionSubitemVO 功能子项VO
     */
    @RemoteMethod
    public void deleteReqFunctionSubitem(final ReqFunctionSubitemVO reqFunctionSubitemVO) {
        // 删除功能用例
        reqFunctionUsecaseFacade.deleteReqFunctionUsecase(reqFunctionSubitemVO.getId());
        // 删除界面原型
        reqFunctionPageAppService.deleteReqPageBySubitemId(reqFunctionSubitemVO.getId());
        // 删除界面原型
        reqSubitemDutyFacade.deleteReqSubitemDuty(reqFunctionSubitemVO.getId());
        // 删除功能子项
        reqFunctionSubitemFacade.deleteReqFunctionSubitem(reqFunctionSubitemVO);
    }
    
    /**
     * 查询功能子项条数
     * 
     * @param reqFunctionSubitemVO 功能子项VO
     * @return 条数
     */
    @RemoteMethod
    public int queryReqFunctionSubitemCount(final ReqFunctionSubitemVO reqFunctionSubitemVO) {
        return reqFunctionSubitemFacade.queryReqFunctionSubitemCount(reqFunctionSubitemVO);
    }
    
    /**
     * 更新 排序号
     * 
     * @param id 主键
     * @param sortNo 排序号
     * 
     */
    @RemoteMethod
    public void updateSortNoById(final String id, final Integer sortNo) {
        ReqFunctionSubitemVO reqFunctionSubitemVO = new ReqFunctionSubitemVO();
        reqFunctionSubitemVO.setId(id);
        reqFunctionSubitemVO.setSortNo(sortNo);
        reqFunctionSubitemFacade.updateSortNoById(reqFunctionSubitemVO);
    }
    
    /**
     * 根据功能子项ID查询对应的最近业务域对象
     * 
     * @param reqFunctionSubitemId 功能子项,建在功能项下面ID
     * @return 业务域对象
     */
    @RemoteMethod
    public BizDomainVO queryDomainByfuncSubId(String reqFunctionSubitemId) {
        return bizDomainFacade.queryDomainByfuncSubId(reqFunctionSubitemId);
    }
    
    /**
     * 根据应用编码查询关联的功能项或功能子项，通过其功能项或功能子项查询业务域列表
     * 
     * @param funcCode 应用编码
     * @return 业务域列表
     */
    @RemoteMethod
    public List<BizDomainVO> queryDomainListByfuncCode(String funcCode) {
        return bizDomainFacade.queryDomainListByfuncCode(funcCode);
    }
    
}
