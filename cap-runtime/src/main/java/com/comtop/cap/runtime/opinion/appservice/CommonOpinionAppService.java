/******************************************************************************
 * Copyright (C) 2016 
 * ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。
 * 未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.opinion.appservice;

import java.util.List;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.opinion.model.CommonOpinionVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;

/**
 * 流程常用意见 业务逻辑处理类
 * 
 * @author 许畅
 * @since 1.0
 * @version 2016-4-12 许畅
 * @param <T>
 *            类泛型参数
 */
@PetiteBean
public class CommonOpinionAppService<T extends CommonOpinionVO> extends
		CapBaseAppService<CommonOpinionVO> {

	/** 查询常用意见数量 */
	private static final String QUERY_COUNT_ID = "com.comtop.cap.runtime.opinion.model.queryOpinionCountByPerson";

	/** 查询常用意见数量(列表查询) */
	private static final String QUERY_LIST_ID = "com.comtop.cap.runtime.opinion.model.queryOpinionListByPerson";

	/** 查询常用意见数量 (内容重复校验) */
	private static final String QUERY_COMMONOPINION_COUNT = "com.comtop.cap.runtime.opinion.model.queryOpinionCount";

	/**
	 * @param condition
	 *            条件
	 * @return int
	 *
	 * @see com.comtop.cap.runtime.base.appservice.CapBaseAppService#queryVOCount(com.comtop.cap.runtime.base.model.CapBaseVO)
	 */
	  @Override
	public int queryVOCount(CommonOpinionVO condition) {
		return ((Integer) capBaseCommonDAO.selectOne(QUERY_COUNT_ID, condition))
				.intValue();
	}

	/**
	 * @param condition
	 *            查询条件
	 * @return count
	 */
	public int queryCommonOpinionCount(CommonOpinionVO condition) {
		return ((Integer) capBaseCommonDAO.selectOne(QUERY_COMMONOPINION_COUNT,
				condition)).intValue();
	}

	/**
	 * @param condition
	 *            条件
	 * @return lst
	 *
	 * @see com.comtop.cap.runtime.base.appservice.CapBaseAppService#queryVOList(com.comtop.cap.runtime.base.model.CapBaseVO)
	 */
	@SuppressWarnings("unchecked")
	  @Override
	public List<CommonOpinionVO> queryVOList(CommonOpinionVO condition) {
		return capBaseCommonDAO.queryList(QUERY_LIST_ID, condition,
				condition.getPageNo(), condition.getPageSize());
	}
}