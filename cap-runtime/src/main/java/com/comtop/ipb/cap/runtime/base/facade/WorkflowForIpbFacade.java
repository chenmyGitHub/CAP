/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.ipb.cap.runtime.base.facade;

import java.util.List;

import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.cap.runtime.base.facade.CapWorkflowFacade;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cap.runtime.bpmsext.model.BpmsVarExtVO;
import com.comtop.ipb.cap.runtime.base.appservice.WorkflowForIpbAppService;
import com.comtop.ipb.cap.runtime.base.model.WorkflowForIpbVO;

/**
 * @author luozhenming
 * @param <VO> 招采平台流程审批基类 
 *
 */
public abstract class WorkflowForIpbFacade<VO extends WorkflowForIpbVO> extends CapWorkflowFacade<VO> {
	//TODO
	/**
     * 获取工作流AppService
     * 
     * @return 工作流AppService
     */
	  @Override
    protected WorkflowForIpbAppService<VO> getWorkflowAppService() {
        return (WorkflowForIpbAppService) getAppService();
        
    }

	/**
	 * 查询节点实例配置的变量
	 * 
	 * @param workflowParam 工作流参数
	 * @return  查询节点实例配置的变量
	 */
	public List<BpmsVarExtVO> queryBpmsVarExtList(CapWorkflowParam workflowParam) {
		return getWorkflowAppService().queryBpmsVarExtList(workflowParam);
	}
	
	/**
	 * 是否需要显示会签按钮
	 * 
	 * @param workflowParam 工作流参数
	 * @return  查询节点实例配置的变量
	 * @throws AbstractBpmsException bpms异常
	 */
	public boolean isShowCountersignButton(CapWorkflowParam workflowParam) throws AbstractBpmsException {
		return getWorkflowAppService().isShowCountersignButton(workflowParam);
	}
	
	/**
	 * 获取指定版本流程所有用户节点信息,如果传递了当前节点则查该节点之后的
	 * 
	 * @param workflowParam 工作流参数
	 * @return 流程所有用户节点集合，涵盖用户节点里配置的人员
	 */
    public List<NodeInfo> queryAllUserNodesByVersion(CapWorkflowParam workflowParam) {
    	return getWorkflowAppService().queryAllUserNodesByVersion(workflowParam);
    }
}
