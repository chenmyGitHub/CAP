
package com.comtop.cip.common.orm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.comtop.cip.validator.javax.validation.constraints.AssertTrue;
import com.comtop.cip.validator.javax.validation.constraints.Max;
import com.comtop.cip.validator.javax.validation.constraints.Min;
import com.comtop.cip.validator.javax.validation.constraints.NotNull;
import com.comtop.cip.validator.javax.validation.constraints.Pattern;

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
    @NotNull
    @Pattern(regexp = "^[\u4e00-\u9fa5]{0,}$", message = "只能输入中文")
    private String name;
    
    /**
     * 编号
     */
    @Column(name = "no")
    @Max(5)
    @Min(2)
    private Long no;
    
    /**
     * 激活
     */
    @Column(name = "active")
    @AssertTrue
    private Boolean active;
    
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
}
