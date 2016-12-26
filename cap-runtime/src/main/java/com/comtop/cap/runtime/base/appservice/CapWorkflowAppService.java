/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.appservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.bpms.common.model.BatchWorkFlowParamVO;
import com.comtop.bpms.common.model.DoneTaskInfo;
import com.comtop.bpms.common.model.NodeExtendAttributeInfo;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.NodeTodoTaskCntInfo;
import com.comtop.bpms.common.model.NodeTrackInfo;
import com.comtop.bpms.common.model.ProcessInfo;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.bpms.common.model.ReassignResultInfo;
import com.comtop.bpms.common.model.TodoTaskInfo;
import com.comtop.bpms.common.model.UserInfo;
import com.comtop.cap.runtime.base.exception.CapWorkflowException;
import com.comtop.cap.runtime.base.model.BpmsNodeInfo;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.base.model.CapWorkflowVO;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cap.runtime.base.util.SystemHelper;
import com.comtop.cap.runtime.base.util.WorkbenchConstant;
import com.comtop.cap.runtime.base.util.WorkbenchHelper;
import com.comtop.cap.runtime.base.util.WorkflowConstant;
import com.comtop.cap.runtime.base.util.WorkflowHelper;
import com.comtop.cap.runtime.base.util.WorkflowWorkbenchHelper;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.sys.usermanagement.organization.model.OrganizationInfoVO;
import com.comtop.top.sys.usermanagement.user.model.UserDTO;
import com.comtop.top.workbench.done.model.DoneDTO;

/**
 * 工作流功能实现
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-12 李忠文
 * @param <VO> 工作流VO基类
 */
public abstract class CapWorkflowAppService<VO extends CapWorkflowVO> extends CapBaseAppService<VO> {
    
    /** 日志对象 */
    static final Logger LOG = LoggerFactory.getLogger(CapWorkflowAppService.class);
    
    /** 注入BpmsService */
    protected CapWorkflowCoreService capWorkflowCoreService = BeanContextUtil
        .getBeanFromJoddContext(CapWorkflowCoreService.class);
    
    /**
     * 获取主流程编号(用于一个实体对应单个流程的情况)
     * 
     * @return 流程编号
     */
    public abstract String getProcessId();
    
    /**
     * 短信发送时获取业务数据名称，例如：检修单；缺陷单等
     * 
     * @return 工作流名称
     */
    public abstract String getDataName();
    
    /**
     * 返回流程编号ProcessId
     * 
     * @param workVO 工单对象
     * @return ProcessId
     */
    protected String getProcessId(VO workVO) {
        if (workVO == null || workVO.getProcessId() == null) {
            return this.getProcessId();
        }
        return workVO.getProcessId();
    }
    
    /**
     * 返回流程编号ProcessId
     * 
     * @param param 流程参数
     * @return 流程Id
     */
    protected String getProcessIdFromParam(CapWorkflowParam param) {
        String strProcessId;
        if (param == null || param.getProcessId() == null) {
            strProcessId = this.getProcessId();
        } else {
            strProcessId = param.getProcessId();
        }
        return strProcessId;
    }
    
    /**************************************************** 流程相关查询接口 *********************************/
    
    /**
     * 查询待办的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject待办列表
     */
    public int queryTodoVOCount(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, false));
        return ((Integer) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryTodo", "Count"), workVO))
            .intValue();
    }
    
    /**
     * 查询待办的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject待办列表
     */
    public List<VO> queryTodoVOList(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, false));
        return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(workVO, "queryTodo", "List"), workVO,
            workVO.getPageNo(), workVO.getPageSize());
    }
    
    /**
     * 查询综合信息数量
     * 
     * @param workVO 查询条件
     * @return InfoProject待办列表
     */
    public int queryComprehensiveVOCount(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setRuNodeTrackTableName(WorkflowHelper.readRuNodeOrTransTrackTableName(strProcessId, true));
        workVO.setRuTransTrackTableName(WorkflowHelper.readRuNodeOrTransTrackTableName(strProcessId, false));
        return ((Integer) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryComprehensive", "Count"),
            workVO)).intValue();
    }
    
    /**
     * 查询综合信息列表
     * 
     * @param workVO 查询条件
     * @return InfoProject待办列表
     */
    public List<VO> queryComprehensiveVOList(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setRuNodeTrackTableName(WorkflowHelper.readRuNodeOrTransTrackTableName(strProcessId, true));
        workVO.setRuTransTrackTableName(WorkflowHelper.readRuNodeOrTransTrackTableName(strProcessId, false));
        List<VO> lstVos = capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(workVO, "queryComprehensive", "List"),
            workVO, workVO.getPageNo(), workVO.getPageSize());
        WorkflowHelper.setNodeInfo(lstVos, strProcessId);
        return lstVos;
    }
    
    /**
     * 读取VO列表
     * 
     * @param condition 查询条件
     * @return vo列表
     */
    @Override
    public List<VO> queryVOList(VO condition) {
        List<VO> voList = super.queryVOList(condition);
        for (VO vo : voList) {
            vo.setProcessId(getProcessId());
        }
        return voList;
    }
    
    /**
     * 查询待上报的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject待办列表
     */
    public int queryToEntryVOCount(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, false));
        return ((Integer) capBaseCommonDAO
            .selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryToEntry", "Count"), workVO)).intValue();
    }
    
    /**
     * 查询待上报的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject待办列表
     */
    public List<VO> queryToEntryVOList(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, false));
        return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(workVO, "queryToEntry", "List"), workVO,
            workVO.getPageNo(), workVO.getPageSize());
    }
    
    /**
     * 查询已上报的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject已办列表
     */
    public int queryEntryVOCount(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, true));
        List<String> nodes = WorkflowHelper.queryFirstNodeIds(strProcessId, null);
        workVO.setNodeId(nodes.get(0));
        return ((Integer) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryEntry", "Count"), workVO))
            .intValue();
    }
    
    /**
     * 查询已上报的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject已办列表
     */
    public List<VO> queryEntryVOList(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, true));
        List<String> nodes = WorkflowHelper.queryFirstNodeIds(strProcessId, null);
        workVO.setNodeId(nodes.get(0));
        return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(workVO, "queryEntry", "List"), workVO,
            workVO.getPageNo(), workVO.getPageSize());
    }
    
    /**
     * 查询已办的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject已办列表
     */
    public int queryDoneVOCount(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, true));
        List<String> nodes = WorkflowHelper.queryFirstNodeIds(strProcessId, null);
        workVO.setFirstNodeIds(nodes);
        return ((Integer) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryDone", "Count"), workVO))
            .intValue();
    }
    
    /**
     * 查询已办的InfoProject列表-分页
     * 
     * @param workVO 查询条件
     * @return InfoProject已办列表
     */
    public List<VO> queryDoneVOList(VO workVO) {
        String curUserId = SystemHelper.getUserId();
        workVO.setTransActor(curUserId);
        String strProcessId = getProcessId(workVO);
        workVO.setProcessId(strProcessId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(strProcessId, true));
        List<String> nodes = WorkflowHelper.queryFirstNodeIds(strProcessId, null);
        workVO.setFirstNodeIds(nodes);
        return capBaseCommonDAO.queryList(CapRuntimeUtils.getSqlKey(workVO, "queryDone", "List"), workVO,
            workVO.getPageNo(), workVO.getPageSize());
    }
    
    /**************************************************** 流程相关操作接口 *********************************/
    
    /**
     * 发送
     * 
     * <pre>
     * 其中workId,curUserId 为必输项 
     * 如果有targetNodeInfos,则其中的nodeId,users中的userId为必输项
     * 如果为上报则taskId非必填,如果发送则必填
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void fore(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 流程实例id
        if (StringUtil.isBlank(workVO.getProcessInsId())) {// 上报
            this.doEntry(workVO, workflowParam);
        } else {// 下发
            this.doFore(workVO, workflowParam);
        }
    }
    
    /**
     * 上报
     * 
     * @param workVO 工单对象
     * @param workflowParam 工作流参数
     */
    private void doEntry(VO workVO, CapWorkflowParam workflowParam) {
        // 获取参数对象的集合,用于条件判断
        Map<String, Object> paramMap = workflowParam.getParamMap();// getParamMap(workVO);
        paramMap.putAll(getParamMap(workVO));
        workflowParam.setOperateType(WorkflowConstant.WORK_FLOW_OPERATE_REPORT);
        // 获取流程实例
        ProcessInstanceInfo processInstanceInfo = null;
        NodeInfo[] nodeInfos = workflowParam.getTargetNodeInfos();
        workflowParam.setParam(workVO.getProcessInsId(), this.getProcessId(workVO), nodeInfos);
        // 查询第一个用户节点
        NodeInfo[] firstNodeInfos = this.queryFirstUserNodes(this.getProcessId(workVO), paramMap);
        if (firstNodeInfos == null || firstNodeInfos.length == 0) {
            throw new CapWorkflowException("没有找到起始节点，请检查流程图。");
        }
        workflowParam.setCurrNodeId(firstNodeInfos[0].getNodeId());
        if (nodeInfos == null || nodeInfos.length == 0) {// 如果节点为空，默认上报
            processInstanceInfo = capWorkflowCoreService.defaultEntry(workflowParam, paramMap);
        } else {// 获取到相关的流程实例，指定上报
            processInstanceInfo = capWorkflowCoreService.specialEntry(workflowParam, paramMap);
        }
        
        // 流程是否已完成，流程结束的下发节点始终为null，如果是最后一个节点，且发送成功，则工单改为已处理
        boolean isComplete = WorkflowHelper.isProcessComplete(processInstanceInfo.getProcessId(),
            processInstanceInfo.getMainProcessInsId());
        if (isComplete) {
            workflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_OVER);
        } else {// 设置工作流状态及当前停留环节
            workflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_RUN);
        }
        workflowParam.setProcessInsId(processInstanceInfo.getMainProcessInsId());
        workflowParam.setWaitingNodeInfos(processInstanceInfo.getNodeInfos());
        // 回调更新主表
        try {
            workVO.setProcessInsId(workflowParam.getProcessInsId());
            workVO.setFlowState(workflowParam.getFlowState());
            this.entryCallback(workVO, workflowParam, processInstanceInfo);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办
        updateWorkFlowDoneByTodoTask(workflowParam, workVO, null, WorkbenchConstant.DONE_OPRATE_ENTRY);
    }
    
    /**
     * 上报回调(已默认实现，子类可覆盖实现)
     * 
     * @param vo 业务工单vo
     * @param workflowParam 工作流参数对象
     * @param processInstanceInfo 流程操作返回的流程实例对象
     */
    protected void entryCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        List<VO> workRecords = new ArrayList<VO>();
        workRecords.add(vo);
        List<CapWorkflowParam> workflowParamList = new ArrayList<CapWorkflowParam>();
        workflowParamList.add(workflowParam);
        ProcessInstanceInfo[] arrProIns = new ProcessInstanceInfo[1];
        arrProIns[0] = processInstanceInfo;
        batchEntryCallback(workRecords, workflowParamList, arrProIns, workflowParam.getFlowState().intValue());
    }
    
    /**
     * 批量上报
     * 
     * @param workflowParamList 节点信息
     * @param version 流程版本
     */
    public void batchEntry(List<CapWorkflowParam> workflowParamList, int version) {
        VO workDTO = queryWorkVOByWorkId(workflowParamList.get(0).getWorkId());
        BatchWorkFlowParamVO workFlowParams = WorkflowHelper.compareWorkFlowParams(workflowParamList);
        workFlowParams.setProcessId(this.getProcessId(workDTO));
        // 获取参数对象的集合,用于条件判断
        Map<String, Object> paramMap = workflowParamList.get(0).getParamMap();
        paramMap.putAll(getParamMap(workDTO));
        workFlowParams.setVarMap(paramMap);
        ProcessInstanceInfo[] processInstanceInfos = null;
        NodeInfo[] nodeInfos = workflowParamList.get(0).getTargetNodeInfos();
        if (nodeInfos == null || nodeInfos.length == 0) {// 默认批量上报
            processInstanceInfos = capWorkflowCoreService.batchDefaultEntryByReturn(workFlowParams, version);
        } else {// 指定批量上报 ，需要对节点人员过滤和返回上报的节点信息，所以调用指定发送方法
            processInstanceInfos = capWorkflowCoreService.batchSpecialEntryByReturn(workFlowParams, version);
        }
        Map<String, CapWorkflowParam> tempWorkflowParamYMap = new HashMap<String, CapWorkflowParam>();
        List<CapWorkflowParam> tempWorkflowParamList = new ArrayList<CapWorkflowParam>();
        // 设置回退到第一个节点，即有流程实例的流程信息
        NodeInfo[] tagetNodeInfos = processInstanceInfos[0].getNodeInfos();
        for (int i = 0; i < workflowParamList.size(); i++) {
            CapWorkflowParam workflowParam = workflowParamList.get(i);
            VO processInsDTO = queryWorkVOByWorkId(workflowParamList.get(i).getWorkId());
            if (null != processInsDTO.getProcessInsId()) {
                // 设置流程实例、流程状态、停留节点
                workflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_RUN);
                workflowParam.setWaitingNodeInfos(tagetNodeInfos);
                workflowParam.setProcessInsId(processInsDTO.getProcessInsId());
                tempWorkflowParamYMap.put(processInsDTO.getProcessInsId(), workflowParam);
            } else {
                tempWorkflowParamList.add(workflowParam);
            }
        }
        // 设置新建的单的流程信息
        for (int j = 0; j < tempWorkflowParamList.size(); j++) {
            CapWorkflowParam tempWorkflowParam = tempWorkflowParamList.get(j);
            for (ProcessInstanceInfo tempProcessInstanceInfo : processInstanceInfos) {
                if (!tempWorkflowParamYMap.containsKey(tempProcessInstanceInfo.getMainProcessInsId())) {
                    tempWorkflowParam.setProcessInsId(tempProcessInstanceInfo.getMainProcessInsId());
                    tempWorkflowParam.setTaskId(tempProcessInstanceInfo.getTaskInfo().getDelTodoTaskInfos()[0]
                        .getTodoTaskId());
                    // 设置流程实例、流程状态、停留节点
                    tempWorkflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_RUN);
                    tempWorkflowParam.setWaitingNodeInfos(tagetNodeInfos);
                    tempWorkflowParamYMap.put(tempProcessInstanceInfo.getMainProcessInsId(), tempWorkflowParam);
                    break;
                }
            }
        }
        List<CapWorkflowParam> newWorkflowParamList = new ArrayList<CapWorkflowParam>();
        newWorkflowParamList.addAll(tempWorkflowParamYMap.values());
        try {
            this.batchEntryCallback(getVOList(newWorkflowParamList, WorkflowConstant.WORK_FLOW_STATE_RUN),
                newWorkflowParamList, processInstanceInfos, WorkflowConstant.WORK_FLOW_STATE_RUN);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办
        updateWorkFlowDoneByTodoTask(newWorkflowParamList, processInstanceInfos, WorkbenchConstant.DONE_OPRATE_FORE);
    }
    
    /**
     * 批量上报回调
     * 
     * @param workRecords 业务工单vo集合
     * @param workflowParamList 工单对象集合
     * @param processInstanceInfos 流程信息
     * @param targetFlowState 流程操作结束后的单据状态
     */
    protected void batchEntryCallback(List<VO> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        // 批量更新业务单据
        if (workRecords.size() > 0) {
            batchUpdate(workRecords);
        }
    }
    
    /**
     * 批量上报回调
     * 
     * @param workflowParamList 工单流程参数对象集合
     * @param targetFlowState 流程操作后工单状态
     * @return 业务工单对象集合
     */
    private List<VO> getVOList(List<CapWorkflowParam> workflowParamList, int targetFlowState) {
        List<VO> workRecords = new ArrayList<VO>();
        VO workDTO = null;
        for (CapWorkflowParam workflowParam : workflowParamList) {
            workDTO = queryWorkVOByWorkId(workflowParam.getWorkId());
            workDTO.setProcessInsId(workflowParam.getProcessInsId());
            workDTO.setFlowState(targetFlowState);
            workRecords.add(workDTO);
        }
        return workRecords;
    }
    
    /**
     * 下发
     * 
     * @param workVO 工单对象
     * @param workflowParam 工作流参数
     */
    private void doFore(VO workVO, CapWorkflowParam workflowParam) {
        // 流程实例id、工作流编码
        String processInsId = workVO.getProcessInsId();
        String processId = this.getProcessId(workVO);
        workflowParam.setOperateType(WorkflowConstant.WORK_FLOW_OPERATE_SEND);
        // 获取下发前的待办信息,用于回调
        TodoTaskInfo todoTaskInfo = WorkflowHelper.queryBeforeForeTodoTaskInfo(workflowParam, processInsId, processId);
        if (todoTaskInfo == null) {
            throw new CapWorkflowException("您的待办任务已处理，已无待处理的待办任务。");
        }
        workflowParam.setCurrNodeId(todoTaskInfo.getCurNodeId());
        // 设置流程参数
        ProcessInstanceInfo processInstanceInfo = null;
        NodeInfo[] nodeInfos = workflowParam.getTargetNodeInfos();
        workflowParam.setParam(processInsId, processId, nodeInfos);
        Map<String, Object> objMap = workflowParam.getParamMap();// this.getParamMap(workVO);
        objMap.putAll(this.getParamMap(workVO));
        if (nodeInfos == null || nodeInfos.length == 0) {// 默认发送
            processInstanceInfo = capWorkflowCoreService.defaultFore(workflowParam, objMap);
        } else {// 指定发送 ，需要对节点人员过滤和返回上报的节点信息，所以调用指定发送方法
            processInstanceInfo = capWorkflowCoreService.specialFore(workflowParam, objMap);
        }
        // 流程是否已完成，流程结束的下发节点始终为null，如果是最后一个节点，且发送成功，则工单改为已处理
        boolean isComplete = WorkflowHelper.isProcessComplete(processId, processInsId);
        if (isComplete) {
            workflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_OVER);
        } else {// 设置工作流状态及当前停留环节
            workflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_RUN);
        }
        workflowParam.setProcessInsId(processInstanceInfo.getMainProcessInsId());
        workflowParam.setWaitingNodeInfos(processInstanceInfo.getNodeInfos());
        
        Integer flowState = workVO.getFlowState();
        try {
            workVO.setProcessInsId(workflowParam.getProcessInsId());
            workVO.setFlowState(workflowParam.getFlowState());
            if (flowState == null || flowState == WorkflowConstant.WORK_FLOW_STATE_UNREPORT) {// 上报回调
                // 更新已办
                updateWorkFlowDoneByTodoTask(workflowParam, workVO, todoTaskInfo, WorkbenchConstant.DONE_OPRATE_ENTRY);
                this.entryCallback(workVO, workflowParam, processInstanceInfo);
            } else {// 发送回调
                    // 更新已办
                updateWorkFlowDoneByTodoTask(workflowParam, workVO, todoTaskInfo, WorkbenchConstant.DONE_OPRATE_FORE);
                this.foreCallback(workVO, workflowParam, processInstanceInfo);
            }
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
    }
    
    /**
     * 发送回调
     * 
     * @param vo 工单对象
     * @param workflowParam 流程参数信息
     * @param processInstanceInfo 流程操作返回的流程实例对象
     */
    protected void foreCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        List<VO> workRecords = new ArrayList<VO>(1);
        workRecords.add(vo);
        List<CapWorkflowParam> workflowParamList = new ArrayList<CapWorkflowParam>(1);
        workflowParamList.add(workflowParam);
        ProcessInstanceInfo[] arrProIns = new ProcessInstanceInfo[1];
        arrProIns[0] = processInstanceInfo;
        batchForeCallback(workRecords, workflowParamList, arrProIns, workflowParam.getFlowState().intValue());
    }
    
    /**
     * 批量发送
     * 
     * @param workflowParamList 节点信息
     */
    public void batchFore(List<CapWorkflowParam> workflowParamList) {
        String workId = workflowParamList.get(0).getWorkId();
        VO workDTO = this.queryWorkVOByWorkId(workId);
        BatchWorkFlowParamVO workFlowParams = WorkflowHelper.compareWorkFlowParams(workflowParamList);
        workFlowParams.setProcessId(this.getProcessId(workDTO));
        // 获取参数对象的集合,用于条件判断
        Map<String, Object> paramMap = workflowParamList.get(0).getParamMap();
        paramMap.putAll(getParamMap(workDTO));
        workFlowParams.setVarMap(paramMap);
        ProcessInstanceInfo[] processInstanceInfos = null;
        NodeInfo[] nodeInfos = workflowParamList.get(0).getTargetNodeInfos();
        if (nodeInfos == null || nodeInfos.length == 0) {// 默认发送
            processInstanceInfos = capWorkflowCoreService.batchDefaultForeByReturn(workFlowParams);
        } else {// 指定发送 ，需要对节点人员过滤和返回上报的节点信息，所以调用指定发送方法
            processInstanceInfos = capWorkflowCoreService.batchSpecialForeByReturn(workFlowParams);
        }
        // 获取工作流目标节点的状态
        int targetFlowState = 0;
        if (processInstanceInfos[0].getState() == WorkflowConstant.WORK_FLOW_STATE_OVER) {
            targetFlowState = WorkflowConstant.WORK_FLOW_STATE_OVER;
        } else {
            targetFlowState = WorkflowConstant.WORK_FLOW_STATE_RUN;
        }
        for (CapWorkflowParam workflowParam : workflowParamList) {
            workflowParam.setFlowState(targetFlowState);
            workflowParam.setWaitingNodeInfos(processInstanceInfos[0].getNodeInfos());
        }
        try {
            this.batchForeCallback(getVOList(workflowParamList, targetFlowState), workflowParamList,
                processInstanceInfos, targetFlowState);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办
        updateWorkFlowDoneByTodoTask(workflowParamList, processInstanceInfos, WorkbenchConstant.DONE_OPRATE_FORE);
    }
    
    /**
     * 批量发送回调
     * 
     * @param workRecords 业务工单集合
     * @param workflowParamList 流程参数集合
     * @param processInstanceInfos 流程信息
     * @param targetFlowState 工作流目标状态
     */
    protected void batchForeCallback(List<VO> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        // 批量更新业务单据
        if (workRecords.size() > 0) {
            batchUpdate(workRecords);
        }
    }
    
    /**
     * 回退
     * 
     * <pre>
     * 其中属性workId,taskId,currentUserId,如果有targetNodeInfos,则其中的nodeId,users中的userId为必输项
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void back(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象、工作流编码
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        String processId = this.getProcessId(workVO);
        workflowParam.setOperateType(WorkflowConstant.WORK_FLOW_OPERATE_BACK);
        TodoTaskInfo todoTaskInfo = this.queryTodoTask(processId, workflowParam.getTaskId());
        if (todoTaskInfo == null) {
            throw new CapWorkflowException("您的待办任务已处理，已无待处理的待办任务。");
        }
        workflowParam.setCurrNodeId(todoTaskInfo.getCurNodeId());
        ProcessInstanceInfo processInstanceInfo = null;
        NodeInfo[] nodeInfos = workflowParam.getTargetNodeInfos();
        Map<String, Object> objMap = workflowParam.getParamMap();// this.getParamMap(workVO);
        objMap.putAll(this.getParamMap(workVO));
        workflowParam.setParam(workVO.getProcessInsId(), processId, nodeInfos);
        if (nodeInfos == null || nodeInfos.length == 0) {// 默认回退
            processInstanceInfo = capWorkflowCoreService.defaultBack(workflowParam, objMap);
        } else {// 指定回退
            processInstanceInfo = capWorkflowCoreService.specialBack(workflowParam, objMap);
        }
        // 获取当前停留环节
        workflowParam.setWaitingNodeInfos(processInstanceInfo.getNodeInfos());
        // 是否回退到起始节点、设置流程状态
        boolean isInFirstNode = WorkflowHelper.isWaitFirstNode(processInstanceInfo.getNodeInfos(), processId, objMap);
        workflowParam.setFlowState(isInFirstNode ? WorkflowConstant.WORK_FLOW_STATE_UNREPORT
            : WorkflowConstant.WORK_FLOW_STATE_RUN);
        // 更新已办数据
        updateWorkFlowDoneByTodoTask(workflowParam, workVO, todoTaskInfo, WorkbenchConstant.DONE_OPRATE_BACK);
        // 回退回调
        try {
            workVO.setProcessInsId(workflowParam.getProcessInsId());
            workVO.setFlowState(workflowParam.getFlowState());
            backCallback(workVO, workflowParam, processInstanceInfo);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
    }
    
    /**
     * 回退回调
     * 
     * @param vo 工单对象
     * @param workflowParam 流程参数信息
     * @param processInstanceInfo 流程操作返回的流程实例对象
     */
    @SuppressWarnings("unchecked")
    protected void backCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        List<VO> workRecords = new ArrayList<VO>(1);
        workRecords.add(vo);
        List<CapWorkflowParam> workflowParamList = new ArrayList<CapWorkflowParam>(1);
        workflowParamList.add(workflowParam);
        ProcessInstanceInfo[] arrProIns = new ProcessInstanceInfo[1];
        arrProIns[0] = processInstanceInfo;
        batchBackCallback(workRecords, workflowParamList, arrProIns, workflowParam.getFlowState().intValue());
    }
    
    /**
     * 批量回退
     * 
     * @param workflowParamList 节点信息
     */
    public void batchBack(List<CapWorkflowParam> workflowParamList) {
        BatchWorkFlowParamVO workFlowParams = WorkflowHelper.compareWorkFlowParams(workflowParamList);
        VO workDTO = queryWorkVOByWorkId(workflowParamList.get(0).getWorkId());
        workFlowParams.setProcessId(this.getProcessId(workDTO));
        workFlowParams.setVarMap(this.getParamMap(workDTO));
        ProcessInstanceInfo[] processInstanceInfos = null;
        NodeInfo[] nodeInfos = workflowParamList.get(0).getTargetNodeInfos();
        if (nodeInfos == null || nodeInfos.length == 0) {// 默认回退
            processInstanceInfos = capWorkflowCoreService.batchDefaultBackByReturn(workFlowParams);
        } else {// 指定回退
            processInstanceInfos = capWorkflowCoreService.batchSpecialBackByReturn(workFlowParams);
        }
        // 是否回退到起始节点、设置流程状态
        boolean isInFirstNode = WorkflowHelper.isWaitFirstNode(processInstanceInfos[0].getNodeInfos(),
            this.getProcessId(workDTO), this.getParamMap(workDTO));
        int targetFlowState = isInFirstNode ? WorkflowConstant.WORK_FLOW_STATE_UNREPORT
            : WorkflowConstant.WORK_FLOW_STATE_RUN;
        for (CapWorkflowParam workflowParam : workflowParamList) {
            workflowParam.setFlowState(targetFlowState);
            workflowParam.setWaitingNodeInfos(processInstanceInfos[0].getNodeInfos());
        }
        try {
            batchBackCallback(getVOList(workflowParamList, targetFlowState), workflowParamList, processInstanceInfos,
                targetFlowState);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办数据
        updateWorkFlowDoneByTodoTask(workflowParamList, processInstanceInfos, WorkbenchConstant.DONE_OPRATE_BACK);
    }
    
    /**
     * 批量回退回调
     * 
     * @param workRecords 业务工单集合
     * @param workflowParamList 工单对象集合
     * @param processInstanceInfos 流程信息
     * @param targetFlowState 流程回退目标节点状态
     */
    protected void batchBackCallback(List<VO> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        if (workRecords.size() > 0) {
            batchUpdate(workRecords);
        }
    }
    
    /**
     * 回退到申请人，
     * 
     * <pre>
     * 返回值为主流程实例对象 ProcessInstanceInfo
     * 其中属性NodeInfo指本次操作后产生的待办停留的用户节点
     * NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * 回退到第一个人工任务，如果没有人工参与过，则抛异常
     * 
     * 其中属性taskId,currentUserId,processId为必输项,建议输入currentUserName以提高性能。
     * </pre>
     * 
     * @param workflowParam - 工作流参数对象
     */
    public void backEntry(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 设置流程参数
        workflowParam.setParam(workVO.getProcessInsId(), this.getProcessId(workVO), workflowParam.getTargetNodeInfos());
        Map<String, Object> objMap = workflowParam.getParamMap();
        objMap.putAll(this.getParamMap(workVO));
        ProcessInstanceInfo processInstanceInfo = capWorkflowCoreService.backwardToFirstUserTaskByReturn(workflowParam,
            objMap);
        // 获取当前停留环节
        workflowParam.setWaitingNodeInfos(processInstanceInfo.getNodeInfos());
        // 回退到上报节点回调
        try {
            backEntryCallback(workVO, workflowParam, processInstanceInfo);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
    }
    
    /**
     * 回退到上报节点回调(如果需要保留原有流程跟踪，此处不应该退出流程，将流程状态保持为1，如果修改为0则以前的流程跟踪将没有)
     * 
     * @param vo 工单对象
     * @param workflowParam 流程参数信息
     * @param processInstanceInfo 流程操作返回的流程实例对象
     */
    @SuppressWarnings("unchecked")
    protected void backEntryCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        vo.setFlowState(WorkflowConstant.WORK_FLOW_STATE_UNREPORT);
        capBaseCommonDAO.update(vo);
    }
    
    /**
     * 流程结束，回退到申请人
     * 
     * <pre>
     * 返回值为主流程实例对象 ProcessInstanceInfo
     * 其中属性NodeInfo指本次操作后产生的待办停留的用户节点
     * NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * 回退到第一个人工任务，如果没有人工参与过，则抛异常
     * 
     * 其中属性taskId,currentUserId,processId为必输项,建议输入currentUserName以提高性能。
     * </pre>
     * 
     * @param workflowParam - 工作流参数对象
     */
    public void backEntry4FlowOver(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 设置流程参数
        workflowParam.setParam(workVO.getProcessInsId(), this.getProcessId(workVO), workflowParam.getTargetNodeInfos());
        Map<String, Object> objMap = workflowParam.getParamMap();
        objMap.putAll(this.getParamMap(workVO));
        ProcessInstanceInfo processInstanceInfo = capWorkflowCoreService.backOverToFirstUserTask(workflowParam, objMap);
        // 获取当前停留环节
        workflowParam.setWaitingNodeInfos(processInstanceInfo.getNodeInfos());
        // 回退到上报节点回调
        try {
            backEntry4FlowOverCallback(workVO, workflowParam, processInstanceInfo);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
    }
    
    /**
     * 流程结束，回退到上报节点回调(流程状态更改为1)
     * 
     * @param vo 工单对象
     * @param workflowParam 流程参数信息
     * @param processInstanceInfo 流程操作返回的流程实例对象
     */
    @SuppressWarnings("unchecked")
    protected void backEntry4FlowOverCallback(VO vo, CapWorkflowParam workflowParam,
        ProcessInstanceInfo processInstanceInfo) {
        vo.setFlowState(WorkflowConstant.WORK_FLOW_STATE_RUN);
        capBaseCommonDAO.update(vo);
    }
    
    /**
     * 撤回
     * 
     * <pre>
     * 其中属性workId,taskId,currentUserId为必输项
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void undo(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象、工作流编码
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        workflowParam.setOperateType(WorkflowConstant.WORK_FLOW_OPERATE_UNDO);
        // 获取下发前的待办信息,用于回调
        DoneTaskInfo doneTaskInfo = this.queryDoneTask(this.getProcessId(workVO), workflowParam.getTaskId());
        if (doneTaskInfo == null) {
            throw new CapWorkflowException("您的已办已被撤回。");
        }
        // 设置流程参数
        workflowParam.setParam(workVO.getProcessInsId(), this.getProcessId(workVO), null);
        Map<String, Object> objMap = workflowParam.getParamMap();
        objMap.putAll(getParamMap(workVO));
        // 执行撤回
        ProcessInstanceInfo processInstanceInfo = capWorkflowCoreService.undoWorkFlow(workflowParam, objMap);
        // 设置当前停留环节
        workflowParam.setWaitingNodeInfos(processInstanceInfo.getNodeInfos());
        // 撤回后如果是开始节点,改变流程状态
        boolean isInFirstNode = WorkflowHelper.isWaitFirstNode(processInstanceInfo.getNodeInfos(),
            this.getProcessId(workVO), objMap);
        workflowParam.setFlowState(isInFirstNode ? WorkflowConstant.WORK_FLOW_STATE_UNREPORT
            : WorkflowConstant.WORK_FLOW_STATE_RUN);
        // 回调更新主表
        try {
            undoCallback(workVO, workflowParam, processInstanceInfo);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        updateWorkFlowDoneByTodoTask(workflowParam, workVO, null, WorkbenchConstant.DONE_OPRATE_UNDO);
    }
    
    /**
     * 撤回回调
     * 
     * @param vo 工单对象
     * @param workflowParam 流程参数信息
     * @param processInstanceInfo 流程实例
     */
    protected void undoCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        undoCallback(vo, workflowParam);
    }
    
    /**
     * 撤回回调，方法被三个参数的方法undoCallback(vo,workflowParam,processInstanceInfo)替代
     * 
     * @param vo 工单对象
     * @param workflowParam 流程参数信息
     */
    @Deprecated
    protected void undoCallback(VO vo, CapWorkflowParam workflowParam) {
        Integer flowState = vo.getFlowState();
        if (flowState == null || !workflowParam.getFlowState().equals(flowState)) {
            vo.setFlowState(workflowParam.getFlowState());
            capBaseCommonDAO.update(vo);
        }
    }
    
    /**
     * 转发
     * 
     * <pre>
     * 其中属性workId,taskId,currentUserId,targetNodeInfos中的users中的userId为必输项
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void reassign(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        workflowParam.setProcessId(this.getProcessId(workVO));
        workflowParam.setOperateType(WorkflowConstant.WORK_FLOW_OPERATE_REASSIGN);
        // 获取下发前的待办信息,用于回调
        TodoTaskInfo todoTaskInfo = this.queryTodoTask(this.getProcessId(workVO), workflowParam.getTaskId());
        NodeInfo[] targetNodeInfos = workflowParam.getTargetNodeInfos();
        if (targetNodeInfos == null || targetNodeInfos.length == 0) {
            throw new CapWorkflowException("转发出错，未找到当前停留节点。");
        }
        NodeInfo nodeInfo = workflowParam.getTargetNodeInfos()[0];
        // 执行转发
        ReassignResultInfo objReassignResultInfo = capWorkflowCoreService.reassignTask(workflowParam,
            nodeInfo.getUsers());
        // 设置流程实例id、待办id、当前停留环节
        workflowParam.setProcessInsId(workVO.getProcessInsId());
        workflowParam.setTaskId(workflowParam.getTaskId());
        workflowParam.setWaitingNodeInfos(workflowParam.getTargetNodeInfos());
        // 转发回调
        try {
            reassignCallback(workVO, workflowParam, objReassignResultInfo.getProcessInstanceInfo());
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办
        updateWorkFlowDoneByTodoTask(workflowParam, workVO, todoTaskInfo, WorkbenchConstant.DONE_OPRATE_REASSIGN);
    }
    
    /**
     * 转发回调
     * 
     * @param vo 工单对象
     * @param workflowParam 流程信息
     * @param processInstanceInfo 流程实例信息
     */
    protected void reassignCallback(VO vo, CapWorkflowParam workflowParam, ProcessInstanceInfo processInstanceInfo) {
        List<VO> workRecords = new ArrayList<VO>(1);
        workRecords.add(vo);
        List<CapWorkflowParam> workflowParamList = new ArrayList<CapWorkflowParam>(1);
        workflowParamList.add(workflowParam);
        ProcessInstanceInfo[] arrProIns = new ProcessInstanceInfo[1];
        arrProIns[0] = processInstanceInfo;
        batchReassignCallback(workRecords, workflowParamList, arrProIns, workflowParam.getFlowState().intValue());
    }
    
    /**
     * 批量转发
     * 
     * @param workflowParamList 节点信息
     */
    public void batchReassign(List<CapWorkflowParam> workflowParamList) {
        String workId = workflowParamList.get(0).getWorkId();
        // 获取工单对象
        VO workDTO = this.queryWorkVOByWorkId(workId);
        NodeInfo[] targetNodeInfos = workflowParamList.get(0).getTargetNodeInfos();
        ProcessInstanceInfo[] processInstanceInfos = null;
        if (targetNodeInfos == null || targetNodeInfos.length == 0) {
            throw new CapWorkflowException("转发出错，未找到当前停留节点。");
        }
        BatchWorkFlowParamVO workFlowParams = WorkflowHelper.compareWorkFlowParams(workflowParamList);
        workFlowParams.setProcessId(this.getProcessId(workDTO));
        workFlowParams.setVarMap(this.getParamMap(workDTO));
        // 执行转发
        processInstanceInfos = capWorkflowCoreService.batchReassign(workFlowParams);
        // 转发回调
        try {
            batchReassignCallback(getVOList(workflowParamList, workDTO.getFlowState().intValue()), workflowParamList,
                processInstanceInfos, workDTO.getFlowState().intValue());
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办
        updateWorkFlowDoneByTodoTask(workflowParamList, processInstanceInfos, WorkbenchConstant.DONE_OPRATE_REASSIGN);
    }
    
    /**
     * 批量转发回调
     * 
     * @param workRecords 工单对象集合
     * @param workflowParamList 流程参数信息
     * @param processInstanceInfos 流程信息
     * @param targetFlowState 工作流目标状态
     */
    protected void batchReassignCallback(List<VO> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        // do something...
    }
    
    /**
     * 终结
     * 
     * <pre>
     * 其中属性workId,taskId,currentUserId,targetNodeInfos中的users中的userId为必输项。为必输项
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void abort(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        workflowParam.setOperateType(WorkflowConstant.WORK_FLOW_OPERATE_ABORT);
        // 获取下发前的待办信息,用于回调
        TodoTaskInfo todoTaskInfo = this.queryTodoTask(this.getProcessId(workVO), workflowParam.getTaskId());
        // 执行终结
        capWorkflowCoreService.abortFlowInstance(this.getProcessId(workVO), workVO.getProcessInsId(),
            workflowParam.getCurrentUserId(), workflowParam.getCurrentUserName());
        // 设置流程实例id、待办id、当前停留环节
        workflowParam.setProcessInsId(workVO.getProcessInsId());
        workflowParam.setTaskId(workflowParam.getTaskId());
        workflowParam.setWaitingNodeInfos(workflowParam.getTargetNodeInfos());
        // 终结回调
        workflowParam.setFlowState(WorkflowConstant.WORK_FLOW_STATE_ABORT);
        try {
            abortCallback(workVO, workflowParam);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
        // 更新已办
        updateWorkFlowDoneByTodoTask(workflowParam, workVO, todoTaskInfo, WorkbenchConstant.DONE_OPRATE_ABORT);
    }
    
    /**
     * 终结回调
     * 
     * @param workflowVO 工单对象
     * @param workflowParam 流程信息
     */
    protected void abortCallback(VO workflowVO, CapWorkflowParam workflowParam) {
        workflowVO.setFlowState(workflowParam.getFlowState());
        capBaseCommonDAO.update(workflowVO);
    }
    
    /**
     * 挂起
     * 
     * <pre>
     * 其中属性processId,processInsId,userId为必输项。输入userName可以提高性能
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void hungup(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 执行挂起
        capWorkflowCoreService.hungUpProcessInstance(this.getProcessId(workVO), workVO.getProcessInsId(),
            workflowParam.getCurrentUserId(), workflowParam.getCurrentUserName());
        try {
            hungupCallback(workVO, workflowParam);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
    }
    
    /**
     * 挂起
     * 
     * <pre>
     * 其中属性processId,processInsId,userId为必输项。输入userName可以提高性能
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     * @return boolean
     */
    public boolean saveNote(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 执行保存意见
        return capWorkflowCoreService.saveNote(workflowParam, workflowParam.getParamMap());
    }
    
    /**
     * 挂起回调
     * 
     * @param workflowVO 工单对象
     * @param workflowParam 流程信息
     */
    protected void hungupCallback(VO workflowVO, CapWorkflowParam workflowParam) {
        workflowVO.setFlowState(WorkflowConstant.WORK_FLOW_STATE_HUNGUP);
        capBaseCommonDAO.update(workflowVO);
    }
    
    /**
     * 恢复
     * 
     * <pre>
     * 其中属性processId,processInsId,userId为必输项。输入userName可以提高性能
     * </pre>
     * 
     * @param workflowParam 工作流参数对象
     */
    public void recover(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 恢复
        capWorkflowCoreService.recoverProcessInstance(this.getProcessId(workVO), workVO.getProcessInsId(),
            workflowParam.getCurrentUserId(), workflowParam.getCurrentUserName());
        // 终结回调
        try {
            recoverCallback(workVO, workflowParam);
        } catch (Exception ex) {
            throw new CapWorkflowException(ex.getMessage(), ex);
        }
    }
    
    /**
     * 正常流程结束
     * 
     * @param workflowParam
     *            - 工作流参数对象其中属性taskId, processId,
     *            currentUserId为必输项,建议输入currentUserName 其中属性
     * @return ProcessInstanceInfo
     */
    public ProcessInstanceInfo overFlow(CapWorkflowParam workflowParam) {
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        // 获取工单对象
        VO workflowVO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 流程正常结束
        ProcessInstanceInfo processInstanceInfo = capWorkflowCoreService.overFlow(workflowParam);
        // 业务信息回调
        this.overFlowCallBack(workflowVO, workflowParam);
        
        return processInstanceInfo;
    }
    
    /**
     * 流程跳转-跨节点跳转,支持同一流程实例上的节点跳转
     * 返回值为主流程实例对象ProcessInstanceInfo,其中属性NodeInfo指本次操作后产生的待办停留的用户节点
     * NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * 
     * @param workflowParam
     *            流程信息 workFlowParam -
     *            工作流参数对象,其中属性taskId,currentUserId,processId为必输项
     *            ,建议输入currentUserName以提高性能。
     *            authorizerId,authorizerName委托人编号和名称为可选项，当输入时委托人编号和名称需同时输入
     *            targetNodeInfos
     *            :跳转的目标节点其中nodeId,users中的userId为必输项,建议输入userName以提高性能
     *            targetNodeInfos中的nodeInstanceId为可选项当输入时则用输入值处理
     *            ，没有输入则系统自动查询对应的nodeInstanceId
     *
     *            参数 paramMap
     *            流程变量，可选输入
     * @return 主流程实例对象
     */
    public ProcessInstanceInfo jump(CapWorkflowParam workflowParam) {
        // 给工作流参数类设置当前用户相关信息
        WorkflowHelper.setWorkflowParam(workflowParam, this.getDataName());
        
        return capWorkflowCoreService.jump(workflowParam, workflowParam.getParamMap());
    }
    
    /**
     * 流程结束回调
     * 
     * @param workflowVO
     *            工单信息
     * @param workflowParam
     *            流程信息
     */
    protected void overFlowCallBack(VO workflowVO, CapWorkflowParam workflowParam) {
        workflowVO.setFlowState(WorkflowConstant.WORK_FLOW_STATE_OVER);
        capBaseCommonDAO.update(workflowVO);
    }
    
    /**
     * 恢复回调
     * 
     * @param workflowVO 工单对象
     * @param workflowParam 流程信息
     */
    protected void recoverCallback(VO workflowVO, CapWorkflowParam workflowParam) {
        workflowVO.setFlowState(WorkflowConstant.WORK_FLOW_STATE_RUN);
        capBaseCommonDAO.update(workflowVO);
    }
    
    /**
     * 根据工单ID获取工单对象
     * 
     * @param workId 工单ID
     * @return 工单对象
     */
    protected VO queryWorkVOByWorkId(String workId) {
        VO workVO = null;
        try {
            workVO = loadVOById(workId);
            if (workVO == null) {
                throw new CapWorkflowException("没有找到待处理的工单，该工单可能已被处理或删除。");
            }
        } catch (Exception ex) {
            throw new CapWorkflowException("根据工单ID查询工单失败。", ex);
        }
        return workVO;
    }
    
    /**
     * 获取工作流所需的参数集合,用于条件判断(业务可重写该对象put Map中值)
     * 
     * @param workflowVO 工单对象
     * @return 工作流所需的参数集合
     */
    protected Map<String, Object> getParamMap(VO workflowVO) {
        return new HashMap<String, Object>();
    }
    
    /**
     * 获取流程第一个节点人员信息
     * 
     * @param processId 流程编码
     * @param paramMap 扩展函数参数列表
     * @return 节点
     */
    public NodeInfo[] queryFirstUserNodes(String processId, Map<String, Object> paramMap) {
        return capWorkflowCoreService.queryFirstUserNodes(processId, paramMap);
    }
    
    /**
     * 根据待办节点信息更新待办
     * 
     * @param workVO 工单对象
     * @param workflowParam 流程信息
     * @param todoTaskInfo 待办信息
     * @param operateType 操作类型
     */
    protected void updateWorkFlowDoneByTodoTask(CapWorkflowParam workflowParam, VO workVO, TodoTaskInfo todoTaskInfo,
        String operateType) {
        try {
            if (todoTaskInfo == null) {// 待办节点为空，则将当前节点ID及节点名称置空
                updateWorkfowDone(workVO, workflowParam, null, null, null, operateType);
            } else {
                updateWorkfowDone(workVO, workflowParam, todoTaskInfo.getCurNodeId(), todoTaskInfo.getCurNodeName(),
                    todoTaskInfo.getActivityInsId(), operateType);
            }
        } catch (Exception ex) {
            throw new CapWorkflowException("更新已办信息出错。", ex);
        }
    }
    
    /**
     * 更新已办
     * 
     * @param workVO 业务dto
     * @param workflowParam 工作流发送信息
     * @param currNodeId 节点id
     * @param currNodeName 节点名称
     * @param activityInsId 活动实例ID
     * @param oprate 操作类型
     */
    public void updateWorkfowDone(VO workVO, CapWorkflowParam workflowParam, String currNodeId, String currNodeName,
        String activityInsId, String oprate) {
        // 获取已办信息ID
        String taskId = WorkflowWorkbenchHelper.getDoneTaskId(workflowParam, currNodeId, activityInsId);
        workVO.setTaskId(taskId);
        
        DoneDTO doneDTO = this.createDoneInfo(workVO);
        if (doneDTO == null) {
            throw new CapWorkflowException("更新已办信息错误，业务模块未正确的实现【createDoneInfo】方法。");
        }
        doneDTO
            .setWorkId(StringUtil.isBlank(doneDTO.getWorkId()) ? CapRuntimeUtils.getId(workVO) : doneDTO.getWorkId());
        // 流程编号
        doneDTO.setProcessId(this.getProcessId(workVO));
        // 操作类型
        doneDTO.setWorkOperate(oprate);
        // 当前节点ID
        doneDTO.setCurNodeId(currNodeId);
        // 当前节点名称
        doneDTO.setCurNodeName(currNodeName);
        // 处理人及停留节点信息
        WorkflowWorkbenchHelper.compareTransactorAndNodeInfo(workflowParam, doneDTO);
        // 更新已办数据
        WorkbenchHelper.createDoneInfo(doneDTO);
    }
    
    /**
     * 获取待办记录
     * 
     * @param processId 流程编码
     * @param todoTaskId 待办id
     * @return 待办信息
     */
    public TodoTaskInfo queryTodoTask(String processId, String todoTaskId) {
        return capWorkflowCoreService.queryTodoTask(processId, todoTaskId);
    }
    
    /**
     * 获取已办记录
     * 
     * @param processId 流程编码
     * @param todoTaskId 已办id
     * @return 已办信息
     */
    public DoneTaskInfo queryDoneTask(String processId, String todoTaskId) {
        return capWorkflowCoreService.queryDoneTask(processId, todoTaskId);
    }
    
    /**
     * 查询用户所参与的待办节点信息,过滤上报节点
     * 
     * @param processId 流程编码
     * @param userId 用户id
     * @return 待办节点和代办总数
     */
    public List<NodeTodoTaskCntInfo> queryTodoTaskCount(String processId, String userId) {
        return capWorkflowCoreService.queryTodoTaskCount(processId, userId);
    }
    
    /**
     * 获取已办信息
     * 
     * @param vo 业务单vo对象
     * @return 已办信息对象
     */
    public DoneDTO createDoneInfo(VO vo) {
        DoneDTO doneDTO = new DoneDTO();
        doneDTO.setWorkId(CapRuntimeUtils.getId(vo));// 此处用于配置已办的链接参数
        doneDTO.setWorkTitle(this.getDataName());
        doneDTO.setTransdate(new Date());
        return doneDTO;
    }
    
    /**
     * 查找指定发送节点和人员
     * 
     * @param workId 工单id
     * @param todoTaskId 待办任务ID,必填项
     * @param userId 用户ID 名称
     * @param param 网关参数
     * @return 节点和人员信息
     */
    public NodeInfo[] queryForeTransNodes(String workId, String todoTaskId, String userId, Map<String, Object> param) {
        return queryForeTransNodesByDeptId(workId, todoTaskId, SystemHelper.getCurUserInfo().getOrgId(), userId, param);
    }
    
    /**
     * 查找指定发送节点和人员
     * 
     * @param workId 工单id
     * @param todoTaskId 待办任务ID,必填项
     * @param deptId 部门ID
     * @param userId 用户ID 名称
     * @return 节点和人员信息
     */
    public NodeInfo[] queryForeTransNodes(String workId, String todoTaskId, String deptId, String userId) {
        return queryForeTransNodesByDeptId(workId, todoTaskId, deptId, userId, null);
    }
    
    /**
     * 查找指定发送节点和人员
     * 
     * @param workId 工单id
     * @param todoTaskId 待办任务ID,必填项
     * @param deptId 部门ID
     * @param userId 用户ID 名称
     * @param param 网关参数
     * @return 节点和人员信息
     */
    private NodeInfo[] queryForeTransNodesByDeptId(String workId, String todoTaskId, String deptId, String userId,
        Map<String, Object> param) {
        // 获取工单对象
        VO workVO = queryWorkVOByWorkId(workId);
        // 获取扩展属性对象,用于条件判断
        Map<String, Object> objMap = param;//
        if (objMap == null) {
            objMap = new HashMap<String, Object>();
        }
        objMap.putAll(getParamMap(workVO));
        if (!objMap.containsKey("comtop_bpms_current_orgId")) {
            objMap.put("comtop_bpms_current_orgId", deptId);
        }
        
        Integer flowState = workVO.getFlowState();
        if (flowState == null || flowState == WorkflowConstant.WORK_FLOW_STATE_UNREPORT) {
            return this.queryEntryTransNodes(this.getProcessId(workVO), objMap);
        }
        NodeInfo nextNodeInfos[] = capWorkflowCoreService.queryForeTransNodes(this.getProcessId(workVO), todoTaskId,
            userId, objMap);
        return nextNodeInfos;
    }
    
    /**
     * 获取上报节点和人员信息(facade内部调用),防止重复查询工单对象
     * 
     * @param processId 流程编码
     * @param objMap 参数对象
     * @return 上报目标节点信息
     */
    public NodeInfo[] queryEntryTransNodes(String processId, Map<String, Object> objMap) {
        // 获取扩展属性对象,用于条件判断
        NodeInfo[] nodeInfos = capWorkflowCoreService.queryFirstUserNodes(processId, objMap);
        List<NodeInfo> newNodeInfos = new ArrayList<NodeInfo>(10);
        if (nodeInfos != null && nodeInfos.length > 0) {
            for (NodeInfo nodeInfo : nodeInfos) {
                NodeInfo[] objReportNode = capWorkflowCoreService.queryNextUserNodes(processId, nodeInfo.getNodeId(),
                    objMap);
                if (objReportNode != null && objReportNode.length > 0) {
                    Collections.addAll(newNodeInfos, objReportNode);
                }
            }
        }
        nodeInfos = newNodeInfos.toArray(new NodeInfo[] {});
        // 过滤节点人员信息
        return nodeInfos;
    }
    
    /**
     * 查找转发节点和人员
     * 
     * @param workId 工单id
     * @param todoTaskId 待办任务ID,必填项
     * @param userId 用户ID 名称
     * @param param 网关参数
     * @return 节点和人员信息
     */
    public NodeInfo[] queryReassignTransNodes(String workId, String todoTaskId, String userId, Map<String, Object> param) {
        // 获取工单对象
        VO workDTO = queryWorkVOByWorkId(workId);
        String processId = this.getProcessId(workDTO);
        
        // 获取下发前的待办信息,用于回调
        TodoTaskInfo todoTaskInfo = this.queryTodoTask(processId, todoTaskId);
        if (todoTaskInfo == null) {
            throw new CapWorkflowException("您的待办任务已处理，已无待处理的待办任务。");
        }
        NodeInfo nodeInfo = WorkflowHelper.taskInfoToNodeInfo(todoTaskInfo);
        // 获取扩展属性对象,用于条件判断
        Map<String, Object> paramMap = param;// getParamMap(workDTO);
        paramMap.putAll(getParamMap(workDTO));
        if (!paramMap.containsKey("comtop_bpms_current_orgId")) {
            paramMap.put("comtop_bpms_current_orgId", SystemHelper.getCurUserInfo().getOrgId());
        }
        UserInfo[] userInfos = capWorkflowCoreService.queryReassignTransUsers(processId, todoTaskId, paramMap);
        nodeInfo.setUsers(userInfos);
        NodeInfo[] colNodeInfo = new NodeInfo[] { nodeInfo };
        return colNodeInfo;
    }
    
    /**
     * 查找指定回退节点和人员
     * 
     * @param workId 工单id
     * @param todoTaskId 待办任务ID,必填项
     * @param userId 用户ID 名称
     * @return 节点和人员信息
     */
    public NodeInfo[] queryBackTransNodes(String workId, String todoTaskId, String userId) {
        // 获取工单对象
        String processId = this.getProcessId(null);
        NodeInfo[] backNodeInfos = capWorkflowCoreService.queryBackTransNodes(processId, todoTaskId, userId);
        if (backNodeInfos == null || backNodeInfos.length == 0) {
            throw new CapWorkflowException("未找到可以指定回退的节点。");
        }
        return backNodeInfos;
    }
    
    /**
     * 分页查询节点上人员
     * 
     * 
     * @param workflowParam 工作流参数对象
     * @param processVersion 流程版本
     * @param specialNodeId 指定节点Id
     * @param filterWord 人员过滤关键字
     * @return 用户集合
     */
    public UserInfo[] queryUserListByNode(CapWorkflowParam workflowParam, int processVersion, String specialNodeId,
        String filterWord) {
        VO workDTO = queryWorkVOByWorkId(workflowParam.getWorkId());
        // 获取扩展属性对象,用于条件判断
        Map<String, Object> userParamMap = this.getParamMap(workDTO);// 用户定义的属性
        Map<String, Object> objMap = workflowParam.getParamMap();
        objMap.putAll(userParamMap);
        
        if (!objMap.containsKey("comtop_bpms_current_userId")) {
            objMap.put("comtop_bpms_current_userId", SystemHelper.getCurUserInfo().getUserId());
        }
        if (!objMap.containsKey("comtop_bpms_current_orgId")) {
            objMap.put("comtop_bpms_current_orgId", workflowParam.getOrgId());
        } else {
            workflowParam.setOrgId((String) objMap.get("comtop_bpms_current_orgId"));
        }
        objMap.put(workflowParam.getParamKey(), workflowParam.getOrgId());
        objMap.put("comtop_bpms_current_breauId", SystemHelper.getCurUserInfo().getOrgId());
        workflowParam.setProcessId(this.getProcessId(workDTO));
        return capWorkflowCoreService.queryNodePostUsersByPage(workflowParam, processVersion, specialNodeId,
            filterWord, objMap);
    }
    
    /**
     * 获取扩展属性值
     * 
     * @param id 单据id
     * @return 扩展属性值
     */
    public Map<String, String> queryExtendAttrOfFirstNode(String id) {
        VO vo = queryWorkVOByWorkId(id);
        Map<String, Object> paramMap = this.getParamMap(vo);
        String processId = this.getProcessId(vo);
        NodeInfo[] nodeInfos = capWorkflowCoreService.queryFirstUserNodes(processId, paramMap);
        if (nodeInfos == null || nodeInfos.length == 0) {
            throw new CapWorkflowException("没有找到起始节点，请检查流程图。");
        }
        NodeExtendAttributeInfo[] extendAttrInfos = capWorkflowCoreService.queryNodeExtendAttributes(processId,
            nodeInfos[0].getProcessVersion(), nodeInfos[0].getNodeId());
        
        Map<String, String> attrMap = new HashMap<String, String>();
        if (extendAttrInfos != null && extendAttrInfos.length > 0) {
            for (NodeExtendAttributeInfo extendAttr : extendAttrInfos) {
                attrMap.put(extendAttr.getExtendAttributeKey(), extendAttr.getDataState());
            }
        }
        return attrMap;
    }
    
    /**
     * 获取扩展属性值
     * 
     * @param id 单据id
     * @param taskId 任务id
     * @return 扩展属性值
     */
    public Map<String, String> queryExtendAttrByTaskId(String id, String taskId) {
        // 获取工单对象
        String processId = this.getProcessId(null);
        String nodeId = null;
        TodoTaskInfo todoTaskInfo = capWorkflowCoreService.queryTodoTask(processId, taskId);
        if (todoTaskInfo == null) {
            DoneTaskInfo taskInfo = capWorkflowCoreService.queryDoneTask(processId, taskId);
            nodeId = taskInfo.getCurNodeId();
        } else {
            nodeId = todoTaskInfo.getCurNodeId();
        }
        if (nodeId == null) {
            throw new CapWorkflowException("找不到当前任务" + taskId + "所在的流程节点。");
        }
        return this.queryExtendAttrByNodeId(id, nodeId);
    }
    
    /**
     * 获取扩展属性值
     * 
     * @param id 单据id
     * @param nodeId 节点id
     * @return 扩展属性值
     */
    public Map<String, String> queryExtendAttrByNodeId(String id, String nodeId) {
        // 获取工单对象
        String processId = this.getProcessId(null);
        ProcessInfo processInfo = capWorkflowCoreService.queryLastProcessInfoById(processId);
        NodeExtendAttributeInfo[] extendAttrInfos = capWorkflowCoreService.queryNodeExtendAttributes(processId,
            processInfo.getVersion(), nodeId);
        
        Map<String, String> attrMap = new HashMap<String, String>();
        if (extendAttrInfos != null && extendAttrInfos.length > 0) {
            for (NodeExtendAttributeInfo extendAttr : extendAttrInfos) {
                attrMap.put(extendAttr.getExtendAttributeKey(), extendAttr.getDataState());
            }
        }
        return attrMap;
    }
    
    /**
     * 
     * 查询用户集合所参与的指定流程上的节点信息，在指定流程的最新版本上（取并集）
     * 
     * @param userIds 指定的用户ID集
     * @param taskType 任务类型，todo:待办，done已办
     * @return 指定的用户集在指定的流程最新版本上所参数的节点集合（取并集）
     */
    public List<BpmsNodeInfo> queryIntersecHumanNodesByUserIds(String userIds, String taskType) {
        // 获取当前业务所对应的流程ID
        String processId = this.getProcessId(null);
        return capWorkflowCoreService.queryIntersecHumanNodesByUserIds(processId, userIds, taskType);
    }
    
    /**
     * 查询指定流程的所有用户节点
     * 
     * @return 指定流程编号的所有用户节点
     */
    public List<BpmsNodeInfo> queryHumanNodesByProcessId() {
        String processId = this.getProcessId(null);
        return capWorkflowCoreService.queryHumanNodesByProcessId(processId);
    }
    
    /**
     * 获取配置过滤函数中变量对应的信息
     * 
     * @param workId 工单Id
     * @param filterKey 过滤函数对应的key
     * @return 用户信息
     */
    public UserInfo getDeptFilerParam(String workId, String filterKey) {
        UserInfo userinfo = new UserInfo();
        
        VO workDTO = queryWorkVOByWorkId(workId);
        Map<String, Object> paramMap = this.getParamMap(workDTO);
        
        String orgId = "";
        if (paramMap.get(filterKey) != null) {
            orgId = String.valueOf(paramMap.get(filterKey));
        }
        if (StringUtil.isNotBlank(orgId)) {
            if (orgId.split(",").length > 1) {
                userinfo.setDeptId(orgId);
                userinfo.setDeptPath("");
            } else {
                OrganizationInfoVO orgVO = SystemHelper.getOrganizationWithOrgId(orgId);
                userinfo.setDeptId(orgVO.getOrgId());
                userinfo.setDeptPath(orgVO.getNameFullPath());
            }
        } else {
            userinfo.setDeptId(SystemHelper.getCurUserInfo().getOrgId());
            userinfo.setDeptPath(SystemHelper.getCurUserInfo().getNameFullPath());
        }
        return userinfo;
    }
    
    /**
     * 根据待办节点信息批量生成已办
     * 
     * @param workflowParamList 流程信息列表
     * @param processInstanceInfos s
     * @param operateType 操作类型
     * @return 生成已办信息的工单集合
     */
    private List<VO> updateWorkFlowDoneByTodoTask(List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, String operateType) {
        List<VO> works = new ArrayList<VO>();
        VO workDTO = null;
        TodoTaskInfo todoTaskInfo = null;
        String doneTaskId = null;
        for (CapWorkflowParam workflowParam : workflowParamList) {
            workDTO = queryWorkVOByWorkId(workflowParam.getWorkId());
            works.add(workDTO);
            String processInsId = workDTO.getProcessInsId();
            String processId = this.getProcessId(workDTO);
            todoTaskInfo = WorkflowHelper.queryBeforeForeTodoTaskInfo(workflowParam, processInsId, processId);
            doneTaskId = getDoneTaskId(workflowParam, processInstanceInfos);
            try {
                if (todoTaskInfo == null) {// 待办节点为空，则将当前节点ID及节点名称置空
                    updateWorkfowDone(workDTO, workflowParam, doneTaskId, null, null, null, operateType);
                } else {
                    updateWorkfowDone(workDTO, workflowParam, doneTaskId, todoTaskInfo.getCurNodeId(),
                        todoTaskInfo.getCurNodeName(), todoTaskInfo.getActivityInsId(), operateType);
                }
            } catch (Exception ex) {
                throw new CapWorkflowException("更新已办信息出错。", ex);
            }
        }
        return works;
    }
    
    /**
     * 更新工作台 已办列表
     * 
     * @param workDTO 工单对象
     * @param workflowParam 流程参数
     * @param taskId 任务ID
     * @param currNodeId 当前节点ID
     * @param currNodeName 当前节点名称
     * @param activityInsId 活动实例ID
     * @param oprate 流程操作类型
     */
    public void updateWorkfowDone(VO workDTO, CapWorkflowParam workflowParam, String taskId, String currNodeId,
        String currNodeName, String activityInsId, String oprate) {
        if (StringUtils.isBlank(taskId)) {
            // 获取已办信息ID
            taskId = WorkflowWorkbenchHelper.getDoneTaskId(workflowParam, currNodeId, activityInsId);
        }
        workDTO.setTaskId(taskId);
        DoneDTO doneDTO = this.createDoneInfo(workDTO);
        if (doneDTO == null) {
            throw new CapWorkflowException("更新已办信息错误，业务模块未正确的实现【createDoneInfo】方法。");
        }
        doneDTO.setWorkId(StringUtils.isBlank(doneDTO.getWorkId()) ? CapRuntimeUtils.getId(workDTO) : doneDTO
            .getWorkId());
        // 业务编号
        doneDTO.setBissnessId(CapRuntimeUtils.getId(workDTO));
        // 流程编号
        doneDTO.setProcessId(this.getProcessId(workDTO));
        // 操作类型
        doneDTO.setWorkOperate(oprate);
        // 当前节点ID
        doneDTO.setCurNodeId(currNodeId);
        // 当前节点名称
        doneDTO.setCurNodeName(currNodeName);
        // 处理人及停留节点信息
        WorkflowWorkbenchHelper.compareTransactorAndNodeInfo(workflowParam, doneDTO);
        // 设置流程是否是为结束节点isDone为“Y”表示流程结束节点，“N”表示流程中节点
        if (WorkflowConstant.WORK_FLOW_STATE_OVER == workflowParam.getFlowState()) {
            doneDTO.setIsDone("Y");
        } else {
            doneDTO.setIsDone("N");
        }
        // 更新已办数据
        WorkbenchHelper.createDoneInfo(doneDTO);
    }
    
    /**
     * 从bpms返回值中获取已办id
     * 
     * @param workflowParam 原始参数
     * @param processInstanceInfos 返回值
     * @return 已办id
     */
    private String getDoneTaskId(CapWorkflowParam workflowParam, ProcessInstanceInfo[] processInstanceInfos) {
        if (null == workflowParam.getTaskId() || "".equals(workflowParam.getTaskId())) {
            return null;
        }
        TodoTaskInfo[] delTodoTaskInfos = null;
        DoneTaskInfo[] addDoneTaskInfos = null;
        for (ProcessInstanceInfo processInstanceInfo : processInstanceInfos) {
            delTodoTaskInfos = processInstanceInfo.getTaskInfo().getDelTodoTaskInfos();
            if (null != delTodoTaskInfos && delTodoTaskInfos.length > 0) {
                if (delTodoTaskInfos[0].getTodoTaskId().equals(workflowParam.getTaskId())) {
                    addDoneTaskInfos = processInstanceInfo.getTaskInfo().getAddDoneTaskInfos();
                    if (null != delTodoTaskInfos && delTodoTaskInfos.length > 0) {
                        return addDoneTaskInfos[0].getDoneTaskId();
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * 批量修改
     * 
     * @param workRecords 记录集
     */
    private void batchUpdate(List<VO> workRecords) {
        capBaseCommonDAO.update(workRecords);
    }
    
    /**
     * 根据voId查询业务审批VO对象
     * 
     * @param voId vo主键Id
     * @param taskId 任务Id
     * @param taskType 任务类型：todo待办 done已办
     * @return 待处理VO数据
     */
    public VO queryObjectAndTaskVOById(String voId, String taskId, String taskType) {
        VO workVO = newQueryInstanceByID(voId);
        workVO = load(workVO);
        workVO.setTaskId(taskId);
        boolean isDone = false;
        String strPrefix = "";
        if ("todo".equals(taskType)) {
            strPrefix = "queryTodo";
        } else if ("done".equals(taskType)) {
            isDone = true;
            strPrefix = "queryDone";
        } else {
            return workVO;
        }
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(getProcessId(workVO), isDone));
        VO taskVO = (VO) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, strPrefix, "VOByTaskId"), workVO);
        return taskVO == null ? workVO : taskVO;
    }
    
    /**
     * 查询待办信息根据taskId
     * 
     * @param voId 工单id
     * @param taskId 任务id
     * @return 待办VO
     */
    public VO queryTodoVOByTaskId(String voId, String taskId) {
        VO workVO = newQueryInstanceByID(voId);
        workVO = load(workVO);
        workVO.setTaskId(taskId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(getProcessId(workVO), false));
        VO taskVO = (VO) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryTodo", "VOByTaskId"),
            workVO);
        return taskVO == null ? workVO : taskVO;
    }
    
    /**
     * 查询已办信息根据taskId
     * 
     * @param voId 工单id
     * @param taskId 任务id
     * @return 已办VO
     */
    public VO queryDoneVOByTaskId(String voId, String taskId) {
        VO workVO = newQueryInstanceByID(voId);
        workVO = load(workVO);
        workVO.setTaskId(taskId);
        workVO.setTransTableName(WorkflowHelper.readTaskTableName(getProcessId(workVO), true));
        VO taskVO = (VO) capBaseCommonDAO.selectOne(CapRuntimeUtils.getSqlKey(workVO, "queryDone", "VOByTaskId"),
            workVO);
        return taskVO == null ? workVO : taskVO;
    }
    
    /**
     * 
     * @param voId vo主键Id
     * @return 查询对象
     */
    private VO newQueryInstanceByID(String voId) {
        Class voClass = getGenericClass();
        VO instance = null;
        try {
            instance = (VO) voClass.newInstance();
            CapRuntimeUtils.setId(instance, voId);
            return instance;
        } catch (InstantiationException e) {
            throw new CapWorkflowException("[信息]" + voClass.getName() + "无法创建实例.", e);
        } catch (IllegalAccessException e) {
            throw new CapWorkflowException("[信息]" + voClass.getName() + "无法创建实例.", e);
        }
    }
    
    /**
     * 查询流程实例处理跟踪（用于流程跟踪表展示）
     * 
     * @param workflowParam 流程参数
     * @return NodeTrackInfo[] 流程实例处理跟踪
     */
    public NodeTrackInfo[] queryProcessInsTransTrack(CapWorkflowParam workflowParam) {
        String strProcessId = getProcessIdFromParam(workflowParam);
        String strProcessInsId = workflowParam.getProcessInsId();
        return capWorkflowCoreService.queryProcessInsTransTrack(strProcessId, strProcessInsId);
    }
    
    /**
     * 查询已办任务ID
     * 
     * @param workflowParam
     *            工作流参数
     * @param activityInsId 活动实例ID
     * @param taskType 任务类型
     * @return 询已办任务ID
     */
    public String queryNewTaskId(CapWorkflowParam workflowParam, String activityInsId, String taskType) {
        return capWorkflowCoreService.queryNewTaskId(workflowParam.getProcessId(), workflowParam.getProcessInsId(),
            workflowParam.getCurrentUserId(), activityInsId, taskType);
    }
    
    /**
     * 查询新的任务ID
     * 
     * @param processId
     *            流程ID
     * @param nodeInstanceId 节点实例ID
     * @return 查询新的任务ID
     */
    public int queryNodeInsTodoTaskCount(String processId, String nodeInstanceId) {
        return capWorkflowCoreService.queryNodeInsTodoTaskCount(processId, nodeInstanceId);
    }
    
    @Override
    public boolean delete(VO vo) {
        
        // if(super.delete(vo)) {
        // 由于top平台的delete方法删除成功返回值是false，近期top平台会改所以先临时使用deleteList方法
        List<VO> tempList = new ArrayList<VO>(1);
        tempList.add(vo);
        if (super.deleteList(tempList)) {
            // 流程实例为null 直接返回true
            if (StringUtils.isBlank(vo.getProcessInsId())) {
                return true;
            }
            
            // 先从vo中获取流程id
            String processId = vo.getProcessId();
            // 若为null则获取appservice里的流程id
            if (StringUtils.isBlank(processId)) {
                processId = getProcessId();
            }
            // 去掉已办中心对应查看链接
            WorkbenchHelper.updateInvalidDone(processId, vo.getPrimaryValue());
            
            // 获取当前登录用户
            UserDTO currentUser = SystemHelper.getCurUserInfo();
            // 删除相关工作流数据
            if (currentUser != null) {
                return capWorkflowCoreService.deleteProcessInstanceData(processId, vo.getProcessInsId(),
                    currentUser.getUserId(), currentUser.getEmployeeName());
            }
        }
        return false;
    }
    
    @Override
    public boolean deleteList(List<VO> voList) {
        for (VO vo : voList) {
            this.delete(vo);
        }
        return true;
    }
    
}
