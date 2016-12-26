/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.List;

import com.comtop.cap.codegen.util.CapCodegenUtils;
import com.comtop.cip.json.JSONArray;
import com.comtop.cip.json.JSONObject;
import com.comtop.top.core.util.StringUtil;


/**
 * SpringMVC新版页面建模Action包装类
 *
 * @author 罗珍明
 * @version 2015-6-29 罗珍明
 */
public class WrapperSpringNewAction extends WrapperNewAction{
	
	@Override
	protected String getJspUrlSuffix() {
		return "";
	}
	
	@Override
	protected void wrapperDicsOrEnums(JSONObject jSONObject) {
		// TODO Auto-generated method stub
		super.wrapperDicsOrEnums(jSONObject);
		JSONArray lstImportClass = jSONObject.getJSONArray("importClass");
        lstImportClass.add("java.util.Map");
        jSONObject.put("importClass", removeDuplicate((List) lstImportClass));
	}
	
	@Override
	protected JSONArray createImportClassList(JSONObject jSONObject) {
		JSONArray lstImportClass = new JSONArray();
		JSONArray lstDataStoreVOList = jSONObject.getJSONArray("dataStoreVOList");
		for (int i = 0; i < lstDataStoreVOList.size(); i++) {
			JSONObject objDataStoreVO = lstDataStoreVOList.getJSONObject(i);
			if("object".equals(objDataStoreVO.getString("modelType"))||"list".endsWith(objDataStoreVO.getString("modelType"))){
				if(!objDataStoreVO.getBooleanValue("saveToSession")){
					lstImportClass.add("com.comtop.cap.runtime.base.annotation.SpringRequestAttribute");
					break;
				}
			}
		}
		return lstImportClass;
	}
    
    /**
     * 包装数据集,设置数据集的定义的属性类型
     *
     * @param jSONObject pageVO JSON
     */
    @Override
	protected void wrapperDataStoreVO(JSONObject jSONObject) {
        JSONArray lstDataStoreVOList = jSONObject.getJSONArray("dataStoreVOList");
        JSONArray lstImportClass = jSONObject.getJSONArray("importClass");
        String sessionKeys = "";
        String sessionTypes = "";
        for (int i = 0; i < lstDataStoreVOList.size(); i++) {
            JSONObject objDataStoreVO = lstDataStoreVOList.getJSONObject(i);
            String entityId = objDataStoreVO.getString("entityId");
            
            if ("list".equals(objDataStoreVO.getString("modelType"))) {
                // 获得实体的全路径，如com.comtop.user.UserVO
                String strFullEntityType = CapCodegenUtils.getFullClassNameByEntityId(entityId);
                lstImportClass.add(strFullEntityType);
                // 添加java.util.List到import集合中
                lstImportClass.add("java.util.List");
                // 获得实体的名称，如UserVO
                String strShortEntityType = CapCodegenUtils.getClassNameByEntityId(entityId);
                objDataStoreVO.put("type", "List<" + strShortEntityType + ">");
                if(saveDataStorSaveToSession(objDataStoreVO)){
                	sessionKeys += "\""+objDataStoreVO.getString("ename") +"\""+ ",";
                	sessionTypes += "List.class"+",";
                }
            } else if ("object".equals(objDataStoreVO.getString("modelType"))) {
                // 获得实体的全路径，如com.comtop.user.UserVO
                String strFullEntityType = CapCodegenUtils.getFullClassNameByEntityId(entityId);
                lstImportClass.add(strFullEntityType);
                // 获得实体的名称，如UserVO
                String strShortEntityType = CapCodegenUtils.getClassNameByEntityId(entityId);
                objDataStoreVO.put("type", strShortEntityType);
                if(saveDataStorSaveToSession(objDataStoreVO)){
                	sessionKeys += "\""+objDataStoreVO.getString("ename") +"\""+ ",";
                	sessionTypes += strShortEntityType+".class"+",";
                }
            }
        }
        if (sessionKeys.length() > 0) {
        	sessionKeys = sessionKeys.substring(0, sessionKeys.length() - 1);
        	sessionTypes = sessionTypes.substring(0, sessionTypes.length() - 1);
        }
        jSONObject.put("importClass", removeDuplicate((List) lstImportClass));
        jSONObject.put("sessionKeys", sessionKeys);
        jSONObject.put("sessionTypes", sessionTypes);
    }

    /**
     * 数据集是否保存到session中
     * @param objDataStoreVO 数据集
     * @return true保存到session false不保存
     */
	private boolean saveDataStorSaveToSession(JSONObject objDataStoreVO) {
		Object value = objDataStoreVO.get("saveToSession");
		boolean flag = false;
        if (value instanceof String && StringUtil.isNotBlank((String) value)) {
            flag = Boolean.valueOf((String) value);
        } else if (value instanceof Boolean) {
            flag = (Boolean) value;
        }
        return flag;
	}
}
