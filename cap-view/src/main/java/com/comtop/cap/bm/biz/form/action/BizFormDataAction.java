/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用
 * 复制、修改或发布本软
 *****************************************************************************/

package com.comtop.cap.bm.biz.form.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.biz.form.facade.BizFormDataFacade;
import com.comtop.cap.bm.biz.form.model.BizFormDataVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 业务表单数据项抽象Action
 * 
 * @author 姜子豪
 * @since 1.0
 * @version 2015-11-11 姜子豪
 */
@DwrProxy
public class BizFormDataAction extends BaseAction {
    
    /** 业务表单数据Facade */
    protected final BizFormDataFacade bizFormDataFacade = AppBeanUtil.getBean(BizFormDataFacade.class);
    
    /**
     * 通过业务表单ID查询业务表单数据
     * 
     * @param bizFormData 业务表单数据
     * @return 业务事项map对象
     */
    @RemoteMethod
    public Map<String, Object> queryFormDataListByFormId(final BizFormDataVO bizFormData) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = bizFormDataFacade.queryFormDataCountByFormId(bizFormData);
        List<BizFormDataVO> bizFormDataList = null;
        if (count > 0) {
            bizFormDataList = bizFormDataFacade.queryFormDataListByFormId(bizFormData);
        }
        ret.put("list", bizFormDataList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 保存业务表单数据项
     * 
     * @param bizFormDataList 业务表单数据项
     * @return 保存结果
     */
    @RemoteMethod
    public boolean saveFormData(final List<BizFormDataVO> bizFormDataList) {
        boolean flag = false;
        for (BizFormDataVO bizFormData : bizFormDataList) {
            if (StringUtil.isNotBlank(bizFormData.getId())) {
                bizFormDataFacade.updateFormData(bizFormData);
                flag = true;
            } else {
                String ID = bizFormDataFacade.insertFormData(bizFormData);
                bizFormData.setId(ID);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * 删除业务表单数据项
     * 
     * @param bizFormDataList 业务表单数据项
     */
    @RemoteMethod
    public void deleteFormData(final List<BizFormDataVO> bizFormDataList) {
        bizFormDataFacade.deleteFormData(bizFormDataList);
    }
}
