/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.func.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.func.facade.ReqTreeFacade;
import com.comtop.cap.bm.req.func.model.ReqTreeVO;
import com.comtop.cip.common.util.CAPCollectionUtils;
import com.comtop.cip.common.util.CAPStringUtils;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 功能项需求视图 Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-25 CAP
 */
@DwrProxy
public class ReqTreeAction extends BaseAction {
    
    /** Facade */
    protected final ReqTreeFacade reqTreeFacade = AppBeanUtil.getBean(ReqTreeFacade.class);
    
    /**
     * 功能项树视图查询方法
     * 
     * @param reqTree 功能项需求视图 。
     * @return 功能项需求视图
     */
    @RemoteMethod
    public Map<String, Object> queryViewReqTreeList(final ReqTreeVO reqTree) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = reqTreeFacade.queryViewReqTreeCount(reqTree);
        List<ReqTreeVO> reqFunctionItemList = null;
        if (count > 0) {
            reqFunctionItemList = reqTreeFacade.queryViewReqTreeList(reqTree);
        }
        ret.put("list", reqFunctionItemList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 功能项树子节点加载方法
     * 
     * @param parentId 父节点Id
     * @return 子节点
     */
    @RemoteMethod
    public List<ReqTreeVO> queryViewReqListById(final String parentId) {
        List<ReqTreeVO> reqFunctionItemList = reqTreeFacade.queryViewReqListById(parentId);
        return reqFunctionItemList;
    }
    
    /**
     * 根据ID集合、对象类型 查询对应的 功能项需求视图（包含业务领域、功能项、功能子项）
     * 
     * @param type 类型,1:业务域、2：功能项、3：功能子项
     * @param ids id集合
     * @return 符合条件的功能项需求视图VO集合
     */
    @RemoteMethod
    public List<ReqTreeVO> queryViewReqTreeListByTypeAndIds(String type, List<String> ids) {
        if (CAPStringUtils.isBlank(type) || CAPCollectionUtils.isEmpty(ids)) {
            return new ArrayList<ReqTreeVO>(0);
        }
        return reqTreeFacade.queryViewReqTreeListByTypeAndIds(type, ids);
    }
    
    /**
     * 根据ID查询父子列表
     * 
     * @param id id
     * @return 符合条件的功能项需求视图VO集合
     */
    @RemoteMethod
    public List<ReqTreeVO> queryViewPTreeById(final String id) {
        return reqTreeFacade.queryViewPTreeById(id);
    }
}
