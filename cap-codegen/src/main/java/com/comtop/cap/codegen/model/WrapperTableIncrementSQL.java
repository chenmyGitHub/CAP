/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.List;

import com.comtop.cap.bm.metadata.database.dbobject.model.ColumnCompareResult;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableCompareResult;
import com.comtop.cap.bm.metadata.database.util.TableUtils;

/**
 * 包装增量SQL信息
 * 
 * @author 林玉千
 * @since jdk1.6
 * @version 2016-9-27 林玉千 新建
 */
public class WrapperTableIncrementSQL {
    
    /**
     * 表比较结果对象
     */
    private final TableCompareResult objResult;
    
    /**
     * 
     * 构造函数
     * 
     * @param objResult 表比较结果对象
     */
    public WrapperTableIncrementSQL(TableCompareResult objResult) {
        this.objResult = objResult;
    }
    
    /**
     * @return 获取 tableCompareResult属性值
     */
    public TableCompareResult getObjResult() {
        return objResult;
    }
    
    /**
     * 判断表的描述是否更改
     * 
     * @return 表的描述是否改变的boolean值 true: 代表描述已改变 false:代表描述没有变化
     */
    public boolean isChangeDescription() {
        return TableUtils.isChangeDescription(objResult.getSrcTable(), objResult.getTargetTable());
    }
    
    /***
     * 
     * 判断表的索引是否更改
     * 
     * @return true: 代表索引已经被更改， false：代表索引没有变化
     */
    public boolean isChangeIndex() {
        return TableUtils.isChangeIndex(objResult.getSrcTable(), objResult.getTargetTable());
    }
    
    /**
     * 
     * 判断表的字段是否更改
     * 
     * @return true:代表存在字段被更改 ，false: 字段没有变化
     */
    public boolean isChangeCloumn() {
        List<ColumnCompareResult> lstColumnResult = objResult.getColumnResults();
        for (ColumnCompareResult objColumn : lstColumnResult) {
            if (objColumn.getResult() != ColumnCompareResult.COLUMN_EQUAL) {
                return true;
            }
        }
        return false;
    }
    
}
