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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.component.loader.LoadFile;
import com.comtop.cap.component.loader.util.LoaderUtil;
import com.comtop.cap.doc.DocImportService;
import com.comtop.cap.doc.DocProcessParams;
import com.comtop.cap.doc.DocServiceException;
import com.comtop.cap.doc.info.model.DocumentVO;
import com.comtop.eic.core.asyn.model.TaskProgressVo;
import com.comtop.eic.core.asyn.model.TaskStatus;
import com.comtop.eic.core.asyn.service.ITaskProcessService;
import com.comtop.eic.core.exception.TaskBusinessException;
import com.comtop.top.component.common.systeminit.WebGlobalInfo;
import comtop.org.directwebremoting.io.FileTransfer;

/**
 * 文档导入任务处理器
 *
 * @author lizhiyong
 * @since jdk1.6
 * @version 2015年12月10日 lizhiyong
 */
public class DocumentImportProcessor implements ITaskProcessService {
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 通过DWR上传的文件传递对象 */
    private final FileTransfer fileTransfer;
    
    /** 处理进度 */
    private final TaskProgressVo progress;
    
    /** 文档处理观察者 ,用于记录文档处理进度 */
    private final DocumentHandleProgress handleProgress;
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentImportProcessor.class);
    
    /** Doc处理参数 */
    private final DocProcessParams docProcessParams;
    
    /**
     * 构造函数
     * 
     * @param fileTransfer 文件传输器
     * @param docProcessParams 处理参数
     */
    public DocumentImportProcessor(DocProcessParams docProcessParams, FileTransfer fileTransfer) {
        this.fileTransfer = fileTransfer;
        this.progress = new TaskProgressVo();
        this.handleProgress = new DocumentHandleProgress();
        this.docProcessParams = docProcessParams;
    }
    
    /**
     * 
     * @see com.comtop.eic.core.asyn.service.ITaskProcessService#process()
     */
    @Override
    public void process() throws TaskBusinessException {
        String webPath = WebGlobalInfo.getWebPath();
        // 创建本地临时文件
        DocumentVO document = docProcessParams.getDocument();
        File tempWordFile = creatLocalTempFile(fileTransfer, webPath, document.getName());
        try {
            // 初始设置状态
            this.progress.setTaskStatus(TaskStatus.HANDLING);
            handleProgress.updateTaskStatus(progress);
            // 上传文件到服务器
            LoadFile loadFile = uploadFileToServer(docProcessParams, tempWordFile);
            
            // 填充参数
            docProcessParams.setDocFile(tempWordFile);
            String fileAddr = loadFile.toFileLocation().toHttpUrlString();
            docProcessParams.setFileHttpUrl(fileAddr);
            docProcessParams.setWebPath(webPath);
            
            // 执行导入
            DocImportService docImportService = new DocImportService();
            docImportService.importDoc(docProcessParams);
            
            // 更新进度
            this.progress.setTaskStatus(TaskStatus.SOLVED);
            this.handleProgress.updateTaskStatus(progress);
        } catch (Exception e) {
            LOGGER.error("导入word文档时发生异常", e);
            this.progress.setErrorInfo(e.getMessage());
            this.progress.setTaskStatus(TaskStatus.EXCEPTION_OVER);
            this.progress.setFilePath(null);
            this.handleProgress.updateTaskStatus(progress);
        } finally {
            // 导入不管成功还是失败，均删除临时文件
            tempWordFile.delete();
        }
    }
    
    /**
     * 创建本地临时文件
     *
     * @param file 参数
     * @param rootDir 根目录
     * @param fileName 文件名
     * @return 临时文件
     */
    private File creatLocalTempFile(FileTransfer file, String rootDir, String fileName) {
        File tempWordFile = new File(rootDir + "tempfile/" + fileName);
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            inputStream = fileTransfer.getInputStream();
            if (!tempWordFile.getParentFile().exists()) {
                tempWordFile.getParentFile().mkdirs();
            }
            if (!tempWordFile.exists()) {
                tempWordFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(tempWordFile);
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.flush();
            return tempWordFile;
        } catch (IOException e) {
            LOGGER.error("创建本地临时文件时发生异常：", e);
            throw new DocServiceException("创建本地临时文件时发生异常：", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }
    
    /**
     * 上传文件到服务器
     * 
     * @param params 参数
     * @param tempWordFile 临时文件
     *
     * @return 完整路径
     */
    private LoadFile uploadFileToServer(DocProcessParams params, File tempWordFile) {
        DocumentVO documentVO = docProcessParams.getDocument();
        // 将文件上传到服务器上
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("正在上传文件到服务器：" + documentVO.getName());
        }
        LoadFile loadFile = new LoadFile();
        // uploadKey 上传关键字
        String uploadKey = documentVO.getUploadKey();
        // uploadId 上传id
        String uploadId = documentVO.getUploadId();
        loadFile.setName(documentVO.getName());
        loadFile.setUploadKey(uploadKey);
        loadFile.setContextType(docProcessParams.getMimeType());
        loadFile.setFileSize(docProcessParams.getSize());
        if (loadFile.getFileSize() == 0) { // 大小为0，表示木有文件
            throw new DocServiceException("文件大小为0");
            // return loadFile;
        }
        loadFile.setFileSuffix(LoaderUtil.getSuffix(loadFile.getName()));
        // 上传文件
        if (StringUtils.isBlank(uploadId)) {
            loadFile.setUploadId(LoaderUtil.generateUploadId());
        } else {
            loadFile.setUploadId(uploadId);
        }
        loadFile.setFolderPath(LoaderUtil.getFolderPath(uploadKey, loadFile.getUploadId()));
        // loadFile.setFileName(LoaderUtil.createRandomFileName(loadFile.getName()));
        loadFile.setFileName(loadFile.getName());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(tempWordFile);
            URI uri = LoaderUtil.upLoad(inputStream, loadFile.getFolderPath(), loadFile.getFileName());
            loadFile.setUri(uri);
            // 将文件上传到服务器上
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("上传完成：" + documentVO.getName());
            }
        } catch (IOException e) {
            LOGGER.error("上传时发生异常：" + documentVO.getName(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        
        return loadFile;
    }
    
    /**
     * @return 获取 handleProgress属性值
     */
    public DocumentHandleProgress getHandleProgress() {
        return handleProgress;
    }
    
}
