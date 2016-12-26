/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.entity.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.ObjectOperator;
import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.common.storage.exception.ValidateException;
import com.comtop.cap.bm.metadata.entity.facade.EntityFacade;
import com.comtop.cap.bm.metadata.entity.model.EntitySource;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.bm.metadata.entity.model.query.QueryModel;
import com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade;
import com.comtop.cap.bm.metadata.sysmodel.model.CapPackageVO;
import com.comtop.cap.bm.metadata.sysmodel.utils.CapSystemModelUtil;
import com.comtop.cap.bm.webapp.util.GenerateCodeUtils;
import com.comtop.cap.codegen.generate.EntityWrapperFactory;
import com.comtop.cap.codegen.generate.ExceptionGenerator;
import com.comtop.cap.codegen.generate.GenerateCode;
import com.comtop.cap.codegen.generate.Generator;
import com.comtop.cap.codegen.model.WrapperAttribute;
import com.comtop.cap.codegen.model.WrapperEntity;
import com.comtop.cap.codegen.model.WrapperQueryModel;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cip.json.JSON;
import com.comtop.cap.bm.metadata.pkg.facade.IPackageFacade;
import com.comtop.cap.bm.metadata.pkg.facade.PackageFacadeImpl;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.StringUtil;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 
 * 新版实体操作Action
 * 
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 * @version 2016年5月11日 许畅 修改
 */
@DwrProxy
@MadvocAction
public class EntityOperateAction {
    
    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(EntityOperateAction.class);
    
    /** 实体 Facade */
    private final EntityFacade entityFacade = AppContext.getBean(EntityFacade.class);
    
    /** 包Facade */
    private final IPackageFacade packageFacade = AppContext.getBean(PackageFacadeImpl.class);
    
    /** 实体配置Facade */
    public PreferencesFacade objPerferencesFacade = AppContext.getBean(PreferencesFacade.class);
    
    /** 数字常量 */
    private final static int NUM_ZERO = 0;
    
    /** error常量 */
    private final static String SUPER_CLASS_CHANGE_ERROR = "com.zeroturnaround.javarebel.SuperClassChangedError";
    
    /** 默认查询条件前缀 */
    private final static String START_PREFIX = "\t";
    
    /** 默认查询条件后缀 */
    private final static String END_PREFIX = "\n\t";
    
    /**
     * 生成实体代码
     * 
     * @param packageId 包id
     * @param genType 生成类型
     * @return 操作结果
     * @throws OperateException 操作异常
     */
    @RemoteMethod
    public String executeGenerateCode(String packageId, int genType) throws OperateException {
        List<EntityVO> lstEntityVO = null;
        try {
            lstEntityVO = entityFacade.queryEntityList(packageId);
        } catch (OperateException e) {
            LOGGER.error("查询包路径下的实体出现异常", e);
            return "查询包路径下的实体出现异常！";
        }
        
        return executeGenerateCode(lstEntityVO, packageId, genType);
    }
    
    /**
     * 保存并生成代码
     * 
     * @param entityVO 实体对象
     * @param genType 代码生成类型
     * @return 实体对象
     * @throws ValidateException 验证失败
     * @throws OperateException 操作异常
     */
    @RemoteMethod
    public EntityVO saveAndGenerateCode(EntityVO entityVO, int genType) throws ValidateException, OperateException {
        EntityVO objEntityVO = entityFacade.saveEntity(entityVO);
        EntityVO objGenVO = entityFacade.loadEntity(objEntityVO.getModelId(), objEntityVO.getPackageId());
        List<EntityVO> lst = new ArrayList<EntityVO>();
        lst.add(objGenVO);
        String strError = executeGenerateCode(lst, entityVO.getPackageId(), genType);
        return ("".equals(strError) ? objGenVO : null);
    }
    
    /**
     * 生成实体所有代码
     * 
     * @param vos 需要生成代码的实体列表
     * @param packageId 包id
     * @return 操作结果
     * @throws OperateException 操作异常
     */
    @RemoteMethod
    public String executeGenerateCode(List<EntityVO> vos, String packageId) throws OperateException {
        return executeGenerateCode(vos, packageId, Generator.GEN_ALL);
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
        List<EntityVO> lstEntityVOs = new ArrayList<EntityVO>(ids.size());
        for (int i = 0; i < ids.size(); i++) {
            EntityVO objEntityVO = entityFacade.loadEntity(ids.get(i), packageId);
            // 已有实体不做处理，新元数据可通过entitySource处理，旧元数据仍然通过entityType来处理
            if (EntitySource.EXIST_ENTITY_INPUT.getValue().equals(objEntityVO.getEntitySource()) || "exist_entity".equals(objEntityVO.getEntityType())) {
                continue;
            }
            lstEntityVOs.add(objEntityVO);
        }
        return lstEntityVOs;
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
        if (vos == null || vos.size() == NUM_ZERO) {
            return "请选择要生成代码的实体";
        }
        // 分析实体关联性，获取关联实体
        List<EntityVO> lstUNGenCodeEntityVO = new ArrayList<EntityVO>();
        for (EntityVO entityVO : vos) {
            List<EntityVO> lstRelationVO = entityFacade.queryEntityRelationVO(entityVO);
            List<EntityVO> lstUNGenCodeRelationVO = entityFacade.filterUnGenerateCodeEntities(lstRelationVO);
            for (EntityVO unGenCodeentityVO : lstUNGenCodeRelationVO) {
                // 判断当前关联实体是否能生成代码
                if (unGenCodeentityVO.isState()) {
                    lstUNGenCodeEntityVO.add(unGenCodeentityVO);
                }
            }
        }
        
        if (lstUNGenCodeEntityVO.size() > 0) {
            vos.addAll(lstUNGenCodeEntityVO);
        }
        
        // 包路径
        String strPackagePath = packageFacade.queryPackageById(packageId).getFullPath();
        
        ObjectOperator objExc = new ObjectOperator(vos);
        try {
            List<ExceptionVO> lst = objExc.queryList("./methods/exceptions", ExceptionVO.class);
            List<ExceptionVO> lstToGene = new ArrayList<ExceptionVO>();
            List<String> lstExpId = new ArrayList<String>();
            CapPackageVO pkg = CapSystemModelUtil.queryCapPackageByEntity(vos.get(0));
            for (ExceptionVO exceptionVO : lst) {
                if (lstExpId.contains(exceptionVO.getModelId())) {
                    continue;
                }
                lstExpId.add(exceptionVO.getModelId());
                exceptionVO.setPkg(pkg);
                lstToGene.add(exceptionVO);
            }
            GenerateCode.generateCode(strPackagePath, new ExceptionGenerator(), lstToGene);
        } catch (OperateException e) {
            LOGGER.error("异常查询出错:" + e.getMessage(), e);
        }
        
        Generator objGenerator = new Generator();
        objGenerator.setGenType(genType);
        GenerateCode.generateCode(strPackagePath, objGenerator, vos);
        
        StringBuilder strMsg = new StringBuilder();
        // 注册soa服务并刷新
        if (Generator.GEN_VO != genType) {
            try {
                String codePath = objGenerator.getProjectDir(vos.get(0));
                for (EntityVO entityVO : vos) {
                    GenerateCodeUtils.registerAndRefreshSoaInfo(entityVO, codePath);
                }
            } catch (Throwable ta) {
                if (SUPER_CLASS_CHANGE_ERROR.equals(ta.getClass().getName())) {
                    strMsg.append("生成代码时注册soa服务失败，修改实体超类后请重启服务！");
                } else {
                    strMsg.append("生成代码时注册soa服务发生异常！");
                }
                LOGGER.error(strMsg.toString(), ta);
            }
        }
        return strMsg.toString();
        
    }
    
    /**
     * 通过实体属性id 包id查询自定义查询条件内容
     * 
     * @param entity
     *            实体VO
     * 
     * @return 自定义查询条件
     */
    @RemoteMethod
    public String loadDefaultSqlCondition(EntityVO entity) {
        
        Map<String, Object> data = new HashMap<String, Object>();
        
        // 如果元数据中存在自定义sql条件或者是启用则直接取元数据中数据
        if (StringUtil.isNotEmpty(entity.getCustomSqlCondition()) || entity.isCustomSqlConditionEnable()) {
            data.put("customSqlConditionEnable", entity.isCustomSqlConditionEnable());
            data.put("customSqlCondition", entity.getCustomSqlCondition());
            return JSON.toJSONString(data);
        }
        
        StringBuffer defaultSqlCondition = new StringBuffer();
        data.put("customSqlConditionEnable", false);
        
        try {
            Map<String, Object> wrapper = EntityWrapperFactory.getDefaultWrapper().wrapper(entity);
            
            if (wrapper == null || !wrapper.containsKey("entity")) {
                data.put("customSqlCondition", defaultSqlCondition);
                return JSON.toJSONString(data);
            }
            
            WrapperEntity wrapperEntity = (WrapperEntity) wrapper.get("entity");
            List<WrapperAttribute> lst = wrapperEntity.getWrapperAttributes();
            if (lst == null) {
                data.put("customSqlCondition", defaultSqlCondition);
                return JSON.toJSONString(data);
            }
            
            int count = 0;
            for (WrapperAttribute attribute : lst) {
                String queryExpr = attribute.getQueryExpr();
                String queryRangeExpr1 = attribute.getQueryRangeExpr1();
                String queryRangeExpr2 = attribute.getQueryRangeExpr2();
                
                if (StringUtils.isNotEmpty(queryExpr)) {
                    if (count == 0) {
                        defaultSqlCondition.append(START_PREFIX);
                    }
                    defaultSqlCondition.append(queryExpr + END_PREFIX);
                }
                
                if (StringUtils.isNotEmpty(queryRangeExpr1)) {
                    if (count == 0) {
                        defaultSqlCondition.append(START_PREFIX);
                    }
                    defaultSqlCondition.append(queryRangeExpr1 + END_PREFIX);
                }
                
                if (StringUtils.isNotEmpty(queryRangeExpr2)) {
                    if (count == 0) {
                        defaultSqlCondition.append(START_PREFIX);
                    }
                    defaultSqlCondition.append(queryRangeExpr2 + END_PREFIX);
                }
                count++;
            }
            
            data.put("customSqlCondition", defaultSqlCondition);
            
        } catch (Exception e) {
            LOGGER.error("获取默认实体查询条件出错:" + e.getMessage(), e);
        }
        
        return JSON.toJSONString(data);
    }
    
    /**
     * 查询建模-另存为公共条件
     * 
     * @param queryModel
     *            查询建模
     * @return 包装查询建模where
     */
    @RemoteMethod
    public String saveCommonQueryCondition(QueryModel queryModel) {
        queryModel.getWhere().setRefCommonCondtion(false);
        WrapperQueryModel wrapper = new WrapperQueryModel(queryModel);
        return wrapper.getWhere().substring("WHERE".length()).trim();
    }
    
    /**
     * 获取关联实体id
     * 
     * @param method
     *            实体方法
     * @return 关联实体id
     */
    @RemoteMethod
    public String getRelationEntityId(MethodVO method) {
        if (method == null || method.getReturnType() == null)
            return "";
        if (method.getReturnType().readRelationEntityIds().size() < 1)
            return "";
        return method.getReturnType().readRelationEntityIds().get(0);
    }
}
