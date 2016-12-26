/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.runtime.base.model.BpmsNodeInfo;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.top.core.base.dao.CoreDAO;
import com.comtop.top.core.base.model.CoreVO;

/**
 * 针对Bpms的业务扩展DAO，处理一些查询流程数据相关操作
 * 
 * 
 * @author 李小强
 * @since 1.0
 * @version 2015-5-25 李小强
 */
@PetiteBean
public class CapWorkflowQueryDAO extends CoreDAO<CoreVO> {
    
    /**
     * 用户包参与指定流程的待办节点集合
     * 
     * @param processId 流程ID
     * @param userId 用户ID
     * @param tableName 表名
     * @return list<BpmsNodeInfo> 用户所参与的节点集合
     * 
     */
    public List<BpmsNodeInfo> queryIntersecHumanTaskNodes(String processId, String userId, String tableName) {
        Map<String, String> objParams = new HashMap<String, String>();
        objParams.put("userId", userId);
        objParams.put("processId", processId);
        objParams.put("tableName", tableName);
        List<BpmsNodeInfo> rsList = queryList("com.comtop.cap.runtime.base.model.queryIntersecHumanTaskNodes",
            objParams);
        return rsList;
    }

	/**
	 * 查询已办任务ID
	 * 
	 * @param params
	 *            参数
	 * 
	 * @return 询已办任务ID
	 */
	public String queryNewTaskId(Map<String, String> params) {
		return (String) selectOne(
				"com.comtop.cap.runtime.base.model.queryNewTaskId", params);
	}

	/**
	 * 查询已办任务ID
	 * 
	 * @param params
	 *            参数
	 * 
	 * @return 询已办任务ID
	 */
	public int queryNodeInsTodoTaskCount(Map<String, String> params) {
		return (Integer) selectOne(
				"com.comtop.cap.runtime.base.model.queryNodeInsTodoTaskCount", params);
	}
}
