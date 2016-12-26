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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.dwr.CapMap;
import com.comtop.cap.bm.metadata.common.storage.exception.OperateException;
import com.comtop.cap.bm.metadata.page.desinger.model.PageVO;
import com.comtop.cap.bm.metadata.page.preference.facade.ComponentDependFilesConfigFacade;
import com.comtop.cap.bm.metadata.page.preference.model.ComponentDependFilesConfigVO;
import com.comtop.cap.bm.metadata.page.uilibrary.facade.ComponentFacade;
import com.comtop.cap.bm.metadata.page.uilibrary.model.ComponentVO;
import com.comtop.cap.codegen.generate.IWrapper;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cip.json.JSON;
import com.comtop.cip.json.JSONArray;
import com.comtop.cip.json.JSONObject;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.StringUtil;
import com.comtop.top.core.util.constant.NumberConstant;

/**
 * 新版页面建模JSP包装类
 * 
 * @author 郑重
 * @version 2015-6-23 郑重
 */
public class WrapperNewPage implements Observer, IWrapper<PageVO> {
    
    /** 日志 */
    protected static final Logger LOGGER = LoggerFactory.getLogger(WrapperNewPage.class);
    
    /**
     * 页面/菜单 facade
     */
    protected ComponentFacade componentFacade = AppContext.getBean(ComponentFacade.class);
    
    /**
     * 控件依赖文件配置 facade
     */
    protected ComponentDependFilesConfigFacade componentDependFilesConfigFacade = AppContext
        .getBean(ComponentDependFilesConfigFacade.class);
    
    /**
     * 页面所有控件的集合
     */
    private final List<JSONObject> layoutList = new ArrayList<JSONObject>();
    
    /**
     * 页面所有控件的Map，key为layout.id
     */
    private final Map<String, JSONObject> layoutMap = new HashMap<String, JSONObject>();
    
    /**
     * 控件属性的类型为Json的属性集合
     */
    private final Map<String, Map<String, Map<String, Boolean>>> allUIJsonTypeAttrs;
    
    /**
     * 运行时可用UI控件属性
     */
    private final Map<String, Map<String, List<String>>> runtimeAvailableAttrs;
    
    @Override
    public void update(Observable o, Object arg) {
        //
    }
    
    /**
     * 页面包装器
     * 
     * @param pageVO 页面对象
     * @return 包装Map
     */
    @Override
    public Map<String, Object> wrapper(PageVO pageVO) {
        JSONObject objJSONObject = (JSONObject) JSON.toJSON(pageVO);
        wrapperJavadoc(objJSONObject);
        wrapperLayout(objJSONObject);
        wrapperIncludeFile(objJSONObject);
        wrapperExpression(objJSONObject);
        wrapperSessionAttributeKey(objJSONObject);
        return objJSONObject;
    }
    
    /**
     * 
     * 构造函数
     */
    public WrapperNewPage() {
        runtimeAvailableAttrs = componentFacade.queryRuntimeAvailableAttrs();
        allUIJsonTypeAttrs = componentFacade.queryListByProJsonType();
    }
    
    /**
     * 包装缓存到session中的数据模型的key
     * 
     * @param objJSONObject pageVO JSON对象
     */
    private void wrapperSessionAttributeKey(JSONObject objJSONObject) {
        JSONArray dataStoreArray = (JSONArray) objJSONObject.get("dataStoreVOList");
        if (null == dataStoreArray || dataStoreArray.size() == 0) {
            return;
        }
        // 保存到session的key（数据模型保存到session，key即数据模型的名称）
        String sessionKeys = "";
        for (Object obj : dataStoreArray) {
            JSONObject jsonObj = (JSONObject) obj;
            Object value = jsonObj.get("saveToSession");
            boolean flag = false;
            if (value instanceof String && StringUtil.isNotBlank((String) value)) {
                flag = Boolean.valueOf((String) value);
            } else if (value instanceof Boolean) {
                flag = (Boolean) value;
            }
            if (flag) {
                sessionKeys += jsonObj.getString("ename") + ",";
            }
        }
        if (sessionKeys.length() > 0) {
            sessionKeys = sessionKeys.substring(0, sessionKeys.length() - 1);
            
            // 存在数据库保存到session中，则页面需要引入/cap/dwr/interface/CapSessionAttributeUtil.js
            List<String> jsFileList = (List<String>) objJSONObject.get("JSFileList");
            jsFileList.add("/cap/dwr/interface/CapSessionAttributeUtil.js");
            objJSONObject.put("JSFileList", jsFileList);
            
        }
        objJSONObject.put("sessionKeys", sessionKeys);
    }
    
    /**
     * 页面头信息
     * 
     * @param objJSONObject pageVO JSON对象
     */
    public void wrapperJavadoc(JSONObject objJSONObject) {
        CapLoginVO objCapLoginVO = CapLoginUtil.getCapCurrentUserSession();
        if (null == objCapLoginVO) {
            throw new RuntimeException("未能从Session中获取当前登录用户的信息，可能是Session已失效，请重新登录后，再尝试执行代码生成");
        }
        objJSONObject.put("author", objCapLoginVO.getBmEmployeeName());
    }
    
    /**
     * 遍历layout 处理UI控件的Option
     * 
     * @param objJSONObject pageVO JSON对象
     */
    public void wrapperLayout(JSONObject objJSONObject) {
        Map<String, ComponentVO> objComponentVOMap;
        try {
            objComponentVOMap = componentFacade.queryList();
        } catch (OperateException e) {
            throw new RuntimeException("获取全部组件出错，可能组件文件错误，刷新工程之后重启服务，再尝试执行代码生成", e);
        }
        StringBuffer objUIConfig = new StringBuffer();
        objUIConfig.append("{\n");
        objUIConfig.append(wrapperLayout(objJSONObject.getJSONObject("layoutVO").getJSONArray("children"),
            objComponentVOMap));
        if (objUIConfig.length() > 2) {
            objUIConfig = new StringBuffer(objUIConfig.substring(0, objUIConfig.length() - 2));
        }
        objUIConfig.append("\n}\n");
        objJSONObject.getJSONObject("layoutVO").put("uiConfig", objUIConfig.toString());
    }
    
    /**
     * 递归处理layout对象
     * 
     * @param lstLayout 布局集合
     * @param objComponentVOMap 组件配置集合
     * @return UIConfig
     */
    private StringBuffer wrapperLayout(JSONArray lstLayout, Map<String, ComponentVO> objComponentVOMap) {
        StringBuffer objUIConfig = new StringBuffer();
        if (lstLayout != null) {
            for (int i = 0; i < lstLayout.size(); i++) {
                JSONObject objLayout = lstLayout.getJSONObject(i);
                // 构建layout List和Map缓存
                layoutList.add(objLayout);
                layoutMap.put(objLayout.getString("id"), objLayout);
                objLayout.put("uiConfigId", objLayout.getString("id"));
                if ("layout".equals(objLayout.getString("type"))) {
                    // 如果是TD则生将options转换成style
                    if ("td".equals(objLayout.getString("uiType"))) {
                        setTdStyle(objLayout);
                    }
                    objUIConfig.append(wrapperLayout(objLayout.getJSONArray("children"), objComponentVOMap));
                } else {
                    JSONObject objOptions = objLayout.getJSONObject("options");
                    if (objOptions.getString("id") != null) {
                        objLayout.put("uiConfigId", objOptions.getString("id"));
                    }
                    objUIConfig.append(buildUIConfigString(objLayout, objOptions, objComponentVOMap));
                }
            }
        }
        return objUIConfig;
    }
    
    /**
     * 构建UIConfig 字符串
     * 
     * @param objLayout 控件json对象
     * @param options 控件属性
     * @param objComponentVOMap 组件集合
     * @return UICONFIG字符串
     */
    private String buildUIConfigString(JSONObject objLayout, JSONObject options,
        Map<String, ComponentVO> objComponentVOMap) {
        Map<String, Boolean> objCurrUIJsonKey = allUIJsonTypeAttrs.get("modelId").get(
            objLayout.getString("componentModelId"));
        List<String> lstCurrUISelfAttr = runtimeAvailableAttrs.get("modelId").get(
            objLayout.getString("componentModelId"));
        String strUiConfigId = objLayout.getString("uiConfigId");
        // 特殊处理CheckboxGroup、RadioGroup以及PullDown的数据源属性（当它们使用了枚举取数据字典时）
        checkboxAndRadioProcess(options);
        
        String strResult = "    \"" + strUiConfigId + "\"" + ":{\n";
        Set<String> objKeySet = options.keySet();
        Iterator<String> objIterator = objKeySet.iterator();
        while (objIterator.hasNext()) {
            String strKey = objIterator.next();
            if (StringUtil.isNotEmpty(options.getString(strKey)) && lstCurrUISelfAttr.contains(strKey)) {
                strResult += "        \"" + strKey + "\"" + ":";
                if (options.get(strKey) instanceof String) {
                    if (objCurrUIJsonKey.get(strKey) != null && objCurrUIJsonKey.get(strKey)) {
                        strResult += options.getString(strKey);
                    } else {
                        if ("uitype".equals(strKey)) {
                            String strComponentModelId = objLayout.getString("componentModelId");
                            ComponentVO objComponentVO = objComponentVOMap.get(strComponentModelId);
                            strResult += "\"" + objComponentVO.getModelName() + "\"";
                        } else {
                            strResult += "\"" + options.getString(strKey) + "\"";
                        }
                    }
                } else {
                    if ("edittype".equals(strKey)) { // 如果是editgrid的edittype属性，则进行一下处理
                        strResult += processEdittype4Editgrid(options.getString(strKey));
                    } else {
                        if ("uitype".equals(strKey)) {
                            String strComponentModelId = objLayout.getString("componentModelId");
                            ComponentVO objComponentVO = objComponentVOMap.get(strComponentModelId);
                            strResult += "\"" + objComponentVO.getModelName() + "\"";
                        } else {
                            strResult += options.getString(strKey);
                        }
                    }
                }
                strResult += ",\n";
            }
        }
        strResult = strResult.substring(0, strResult.length() - 2);
        strResult += "\n    },\n";
        
        return strResult;
    }
    
    /**
     * 处理editGrid的edittype属性。
     * 
     * <pre>
     *  e.g.： 
     *  处理前：
     *  String edittype = &quot;{\&quot;adress\&quot;:{\&quot;name\&quot;:\&quot;adress\&quot;,\&quot;radio_list\&quot;:\&quot;cap.getDicByCode(\&quot;adress\&quot;)\&quot;,\&quot;uitype\&quot;:\&quot;RadioGroup\&quot;},\&quot;name\&quot;:{\&quot;checkbox_list\&quot;:\&quot;cap.getDicByCode(\&quot;name\&quot;)\&quot;,\&quot;uitype\&quot;:\&quot;CheckboxGroup\&quot;},\&quot;sex\&quot;:{\&quot;dictionary\&quot;:\&quot;user\&quot;,\&quot;name\&quot;:\&quot;sex\&quot;,\&quot;radio_list\&quot;:\&quot;\&quot;,\&quot;uitype\&quot;:\&quot;RadioGroup\&quot;}}&quot;;
     *  处理后：
     *  String edittype = &quot;{\&quot;adress\&quot;:{\&quot;name\&quot;:\&quot;adress\&quot;,\&quot;radio_list\&quot;:cap.getDicByCode(\&quot;adress\&quot;),\&quot;uitype\&quot;:\&quot;RadioGroup\&quot;},\&quot;name\&quot;:{\&quot;checkbox_list\&quot;:cap.getDicByCode(\&quot;name\&quot;),\&quot;uitype\&quot;:\&quot;CheckboxGroup\&quot;},\&quot;sex\&quot;:{\&quot;dictionary\&quot;:\&quot;user\&quot;,\&quot;name\&quot;:\&quot;sex\&quot;,\&quot;radio_list\&quot;:\&quot;\&quot;,\&quot;uitype\&quot;:\&quot;RadioGroup\&quot;}}&quot;;
     * </pre>
     * 
     * @param edittype edittype属性字符串
     * @return 处理后的结果
     */
    private String processEdittype4Editgrid(String edittype) {
        String strResult = "";
        JSONObject objEditType = JSONObject.parseObject(edittype);
        Set<String> objKeySet = objEditType.keySet();
        Iterator<String> objIterator = objKeySet.iterator();
        while (objIterator.hasNext()) {
            String strKey = objIterator.next();
            if (StringUtil.isNotEmpty(objEditType.getString(strKey))) {
                strResult += "\"" + strKey + "\"" + ": {";
                JSONObject objOptions = (JSONObject) objEditType.get(strKey);
                Set<String> objKeySet2 = objOptions.keySet();
                String strTypeKey = "modelId";
                String strTypeValue = null;
                if (objOptions.getString("componentModelId") != null) {
                    strTypeValue = objOptions.getString("componentModelId");
                } else {
                    strTypeKey = "uitype";
                    strTypeValue = objOptions.getString("uitype");
                }
                Map<String, Boolean> objCurrUIJsonKey = allUIJsonTypeAttrs.get(strTypeKey).get(strTypeValue);
                List<String> lstCurrUISelfAttr = runtimeAvailableAttrs.get(strTypeKey).get(strTypeValue);
                Iterator<String> objIterator2 = objKeySet2.iterator();
                while (objIterator2.hasNext()) {
                    String strKey2 = objIterator2.next();
                    if (lstCurrUISelfAttr.contains(strKey2)) {
                        strResult += "\"" + strKey2 + "\"" + ": ";
                        if (objOptions.get(strKey2) instanceof String) {
                            if (objCurrUIJsonKey.get(strKey2) != null && objCurrUIJsonKey.get(strKey2)) {
                                strResult += objOptions.getString(strKey2);
                            } else {
                                strResult += "\"" + objOptions.getString(strKey2) + "\"";
                            }
                        } else {
                            strResult += objOptions.getString(strKey2);
                        }
                        strResult += ", ";
                    }
                }
                strResult = strResult.substring(0, strResult.length() - 2);
                strResult += "}, ";
            }
        }
        strResult = "{" + strResult.substring(0, strResult.length() - 2) + "}";
        return strResult;
    }
    
    /**
     * 特殊处理CheckboxGroup、RadioGroup以及PullDown的数据源属性（当它们使用了枚举取数据字典时）
     * 
     * @param options 组件属性集合
     * @return 处理后的组件属性集合
     */
    private JSONObject checkboxAndRadioProcess(JSONObject options) {
        // 处理单独的pulldown\checkboxgroup\radiogroup
        setUIComponentDataSource(options);
        
        // 处理editgrid中的pulldown\checkboxgroup\radiogroup
        String uitype = (String) options.get("uitype");
        if ("EditableGrid".equalsIgnoreCase(uitype)) {
            String editType = (String) options.get("edittype");
            // 没定义列的表需要如下判断
            if (StringUtils.isNotBlank(editType)) {
                JSONObject jsonObj = JSON.parseObject(editType);
                Set<String> keySet = jsonObj.keySet();
                Iterator<String> it = keySet.iterator();
                while (it.hasNext()) {
                    String attributeName = it.next();
                    JSONObject _obj = (JSONObject) jsonObj.get(attributeName);
                    setUIComponentDataSource(_obj);
                    jsonObj.put(attributeName, _obj);
                }
                options.put("edittype", jsonObj);
            }
        }
        return options;
    }
    
    /**
     * 处理CheckboxGroup、RadioGroup以及PullDown的数据源属性（当它们使用了枚举取数据字典时）
     * 
     * @param _obj json对象 options
     */
    private void setUIComponentDataSource(JSONObject _obj) {
        String _uitype = (String) _obj.get("uitype");
        if ("CheckboxGroup".equalsIgnoreCase(_uitype) || "RadioGroup".equalsIgnoreCase(_uitype)
            || "PullDown".equalsIgnoreCase(_uitype)) {
            String enumcp = (String) _obj.get("enumdata");
            if (StringUtil.isNotBlank(enumcp)) {
                _obj.remove("enumdata");
                String datasource = "cap.getDicByCode(\"" + enumcp + "\")";
                if ("CheckboxGroup".equalsIgnoreCase(_uitype)) {
                    _obj.put("checkbox_list", datasource);
                } else if ("RadioGroup".equalsIgnoreCase(_uitype)) {
                    _obj.put("radio_list", datasource);
                } else {
                    _obj.put("datasource", datasource);
                }
            }
        }
    }
    
    /**
     * 将td的options转换成style
     * 
     * @param objLayout 布局对象
     */
    private void setTdStyle(JSONObject objLayout) {
        String strStyle = "";
        JSONObject objOptions = objLayout.getJSONObject("options");
        if (objOptions != null) {
            Set<String> objKeySet = objOptions.keySet();
            Iterator<String> objIterator = objKeySet.iterator();
            while (objIterator.hasNext()) {
                String strKey = objIterator.next();
                if (!"id".equals(strKey) && !"name".equals(strKey) && !"uitype".equals(strKey)
                    && !"colspan".equals(strKey) && !"label".equals(strKey)) {
                    
                    if (StringUtil.isNotEmpty(objOptions.getString(strKey))) {
                        strStyle += strKey + ":" + objOptions.getString(strKey) + ";";
                    }
                } else if ("colspan".equals(strKey)) {
                    objLayout.put("colspan", "colspan='" + objOptions.getString(strKey) + "'");
                }
            }
        }
        if (!"".equals(strStyle)) {
            strStyle = "style=\"" + strStyle + "\"";
        }
        objLayout.put("style", strStyle);
    }
    
    /**
     * 处理引用文件
     * 
     * @param objJSONObject pageVO
     */
    @SuppressWarnings("unchecked")
    public void wrapperIncludeFile(JSONObject objJSONObject) {
        List<String> lstJs = new ArrayList<String>();
        List<String> lstCss = new ArrayList<String>();
        List<String> lstJsp = new ArrayList<String>();
        
        // 默认引入JS、CSS、JSP
        JSONArray lstCustom = objJSONObject.getJSONArray("includeFileList");
        for (int i = 0; i < lstCustom.size(); i++) {
            JSONObject objIncludeFileVO = lstCustom.getJSONObject(i);
            if (objIncludeFileVO.getBooleanValue("defaultReference")) {
                if ("js".equals(objIncludeFileVO.getString("fileType"))) {
                    lstJs.add(objIncludeFileVO.getString("filePath"));
                } else if ("css".equals(objIncludeFileVO.getString("fileType"))) {
                    lstCss.add(objIncludeFileVO.getString("filePath"));
                } else if ("jsp".equals(objIncludeFileVO.getString("fileType"))) {
                    lstJsp.add(objIncludeFileVO.getString("filePath"));
                }
            }
        }
        
        // 控件识别的引入文件
        Map<String, List<String>> objIncludeFile = wrapperIncludeFile(objJSONObject.getJSONObject("layoutVO")
            .getJSONArray("children"));
        
        List<String> lstJs4ComponentDepend = this.removeDuplicate(objIncludeFile.get("js"));
        List<String> lstCss4ComponentDepend = this.removeDuplicate(objIncludeFile.get("css"));
        // 根据过滤配置，过滤引用文件
        this.replaceIncludeFile(lstJs4ComponentDepend, lstJsp, lstCss4ComponentDepend);
        
        lstJs.addAll(lstJs4ComponentDepend);
        lstCss.addAll(lstCss4ComponentDepend);
        // 行为识别的引入JS
        lstJs.addAll(getActionJS(objJSONObject));
        // 行为识别的引入CSS
        lstCss.addAll(getActionCSS(objJSONObject));
        
        // 用户自定义引入文件
        for (int i = 0; i < lstCustom.size(); i++) {
            JSONObject objIncludeFileVO = lstCustom.getJSONObject(i);
            if (!objIncludeFileVO.getBooleanValue("defaultReference")) {
                if ("js".equals(objIncludeFileVO.getString("fileType"))) {
                    lstJs.add(objIncludeFileVO.getString("filePath"));
                } else if ("css".equals(objIncludeFileVO.getString("fileType"))) {
                    lstCss.add(objIncludeFileVO.getString("filePath"));
                } else if ("jsp".equals(objIncludeFileVO.getString("fileType"))) {
                    lstJsp.add(objIncludeFileVO.getString("filePath"));
                }
            }
        }
        
        objJSONObject.put("JSFileList", this.removeDuplicate(lstJs));
        objJSONObject.put("CSSFileList", this.removeDuplicate(lstCss));
        objJSONObject.put("JSPFileList", this.removeDuplicate(lstJsp));
    }
    
    /**
     * 过滤引入文件
     * 
     * @param jsList 引入的js清单
     * @param jspList 引入的jsp清单
     * @param cssList 引入的css清单
     */
    private void replaceIncludeFile(List<String> jsList, List<String> jspList, List<String> cssList) {
        ComponentDependFilesConfigVO objCompDependFilesConfigVO = new ComponentDependFilesConfigVO();
        try {
            objCompDependFilesConfigVO = componentDependFilesConfigFacade.loadModel(objCompDependFilesConfigVO
                .getModelId());
        } catch (OperateException e) {
            LOGGER.error("读取控件依赖文件配置元数据文件异常.", e);
        }
        if (objCompDependFilesConfigVO != null) {
            List<CapMap> lstDependFilesChangeInfo = objCompDependFilesConfigVO.getDependFilesChangeInfoList();
            for (CapMap objMap : lstDependFilesChangeInfo) {
                String strCustomFilePath = (String) objMap.get("customFilePath");
                if (StringUtils.isNotBlank(strCustomFilePath)) {
                    String strDefaultFilePath = (String) objMap.get("defaultFilePath");
                    String strCustomFileType = strCustomFilePath.substring(strCustomFilePath.lastIndexOf(".") + 1,
                        strCustomFilePath.length());
                    // 添加变更的文件路径
                    if ("js".equalsIgnoreCase(strCustomFileType)) {
                        jsList.add(strCustomFilePath);
                    } else if ("jsp".equalsIgnoreCase(strCustomFileType)) {
                        jspList.add(strCustomFilePath);
                    } else if ("css".equalsIgnoreCase(strCustomFileType)) {
                        cssList.add(strCustomFilePath);
                    }
                    
                    String strDefaultFileType = strDefaultFilePath.substring(strDefaultFilePath.lastIndexOf(".") + 1,
                        strDefaultFilePath.length());
                    // 删除被替换的文件路径
                    if ("js".equalsIgnoreCase(strDefaultFileType)) {
                        jsList.remove(strDefaultFilePath);
                    } else if ("jsp".equalsIgnoreCase(strDefaultFileType)) {
                        jspList.remove(strDefaultFilePath);
                    } else if ("css".equalsIgnoreCase(strDefaultFileType)) {
                        cssList.remove(strDefaultFilePath);
                    }
                }
            }
        }
    }
    
    /**
     * 包装引入文件
     * 
     * @param lstLayout 布局集合
     * @return 布局Map
     */
    private Map<String, List<String>> wrapperIncludeFile(JSONArray lstLayout) {
        Map<String, List<String>> objResult = new HashMap<String, List<String>>();
        objResult.put("js", new ArrayList<String>());
        objResult.put("css", new ArrayList<String>());
        if (lstLayout == null) {
            return objResult;
        }
        for (int i = 0; i < lstLayout.size(); i++) {
            JSONObject objLayout = lstLayout.getJSONObject(i);
            if ("ui".equals(objLayout.getString("type"))) {
                this.addComponentIncludeFile(objLayout.getString("componentModelId"), objResult);
                this.handleDependComponentIncludeFile(objLayout, objResult);
            } else {
                Map<String, List<String>> objTempResult = wrapperIncludeFile(objLayout.getJSONArray("children"));
                objResult.get("js").addAll(objTempResult.get("js"));
                objResult.get("css").addAll(objTempResult.get("css"));
            }
        }
        return objResult;
    }
    
    /**
     * 处理依赖控件的引用文件（目前识别到的只有编辑网格控件定义）
     *
     * @param layout 布局控件对象
     * @param includeFileMaps 引用对象
     */
    private void handleDependComponentIncludeFile(JSONObject layout, Map<String, List<String>> includeFileMaps){
        if("EditableGrid".equals(layout.getString("uiType"))){
            JSONObject objOptions = layout.getJSONObject("options");
            if(objOptions.containsKey("edittype")){
                String strEdittype = objOptions.getString("edittype");
                Pattern objPattern = Pattern.compile("\"componentModelId\":\\s*\"([\\w.]+)\"");
                Matcher objMatcher = objPattern.matcher(strEdittype);
                while (objMatcher.find()) {
                    this.addComponentIncludeFile(objMatcher.group(NumberConstant.ONE), includeFileMaps);
                }
            }
        }
    }
    
    /**
     * 添加控件引用文件
     *
     * @param componentModelId 控件modelId
     * @param includeFileMaps 引用对象
     */
    private void addComponentIncludeFile(String componentModelId, Map<String, List<String>> includeFileMaps){
        ComponentVO objComponentVO = componentFacade.loadModel(componentModelId);
        includeFileMaps.get("js").addAll(objComponentVO.getJs());
        includeFileMaps.get("css").addAll(objComponentVO.getCss());
    }
    
    /**
     * 处理引用文件
     * 
     * @param objJSONObject PageVO
     * @return 行为清单
     */
    @SuppressWarnings({ "rawtypes" })
    private List getActionJS(JSONObject objJSONObject) {
        JSONArray lstResult = new JSONArray();
        JSONArray lstPageActionVO = objJSONObject.getJSONArray("pageActionVOList");
        for (int i = 0; i < lstPageActionVO.size(); i++) {
            JSONObject objPageActionVO = lstPageActionVO.getJSONObject(i);
            JSONObject objActionDefineVO = objPageActionVO.getJSONObject("actionDefineVO");
            lstResult.addAll(objActionDefineVO.getJSONArray("js"));
        }
        lstResult.add("/cap/dwr/engine.js");
        lstResult.add("/cap/dwr/util.js");
        lstResult.add("/cap/dwr/interface/" + objJSONObject.getString("modelName") + "Action.js");
        return lstResult;
    }
    
    /**
     * 处理引用文件
     * 
     * @param objJSONObject PageVO
     * @return 行为css清单
     */
    @SuppressWarnings("rawtypes")
    private List getActionCSS(JSONObject objJSONObject) {
        JSONArray lstResult = new JSONArray();
        JSONArray lstPageActionVO = objJSONObject.getJSONArray("pageActionVOList");
        for (int i = 0; i < lstPageActionVO.size(); i++) {
            JSONObject objPageActionVO = lstPageActionVO.getJSONObject(i);
            JSONObject objActionDefineVO = objPageActionVO.getJSONObject("actionDefineVO");
            lstResult.addAll(objActionDefineVO.getJSONArray("css"));
        }
        return lstResult;
    }
    
    /**
     * 
     * 删除重复对象
     * 
     * @param list 集合
     * @return 去重后集合
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<String> removeDuplicate(List<String> list) {
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
     * 包装页面控件表达式
     * 
     * @param objJSONObject PageVO
     */
    private void wrapperExpression(JSONObject objJSONObject) {
        Map<String, JSONObject> objJavaExpressionMap = new HashMap<String, JSONObject>();
        JSONArray lstExpression = objJSONObject.getJSONArray("pageComponentExpressionVOList");
        for (int i = 0; i < lstExpression.size(); i++) {
            JSONObject objExpression = lstExpression.getJSONObject(i);
            JSONArray lstComponentState = objExpression.getJSONArray("pageComponentStateList");
            JSONArray lstNewPageComponentStateVO = new JSONArray();
            for (int j = 0; j < lstComponentState.size(); j++) {
                JSONObject objComponentState = lstComponentState.getJSONObject(j);
                JSONObject objLayoutVO = layoutMap.get(objComponentState.getString("componentId"));
                // 如果objLayoutVO为空则表示页面控件已经被删除
                if (objLayoutVO != null) {
                    if (objLayoutVO.getJSONObject("options").get("id") != null) {
                        objComponentState.put("uiConfigId", objLayoutVO.getJSONObject("options").get("id"));
                    } else {
                        objComponentState.put("uiConfigId", objLayoutVO.getString("id"));
                    }
                    if ("java".equals(objExpression.getString("expressionType"))) {
                        objJavaExpressionMap.put(objComponentState.getString("componentId"), objExpression);
                    }
                    lstNewPageComponentStateVO.add(objComponentState);
                }
            }
            lstExpression.getJSONObject(i).put("pageComponentStateList", lstNewPageComponentStateVO);
        }
        objJSONObject.put("javaExpressionMap", objJavaExpressionMap);
    }
    
    @Override
    public Map<String, Object> wrapper(PageVO entity, String format) {
        return null;
    }
    
}
