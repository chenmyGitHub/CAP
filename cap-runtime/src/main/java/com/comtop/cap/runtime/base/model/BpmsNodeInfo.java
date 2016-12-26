/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.io.Serializable;

import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * 流程节点对象——业务扩展的BPMS对象
 * 
 * @author 李小强
 * @since 1.0
 * @version 2015-5-25 李小强
 */
@DataTransferObject
public class BpmsNodeInfo implements Serializable {
    
    /** 序列化 */
    private static final long serialVersionUID = 5853545653946709548L;
    
    /** 节点ID */
    private String nodeId;
    
    /** 节点名称 */
    private String nodeName;
    
    /** 流程ID */
    private String processId;
    
    /**
     * 构造函数
     */
    public BpmsNodeInfo() {
        super();
    }
    
    /**
     * 构造函数
     * 
     * @param processId 流程ID
     * @param nodeId 节点ID
     * @param nodeName 节点名称
     */
    public BpmsNodeInfo(String processId, String nodeId, String nodeName) {
        super();
        this.processId = processId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
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
    
}
