/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.dao;

import java.util.List;

import com.comtop.cap.runtime.base.dao.CapBaseCommonDAO;
import com.comtop.cap.runtime.component.model.BizSequenceVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.corm.resource.util.CollectionUtils;

/**
 * 业务序列 数据访问接口类
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-26 李忠文
 */
@PetiteBean
public class BizSequenceDAO extends CapBaseCommonDAO<BizSequenceVO> {
    
    /**
     * 通过关键字查询业务序列
     * 
     * 
     * @param key 关键字
     * @return 业务队列
     */
    public BizSequenceVO loadBizSequenceByKey(String key) {
        BizSequenceVO objParam = new BizSequenceVO();
        objParam.setKey(key);
        List<BizSequenceVO> lstBizSequence = this.queryList("loadBizSequenceByKey", objParam);
        return CollectionUtils.isEmpty(lstBizSequence) ? null : lstBizSequence.get(0);
    }
}
