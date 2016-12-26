/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.validator;

/**
 * 模拟用户DAO
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-23 郑重
 */
public class UserDAO {
    
    /**
     * 此用户是否已经注册
     * 
     * @param userName 用户名
     * 
     * @return boolean
     */
    public boolean isRegister(String userName) {
        System.out.println(userName);
        return false;
    }
}
