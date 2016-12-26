/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cip.jodd.madvoc.ActionRequest;
import com.comtop.cip.jodd.madvoc.result.ActionResult;

/**
 * 
 * 导出EA文件的结果处理类
 *
 * @author duqi
 * @since jdk1.6
 * @version 2015年9月11日 duqi
 */
public class ExportEaFileResult extends ActionResult {
    
    /** 日志 */
    private final static Logger LOG = LoggerFactory.getLogger(ExportEaFileResult.class);
    
    /**
     * result mapping name
     */
    public static final String NAME = "exportEaFile";
    
    /**
     * 
     * 构造函数
     */
    public ExportEaFileResult() {
        super(NAME);
    }
    
    /**
     * @param actionRequest actionRequest
     * @param resultObject 结果对象
     * @param resultValue 结果值
     * @param resultPath 结果路径
     */
    @Override
    public void render(ActionRequest actionRequest, Object resultObject, String resultValue, String resultPath)
        throws IOException {
        ExportParam exportParam = (ExportParam) resultObject;
        String filePath;
        String fileName;
        File file = null;
        HttpServletResponse response = actionRequest.getHttpServletResponse();
        response.reset();
        InputStream inputStream = null;
        if ("downloadEaDtdFile".equals(exportParam.getActionType())) {
            inputStream = getDtdFilePath();
            fileName = "UML_EA.DTD";
        } else {
            filePath = exportParam.getFilePath();
            fileName = "class-graph-" + System.currentTimeMillis() + ".xml";
            response.setHeader("Set-Cookie", "fileDownload=true; path=/");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            file = new File(filePath);
            inputStream = new FileInputStream(file);
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            byte buffer[] = new byte[1024];
            int i = 0;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                
            } catch (IOException e) {
                LOG.error("关闭文件输入流是出现IO异常。", e);
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOG.error("关闭输出流是出现IO异常。", e);
            }
            if (!"downloadEaDtdFile".equals(exportParam.getActionType())) {
                // 删除临时文件
                if (file != null && !file.delete()) {
                    // 记录日志
                    LOG.error("删除临时文件出现异常，file=" + file.getAbsolutePath());
                }
            }
            // TODO 处理历史过往删除失败文件
        }
    }
    
    /**
     * 
     * 获取dtd资源文件输入流
     *
     * @return UML_EA.DTD
     */
    private InputStream getDtdFilePath() {
        return ExportEaFileResult.class.getResourceAsStream("/com/comtop/cip/graph/xmi/UML_EA.DTD");
    }
}
