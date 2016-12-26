/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.serve.action;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.ObjectOperator;
import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.bm.metadata.serve.facade.ServiceObjectFacade;
import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cap.bm.webapp.serve.util.ServiceObjectConstants;
import com.comtop.cap.bm.webapp.util.GenerateCodeUtils;
import com.comtop.cap.codegen.generate.ExceptionGenerator;
import com.comtop.cap.codegen.generate.GenerateCode;
import com.comtop.cap.codegen.generate.ServiceFacadeGenerator;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cap.bm.metadata.pkg.facade.IPackageFacade;
import com.comtop.cap.bm.metadata.pkg.facade.PackageFacadeImpl;
import com.comtop.top.core.jodd.AppContext;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 服务对象Action类
 * 
 * @author 林玉千
 * @since jdk1.6
 * @version 2016-5-31 林玉千 新建
 */
@DwrProxy
@MadvocAction
public class ServiceAction {
    
    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceAction.class);
    
    /** 服务对象门面类 */
    private final static ServiceObjectFacade serviceObjectFacade = AppContext.getBean(ServiceObjectFacade.class);
    
    /** 包Facade */
    private final IPackageFacade packageFacade = AppContext.getBean(PackageFacadeImpl.class);
    
    /** error常量 */
    private final static String SUPER_CLASS_CHANGE_ERROR = "com.zeroturnaround.javarebel.SuperClassChangedError";
    
    /**
     * 生成所有服务代码
     * 
     * @param packageId 包id
     * @return 操作结果
     */
    @RemoteMethod
    public String executeGenerateCode(String packageId) {
        try {
            List<ServiceObjectVO> lstServiceObjectVO = serviceObjectFacade.queryServiceObjectList(packageId);
            return executeGenerateCode(lstServiceObjectVO, packageId);
        } catch (OperateException e) {
            LOGGER.error("查询包路径下的服务出现异常", e);
            return "查询包路径下的服务出现异常！";
        }
        
    }
    
    /**
     * 根据模块Id生成服务代码
     * 
     * @param packageId 包Id
     * @param modelId 模块Id
     * @return 操作结果
     */
    @RemoteMethod
    public String executeGenerateCodeByModelId(String modelId, String packageId) {
        ServiceObjectVO serviceObjectVO = serviceObjectFacade.loadServiceObject(modelId, packageId);
        List<ServiceObjectVO> lstServiceObjectVO = new ArrayList<ServiceObjectVO>();
        if (serviceObjectVO != null) {
            lstServiceObjectVO.add(serviceObjectVO);
        }
        return executeGenerateCode(lstServiceObjectVO, packageId);
    }
    
    /**
     * 生成服务代码
     * 
     * @param vos 需要生成代码的服务列表
     * @param packageId 包id
     * @return 操作结果
     */
    @RemoteMethod
    public String executeGenerateCode(List<ServiceObjectVO> vos, String packageId) {
        if (vos == null || vos.size() == ServiceObjectConstants.NUM_ZERO) {
            return "请选择要生成代码的服务";
        }
        
        // 包路径
        String strPackagePath = packageFacade.queryPackageById(packageId).getFullPath();
        
        ObjectOperator objExc = new ObjectOperator(vos);
        try {
            List<ExceptionVO> lst = objExc.queryList("./methods/exceptions", ExceptionVO.class);
            List<ExceptionVO> lstToGene = new ArrayList<ExceptionVO>();
            List<String> lstExpId = new ArrayList<String>();
            for (ExceptionVO exceptionVO : lst) {
                if (lstExpId.contains(exceptionVO.getModelId())) {
                    continue;
                }
                lstExpId.add(exceptionVO.getModelId());
                lstToGene.add(exceptionVO);
            }
            GenerateCode.generateCode(strPackagePath, new ExceptionGenerator(), lstToGene);
        } catch (OperateException e) {
            e.printStackTrace();
        }
        
        ServiceFacadeGenerator obj = new ServiceFacadeGenerator();
        GenerateCode.generateCode(strPackagePath, obj, vos);
        
        StringBuilder strMsg = new StringBuilder();
        // 注册soa服务并刷新
        try {
        	String codePath = obj.getProjectDir(vos.get(0));
			for (ServiceObjectVO vo : vos) {
				GenerateCodeUtils.registerAndRefreshSoaInfo(vo, codePath);
			}
        } catch (Throwable ta) {
            if (SUPER_CLASS_CHANGE_ERROR.equals(ta.getClass().getName())) {
                strMsg.append("生成代码时注册soa服务失败，修改服务超类后请重启服务！");
            } else {
                strMsg.append("生成代码时注册soa服务发生异常！");
            }
            LOGGER.error(strMsg.toString(), ta);
        }
        
        return strMsg.toString();
    }
}
