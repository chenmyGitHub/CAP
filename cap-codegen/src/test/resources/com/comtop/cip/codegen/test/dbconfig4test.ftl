<?xml version="1.0" encoding="UTF-8" ?>  
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${entity.packagePath}.model">
	<#assign fullClassName="${entity.packagePath}.model.${entity.className}">
	<#assign alias="${entity.className ? uncap_first}">
	
	<generateOperate class="${fullClassName}"></generateOperate>
	
	<select id="query${entity.className}List" parameterType="${fullClassName}" resultType="${fullClassName}">
		SELECT * FROM ${entity.table}
	</select>
	
	<#--处理一对多的级联操作方法-->
	<#list entity.cascadeOMMethods as cascadeMethod>
    <delete id="delete${cascadeMethod.simpleName}ListBy${entity.className}Id" parameterType="java.lang.String" >
		DELETE FROM ${cascadeMethod.table} WHERE ${cascadeMethod.keyField} = ${'#'}{${alias}Id}
	</delete>
	
	</#list>
	<#--处理多对多的关联关系处理的方法-->
	<#list entity.cascadeMMMethods as cascadeMethod>
    <insert id="addRelated${cascadeMethod.simpleName}By${entity.className}" parameterType="java.util.Map" >
		INSERT INTO ${cascadeMethod.table}(${cascadeMethod.keyField},${cascadeMethod.sourceField},${cascadeMethod.targetField}) VALUES (${'#'}{relationId}, ${'#'}{${alias}Id},${'#'}{${cascadeMethod.simpleName ? uncap_first}Id})
	</insert>
	
	<delete id="deleteRelated${cascadeMethod.simpleName}By${entity.className}Id" parameterType="java.lang.String" >
		DELETE FROM ${cascadeMethod.table} WHERE ${cascadeMethod.sourceField} = ${'#'}{${alias}Id}
	</delete>
	
	<select id="queryRelated${cascadeMethod.simpleName}By${entity.className}Id" parameterType="java.lang.String" resultType="${cascadeMethod.pkg}.model.${cascadeMethod.simpleName}">
		SELECT T.* FROM ${cascadeMethod.table} C INNER JOIN ${cascadeMethod.targetTable} T ON C.${cascadeMethod.targetField} = T.${cascadeMethod.targetKey} WHERE C.${cascadeMethod.sourceField} = ${'#'}{${alias}Id}
	</select> 
	</#list>
	<#--处理级联查询产生的其他方法 -->
	<#list entity.cascadeQueryMethods as queryMethod>
    <select id="queryRelated${queryMethod.simpleName}By${entity.className}Id" parameterType="java.lang.String" resultType="${queryMethod.pkg}.model.${queryMethod.simpleName}">
		SELECT * FROM ${queryMethod.table} WHERE ${queryMethod.targetField} = ${'#'}{${alias}Id}
	</select> 
    </#list>
    <#--自定义查询方法 -->
    <#list entity.wrapperMethods as mt>
    <select id="${mt.simpleName}" >
		${mt.querySQL}
	</select> 
    </#list>
</mapper>