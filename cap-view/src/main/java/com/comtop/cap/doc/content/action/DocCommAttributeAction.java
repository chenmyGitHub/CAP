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

import com.comtop.cap.doc.content.facade.DocCommAttributeFacade;
import com.comtop.cap.doc.content.model.DocCommAttributeVO;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-11 CAP
 */
@DwrProxy
@MadvocAction
public class DocCommAttributeAction extends BaseAction {
    
    /** 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中Facade */
    protected final DocCommAttributeFacade docCommAttributeFacade = AppBeanUtil.getBean(DocCommAttributeFacade.class);
    
    /**
     * 通过模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中ID查询模型对象属性实例。如果扩展的模型对象没有独立的存储结构，
     * 则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中
     * 
     * @param docCommAttributeId 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中ID
     * @return 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中对象
     */
    @RemoteMethod
    public DocCommAttributeVO queryDocCommAttributeById(final String docCommAttributeId) {
        DocCommAttributeVO objDocCommAttribute = docCommAttributeFacade.loadDocCommAttributeById(docCommAttributeId);
        if (objDocCommAttribute == null) {
            objDocCommAttribute = new DocCommAttributeVO();
        }
        return objDocCommAttribute;
    }
    
    /**
     * 通过模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中ID查询模型对象属性实例。如果扩展的模型对象没有独立的存储结构，
     * 则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中
     * 
     * @param docCommAttribute 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中ID
     * @return 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中对象
     */
    @RemoteMethod
    public String saveDocCommAttribute(final DocCommAttributeVO docCommAttribute) {
        if (docCommAttribute.getId() == null) {
            String strId = (String) docCommAttributeFacade.insertDocCommAttribute(docCommAttribute);
            docCommAttribute.setId(strId);
        } else {
            docCommAttributeFacade.updateDocCommAttribute(docCommAttribute);
        }
        return docCommAttribute.getId();
    }
    
    /**
     * 通过模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中ID查询模型对象属性实例。如果扩展的模型对象没有独立的存储结构，
     * 则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中
     * 
     * @param docCommAttribute 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中
     * @return 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDocCommAttributeList(final DocCommAttributeVO docCommAttribute) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = docCommAttributeFacade.queryDocCommAttributeCount(docCommAttribute);
        List<DocCommAttributeVO> docCommAttributeList = null;
        if (count > 0) {
            docCommAttributeList = docCommAttributeFacade.queryDocCommAttributeList(docCommAttribute);
        }
        ret.put("list", docCommAttributeList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中
     * 
     * @param docCommAttributeList 模型对象属性实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在CAP_DOC_COMM_OBJECT表中，对象属性存储在此表中集合
     */
    @RemoteMethod
    public void deleteDocCommAttributeList(final List<DocCommAttributeVO> docCommAttributeList) {
        docCommAttributeFacade.deleteDocCommAttributeList(docCommAttributeList);
    }
    
}
