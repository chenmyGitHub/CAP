/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.ipb.cap.runtime.base.appservice;

import com.comtop.cip.jodd.petite.meta.PetiteBean;

/**
 * 招采平台流程审批基类(继承实现类)
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月3日 许畅 新建
 */
@PetiteBean
public class WfForIpbExtAppService extends WorkflowForIpbAppService {

	/**
	 * processId
	 */
	private String processId;

	/**
	 * dataName
	 */
	private String dataName;

	/**
	 *
	 * @return ProcessId
	 *
	 */
	  @Override
	public String getProcessId() {
		return processId;
	}

	/**
	 * @return dataname
	 */
	  @Override
	public String getDataName() {
		return dataName;
	}

	/**
	 * @param processId
	 *            the processId to set
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * @param dataName
	 *            the dataName to set
	 */
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

}
