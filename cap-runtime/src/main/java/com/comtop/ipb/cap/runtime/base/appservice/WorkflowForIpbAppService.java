/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.ipb.cap.runtime.base.appservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.bpms.client.ClientFactory;
import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.bpms.common.model.DoneTaskInfo;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.bpms.common.model.TaskInfo;
import com.comtop.bpms.common.model.TodoTaskInfo;
import com.comtop.bpms.common.model.UserInfo;
import com.comtop.cap.runtime.base.appservice.CapWorkflowAppService;
import com.comtop.cap.runtime.base.exception.CapWorkflowException;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.bpmsext.appservice.BpmsVarExtAppService;
import com.comtop.cap.runtime.bpmsext.model.BpmsVarExtVO;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.JSON;
import com.comtop.ipb.cap.runtime.base.model.WorkflowForIpbVO;
import com.comtop.top.workbench.done.model.DoneDTO;

/**
 * @author luozhenming
 * @param <VO>
 *            招采平台流程审批基类
 *
 */
public abstract class WorkflowForIpbAppService<VO extends WorkflowForIpbVO> extends CapWorkflowAppService<VO> {
    
    /** 日志对象 */
    static final Logger LOG = LoggerFactory.getLogger(WorkflowForIpbAppService.class);
    
    /** 注入Service */
    @PetiteInject
    protected BpmsVarExtAppService bpmsVarExtAppService;
    
    /** 会签状态-开始 1 */
    private static final String IPB_STATUS_START = "start";
    
    /** 会签状态-进行中 2 */
    private static final String IPB_STATUS_RUNNING = "running";
    
    /** 会签状态-结束 会签返回本人的结束标志 */
    private static final String IPB_STATUS_END_BACKSELF_TRUE = "end_backself_true";
    
    /** 会签状态-结束 会签不返回本人的结束标志 */
    private static final String IPB_STATUS_END_BACKSELF_FALSE = "end_backself_false";
    
    /** 会签状态 */
    private static final String IPB_KEY_STATUS = "ipb_countersign_status";
    
    /** 会签发起人的节点用户信息 */
    private static final String IPB_KEY_NODEUSERINFO = "ipb_back_self_nodeuserinfo";
    
    /** 回退给自己 */
    private static final String IPB_KEY_BACKSELF = "backSelf";
    
    /** 会签发起人ID */
    private static final String IPB_KEY_BACK_SELF_USER_ID = "ipb_back_self_userId";
    
    /** 部门ID */
    private static final String IPB_KEY_CURRENT_ORG_ID = "comtop_bpms_current_orgId";
    
    /**
     * 查询节点实例配置的变量
     * 
     * @param workflowParam
     *            工作流参数
     * @return 查询节点实例配置的变量
     */
    public List<BpmsVarExtVO> queryBpmsVarExtList(CapWorkflowParam workflowParam) {
        String strProcessId = getProcessIdFromParam(workflowParam);
        return this.queryBpmsVarExtList(strProcessId, workflowParam.getProcessInsId(), workflowParam.getCurrNodeId());
    }
    
    /**
     * 获取指定版本流程所有用户节点信息,如果传递了当前节点则查该节点之后的
     * 
     * @param workflowParam
     *            工作流参数
     * @return 流程所有用户节点集合，涵盖用户节点里配置的人员
     */
    public List<NodeInfo> queryAllUserNodesByVersion(CapWorkflowParam workflowParam) {
        String curNodeId = (String) workflowParam.getParamMap().get("query_node_id");
        putBpmsVarExt(workflowParam);
        String strProcessId = getProcessIdFromParam(workflowParam);
        try {
            return ClientFactory.getUserTaskService().queryAllUserNodesByVersion(curNodeId, strProcessId,
                workflowParam.getProcessVersion(), workflowParam.getParamMap());
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException("查询流程实例处理跟踪出现异常：curNodeId=" + curNodeId + ",processId:" + strProcessId
                + ",version:" + workflowParam.getProcessVersion() + ",varMap:" + workflowParam.getParamMap(), e);
        }
    }
    
    /**
     * 是否需要显示会签按钮
     * 
     * @param workflowParam 工作流参数
     * @return 查询节点实例配置的变量
     * @throws AbstractBpmsException AbstractBpmsException
     */
    public boolean isShowCountersignButton(CapWorkflowParam workflowParam) throws AbstractBpmsException {
        String strProcessId = getProcessIdFromParam(workflowParam);
        
        if (StringUtil.isEmpty(workflowParam.getCurrNodeId()) || StringUtil.isEmpty(strProcessId)) {
            return false;
        }
        
        boolean isMuti = ClientFactory.getUserTaskService().isMultiInsNode(workflowParam.getCurrNodeId(), strProcessId,
            workflowParam.getProcessVersion());
        
        if (!isMuti) {
            return false;
        }
        String cStatus = null;
        List<BpmsVarExtVO> lstBpmsVarExt = this.queryBpmsVarExtList(strProcessId, workflowParam.getProcessInsId(),
            workflowParam.getCurrNodeId());
        for (Iterator<BpmsVarExtVO> iterator = lstBpmsVarExt.iterator(); iterator.hasNext();) {
            BpmsVarExtVO bpmsVarExtVO = iterator.next();
            if (IPB_KEY_STATUS.equals(bpmsVarExtVO.getVariableKey())) {
                cStatus = bpmsVarExtVO.getVariableValue();
                break;
            }
        }
        if (IPB_STATUS_RUNNING.equals(cStatus)) {
            return false;
        }
        
        int todoCount = this.queryNodeInsTodoTaskCount(strProcessId,
            (String) workflowParam.getParamMap().get("curNodeInsId"));
        if (todoCount > 1) {
            return false;
        }
        return true;
    }
    
    /**
     * 1.发送时将数据库会签状态变量存放到paramMap中
     * 2.如果发送是会签且返回给本人 将发送的节点和用户改为会签发起人及所在节点
     * 
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#fore(com.comtop.cap.runtime.base.model.CapWorkflowParam)
     */
    @Override
    public void fore(CapWorkflowParam workflowParam) {
        // 将运行时变量放入 workflowParam 工作流参数中
        this.putBpmsVarExt(workflowParam);
        
        Map<String, Object> paramMap = workflowParam.getParamMap();
        String backSelf = (String) paramMap.get(IPB_KEY_BACKSELF);
        String countersignStatus = (String) paramMap.get(IPB_KEY_STATUS);
        String curNodeInsId = (String) paramMap.get("curNodeInsId");
        if ("true".equals(backSelf) && IPB_STATUS_RUNNING.equals(countersignStatus)
            && isLeaveNode(curNodeInsId, workflowParam)) {
            handlerNodeUser(workflowParam);
        }
        
        super.fore(workflowParam);
    }
    
    /**
     *
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#batchFore(java.util.List)
     */
    @Override
    public void batchFore(List<CapWorkflowParam> workflowParamList) {
        if (workflowParamList != null) {
            for (CapWorkflowParam workflowParam : workflowParamList) {
                this.putBpmsVarExt(workflowParam);
                handlerNodeUser(workflowParam);
            }
        }
        super.batchFore(workflowParamList);
    }
    
    /**
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#foreCallback(com.comtop.cap.runtime.base.model.CapWorkflowVO,
     *      com.comtop.cap.runtime.base.model.CapWorkflowParam, com.comtop.bpms.common.model.ProcessInstanceInfo)
     */
    @Override
    protected void foreCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        handleForeCountersign(workflowParam, processInstanceInfo);
        super.foreCallback(vo, workflowParam, processInstanceInfo);
    }
    
    /**
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#batchForeCallback(java.util.List,
     *      java.util.List, com.comtop.bpms.common.model.ProcessInstanceInfo[], int)
     */
    @Override
    protected void batchForeCallback(List<VO> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        
        for (int i = 0; i < processInstanceInfos.length; i++) {
            handleForeCountersign(workflowParamList.get(i), processInstanceInfos[i]);
        }
        super.batchForeCallback(workRecords, workflowParamList, processInstanceInfos, targetFlowState);
    }
    
    /**
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#backEntryCallback(com.comtop.cap.runtime.base.model.CapWorkflowVO,
     *      com.comtop.cap.runtime.base.model.CapWorkflowParam, com.comtop.bpms.common.model.ProcessInstanceInfo)
     */
    @Override
    protected void backEntryCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        // 回退申请人只需要删除掉当前节点的状态变量 故传入一个空Map集合
        Map<String, Object> bpmsVarMap = new HashMap<String, Object>();
        this.saveOrUpdateBpmsVar(workflowParam, bpmsVarMap, processInstanceInfo.getMainProcessInsId());
        super.backEntryCallback(vo, workflowParam, processInstanceInfo);
    }
    
    /**
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#backCallback(com.comtop.cap.runtime.base.model.CapWorkflowVO,
     *      com.comtop.cap.runtime.base.model.CapWorkflowParam, com.comtop.bpms.common.model.ProcessInstanceInfo)
     */
    @Override
    protected void backCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        handleBackCountersign(workflowParam, processInstanceInfo);
        super.backCallback(vo, workflowParam, processInstanceInfo);
    }
    
    /**
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#batchBackCallback(java.util.List,
     *      java.util.List, com.comtop.bpms.common.model.ProcessInstanceInfo[], int)
     */
    @Override
    protected void batchBackCallback(List<VO> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        if (workflowParamList != null && workflowParamList.size() == 0) {
            for (int i = 0; i < processInstanceInfos.length; i++) {
                handleBackCountersign(workflowParamList.get(i), processInstanceInfos[i]);
            }
        }
        
        super.batchBackCallback(workRecords, workflowParamList, processInstanceInfos, targetFlowState);
    }
    
    /**
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#undoCallback(com.comtop.cap.runtime.base.model.CapWorkflowVO,
     *      com.comtop.cap.runtime.base.model.CapWorkflowParam, com.comtop.bpms.common.model.ProcessInstanceInfo)
     */
    @Override
    protected void undoCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        if (workflowParam == null || processInstanceInfo == null) {
        	 // 更新已办
//        	updateWorkFlowDoneByTodoTask(workflowParam, vo, null, WorkbenchConstant.DONE_OPRATE_UNDO);
            super.undoCallback(vo, workflowParam, processInstanceInfo);
            return;
        }
        if (StringUtils.isBlank(workflowParam.getProcessInsId()) || StringUtils.isBlank(workflowParam.getCurrNodeId())) {
        	 // 更新已办
//        	updateWorkFlowDoneByTodoTask(workflowParam, vo, null, WorkbenchConstant.DONE_OPRATE_UNDO);
            super.undoCallback(vo, workflowParam, processInstanceInfo);
            return;
        }
        boolean isLeaveNodeIns = isLeaveNodeIns(processInstanceInfo.getTaskInfo(), 3);
        if (isLeaveNodeIns) {
            Map<String, Object> bpmsVarMap = handleUndoCountersignStatus(workflowParam, processInstanceInfo);
            this.saveOrUpdateBpmsVar(workflowParam, bpmsVarMap, processInstanceInfo.getMainProcessInsId());
        }
        // 更新已办
//    	updateWorkFlowDoneByTodoTask(workflowParam, vo, null, WorkbenchConstant.DONE_OPRATE_UNDO);
        super.undoCallback(vo, workflowParam, processInstanceInfo);
    }
    
    /**
     * 1. 发送时会签状态不为空 需要更新当前节点的会签状态
     * 2. 发送时需要删除目标节点的会签状态（历史数据）
     * 
     * @param workflowParam
     *            工作流参数
     * @param processInstanceInfo
     *            流程实例信息
     */
    private void handleForeCountersign(CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        if (workflowParam == null || processInstanceInfo == null) {
            return;
        }
        // 流程实例id
        String mainProcessInsId = processInstanceInfo.getMainProcessInsId();
        TaskInfo taskInfo = processInstanceInfo.getTaskInfo();
        Map<String, Object> paramMap = workflowParam.getParamMap();
        if (StringUtils.isBlank((String) paramMap.get(IPB_KEY_STATUS))) {
            return;
        }
        if (StringUtils.isBlank(workflowParam.getProcessInsId()) || StringUtils.isBlank(workflowParam.getCurrNodeId())) {
            return;
        }
        boolean isLeaveNodeIns = isLeaveNodeIns(taskInfo, 1);// 是否离开节点实例
        
        if (isLeaveNodeIns) {
            Map<String, Object> bpmsVarMap = handleForeCountersignStatus(workflowParam);
            this.saveOrUpdateBpmsVar(workflowParam, bpmsVarMap, mainProcessInsId);
        }
        
        boolean isLeaveNode = isLeaveNode(taskInfo, 1);// 是否离开节点
        if (isLeaveNode) {// 当下发离开节点时 删除下发目标节点的历史会签状态记录
            TodoTaskInfo[] todoTaskInfos = taskInfo.getAddTodoTaskInfos();
            for (int j = 0; j < todoTaskInfos.length; j++) {
                TodoTaskInfo todoTaskInfo = todoTaskInfos[j];
                if (StringUtils.isBlank(todoTaskInfo.getCurNodeId())) {
                    continue;
                }
                bpmsVarExtAppService.deleteBpmsVar(todoTaskInfo.getMainProcessInsId(), todoTaskInfo.getCurNodeId());
            }
        }
    }
    
    /**
     * 1.会签状态为开始 改为运行中
     * 2.会签状态为运行中改为结束
     * 3.会签状态为结束 不做任何处理
     * 
     * @param workflowParam
     *            工作流参数
     * @return 新的会签状态变量
     */
    private Map<String, Object> handleForeCountersignStatus(CapWorkflowParam workflowParam) {
        Map<String, Object> bpmsVarMap = new HashMap<String, Object>();
        
        String currCountersignStatus = (String) workflowParam.getParamMap().get(IPB_KEY_STATUS);
        String currBackSelf = (String) workflowParam.getParamMap().get(IPB_KEY_BACKSELF);
        String newCountersignStatus = null;
        String newBackSelf = null;
        // 点击增加会签人按钮触发的下发操作回调会进入到该分支 回调中下发已经完成 把状态改为运行中
        if (IPB_STATUS_START.equals(currCountersignStatus)) {
            bpmsVarMap.put(IPB_KEY_CURRENT_ORG_ID, workflowParam.getOrgId());
            bpmsVarMap.put(IPB_KEY_BACK_SELF_USER_ID, workflowParam.getCurrentUserId());
            bpmsVarMap.put(IPB_KEY_NODEUSERINFO, getNodeUserInfoString(workflowParam));
            newCountersignStatus = IPB_STATUS_RUNNING;
            newBackSelf = currBackSelf;
            bpmsVarMap.put(IPB_KEY_STATUS, newCountersignStatus);
            bpmsVarMap.put(IPB_KEY_BACKSELF, newBackSelf);
        } else if (IPB_STATUS_RUNNING.equals(currCountersignStatus)) {// 会签运行中 下发完成状态改为结束
            newCountersignStatus = "true".equals(currBackSelf) ? IPB_STATUS_END_BACKSELF_TRUE
                : IPB_STATUS_END_BACKSELF_FALSE;
            newBackSelf = "false";
            bpmsVarMap.put(IPB_KEY_STATUS, newCountersignStatus);
            bpmsVarMap.put(IPB_KEY_BACKSELF, newBackSelf);
        } else {// 当前会签状态为结束 暂时不删除 回退需要使用
            String strProcessId = getProcessIdFromParam(workflowParam);
            return getBpmsVarMap(strProcessId, workflowParam.getProcessInsId(), workflowParam.getCurrNodeId());
        }
        
        return bpmsVarMap;
    }
    
    /**
     * 获得会签发起人信息及所在节点信息 以json字符串形式返回
     * 
     * @param workflowParam 工作流参数
     * @return 会签发起人信息及所在节点信息
     */
    private String getNodeUserInfoString(CapWorkflowParam workflowParam) {
        Map<String, Object> paramMap = workflowParam.getParamMap();
        NodeInfo objNodeInfo = new NodeInfo();
        objNodeInfo.setNodeId((String) paramMap.get("nodeId"));
        objNodeInfo.setNodeName((String) paramMap.get("nodeName"));
        objNodeInfo.setNodeType("USERTASK");
        objNodeInfo.setProcessId((String) paramMap.get("processId"));
        objNodeInfo.setProcessVersion(workflowParam.getProcessVersion());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(workflowParam.getCurrentUserId());
        userInfo.setUserName(workflowParam.getCurrentUserName());
        UserInfo[] arrUserInfo = new UserInfo[1];
        arrUserInfo[0] = userInfo;
        objNodeInfo.setUsers(arrUserInfo);
        
        return JSON.toJSONString(objNodeInfo);
    }
    
    /**
     * 撤回时控制会签状态
     * 
     * @param workflowParam
     *            工作流参数
     * @param processInstanceInfo
     *            工作流参数
     * @return 新的会签状态变量
     */
    private Map<String, Object> handleUndoCountersignStatus(CapWorkflowParam workflowParam,
        ProcessInstanceInfo processInstanceInfo) {
        String strProcessId = getProcessIdFromParam(workflowParam);
        Map<String, Object> oldBpmsVarMap = getBpmsVarMap(strProcessId, workflowParam.getProcessInsId(),
            workflowParam.getCurrNodeId());
        
        String currCountersignStatus = (String) oldBpmsVarMap.get(IPB_KEY_STATUS);
        
        boolean isLeaveNode = isLeaveNode(processInstanceInfo.getTaskInfo(), 3);
        
        HashMap<String, Object> bpmsVarMap = new HashMap<String, Object>();
        String newCountersignStatus = null;
        String newBackSelf = null;
        if (!isLeaveNode) {// 撤回时没有离开节点 说明之前发送操作是发到了本节点
            if (IPB_STATUS_END_BACKSELF_TRUE.equals(currCountersignStatus)) {
                // 会签人 发送给（返回本人） 会签发起人， 触发该分支
                newCountersignStatus = IPB_STATUS_RUNNING;
                newBackSelf = "true";
                bpmsVarMap.put(IPB_KEY_STATUS, newCountersignStatus);
                bpmsVarMap.put(IPB_KEY_BACKSELF, newBackSelf);
            } else if (IPB_STATUS_END_BACKSELF_FALSE.equals(currCountersignStatus)) {
                // 这种情况不存在
                return oldBpmsVarMap;
            } else {// 会签发起人 增加会签人后 点击撤回触发
                return bpmsVarMap;// 会签状态运行中 都需要将变量删除掉
            }
        } else {
            if (IPB_STATUS_END_BACKSELF_TRUE.equals(currCountersignStatus)) {
                // 这种情况不需要修改状态
                return oldBpmsVarMap;
            } else if (IPB_STATUS_END_BACKSELF_FALSE.equals(currCountersignStatus)) {
                newCountersignStatus = IPB_STATUS_RUNNING;
                newBackSelf = "false";
                bpmsVarMap.put(IPB_KEY_STATUS, newCountersignStatus);
                bpmsVarMap.put(IPB_KEY_BACKSELF, newBackSelf);
            } else {
                return bpmsVarMap;// 会签状态运行中 都需要将变量删除掉
            }
        }
        return bpmsVarMap;
    }
    
    /**
     * 控制回退会签
     * 
     * @param workflowParam
     *            工作流参数
     * @param processInstanceInfo
     *            流程实例ID
     */
    private void handleBackCountersign(CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        if (workflowParam == null || processInstanceInfo == null) {
            return;
        }
        if (StringUtils.isBlank(workflowParam.getProcessInsId()) || StringUtils.isBlank(workflowParam.getCurrNodeId())) {
            return;
        }
        TaskInfo taskInfo = processInstanceInfo.getTaskInfo();
        boolean isLeaveNodeIns = isLeaveNodeIns(taskInfo, 2);
        if (isLeaveNodeIns) {
            TodoTaskInfo[] todoTaskInfos = taskInfo.getAddTodoTaskInfos();
            // 回退目标节点ID集合
            Set<String> todoNodeSet = new HashSet<String>();
            for (int i = 0; i < todoTaskInfos.length; i++) {
                TodoTaskInfo todoTaskInfo = todoTaskInfos[i];
                todoNodeSet.add(todoTaskInfo.getCurNodeId());
            }
            // 回退修改目标节点的会签状态
            for (Iterator<String> iterator = todoNodeSet.iterator(); iterator.hasNext();) {
                String nodeId = iterator.next();
                Map<String, Object> bpmsVarMap = handleBackCountersignStatus(taskInfo, nodeId);
                this.saveOrUpdateBpmsVar(workflowParam, bpmsVarMap, processInstanceInfo.getMainProcessInsId());
            }
            
        }
    }
    
    /**
     * 控制回退操作会签状态
     * 
     * @param taskInfo
     *            任务信息
     * @param curNodeId
     *            当前节点
     * 
     * @return 新的会签状态变量
     */
    private Map<String, Object> handleBackCountersignStatus(TaskInfo taskInfo, String curNodeId) {
        String processId = taskInfo.getAddTodoTaskInfos()[0].getMainProcessId();
        String mainProcessInsId = taskInfo.getAddTodoTaskInfos()[0].getMainProcessInsId();
        
        Map<String, Object> oldBpmsVarMap = getBpmsVarMap(processId, mainProcessInsId, curNodeId);
        
        String currCountersignStatus = (String) oldBpmsVarMap.get(IPB_KEY_STATUS);
        HashMap<String, Object> bpmsVarMap = new HashMap<String, Object>();
        String newCountersignStatus = null;
        String newBackSelf = null;
        
        boolean isLeaveNode = isLeaveNode(taskInfo, 1);
        if (!isLeaveNode) {// 没有离开节点
            if (IPB_STATUS_END_BACKSELF_TRUE.equals(currCountersignStatus)) {
                // 会签人返回本人后 到达会签发起人 此时会签发起人点击回退触发
                newCountersignStatus = IPB_STATUS_RUNNING;
                newBackSelf = "true";
            } else if (IPB_STATUS_END_BACKSELF_FALSE.equals(currCountersignStatus)) {// 这种情况不存在
                newCountersignStatus = IPB_STATUS_RUNNING;
                newBackSelf = "false";
            } else {// 会签人点击回退给会签发起人
                return bpmsVarMap;// 会签状态运行中 都需要将变量删除掉
            }
        } else {
            if (IPB_STATUS_END_BACKSELF_TRUE.equals(currCountersignStatus)) {
                // 会签人发送到会签发起人 然后会签发起人再发送到下一个节点 下一个节点用户点击回退触发
                return oldBpmsVarMap;
            } else if (IPB_STATUS_END_BACKSELF_FALSE.equals(currCountersignStatus)) {// 会签人发送到下一个节点（会签不返回本人）
                                                                                     // 下一个节点用户点击回退触发
                newCountersignStatus = IPB_STATUS_RUNNING;
                newBackSelf = "false";
            } else {// 会签人点击回退给会签发起人
                return bpmsVarMap;// 会签状态运行中 都需要将变量删除掉
            }
        }
        bpmsVarMap.put(IPB_KEY_STATUS, newCountersignStatus);
        bpmsVarMap.put(IPB_KEY_BACKSELF, newBackSelf);
        return bpmsVarMap;
    }
    
    /**
     * 1.发送时将数据库会签状态变量存放到paramMap中
     * 2.如果发送是会签且返回给本人 将发送的节点和用户改为会签发起人及所在节点
     * 
     * @param workflowParam
     *            工作流参数
     */
    private void handlerNodeUser(CapWorkflowParam workflowParam) {
        Map<String, Object> paramMap = workflowParam.getParamMap();
        String curNodeInsId = (String) paramMap.get("curNodeInsId");
        String nodeUserInfo = (String) paramMap.get(IPB_KEY_NODEUSERINFO);
        if (StringUtils.isBlank(nodeUserInfo)) {
            return;
        }
        NodeInfo objNodeInfo = JSON.parseObject(nodeUserInfo, NodeInfo.class);
        objNodeInfo.setNodeInstanceId(curNodeInsId);
        NodeInfo[] arrNodeInfo = new NodeInfo[1];
        arrNodeInfo[0] = objNodeInfo;
        workflowParam.setTargetNodeInfos(arrNodeInfo);
    }
    
    /**
     * 查询节点实例配置的变量
     * 
     * @param processId processId
     * 
     * @param mainProcessInsId
     *            主流程实例ID
     * @param currNodeId
     *            当前节点ID
     * 
     * @return 查询节点实例配置的变量
     */
    private List<BpmsVarExtVO> queryBpmsVarExtList(String processId, String mainProcessInsId, String currNodeId) {
        BpmsVarExtVO condition = new BpmsVarExtVO();
        condition.setMainProcessId(processId);
        condition.setMainProcessInsId(mainProcessInsId);
        condition.setCurNodeId(currNodeId);
        return bpmsVarExtAppService.queryBpmsVarExtList(condition);
    }
    
    /**
     * 将运行时变量放入 workflowParam 工作流参数中
     * 
     * @param workflowParam
     *            工作流参数对象
     */
    private void putBpmsVarExt(CapWorkflowParam workflowParam) {
        HashMap<String, Object> map = workflowParam.getParamMap();
        Object countersign_status = map.get(IPB_KEY_STATUS);
        if (!IPB_STATUS_START.equals(countersign_status)) {
            List<BpmsVarExtVO> lst = queryBpmsVarExtList(workflowParam);
            for (BpmsVarExtVO vo : lst) {
                map.put(vo.getVariableKey(), vo.getVariableValue());
            }
        }
        if (map.get(IPB_KEY_STATUS) == null || map.get(IPB_KEY_BACKSELF) == null) {
            map.put(IPB_KEY_BACKSELF, "false");
            map.put(IPB_KEY_STATUS, "");
        }
    }
    
    /**
     * 将运行时变量放入 workflowParam 工作流参数中
     * 
     * @param workflowParam 工作流参数对象
     * 
     * @param map
     *            工作流参数对象
     */
    public void putBpmsVarExt(CapWorkflowParam workflowParam, Map<String, Object> map) {
        Object countersign_status = map.get(IPB_KEY_STATUS);
        if (!IPB_STATUS_START.equals(countersign_status)) {
            List<BpmsVarExtVO> lst = queryBpmsVarExtList(workflowParam);
            for (BpmsVarExtVO vo : lst) {
                map.put(vo.getVariableKey(), vo.getVariableValue());
            }
        }
        if (map.get(IPB_KEY_STATUS) == null || map.get(IPB_KEY_BACKSELF) == null) {
            map.put(IPB_KEY_BACKSELF, "false");
            map.put(IPB_KEY_STATUS, "");
        }
    }
    
    /**
     * 更新bpmsVarExt 运行时变量
     * 
     * @param workflowParam
     *            工作流参数
     * @param bpmsVarMap
     *            需要更新的值
     * @param mainProcessInsId
     *            主流程实例ID
     */
    private void saveOrUpdateBpmsVar(CapWorkflowParam workflowParam, Map<String, Object> bpmsVarMap,
        String mainProcessInsId) {
        
        if (bpmsVarMap.size() == 0) {// 清除该节点的变量
            bpmsVarExtAppService.deleteBpmsVar(mainProcessInsId, workflowParam.getCurrNodeId());
            return;
        }
        
        List<BpmsVarExtVO> lstBpmsVarExtVO = this.queryBpmsVarExtList(workflowParam);
        
        Map<String, BpmsVarExtVO> mapBpmsVarExtVO = new HashMap<String, BpmsVarExtVO>();
        for (Iterator<BpmsVarExtVO> iterator = lstBpmsVarExtVO.iterator(); iterator.hasNext();) {
            BpmsVarExtVO bpmsVarExtVO = iterator.next();
            mapBpmsVarExtVO.put(bpmsVarExtVO.getVariableKey(), bpmsVarExtVO);
        }
        
        List<BpmsVarExtVO> resultList = new ArrayList<BpmsVarExtVO>();
        Set<Entry<String, Object>> entrySet = bpmsVarMap.entrySet();
        for (Iterator<Entry<String, Object>> iterator = entrySet.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = iterator.next();
            String varKey = entry.getKey();
            String varValue = String.valueOf(entry.getValue());
            BpmsVarExtVO bpmsVarExtVO = mapBpmsVarExtVO.get(varKey);
            if (bpmsVarExtVO != null) {
                bpmsVarExtVO.setVariableKey(varKey);
                bpmsVarExtVO.setVariableValue(varValue);
            } else {
                bpmsVarExtVO = new BpmsVarExtVO();
                bpmsVarExtVO.setCurNodeId(workflowParam.getCurrNodeId());
                bpmsVarExtVO.setVariableKey(varKey);
                bpmsVarExtVO.setVariableValue(varValue);
                bpmsVarExtVO.setVariableType("java.lang.String");
                bpmsVarExtVO.setMainProcessId(getProcessIdFromParam(workflowParam));
                bpmsVarExtVO.setMainProcessInsId(mainProcessInsId);
            }
            resultList.add(bpmsVarExtVO);
        }
        bpmsVarExtAppService.saveBpmsVarExtList(resultList);
    }
    
    /**
     * 获得bpms会签变量map
     * 
     * @param processId processId
     * @param processInsId 流程实例ID
     * @param curNodeId 当前节点ID
     * @return bpms会签变量map
     */
    private Map<String, Object> getBpmsVarMap(String processId, String processInsId, String curNodeId) {
        List<BpmsVarExtVO> lstBpmsVarExtVO = this.queryBpmsVarExtList(processId, processInsId, curNodeId);
        
        Map<String, Object> bpmsVarMap = new HashMap<String, Object>();
        for (Iterator<BpmsVarExtVO> iterator = lstBpmsVarExtVO.iterator(); iterator.hasNext();) {
            BpmsVarExtVO bpmsVarExtVO = iterator.next();
            bpmsVarMap.put(bpmsVarExtVO.getVariableKey(), bpmsVarExtVO.getVariableValue());
        }
        return bpmsVarMap;
    }
    
    /**
     * 是否离开节点实例
     * 
     * @param taskInfo
     *            任务信息
     * @param operType 1 下发 2 回退 3 撤回
     * @return null 离开节点实例，非空反馈会 未离开的节点实例Id
     */
    private boolean isLeaveNodeIns(TaskInfo taskInfo, int operType) {
        if (taskInfo == null) {
            return false;
        }
        TodoTaskInfo[] todo = operType == 3 ? taskInfo.getDelTodoTaskInfos() : taskInfo.getAddTodoTaskInfos();
        DoneTaskInfo[] done = operType == 3 ? taskInfo.getDelDoneTaskInfos() : taskInfo.getAddDoneTaskInfos();
        if (todo == null || todo.length == 0) {
            return false;
        }
        if (done == null || done.length == 0) {
            return false;
        }
        // 新增的待办和新增的已办都在同一个节点实例下，则说明流程还未离开当前节点实例
        if (todo[0].getCurNodeInsId().equals(done[0].getCurNodeInsId())) {
            return false;
        }
        return true;
    }
    
    /**
     * 是否离开节点
     * 
     * @param taskInfo
     *            任务信息
     * @param operType 1 下发 2 回退 3 撤回
     * @return false 未离开节点
     */
    private boolean isLeaveNode(TaskInfo taskInfo, int operType) {
        if (taskInfo == null) {
            return false;
        }
        TodoTaskInfo[] todo = operType == 3 ? taskInfo.getDelTodoTaskInfos() : taskInfo.getAddTodoTaskInfos();
        DoneTaskInfo[] done = operType == 3 ? taskInfo.getDelDoneTaskInfos() : taskInfo.getAddDoneTaskInfos();
        if (todo == null || todo.length == 0) {
            return false;
        }
        if (done == null || done.length == 0) {
            return false;
        }
        // 新增的待办和新增的已办都在同一个节点实例下，则说明流程还未离开当前节点实例
        if (todo[0].getCurNodeId().equals(done[0].getCurNodeId())) {
            return false;
        }
        return true;
    }
    
    /**
     * 是否离开节点
     * 
     * @param curNodeInsId
     *            curNodeInsId
     * @param param 流程参数
     * @return boolean
     */
    private boolean isLeaveNode(String curNodeInsId, CapWorkflowParam param) {
        String strProcessId = getProcessIdFromParam(param);
        int todoCount = this.queryNodeInsTodoTaskCount(strProcessId, curNodeInsId);
        return todoCount == 1 ? true : false;
    }
    
    /**
     * 创建已办消息中心的对象
     * 
     * @param vo 业务单vo对象
     * @return 已办信息对象
     */
    @Override
    public DoneDTO createDoneInfo(VO vo) {
        DoneDTO doneDTO = super.createDoneInfo(vo);
        doneDTO.setExtendAttr(vo.getTaskId() + "|" + "done");
        return doneDTO;
    }
}
