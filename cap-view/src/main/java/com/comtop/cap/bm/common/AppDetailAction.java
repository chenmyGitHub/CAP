/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.common;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.database.dbobject.facade.FunctionFacade;
import com.comtop.cap.bm.metadata.database.dbobject.facade.ProcedureFacade;
import com.comtop.cap.bm.metadata.database.dbobject.facade.TableFacade;
import com.comtop.cap.bm.metadata.database.dbobject.facade.ViewFacade;
import com.comtop.cap.bm.metadata.database.dbobject.model.FunctionVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.ProcedureVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableVO;
import com.comtop.cap.bm.metadata.database.dbobject.model.ViewVO;
import com.comtop.cap.bm.metadata.database.dbobject.util.DBType;
import com.comtop.cap.bm.metadata.database.dbobject.util.DBTypeAdapter;
import com.comtop.cap.bm.metadata.entity.facade.EntityFacade;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.page.desinger.facade.PageFacade;
import com.comtop.cap.bm.metadata.page.desinger.model.PageVO;
import com.comtop.cap.bm.metadata.page.template.facade.MetadataGenerateFacade;
import com.comtop.cap.bm.metadata.page.template.model.MetadataGenerateVO;
import com.comtop.cap.bm.metadata.serve.facade.ServiceObjectFacade;
import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cap.bm.webapp.entity.action.EntityOperateAction;
import com.comtop.cap.bm.webapp.workbenchconfig.action.WorkflowWorkbenchAction;
import com.comtop.cap.bm.webapp.workflow.action.CipWorkFlowListAction;
import com.comtop.cap.doc.info.action.DocumentAction;
import com.comtop.cap.doc.info.action.ImportResult;
import com.comtop.cap.ptc.team.action.CapAppAction;
import com.comtop.cap.ptc.team.model.CapAppVO;
import com.comtop.cap.ptc.team.model.CapEmployeeVO;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.top.cfg.action.ConfigItemAction;
import com.comtop.top.cfg.facade.ConfigClassifyFacade;
import com.comtop.top.cfg.facade.ConfigItemFacade;
import com.comtop.top.cfg.facade.IConfigClassifyFacade;
import com.comtop.top.cfg.facade.IConfigItemFacade;
import com.comtop.top.cfg.model.ConfigItemDTO;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.sys.accesscontrol.func.facade.FuncFacade;
import com.comtop.top.sys.accesscontrol.func.facade.IFuncFacade;
import com.comtop.top.sys.accesscontrol.func.model.FuncDTO;
import com.comtop.top.sys.module.facade.IModuleFacade;
import com.comtop.top.sys.module.facade.ModuleFacade;
import com.comtop.top.sys.module.model.ModuleDTO;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 获取CAP开发建模模块首页初始化数据
 * 
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年8月30日 凌晨
 */
@DwrProxy
public class AppDetailAction {
    
    /**
     * 功能facade
     */
    protected IFuncFacade funcFacade = AppContext.getBean(FuncFacade.class);
    
    /** 系统模块 Facade */
    protected IModuleFacade moduleFacade = AppContext.getBean(ModuleFacade.class);
    
    /** 实体 Facade */
    protected EntityFacade entityFacade = AppContext.getBean(EntityFacade.class);
    
    /** 页面 Facade */
    protected PageFacade pageFacade = AppContext.getBean(PageFacade.class);
    
    /** 元数据生成 Facade */
    protected MetadataGenerateFacade metadataGenerateFacade = AppContext.getBean(MetadataGenerateFacade.class);
    
    /** 数据库表元数据 Facade */
    protected TableFacade tableFacade = AppContext.getBean(TableFacade.class);
    
    /** 数据库视图元数据 Facade */
    protected ViewFacade viewFacade = AppContext.getBean(ViewFacade.class);
    
    /** 数据库存储过程元数据 Facade */
    protected ProcedureFacade procedureFacade = AppContext.getBean(ProcedureFacade.class);
    
    /** 数据库函数元数据 Facade */
    protected FunctionFacade functionFacade = AppContext.getBean(FunctionFacade.class);
    
    /** 服务元数据 Facade */
    protected ServiceObjectFacade serviceObjectFacade = AppContext.getBean(ServiceObjectFacade.class);
    
    /**
     * 获取页面初始化需要的数据
     * 
     * @param funcId 模块ID
     * @param capAppVO 个人应用vo
     * @param objConfigItemDTO 配置项DTO
     * @return 页面数据
     * @throws OperateException 元数据操作异常
     * @throws IllegalAccessException 反射异常
     * @throws InstantiationException 反射异常
     * @throws NoSuchMethodException 反射异常
     * @throws SecurityException 反射异常
     * @throws NoSuchFieldException 反射异常
     */
    @RemoteMethod
    public Map<String, Object> getInitMetaData(String funcId, CapAppVO capAppVO, ConfigItemDTO objConfigItemDTO)
        throws OperateException, InstantiationException, IllegalAccessException, SecurityException,
        NoSuchMethodException, NoSuchFieldException {
        Map<String, Object> map = new HashMap<String, Object>();
        
        // 初始化应用信息
        FuncDTO funcDTO = readFuncByModuleId(funcId);
        map.put("func", funcDTO);
        
        // 初始化个人应用
        CapAppAction capAppAction = new CapAppAction();
        Object vo = null;
        if (StringUtil.isNotBlank(capAppVO.getId())) {
            vo = capAppAction.queryById(capAppVO.getId());
        } else {
            vo = capAppAction.queryStoreApp(capAppVO);
        }
        map.put("appVO", vo);
        
        // 初始化实体信息
        List<EntityVO> lstEntity = entityFacade.queryEntityList(funcDTO.getParentFuncId());
        map.put("entity", lstEntity);
        
        // 初始化页面信息
        List<PageVO> lstPage = pageFacade.queryPageList(funcDTO.getParentFuncId());
        map.put("page", lstPage);
        
        // 初始化页面的元数据生成信息
        List<MetadataGenerateVO> lstMetadataGenerate = metadataGenerateFacade.queryMetadataGenerateList(funcDTO
            .getParentFuncId());
        map.put("pageMetadata", lstMetadataGenerate);
        
        // 初始化流程信息
        CipWorkFlowListAction cipWorkFlowListAction = new CipWorkFlowListAction();
        Map<String, Object> unDeployeWorkflowMap = cipWorkFlowListAction.queryUnDeployeProcessByDirCode(
            funcDTO.getFuncCode(), capAppVO.getEmployeeId(), 1, 100);
        map.put("unDeployeWorkflow", unDeployeWorkflowMap.get("list"));
        
        Map<String, Object> deployeWorkflowMap = cipWorkFlowListAction.queryDeployeEdProcesses(funcDTO.getFuncCode(),
            capAppVO.getEmployeeId(), 1, 100);
        map.put("deployeWorkflow", deployeWorkflowMap.get("list"));
        
        // 初始化工作台待办配置
        Map<String, Object> workbenchConfigMap = new WorkflowWorkbenchAction().getListData(funcDTO.getFuncCode());
        Object o = workbenchConfigMap == null ? new ArrayList() : workbenchConfigMap.get("list");
        map.put("workbenchConfig", o);
        
        // 初始化数据库表元数据信息
        List<TableVO> tables = tableFacade.queryTableList(funcDTO.getFullPath());
        map.put("tables", tables);
        
        // 初始化数据库视图信息
        List<ViewVO> views = viewFacade.queryViewList(funcDTO.getFullPath());
        map.put("views", views);
        
        // 初始化数据库存储过程信息
        List<ProcedureVO> produces = procedureFacade.queryProcedureList(funcDTO.getFullPath());
        map.put("produces", produces);
        
        // 初始化数据库函数信息
        List<FunctionVO> functions = functionFacade.queryProcedureList(funcDTO.getFullPath());
        map.put("functions", functions);
        
        // 初始化服务信息
        List<ServiceObjectVO> services = serviceObjectFacade.queryServiceObjectList(funcId);
        map.put("service", services);
        
        // 初始化配置项信息
        ConfigItemAction configItemAction = new ConfigItemAction();
        ConfigItemAction.class.getConstructor().setAccessible(true);
        Field configItemFacadeField = ConfigItemAction.class.getDeclaredField("configItemFacade");
        Field configClassifyFacadeField = ConfigItemAction.class.getDeclaredField("configClassifyFacade");
        configItemFacadeField.setAccessible(true);
        configClassifyFacadeField.setAccessible(true);
        
        IConfigItemFacade configItemFacade = AppContext.getBean(ConfigItemFacade.class);
        IConfigClassifyFacade configClassifyFacade = AppContext.getBean(ConfigClassifyFacade.class);
        configItemFacadeField.set(configItemAction, configItemFacade);
        configClassifyFacadeField.set(configItemAction, configClassifyFacade);
        
        // 走他们的全量查询需要设置ConfigClassifyCode，我们默认传递ConfigClassifyId
        // objConfigItemDTO.setShowAllConfig("true");
        // objConfigItemDTO.setConfigClassifyCode(funcDTO.getFuncCode());
        // top提供的实现方式有问题 手动设置分页参数为最大
        objConfigItemDTO.setPageSize(2147483647);
        Map<String, Object> configMap = configItemAction.queryConfigItemList(objConfigItemDTO);
        map.put("dictionary", configMap.get("list"));
        
        //初始化数据库类型
        DBType dbType=  DBTypeAdapter.getDBType();
        map.put("dbType", dbType.getValue());
        return map;
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
     * 导出指定开发树节点ID下的设计文档
     *
     * @param nodeId 开发树节点ID,
     * @param docType 文档类型，DBD:数据库设计文档、HLD：概要设计文档；LLD详细设计文档,需与模板文件规则保持一致
     * @return 导出结果
     */
    @RemoteMethod
    public ImportResult exportDocumentOutDoc(final String nodeId, String docType) {
        return new DocumentAction().exportDocumentOutDoc(nodeId, docType);
    }
    
    /**
     * 根据ids获取实体的元数据
     * 
     * @param ids 需要生成代码的实体id列表
     * @param packageId 包id
     * @return 操作结果
     */
    @RemoteMethod
    public List<EntityVO> queryEntityVOByIds(List<String> ids, String packageId) {
        EntityOperateAction entityOperateAction = new EntityOperateAction();
        return entityOperateAction.queryEntityVOByIds(ids, packageId);
    }
    
    /**
     * 生成实体代码
     * 
     * @param vos 需要生成代码的实体列表
     * @param packageId 包id
     * @param genType 代码生成类型
     * @return 操作结果
     * @throws OperateException 操作异常
     */
    @RemoteMethod
    public String executeGenerateCode(List<EntityVO> vos, String packageId, int genType) throws OperateException {
        EntityOperateAction entityOperateAction = new EntityOperateAction();
        return entityOperateAction.executeGenerateCode(vos, packageId, genType);
    }
    
    /**
     * 收藏应用
     *
     * @param capAppVO 应用VO
     * @return 成功标志
     */
    @RemoteMethod
    public String storeUpApp(final CapAppVO capAppVO) {
        return new CapAppAction().storeUpApp(capAppVO);
    }
    
    /**
     * 根据登录人的应用收藏信息
     *
     * @param capAppVO 应用ID,人员ID
     * @return 应用收藏信息
     */
    @RemoteMethod
    public CapAppVO queryStoreApp(final CapAppVO capAppVO) {
        return new CapAppAction().queryStoreApp(capAppVO);
    }
    
    /**
     * 取消收藏
     *
     * @param capAppVO 应用VO
     * @return 成功标志
     */
    @RemoteMethod
    public boolean cancelAppStore(final CapAppVO capAppVO) {
        return new CapAppAction().cancelAppStore(capAppVO);
    }
    
    /**
     * 分配应用
     *
     * @param lstCapEmployee 分配人
     * @param appId 应用ID
     * @param teamId 团队ID
     * @return 成功标志
     */
    @RemoteMethod
    public int assignApp(final List<CapEmployeeVO> lstCapEmployee, final String appId, final String teamId) {
        return new CapAppAction().assignApp(lstCapEmployee, appId, teamId);
    }
    
    /**
     * 删除表
     *
     * @param models ID集合
     * @return 是否成功
     */
    @RemoteMethod
    public boolean delTables(String[] models) {
        return tableFacade.delTables(models);
    }
    
    /**
     * 查表列表
     * 
     * @param packagePath 模块包路径
     * 
     * @return 实体列表
     * @throws OperateException 异常
     */
    @RemoteMethod
    public List<TableVO> queryTableList(String packagePath) throws OperateException {
        return tableFacade.queryTableList(packagePath);
    }
    
    /**
     * 删除模型
     *
     * @param models ID集合
     * @return 是否成功
     */
    @RemoteMethod
    public boolean delVeiws(String[] models) {
        return viewFacade.delVeiws(models);
    }
    
    /**
     * 查询视图列表
     * 
     * @param packagePath 模块包路径
     * 
     * @return 实体列表
     * @throws OperateException 异常
     */
    @RemoteMethod
    public List<ViewVO> queryViewList(String packagePath) throws OperateException {
        return viewFacade.queryViewList(packagePath);
    }
    
    /**
     * 删除模型
     *
     * @param models ID集合
     * @return 是否成功
     */
    @RemoteMethod
    public boolean delProcedures(String[] models) {
        return procedureFacade.delProcedures(models);
    }
    
    /**
     * 查询视图列表
     * 
     * @param packagePath 模块包路径
     * 
     * @return 实体列表
     * @throws OperateException 异常
     */
    @RemoteMethod
    public List<ProcedureVO> queryProcedureList(String packagePath) throws OperateException {
        return procedureFacade.queryProcedureList(packagePath);
    }
    
    /**
     * 删除模型
     *
     * @param models ID集合
     * @return 是否成功
     */
    @RemoteMethod
    public boolean delFunctions(String[] models) {
        return functionFacade.delFunctions(models);
    }
    
    /**
     * 查询视图列表
     * 
     * @param packagePath 模块包路径
     * 
     * @return 实体列表
     * @throws OperateException 异常
     */
    @RemoteMethod
    public List<FunctionVO> queryFuntionList(String packagePath) throws OperateException {
        return functionFacade.queryProcedureList(packagePath);
    }
    
    /**
     * 删除模型
     * 
     * @param models ID集合
     * @return 是否成功
     */
    @RemoteMethod
    public boolean delServiceObjectList(String[] models) {
        return serviceObjectFacade.delServiceObjectList(models);
    }
    
    /**
     * 查询服务列表
     * 
     * @param packageId 当前模块包ID
     * 
     * @return 服务列表
     * @throws OperateException 异常
     */
    @RemoteMethod
    public List<ServiceObjectVO> queryServiceObjectList(String packageId) throws OperateException {
        return serviceObjectFacade.queryServiceObjectList(packageId);
    }
    
    /**
     * 删除模型
     *
     * @param models ID集合
     * @return 是否成功
     */
    @RemoteMethod
    public boolean deleteModels(String[] models) {
        return metadataGenerateFacade.deleteModels(models);
    }
    
    /**
     * 查询生成数据源列表
     *
     * @param modelPackage 查询当前模块下的页面
     * @return 页面对象集合
     * @throws OperateException 异常
     */
    @RemoteMethod
    public List<MetadataGenerateVO> queryMetadataGenerateList(String modelPackage) throws OperateException {
        return metadataGenerateFacade.queryMetadataGenerateList(modelPackage);
    }
    
    /**
     * 删除
     * 
     * @param ids
     *            界面实体
     */
    @RemoteMethod
    public void deleteAction(String[] ids) {
        new WorkflowWorkbenchAction().deleteAction(ids);
    }
    
    /**
     * 获取界面和列表数据
     * 
     * @param moduleCode
     *            moduleCode
     * @return map
     */
    @RemoteMethod
    public Map<String, Object> getListData(String moduleCode) {
        return new WorkflowWorkbenchAction().getListData(moduleCode);
    }
}
