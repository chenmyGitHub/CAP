/******************************************************************************
 * Copyright (C) 2013 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.ptc.login.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade;
import com.comtop.cap.ptc.login.facade.CapLoginFacade;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cip.jodd.madvoc.meta.Action;
import com.comtop.cip.jodd.madvoc.meta.In;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.top.cfg.facade.ConfigFacade;
import com.comtop.top.component.app.session.HttpSessionUtil;
import com.comtop.top.component.app.sna.HttpServletRequestWrapper;
import com.comtop.top.component.app.sna.HttpSessionSidWrapper;
import com.comtop.top.component.app.sna.HttpSessionWrapper;
import com.comtop.top.component.app.sna.SIDKey;
import com.comtop.top.component.app.sna.SNAConfig;
import com.comtop.top.component.common.config.UniconfigManager;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.base.action.BaseAction;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.JsonUtil;
import com.comtop.top.sys.login.facade.LoginFacade;
import com.comtop.top.sys.login.model.LoginMessageDTO;
import com.comtop.top.sys.login.util.LoginConstant;
import com.comtop.top.sys.login.util.LoginUtil;
import com.comtop.top.sys.login.util.OnlineUserUtil;
import com.comtop.top.sys.usermanagement.user.facade.UserManageFacade;
import com.comtop.top.sys.usermanagement.user.model.UserDTO;
import com.comtop.top.workbench.loginpicture.facade.LoginPictureFacade;
import com.comtop.top.workbench.loginpicture.model.LoginPictureVO;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * CAP登陆Action
 *
 * @author 丁庞
 * @since jdk1.6
 * @version 2015年9月21日 丁庞
 */
@MadvocAction
@DwrProxy
public class CapLoginAction extends BaseAction {
    
    /** 登陆图片 */
    @PetiteInject
    private LoginPictureFacade loginPictureFacade;
    
    /**
     * 账号
     */
    @In
    private String account;
    
    /**
     * 密码
     */
    @In
    private String password;
    
    /** * 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(CapLoginAction.class);
    
    /** 登录页面 */
    public static final String LOGIN = BeanContextUtil.getBean(PreferencesFacade.class).getConfig("capLoginURL")
        .getConfigValue();
    
    /** cap登陆Facade */
    protected CapLoginFacade capLoginFacade = AppContext.getBean(CapLoginFacade.class);
    
    /** top登陆Facade */
    protected LoginFacade loginFacade = AppContext.getBean(LoginFacade.class);
    
    /** 用户Facade */
    protected UserManageFacade userManageFacade = AppContext.getBean(UserManageFacade.class);
    
    /**
     * 登陆验证
     * 
     * @return objBMLoginVO
     */
    @Action(value = "/CapLogin.ac", result = "caploginActionResult")
    public CapLoginVO login() {
        CapLoginVO objBMLoginVO = new CapLoginVO();
        if (StringUtil.isNotBlank(account) && StringUtil.isNotBlank(password)) {
            objBMLoginVO = capLoginFacade.queryPersonnelList(account, password);
            HttpServletRequest objRequest = TopServletListener.getRequest();
            if (!objBMLoginVO.getValidateCode()) {
                LoginMessageDTO objLoginMessageDTO = new LoginMessageDTO();
                objLoginMessageDTO.setLoginMessage("failure");
                objRequest.setAttribute("loginMessageVO", JsonUtil.objectToJson(objLoginMessageDTO, true));
                return objBMLoginVO;
            }
            if (objBMLoginVO.getValidateCode()) {
                UserDTO userDTO = this.queryUserByAccount(objBMLoginVO.getRelatedAccount());
                if (userDTO == null) {
                    throw new RuntimeException("关联账号:  " + objBMLoginVO.getRelatedAccount() + " 不存在，请重新指定关联账号.");
                }
                
                Object objInitConfig = objRequest.getAttribute("initConfig");
                if (objInitConfig == null)
                    objRequest.setAttribute("initConfig",
                        JsonUtil.objectToJson(getInitConfig(userDTO.getUserId()), true));
                this.processLogin(objBMLoginVO, userDTO, objRequest);
                
            }
        } else {
            objBMLoginVO.setValidateCode(false);
        }
        return objBMLoginVO;
    }
    
    /**
     * 初始化登录页面
     * 
     * @return 返回初始化好的信息
     */
    @Action("/CapInitLogin.ac")
    public String initLogin() {
        HttpServletRequest objRequest = TopServletListener.getRequest();
        objRequest.setAttribute("initConfig", JsonUtil.objectToJson(this.getInitConfig(), true));
        // String strIpAddr = LoginUtil.getIpAddr(objRequest);
        // loginFacade.saveUserAccessInfo(strIpAddr);
        return LOGIN;
    }
    
    /**
     * @return 初始化信息
     */
    private Map<String, String> getInitConfig() {
        Map<String, String> objMap = new HashMap<String, String>();
        objMap.put("verifyCode", UniconfigManager.getGlobalConfig("verifyCode"));
        objMap.put("httpsPort", UniconfigManager.getGlobalConfig("httpsPort"));
        objMap.put("httpPort", UniconfigManager.getGlobalConfig("httpPort"));
        objMap.put("mainFrameURL", UniconfigManager.getGlobalConfig("mainFrameURL"));
        objMap.put("chineseSystemName", UniconfigManager.getGlobalConfig("chineseSystemName"));
        ConfigFacade objConfigFacade = (ConfigFacade) AppContext.getBean("configFacade");
        objMap.put("SSO4ASwitch", objConfigFacade.getBaseStringConfigValue("ct.top.system.sso4AInterfaceSwitch"));
        objMap.put("forgetPasswordTip", objConfigFacade.getBaseStringConfigValue("ct.top.login.forgetPasswordTip"));
        // 获取登录背景图
        List<LoginPictureVO> loginPicList = loginPictureFacade.queryCurPictureList();
        if (loginPicList != null && loginPicList.size() > 0) {
            TopServletListener.getRequest().setAttribute("loginPictureId", loginPicList.get(0).getId());
        }
        return objMap;
    }
    
    /**
     * 查询用户
     * 
     * @param relatedAccount 参数
     * @return top用户
     */
    private UserDTO queryUserByAccount(String relatedAccount) {
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        userDTOs = userManageFacade.queryUserByUserAccount(relatedAccount);
        if (userDTOs == null || "[]".equals(userDTOs.toString())) {
            userDTOs = userManageFacade.queryUserByUserAccount("SuperAdmin");
        }
        return userDTOs.get(0);
        
    }
    
    /**
     * 处理
     * 
     * @param objBMLoginVO objBMLoginVO
     * @param objUserDTO objUserDTO
     * @param objRequest objRequest
     */
    private void processLogin(CapLoginVO objBMLoginVO, UserDTO objUserDTO, HttpServletRequest objRequest) {
        HttpSession objOldSession = objRequest.getSession();
        Map<String, Object> objAttributeMap = extractAttribute(objOldSession);
        HttpSession objNewSession = objRequest.getSession();
        copyAttribute(objNewSession, objAttributeMap);
        Object objTemp = objNewSession.getAttribute("loginMessageVO");
        // 如果cookie里没有找到，则随机生成一个sid并保存在cookie里。
        String strSid = objNewSession.getId();
        if (objNewSession instanceof HttpSessionWrapper) {
            String strId = ((HttpSessionWrapper) objNewSession).getOriginSession().getId();
            strSid = SIDKey.wrapKey(strId);
        }
        String strSnaKey = SNAConfig.getSessionId();
        if (objNewSession instanceof HttpSessionSidWrapper) {
            
            ((HttpSessionSidWrapper) objNewSession).setSid(strSid);
        }
        
        if (objRequest instanceof HttpServletRequestWrapper) {
            HttpServletRequestWrapper objRequestWrapper = (HttpServletRequestWrapper) objRequest;
            objRequestWrapper.setSid(strSid);
            objRequestWrapper.setCookie(strSnaKey, strSid);
        }
        // 初始化系统管理功能是否隐藏配置项的值信息
        LoginUtil.initHideSystemBtnCfg();
        HttpSessionUtil.saveLoginInfoToSession(objUserDTO);
        OnlineUserUtil.addUserSessionIdsMap(HttpSessionUtil.getSession().getId(), objUserDTO.getAccount());
        OnlineUserUtil.addOnlineUser(objUserDTO);
        // 建模登陆人员信息存储到session
        CapLoginUtil.setCapCurrentUserSession(objBMLoginVO);
        // 用户身份验证通过，并且用户不存在多组织情况
        // if (!StringUtil.equals(LoginConstant.HAS_MANY_IDENTITY, objLoginMessageDTO.getLoginMessage())) {
        // 记录登录日志，多部门时在resetUserOrgInfo方法中调用logLogin方法记录日志
        this.logLogin(LoginConstant.LOG_COMMON_LOGIN_TYPE);
        // }
        // 获取验证通过后的页面跳转地址
        objNewSession.setAttribute("loginMessageVO", objTemp);
    }
    
    /**
     * 记录用户登入的日志
     * 
     * @param operType 用户的操作类型
     */
    private void logLogin(int operType) {
        loginFacade.logLogin(operType);
    }
    
    /**
     * 将旧Session中信息放到新Session中
     * 
     * @param newSession 新Session
     * @param attributeMap 旧Session信息
     */
    private void copyAttribute(HttpSession newSession, Map<String, Object> attributeMap) {
        String objName;
        Object objValue;
        for (Map.Entry<String, Object> objEntry : attributeMap.entrySet()) {
            objName = objEntry.getKey();
            objValue = objEntry.getValue();
            newSession.setAttribute(objName, objValue);
        }
    }
    
    /**
     * 将旧Session中的信息复制出来，并将旧Session置为无效
     * 
     * @param oldSession 旧Session
     * @return valueMap
     */
    private Map<String, Object> extractAttribute(HttpSession oldSession) {
        Enumeration<String> objNames = oldSession.getAttributeNames();
        Map<String, Object> objValueMap = new HashMap<String, Object>();
        String objKey;
        Object objValue;
        for (; objNames.hasMoreElements();) {
            
            try {
                objKey = objNames.nextElement();
                objValue = oldSession.getAttribute(objKey);
                objValueMap.put(objKey, objValue);
            } catch (Exception e) {
                LOGGER.error("session复制失败，失败信息：" + e.getMessage());
            }
        }
        oldSession.invalidate();
        return objValueMap;
    }
    
    /**
     * @param userId 关联账户id
     * @return Map
     */
    private Map getInitConfig(String userId) {
        Map objMap = new HashMap();
        objMap.put("verifyCode", UniconfigManager.getGlobalConfig("verifyCode"));
        objMap.put("httpsPort", UniconfigManager.getGlobalConfig("httpsPort"));
        objMap.put("httpPort", UniconfigManager.getGlobalConfig("httpPort"));
        objMap.put("mainFrameURL", UniconfigManager.getGlobalConfig("mainFrameURL"));
        objMap.put("chineseSystemName", UniconfigManager.getGlobalConfig("chineseSystemName"));
        ConfigFacade objConfigFacade = (ConfigFacade) AppContext.getBean("configFacade");
        objMap.put("SSO4ASwitch", objConfigFacade.getBaseStringConfigValue("ct.top.system.sso4AInterfaceSwitch"));
        objMap.put("forgetPasswordTip", objConfigFacade.getBaseStringConfigValue("ct.top.login.forgetPasswordTip"));
        // 获取登录背景图
        List<LoginPictureVO> loginPicList = loginPictureFacade.queryCurPictureList();
        if (loginPicList != null && loginPicList.size() > 0)
            TopServletListener.getRequest().setAttribute("loginPictureId", userId);
        return objMap;
    }
    
    /**
     * 退出系统
     * 
     * @return 登录类型，0：常规登录；
     */
    @RemoteMethod
    public String exit() {
        String strLoginType = "0";
        HttpSession objSession = HttpSessionUtil.getSession();
        Object objLogin = objSession.getAttribute("loginType");
        if (objLogin != null) {
            strLoginType = (String) objLogin;
        }
        try {
            if (HttpSessionUtil.isValidSession(objSession)) {
                OnlineUserUtil.removeOnlineUser(objSession.getId());
                CapLoginUtil.removeCapCurrentUserSession(objSession);
                objSession.invalidate();
            }
        } catch (Exception e) {
            LOGGER.error("系统注销出现异常，异常原因：", e);
        }
        LOGGER.info("系统注销，类型：" + strLoginType);
        return strLoginType;
    }
}
