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

import com.comtop.cap.bm.req.cfg.facade.AttElementFacade;
import com.comtop.cap.bm.req.cfg.model.AttElementVO;
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
public class AttElementAction extends BaseAction {
    
    /** 需求附件元素Facade */
    protected final AttElementFacade attElementFacade = AppBeanUtil.getBean(AttElementFacade.class);
    
    /**
     * 查询需求附件元素集合
     * 
     * @param attElement 需求对象元素
     * @param reqType 需求类型
     * @return 需求对象元素map对象
     */
    @RemoteMethod
    public Map<String, Object> queryAttElementList(final AttElementVO attElement, String reqType) {
        Map<String, Object> res = new HashMap<String, Object>(2);
        int iCount = 0;
        iCount = attElementFacade.queryAttElementCount(attElement, reqType);
        List<AttElementVO> lstAttElement = new ArrayList<AttElementVO>();
        if (iCount > 0) {
            lstAttElement = attElementFacade.queryAttElementList(attElement, reqType);
            if (lstAttElement.isEmpty() || lstAttElement == null) {
                int strPageNo = attElement.getPageNo();
                if (strPageNo > 1) {
                    attElement.setPageNo(strPageNo - 1);
                } else {
                    attElement.setPageNo(1);
                }
                lstAttElement = attElementFacade.queryAttElementList(attElement, reqType);
            }
        }
        res.put("count", iCount);// 条数
        res.put("list", lstAttElement);// 集合
        return res;
    }
    
    /**
     * 保存需求附件集合
     * 
     * @param attElementLst 需求附件元素集合
     * @return 需求附件元素ID
     */
    @RemoteMethod
    public boolean saveAttElement(final List<AttElementVO> attElementLst) {
        boolean flag = false;
        for (AttElementVO attVO : attElementLst) {
            if (StringUtil.isNotBlank(attVO.getId())) {
                attElementFacade.updateAttElement(attVO);
                flag = true;
            } else {
                String ID = attElementFacade.insertAttElement(attVO);
                attVO.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 删除需求附件元素集合
     * 
     * @param attElementVOLst 需求附件元素集合
     */
    @RemoteMethod
    public void deleteAttElementlst(List<AttElementVO> attElementVOLst) {
        attElementFacade.deleteAttElementlst(attElementVOLst);
    }
}
