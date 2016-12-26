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

import com.comtop.cap.bm.biz.common.model.BizCommonVO;
import com.comtop.cap.bm.biz.info.common.BizObjImportOperator;
import com.comtop.cap.bm.biz.info.facade.BizObjInfoFacade;
import com.comtop.cap.bm.biz.info.model.BizObjInfoVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableVO;
import com.comtop.cip.common.constant.CapNumberConstant;
import com.comtop.cip.common.util.CAPCollectionUtils;
import com.comtop.cip.common.util.CAPStringUtils;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务对象基本信息Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-10 CAP
 */
@DwrProxy
public class BizObjInfoAction extends BaseAction {
    
    /** 业务对象基本信息Facade */
    protected final BizObjInfoFacade bizObjInfoFacade = AppBeanUtil.getBean(BizObjInfoFacade.class);
    
    /**
     * 通过业务对象基本信息ID查询业务对象基本信息
     * 
     * @param bizObjInfoId
     *            业务对象基本信息ID
     * @return 业务对象基本信息对象
     */
    @RemoteMethod
    public BizObjInfoVO queryBizObjInfoById(final String bizObjInfoId) {
        BizObjInfoVO objBizObjInfo = bizObjInfoFacade.loadBizObjInfoById(bizObjInfoId);
        if (objBizObjInfo == null) {
            objBizObjInfo = new BizObjInfoVO();
        }
        return objBizObjInfo;
    }
    
    /**
     * 通过业务对象基本信息ID查询业务对象基本信息
     * 
     * @param bizObjInfo
     *            业务对象基本信息ID
     * @return 业务对象基本信息对象
     */
    @RemoteMethod
    public String saveBizObjInfo(final BizObjInfoVO bizObjInfo) {
        if (bizObjInfo.getId() == null) {
            String strId = (String) bizObjInfoFacade.insertBizObjInfo(bizObjInfo);
            bizObjInfo.setId(strId);
        } else {
            bizObjInfoFacade.updateBizObjInfo(bizObjInfo);
        }
        return bizObjInfo.getId();
    }
    
    /**
     * 通过业务对象基本信息ID查询业务对象基本信息
     * 
     * @param bizObjInfo
     *            业务对象基本信息
     * @return 业务对象基本信息map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizObjInfoList(final BizObjInfoVO bizObjInfo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizObjInfoFacade.queryBizObjInfoCount(bizObjInfo);
        List<BizObjInfoVO> bizObjInfoList = null;
        if (count > 0) {
            bizObjInfoList = bizObjInfoFacade.queryBizObjInfoList(bizObjInfo);
        }
        ret.put("list", bizObjInfoList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 通过业务对象基本信息ID查询业务对象基本信息
     * 
     * @param bizObjInfo
     *            业务对象基本信息
     * @return 业务对象基本信息map对象
     */
    @RemoteMethod
    public Map<String, Object> queryBizObjInfoListByDomainIds(BizObjInfoVO bizObjInfo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizObjInfoFacade.queryBizObjInfoCountByDomainIdList(bizObjInfo);
        List<BizObjInfoVO> bizObjInfoList = null;
        if (count > 0) {
            bizObjInfoList = bizObjInfoFacade.queryBizObjInfoListByDomainIdList(bizObjInfo);
        }
        ret.put("list", bizObjInfoList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 根据业务对象ID集合查询对应的业务对象VO集合
     * 
     * @param bizInfoIds 业务对象的ID集合，为空是返回空集合
     * @return 符合条件的业务对象VO集合
     */
    @RemoteMethod
    public List<BizObjInfoVO> queryBizInfoListByIds(List<String> bizInfoIds) {
        if (CAPCollectionUtils.isEmpty(bizInfoIds)) {
            return new ArrayList<BizObjInfoVO>(0);
        }
        return bizObjInfoFacade.queryBizInfoListByIds(bizInfoIds);
    }
    
    /****
     * 判断指定的业务域下是否已存在同名业务对象
     * 
     * @param bizObjInfo
     *            业务对象, 业务域ID、业务名称必须(如果是修改还需要传ID)
     * @return 如果存在则返回true;否则返回false
     */
    @RemoteMethod
    public boolean isExistSameNameBizInfo(final BizObjInfoVO bizObjInfo) {
        return bizObjInfoFacade.isExistSameNameBizInfo(bizObjInfo);
    }
    
    /****
     * 判断指定的业务域下是否已存在相同编号的业务对象
     * 
     * @param bizObjInfo
     *            业务对象, 业务域ID、业务编号必须(如果是修改还需要传ID)
     * @return 如果存在则返回true;否则返回false
     */
    @RemoteMethod
    public boolean isExistSameCodeBizInfo(final BizObjInfoVO bizObjInfo) {
        return bizObjInfoFacade.isExistSameCodeBizInfo(bizObjInfo);
    }
    
    /***
     * 修改业务对象排序
     * 
     * @param bizObjInfo
     *            业务对象,ID必须
     * @param type
     *            1:升级、-1：降级,否则返回false
     * @return 操作结果 正常返回true,否则返回false
     */
    @RemoteMethod
    public boolean updateSortNo(final BizObjInfoVO bizObjInfo, int type) {
        if (bizObjInfo == null || CAPStringUtils.isBlank(bizObjInfo.getId())) {
            return false;
        }
        if (type == CapNumberConstant.NUMBER_INT_ONE || type == CapNumberConstant.NUMBER_INT_MINUS) {
            return bizObjInfoFacade.updateSortNo(bizObjInfo, type);
        }
        return false;
    }
    
    /**
     * 删除业务对象基本信息
     * 
     * @param bizObjInfoList
     *            业务对象基本信息集合
     */
    @RemoteMethod
    public void deleteBizObjInfoList(final List<BizObjInfoVO> bizObjInfoList) {
        bizObjInfoFacade.deleteBizObjInfoList(bizObjInfoList);
    }
    
    /**
     * 查询业务对象是否被引用
     * 
     * @param bizObjInfo 业务对象基本信息
     * @return 操作结果
     */
    @RemoteMethod
    public int checkObjInfoIsUse(final BizObjInfoVO bizObjInfo) {
        return bizObjInfoFacade.checkObjInfoIsUse(bizObjInfo);
    }
    
    /**
     * 修改业务对象排序
     * 
     * @param bizObjInfo 业务对象基本信息
     */
    @RemoteMethod
    public void updateSortNo(final BizObjInfoVO bizObjInfo) {
        bizObjInfoFacade.updateSortNo(bizObjInfo);
    }
    
    /**
     * 业务对象转表对象
     * 
     * @param lstBizCommon 业务对象
     * @return 表modelId
     */
    @RemoteMethod
    public TableVO parserBizToTable(List<BizCommonVO> lstBizCommon) {
        BizObjImportOperator objBiz = new BizObjImportOperator();
        return objBiz.parserBizToTable(lstBizCommon);
    }
}
