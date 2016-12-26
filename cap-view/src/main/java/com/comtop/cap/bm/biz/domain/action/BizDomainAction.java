/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.biz.domain.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.domain.facede.BizDomainFacade;
import com.comtop.cap.bm.biz.domain.model.BizDomainVO;
import com.comtop.top.core.base.action.BaseAction;
import com.comtop.top.core.jodd.AppContext;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 项目团队基本信息Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-9-9 CAP
 */
@DwrProxy
public class BizDomainAction extends BaseAction {
    
    /** 业务域Facade */
    protected final BizDomainFacade domainFacade = AppContext.getBean(BizDomainFacade.class);
    
    /**
     * 查询业务域对象map
     * 
     * @param domain 业务域
     * @return 业务域map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDomainList(final BizDomainVO domain) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = domainFacade.queryDomainCount(domain);
        List<BizDomainVO> domainList = null;
        if (count > 0) {
            domainList = domainFacade.queryDomainList(domain);
        }
        ret.put("list", domainList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 根据业务域ID查询业务域对象
     * 
     * @param domainId 业务域ID
     * @return 业务域对象
     */
    @RemoteMethod
    public BizDomainVO queryDomainById(final String domainId) {
        return domainFacade.queryDomainById(domainId);
    }
    
    /**
     * 保存业务域基本信息
     * 
     * @param domain 业务域基本信息
     * @return 业务域ID
     */
    @RemoteMethod
    public String saveDomain(final BizDomainVO domain) {
        if (domain.getId() == null) {
            String strId = domainFacade.insertDomain(domain);
            domain.setId(strId);
        } else {
            domainFacade.updateDomain(domain);
        }
        return domain.getId();
    }
    
    /**
     * 删除业务域对象
     * 
     * @param domainId 业务域ID
     */
    @RemoteMethod
    public void deleteDomain(final String domainId) {
        domainFacade.deleteDomain(domainId);
    }
    
    /**
     * 业务域编码查重
     * 
     * @param domain 业务域
     * @return 结果
     */
    @RemoteMethod
    public int checkDomainCode(final BizDomainVO domain) {
        return domainFacade.checkDomainCode(domain);
    }
    
    /**
     * 业务是否存在引用
     * 
     * @param domainId 业务域ID
     * @return 结果
     */
    @RemoteMethod
    public int checkDomainIsUse(final String domainId) {
        return domainFacade.checkDomainIsUse(domainId);
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
        BizDomainVO bizDomainVO = new BizDomainVO();
        bizDomainVO.setId(id);
        bizDomainVO.setSortNo(sortNo);
        domainFacade.updateSortNoById(bizDomainVO);
    }
    
    /**
     * 检查同级业务域是否名称重复
     * 
     * @param domainVO 业务域
     * @return 结果
     */
    @RemoteMethod
    public boolean checkDomainName(final BizDomainVO domainVO) {
        return domainFacade.checkDomainName(domainVO);
    }
}
