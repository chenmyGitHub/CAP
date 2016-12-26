package com.comtop.cap.doc.util;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.bm.metadata.sysmodel.utils.CapModuleConstants;
import com.comtop.cap.doc.dbd.facade.DataBaseObjectDocFacade;
import com.comtop.cap.document.expression.annotation.Function;
import com.comtop.cap.document.word.docmodel.SectionProperties.Margin;
import com.comtop.cap.document.word.docmodel.SectionProperties.PageSize;
import com.comtop.cap.document.word.docmodel.data.Graphic;
import com.comtop.cap.document.word.util.MeasurementUnits;
import com.comtop.cip.common.util.CAPCollectionUtils;
import com.comtop.cip.common.util.CAPStringUtils;
import com.comtop.cip.graph.image.facade.GraphToImageFacadeImpl;
import com.comtop.cip.graph.image.facade.IGraphToImageFacade;
import com.comtop.cip.graph.image.model.ImageVO;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.sys.module.facade.IModuleFacade;
import com.comtop.top.sys.module.facade.ModuleFacade;
import com.comtop.top.sys.module.model.ModuleDTO;

/**
 * 数据库文档处理封装类-数据表操作
 * 
 * @author 李小强
 * @since 1.0
 * @version 2016-12-8 李小强
 */

public class CAPDocDBHelper {
	/**图形类型-数据库对象关系图--DBER*/
	public static final String GRAPHIC_TYPE_DBER="DBER";
	/**图形类型-应用模块结构图(概要设计中使用)--APP_MODULE_STURCE*/
	public static final String GRAPHIC_TYPE_APP_MODULE_STURCE="APP_MODULE_STURCE";
	
	  /**  数据库文档处理封装类-数据表操作 */
	static DataBaseObjectDocFacade docFacade= AppContext.getBean(DataBaseObjectDocFacade.class);
	/***
	 *  根据节点ID获取对应的文档图片显示html代码
	 * @param moduleId 节点ID，对应业务系统的模块ID
	 * @param cookiesJsonStr 供图片化组件使用的cookies[] 组件Json字符串
	 * @param httpUrl http请求的前缀地址--"http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath()
	 * @return 文档图片显示html代码（一到多个）
	 */
    @Function(name = "CAPDocDBHelper_getDataBaseErGraphHtml", description = "根据节点ID获取对应的文档图片显示html代码")
	public static List<Graphic> getDataBaseErGraphHtml(String moduleId,String cookiesJsonStr,String httpUrl) {
		if (CAPStringUtils.isBlank(moduleId)) {
			return	new ArrayList<Graphic>(0);
		}
		List<ModuleDTO> lstApp = getAllChildApp(moduleId);
		if (CAPCollectionUtils.isEmpty(lstApp)) {
			return new ArrayList<Graphic>(0);
		}
		 List<Graphic>  lstGraphic = new ArrayList<Graphic>();
		for (ModuleDTO dto : lstApp) {
			// 对于一个模块下没有表就不用去调用Graphic接口生成图片了 (TODO:后续需考虑视图、存储过程、函数)
			if(CAPCollectionUtils.isEmpty(docFacade.loadTableData(dto.getModuleId()))){
				continue;
			}
			Graphic objgp = getGraphic2DocByType(GRAPHIC_TYPE_DBER,dto.getModuleId(), cookiesJsonStr,httpUrl);
			lstGraphic.add(objgp);
		}
		return lstGraphic;
	}

	/**
	 * 获取指定应用ID对应的类型图片
	 * @param graphicType  图形类型.应用模块结构图(概要设计中使用)--APP_MODULE_STURCE; 数据库对象关系图--DBER
	 * 
	 * @param moduleId  应用ID--top的模块ID
	 * @param cookiesJsonStr 供图片化组件使用的cookies[] 组件Json字符串
	 * @param httpUrl http请求的前缀地址--"http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath()
	 * @return E-R图的html代码--http://localhost/web/ddd/iac.img
	 */
    @Function(name = "CAPDocDBHelper_getGraphic2DocByType", description = "根据节点ID获取对应的文档图片显示html代码")
	public static Graphic getGraphic2DocByType(String graphicType,String moduleId,String cookiesJsonStr,String httpUrl) {
		Graphic objgp = new Graphic();
		try { 
			IGraphToImageFacade imgFacade =AppContext.getBean(GraphToImageFacadeImpl.class);
			ImageVO imgVO=null;
			if(GRAPHIC_TYPE_DBER.equals(graphicType)){
				imgVO = imgFacade.getERImage(moduleId,httpUrl, cookiesJsonStr);
			}
			else if(GRAPHIC_TYPE_APP_MODULE_STURCE.equals(graphicType)){
				imgVO = imgFacade.getModuleStructImage(moduleId,httpUrl, cookiesJsonStr);
			}else{
				return objgp;
			}
			objgp.setPath(imgVO.getImagePath());
			Float aaWidth=MeasurementUnits.px2cm(Float.valueOf(imgVO.getWidth()));
			float rate = 1f;
			float widthRate = (PageSize.getA4().getWidth()-(Margin.getA4Margin().getLeft()+Margin.getA4Margin().getRight()))/aaWidth;
			rate = Math.min(rate, widthRate);
			objgp.setWidth(aaWidth*rate);//983*506
			objgp.setHeight(MeasurementUnits.px2cm(Float.valueOf(imgVO.getHigh()))*rate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objgp;
	}

 /**
  * 根据模块ID获得当前模块及模块下的所有子节点的应用集合
 * @param moduleId TOP模块ID
 * @return 所有的应用
 */
public static List<ModuleDTO> getAllChildApp(String moduleId){
	 IModuleFacade moduleFacade = AppContext.getBean(ModuleFacade.class);
	 ModuleDTO objModuleDTO = moduleFacade.readModuleVO(moduleId);
	 List<ModuleDTO> lstModel = moduleFacade.getSubChildModuleByModuleId(moduleId);
	 lstModel.add(objModuleDTO);
	 List<ModuleDTO> lstApp= new ArrayList<ModuleDTO>();
	 for(ModuleDTO dto:lstModel){
		 if(dto.getModuleType()==CapModuleConstants.APPLICATION_MODULE_TYPE){
			 lstApp.add(dto);
		 }
	 }
	 return lstApp;
 }
}
