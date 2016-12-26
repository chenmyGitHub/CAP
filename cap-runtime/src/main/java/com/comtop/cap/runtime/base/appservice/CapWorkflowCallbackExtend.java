/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.appservice;

import java.util.List;

import com.comtop.cap.runtime.base.model.CapWorkflowParam;

/**
 * 工作流回调扩展
 *
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2015年9月1日 lizhongwen
 */
public interface CapWorkflowCallbackExtend {
    
    /**
     * 回调
     * 
     * @param vo VO
     * @param param 工作流参数
     * @param taskId 任务ID
     * @param operateType 操作类型
     */
    void callback(Object vo, CapWorkflowParam param, String taskId, int operateType);
    
    /**
     * 批量回调
     * 
     * @param vos VO
     * @param params 工作流参数
     * @param operateType 操作类型
     */
    void batchCallback(List<?> vos, List<CapWorkflowParam> params, int operateType);
}
