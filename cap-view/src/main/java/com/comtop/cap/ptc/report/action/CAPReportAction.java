/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.ptc.report.action;

import com.comtop.cap.ptc.report.facade.ICAPReportFacade;
import com.comtop.cap.ptc.report.model.ReportCondition;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * ReportAction
 * 
 * @author 杨赛
 * @since 1.0
 * @version 2015-9-23 杨赛
 */
@DwrProxy
public class CAPReportAction {
	
	/** 注入ReportFacade **/
    @PetiteInject("CAPReportFacade")
	protected ICAPReportFacade reportFacade; 
	/**
     * 查询系统整体资源统计数据
     * 
     * @return ReportVO 统计数据
     */
	@RemoteMethod
	public Object querySystemIntegralityReportVO() {
		return reportFacade.querySystemIntegralityReportVO();
	}
	
	/**
	 * 根据条件查询阶段性资源统计数据
	 * @param reportCondition 查询条件
	 * @return 统计数据  key为月份 或者 季度
	 */
	@RemoteMethod
	public Object queryPhasedReportVO(ReportCondition reportCondition) {
		return reportFacade.queryPhasedReportVO(reportCondition);
	}
	
	/**
	 * 根据条件查询对应模块资源统计数据
	 * @param reportCondition 查询条件
	 * @return 统计数据  key模块名
	 */
	@RemoteMethod
	public Object queryModuleReportVO(ReportCondition reportCondition) {
		return reportFacade.queryModuleReportVO(reportCondition);
	}
}
