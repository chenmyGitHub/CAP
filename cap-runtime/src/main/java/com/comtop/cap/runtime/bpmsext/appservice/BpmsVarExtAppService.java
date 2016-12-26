/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.bpmsext.appservice;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.comtop.cap.runtime.base.appservice.CapBaseAppService;
import com.comtop.cap.runtime.bpmsext.dao.BpmsVarExtDAO;
import com.comtop.cap.runtime.bpmsext.model.BpmsVarExtVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;

/**
 * 业务域 业务逻辑处理类
 * 
 * @author CAP超级管理员
 * @since 1.0
 * @version 2016-4-2 CAP超级管理员
 */
@PetiteBean
public class BpmsVarExtAppService extends CapBaseAppService {
    
    /** 注入DAO **/
    @PetiteInject
    protected BpmsVarExtDAO bpmsVarExtDAO;
    
    /**
     * 读取 记录运行时变量 列表
     * 
     * @param condition 查询条件
     * @return 记录运行时变量
     */
    public List<BpmsVarExtVO> queryBpmsVarExtList(BpmsVarExtVO condition) {
        return bpmsVarExtDAO.queryBpmsVarExtList(condition);
    }

    /**
     * 保存记录运行时变量列表
     * 
     * @param condition 记录运行时变量列表
     */
	public void saveBpmsVarExtList(List<BpmsVarExtVO> condition) {
		List<BpmsVarExtVO> insertList = new ArrayList<BpmsVarExtVO>();
		List<BpmsVarExtVO> updateList = new ArrayList<BpmsVarExtVO>();
		for(BpmsVarExtVO bpmsVarExtVO : condition){
			if(StringUtils.isNotBlank(bpmsVarExtVO.getVariableId())){
				updateList.add(bpmsVarExtVO);
			}else{
				insertList.add(bpmsVarExtVO);
			}
		}
		if(insertList!=null && insertList.size()>0){
			bpmsVarExtDAO.insert(insertList);
		}
		if(updateList!=null && updateList.size()>0){
			bpmsVarExtDAO.update(updateList);
		}
	}
	
	/**
	 * 删除流程变量
	 * @param processInstanceId 流程实例ID
	 * @param nodeId 节点Id
	 */
	public void deleteBpmsVar(String processInstanceId, String nodeId){
		bpmsVarExtDAO.deleteByNodeInsId(processInstanceId, nodeId);
	}
	
}
