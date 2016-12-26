/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.biz.info.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.info.facade.BizObjDataItemFacade;
import com.comtop.cap.bm.biz.info.model.BizObjDataItemVO;
import com.comtop.cip.common.util.CAPCollectionUtils;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;
/**
 *  业务对象数据项Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-10 CAP
 */
@DwrProxy
public class BizObjDataItemAction  extends BaseAction {
    
    /** 业务对象数据项Facade */
    protected final BizObjDataItemFacade bizObjDataItemFacade = AppBeanUtil.getBean(BizObjDataItemFacade.class);
    
    /**
     * 通过业务对象数据项ID查询业务对象数据项
     * 
     * @param bizObjDataItemId 业务对象数据项ID
     * @return 业务对象数据项对象
     */
    @RemoteMethod
    public BizObjDataItemVO queryBizObjDataItemById(final String bizObjDataItemId) {
        BizObjDataItemVO objBizObjDataItem = bizObjDataItemFacade.loadBizObjDataItemById(bizObjDataItemId);
        if (objBizObjDataItem == null) {
            objBizObjDataItem = new BizObjDataItemVO();
        }
        return objBizObjDataItem;
    }
    
    /**
     * 通过业务对象数据项ID查询业务对象数据项
     * 
     * @param bizObjDataItem 业务对象数据项ID
     * @return 业务对象数据项对象
     */
    @RemoteMethod
    public String saveBizObjDataItem(final BizObjDataItemVO bizObjDataItem) {
        if (bizObjDataItem.getId() == null) {
            String strId = (String) bizObjDataItemFacade.insertBizObjDataItem(bizObjDataItem);
            bizObjDataItem.setId(strId);
        } else {
        	bizObjDataItemFacade.updateBizObjDataItem(bizObjDataItem);
        }
        return bizObjDataItem.getId();
    }
    
    /**
     * 通过业务对象数据项ID查询业务对象数据项
     * 
     * @param bizObjDataItem 业务对象数据项
     * @return 业务对象数据项map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizObjDataItemList(final BizObjDataItemVO bizObjDataItem) {
    	final Map<String, Object> ret = new HashMap<String, Object>(2);
    	int count = bizObjDataItemFacade.queryBizObjDataItemCount(bizObjDataItem);
    	List<BizObjDataItemVO> bizObjDataItemList = null;
    	if(count > 0){
            bizObjDataItemList = bizObjDataItemFacade.queryBizObjDataItemList(bizObjDataItem);
        }
        ret.put("list", bizObjDataItemList);
        ret.put("count", count);
        return ret;
    }
    
    
    /**
     * 根据业务对象数据项的ID集合查询对应的业务对象数据项VO集合
     * @param ids 业务对象数据项的ID集合，为空是返回空集合
     * @return 符合条件的业务对象数据项VO集合
     */
    @RemoteMethod
    public List<BizObjDataItemVO> queryBizObjDataItemListByIds(List<String> ids){
    	if(CAPCollectionUtils.isEmpty(ids)){
    		return new ArrayList<BizObjDataItemVO>();
    	}
    	 return bizObjDataItemFacade.queryBizObjDataItemListByIds(ids);
    }
}
