/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.operatelog.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.doc.operatelog.facade.DocOperLogFacade;
import com.comtop.cap.doc.operatelog.model.DocOperLogVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.component.common.systeminit.WebGlobalInfo;
import com.comtop.top.core.base.action.BaseAction;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 文档抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-9 CAP
 */
@DwrProxy
public class DocOperLogAction extends BaseAction {
    
    /** Facade */
    protected final DocOperLogFacade docOperLogFacade = AppBeanUtil.getBean(DocOperLogFacade.class);
    
    /**
     * 分页查询操作记录
     * 
     * @param docOperLog 查询条件
     * @return map对象
     */
    @RemoteMethod
    public Map<String, Object> queryOperLogByPage(final DocOperLogVO docOperLog) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = docOperLogFacade.queryOperLogCountByPage(docOperLog);
        List<DocOperLogVO> docOperLogList = null;
        if (count > 0) {
            docOperLogList = docOperLogFacade.queryOperLogListByPage(docOperLog);
        }
        ret.put("list", docOperLogList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除操作记录
     * 
     * @param docOperLogList 操作记录
     */
    @RemoteMethod
    public void deleteOperLog(final List<DocOperLogVO> docOperLogList) {
        docOperLogFacade.deleteOperLog(docOperLogList);
    }
    
    /**
     * 查询文件是否存在
     * 
     * @param url 操作记录
     * @return 结果
     */
    @RemoteMethod
    public boolean isExitFile(final String url) {
        String webPath = WebGlobalInfo.getWebPath();
        String filePath = webPath.replace("\\", "/") + url;
        File f = new File(filePath);
        return f.exists();
    }
}
