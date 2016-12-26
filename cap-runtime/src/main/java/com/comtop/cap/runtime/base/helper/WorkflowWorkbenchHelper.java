/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.helper;

import java.util.ArrayList;
import java.util.List;

import com.comtop.bpms.common.model.DoneTaskInfo;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.top.workbench.common.model.CommonDTO;
import com.comtop.top.workbench.done.model.DoneDTO;

/**
 * 工作台操作帮助类（已办操作）
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-13 李忠文
 */
public final class WorkflowWorkbenchHelper {
    
    /**
     * 私有构造函数，避免通过new的方式实例化
     */
    private WorkflowWorkbenchHelper() {
    }
    
    /**
     * 拼装处理人及停留节点信息
     * 
     * @param workflowParam 工作流对象
     * @param doneDTO 已办对象
     */
    public static void compareTransactorAndNodeInfo(CapWorkflowParam workflowParam, DoneDTO doneDTO) {
        if (workflowParam == null) {
            return;
        }
        // 工单处理人ID
        doneDTO.setTransactorid(workflowParam.getCurrentUserId());
        // 工单处理人
        doneDTO.setTransactor(workflowParam.getCurrentUserName());
        // 当前停留节点信息集合（ID和NAME）
        NodeInfo[] objWaitNodes = workflowParam.getWaitingNodeInfos();
        List<CommonDTO> lstLastNodeList = new ArrayList<CommonDTO>();
        if (objWaitNodes != null && objWaitNodes.length > 0) {
            for (NodeInfo lstNodeInfo : objWaitNodes) {
                CommonDTO objCommonDTO = new CommonDTO();
                objCommonDTO.setId(lstNodeInfo.getNodeId());
                objCommonDTO.setName(lstNodeInfo.getNodeName());
                lstLastNodeList.add(objCommonDTO);
            }
        }
        doneDTO.setLastNodeList(lstLastNodeList);
    }
    
    /**
     * 获取已办信息ID
     * 
     * @param workflowParam 流程信息
     * @param currNodeId 当前节点ID
     * @param activityInsId 活动实例编号
     * @return 已办信息ID
     */
    public static String getDoneTaskId(CapWorkflowParam workflowParam, String currNodeId, String activityInsId) {
        DoneTaskInfo objDoneTaskInfo = new DoneTaskInfo();
        objDoneTaskInfo.setMainProcessId(workflowParam.getProcessId());
        objDoneTaskInfo.setMainProcessInsId(workflowParam.getProcessInsId());
        objDoneTaskInfo.setCurNodeId(currNodeId);
        objDoneTaskInfo.setTransActorId(SystemHelper.getUserId());
        objDoneTaskInfo.setActivityInsId(activityInsId);
        List<DoneTaskInfo> objDoneTaskInfos = WorkflowCoreHelper.queryDoneTasks(workflowParam.getProcessId(),
            objDoneTaskInfo);
        if (objDoneTaskInfos == null || objDoneTaskInfos.size() == 0) {
            return "";
        }
        return objDoneTaskInfos.get(0).getDoneTaskId();
    }
}
