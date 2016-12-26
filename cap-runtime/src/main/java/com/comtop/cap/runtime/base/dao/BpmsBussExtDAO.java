/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.dao;

import java.util.ArrayList;
import java.util.Date;
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
public class BpmsBussExtDAO extends CoreDAO<CoreVO> {
    
    /**
     * 用户包参与指定流程的待办节点集合
     * 
     * @param processId
     *            流程ID
     * @param userId
     *            用户ID
     * @param tableName
     *            表名
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
    
    /****
     * 按条件统计已部署的流程数量
     * 
     * @param dirCode
     *            对应的目录编号（如果统计某个模块下的流程，这里传入模块的应用编码）,如果不传则表示查询所有数据
     * @param deployTimeStart
     *            流程部署时间查询的开始值
     * @param deployTimeEnd
     *            流程部署时间查询的结束值
     * @param countDifVer
     *            不同版本流程是否计算版本，true：需要计算，false：不计算，默认不计算
     * @param deployerId 流程部署人ID，如果为空则表示不需要将此做为查询条件
     * @return 统计的数值
     */
    public int queryDeployeProcessCount(String dirCode, Date deployTimeStart, Date deployTimeEnd, boolean countDifVer,
        String deployerId) {
        Map<String, Object> objParams = new HashMap<String, Object>();
        objParams.put("dirCode", dirCode);
        objParams.put("deployTimeStart", deployTimeStart);
        objParams.put("deployTimeEnd", deployTimeEnd);
        objParams.put("deployerId", deployerId);
        List<Integer> rsList = new ArrayList<Integer>();
        if (countDifVer) {
            rsList = queryList("com.comtop.cap.runtime.base.model.queryDeployeProcessCountAll", objParams);
        } else {
            rsList = queryList("com.comtop.cap.runtime.base.model.queryDeployeProcessCountDist", objParams);
        }
        return rsList.get(0);
    }
    
    /****
     * 按条件统计已部署的流程数量
     * 
     * @param deployTimeStart
     *            流程部署时间查询的开始值
     * @param deployTimeEnd
     *            流程部署时间查询的结束值
     * @param countDifVer
     *            不同版本流程是否计算版本，true：需要计算，false：不计算，默认不计算
     * @return 统计的数值, Map<String--模块所对应的应用编号,统计结果值>
     */
    public Map<String, Integer> queryDeployeProcessMap(Date deployTimeStart, Date deployTimeEnd, boolean countDifVer) {
        Map<String, Object> objParams = new HashMap<String, Object>();
        objParams.put("deployTimeStart", deployTimeStart);
        objParams.put("deployTimeEnd", deployTimeEnd);
        List<Map> rslst = new ArrayList<Map>();
        if (countDifVer) {
            rslst = queryList("com.comtop.cap.runtime.base.model.queryDeployeProcessMapAll", objParams);
        } else {
            rslst = queryList("com.comtop.cap.runtime.base.model.queryDeployeProcessDist", objParams);
        }
        if (rslst == null || rslst.size() == 0) {
            return new HashMap<String, Integer>();
        }
        Map<String, Integer> rsMap = new HashMap<String, Integer>();
        for (Map m : rslst) {
            rsMap.put(m.get("DEPLOY_DIR_CODE").toString(), Integer.parseInt(m.get("COUN_NUM").toString()));
        }
        return rsMap;
    }
}
