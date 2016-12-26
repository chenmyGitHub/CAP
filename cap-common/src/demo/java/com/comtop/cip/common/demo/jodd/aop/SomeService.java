/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.aop;

/**
 * 
 * 服务类（用于aop测试）
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-17 柯尚福
 */
public class SomeService {
    
    /**
     * 
     * 执行某些业务操作，并记录日志
     * 
     * @return 返回操作结果
     * 
     */
    @Log(message = "hello")
    public String doSomething() {
        System.out.println("do some service....");
        return "ok";
    }
}
