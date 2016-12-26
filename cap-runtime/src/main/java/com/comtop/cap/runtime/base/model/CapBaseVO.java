/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.top.core.base.model.CoreVO;

/**
 * CAP基础VO类
 * 
 * @since JDK1.6
 * @author luozhenming
 * @version 2016年5月17日 许畅 修改
 */
public class CapBaseVO extends CoreVO {
    
    /** 用于session中缓存查询条件 排序字段 **/
    protected List<String> sortNames = new ArrayList<String>();
    
    /** 用于session中缓存查询条件 排序类型 **/
    protected List<String> sortTypes = new ArrayList<String>();
    
	/** FIXME */
	private static final long serialVersionUID = -7826860844554261022L;

	// 隔离使用
	/**
	 * 获取主键值
	 * 
	 * @return 主键值
	 */
	public String getPrimaryValue() {
		return CapRuntimeUtils.getId(this);
	}
    
    /**
     * @return 获取 sortNames属性值
     */
    public List<String> getSortNames() {
        return sortNames;
    }
    
    /**
     * @param sortNames 设置 sortNames 属性值为参数值 sortNames
     */
    public void setSortNames(List<String> sortNames) {
        this.sortNames = sortNames;
    }
    
    /**
     * @return 获取 sortTypes属性值
     */
    public List<String> getSortTypes() {
        return sortTypes;
    }
    
    /**
     * @param sortTypes 设置 sortTypes 属性值为参数值 sortTypes
     */
    public void setSortTypes(List<String> sortTypes) {
        this.sortTypes = sortTypes;
    }
    
}
