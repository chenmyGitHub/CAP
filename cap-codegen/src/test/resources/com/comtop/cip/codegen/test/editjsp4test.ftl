<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>${entity.comment}编辑页面</title>
		<script type="text/javascript" src="../js/jquery-1.11.0.min.js" ></script>
		<style type="text/css">
			body {
				margin: 0;
				font-size: 12px;
			}
			h4 {
				font-size: 14px;
			}
			.formWrap {
				padding: 10px;
			}
			.formtable td {
				line-height: 25px;
				height: 25px;
			}
			.formtable .td_label {
				text-align: right;
			}
			.formtable .td_data {
				text-align: left;
			}
			.formtable .bottom {
				text-align: center;
			}
			.odd {
				background: #fff4e5
			}
		</style>
	</head>
	<body>
	<c:set var="disabled" value='${"${"}"view" eq operate ? "disabled" : "" ${"}"}'/>
		<div class="formWrap">
			<form id="hiddenForm" name="hiddenForm" method="post" action="sample.dispatcher" >
				<table class="formtable">
					<tbody>
						<#-- 实体基本属性 -->
						<#list entity.wrapperAttributes as attr>
						<#if attr.mutiple == -1 && !(attr.attribute.relationshipId??) && attr.alias != "id">
						<tr>
							<td class="td_label">${attr.comment}：</td>
							<td class="td_data">
							<input type="text" name="${attr.alias}" value="${'${'}bizVO.${attr.alias}${'}'}" ${"${"}disabled${"}"} size="60"/>
							</td>
						</tr>
						<#elseif !attr.relatedByOpposite && !attr.cascade && (attr.mutiple == 0 || attr.mutiple == 2) >
						<#assign relatedAttrAlias = attr.relatedAttrType ? uncap_first>
						<#--关联属性(一对一或者多对一)-->
						<tr>
							<td class="td_label">${attr.comment}：</td>
							<td class="td_data">
								<select name="${attr.alias}Id" ${"${"}disabled${"}"} style="width: 397px">
									<option value="">--------请选择--------</option>
									<c:forEach var="${relatedAttrAlias}" items="${'${'}${relatedAttrAlias}List${'}'}">  
								     <option value="${'${'}${relatedAttrAlias}.id ${'}'}"  ${'${'} bizVO.${attr.alias}Id eq ${relatedAttrAlias}.id ? "selected" : "" ${'}'}>
								     <c:out value="${'${'}${relatedAttrAlias}.${attr.relatedEntity.attributes[1].englishName ? uncap_first} ${'}'}"/>
								     </option>
								    </c:forEach>  
								</select>
							</td>
						</tr>
						<#--关联属性(多对多)-->
						<#elseif !attr.relatedByOpposite && !attr.cascade && attr.mutiple == 3 >
						<#assign relatedAttrAlias = attr.relatedAttrType ? uncap_first>
						<tr>
							<td class="td_label">${attr.comment}：</td>
							<td class="td_data">
								<select multiple="multiple" name="${attr.alias}IdList" ${"${"}disabled${"}"} style="width: 392px">
									<option value="">--------请选择--------</option>  
								    <c:forEach var="${relatedAttrAlias}" items="${'${'}${relatedAttrAlias}List${'}'}">  
								     <c:set var="selected" value=""></c:set> 
								     <c:forEach var="${relatedAttrAlias}Id" items="${'${'}bizVO.${attr.alias}IdList ${'}'}">
								     	<c:if test="${'${'}${relatedAttrAlias}Id == ${relatedAttrAlias}.id ${'}'}">  
                                             <c:set var="selected" value="selected"></c:set>  
                                        </c:if>  
								     </c:forEach>
								     <option value="${'${'}${relatedAttrAlias}.id ${'}'}" ${'${'}selected ${'}'}>
								     <c:out value="${'${'}${relatedAttrAlias}.${attr.relatedEntity.attributes[1].englishName ? uncap_first} ${'}'}"/>
								    </c:forEach>  
								</select>
							</td>
						</tr>
						<#-- 组合属性(一对一) -->
						<#elseif !attr.relatedByOpposite && attr.cascade && attr.mutiple == 0 >
						<tr>
							<td colspan="2">
							组合属性(一对一) ：${attr.comment}
							</td>
						<tr>
						<tr>
							<td>
								<table>
									<tbody>
										<#assign relatedAlias = relatedEntity.englishName ? uncap_first>
										<#list relatedEntity.attributes as combinaAttr>
										<tr>
											<td class="td_label">${combinaAttr.chineseName}：</td>
											<td class="td_data">
											<input type="text" name="${combinaAttr.englishName ? uncap_first}.${combinaAttr.englishName ? uncap_first}" value="${'${'} bizVO.${combinaAttr.englishName ? uncap_first}.${combinaAttr.englishName ? uncap_first} ${'}'}" ${"${"}disabled${"}"} size="60"/>
											</td>
										</tr>
										</#list>
										<input type="hidden" name="${relatedAlias}List[${item}].id" value="${'${'} bizVO.${relatedAlias}List[${item}].id ${'}'}" />
										<input type="hidden" name="${relatedAlias}List[${item}].${attr.alias}Id" value="${'${'} bizVO.${relatedAlias}List[${item}].${attr.alias}Id ${'}'}" />
									</tbody>
								</table>
							</td>
						</tr>
						<#elseif !attr.relatedByOpposite && attr.cascade && attr.mutiple == 1 >
						<tr>
							<td colspan="2">
							组合属性(一对多)：${attr.comment}
							</td>
						<tr>
						<tr>
							<td colspan="2">
								<table class="formtable">
									<thead>
										<tr>
											<th></th>
											<#list attr.relatedEntity.attributes as combinaAttr>
											<#if !(combinaAttr.relationshipId??) && combinaAttr.englishName != "id" >
											<th>${combinaAttr.chineseName}</th>
											</#if>
											</#list>
										</tr>
									</thead>
									<tbody>
										<#list 0..4 as item>
										<tr>
											<td>
											<input type="button" onclick="removeRow(this)" value="删除行" ${"${"}disabled${"}"}>
											<input type="hidden" name="${attr.alias}[${item}].id" value="${'${'} bizVO.${attr.alias}[${item}].id ${'}'}" />
											</td>
											<#list attr.relatedEntity.attributes as combinaAttr>
											<#if !(combinaAttr.relationshipId??) && combinaAttr.englishName != "id" >
											<td>
											<input type="text" name="${attr.alias}[${item}].${combinaAttr.englishName ? uncap_first}" value="${'${'} bizVO.${attr.alias}[${item}].${combinaAttr.englishName ? uncap_first} ${'}'}" ${"${"}disabled${"}"}/>
											</td>
											</#if>
											</#list>
										</tr>
										</#list>
									</tbody>
								</table>
							</td>
						</tr>
						</#if>
 						</#list>
					</tbody>
				</table>
				<input type="hidden" hidden="true" name="target" value="${entity.entity.id}">
				<input type="hidden" hidden="true" name="operate" value="${'${'}operate ${'}'}">
				<input type="hidden" hidden="true" name="id" value="${'${'}bizVO.id ${'}'}">
				<input type="submit" ${"${"}disabled${"}"}>
			</form>
		</div>
	</body>
	<script type="text/javascript">
		function removeRow(obj) { 
			${'$'}(obj).closest('tr').remove();
		}
	</script>
</html>