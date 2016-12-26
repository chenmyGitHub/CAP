
package com.comtop.cip.runtime.demo.defect.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 缺陷位置
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "defect_position")
public class ProdDefectPosition {
    
    /**
     * id
     */
    @Id
    @Column(name = "defect_position_id")
    private int id;
    
    /**
     * 设备分类id
     */
    @Column(name = "device_classify_id")
    private int deviceClassifyId;
    
    /**
     * 缺陷位置id
     */
    @Column(name = "defect_position_code")
    private String defectPositionCode;
    
    /**
     * 缺陷位置名称
     */
    @Column(name = "defect_position_name")
    private String defectPositionName;
    
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
     * @return 获取 defectPositionCode属性值
     */
    public String getDefectPositionCode() {
        return defectPositionCode;
    }
    
    /**
     * @param defectPositionCode 设置 defectPositionCode 属性值为参数值 defectPositionCode
     */
    public void setDefectPositionCode(String defectPositionCode) {
        this.defectPositionCode = defectPositionCode;
    }
    
    /**
     * @return 获取 defectPositionName属性值
     */
    public String getDefectPositionName() {
        return defectPositionName;
    }
    
    /**
     * @param defectPositionName 设置 defectPositionName 属性值为参数值 defectPositionName
     */
    public void setDefectPositionName(String defectPositionName) {
        this.defectPositionName = defectPositionName;
    }
}
