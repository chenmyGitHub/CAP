<%
/**********************************************************************
* ${cname}
* ${.now?string('yyyy-MM-dd')} ${author} 新建
**********************************************************************/
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<#list JSPFileList as filePath>
<%@ include file="${filePath}" %>
</#list>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${cname}</title>
    <#list CSSFileList as filePath>
    <top:link href="${filePath}"/>
	</#list>
    <style type="text/css">
		<#if minWidth?exists && minWidth != "">
    	.cap-page{
    		width: ${minWidth};
    		<#if minWidth == "100%"&&pageMinWidth?exists && pageMinWidth != "">
    		min-width: ${pageMinWidth};
    		padding: 0;
    		</#if>
    	}
    	</#if>
    </style>
	<#list JSFileList as filePath>
	<top:script src='${filePath}'></top:script>
	</#list>
	<top:verifyRight resourceString="[{menuCode:'${code}'}]"/> 
    
</head>
<body>
<div id="pageRoot" class="cap-page">
<div class="cap-area" style="width:100%;">
<#if layoutVO.children?exists >
    <#include "/common/tablelayout.ftl" >
</#if>
</div>
</div>
</body>

<script type="text/javascript">
<#list pageAttributeVOList as pageAttributeVO>
var ${pageAttributeVO.attributeName}=<%=com.comtop.top.core.util.JsonUtil.objectToJson(request.getAttribute("${pageAttributeVO.attributeName}"))%>;
</#list>

<#list dataStoreVOList[2].verifyIdList as rightVO>
var ${rightVO.funcCode}=<%=com.comtop.top.core.util.JsonUtil.objectToJson(request.getAttribute("${rightVO.funcCode}"))%>;
</#list>

cap.dicDatas=<%=com.comtop.top.core.util.JsonUtil.objectToJson(request.getAttribute("dicDatas"))%>;

<#if sessionKeys?? && sessionKeys?length gt 0>
cap.dwrAction=${modelName}Action;
cap.sessionAttribute=cap.getSessionAttributes("${sessionKeys}");
</#if>

<#list dataStoreVOList as dataStoreVO>
	<#if dataStoreVO.modelType=="list" >
	  <#if dataStoreVO.saveToSession?exists && dataStoreVO.saveToSession==true>
var ${dataStoreVO.ename}=cap.getSessionAttribute("${dataStoreVO.ename}",true);
	  <#else>
var ${dataStoreVO.ename}=[];
	  </#if>
	</#if>
	<#if dataStoreVO.modelType=="object" >
	   <#if dataStoreVO.saveToSession?exists && dataStoreVO.saveToSession==true>
var ${dataStoreVO.ename}=cap.getSessionAttribute("${dataStoreVO.ename}");
	  <#else>
var ${dataStoreVO.ename}={};
      </#if>
	</#if>
</#list>

<#list dataStoreVOList as dataStoreVO>
	<#if dataStoreVO.modelType=="pageConstant" >
		<#list dataStoreVO.pageConstantList as pageConstantVO>
			<#if pageConstantVO.constantType=="String">
var ${pageConstantVO.constantName}="${pageConstantVO.constantValue}";
			</#if>
			<#if pageConstantVO.constantType!="String" && pageConstantVO.constantType!="url">
var ${pageConstantVO.constantName}=${pageConstantVO.constantValue};
			</#if>
			<#if pageConstantVO.constantType=="url">
var ${pageConstantVO.constantName}=${r"'${pageScope.cuiWebRoot}'"}+${pageConstantVO.constantValue};
			</#if>
		</#list>
	</#if>
</#list>

<#list pageActionVOList as pageActionVO>
${pageActionVO.methodBody}
</#list>

//页面初始化状态
function pageInitState(){
<#list pageComponentExpressionVOList as expressionVO>
	<#if expressionVO.expression?exists && expressionVO.expression != "">
		<#if expressionVO.expressionType=="java" && expressionVO.hasSetState==true>
		<c:if test="${r"${"}${expressionVO.expression}${r"}"}">
	    </#if>
	    <#if expressionVO.expressionType=="js" >
	    if(${expressionVO.expression}){
	    </#if>
		<#list expressionVO.pageComponentStateList as pageComponentStateVO>
		<#if expressionVO.expressionType=="js" || (expressionVO.expressionType=="java" && expressionVO.hasSetState==true)>
			<#if pageComponentStateVO.state=="readOnly" >
			cap.setUIState('${pageComponentStateVO.uiConfigId}',"readOnly");
		    </#if>
		    <#if pageComponentStateVO.state=="hide" >
		    cap.setUIState('${pageComponentStateVO.uiConfigId}',"hide");
		    </#if>
		    <#if pageComponentStateVO.state=="edit" >
		    cap.setUIState('${pageComponentStateVO.uiConfigId}',"edit");
		    </#if>
		    <#if pageComponentStateVO.hasValidate==false>
	        cap.disValid('${pageComponentStateVO.uiConfigId}', true);
		    </#if>
		    <#if pageComponentStateVO.hasValidate==true>
		    cap.disValid('${pageComponentStateVO.uiConfigId}', false);
		    </#if>
		</#if>
		</#list>
		<#if expressionVO.expressionType=="java" && expressionVO.hasSetState==true>
		</c:if>
	    </#if>
	    <#if expressionVO.expressionType=="js" >
	    }
	    </#if>
    </#if>
</#list>
}
        
jQuery(document).ready(function(){
	cap.beforePageInit.fire();
	cap.executeFunction("pageInitBeforeProcess");
	if(window['pageMode'] == "textmode" || window['pageMode'] == "readonly"){
		comtop.UI.scan[pageMode]=true;
	}
	comtop.UI.scan();
	cap.errorHandler();
	cap.executeFunction("pageInitLoadData");
	cap.pageInit();
	pageInitState();
	cap.executeFunction("pageInitAfterProcess");
});
        
        
//页面控件属性配置
var uiConfig=${layoutVO.uiConfig}
</script>
</html>