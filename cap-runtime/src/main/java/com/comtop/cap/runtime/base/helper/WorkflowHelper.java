/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.comtop.bpms.common.model.BatchWorkFlowParamVO;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.bpms.common.model.TodoTaskInfo;
import com.comtop.bpms.common.util.BpmsConstant;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.base.util.WorkflowConstant;
import com.comtop.top.core.util.StringUtil;

/**
 * 工作流帮助类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-12 李忠文
 */
public class WorkflowHelper {
    
    /**
     * 给工作流参数类设置当前用户相关信息
     * 
     * @param workflowParam 工作流参数类
     * @param dataName 数据名称
     */
    public static void setWorkflowParam(CapWorkflowParam workflowParam, String dataName) {
        if (StringUtil.isBlank(workflowParam.getCurrentUserId())) {
            workflowParam.setCurrentUserId(SystemHelper.getUserId());
        }
        if (StringUtil.isBlank(workflowParam.getCurrentUserName())) {
            workflowParam.setCurrentUserName(SystemHelper.getCurUserInfo().getEmployeeName());
        }
        if (StringUtil.isBlank(workflowParam.getOrgId())) {
            workflowParam.setOrgId(SystemHelper.getCurUserInfo().getOrgId());
        }
        // workflowParam.setCurrentUserId("SuperAdmin");
        // workflowParam.setCurrentUserName("SuperAdmin");
        // workflowParam.setOrgId("0");
        workflowParam.setDataName(dataName);
    }
    
    /**
     * 获取下发前的待办信息
     * 
     * @param workflowParam 工作流参数
     * @param processInsId 流程实例id
     * @param processId 流程编码
     * @return 待办信息
     */
    public static TodoTaskInfo queryBeforeForeTodoTaskInfo(CapWorkflowParam workflowParam, String processInsId,
        String processId) {
        TodoTaskInfo todoTaskInfo = null;
        String todoTaskId = workflowParam.getTaskId();
        if (StringUtil.isNotBlank(todoTaskId)) {
            todoTaskInfo = WorkflowCoreHelper.queryTodoTask(processId, todoTaskId);
        } else {
            TodoTaskInfo paramTask = new TodoTaskInfo();
            paramTask.setMainProcessId(processId);
            paramTask.setTransActorId(workflowParam.getCurrentUserId());
            paramTask.setMainProcessInsId(processInsId);
            List<TodoTaskInfo> tasks = WorkflowCoreHelper.queryTodoTasks(processId, paramTask);
            if (tasks != null && !tasks.isEmpty()) {
                todoTaskInfo = tasks.get(0);
                workflowParam.setTaskId(todoTaskInfo.getTodoTaskId());
            }
        }
        return todoTaskInfo;
    }
    
    /**
     * 流程是否已结束
     * 
     * @param processId 流程编码
     * @param processInsId 流程实例id
     * @return 流程是否已结束
     */
    public static boolean isProcessComplete(String processId, String processInsId) {
        ProcessInstanceInfo processInstanceInfo = WorkflowCoreHelper.queryProcessInsInfo(processId, processInsId);
        return processInstanceInfo.getState() == WorkflowConstant.WORK_FLOW_STATE_OVER ? true : false;
    }
    
    /**
     * 流程是否停留在第一批节点
     * 
     * @param waitingNodes 当前停留环节
     * @param processId 流程ID
     * @param paramMap 参数
     * @return 是否停留在第一批节点
     */
    public static boolean isWaitFirstNode(NodeInfo[] waitingNodes, String processId, Map<String, Object> paramMap) {
        if (waitingNodes == null || waitingNodes.length == 0) {
            return false;
        }
        NodeInfo[] firstNodes = WorkflowCoreHelper.queryFirstUserNodes(processId, paramMap);
        for (NodeInfo nodeInfo : firstNodes) {
            for (NodeInfo waitingNode : waitingNodes) {
                if (StringUtils.equals(nodeInfo.getNodeId(), waitingNode.getNodeId())) {
                    return true; // 停留在第一个节点
                }
            }
        }
        return false;
    }
    
    /**
     * 获取第一个节点的ID集合
     * 
     * @param processId 流程ID
     * @param paramMap 参数
     * @return 第一个节点的ID集合
     */
    public static List<String> queryFirstNodeIds(String processId, Map<String, Object> paramMap) {
        NodeInfo[] firstNodes = WorkflowCoreHelper.queryFirstUserNodes(processId, paramMap);
        List<String> ids = new ArrayList<String>();
        for (NodeInfo nodeInfo : firstNodes) {
            ids.add(nodeInfo.getNodeId());
        }
        return ids;
    }
    
    /**
     * 获取工作流数据表
     * 
     * @param processId 流程ID
     * @param isDone 是否为已完成
     * @return 工作流数据表
     */
    public static String readTaskTableName(String processId, boolean isDone) {
        return WorkflowCoreHelper.readTaskTableName(processId, isDone);
    }
    
    /**
     * 将任务信息转换成节点信息
     * 
     * @param todoTaskInfo 任务信息
     * @return 节点信息
     */
    public static NodeInfo taskInfoToNodeInfo(TodoTaskInfo todoTaskInfo) {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setNodeId(todoTaskInfo.getCurNodeId());
        nodeInfo.setNodeInstanceId(todoTaskInfo.getCurNodeInsId());
        nodeInfo.setNodeName(todoTaskInfo.getCurNodeName());
        nodeInfo.setNodeType(BpmsConstant.TASK_TYPE_USERTASK);
        nodeInfo.setProcessId(todoTaskInfo.getCurProcessId());
        nodeInfo.setProcessVersion(todoTaskInfo.getVersion());
        return nodeInfo;
    }

    /**
     * 根据工作流参数列表组装工作流批量操作所需的参数
     * 
     * @param workflowParamList 工作流参数列表
     * @return 工作流批量操作参数
     */
    public static BatchWorkFlowParamVO compareWorkFlowParams(List<? extends  CapWorkflowParam> workflowParamList) {
        
    	if(workflowParamList == null){
    		return null;
    	}
    	
        BatchWorkFlowParamVO workFlowParams = new BatchWorkFlowParamVO();
        List<String> lstTasIds = new ArrayList<String>();
        for (int i = 0; i < workflowParamList.size(); i++) {
           if (StringUtil.isNotEmpty(workflowParamList.get(i).getTaskId())) {
                lstTasIds.add(workflowParamList.get(i).getTaskId());
            }
        }
        if (lstTasIds.size() > 0) {
            workFlowParams.setTaskIds(lstTasIds.toArray(new String[lstTasIds.size()]));
        }
//        workFlowParams.setEntrySize(workflowParamList.size());
        workFlowParams.setTargetNodeInfos(workflowParamList.get(0).getTargetNodeInfos());
        workFlowParams.setCurrentUserId(SystemHelper.getUserId());
        workFlowParams.setCurrentUserName(SystemHelper.getCurUserInfo().getEmployeeName());
        workFlowParams.setOrgId(SystemHelper.getCurUserInfo().getOrgId());
        workFlowParams.setOpinion(workflowParamList.get(0).getOpinion());
        return workFlowParams;
    }
}
