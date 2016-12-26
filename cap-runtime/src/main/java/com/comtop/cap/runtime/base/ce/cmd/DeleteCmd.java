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
 * 插入命令
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月29日 龚斌
 */
public class DeleteCmd implements IEntityCommand {
    
    /**
     * 执行器
     */
    private IExecutor executor;
    
    /**
     * 
     * @see com.comtop.cap.runtime.base.ce.cmd.IEntityCommand#execute()
     */
    @Override
    public void execute() {
        // TODO 自动生成方法存根注释，方法实现时请删除此注释
        executor.delete();
    }
    
    /**
     * 构造函数
     * 
     * @param executor 执行对象
     */
    public DeleteCmd(IExecutor executor) {
        super();
        this.executor = executor;
    }
    
}
