/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.Map;

/**
 * 数据包装接口
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 * @param <T> xxx
 */
public interface IWrapper<T> {
    
    /**
     * 包装实体，以便代码生成时使用
     * 
     * @param entity 实体VO
     * @return 包装后的数据
     */
    Map<String, Object> wrapper(final T entity);
    
    /**
     * 包装实体，以便代码生成时使用
     * 
     * @param entity 实体VO
     * @param format VO名称格式
     * @return 包装后的数据
     */
    Map<String, Object> wrapper(T entity, String format);
}
