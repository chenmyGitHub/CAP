<#include "/common/capCopyright.ftl">

package ${modelPackage}.action.abs;

import com.comtop.cap.runtime.base.action.BaseAction;
<#list dataStoreVOList as dataStoreVO>
    <#if dataStoreVO.saveToSession?exists && dataStoreVO.saveToSession==true>
import com.comtop.cip.jodd.madvoc.ScopeType;
         <#break>
    </#if>
</#list>
import com.comtop.cip.jodd.madvoc.meta.Action;
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
public abstract class Abstract${modelName}Action extends BaseAction {

<#list pageAttributeVOList as pageAttributeVO>
	/**
	 * ${pageAttributeVO.attributeDescription}
	 */
	@InOut
	<#if pageAttributeVO.attributeType=="String">
	<#if pageAttributeVO.attributeValue=="">
	protected ${pageAttributeVO.attributeType} ${pageAttributeVO.attributeName};
	</#if>
	<#if pageAttributeVO.attributeValue!="">
	protected ${pageAttributeVO.attributeType} ${pageAttributeVO.attributeName}="${pageAttributeVO.attributeValue}";
	</#if>
	</#if>
	<#if pageAttributeVO.attributeType!="String">
	<#if pageAttributeVO.attributeValue=="">
	protected ${pageAttributeVO.attributeType} ${pageAttributeVO.attributeName};
	</#if>
	<#if pageAttributeVO.attributeValue!="">
	protected ${pageAttributeVO.attributeType} ${pageAttributeVO.attributeName}=${pageAttributeVO.attributeValue};
	</#if>
	</#if>
	
</#list>
<#list dataStoreVOList[2].verifyIdList as rightVO>
	/**
	 * ${rightVO.description!""}
	 */
	@InOut
	protected boolean ${rightVO.funcCode};
</#list>

<#list dataStoreVOList as dataStoreVO>
<#if dataStoreVO.modelType=="object" || dataStoreVO.modelType=="list">
	/**
	 * ${dataStoreVO.cname!""}
	 */
	<#if dataStoreVO.saveToSession?exists && dataStoreVO.saveToSession==true>
	@InOut(scope = ScopeType.SESSION)
	<#else>
    @InOut
    </#if>
    protected ${dataStoreVO.type} ${dataStoreVO.ename};
</#if>
</#list>

	/**
	 * 初始化页面参数-权限
	 */
	@Override
	public void initVerifyAttr() {
	//初始化权限变量
	<#list dataStoreVOList[2].verifyIdList as rightVO  >
		${rightVO.funcCode} = verify("${rightVO.parentFuncCode}", "${rightVO.funcCode}");
	</#list>
	}
	
	/**
	 * 初始化页面使用的数据字典集合
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public void initDicDatas() {
		List<String> lstCode = Arrays.asList(${dicCodes});
		List<List<String>> lstAttrs = Arrays.asList(${dicAttrs});
		initDicDatas(lstCode, lstAttrs);
	}
	
	
	/**
	 * 初始化页面使用的枚举集合
	 */
	@Override
	@SuppressWarnings({ "unchecked" })
	public void initEnumDatas() {
		List<String> lstCode = Arrays.asList(${eumCodes});
		List<List<String>> lstAttrs = Arrays.asList(${enumAttrs});
		initEnumDatas(lstCode, lstAttrs);
	}

	/**
	 * 页面跳转
	 * @return 页面URL
	 */
	@Action("${url}")
	public String pageInit() {
		initPageAttr();
		return "${jspURL}";
	}
}