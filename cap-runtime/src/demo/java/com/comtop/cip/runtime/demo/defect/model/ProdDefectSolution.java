
package com.comtop.cip.runtime.demo.defect.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 缺陷方案
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "defect_solution")
public class ProdDefectSolution {
    
    /**
     * id
     */
    @Id
    @Column(name = "defect_solution_id")
    private int id;
    
    /**
     * 缺陷id
     */
    @Column(name = "defect_id")
    private int defectId;
    
    /**
     * 缺陷
     */
    private ProdDefect prodDefect;
    
    /**
     * 缺陷描述
     */
    @Column(name = "disposal_descr")
    private String disposalDescr;
    
    /**
     * @return 获取 id属性值
     */
    public int getId() {
        return id;
    }
    
    /**
     * @param id 设置 id 属性值为参数值 id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return 获取 defectId属性值
     */
    public int getDefectId() {
        return defectId;
    }
    
    /**
     * @param defectId 设置 defectId 属性值为参数值 defectId
     */
    public void setDefectId(int defectId) {
        this.defectId = defectId;
    }
    
    /**
     * @return 获取 prodDefect属性值
     */
    public ProdDefect getProdDefect() {
        return prodDefect;
    }
    
    /**
     * @param prodDefect 设置 prodDefect 属性值为参数值 prodDefect
     */
    public void setProdDefect(ProdDefect prodDefect) {
        this.prodDefect = prodDefect;
    }
    
    /**
     * @return 获取 disposalDescr属性值
     */
    public String getDisposalDescr() {
        return disposalDescr;
    }
    
    /**
     * @param disposalDescr 设置 disposalDescr 属性值为参数值 disposalDescr
     */
    public void setDisposalDescr(String disposalDescr) {
        this.disposalDescr = disposalDescr;
    }
}
