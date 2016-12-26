/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.appservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.bpms.client.ClientFactory;
import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.bpms.common.model.BatchWorkFlowParamVO;
import com.comtop.bpms.common.model.DoneTaskInfo;
import com.comtop.bpms.common.model.HumanNodeInfo;
import com.comtop.bpms.common.model.NodeDefinitionInfo;
import com.comtop.bpms.common.model.NodeExtendAttributeInfo;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.NodeTodoTaskCntInfo;
import com.comtop.bpms.common.model.NodeTrackInfo;
import com.comtop.bpms.common.model.ProcessInfo;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.bpms.common.model.ReassignResultInfo;
import com.comtop.bpms.common.model.ResultVO;
import com.comtop.bpms.common.model.TodoTaskInfo;
import com.comtop.bpms.common.model.UserInfo;
import com.comtop.bpms.common.model.WorkFlowParamVO;
import com.comtop.bpms.common.util.BpmsConstant;
import com.comtop.cap.runtime.base.dao.BpmsBussExtDAO;
import com.comtop.cap.runtime.base.dao.CapWorkflowQueryDAO;
import com.comtop.cap.runtime.base.exception.CapWorkflowException;
import com.comtop.cap.runtime.base.model.BpmsNodeInfo;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.base.util.WorkflowCoreHelper;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;

/**
 * 工作流内部service服务类 <br>
 * 有些功能bpms无法提供,需直接查询工作流表
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-12 李忠文
 */
@PetiteBean
public class CapWorkflowCoreService extends CapBaseAppService {
    
    /** 日志对象 */
    static final Logger LOG = LoggerFactory.getLogger(CapWorkflowCoreService.class);
    
    /**
     * BPMS数据操作对象 BpmsBussExtDAO
     */
    @PetiteInject
    protected CapWorkflowQueryDAO capWorkflowQueryDAO;
    
    /**
     * BPMS数据操作对象 BpmsBussExtDAO
     */
    @PetiteInject
    protected BpmsBussExtDAO bpmsDao;
    
    /**
     * 默认上报
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId， currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 流程实例信息对象
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo defaultEntry(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().defaultEntry(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 指定上报
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，targetNodeInfos， currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 流程实例信息对象
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo specialEntry(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().specialEntry(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 指定流程实例ID指定上报
     * 
     * @param workFlowParamVO
     *            其中 processId,curUserId,processInsId,targetNodeInfos中的nodeId,
     *            users中的userId为必输项
     * @param paramsMap
     *            流程参数，允许为空
     * @return 流程实例信息对象
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo specialEntryByProcessInsId(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().specialEntryByProcessInsId(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 默认发送
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，processInsId，taskId(待办ID)， currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 是否发送成功
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo defaultFore(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().defaultForeByReturn(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 指定发送
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，processInsId，taskId(待办ID)，
     *            targetNodeInfos，currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 是否发送成功
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo specialFore(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().specialForeByReturn(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 默认回退
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，processInsId，taskId(待办ID)， currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 是否发送成功
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo defaultBack(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().defaultBackByReturn(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 指定回退
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，processInsId，taskId(待办ID)，
     *            targetNodeInfos，currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 是否发送成功
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo specialBack(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().specialBackByReturn(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
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
     * </pre>
     * 
     * @param workFlowParam
     *            - 工作流参数对象,其中属性taskId,currentUserId,processId为必输项,
     *            建议输入currentUserName以提高性能。
     * @param paramMap
     *            - 流程变量
     * @return 主流程实例对象
     * @throws CapWorkflowException
     *             异常
     */
    public ProcessInstanceInfo backwardToFirstUserTaskByReturn(WorkFlowParamVO workFlowParam,
        Map<String, Object> paramMap) throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().backwardToFirstUserTaskByReturn(workFlowParam, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 已结束流程回退到申请人，
     * 
     * <pre>
     * 返回值为主流程实例对象 ProcessInstanceInfo
     * 其中属性NodeInfo指本次操作后产生的待办停留的用户节点
     * NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * 回退到第一个人工任务，如果没有人工参与过，则抛异常
     * </pre>
     * 
     * @param workFlowParam
     *            - 工作流参数对象,其中属性taskId,currentUserId,processId为必输项,
     *            建议输入currentUserName以提高性能。
     * @param paramMap
     *            - 流程变量;非必填项
     * @return 主流程实例对象
     * @throws CapWorkflowException
     *             异常
     */
    public ProcessInstanceInfo backOverToFirstUserTask(WorkFlowParamVO workFlowParam, Map<String, Object> paramMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().backOverToFirstUserTask(workFlowParam, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 回退已结束，返回值为主流程实例对象ProcessInstanceInfo,其中属性NodeInfo指本次操作后产生的待办停留的用户节点
     * 
     * @param workFlowParamVO
     *            工作流参数对象,其中属性processInsId,currentUserId,processId为必输项,
     *            建议输入currentUserName以提高性能。
     * @param paramsMap
     *            参数对象的集合
     * @return 主流程实例对象
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo backOverByReturn(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().backOverByReturn(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 保存工作流待办意见
     *
     * @param workFlowParamVO
     *            工作流参数对象,其中属性processInsId,currentUserId,processId为必输项,
     *            建议输入currentUserName以提高性能。
     * @param paramsMap
     *            参数对象的集合
     * @return 成功或者失败
     * @throws CapWorkflowException
     *             工作流异常
     */
    public boolean saveNote(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap) throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().updateTaskNote(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 撤回
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，processInsId，taskId(已办ID)，currentUserId 需要必填
     * @param paramsMap
     *            参数对象的集合
     * @return 是否发送成功
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo undoWorkFlow(WorkFlowParamVO workFlowParamVO, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().undoWorkFlowByReturn(workFlowParamVO, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 转发
     * 
     * @param workFlowParamVO
     *            此参数模型中 processId，processInsId，taskId(已办ID)，currentUserId 需要必填
     * @param userInfos
     *            人员信息集合
     * @return 转发结果信息对象,返回转发的结果.
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ReassignResultInfo reassignTask(WorkFlowParamVO workFlowParamVO, UserInfo[] userInfos)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().reassignUserTask(workFlowParamVO, userInfos);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 终结
     * 
     * @param processId
     *            工作流编码
     * @param processInsId
     *            待办任务ID
     * @param userId
     *            用户ID
     * @param userName
     *            意见
     * @return 转发结果信息对象,返回转发的结果.
     * @throws CapWorkflowException
     *             工作流异常
     */
    public boolean abortFlowInstance(String processId, String processInsId, String userId, String userName)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().abortProcessInstance(processId, processInsId, userId,
                userName);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询用户参与的已办节点
     * 
     * @param processId
     *            工作流编码
     * @param userId
     *            用户ID
     * @return 已办节点的集合
     */
    public List<NodeInfo> queryDoneTransNode(String processId, String userId) {
        DoneTaskInfo doneTaskInfo = new DoneTaskInfo();
        doneTaskInfo.setMainProcessId(processId);
        doneTaskInfo.setTransActorId(userId);
        return capBaseCommonDAO.queryList("workFlow.queryDoneTransNode", doneTaskInfo);
    }
    
    /**
     * 查询当前节点的下一批用户节点（获取最新版本的）
     * 
     * @param processId
     *            工作流编码
     * @param curNodeId
     *            当前节点ID
     * @param paramsMap
     *            参数
     * @return 下一批用户节点（获取最新版本的）
     * @throws CapWorkflowException
     *             工作流异常
     */
    public NodeInfo[] queryNextUserNodes(String processId, String curNodeId, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().queryNextUserNodes(processId, curNodeId, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询指定下发节点和人员
     * 
     * @param processId
     *            流程编码
     * @param paramMap
     *            参数
     * @param todoTaskId
     *            待办id
     * @param userId
     *            当前用户id
     * @return 指定下发节点和人员
     * @throws CapWorkflowException
     *             工作流异常
     */
    public NodeInfo[] queryForeTransNodes(String processId, String todoTaskId, String userId,
        Map<String, Object> paramMap) throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().queryForeTransNodeAndTransUser(processId, todoTaskId, userId,
                paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询转发节点和人员
     * 
     * @param processId
     *            流程编码
     * @param paramMap
     *            参数
     * @param todoTaskId
     *            待办id
     * @return 指定下发节点和人员
     * @throws CapWorkflowException
     *             工作流异常
     */
    public UserInfo[] queryReassignTransUsers(String processId, String todoTaskId, Map<String, Object> paramMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().queryReassignUser(processId, todoTaskId, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询指定回退节点和人员
     * 
     * @param processId
     *            流程编码
     * @param todoTaskId
     *            待办任务ID,必填项
     * @param userId
     *            用户ID 名称
     * @return 节点和人员信息
     * @throws CapWorkflowException
     *             工作流异常
     */
    public NodeInfo[] queryBackTransNodes(String processId, String todoTaskId, String userId)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().queryBackTransNodeAndTransUser(processId, todoTaskId, userId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询指定节点上符合过滤条件的人员信息
     * 
     * @param workflowUserSelectParam
     *            工作流参数对象
     * @param processVersion
     *            流程版本
     * @param specialNodeId
     *            指定节点Id
     * @param filterWord
     *            人员过滤关键字
     * @param paramMap
     *            流程参数集合
     * @return UserInfo[]
     * @throws CapWorkflowException
     *             异常
     */
    public UserInfo[] queryNodePostUsersByPage(CapWorkflowParam workflowUserSelectParam, int processVersion,
        String specialNodeId, String filterWord, Map<String, Object> paramMap) throws CapWorkflowException {
        try {
            WorkFlowParamVO workFlowParam = new WorkFlowParamVO();
            workFlowParam.setProcessId(workflowUserSelectParam.getProcessId());
            workFlowParam.setOrgId(workflowUserSelectParam.getOrgId());
            return ClientFactory.getHumanNodeService().queryNodeAllPostUsers(workFlowParam, processVersion,
                specialNodeId, filterWord, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询转发人员
     * 
     * @param processId
     *            流程编码
     * @param todoTaskId
     *            待办任务ID,必填项
     * @param userId
     *            用户ID 名称
     * @return 转发人员信息
     * @throws CapWorkflowException
     *             工作流异常
     */
    public UserInfo[] queryReassignUsers(String processId, String todoTaskId, String userId)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().queryReassignUsers(processId, todoTaskId, userId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 启动 工作流 返回 流程实例 ID(现有业务暂时没有启动工作流的要求，如有需要可放开此方法)
     * 
     * @param processeId
     *            工作流编码
     * @param userId
     *            用户ID
     * @param userName
     *            用户名称
     * @param paramMap
     *            参数
     * @return 流程实例ID
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo startProcessInstance(String processeId, String userId, String userName,
        Map<String, Object> paramMap) throws CapWorkflowException {
        ProcessInstanceInfo processInstanceInfo;
        try {
            processInstanceInfo = ClientFactory.getProcessInstanceService().startProcessInstance(processeId, userId,
                userName, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
        return processInstanceInfo;
    }
    
    /**
     * 删除流程实例及其所有相关实例数据--将与此流程实例相关的流程运行数据备份到对应的：_TRH数据表
     * 
     * @param processId
     *            流程id,必输项
     * @param processInsId
     *            流程实例ID,必输项
     * @param userId
     *            当前用户id,必输项
     * @param userName
     *            用户名称，必输项
     * @return 返回删除是否成功，true：成功
     * @throws CapWorkflowException
     *             工作流异常。
     */
    public boolean deleteProcessInstanceData(String processId, String processInsId, String userId, String userName)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().deleteProcessInstanceData(processId, processInsId, userId,
                userName);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 修改指定用户的待办任务为已读
     * 
     * @param processId
     *            流程Id, 必须输入
     * @param todoTaskId
     *            待办任务Id, 必须输入
     * @param userId
     *            用户Id, 必须输入
     * @throws CapWorkflowException
     *             异常
     */
    public void updateTaskReadFlag(String processId, String todoTaskId, String userId) throws CapWorkflowException {
        try {
            ClientFactory.getUserTaskService().updateTaskReadFlag(processId, todoTaskId, userId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 触发信号中间事件（广播式触发）
     * 
     * @param signalName
     *            信号名称,必须输入
     * @param userInfo
     *            用户对象，允许为空
     * @param paramMap
     *            流程变量 ,允许为空
     * @throws CapWorkflowException
     *             异常
     */
    public void throwSignalEvent(String signalName, UserInfo userInfo, Map<String, Object> paramMap)
        throws CapWorkflowException {
        try {
            ClientFactory.getEventService().throwSignalEvent(signalName, userInfo, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 触发信号中间事件，信号事件一对多触发，若不指定流程实例编号，则触发所有信号（指定式触发）
     * 
     * @param processId
     *            流程编码
     * @param signalName
     *            信号名称,必须输入
     * @param processInsIds
     *            目标流程实例编号
     * @param userInfo
     *            用户对象，允许为空
     * @param paramMap
     *            流程变量 ,允许为空
     * @throws CapWorkflowException
     *             异常
     */
    public void throwSignalEventByProcessInsIds(String processId, String signalName, String[] processInsIds,
        UserInfo userInfo, Map<String, Object> paramMap) throws CapWorkflowException {
        try {
            ClientFactory.getEventService().throwSignalEventByProcessInsIds(processId, signalName, processInsIds,
                userInfo, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 触发消息中间事件，消息事件只能一对一触发（指定式触发，不指定则按照消息队列取第一条消息）
     * 
     * @param processId
     *            流程编码
     * @param messageName
     *            信号名称,必须输入
     * @param processInsId
     *            目标流程实例编号
     * @param userInfo
     *            用户对象，允许为空
     * @param paramMap
     *            流程变量 ,允许为空
     * @throws CapWorkflowException
     *             异常
     */
    public void throwMessageEventByProcessInsIds(String processId, String messageName, String processInsId,
        UserInfo userInfo, Map<String, Object> paramMap) throws CapWorkflowException {
        try {
            ClientFactory.getEventService().throwMessageEventByProcessInsId(processId, messageName, processInsId,
                userInfo, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程实例id挂起流程实例
     * 
     * @param processId
     *            流程id,必输项
     * @param processInsId
     *            主流程实例ID,必输项
     * @param userId
     *            当前用户id,必输项
     * @param userName
     *            用户名
     * @return true:成功，false：失败
     * @throws CapWorkflowException
     *             异常
     */
    public boolean hungUpProcessInstance(String processId, String processInsId, String userId, String userName)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().hungUpProcessInstance(processId, processInsId, userId,
                userName);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量挂起流程实例
     * 
     * @param processId
     *            流程id,必输项
     * @param processInsIds
     *            主流程实例ID,必输项
     * @param userId
     *            当前用户id,必输项
     * @param userName
     *            用户名
     * @return true:成功，false：失败
     * @throws CapWorkflowException
     *             异常
     */
    public ResultVO hungUpProcessInstances(String processId, String[] processInsIds, String userId, String userName)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().hungUpProcessInstances(processId, processInsIds, userId,
                userName);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程实例id恢复流程实例
     * 
     * @param processId
     *            流程id,必输项
     * @param processInsId
     *            主流程实例ID,必输项
     * @param userId
     *            当前用户id,必输项
     * @param userName
     *            用户名
     * @return true:成功，false：失败
     * @throws CapWorkflowException
     *             异常
     */
    public boolean recoverProcessInstance(String processId, String processInsId, String userId, String userName)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().recoverProcessInstance(processId, processInsId, userId,
                userName);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 工作流正常流程结束
     * 
     * @param workflowParam
     *            - 工作流参数对象其中属性taskId, processId,
     *            currentUserId为必输项,建议输入currentUserName 其中属性
     * @return ProcessInstanceInfo
     */
    public ProcessInstanceInfo overFlow(CapWorkflowParam workflowParam) {
        try {
            return ClientFactory.getUserTaskService().overFlow(workflowParam);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
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
     * @param paramMap
     *            流程变量，可选输入
     * @return 主流程实例对象
     */
    public ProcessInstanceInfo jump(CapWorkflowParam workflowParam, Map<String, Object> paramMap) {
        try {
            return ClientFactory.getUserTaskService().jump(workflowParam, paramMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程实例id恢复流程实例
     * 
     * @param processId
     *            流程id,必输项
     * @param processInsIds
     *            主流程实例ID,必输项
     * @param userId
     *            当前用户id,必输项
     * @param userName
     *            用户名
     * @return true:成功，false：失败
     * @throws CapWorkflowException
     *             异常
     */
    public ResultVO recoverProcessInstances(String processId, String[] processInsIds, String userId, String userName)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().recoverProcessInstances(processId, processInsIds, userId,
                userName);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取最新版本流程下面的所有用户任务节点
     * 
     * @param processId
     *            流程编码
     * @return 节点信息
     * @throws CapWorkflowException
     *             异常
     */
    public NodeDefinitionInfo[] queryUserTaskNodeInfo(String processId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryUserTaskNodeInfo(processId);
    }
    
    /**
     * 查询指定流程、指定版本中所有设置了指定扩展属性，或指定扩展属性值为指定值的相关节点集合
     * 
     * @param processId
     *            流程编码
     * @param attrKey
     *            扩展属性key
     * @return 节点信息集合
     * @throws CapWorkflowException
     *             异常
     */
    public NodeDefinitionInfo[] queryNodeExtAttsByCondition(String processId, String attrKey)
        throws CapWorkflowException {
        return WorkflowCoreHelper.queryNodeExtAttsByCondition(processId, attrKey);
    }
    
    /**
     * 获取节点上的所有扩展属性
     * 
     * @param processId
     *            工作流编码
     * @param version
     *            版本
     * @param nodeId
     *            节点ID
     * @return 扩展属性对象
     * @throws CapWorkflowException
     *             工作流异常
     */
    public NodeExtendAttributeInfo[] queryNodeExtendAttributes(String processId, int version, String nodeId)
        throws CapWorkflowException {
        return WorkflowCoreHelper.queryNodeExtendAttributes(processId, version, nodeId);
    }
    
    /**
     * 获取最新版本的流程
     * 
     * @param processId
     *            工作流编码
     * @return 流程定义信息
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInfo queryLastProcessInfoById(String processId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryLastProcessInfoById(processId);
    }
    
    /**
     * 获取指定版本的流程节点的扩展属性值
     * 
     * @param processId
     *            工作流编码
     * @param nodeId
     *            节点ID
     * @param version
     *            流程版本号
     * @param attrKey
     *            扩展属性KEY
     * @return 扩展属性的值
     * @throws CapWorkflowException
     *             工作流异常
     */
    public String queryNodeExtendAttributeValueByKey(String processId, int version, String nodeId, String attrKey)
        throws CapWorkflowException {
        return WorkflowCoreHelper.queryNodeExtendAttributeValueByKey(processId, version, nodeId, attrKey);
    }
    
    /**
     * 查询用户参与的待办节点
     * 
     * @param processId
     *            流程编码
     * @param userId
     *            用户id
     * @return 待办节点的集合
     * @throws CapWorkflowException
     *             工作流异常
     */
    public List<NodeInfo> queryTodoTransNode(String processId, String userId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryTodoTransNode(processId, userId);
    }
    
    /**
     * 获取指定节点上的用户
     * 
     * @param processId
     *            流程编码
     * @param nodeId
     *            节点编号
     * @param version
     *            当前流程版本
     * @return 节点信息
     * @throws CapWorkflowException
     *             异常
     */
    public UserInfo[] queryUsersByProcessIdNodeIdAndVersion(String processId, String nodeId, int version)
        throws CapWorkflowException {
        return WorkflowCoreHelper.queryUsersByProcessIdNodeIdAndVersion(processId, nodeId, version);
    }
    
    /**
     * 获取已办记录
     * 
     * @param processId
     *            流程编码
     * @param todoTaskId
     *            已办id
     * @return 已办记录
     * @throws CapWorkflowException
     *             工作流异常
     */
    public DoneTaskInfo queryDoneTask(String processId, String todoTaskId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryDoneTask(processId, todoTaskId);
    }
    
    /**
     * 查询用户参与的待办节点,过滤上报节点
     * 
     * @param processId
     *            流程编码
     * @param userId
     *            用户id
     * @return 待办节点的集合
     */
    public List<NodeTodoTaskCntInfo> queryTodoTaskCount(String processId, String userId) {
        return WorkflowCoreHelper.queryTodoTaskCount(processId, userId);
    }
    
    /**
     * 根据流实例ID，查询流程实例当前停留节点
     * 
     * @param processId
     *            流程编号
     * @param processInsId
     *            流程实例id
     * @return 节点信息
     * @throws CapWorkflowException
     *             工作流异常
     */
    public NodeInfo[] queryWatingNodes(String processId, String processInsId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryWatingNodes(processId, processInsId);
    }
    
    /**
     * 根据 工作流编码、其他组合条件 查询已办的任务列表信息
     * 
     * @param processId
     *            流程编码
     * @param doneTaskInfo
     *            已办信息
     * @return 办的任务列表信息集合(没有返回NULL)
     * @throws CapWorkflowException
     *             工作流异常
     */
    public List<DoneTaskInfo> queryDoneTasks(String processId, DoneTaskInfo doneTaskInfo) throws CapWorkflowException {
        return WorkflowCoreHelper.queryDoneTasks(processId, doneTaskInfo);
    }
    
    /**
     * 根据流程编码查询第一批用户节点
     * 
     * @param processId
     *            流程编码
     * @param paramsMap
     *            参数
     * @return 第一批用户节点
     * @throws CapWorkflowException
     *             工作流异常
     */
    public NodeInfo[] queryFirstUserNodes(String processId, Map<String, Object> paramsMap) throws CapWorkflowException {
        return WorkflowCoreHelper.queryFirstUserNodes(processId, paramsMap);
    }
    
    /**
     * 查询 流程实例信息
     * 
     * @param processId
     *            工作流编码
     * @param processInsId
     *            流程实例ID
     * @return 流程实例信息对象
     * @throws CapWorkflowException
     *             工作流异常
     */
    public ProcessInstanceInfo queryProcessInsInfo(String processId, String processInsId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryProcessInsInfo(processId, processInsId);
    }
    
    /**
     * 根据 工作流编码、其他组合条件 查询待办的任务列表信息
     * 
     * @param processId
     *            流程编码
     * @param todoTaskInfo
     *            待办信息
     * @return 办的任务列表信息集合(没有返回NULL)
     * @throws CapWorkflowException
     *             工作流异常
     */
    public List<TodoTaskInfo> queryTodoTasks(String processId, TodoTaskInfo todoTaskInfo) throws CapWorkflowException {
        return WorkflowCoreHelper.queryTodoTasks(processId, todoTaskInfo);
    }
    
    /**
     * 获取待办记录
     * 
     * @param processId
     *            流程编码
     * @param todoTaskId
     *            待办id
     * @return 待办记录
     * @throws CapWorkflowException
     *             工作流异常
     */
    public TodoTaskInfo queryTodoTask(String processId, String todoTaskId) throws CapWorkflowException {
        return WorkflowCoreHelper.queryTodoTask(processId, todoTaskId);
    }
    
    /**
     * 
     * 查询用户集合所参与的指定流程上的节点信息，在指定流程的最新版本上（取并集）
     * 
     * @param processId
     *            流程ID
     * @param userIds
     *            指定的用户ID集
     * @param taskType
     *            任务类型，todo:待办，done已办
     * @return 指定的用户集在指定的流程最新版本上所参数的节点集合（取并集）
     */
    public List<BpmsNodeInfo> queryIntersecHumanNodesByUserIds(String processId, String userIds, String taskType) {
        List<BpmsNodeInfo> nodeList = new ArrayList<BpmsNodeInfo>();
        if ("todo".equals(taskType)) {
            String tableName = WorkflowCoreHelper.readToDoTaskTableName(processId);
            nodeList = capWorkflowQueryDAO.queryIntersecHumanTaskNodes(processId, userIds, tableName);
        } else if ("done".equals(taskType)) {
            String tableName = WorkflowCoreHelper.readDoneTaskTableName(processId);
            nodeList = capWorkflowQueryDAO.queryIntersecHumanTaskNodes(processId, userIds, tableName);
        }
        return nodeList;
    }
    
    /**
     * 查询指定流程的所有用户节点
     * 
     * @param processId
     *            流程ID
     * @return 指定流程编号的所有用户节点
     */
    public List<BpmsNodeInfo> queryHumanNodesByProcessId(String processId) {
        List<BpmsNodeInfo> nodeList = new ArrayList<BpmsNodeInfo>();
        if (StringUtils.isEmpty(processId)) {
            return nodeList;
        }
        try {
            HumanNodeInfo[] nodes = ClientFactory.getHumanNodeService().queryHumanNodes("", processId, 0, null, 1, 500);
            if (null != nodes && nodes.length > 0) {
                for (HumanNodeInfo node : nodes) {
                    nodeList.add(new BpmsNodeInfo(processId, node.getNodeId(), node.getNodeName()));
                }
            }
            return nodeList;
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量默认上报
     * 
     * @param workFlowParams
     *            节点信息
     * @param version
     *            流程版本
     * @return ProcessInstanceInfo[]
     */
    public ProcessInstanceInfo[] batchDefaultEntryByReturn(BatchWorkFlowParamVO workFlowParams, int version) {
        try {
            return ClientFactory.getUserTaskService().batchDefaultEntryByReturn(workFlowParams, version);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量指定上报
     * 
     * @param workFlowParams
     *            节点信息
     * @param version
     *            流程版本
     * @return ProcessInstanceInfo[]
     */
    public ProcessInstanceInfo[] batchSpecialEntryByReturn(BatchWorkFlowParamVO workFlowParams, int version) {
        try {
            return ClientFactory.getUserTaskService().batchSpecialEntryByReturn(workFlowParams, version);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量默认下发
     * 
     * <pre>
     * NodeInfo指本次操作后产生的待办停留的用户节点
     * NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * </pre>
     * 
     * @param workFlowParams
     *            批量工作流对象
     * @return ProcessInstanceInfo[] 主流程实例对象ProcessInstanceInfo
     * @throws CapWorkflowException
     *             异常
     */
    public ProcessInstanceInfo[] batchDefaultForeByReturn(BatchWorkFlowParamVO workFlowParams) {
        try {
            return ClientFactory.getUserTaskService().batchDefaultForeByReturn(workFlowParams);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量指定下发
     * 
     * <pre>
     * NodeInfo指本次操作后产生的待办停留的用户节点
     * NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * </pre>
     * 
     * @param workFlowParams
     *            批量工作流对象
     * @return ProcessInstanceInfo[] 主流程实例对象ProcessInstanceInfo
     * @throws CapWorkflowException
     *             异常
     */
    public ProcessInstanceInfo[] batchSpecialForeByReturn(BatchWorkFlowParamVO workFlowParams) {
        try {
            return ClientFactory.getUserTaskService().batchSpecialForeByReturn(workFlowParams);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量默认回退
     * 
     * <pre>
     * NodeInfo指本次操作后产生的待办停留的用户节点 
     * authorizerId,authorizerName委托人编号和名称为可选项， 当输入时委托人编号和名称需同时输入NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * </pre>
     * 
     * @param workFlowParams
     *            批量工作流对象
     * @return ProcessInstanceInfo[] 主流程实例对象ProcessInstanceInfo
     * @throws CapWorkflowException
     *             异常
     */
    public ProcessInstanceInfo[] batchDefaultBackByReturn(BatchWorkFlowParamVO workFlowParams) {
        try {
            return ClientFactory.getUserTaskService().batchDefaultBackByReturn(workFlowParams);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量指定回退
     * 
     * <pre>
     * NodeInfo指本次操作后产生的待办停留的用户节点 
     * authorizerId,authorizerName委托人编号和名称为可选项， 当输入时委托人编号和名称需同时输入NodeInfo中的属性cooperationFlag和cooperationId在本接口返回值中无效
     * </pre>
     * 
     * @param workFlowParams
     *            批量工作流对象
     * @return ProcessInstanceInfo[] 主流程实例对象ProcessInstanceInfo
     * @throws CapWorkflowException
     *             异常
     */
    public ProcessInstanceInfo[] batchSpecialBackByReturn(BatchWorkFlowParamVO workFlowParams) {
        try {
            return ClientFactory.getUserTaskService().batchSpecialBackByReturn(workFlowParams);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 批量转发
     * 
     * @param workFlowParams
     *            批量工作流对象
     * @return ProcessInstanceInfo[] 主流程实例对象ProcessInstanceInfo
     */
    public ProcessInstanceInfo[] batchReassign(BatchWorkFlowParamVO workFlowParams) {
        try {
            return ClientFactory.getUserTaskService().batchReassignUserTaskByReturn(workFlowParams);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询新的任务ID
     * 
     * @param processId
     *            流程ID
     * @param processInsId
     *            流程实例ID
     * @param userId
     *            用户ID
     * @param activityInsId
     *            活动实例ID
     * @param taskType
     *            任务类型
     * 
     * @return 查询新的任务ID
     */
    public String queryNewTaskId(String processId, String processInsId, String userId, String activityInsId,
        String taskType) {
        String taskIdFieldName = null;
        
        String tableName = null;
        if ("todo".equals(taskType)) {
            taskIdFieldName = "todo_task_id";
            tableName = WorkflowCoreHelper.readToDoTaskTableName(processId);
        } else if ("done".equals(taskType)) {
            taskIdFieldName = "done_task_id";
            tableName = WorkflowCoreHelper.readDoneTaskTableName(processId);
        }
        Map<String, String> objParams = new HashMap<String, String>();
        objParams.put("processId", processId);
        objParams.put("processInsId", processInsId);
        objParams.put("activityInsId", activityInsId);
        objParams.put("userId", userId);
        objParams.put("taskIdFieldName", taskIdFieldName);
        objParams.put("tableName", tableName);
        return capWorkflowQueryDAO.queryNewTaskId(objParams);
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
        String tableName = WorkflowCoreHelper.readToDoTaskTableName(processId);
        Map<String, String> objParams = new HashMap<String, String>();
        objParams.put("processId", processId);
        objParams.put("nodeInsId", nodeInstanceId);
        objParams.put("tableName", tableName);
        return capWorkflowQueryDAO.queryNodeInsTodoTaskCount(objParams);
    }
    
    /**
     * 查询流程实例处理跟踪（用于流程跟踪表展示）
     * 
     * @param processId 流程Id
     * @param processInsId 流程实例Id
     * 
     * @return NodeTrackInfo[] 流程实例处理跟踪
     */
    public NodeTrackInfo[] queryProcessInsTransTrack(String processId, String processInsId) {
        try {
            NodeTrackInfo[] nodeTrackInfos = ClientFactory.getTrackService().queryAllTrackByProcessInsId(processId,
                processInsId);
            List<NodeTrackInfo> lst = new ArrayList<NodeTrackInfo>();
            for (NodeTrackInfo nodeInfo : nodeTrackInfos) {
                if (BpmsConstant.TASK_TYPE_USERTASK.equals(nodeInfo.getCurNodeType())) {
                    lst.add(nodeInfo);
                }
            }
            return lst.toArray(new NodeTrackInfo[] {});
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException("查询流程实例处理跟踪出现异常：processId=" + processId + ",processInsId:" + processInsId, e);
        }
    }
    
    /***
     *  统计已部署的流程数量
     * @return 统计到目前已部署的流程数量（不同版本只算一个流程）
     */
     public int queryDeployeProcessCount(){
     	return queryDeployeProcessCount(null,null,null,false,null);
     }
     /***
      * 统计到目前各模块已部署的流程数量
      * @return 统计到目前各模块已部署的流程数量（不同版本只算一个流程）,Map<String--模块所对应的应用编号,统计结果值> 
      */
     public Map<String,Integer> queryDeployeProcessMap(){
     	return queryDeployeProcessMap(null, null, false);
     }
     
     /****
      *  按条件统计已部署的流程数量
       * 
      * @param dirCode 对应的目录编号（如果统计某个模块下的流程，这里传入模块的应用编码）,如果不传则表示查询所有数据
      * @param deployTimeStart 流程部署时间查询的开始值
      * @param deployTimeEnd 流程部署时间查询的结束值
      * @param countDifVer 不同版本流程是否计算版本，true：需要计算，false：不计算，默认不计算
      * @param deployerId 流程部署人ID，如果为空则表示不需要将此做为查询条件
      * @return 统计的数值
      */
      public int queryDeployeProcessCount(String dirCode,Date deployTimeStart,Date deployTimeEnd,boolean countDifVer,String deployerId){
     	 return bpmsDao.queryDeployeProcessCount(dirCode, deployTimeStart, deployTimeEnd, countDifVer,deployerId);
      }
      
      /****
       *  按条件统计已部署的流程数量
        * 
       * @param deployTimeStart 流程部署时间查询的开始值
       * @param deployTimeEnd 流程部署时间查询的结束值
       * @param countDifVer 不同版本流程是否计算版本，true：需要计算，false：不计算，默认不计算
       * @return 统计的数值, Map<String--模块所对应的应用编号,统计结果值> 
       */
      public Map<String,Integer> queryDeployeProcessMap(Date deployTimeStart,Date deployTimeEnd,boolean countDifVer){
     	 return bpmsDao.queryDeployeProcessMap(deployTimeStart, deployTimeEnd, countDifVer);
     			 
      }
}
