
package com.comtop.cap.bm.webapp.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.zip.Deflater;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.doc.DocServiceException;
import com.comtop.cap.doc.info.action.DocumentAction;
import com.comtop.cip.graph.uml.facade.GraphUmlFacadeImpl;
import com.comtop.cip.graph.uml.facade.IGraphUmlFacade;
import com.comtop.eic.core.asyn.model.TaskProgressVo;
import com.comtop.eic.core.asyn.model.TaskStatus;
import com.comtop.eic.core.asyn.service.ITaskProcessService;
import com.comtop.eic.core.exception.TaskBusinessException;
import com.comtop.top.core.jodd.AppContext;

/**
 * Graph EA文件导出业务执行者
 *
 * @author 刘城
 * @since jdk1.6
 * @version 2016年12月22日 刘城
 */
public class GraphExportProcessor implements ITaskProcessService {
    
    /** 处理进度 */
    private final TaskProgressVo progress;
    
    /** 文档处理观察者 ,用于记录文档处理进度 */
    private final GraphHandleProgress handleProgress;
    
    /** 外部变量 */
    private final Map<String, Object> extVars;
    
    /** 界面调用接口 */
    private final IGraphUmlFacade graphUmlFacade = AppContext.getBean(GraphUmlFacadeImpl.class);
    
    /** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentAction.class);
    
    /**
     * 构造函数
     */
    public GraphExportProcessor() {
        this(null);
    }
    
    /**
     * 构造函数
     * 
     * @param extVars 外部变量
     */
    public GraphExportProcessor(final Map<String, Object> extVars) {
        super();
        this.progress = new TaskProgressVo();
        this.handleProgress = new GraphHandleProgress();
        this.extVars = extVars;
    }
    
    @Override
    public void process() throws TaskBusinessException {
        this.progress.setTaskStatus(TaskStatus.HANDLING);
        handleProgress.updateTaskStatus(progress);
        try {
            boolean result = true;
            String dtdFilePath;
            String eaFilePath = "";
            String zipPath = "";
            String tmpFileDir = "";
            String exModuleId = "";
            // 获取传递过来的参数
            if (this.extVars != null && !this.extVars.isEmpty()) {
                zipPath = (String) extVars.get("zipPath");
                tmpFileDir = (String) extVars.get("tmpFileDir");
                eaFilePath = (String) extVars.get("eaFilePath");
                exModuleId = (String) extVars.get("exModuleId");
            }
            File file = new File(eaFilePath);
            graphUmlFacade.exportXMIFile(file, exModuleId);
            // 业务逻辑
            dtdFilePath = tmpFileDir + File.separator + "UML_EA.DTD";
            writeEAXmlFile(eaFilePath, exModuleId);
            pack(zipPath, new String[] { dtdFilePath, eaFilePath });
            this.progress.setTaskStatus(result ? TaskStatus.SOLVED : TaskStatus.EXCEPTION_OVER);
            this.progress.setFilePath(zipPath);
            this.progress.setErrorInfo(result ? null : "文件导出过程中存在错误，请下载后查看导出日志！");
            this.handleProgress.updateTaskStatus(progress);
        } catch (Exception e) {
            LOGGER.error("EA文件导出出现错误", e.getMessage());
            this.progress.setErrorInfo(StringUtils.isBlank(e.getMessage()) ? e.getClass().getName() : e.getMessage());
            this.progress.setTaskStatus(TaskStatus.EXCEPTION_OVER);
            this.progress.setFilePath(null);
            this.handleProgress.updateTaskStatus(progress);
        } finally {
            System.gc();
        }
        
    }
    
    /**
     * 写出EA xml临时文件
     * 
     * @param filePath 临时文件路径
     * @param moduleId 模块ID
     * 
     */
    private void writeEAXmlFile(String filePath, String moduleId) {
        File file = new File(filePath);
        graphUmlFacade.exportXMIFile(file, moduleId);
    }
    
    /**
     * @return 获取 handleProgress属性值
     */
    public GraphHandleProgress getHandleProgress() {
        return handleProgress;
    }
    
    /**
     * @return 获取 progress属性值
     */
    public TaskProgressVo getProgress() {
        return progress;
    }
    
    /**
     * @return 获取 extVars属性值
     */
    public Map<String, Object> getExtVars() {
        return extVars;
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
                String entryName = file.getName();
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
    
}
