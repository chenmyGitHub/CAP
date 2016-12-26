/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.helper;

import java.util.Date;
import java.util.List;

import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.workbench.done.facade.WorkbenchDoneFacade;
import com.comtop.top.workbench.done.model.DoneDTO;
import com.comtop.top.workbench.message.facade.WorkbenchMessageFacade;
import com.comtop.top.workbench.message.model.MessageDTO;
import com.comtop.top.workbench.todo.facade.WorkbenchTodoFacade;
import com.comtop.top.workbench.todo.model.TodoStatisticsDTO;

/**
 * 工作台帮助类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-13 李忠文
 */
public final class WorkbenchHelper {
    
    /**
     * 构造函数
     */
    private WorkbenchHelper() {
    }
    
    /** 工作台已办 */
    private static WorkbenchDoneFacade workbenchDoneFacade = AppBeanUtil
        .getBean(WorkbenchDoneFacade.class);
    
    /** 工作台消息 */
    private static WorkbenchMessageFacade workbenchMessageFacade = AppBeanUtil
        .getBean(WorkbenchMessageFacade.class);
    
    /** 工作台待办 */
    private static WorkbenchTodoFacade workbenchTodoFacade = AppBeanUtil
        .getBean(WorkbenchTodoFacade.class);
    
    /**
     * 创建已办记录
     * 
     * @param doneDTO 已办信息
     */
    public static void createDoneInfo(DoneDTO doneDTO) {
        if (StringUtil.isBlank(doneDTO.getTransactorid())) {
            doneDTO.setTransactorid(SystemHelper.getUserId());
            doneDTO.setTransactor(SystemHelper.getCurUserInfo().getEmployeeName());
        }
        workbenchDoneFacade.updateWorkfowDone(doneDTO);
    }
    
    /**
     * 更新非工作流待办数目
     * 
     * @param todoStatisticsDTOs 待办信息
     */
    public static void updateTodoStatistics(List<TodoStatisticsDTO> todoStatisticsDTOs) {
        workbenchTodoFacade.updateUnworkflowTodoStatistics(todoStatisticsDTOs);
    }
    
    /**
     * 发送消息
     * 
     * @param messageDTO 消息
     */
    public static void sendMessage(MessageDTO messageDTO) {
        if (messageDTO.getSendDate() == null) {
            messageDTO.setSendDate(new Date());
        }
        if (StringUtil.isBlank(messageDTO.getRecipientId())) {
            messageDTO.setRecipientId(SystemHelper.getUserId());
        }
        workbenchMessageFacade.sendMessage(messageDTO);
    }
}
