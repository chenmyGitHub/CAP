<#--文件头注释-->
<#include "/common/capCopyright.ftl">

package ${entity.packagePath}.facade;

<#if entity.classPattern != "abstract">
import org.springframework.stereotype.Service;
</#if>
import ${entity.packagePath}.facade.abs.Abstract${entity.entityName}Facade;
import ${entity.packagePath}.model.${entity.className};

<#--导入关联类-->
<#if (entity.impFacadeImports ? size > 0) >
<#list entity.impFacadeImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${entity.comment}扩展实现
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 */
<#if entity.classPattern == "abstract">
public abstract class ${entity.entityName}Facade extends Abstract${entity.entityName}Facade<${entity.className}> {
<#else>
@Service(value="${entity.entity.aliasName}Facade")
public class ${entity.entityName}Facade extends Abstract${entity.entityName}Facade<${entity.className}> {
</#if>
	//TODO 请在此编写自己的业务代码
	
	<#-- 处理实体中定义的方法 -->
    <#list entity.wrapperMethods as mt >
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
