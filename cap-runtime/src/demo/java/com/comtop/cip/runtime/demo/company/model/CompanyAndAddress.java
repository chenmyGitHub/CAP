
package com.comtop.cip.runtime.demo.company.model;

/**
 * 
 * 公司以及公司地址
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
public class CompanyAndAddress {
    
    /**
     * id
     */
    private String id;
    
    /**
     * 公司名称
     */
    private String name;
    
    /**
     * 公司编号
     */
    private Long no;
    
    /**
     * 激活标志
     */
    private Boolean active;
    
    /**
     * 详细地址信息
     */
    private String detail;
    
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
}
