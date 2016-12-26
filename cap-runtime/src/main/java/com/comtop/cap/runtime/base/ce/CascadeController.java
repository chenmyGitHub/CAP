/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.ce;

import java.util.List;

import com.comtop.cap.runtime.base.ce.cmd.CmdFactory;
import com.comtop.cap.runtime.base.ce.cmd.IEntityCommand;
import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cap.runtime.base.model.CascadeVO;

/**
 * 级联操作控制器
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月29日 龚斌
 */
public final class CascadeController {
    
    /**
     * 控制器单例
     */
    private static CascadeController controller = new CascadeController();
    
    /**
     * 
     * 构造函数
     */
    private CascadeController() {
    }
    
    /**
     * 获取流程控制器
     * 
     * @return 流程控制器
     */
    public static CascadeController getInstance() {
        if (controller == null) {
            controller = new CascadeController();
        }
        return controller;
    }
    
    /**
     * 级联控制器执行方法
     * 
     * @param vo 业务VO对象
     * @param lstCascade 级联VO对象集合
     * @param operateType 操作类型
     */
    public void run(CapBaseVO vo, List<CascadeVO> lstCascade, String operateType) {
        if (lstCascade == null || lstCascade.size() == 0) { // 级联VO对象集合为空，不需级联，直接返回
            return;
        }
        IEntityCommand objFirstCmd = CmdFactory.createIterateCmd(vo, lstCascade, operateType);
        objFirstCmd.execute();
    }
}
