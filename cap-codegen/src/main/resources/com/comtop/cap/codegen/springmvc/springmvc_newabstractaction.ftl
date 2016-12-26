<#include "/common/capCopyright.ftl">

package ${modelPackage}.action.abs;

import com.comtop.cap.runtime.base.action.SpringBaseAction;
<#if sessionKeys?? && sessionKeys?length gt 0>
import org.springframework.web.bind.annotation.SessionAttributes;
</#if>
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
<#list importClass as iclass>
import ${iclass};
</#list>

/**
 * ${cname}Action
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 */
<#if sessionKeys?? && sessionKeys?length gt 0>
@SessionAttributes(value={${sessionKeys}},types={${sessionTypes}})
</#if>
public abstract class Abstract${modelName}Action extends SpringBaseAction {

	/**
	 * 设置默认页面参数到request的Attribute中
	 */
	@Override
	protected final void setDefaultUrlParamToAttribute() {
		//初始化url参数的默认值
<#list pageAttributeVOList as pageAttributeVO>
		/**${pageAttributeVO.attributeDescription}**/
	<#if pageAttributeVO.attributeType=="String">
	<#if pageAttributeVO.attributeValue=="">
		setAttributeToRequest("${pageAttributeVO.attributeName}", "");
	</#if>
	<#if pageAttributeVO.attributeValue!="">
		setAttributeToRequest("${pageAttributeVO.attributeName}", "${pageAttributeVO.attributeValue}");
	</#if>
	</#if>
	<#if pageAttributeVO.attributeType!="String">
	<#if pageAttributeVO.attributeValue=="">
		setAttributeToRequest("${pageAttributeVO.attributeName}", null);
	</#if>
	<#if pageAttributeVO.attributeValue!="">
		setAttributeToRequest("${pageAttributeVO.attributeName}", ${pageAttributeVO.attributeValue});
	</#if>
	</#if>
</#list>
	}

<#list dataStoreVOList as dataStoreVO>
<#if dataStoreVO.modelType=="object" || dataStoreVO.modelType=="list">
	<#if dataStoreVO.saveToSession?exists && dataStoreVO.saveToSession==true>
	<#else>
	/**
	 * 初始化数据集${dataStoreVO.cname!""}
	 * @return ${dataStoreVO.ename}
     */
    @SpringRequestAttribute("${dataStoreVO.ename}")
    public final ${dataStoreVO.type} initPageAttr_${dataStoreVO.ename}(){
    	return ${dataStoreVO.ename}();
    }
    
    /**
     * 初始化数据集${dataStoreVO.cname!""}
     * @return ${dataStoreVO.ename}
     */
    protected ${dataStoreVO.type} ${dataStoreVO.ename}(){
    	return null;
    }
    
  </#if>
</#if>
</#list>

	/**
	 * 初始化页面参数-权限
	 */
	@Override
	public void initVerifyAttr() {
	//初始化权限变量
	<#list dataStoreVOList[2].verifyIdList as rightVO  >
		boolean has_${rightVO.funcCode} = verify("${rightVO.parentFuncCode}", "${rightVO.funcCode}");
		setRightToAttr("${rightVO.funcCode}", has_${rightVO.funcCode});
	</#list>
	}
	
	/**
	 * 初始化页面使用的数据字典集合
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, Object>> initDicDatas() {
		List<String> lstCode = Arrays.asList(${dicCodes});
		List<List<String>> lstAttrs = Arrays.asList(${dicAttrs});
		return initDicDatas(lstCode, lstAttrs);
	}
	
	
	/**
	 * 初始化页面使用的枚举集合
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public List<Map<String, Object>> initEnumDatas() {
		List<String> lstCode = Arrays.asList(${eumCodes});
		List<List<String>> lstAttrs = Arrays.asList(${enumAttrs});
		return initEnumDatas(lstCode, lstAttrs);
	}

	/**
	 * 页面跳转
	 * @param sessionStatus session状态
	 * @return 页面URL
	 */
	@RequestMapping("${url}")
	public String pageInit(SessionStatus sessionStatus) {
		initPageAttr(sessionStatus);
		return "${jspURL}";
	}
}