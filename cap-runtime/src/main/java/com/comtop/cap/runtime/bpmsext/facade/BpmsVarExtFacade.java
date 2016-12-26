/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.bpmsext.facade;


import java.util.List;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.base.facade.CapBaseFacade;
import com.comtop.cap.runtime.bpmsext.appservice.BpmsVarExtAppService;
import com.comtop.cap.runtime.bpmsext.model.BpmsVarExtVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;

/**
 * 记录运行时变量DAO
 * 
 * @author CAP超级管理员
 * @since 1.0
 * @version 2016-4-2 CAP超级管理员
 */
@PetiteBean
public class BpmsVarExtFacade extends CapBaseFacade {
    
    /** 注入AppService **/
    @PetiteInject
    protected BpmsVarExtAppService bpmsVarExtAppService;
    
    /**
     * 读取 记录运行时变量 列表
     * 
     * @param condition 查询条件
     * @return 记录运行时变量列表
     */
    public List<BpmsVarExtVO> queryBpmsVarExtList(BpmsVarExtVO condition) {
        return bpmsVarExtAppService.queryBpmsVarExtList(condition);
    }
    
    
    /**
     * 保存记录运行时变量列表
     * 
     * @param condition 记录运行时变量列表
     */
    public void saveBpmsVarExtList(List<BpmsVarExtVO> condition) {
    	bpmsVarExtAppService.saveBpmsVarExtList(condition);
    }


	/** 
	 *
	 * @return xx
	 *		
	 * @see com.comtop.cap.runtime.base.facade.CapBaseFacade#getAppService()
	 */
	@Override
	protected CapBaseAppService getAppService() {
		return null;
	}
}
