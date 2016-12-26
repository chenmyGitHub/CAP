/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.entity.util;

/**
 * 实体常量类
 *
 * @author 沈康
 * @since 1.0
 * @version 2014-10-13 沈康
 */
public class EntityConstants {
    
    /**
     * 构造函数
     */
    private EntityConstants() {
        
    }
    
    /** 表前缀 */
    public final static String TABLE_PREFIX = "T";
    
    /** 实体名称下划线连接符 */
    public final static String ENTITY_UNDER_LINE = "_";
    
    /** 操作成功返回值 */
    public final static int ENTITY_OPERATE_SUCCESS = 1;
    
    /** 操作失败返回值 */
    public final static int ENTITY_OPERATE_FAILURE = 0;
    
    /** 校验存在返回值 */
    public final static int VALIDATE_IS_EXIST = 1;
    
    /** 校验不存在返回值 */
    public final static int VALIDATE_IS_NOT_EXIST = 0;
    
    /** 操作成功但代码生成出错 */
    public final static int OPERATE_SUCCESS_CODE_ERROR = 2;
    
    /** 操作成功但代码生成出错 */
    public final static String OPERATE_SUCCESS_BUT_CODE_ERROR = "1";
    
    /** 操作成功 */
    public final static String OPERATE_SUCCESS_VALUE="SUCCESS";
    
}
