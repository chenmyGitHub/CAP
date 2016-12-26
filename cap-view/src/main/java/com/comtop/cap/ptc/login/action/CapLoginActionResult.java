/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.ptc.login.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cip.jodd.madvoc.ActionRequest;
import com.comtop.cip.jodd.madvoc.result.ActionResult;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.top.component.app.sna.SNAConfig;
import com.comtop.top.core.util.constant.NumberConstant;
import com.comtop.top.sys.login.appservice.IOperateAfterLogin;
import com.comtop.top.sys.login.facade.LoginFacade;

/**
 * 
 * 登录成功后响应类
 * 
 * @author 丁庞
 * @since jdk1.6
 * @version 2015年9月23日 丁庞
 */
public class CapLoginActionResult extends ActionResult {
    
    /** 登录页面 */
    public static final String LOGIN = BeanContextUtil.getBean(PreferencesFacade.class).getConfig("capLoginURL")
        .getConfigValue();
    
    /** 登录页面 */
    
    /** 登录页面 */
    public static final String CAP_MAIN_PAGE = BeanContextUtil.getBean(PreferencesFacade.class)
        .getConfig("capMainpage").getConfigValue();
    
    /**
     * result mapping name
     */
    public static final String NAME = "caploginActionResult";
    
    /**
     * 构造函数
     */
    public CapLoginActionResult() {
        super(NAME);
    }
    
    /**
     * 注入LoginFacade
     */
    @PetiteInject
    private LoginFacade loginFacade;
    
    /**
     * 
     * @see com.comtop.cip.jodd.madvoc.result.ActionResult #render(com.comtop.cip.jodd.madvoc.ActionRequest,
     *      java.lang.Object, java.lang.String, java.lang.String)
     * @param actionRequest actionRequest
     * @param resultObject resultObject
     * @param resultValue resultValue
     * @param resultPath resultPath
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    @Override
    public void render(ActionRequest actionRequest, Object resultObject, String resultValue, String resultPath)
        throws IOException, ServletException {
        
        HttpServletResponse objResponse = actionRequest.getHttpServletResponse();
        HttpServletRequest objRequest = actionRequest.getHttpServletRequest();
        objResponse.setHeader("P3P", "CP=\"NON DSP COR CURa ADMa DEVa TAIa PSAa PSDa IVAa IVDa CONa HISa TELa "
            + "OTPa OUR UNRa IND UNI COM NAV INT DEM CNT PRE LOC\"");
        objResponse.setContentType("text/html;charset=utf-8");
        CapLoginVO objBMLoginVO = (CapLoginVO) resultObject;
        String strSnaId = SNAConfig.getSessionId();
        String strSid = objRequest.getSession().getId();
        Cookie objCookie = new Cookie(strSnaId, strSid);
        // path应该是/，因为该Cookie将被这个domain的所有请求使用
        objCookie.setPath("/");
        // maxAge应该是-1，因为会话在浏览器关闭时即失效
        objCookie.setMaxAge(NumberConstant.MINUS_ONE);
        objResponse.addCookie(objCookie);
        if (objBMLoginVO.getValidateCode()) {
            // 验证成功之后，调用对外提供的接口，处理业务相关的操作，平台默认为不配置，
            IOperateAfterLogin objIOperateAfterLogin = loginFacade.getiOperateAfterLogin();
            if (objIOperateAfterLogin != null) {
                objIOperateAfterLogin.doBusinessAfterLogin();
            }
            String strRedirectUrl = this.getRedirectURL();
            objResponse.sendRedirect(objRequest.getContextPath() + strRedirectUrl);
        } else {
            objRequest.getRequestDispatcher(LOGIN).forward(objRequest, objResponse);
        }
        
    }
    
    /**
     * 获取验证通过后的页面跳转地址
     * 
     * @return String
     */
    private String getRedirectURL() {
        return CAP_MAIN_PAGE;
    }
}
