/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.codegen.model;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.consistency.entity.util.EntityConsistencyUtil;
import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.bm.metadata.entity.model.ParameterVO;
import com.comtop.cap.bm.metadata.entity.model.QueryExtend;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 包装重写方法
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年6月3日 许畅 新建
 */
public final class WrapperOverrideMethod extends Observable {

	/** 实体方法 */
	private final MethodVO method;

	/** 导入类型观察者 */
	private final Observer observer;

	/** mybatis方法sql */
	private final String methodSQL;

	/** mybatis SQL Id */
	private final String selectId;

	/** mybatis参数类型 */
	private final String parameterType;

	/** mybatis返回值类型 */
	private final String resultType;

	/** mybatis完整语法 */
	private final String completeGrammar;

	/**
	 * 构造方法
	 * 
	 * @param method
	 *            实体方法
	 * @param observer
	 *            观察者对象
	 */
	public WrapperOverrideMethod(MethodVO method, Observer observer) {
		super();
		this.method = method;
		this.observer = observer;
		this.addObserver(observer);
		this.methodSQL = this.wrapperMethodSQL();
		this.selectId = this.wrapperSelectId();
		this.parameterType = this.wrapperParameterType();
		this.resultType = this.wrapperResultType();
		this.completeGrammar = this.wrapperCompleteGrammar();
	}

	/**
	 * 包装mybatis方法sql
	 * 
	 * @return mybatis方法sql
	 */
	private String wrapperMethodSQL() {
		QueryExtend query = method.getQueryExtend();
		if (query.getMybatisSQL() == null)
			return "";

		WrapperEntity entity = (WrapperEntity) observer;
		String wrapperSQL = query.getMybatisSQL().replace("${entity.table}",
				entity.getTable() == null ? "" : entity.getTable());
		return wrapperSQL.replace("${alias}",
				StringUtil.uncapitalize(entity.getEntityName()));
	}

	/**
	 * 包装mybatis selectId
	 * 
	 * @return selectId
	 */
	private String wrapperSelectId() {
		String selctId = method.getQueryExtend().getSelectId();
		WrapperEntity entity = (WrapperEntity) observer;
		if (StringUtil.isNotEmpty(selctId)) {
			return EntityConsistencyUtil.parsingExpression(selctId, entity.getEntityName());
		}
		return method.getEngName().replace("VO", entity.getEntityName());
	}

	/**
	 * 包装mybatis参数类型
	 * 
	 * @return mybatis参数类型
	 */
	private String wrapperParameterType() {
		String entityFullClassName = getCompleteClassName();
		List<ParameterVO> parameters = method.getParameters();
		if (parameters != null && parameters.size() > 0) {
			ParameterVO param = parameters.get(0);
			// 实体类型
			if ("entity".equals(param.getDataType().getType())) {
				return entityFullClassName;
			}
			// 第三方类型
			if ("thirdPartyType".equals(param.getDataType().getType())) {
				return param.getDataType().getValue();
			}
			return param.getDataType().getType();
		}
		return entityFullClassName;
	}

	/**
	 * 包装mybatis返回值
	 * 
	 * @return mybatis返回值
	 */
	private String wrapperResultType() {
		String entityFullClassName = getCompleteClassName();
		if (method.getReturnType() == null)
			return "";

		String type = method.getReturnType().getType();
		if ("void".equals(type)) {
			return "";
		} else if ("java.util.List".equals(type) || "entity".equals(type)) {
			// 实体和list类型
			return entityFullClassName;
		} else if ("thirdPartyType".equals(type)) {
			// 第三方类型
			return method.getReturnType().getValue();
		} else {
			return type;
		}
	}

	/***
	 * 包装一段完整的mybatis语法
	 * 
	 * @return mybatis完整的语法
	 */
	private String wrapperCompleteGrammar() {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"" + this.selectId + "\" parameterType=\""
				+ this.parameterType + "\" resultType=\"" + this.resultType
				+ "\" >");
		sb.append("\n");
		sb.append(this.methodSQL);
		sb.append("\n");
		sb.append("</select>");
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * 校验目标实体方法是否已重写
	 * 
	 * @param overrideMethods
	 *            重写的方法集合
	 * @param targetMethodVO
	 *            目标方法VO
	 * @return 是否已重写
	 */
	public static boolean hasOverride(
			List<WrapperOverrideMethod> overrideMethods, MethodVO targetMethodVO) {
		for (WrapperOverrideMethod overrideMethod : overrideMethods) {
			MethodVO sourceMethodVO = overrideMethod.getMethod();
			if (sourceMethodVO.getEngName().equals(targetMethodVO.getEngName())
					&& Arrays.equals(sourceMethodVO.getParameters().toArray(),
							targetMethodVO.getParameters().toArray())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过关联名称获取MethodVO
	 * 
	 * @param assosiationName
	 *            关联方法名
	 * @param pMethods
	 *            父类方法集合
	 * @return 关联的方法
	 */
	public static MethodVO getMethod(String assosiationName,
			List<WrapperOverrideMethod> pMethods) {
		for (WrapperOverrideMethod overrideMethod : pMethods) {
			if (assosiationName.equals(overrideMethod.getMethod().getAssoMethodName())) {
				return overrideMethod.getMethod();
			}
		}
		return null;
	}

	/**
	 * 获取实体完整类名称
	 * 
	 * @return Entity fullClassName
	 */
	private String getCompleteClassName() {
		WrapperEntity entity = (WrapperEntity) observer;
		return entity.getPackagePath() + ".model." + entity.getClassName();
	}

	/**
	 * @return the methodSQL
	 */
	public String getMethodSQL() {
		return methodSQL;
	}

	/**
	 * @return 实体方法
	 */
	public MethodVO getMethod() {
		return method;
	}

	/**
	 * @return the selectId
	 */
	public String getSelectId() {
		return selectId;
	}

	/**
	 * @return the parameterType
	 */
	public String getParameterType() {
		return parameterType;
	}

	/**
	 * @return the resultType
	 */
	public String getResultType() {
		return resultType;
	}

	/**
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}

	/**
	 * @return the completeGrammar
	 */
	public String getCompleteGrammar() {
		return completeGrammar;
	}

}
