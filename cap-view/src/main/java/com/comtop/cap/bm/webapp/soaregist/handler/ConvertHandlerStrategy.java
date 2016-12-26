/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.soaregist.handler;

import com.comtop.cap.bm.metadata.soareg.model.SoaBaseVO;

/**
 * 数据处理策略接口
 * 
 * @author 林玉千
 * @since jdk1.6
 * @version 2016-6-28 林玉千
 */
public interface ConvertHandlerStrategy {
    
    /**
     * 对数据进行加工处理
     * 
     * @param obj 需要处理的数据对象
     * @return 转换后的对象
     */
    SoaBaseVO convertType(Object obj);
}
