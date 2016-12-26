/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.req.subfunc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.req.subfunc.facade.ReqFunctionPageFacade;
import com.comtop.cap.bm.req.subfunc.model.ReqPageVO;
import com.comtop.cap.component.loader.LoadFile;
import com.comtop.cap.component.loader.util.LoaderUtil;
import com.comtop.cip.common.util.CAPCollectionUtils;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 界面原型Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-12-22 CAP
 */
@DwrProxy
public class ReqFunctionPageAction extends BaseAction {
    
    /** 界面原型Facade */
    protected final ReqFunctionPageFacade reqFunctionPageFacade = AppBeanUtil.getBean(ReqFunctionPageFacade.class);
    
    /**
     * 保存界面原型基本信息
     * 
     * @param reqFunctionPage 界面原型基本信息
     * @return id
     */
    @RemoteMethod
    public String saveReqFunctionPage(final ReqPageVO reqFunctionPage) {
        if (reqFunctionPage.getId() == null) {
            String strId = reqFunctionPageFacade.insertReqFunctionPage(reqFunctionPage);
            reqFunctionPage.setId(strId);
        } else {
            reqFunctionPageFacade.updateReqFunctionPage(reqFunctionPage);
        }
        return reqFunctionPage.getId();
    }
    
    /**
     * 根据界面原型的ID集合查询对应的界面原型VO集合
     * 
     * @param pageIds 界面原型的ID集合，为空是返回空集合
     * @return 符合条件的界面原型VO集合
     */
    @RemoteMethod
    public List<ReqPageVO> queryReqPageListByIds(List<String> pageIds) {
        if (CAPCollectionUtils.isNotEmpty(pageIds)) {
            return reqFunctionPageFacade.queryReqPageListByIds(pageIds);
        }
        return new ArrayList<ReqPageVO>(0);
    }
    
    /**
     * 
     * 获取界面原型列表（分页）
     *
     * @param reqFunctionPage 查询条件
     * @return map
     */
    @RemoteMethod
    public Map<String, Object> queryReqPageListByPage(final ReqPageVO reqFunctionPage) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = reqFunctionPageFacade.queryReqPageCount(reqFunctionPage);
        List<ReqPageVO> bizItemsList = null;
        if (count > 0) {
            bizItemsList = reqFunctionPageFacade.queryReqPageList(reqFunctionPage);
        }
        ret.put("list", bizItemsList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 
     * 删除界面原型
     * 
     * @param reqFunctionPageList 界面原型
     */
    @RemoteMethod
    public void deleteReqPageList(final List<ReqPageVO> reqFunctionPageList) {
        reqFunctionPageFacade.deleteReqPageList(reqFunctionPageList);
    }
    
    /**
     * 
     * 获取服务器上图片存储路径
     * 
     * @param uploadKey 文件上传uploadkey
     * @param uploadId 文件上传uploadId
     * @return 存储路径
     */
    @RemoteMethod
    public String getImgFolderPath(final String uploadKey, final String uploadId) {
        String folderpath = LoaderUtil.getFolderPath(uploadKey, uploadId);
        String[] name = LoaderUtil.getFileNames(uploadKey, uploadId);
        LoadFile uploadFile = new LoadFile();
        uploadFile.setUploadId(uploadId);
        uploadFile.setFileName(name[0]);
        uploadFile.setFolderPath(folderpath);
        String path = uploadFile.toFileLocation().toHttpUrlString();
        return path;
    }
    
    /**
     * 
     * 批量保存界面原型
     * 
     * @param pageList 界面原型list
     */
    @RemoteMethod
    public void updatePageList(final List<ReqPageVO> pageList) {
        reqFunctionPageFacade.updatePageList(pageList);
    }
}
