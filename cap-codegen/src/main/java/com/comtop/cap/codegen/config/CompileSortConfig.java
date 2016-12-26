/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.config;

/**
 * 
 * 代码层次配置
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class CompileSortConfig {
	
	/**指向的层Id*/
	private String refLayerId;
	
	/**编译顺序*/
	private int sortNo;

	/**
	 * @return the refLayerId
	 */
	public String getRefLayerId() {
		return refLayerId;
	}

	/**
	 * @param refLayerId the refLayerId to set
	 */
	public void setRefLayerId(String refLayerId) {
		this.refLayerId = refLayerId;
	}

	/**
	 * @return the sortNo
	 */
	public int getSortNo() {
		return sortNo;
	}

	/**
	 * @param sortNo the sortNo to set
	 */
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	
}
