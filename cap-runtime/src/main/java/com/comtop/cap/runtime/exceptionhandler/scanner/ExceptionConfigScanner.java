/******************************************************************************
 * Copyright (C) 2014  ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.exceptionhandler.scanner;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cip.jodd.io.findfile.ClassFinder;
import com.comtop.cip.jodd.madvoc.MadvocException;
import com.comtop.cip.jodd.util.ClassLoaderUtil;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * 扫描异常配置文件并进行初始化
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-25 柯尚福
 */
public class ExceptionConfigScanner extends ClassFinder {
    
    /** 异常配置文件后缀 */
    private static final String CONFIG_FILE_EXT = "exception.xml";
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionConfigScanner.class);
    
    /**
     * 构造函数
     */
    public ExceptionConfigScanner() {
        super();
        this.setIncludeResources(true);
    }
    
    /**
	 * 
	 */
    public void configure() {
        configure(ClassLoaderUtil.getDefaultClasspath());
    }
    
    /**
     * 扫描构建路径下的配置文件
     * 
     * @param classpath 类编译路径
     */
    public void configure(File[] classpath) {
        
        try {
            scanPaths(classpath);
        } catch (Exception ex) {
            throw new MadvocException("Unable to scan classpath.", ex);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * jodd.io.findfile.ClassFinder#onEntry(jodd.io.findfile.ClassFinder.EntryData
     * )
     */
    @Override
    protected void onEntry(EntryData entryData) throws Exception {
        
        String strEntryName = entryData.getName();
        if (StringUtil.endsWithIgnoreCase(strEntryName, CONFIG_FILE_EXT)) {
            LOGGER.info("扫描到异常配置文件：" + strEntryName);
        }
        
    }
    
}
