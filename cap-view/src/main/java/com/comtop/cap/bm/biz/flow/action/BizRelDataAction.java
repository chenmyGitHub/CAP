/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.biz.flow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.flow.facade.BizRelDataFacade;
import com.comtop.cap.bm.biz.flow.model.BizRelDataVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务关联数据项抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-26 CAP
 */
@DwrProxy
public class BizRelDataAction extends BaseAction {
    
    /** 业务关联数据项Facade */
    protected final BizRelDataFacade bizRelDataFacade = AppBeanUtil.getBean(BizRelDataFacade.class);
    
    /**
     * 通过业务关联数据项ID查询业务关联数据项
     * 
     * @param bizRelDataId 业务关联数据项ID
     * @return 业务关联数据项对象
     */
    @RemoteMethod
    public BizRelDataVO queryBizRelDataById(final String bizRelDataId) {
        BizRelDataVO objBizRelData = bizRelDataFacade.loadBizRelDataById(bizRelDataId);
        if (objBizRelData == null) {
            objBizRelData = new BizRelDataVO();
        }
        return objBizRelData;
    }
    
    /**
     * 通过业务关联数据项ID查询业务关联数据项
     * 
     * @param bizRelData 业务关联数据项ID
     * @return 业务关联数据项对象
     */
    @RemoteMethod
    public String saveBizRelData(final BizRelDataVO bizRelData) {
        if (bizRelData.getId() == null) {
            String strId = (String) bizRelDataFacade.insertBizRelData(bizRelData);
            bizRelData.setId(strId);
        } else {
            bizRelDataFacade.updateBizRelData(bizRelData);
        }
        return bizRelData.getId();
    }
    
    /**
     * 通过业务关联数据项ID查询业务关联数据项集合
     * 
     * @param bizRelDataVOLst 需求附件元素集合
     * @return 需求附件元素ID
     */
    @RemoteMethod
    public boolean saveBizRelDataList(final List<BizRelDataVO> bizRelDataVOLst) {
        boolean flag = false;
        for (BizRelDataVO bizRelDataVO : bizRelDataVOLst) {
            if (StringUtil.isNotBlank(bizRelDataVO.getId())) {
                bizRelDataFacade.updateBizRelData(bizRelDataVO);
                flag = true;
            } else {
                String ID = (String) bizRelDataFacade.insertBizRelData(bizRelDataVO);
                bizRelDataVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 通过业务关联数据项ID查询业务关联数据项
     * 
     * @param bizRelData 业务关联数据项
     * @return 业务关联数据项map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizRelDataList(final BizRelDataVO bizRelData) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizRelDataFacade.queryBizRelDataCount(bizRelData);
        List<BizRelDataVO> bizRelDataList = null;
        if (count > 0) {
            bizRelDataList = bizRelDataFacade.queryBizRelDataList(bizRelData);
        }
        ret.put("list", bizRelDataList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除业务关联数据项
     * 
     * @param bizRelDataList 业务关联数据项集合
     */
    @RemoteMethod
    public void deleteBizRelDataList(final List<BizRelDataVO> bizRelDataList) {
        bizRelDataFacade.deleteBizRelDataList(bizRelDataList);
    }
    
}
