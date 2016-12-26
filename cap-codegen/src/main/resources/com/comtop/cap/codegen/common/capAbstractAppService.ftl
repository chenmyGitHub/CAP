<#--文件头注释-->
<#include "/common/capCopyright.ftl">

package ${entity.packagePath}.appservice.abs;

import ${entity.packagePath}.model.${entity.className};
<#--导入继承的基类-->
import ${entity.parentEntity.modelPackage}.appservice.${entity.parentEntity.engName}AppService;

<#if (entity.absServiceImports ? size > 0) >
<#list entity.absServiceImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${entity.comment} 业务逻辑处理类
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 * @param <T> 类泛型参数
 */
public abstract class Abstract${entity.entityName}AppService<T extends ${entity.className}> extends ${entity.parentEntity.engName}AppService<${entity.className}> {
	
	<#--<#if !entity.isProcessable()&&entity.wrapperMethods?size==0>-->
		//todo
	<#--</#if>-->
	
	<#-- 实体绑定流程后，添加流程相关方法 -->
	<#if entity.isProcessable()>
 	/** ${entity.comment}工作流ID */
   	public static final String ${entity.entityName?upper_case}_PROCESS_ID ="${entity.processId}";
	   	
    /**
     * 返回流程编码
     * 
     * @return ${entity.comment}工作流ID
     */
    @Override
    public String getProcessId() {
        return ${entity.entityName?upper_case}_PROCESS_ID;
    }
    
    /**
     * 短信发送时获取业务数据名称，例如：检修单；缺陷单等
     * 
     * @return 工作流名称
     */
    @Override
    public String getDataName() {
        return "${entity.comment}";
    }
	</#if> 
	
	<#-- 处理实体中的自定义查询方法和存储过程调用方法 -->
    <#list entity.wrapperMethods as mt >
    <#-- 自定义sql方法和存储过程调用方法执行生成方法体 -->
    <#if mt.methodType == "userDefinedSQL" || mt.methodType == "queryModeling" || mt.methodType == "procedure" || mt.methodType == "function">
    <#-- 方法注释 -->
    /**
     * ${mt.comment}
     * 
     <#list mt.parameterTypes as pt >
     * @param ${pt.alias} ${pt.comment}
     </#list>
     <#list mt.exceptionTypes as ex>
     * @throws ${ex.exceptionType} ${ex.comment}
     </#list>
     <#if mt.returnComment != "" >
     * @return  ${mt.returnComment}
     </#if>
     * 
     */
    ${mt.methodName}{
     	<#-- 如果方法返回类型为存储过程或函数 -->
     	<#if mt.methodType == "procedure" || mt.methodType == "function">
     	Map<String,Object> params = new HashMap<String,Object>();
     	    <#if (mt.dbObjectParameters?size>0)>
    		   <#list mt.dbObjectParameters as dbpt>
    	params.put("${dbpt.paramName}", ${dbpt.value});
    		   </#list>
     	    </#if>
     	<#else>
     	<#-- 拼装sql参数 -->
     	    <#if (mt.parameterTypes?size) == 1>
     	${mt.parameterTypes[0].parameterType} params = ${mt.parameterTypes[0].alias};
     	    <#elseif (mt.parameterTypes?size>1)>
     	Map<String,Object> params = new HashMap<String,Object>();
    		   <#list mt.parameterTypes as pt>
    	params.put("${pt.alias}", ${pt.alias});
    		   </#list>
    	    <#else>
    	Object params = null;
     	     </#if>
     	</#if>
     	<#-- 存储过程调用 -->
     	<#if mt.methodType == "procedure">
     	capBaseCommonDAO.selectOne("${entity.packagePath}.model.${entity.entity.engName?uncap_first}${mt.simpleName?cap_first}Procedure", params);
     	return params;
     	<#-- 函数调用 -->
     	<#elseif mt.methodType == "function">
     	capBaseCommonDAO.selectOne("${entity.packagePath}.model.${entity.entity.engName?uncap_first}${mt.simpleName?cap_first}Function", params);
     	return params;
     	<#else>
     	<#-- 自定义非查询类sql方法 -->
     	  <#if mt.methodOperateType ?? && mt.methodOperateType != "query">
     	     <#if mt.returnType == "void">
     	capBaseCommonDAO.${mt.methodOperateType}("${entity.packagePath}.model.${entity.entity.engName?uncap_first}_${mt.simpleName}", params);
     	     <#else>
     	return capBaseCommonDAO.${mt.methodOperateType}("${entity.packagePath}.model.${entity.entity.engName?uncap_first}_${mt.simpleName}", params);
     	     </#if>
     	  <#else>
     	<#-- 自定义查询类sql方法和查询建模  -->
              <#if mt.method.returnType.type == "java.util.List">
                  <#if mt.isNeedPagination()>
         return  capBaseCommonDAO.queryList("${entity.packagePath}.model.${entity.entity.engName?uncap_first}_${mt.simpleName}", params,${mt.parameterTypes[0].alias}.getPageNo(), ${mt.parameterTypes[0].alias}.getPageSize());
                  <#else>
         return  capBaseCommonDAO.queryList("${entity.packagePath}.model.${entity.entity.engName?uncap_first}_${mt.simpleName}", params);
                  </#if>
              <#else>
        return (${mt.returnType})capBaseCommonDAO.selectOne("${entity.packagePath}.model.${entity.entity.engName?uncap_first}_${mt.simpleName}", params);
              </#if>
          </#if>
        </#if>
     }
      
    </#if>  
    </#list>
}