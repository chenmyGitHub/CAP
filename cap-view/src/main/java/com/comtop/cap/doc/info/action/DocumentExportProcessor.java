/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.info.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.comtop.cap.doc.DocServiceException;
import com.comtop.cap.doc.info.model.DocumentVO;
import com.comtop.cap.doc.operatelog.facade.DocOperLogFacade;
import com.comtop.cap.doc.tmpl.model.CapDocTemplateVO;
import com.comtop.cap.document.word.docmodel.DocxProperties;
import com.comtop.cap.document.word.expression.ContainerInitializer;
import com.comtop.cap.document.word.expression.ExpressionExecuteHelper;
import com.comtop.cap.document.word.write.DocxExportConfiguration;
import com.comtop.cap.document.word.write.DocxWriter;
import com.comtop.cip.common.util.CAPStringUtils;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.eic.core.asyn.model.TaskProgressVo;
import com.comtop.eic.core.asyn.model.TaskStatus;
import com.comtop.eic.core.asyn.service.ITaskProcessService;
import com.comtop.eic.core.exception.TaskBusinessException;
import com.comtop.top.component.common.systeminit.WebGlobalInfo;
import com.comtop.top.core.util.DateTimeUtil;

/**
 * 文档导出任务处理器
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2015年12月10日 lizhongwen
 */
public class DocumentExportProcessor implements ITaskProcessService {
    
    /** 导出临时文件目录 */
    public final static String TEMP_FILE_NAME = "eic/temp/";
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 文档对象 */
    private final DocumentVO document;
    
    /** 文档模板对象 */
    private final CapDocTemplateVO template;
    
    /** 处理进度 */
    private final TaskProgressVo progress;
    
    /** 文档处理观察者 ,用于记录文档处理进度 */
    private final DocumentHandleProgress handleProgress;
    
    /** 外部变量 */
    private final Map<String, Object> extVars;
    
    /** 文档操作记录Facade */
    protected final DocOperLogFacade docOperLogFacade = AppBeanUtil.getBean(DocOperLogFacade.class);
    
    /**
     * 构造函数
     * 
     * @param document 文档对象
     * @param template 文档模板对象
     */
    public DocumentExportProcessor(final DocumentVO document, final CapDocTemplateVO template) {
        this(document, template, null);
    }
    
    /**
     * 构造函数
     * 
     * @param document 文档对象
     * @param template 文档模板对象
     * @param extVars 外部变量
     */
    public DocumentExportProcessor(final DocumentVO document, final CapDocTemplateVO template,
        final Map<String, Object> extVars) {
        this.document = document;
        this.template = template;
        this.progress = new TaskProgressVo();
        this.handleProgress = new DocumentHandleProgress();
        this.extVars = extVars;
    }
    
    /**
     * 
     * @see com.comtop.eic.core.asyn.service.ITaskProcessService#process()
     */
    @Override
    public void process() throws TaskBusinessException {
        this.progress.setTaskStatus(TaskStatus.HANDLING);
        handleProgress.updateTaskStatus(progress);
        ContainerInitializer initializer = (ContainerInitializer) WebGlobalInfo.getServletContext().getAttribute(
            ContainerInitializer.class.getName());
        String webPath = WebGlobalInfo.getWebPath();
        DocxProperties properties = new DocxProperties();
        DocxExportConfiguration config = new DocxExportConfiguration(properties, initializer);
        config.setMaxChapterCount(5000);
        ExpressionExecuteHelper helper = config.getExecuter();
        if (StringUtils.isBlank(document.getId())) {
            helper.setVariable("documentId", "未创建Document对象的文档ID");
        } else {
            helper.setVariable("documentId", document.getId());
        }
        helper.setVariable("domainId", document.getBizDomain());
        helper.setVariable("packageId", document.getBizDomain());
        if (this.extVars != null && !this.extVars.isEmpty()) {
            for (Entry<String, Object> extVar : extVars.entrySet()) {
                helper.setVariable(extVar.getKey(), extVar.getValue());
            }
        }
        String name = clear(document.getName());
        properties.setTitle(name);
        String time = DateTimeUtil.formatAsString(new Date(System.currentTimeMillis()), "yyyyMMddHHmmssSSS");
        config.setTemplatePath(WebGlobalInfo.getWebInfoPath() + template.getPath());
        String pathPrefix = webPath.replace("\\", "/") + TEMP_FILE_NAME + name + "_" + time;
        String fileEngPath = webPath.replace("\\", "/") + TEMP_FILE_NAME + "docexport" + "-" + document.getId() + "_"
            + time;
        String zipPath = fileEngPath + ".zip";
        String filePath = pathPrefix + ".docx";
        String logPath = fileEngPath + ".log";
        config.setLogPath(logPath);
        LoggerConfigUtil.update("com.comtop.cap.document", DocumentExportProcessor.class.getSimpleName(), logPath);
        config.setFilePath(filePath);
        DocxWriter writer = new DocxWriter();
        Map<String, Object> logPropertie = new HashMap<String, Object>();
        logPropertie.put("logFilePath", "/" + logPath.substring(webPath.length()));
        if (CAPStringUtils.isNotBlank(document.getOperateLogId())) {
            docOperLogFacade.updatePropertiesById(document.getOperateLogId(), logPropertie);
        }
        try {
            boolean result = writer.write(config);
            LoggerConfigUtil.finish("com.comtop.cap.document", DocumentExportProcessor.class.getSimpleName());
            pack(zipPath, new String[] { filePath, logPath });
            this.progress.setTaskStatus(result ? TaskStatus.SOLVED : TaskStatus.EXCEPTION_OVER);
            this.progress.setFilePath(zipPath);
            this.progress.setErrorInfo(result ? null : "文件导出过程中存在错误，请下载后查看导出日志！");
            this.handleProgress.updateTaskStatus(progress);
            logPropertie.put("fileAddr", "/" + zipPath.substring(webPath.length()));
            if (CAPStringUtils.isNotBlank(document.getOperateLogId())) {
                docOperLogFacade.updatePropertiesById(document.getOperateLogId(), logPropertie);
                docOperLogFacade.updateOperResult(document.getOperateLogId(), "SUCCEED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.progress.setErrorInfo(StringUtils.isBlank(e.getMessage()) ? e.getClass().getName() : e.getMessage());
            this.progress.setTaskStatus(TaskStatus.EXCEPTION_OVER);
            this.progress.setFilePath(null);
            this.handleProgress.updateTaskStatus(progress);
            if (CAPStringUtils.isNotBlank(document.getOperateLogId())) {
                docOperLogFacade.updateOperResult(document.getOperateLogId(), "FAIL");
            }
        } finally {
            LoggerConfigUtil.finish("com.comtop.cap.document", DocumentExportProcessor.class.getSimpleName());
            System.gc();
        }
    }
    
    /**
     * 打包
     *
     * @param path zip路径
     * @param filePaths 文件路径
     * 
     */
    private void pack(String path, String... filePaths) {
        ZipArchiveOutputStream zos = null;
        FileOutputStream fos = null;
        int index = path.lastIndexOf('/') + 1;
        try {
            fos = new FileOutputStream(path);
            zos = new ZipArchiveOutputStream(fos);
            zos.setMethod(ZipArchiveOutputStream.DEFLATED);
            zos.setLevel(Deflater.BEST_COMPRESSION);
            zos.setEncoding(System.getProperty("file.encoding"));
            // String name;
            byte[] buffer;
            ZipArchiveEntry entry;
            for (String filePath : filePaths) {
                FileInputStream fis = null;
                File file = new File(filePath);
                String entryName = filePath.substring(index);
                buffer = new byte[1024];
                int count;
                try {
                    // name = new String(entryName.getBytes(System.getProperty("file.encoding")),
                    // Charset.forName("UTF-8"));
                    entry = new ZipArchiveEntry(entryName);
                    zos.putArchiveEntry(entry);
                    fis = new FileInputStream(file);
                    while ((count = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, count);
                        zos.flush();
                    }
                } finally {
                    zos.closeArchiveEntry();
                    IOUtils.closeQuietly(fis);
                    file.delete();
                }
            }
        } catch (Exception e) {
            throw new DocServiceException(MessageFormat.format("保存文件''{0}''失败！", path), e);
        } finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(fos);
        }
    }
    
    /**
     * 清理名称
     *
     * @param dirty 名称
     * @return 清理后的名称
     */
    private String clear(String dirty) {
        String temp = dirty.replace("-", "");
        char[] chars = { 'V', 'v', '.' };
        for (char flag : chars) {
            int index = temp.indexOf(flag);
            if (index > 0) {
                temp = temp.substring(0, index);
            }
        }
        return temp;
    }
    
    /**
     * @return 获取 handleProgress属性值
     */
    public DocumentHandleProgress getHandleProgress() {
        return handleProgress;
    }
}
