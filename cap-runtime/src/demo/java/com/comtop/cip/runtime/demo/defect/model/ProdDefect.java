
package com.comtop.cip.runtime.demo.defect.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 缺陷的主类
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "prod_defect")
public class ProdDefect {
    
    /**
     * id
     */
    @Id
    @Column(name = "defect_id")
    private int id;
    
    /**
     * 缺陷现象id
     */
    @Column(name = "defect_phenomenon_id")
    private int defectPhenomenonId;
    
    /**
     * 缺陷类型id
     */
    @Column(name = "defect_type_id")
    private int defectTypeId;
    
    /**
     * 损伤等级id
     */
    @Column(name = "defect_damage_level_id")
    private int defectDamageLevelId;
    
    /**
     * 缺陷代码
     */
    @Column(name = "defect_code")
    private String defectCode;
    
    /**
     * 功能位置代码
     */
    @Column(name = "function_location_code")
    private String functionLocationCode;
    
    /**
     * 功能位置名称
     */
    @Column(name = "function_location_name")
    private String functionLocationName;
    
    /**
     * 设备名称
     */
    @Column(name = "device_name")
    private String deviceName;
    
    /**
     * 设备分类id
     */
    @Column(name = "device_classify_id")
    private int deviceClassifyId;
    
    /**
     * 设备状态
     */
    @Column(name = "state")
    private int state;
    
    /**
     * 产品缺陷类型
     */
    private ProdDefectType prodDefectType;
    
    /**
     * 缺陷现象
     */
    private ProdDefectPhenomenon prodDefectPhenomenon;
    
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
     * @return 获取 defectPhenomenonId属性值
     */
    public int getDefectPhenomenonId() {
        return defectPhenomenonId;
    }
    
    /**
     * @param defectPhenomenonId 设置 defectPhenomenonId 属性值为参数值 defectPhenomenonId
     */
    public void setDefectPhenomenonId(int defectPhenomenonId) {
        this.defectPhenomenonId = defectPhenomenonId;
    }
    
    /**
     * @return 获取 defectTypeId属性值
     */
    public int getDefectTypeId() {
        return defectTypeId;
    }
    
    /**
     * @param defectTypeId 设置 defectTypeId 属性值为参数值 defectTypeId
     */
    public void setDefectTypeId(int defectTypeId) {
        this.defectTypeId = defectTypeId;
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
     * @return 获取 defectCode属性值
     */
    public String getDefectCode() {
        return defectCode;
    }
    
    /**
     * @param defectCode 设置 defectCode 属性值为参数值 defectCode
     */
    public void setDefectCode(String defectCode) {
        this.defectCode = defectCode;
    }
    
    /**
     * @return 获取 functionLocationCode属性值
     */
    public String getFunctionLocationCode() {
        return functionLocationCode;
    }
    
    /**
     * @param functionLocationCode 设置 functionLocationCode 属性值为参数值 functionLocationCode
     */
    public void setFunctionLocationCode(String functionLocationCode) {
        this.functionLocationCode = functionLocationCode;
    }
    
    /**
     * @return 获取 functionLocationName属性值
     */
    public String getFunctionLocationName() {
        return functionLocationName;
    }
    
    /**
     * @param functionLocationName 设置 functionLocationName 属性值为参数值 functionLocationName
     */
    public void setFunctionLocationName(String functionLocationName) {
        this.functionLocationName = functionLocationName;
    }
    
    /**
     * @return 获取 deviceName属性值
     */
    public String getDeviceName() {
        return deviceName;
    }
    
    /**
     * @param deviceName 设置 deviceName 属性值为参数值 deviceName
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
     * @return 获取 prodDefectPhenomenon属性值
     */
    public ProdDefectPhenomenon getProdDefectPhenomenon() {
        return prodDefectPhenomenon;
    }
    
    /**
     * @param prodDefectPhenomenon 设置 prodDefectPhenomenon 属性值为参数值 prodDefectPhenomenon
     */
    public void setProdDefectPhenomenon(ProdDefectPhenomenon prodDefectPhenomenon) {
        this.prodDefectPhenomenon = prodDefectPhenomenon;
    }
    
}
