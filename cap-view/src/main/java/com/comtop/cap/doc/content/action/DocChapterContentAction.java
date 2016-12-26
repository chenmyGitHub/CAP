/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.content.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.doc.content.facade.DocChapterContentFacade;
import com.comtop.cap.doc.content.model.DocChapterContentVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;
/**
 *  指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-24 CAP
 */
@DwrProxy
public class DocChapterContentAction extends BaseAction{
	 
    /** 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储Facade */
    protected final DocChapterContentFacade docChapterContentFacade = AppBeanUtil.getBean(DocChapterContentFacade.class);
    
    /**
     * 通过指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储ID查询指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储
     * 
     * @param docChapterContentId 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储ID
     * @return 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储对象
     */
    @RemoteMethod
    public DocChapterContentVO queryDocChapterContentById(final String docChapterContentId) {
        DocChapterContentVO objDocChapterContent = docChapterContentFacade.loadDocChapterContentById(docChapterContentId);
        if (objDocChapterContent == null) {
            objDocChapterContent = new DocChapterContentVO();
        }
        return objDocChapterContent;
    }
    
    /**
     * 通过指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储ID查询指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储
     * 
     * @param docChapterContent 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储ID
     * @return 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储对象
     */
    @RemoteMethod
    public String saveDocChapterContent(final DocChapterContentVO docChapterContent) {
        if (docChapterContent.getId() == null) {
            String strId = (String) docChapterContentFacade.insertDocChapterContent(docChapterContent);
            docChapterContent.setId(strId);
        } else {
        	docChapterContentFacade.updateDocChapterContent(docChapterContent);
        }
        return docChapterContent.getId();
    }
    
    /**
     * 通过指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储ID查询指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储
     * 
     * @param docChapterContent 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储
     * @return 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDocChapterContentList(final DocChapterContentVO docChapterContent) {
    	final Map<String, Object> ret = new HashMap<String, Object>(2);
    	int count = docChapterContentFacade.queryDocChapterContentCount(docChapterContent);
    	List<DocChapterContentVO> docChapterContentList = null;
    	if(count > 0){
            docChapterContentList = docChapterContentFacade.queryDocChapterContentList(docChapterContent);
        }
        ret.put("list", docChapterContentList);
        ret.put("count", count);
        return ret;
    }
    
     /**
     * 删除指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储
     * 
     * @param docChapterContentList 指word中的纯文本内容、非结构化的表格内容。非结构化的表格内容以原始的表格结构字符串存储集合
     */
    @RemoteMethod
    public void deleteDocChapterContentList(final List<DocChapterContentVO> docChapterContentList) {
    	docChapterContentFacade.deleteDocChapterContentList(docChapterContentList);
    }
}
