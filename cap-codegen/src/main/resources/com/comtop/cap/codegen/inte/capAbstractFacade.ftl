<#--文件头注释-->
<#include "/common/capCopyright.ftl">

package ${entity.packagePath}.facade.abs;


import ${entity.packagePath}.appservice.${entity.entityName}AppService;
import ${entity.packagePath}.model.${entity.className};
import com.comtop.cap.runtime.base.util.BeanContextUtil;
<#--继承的基类-->
import ${entity.parentEntity.modelPackage}.facade.${entity.parentEntity.engName}Facade;

<#--导入关联类-->
<#if (entity.absFacadeImports ? size > 0) >
<#list entity.absFacadeImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${entity.comment}业务逻辑处理类门面
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 * @param <T> 类泛型参数
 */
public abstract class Abstract${entity.entityName}Facade<T extends ${entity.className}> extends ${entity.parentEntity.engName}Facade {

	<#assign appService="${entity.entityName ? uncap_first}AppService"/>
	<#assign alias="${entity.className ? uncap_first}">
	
	/** 注入AppService **/
    protected ${entity.entityName}AppService ${appService} = (${entity.entityName}AppService)BeanContextUtil.getBean("${entity.entity.aliasName}AppService");
	
     /**
     * @see com.comtop.cap.runtime.base.facade.CapBaseFacade#getAppService()
     */
    @Override
    public ${entity.entityName}AppService getAppService() {
    	return ${appService};
    }
    
<#-- 处理实体中定义的方法 -->
<#list entity.wrapperMethods as mt >
    
   <#-- 查询重写不生成方法 -->
   <#if mt.methodType != "queryExtend">
   
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
    @SoaMethod(alias="${mt.method.aliasName}")   
     <#-- 空方法 -->
    <#if mt.methodType == "blank">
    abstract ${mt.methodName};
     <#-- 级联方法 -->
    <#elseif mt.methodType == "cascade">
     ${mt.methodName}{
      ${mt.cascadeCode}
     }
     <#-- 存储过程调用 或函数调用 方法-->
     <#elseif (mt.methodType == "procedure" || mt.methodType == "function")>
     ${mt.methodName}{
 	    return getAppService().${mt.singleMethodName}(${mt.paramterArgs});
     }
     <#-- 自定义sql方法-->
     <#elseif (mt.methodType == "userDefinedSQL" || mt.methodType == "queryModeling")>
     <#-- 自定义非查询类sql方法 -->
     <#if mt.returnType == "void">
     ${mt.methodName}{
        getAppService().${mt.singleMethodName}(${mt.paramterArgs});
     }
     <#else>
     <#-- 自定义查询类sql方法 -->
     ${mt.methodName}{
    	return getAppService().${mt.singleMethodName}(${mt.paramterArgs});
     }
     </#if>
   	 <#-- 自定义查询分页查询 -->
     <#elseif mt.methodType == "pagination">  
      <#-- 分页方法 -->
     ${mt.methodName}{
    	final Map<String, Object> ret = new HashMap<String, Object>(2);
        int count = getAppService().${mt.assoMethodName}Count(${mt.paramterArgs});
        List<T> voList = null;
        if (count > 0) {
        	${mt.paramterArgs?split(",")[0]}.setPageNo(getRightPageNo(count, ${mt.paramterArgs?split(",")[0]}.getPageNo(), ${mt.paramterArgs?split(",")[0]}.getPageSize()));
            voList = getAppService().${mt.assoMethodName}(${mt.paramterArgs});
        }
        ret.put("list", voList);
        ret.put("count", count);
        ret.put("pageNo", ${mt.paramterArgs?split(",")[0]}.getPageNo());
        return ret;
     }
    </#if>
   </#if>
</#list>

}