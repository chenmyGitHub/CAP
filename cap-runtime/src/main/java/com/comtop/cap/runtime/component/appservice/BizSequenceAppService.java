/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.appservice;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.component.dao.BizSequenceDAO;
import com.comtop.cap.runtime.component.model.BizSequenceVO;
import com.comtop.cip.jodd.jtx.JtxPropagationBehavior;
import com.comtop.cip.jodd.jtx.meta.Transaction;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;

/**
 * 业务序号 服务类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-26 李忠文
 */
@PetiteBean
public class BizSequenceAppService extends CapBaseAppService {
    
    /** DAO */
    @PetiteInject
    protected BizSequenceDAO dao;
    
    /**
     * 通过关键字查询业务序列
     * 
     * @param key 关键字
     * @return 业务队列
     */
    public BizSequenceVO loadBizSequenceByKey(String key) {
        return dao.loadBizSequenceByKey(key);
    }
    
    /**
     * 通过关键字查询业务序列
     * 
     * @param seq 业务队列
     */
    @Transaction(propagation = JtxPropagationBehavior.PROPAGATION_REQUIRED, readOnly = false)
    public void saveBizSequenceByKey(BizSequenceVO seq) {
        dao.merge(seq);
    }
}
