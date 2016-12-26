/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.bm.webapp.systemmodel.facade;

import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cap.bm.webapp.systemmodel.appservice.MoveSystemModelAppService;
import com.comtop.top.sys.module.model.ModuleVO;


/**
 * 移动应用、目录、系统业务逻辑处理类
 * @author  李杰
 * @since   1.0
 * @version 2015-5-14 李杰
 */
@PetiteBean
public class MoveSystemModelFacade implements IMoveSystemModelFacade {
    
    /** 业务逻辑处理 */
    @PetiteInject
    MoveSystemModelAppService moveSystemModelAppService;
    
    /**
     * 移动应用、目录、系统
     * @param objMoveModel  应用、目录、系统VO
     * @param strMoveTargetId 移动目标ID
     */
    @Override
    public void moveSystemModel(ModuleVO objMoveModel, String strMoveTargetId) {
        moveSystemModelAppService.moveSystemModel(objMoveModel, strMoveTargetId);
    }
    
}
