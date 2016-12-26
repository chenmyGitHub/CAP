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


/**
 * 新版页面建模Action包装类
 *
 * @author 郑重
 * @version 2015-6-29 郑重
 */
public class WrapperJoddNewAction extends WrapperNewAction {
	
    @Override
    protected JSONArray createImportClassList(JSONObject jSONObject) {
    	JSONArray lstImportClass = new JSONArray();
        if (jSONObject.getJSONArray("pageAttributeVOList").size() > 0) {
            lstImportClass.add("com.comtop.cip.jodd.madvoc.meta.InOut");
        }
        if (jSONObject.getJSONArray("dataStoreVOList").getJSONObject(2).getJSONArray("verifyIdList").size() > 0) {
            lstImportClass.add("com.comtop.cip.jodd.madvoc.meta.InOut");
        }
        if (jSONObject.getJSONArray("dataStoreVOList").size() > 4) {
            lstImportClass.add("com.comtop.cip.jodd.madvoc.meta.InOut");
        }
        return lstImportClass;
    }

	@Override
	protected String getJspUrlSuffix() {
		return ".jsp";
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
            } else if ("object".equals(objDataStoreVO.getString("modelType"))) {
                // 获得实体的全路径，如com.comtop.user.UserVO
                String strFullEntityType = CapCodegenUtils.getFullClassNameByEntityId(entityId);
                lstImportClass.add(strFullEntityType);
                // 获得实体的名称，如UserVO
                String strShortEntityType = CapCodegenUtils.getClassNameByEntityId(entityId);
                objDataStoreVO.put("type", strShortEntityType);
                
            }
        }
        jSONObject.put("importClass", removeDuplicate((List) lstImportClass));
    }
	
}
