/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.info.action;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.doc.DocProcessParams;
import com.comtop.cap.doc.info.facade.DocumentFacade;
import com.comtop.cap.doc.info.model.DocumentVO;
import com.comtop.cap.doc.operatelog.facade.DocOperLogFacade;
import com.comtop.cap.doc.operatelog.model.DocOperLogVO;
import com.comtop.cap.doc.service.DocType;
import com.comtop.cap.doc.tmpl.facade.CapDocTemplateFacade;
import com.comtop.cap.doc.tmpl.model.CapDocTemplateVO;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cip.common.util.CAPStringUtils;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.corm.resource.util.CollectionUtils;
import com.comtop.eic.core.asyn.control.TaskManager;
import com.comtop.eic.core.asyn.execute.TaskProgressObserver;
import com.comtop.eic.core.asyn.model.BaseTask;
import com.comtop.eic.core.asyn.model.TaskType;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.base.action.BaseAction;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.JsonUtil;
import com.comtop.top.sys.module.facade.IModuleFacade;
import com.comtop.top.sys.module.facade.ModuleFacade;
import com.comtop.top.sys.module.model.ModuleDTO;
import comtop.org.directwebremoting.WebContextFactory;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;
import comtop.org.directwebremoting.io.FileTransfer;

/**
 * 文档抽象Action
 * 
 * @author CAP
 * @since 1.0
 * @version 2015-11-9 CAP
 */
@DwrProxy
@MadvocAction
public class DocumentAction extends BaseAction {
    
    /** 文档Facade */
    protected final DocumentFacade documentFacade = AppBeanUtil.getBean(DocumentFacade.class);
    
    /** 文档模板Facade */
    protected final CapDocTemplateFacade capDocTemplateFacade = AppBeanUtil.getBean(CapDocTemplateFacade.class);
    
    /** 文档操作记录Facade */
    protected final DocOperLogFacade docOperLogFacade = AppBeanUtil.getBean(DocOperLogFacade.class);
    
    /** TOP系统模块 Facade */
    private final IModuleFacade moduleFacade = AppContext.getBean(ModuleFacade.class);
    
    /** xml工厂key */
    private static final String XML_FACTORY_KEY = "javax.xml.stream.XMLEventFactory";
    
    /** 工厂实现 */
    private static final String XML_FACTORY_IMPL = "com.sun.xml.internal.stream.events.XMLEventFactoryImpl";
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAction.class);
    
    /**
     * 构造函数
     */
    public DocumentAction() {
        // 此句话的目标是避免jar包冲突
        System.setProperty(XML_FACTORY_KEY, XML_FACTORY_IMPL);
    }
    
    /**
     * 通过文档ID查询文档
     * 
     * @param documentId 文档ID
     * @return 文档对象
     */
    @RemoteMethod
    public DocumentVO queryDocumentById(final String documentId) {
        DocumentVO condition = new DocumentVO();
        condition.setId(documentId);
        List<DocumentVO> list = documentFacade.queryDocumentList(condition);
        if (list == null || list.size() == 0) {
            return new DocumentVO();
        }
        return list.get(0);
    }
    
    /**
     * 通过文档ID查询文档
     * 
     * @param document 文档ID
     * @return 文档对象
     */
    @RemoteMethod
    public String saveDocument(final DocumentVO document) {
        if (document.getId() == null) {
            String strId = (String) documentFacade.insertDocument(document);
            document.setId(strId);
        } else {
            documentFacade.updateDocument(document);
        }
        return document.getId();
    }
    
    /**
     * 判断重名
     * 
     * @param document 文档对象
     * @return 存在同名时返回true，否则返回false
     */
    @RemoteMethod
    public boolean isExistSameName(DocumentVO document) {
        // 查询当前业务域下同名文档
        List<DocumentVO> lstDocument = documentFacade.queryDocumentByName(document);
        if (CollectionUtils.isEmpty(lstDocument)) {
            return true;
        }
        return false;
    }
    
    /**
     * 通过文档ID查询文档
     * 
     * @param document 文档
     * @return 文档map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDocumentByName(final DocumentVO document) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        List<DocumentVO> documentList = documentFacade.queryDocumentByName(document);
        ret.put("list", documentList);
        ret.put("count", documentList.size());
        return ret;
    }
    
    /**
     * 通过文档ID查询文档
     * 
     * @param document 文档
     * @return 文档map对象
     */
    @RemoteMethod
    public Map<String, Object> queryDocumentList(final DocumentVO document) {
        final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = documentFacade.queryDocumentCount(document);
        List<DocumentVO> documentList = null;
        if (count > 0) {
            documentList = documentFacade.queryDocumentList(document);
        }
        ret.put("list", documentList);
        ret.put("count", count);
        return ret;
    }
    
    /**
     * 删除文档
     * 
     * @param documentList 文档集合
     */
    @RemoteMethod
    public void deleteDocumentList(final List<DocumentVO> documentList) {
        documentFacade.deleteDocumentList(documentList);
    }
    
    /**
     * 导入文档
     * 
     * @param document 文档集合
     * @param file 上传后的文件对象
     * @return 导入结果
     */
    @RemoteMethod
    public ImportResult importDocument(final DocumentVO document, FileTransfer file) {
        StringBuilder builder = new StringBuilder();
        try {
            DocProcessParams docProcessParams = createImportParams(document, file);
            DocumentImportProcessor processor = new DocumentImportProcessor(docProcessParams, file);
            TaskProgressObserver observable = new TaskProgressObserver(processor.getHandleProgress());
            BaseTask task = createTask(file.getFilename(), TaskType.IMPORT);
            // 提交异步任务
            TaskManager.getInstance().submitTask(task, processor, observable);
        } catch (Exception e) {
            LOGGER.error("导入文档时发生异常", e);
            builder.append(file.getFilename());
            builder.append(";");
        }
        ImportResult importResult = new ImportResult();
        if (builder.length() > 0) {
            importResult.setCode("Error");
            importResult.setMessage("提交以下文档导出任务错误：" + builder.toString());
        } else {
            importResult.setCode("Success");
            importResult.setMessage("正在进行异步导入，请在异步导入任务窗口查看导出详情！");
        }
        return importResult;
        
    }
    
    /**
     * 创建DocProcessParams参数
     *
     * @param document 文档对象
     * @param file 当前上传的文件
     * @return DocProcessParams 参数
     */
    private DocProcessParams createImportParams(final DocumentVO document, FileTransfer file) {
        // 初始化文档对象
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        String uploadKey = (String) request.getAttribute("uploadKey");
        String uploadId = (String) request.getAttribute("uploadId");
        document.setUploadKey(uploadKey);
        document.setUploadId(uploadId);
        document.setName(file.getFilename());
        String httpUrl = "http://" + getLocalHostIp(request) + ":" + request.getLocalPort() + request.getContextPath();
        Cookie[] cookies = request.getCookies();
        CapLoginVO objCapUserVO = (CapLoginVO) request.getSession().getAttribute(CapLoginUtil.CAP_CURRENT_USER);
        List<Cookie> lstCookies = Arrays.asList(cookies);
        String cookiesJsonStr = JsonUtil.listToJson(lstCookies);
        DocProcessParams docProcessParams = new DocProcessParams();
        // docProcessParams.setSrcInput(file.getInputStream());
        docProcessParams.setCookiesJsonStr(cookiesJsonStr);
        docProcessParams.setCurUserId(objCapUserVO.getBmEmployeeId());
        docProcessParams.setCurUserName(objCapUserVO.getBmEmployeeName());
        docProcessParams.setCurUserAccount(objCapUserVO.getAccount());
        docProcessParams.setHttpRootUrl(httpUrl);
        docProcessParams.setMimeType(file.getMimeType());
        docProcessParams.setSize(file.getSize());
        docProcessParams.setDocument(document);
        @SuppressWarnings("rawtypes")
        Enumeration attrNames = request.getAttributeNames();
        String attrName = null;
        while (attrNames.hasMoreElements()) {
            attrName = (String) attrNames.nextElement();
            docProcessParams.addParam(attrName, request.getAttribute(attrName));
        }
        docProcessParams.addParams(request.getParameterMap());
        return docProcessParams;
    }
    
    /**
     * 导出指定开发树节点ID下的设计文档
     *
     * @param nodeId 开发树节点ID,
     * @param docType 文档类型，DBD:数据库设计文档、HLD：概要设计文档；LLD详细设计文档,需与模板文件规则保持一致
     * @return 导出结果
     */
    @RemoteMethod
    public ImportResult exportDocumentOutDoc(final String nodeId, String docType) {
        
        ImportResult result = new ImportResult();
        result.setCode("Success");
        if (CAPStringUtils.isBlank(docType) || CAPStringUtils.isBlank(nodeId)) {
            result.setCode("Error");
            result.setMessage("导出失败：请选择要导出的目录并指定导出的文档类型");
            return result;
        }
        String tempPath = "/docconfig/docconfig-" + docType.toLowerCase() + ".xml";
        CapDocTemplateVO tempVO = new CapDocTemplateVO();
        tempVO.setPath(tempPath);
        DocumentVO document = createDocument(nodeId, docType);
        insertExpLog(document);
        StringBuilder builder = new StringBuilder();
        try {
            Map<String, Object> varMap = initExtVars();
            DocumentExportProcessor processor = new DocumentExportProcessor(document, tempVO, varMap);
            TaskProgressObserver observable = new TaskProgressObserver(processor.getHandleProgress());
            BaseTask task = createTask(fixed(document.getName()), TaskType.EXPORT);
            TaskManager.getInstance().submitTask(task, processor, observable);
        } catch (Throwable e) {
        	LOGGER.debug("error", e);
            builder.append(document.getName());
            builder.append(";");
            docOperLogFacade.updateOperResult(document.getOperateLogId(), "FAIL");
        }
        if (builder.length() > 0) {
            result.setCode("Error");
            result.setMessage("提交以下文档导出任务错误：" + builder.toString());
        } else {
            result.setMessage("正在进行异步导出，请在异步导出任务窗口查看导出详情！");
        }
        return result;
    }
    
    /**
     * 创建一个虚拟的文档对象
     * 
     * @param nodeId 开发树节点ID,
     * @param docType 文档类型，dld:数据库设计文档、hld：概要设计文档；lld详细设计文档,需与模板文件规则保持一致
     * @return 虚拟的文档对象
     */
    private DocumentVO createDocument(final String nodeId, String docType) {
        DocumentVO document = new DocumentVO();
        document.setBizDomain(nodeId);
        ModuleDTO dto = moduleFacade.readModuleVO(nodeId);
        document.setName(dto.getModuleName() + "_" + DocType.getDocType(docType).getCnName() + ".docx");
        // document.setSortFieldName(dto.getModuleName());
        document.setId(document.getName());// TODO:无document情况下，直接使用文档名称
        return document;
    }
    
    /**
     * 初始化自定义全局扩展变量
     * 
     * @return 自定义全局扩展变量
     */
    private Map<String, Object> initExtVars() {
        Map<String, Object> varMap = new HashMap<String, Object>();
        HttpServletRequest request = TopServletListener.getRequest();
        String httpUrl = "http://" + getLocalHostIp(request) + ":" + request.getLocalPort() + request.getContextPath();
        Cookie[] cookies = request.getCookies();
        CapLoginVO objCapUserVO = (CapLoginVO) request.getSession().getAttribute(CapLoginUtil.CAP_CURRENT_USER);
        varMap.put(CapLoginUtil.CAP_CURRENT_USER, objCapUserVO);
        List<Cookie> lstCookies = Arrays.asList(cookies);
        String cookiesJsonStr = JsonUtil.listToJson(lstCookies);
        varMap.put("cookiesJsonStr", cookiesJsonStr);
        varMap.put("httpUrl", httpUrl);
        return varMap;
    }
    
    /** 本机默认主机名 */
    public static final String LOCAL_HOST = "localhost";
    
    /** 本机默认主机IP */
    public static final String LOCAL_HOST_IP = "127.0.0.1";
    
    /** 本机默认主机IPV6 */
    public static final String LOCAL_HOST_IPV6 = "0.0.0.0";
    
    /**
     * 获取本机IP列表
     * 
     * @return 本机IP列表
     */
    public static List<String> getLocalHostList() {
        List<String> lstAddresses = new ArrayList<String>();
        try {
            String strIp = "";
            for (Enumeration<NetworkInterface> objEnum = NetworkInterface.getNetworkInterfaces(); objEnum
                .hasMoreElements();) {
                NetworkInterface objIntf = objEnum.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = objIntf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress objInetAddress = enumIpAddr.nextElement();
                    if (!objInetAddress.isLoopbackAddress() && !objInetAddress.isLinkLocalAddress()
                        && objInetAddress.isSiteLocalAddress()) {
                        strIp = objInetAddress.getHostAddress().toString();
                        if (!LOCAL_HOST.equals(strIp) && !LOCAL_HOST_IP.equals(strIp)) {
                            lstAddresses.add(strIp);
                        }
                    }
                    
                }
            }
        } catch (SocketException ex) {
        	LOGGER.debug("Socket error", ex);
            return null;
        }
        return lstAddresses;
    }
    
    /**
     * 获取本机IP
     * 
     * @param request HttpServletRequest
     * 
     * @return 本机IP
     */
    public static String getLocalHostIp(HttpServletRequest request) {
        String localAddr = request.getLocalAddr();
        if (LOCAL_HOST_IPV6.equals(localAddr) || "0:0:0:0:0:0:0:1".equals(localAddr)) {
            return getLocalHostList().get(0);
        }
        return localAddr;
    }
    
    /**
     * 文档导出
     *
     * @param documents 文档集合
     * @return 导出结果
     */
    @RemoteMethod
    public ImportResult exportDocument(final List<DocumentVO> documents) {
        // 初始化word选项
        DocumentVO doc;
        CapDocTemplateVO template;
        TaskProgressObserver observable;
        BaseTask task;
        DocumentExportProcessor processor;
        ImportResult result = new ImportResult();
        result.setCode("Success");
        String message;
        StringBuilder builder = new StringBuilder();
        for (DocumentVO document : documents) {
            // 通过文档id查询文档对象
            doc = documentFacade.loadDocumentById(document.getId());
            insertExpLog(doc);
            // 根据文档模板ID获取文档模板
            template = capDocTemplateFacade.loadCapDocTemplateById(doc.getDocTmplId());
            // 执行文档导出
            processor = new DocumentExportProcessor(doc, template);
            observable = new TaskProgressObserver(processor.getHandleProgress());
            // 创建一个导出任务
            task = createTask(fixed(doc.getName()), TaskType.EXPORT);
            try {
                TaskManager.getInstance().submitTask(task, processor, observable);
            } catch (Exception e) {
            	LOGGER.debug("error", e);
                builder.append(doc.getName());
                builder.append(";");
                docOperLogFacade.updateOperResult(document.getOperateLogId(), "FAIL");
            }
        }
        if (builder.length() > 0) {
            result.setCode("Error");
            message = "提交以下文档导出任务错误：" + builder.toString();
        } else {
            message = "正在进行异步导出，请在异步导出任务窗口查看导出详情！";
        }
        result.setMessage(message);
        return result;
    }
    
    /**
     * 构建操作任务数据
     * 
     * @param docName 文档名称
     * @param taskType 任务类型：TaskType.IMPORT｜TaskType.EXPORT
     * @return 任务对象
     */
    private BaseTask createTask(String docName, TaskType taskType) {
        HttpServletRequest request = TopServletListener.getRequest();
        CapLoginVO objCapUserVO = (CapLoginVO) request.getSession().getAttribute(CapLoginUtil.CAP_CURRENT_USER);
        String ip = request.getLocalAddr();
        int port = request.getServerPort();
        // String userId = HttpSessionUtil.getCurUserId();
        BaseTask task = new BaseTask();
        task.setServerIp(ip);
        task.setServerPort(port);
        task.setUserId(objCapUserVO.getBmEmployeeId());
        task.setFileName(docName);// loadFile.getFileName()
        task.setSubSystem(null);
        task.setTaskType(taskType);// TaskType.IMPORT
        return task;
    }
    
    /**
     * 插入展出执行日志记录数据
     * 
     * @param doc 文档对象
     */
    private void insertExpLog(DocumentVO doc) {
        String operLogId = saveOperLogVO(doc.getBizDomain(), doc.getId(), "EXP", "", doc.getName());
        doc.setOperateLogId(operLogId);
    }
    
    /**
     * 保存导入、导出日志
     * 
     * @param bizDomainId 业务域.关联的业务域id
     * @param documentId 文档ID
     * @param operType 操作类型：IMP：导入、EXP:导出
     * @param fileAddr 文件地址，导入时直接填写，导出为空
     * @param documentName 文档名称
     * @return 插入后的日志数据ID
     */
    private String saveOperLogVO(String bizDomainId, String documentId, String operType, String fileAddr,
        String documentName) {
        CapLoginVO objCAPLoginVO = (CapLoginVO) TopServletListener.getRequest().getSession()
            .getAttribute(CapLoginUtil.CAP_CURRENT_USER);
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        DocOperLogVO operLogVO = new DocOperLogVO();
        operLogVO.setUserId(objCAPLoginVO.getBmEmployeeId());
        operLogVO.setUserName(objCAPLoginVO.getBmEmployeeName());
        operLogVO.setBizDomainId(bizDomainId);
        operLogVO.setOperType(operType);
        operLogVO.setOperTime(curTime);
        operLogVO.setFileAddr(fileAddr);// loadFile.toFileLocation().toHttpUrlString()
        operLogVO.setDocumentId(documentId);
        operLogVO.setRemark(documentName);// 暂使用备注字段保存文档名称
        String operLogId = docOperLogFacade.inserOperLog(operLogVO);
        return operLogId;
    }
    
    /**
     * 修复文件名
     *
     * @param name 原文件名称
     * @return 修复后的文件名称
     */
    private String fixed(String name) {
        int dotIndex = name.lastIndexOf('.');
        String fixed;
        if (dotIndex > 0) {
            String subfix = name.substring(dotIndex + 1);
            if (!"zip".equalsIgnoreCase(subfix)) {
                fixed = name.substring(0, dotIndex) + ".zip";
            } else {
                fixed = name;
            }
        } else {
            fixed = name + ".zip";
        }
        return fixed;
    }
}
