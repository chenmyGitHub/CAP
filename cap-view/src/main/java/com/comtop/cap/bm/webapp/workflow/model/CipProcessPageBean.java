/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.workflow.model;

import java.util.List;

/**
 * 分页实体
 * 
 * 
 * @author 李小强
 * @since 1.0
 * @version 2014-11-24 李小强
 */
public class CipProcessPageBean {
    
    /** 数据列表 */
    private List<CIPProcessBean> valueList;
    
    /** 数据总条数 */
    private int allRows = 0;
    
    /**
     * @return 获取 valueList属性值
     */
    public List<CIPProcessBean> getValueList() {
        return valueList;
    }
    
    /**
     * @param valueList 设置 valueList 属性值为参数值 valueList
     */
    public void setValueList(List<CIPProcessBean> valueList) {
        this.valueList = valueList;
    }
    
    /**
     * @return 获取 allRows属性值
     */
    public int getAllRows() {
        return allRows;
    }
    
    /**
     * @param allRows 设置 allRows 属性值为参数值 allRows
     */
    public void setAllRows(int allRows) {
        this.allRows = allRows;
    }
    
}
