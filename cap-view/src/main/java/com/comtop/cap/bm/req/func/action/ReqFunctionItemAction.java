/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.func.action;

import com.comtop.cap.bm.req.func.facade.ReqFunctionItemFacade;
import com.comtop.cap.bm.req.func.model.ReqFunctionItemVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 功能项，建立在系统、子系统、目录下面。Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-25 CAP
 */
@DwrProxy
public class ReqFunctionItemAction extends BaseAction {
    
    /** 功能项，建立在系统、子系统、目录下面。Facade */
    protected final ReqFunctionItemFacade reqFunctionItemFacade = AppBeanUtil.getBean(ReqFunctionItemFacade.class);
    
    /**
     * 通过功能项ID查询功能项
     * 
     * @param reqFunctionItemId 功能项ID
     * @return 功能项对象
     */
    @RemoteMethod
    public ReqFunctionItemVO queryReqFunctionItemById(final String reqFunctionItemId) {
        ReqFunctionItemVO objReqFunctionItem = reqFunctionItemFacade.queryReqFunctionItemById(reqFunctionItemId);
        if (objReqFunctionItem == null) {
            objReqFunctionItem = new ReqFunctionItemVO();
        }
        return objReqFunctionItem;
    }
    
    /**
     * 保存功能项
     * 
     * @param reqFunctionItem 功能项
     * @return ID
     */
    @RemoteMethod
    public String saveReqFunctionItem(final ReqFunctionItemVO reqFunctionItem) {
        if (reqFunctionItem.getId() == null) {
            String strId = reqFunctionItemFacade.insertReqFunctionItem(reqFunctionItem);
            reqFunctionItem.setId(strId);
        } else {
            reqFunctionItemFacade.updateReqFunctionItem(reqFunctionItem);
        }
        return reqFunctionItem.getId();
    }
    
    /**
     * 删除功能项
     * 
     * @param reqFunctionItem 功能项
     * @return 删除结果
     */
    @RemoteMethod
    public boolean deleteReqFunctionItem(final ReqFunctionItemVO reqFunctionItem) {
        return reqFunctionItemFacade.deleteReqFunctionItem(reqFunctionItem);
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
        ReqFunctionItemVO reqFunctionItemVO = new ReqFunctionItemVO();
        reqFunctionItemVO.setId(id);
        reqFunctionItemVO.setSortNo(sortNo);
        reqFunctionItemFacade.updateSortNoById(reqFunctionItemVO);
    }
    
    /**
     * 查询功能项是否有关联子项
     * 
     * @param reqFunctionItem 功能项
     * @return 结果
     */
    @RemoteMethod
    public boolean checkSubFunByFunItem(final ReqFunctionItemVO reqFunctionItem) {
        return reqFunctionItemFacade.checkSubFunByFunItem(reqFunctionItem);
    }
    
    /**
     * 
     * 检查功能项是否重名
     * 
     * @param reqFunctionItem 功能项
     * @return 结果
     */
    @RemoteMethod
    public boolean checkFuncItemName(final ReqFunctionItemVO reqFunctionItem) {
        return reqFunctionItemFacade.checkFuncItemName(reqFunctionItem);
    }
}
