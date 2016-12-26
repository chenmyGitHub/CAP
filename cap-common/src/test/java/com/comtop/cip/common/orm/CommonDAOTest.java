/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.orm;

/**
 * CommonDAO的测试类
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-24 冯展
 */
public class CommonDAOTest {
    
    /**
     * 
     */
    private final CommonDAO<Company> commonDAO = new CommonDAO<Company>();
    
    /**
     * 
     * 测试Add方法
     * 
     */
    public void testInsert() {
        Company company = new Company();
        company.setActive(true);
        company.setName("company1");
        company.setNo(1L);
        commonDAO.insert(company);
    }
    
    /**
     * 
     * main function
     * 
     * @param args arguments
     */
    public static void main(String[] args) {
        new CommonDAOTest().testInsert();
    }
}
