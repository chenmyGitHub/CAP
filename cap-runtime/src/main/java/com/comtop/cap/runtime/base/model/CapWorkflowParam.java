/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.util.HashMap;

import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.bpms.common.model.WorkFlowParamVO;
import com.comtop.cap.runtime.base.util.WorkflowConstant;

import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * 工作流参数对象
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月22日 龚斌
 */
@DataTransferObject
public class CapWorkflowParam extends WorkFlowParamVO {
    
    /*** 目标节点字符串 */
    private String tagNodeInfoString;
    
    /*** 参数字符串 */
    private String paramMapString;
    
    /** 版本标识 */
    private static final long serialVersionUID = 6778131244900858233L;
    
    /**
     * 构造函数
     */
    public CapWorkflowParam() {
        super();
    }
    
    /** 下发后的停留节点(区别于下发目标节点) */
    private NodeInfo[] waitingNodeInfos;
    
    /** 工作流状态,0:未上报,1:流程中,2:流程结束 */
    private Integer flowState;
    
    /** 工单id */
    private String workId;
    
    /** 发送短信扩展属性，用于默认操作时识别是否发送短信，true：发送短信，false：不发送短信 */
    private String smsType;
    
    /** 发送邮件扩展属性，用于默认操作时识别是否发送邮件，true：发送邮件，false：不发送短信 */
    private String emailType;
    
    /** 工作流操作类别 */
    private int operateType;
    
    /** 短信发送时获取业务数据名称，例如：检修单；缺陷单等 */
    private String dataName;
    
    /** 短信发送时获取模块名称 */
    private String modelName;
    
    /** 流程名称 */
    private String processName;
    
    /** 当前节点ID */
    private String currNodeId;
    
    /** map参数 */
    private HashMap<String, Object> paramMap;
    
    /** 过滤函数所需的参数名称 */
    private String paramKey;
    
    /** 关联的实体服务名称 */
    private String workflowFacadeName;
    
    /**
     * @return 获取 workflowFacadeName属性值
     */
    public String getWorkflowFacadeName() {
        return workflowFacadeName;
    }
    
    /**
     * @param workflowFacadeName 设置 workflowFacadeName 属性值为参数值 workflowFacadeName
     */
    public void setWorkflowFacadeName(String workflowFacadeName) {
        this.workflowFacadeName = workflowFacadeName;
    }
    
    /** 流程版本 */
    private int processVersion;
    
    /**
     * @return 获取 processVersion属性值
     */
    public int getProcessVersion() {
        return processVersion;
    }
    
    /**
     * @param processVersion 设置 processVersion 属性值为参数值 processVersion
     */
    public void setProcessVersion(int processVersion) {
        this.processVersion = processVersion;
    }
    
    /**
     * @return 获取 paramKey属性值
     */
    public String getParamKey() {
        return paramKey;
    }
    
    /**
     * @param paramKey 设置 paramKey 属性值为参数值 paramKey
     */
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
    
    /**
     * @return currNodeId
     */
    public String getCurrNodeId() {
        return currNodeId;
    }
    
    /**
     * @param currNodeId 要设置的 currNodeId
     */
    public void setCurrNodeId(String currNodeId) {
        this.currNodeId = currNodeId;
    }
    
    /**
     * @return processName
     */
    public String getProcessName() {
        return processName;
    }
    
    /**
     * @param processName 要设置的 processName
     */
    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    /**
     * @return operateType
     */
    public int getOperateType() {
        return operateType;
    }
    
    /**
     * @return 回退标识
     */
    public int getBackFlag() {
        return operateType == WorkflowConstant.WORK_FLOW_OPERATE_BACK ? 1 : 0;
    }
    
    /**
     * @param operateType 要设置的 operateType
     */
    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
    
    /**
     * @return smsType
     */
    public String getSmsType() {
        return smsType;
    }
    
    /**
     * @param smsType 要设置的 smsType
     */
    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }
    
    /**
     * @return emailType
     */
    public String getEmailType() {
        return emailType;
    }
    
    /**
     * @param emailType 要设置的 emailType
     */
    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
    
    /**
     * @return paramMap
     */
    public HashMap<String, Object> getParamMap() {
        return paramMap;
    }
    
    /**
     * @param paramMap 要设置的 paramMap
     */
    public void setParamMap(HashMap<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
    
    /**
     * @return flowState
     */
    public Integer getFlowState() {
        return flowState;
    }
    
    /**
     * @param flowState 要设置的 flowState
     */
    public void setFlowState(Integer flowState) {
        this.flowState = flowState;
    }
    
    /**
     * @return waitingNodeInfo
     */
    public NodeInfo[] getWaitingNodeInfos() {
        return waitingNodeInfos;
    }
    
    /**
     * @param waitingNodeInfo 要设置的 waitingNodeInfo
     */
    public void setWaitingNodeInfos(NodeInfo[] waitingNodeInfo) {
        this.waitingNodeInfos = waitingNodeInfo;
    }
    
    /**
     * @return workId
     */
    @Override
    public String getWorkId() {
        return workId;
    }
    
    /**
     * @param workId 要设置的 workId
     */
    @Override
    public void setWorkId(String workId) {
        this.workId = workId;
    }
    
    /**
     * 设置参数
     * 
     * @param processInsId 流程实例id
     * @param processId 流程编码
     * @param nodeInfos 节点信息
     */
    public void setParam(String processInsId, String processId, NodeInfo[] nodeInfos) {
        this.setProcessId(processId);
        this.setProcessInsId(processInsId);
        this.setTargetNodeInfos(nodeInfos);
        if (nodeInfos == null) {
            return;
        }
        for (NodeInfo lstInfo : nodeInfos) {
            lstInfo.setNodeInstanceId(null);
        }
    }
    
    /**
     * @return dataName
     */
    public String getDataName() {
        return dataName;
    }
    
    /**
     * @param dataName 要设置的 dataName
     */
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    
    /**
     * @return modelName
     */
    public String getModelName() {
        return modelName;
    }
    
    /**
     * @param modelName 要设置的 modelName
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    @Override
    public String toString() {
        StringBuffer sbParam = new StringBuffer(512);
        sbParam.append("WorkflowParam[");
        sbParam.append(super.toString());
        if (waitingNodeInfos != null) {
            sbParam.append("\n    waitingNodeInfos=[");
            for (int i = 0; i < this.waitingNodeInfos.length; i++) {
                sbParam.append("\n        ").append(this.waitingNodeInfos[i]);
            }
            sbParam.append("]");
        }
        sbParam.append("\n    flowState=").append(this.flowState);
        sbParam.append("\n    workId=").append(this.workId);
        sbParam.append("\n    smsType=").append(this.smsType);
        sbParam.append("\n    emailType=").append(this.emailType);
        sbParam.append("\n    operateType=").append(this.operateType);
        sbParam.append("\n    dataName=").append(this.dataName);
        sbParam.append("\n    modelName=").append(this.modelName);
        sbParam.append("\n    processName=").append(this.processName);
        sbParam.append("\n    currNodeId=").append(this.currNodeId);
        sbParam.append("\n    paramMap=").append(this.paramMap);
        sbParam.append("\n]");
        return sbParam.toString();
    }
    
    /**
     * @return 获取 paramMapString属性值
     */
    public String getParamMapString() {
        return paramMapString;
    }
    
    /**
     * @param paramMapString 设置 paramMapString 属性值为参数值 paramMapString
     */
    public void setParamMapString(String paramMapString) {
        this.paramMapString = paramMapString;
    }
    
    /**
     * @return 获取 tagNodeInfoString属性值
     */
    public String getTagNodeInfoString() {
        return tagNodeInfoString;
    }
    
    /**
     * @param tagNodeInfoString 设置 tagNodeInfoString 属性值为参数值 tagNodeInfoString
     */
    public void setTagNodeInfoString(String tagNodeInfoString) {
        this.tagNodeInfoString = tagNodeInfoString;
    }
}
