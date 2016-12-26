/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.NodeTrackInfo;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.bpms.common.model.UserInfo;
import com.comtop.cap.runtime.base.appservice.CapWorkflowAppService;
import com.comtop.cap.runtime.base.model.BpmsNodeInfo;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.base.model.CapWorkflowVO;

/**
 * 
 * 工作流facade
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月24日 龚斌
 * @param <VO> 继承WorkflowVO
 */
public abstract class CapWorkflowFacade<VO extends CapWorkflowVO> extends CapBaseFacade<VO> {
    
    /**
     * 获取工作流AppService
     * 
     * @return 工作流AppService
     */
    protected CapWorkflowAppService<VO> getWorkflowAppService() {
        return (CapWorkflowAppService) getAppService();
        
    }
    
    /**
     * 查询待处理VO列表
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public int queryTodoVOCount(VO vo) {
        return getWorkflowAppService().queryTodoVOCount(vo);
    }
    
    /**
     * 查询待处理VO列表
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public List<VO> queryTodoVOList(VO vo) {
        return getWorkflowAppService().queryTodoVOList(vo);
    }
    
    /**
     * 查询综合信息VO列表
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public int queryComprehensiveVOCount(VO vo) {
        return getWorkflowAppService().queryComprehensiveVOCount(vo);
    }
    
    /**
     * 查询综合信息VO列表
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public List<VO> queryComprehensiveVOList(VO vo) {
        return getWorkflowAppService().queryComprehensiveVOList(vo);
    }
    
    /**
     * 查询待处理VO列表
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public int queryToEntryVOCount(VO vo) {
        return getWorkflowAppService().queryToEntryVOCount(vo);
    }
    
    /**
     * 查询待处理VO列表
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public List<VO> queryToEntryVOList(VO vo) {
        return getWorkflowAppService().queryToEntryVOList(vo);
    }
    
    /**
     * 根据voId查询业务待办VO对象
     * 
     * @param voId vo主键Id
     * @param taskId 任务Id
     * @param taskType 任务类型：todo待办 done已办
     * @return 业务对象及任务信息VO
     */
    public VO queryObjectAndTaskVOById(String voId, String taskId, String taskType) {
        return getWorkflowAppService().queryObjectAndTaskVOById(voId, taskId, taskType);
    }
    
    /**
     * 查询待办信息根据taskId
     * 
     * @param voId 工单id
     * @param taskId 任务id
     * @return 待办VO
     */
    public VO queryTodoVOByTaskId(String voId, String taskId) {
        return getWorkflowAppService().queryTodoVOByTaskId(voId, taskId);
    }
    
    /**
     * 查询已办信息根据taskId
     * 
     * @param voId 工单id
     * @param taskId 任务id
     * @return 已办VO
     */
    public VO queryDoneVOByTaskId(String voId, String taskId) {
        return getWorkflowAppService().queryDoneVOByTaskId(voId, taskId);
    }
    
    /**
     * 查询待处理 VO
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public int queryEntryVOCount(VO vo) {
        return getWorkflowAppService().queryEntryVOCount(vo);
    }
    
    /**
     * 查询待处理 VO
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public List<VO> queryEntryVOList(VO vo) {
        return getWorkflowAppService().queryEntryVOList(vo);
    }
    
    /**
     * 查询待处理VO
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public int queryDoneVOCount(VO vo) {
        return getWorkflowAppService().queryDoneVOCount(vo);
    }
    
    /**
     * 查询待处理 VO
     * 
     * @param vo 查询条件
     * @return 待处理VO数据
     */
    public List<VO> queryDoneVOList(VO vo) {
        return getWorkflowAppService().queryDoneVOList(vo);
    }
    
    /**
     * 查询待处理 VO列表，分页查询--新版设计器使用
     * 
     * @param vo 查询条件
     * @return 待处理 VO列表
     */
    public Map<String, Object> queryTodoVOListByPage(VO vo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = getWorkflowAppService().queryTodoVOCount(vo);
        List<VO> voList = null;
        if (count > 0) {
            vo.setPageNo(getRightPageNo(count, vo.getPageNo(), vo.getPageSize()));
            voList = getWorkflowAppService().queryTodoVOList(vo);
        }
        ret.put("list", voList);
        ret.put("count", count);
        ret.put("pageNo", vo.getPageNo());
        return ret;
    }
    
    /**
     * 查询待上报 VO列表，分页查询--新版设计器使用
     * 
     * @param vo 查询条件
     * @return 待上报 VO列表
     */
    public Map<String, Object> queryToEntryVOListByPage(VO vo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = getWorkflowAppService().queryToEntryVOCount(vo);
        List<VO> voList = null;
        if (count > 0) {
            vo.setPageNo(getRightPageNo(count, vo.getPageNo(), vo.getPageSize()));
            voList = getWorkflowAppService().queryToEntryVOList(vo);
        }
        ret.put("list", voList);
        ret.put("count", count);
        ret.put("pageNo", vo.getPageNo());
        return ret;
    }
    
    /**
     * 综合查询，分页查询--新版设计器使用
     * 
     * @param vo 查询条件
     * @return VO列表
     */
    public Map<String, Object> queryComprehensiveVOListByPage(VO vo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = getWorkflowAppService().queryComprehensiveVOCount(vo);
        List<VO> voList = null;
        if (count > 0) {
            vo.setPageNo(getRightPageNo(count, vo.getPageNo(), vo.getPageSize()));
            voList = getWorkflowAppService().queryComprehensiveVOList(vo);
        }
        ret.put("list", voList);
        ret.put("count", count);
        ret.put("pageNo", vo.getPageNo());
        return ret;
    }
    
    /**
     * 查询已上报 VO列表，分页查询--新版设计器使用
     * 
     * @param vo 查询条件
     * @return 已上报 VO列表
     */
    public Map<String, Object> queryEntryVOListByPage(VO vo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = getWorkflowAppService().queryEntryVOCount(vo);
        List<VO> voList = null;
        if (count > 0) {
            vo.setPageNo(getRightPageNo(count, vo.getPageNo(), vo.getPageSize()));
            voList = getWorkflowAppService().queryEntryVOList(vo);
        }
        ret.put("list", voList);
        ret.put("count", count);
        ret.put("pageNo", vo.getPageNo());
        return ret;
    }
    
    /**
     * 查询已处理 VO列表，分页查询--新版设计器使用
     * 
     * @param vo 查询条件
     * @return 已处理 VO列表
     */
    public Map<String, Object> queryDoneVOListByPage(VO vo) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = getWorkflowAppService().queryDoneVOCount(vo);
        List<VO> voList = null;
        if (count > 0) {
            vo.setPageNo(getRightPageNo(count, vo.getPageNo(), vo.getPageSize()));
            voList = getWorkflowAppService().queryDoneVOList(vo);
        }
        ret.put("list", voList);
        ret.put("count", count);
        ret.put("pageNo", vo.getPageNo());
        return ret;
    }
    
    /**
     * 批量上报
     * 
     * @param workflowParamList 节点信息
     */
    public void batchEntry(List<CapWorkflowParam> workflowParamList) {
        getWorkflowAppService().batchEntry(workflowParamList, 0);
    }
    
    /**
     * 批量发送（包含上报）
     * 
     * @param workflowParamList 节点信息
     */
    public void batchFore(List<CapWorkflowParam> workflowParamList) {
        getWorkflowAppService().batchFore(workflowParamList);
    }
    
    /**
     * 批量回退
     * 
     * @param workflowParamList 节点信息
     */
    public void batchBack(List<CapWorkflowParam> workflowParamList) {
        getWorkflowAppService().batchBack(workflowParamList);
    }
    
    /**
     * 批量转发
     * 
     * @param workflowParamList 节点信息
     */
    public void batchReassign(List<CapWorkflowParam> workflowParamList) {
        getWorkflowAppService().batchReassign(workflowParamList);
    }
    
    /**
     * 发送(包含上报)
     * 
     * @param workflowParam 节点信息
     */
    public void fore(CapWorkflowParam workflowParam) {
        getWorkflowAppService().fore(workflowParam);
    }
    
    /**
     * 回退
     * 
     * @param workflowParam 节点信息
     */
    public void back(CapWorkflowParam workflowParam) {
        getWorkflowAppService().back(workflowParam);
    }
    
    /**
     * 回退到上报节点
     * 
     * @param workflowParam 节点信息
     */
    public void backEntry(CapWorkflowParam workflowParam) {
        getWorkflowAppService().backEntry(workflowParam);
    }
    
    /**
     * 流程结束，回退到上报节点
     * 
     * @param workflowParam 节点信息
     */
    public void backEntry4FlowOver(CapWorkflowParam workflowParam) {
        getWorkflowAppService().backEntry4FlowOver(workflowParam);
    }
    
    /**
     * 撤回
     * 
     * @param workflowParam 节点信息
     */
    public void undo(CapWorkflowParam workflowParam) {
        getWorkflowAppService().undo(workflowParam);
    }
    
    /**
     * 转发
     * 
     * @param workflowParam 节点信息
     */
    public void reassign(CapWorkflowParam workflowParam) {
        getWorkflowAppService().reassign(workflowParam);
    }
    
    /**
     * 终结
     * 
     * @param workflowParam 节点信息
     */
    public void abort(CapWorkflowParam workflowParam) {
        getWorkflowAppService().abort(workflowParam);
    }
    
    /**
     * 挂起
     * 
     * @param workflowParam 节点信息
     */
    public void hungup(CapWorkflowParam workflowParam) {
        getWorkflowAppService().hungup(workflowParam);
    }
    
    /**
     * 保存意见
     * 
     * @param workflowParam 节点信息
     * @return boolean
     */
    public boolean saveNote(CapWorkflowParam workflowParam) {
        return getWorkflowAppService().saveNote(workflowParam);
    }
    
    /**
     * 流程恢复
     * 
     * @param workflowParam 节点信息
     */
    public void recover(CapWorkflowParam workflowParam) {
        getWorkflowAppService().recover(workflowParam);
    }
    
    /**
     * 正常结束流程
     * 
     * @param workflowParam
     *            节点信息
     * @return ProcessInstanceInfo 结束的流程实例信息
     */
    public ProcessInstanceInfo overFlow(CapWorkflowParam workflowParam) {
        return getWorkflowAppService().overFlow(workflowParam);
    }
    
    /**
     * 流程跳转-跨节点跳转,支持同一流程实例上的节点跳转
     * 返回值为主流程实例对象ProcessInstanceInfo,其中属性NodeInfo指本次操作后产生的待办停留的用户节点
     * 
     * @param workflowParam
     *            流程信息
     * @return 主流程实例对象
     */
    public ProcessInstanceInfo jump(CapWorkflowParam workflowParam) {
        return getWorkflowAppService().jump(workflowParam);
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
        return getWorkflowAppService().queryForeTransNodes(workId, todoTaskId, userId, param);
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
        return getWorkflowAppService().queryForeTransNodes(workId, todoTaskId, deptId, userId);
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
        return getWorkflowAppService().queryReassignTransNodes(workId, todoTaskId, userId, param);
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
        return getWorkflowAppService().queryBackTransNodes(workId, todoTaskId, userId);
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
        return getWorkflowAppService().queryUserListByNode(workflowParam, processVersion, specialNodeId, filterWord);
    }
    
    /**
     * 获取扩展属性值
     * 
     * @param id 单据id
     * @return 扩展属性值
     */
    public Map<String, String> queryExtendAttrOfFirstNode(String id) {
        return getWorkflowAppService().queryExtendAttrOfFirstNode(id);
    }
    
    /**
     * 获取扩展属性值
     * 
     * @param id 单据id
     * @param taskId 任务id
     * @return 扩展属性值
     */
    public Map<String, String> queryExtendAttrByTaskId(String id, String taskId) {
        return getWorkflowAppService().queryExtendAttrByTaskId(id, taskId);
    }
    
    /**
     * 获取扩展属性值
     * 
     * @param id 单据id
     * @param nodeId 节点id
     * @return 扩展属性值
     */
    public Map<String, String> queryExtendAttrByNodeId(String id, String nodeId) {
        return getWorkflowAppService().queryExtendAttrByNodeId(id, nodeId);
    }
    
    /**
     * 
     * 查询用户集合所参与的指定流程上的节点信息，在指定流程的最新版本上（取并集）
     * 
     * @param userId 指定的用户ID集
     * @param taskType 任务类型
     * @return 指定的用户集在指定的流程最新版本上所参数的节点集合（取并集）
     */
    public List<BpmsNodeInfo> queryIntersecHumanNodesByUserIds(String userId, String taskType) {
        return getWorkflowAppService().queryIntersecHumanNodesByUserIds(userId, taskType);
    }
    
    /**
     * 查询指定流程的所有用户节点
     * 
     * @return 指定流程编号的所有用户节点
     */
    public List<BpmsNodeInfo> queryHumanNodesByProcessId() {
        return getWorkflowAppService().queryHumanNodesByProcessId();
    }
    
    /**
     * 获取配置过滤函数中变量对应的信息
     * 
     * @param workId 工单Id
     * @param filterKey 过滤函数对应的key
     * @return 用户信息
     */
    public UserInfo getDeptFilerParam(String workId, String filterKey) {
        return getWorkflowAppService().getDeptFilerParam(workId, filterKey);
    }
    
    /**
     * 查询流程实例处理跟踪（用于流程跟踪表展示）
     * 
     * @param workflowParam 流程参数
     * 
     * @return NodeTrackInfo[] 流程实例处理跟踪
     */
    public NodeTrackInfo[] queryProcessInsTransTrack(CapWorkflowParam workflowParam) {
        return getWorkflowAppService().queryProcessInsTransTrack(workflowParam);
    }
    
    /**
     * 查询已办任务ID
     * 
     * @param workflowParam
     *            工作流参数
     * @param activityInsId 活动实例ID
     * @param taskType 任務類型
     * @return 询已办任务ID
     */
    public String queryNewTaskId(CapWorkflowParam workflowParam, String activityInsId, String taskType) {
        return getWorkflowAppService().queryNewTaskId(workflowParam, activityInsId, taskType);
    }
    
}
