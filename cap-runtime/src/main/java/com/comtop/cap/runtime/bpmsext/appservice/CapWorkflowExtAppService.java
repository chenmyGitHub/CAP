/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.bpmsext.appservice;

import java.util.List;

import com.comtop.cap.runtime.base.appservice.CapWorkflowAppService;
import com.comtop.cap.runtime.base.model.CapWorkflowVO;
import com.comtop.cap.runtime.bpmsext.model.CapWorkflowExtVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;

/**
 * CapWorkflowAppService继承实现类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月5日 许畅 新建
 */
@PetiteBean
public class CapWorkflowExtAppService extends
		CapWorkflowAppService<CapWorkflowExtVO> {

	/** 查询已办sql id **/
	private static final String DONE_TASK_STATMENT_ID = "com.comtop.cap.runtime.bpmsext.model.queryDoneTaskByProcessInstId";

	/** 查询待办sql id **/
	private static final String TODO_TASK_STATMENT_ID = "com.comtop.cap.runtime.bpmsext.model.queryTodoTaskByProcessInstId";

	/**
	 * 查询已办信息
	 * 
	 * @param workflowVO
	 *            流程参数
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<CapWorkflowVO> queryDoneTaskVOByProcessInsId(
			CapWorkflowVO workflowVO) {
		List<CapWorkflowVO> lst = capBaseCommonDAO.queryList(
				DONE_TASK_STATMENT_ID, workflowVO);
		return lst;
	}

	/**
	 * 查询待办信息
	 * 
	 * @param workflowVO
	 *            流程参数
	 * @return lst
	 */
	@SuppressWarnings("unchecked")
	public List<CapWorkflowVO> queryTodoTaskVOByProcessInsId(
			CapWorkflowVO workflowVO) {
		List<CapWorkflowVO> lst = capBaseCommonDAO.queryList(
				TODO_TASK_STATMENT_ID, workflowVO);
		return lst;
	}

	@Override
	public String getProcessId() {
		return null;
	}

	@Override
	public String getDataName() {
		return null;
	}
}
