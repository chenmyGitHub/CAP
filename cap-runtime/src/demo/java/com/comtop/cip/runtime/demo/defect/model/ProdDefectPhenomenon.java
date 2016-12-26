
package com.comtop.cip.runtime.demo.defect.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 缺陷现象
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "defect_phenomenon")
public class ProdDefectPhenomenon {
    
    /**
     * id
     */
    @Id
    @Column(name = "defect_phenomenon_id")
    private int id;
    
    /**
     * 缺陷类型
     */
    // defect_type_id
    private ProdDefectType prodDefectType;
    
    /**
     * 缺陷损伤等级
     */
    @Column(name = "defect_damage_level_id")
    private int defectDamageLevelId;
    
    /**
     * 设备分类id
     */
    @Column(name = "device_classify_id")
    private int deviceClassifyId;
    
    /**
     * 缺陷现象代码
     */
    @Column(name = "defect_phenomenon_code")
    private String defectPhenomenonCode;
    
    /**
     * 缺陷现象
     */
    @Column(name = "defect_phenomenon")
    private String defectPhenomenon;
    
    /**
     * 缺陷类型
     */
    @Column(name = "defect_type_code")
    private String defectTypeCode; // --> use defect_type_id?
    
    /**
     * 缺陷类型名称
     */
    @Column(name = "defect_type_name")
    private String defectTypeName;
    
    /**
     * 缺陷损伤等级
     */
    @Column(name = "defect_damage_level")
    private String defectDamageLevel;
    
    /**
     * 处理标志
     */
    @Column(name = "deal_flag")
    private int dealFlag;
    
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
     * @return 获取 prodDefectType属性值
     */
    public ProdDefectType getProdDefectType() {
        return prodDefectType;
    }
    
    /**
     * @param prodDefectType 设置 prodDefectType 属性值为参数值 prodDefectType
     */
    public void setProdDefectType(ProdDefectType prodDefectType) {
        this.prodDefectType = prodDefectType;
    }
    
    /**
     * @return 获取 defectDamageLevelId属性值
     */
    public int getDefectDamageLevelId() {
        return defectDamageLevelId;
    }
    
    /**
     * @param defectDamageLevelId 设置 defectDamageLevelId 属性值为参数值 defectDamageLevelId
     */
    public void setDefectDamageLevelId(int defectDamageLevelId) {
        this.defectDamageLevelId = defectDamageLevelId;
    }
    
    /**
     * @return 获取 deviceClassifyId属性值
     */
    public int getDeviceClassifyId() {
        return deviceClassifyId;
    }
    
    /**
     * @param deviceClassifyId 设置 deviceClassifyId 属性值为参数值 deviceClassifyId
     */
    public void setDeviceClassifyId(int deviceClassifyId) {
        this.deviceClassifyId = deviceClassifyId;
    }
    
    /**
     * @return 获取 defectPhenomenonCode属性值
     */
    public String getDefectPhenomenonCode() {
        return defectPhenomenonCode;
    }
    
    /**
     * @param defectPhenomenonCode 设置 defectPhenomenonCode 属性值为参数值 defectPhenomenonCode
     */
    public void setDefectPhenomenonCode(String defectPhenomenonCode) {
        this.defectPhenomenonCode = defectPhenomenonCode;
    }
    
    /**
     * @return 获取 defectPhenomenon属性值
     */
    public String getDefectPhenomenon() {
        return defectPhenomenon;
    }
    
    /**
     * @param defectPhenomenon 设置 defectPhenomenon 属性值为参数值 defectPhenomenon
     */
    public void setDefectPhenomenon(String defectPhenomenon) {
        this.defectPhenomenon = defectPhenomenon;
    }
    
    /**
     * @return 获取 defectTypeCode属性值
     */
    public String getDefectTypeCode() {
        return defectTypeCode;
    }
    
    /**
     * @param defectTypeCode 设置 defectTypeCode 属性值为参数值 defectTypeCode
     */
    public void setDefectTypeCode(String defectTypeCode) {
        this.defectTypeCode = defectTypeCode;
    }
    
    /**
     * @return 获取 defectTypeName属性值
     */
    public String getDefectTypeName() {
        return defectTypeName;
    }
    
    /**
     * @param defectTypeName 设置 defectTypeName 属性值为参数值 defectTypeName
     */
    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }
    
    /**
     * @return 获取 defectDamageLevel属性值
     */
    public String getDefectDamageLevel() {
        return defectDamageLevel;
    }
    
    /**
     * @param defectDamageLevel 设置 defectDamageLevel 属性值为参数值 defectDamageLevel
     */
    public void setDefectDamageLevel(String defectDamageLevel) {
        this.defectDamageLevel = defectDamageLevel;
    }
    
    /**
     * @return 获取 dealFlag属性值
     */
    public int getDealFlag() {
        return dealFlag;
    }
    
    /**
     * @param dealFlag 设置 dealFlag 属性值为参数值 dealFlag
     */
    public void setDealFlag(int dealFlag) {
        this.dealFlag = dealFlag;
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
