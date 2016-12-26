<#include "/common/capCopyright.ftl">

package ${service.packagePath}.facade;

import org.springframework.stereotype.Service;
import ${service.packagePath}.facade.abs.Abstract${service.serviceName}Facade;

<#--导入关联类-->
<#if (service.impFacadeImports ? size > 0) >
<#list service.impFacadeImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${service.comment}扩展实现
 * 
 * @author CAP
 * @since 1.0
 * @version ${.now?date} CAP
 */
@Service(value="${service.aliasName}Facade")
public class ${service.serviceName}Facade extends Abstract${service.serviceName}Facade {
	//TODO 请在此编写自己的业务代码
	
	<#-- 处理实体中定义的方法 -->
    <#list service.wrapperMethods as mt >
    <#if mt.methodType == "blank">
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
    @Override
    @SoaMethod(alias="${mt.method.aliasName}")     
    ${mt.methodName}{
      <#if mt.returnComment != "" > 
      return null;
      <#else>
      	//todo
      </#if> 
    }
      </#if>
    </#list>
}
