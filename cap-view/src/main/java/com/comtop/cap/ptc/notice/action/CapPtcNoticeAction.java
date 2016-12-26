/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.ptc.notice.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.ptc.notice.facade.CapPtcNoticeFacade;
import com.comtop.cap.ptc.notice.model.CapPtcNoticeVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 公告基本信息Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-9-25 CAP
 */
@DwrProxy
public class CapPtcNoticeAction extends BaseAction {
    
    /** 公告基本信息Facade */
    protected final CapPtcNoticeFacade capPtcNoticeFacade = AppBeanUtil.getBean(CapPtcNoticeFacade.class);
    
    /**
     * 通过公告基本信息ID查询公告基本信息
     * 
     * @param capPtcNoticeId 公告基本信息ID
     * @return 公告基本信息对象
     */
    @RemoteMethod
    public CapPtcNoticeVO queryCapPtcNoticeById(final String capPtcNoticeId) {
        CapPtcNoticeVO objCapPtcNotice = capPtcNoticeFacade.loadCapPtcNoticeById(capPtcNoticeId);
        if (objCapPtcNotice == null) {
            objCapPtcNotice = new CapPtcNoticeVO();
        }
        return objCapPtcNotice;
    }
    
    /**
     * 通过公告基本信息ID查询公告基本信息
     * 
     * @param capPtcNotice 公告基本信息ID
     * @return 公告基本信息对象
     */
    @RemoteMethod
    public String saveCapPtcNotice(final CapPtcNoticeVO capPtcNotice) {
        if (capPtcNotice.getId() == null) {
            String strId = (String) capPtcNoticeFacade.insertCapPtcNotice(capPtcNotice);
            capPtcNotice.setId(strId);
        } else {
            capPtcNoticeFacade.updateCapPtcNotice(capPtcNotice);
        }
        return capPtcNotice.getId();
    }
    
    /**
     * 通过公告基本信息ID查询公告基本信息
     * 
     * @param capPtcNotice 公告基本信息
     * @return 公告基本信息map对象
     */
    @RemoteMethod
    public Map<String, Object> queryCapPtcNoticeList(final CapPtcNoticeVO capPtcNotice) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        if(StringUtil.isNotBlank(capPtcNotice.getTitle())) {
        	capPtcNotice.setTitle(capPtcNotice.getTitle().trim());	//标题内容多余内容清除
        }
        int count = capPtcNoticeFacade.queryCapPtcNoticeCount(capPtcNotice);
        List<CapPtcNoticeVO> capPtcNoticeList = null;
        if (count > 0) {
            capPtcNoticeList = capPtcNoticeFacade.queryCapPtcNoticeList(capPtcNotice);
            if (capPtcNoticeList.isEmpty() || capPtcNoticeList == null) {
                int strPageNo = capPtcNotice.getPageNo();
                if (strPageNo > 1) {
                    capPtcNotice.setPageNo(strPageNo - 1);
                } else {
                    capPtcNotice.setPageNo(1);
                }
                capPtcNoticeList = capPtcNoticeFacade.queryCapPtcNoticeList(capPtcNotice);
            }
        }
        ret.put("list", capPtcNoticeList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除公告基本信息
     * 
     * @param capPtcNoticeList 公告基本信息集合
     */
    @RemoteMethod
    public void deleteCapPtcNoticeList(final List<CapPtcNoticeVO> capPtcNoticeList) {
        capPtcNoticeFacade.deleteCapPtcNoticeList(capPtcNoticeList);
    }
    
    /**
     * 公告基本信息对象列表 (不翻页)
     * 
     * @param capPtcNotice 查询条件对象
     * @return 公告基本信息对象列表
     */
    @RemoteMethod
    public List<CapPtcNoticeVO> queryCapPtcNoticeListNoPage(final CapPtcNoticeVO capPtcNotice) {
        return capPtcNoticeFacade.queryCapPtcNoticeListNoPage(capPtcNotice);
    }
}
