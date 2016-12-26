/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.doc.info.action;

import java.util.Observable;

import com.comtop.eic.core.asyn.model.TaskProgressVo;

/**
 * 文档观察者
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2015年12月10日 lizhongwen
 */
public class DocumentHandleProgress extends Observable {
    
    /**
     * 更新任务运行信息
     * 
     * @param progress 任务处理进度
     */
    public void updateTaskStatus(TaskProgressVo progress) {
        setChanged();
        notifyObservers(progress);
    }
}
