/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.bpmsext.model;

import com.comtop.cap.runtime.base.model.CapWorkflowVO;

import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月5日 许畅 新建
 */
@DataTransferObject
public class CapWorkflowExtVO extends CapWorkflowVO {

	/** 版本标识 */
	private static final long serialVersionUID = -8345652350605399408L;

	/** 流程实例id **/
	private String processInsId;

	/** 工作流状态 **/
	private int flowState;
	  @Override
	public String getProcessInsId() {
		return processInsId;
	}
	  @Override
	public void setProcessInsId(String processInsId) {
		this.processInsId = processInsId;
	}
	  @Override
	public Integer getFlowState() {
		return flowState;
	}
	  @Override
	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}

}
