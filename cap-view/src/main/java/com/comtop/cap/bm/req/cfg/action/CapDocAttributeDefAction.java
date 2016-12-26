/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.cfg.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.cfg.facade.CapDocAttributeDefFacade;
import com.comtop.cap.bm.req.cfg.model.CapDocAttributeDefVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 需求对象元素Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-9-11 姜子豪
 */
@DwrProxy
public class CapDocAttributeDefAction extends BaseAction {
    
    /** 需求对象元素Facade */
    protected final CapDocAttributeDefFacade reqElementFacade = AppBeanUtil.getBean(CapDocAttributeDefFacade.class);
    
    /**
     * 查询需求对象元素集合
     * 
     * @param reqElement 需求对象元素
     * @param reqType 需求类型
     * @return 需求对象元素map对象
     */
    @RemoteMethod
    public Map<String, Object> queryReqElementList(final CapDocAttributeDefVO reqElement, String reqType) {
        Map<String, Object> res = new HashMap<String, Object>(2);
        int iCount = 0;
        iCount = reqElementFacade.queryReqElementCount(reqElement, reqType);
        List<CapDocAttributeDefVO> lstMeetingList = new ArrayList<CapDocAttributeDefVO>();
        if (iCount > 0) {
            lstMeetingList = reqElementFacade.queryReqElementList(reqElement, reqType);
            if (lstMeetingList.isEmpty() || lstMeetingList == null) {
                int strPageNo = reqElement.getPageNo();
                if (strPageNo > 1) {
                    reqElement.setPageNo(strPageNo - 1);
                } else {
                    reqElement.setPageNo(1);
                }
                lstMeetingList = reqElementFacade.queryReqElementList(reqElement, reqType);
            }
        }
        res.put("count", iCount);// 条数
        res.put("list", lstMeetingList);// 集合
        return res;
    }
    
    /**
     * 保存对象元素集合
     * 
     * @param reqElementLst 需求附件元素集合
     * @return 需求附件元素ID
     */
    @RemoteMethod
    public boolean saveRElement(final List<CapDocAttributeDefVO> reqElementLst) {
        boolean flag = false;
        for (CapDocAttributeDefVO reqVO : reqElementLst) {
            if (StringUtil.isNotBlank(reqVO.getId())) {
                reqElementFacade.updateReqElement(reqVO);
                flag = true;
            } else {
                String ID = reqElementFacade.insertReqElement(reqVO);
                reqVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 删除对象元素集合
     * 
     * @param reqElementLst 需求对象元素集合
     */
    @RemoteMethod
    public void deleteReqElementlst(List<CapDocAttributeDefVO> reqElementLst) {
        reqElementFacade.deleteReqElementlst(reqElementLst);
    }
    
    /**
     * 根据对象的URI查询对象属性定义
     *
     * @param uri 对象定义URI
     * @return 对象属性定义
     */
    @RemoteMethod
    public List<CapDocAttributeDefVO> queryObjectAttribute(String uri) {
        return reqElementFacade.queryObjectAttribute(uri);
    }
}
