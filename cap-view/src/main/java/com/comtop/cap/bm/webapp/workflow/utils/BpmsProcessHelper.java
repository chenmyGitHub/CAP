/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.workflow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.bpms.client4monitor.IDeployMonitorClient;
import com.comtop.bpms.client4monitor.MonitorClientFactory;
import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.bpms.common.model.BpmsPageInfo;
import com.comtop.bpms.common.model.DeployProcessInfo;
import com.comtop.bpms.common.model.DeployQueryConditionInfo;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cap.bm.webapp.workflow.ProcessBeanConverter;
import com.comtop.cap.bm.webapp.workflow.model.CIPProcessBean;
import com.comtop.cap.bm.webapp.workflow.model.CipProcessPageBean;

/**
 * BPMS流程列表查询与操作处理助手
 * 
 * 
 * @author 李小强
 * @since 1.0
 * @version 2014-11-17 李小强
 */
@PetiteBean
public class BpmsProcessHelper {
    
    /***
     * 根据目录编码获取指定目录编号下的所有未部署的流程数据
     * 
     * @param dirCode 目录树编码，不能为空
     * @param userId 当前用户编码，不能为空
     * @param pageNo 当面页面编码
     * @param pageSize 每页显示条数
     * @return 目录编号下的所有未部署的流程数据，当目录编号为空是返回 new ArrayList<CIPProcessBean>
     * @throws AbstractBpmsException 操作失败信息
     */
    public static CipProcessPageBean queryUnDeployeProcessByDirCode(String dirCode, String userId, int pageNo,
        int pageSize) throws AbstractBpmsException {
        CipProcessPageBean bean = new CipProcessPageBean();
        if (dirCode == null || ("").equals(dirCode.trim())) {
            bean.setValueList(new ArrayList<CIPProcessBean>());
            return bean;
        }
        return queryProcessoByDirCodeState(dirCode, userId, 0, pageNo, pageSize);
    }
    
    /***
     * 根据目录编码获取指定目录编号下的所有已部署的流程数据
     * 
     * @param dirCode 目录树编码，不能为空
     * @param userId 当前用户编码，不能为空
     * @param pageNo 当面页面编码
     * @param pageSize 每页显示条数
     * @return 目录编号下的所有未部署的流程数据，当目录编号为空是返回 new ArrayList<CIPProcessBean>
     * @throws AbstractBpmsException 操作失败信息
     */
    public static CipProcessPageBean queryDeployedProcessByDirCode(String dirCode, String userId, int pageNo,
        int pageSize) throws AbstractBpmsException {
        CipProcessPageBean bean = new CipProcessPageBean();
        if (dirCode == null || ("").equals(dirCode.trim())) {
            bean.setValueList(new ArrayList<CIPProcessBean>());
            return bean;
        }
        return queryProcessoByDirCodeState(dirCode, userId, 1, pageNo, pageSize);
    }
    
    /**
     * 删除流程(限状态为未部署的流程,即流程草稿)
     * 
     * @param deployeIds 部署编号（必须）
     * @param userId 操作人编号（必须）
     * @return 操作结论
     */
    public static String deleteUndeployeById(String[] deployeIds, String userId) {
        StringBuffer errorIds = new StringBuffer();
        String errorMessage = "";
        for (String deployId : deployeIds) {
            try {
                getDeployMonitorClient().deleteDraft(deployId, userId);
            } catch (AbstractBpmsException e) {
                errorIds.append(deployId + ",");
                errorMessage = "错误原因：" + e.getMessage();
            }
        }
        if (errorMessage.length() > 0) {
            String errIds = errorIds.deleteCharAt(errorIds.length() - 1).toString();
            return "删除部署ID为：" + errIds + "的草稿流程失败。" + errorMessage;
        }
        return null;
    }
    
    /**
     * 卸载流程
     * 
     * @param deployeIds 部署编号（必须）
     * @param userId 操作人编号（必须）
     * @return 卸载流程操作结论
     */
    public static String uninstallDeployeById(String[] deployeIds, String userId) {
        StringBuffer errorIds = new StringBuffer();
        String errorMessage = "";
        for (String deployId : deployeIds) {
            try {
                getDeployMonitorClient().uninstallDeployeById(deployId, userId);
            } catch (AbstractBpmsException e) {
                errorIds.append(deployId + ",");
                errorMessage = e.getMessage();
            }
        }
        if (errorMessage.length() > 0) {
            // String errIds = errorIds.deleteCharAt(errorIds.length() - 1).toString();
            return errorMessage;
        }
        return null;
    }
    
    /**
     * 根据应用编码、部署状态查询流程
     * 
     * @param dirCode 目录树编码，不能为空
     * @param userId 当前用户编码，不能为空
     * @param state 部署状态，0表示未部署、1表示已部署、2表示卸载 (CIP集成查询已部署传入1即可,查询草稿传入0即可)
     * @param pageNo 当面页面编码
     * @param pageSize 每页显示条数
     * @return 符合条件的数据
     * @throws AbstractBpmsException 操作失败信息
     */
    @SuppressWarnings("unchecked")
    private static CipProcessPageBean queryProcessoByDirCodeState(String dirCode, String userId, int state, int pageNo,
        int pageSize) throws AbstractBpmsException {
        int childLevel = 0;// 目录级别 -1表示全部子目录，0表示本身，1表示1级子目录 2表示2级子目录 (CIP集成传入0即可)
        CipProcessPageBean rsPageBean = new CipProcessPageBean();
        DeployQueryConditionInfo conditionInfo = new DeployQueryConditionInfo();
        conditionInfo.setDireTreeCode(dirCode);
        BpmsPageInfo bpmsData = getDeployMonitorClient().readProcessInfoByDirectoryCode(conditionInfo, userId,
            childLevel, state, pageNo, pageSize);
        rsPageBean.setAllRows(bpmsData.getAllRows());
        Map<String, CIPProcessBean> processmap = new HashMap<String, CIPProcessBean>();
        if (bpmsData.getList() != null && bpmsData.getList().size() > 0) {
            List<DeployProcessInfo> processList = bpmsData.getList();
            for (DeployProcessInfo process : processList) {
                if (processmap.containsKey(process.getProcessId())) {
                    CIPProcessBean bean = processmap.get(process.getProcessId());
                    // 如果map中的版本比新的版本高，则不添加。继续下一数据处理.李小强--2015-10-21 18:56
                    if (bean.getVersion().intValue() > process.getVersion().intValue()) {
                        continue;
                    }
                }
                CIPProcessBean bean = new CIPProcessBean();
                ProcessBeanConverter.deployProcessInfo2CIPProcessBean(process, bean);
                processmap.put(bean.getProcessId(), bean);
            }
            // 将map中的集合返回
//            Iterator<Map.Entry<String, CIPProcessBean>> iter = processmap.entrySet().iterator();
            List<CIPProcessBean> lstRsList = new ArrayList<CIPProcessBean>(processmap.values());
//            lstRsList.addAll(processmap.values());
//            while (iter.hasNext()) {
//                Map.Entry<String, CIPProcessBean> entry = iter.next();
//                lstRsList.add(entry.getValue());
//            }
            rsPageBean.setValueList(lstRsList);
            return rsPageBean;
        }
        rsPageBean.setValueList(new ArrayList<CIPProcessBean>());
        return rsPageBean;
    }
    
    /***
     * 获取BPMS的流程部署服务接口
     * 
     * @return BPMS的流程部署服务接口
     */
    private static IDeployMonitorClient getDeployMonitorClient() {
        return MonitorClientFactory.getDeployMonitorClient();
    }
}
