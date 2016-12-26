/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.bpmsext;

import java.util.List;
import java.util.Map;

import com.comtop.cap.runtime.base.model.CapWorkflowVO;

/**
 * CAP流程查询facade接口
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月5日 许畅 新建
 */
public interface ICapWorkflowExtFacade {
	
	/**
	 * 通过流程实例id查询已办或者待办的信息
	 * 
	 * @param workflowVO 流程参数
	 * @return 待办已办的流程信息
	 */
	public Map<String,Object> queryTaskVOByProcessInsId(CapWorkflowVO workflowVO);
	
	/**
	 * 通过流程实例id查询已办信息
	 * 
	 * @param workflowVO 流程参数
	 * @return 查询出已办的流程信息
	 */
	public List<CapWorkflowVO> queryDoneTaskVOByProcessInsId(CapWorkflowVO workflowVO);
	
	/**
	 * 通过流程实例id查询待办信息
	 * 
	 * @param workflowVO 流程参数
	 * @return 查询待办信息
	 */
	public List<CapWorkflowVO> queryTodoTaskVOByProcessInsId(CapWorkflowVO workflowVO);

}
