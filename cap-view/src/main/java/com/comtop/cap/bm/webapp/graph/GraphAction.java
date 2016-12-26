/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.graph;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.doc.info.action.DocumentAction;
import com.comtop.cap.doc.info.action.ImportResult;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cip.graph.entity.facade.GraphFacadeImpl;
import com.comtop.cip.graph.entity.facade.GraphModuleFacadeImpl;
import com.comtop.cip.graph.entity.facade.IGraphFacade;
import com.comtop.cip.graph.entity.facade.IGraphModuleFacade;
import com.comtop.cip.graph.entity.model.GraphModuleVO;
import com.comtop.cip.graph.uml.facade.GraphUmlFacadeImpl;
import com.comtop.cip.graph.uml.facade.IGraphUmlFacade;
import com.comtop.cip.jodd.madvoc.meta.Action;
import com.comtop.cip.jodd.madvoc.meta.In;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.eic.core.asyn.control.TaskManager;
import com.comtop.eic.core.asyn.execute.TaskProgressObserver;
import com.comtop.eic.core.asyn.model.BaseTask;
import com.comtop.eic.core.asyn.model.TaskType;
import com.comtop.eic.core.exception.TaskBusinessException;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.jodd.AppContext;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 界面Action
 * 
 * 
 * @author 杜祺
 * @since 1.0
 * @version 2014-11-5 杜祺
 */

@DwrProxy
@MadvocAction("GraphAction")
public class GraphAction {
    
    /** 日志 */
    private final static Logger LOG = LoggerFactory.getLogger(GraphAction.class);
    
    /** 图片XML */
    @In
    private String imageXML;
    
    /** 图片文件名 */
    @In
    private String fileName;
    
    /** 图片格式 */
    @In
    private String format;
    
    /** 宽度 */
    @In
    private int width;
    
    /** 高度 */
    @In
    private int height;
    
    /** 操作 */
    @In
    private String actionType;
    
    /** 模块ID */
    @In
    private String eAModuleId;
    
    /** 界面调用接口 */
    // private final IModuleGraphFacade moduleGraphFacade = AppContext.getBean(ModuleGraphFacadeImpl.class);
    
    /** 界面调用接口 */
    private final IGraphFacade graphFacade = AppContext.getBean(GraphFacadeImpl.class);
    
    /** 界面调用接口 */
    private final IGraphUmlFacade graphUmlFacade = AppContext.getBean(GraphUmlFacadeImpl.class);
    
    /** 界面调用接口 */
    private final IGraphModuleFacade graphModuleFacadel = AppContext.getBean(GraphModuleFacadeImpl.class);
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAction.class);
    
    /**
     * 查询界面列表
     * 
     * @param id
     *            模块id
     * @return 实体属性集合
     */
    @RemoteMethod
    public GraphModuleVO queryModule(String id) {
        return graphFacade.queryGraphEntityByPackageId(id);
    }
    
    /**
     * 查询页面
     * 
     * @param packageId
     *            模块id
     * @return 页面VO
     */
    @RemoteMethod
    public GraphModuleVO queryGraphModuleByModuleId(final String packageId) {
        return graphModuleFacadel.queryGraphModuleByModuleId(packageId);
    }
    
    /**
     * 查询moduleName
     * 
     * @param packageId
     *            模块id
     * @return 页面VO
     */
    @RemoteMethod
    public GraphModuleVO queryGraphModuleNameByModuleId(final String packageId) {
        return graphModuleFacadel.queryGraphModuleNameByModuleId(packageId);
    }
    
    /**
     * 查询uml
     * 
     * @param packageId
     *            模块id
     * @return 页面VO
     */
    @RemoteMethod
    public GraphModuleVO queryGraphUmlByModuleId(final String packageId) {
        return graphUmlFacade.queryGraphUmlByPackageId(packageId);
    }
    
    /**
     * EA文件导出
     *
     * @param exModuleId 模块ID
     * @return 导出结果
     */
    @RemoteMethod
    public ImportResult exoprtEaFile(final String exModuleId) {
        ImportResult result = new ImportResult();
        result.setCode("Success");
        String message;
        StringBuilder builder = new StringBuilder();
        Map<String, Object> extVars = new HashMap<String, Object>();
        String exfileName = "eaexport-" + System.currentTimeMillis() + ".xml";
        String tmpFileDir = createTmpFileDir();
        String eaFilePath = tmpFileDir + File.separator + exfileName;
        extVars.put("eaFilePath", eaFilePath);
        extVars.put("zipPath", fixed(exfileName));
        extVars.put("tmpFileDir", tmpFileDir);
        extVars.put("exModuleId", exModuleId);
        GraphExportProcessor processor = new GraphExportProcessor(extVars);
        TaskProgressObserver observable = new TaskProgressObserver(processor.getHandleProgress());
        BaseTask task = createTask(fixed(exfileName), TaskType.EXPORT);
        try {
            TaskManager.getInstance().submitTask(task, processor, observable);
        } catch (TaskBusinessException e) {
            LOGGER.debug("error", e);
            builder.append(exfileName);
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
     *
     * 导出图片
     *
     *
     * @return 图片
     */
    @Action(value = "/cip/graph/exportImage.ac", result = "exportImage")
    public ExportParam exoprtImage() {
        ExportParam params = new ExportParam();
        params.setFileName(fileName);
        params.setFormat(format);
        params.setHeight(height);
        params.setWidth(width);
        String xml = null;
        try {
            xml = URLDecoder.decode(imageXML, "UTF-8");
            params.setImageXML(xml);
        } catch (UnsupportedEncodingException e) {
            LOG.error("不直接编码异常。", e);
        }
        return params;
    }
    
    /**
     * 查询应用结构图
     * 
     * @param moduleId 模块Id
     * @return 模块对象
     */
    @RemoteMethod
    public GraphModuleVO queryThreeChildrenModuleVOList(String moduleId) {
        return graphFacade.queryThreeChildrenModuleVOList(moduleId);
    }
    
    /**
     * 获取webinf路径
     *
     * @return WebinfPath
     */
    private String getWebinfPath() {
        String classesFilePath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
        File file = new File(classesFilePath);
        return file.getParent();
    }
    
    /**
     * 获取webinf路径
     *
     * @return WebinfPath
     */
    private String createTmpFileDir() {
        String webinfPath = getWebinfPath();
        String tmpFileDir = webinfPath + File.separator + "cap" + File.separator + "graph";
        File file = new File(tmpFileDir);
        mkDir(file);
        return tmpFileDir;
    }
    
    /**
     * 递归创建目录
     *
     * @param file 文件
     */
    private void mkDir(File file) {
        if (file.getParentFile().exists()) {
            file.mkdir();
        } else {
            mkDir(file.getParentFile());
            file.mkdir();
        }
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
