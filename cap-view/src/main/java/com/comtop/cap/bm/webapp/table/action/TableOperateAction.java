/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.table.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.common.storage.exception.ValidateException;
import com.comtop.cap.bm.metadata.database.datasource.DataSourceFactory;
import com.comtop.cap.bm.metadata.database.dbobject.facade.TableFacade;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableCompareResult;
import com.comtop.cap.bm.metadata.database.dbobject.model.TableVO;
import com.comtop.cap.bm.metadata.database.dbobject.util.DBType;
import com.comtop.cap.bm.metadata.database.execute.IBuilder;
import com.comtop.cap.bm.metadata.database.execute.IncrementSQLBuilder;
import com.comtop.cap.bm.metadata.database.execute.SQLExecutorFactory;
import com.comtop.cap.bm.metadata.database.util.MetaConnection;
import com.comtop.cap.bm.metadata.database.util.TableUtils;
import com.comtop.cap.bm.metadata.entity.util.EntityImportUtils;
import com.comtop.cap.bm.metadata.pdm.model.PdmSelectVO;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.metadata.sysmodel.facade.SysmodelFacade;
import com.comtop.cap.bm.metadata.sysmodel.model.CapPackageVO;
import com.comtop.cap.bm.webapp.util.CapViewUtils;
import com.comtop.cap.codegen.config.ConfigFactory;
import com.comtop.cap.codegen.config.GeneratorConfig;
import com.comtop.cap.codegen.generate.GenerateCode;
import com.comtop.cap.codegen.generate.ICodeGenerateConfig;
import com.comtop.cap.codegen.model.WrapperTableIncrementSQL;
import com.comtop.cap.codegen.model.WrapperTableSQL;
import com.comtop.cip.common.util.DBUtil;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.corm.resource.util.CollectionUtils;
import com.comtop.top.core.jodd.AppContext;

import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;
import freemarker.template.Configuration;

/**
 * 元数据表操作Action
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年9月23日 许畅 新建
 */
@DwrProxy
public class TableOperateAction {
    
    /** 实体 Facade */
    private final TableFacade tableFacade = AppContext.getBean(TableFacade.class);
    
    /** 日志记录 */
    private static final Logger LOGGER = LoggerFactory.getLogger(TableOperateAction.class);
    
    /** 包Facade */
    private final SysmodelFacade packageFacade = AppContext.getBean(SysmodelFacade.class);
    
    /**
     * 生成创建表sql
     * 
     * @param modelIds
     *            table元数据ModelId
     * @param pkgPath
     *            包路径
     * @param isExecuteSQL
     *            是否执行SQL
     * @return 是否成功
     * @throws ValidateException ValidateException
     */
    @RemoteMethod
    public String genCreateTableSQL(List<String> modelIds, String pkgPath, boolean isExecuteSQL) throws ValidateException {
        if (CollectionUtils.isEmpty(modelIds)) {
            throw new RuntimeException("modelIds size can not be null");
        }
        MetaConnection objMetaConn = EntityImportUtils.getMetaConnection();
        File srcFile = null;
        for (String modelId : modelIds) {
            TableVO srcTable = (TableVO) TableVO.loadModel(modelId);
            List<TableVO> lstTableVO = tableFacade.loadTableFromDatabase(objMetaConn.getSchema(), srcTable.getEngName(), 2, true, true, objMetaConn);
            TableVO newTable = null;
            // 如果数据库表存在 ，则需要同步本地元数据，否则直接拿旧的元数据导出表
            if (null != lstTableVO && lstTableVO.size() > 0) {
                newTable = tableFacade.setMessageToTableVO(lstTableVO.get(0), srcTable);
                // 同步本地数据表元数据
                newTable.saveModel();
            } else {
                newTable = srcTable;
            }
            
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("t", new WrapperTableSQL(newTable));
            
            String fileName = "[1]createTable_" + newTable.getEngName() + ".sql";
            srcFile = CapViewUtils.getFile(PreferenceConfigQueryUtil.getCodePath(), null, pkgPath, fileName);
            
            if (srcFile.exists()) {
                srcFile.delete();
            }
            GenerateCode.generateCode(param, getConfig(), this.getFtlTemple(), srcFile);
            if (isExecuteSQL) {
                // 获取SQL
                String sql = GenerateCode.getFtlContent(param, getConfig(), this.getFtlTemple());
                // 执行全量SQL
                SQLExecutorFactory.getInstance().executeCreateTableSQL(sql);
            }
        }
        DBUtil.closeConnection(objMetaConn.getConn(), null, null);
        
        return srcFile != null ? srcFile.getAbsolutePath() : null;
    }
    
    /**
     * 导入表SQL
     * 
     * @param packageId
     *            包id
     * @param lstPdmSelectVO
     *            选择的PdmSelectVO集合
     * @param isExecuteSQL
     *            是否执行SQL
     * @return 导入SQL文件
     * @throws ValidateException ValidateException
     */
    @RemoteMethod
    public String importTableSQL(String packageId, List<PdmSelectVO> lstPdmSelectVO, boolean isExecuteSQL) throws ValidateException {
        String pkgPath = getPackagePath(packageId);
        if (StringUtil.isBlank(pkgPath)) {
            throw new RuntimeException("package path can not be null");
        }
        List<String> modelIds = new ArrayList<String>();
        for (PdmSelectVO selectVO : lstPdmSelectVO) {
            String modelId = pkgPath + ".table." + selectVO.getEngName();
            modelIds.add(modelId);
        }
        
        return this.genCreateTableSQL(modelIds, pkgPath, isExecuteSQL);
    }
    
    /**
     * @param packageId 当前模块包ID
     * @return 模块包路径
     */
    private String getPackagePath(String packageId) {
        CapPackageVO objPackageVO = packageFacade.queryPackageById(packageId);
        return objPackageVO == null ? null : objPackageVO.getFullPath();
    }
    
    /**
     * 生成修改表sql
     * 
     * @param modelIds 表名列表
     * @param pkgPath 包路径
     * @return 返回生成结果
     * @throws ValidateException ValidateException
     */
    @RemoteMethod
    public String genIncrementSQL(List<String> modelIds, String pkgPath) throws ValidateException {
        // 获取需要更新的表信息
        List<TableCompareResult> lstResult = tableFacade.compareTable(modelIds);
        if (CollectionUtils.isEmpty(lstResult)) {
            throw new RuntimeException("table compare result can not be null.");
        }
        // 判断是否存在发生变化的表
        if (tableFacade.getTableChangeType(lstResult) == TableCompareResult.TABLE_EQUAL) {
            return "tableEqual";
        } else if (tableFacade.getTableChangeType(lstResult) == TableCompareResult.TABLE_NOT_EXISTS) {
            return "tableNotExists";
        } else {
            File srcFile = null;
            for (TableCompareResult objResult : lstResult) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("t", new WrapperTableIncrementSQL(objResult));
                
                String fileName = "[6]incrementTable_" + getTime() + "_" + objResult.getSrcTable().getCode() + ".sql";
                srcFile = CapViewUtils.getFile(PreferenceConfigQueryUtil.getCodePath(), null, pkgPath, fileName);
                
                // 开始生成代码
                GenerateCode.generateCode(param, getConfig(), this.getIncrementFtlTemple(), srcFile);
                // 同步本地数据表元数据
                objResult.getTargetTable().saveModel();
            }
            // 生成代码成功
            return srcFile != null ? srcFile.getAbsolutePath() : null;
        }
    }
    
    /**
     * 对比表结构
     * 
     * @param lstPdmSelectVO
     *            lstPdmSelectVO
     * @param packageId
     *            包id
     * 
     * @return TableCompareResult
     */
    @RemoteMethod
    public Map<String, Object> compareTable(List<PdmSelectVO> lstPdmSelectVO, String packageId) {
        String pkgPath = getPackagePath(packageId);
        
        List<String> modelIds = new ArrayList<String>();
        for (PdmSelectVO selectVO : lstPdmSelectVO) {
            String modelId = pkgPath + ".table." + selectVO.getEngName();
            modelIds.add(modelId);
        }
        
        return this.compare4Table(modelIds, pkgPath);
    }
    
    /**
     * 比较元数据表和数据库表
     * 
     * @param modelIds
     *            实体id集合
     * @param pkgPath
     *            包路径
     * @return Map
     */
    @RemoteMethod
    public Map<String, Object> compare4Table(List<String> modelIds, String pkgPath) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<TableCompareResult> genCreateSQL = new ArrayList<TableCompareResult>();// 执行全量SQL
        List<TableCompareResult> genIncrementSQL = new ArrayList<TableCompareResult>();// 执行增量SQL
        
        // 比较表结构
        this.compareTable(genCreateSQL, genIncrementSQL, modelIds);
        
        // 设置全量SQL
        resultMap.put("createTableSqls", getCreateTableSQL(genCreateSQL, pkgPath));
        
        // 列增量结果集
        List<Map<String, Object>> columns = DataSourceFactory.getDataProvider(genIncrementSQL).initColumn();
        
        // 索引增量结果集
        List<Map<String, Object>> indexs = DataSourceFactory.getDataProvider(genIncrementSQL).initIndex();
        
        resultMap.put("columnSize", genIncrementSQL.size());
        resultMap.put("columns", columns);
        resultMap.put("indexs", indexs);
        return resultMap;
    }
    
    /**
     * 判断是否需要增量执行
     * 
     * @param lstPdmSelectVO
     *            lstPdmSelectVO 包id
     * @return 是否增量执行
     */
    @RemoteMethod
    public boolean isIncrementExecute(List<PdmSelectVO> lstPdmSelectVO) {
        for (PdmSelectVO pdmSelectVO : lstPdmSelectVO) {
            if (tableFacade.isTableExsits(pdmSelectVO.getCode()))
                return true;
        }
        
        return false;
    }
    
    /**
     * 同步列至数据库
     * 
     * @param compareResults
     *            比较结果集
     * @param packageId
     *            包id
     * @return 是否成功
     */
    @RemoteMethod
    public String sysncColumn(List<TableCompareResult> compareResults, String packageId) {
        if (CollectionUtils.isEmpty(compareResults)) {
            throw new RuntimeException("compareResults can not be null...");
        }
        
        String pkgPath = getPackagePath(packageId);
        
        List<TableCompareResult> mergeResult = TableUtils.mergeColumn(compareResults);
        
        this.doGenerate(mergeResult, pkgPath);
        
        return "success";
    }
    
    /**
     * 同步索引至数据库
     * 
     * @param compareResults
     *            比较结果集
     * @param packageId
     *            包id
     * @return 是否成功
     */
    @RemoteMethod
    public String sysncIndex(List<TableCompareResult> compareResults, String packageId) {
        if (CollectionUtils.isEmpty(compareResults)) {
            throw new RuntimeException("compareResults can not be null...");
        }
        
        String pkgPath = getPackagePath(packageId);
        
        List<TableCompareResult> mergeResult = TableUtils.mergeIndex(compareResults);
        
        this.doGenerate(mergeResult, pkgPath);
        
        return "success";
    }
    
    /**
     * 执行生成SQL脚本和执行SQL功能
     * 
     * @param mergeResult
     *            合并后结果集
     * @param pkgPath
     *            包路径
     */
    public void doGenerate(List<TableCompareResult> mergeResult, String pkgPath) {
        for (TableCompareResult objResult : mergeResult) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("t", new WrapperTableIncrementSQL(objResult));
            
            String fileName = "[6]incrementTable_" + getTime() + "_" + objResult.getSrcTable().getCode() + ".sql";
            File srcFile = CapViewUtils.getFile(PreferenceConfigQueryUtil.getCodePath(), null, pkgPath, fileName);
            
            // 生成本地SQL文件
            GenerateCode.generateCode(param, getConfig(), this.getIncrementFtlTemple(), srcFile);
            
            // 获取SQL内容
            String sql = GenerateCode.getFtlContent(param, getConfig(), this.getIncrementFtlTemple());
            LOGGER.info("increment sql:" + sql);
            
            // 执行增量SQL
            SQLExecutorFactory.getInstance().executeIncrementSQL(sql);
        }
    }
    
    /**
     * 预览功能
     * 
     * @param columns
     *            列比较结果集
     * @param indexs
     *            索引比较结果集
     * @return SQL增量内容
     */
    @RemoteMethod
    public String preview(List<TableCompareResult> columns, List<TableCompareResult> indexs) {
        IBuilder builder = new IncrementSQLBuilder();
        
        List<TableCompareResult> columnsResults = TableUtils.mergeColumn(columns);
        for (TableCompareResult result : columnsResults) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("t", new WrapperTableIncrementSQL(result));
            String sql = GenerateCode.getFtlContent(param, getConfig(), this.getIncrementFtlTemple());
            builder.append(sql);
        }
        
        List<TableCompareResult> indexResults = TableUtils.mergeIndex(indexs);
        for (TableCompareResult result : indexResults) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("t", new WrapperTableIncrementSQL(result));
            String sql = GenerateCode.getFtlContent(param, getConfig(), this.getIncrementFtlTemple());
            builder.append(sql);
        }
        return builder.getSql();
    }
    
    /**
     * @param genCreateSQL
     *            全量结果集
     * @param genIncrementSQL
     *            增量结果集
     * @param modelIds
     *            元数据id集合
     */
    private void compareTable(List<TableCompareResult> genCreateSQL, List<TableCompareResult> genIncrementSQL, List<String> modelIds) {
        List<TableCompareResult> lstResult = tableFacade.compareMetadataToDB(modelIds);
        for (TableCompareResult result : lstResult) {
            // 对比结果无差异不执行SQL
            // 对比结果表不存在则执行全量SQL
            if (result.getResult() == TableCompareResult.TABLE_NOT_EXISTS) {
                genCreateSQL.add(result);
            }
            // 对比结果表有差异执行增量SQL
            if (result.getResult() == TableCompareResult.TABLE_DIFF) {
                genIncrementSQL.add(result);
            }
        }
    }
    
    /**
     * 执行全量创建表SQL
     * 
     * @param genCreateSQL
     *            全量结果集
     * @param pkgPath
     *            包路径
     * @return SQL
     */
    public List<String> getCreateTableSQL(List<TableCompareResult> genCreateSQL, String pkgPath) {
        List<String> sqls = new ArrayList<String>();
        for (TableCompareResult result : genCreateSQL) {
            TableVO newTable = result.getTargetTable();
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("t", new WrapperTableSQL(newTable));
            
            String fileName = "[1]createTable_" + newTable.getEngName() + ".sql";
            File srcFile = CapViewUtils.getFile(PreferenceConfigQueryUtil.getCodePath(), null, pkgPath, fileName);
            if (srcFile.exists()) {
                srcFile.delete();
            }
            GenerateCode.generateCode(param, getConfig(), this.getFtlTemple(), srcFile);
            // 获取SQL
            String sql = GenerateCode.getFtlContent(param, getConfig(), this.getFtlTemple());
            SQLExecutorFactory.getInstance().executeCreateTableSQL(sql);
            sqls.add(sql);
        }
        return sqls;
    }
    
    /**
     * 获取创建表SQL
     * 
     * @param tableVO
     *            表
     * @param pkgPath
     *            包路径
     * @return SQL
     */
    @RemoteMethod
    public String getCreateTableSQL(TableVO tableVO, String pkgPath) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("t", new WrapperTableSQL(tableVO));
        
        String fileName = "[1]createTable_" + tableVO.getEngName() + ".sql";
        File srcFile = CapViewUtils.getFile(PreferenceConfigQueryUtil.getCodePath(), null, pkgPath, fileName);
        if (srcFile.exists()) {
            srcFile.delete();
        }
        GenerateCode.generateCode(param, getConfig(), this.getFtlTemple(), srcFile);
        // 获取SQL
        return GenerateCode.getFtlContent(param, getConfig(), this.getFtlTemple());
    }
    
    /**
     * 执行全量SQL
     * 
     * @param sql
     *            执行SQL
     * @param packageId
     *            包id
     * @param tableVO
     *            表VO
     */
    @RemoteMethod
    public void executeCreateTable(String sql, String packageId, TableVO tableVO) {
        // 保存并同步实体
        try {
            tableFacade.saveAndSysncEntity(tableVO, packageId);
        } catch (ValidateException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (OperateException e) {
            LOGGER.error(e.getMessage(), e);
        }
        SQLExecutorFactory.getInstance().executeCreateTableSQL(sql);
    }
    
    /**
     * 获取ftl模板信息
     * 
     * @return 获取ftl模板信息
     */
    private String getFtlTemple() {
        String ftlName = "";
        if (DBType.ORACLE.equals(tableFacade.getDataBaseType())) {
            ftlName = "/common/capTableSQL_oracle.ftl";
        } else {
            ftlName = "/common/capTableSQL_mysql.ftl";
        }
        return ftlName;
    }
    
    /**
     * 获取ftl模板信息
     * 
     * @return 获取ftl模板信息
     */
    private String getIncrementFtlTemple() {
        String ftlName = "";
        if (DBType.ORACLE.equals(tableFacade.getDataBaseType())) {
            ftlName = "/common/capTableIncrementSQL_oracle.ftl";
        } else {
            ftlName = "/common/capTableIncrementSQL_mysql.ftl";
        }
        return ftlName;
    }
    
    /**
     * 获取Ftl配置对象
     * 
     * @return ftl配置对象
     */
    public Configuration getConfig() {
        GeneratorConfig config = ConfigFactory.getInstance().getDefaultConfig();
        Configuration objTemplateConfig = new Configuration();
        objTemplateConfig.setClassForTemplateLoading(ICodeGenerateConfig.class, config.getFtlRoot());
        objTemplateConfig.setDefaultEncoding("UTF-8"); // 这个一定要设置，不然在生成的页面中 会乱码
        return objTemplateConfig;
    }
    
    /**
     * 
     * 获取时间格式化，作为文件名使用
     * 
     * @return 时间字符串 yyyyMMddhhmmss
     */
    private String getTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(new Date());
    }
}
