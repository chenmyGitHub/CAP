/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.dictionary.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.webapp.util.CapViewUtils;
import com.comtop.cip.jodd.io.FileUtil;
import com.comtop.cip.json.util.IOUtils;
import com.comtop.top.cfg.facade.ConfigClassifyFacade;
import com.comtop.top.cfg.facade.ConfigItemAssembler;
import com.comtop.top.cfg.facade.ConfigItemFacade;
import com.comtop.top.cfg.model.AttributeValueVO;
import com.comtop.top.cfg.model.ConfigClassifyDTO;
import com.comtop.top.cfg.model.ConfigItemVO;
import com.comtop.top.cfg.model.ConfigurationItemValueVO;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.StringUtil;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 数据字典操作Action
 * 
 * @author 林玉千
 * @since JDK1.6
 * @version 2016年9月26日 林玉千 新建
 */
@DwrProxy
public class DictionaryOperateAction {
    
    /** 日志 */
    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryOperateAction.class);
    
    /** top 配置Facade */
    private ConfigItemFacade configItemFacade = ((ConfigItemFacade) AppContext.getBean("configItemFacade"));
    
    /** top 配置configItemAssembler */
    private ConfigItemAssembler configItemAssembler = ((ConfigItemAssembler) AppContext.getBean("configItemAssembler"));
    
    /** top configClassifyFacade */
    private ConfigClassifyFacade configClassifyFacade = ((ConfigClassifyFacade) AppContext.getBean("configClassifyFacade"));
    
    /**
     * 生成创建表sql
     * 
     * @param fullcodes
     *            数据字典 全编码
     * @param pkgPath
     *            包路径
     * @return String
     */
    @RemoteMethod
    public String genCreateDicSQL(List<String> fullcodes, String pkgPath) {
        File srcFile = null;
        if (fullcodes != null) {
            for (int i = 0; i < fullcodes.size(); i++) {
                List<String> lstExportConfig = new ArrayList<String>();
                String strDicConfigCode = fullcodes.get(i);
                ConfigItemVO ojbIteam = null;
                ConfigClassifyDTO objClassify = null;
                ojbIteam = this.configItemAssembler.createVOByDTO(this.configItemFacade.queryBaseConfigItemVOByFullcode(strDicConfigCode));
                objClassify = this.configClassifyFacade.configCodeBelongsToClassify(strDicConfigCode);
                if (objClassify != null) {
                    this.appendRegisterSQL(lstExportConfig, objClassify, ojbIteam, strDicConfigCode);
                }
                String fileName = "[5]createDictionary" + strDicConfigCode + ".sql";
                srcFile = CapViewUtils.getFile(PreferenceConfigQueryUtil.getCodePath(), null, pkgPath, fileName);
                this.genSqlFile(srcFile, lstExportConfig);
            }
        }
        return srcFile != null ? srcFile.getAbsolutePath() : null;
    }
    
    /**
     * 生成文件并写入内容
     * 
     * @param file
     *            文件
     * @param lstExportConfig
     *            脚本
     */
    @SuppressWarnings("resource")
    private void genSqlFile(File file, List<String> lstExportConfig) {
        FileOutputStream objFileWriter = null;
        BufferedWriter objBuffererWriter = null;
        Exception ex = null;
        try {
            FileUtil.mkdirs(file.getParentFile().getPath());
            // 获取模版
            objFileWriter = new FileOutputStream(file);
            objBuffererWriter = new BufferedWriter(new OutputStreamWriter(objFileWriter, "UTF-8"));
            StringBuffer sbSQL = new StringBuffer();
            for (Iterator<String> iterator = lstExportConfig.iterator(); iterator.hasNext();) {
                String strSql = iterator.next();
                sbSQL.append(strSql);
                sbSQL.append("\n");
            }
            objBuffererWriter.write(sbSQL.toString());
            objBuffererWriter.flush();
        } catch (FileNotFoundException e) {
            ex = e;
        } catch (UnsupportedEncodingException e) {
            ex = e;
        } catch (IOException e) {
            ex = e;
        } finally {
            IOUtils.close(objFileWriter);
            IOUtils.close(objBuffererWriter);
        }
        if (ex != null) {
            LOGGER.error("生成字典项SQL出错.", ex);
            throw new RuntimeException("生成字典项SQL出错.", ex);
        }
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * @param lstExportConfig
     *            导出配置
     * @param classify
     *            类别
     * @param item
     *            配置vo
     * @param configFullCode
     *            配置code
     */
    private void appendRegisterSQL(List<String> lstExportConfig, ConfigClassifyDTO classify, ConfigItemVO item, String configFullCode) {
        String strClassifyCode = classify.getConfigClassifyFullCode();
        item.setConfigClassifyCode(strClassifyCode);
        item.setSysModule(classify.getSysModule());
        
        lstExportConfig.add(createDelAttrConfigSql(configFullCode));
        
        lstExportConfig.add(createDelAttrSql(configFullCode));
        
        lstExportConfig.add(createDelConfigSql(configFullCode));
        
        List lstClassify = this.configClassifyFacade.queryChainParentClassifyByCode(strClassifyCode);
        if ((lstClassify != null) && (lstClassify.size() > 0)) {
            String strPreViewCode = "";
            for (int i = lstClassify.size() - 1; i >= 0; --i) {
                if (i != lstClassify.size() - 1) {
                    strPreViewCode = ((ConfigClassifyDTO) lstClassify.get(i + 1)).getConfigClassifyFullCode();
                }
                lstExportConfig.add(createClassifySql((ConfigClassifyDTO) lstClassify.get(i), strPreViewCode));
            }
        }
        
        lstExportConfig.add(createConfigSql(item));
        
        if (!("5".equals(item.getConfigItemType()))) {
            lstExportConfig.add(createAttributeSql(item, null));
            lstExportConfig.add(createAttributeConfigSql(item, null, null));
        } else {
            Map objMap = createCollectionAttributeSql(item);
            lstExportConfig.addAll((Collection) objMap.get("attribute"));
            lstExportConfig.addAll((Collection) objMap.get("attributeConfig"));
        }
        
        lstExportConfig.add("COMMIT;");
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * @param classifyDTO
     *            配置分类
     * @param preViewCode
     *            code
     * @return 脚本
     */
    private String createClassifySql(ConfigClassifyDTO classifyDTO, String preViewCode) {
        StringBuffer objClassifyBuffer = new StringBuffer(150);
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("declare recordExistedCount number;");
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("begin");
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("      SELECT COUNT(1) into recordExistedCount FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='");
            
            objClassifyBuffer.append(classifyDTO.getConfigClassifyFullCode());
            objClassifyBuffer.append("';");
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("      if recordExistedCount  < 1 then");
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("          execute immediate ");
            objClassifyBuffer.append("\r\n");
            
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append("INSERT INTO TOP_CLASSIFY(CLASSIFY_ID,PARENT_CLASSIFY_ID,PARENT_CLASSIFY_TYPE,");
        objClassifyBuffer.append("CLASSIFY_NAME,NAME_FULL_PATH,CLASSIFY_CODE,SORT_NO,CLASSIFY_TYPE,");
        objClassifyBuffer.append("ISFLAG,DESCR,CREATOR_ID,CREATE_TIME,MODIFIER_ID,UPDATE_TIME)");
        if (this.configItemFacade.isMysql()) {
            objClassifyBuffer.append("select replace(uuid(),'-',''),");
        } else {
            objClassifyBuffer.append(" VALUES(");
            objClassifyBuffer.append("sys_guid(),");
        }
        
        if ("SYS_MODULE".equalsIgnoreCase(classifyDTO.getParentClassifyType())) {
            String strModuleCode = this.configClassifyFacade.queryModuleCodeByModuleId(classifyDTO.getParentConfigClassifyId());
            
            if (StringUtil.isNotBlank(strModuleCode)) {
                objClassifyBuffer.append("(");
                objClassifyBuffer.append("SELECT S.MODULE_ID FROM TOP_SYS_MODULE S WHERE S.MODULE_CODE = ");
                objClassifyBuffer.append("'");
                if (!(this.configItemFacade.isMysql())) {
                    objClassifyBuffer.append("'");
                }
                objClassifyBuffer.append(strModuleCode);
                objClassifyBuffer.append("'");
                if (!(this.configItemFacade.isMysql())) {
                    objClassifyBuffer.append("'");
                }
                objClassifyBuffer.append(" AND S.STATE = 1");
                objClassifyBuffer.append(")");
            } else {
                objClassifyBuffer.append("'");
                if (!(this.configItemFacade.isMysql())) {
                    objClassifyBuffer.append("'");
                }
                objClassifyBuffer.append(classifyDTO.getParentConfigClassifyId());
                objClassifyBuffer.append("'");
                if (!(this.configItemFacade.isMysql())) {
                    objClassifyBuffer.append("'");
                }
            }
            if (this.configItemFacade.isMysql())
                objClassifyBuffer.append(",'SYS_MODULE',");
            else
                objClassifyBuffer.append(",''SYS_MODULE'',");
        } else {
            objClassifyBuffer.append("(");
            objClassifyBuffer.append("SELECT C.CLASSIFY_ID FROM TOP_CLASSIFY C WHERE C.CLASSIFY_CODE = ");
            objClassifyBuffer.append("'");
            if (!(this.configItemFacade.isMysql())) {
                objClassifyBuffer.append("'");
            }
            objClassifyBuffer.append(preViewCode);
            objClassifyBuffer.append("'");
            if (!(this.configItemFacade.isMysql())) {
                objClassifyBuffer.append("'");
            }
            objClassifyBuffer.append(" AND C.CLASSIFY_TYPE = 1");
            objClassifyBuffer.append(")");
            if (this.configItemFacade.isMysql())
                objClassifyBuffer.append(",'UNI_CLASSIFY',");
            else {
                objClassifyBuffer.append(",''UNI_CLASSIFY'',");
            }
        }
        
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(classifyDTO.getConfigClassifyName());
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(",");
        if (this.configItemFacade.isMysql()) {
            objClassifyBuffer.append("' ',");
        } else {
            objClassifyBuffer.append("'' '',");
        }
        
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(classifyDTO.getConfigClassifyFullCode());
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        
        objClassifyBuffer.append(",0,1,1,");
        
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(trimString(classifyDTO.getConfigClassifyDescription()));
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(",");
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(trimString(classifyDTO.getCreatorId()));
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(",");
        if (this.configItemFacade.isMysql())
            objClassifyBuffer.append("now()");
        else {
            objClassifyBuffer.append("SYSDATE");
        }
        objClassifyBuffer.append(",");
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(trimString(classifyDTO.getModifierId()));
        objClassifyBuffer.append("'");
        if (!(this.configItemFacade.isMysql())) {
            objClassifyBuffer.append("'");
        }
        objClassifyBuffer.append(",");
        if (this.configItemFacade.isMysql()) {
            objClassifyBuffer.append("now()");
            objClassifyBuffer.append("  from dual where not exists(SELECT classify_id FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='");
            
            objClassifyBuffer.append(classifyDTO.getConfigClassifyFullCode());
            objClassifyBuffer.append("');");
        } else {
            objClassifyBuffer.append("SYSDATE");
            objClassifyBuffer.append(")");
            objClassifyBuffer.append("';");
            
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("      end if;  ");
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("end;  ");
            objClassifyBuffer.append("\r\n");
            objClassifyBuffer.append("/");
        }
        return objClassifyBuffer.toString();
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * 
     * @param configItemFullCode
     *            配置code
     * @return 脚本
     */
    private String createDelAttrConfigSql(String configItemFullCode) {
        StringBuffer objAttrConfigBuffer = new StringBuffer(200);
        objAttrConfigBuffer.append("DELETE FROM TOP_CFG_ATTRIBUTE_CONFIG  WHERE ATTRIBUTE_ID IN ");
        objAttrConfigBuffer.append("(SELECT A.ATTRIBUTE_ID FROM TOP_CFG_ATTRIBUTE A WHERE A.CONFIG_ID IN ");
        objAttrConfigBuffer.append("(SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = '");
        objAttrConfigBuffer.append(configItemFullCode);
        objAttrConfigBuffer.append("'));");
        return objAttrConfigBuffer.toString();
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * 
     * @param configItemFullCode
     *            配置code
     * @return 脚本
     */
    private String createDelAttrSql(String configItemFullCode) {
        StringBuffer objAttrBuffer = new StringBuffer(200);
        objAttrBuffer.append("DELETE FROM TOP_CFG_ATTRIBUTE WHERE CONFIG_ID IN ");
        objAttrBuffer.append("(SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = '");
        objAttrBuffer.append(configItemFullCode);
        objAttrBuffer.append("');");
        return objAttrBuffer.toString();
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * @param configItemFullCode
     *            配置code
     * @return 脚本
     */
    private String createDelConfigSql(String configItemFullCode) {
        StringBuffer objConfigBuffer = new StringBuffer(150);
        objConfigBuffer.append("DELETE FROM TOP_CFG_CONFIG  WHERE CONFIG_FULLCODE = '");
        objConfigBuffer.append(configItemFullCode);
        objConfigBuffer.append("';");
        return objConfigBuffer.toString();
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * @param vo
     *            配置vo
     * @return 脚本
     */
    private String createConfigSql(ConfigItemVO vo) {
        StringBuffer objConfigBuffer = new StringBuffer(500);
        objConfigBuffer.append("INSERT INTO TOP_CFG_CONFIG");
        objConfigBuffer.append("(CONFIG_ID,CONFIG_CLASSIFY_ID,CONFIG_FULLCODE,CONFIG_NAME,CONFIG_TYPE,CONFIG_DESCRIPTION,");
        
        objConfigBuffer.append("IS_VALID,SORT_NO,CLASSIFY_TYPE, CREATE_TIME,UPDATE_TIME,CREATOR_ID,MODIFIER_ID)");
        objConfigBuffer.append("VALUES(");
        if (this.configItemFacade.isMysql())
            objConfigBuffer.append("replace(uuid(),'-',''),");
        else {
            objConfigBuffer.append("sys_guid(),");
        }
        if ("Yes".equalsIgnoreCase(vo.getSysModule())) {
            objConfigBuffer.append("(SELECT SM.MODULE_ID FROM TOP_SYS_MODULE SM WHERE SM.MODULE_CODE = '");
            objConfigBuffer.append(vo.getConfigClassifyCode());
            objConfigBuffer.append("')");
        } else if ("No".equalsIgnoreCase(vo.getSysModule())) {
            objConfigBuffer.append("(SELECT TC.CLASSIFY_ID FROM TOP_CLASSIFY TC WHERE TC.CLASSIFY_CODE = '");
            objConfigBuffer.append(vo.getConfigClassifyCode());
            objConfigBuffer.append("')");
        } else {
            objConfigBuffer.append("'");
            objConfigBuffer.append(vo.getConfigClassifyId());
            objConfigBuffer.append("'");
        }
        objConfigBuffer.append(",'");
        objConfigBuffer.append(vo.getConfigItemFullCode());
        objConfigBuffer.append("','");
        objConfigBuffer.append(vo.getConfigItemName());
        objConfigBuffer.append("','");
        objConfigBuffer.append(vo.getConfigItemType());
        objConfigBuffer.append("','");
        objConfigBuffer.append(trimString(vo.getConfigItemDescription()));
        objConfigBuffer.append("',");
        objConfigBuffer.append(vo.getIsValid());
        objConfigBuffer.append(",");
        objConfigBuffer.append(0);
        objConfigBuffer.append(",'");
        objConfigBuffer.append(vo.getClassifyType());
        objConfigBuffer.append("',");
        if (this.configItemFacade.isMysql())
            objConfigBuffer.append("now()");
        else {
            objConfigBuffer.append("SYSDATE");
        }
        objConfigBuffer.append(",");
        if (this.configItemFacade.isMysql())
            objConfigBuffer.append("now()");
        else {
            objConfigBuffer.append("SYSDATE");
        }
        objConfigBuffer.append(",'");
        objConfigBuffer.append(trimString(vo.getCreatorId()));
        objConfigBuffer.append("','");
        objConfigBuffer.append(trimString(vo.getModifierId()));
        objConfigBuffer.append("');");
        return objConfigBuffer.toString();
    }
    
    /**
     *
     * @param param
     *            参数
     * @return 参数
     */
    private String trimString(String param) {
        if (StringUtil.isBlank(param)) {
            return "";
        }
        return param;
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * @param configItemVO
     *            配置vo
     * @param attributeValueVO
     *            变量vo
     * @return 脚本
     */
    private String createAttributeSql(ConfigItemVO configItemVO, AttributeValueVO attributeValueVO) {
        StringBuffer objAttributeBuffer = new StringBuffer(200);
        objAttributeBuffer.append("INSERT INTO TOP_CFG_ATTRIBUTE");
        objAttributeBuffer.append("(ATTRIBUTE_ID,CONFIG_ID,ATTRIBUTE_CODE,ATTRIBUTE_NAME,CONFIG_TYPE,SORT_NO,");
        objAttributeBuffer.append("CREATE_TIME,UPDATE_TIME,CREATOR_ID, MODIFIER_ID)");
        objAttributeBuffer.append("VALUES(");
        if (this.configItemFacade.isMysql())
            objAttributeBuffer.append("replace(uuid(),'-',''),");
        else {
            objAttributeBuffer.append("sys_guid(),");
        }
        objAttributeBuffer.append("(SELECT CONFIG_ID FROM TOP_CFG_CONFIG ITEM WHERE ITEM.CONFIG_FULLCODE='");
        objAttributeBuffer.append(configItemVO.getConfigItemFullCode());
        if (attributeValueVO != null) {
            objAttributeBuffer.append("'),'");
            objAttributeBuffer.append(attributeValueVO.getAttributeCode());
            objAttributeBuffer.append("','");
            objAttributeBuffer.append(attributeValueVO.getAttributeName());
            objAttributeBuffer.append("','");
            objAttributeBuffer.append(attributeValueVO.getAttributeTypeFlag());
            objAttributeBuffer.append("',");
            objAttributeBuffer.append(attributeValueVO.getColumnNumber());
            objAttributeBuffer.append(",");
        } else {
            objAttributeBuffer.append("'),'','','0',0,");
        }
        if (this.configItemFacade.isMysql())
            objAttributeBuffer.append("now()");
        else {
            objAttributeBuffer.append("SYSDATE");
        }
        objAttributeBuffer.append(",");
        if (this.configItemFacade.isMysql())
            objAttributeBuffer.append("now()");
        else {
            objAttributeBuffer.append("SYSDATE");
        }
        objAttributeBuffer.append(",'");
        objAttributeBuffer.append(trimString(configItemVO.getCreatorId()));
        objAttributeBuffer.append("','");
        objAttributeBuffer.append(trimString(configItemVO.getModifierId()));
        objAttributeBuffer.append("');");
        return objAttributeBuffer.toString();
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * 
     * @param configItemVO
     *            配置vo
     * @param attributeValueVO
     *            变量vo
     * @param configurationItemValueVO
     *            配置vo
     * @return 脚本
     */
    private String createAttributeConfigSql(ConfigItemVO configItemVO, AttributeValueVO attributeValueVO, ConfigurationItemValueVO configurationItemValueVO) {
        StringBuffer objAttributeConfigBuffer = new StringBuffer(200);
        objAttributeConfigBuffer.append("INSERT INTO TOP_CFG_ATTRIBUTE_CONFIG");
        objAttributeConfigBuffer.append("(ATTRIBUTE_CONFIG_ID,ATTRIBUTE_ID,IS_DEFALUT_VALUE,ATTRIBUTE_VALUE,SORT_NO,");
        objAttributeConfigBuffer.append("CREATE_TIME,CREATOR_ID,UPDATE_TIME,MODIFIER_ID)");
        objAttributeConfigBuffer.append("VALUES(");
        if (this.configItemFacade.isMysql())
            objAttributeConfigBuffer.append("replace(uuid(),'-',''),");
        else {
            objAttributeConfigBuffer.append("sys_guid(),");
        }
        objAttributeConfigBuffer.append("(SELECT a.attribute_id FROM top_cfg_config c,top_cfg_attribute a WHERE a.config_id =");
        
        objAttributeConfigBuffer.append("c.config_id AND c.config_fullcode = '");
        objAttributeConfigBuffer.append(configItemVO.getConfigItemFullCode());
        if (attributeValueVO != null) {
            objAttributeConfigBuffer.append("'");
            objAttributeConfigBuffer.append("  AND A.ATTRIBUTE_CODE = '");
            objAttributeConfigBuffer.append(attributeValueVO.getAttributeCode());
        }
        objAttributeConfigBuffer.append("'),");
        if (configurationItemValueVO != null) {
            objAttributeConfigBuffer.append(configurationItemValueVO.getIsDefaultValue());
            objAttributeConfigBuffer.append(",'");
            objAttributeConfigBuffer.append(trimString(configurationItemValueVO.getConfigItemValue()));
            objAttributeConfigBuffer.append("',");
            objAttributeConfigBuffer.append(configurationItemValueVO.getRowNumber());
            objAttributeConfigBuffer.append(",");
        } else {
            objAttributeConfigBuffer.append("1,'");
            objAttributeConfigBuffer.append(trimString(configItemVO.getConfigItemValue()));
            objAttributeConfigBuffer.append("',0,");
        }
        if (this.configItemFacade.isMysql())
            objAttributeConfigBuffer.append("now()");
        else {
            objAttributeConfigBuffer.append("SYSDATE");
        }
        objAttributeConfigBuffer.append(",'");
        objAttributeConfigBuffer.append(trimString(configItemVO.getCreatorId()));
        objAttributeConfigBuffer.append("',");
        if (this.configItemFacade.isMysql())
            objAttributeConfigBuffer.append("now()");
        else {
            objAttributeConfigBuffer.append("SYSDATE");
        }
        objAttributeConfigBuffer.append(",'");
        objAttributeConfigBuffer.append(trimString(configItemVO.getModifierId()));
        objAttributeConfigBuffer.append("');");
        return objAttributeConfigBuffer.toString();
    }
    
    /**
     * @see com.comtop.top.cfg.action.ConfigExportAction top平台未开放方法调用，进行重写
     * 
     * @param vo
     *            配置vo
     * @return Map集合
     */
    private Map<String, List<String>> createCollectionAttributeSql(ConfigItemVO vo) {
        Map objMap = new HashMap();
        
        List lstAttributeValueVO = vo.getLstAttributeValueVO();
        List lstArrAttributeSql = new ArrayList(lstAttributeValueVO.size());
        List lstAttributeConfigSql = new ArrayList();
        AttributeValueVO objAttributeValueVO = null;
        
        List lstConfigurationItemValueVO = null;
        ConfigurationItemValueVO objConfigurationItemValueVO = null;
        if (lstAttributeValueVO.size() > 0) {
            for (int i = 0; i < lstAttributeValueVO.size(); ++i) {
                objAttributeValueVO = (AttributeValueVO) lstAttributeValueVO.get(i);
                lstArrAttributeSql.add(createAttributeSql(vo, objAttributeValueVO));
                
                lstConfigurationItemValueVO = objAttributeValueVO.getLstConfigValueVO();
                if ((lstConfigurationItemValueVO != null) && (lstConfigurationItemValueVO.size() > 0)) {
                    for (int j = 0; j < lstConfigurationItemValueVO.size(); ++j) {
                        objConfigurationItemValueVO = (ConfigurationItemValueVO) lstConfigurationItemValueVO.get(j);
                        lstAttributeConfigSql.add(createAttributeConfigSql(vo, objAttributeValueVO, objConfigurationItemValueVO));
                    }
                }
            }
            
            objMap.put("attribute", lstArrAttributeSql);
            objMap.put("attributeConfig", lstAttributeConfigSql);
        }
        return objMap;
    }
}
