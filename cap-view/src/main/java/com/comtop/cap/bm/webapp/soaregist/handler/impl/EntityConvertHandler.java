/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.soaregist.handler.impl;

import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.soareg.model.SoaBaseVO;
import com.comtop.cap.bm.webapp.soaregist.handler.ConvertHandlerStrategy;

/**
 * 实体处理策略
 * 
 * @author 林玉千
 * @since jdk1.6
 * @version 2016-6-28 林玉千
 */
public class EntityConvertHandler implements ConvertHandlerStrategy {
    
    /**
     * 
     * @see com.comtop.cap.bm.webapp.soaregist.handler.ConvertHandlerStrategy#convertType(java.lang.Object)
     *      renturn 返回soa注册对象
     */
    @Override
    public SoaBaseVO convertType(Object obj) {
        SoaBaseVO soaBaseVO = new SoaBaseVO();
        EntityVO entity = (EntityVO) obj;
        if (entity != null) {
            soaBaseVO.setEngName(entity.getEngName());
            soaBaseVO.setAliasName(entity.getAliasName());
            soaBaseVO.setModelPackage(entity.getModelPackage());
            soaBaseVO.setModelId(entity.getModelId());
            soaBaseVO.setProcessId(entity.getProcessId());
            soaBaseVO.setEntityType(entity.getEntityType());
        }
        return soaBaseVO;
    }
    
}
