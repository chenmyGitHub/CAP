/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.soaregist.handler;

import java.util.HashMap;
import java.util.Map;

import com.comtop.cap.bm.metadata.base.model.SoaBaseType;
import com.comtop.cap.bm.webapp.soaregist.handler.impl.EntityConvertHandler;
import com.comtop.cap.bm.webapp.soaregist.handler.impl.ServiceConvertHandler;

/**
 * 负责数据处理策略器的管理
 * 
 * @author 凌晨
 * @since jdk1.6
 * @version 2016-5-20 凌晨
 */
public class HandlerContext {
    
    /** soa注册 对象转换处理策略 */
    private static Map<String, ConvertHandlerStrategy> mapHandler = new HashMap<String, ConvertHandlerStrategy>();
    
    static {
        // 初始化数据处理策略器
        
        mapHandler.put(SoaBaseType.ENTITY_TYPE.getValue(), new EntityConvertHandler());
        mapHandler.put(SoaBaseType.SERVICEOBJECT_TYPE.getValue(), new ServiceConvertHandler());
    }
    
    /**
     * @return 获取 mapHandler属性值
     */
    public static Map<String, ConvertHandlerStrategy> getMapHandler() {
        return mapHandler;
    }
    
}
