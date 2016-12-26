/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.bm.metadata.database.dbobject.model.ColumnVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.Index;
import com.comtop.cap.bm.metadata.database.dbobject.model.IndexColumnVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableIndexVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableVO;

/**
 * 包装Table信息
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年9月22日 许畅 新建
 */
public final class WrapperTableSQL {
    
    /** table表信息 */
    private final TableVO tableVO;
    
    /** 主键 */
    private final String pk;
    
    /** 索引 */
    private final List<Index> indexes;
    
    /**
     * 构造方法
     * 
     * @param tableVO
     *            表信息
     */
    public WrapperTableSQL(TableVO tableVO) {
        this.tableVO = tableVO;
        this.pk = wrapperPrimarykey(tableVO);
        this.indexes = wrapperIndex(tableVO);
    }
    
    /**
     * 包装索引
     * 
     * @param t
     *            表信息
     * @return 索引
     */
    private List<Index> wrapperIndex(TableVO t) {
        List<Index> lst = new ArrayList<Index>();
        List<TableIndexVO> indexs = t.getIndexs();
        // 去除主键默认索引
        for (TableIndexVO index : indexs) {
            if (index.isUnique()) {
                continue;
            }
            
            List<IndexColumnVO> columns = index.getColumns();
            Index idx = new Index();
            String columnName = "";
            for (int i = 0; i < columns.size(); i++) {
                if (i == columns.size() - 1) {
                    columnName += columns.get(i).getColumn().getCode();
                } else {
                    columnName += columns.get(i).getColumn().getCode() + ",";
                }
            }
            idx.setColumnName(columnName);
            idx.setIndexName(index.getEngName());
            lst.add(idx);
        }
        return lst;
    }
    
    /**
     * 
     * 包装主键
     * 
     * @param t
     *            表信息
     * @return 主键
     */
    private String wrapperPrimarykey(TableVO t) {
        List<ColumnVO> columns = t.getColumns();
        for (ColumnVO column : columns) {
            if (column.getIsPrimaryKEY()) {
                return column.getCode();
            }
        }
        return null;
    }
    
    /**
     * 是否存在主键
     * 
     * @return true 是，false 否
     */
    public boolean existsPrimarykey() {
        List<ColumnVO> columns = tableVO.getColumns();
        boolean flag = false;
        for (ColumnVO column : columns) {
            if (column.getIsPrimaryKEY()) {
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * @return the tableVO
     */
    public TableVO getTableVO() {
        return tableVO;
    }
    
    /**
     * @return the pk
     */
    public String getPk() {
        return pk;
    }
    
    /**
     * @return the indexes
     */
    public List<Index> getIndexes() {
        return indexes;
    }
    
}
