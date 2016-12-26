
package com.comtop.cap.bm.webapp.graph;

import java.util.Observable;

import com.comtop.eic.core.asyn.model.TaskProgressVo;

/**
 * graphEA导出观察者
 *
 * @author 刘城
 * @since jdk1.6
 * @version 2016年12月22日 刘城
 */
public class GraphHandleProgress extends Observable {
    
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
