/**
 * 
 */
package com.comtop.cap.codegen.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.serve.model.ServiceObjectVO;
import com.comtop.cap.codegen.config.ConfigFactory;
import com.comtop.cap.codegen.config.GeneratorConfig;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.cap.codegen.generate.IWrapper;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * @author luozhenming
 *
 */
public class WrapperServiceFacace implements Observer, IWrapper<ServiceObjectVO> {

	@Override
	public Map<String, Object> wrapper(ServiceObjectVO service) {
		 WrapperServiceObject objServiceObject = new WrapperServiceObject(service, getVOFormat());
	     Map<String, Object> objParam = new HashMap<String, Object>(1000);
	     objParam.put("service", objServiceObject);
	     objParam.put("exceptions", objServiceObject.getExceptions());
	     return objParam;
	}

	@Override
	public Map<String, Object> wrapper(ServiceObjectVO service, String format) {
		 WrapperServiceObject objServiceObject = new WrapperServiceObject(service, format);
	     Map<String, Object> objParam = new HashMap<String, Object>(1000);
	     objParam.put("service", objServiceObject);
	     objParam.put("exceptions", objServiceObject.getExceptions());
	     return objParam;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	/**
     * 从配置中获取VO生成的名称格式
     * 
     * 
     * @return VO生成的名称格式
     */
    private static String getVOFormat() {
    	GeneratorConfig objConfig = ConfigFactory.getInstance().getDefaultConfig();
        List<LayerConfig> lstLayers = objConfig.getLayers();
        String strFormat = null;
        for (LayerConfig objLayer : lstLayers) {
            if ("model".equals(objLayer.getName())) {
                String strSourcePattern = objLayer.getSourceNamePattern()[0];
                strFormat = strSourcePattern.substring(0, strSourcePattern.indexOf('.'));
            }
        }
        if (StringUtil.isEmpty(strFormat)) {
            strFormat = "{0}";
        }
        return strFormat;
    }
	
}
