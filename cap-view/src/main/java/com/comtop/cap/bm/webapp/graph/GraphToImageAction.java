/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.graph;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.comtop.cip.graph.image.facade.GraphToImageFacadeImpl;
import com.comtop.cip.graph.image.facade.IGraphToImageFacade;
import com.comtop.cip.jodd.madvoc.meta.Action;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.jodd.AppContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试生成图片action
 * @author duqi
 *
 */
@MadvocAction("GraphToImageAction")
public class GraphToImageAction {
	/** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(GraphToImageAction.class);

    /** 测试接口 */
    private final IGraphToImageFacade graphToImageFacade = AppContext.getBean(GraphToImageFacadeImpl.class);

    
    /**
     * 
     */
    @Action(value = "/cip/graph/testToImage.ac")
    public void testToImage() {
    	HttpServletRequest request = TopServletListener.getRequest();
    	Cookie[] cookies = request.getCookies();
    	String cookieString = null;
    	if(cookies != null && cookies.length > 0){
    		ObjectMapper mapper = new ObjectMapper(); 
    		try {
				cookieString = mapper.writeValueAsString(cookies);
			} catch (Exception e) {
				LOGGER.error("cookie转cookie字符串出错", e);
			} 
    	}

    	String baseUrl = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath();
    	System.out.println("baseUrl:" + baseUrl);
    	String moduleRelationImagePath = graphToImageFacade.getModuleRelationImage("E520E65B42334972ADE9460A87B3B544",baseUrl,cookieString).getImagePath();
    	System.out.println("模块关系图：" + moduleRelationImagePath);
    	String resourceRelationImagePath = graphToImageFacade.getResourceRelationImage("221C009410B246D487B9A5CB53D09495",baseUrl,cookieString).getImagePath();
    	System.out.println("资源关系图：" + resourceRelationImagePath);
    	String logicDeploymentImagePath = graphToImageFacade.getLogicDeploymentImage("E520E65B42334972ADE9460A87B3B544",baseUrl,cookieString).getImagePath();
    	System.out.println("逻辑部署图：" + logicDeploymentImagePath);
    	String physicDeploymentImagePath = graphToImageFacade.getPhysicDeploymentImage("E520E65B42334972ADE9460A87B3B544",baseUrl,cookieString).getImagePath();
    	System.out.println("物理部署图：" + physicDeploymentImagePath);
    	String erImagePath = graphToImageFacade.getERImage("C98D9F55F82D494599F2CABEB0F075D1",baseUrl,cookieString).getImagePath();
    	System.out.println("ER图：" + erImagePath);
    	String moduleStructImagePath = graphToImageFacade.getModuleStructImage("5662EECB20B84CA38B26E5439E65654B",baseUrl,cookieString).getImagePath();
    	System.out.println("功能结构图：" + moduleStructImagePath);
    }
    
}
