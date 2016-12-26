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

import com.comtop.cap.doc.content.facade.DocCommObjectFacade;
import com.comtop.cap.doc.content.model.DocCommObjectVO;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-11 CAP
 */
@DwrProxy
@MadvocAction
public class DocCommObjectAction extends BaseAction {
    
    /** 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中Facade */
    protected final DocCommObjectFacade docCommObjectFacade = AppBeanUtil.getBean(DocCommObjectFacade.class);
    
    /**
     * 通过模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中ID查询模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，
     * 对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中
     * 
     * @param docCommObjectId 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中ID
     * @return 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中对象
     */
    @RemoteMethod
    public DocCommObjectVO queryDocCommObjectById(final String docCommObjectId) {
        DocCommObjectVO objDocCommObject = docCommObjectFacade.loadDocCommObjectById(docCommObjectId);
        if (objDocCommObject == null) {
            objDocCommObject = new DocCommObjectVO();
        }
        return objDocCommObject;
    }
    
    /**
     * 通过模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中ID查询模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，
     * 对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中
     * 
     * @param docCommObject 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中ID
     * @return 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中对象
     */
    @RemoteMethod
    public String saveDocCommObject(final DocCommObjectVO docCommObject) {
        if (docCommObject.getId() == null) {
            String strId = (String) docCommObjectFacade.insertDocCommObject(docCommObject);
            docCommObject.setId(strId);
        } else {
            docCommObjectFacade.updateDocCommObject(docCommObject);
        }
        return docCommObject.getId();
    }
    
    /**
     * 通过模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中ID查询模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，
     * 对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中
     * 
     * @param docCommObject 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中
     * @return 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDocCommObjectList(final DocCommObjectVO docCommObject) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = docCommObjectFacade.queryDocCommObjectCount(docCommObject);
        List<DocCommObjectVO> docCommObjectList = null;
        if (count > 0) {
            docCommObjectList = docCommObjectFacade.queryDocCommObjectList(docCommObject);
        }
        ret.put("list", docCommObjectList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中
     * 
     * @param docCommObjectList 模型对象实例。如果扩展的模型对象没有独立的存储结构，则对象本身存储在此表中，对象属性存储在CAP_DOC_COMM_ATTRIBUTE表中集合
     */
    @RemoteMethod
    public void deleteDocCommObjectList(final List<DocCommObjectVO> docCommObjectList) {
        docCommObjectFacade.deleteDocCommObjectList(docCommObjectList);
    }
    
    /**
     * 根据对象定义URI和对象ID，查询对象实例VO及对象实例属性VO
     *
     * @param docCommObject 对象实例VO
     * @return 对象实例VO
     */
    @RemoteMethod
    public DocCommObjectVO readObjectInstance(final DocCommObjectVO docCommObject) {
        return docCommObjectFacade.readObjectInstance(docCommObject);
    }
    
}
