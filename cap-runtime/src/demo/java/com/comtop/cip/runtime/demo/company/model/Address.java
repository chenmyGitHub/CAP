
package com.comtop.cip.runtime.demo.company.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 公司地址
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "address")
public class Address {
    
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    private String id;
    
    /**
     * 详细地址
     */
    @Column(name = "detail")
    private String detail;
    
    /**
     * 公司id
     */
    @Column(name = "company_id")
    private String companyId;
    
    /**
     * @return 获取 id属性值
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id 设置 id 属性值为参数值 id
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return 获取 detail属性值
     */
    public String getDetail() {
        return detail;
    }
    
    /**
     * @param detail 设置 detail 属性值为参数值 detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    /**
     * @return 获取 companyId属性值
     */
    public String getCompanyId() {
        return companyId;
    }
    
    /**
     * @param companyId 设置 companyId 属性值为参数值 companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
