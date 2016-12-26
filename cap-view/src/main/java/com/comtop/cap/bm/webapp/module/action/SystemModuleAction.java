/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.module.action;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.common.storage.exception.ValidateException;
import com.comtop.cap.bm.metadata.sysmodel.facade.SysmodelFacade;
import com.comtop.cap.bm.metadata.sysmodel.model.CapPackageVO;
import com.comtop.cap.bm.metadata.sysmodel.model.FunctionItemVO;
import com.comtop.cap.bm.req.func.model.ReqTreeVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.bm.metadata.pkg.facade.PackageFacadeImpl;
import com.comtop.cap.bm.metadata.pkg.model.PackageVO;
import com.comtop.cap.runtime.core.AppBeanUtil;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.sys.module.model.ModuleDTO;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 系统模块Action
 *
 * @author 龚斌
 * @since 1.0
 * @version 2016年1月11日 龚斌
 */
@DwrProxy
public class SystemModuleAction {
    
    /**
     * 应用模块服务
     */
    protected SysmodelFacade sysmodelFacade = AppContext.getBean(SysmodelFacade.class);
    
	/** PackageFacadeImpl */
	protected PackageFacadeImpl packageFacadeImpl = AppBeanUtil.getBean(PackageFacadeImpl.class);
    
	/**
	 * 根据模块id获取所对应的功能项及功能子项集合
	 * 
	 * @param moduleId
	 *            模块id
	 * @return 功能项及功能子项集合
	 */
	@RemoteMethod
	public List<ReqTreeVO> queryReqTreeVOByModuleId(String moduleId) {
		return sysmodelFacade.queryReqTreeVOByModuleId(moduleId);
	}
    
    /**
     * 根据模块id获取所对应的功能项及功能子项集合
     * 
     * @param moduleDTO 模块id
     * @param lstReqTreeVO 功能项VO集合
     * @return 保存成功与否
     * @throws ValidateException 异常
     */
    @RemoteMethod
    public boolean saveModuleFuncItem(ModuleDTO moduleDTO, List<ReqTreeVO> lstReqTreeVO) throws ValidateException {
        if (StringUtil.isEmpty(moduleDTO.getModuleCode())) {
            throw new ValidateException("模块编码不能为空！");
        }
        CapPackageVO objCapPackageVO = new CapPackageVO();
        objCapPackageVO.setModuleId(moduleDTO.getModuleId());
        objCapPackageVO.setModuleCode(moduleDTO.getModuleCode());
        objCapPackageVO.setModuleName(moduleDTO.getModuleName());
        objCapPackageVO.setModuleType(moduleDTO.getModuleType());
        objCapPackageVO.setModuleDescription(moduleDTO.getDescription());
        List<FunctionItemVO> lstFunctionItem = new ArrayList<FunctionItemVO>();
        if (lstReqTreeVO != null) {
            FunctionItemVO objFunctionItemVO = null;
            for (ReqTreeVO objReqTreeVO : lstReqTreeVO) {
                objFunctionItemVO = new FunctionItemVO(objReqTreeVO.getId(), objReqTreeVO.getType());
                lstFunctionItem.add(objFunctionItemVO);
            }
        }
        objCapPackageVO.setLstFunctionItem(lstFunctionItem);
        objCapPackageVO = sysmodelFacade.savePackageVO(objCapPackageVO);
        return (objCapPackageVO != null);
    }
    
    /**
     * 根据模块编码保存新增的俩个属性
     * 
     * @param capPackageVO 模块id
     * @return 保存成功与否
     * @throws ValidateException 异常
     */
    @RemoteMethod
    public boolean saveCapPackage4CodePath(CapPackageVO capPackageVO) throws ValidateException {
        if (StringUtil.isEmpty(capPackageVO.getModuleCode())) {
            throw new ValidateException("模块编码不能为空！");
        }
        // 根据moduldId获取当前存储的CapPackageVO
        CapPackageVO objCapPackageVO = sysmodelFacade.saveCapPackage4CodePath(capPackageVO);
        return (objCapPackageVO != null);
    }
    
    /**
     * 删除模块所对应的功能项
     * 
     * @param moduleDTO 模块VO
     * @return 保存成功与否
     * @throws OperateException 异常
     */
    @RemoteMethod
    public boolean deleteModuleFuncItem(ModuleDTO moduleDTO) throws OperateException {
        if (StringUtil.isEmpty(moduleDTO.getModuleId()) || StringUtil.isEmpty(moduleDTO.getModuleCode())) {
            throw new OperateException("模块ID及编码不能为空！");
        }
        //删除CIP_PACKAGE
        PackageVO packageVO= new PackageVO();
        packageVO.setId(moduleDTO.getModuleId());
        packageFacadeImpl.deletePackageByPackageId(packageVO);
        
        //删除CAP模块包对象
        CapPackageVO objCapPackageVO = new CapPackageVO();
        objCapPackageVO.setModuleId(moduleDTO.getModuleId());
        objCapPackageVO.setModuleCode(moduleDTO.getModuleCode());
        return sysmodelFacade.deletePackageVO(objCapPackageVO);
    }
    
    /**
     * 判断包全路径是否已存在
     *
     * @param packageVO 包全路径
     * @return 是否存在
     */
    @RemoteMethod
    public boolean isExistPackageFullPath(PackageVO packageVO) {
        return sysmodelFacade.isExistPackageFullPath(packageVO);
    }
}
