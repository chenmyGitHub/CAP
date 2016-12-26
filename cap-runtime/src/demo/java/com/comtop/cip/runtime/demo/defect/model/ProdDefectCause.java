
package com.comtop.cip.runtime.demo.defect.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 缺陷原因
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "defect_cause")
public class ProdDefectCause {
    
    /**
     * id
     */
    @Id
    @Column(name = "defect_cause_id")
    private int id;
    
    /**
     * 缺陷分类id
     */
    @Column(name = "defect_cause_classify_id")
    private int defectCauseClassifyId;
    
    /**
     * 缺陷原因代码
     */
    @Column(name = "defect_cause_classify_code")
    private String defectCauseCode;
    
    /**
     * 缺陷原因名称
     */
    @Column(name = "defect_cause_name")
    private String defectCauseName;
    
    /**
     * 状态
     */
    @Column(name = "state")
    private int state;
    
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
     * @return 获取 defectCauseClassifyId属性值
     */
    public int getDefectCauseClassifyId() {
        return defectCauseClassifyId;
    }
    
    /**
     * @param defectCauseClassifyId 设置 defectCauseClassifyId 属性值为参数值 defectCauseClassifyId
     */
    public void setDefectCauseClassifyId(int defectCauseClassifyId) {
        this.defectCauseClassifyId = defectCauseClassifyId;
    }
    
    /**
     * @return 获取 defectCauseCode属性值
     */
    public String getDefectCauseCode() {
        return defectCauseCode;
    }
    
    /**
     * @param defectCauseCode 设置 defectCauseCode 属性值为参数值 defectCauseCode
     */
    public void setDefectCauseCode(String defectCauseCode) {
        this.defectCauseCode = defectCauseCode;
    }
    
    /**
     * @return 获取 defectCauseName属性值
     */
    public String getDefectCauseName() {
        return defectCauseName;
    }
    
    /**
     * @param defectCauseName 设置 defectCauseName 属性值为参数值 defectCauseName
     */
    public void setDefectCauseName(String defectCauseName) {
        this.defectCauseName = defectCauseName;
    }
    
    /**
     * @return 获取 state属性值
     */
    public int getState() {
        return state;
    }
    
    /**
     * @param state 设置 state 属性值为参数值 state
     */
    public void setState(int state) {
        this.state = state;
    }
    
}
