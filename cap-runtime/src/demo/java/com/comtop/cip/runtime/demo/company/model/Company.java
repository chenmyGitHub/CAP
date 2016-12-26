
package com.comtop.cip.runtime.demo.company.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * 公司信息
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@Table(name = "company")
public class Company {
    
    /**
     * id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    private String id;
    
    /**
     * 名称
     */
    @Column(name = "name")
    private String name;
    
    /**
     * 编号
     */
    @Column(name = "no")
    private Long no;
    
    /**
     * 激活
     */
    @Column(name = "active")
    private Boolean active;
    
    /**
     * 地址
     */
    private Address address;
    
    /**
     * 员工列表
     */
    private List<Staff> staffs;
    
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
     * @return 获取 no属性值
     */
    public Long getNo() {
        return no;
    }
    
    /**
     * @param no 设置 no 属性值为参数值 no
     */
    public void setNo(Long no) {
        this.no = no;
    }
    
    /**
     * @return 获取 active属性值
     */
    public Boolean getActive() {
        return active;
    }
    
    /**
     * @param active 设置 active 属性值为参数值 active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    /**
     * @return 获取 address属性值
     */
    public Address getAddress() {
        return address;
    }
    
    /**
     * @param address 设置 address 属性值为参数值 address
     */
    public void setAddress(Address address) {
        this.address = address;
    }
    
    /**
     * @return 获取 staffs属性值
     */
    public List<Staff> getStaffs() {
        return staffs;
    }
    
    /**
     * @param staffs 设置 staffs 属性值为参数值 staffs
     */
    public void setStaffs(List<Staff> staffs) {
        this.staffs = staffs;
    }
}
