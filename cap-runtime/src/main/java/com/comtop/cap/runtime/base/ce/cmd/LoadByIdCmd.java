/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.ce.cmd;

import com.comtop.cap.runtime.base.ce.IExecutor;
import com.comtop.cap.runtime.base.ce.cmd.IEntityCommand;

/**
 * 根据id查询实体命令
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月15日 龚斌
 */
public class LoadByIdCmd implements IEntityCommand {
    
    /**
     * 迭代命令接收者
     */
    private IExecutor executor;
    
    /**
     * 
     * @see com.comtop.cap.runtime.base.ce.cmd.IEntityCommand#execute()
     */
    @Override
    public void execute() {
        executor.loadById();
        
    }
    
    /**
     * 构造函数
     * 
     * @param executor 迭代命令接收者
     */
    public LoadByIdCmd(IExecutor executor) {
        super();
        this.executor = executor;
    }
    
}
