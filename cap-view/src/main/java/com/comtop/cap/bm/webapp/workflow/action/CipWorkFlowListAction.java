/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.workflow.action;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.cap.bm.webapp.entity.util.EntityConstants;
import com.comtop.cap.bm.webapp.workflow.model.CipProcessPageBean;
import com.comtop.cap.bm.webapp.workflow.utils.BpmsProcessHelper;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * CIP访问流程列表相关请求后台处理Action
 * 
 * @author 李小强
 * @since 1.0
 * @version 2014-11-17 李小强
 */
@DwrProxy
public class CipWorkFlowListAction extends BaseAction {

	/** 日志 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CipWorkFlowListAction.class);

	/**
	 * 根据目录编号查询未部署的流程列表
	 * 
	 * @param dirCode
	 *            目录编号
	 * @param userId
	 *            操作人编号（必须）
	 * @param pageNo
	 *            当面页面编码
	 * @param pageSize
	 *            每页显示条数
	 * @return 指定目录编号查询未部署的流程列表
	 */
	@RemoteMethod
	public Map<String, Object> queryUnDeployeProcessByDirCode(String dirCode,
			String userId, int pageNo, int pageSize) {
		Map<String, Object> objRet = new HashMap<String, Object>(1000);
		try {
			CipProcessPageBean objRsPageBean = BpmsProcessHelper
					.queryUnDeployeProcessByDirCode(dirCode, userId, pageNo,
							pageSize);
			objRet.put("list", objRsPageBean.getValueList());
			objRet.put("count", objRsPageBean.getAllRows());
		} catch (AbstractBpmsException e) {
			LOGGER.error("根据目录编号查询未部署的流程列表出错！", e);
		}
		return objRet;
	}

	/**
	 * 删除流程(限状态为未部署的流程,即流程草稿)
	 * 
	 * @param dirCode
	 *            部署目录编码
	 * 
	 * @param deployeId
	 *            部署编号（必须）
	 * @param userId
	 *            操作人编号（必须）
	 * @return 指定目录编号查询未部署的流程列表
	 */
	@RemoteMethod
	public String deleteDeployeById(String dirCode, String[] deployeId,
			String userId) {
		String objRs = BpmsProcessHelper.deleteUndeployeById(deployeId, userId);
		if (objRs != null && objRs.trim().length() > 1) {
			return objRs;
		}
		return EntityConstants.OPERATE_SUCCESS_VALUE;
	}

	/**
	 * 通过缺陷属性ID查询缺陷属性
	 * 
	 * @param dirCode
	 *            目录编码
	 * @param userId
	 *            操作人编号（必须）
	 * @param pageNo
	 *            当面页面编码
	 * @param pageSize
	 *            每页显示条数
	 * @return 已部署的流程数据
	 */
	@RemoteMethod
	public Map<String, Object> queryDeployeEdProcesses(String dirCode,
			String userId, int pageNo, int pageSize) {
		Map<String, Object> objRet = new HashMap<String, Object>();
		try {
			CipProcessPageBean objRsPageBean = BpmsProcessHelper
					.queryDeployedProcessByDirCode(dirCode, userId, pageNo,
							pageSize);
			objRet.put("list", objRsPageBean.getValueList());
			objRet.put("count", objRsPageBean.getAllRows());
		} catch (AbstractBpmsException e) {
			LOGGER.error("通过缺陷属性ID查询缺陷属性出错！", e);
		}
		return objRet;
	}

	/**
	 * 卸载流程
	 * 
	 * @param dirCode
	 *            目录编码
	 * 
	 * @param deployeId
	 *            部署编号（必须）
	 * @param userId
	 *            操作人编号（必须）
	 * @return 已部署的流程数据
	 */
	@RemoteMethod
	public String uninstallDeployeById(String dirCode, String[] deployeId,
			String userId) {
		String objRs = BpmsProcessHelper
				.uninstallDeployeById(deployeId, userId);
		if (objRs != null && objRs.trim().length() > 1) {
			return objRs;
		}
		return EntityConstants.OPERATE_SUCCESS_VALUE;
	}
}
