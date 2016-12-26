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
 * 级联操作执行器
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月29日 龚斌
 */
public class IteratorExecutor implements IIteratorExcutor {
    
    /**
     * 级联链表
     */
    private List<CascadeVO> lstCascade;
    
    /**
     * 实体vo对象
     */
    private CapBaseVO vo;
    
    /**
     * 操作类型
     */
    private String operateType;
    
    /**
     * 构造函数
     * 
     * @param vo 对象
     * @param lstCascade 级联集合
     * @param operateType 操作类型
     */
    public IteratorExecutor(CapBaseVO vo, List<CascadeVO> lstCascade, String operateType) {
        super();
        this.lstCascade = lstCascade;
        this.vo = vo;
        this.operateType = operateType;
    }
    
    @Override
    public void iterate() {
        for (CascadeVO objCascadeVO : lstCascade) {
            IEntityCommand objCmd = CmdFactory.createEntityCmd(vo, objCascadeVO, operateType);
            objCmd.execute();
        }
    }
}
