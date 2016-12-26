/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.req.prototype.design.model.PrototypeVO;
import com.comtop.cap.codegen.generate.IWrapper;
import com.comtop.cip.json.JSON;
import com.comtop.cip.json.JSONObject;

/**
 * 需求建模之界面原型的包装类
 *
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年10月31日 凌晨
 */
public class WrapperPrototypeHTML implements Observer, IWrapper<PrototypeVO> {
    
    /** 日志 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(WrapperPrototypeHTML.class);
    
    /** PageVO的包装类对象 */
    private final WrapperNewPage wrapperNewPage = new WrapperNewPage();
    
    /**
     * 
     * @see com.comtop.cap.codegen.generate.IWrapper#wrapper(java.lang.Object)
     */
    @Override
    public Map<String, Object> wrapper(PrototypeVO prototype) {
        JSONObject objJSONObject = (JSONObject) JSON.toJSON(prototype);
        wrapperNewPage.wrapperJavadoc(objJSONObject);
        wrapperNewPage.wrapperLayout(objJSONObject);
        wrapperNewPage.wrapperIncludeFile(objJSONObject);
        this.wrapperIncludeFileAgain(objJSONObject);
        return objJSONObject;
    }
    
    /**
     * 再次处理引入文件
     * 
     * @param objJSONObject 界面原型json数据
     */
    @SuppressWarnings("unchecked")
    public void wrapperIncludeFileAgain(JSONObject objJSONObject) {
        // 计算相对路径
        String relativePath = this.getFileRelativePath(objJSONObject);
        objJSONObject.put("webPath", relativePath);
        // 获取各类文件数组
        List<String> jsFiles = (List<String>) objJSONObject.get("JSFileList");
        // 去掉部分不可用的js、css文件，比如控件、行为模板里面依赖的/cap/rt下的一些文件。
        jsFiles = this.removeUnusableFiles(jsFiles);
        // 设置引入文件文件的相对路径
        this.setFileRelativePath(jsFiles, relativePath);
        objJSONObject.put("JSFileList", jsFiles);
        
        List<String> cssFiles = (List<String>) objJSONObject.get("CSSFileList");
        cssFiles = this.removeUnusableFiles(cssFiles);
        this.setFileRelativePath(cssFiles, relativePath);
        objJSONObject.put("CSSFileList", cssFiles);
        // 忽略引入的jsp文件
        objJSONObject.remove("JSPFileList");
    }
    
    /**
     * 去掉部分不可用的js、css文件，比如控件、行为模板里面依赖的/cap/rt下的一些文件。
     *
     * @param files 原始文件集合
     * @return 取出不可用的文件后剩余的文件集合
     */
    private List<String> removeUnusableFiles(List<String> files) {
        if (null == files) {
            return new ArrayList<String>();
        }
        Iterator<String> it = files.iterator();
        while (it.hasNext()) {
            String filePath = it.next();
            // 如果引入的文件的路径不是以common大头的，则忽略
            if (!filePath.startsWith("common")) {
                it.remove();
            }
        }
        return files;
    }
    
    /**
     * 获取界面原型HTML引入的js、css文件的相对路径
     *
     * @param objJSONObject 界面原型json数据
     * @return 相对路径标识
     */
    private String getFileRelativePath(JSONObject objJSONObject) {
        String relativePath = "";
        String modelPackage = objJSONObject.getString("modelPackage");
        Pattern p = Pattern.compile("^(com\\.comtop\\.prototype\\.)(.+)");
        Matcher m = p.matcher(modelPackage);
        if (m.matches()) {
            String[] pathArray = m.group(2).split("[.]");
            for (int i = 0, len = pathArray.length; i < len; i++) {
                relativePath += "../";
            }
        }
        return relativePath;
    }
    
    /**
     * 设置引入文件的相对路径
     *
     * @param files 引入的文件
     * @param relativePath 相对路径
     */
    private void setFileRelativePath(List<String> files, String relativePath) {
        if (null == files) {
            return;
        }
        for (int i = 0, len = files.size(); i < len; i++) {
            files.set(i, relativePath + files.get(i));
        }
    }
    
    /**
     * 
     * @see com.comtop.cap.codegen.generate.IWrapper#wrapper(java.lang.Object, java.lang.String)
     */
    @Override
    public Map<String, Object> wrapper(PrototypeVO entity, String format) {
        return null;
    }
    
    /**
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(Observable o, Object arg) {
        // 暂无需处理
    }
    
}
