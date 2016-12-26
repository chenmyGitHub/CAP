/******************************************************************************
 * Copyright (C) 2016 
 * ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。
 * 未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.opinion.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.base.facade.CapBaseFacade;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cap.runtime.opinion.appservice.CommonOpinionAppService;
import com.comtop.cap.runtime.opinion.model.CommonOpinionVO;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.util.StringUtil;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 流程常用意见业务逻辑处理类门面
 * 
 * @author 许畅
 * @since 1.0
 * @version 2016-4-12 许畅
 * @param <T>
 *            类泛型参数
 */
@SuppressWarnings("rawtypes")
@DwrProxy
@Service
public class CommonOpinionFacade<T extends CommonOpinionVO> extends
		CapBaseFacade {

	/** 注入AppService **/
	@PetiteInject
	protected CommonOpinionAppService commonOpinionAppService;

	/**
	 * @return CommonOpinionAppService
	 *
	 * @see com.comtop.cap.runtime.base.facade.CapBaseFacade#getAppService()
	 */
	@Override
	protected CapBaseAppService getAppService() {
		if (commonOpinionAppService == null) {
			commonOpinionAppService = BeanContextUtil.getBean(CommonOpinionAppService.class);
		}
		return commonOpinionAppService;
	}

	/**
	 * 列表界面数据查询
	 * 
	 * @param personid
	 *            员工id
	 * 
	 *            常用意见VO 查询条件
	 * @return map
	 */
	@SuppressWarnings("unchecked")
	@RemoteMethod
	public Map<String, Object> getListData(String personid) {
		CommonOpinionVO opinion = new CommonOpinionVO();
		opinion.setPersonId(personid);
		return queryVOListByPage(opinion);
	}

	/**
	 * 保存方法
	 * 
	 * @param vo
	 *            常用意见保存
	 * @return billId 实体id
	 */
	@SuppressWarnings("unchecked")
	@RemoteMethod
	public String saveAction(CommonOpinionVO vo) {
		String opinion = vo.getOpinion();
		String personid = vo.getPersonId();
		if (StringUtil.isNotEmpty(opinion) && StringUtil.isNotEmpty(personid)) {

			int count = commonOpinionAppService.queryCommonOpinionCount(vo);
			//意见内容重复则返回空
			if (count > 0 && StringUtil.isEmpty(vo.getId()))
				return null;
		}

		return super.save(vo);
	}

	/**
	 * 删除操作
	 * 
	 * @param ids
	 *            列表对象集合
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	@RemoteMethod
	public boolean deleteAction(List<String> ids) {
		List<CommonOpinionVO> list = new ArrayList<CommonOpinionVO>();
		for (String id : ids) {
			CommonOpinionVO opinion = new CommonOpinionVO();
			opinion.setId(id);
			list.add(opinion);
		}

		return deleteList(list);
	}

	/**
	 * @param id
	 *            实体id
	 * @return 加载页面数据
	 */
	@SuppressWarnings("unchecked")
	@RemoteMethod
	public T loadData(String id) {

		return (T) super.loadById(id);
	}

}