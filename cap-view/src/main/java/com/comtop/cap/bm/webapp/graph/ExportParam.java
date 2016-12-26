/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.graph;

/**
 * 
 * 导出参数
 *
 * @author duqi
 * @since jdk1.6
 * @version 2015年9月11日 duqi
 */
public class ExportParam {
    
    /** 图片XML */
    private String imageXML;
    
    /** 图片文件名 */
    private String fileName;
    
    /** 图片格式 */
    private String format;
    
    /** 宽度 */
    private int width;
    
    /** 高度 */
    private int height;
    
    /** 文件路径 */
    private String filePath;
    
    /** 操作类型 */
    private String actionType;
    
    /**
     * @return the imageXML
     */
    public String getImageXML() {
        return imageXML;
    }
    
    /**
     * @param imageXML the imageXML to set
     */
    public void setImageXML(String imageXML) {
        this.imageXML = imageXML;
    }
    
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }
    
    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * @return 获取 filePath属性值
     */
    public String getFilePath() {
        return filePath;
    }
    
    /**
     * @param filePath 设置 filePath 属性值为参数值 filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    /**
     * @return 获取 actionType属性值
     */
    public String getActionType() {
        return actionType;
    }
    
    /**
     * @param actionType 设置 actionType 属性值为参数值 actionType
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
}
