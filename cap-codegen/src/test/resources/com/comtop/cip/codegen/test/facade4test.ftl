<#include "/copyright.ftl">

package ${entity.packagePath}.facade;

import java.util.List;
import com.comtop.cap.runtime.base.facade.BaseFacade;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import ${entity.packagePath}.appservice.${entity.className}AppService;
import ${entity.packagePath}.model.${entity.className};
<#if (entity.otherImports ? size > 0) >
<#list entity.otherImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${entity.comment} 业务逻辑处理类 门面
 * 
 * @author CIP
 * @since 1.0
 * @version ${.now?date} CIP
 */
@PetiteBean
public class ${entity.className}Facade extends BaseFacade {
	<#assign appService="${entity.className ? uncap_first}AppService"/>
	<#assign alias="${entity.className ? uncap_first}">
   /** 注入AppService **/
    @PetiteInject
    private ${entity.className}AppService ${appService};
    
    /**
     * 新增 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  ${entity.comment}
     * 
     */
    public Object insert${entity.className}(${entity.className} ${alias}) {
        return ${appService}.insert${entity.className}(${alias});
    }
    
    /**
     * 更新 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  更新结果
     * 
     */
    public boolean update${entity.className}(${entity.className} ${alias}) {
        return ${appService}.update${entity.className ? cap_first}(${alias});
    }
    
   /**
     * 删除 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  删除结果
     * 
     */
    public boolean delete${entity.className}(${entity.className} ${alias}) {
        return ${appService}.delete${entity.className}(${alias});
    }
    
    
    /**
     * 读取 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  ${entity.comment}
     * 
     */
    public ${entity.className} load${entity.className}(${entity.className} ${alias}) {
        return ${appService}.load${entity.className}(${alias});
    }
    
    /**
     * 根据${entity.comment}主键 读取 ${entity.comment}
     * 
     * @param id ${entity.comment}主键
     * @return  ${entity.comment}
     * 
     */
    public ${entity.className} load${entity.className}ById(String id) {
    	return ${appService}.load${entity.className}ById(id);
    }
    
     /**
     * 读取 ${entity.comment} 列表
     * 
     * @param condition 查询条件
     * @return  ${entity.comment}列表
     * 
     */
    public List<${entity.className}> query${entity.className}List(${entity.className} condition) {
        return ${appService}.query${entity.className}List(condition);
    }
    <#-- 处理实体中定义的方法 -->
    <#list entity.wrapperMethods as mt >
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
      <#if mt.methodType == 0 || mt.methodType == 1 || mt.methodType == 2>
      <#if mt.returnComment != "" >return </#if> ${appService}.${mt.simpleName}(${mt.paramterArgs});
      </#if>
    }
    </#list>

}