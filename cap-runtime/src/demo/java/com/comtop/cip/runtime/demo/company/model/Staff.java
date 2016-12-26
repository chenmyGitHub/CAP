
package com.comtop.cip.runtime.demo.company.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 员工信息
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "staff")
public class Staff {
    
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    private String id;
    
    /**
     * 员工姓名
     */
    @Column(name = "name")
    private String name;
    
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
     * @return 获取 name属性值
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name 设置 name 属性值为参数值 name
     */
    public void setName(String name) {
        this.name = name;
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
