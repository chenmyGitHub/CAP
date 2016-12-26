<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${entity.packagePath}.model">

<#if entity.classPattern != "abstract" && entity.table??>
	<#assign alias="${entity.entityName ? uncap_first}">
	<#assign fullClassName="${entity.packagePath}.model.${entity.className}">
	
<generateOperate class="${fullClassName}"></generateOperate>
	
<sql id = "${alias}_cascade_query_condition" >
<#list entity.wrapperAttributes as attr>
<#if attr.cascadeExpr??>
	${attr.cascadeExpr}
</#if>
</#list>
</sql>
	
<sql id = "${alias}_default_query_condition" >
<#if entity.isCustomSqlConditionEnable()>
    ${entity.customSqlCondition}
<#else>
	<#list entity.wrapperAttributes as attr>
	<#if attr.queryExpr??>
	${attr.queryExpr}
	</#if>
	<#if attr.queryRangeExpr1??>
	${attr.queryRangeExpr1}
	</#if>
	<#if attr.queryRangeExpr2??>
	${attr.queryRangeExpr2}
	</#if>
	</#list>
</#if>
</sql>
		
<#-- 取父实体元数据中对应的sql方法 -->
<#list entity.wrapperOverrideMethods as overrideMethod>
${overrideMethod.completeGrammar}
</#list>
	
</#if>   
<#-- 查询实体也需要默认查询条件 -->
<#if entity.entityType?? && entity.entityType=="query_entity">
<#assign alias="${entity.entityName ? uncap_first}">

<sql id = "${alias}_default_query_condition" >
	<#list entity.wrapperAttributes as attr>
		<#if attr.queryExpr??>
${attr.queryExpr}
		</#if>
		<#if attr.queryRangeExpr1??>
${attr.queryRangeExpr1}
		</#if>
		<#if attr.queryRangeExpr2??>
${attr.queryRangeExpr2}
		</#if>
	</#list>
</sql>

</#if>
<#-- 生成自定义sql方法的sql -->
<#list entity.wrapperMethods as mt>
	<#if mt.methodType == "userDefinedSQL">
		 <#-- 自定义非查询类sql方法 -->
		 <#if mt.methodOperateType != "query">
<${mt.methodOperateType} id="${entity.entity.engName?uncap_first}_${mt.simpleName}" parameterType="${mt.sqlParamType}">
 ${mt.querySQL!}
</${mt.methodOperateType}> 
		 <#else>
<#-- 自定义查询类sql方法 -->
<select id="${entity.entity.engName?uncap_first}_${mt.simpleName}" <#if mt.sqlParamType?? && mt.sqlParamType !="" >parameterType="${mt.sqlParamType}"</#if> resultType="${mt.sqlReturnType}">
 ${mt.querySQL!}
<#if mt.sortSQL ??>
 ${mt.sortSQL}
</#if>
</select> 
	     </#if>
	</#if>
	<#-- 查询建模 -->
	<#if mt.methodType == 'queryModeling'>
<select id="${entity.entity.engName?uncap_first}_${mt.simpleName}" <#if mt.queryModel.parameterType?? && mt.queryModel.parameterType !="" >parameterType="${mt.queryModel.parameterType}"</#if> resultType="${mt.queryModel.resultType}">
${mt.queryModel.sql}
</select> 	
	</#if>
	
	<#-- 存储过程方法 -->
	<#if mt.methodType == "procedure">
<select id="${entity.entity.engName?uncap_first}${mt.simpleName?cap_first}Procedure" parameterType="java.util.Map" resultType="java.util.Map"  statementType="CALLABLE">  
 <![CDATA[  
      ${mt.procedureMessage} 
 ]]>
</select>
	</#if>
	<#-- 函数调用 -->
	<#if mt.methodType == "function">
<select id="${entity.entity.engName?uncap_first}${mt.simpleName?cap_first}Function" parameterType="java.util.Map" resultType="java.util.Map" statementType="CALLABLE">  
 <![CDATA[  
     ${mt.functionMessage}
 ]]>
</select>
	</#if>
</#list>
</mapper>