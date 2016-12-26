/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.test.robot;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.test.robot.ResponseData.ResultType;
import com.comtop.top.core.util.JsonUtil;

/**
 * Robot测试工具，远程方法调用转发
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2016年6月30日 lizhongwen
 */
public class RemoteMethodDispatcher extends HttpServlet {
    
    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    /** 日志 */
    private final static Logger logger = LoggerFactory.getLogger(RemoteMethodDispatcher.class);
    
    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<?, ?> params = req.getParameterMap();
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String json = req.getParameter("RequestData");
        if (StringUtils.isBlank(json) && params != null && !params.isEmpty()) {
            json = (String) params.keySet().iterator().next();
        }
        ResponseData response;
        if (StringUtils.isBlank(json)) {
            response = new ResponseData();
            response.setType(ResultType.FAIL);
            response.setMessage("请求数据不能为空！");
        } else {
            try {
                RequestData data = JsonUtil.jsonToObject(json, RequestData.class);
                response = MethodExecutor.execute(data);
            } catch (Throwable e) {
                response = new ResponseData();
                response.setType(ResultType.FAIL);
                response.setMessage("请求数格式不正确！");
                response.setDetailMessage(e.getMessage());
                logger.error(e.getMessage(), e);
            }
        }
        String result = JsonUtil.objectToJson(response);
        resp.getWriter().write(result);
        resp.getWriter().flush();
    }
    
    /**
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
