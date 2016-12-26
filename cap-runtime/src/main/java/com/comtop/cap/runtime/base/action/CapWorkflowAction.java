/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.bpms.client.ClientFactory;
import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.NodeTrackInfo;
import com.comtop.bpms.common.model.UserInfo;
import com.comtop.cap.runtime.base.appservice.CapWorkflowCoreService;
import com.comtop.cap.runtime.base.exception.CapWorkflowException;
import com.comtop.cap.runtime.base.facade.CapWorkflowFacade;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.base.model.CapWorkflowVO;
import com.comtop.cap.runtime.base.model.WorkflowNodeConfigVO;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cap.runtime.base.util.CapWorkflowObjectHelper;
import com.comtop.cap.runtime.base.util.SystemHelper;
import com.comtop.cap.runtime.base.util.WorkflowConstant;
import com.comtop.cap.runtime.base.util.WorkflowExpandAttrUtil;
import com.comtop.cap.runtime.bpmsext.CapWorkflowFacadeFactory;
import com.comtop.cap.runtime.bpmsext.model.CapWorkflowExtVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.JSON;
import com.comtop.top.core.base.exception.TopBaseException;
import com.comtop.top.core.jodd.AppContext;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * CAP代码生成Action基类
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2015-5-14 郑重
 */
@DwrProxy
public class CapWorkflowAction {
    
    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(CapWorkflowAction.class);
    
    /**
     * 通过CapWorkflowParam获取实体facade
     * 
     * 
     * @param workflowParam 流程参数
     * @return 流程facade
     */
    public static CapWorkflowFacade<CapWorkflowVO> getWorkflowFacade(CapWorkflowParam workflowParam) {
        if (workflowParam == null || workflowParam.getWorkflowFacadeName() == null) {
            return null;
        }
        return (CapWorkflowFacade<CapWorkflowVO>) BeanContextUtil.getBean(workflowParam.getWorkflowFacadeName());
    }
    
    /**
     * 查询第一个用户节点上的扩展属性
     * 
     * 
     * @param workflowParam 流程参数
     * @return 节点扩展属性配置对象
     */
    @RemoteMethod
    public WorkflowNodeConfigVO queryFirstUserNodeConfig(CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        Map<String, String> objMap = objWorkflowFacade.queryExtendAttrOfFirstNode(workflowParam.getWorkId());
        return convertExtendsAttrsMapToVO(objMap);
    }
    
    /**
     * 将扩展属性Map转换成节点扩展属性配置VO
     * 
     * 
     * @param objMap 扩展属性Map
     * @return 扩展属性配置VO
     */
    private WorkflowNodeConfigVO convertExtendsAttrsMapToVO(Map<String, String> objMap) {
        WorkflowNodeConfigVO objConfigVO = new WorkflowNodeConfigVO();
        objConfigVO.setExtendsAttrs(objMap);
        
        // 处理非CAP工作流定义的扩展属性将用户定义的扩展属性设置到CAP的objMap中
        WorkflowExpandAttrUtil.putExpandAttribute(objMap);
        
        for (String strExtendAtt : WorkflowNodeConfigVO.NODE_CONFIG_CONSTANTS) {
            if (objMap.containsKey(strExtendAtt)) {
                setNodeConfigVO(strExtendAtt, objConfigVO, objMap.get(strExtendAtt));
            }
        }
        return objConfigVO;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param strExtendAtt 扩展属性
     * @param objConfigVO 流程节点配置信息
     * @param value 值
     */
    private void setNodeConfigVO(String strExtendAtt, WorkflowNodeConfigVO objConfigVO, String value) {
        
        String strMethodName = "set" + String.valueOf(strExtendAtt.charAt(0)).toUpperCase() + strExtendAtt.substring(1);
        
        try {
            // viewSelectDept是否显示部门选择组件特殊处理 需要存在null的情况
            if ("viewSelectDept".equals(strExtendAtt)) {
                Method objMethod = WorkflowNodeConfigVO.class.getDeclaredMethod(strMethodName, Boolean.class);
                objMethod.invoke(objConfigVO, Boolean.valueOf("true".equals(value)));
            } else if ("smsColHide".equals(strExtendAtt) || "emailColHide".equals(strExtendAtt)) {// 短信与邮件逻辑需要相反处理
                Method objMethod = WorkflowNodeConfigVO.class.getDeclaredMethod(strMethodName, boolean.class);
                objMethod.invoke(objConfigVO, Boolean.valueOf(!"true".equals(value)).booleanValue());
            } else {
                Method objMethod = WorkflowNodeConfigVO.class.getDeclaredMethod(strMethodName, boolean.class);
                objMethod.invoke(objConfigVO, Boolean.valueOf("true".equals(value)).booleanValue());
            }
        } catch (SecurityException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * 查询单据当前停留节点的扩展属性配置VO
     * 
     * 
     * @param workflowParam 流程参数
     * @return 节点扩展属性配置对象
     */
    @RemoteMethod
    public WorkflowNodeConfigVO queryCurrentNodeConfig(CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        Map<String, String> objMap = objWorkflowFacade.queryExtendAttrByNodeId(workflowParam.getWorkId(), workflowParam.getCurrNodeId());
        return convertExtendsAttrsMapToVO(objMap);
    }
    
    /**
     * 查询单据下一节点集合
     * 
     * 
     * @param workflowParam 流程参数
     * @return 下一节点集合
     */
    @RemoteMethod
    public List queryForeNode(CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        
        workflowParam = CapWorkflowObjectHelper.tansJsonString(workflowParam);
        
        NodeInfo[] objNodeInfos = objWorkflowFacade.queryForeTransNodes(workflowParam.getWorkId(), workflowParam.getTaskId(), workflowParam.getCurrentUserId(), workflowParam.getParamMap());
        
        return (List) JSON.toJSON(objNodeInfos);
        
    }
    
    /**
     * 分页查询节点上人员
     * 
     * 
     * @param workflowParam 工作流参数对象
     * @param version 流程版本
     * @param nodeId 指定节点Id
     * @param filterWord 人员过滤关键字
     * @return 用户节点集合
     */
    @RemoteMethod
    public List queryUserListByNode(CapWorkflowParam workflowParam, int version, String nodeId, String filterWord) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        workflowParam = CapWorkflowObjectHelper.tansJsonString(workflowParam);
        UserInfo[] objUserInfos = objWorkflowFacade.queryUserListByNode(workflowParam, version, nodeId, filterWord);
        return (List) JSON.toJSON(objUserInfos);
    }
    
    /**
     * 查询单据回退节点集合
     * 
     * 
     * @param workflowParam 流程参数
     * @return 下一节点集合
     */
    @RemoteMethod
    public List queryBackNode(CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        NodeInfo[] nodeInfos = objWorkflowFacade.queryBackTransNodes(workflowParam.getWorkId(), workflowParam.getTaskId(), workflowParam.getCurrentUserId());
        // return WorkflowObjectHelper.transNodeArrayToDwrObj(nodeInfos);
        return (List) JSON.toJSON(nodeInfos);
    }
    
    /**
     * 查询单据转发节点集合
     * 
     * 
     * @param workflowParam 流程参数
     * @return 下一节点集合
     */
    @RemoteMethod
    public List queryReassignNode(CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        workflowParam = CapWorkflowObjectHelper.tansJsonString(workflowParam);
        NodeInfo[] nodeInfos = objWorkflowFacade.queryReassignTransNodes(workflowParam.getWorkId(), workflowParam.getTaskId(), workflowParam.getCurrentUserId(), workflowParam.getParamMap());
        
        return (List) JSON.toJSON(nodeInfos);
    }
    
    /**
     * 获取配置过滤函数中变量对应的信息
     * 
     * @param workflowParam 流程参数
     * @return 用户信息
     */
    @RemoteMethod
    public Map getDeptFilerParam(CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            return null;
        }
        // workflowParam = CapWorkflowObjectHelper.tansJsonString(workflowParam);
        UserInfo objUserInfo = objWorkflowFacade.getDeptFilerParam(workflowParam.getWorkId(), workflowParam.getParamKey());
        
        return (Map) JSON.toJSON(objUserInfo);
    }
    
    /**
     * 提交流程审批数据
     * 
     * @param workflowParams 流程参数
     * @return 操作结果集
     */
    @RemoteMethod
    public Map<String, Object> submit(CapWorkflowParam[] workflowParams) {
        int success = 0;// 成功数
        int error = 0; // 失败数
        StringBuffer message = new StringBuffer();// 详细信息
        
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParams[0]);
        if (objWorkflowFacade == null) {
            throw new CapWorkflowException("无法注入CapWorkflowFacade...");
        }
        NodeInfo[] nodeInfos = CapWorkflowObjectHelper.tansJsonToBpmsNode(workflowParams[0].getTagNodeInfoString());
        HashMap<String, Object> objParamMap = null;
        for (CapWorkflowParam workflowParam : workflowParams) {
            workflowParam.setTargetNodeInfos(nodeInfos);
            objParamMap = CapWorkflowObjectHelper.tansJsonToBpmsParamMap(workflowParam.getParamMapString());
            workflowParam.setParamMap(objParamMap);
            try {
                // 调用工作流上报,发送,回退等转发操作
                invokeFacade(objWorkflowFacade, workflowParam);
                success++;
            } catch (Exception ex) {
                if (ex instanceof TopBaseException) {
                    ex = (Exception) ex.getCause();
                }
                // 处理反射的invoke异常因为该异常会丢失详细堆栈信息(循环查找堆栈是否存在InvocationTargetException)
                while (ex instanceof InvocationTargetException) {
                    Throwable throwable = ((InvocationTargetException) ex).getTargetException();
                    if (throwable.getCause() == null) {
                        ex = (Exception) throwable;
                        break;
                    }
                    ex = (Exception) throwable.getCause();
                }
                error++;
                message.append(ex.getMessage() + " <br>");
            }
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("successes", success);
        map.put("errors", error);
        map.put("totalCounts", workflowParams.length);
        map.put("message", message.toString());
        return map;
    }
    
    /**
     * 批量处理流程审批数据
     * 
     * @param workflowParams 流程参数
     * @return 返回批量操作结果
     */
    @RemoteMethod
    public boolean batchSubmit(CapWorkflowParam[] workflowParams) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParams[0]);
        executeBatchSubmit(objWorkflowFacade, workflowParams);
        return true;
    }
    
    /**
     * 查询流程实例处理跟踪（用于流程跟踪表展示）
     * 
     * @param workflowParam
     *            流程参数
     * @return NodeTrackInfo[] 流程实例处理跟踪
     */
    @RemoteMethod
    public String queryProcessInsTransTrack(CapWorkflowParam workflowParam) {
        
        if (StringUtil.isEmpty(workflowParam.getProcessInsId()))
            return null;
        
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        NodeTrackInfo[] nodeTrackInfos = objWorkflowFacade.queryProcessInsTransTrack(workflowParam);
        return JSON.toJSONString(nodeTrackInfos);
    }
    
    /**
     * 查询流程实例处理跟踪（用于流程跟踪表展示）
     * 
     * @param processId 工作流流程id
     * @param processInsId 流程实例id
     * @return nodeTrackInfos
     */
    @RemoteMethod
    public String queryProcessInsTransTrackById(String processId, String processInsId) {
        if (StringUtil.isEmpty(processId) || StringUtil.isEmpty(processInsId))
            return null;
        
        CapWorkflowCoreService core = AppContext.getBean(CapWorkflowCoreService.class);
        NodeTrackInfo[] nodeTrackInfos = core.queryProcessInsTransTrack(processId, processInsId);
        
        return JSON.toJSONString(nodeTrackInfos);
    }
    
    /**
     * 查询已办任务ID或待办任务ID
     * 
     * @param workflowParam
     *            工作流参数
     * @param activityInsId 活动实例ID
     * @param taskType 任务类型
     * @return 询已办任务ID
     */
    @RemoteMethod
    public String queryNewTaskId(CapWorkflowParam workflowParam, String activityInsId, String taskType) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        return objWorkflowFacade.queryNewTaskId(workflowParam, activityInsId, taskType);
    }
    
    /**
     * 查询待办和已办的任务信息
     * 
     * @param processId 流程id
     * @param processInsId 流程实例id
     * 
     * @return 任务类型
     */
    @RemoteMethod
    public Map<String, Object> queryTaskVOByProcessInsId(String processId, String processInsId) {
        if (StringUtil.isEmpty(processId) || StringUtil.isEmpty(processInsId))
            return null;
        
        CapWorkflowExtVO workflowVO = new CapWorkflowExtVO();
        workflowVO.setProcessId(processId);
        workflowVO.setProcessInsId(processInsId);
        String curUserId = SystemHelper.getUserId();
        workflowVO.setTransActor(curUserId);
        
        return CapWorkflowFacadeFactory.getInstance().queryTaskVOByProcessInsId(workflowVO);
    }
    
    /**
     * 查询当前用户在指定流程、流程实例下的最后一个已办数据，
     * 
     * @param processId
     *            流程id
     * @param processInsId
     *            流程实例id
     * 
     * @return 当前用户在指定流程、流程实例下的最后一个已办数据
     */
    @RemoteMethod
    public CapWorkflowVO queryLastDoneTaskByProcessInsId(String processId, String processInsId) {
        if (StringUtil.isEmpty(processId) || StringUtil.isEmpty(processInsId))
            return null;
        
        CapWorkflowExtVO workflowVO = new CapWorkflowExtVO();
        workflowVO.setProcessId(processId);
        workflowVO.setProcessInsId(processInsId);
        
        List<CapWorkflowVO> lst = CapWorkflowFacadeFactory.getInstance().queryDoneTaskVOByProcessInsId(workflowVO);
        
        if (lst != null && lst.size() > 0) {
            return lst.get(0);
        }
        
        return null;
    }
    
    /**
     * 获取指定版本流程所有用户节点信息,如果传递了当前节点则查该节点之后的
     * 
     * @param workflowParam CapWorkflowParam
     * @param paramMap paramMap
     * @return List
     */
    @RemoteMethod
    public String queryAllUserNodesByVersion(CapWorkflowParam workflowParam, Map<String, String> paramMap) {
        try {
            List<NodeInfo> lst = ClientFactory.getUserTaskService()
                .queryAllUserNodesByVersion(workflowParam.getCurrNodeId(), workflowParam.getProcessId(), workflowParam.getProcessVersion(), paramMap);
            
            return JSON.toJSONString(lst);
        } catch (AbstractBpmsException e) {
            throw new CapWorkflowException("查询流程实例处理跟踪出现异常：curNodeId=" + workflowParam.getCurrNodeId() + ",processId:" + workflowParam.getProcessId() + ",version:" + workflowParam.getProcessVersion()
                + ",varMap:" + paramMap, e);
        }
    }
    
    /**
     * 执行批量提交方法
     * 
     * @param objWorkflowFacade 流程Facade
     * 
     * 
     * @param workflowParams 流程参数集合
     */
    private void executeBatchSubmit(CapWorkflowFacade<?> objWorkflowFacade, CapWorkflowParam[] workflowParams) {
        
        NodeInfo[] nodeInfos = CapWorkflowObjectHelper.tansJsonToBpmsNode(workflowParams[0].getTagNodeInfoString());
        HashMap<String, Object> objParamMap = null;
        for (CapWorkflowParam workflowParam : workflowParams) {
            workflowParam.setTargetNodeInfos(nodeInfos);
            objParamMap = CapWorkflowObjectHelper.tansJsonToBpmsParamMap(workflowParam.getParamMapString());
            workflowParam.setParamMap(objParamMap);
        }
        executeBatch(objWorkflowFacade, workflowParams);
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param objWorkflowFacade 流程Facade
     * @param workflowParams 流程配置参数
     */
    private void executeBatch(CapWorkflowFacade<?> objWorkflowFacade, CapWorkflowParam[] workflowParams) {
        int iOperateType = workflowParams[0].getOperateType();
        List<CapWorkflowParam> lstParams = Arrays.asList(workflowParams);
        switch (iOperateType) {
            case WorkflowConstant.WORK_FLOW_OPERATE_REPORT:
                /** 工作流操作：上报 ,1 */
                objWorkflowFacade.batchEntry(lstParams);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_SEND:
                /** 工作流操作：发送,2 */
                objWorkflowFacade.batchFore(lstParams);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_BACK:
                /** 工作流操作：回退,3 */
                objWorkflowFacade.batchBack(lstParams);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_REASSIGN:
                /** 工作流操作：转发,4 */
                objWorkflowFacade.batchReassign(lstParams);
                break;
            default:
                break;
        }
    }
    
    /***
     * 调用工作流上报,发送,回退等转发操作
     * 
     * @param objWorkflowFacade
     *            流程Facade
     * @param workflowParam
     *            流程配置参数
     */
    private void invokeFacade(CapWorkflowFacade<?> objWorkflowFacade, CapWorkflowParam workflowParam) {
        int iOperateType = workflowParam.getOperateType();
        switch (iOperateType) {
            case WorkflowConstant.WORK_FLOW_OPERATE_REPORT:
                /** 工作流操作：上报 ,1 */
                objWorkflowFacade.fore(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_SEND:
                /** 工作流操作：发送,2 */
                objWorkflowFacade.fore(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_BACK:
                /** 工作流操作：回退,3 */
                objWorkflowFacade.back(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_REASSIGN:
                /** 工作流操作：转发,4 */
                objWorkflowFacade.reassign(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_ABORT:
                /** 工作流操作：终结,5 */
                objWorkflowFacade.abort(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_HUNGUP:
                /** 工作流操作：挂起,6 */
                objWorkflowFacade.hungup(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_UNDO:
                /** 工作流操作：撤回,7 */
                objWorkflowFacade.undo(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_COPYTO:
                /** 工作流操作：抄送,8 */
                // TODO objWorkflowFacade.backEntry(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_BACKREPORT:
                /** 工作流操作：回退申请人,9 */
                objWorkflowFacade.backEntry(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_UNDOREPORT:
                /** 工作流操作：撤回已结束流程并回退申请人,10 */
                objWorkflowFacade.backEntry4FlowOver(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_SAVENOTE:
                /** 工作流操作：保存意见,11 */
                objWorkflowFacade.saveNote(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_OVERFLOW:
                /** 工作流操作：结束流程,12 */
                objWorkflowFacade.overFlow(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_JUMP:
                /** 工作流操作：流程跳转（流程回退后再次发送可跳转到之前走过的流程节点）,13 */
                objWorkflowFacade.jump(workflowParam);
                break;
            case WorkflowConstant.WORK_FLOW_OPERATE_RECOVER:
                /** 工作流操作：流程恢复,14 */
                objWorkflowFacade.recover(workflowParam);
                break;
            default:
                break;
        }
    }
    
    /**
     * 调用WorklfowFacadeMethod
     * 
     * @param methodName 方法名称
     * @param workflowParam 流程参数
     * @return json 字符串
     */
    @RemoteMethod
    public String invokeWorklfowFacadeMethod(String methodName, CapWorkflowParam workflowParam) {
        CapWorkflowFacade<?> objWorkflowFacade = getWorkflowFacade(workflowParam);
        if (objWorkflowFacade == null) {
            throw new CapWorkflowException("根据CapWorkflowParam类中的getWorkflowFacadeName()未找到workflowfacade对象");
        }
        workflowParam = CapWorkflowObjectHelper.tansJsonString(workflowParam);
        Method objMethod;
        try {
            objMethod = objWorkflowFacade.getClass().getMethod(methodName, CapWorkflowParam.class);
            Object result = objMethod.invoke(objWorkflowFacade, workflowParam);
            if (result != null) {
                return JSON.toJSONString(result);
            }
        } catch (SecurityException e) {
            LOGGER.error("invokeWorklfowFacadeMethod方法调用反射失败:" + e.getMessage(), e);
            throw new CapWorkflowException(objWorkflowFacade + "中方法" + methodName + "不存在", e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("invokeWorklfowFacadeMethod方法调用反射失败:" + e.getMessage(), e);
            throw new CapWorkflowException(objWorkflowFacade + "中方法" + methodName + "不存在", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("invokeWorklfowFacadeMethod方法调用反射失败:" + e.getMessage(), e);
            throw new CapWorkflowException(objWorkflowFacade + "中方法" + methodName + "调用失败", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("invokeWorklfowFacadeMethod方法调用反射失败:" + e.getMessage(), e);
            throw new CapWorkflowException(objWorkflowFacade + "中方法" + methodName + "调用失败", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("invokeWorklfowFacadeMethod方法调用反射失败:" + e.getMessage(), e);
            throw new CapWorkflowException(objWorkflowFacade + "中方法" + methodName + "调用失败", e);
        }
        return "{}";
    }
    
}
