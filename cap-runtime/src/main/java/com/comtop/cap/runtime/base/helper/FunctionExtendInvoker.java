/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.helper;

import java.util.ArrayList;
import java.util.List;

import com.comtop.bpms.depend.userorg.model.UserVO;
import com.comtop.cap.runtime.base.exception.CapWorkflowException;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.sys.client.facade.ClientOrganizationFacade;

/**
 * bpms扩展函数类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-6 李忠文
 */
public final class FunctionExtendInvoker {
    
    /**
     * 构造函数
     */
    private FunctionExtendInvoker() {
    }
    
    /**
     * 通过组织id过滤人员
     * 
     * @param assignUserList 人员列表
     * 
     *            <pre>
     * BPMS使用反射注入,参数必须使用List的实现类ArrayList
     * </pre>
     * @param orgId 组织id
     * @return 指定组织id下的人员列表
     */
    public static List<UserVO> getBelongToDepartUsers(ArrayList<UserVO> assignUserList, String orgId) {
        ClientOrganizationFacade objClientOrganizationFacade = AppBeanUtil.getBean(ClientOrganizationFacade.class);
        List<UserVO> lstFilterUserList = new ArrayList<UserVO>();
        for (UserVO objUserVo : assignUserList) {
            if (objClientOrganizationFacade.hasUserInOrgChain(objUserVo.getId(), orgId)) {
                lstFilterUserList.add(objUserVo);
            }
        }
        if (lstFilterUserList.size() == 0) {
            throw new CapWorkflowException("通过过滤函数【getBelongToDepartUsers】获取处理人失败！");
        }
        return lstFilterUserList;
    }
    
    /**
     * 通过用户ID过滤当前节点对应的用户
     * 
     * @param assignUserList 人员列表
     * 
     *            <pre>
     * BPMS使用反射注入,参数必须使用List的实现类ArrayList
     * </pre>
     * @param userId 用户ID
     * @return 指定组织id下的人员列表
     */
    public static List<UserVO> getUserByUserId(ArrayList<UserVO> assignUserList, String userId) {
        List<UserVO> lstFilterUserList = new ArrayList<UserVO>();
        for (UserVO objUserVo : assignUserList) {
            if (objUserVo.getId().equals(userId)) {
                lstFilterUserList.add(objUserVo);
            }
        }
        if (lstFilterUserList.size() == 0) {
            throw new CapWorkflowException("通过过滤函数【getUserByUserId】获取处理人失败，原因为：当前用户未正确配置到当前节点！");
        }
        return lstFilterUserList;
    }
}
