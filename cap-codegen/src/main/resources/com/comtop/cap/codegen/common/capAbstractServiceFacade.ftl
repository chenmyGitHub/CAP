<#include "/common/capCopyright.ftl">

package ${service.packagePath}.facade.abs;

<#--导入关联类-->
<#if (service.absFacadeImports ? size > 0) >
<#list service.absFacadeImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${service.comment} 业务逻辑处理类 门面
 * 
 * @author CAP
 * @since 1.0
 * @version ${.now?date} CAP
 */
public abstract class Abstract${service.serviceName}Facade {

	<#-- 处理查询实体中定义的方法 -->
	<#if (service.wrapperMethods?size>0) >
	<#assign appService="${service.serviceName ? uncap_first}AppService"/>
	<#assign alias="${service.className ? uncap_first}">
    
    <#list service.wrapperMethods as mt >
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
    @SoaMethod(alias="${mt.method.aliasName}")     
     <#-- 空方法 -->
    abstract ${mt.methodName};
	</#list>
    </#if>
}