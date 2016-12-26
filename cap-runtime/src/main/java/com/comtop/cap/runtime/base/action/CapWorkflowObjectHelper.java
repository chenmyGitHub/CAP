/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.util.HashMap;
import java.util.List;

import com.comtop.bpms.common.model.NodeInfo;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
import com.comtop.cip.json.JSON;

/**
 * FIXME 类注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
 * 
 * 
 * @author 作者
 * @since 1.0
 * @version 2015-7-2 作者
 */
public class CapWorkflowObjectHelper {
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param workflowParams 转换前的流程参数集合
     * @return 转换后的参数集合
     */
    public static CapWorkflowParam[] tansJsonStringOfArray(CapWorkflowParam[] workflowParams) {
        NodeInfo[] nodeInfos = tansJsonToBpmsNode(workflowParams[0].getTagNodeInfoString());
        HashMap<String, Object> objParamMap = null;
        for (CapWorkflowParam workflowParam : workflowParams) {
            workflowParam.setTargetNodeInfos(nodeInfos);
            objParamMap = tansJsonToBpmsParamMap(workflowParam.getParamMapString());
            workflowParam.setParamMap(objParamMap);
        }
        return workflowParams;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param workflowParam 流程参数
     * @return 转换后的流程参数
     */
    public static CapWorkflowParam tansJsonString(CapWorkflowParam workflowParam) {
        NodeInfo[] nodeInfos = tansJsonToBpmsNode(workflowParam.getTagNodeInfoString());
        workflowParam.setTargetNodeInfos(nodeInfos);
        HashMap<String, Object> objParamMap = tansJsonToBpmsParamMap(workflowParam.getParamMapString());
        workflowParam.setParamMap(objParamMap);
        return workflowParam;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param json 需要转换的流程节点JSON字符串
     * @return 转换揀的流程节点集合
     */
    public static NodeInfo[] tansJsonToBpmsNode(String json) {
        List<NodeInfo> lstNode = JSON.parseArray(json, NodeInfo.class);
        if (lstNode == null) {
            return null;
        }
        NodeInfo[] nodeInfos = new NodeInfo[lstNode.size()];
        return lstNode.toArray(nodeInfos);
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     * 
     * 
     * @param json 转换前的JSon字符串
     * @return 转换后的MAP对象
     */
    public static HashMap<String, Object> tansJsonToBpmsParamMap(String json) {
        if (json == null || "".equals(json.trim())) {
            return new HashMap<String, Object>();
        }
        HashMap<String, Object> objMap = JSON.parseObject(json, HashMap.class);
        if (objMap == null) {
            objMap = new HashMap<String, Object>();
        }
        return objMap;
    }
    
}
