/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.cfg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.cfg.facade.CapDocClassDefFacade;
import com.comtop.cap.bm.req.cfg.model.CapDocClassDefVO;
import com.comtop.cap.bm.req.cfg.util.ReqConstants;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 需求对象信息表(系统初始数据，不允许修改)Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-9-11 姜子豪
 */
@DwrProxy
public class CapDocClassDefAction extends BaseAction {
    
    /** 需求对象信息表(系统初始数据，不允许修改)Facade */
    protected final CapDocClassDefFacade reqFacade = AppBeanUtil.getBean(CapDocClassDefFacade.class);
    
    /**
     * 查询需求集合
     * 
     * 
     * @return 需求集合
     */
    @RemoteMethod
    public Map<String, Object> queryReqList() {
        Map<String, Object> res = new HashMap<String, Object>(ReqConstants.ONE);
        List<CapDocClassDefVO> lstReq = reqFacade.queryReqList();
        res.put("list", lstReq);// 集合
        return res;
    }
}
