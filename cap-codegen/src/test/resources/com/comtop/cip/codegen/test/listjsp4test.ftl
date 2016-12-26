<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
     <script type="text/javascript" src="../js/jquery-1.11.0.min.js" ></script>
    <title>${entity.comment}列表</title>
    <style type="text/css">
    	html {
			overflow: visible;
		}
		
		body {
			margin: 0px;
			font: 12px/1.5 Arial, "宋体", Helvetica, sans-serif;
			background-color: #EFF1F6;
		}
		
		.thw_wrap {
			border-top: 1px solid #BFCFDA;
			padding: 5px 0 0 0;
			height: 25px;
		}
		
		.thw_title {
			float: left;
			font: 12px "宋体";
			margin-top: 10px;
			margin-left: 5px;
			font-weight: bold;
			color: #333;
			margin-left: 5px;
		}
		
		.thw_operate {
			float: right;
			padding: 0 6px;
		}
		
		.content_wrap {
			padding: 12px;
			height: 100%;
			background-color: #FFFFFF;
		}
		
		table {
			border-collapse: collapse;
			border-spacing: 0;
			border-left: 1px solid #888;
			border-top: 1px solid #888;
		}
		
		th,td {
			border-right: 1px solid #888;
			border-bottom: 1px solid #888;
			padding: 5px 15px;
		}
		
		th {
			font-weight: bold;
			background: #BADBFB;
		}
    </style>
</head>
<body>
	<div class="thw_wrap">
		<div class="thw_title">${entity.comment}列表：</div>
		<div class="thw_operate">
			<button onclick="back()">返回</button>
			<button onclick="add()">新增</button>
		</div>
	</div>
	<div class="content_wrap">
	    <table>
	    	<thead>
	    		<tr>
	    			<#list entity.wrapperAttributes as attr>
	    			<#if (attr.alias != "id") && (attr.mutiple == -1 || attr.mutiple == 0 ||  attr.mutiple == 2) && 
		        	 (attr.attribute.attributeType !=-1 || attr.attribute.attributeType !=7 || attr.attribute.attributeType !=8) >
		    		<th>${attr.comment}</th>
		    		</#if>
		    		</#list>
		    		<th width="100px">业务操作</th>
	    		</tr>
	    	</thead>
	    	<tbody>
	    	<c:forEach var="bizVO" items="${'${'} bizVOs ${'}'}">
		        <tr>
		        	<#list entity.wrapperAttributes as attr>
		        	<#if (attr.alias != "id") && (attr.mutiple == -1 || attr.mutiple == 0 ||  attr.mutiple == 2) && 
		        	 (attr.attribute.attributeType !=-1 || attr.attribute.attributeType !=7 || attr.attribute.attributeType !=8) >
		            <td >${'${'}bizVO.${attr.alias ? uncap_first} ${'}'}</td>
		            </#if>
		            </#list>
		            <td><a href="javascript:void(0)" onclick="operate('view','${'${'}bizVO.id ${'}'}')">查看</a>&nbsp;
		            	<a href="javascript:void(0)" onclick="operate('modify','${'${'}bizVO.id ${'}'}')">修改</a>&nbsp;
		            	<a href="javascript:void(0)" onclick="operate('delete','${'${'}bizVO.id ${'}'}')">删除</a>
		            </td>
		        </tr>
		    </c:forEach>
	        </tbody>
	     </table>
	    <form id="hiddenForm" name="hiddenForm" method="post" action="sample.dispatcher" hidden="hidden">
	    	<input type="hidden" hidden="true" name="target" value="${entity.entity.id}">
	    	<input id="operate" type="hidden" hidden="true" name="operate">
	    	<input id="id" name="id" type="hidden" hidden="true">
	    </form>
     </div>
</body>
<script type="text/javascript">
function add(){
	operate("add","");
}

function back(){
	 window.location.href="../index.jsp"; 
}

function operate(o,id){
	${'$("#'}operate").val(o);
	${'$("#'}id").val(id);
	hiddenForm.submit();
}

function refresh(){
	${'$("#'}operate").val("list");
	hiddenForm.submit();
}
</script>
</html>