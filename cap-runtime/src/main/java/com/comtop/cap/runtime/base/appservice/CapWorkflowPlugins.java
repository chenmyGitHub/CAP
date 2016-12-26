/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.appservice;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 工作流插件
 *
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2015年9月1日 lizhongwen
 */
public class CapWorkflowPlugins {
    
    /** 插件集合 */
    private static Set<CapWorkflowCallbackExtend> queue;
    
    static {
        Comparator<CapWorkflowCallbackExtend> comparator = new Comparator<CapWorkflowCallbackExtend>() {
            
            /**
             * 比较
             * 
             * @param o1 扩展1
             * @param o2 扩展2
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(CapWorkflowCallbackExtend o1, CapWorkflowCallbackExtend o2) {
                CapWorkflowPlugin a1 = o1.getClass().getAnnotation(CapWorkflowPlugin.class);
                CapWorkflowPlugin a2 = o1.getClass().getAnnotation(CapWorkflowPlugin.class);
                if (a2.order() == a1.order()) {
                    String name1 = StringUtil.isBlank(a1.name()) ? o1.getClass().getName() : a2.name();
                    String name2 = StringUtil.isBlank(a2.name()) ? o2.getClass().getName() : a2.name();
                    return name2.compareTo(name1);
                }
                return a2.order() - a1.order();
            }
        };
        queue = new TreeSet<CapWorkflowCallbackExtend>(comparator);
    }
    
    /**
     * 注册
     *
     * @param extend 扩展
     */
    public static void register(CapWorkflowCallbackExtend extend) {
        queue.add(extend);
    }
    
    /**
     * 执行
     * 
     * @param vo VO
     * @param param 工作流参数
     * @param taskId 任务ID
     * @param operateType 操作类型
     */
    public static void execute(Object vo, CapWorkflowParam param, String taskId, int operateType) {
        for (CapWorkflowCallbackExtend extend : queue) {
            extend.callback(vo, param, taskId, operateType);
        }
    }
    
    /**
     * 批量执行
     * 
     * @param vos VO
     * @param params 工作流参数
     * @param operateType 操作类型
     */
    public static void execute(List<?> vos, List<CapWorkflowParam> params, int operateType) {
        for (CapWorkflowCallbackExtend extend : queue) {
            extend.batchCallback(vos, params, operateType);
        }
    }
}
