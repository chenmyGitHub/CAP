<!--
/**********************************************************************
* ${cname}
* ${.now?string('yyyy-MM-dd')} ${author} 新建
**********************************************************************/
-->
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${cname}</title>
    <#list CSSFileList as filePath>
    <link rel="stylesheet" type="text/css" href="${filePath}">
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
	<script type="text/javascript" src="${filePath}"></script>
	</#list>
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
<#--
<#list pageAttributeVOList as pageAttributeVO>
var ${pageAttributeVO.attributeName}=cap.getURLParameter("${pageAttributeVO.attributeName}");
</#list>
-->
<#if webPath?exists >
var webPath = '${webPath}';
</#if>

<#list pageActionVOList as pageActionVO>
${pageActionVO.methodBody}
</#list>
        
jQuery(document).ready(function(){
	cap.beforePageInit.fire();
	cap.executeFunction("pageInitBeforeProcess");
	if(window['pageMode'] == "textmode" || window['pageMode'] == "readonly"){
		comtop.UI.scan[pageMode]=true;
	}
	comtop.UI.scan();
	cap.executeFunction("pageInitLoadData");
	cap.pageInit();
	cap.executeFunction("pageInitAfterProcess");
});
        
        
//页面控件属性配置
var uiConfig=${layoutVO.uiConfig}
</script>
</html>