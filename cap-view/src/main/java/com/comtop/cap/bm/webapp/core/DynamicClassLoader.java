/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.core;

import com.comtop.cip.script.org.mozilla.javascript.SecurityUtilities;

/**
 * 动态类加载器
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-27 李忠文
 */
public class DynamicClassLoader extends ClassLoader {
    
    /** 父加载器 */
    private final ClassLoader parentLoader;
    
    /**
     * 构造函数
     */
    public DynamicClassLoader() {
        parentLoader = getClass().getClassLoader();
    }
    
    /**
     * 构造函数
     * 
     * @param parentLoader 父加载器
     */
    public DynamicClassLoader(ClassLoader parentLoader) {
        this.parentLoader = parentLoader;
    }
    
    /**
     * 定义类
     * 
     * 
     * @param name 名称
     * @param data 二进制Class
     * @return 类
     */
    public Class<?> defineClass(String name, byte data[]) {
        return super.defineClass(name, data, 0, data.length, SecurityUtilities.getProtectionDomain(getClass()));
    }
    
    /**
     * 连接类
     * 
     * 
     * @param cl 类
     */
    public void linkClass(Class<?> cl) {
        resolveClass(cl);
    }
    
    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> cl = findLoadedClass(name);
        if (cl == null)
            if (parentLoader != null)
                cl = parentLoader.loadClass(name);
            else
                cl = findSystemClass(name);
        if (resolve)
            resolveClass(cl);
        return cl;
    }
}
