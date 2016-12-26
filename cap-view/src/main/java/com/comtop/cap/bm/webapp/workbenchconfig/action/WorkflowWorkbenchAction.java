/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.workbenchconfig.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.metadata.soareg.util.SoaReqServiceUtil;
import com.comtop.cap.bm.metadata.sysmodel.facade.SysmodelFacade;
import com.comtop.cap.bm.metadata.workbenchconfig.convert.WorkbenchConfigBuilderFactory;
import com.comtop.cap.bm.metadata.workbenchconfig.facade.IWorkbenchConfigFacade;
import com.comtop.cap.bm.metadata.workbenchconfig.facade.WorkbenchConfigFacadeImpl;
import com.comtop.cap.bm.metadata.workbenchconfig.model.WorkbenchConfigVO;
import com.comtop.cap.bm.webapp.util.CapViewUtils;
import com.comtop.cap.bm.webapp.workflow.model.CIPProcessBean;
import com.comtop.cap.bm.webapp.workflow.model.CipProcessPageBean;
import com.comtop.cap.bm.webapp.workflow.utils.BpmsProcessHelper;
import com.comtop.top.core.jodd.AppContext;

import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 工作台待办配置Action
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年2月24日 许畅 新建
 */
@DwrProxy
public class WorkflowWorkbenchAction {

	/** 界面调用接口 */
	private final IWorkbenchConfigFacade workbenchConfigFacade = AppContext
			.getBean(WorkbenchConfigFacadeImpl.class);

	/** 包Facade */
	private final SysmodelFacade packageFacade = AppContext
			.getBean(SysmodelFacade.class);

	/** 日志 */
	private final static Logger LOGGER = LoggerFactory
			.getLogger(WorkflowWorkbenchAction.class);

	/** 资源文件路径 */
	private static final String RESOURCE_PATH = "src/main/resources/";

	/** 文件后缀名称 */
	private static final String END_PREFIX = "_WorkbenchConfig.sql";

	/** 用户ID */
	private static final String USER_ID = "SuperAdmin";

	/** 当前页面编码 */
	private static final int PAGE_NO = 1;

	/** 每页显示的条数 */
	private static final int PAGE_SIZE = 50;

	/**
	 * 保存操作
	 * 
	 * @param moduleCode
	 *            系统目录树的，应用模块编码
	 * 
	 * @param projectPath
	 *            项目路径
	 * @param packageId
	 *            包id
	 * @param pageVO
	 *            界面实体
	 * @return 界面ID
	 */
	@RemoteMethod
	public String saveAction(String moduleCode, String projectPath,
			String packageId, WorkbenchConfigVO pageVO) {
		// 调用存储过程
		String processId = workbenchConfigFacade.save(pageVO);
		// 生成SQL文件
		if (StringUtils.isNotEmpty(processId)) {
			String packagePath = packageFacade.queryPackageById(packageId)
					.getFullPath();

			Map<String, Object> data = getListData(moduleCode);
			if (data == null) {
				return null;
			}

			String exeSQL = this.getMainContent(data);
			String clientSQL = WorkbenchConfigBuilderFactory.getInstance()
					.buildClientSQL(exeSQL);
			LOGGER.info("生成工作台配置WorkbenchConfig.sql脚本内容:\n" + clientSQL);
			// 文件名称
			String fileName = CapViewUtils.getFileName(packagePath, END_PREFIX);
			if (clientSQL.indexOf("P_CAP_WB_PROcessCfg") > -1) {
				CapViewUtils.writeResourceSqlFile(
						PreferenceConfigQueryUtil.getCodePath(), RESOURCE_PATH,
						packagePath, fileName, clientSQL);
				// 注册soa服务
				String procedureSql = SoaReqServiceUtil
						.assembleToJdbcExecSQL(exeSQL);
				SoaReqServiceUtil.executeProcedureSql(procedureSql);
			}
		}
		return processId;
	}

	/**
	 * @param data
	 *            data
	 * @return 主要内容
	 */
	private String getMainContent(Map<String, Object> data) {
		StringBuffer main = new StringBuffer();
		@SuppressWarnings("unchecked")
		List<WorkbenchConfigVO> list = (List<WorkbenchConfigVO>) data
				.get("list");
		for (WorkbenchConfigVO configVO : list) {
			main.append(this.getMainContent(configVO));
		}
		return main.toString();
	}

	/**
	 * 生成数据库脚本内容
	 * 
	 * @param configVO
	 *            configVO
	 * @return 脚本内容
	 */
	private String getMainContent(WorkbenchConfigVO configVO) {
		StringBuffer main = new StringBuffer();
		main.append("\t P_CAP_WB_PROcessCfg('"
				+ replaceNull(configVO.getProcessId()) + "','"
				+ replaceNull(configVO.getProcessName()) + "','"
				+ replaceNull(configVO.getTodo_url()) + "',1); \n");
		main.append("\t P_CAP_WB_PROcessCfg('"
				+ replaceNull(configVO.getProcessId()) + "','"
				+ replaceNull(configVO.getProcessName()) + "','"
				+ replaceNull(configVO.getDone_url()) + "',2); \n");
		return main.toString();
	}

	/**
	 * 替换null为""
	 * 
	 * @param str
	 *            str
	 * @return str
	 */
	private String replaceNull(String str) {
		return str == null || "null".equalsIgnoreCase(str) ? "" : str;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 *            界面实体
	 */
	@RemoteMethod
	public void deleteAction(String[] ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> list = new ArrayList<String>();
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				list.add(id);
			}
		}

		if (list.size() > 0)
			map.put("processIdList", list);

		workbenchConfigFacade.delete(map);
	}

	/**
	 * 获取界面和列表数据
	 * 
	 * @param moduleCode
	 *            moduleCode
	 * @return map
	 */
	@RemoteMethod
	public Map<String, Object> getListData(String moduleCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		CipProcessPageBean objRsPageBean = null;
		try {
			objRsPageBean = BpmsProcessHelper.queryDeployedProcessByDirCode(
					moduleCode, USER_ID, PAGE_NO, PAGE_SIZE);
		} catch (AbstractBpmsException e) {
			LOGGER.error(e.getMessage(), e);
		}

		if (objRsPageBean != null) {
			List<CIPProcessBean> processes = objRsPageBean.getValueList();
			List<String> list = new ArrayList<String>();
			for (CIPProcessBean process : processes) {
				list.add(process.getProcessId());
			}
			if (list.size() > 0)
				map.put("processIdList", list);
		}

		return workbenchConfigFacade.getWorkbenchConfigListData(map);
	}

	/**
	 * 获取单据信息根据id
	 * 
	 * @param modelId
	 *            processId
	 * @return map
	 */
	@RemoteMethod
	public Map<String, Object> loadData(String modelId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(modelId)) {
			List<String> list = new ArrayList<String>();
			list.add(modelId);
			map.put("processIdList", list);
			return workbenchConfigFacade.getWorkbenchConfigListData(map);
		}
		return null;
	}
}
