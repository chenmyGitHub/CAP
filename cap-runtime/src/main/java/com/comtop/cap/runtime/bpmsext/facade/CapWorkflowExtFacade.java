/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.bpmsext.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.base.facade.CapWorkflowFacade;
import com.comtop.cap.runtime.base.model.CapWorkflowVO;
import com.comtop.cap.runtime.base.util.WorkflowHelper;
import com.comtop.cap.runtime.bpmsext.ICapWorkflowExtFacade;
import com.comtop.cap.runtime.bpmsext.appservice.CapWorkflowExtAppService;
import com.comtop.cap.runtime.bpmsext.model.CapWorkflowExtVO;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.top.core.jodd.AppContext;

/**
 * CapWorkflowFacade继承实现类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月5日 许畅 新建
 */
@Service
public class CapWorkflowExtFacade extends CapWorkflowFacade<CapWorkflowExtVO>
		implements ICapWorkflowExtFacade {

	/** 注入CapWorkflowExtAppService **/
	@PetiteInject
	protected CapWorkflowExtAppService capWorkflowExtAppService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	  @Override
	protected CapBaseAppService getAppService() {
		if (capWorkflowExtAppService == null) {
			capWorkflowExtAppService = AppContext
					.getBean(CapWorkflowExtAppService.class);
		}
		return capWorkflowExtAppService;
	}

	/**
	 * 根据流程实例id查询待办或者已办流程信息
	 *
	 * @param workflowVO
	 *            工作流参数
	 * @return 待办或者已办流程信息
	 */
	@Override
	public Map<String, Object> queryTaskVOByProcessInsId(
			CapWorkflowVO workflowVO) {
		// 返回结果集
		Map<String, Object> result = new HashMap<String, Object>();

		List<CapWorkflowVO> dones = queryDoneTaskVOByProcessInsId(workflowVO);
		if (dones != null && dones.size() > 0) {
			result.put("vo", dones.get(0));
			result.put("workType", "done");
		} else {
			List<CapWorkflowVO> todos = queryTodoTaskVOByProcessInsId(workflowVO);
			if (todos != null && todos.size() > 0) {
				result.put("vo", todos.get(0));
				result.put("workType", "todo");
			}
		}

		return result;
	}

	/**
	 * 查询已办信息
	 *
	 * @param workflowVO
	 *            工作流参数
	 * @return 已办信息
	 */
	@Override
	public List<CapWorkflowVO> queryDoneTaskVOByProcessInsId(
			CapWorkflowVO workflowVO) {
		String processId = workflowVO.getProcessId();
		String processInsId = workflowVO.getProcessInsId();

		if (StringUtil.isEmpty(processInsId) || StringUtil.isEmpty(processId))
			return null;
		
		List<String> nodes = WorkflowHelper.queryFirstNodeIds(processId, null);

		workflowVO.setFirstNodeIds(nodes);
		workflowVO.setTransTableName(WorkflowHelper.readTaskTableName(
				processId, true));

		return ((CapWorkflowExtAppService) getAppService())
				.queryDoneTaskVOByProcessInsId(workflowVO);
	}

	/**
	 * 查询待办信息
	 *
	 * @param workflowVO
	 *            工作流参数
	 * @return 待办信息
	 */
	@Override
	public List<CapWorkflowVO> queryTodoTaskVOByProcessInsId(
			CapWorkflowVO workflowVO) {

		String processId = workflowVO.getProcessId();
		String processInsId = workflowVO.getProcessInsId();

		if (StringUtil.isEmpty(processInsId) || StringUtil.isEmpty(processId))
			return null;

		workflowVO.setTransTableName(WorkflowHelper.readTaskTableName(
				processId, false));

		return ((CapWorkflowExtAppService) getAppService())
				.queryTodoTaskVOByProcessInsId(workflowVO);
	}

}
