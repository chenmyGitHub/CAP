/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.comtop.cip.jodd.madvoc.ActionRequest;
import com.comtop.cip.jodd.madvoc.result.ActionResult;
import com.mxgraph.canvas.mxGraphicsCanvas2D;
import com.mxgraph.canvas.mxICanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import com.mxgraph.util.mxUtils;

/**
 * 
 * 图片导出结果处理类
 *
 * @author duqi
 * @since jdk1.6
 * @version 2015年9月11日 duqi
 */
public class ExportImageResult extends ActionResult {
    
    /** 日志 */
    private final static Logger LOG = LoggerFactory.getLogger(ExportImageResult.class);
    
    /**
     * result mapping name
     */
    public static final String NAME = "exportImage";
    
    /**
     * 
     */
    private transient SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    
    /**
     * 
     * 构造函数
     */
    public ExportImageResult() {
        super(NAME);
    }
    
    /**
     * @param actionRequest actionRequest
     * @param resultObject 结果对象
     * @param resultValue 结果值
     * @param resultPath 结果路径
     * @throws IOException IO异常
     */
    @Override
    public void render(ActionRequest actionRequest, Object resultObject, String resultValue, String resultPath)
        throws IOException {
        ExportParam exportParam = (ExportParam) resultObject;
        HttpServletResponse response = actionRequest.getHttpServletResponse();
        BufferedImage image = mxUtils.createBufferedImage(exportParam.getWidth(), exportParam.getHeight(), Color.WHITE);
        Graphics2D g2 = image.createGraphics();
        mxUtils.setAntiAlias(g2, true, true);
        
        mxGraphicsCanvas2D g2c = new mxGraphicsCanvas2D(g2);
        try {
            renderXml(exportParam.getImageXML(), g2c);
        } catch (SAXException e) {
            LOG.error("解析XML时出现SAX异常。", e);
        } catch (ParserConfigurationException e) {
            LOG.error("解析XML配置时出现异常。", e);
        }
        String fName = exportParam.getFileName() + "." + exportParam.getFormat();
        response.setContentType("image/" + exportParam.getFormat().toLowerCase());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fName + "\"");
        ImageIO.write(image, exportParam.getFormat(), response.getOutputStream());
    }
    
    /**
     * Renders the XML to the given canvas.
     * 
     * @param xml 图片XML
     * @param canvas 画布
     * @throws SAXException 异常
     * @throws ParserConfigurationException 解析异常
     * @throws IOException IO异常
     */
    protected void renderXml(String xml, mxICanvas2D canvas) throws SAXException, ParserConfigurationException,
        IOException {
        XMLReader reader = parserFactory.newSAXParser().getXMLReader();
        reader.setContentHandler(new mxSaxOutputHandler(canvas));
        reader.parse(new InputSource(new StringReader(xml)));
    }
}
