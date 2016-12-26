/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.core;

import java.io.File;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cip.jodd.io.StreamUtil;
import com.comtop.cip.jodd.io.findfile.ClassFinder;
import com.comtop.cip.jodd.util.ArraysUtil;
import com.comtop.cip.jodd.util.ClassLoaderUtil;
import com.comtop.top.core.base.exception.TopBaseException;
import com.comtop.top.core.util.constant.NumberConstant;

/**
 * 扫描使用了指定注解的类
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2014-06-05 谢阳
 * @version 2015-11-27 李志勇 修改 isTypeSignatureInUse方法，判断逻辑修改为只要存在即返回true。
 */
public class CAPAnnotatoinScanner extends ClassFinder {
    
    /** * 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(CAPAnnotatoinScanner.class);
    
    /** 注解 */
    @SuppressWarnings("rawtypes")
    protected List<Class> annotations;
    
    /** 注解的byte数据 */
    protected List<byte[]> annotationBytes = new ArrayList<byte[]>(NumberConstant.FOUR);
    
    /** 类名 */
    protected List<String> classNames = new ArrayList<String>(NumberConstant.FOUR);
    
    /**
     * 构造函数
     * 
     * @param annotations 注解
     */
    @SuppressWarnings("rawtypes")
    public CAPAnnotatoinScanner(List<Class> annotations) {
        this.annotations = annotations;
        if (annotations != null && !annotations.isEmpty()) {
            StringBuffer objStringBuffer = new StringBuffer("");
            byte[] objAnnotationByte;
            for (Class objAnnotation : annotations) {
                objAnnotationByte = getTypeSignatureBytes(objAnnotation);
                annotationBytes.add(objAnnotationByte);
                objStringBuffer.append(objAnnotation.getSimpleName()).append(",");
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("[信息]扫描的注解是："
                    + objStringBuffer.substring(NumberConstant.ZERO, objStringBuffer.length() - NumberConstant.ONE));
            }
        }
    }
    
    /**
     * 获取含有指定注解的类
     * 
     * @return 类的全名
     */
    public String getClassNames() {
        if (annotations == null || annotations.isEmpty()) {
            return null;
        }
        return getClassNames(ClassLoaderUtil.getDefaultClasspath());
    }
    
    /**
     * 获取含有指定注解的类
     * 
     * @param classpath 路径
     * @return 类的全名
     */
    public String getClassNames(File[] classpath) {
        if (annotations == null || annotations.isEmpty()) {
            return null;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("[信息]扫描的路径如下：");
            for (File objFile : classpath) {
                LOGGER.debug(objFile.getAbsolutePath());
            }
        }
        
        try {
            scanPaths(classpath);
        } catch (Exception ex) {
            throw new TopBaseException("扫描" + classpath + "出错.", ex);
        }
        return this.listToString(classNames);
    }
    
    /**
     * 
     * @param entryData entryData
     * @throws Exception Exception
     * @see com.comtop.cip.jodd.io.findfile.ClassFinder#onEntry(com.comtop.cip.jodd.io.findfile.ClassFinder.EntryData)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onEntry(EntryData entryData) throws Exception {
        String strEntryName = entryData.getName();
        InputStream objInputStream = entryData.openInputStream();
        boolean bFlag = false;
        byte[] objData = StreamUtil.readBytes(objInputStream);
        for (byte[] objAnnotationByte : annotationBytes) {
            if (isTypeSignatureInUse(objData, objAnnotationByte)) {
                bFlag = true;
                break;
            }
        }
        objData = null;
        if (!bFlag) {
            return;
        }
        
        Class<?> objBeanClass;
        try {
            objBeanClass = ClassLoaderUtil.loadClass(strEntryName);
        } catch (ClassNotFoundException cnfex) {
            throw new TopBaseException("加载" + strEntryName + "出错.", cnfex);
        }
        bFlag = false;
        Annotation objTmpAnnotation;
        for (Class objAnnotation : annotations) {
            objTmpAnnotation = objBeanClass.getAnnotation(objAnnotation);
            if (objTmpAnnotation != null) {
                bFlag = true;
                break;
            }
        }
        if (bFlag) {
            classNames.add(strEntryName);
        }
    }
    
    /**
     * 判断bytes是否存在data中
     * 
     * @param data data
     * @param bytes bytes
     * @return true/false
     */
    private boolean isTypeSignatureInUse(byte[] data, byte[] bytes) {
        int iIndex = ArraysUtil.indexOf(data, bytes);
        return iIndex >= 0;
    }
    
    /**
     * List转换成String
     * 
     * @param list List
     * @return string
     */
    private String listToString(List<String> list) {
        StringBuilder sbBuffer = new StringBuilder("");
        if (list != null && !list.isEmpty()) {
            for (int i = NumberConstant.ZERO; i < list.size(); i++) {
                if (i < list.size() - NumberConstant.ONE) {
                    sbBuffer.append(list.get(i) + ",");
                } else {
                    sbBuffer.append(list.get(i));
                }
            }
        }
        return sbBuffer.toString();
    }
}
