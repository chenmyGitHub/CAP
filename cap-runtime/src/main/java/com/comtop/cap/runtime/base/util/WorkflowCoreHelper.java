/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.comtop.bpms.client.ClientFactory;
import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.bpms.common.model.DoneTaskInfo;
import com.comtop.bpms.common.model.HumanNodeInfo;
import com.comtop.bpms.common.model.NodeDefinitionInfo;
import com.comtop.bpms.common.model.NodeExtendAttributeInfo;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.NodeTodoTaskCntInfo;
import com.comtop.bpms.common.model.ProcessInfo;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.bpms.common.model.TodoTaskInfo;
import com.comtop.bpms.common.model.UserInfo;
import com.comtop.cap.runtime.base.exception.CapWorkflowException;

/**
 * 工作流接口帮助类（用于非工作流情况下查询工作流内容使用）
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-12 李忠文
 */
public final class WorkflowCoreHelper {
    
    /**
     * 私有构造函数，避免通过new的方式实例化
     */
    private WorkflowCoreHelper() {
    }
    
    /**
     * 查询指定流程、指定版本中所有设置了指定扩展属性，或指定扩展属性值为指定值的相关节点集合
     * 
     * @param processId 流程编码
     * @param attrKey 扩展属性key
     * @return 节点信息集合
     * @throws CapWorkflowException 异常
     */
    public static NodeDefinitionInfo[] queryNodeExtAttsByCondition(String processId, String attrKey)
        throws CapWorkflowException {
        try {
            return ClientFactory.getDefinitionQueryService().queryNodeExtAttsByCondition(processId, 0, attrKey, null);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取最新版本流程下面的所有用户任务节点
     * 
     * @param processId 流程编码
     * @return 节点信息
     * @throws CapWorkflowException 异常
     */
    public static NodeDefinitionInfo[] queryUserTaskNodeInfo(String processId) throws CapWorkflowException {
        try {
            return ClientFactory.getDefinitionQueryService().queryNodeDefinitionInfo(processId, 0, null, null,
                "USERTASK", null);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程编号获取最新版本的流程
     * 
     * @param processId 流程编号
     * @return 流程信息
     * @throws CapWorkflowException 异常
     */
    public static ProcessInfo queryLastProcessInfoById(String processId) throws CapWorkflowException {
        try {
            return ClientFactory.getDefinitionQueryService().readLastProcessInfoById(processId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取节点上的所有扩展属性
     * 
     * @param processId 工作流编码
     * @param version 版本
     * @param nodeId 节点ID
     * @return 扩展属性对象
     * @throws CapWorkflowException 工作流异常
     */
    public static NodeExtendAttributeInfo[] queryNodeExtendAttributes(String processId, int version, String nodeId)
        throws CapWorkflowException {
        try {
            return ClientFactory.getDefinitionQueryService().queryNodeExtendAttributes(processId, version, nodeId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取指定版本的流程节点的扩展属性值
     * 
     * @param processId 工作流编码
     * @param nodeId 节点ID
     * @param version 流程版本号
     * @param extendAttributeKey 扩展属性KEY
     * @return 扩展属性的值
     * @throws CapWorkflowException 工作流异常
     */
    public static String queryNodeExtendAttributeValueByKey(String processId, int version, String nodeId,
        String extendAttributeKey) throws CapWorkflowException {
        try {
            return ClientFactory.getDefinitionQueryService().readNodeExtendAttributeValueByKey(processId, version,
                nodeId, extendAttributeKey);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取指定节点上的用户
     * 
     * @param processId 流程编码
     * @param nodeId 节点编号
     * @param version 当前流程版本
     * @return 节点信息
     * @throws CapWorkflowException 异常
     */
    public static UserInfo[] queryUsersByProcessIdNodeIdAndVersion(String processId, String nodeId, int version)
        throws CapWorkflowException {
        try {
            return ClientFactory.getHumanNodeService()
                .queryUsersByProcessIdNodeIdAndVersion(processId, nodeId, version);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取已办记录
     * 
     * @param processId 流程编码
     * @param todoTaskId 已办id
     * @return 已办记录
     * @throws CapWorkflowException 工作流异常
     */
    public static DoneTaskInfo queryDoneTask(String processId, String todoTaskId) throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().readDoneTask(processId, todoTaskId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流实例ID，查询流程实例当前停留节点
     * 
     * @param processId 流程编号
     * @param processInsId 流程实例id
     * @return 节点信息
     * @throws CapWorkflowException 工作流异常
     */
    public static NodeInfo[] queryWatingNodes(String processId, String processInsId) throws CapWorkflowException {
        try {
            return ClientFactory.getTrackService().queryWatingNodeByProcessInsId(processId, processInsId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询所有最新版本的流程信息
     * 
     * @return 流程信息
     * @throws CapWorkflowException 工作流异常
     */
    public static ProcessInfo[] queryLastestVersionProcesses() throws CapWorkflowException {
        try {
            return ClientFactory.getDefinitionQueryService().queryLastestVersionProcesses();
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询用户参与的待办节点
     * 
     * @param processId 流程编码
     * @param userId 用户id
     * @return 待办节点的集合
     * @throws CapWorkflowException 工作流异常
     */
    public static List<NodeInfo> queryTodoTransNode(String processId, String userId) throws CapWorkflowException {
        List<NodeInfo> objNodeInfos = new ArrayList<NodeInfo>();
        try {
            List<NodeTodoTaskCntInfo> objNodeTodoTasks = ClientFactory.getUserTaskService()
                .queryTodoTaskCountByUserAndProId(processId, userId);
            NodeInfo[] objFirstNodes = ClientFactory.getUserTaskService().queryFirstUserNodes(processId, null);
            if (objNodeTodoTasks != null && !objNodeTodoTasks.isEmpty() && objFirstNodes != null
                && objFirstNodes.length > 0) {
                for (NodeTodoTaskCntInfo objNodeTodoTask : objNodeTodoTasks) {
                    boolean isFirstNode = false;
                    for (NodeInfo node : objFirstNodes) {
                        if (objNodeTodoTask.getNodeId().equals(node.getNodeId())) {
                            isFirstNode = true;
                            break;
                        }
                    }
                    if (!isFirstNode) {
                        NodeInfo objNodeInfo = new NodeInfo();
                        objNodeInfo.setNodeId(objNodeTodoTask.getNodeId());
                        objNodeInfo.setNodeName(objNodeTodoTask.getNodeName());
                        objNodeInfos.add(objNodeInfo);
                    }
                }
            }
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
        return objNodeInfos;
    }
    
    /**
     * 查询用户参与的待办节点,过滤上报节点
     * 
     * @param processId 流程编码
     * @param userId 用户id
     * @return 待办节点的集合
     */
    public static List<NodeTodoTaskCntInfo> queryTodoTaskCount(String processId, String userId) {
        List<NodeTodoTaskCntInfo> objNodeTodos = new ArrayList<NodeTodoTaskCntInfo>();
        try {
            List<NodeTodoTaskCntInfo> objNodeTodoTasks = ClientFactory.getUserTaskService()
                .queryTodoTaskCountByUserAndProId(processId, userId);
            NodeInfo[] objFirstNodes = ClientFactory.getUserTaskService().queryFirstUserNodes(processId, null);
            if (objNodeTodoTasks != null && !objNodeTodoTasks.isEmpty() && objFirstNodes != null
                && objFirstNodes.length > 0) {
                for (NodeTodoTaskCntInfo objNodeTodoTask : objNodeTodoTasks) {
                    boolean isFirstNode = false;
                    for (NodeInfo node : objFirstNodes) {
                        if (objNodeTodoTask.getNodeId().equals(node.getNodeId())) {
                            isFirstNode = true;
                            break;
                        }
                    }
                    if (!isFirstNode) {
                        objNodeTodos.add(objNodeTodoTask);
                    }
                }
            }
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
        return objNodeTodos;
    }
    
    /**
     * 根据 工作流编码、其他组合条件 查询已办的任务列表信息
     * 
     * @param processId 流程编码
     * @param doneTaskInfo 已办信息
     * @return 办的任务列表信息集合(没有返回NULL)
     * @throws CapWorkflowException 工作流异常
     */
    public static List<DoneTaskInfo> queryDoneTasks(String processId, DoneTaskInfo doneTaskInfo)
        throws CapWorkflowException {
        try {
            doneTaskInfo.setMainProcessId(processId);
            return ClientFactory.getUserTaskService().queryDoneTaskByQueryCondition(processId, doneTaskInfo);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程编码查询第一批用户节点
     * 
     * @param processId 流程编码
     * @param paramsMap 参数
     * @return 第一批用户节点
     * @throws CapWorkflowException 工作流异常
     */
    public static NodeInfo[] queryFirstUserNodes(String processId, Map<String, Object> paramsMap)
        throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().queryFirstUserNodes(processId, paramsMap);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 查询 流程实例信息
     * 
     * @param processId 工作流编码
     * @param processInsId 流程实例ID
     * @return 流程实例信息对象
     * @throws CapWorkflowException 工作流异常
     */
    public static ProcessInstanceInfo queryProcessInsInfo(String processId, String processInsId)
        throws CapWorkflowException {
        try {
            return ClientFactory.getProcessInstanceService().queryProcessInsInfo(processId, processInsId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据 工作流编码、其他组合条件 查询待办的任务列表信息
     * 
     * @param processId 流程编码
     * @param todoTaskInfo 待办信息
     * @return 办的任务列表信息集合(没有返回NULL)
     * @throws CapWorkflowException 工作流异常
     */
    public static List<TodoTaskInfo> queryTodoTasks(String processId, TodoTaskInfo todoTaskInfo)
        throws CapWorkflowException {
        // 查询指定流程下 某个人的待办任务列表
        try {
            todoTaskInfo.setMainProcessId(processId);
            return ClientFactory.getUserTaskService().queryTodoTaskByQueryCondition(processId, todoTaskInfo);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取待办记录
     * 
     * @param processId 流程编码
     * @param todoTaskId 待办id
     * @return 待办记录
     * @throws CapWorkflowException 工作流异常
     */
    public static TodoTaskInfo queryTodoTask(String processId, String todoTaskId) throws CapWorkflowException {
        try {
            return ClientFactory.getUserTaskService().readTodoTask(processId, todoTaskId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程ID查询已办任务分表名
     * 
     * @param processId - 流程编号,必须输入
     * 
     * @return 根据流程ID查询已办任务分表名
     */
    public static String readDoneTaskTableName(String processId) {
        try {
            return ClientFactory.getBPMSConfigService().readDoneTaskTableName(processId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 根据流程ID查询待办任务分表名
     * 
     * @param processId - 流程编号,必须输入
     * 
     * @return 根据流程ID查询待办任务分表名
     */
    public static String readToDoTaskTableName(String processId) {
        try {
            return ClientFactory.getBPMSConfigService().readToDoTaskTableName(processId);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 
     * 查询用户集合所参与的指定流程上的节点信息，在指定流程的最新版本上（取并集）
     * 
     * @param processId 流程ID
     * @param userIds 指定的用户ID集
     * @return 指定的用户集在指定的流程最新版本上所参数的节点集合（取并集）
     */
    public static HumanNodeInfo[] queryIntersecHumanNodesByUserIds(String processId, String[] userIds) {
        try {
            HumanNodeInfo[] objNodes = ClientFactory.getHumanNodeService().queryUnionHumanNodesByUserIdsInLastVersion(
                "", processId, userIds, null, 1, 50);
            return objNodes;
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取工作流数据表
     * 
     * @param processId 流程ID
     * @param isDone 是否为已完成
     * @return 工作流数据表
     */
    public static String readTaskTableName(String processId, boolean isDone) {
        String strTableName;
        try {
            if (isDone) {
                strTableName = ClientFactory.getBPMSConfigService().readDoneTaskTableName(processId);
            } else {
                strTableName = ClientFactory.getBPMSConfigService().readToDoTaskTableName(processId);
            }
            return strTableName;
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
    /**
     * 获取工作流节点跟踪信息表
     * 
     * @param processId 流程ID
     * @param isNodeTrack 是否是跟踪信息表
     * @return 工作流节点跟踪信息表
     */
    public static String readRuNodeOrTransTrackTableName(String processId, boolean isNodeTrack) {
        String strTableName;
        try {
            if (isNodeTrack) {
                strTableName = ClientFactory.getBPMSConfigService().readRuNodeTrackName(processId);
            } else {
                strTableName = ClientFactory.getBPMSConfigService().readRuTransTrackName(processId);
            }
            return strTableName;
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException(e);
        }
    }
    
}
