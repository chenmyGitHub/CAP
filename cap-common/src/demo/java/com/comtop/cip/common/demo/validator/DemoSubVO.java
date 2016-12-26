/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.validator;

import com.comtop.cip.common.demo.validator.custom.Script;
import com.comtop.cip.validator.javax.validation.constraints.NotNull;

/**
 * 实体类注解元数据
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-22 郑重
 */
// 类级别校验可以使用脚本语言编写逻辑做综合校验
@Script(script = "return input.min<input.max", message = "最大值不能小于最小值")
public class DemoSubVO {
    
    /** FIXME */
    private int min = 5;
    
    /** FIXME */
    private int max = 1;
    
    /** FIXME */
    @NotNull
    private String notNullValue;
    
    /**
     * @return the notNullValue
     */
    public String getNotNullValue() {
        return notNullValue;
    }
    
    /**
     * @param notNullValue the notNullValue to set
     */
    public void setNotNullValue(String notNullValue) {
        this.notNullValue = notNullValue;
    }
    
    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }
    
    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }
    
    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
}
