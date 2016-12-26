/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.metadata.preferencesconfig.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.common.storage.exception.ValidateException;
import com.comtop.cap.bm.metadata.database.util.DBUtils;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.metadata.preferencesconfig.model.PreferenceConfigVO;
import com.comtop.cap.bm.metadata.preferencesconfig.model.PreferencesFileVO;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 通过XPath获取首选项配置信息
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年5月27日 许畅 新建
 */
@DwrProxy
public class PreferenceConfigAction extends AbstractPreferenceConfig {
    
    /** 日志 */
    private final static Logger LOGGER = LoggerFactory.getLogger(PreferenceConfigAction.class);
    
    /** 成功标识 */
    private final static String SUCCESS = "success";
    
    /**
     * 默认加载 Preferences.preferenceConfig.xml 中配置信息 <br>
     * 如果存在CustomPreferences.preferenceConfig.xml 则加载此xml首选项信息
     * 
     * @return 首选项中的配置信息
     */
    @Override
    @RemoteMethod
    public Map<String, String> loadPreferenceConfig() {
        PreferencesFileVO objFileVO = getCustomPreferencesFileVO();
        
        return convertSubConfigToData(objFileVO);
    }
    
    /**
     * 加载默认配置信息
     * 
     * @return 首选项中的配置信息
     */
    @Override
    @RemoteMethod
    public Map<String, String> loadDefaultPreferenceConfig() {
        PreferencesFileVO objFileVO = getDefaultPreferencesFileVO();
        
        return convertSubConfigToData(objFileVO);
    }
    
    /**
     * 保存首选项配置信息
     * 
     * @param map
     *            配置信息集合
     * @return string
     */
    @Override
    @RemoteMethod
    public String savePreferenceConfig(Map<String, String> map) {
        PreferencesFileVO objFileVO = getCustomPreferencesFileVO();
        try {
            Set<String> keys = map.keySet();
            PreferenceConfigVO configVO = new PreferenceConfigVO();
            for (String key : keys) {
                PreferenceConfigVO copyObj = configVO.clone();
                copyObj.setConfigKey(key);
                copyObj.setConfigValue(map.get(key));
                String strPath = "./subConfig[configKey='" + copyObj.getConfigKey() + "']";
                if (objFileVO.query(strPath) == null) {
                    objFileVO.add("./subConfig", copyObj);
                } else {
                    objFileVO.update(strPath, copyObj);
                }
            }
        } catch (OperateException e) {
            LOGGER.error("更新首选项配置出错，配置：" + e.getMessage(), e);
            return e.getMessage();
        } catch (ValidateException e) {
            LOGGER.error("更新首选项配置出错，配置：" + e.getMessage(), e);
            return e.getMessage();
        }
        return SUCCESS;
    }
    
    /**
     * 删除首选项配置信息
     * 
     * @param map
     *            配置信息集合
     * @return boolean
     */
    @Override
    @RemoteMethod
    public boolean deletePreferenceConfig(Map<String, String> map) {
        PreferencesFileVO objFileVO = getCustomPreferencesFileVO();
        try {
            Set<String> keys = map.keySet();
            for (String key : keys) {
                String strPath = "./subConfig[configKey='" + key + "']";
                objFileVO.delete(strPath);
            }
        } catch (OperateException e) {
            LOGGER.error("更新首选项配置出错，配置：" + e.getMessage(), e);
            return false;
        } catch (ValidateException e) {
            LOGGER.error("更新首选项配置出错，配置：" + e.getMessage(), e);
            return false;
        }
        return true;
    }
    
    /**
     * xml子节点转为具体数据
     * 
     * @param objFileVO
     *            PreferencesFileVO
     * @return map
     */
    private Map<String, String> convertSubConfigToData(PreferencesFileVO objFileVO) {
        Map<String, String> map = new HashMap<String, String>();
        List<PreferenceConfigVO> configs = objFileVO.getSubConfig();
        for (PreferenceConfigVO configVO : configs) {
            map.put(configVO.getConfigKey(), configVO.getConfigValue());
        }
        return map;
    }
    
    /**
     * 测试数据库链接(暂只支持oracle数据库)
     * 
     * @param driver 数据库驱动
     * @param url 链接的目标地址
     * @param user 用户名
     * @param password 密码
     * 
     * @return 返回是否能够连通
     */
    @RemoteMethod
    public boolean testDBConnection(String driver, String url, String user, String password) {
        return DBUtils.isConnection(driver, url, user, password);
    }
    
    /**
     * 获取首选项工程目录
     * 
     * 
     * @return 工程目录
     */
    @RemoteMethod
    public String getProjectDir() {
        return PreferenceConfigQueryUtil.getCodePath();
    }
}
