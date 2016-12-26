/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.base.ce.CascadeController;
import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cap.runtime.base.model.CascadeVO;
import com.comtop.cap.runtime.base.util.CapRuntimeConstant;
import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.JSON;

/**
 * 
 * CAP门面类父类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月23日 龚斌
 * @param <T>
 *            xx
 */
public abstract class CapBaseFacade<T extends CapBaseVO> {

	/**
	 * 抽象获取级联链表
	 * 
	 * @return BaseAppService
	 */
	protected abstract CapBaseAppService<T> getAppService();

	/**
	 * 新增vo
	 * 
	 * @param vo
	 *            vo对象
	 * @return vo主键编码
	 */
	public String insert(T vo) {
		return getAppService().insert(vo);
	}

	/**
	 * 新增vo集合
	 * 
	 * @param voList
	 *            vo集合
	 * @return 新增集合个数
	 */
	public int insert(List<T> voList) {
		return getAppService().insert(voList);
	}

	/**
	 * 更新 vo
	 * 
	 * @param vo
	 *            vo对象
	 * @return 更新结果
	 */
	public boolean update(T vo) {
		return getAppService().update(vo);
	}

	/**
	 * 更新vo集合
	 * 
	 * @param voList
	 *            vo集合
	 * @return 更新集合个数
	 */
	public int update(List<T> voList) {
		return getAppService().update(voList);
	}

	/**
	 * 批量更新指定列值方法
	 * 
	 * @param voList
	 *            待修改的model list集合
	 * @param attributes
	 *            需要更新的VO属性值
	 * @return 成功修改的个数
	 */
	public int batchUpdate(List<T> voList, String[] attributes) {
		return getAppService().batchUpdate(voList, attributes);
	}

	/**
	 * 保存或更新VO，根据ID是否为空
	 * 
	 * @param vo
	 *            VO对象
	 * @return VO保存后的主键ID
	 */
	public String save(T vo) {
		return getAppService().save(vo);
	}

	/**
	 * 读取VO列表，分页查询--新版设计器使用
	 * 
	 * @param condition
	 *            查询条件
	 * @return VO列表
	 */
	public Map<String, Object> queryVOListByPage(T condition) {
		final Map<String, Object> ret = new HashMap<String, Object>(3);
		int count = getAppService().queryVOCount(condition);
		List<T> voList = null;
		if (count > 0) {
			condition.setPageNo(getRightPageNo(count, condition.getPageNo(),
					condition.getPageSize()));
			voList = getAppService().queryVOList(condition);
		}
		ret.put("list", voList);
		ret.put("count", count);
		ret.put("pageNo", condition.getPageNo());
		return ret;
	}

	/**
	 * 获取正确的pageNo
	 * 
	 * @param total
	 *            总记录数
	 * @param pageNo
	 *            当前传进来的pageNo
	 * @param pageSize
	 *            每页记录数
	 * @return 页码
	 */
	protected int getRightPageNo(int total, int pageNo, int pageSize) {
		if (pageSize == 0) {
			throw new RuntimeException("pageSize设置不正确");
		}

		int totalPageNo = total / pageSize;
		if (total % pageSize > 0) {
			totalPageNo++;
		}

		return pageNo > totalPageNo ? totalPageNo : pageNo;
	}

	/**
	 * 删除 vo
	 * 
	 * @param vo
	 *            vo对象
	 * @return 删除结果
	 */
	public boolean delete(T vo) {
		return getAppService().delete(vo);
	}

	/**
	 * 删除VO集合
	 * 
	 * @param voList
	 *            VO对象集合
	 * @return 删除结果
	 */
	public boolean deleteList(List<T> voList) {
		return getAppService().deleteList(voList);
	}

	/**
	 * 读取VO对象
	 * 
	 * @param vo
	 *            VO对象
	 * @return VO对象
	 */
	public T load(T vo) {
		return getAppService().load(vo);
	}

	/**
	 * 根据主键读取VO对象
	 * 
	 * @param id
	 *            主键
	 * @return VO对象
	 */
	public T loadById(String id) {
		return getAppService().loadVOById(id);
	}

	/**
	 * 读取VO列表
	 * 
	 * @param condition
	 *            查询条件
	 * @return VO列表
	 */
	public List<T> queryVOList(T condition) {
		return getAppService().queryVOList(condition);
	}

	/**
	 * 读取VO数据条数
	 * 
	 * @param condition
	 *            查询条件
	 * @return VO数据条数
	 */
	public int queryVOCount(T condition) {
		return getAppService().queryVOCount(condition);
	}

	/**
	 * 读取VO列表
	 * 
	 * @param condition
	 *            查询条件
	 * @return VO列表
	 */
	public List<T> queryVOListByCondition(T condition) {
		return getAppService().queryVOListByCondition(condition);
	}

	/**
	 * 查询VO列表数据(非分页查询)
	 * 
	 * @param condition
	 *            查询条件
	 * @return 列表数据
	 */
	public List<T> queryVOListNoPaging(T condition) {
		return getAppService().queryVOListNoPaging(condition);
	}

	/**
	 * 级联新增vo对象
	 * 
	 * @param vo
	 *            待插入级联vo对象
	 * @param lstCascadeVO
	 *            级联链表
	 * @return vo编码
	 */
	public String insertCascadeVO(T vo, List<CascadeVO> lstCascadeVO) {
		// 先插入自身VO对象
		String voId = this.insert(vo);
		// 级联插入当前实体的关联实体属性
		if (lstCascadeVO == null || lstCascadeVO.size() == 0) {
			return voId;
		}
		// CapRuntimeUtils级联插入实体
		CascadeController.getInstance().run(vo, lstCascadeVO,
				CapRuntimeConstant.INSERT_OPE_TYPE);
		return voId;

	}

	/**
	 * 级联更新 vo
	 * 
	 * @param vo
	 *            vo对象
	 * @param lstCascadeVO
	 *            lstCascade
	 * @return 更新结果
	 */
	public boolean updateCascadeVO(T vo, List<CascadeVO> lstCascadeVO) {
		// 获取方法默认级联性链表结构
		if (lstCascadeVO != null) {
			CascadeController.getInstance().run(vo, lstCascadeVO,
					CapRuntimeConstant.DELETE_OPE_TYPE);
			CascadeController.getInstance().run(vo, lstCascadeVO,
					CapRuntimeConstant.INSERT_OPE_TYPE);
		}
		return getAppService().update(vo);
	}

	/**
	 * 级联保存或更新VO（根据ID是否为空）
	 * 
	 * @param vo
	 *            VO对象
	 * @param lstCascadeVO
	 *            级联对象集合
	 * @return 保存后的主键ID
	 */
	public String saveCascadeVO(T vo, List<CascadeVO> lstCascadeVO) {
		String voId = CapRuntimeUtils.getId(vo);
		if (StringUtil.isEmpty(voId)) {
			voId = insertCascadeVO(vo, lstCascadeVO);
		} else {
			updateCascadeVO(vo, lstCascadeVO);
		}
		return voId;
	}

	/**
	 * 删除 vo
	 * 
	 * @param vo
	 *            vo对象
	 * @param lstCascadeVO
	 *            级联链表
	 * @return 删除结果
	 */
	public boolean deleteCascadeVO(T vo, List<CascadeVO> lstCascadeVO) {
		// 获取方法默认级联性链表结构
		if (lstCascadeVO != null) {
			CascadeController.getInstance().run(vo, lstCascadeVO,
					CapRuntimeConstant.DELETE_OPE_TYPE);
		}
		return getAppService().delete(vo);
	}

	/**
	 * 删除VO集合
	 * 
	 * @param voList
	 *            VO对象集合
	 * @param lstCascadeVO
	 *            级联链表
	 * @return 删除结果
	 */
	public boolean deleteCascadeList(List<T> voList,
			List<CascadeVO> lstCascadeVO) {
		if (voList != null && voList.size() > 0) {
			// 获取方法默认级联性链表结构
			if (lstCascadeVO != null) { // 级联删除
				for (T vo : voList) {
					CascadeController.getInstance().run(vo, lstCascadeVO,
							CapRuntimeConstant.DELETE_OPE_TYPE);
					getAppService().delete(vo);
				}
			} else { // 非级联删除
				deleteList(voList);
			}
		}
		return true;
	}

	/**
	 * 根据主键 级联性的读取VO对象
	 * 
	 * @param vo
	 *            VO对象
	 * @param lstCascadeVO
	 *            级联链表
	 * @return VO对象
	 */
	public T loadCascadeById(T vo, List<CascadeVO> lstCascadeVO) {
		// T vo = this.loadById(id);
		// 级联插入当前实体的关联实体属性
		if (lstCascadeVO != null && lstCascadeVO.size() != 0) {
			CascadeController.getInstance().run(vo, lstCascadeVO,
					CapRuntimeConstant.LOADBYID_OPE_TYPE);
		}
		return vo;
	}

	/**
	 * 
	 * 根据级联字符串集合获取级联对象集合
	 * 
	 * @param lstStrCscade
	 *            级联字符串集合
	 * @return 级联对象集合
	 */
	protected List<CascadeVO> getCascadeVOList(List<String> lstStrCscade) {
		List<CascadeVO> lstCascadeVO = new ArrayList<CascadeVO>();
		if (lstStrCscade == null || lstStrCscade.size() == 0) {
			return lstCascadeVO;
		}
		CascadeVO objCascadeVO = null;
		// 将json数组 转换成 List<CascadeVO>泛型
		for (String str : lstStrCscade) {
			objCascadeVO = JSON.parseObject(str, CascadeVO.class);
			if (objCascadeVO != null) {
				lstCascadeVO.add(objCascadeVO);
			}
		}

		return lstCascadeVO;
	}

}
