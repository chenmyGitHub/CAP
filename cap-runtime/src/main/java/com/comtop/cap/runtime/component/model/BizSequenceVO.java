/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cip.validator.org.hibernate.validator.constraints.Length;
import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * 业务序列
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-26 李忠文
 */
@DataTransferObject
@Table(name = "CAP_RT_BIZ_SEQUENCE")
public class BizSequenceVO extends CapBaseVO {
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** ID */
    @Id
    @Length(max = 32)
    @Column(name = "SEQ_ID", length = 32)
    private String id;
    
    /** KEY */
    @Column(name = "SEQ_KEY", length = 200)
    private String key;
    
    /** 最后的值 */
    @Column(name = "SEQ_LAST")
    private Integer last;
    
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
     * @return 获取 key属性值
     */
    public String getKey() {
        return key;
    }
    
    /**
     * @param key 设置 key 属性值为参数值 key
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * @return 获取 last属性值
     */
    public Integer getLast() {
        return last;
    }
    
    /**
     * @param last 设置 last 属性值为参数值 last
     */
    public void setLast(Integer last) {
        this.last = last;
    }
    
}
