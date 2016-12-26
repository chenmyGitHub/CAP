/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import java.io.File;

import com.comtop.cip.jodd.io.findfile.FindFile;

/**
 * 
 * jodd 查找文件 工具类
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-19 柯尚福
 */
public class FindFileDemo {
    
    /**
     * 
     * 自定义一个文件查找类
     * 
     * @author 作者 柯尚福
     * @since 1.0
     * @version 2014-2-19 作者 柯尚福
     */
    static class MyFindFile extends FindFile<MyFindFile> {
        
        /**
         * 覆盖acceptFile() 方法,设置查找条件
         */
        @Override
        protected boolean acceptFile(File file) {
            
            // 查找jar包
            return file.getName().endsWith(".jar");
        }
    }
    
    /**
     * 查找文件例子（最简单的例子）
     * 
     */
    public static void findFile() {
        FindFile ff = new FindFile();
        ff.setRecursive(true); // 是否递归查找
        ff.setIncludeDirs(true); // 是否包含目录
        ff.sortByName(); // 按文件排序
        ff.searchPath("/some/path"); // 要查找的目录
        
        File f;
        while ((f = ff.nextFile()) != null) {
            if (f.isDirectory() == true) {
                System.out.println(". >" + f.getName());
            } else {
                System.out.println(". " + f.getName());
            }
        }
    }
    
    /**
     * 
     * 通过继承FindFile类，覆盖acceptFile（）方法来实现文件的查找
     * 
     */
    public static void customFindFile() {
        MyFindFile ff = new FindFileDemo.MyFindFile();
        ff.setRecursive(true); // 是否递归查找
        ff.setIncludeDirs(true); // 是否包含目录
        ff.searchPath("D:\\"); // 要查找的目录
        
        // 对所有查找到的符合条件的文件进行操作
        File f;
        while ((f = ff.nextFile()) != null) {
            System.out.println(f.getAbsolutePath());
        }
    }
    
    /**
     * 测试主方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        // 查找所有文件
        FindFileDemo.findFile();
        // 有条件的查找文件
        FindFileDemo.customFindFile();
    }
    
}
