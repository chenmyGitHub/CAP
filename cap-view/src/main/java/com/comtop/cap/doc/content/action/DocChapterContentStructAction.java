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

import com.comtop.cap.doc.content.facade.DocChapterContentStructFacade;
import com.comtop.cap.doc.content.model.DocChapterContentStructVO;
import com.comtop.cap.doc.content.model.DocChapterContentTree;
import com.comtop.cap.doc.info.model.DocumentVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import com.comtop.top.core.util.tree.TreeTransformUtils;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
 * 则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-24 CAP
 */
@DwrProxy
public class DocChapterContentStructAction extends BaseAction {
    
    /**
     * 章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     * 则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。Facade
     */
    protected final DocChapterContentStructFacade docChapterContentStructFacade = AppBeanUtil
        .getBean(DocChapterContentStructFacade.class);
    
    /**
     * 通过章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     * 则将表格内容对应的模型定义将其存储在已定义的模型存储结构中
     * ，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。ID查询章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方
     * 。如果是文本、图片、嵌入式对象内容
     * ，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象
     * ，则会将该文本存储在该对象自己的结构中。
     * 
     * @param docChapterContentStructId
     *            章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *            则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。ID
     * @return 章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *         则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。对象
     */
    @RemoteMethod
    public DocChapterContentStructVO queryDocChapterContentStructById(final String docChapterContentStructId) {
        DocChapterContentStructVO objDocChapterContentStruct = docChapterContentStructFacade
            .loadDocChapterContentStructById(docChapterContentStructId);
        if (objDocChapterContentStruct == null) {
            objDocChapterContentStruct = new DocChapterContentStructVO();
        }
        return objDocChapterContentStruct;
    }
    
    /**
     * 通过章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     * 则将表格内容对应的模型定义将其存储在已定义的模型存储结构中
     * ，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。ID查询章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方
     * 。如果是文本、图片、嵌入式对象内容
     * ，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象
     * ，则会将该文本存储在该对象自己的结构中。
     * 
     * @param docChapterContentStruct
     *            章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *            则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。ID
     * @return 章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *         则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。对象
     */
    @RemoteMethod
    public String saveDocChapterContentStruct(final DocChapterContentStructVO docChapterContentStruct) {
        
        // if (docChapterContentStruct.getId() == null) {
        // String strId = (String) docChapterContentStructFacade
        // .insertDocChapterContentStruct(docChapterContentStruct);
        // docChapterContentStruct.setId(strId);
        // } else {
        // docChapterContentStructFacade.updateDocChapterContentStruct(docChapterContentStruct);
        // }
        // return docChapterContentStruct.getId();
        return docChapterContentStructFacade.saveDocChapterContentStruct(docChapterContentStruct);
    }
    
    /**
     * 通过章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     * 则将表格内容对应的模型定义将其存储在已定义的模型存储结构中
     * ，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。ID查询章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方
     * 。如果是文本、图片、嵌入式对象内容
     * ，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象
     * ，则会将该文本存储在该对象自己的结构中。
     * 
     * @param docChapterContentStruct
     *            章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *            则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。
     * @return 章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *         则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDocChapterContentStructList(final DocChapterContentStructVO docChapterContentStruct) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = docChapterContentStructFacade.queryDocChapterContentStructCount(docChapterContentStruct);
        List<DocChapterContentStructVO> docChapterContentStructList = null;
        if (count > 0) {
            docChapterContentStructList = docChapterContentStructFacade
                .queryDocChapterContentStructList(docChapterContentStruct);
        }
        ret.put("list", docChapterContentStructList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     * 则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。
     * 
     * @param docChapterContentStructList
     *            章节内容结构。章节的所有内容均会在此表中存在。以类型字段区分具体的内容存储在什么地方。如果是文本、图片、嵌入式对象内容，则对应存储在文本内容表、图片内容表、嵌入式对象内容表。如果已定义的表格内容，
     *            则将表格内容对应的模型定义将其存储在已定义的模型存储结构中，比如业务事项表、业务对象表等。如果某段纯文本归属于一个已经定义的对象，则会将该文本存储在该对象自己的结构中。集合
     */
    @RemoteMethod
    public void deleteDocChapterContentStructList(final List<DocChapterContentStructVO> docChapterContentStructList) {
        docChapterContentStructFacade.deleteDocChapterContentStructList(docChapterContentStructList);
    }
    
    /**
     * 查询章节结构
     *
     * @param objDocChapterContentStructVO 章节结构
     * @return 章节内容
     */
    @RemoteMethod
    public DocChapterContentStructVO queryDocChapterContentStruct(DocChapterContentStructVO objDocChapterContentStructVO) {
        return docChapterContentStructFacade.queryDocChapterContentStruct(objDocChapterContentStructVO);
    }
    
    /**
     * 取章节树信息
     * 
     * @param documentVO 文档基本信息
     * 
     * @return 返回章节树信息
     */
    @RemoteMethod
    public String getAllChapterTree(DocumentVO documentVO) {
        List<DocChapterContentStructVO> lstDocChapterContentStruct = docChapterContentStructFacade
            .getAllChapterTree(documentVO);
        return TreeTransformUtils.listToTree(lstDocChapterContentStruct, new DocChapterContentTree());
    }
    
    /**
     * 懒加载树
     *
     * @param docChapterContentStructVO 树查询条件
     * @return 懒加载树
     */
    @RemoteMethod
    public String getChapterTree(DocChapterContentStructVO docChapterContentStructVO) {
        List<DocChapterContentStructVO> lstDocChapterContentStruct = docChapterContentStructFacade
            .getChapterTree(docChapterContentStructVO);
        return TreeTransformUtils.listToTree(lstDocChapterContentStruct, new DocChapterContentTree());
    }
    
    /**
     * 查询xml配置
     *
     * @param docChapterContentStructVO id信息
     * @return 内容信息
     */
    @RemoteMethod
    public List<DocChapterContentStructVO> getChapterXmlContentById(DocChapterContentStructVO docChapterContentStructVO) {
        return docChapterContentStructFacade.getChapterXmlContentById(docChapterContentStructVO);
    }
    
}
