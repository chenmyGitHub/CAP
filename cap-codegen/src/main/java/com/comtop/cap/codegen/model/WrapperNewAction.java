/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.entity.facade.EntityFacade;
import com.comtop.cap.bm.metadata.entity.model.AttributeSourceType;
import com.comtop.cap.bm.metadata.entity.model.EntityAttributeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.page.PageUtil;
import com.comtop.cap.bm.metadata.page.desinger.model.PageVO;
import com.comtop.cap.codegen.generate.IWrapper;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cip.json.JSON;
import com.comtop.cip.json.JSONArray;
import com.comtop.cip.json.JSONObject;
import com.comtop.corm.resource.util.CollectionUtils;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.sys.accesscontrol.func.facade.FuncFacade;
import com.comtop.top.sys.accesscontrol.func.facade.IFuncFacade;
import com.comtop.top.sys.accesscontrol.func.model.FuncDTO;

/**
 * 新版页面建模Action包装类
 *
 * @author 郑重
 * @version 2015-6-29 郑重
 */
public abstract class WrapperNewAction implements Observer,IWrapper<PageVO> {
    
    /** 日志 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(WrapperNewAction.class);
    
    /**
     * 页面/菜单 facade
     */
    protected IFuncFacade funcFacade = AppContext.getBean(FuncFacade.class);
    
    /**
     * 实体 facade
     */
    protected EntityFacade entityFacade = AppContext.getBean(EntityFacade.class);
    
    @Override
    public void update(Observable o, Object arg) {
        // TODO: 后续处理
    }
    
    /**
     * 页面包装器
     *
     * @param pageVO 界面对象
     * @return 包装后的参数Map
     */
    @Override
	public Map<String, Object> wrapper(PageVO pageVO) {
        JSONObject objJSONObject = (JSONObject) JSON.toJSON(pageVO);
        //基于spring 和 基于jodd的不同框架时，pageinit方法的注解上url的后缀不一样
        objJSONObject.put("url", PageUtil.getPageUrl(pageVO));
        wrapperJavadoc(objJSONObject);
        wrapperJspURL(objJSONObject);
        wrapperRight(objJSONObject);
        wrapperImportClass(objJSONObject);
        wrapperDataStoreVO(objJSONObject);
        wrapperDicsOrEnums(objJSONObject);
        return objJSONObject;
    }
    
    /**
     * 包装访问URL
     *
     * @param jSONObject 界面内容JSON对象
     */
    protected void wrapperJspURL(JSONObject jSONObject) {
        String strPackageName = jSONObject.getString("modelPackage");
        strPackageName = PageUtil.getPageFilePath(strPackageName);
        String strModelName = jSONObject.getString("modelName");
        jSONObject.put("jspURL", strPackageName + strModelName + getJspUrlSuffix());
    }
    
    /**
     * 返回action跳转jsp的后缀
     * @return 字符串
     */
    abstract protected String getJspUrlSuffix();
    
    /**
     * 包装操作权限
     *
     * @param jSONObject 界面内容JSON对象
     */
    private void wrapperRight(JSONObject jSONObject) {
        JSONArray lstDataStoreVOList = jSONObject.getJSONArray("dataStoreVOList");
        JSONObject objDataStoreVO = lstDataStoreVOList.getJSONObject(2);
        JSONArray lstVerifyIdList = objDataStoreVO.getJSONArray("verifyIdList");
        for (int i = 0; i < lstVerifyIdList.size(); i++) {
            JSONObject objRightVO = lstVerifyIdList.getJSONObject(i);
            FuncDTO objFuncVO = funcFacade.readFuncVO(objRightVO.getString("funcId"));
            FuncDTO objParentFuncVO = funcFacade.readFuncVO(objFuncVO.getParentFuncId());
            objRightVO.put("parentFuncCode", objParentFuncVO.getFuncCode());
        }
    }
    
    /**
     * 取出重复数据
     *
     * @param list 集合
     * @return 集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected List<String> removeDuplicate(List<String> list) {
        Set objSet = new HashSet();
        List lstResult = new ArrayList();
        for (Iterator objIter = list.iterator(); objIter.hasNext();) {
            Object objElement = objIter.next();
            if (objSet.add(objElement)) {
                lstResult.add(objElement);
            }
        }
        return lstResult;
    }
    
    /**
     * 包装导入类
     *
     * @param jSONObject pageVO JSON
     */
    protected void wrapperImportClass(JSONObject jSONObject){
    	JSONArray lstImportClass = createImportClassList(jSONObject);
    	jSONObject.put("importClass", removeDuplicate((List)lstImportClass));
    }
    
    /**
     * 获取
     * @param jSONObject pageVO生成的json对象
     * @return 创建imortClass的集合
     */
    abstract protected JSONArray createImportClassList(JSONObject jSONObject);
    
    /**
     * 包装数据集,设置数据集的定义的属性类型
     *
     * @param jSONObject pageVO JSON
     */
    abstract protected void wrapperDataStoreVO(JSONObject jSONObject) ;
    
    /**
     * 根据数据集中关联实体属性，包装数据字典
     *
     * @param jSONObject pageVO JSON
     */
    protected void wrapperDicsOrEnums(JSONObject jSONObject) {
        // 获取页面的所有数据集
        JSONArray lstDataStoreVOList = jSONObject.getJSONArray("dataStoreVOList");
        Map<String, List<String>> objDicMap = new HashMap<String, List<String>>();
        Map<String, List<String>> objEnumMap = new HashMap<String, List<String>>();
        for (int i = 0; i < lstDataStoreVOList.size(); i++) {
            JSONObject objDataStoreVO = lstDataStoreVOList.getJSONObject(i);
            
            // 如果数据集类型为集合或对象
            if ("list".equals(objDataStoreVO.getString("modelType"))
                || "object".equals(objDataStoreVO.getString("modelType"))) {
                
                // 获得数据集中关联的实体
                JSONObject objEntityVO = objDataStoreVO.getJSONObject("entityVO");
                // 获得实体中的所有属性
                JSONArray lstAttributes = objEntityVO.getJSONArray("attributes");
                if (!CollectionUtils.isEmpty(lstAttributes)) {
                    // 遍历实体所有属性
                    for (int j = 0; j < lstAttributes.size(); j++) {
                        JSONObject objEntityAttributeVO = lstAttributes.getJSONObject(j);
                        addDicOrEnumKey(objEntityAttributeVO, objDicMap, objEnumMap);
                        addSubEntityDicOrEnum(objEntityVO.getJSONArray("lstRelation"), objEntityAttributeVO, objDicMap,
                            objEnumMap);
                    }
                }
            }
        }
        
        // 设置数据字典的数据
        setDicsOrEnumsData(jSONObject, objDicMap, true);
        // 设置枚举的数据
        setDicsOrEnumsData(jSONObject, objEnumMap, false);
        
        // 处理import的class
        extraWrapperImportClass(jSONObject);
    }
    
    /**
     * 设置数据字典或枚举数据
     * 
     * @param jSONObject 设置的目标对象
     * @param dataMap 设置的数据
     * @param flag true:表示处理数据字典；false:表示处理枚举
     */
    private void setDicsOrEnumsData(JSONObject jSONObject, Map<String, List<String>> dataMap, boolean flag) {
        Set<Map.Entry<String, List<String>>> objSet = dataMap.entrySet();
        Iterator<Map.Entry<String, List<String>>> objIterator = objSet.iterator();
        String strLstCode = "";
        String strLstAttrs = "";
        while (objIterator.hasNext()) {
            Map.Entry<String, List<String>> objEntry = objIterator.next();
            strLstCode += "\"" + objEntry.getKey() + "\"" + ",";
            List<String> lstAttrs = removeDuplicate(objEntry.getValue());
            String strAttrNames = "";
            for (int i = 0; i < lstAttrs.size(); i++) {
                String strAttr = lstAttrs.get(i);
                strAttrNames += "\"" + strAttr + "\"" + ",";
            }
            strAttrNames = strAttrNames.substring(0, strAttrNames.length() - 1);
            strLstAttrs += "java.util.Arrays.asList(" + strAttrNames + ")" + ",";
        }
        
        strLstCode = strLstCode.length() > 0 ? strLstCode.substring(0, strLstCode.length() - 1) : strLstCode;
        strLstAttrs = strLstAttrs.length() > 0 ? strLstAttrs.substring(0, strLstAttrs.length() - 1) : strLstAttrs;
        
        if (flag) {
            jSONObject.put("dicCodes", strLstCode);
            jSONObject.put("dicAttrs", strLstAttrs);
        } else {
            jSONObject.put("eumCodes", strLstCode);
            jSONObject.put("enumAttrs", strLstAttrs);
        }
        
    }
    
    /**
     * 处理import的class
     *
     * @param jSONObject pageVO JSON
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void extraWrapperImportClass(JSONObject jSONObject) {
        JSONArray lstImportClass = jSONObject.getJSONArray("importClass");
        lstImportClass.add("java.util.List");
        lstImportClass.add("java.util.Arrays");
        jSONObject.put("importClass", removeDuplicate((List) lstImportClass));
    }
    
    /**
     * 添加字典Key
     *
     * @param entityAttributeVO 实体属性
     * @param dicMap 字典Map
     * @param enumMap 枚举Map
     */
    private void addDicOrEnumKey(JSONObject entityAttributeVO, Map<String, List<String>> dicMap,
        Map<String, List<String>> enumMap) {
        JSONObject dataTypeVO = entityAttributeVO.getJSONObject("attributeType");
        // 获取属性的来源
        String source = dataTypeVO.getString("source");
        
        Map<String, List<String>> map = null;
        
        if (StringUtil.isNotBlank(source)
            && (AttributeSourceType.DATA_DICTIONARY.getValue().equals(source) || AttributeSourceType.ENUM_TYPE
                .getValue().equals(source))) {
            
            // 根据属性来源为map赋值
            if (AttributeSourceType.DATA_DICTIONARY.getValue().equals(source)) {
                map = dicMap;
            } else {
                map = enumMap;
            }
            
            // 根据数据字典的key取得数据字典的value
            String dicOrEnumKey = dataTypeVO.getString("value");
            List<String> lstAttrs = map.get(dicOrEnumKey);
            if (lstAttrs == null) {
                lstAttrs = new ArrayList<String>();
                lstAttrs.add(entityAttributeVO.getString("engName"));
                map.put(dicOrEnumKey, lstAttrs);
            } else {
                lstAttrs.add(entityAttributeVO.getString("engName"));
            }
            
        }
        
    }
    
    /**
     * 添加级联实体属性的数据字典Key
     * 
     * @param lstRelationVO 实体的关系VO集合
     * @param entityAttributeVO 实体属性
     * @param dicMap 字典Map
     * @param enumMap 枚举Map
     */
    private void addSubEntityDicOrEnum(JSONArray lstRelationVO, JSONObject entityAttributeVO,
        Map<String, List<String>> dicMap, Map<String, List<String>> enumMap) {
        // 取得属性的关系Id
        String relationId = entityAttributeVO.getString("relationId");
        
        if (StringUtil.isNotBlank(relationId) && !CollectionUtils.isEmpty(lstRelationVO)) {
            for (int i = 0, len = lstRelationVO.size(); i < len; i++) {
                JSONObject relationVO = lstRelationVO.getJSONObject(i);
                if (relationId.equals(relationVO.getString("relationId"))) {
                    // 取得目标实体的ID
                    String targetEntityId = relationVO.getString("targetEntityId");
                    // 取得目标实体
                    EntityVO targetEntityVO = entityFacade.loadEntity(targetEntityId, null);
                    // 取得目标所有属性
                    List<EntityAttributeVO> attributes = targetEntityVO.getAttributes();
                    if (!CollectionUtils.isEmpty(attributes)) {
                        // 遍历目标所有属性
                        for (EntityAttributeVO _entityAttributeVO : attributes) {
                            addDicOrEnumKey((JSONObject) JSON.toJSON(_entityAttributeVO), dicMap, enumMap);
                        }
                    }
                    
                    break;
                }
            }
        }
    }
    
    /**
     * 页面头信息
     *
     * @param objJSONObject pageVO JSON对象
     */
    private void wrapperJavadoc(JSONObject objJSONObject) {
        CapLoginVO objCapLoginVO = CapLoginUtil.getCapCurrentUserSession();
        if (null == objCapLoginVO) {
            throw new RuntimeException("未能从Session中获取当前登录用户的信息，可能是Session已失效，请重新登录后，再尝试执行代码生成");
        }
        objJSONObject.put("author", objCapLoginVO.getBmEmployeeName());
    }

	@Override
	public Map<String, Object> wrapper(PageVO entity, String format) {
		// TODO Auto-generated method stub
		return null;
	}
}
