/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.helper;

import com.comtop.top.component.app.session.HttpSessionUtil;
import com.comtop.top.component.common.config.UniconfigManager;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.sys.client.facade.ClientOrganizationFacade;
import com.comtop.top.sys.usermanagement.organization.model.OrganizationInfoVO;
import com.comtop.top.sys.usermanagement.user.model.UserDTO;

/**
 * 系统工具类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-12 李忠文
 */
public class SystemHelper {
    
    /**
     * 
     * 构造函数
     */
    private SystemHelper() {
        
    }
    
    /** 超级管理员用户的用户编号 配置键 */
    final static String SYSTEM_ADMIN = "SystemAdmin";
    
    /**
     * 获取当前登录用户信息
     * 
     * @return UserInfoVO
     */
    public static UserDTO getCurUserInfo() {
        return (UserDTO) HttpSessionUtil.getCurUserInfo();
    }
    
    /**
     * 获取登录当前用户编号
     * 
     * @return UserInfoVO 当前登录用户编号
     */
    public static String getUserId() {
        UserDTO objUser = null;
        // 判断是否为Web系统登录用户
        if (HttpSessionUtil.getSession() != null) {
            objUser = getCurUserInfo();
        }
        // 非Web系统登录用户
        if (objUser == null || StringUtil.isEmpty(objUser.getUserId())) {
            // 读取配置为超级管理员用户的用户编号
            return UniconfigManager.getGlobalConfig(SYSTEM_ADMIN);
        }
        return objUser.getUserId();
    }
    
    /**
     * 根据组织ID获取组织对象
     * 
     * @param orgId 组织ID
     * @return OrganizationInfoVO 组织对象
     */
    public static OrganizationInfoVO getOrganizationWithOrgId(String orgId) {
        return new ClientOrganizationFacade().queryOrganizationInfoByOrgId(orgId);
    }
}
