/******************************************************************************
 * Copyright (C) 2012 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.ptc.login.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.top.component.common.config.UniconfigManager;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.core.util.constant.NumberConstant;
import com.comtop.top.sys.login.util.LoginConstant;

/**
 * 验证Cap Session是否有效过滤器
 * 
 * @author 杨赛
 * @since 1.0
 * @version 2013-11-02 杨赛
 */
public class CapSessionFilter implements Filter {
    
    /** Logger 日志对象 */
    private final static Logger LOGGER = LoggerFactory.getLogger(CapSessionFilter.class);
    
    /** 不操作的页面数组 */
    private static String[] freePages;
    
    /** 不操作的页面正则表达式 */
    private static String[] regexFreePages;
    
    /** 无效时返回的页面 */
    private static String toPage = null;
    
    /** 页面主框架名字 */
    private static String mainFrameName;
    
    /** 登录成功后页面url */
    private static String mainIframeUrl;
    
    /** 是否开启capsessionFilter */
    private static boolean enabled = false;
    
    /** web.xml 配置项enabled */
    public final static String FILTER_ENABLED = "enabled";
    
    /** 首选项的facade实例 **/
    private final PreferencesFacade preferencesFacade = BeanContextUtil.getBean(PreferencesFacade.class);
    
    /**
     * @param enabled 设置是否开启capsessionFilter
     */
    private static void setEnabled(boolean enabled) {
        CapSessionFilter.enabled = enabled;
    }
    
    /**
     * @param mainIframeUrl 设置 mainIframeUrl 属性值为参数值 mainIframeUrl
     */
    private static void setMainIframeUrl(String mainIframeUrl) {
        CapSessionFilter.mainIframeUrl = mainIframeUrl;
    }
    
    /**
     * @param regexFreePages 设置 regexFreePages 属性值为参数值 regexFreePages
     */
    public static void setRegexFreePages(String[] regexFreePages) {
        CapSessionFilter.regexFreePages = regexFreePages;
    }
    
    /**
     * @param freePages 设置 freePages 属性值为参数值 freePages
     */
    private static void setFreePages(String[] freePages) {
        CapSessionFilter.freePages = freePages;
    }
    
    /**
     * @param toPage 设置 toPage 属性值为参数值 toPage
     */
    private static void setToPage(String toPage) {
        CapSessionFilter.toPage = toPage;
    }
    
    /**
     * 设置主框架名
     * 
     * @param mainFrameName 主框架名
     */
    private static void setMainFrameName(String mainFrameName) {
        CapSessionFilter.mainFrameName = mainFrameName;
    }
    
    /**
     * <pre>
     * 初始化filter
     * 1、检查SessionFilter的可通过页面的配置情况
     * 2、检查SessionFilter的拦截后去向页面的配置情况
     * </pre>
     * 
     * @param config FilterConfig filter配置对象
     * @throws ServletException Wervlet异常
     */
    @Override
    public void init(final FilterConfig config) throws ServletException {
        setToPage(preferencesFacade.getConfig("capLoginURL").getConfigValue());
        setMainIframeUrl(preferencesFacade.getConfig("capMainpage").getConfigValue());
        // 以下从配置文件获取配置信息
        Enumeration<String> objNames = config.getInitParameterNames();
        String strFreePages = null;
        String strRegexFreePages = null;
        while (objNames.hasMoreElements()) {
            String strName = objNames.nextElement();
            if (LoginConstant.FREE_PAGES.equals(strName)) {
                strFreePages = config.getInitParameter(LoginConstant.FREE_PAGES);
            } else if (LoginConstant.REGEX_FREE_PAGES.equals(strName)) {
                strRegexFreePages = config.getInitParameter(LoginConstant.REGEX_FREE_PAGES);
            } else if (FILTER_ENABLED.equals(strName)) {
                CapSessionFilter.setEnabled("true".equals(config.getInitParameter(FILTER_ENABLED)));
            }
        }
        CapSessionFilter.setMainFrameName(config.getInitParameter(LoginConstant.MAIN_FRAME_NAME));
        if (StringUtil.isBlank(strFreePages)) {
            LOGGER.error("web.xml中SessionFilter没有配置初始化参数\"freePage\"。");
            throw new ServletException("web.xml中SessionFilter没有配置初始化参数\"freePage\"。");
        }
        if (StringUtil.isBlank(toPage)) {
            LOGGER.error("首选项元数据文件中中没有配置登录地址\"capLoginURL\"。");
            throw new ServletException("首选项元数据文件中中没有配置登录地址\"capLoginURL\"。");
        }
        
        if (StringUtil.isBlank(mainIframeUrl)) {
            LOGGER.error("首选项元数据文件中中没有配置登录地址\"capMainpage\"。");
            throw new ServletException("首选项元数据文件中中没有配置登录地址\"capMainpage\"。");
        }
        if (strRegexFreePages == null || StringUtil.isBlank(strRegexFreePages)) {
            LOGGER.warn("web.xml中SessionFilter没有配置初始化参数\"regexFreePages\"。");
        } else {
            CapSessionFilter.setRegexFreePages(strRegexFreePages.split(";"));
        }
        if (strFreePages != null) {
            CapSessionFilter.setFreePages(strFreePages.split(";"));
        }
        if (!isFreePage(toPage) && !isRegexFreePage(toPage)) {
            LOGGER.error("web.xml中SessionFilter初始化参数\"toPage\"的值必须是\"freePage\"中的某个页面或者是符合\"regexFreePages\"表达式.");
            throw new ServletException(
                "web.xml中SessionFilter初始化参数\"toPage\"的值必须是\"freePage\"中的某个页面或者是符合\"regexFreePages\"表达式.");
        }
    }
    
    /**
     * 过滤动作
     * 
     * @param servletRequest ServletRequest 请求对象
     * @param servletResponse ServletResponse 响应对象
     * @param filterChain FilterChain 过滤器链对象
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
        final FilterChain filterChain) throws IOException, ServletException {
        if (!enabled) { // 未开启的话就直接跳过
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        // TopServletListener.setRequest(request);
        final HttpSession session = request.getSession();
        final String strRequestURI = request.getRequestURI();
        final String strQueryString = request.getQueryString();
        final String strContextPath = request.getContextPath();
        final String strRequestPage = strRequestURI.substring(strContextPath.length());
        
        if (this.isFreePage(strRequestPage) || isRegexFreePage(strRequestPage)) { // 自由页面直接访问
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        if (!isValidCAPSession(session)) { // 未登录跳转到cap登录页面
        
            if (this.isRootPath(strRequestPage)) { // 已登录且请求的是rootpath跳转到登录后的首页
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/CapInitLogin.ac")); // 跳转到CapInitLogin.ac
                return;
            }
            
            if ("/top/ReLogin.jsp".equals(strRequestPage) && StringUtil.isNotBlank(strQueryString)
                && StringUtil.contains(strQueryString, "url=/CapInitLogin.ac")) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/CapInitLogin.ac")); // 跳转到CapInitLogin.ac
                return;
            }
            
            StringBuffer sbSkip = new StringBuffer(NumberConstant.HUNDRED_TWENTY);
            sbSkip.append("/cap/ptc/login/jsp/CapReLogin.jsp?mainFrameName=");
            sbSkip.append(CapSessionFilter.mainFrameName);
            if (isRedirectURL(strRequestPage)) {
                sbSkip.append("&url=").append(strRequestPage);
            }
            if (StringUtil.isNotBlank(strQueryString)) {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + sbSkip.toString() + "?"
                    + strQueryString));
            } else {
                response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + sbSkip.toString()));
            }
            // response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/CapInitLogin.ac"));
            // //跳转到CapInitLogin.ac
            return;
        }
        
        if (this.isRootPath(strRequestPage)) { // 已登录且请求的是rootpath跳转到登录后的首页
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + mainIframeUrl));
            return;
        }
        
        if (!response.isCommitted()) { // 如果响应未提交,交给过滤器链
            try {
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (IOException ex) {
                LOGGER.error("Session filter过滤时发生IO异常", ex);
            }
        }
    }
    
    /**
     * 验证CAPSession是否有登录用户
     * 
     * @param session session
     * @return true 登录 false未登录
     */
    private boolean isValidCAPSession(HttpSession session) {
        CapLoginVO objCapUserVO = (CapLoginVO) session.getAttribute(CapLoginUtil.CAP_CURRENT_USER);
        return objCapUserVO != null;
    }
    
    /**
     * 判断一个请求URI是否是指定跳转URL
     * 
     * @param url 请求URI
     * @return bRtn 判断结果
     */
    private boolean isRedirectURL(String url) {
        String strUrls = UniconfigManager.getGlobalConfig("redirectURL");
        if (StringUtil.isBlank(strUrls)) {
            return false;
        }
        // 若配置了*.*则任意url都可以跳转
        if (strUrls.indexOf("*.*") > NumberConstant.MINUS_ONE) {
            return true;
        }
        return strUrls.indexOf(url) == NumberConstant.MINUS_ONE ? false : true;
    }
    
    /**
     * 判断一个请求URI是否是不过滤的页面
     * 
     * @param requestURI String 请求URI
     * @return boolean 返回true为不过滤页面
     */
    private boolean isFreePage(final String requestURI) {
        final int iLength = freePages.length;
        for (int i = NumberConstant.ZERO; i < iLength; i++) {
            if (requestURI.endsWith(freePages[i])) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断一个请求URI是否是不过滤的页面【通过正则表达式来过滤】
     * 
     * @param requestURI String 请求URI
     * @return boolean 返回true为不过滤页面
     */
    private boolean isRegexFreePage(final String requestURI) {
        boolean bResult = false;
        if (regexFreePages != null && regexFreePages.length > NumberConstant.ZERO) {
            final int iLength = regexFreePages.length;
            Pattern objPattern = null;
            Matcher objMatcher = null;
            for (int i = NumberConstant.ZERO; i < iLength; i++) {
                try {
                    objPattern = Pattern.compile(regexFreePages[i], Pattern.CASE_INSENSITIVE);
                    objMatcher = objPattern.matcher(requestURI);
                    if (objMatcher.matches()) {
                        bResult = true;
                        break;
                    }
                } catch (PatternSyntaxException e) {
                    LOGGER.error("web.xml中\"regexFreePages\"中的正则表达式语法错误，请修改。", e);
                    bResult = false;
                }
            }
        }
        return bResult;
    }
    
    /**
     * @param requestPath requestPath
     * @return false
     */
    private boolean isRootPath(String requestPath) {
        if (requestPath.startsWith("/cap/;jsession") || requestPath.equals("/cap/") || requestPath.equals("/cap")
            || requestPath.startsWith("/top/Login.jsp") || requestPath.startsWith("/CapInitLogin.ac")) {
            return true;
        }
        return false;
    }
    
    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // 实现filter的销毁方法，目前为空方法。
    }
    
}
