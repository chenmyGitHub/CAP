/******************************************************************************
 * Copyright (C) 2016 
 * ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。
 * 未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.bpmsext.model;

import com.comtop.top.core.base.model.CoreVO;

import comtop.org.directwebremoting.annotations.DataTransferObject;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

import java.sql.Timestamp;


/**
 * 记录运行时变量
 * 
 * @author CAP超级管理员
 * @since 1.0
 * @version 2016-4-2 CAP超级管理员
 */
@Table(name = "CAP_RT_BPMS_VAR_EXT")
@DataTransferObject
public class BpmsVarExtVO extends CoreVO {

    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 变量主键 */
    @Id
    @Column(name = "VARIABLE_ID",length=40,precision=0)
    private String variableId;
    
    /** 主流程实例ID */
    @Column(name = "MAIN_PROCESS_INS_ID",length=40,precision=0)
    private String mainProcessInsId;
    
    /** 变量KEY */
    @Column(name = "VARIABLE_KEY",length=80,precision=0)
    private String variableKey;
    
    /** 变量值 */
    @Column(name = "VARIABLE_VALUE",length=2000,precision=0)
    private String variableValue;
    
    /** 变量类型 */
    @Column(name = "VARIABLE_TYPE",length=200,precision=0)
    private String variableType;
    
    /** 创建时间 */
    @Column(name = "CREATE_TIME",precision=6)
    private Timestamp createTime;
    
    /** 当前节点实例的唯一标识 */
    @Column(name = "CUR_NODE_INS_ID",length=40,precision=0)
    private String curNodeInsId;
    
    /** 当前节点的唯一标识 */
    @Column(name = "CUR_NODE_ID",length=40,precision=0)
    private String curNodeId;
    
    /** 修改时间 */
    @Column(name = "MODIFY_DATE",precision=6)
    private Timestamp modifyDate;
    
    /** 主流程编号 */
    @Column(name = "MAIN_PROCESS_ID",length=40,precision=0)
    private String mainProcessId;
    
	
    /**
     * @return 获取 变量主键 属性值
     */
    public String getVariableId() {
        return variableId;
    }
    	
    /**
     * @param variableId 设置 变量主键 属性值为参数值 variableId
     */
    public void setVariableId(String variableId) {
        this.variableId = variableId;
    }
    
    /**
     * @return 获取 主流程实例ID 属性值
     */
    public String getMainProcessInsId() {
        return mainProcessInsId;
    }
    	
    /**
     * @param mainProcessInsId 设置 主流程实例ID 属性值为参数值 mainProcessInsId
     */
    public void setMainProcessInsId(String mainProcessInsId) {
        this.mainProcessInsId = mainProcessInsId;
    }
    
    /**
     * @return 获取 变量KEY 属性值
     */
    public String getVariableKey() {
        return variableKey;
    }
    	
    /**
     * @param variableKey 设置 变量KEY 属性值为参数值 variableKey
     */
    public void setVariableKey(String variableKey) {
        this.variableKey = variableKey;
    }
    
    /**
     * @return 获取 变量值 属性值
     */
    public String getVariableValue() {
        return variableValue;
    }
    	
    /**
     * @param variableValue 设置 变量值 属性值为参数值 variableValue
     */
    public void setVariableValue(String variableValue) {
        this.variableValue = variableValue;
    }
    
    /**
     * @return 获取 变量类型 属性值
     */
    public String getVariableType() {
        return variableType;
    }
    	
    /**
     * @param variableType 设置 变量类型 属性值为参数值 variableType
     */
    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }
    
    /**
     * @return 获取 创建时间 属性值
     */
    public Timestamp getCreateTime() {
        return createTime;
    }
    	
    /**
     * @param createTime 设置 创建时间 属性值为参数值 createTime
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    /**
     * @return 获取 当前节点实例的唯一标识 属性值
     */
    public String getCurNodeInsId() {
        return curNodeInsId;
    }
    	
    /**
     * @param curNodeInsId 设置 当前节点实例的唯一标识 属性值为参数值 curNodeInsId
     */
    public void setCurNodeInsId(String curNodeInsId) {
        this.curNodeInsId = curNodeInsId;
    }
    
    /**
     * @return 获取 修改时间 属性值
     */
    public Timestamp getModifyDate() {
        return modifyDate;
    }
    	
    /**
     * @param modifyDate 设置 修改时间 属性值为参数值 modifyDate
     */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
    
    /**
     * @return 获取 主流程编号 属性值
     */
    public String getMainProcessId() {
        return mainProcessId;
    }
    	
    /**
     * @param mainProcessId 设置 主流程编号 属性值为参数值 mainProcessId
     */
    public void setMainProcessId(String mainProcessId) {
        this.mainProcessId = mainProcessId;
    }
	 
    /**
	 * @return the curNodeId
	 */
	public String getCurNodeId() {
		return curNodeId;
	}

	/**
	 * @param curNodeId the curNodeId to set
	 */
	public void setCurNodeId(String curNodeId) {
		this.curNodeId = curNodeId;
	}

	/**
     * 获取主键值
     * @return 主键值
     */
    public String getPrimaryValue(){
    		return  this.variableId;
    }
}