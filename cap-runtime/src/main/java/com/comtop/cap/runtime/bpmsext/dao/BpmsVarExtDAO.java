/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.bpmsext.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.runtime.bpmsext.model.BpmsVarExtVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.top.core.base.dao.CoreDAO;

/**
 * 记录运行时变量DAO
 * 
 * @author CAP超级管理员
 * @since 1.0
 * @version 2016-4-2 CAP超级管理员
 */
@PetiteBean
public class BpmsVarExtDAO extends CoreDAO<BpmsVarExtVO> {
    
    /**
     * 读取 记录运行时变量 列表
     * 
     * @param condition 查询条件
     * @return 记录运行时变量列表
     */
    public List<BpmsVarExtVO> queryBpmsVarExtList(BpmsVarExtVO condition) {
        return queryList("com.comtop.cap.runtime.bpmsext.model.queryBpmsVarExtList", condition);
    }

    /**
     * 
     * @param processInsId 流程实例ID
     * @param nodeId 节点Id
     */
	public void deleteByNodeInsId(String processInsId, String nodeId) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("processInsId", processInsId);
		map.put("nodeId", nodeId);
		delete("com.comtop.cap.runtime.bpmsext.model.deleteBpmsVarByNodeId", map);
	}
}
