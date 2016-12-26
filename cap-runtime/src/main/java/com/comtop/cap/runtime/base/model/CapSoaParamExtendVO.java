/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

/**
 * @author luozhenming
 *
 */
public class CapSoaParamExtendVO extends CapBaseVO {
	
    /** FIXME */
    private static final long serialVersionUID = -4094382421517595294L;

    /***/
	private String soaSid;
	
	/***/
	private String paramCode;
	
	/***/
	private String paramTypeFullName;
	
	/**1 传入参数 2 返回参数*/
	private int paramIOType;
	
	/***/
	private int seq;
	
	/***/
	private String soaParamType;

	/**
	 * @return the soaParamType
	 */
	public String getSoaParamType() {
		return soaParamType;
	}

	/**
	 * @param soaParamType the soaParamType to set
	 */
	public void setSoaParamType(String soaParamType) {
		this.soaParamType = soaParamType;
	}

	/**
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}

	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}

	/**
	 * @return the soaSid
	 */
	public String getSoaSid() {
		return soaSid;
	}

	/**
	 * @param soaSid the soaSid to set
	 */
	public void setSoaSid(String soaSid) {
		this.soaSid = soaSid;
	}

	/**
	 * @return the paramCode
	 */
	public String getParamCode() {
		return paramCode;
	}

	/**
	 * @param paramCode the paramCode to set
	 */
	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	/**
	 * @return the paramTypeFullName
	 */
	public String getParamTypeFullName() {
		return paramTypeFullName;
	}

	/**
	 * @param paramTypeFullName the paramTypeFullName to set
	 */
	public void setParamTypeFullName(String paramTypeFullName) {
		this.paramTypeFullName = paramTypeFullName;
	}

	/**
	 * @return the paramIOType
	 */
	public int getParamIOType() {
		return paramIOType;
	}

	/**
	 * @param paramIOType the paramIOType to set
	 */
	public void setParamIOType(int paramIOType) {
		this.paramIOType = paramIOType;
	}
}
