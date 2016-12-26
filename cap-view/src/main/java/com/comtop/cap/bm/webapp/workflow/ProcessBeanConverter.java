/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.workflow;

import com.comtop.bpms.common.model.DeployProcessInfo;
import com.comtop.cap.bm.webapp.workflow.model.CIPProcessBean;

/**
 * 数据转换类
 * 
 * 
 * @author 李小强
 * @since 1.0
 * @version 2014-11-18 李小强
 */
public class ProcessBeanConverter {
    
    /**
     * 构造函数
     */
    private ProcessBeanConverter() {
        
    }
    
    /**
     * 将BPMS的 DeployProcessInfo数据转换为CIP的 CIPProcessBean 数据对象
     * 
     * @param from BPMS的 DeployProcessInfo数据对象
     * @param to CIP的 CIPProcessBean 数据对象
     */
    public static void deployProcessInfo2CIPProcessBean(DeployProcessInfo from, CIPProcessBean to) {
        to.setDeployeId(from.getDeployeId());
        to.setDeployPersonName(from.getDeployPersonName());
        to.setDeployTime(from.getDeployTime());
        to.setDirCode(from.getDirectoryCode());
        to.setName(from.getName());
        to.setProcessId(from.getProcessId());
        to.setState(from.getState());
        to.setVersion(from.getVersion());
    }
}
