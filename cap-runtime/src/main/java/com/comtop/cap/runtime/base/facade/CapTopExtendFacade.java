/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.facade;

import org.apache.commons.lang.StringUtils;

import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.sys.client.facade.ClientUserFacade;
import com.comtop.top.sys.usermanagement.user.model.UserInfoVO;

/**
 * 人员基本信息扩展实现
 * 
 * @author 李小强
 * @since 1.0
 * @version 2015-12-8  李小强
 */
@PetiteBean
public class CapTopExtendFacade {
	/**
	* 注入ClientUserFacade
	*/
	protected ClientUserFacade clientUserFacade=(ClientUserFacade)AppContext.getBean("clientUserFacade");
	
	
	/**根据用户ID获取用户对应的帐号信息
	 * @param userId 用户ID
	 * @return 用户所对应的帐号信息
	 */
	public String queryUserByUserId(String userId){
		if(StringUtils.isNotBlank(userId)){
			UserInfoVO uservo =	clientUserFacade.queryUserByUserId(userId);
			if(uservo!=null){
				return uservo.getAccount();
			}
		}
		return "";
	}
}
