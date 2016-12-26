/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.ce.cmd;

import java.util.List;

import com.comtop.cap.runtime.base.ce.EntityExecutor;
import com.comtop.cap.runtime.base.ce.IExecutor;
import com.comtop.cap.runtime.base.ce.IteratorExecutor;
import com.comtop.cap.runtime.base.model.CapBaseVO;
import com.comtop.cap.runtime.base.model.CascadeVO;
import com.comtop.cap.runtime.base.util.CapRuntimeConstant;

/**
 * 命令工厂类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月15日 龚斌
 */
public final class CmdFactory {
    
    /**
     * 
     * 构造函数
     */
    private CmdFactory() {
    }
    
    /**
     * 
     * 获取迭代命令
     * 
     * @param vo vo
     * @param lstCascade lstCascade
     * @param operateType operateType
     * @return IEntityCommand
     */
    public static IEntityCommand createIterateCmd(CapBaseVO vo, List<CascadeVO> lstCascade, String operateType) {
        IteratorExecutor objExecutor = new IteratorExecutor(vo, lstCascade, operateType);
        // todo 根据操作类型获取命令
        IteratorCmd objCmd = new IteratorCmd(objExecutor);
        return objCmd;
    }
    
    /**
     * 
     * 获取实体命令
     * 
     * @param vo vo
     * @param objCascadeVO objCascadeVO
     * @param operateType operateType
     * @return IEntityCommand
     */
    public static IEntityCommand createEntityCmd(CapBaseVO vo, CascadeVO objCascadeVO, String operateType) {
        IExecutor objExecutor = getExecutorByClass(vo, objCascadeVO);
        IEntityCommand objCmd = getEntityCmdByOperateType(objExecutor, operateType);
        return objCmd;
    }
    
    /**
     * 根据class获取执行器
     * 
     * @param vo v0
     * @param objCascadeVO objCascadeVO
     * 
     * @return IExecutor
     */
    private static IExecutor getExecutorByClass(CapBaseVO vo, CascadeVO objCascadeVO) {
        String entityName = objCascadeVO.getEntityName();
        if (entityName == null || "".equals(entityName)) {
            return null;
        }
        return new EntityExecutor(objCascadeVO, vo);
    }
    
    /**
     * 根据操作类型，获取不同的操作命令
     * 
     * @param objExecutor 命令接收者
     * @param operateType 操作类型
     * 
     * @return 操作
     */
    private static IEntityCommand getEntityCmdByOperateType(IExecutor objExecutor, String operateType) {
        // TODO 自动生成方法存根注释，方法实现时请删除此注释
        IEntityCommand objCmd = null;
        if (CapRuntimeConstant.INSERT_OPE_TYPE.equals(operateType)) {
            objCmd = new InsertCmd(objExecutor);
        } else if (CapRuntimeConstant.LOADBYID_OPE_TYPE.equals(operateType)) {
            objCmd = new LoadByIdCmd(objExecutor);
        } else if (CapRuntimeConstant.DELETE_OPE_TYPE.equals(operateType)) {
            objCmd = new DeleteCmd(objExecutor);
        }
        return objCmd;
    }
}
