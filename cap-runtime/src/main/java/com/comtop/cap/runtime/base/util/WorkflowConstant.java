/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

/**
 * 工作流静态常量
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-12 李忠文
 */
public class WorkflowConstant {
    
    /**
     * 构造函数
     */
    private WorkflowConstant() {
    }
    
    /**************************************************************** 工作流状态 ********/
    
    /** 工作流状态：未上报 */
    public static final int WORK_FLOW_STATE_UNREPORT = 0;
    
    /** 工作流状态：运行中 */
    public static final int WORK_FLOW_STATE_RUN = 1;
    
    /** 工作流状态：已完成 */
    public static final int WORK_FLOW_STATE_OVER = 2;
    
    /** 工作流状态：终结 */
    public static final int WORK_FLOW_STATE_ABORT = 3;
    
    /** 工作流状态：挂起 */
    public static final int WORK_FLOW_STATE_HUNGUP = 4;
    
    /*************************************************************** 工作流操作 *********/
    
    /** 工作流操作：上报 ,1 */
    public static final int WORK_FLOW_OPERATE_REPORT = 1;
    
    /** 工作流操作：发送,2 */
    public static final int WORK_FLOW_OPERATE_SEND = 2;
    
    /** 工作流操作：回退,3 */
    public static final int WORK_FLOW_OPERATE_BACK = 3;
    
    /** 工作流操作：转发,4 */
    public static final int WORK_FLOW_OPERATE_REASSIGN = 4;
    
    /** 工作流操作：终结,5 */
    public static final int WORK_FLOW_OPERATE_ABORT = 5;
    
    /** 工作流操作：挂起,6 */
    public static final int WORK_FLOW_OPERATE_HUNGUP = 6;
    
    /** 工作流操作：撤回,7 */
    public static final int WORK_FLOW_OPERATE_UNDO = 7;
    
    /** 工作流操作：抄送,8 */
    public static final int WORK_FLOW_OPERATE_COPYTO = 8;
    
    /** 工作流操作：回退申请人,9 */
    public static final int WORK_FLOW_OPERATE_BACKREPORT = 9;
    
    /** 工作流操作：撤回已结束流程并回退申请人,10 */
    public static final int WORK_FLOW_OPERATE_UNDOREPORT = 10;
    
    /** 工作流操作：保存意见,11 */
    public static final int WORK_FLOW_OPERATE_SAVENOTE = 11;
    
    /** 工作流操作： 结束流程,12 */
    public static final int WORK_FLOW_OPERATE_OVERFLOW = 12;
    
    /** 工作流操作： 流程跳转（流程回退后再次发送时可进行跳转）,13 */
    public static final int WORK_FLOW_OPERATE_JUMP = 13;
    
    /** 工作流操作：恢复,14 */
    public static final int WORK_FLOW_OPERATE_RECOVER = 14;
    
    /*************************************************************** 扩展属性名 *********/
    
    /** 发送类型扩展属性名,default:默认发送,special:指定发送 */
    public static final String EXTEND_ATTR_FORE_TYPE = "fore_type";
    
    /** 回退类型扩展属性名称(default:默认回退,special:指定回退) */
    public static final String EXTEND_ATTR_ATTR_BACK_TYPE = "back_type";
    
    /*************************************************************** 意见扩展属性名称 *********/
    
    /** 发送意见扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_FORE_OPINION_TYPE = "fore_opinion_type";
    
    /** 回退意见扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_BACK_OPINION_TYPE = "back_opinion_type";
    
    /*************************************************************** 短信扩展属性名称 *********/
    /** 发送短信扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_SMS_TYPE_FORE = "fore_sms_type";
    
    /** 回退短信扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_SMS_TYPE_BACK = "back_sms_type";
    
    /** 转发短信扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_SMS_TYPE_REASSIGN = "reassign_sms_type";
    
    /** 终结短信扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_SMS_TYPE_ABORT = "abort_sms_type";
    
    /*************************************************************** email扩展属性名称 *********/
    
    /** 发送email扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_EMAIL_TYPE_FORE = "fore_email_type";
    
    /** 回退email扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_EMAIL_TYPE_BACK = "back_email_type";
    
    /** 转发email扩展属性名称(hide:不需要,optional:可填,required:必须) */
    public static final String EXTEND_ATTR_EMAIL_TYPE_FORWARD = "reassign_email_type";
    
    /*************************************************************** 发送操作类别 *********/
    
    /** 发送操作类别-默认发送 */
    public static final String EXTEND_VALUE_DEFAULT = "default";
    
    /** 发送操作类别-指定发送 */
    public static final String EXTEND_VALUE_SPECIAL = "special";
    
    /*************************************************************** 扩展属性值 *********/
    
    /** 扩展属性值-不需要 */
    public static final String EXTEND_VALUE_HIDE = "hide";
    
    /** 扩展属性值-可填,可选 */
    public static final String EXTEND_VALUE_OPTIONAL = "optional";
    
    /** 扩展属性值-必须,必填 */
    public static final String EXTEND_VALUE_REQUIRED = "required";
    
    /*************************************************************** 工作流已办撤回状态 *********/
    
    /** 工作流已办撤回标志，1：可以撤回，2：不能撤回 */
    public static final int WORK_FLOW_ALLOW_UNDO = 1;
    
    /** 工作流已办撤回标志，1：可以撤回，2：不能撤回 */
    public static final int WORK_FLOW_NO_ALLOW_UNDO = 2;
    
}
