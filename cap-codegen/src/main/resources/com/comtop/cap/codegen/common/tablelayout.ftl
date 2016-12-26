<#list layoutVO.children as layoutVO>
<#if javaExpressionMap?? && javaExpressionMap[layoutVO.id]??>
	<c:if test="${r"${"}${javaExpressionMap[layoutVO.id].expression}${r"}"}">
</#if>
<#if layoutVO.type=="layout">
	<#if layoutVO.uiType=="table">
	<table id="${layoutVO.uiConfigId}" class="cap-table-fullWidth">
		<#if layoutVO.children?exists >
	        <#include "/common/tablelayout.ftl" >
	    </#if>
	</table>
	</#if>
	<#if layoutVO.uiType=="tr">
		<tr id="${layoutVO.uiConfigId}">
			<#if layoutVO.children?exists >
	            <#include "/common/tablelayout.ftl" >
	        </#if>
		</tr>
	</#if>
	<#if layoutVO.uiType=="td">
			<td id="${layoutVO.uiConfigId}" class="cap-td" ${layoutVO.style} ${layoutVO.colspan!}>
				<#if layoutVO.children?exists >
	                <#include "/common/tablelayout.ftl" >
	            </#if>
			</td>
	</#if>
</#if>
<#if layoutVO.type!="layout">
            <#if layoutVO.uiType=='Grid' || layoutVO.uiType=='EditableGrid'>
            	<table id="${layoutVO.uiConfigId}" uitype="${layoutVO.uiType}" ></table>
            <#elseif layoutVO.uiType=='Borderlayout' || layoutVO.uiType=='Panel' || layoutVO.uiType=='Tab' || layoutVO.uiType=='Editor'>
            	<div id="${layoutVO.uiConfigId}" uitype="${layoutVO.uiType}"></div>
            <#elseif layoutVO.uiType=='AtmSep'>
            	<div id="${layoutVO.uiConfigId}" valign="middle" align="left" style="border-right-style:none;border-left-style:none;border-top-style:none;border-bottom-style:none" uitype="${layoutVO.uiType}"></div>
            <#elseif layoutVO.uiType=='CodeArea'>
            	<div id="${layoutVO.uiConfigId}" uitype="${layoutVO.uiType}" >${layoutVO.options.text}</div>
            <#elseif layoutVO.uiType=='IncludePage'>  
            	<%@ include  file="${layoutVO.options.pageUrl}"%>
            <#elseif layoutVO.uiType=='ECharts'>
            	<div id="${layoutVO.uiConfigId}" uitype="${layoutVO.uiType}" style="height:${layoutVO.options.height};width:${layoutVO.options.width};"></div>
            <#else>
                <#if layoutVO.options.labelType?exists>
                   <#if layoutVO.options.labelType=="">
                <div id="${layoutVO.uiConfigId}" style="height:${layoutVO.options.height!};width:${layoutVO.options.width!};"></div>
                   <#else>
                <${layoutVO.options.labelType} id="${layoutVO.uiConfigId}" style="height:${layoutVO.options.height!};width:${layoutVO.options.width!};"></${layoutVO.options.labelType}>
                   </#if>
                <#else>
            	<span id="${layoutVO.uiConfigId}" uitype="${layoutVO.uiType}" ></span>
            	</#if>
            </#if>
</#if>
<#if javaExpressionMap?? && javaExpressionMap[layoutVO.id]??>
	</c:if>
</#if>
</#list>