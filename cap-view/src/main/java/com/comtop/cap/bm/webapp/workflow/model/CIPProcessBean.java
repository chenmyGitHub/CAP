/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.workflow.model;

import java.util.Date;

import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * CIP流程实体
 * 
 * @author 李小强
 * @since 1.0
 * @version 2014-11-18 李小强
 */
@DataTransferObject
public class CIPProcessBean implements java.io.Serializable {
    
    /** FIXME */
    private static final long serialVersionUID = -4525662498220053865L;

    /** 流程Id */
    private String processId;
    
    /** 部署Id */
    private String deployeId;
    
    /** 流程版本 */
    private Integer version;
    
    /** 流程名称 */
    private String name;
    
    /** 部署时间 */
    private Date deployTime;
    
    /** 目录编号 */
    private String dirCode;
    
    /** 部署人姓名 */
    private String deployPersonName;
    
    /** 部署状态(0表示未部署、1表示已部署、2表示卸载) */
    private Integer state;
    
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
     * @return 获取 deployeId属性值
     */
    public String getDeployeId() {
        return deployeId;
    }
    
    /**
     * @param deployeId 设置 deployeId 属性值为参数值 deployeId
     */
    public void setDeployeId(String deployeId) {
        this.deployeId = deployeId;
    }
    
    /**
     * @return 获取 version属性值
     */
    public Integer getVersion() {
        return version;
    }
    
    /**
     * @param version 设置 version 属性值为参数值 version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    /**
     * @return 获取 name属性值
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name 设置 name 属性值为参数值 name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return 获取 deployTime属性值
     */
    public Date getDeployTime() {
        return deployTime;
    }
    
    /**
     * @param deployTime 设置 deployTime 属性值为参数值 deployTime
     */
    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }
    
    /**
     * @return 获取 dirCode属性值
     */
    public String getDirCode() {
        return dirCode;
    }
    
    /**
     * @param dirCode 设置 dirCode 属性值为参数值 dirCode
     */
    public void setDirCode(String dirCode) {
        this.dirCode = dirCode;
    }
    
    /**
     * @return 获取 deployPersonName属性值
     */
    public String getDeployPersonName() {
        return deployPersonName;
    }
    
    /**
     * @param deployPersonName 设置 deployPersonName 属性值为参数值 deployPersonName
     */
    public void setDeployPersonName(String deployPersonName) {
        this.deployPersonName = deployPersonName;
    }
    
    /**
     * @return 获取 state属性值
     */
    public Integer getState() {
        return state;
    }
    
    /**
     * @param state 设置 state 属性值为参数值 state
     */
    public void setState(Integer state) {
        this.state = state;
    }
    
    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CIPProcessBean [processId=" + processId + ", deployeId=" + deployeId + ", version=" + version
            + ", name=" + name + ", deployTime=" + deployTime + ", dirCode=" + dirCode + ", deployPersonName="
            + deployPersonName + ", state=" + state + "]";
    }
    
}
