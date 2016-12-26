/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

/**
 * 工作台常量类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-13 李忠文
 */
public class WorkbenchConstant {
    
    /**
     * 构造函数
     */
    private WorkbenchConstant() {
    }
    
    /** 已办操作类型：填报 */
    public static final String DONE_OPRATE_FILL = "填报一条";
    
    /** 工作流已办操作类型：审核 */
    public static final String DONE_OPRATE_FORE = "审核一条";
    
    /** 工作流已办操作类型：回退 */
    public static final String DONE_OPRATE_BACK = "回退一条";
    
    /** 工作流已办操作类型：转发 */
    public static final String DONE_OPRATE_REASSIGN = "转发一条";
    
    /** 工作流已办操作类型：撤回 */
    public static final String DONE_OPRATE_UNDO = "撤回一条";
    
    /** 工作流已办操作类型：上报 */
    public static final String DONE_OPRATE_ENTRY = "上报一条";
    
    /** 已办操作类型：执行 */
    public static final String DONE_OPRATE_EXCUTE = "执行一条";
    
    /** 已办操作类型：终结 */
    public static final String DONE_OPRATE_ABORT = "终结一条";
}
