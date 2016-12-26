/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.soaregist.handler.impl;

import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cap.bm.metadata.soareg.model.SoaBaseVO;
import com.comtop.cap.bm.webapp.soaregist.handler.ConvertHandlerStrategy;

/**
 * 服务实体处理策略
 * 
 * @author 林玉千
 * @since jdk1.6
 * @version 2016-6-28 林玉千
 */
public class ServiceConvertHandler implements ConvertHandlerStrategy {
    
    /**
     * 对象转换处理
     * 
     * @see com.comtop.cap.bm.webapp.soaregist.handler.ConvertHandlerStrategy#convertType(java.lang.Object)
     *      renturn 返回soa注册对象
     */
    @Override
    public SoaBaseVO convertType(Object obj) {
        SoaBaseVO soaBaseVO = new SoaBaseVO();
        ServiceObjectVO serviceObject = (ServiceObjectVO) obj;
        if (serviceObject != null) {
            soaBaseVO.setEngName(serviceObject.getEnglishName());
            soaBaseVO.setAliasName(serviceObject.getServiceAlias());
            soaBaseVO.setModelPackage(serviceObject.getModelPackage());
            soaBaseVO.setModelId(serviceObject.getModelId());
            soaBaseVO.setProcessId("");
            soaBaseVO.setEntityType("");
        }
        return soaBaseVO;
    }
    
}
