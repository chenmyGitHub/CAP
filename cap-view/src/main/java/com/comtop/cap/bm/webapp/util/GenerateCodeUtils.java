/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.base.inter.SoaRegisterBaseData;
import com.comtop.cap.bm.metadata.entity.model.EntityType;
import com.comtop.cap.bm.metadata.pkg.model.ProjectVO;
import com.comtop.cap.bm.metadata.soareg.ISoaServiceManager;
import com.comtop.cap.bm.metadata.soareg.SoaServiceFactory;
import com.comtop.cap.bm.metadata.soareg.model.SoaBaseVO;
import com.comtop.cap.bm.webapp.soaregist.handler.HandlerContext;
import com.comtop.cap.codegen.util.CapCodegenUtils;

/**
 * 
 * 代码生成工具类
 * 
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class GenerateCodeUtils {
    
    /**
     * 构造函数
     */
    private GenerateCodeUtils() {
        
    }
    
    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateCodeUtils.class);
    
    /**
     * 生成实体代码时，注册并刷新实体代码的soa信息
     * 
     * @param vo 待注册实体
     * @param codePath 配置的生成代码路径
     */
    public static void registerAndRefreshSoaInfo(SoaRegisterBaseData vo, final String codePath) {
        List<String> lstServiceCodes = new ArrayList<String>();
        SoaBaseVO soaBaseVO = new SoaBaseVO();
        if (vo == null) {
            return;
        }
        
        // 实体类型转换为SoaBaseVO
        soaBaseVO = HandlerContext.getMapHandler().get(vo.gainObjectType()).convertType(vo);
        
        // 数据实体不注册soa
        if (EntityType.DATA_ENTITY.getValue().equals(soaBaseVO.getEntityType()))
            return;
        
		// 执行并生成soa脚本  刷新soa服务缓存
		constructAndExecEntityRegSqlScript(soaBaseVO, lstServiceCodes, codePath);
    }
    
    /**
     * 组装并执行和持久化实体代码生成时的注册sql脚本（包括扩展参数脚本、实体SOA服务注册脚本）
     * 
     * @param soaBaseVO soa实体
     * @param lstServiceCodes 待刷新实体服务编码集合
     * @param codePath 配置的生成代码路径
     */
    private static void constructAndExecEntityRegSqlScript(SoaBaseVO soaBaseVO, List<String> lstServiceCodes, final String codePath) {
		ISoaServiceManager iSoaServiceManager = SoaServiceFactory.getSoaServiceExecutor();
		
		String executeSQL = iSoaServiceManager.getExecuteSQL(soaBaseVO);
		LOGGER.info("开始执行实体的soa服务sql注册脚本");
        // 注册实体方法的soa服务sql脚本
		iSoaServiceManager.registerSoaSql(executeSQL);
        // 通过soa远程调用，执行soa服务注册脚本
        String sid = "capSoaExtendFacade.registerEntitySoaSql";
        iSoaServiceManager.callSoaRemoteService(sid, executeSQL);
		LOGGER.info("结束执行实体的soa服务sql注册");
		
		// 根据实体生成实体注册SOA的SQL脚本
		String clientSQL = iSoaServiceManager.getClientSQL(soaBaseVO);
		writeEntitySqlFile(soaBaseVO, codePath, clientSQL);
		
		// 刷新soa服务缓存
		lstServiceCodes.add(CapCodegenUtils.firstLetterToLower(soaBaseVO.getAliasName()) + "Facade");
		refreshSoaService(lstServiceCodes, iSoaServiceManager);
	}
    
    /**
     * 根据实体及路径，生成相关实体soa注册等sql文件
     * 
     * @param soaBaseVO 实体vo
     * @param codePath 项目路径
     * @param fileContent sql脚本内容
     * 
     */
    private static void writeEntitySqlFile(SoaBaseVO soaBaseVO, String codePath, String fileContent) {
        String strSqlFileId = soaBaseVO.getModelId().replaceAll(".entity.", ".soa.");
        // com.comtop.techproject.soa.GbProject
        if (!"".equals(strSqlFileId)) {
            String[] strPaths = strSqlFileId.split("\\.");
            int iLong = strPaths.length;
            String strType = strPaths[iLong - 2];
            String strName = "[3]" + strPaths[iLong - 1] + "." + strType + ".sql";
            String strPackage = "";
            for (int i = 0; i < iLong - 2; i++) {
                strPackage += File.separator + strPaths[i];
            }
            ProjectVO project = CapCodegenUtils.getProject();
            CapViewUtils.writeResourceSqlFile(codePath, project.getSrcResourceDir(), strPackage, strName, fileContent);
        }
    }
    
    /**
     * 根据实体新信息刷新soa缓存服务
     * 
     * @param lstServiceCodes 待刷新soa服务的实体vo编码集合
     * @param iSoaServiceManager ISoaServiceManager
     */
    private static void refreshSoaService(List<String> lstServiceCodes,ISoaServiceManager iSoaServiceManager) {
        LOGGER.info("开始刷新soa缓存...");
        iSoaServiceManager.refreshSoaService(lstServiceCodes);
        // 通过soa远程调用，刷新soa缓存
        String sid = "capSoaExtendFacade.refreshSoaService";
        iSoaServiceManager.callSoaRemoteService(sid, lstServiceCodes);
    }
    
}
