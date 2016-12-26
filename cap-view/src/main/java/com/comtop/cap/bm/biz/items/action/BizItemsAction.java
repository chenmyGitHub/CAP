/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用
 * 复制、修改或发布本软
 *****************************************************************************/

package com.comtop.cap.bm.biz.items.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.items.facade.BizItemsFacade;
import com.comtop.cap.bm.biz.items.model.BizItemsVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务事项抽象Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-11-11 姜子豪
 */
@DwrProxy
public class BizItemsAction extends BaseAction {
    
    /** 业务事项Facade */
    protected final BizItemsFacade bizItemsFacade = AppBeanUtil.getBean(BizItemsFacade.class);
    
    /**
     * 通过业务事项ID查询业务事项
     * 
     * @param bizItemsId 业务事项ID
     * @return 业务事项对象
     */
    @RemoteMethod
    public BizItemsVO queryBizItemsById(final String bizItemsId) {
        BizItemsVO objBizItems = bizItemsFacade.queryBizItemsById(bizItemsId);
        if (objBizItems == null) {
            objBizItems = new BizItemsVO();
        }
        return objBizItems;
    }
    
    /**
     * 保存业务事项
     * 
     * @param bizItems 业务事项
     * @return 业务事项ID
     */
    @RemoteMethod
    public String saveBizItems(final BizItemsVO bizItems) {
        if (bizItems.getId() == null) {
            String strId = bizItemsFacade.insertBizItems(bizItems);
            bizItems.setId(strId);
        } else {
            bizItemsFacade.updateBizItems(bizItems);
        }
        return bizItems.getId();
    }
    
    /**
     * 查询业务事项
     * 
     * @param bizItems 业务事项
     * @return 业务事项map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizItemsList(final BizItemsVO bizItems) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizItemsFacade.queryBizItemsCount(bizItems);
        List<BizItemsVO> bizItemsList = null;
        if (count > 0) {
            bizItemsList = bizItemsFacade.queryBizItemsList(bizItems);
        }
        ret.put("list", bizItemsList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除业务事项
     * 
     * @param bizItemsList 业务事项集合
     */
    @RemoteMethod
    public void deleteBizItemsList(final List<BizItemsVO> bizItemsList) {
        bizItemsFacade.deleteBizItemsList(bizItemsList);
    }
    
    /**
     * 通过业务域ID查询业务事项
     * 
     * @param bizItems 业务事项
     * @return 业务事项map对象
     */
    @RemoteMethod
    public Map<String, Object> queryItemsListByDomainId(final BizItemsVO bizItems) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizItemsFacade.queryItemsCountByDomainId(bizItems);
        List<BizItemsVO> bizItemsList = null;
        if (count > 0) {
            bizItemsList = bizItemsFacade.queryItemsListByDomainId(bizItems);
        }
        ret.put("list", bizItemsList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 保存业务事项列表
     * 
     * @param itemList 业务事项列表
     */
    @RemoteMethod
    public void saveItemList(final List<BizItemsVO> itemList) {
        bizItemsFacade.saveItemList(itemList);
    }
    
    /**
     * 业务事项名称查重
     * 
     * @param bizItems 业务事项
     * @return 结果
     */
    @RemoteMethod
    public boolean checkItemName(final BizItemsVO bizItems) {
        return bizItemsFacade.checkItemName(bizItems);
    }
    
    /**
     * 查询事项是否被引用
     * 
     * @param bizItems 事项
     * @return 结果
     */
    @RemoteMethod
    public int checkItemIsUse(final BizItemsVO bizItems) {
        return bizItemsFacade.checkItemIsUse(bizItems);
    }
    
    /**
     * 查询业务事项
     * 
     * @param bizItems 业务事项
     * @return 业务事项
     */
    @RemoteMethod
    public List<BizItemsVO> queryItemsList(final BizItemsVO bizItems) {
        return bizItemsFacade.queryItemsList(bizItems);
    }
    
    /**
     * 批量修改业务事项
     * 
     * @param bizItemList 业务事项
     */
    @RemoteMethod
    public void updateItemList(final List<BizItemsVO> bizItemList) {
        bizItemsFacade.updateItemList(bizItemList);
    }
}
