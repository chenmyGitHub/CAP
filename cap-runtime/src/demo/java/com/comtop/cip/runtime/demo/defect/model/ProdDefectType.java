
package com.comtop.cip.runtime.demo.defect.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 缺陷类型
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "defect_type")
public class ProdDefectType {
    
    /**
     * id
     */
    @Id
    @Column(name = "defect_type_id")
    private int id;
    
    /**
     * 缺限类型代码
     */
    @Column(name = "defect_type_code")
    private String defectTypeCode;
    
    /**
     * 缺陷类型名称
     */
    @Column(name = "defect_type_name")
    private String defectTypeName;
    
    /**
     * 设备分类id
     */
    @Column(name = "device_classify_id")
    private int deviceClassifyId;
    
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
}
