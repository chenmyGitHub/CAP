/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.validator;

import java.util.Iterator;
import java.util.Set;

import com.comtop.cip.validator.javax.validation.ConstraintViolation;
import com.comtop.cip.validator.javax.validation.Validation;
import com.comtop.cip.validator.javax.validation.Validator;
import com.comtop.cip.validator.javax.validation.ValidatorFactory;

/**
 * 实体类注解元数据
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-22 郑重
 */
public class Test {
    
    /** FIXME */
    private static Validator validator;
    
    /**
     * 构造函数
     */
    public Test() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    /**
     * 验证VO对象
     */
    public void validatorVO() {
        System.out.println("-----------验证VO对象----------------");
        DemoVO objDemoVO = new DemoVO();
        Set<ConstraintViolation<DemoVO>> constraintViolations = validator.validate(objDemoVO);
        Iterator<ConstraintViolation<DemoVO>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<DemoVO> type = iterator.next();
            System.out.println(type.getPropertyPath() + ":" + type.getMessage());
        }
    }
    
    /**
     * 验证VO对象字段值
     */
    public void validatorVOFiledValue() {
        System.out.println("-----------验证VO对象字段值----------------");
        DemoVO objDemoVO = new DemoVO();
        Set<ConstraintViolation<DemoVO>> constraintViolations = validator.validateProperty(objDemoVO, "notNullValue");
        Iterator<ConstraintViolation<DemoVO>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<DemoVO> type = iterator.next();
            System.out.println(type.getPropertyPath() + ":" + type.getMessage());
        }
    }
    
    /**
     * 根据VO类注解信息验证字段值
     */
    public void validatorVOClassFiledValue() {
        System.out.println("-----------根据VO类注解信息验证字段值----------------");
        
        //validateValue() 方法,你能够校验如果把一个特定的值赋给一个类的某一个属性的话,是否会违反此类中定义的约束条件.
        Set<ConstraintViolation<DemoVO>> constraintViolations = validator.validateValue(DemoVO.class, "notNullValue",
            null);
        Iterator<ConstraintViolation<DemoVO>> iterator = constraintViolations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<DemoVO> type = iterator.next();
            System.out.println(type.getPropertyPath() + ":" + type.getMessage());
        }
    }
    
    /**
     * @param args arg
     */
    public static void main(String[] args) {
        Test objTest = new Test();
        objTest.validatorVO();
        objTest.validatorVOFiledValue();
        objTest.validatorVOClassFiledValue();
    }
}
