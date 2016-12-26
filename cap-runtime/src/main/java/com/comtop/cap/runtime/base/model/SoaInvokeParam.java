/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;


import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * @author luozhenming
 *
 */
@DataTransferObject
public class SoaInvokeParam {
	
	/**
	 * 
	 */
	private String soaServiceId;

	/**
	 * 
	 */
	private String[] paramJosn;
	
	
	/***
	 * 
	 */
	private String facadeSeviceFullName;

	/**
	 * @return the soaServiceId
	 */
	public String getSoaServiceId() {
		return soaServiceId;
	}

	/**
	 * @return the facadeSeviceFullName
	 */
	public String getFacadeSeviceFullName() {
		return facadeSeviceFullName;
	}

	/**
	 * @param facadeSeviceFullName the facadeSeviceFullName to set
	 */
	public void setFacadeSeviceFullName(String facadeSeviceFullName) {
		this.facadeSeviceFullName = facadeSeviceFullName;
	}

	/**
	 * @param soaServiceId the soaServiceId to set
	 */
	public void setSoaServiceId(String soaServiceId) {
		this.soaServiceId = soaServiceId;
	}

	/**
	 * @return the paramJosn
	 */
	public String[] getParamJosn() {
		return paramJosn;
	}

	/**
	 * @param paramJosn the paramJosn to set
	 */
	public void setParamJosn(String[] paramJosn) {
		this.paramJosn = paramJosn;
	}

}
