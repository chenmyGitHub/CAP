/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.util.List;

import javax.persistence.Transient;

import com.comtop.cap.runtime.base.annotation.EntityAlias;
import com.comtop.cip.jodd.util.StringUtil;
import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * 工作流基本信息vo
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月21日 龚斌
 */
@DataTransferObject
public abstract class CapWorkflowVO extends CapBaseVO {
    
    /** 版本标识 */
    private static final long serialVersionUID = 1824565326197148124L;
    
    /** 代办或已办任务id */
    @Transient
    protected String taskId;
    
    // /**
    // * @return 获取 id属性值
    // */
    // public abstract String getId();
    //
    // /**
    // * @param id 设置 id 属性值为参数值 id
    // */
    // public abstract void setId(String id);
    
    /**
     * @return processInsId
     */
    public abstract String getProcessInsId();
    
    /**
     * @param processInsId 要设置的 processInsId
     */
    public abstract void setProcessInsId(String processInsId);
    
    /**
     * @return flowState
     */
    public abstract Integer getFlowState();
    
    /**
     * @param flowState 要设置的 flowState
     */
    public abstract void setFlowState(Integer flowState);
    
    /**
     * 
     * 返回流程操作的facade名称前缀
     * 
     * 
     * @return 流程Facade名称
     */
    public String getWorkflowFacadeName() {
        String strVoName = this.getClass().getSimpleName();
        
        EntityAlias entityAlias = this.getClass().getAnnotation(EntityAlias.class);
        if (entityAlias == null || StringUtil.isBlank(entityAlias.value())) {
            if (strVoName.endsWith("VO")) {
                // 截掉VO最后两位
                String strEntityName = strVoName.substring(0, strVoName.length() - 2);
                // 首字母小写
                String strFirstWord = String.valueOf(strEntityName.charAt(0)).toLowerCase();
                strEntityName = strFirstWord + strEntityName.substring(1) + "Facade";
                return strEntityName;
            }
        } else {
            return entityAlias.value() + "Facade";
        }
        return null;
    }
    
    /**
     * 回退标识
     */
    protected String backFlag;
    
    /**
     * 流程唯一编码
     */
    protected String processId;
    
    /**
     * 当前处理人
     */
    protected String transActor;
    
    /**
     * 流程节点唯一编码
     */
    protected String nodeId;
    
    /**
     * 流程节点唯一名称
     */
    protected String nodeName;
    
    /**
     * 已办是否能撤回标志位
     */
    protected String revokeBackFlag;
    
    /**
     * 待办已办的数据表名--主要为分区服务
     */
    protected String transTableName;
    
    /**
     * 流程节点跟踪信息表名--主要为分区服务
     */
    protected String ruNodeTrackTableName;
    
    /**
     * 流程节点处理数据表名--主要为分区服务
     */
    protected String ruTransTrackTableName;
    
    /**
     * 流程版本
     */
    protected int version;
    
    /**
     * 审批意见
     */
    protected String opinion;
    
    /**
     * 第一个节点的ID集合
     */
    protected List<String> firstNodeIds;
    
    /**
     * 当前节点实例ID
     */
    protected String curNodeInsId;
    
    /**
     * 活动实例ID
     */
    protected String activityInsId;
    
    /**
     * @return 获取 firstNodeIds属性值
     */
    public List<String> getFirstNodeIds() {
        return firstNodeIds;
    }
    
    /**
     * @param firstNodeIds 设置 firstNodeIds 属性值为参数值 firstNodeIds
     */
    public void setFirstNodeIds(List<String> firstNodeIds) {
        this.firstNodeIds = firstNodeIds;
    }
    
    /**
     * @return 获取 version属性值
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * @param version 设置 version 属性值为参数值 version
     */
    public void setVersion(int version) {
        this.version = version;
    }
    
    /**
     * @return 获取 opinion属性值
     */
    public String getOpinion() {
        return opinion;
    }
    
    /**
     * @param opinion 设置 opinion 属性值为参数值 opinion
     */
    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
    
    /**
     * @return taskId
     */
    public String getTaskId() {
        return taskId;
    }
    
    /**
     * @param taskId 要设置的 taskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    /**
     * @return 获取 backFlag属性值
     */
    public String getBackFlag() {
        return backFlag;
    }
    
    /**
     * @param backFlag 设置 backFlag 属性值为参数值 backFlag
     */
    public void setBackFlag(String backFlag) {
        this.backFlag = backFlag;
    }
    
    /**
     * @return 获取 processId属性值
     */
    public String getProcessId() {
        return processId;
    }
    
    /**
     * @param processId 设置 processId 属性值为参数值 processId
     */
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    
    /**
     * @return 获取 transActor属性值
     */
    public String getTransActor() {
        return transActor;
    }
    
    /**
     * @param transActor 设置 transActor 属性值为参数值 transActor
     */
    public void setTransActor(String transActor) {
        this.transActor = transActor;
    }
    
    /**
     * @return 获取 nodeId属性值
     */
    public String getNodeId() {
        return nodeId;
    }
    
    /**
     * @param nodeId 设置 nodeId 属性值为参数值 nodeId
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    /**
     * @return 获取 nodeName属性值
     */
    public String getNodeName() {
        return nodeName;
    }
    
    /**
     * @param nodeName 设置 nodeName 属性值为参数值 nodeName
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    /**
     * @return 获取 revokeBackFlag属性值
     */
    public String getRevokeBackFlag() {
        return revokeBackFlag;
    }
    
    /**
     * @param revokeBackFlag 设置 revokeBackFlag 属性值为参数值 revokeBackFlag
     */
    public void setRevokeBackFlag(String revokeBackFlag) {
        this.revokeBackFlag = revokeBackFlag;
    }
    
    /**
     * @return 获取 transTableName属性值
     */
    public String getTransTableName() {
        return transTableName;
    }
    
    /**
     * @param transTableName 设置 transTableName 属性值为参数值 transTableName
     */
    public void setTransTableName(String transTableName) {
        this.transTableName = transTableName;
    }
    
    /**
     * @return 获取 ruNodeTrackTableName属性值
     */
    public String getRuNodeTrackTableName() {
        return ruNodeTrackTableName;
    }
    
    /**
     * @param ruNodeTrackTableName 设置 ruNodeTrackTableName 属性值为参数值 ruNodeTrackTableName
     */
    public void setRuNodeTrackTableName(String ruNodeTrackTableName) {
        this.ruNodeTrackTableName = ruNodeTrackTableName;
    }
    
    /**
     * @return 获取 ruTransTrackTableName属性值
     */
    public String getRuTransTrackTableName() {
        return ruTransTrackTableName;
    }
    
    /**
     * @param ruTransTrackTableName 设置 ruTransTrackTableName 属性值为参数值 ruTransTrackTableName
     */
    public void setRuTransTrackTableName(String ruTransTrackTableName) {
        this.ruTransTrackTableName = ruTransTrackTableName;
    }
    
    /**
     * @return the curNodeInsId
     */
    public String getCurNodeInsId() {
        return curNodeInsId;
    }
    
    /**
     * @param curNodeInsId the curNodeInsId to set
     */
    public void setCurNodeInsId(String curNodeInsId) {
        this.curNodeInsId = curNodeInsId;
    }
    
    /**
     * @return the activityInsId
     */
    public String getActivityInsId() {
        return activityInsId;
    }
    
    /**
     * @param activityInsId the activityInsId to set
     */
    public void setActivityInsId(String activityInsId) {
        this.activityInsId = activityInsId;
    }
    
}
