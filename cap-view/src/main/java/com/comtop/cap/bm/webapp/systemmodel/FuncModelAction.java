/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.systemmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.comtop.bpms.common.AbstractBpmsException;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.metadata.sqlfile.GenerateSqlFileFactory;
import com.comtop.cap.bm.metadata.sqlfile.IGenerateSqlFile;
import com.comtop.cap.bm.webapp.util.CapViewUtils;
import com.comtop.cap.bm.webapp.workflow.model.CipProcessPageBean;
import com.comtop.cap.bm.webapp.workflow.utils.BpmsProcessHelper;
import com.comtop.cap.runtime.base.dao.CapBaseCommonDAO;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.top.cfg.facade.IConfigFacade;
import com.comtop.top.component.app.session.HttpSessionUtil;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.DateTimeUtil;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.core.util.constant.NumberConstant;
import com.comtop.top.sys.accesscontrol.func.FuncConstants;
import com.comtop.top.sys.accesscontrol.func.facade.FuncAssembler;
import com.comtop.top.sys.accesscontrol.func.facade.IFuncFacade;
import com.comtop.top.sys.accesscontrol.func.model.FuncDTO;
import com.comtop.top.sys.accesscontrol.func.model.FuncVO;
import com.comtop.top.sys.accesscontrol.func.model.TagValueDTO;
import com.comtop.top.sys.accesscontrol.util.AccessConstants;
import com.comtop.top.sys.module.facade.IModuleFacade;
import com.comtop.top.sys.module.facade.ModuleAssembler;
import com.comtop.top.sys.module.model.ModuleDTO;
import com.comtop.top.sys.module.model.ModuleVO;
import com.comtop.top.sys.usermanagement.user.model.UserDTO;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 应用Action
 * 
 * @author 沈康
 * @since 1.0
 * @version 2014-11-15 沈康
 */

@DwrProxy
@MadvocAction
public class FuncModelAction {
    
    /**
     * 注入功能facade
     */
    @PetiteInject
    protected IFuncFacade funcFacade;
    
    /**
     * 注入配置项facade
     */
    @PetiteInject
    protected IConfigFacade configFacade;
    
    /** 系统模块 Facade */
    @PetiteInject
    protected IModuleFacade moduleFacade;
    
    /** coreDAO */
    @SuppressWarnings("rawtypes")
	private final CapBaseCommonDAO coreDAO = AppContext.getBean(CapBaseCommonDAO.class);
    
    /**
     * 查询指定应用下的所有功能实体，包括菜单目录、菜单、页面、操作数据
     * 
     * @param funcDTO 父级应用查询条件
     * @return 指定应用下的所有功能实体List
     */
    @RemoteMethod
    public List<FuncDTO> queryAllFunc(FuncDTO funcDTO) {
        return funcFacade.queryFuncChildByAppId(funcDTO);
    }
    
    /**
     * 查询应用下的所有应用分类集合信息
     * 
     * @return 应用的分类数据集合
     */
    @RemoteMethod
    public List<TagValueDTO> getFuncTagList() {
        List<Map<String, String>> lstTagConfig = configFacade.getAllValue(FuncConstants.FUNC_TAG_CONFIG_KEY, null);
        List<TagValueDTO> lstTagValues = new ArrayList<TagValueDTO>(NumberConstant.FIVE);
        if (lstTagConfig != null && !lstTagConfig.isEmpty()) {
            for (Map<String, String> objValueMap : lstTagConfig) {
                TagValueDTO objTagValue = new TagValueDTO();
                objTagValue.setText(objValueMap.get("text"));
                objTagValue.setValue(objValueMap.get("value"));
                lstTagValues.add(objTagValue);
            }
        }
        return lstTagValues;
    }
    
    /**
     * 保存应用数据
     * 
     * @param funcDTO
     *            实体VO
     * @return 保存成功后返回应用及菜单目录ID，
     */
    @RemoteMethod
    public String saveFunc(FuncDTO funcDTO) {
        funcDTO.setCreateTime(DateTimeUtil.formatDateTime(new Date()));
        UserDTO objUserDTO = (UserDTO) HttpSessionUtil.getCurUserInfo();
        if (objUserDTO != null) {
            funcDTO.setCreatorId(objUserDTO.getUserId());
        }
        return funcFacade.saveFuncVO(funcDTO);
    }
    
    /**
     * 修改应用数据
     * 
     * @param funcDTO
     *            实体VO
     */
    @RemoteMethod
    public void updateFunc(FuncDTO funcDTO) {
        if (StringUtil.isEmpty(funcDTO.getUpdateTime())) {
            funcDTO.setUpdateTime(DateTimeUtil.formatDateTime(new Date()));
        }
        UserDTO objUserDTO = (UserDTO) HttpSessionUtil.getCurUserInfo();
        if (objUserDTO != null) {
            funcDTO.setModifierId(objUserDTO.getUserId());
        }
        funcFacade.updateFuncVO(funcDTO);
    }
    
    /**
     * 根据模块ID获取关联的应用实体信息
     * 
     * @param moduleId 模块ID
     * @return 模块关联的应用信息
     */
    @RemoteMethod
    public FuncDTO readFuncByModuleId(String moduleId) {
        FuncDTO objFuncDTO = new FuncDTO();
        objFuncDTO.setParentFuncId(moduleId);
        objFuncDTO.setParentFuncType("MODULE");
        objFuncDTO.setFuncNodeType(3);
        
        List<FuncDTO> lstFunc = funcFacade.queryFuncChild(objFuncDTO);
        objFuncDTO = (((lstFunc != null) && (!(lstFunc.isEmpty()))) ? (FuncDTO) lstFunc.get(0) : null);
        
        ModuleDTO objModuleDTO = moduleFacade.readModuleVO(moduleId);
        if (objModuleDTO != null && objFuncDTO != null) {
            objFuncDTO.setFullPath(objModuleDTO.getFullPath());
            objFuncDTO.setShortName(objModuleDTO.getShortName());
        }
        return objFuncDTO;
    }
    
    /**
     * 在模块下保存应用，应用挂接模块，模块编码名称统一使用应用编码名称
     * 
     * @param funcDTO 应用实体
     * @return 保存成功的模块ID
     */
    @RemoteMethod
    public String saveFuncVOInModule(FuncDTO funcDTO) {
        ModuleDTO objModuleVO = new ModuleDTO();
        objModuleVO.setModuleCode(funcDTO.getFuncCode());
        objModuleVO.setModuleType(2);
        objModuleVO.setModuleName(funcDTO.getFuncName());
        objModuleVO.setParentModuleId(funcDTO.getParentFuncId());
        objModuleVO.setState(1);
        objModuleVO.setFullPath(funcDTO.getFullPath());
        objModuleVO.setShortName(funcDTO.getShortName());
        objModuleVO.setNodeType("package");
        
        String strModuleId = moduleFacade.saveModule(objModuleVO);
        objModuleVO = moduleFacade.readModuleVO(strModuleId);
        
        funcDTO.setParentFuncId(strModuleId);
        funcDTO.setParentFuncType("MODULE");
        funcDTO.setSortNo(objModuleVO.getSortId());
        funcDTO.setCreateTime(DateTimeUtil.formatDateTime(new Date()));
        UserDTO objUserDTO = (UserDTO) HttpSessionUtil.getCurUserInfo();
        if (objUserDTO != null) {
            funcDTO.setCreatorId(objUserDTO.getUserId());
        }
        String funcId = funcFacade.saveFuncVO(funcDTO);
        funcDTO.setFuncId(funcId);
        //生成应用菜单SQL脚本
        this.createFuncSQL(objModuleVO, funcDTO);
        
        return strModuleId;
    }
    
	/**
	 * 生成应用菜单sql脚本
	 * 
	 * @param moduleDTO
	 *            xx
	 * @param funcDTO
	 *            应用实体
	 */
	@SuppressWarnings("rawtypes")
	private void createFuncSQL(ModuleDTO moduleDTO, FuncDTO funcDTO) {
		IGenerateSqlFile iGenerateSqlFile = 
				GenerateSqlFileFactory.getTopFuncSqlFileBestWay();
		ModuleAssembler moduleAssembler = 
				AppContext.getBean(ModuleAssembler.class);
		ModuleVO moduleVO = moduleAssembler.createVOByDTO(moduleDTO);
		this.saveModule(moduleVO, iGenerateSqlFile);
		this.saveFuncVO(funcDTO, iGenerateSqlFile);
		
		CapViewUtils.writeResourceSqlFile(PreferenceConfigQueryUtil.getCodePath(), null,
				funcDTO.getFullPath(), "[2]createFunc_"+moduleDTO.getModuleCode()+".sql", wrapperSQL(iGenerateSqlFile));
	}
	
	/**
	 * wrapper SQL content
	 * 
	 * @param iGenerateSqlFile
	 *            generate SQL file Interface
	 * @return string
	 */
	@SuppressWarnings("rawtypes")
	private String wrapperSQL(IGenerateSqlFile iGenerateSqlFile) {
		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		sb.append(iGenerateSqlFile.getSql());
		sb.append("end; \n");
		sb.append("/  \n");
		sb.append("commit; ");
		return sb.toString();
	}
	
	/**
	 * @param moduleVO
	 *            ModuleVO
	 * @param iGenerateSqlFile
	 *            生成SQL文件接口
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void saveModule(ModuleVO moduleVO, IGenerateSqlFile iGenerateSqlFile) {
		moduleVO.setState(1);
		ModuleVO objModuleVO = readFirstModuleVO(moduleVO.getParentModuleId());
		if (objModuleVO == null)
			moduleVO.setSortId(1);
		else {
			moduleVO.setSortId(objModuleVO.getSortId() - 1);
		}

		iGenerateSqlFile.createInsertSQL(moduleVO);
	}
	
	/**
	 * @param funcDTO
	 *            FuncDTO
	 * @param iGenerateSqlFile
	 *            生成SQL文件接口
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveFuncVO(FuncDTO funcDTO, IGenerateSqlFile iGenerateSqlFile) {
		FuncAssembler funcAssembler = AppContext.getBean(FuncAssembler.class);
		FuncVO funcVO = funcAssembler.createVOByDTO(funcDTO);
		if (funcVO.getFuncNodeType() != 3) {
			funcVO.setSortNo(getMaxSortNo(funcVO) + 1);
		}
		iGenerateSqlFile.createInsertSQL(funcVO);
	}
	
	/**
	 * @param funcVO FuncVO
	 * @return MaxSortNo
	 */
	private int getMaxSortNo(FuncVO funcVO) {
		return ((Integer) this.coreDAO.selectOne(
				"com.comtop.top.sys.accesscontrol.func.model.queryMaxSortNo",
				funcVO)).intValue();
	}

	/**
	 * @param moduleId 应用id
	 * @return ModuleVO
	 */
	private ModuleVO readFirstModuleVO(String moduleId) {
		ModuleVO objModuleVO = (ModuleVO) coreDAO.selectOne(
				"com.comtop.top.sys.module.model.readFirstModuleVO", moduleId);
		return objModuleVO;
	}

    /**
     * 修改应用，同时更新应用关联的模块信息
     * 
     * @param funcDTO 应用实体
     */
    @RemoteMethod
    public void updateFuncVOInModule(FuncDTO funcDTO) {
        String strModuleId = funcDTO.getParentFuncId();
        ModuleDTO objModuleDTO = moduleFacade.readModuleVO(strModuleId);
        objModuleDTO.setModuleCode(funcDTO.getFuncCode());
        objModuleDTO.setModuleType(2);
        objModuleDTO.setModuleName(funcDTO.getFuncName());
        objModuleDTO.setParentModuleId(objModuleDTO.getParentModuleId());
        objModuleDTO.setState(1);
        objModuleDTO.setFullPath(funcDTO.getFullPath());
        objModuleDTO.setShortName(funcDTO.getShortName());  
        objModuleDTO.setDescription(funcDTO.getDescription());
        moduleFacade.updateModule(objModuleDTO);
        funcFacade.updateFuncVOInModule(funcDTO);
        //生成应用菜单SQL脚本
        this.createFuncSQL(objModuleDTO, funcDTO);
    }
    
    /**
     * 删除应用，同时删除应用关联的模块
     * 
     * @param funcDTO parentFuncId 关联模块ID funcId 应用ID
     */
    @RemoteMethod
    public void deleteFuncVOInModule(FuncDTO funcDTO) {
        funcFacade.deleteFuncVOInModule(funcDTO);
    }
    
    /**
     * 读取应用信息
     * 
     * @param funcId
     *            应用Id
     * @return 应用信息
     */
    @RemoteMethod
    public FuncDTO readFunc(String funcId) {
        return funcFacade.getFuncVO(funcId);
    }
    
    /**
     * 获得模块下的子模块中状态标识为正常的应用数量
     * 
     * @param moduleId 模块ID
     * @return 模块下的子模块中状态标识为正常的应用数量
     */
    @RemoteMethod
    public boolean isHasNormalFunc(String moduleId) {
        return funcFacade.getNormalFuncCount(moduleId) > NumberConstant.ZERO ? true : false;
    }
    
    /**
     * 删除应用时需判断应用下是否有菜单数据
     * 
     * @param lstFuncId
     *            待删除的应用ID集合
     * @return 不能删除的应用ID
     */
    @RemoteMethod
    public List<String> getNoDelFuncId(List<String> lstFuncId) {
        FuncDTO objFuncDTO = new FuncDTO();
        List<String> lstFunc = new ArrayList<String>(NumberConstant.TEN);
        for (String strFuncId : lstFuncId) {
            objFuncDTO.setParentFuncId(strFuncId);
            objFuncDTO.setParentFuncType(AccessConstants.ENTITY_TYPE_FUNC);
            // 哪些应用下有菜单数据，不允许直接删除
            int iCount = funcFacade.queryFuncChildCount(objFuncDTO);
            if (iCount > NumberConstant.ZERO) {
                lstFunc.add(strFuncId);
            }
        }
        return lstFunc;
    }
    
    /**
     * 判断名称是否重复
     * 
     * @param funcDTO
     *            实体VO
     * @return 同级名称重复返回NameExists,验证通过返回Success
     */
    @RemoteMethod
    public String judgeNameRepeat(FuncDTO funcDTO) {
        // 判断同级名称是否重复
        if (funcFacade.isExistFuncName(funcDTO)) {
            return FuncConstants.FUNC_NAME_EXISTS;
        }
        return FuncConstants.FUNC_SUCCESS;
    }
    
    /**
     * 判断编码是否重复
     * 
     * @param funcDTO
     *            实体VO
     * @return 全局编码重复返回 CodeExists 验证通过返回Success
     */
    @RemoteMethod
    public String judgeCodeRepeat(FuncDTO funcDTO) {
        // 判断编码是否重复
        if (funcFacade.isExistFuncCode(funcDTO)) {
            return FuncConstants.FUNC_CODE_EXISTS;
        }
        return FuncConstants.FUNC_SUCCESS;
    }
    
    /**
     * 删除选中的应用数据
     * 
     * @param lstFuncIds
     *            应用ID集合
     */
    @RemoteMethod
    public void deleteFuncList(List<String> lstFuncIds) {
        funcFacade.deleteFuncList(lstFuncIds);
    }
    
    /**
     * 删除指定的功能数据，如果是删除页面或者菜单，一并将页面或者菜单下的操作删除掉
     * 
     * @param funcId 功能ID
     * @param funcNodeType 功能类型
     */
    @RemoteMethod
    public void deleteFunc(String funcId, String funcNodeType) {
        funcFacade.deleteFuncVO(funcId, funcNodeType);
    }
    
    /**
     * 上移、下移
     * 
     * @param funcIds 需要交换顺序的两个功能ID
     */
    @RemoteMethod
    public void updateFuncSortNo(List<String> funcIds) {
        funcFacade.updateFuncSortNo(funcIds);
    }
    
    /**
     * 判断下级存在实体、表、服务、界面、常用数据类型、工作流等信息
     * 
     * @param moduleId 应用ID
     * @return true 存在信息; false 不存在信息
     */
    @RemoteMethod
	public boolean hasSomeInfoByFunc(final String moduleId) {
		// 根据应用ID查询实体
		// 根据应用ID查询表，实体不存在，表肯定不存在
		// TODO

		// 根据应用ID查询服务
		// 根据应用ID查询界面
		// TODO

		// 根据应用ID查询常用数据类型
		// TODO

		// 根据应用ID查询工作流
		try {
			CipProcessPageBean objRsPageBean = BpmsProcessHelper
					.queryUnDeployeProcessByDirCode(moduleId, "", 1, 10);
			if (objRsPageBean.getAllRows() > 0) {
				return true;
			}
			objRsPageBean = BpmsProcessHelper.queryDeployedProcessByDirCode(
					moduleId, "", 1, 10);
			if (objRsPageBean.getAllRows() > 0) {
				return true;
			}
		} catch (AbstractBpmsException e) {
			e.printStackTrace();
		}
		return false;
	}
    
}
