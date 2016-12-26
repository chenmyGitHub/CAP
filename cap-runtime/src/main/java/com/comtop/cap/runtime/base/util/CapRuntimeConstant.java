/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

/**
 * 运行时常量类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月23日 龚斌
 */
public class CapRuntimeConstant {
    
    /**
     * 构造函数
     */
    private CapRuntimeConstant() {
    }
    
    /** 插入操作类型 */
    public final static String INSERT_OPE_TYPE = "insert";
    
    /** 删除操作类型 */
    public final static String DELETE_OPE_TYPE = "delete";
    
    /** 根据ID查询操作类型 */
    public final static String LOADBYID_OPE_TYPE = "loadById";
    
    /** 实体关联关系：1对多 */
    public final static String ONE_TO_MANY_ASS_TYPE = "One-Many";
    
    /** 实体关联关系：多对1 */
    public final static String MANY_TO_ONE_ASS_TYPE = "Many-One";
    
    /** 实体关联关系：1对1 */
    public final static String ONE_TO_ONE_ASS_TYPE = "One-One";
    
    /** 实体关联关系：多对多 */
    public final static String MANY_TO_MANY_ASS_TYPE = "Many-Many";
}
