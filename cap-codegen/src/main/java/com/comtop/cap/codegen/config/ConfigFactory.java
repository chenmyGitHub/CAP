/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.runtime.base.exception.CapMetaDataException;
import com.comtop.cip.jodd.bean.BeanCopy;
import com.comtop.cip.json.util.IOUtils;

/**
 * 
 * 配置文件工厂
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class ConfigFactory {
    
//    /** 日志 */
//    protected static final Logger LOGGER = LoggerFactory.getLogger(ConfigFactory.class);
    
    /** 配置工厂实例 */
    private static ConfigFactory instance;
    
    /**代码生成相关配置*/
    private List<GeneratorConfig> generatorConfigs;
    
    /**
     * 构造函数
     */
    private ConfigFactory() {
        super();
    }
    
    /**
     * 初始化配置信息
     */
    private void initGeneratorConfig(){
    	List<GeneratorConfig> lstGeneratorConfigs = new ArrayList<GeneratorConfig>();
        DocumentBuilderFactory objDbf = null;
        DocumentBuilder objBuilder = null;
        // 将xml文件解析
        Document objDoc = null;
        objDbf = DocumentBuilderFactory.newInstance();
       
        InputStream objIn = null;
        Exception ex = null;
        Map<String,GeneratorConfig> configMap = new HashMap<String,GeneratorConfig>();
        try {
            objIn = getConfigStream();
            if (objIn == null) {
                throw new CapMetaDataException("找不到代码生成器配置文件。");
            }
            objBuilder = objDbf.newDocumentBuilder();
            objDoc = objBuilder.parse(objIn);
            
            // 获得配置文件中代码生成器配置
            NodeList objNodes = objDoc.getElementsByTagName("generator");
           
            for (int iIndex = 0; iIndex < objNodes.getLength(); iIndex++) {
            	Node objNode = objNodes.item(iIndex);
                GeneratorConfig objGeneratorConfig = createGenerateConfigFromXMLNode(objNode);
                configMap.put(objGeneratorConfig.getId(), objGeneratorConfig);
                lstGeneratorConfigs.add(objGeneratorConfig);
            }
//            // 获得配置文件中代码编译顺序
//            NodeList objCompileNodes = objDoc.getElementsByTagName("compile");
//            for (int i = 0; i < objCompileNodes.getLength(); i++) {
//            	Node objNode = objCompileNodes.item(i);
//            	initGenerateConfigCompleSort(objNode,configMap);
//			}
            
        } catch (ParserConfigurationException e) {
            ex = e;
        } catch (SAXException e) {
            ex = e;
        } catch (IOException e) {
            ex = e;
        } finally {
            IOUtils.close(objIn);
        }
        if (ex != null) {
            //LOGGER.error(ex.getMessage(), ex);
            throw new CapMetaDataException("获取生成代码配置出错。");
        }
        this.generatorConfigs = new ArrayList<GeneratorConfig>(lstGeneratorConfigs.size());
        GeneratorConfig objParentConfig;
        GeneratorConfig objFinalConfig;
        for (GeneratorConfig generatorConfig : lstGeneratorConfigs) {
        	objParentConfig = configMap.get(generatorConfig.getParentConfig());
        	objFinalConfig = generatorConfig;
			if(generatorConfig.getParentConfig() != null && objParentConfig != null){
				objFinalConfig = megerParentConfig(generatorConfig,objParentConfig);
				this.generatorConfigs.add(objFinalConfig);
			}else{
				this.generatorConfigs.add(objFinalConfig);
			}
//			intiConfigCompileSort(objFinalConfig);
		}
    }

//    /**
//     * 初始化编译顺序
//     * @param generatorConfig 代码生成配置对象
//     */
//    private void intiConfigCompileSort(GeneratorConfig generatorConfig) {
//    	if(generatorConfig.getCompileConfig() == null){
//    		return;
//    	}
//    	
//    	List<LayerConfig> lstLayer = generatorConfig.getLayers();
//    	
//    	Map<String,LayerConfig> layerMap= new HashMap<String, LayerConfig>();
//    	
//    	for (LayerConfig layerConfig : lstLayer) {
//    		layerMap.put(layerConfig.getId(), layerConfig);
//		}
//
//    	List<CompileSortConfig> lstSortOrder = generatorConfig.getCompileConfig();
//
//		Collections.sort(lstSortOrder, new Comparator<CompileSortConfig>() {
//			
//			@Override
//			public int compare(CompileSortConfig o1, CompileSortConfig o2) {
//				return o1.getSortNo() - o2.getSortNo();
//			}
//		});
//		
//		List<String> lstDir = new ArrayList<String>(lstSortOrder.size());
//		for (CompileSortConfig compileSortConfig : lstSortOrder) {
//			if(lstDir.contains(layerMap.get(compileSortConfig.getRefLayerId()).getName())){
//				continue;
//			}
//			lstDir.add(layerMap.get(compileSortConfig.getRefLayerId()).getName());
//		}
//		
//		generatorConfig.setCompilePath(lstDir);
//	}

//	/**
//     * 初始化config对象的代码编译顺序信息
//     * @param objNode 代码编译xml配置
//     * @param configMap 代码生成配置map
//     */
//	private void initGenerateConfigCompleSort(Node objNode,
//			Map<String, GeneratorConfig> configMap) {
//		NamedNodeMap objAttrMap = objNode.getAttributes();
//		
//		if(objAttrMap.getNamedItem("ref-generator") == null){
//			throw new MetaDataException("the attr 'ref-generator' of xml node 'compile' is required");
//		}
//		String strRefGenerateId = objAttrMap.getNamedItem("ref-generator").getTextContent();
//		
//		NodeList objSortNodes = objNode.getChildNodes();
//		List<CompileSortConfig> lstSortOrder = new ArrayList<CompileSortConfig>(objSortNodes.getLength());
//		
//		CompileSortConfig objCompileSortConfig = null;
//		 Node objSortNod = null;
//		 NamedNodeMap objSortAttrMap = null;
//		for (int i = 0; i < objSortNodes.getLength(); i++) {
//			 objSortNod = objSortNodes.item(i);
//			 if (!"sort".equals(objSortNod.getNodeName())) {
//			        continue;
//			    }
//			 objSortAttrMap = objSortNod.getAttributes();
//			 objCompileSortConfig = new CompileSortConfig();
//			 objCompileSortConfig.setRefLayerId(objSortAttrMap.getNamedItem("ref-layerId").getTextContent());
//			 
//			 if(objSortAttrMap.getNamedItem("sortNo") != null){
//				 String strSort = objSortAttrMap.getNamedItem("sortNo").getTextContent();
//				 objCompileSortConfig.setSortNo(Integer.parseInt(strSort));
//			 }else{
//				 objCompileSortConfig.setSortNo(i+1);
//			 }
//			 lstSortOrder.add(objCompileSortConfig);
//		}
//		
//		GeneratorConfig objConfig = configMap.get(strRefGenerateId);
//		
//		objConfig.setCompileConfig(lstSortOrder) ;
//	}

	/**
	 * @param objNode xml节点
	 * @return config对象
	 */
	private GeneratorConfig createGenerateConfigFromXMLNode(Node objNode) {
		// 取得一个节点
		
		NamedNodeMap objAttrMap = objNode.getAttributes();
		GeneratorConfig objGeneratorConfig = new GeneratorConfig();
		if(objAttrMap.getNamedItem("default") == null){
			objGeneratorConfig.setDefault(false);
		}else if ("true".equals(objAttrMap.getNamedItem("default").getTextContent())) {
		    objGeneratorConfig.setDefault(true);
		}
		objGeneratorConfig.setId(objAttrMap.getNamedItem("id").getTextContent());
		objGeneratorConfig.setName(objAttrMap.getNamedItem("name").getTextContent());
		if(objAttrMap.getNamedItem("wrapper") != null){
			objGeneratorConfig.setWrapper(objAttrMap.getNamedItem("wrapper").getTextContent());
		}
		objGeneratorConfig.setFtlParentDir(objAttrMap.getNamedItem("ftlPath").getTextContent());
		if(objAttrMap.getNamedItem("ftlRoot") != null){
			objGeneratorConfig.setFtlRoot(objAttrMap.getNamedItem("ftlRoot").getTextContent());
		}
		if(objAttrMap.getNamedItem("parent")!= null){
			objGeneratorConfig.setParentConfig(objAttrMap.getNamedItem("parent").getTextContent());
		}
		NodeList objLayerNodes = objNode.getChildNodes();
		
		LayerConfig objLayerConfig = null;
		for (int j = 0; j < objLayerNodes.getLength(); j++) {
		    Node objLayerNode = objLayerNodes.item(j);
		    NamedNodeMap objLayerAttrMap = objLayerNode.getAttributes();
		    if (!"layer".equals(objLayerNode.getNodeName())) {
		        continue;
		    }
		    objLayerConfig = new LayerConfig();
		    objLayerConfig.setName(objLayerAttrMap.getNamedItem("name").getTextContent());
		    
		    String strType = objLayerAttrMap.getNamedItem("sourceType").getTextContent();
		    int iType = getSourceType(strType);
		    objLayerConfig.setSourceType(iType);
		    
		    String[] sourceNamePattern = objLayerAttrMap.getNamedItem("sourceNamePattern")
		            .getTextContent().split(",");
		    objLayerConfig.setSourceNamePattern(sourceNamePattern);
		    
		    String[] fltNames = objLayerAttrMap.getNamedItem("ftlName").getTextContent().split(",");
		    objLayerConfig.setFtlName(fltNames);
		    
		    if (objLayerAttrMap.getNamedItem("id") != null) {
		        objLayerConfig.setId(objLayerAttrMap.getNamedItem("id").getTextContent());
		    }
		    
		    if (objLayerAttrMap.getNamedItem("processClass") != null) {
		        objLayerConfig.setProcessClass(objLayerAttrMap.getNamedItem("processClass").getTextContent());
		    }
		    
		    if(objLayerAttrMap.getNamedItem("rewritable") != null){
		    	String[] strRewrite = objLayerAttrMap.getNamedItem("rewritable").getTextContent().split(",");
		    	Boolean[] bRewrite = new Boolean[strRewrite.length];
		    	for (int i = 0; i < strRewrite.length; i++) {
					bRewrite[i] = Boolean.valueOf(strRewrite[i]);
				}
		    	objLayerConfig.setRewritable(bRewrite);
		    }
		    //若layer未定义layerWrapper属性，则使用GeneratorConfig的wrapper属性
		    if(objLayerAttrMap.getNamedItem("layerWrapper") != null){
		    	objLayerConfig.setLayerWrapper(objLayerAttrMap.getNamedItem("layerWrapper").getTextContent());
		    }else{
		    	objLayerConfig.setLayerWrapper(objGeneratorConfig.getWrapper());
		    }
		    if(objLayerAttrMap.getNamedItem("ftlPath") != null){
		    	objLayerConfig.setFtlFilePath(objLayerAttrMap.getNamedItem("ftlPath").getTextContent());
		    }else{
		    	objLayerConfig.setFtlFilePath(objGeneratorConfig.getFtlParentDir());
		    }
		    
		    objGeneratorConfig.addLayer(objLayerConfig);
		    
		}
		return objGeneratorConfig;
	}
    
    /**
     * 
     * @param generatorConfig 当前配置
     * @param objParentConfig 父配置
     * @return 合并属性后的配置
     */
    private GeneratorConfig megerParentConfig(GeneratorConfig generatorConfig,
			GeneratorConfig objParentConfig) {
    	GeneratorConfig objNewConfig = new GeneratorConfig();
    	
    	BeanCopy.beans(objParentConfig, objNewConfig).copy();
    	BeanCopy.beans(generatorConfig, objNewConfig).ignoreNulls(true).copy();
    	
    	List<LayerConfig> lstLayer = new ArrayList<LayerConfig>();
    	
    	lstLayer.addAll(generatorConfig.getLayers());
    	
    	List<String> lstLayId=new ArrayList<String>(lstLayer.size());
    	
    	for (LayerConfig layerConfig : lstLayer) {
    		lstLayId.add(layerConfig.getId());
		}
    	
    	for (LayerConfig layerConfig : objParentConfig.getLayers()) {
			if(lstLayId.contains(layerConfig.getId())){
				continue;
			}
			lstLayer.add(layerConfig);
		}
    	
    	objNewConfig.setLayers(lstLayer);
    	
		return objNewConfig;
	}

	/**
     * 获取配置工厂实例
     * 
     * @return 配置工厂实例
     */
    public static ConfigFactory getInstance() {
        if (instance == null) {
            synchronized (ConfigFactory.class) {
                if (instance == null) {
                    instance = new ConfigFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * 获取代码生成默认配置
     * 
     * @return 代码生成配置
     */
    public GeneratorConfig getDefaultConfig() {
    	List<GeneratorConfig> lstConfig = getGeneratorConfigs();
    	if(lstConfig == null){
    		throw new CapMetaDataException("can not find <generator> in GeneratorConfig.xml ");
    	}
    	String strFramework = PreferenceConfigQueryUtil.getGenerateCodeFramework();
    	if(PreferenceConfigQueryUtil.GENERATECODE_FRAMEWORK_SPRING.equals(strFramework)){
    		return getConfigByGeneratorId("cap_spring");
    	}
    	
    	for (GeneratorConfig generatorConfig : lstConfig) {
			if(generatorConfig.isDefault()){
				return generatorConfig;
			}
		}
    	if(lstConfig.size() > 0){
    		return lstConfig.get(0);
    	}
    	throw new CapMetaDataException("can not find <generator> in GeneratorConfig.xml ");
    }
    
    /**
     * 获取代码生成默认配置
     * @param generatorId GeneratorConfig.xml配置文件中<generator>节点的id
     * 
     * @return 代码生成配置
     */
    public GeneratorConfig getConfigByGeneratorId(String generatorId) {
    	List<GeneratorConfig> lstConfig = getGeneratorConfigs();
    	if(lstConfig == null){
    		return null;
    	}
    	for (GeneratorConfig generatorConfig : lstConfig) {
			if(generatorConfig.getId().equals(generatorId)){
				return generatorConfig;
			}
		}
    	throw new CapMetaDataException("can not find <generator> by id: '"+generatorId+"' in GeneratorConfig.xml ");
    }
    
    /**
     * 获取代码生成默认配置
     * 
     * @return 代码生成配置
     */
    public List<GeneratorConfig> getGeneratorConfigs() {
    	if(generatorConfigs == null){
    		initGeneratorConfig();
    	}
    	return generatorConfigs;
    }
    
    /**
     * 获取代码类型
     * 
     * @param strType 代码类型字符串
     * @return 代码类型;
     */
    private int getSourceType(String strType) {
        int iType;
        if (LayerConfig.RES_TYPE_VALUE.equals(strType.toLowerCase())) {
            iType = LayerConfig.RES_TYPE;
        } else if (LayerConfig.WEB_TYPE_VALUE.equals(strType.toLowerCase())) {
            iType = LayerConfig.WEB_TYPE;
        } else {
            iType = LayerConfig.JAVA_TYPE;
        }
        return iType;
    }
    
    /**
     * 获取配置文件的输入流
     * 
     * @return 配置文件的输入流
     */
    private InputStream getConfigStream() {
        ClassLoader objLoader = Thread.currentThread().getContextClassLoader();
        InputStream objIn = objLoader.getResourceAsStream("GeneratorConfig.xml");
        if (objIn == null) {
            objIn = objLoader.getResourceAsStream("com/comtop/cap/codegen/config/GeneratorConfig.xml");
        }
        return objIn;
    }
    
    /**
     * 
     * @param args xx
     */
   public static void main(String[] args) {
	   ConfigFactory objcConfigFactory = new ConfigFactory();
	   objcConfigFactory.initGeneratorConfig();
	   
	   objcConfigFactory.getDefaultConfig();
   }
}
