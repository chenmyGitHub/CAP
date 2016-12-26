<#include "/copyright.ftl">

package ${entity.packagePath}.appservice;

import java.util.List;
import com.comtop.cap.runtime.base.appservice.BaseAppService;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import ${entity.packagePath}.dao.${entity.className}DAO;
import ${entity.packagePath}.model.${entity.className};
<#if (entity.otherImports ? size > 0) >
<#list entity.otherImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${entity.comment} 业务逻辑处理类
 * 
 * @author CIP
 * @since 1.0
 * @version ${.now?date} CIP
 */
@PetiteBean
public class ${entity.className}AppService extends BaseAppService {
	<#assign dao="${entity.className ? uncap_first}DAO"/>
	<#assign alias="${entity.className ? uncap_first}">
   /** 注入DAO **/
    @PetiteInject
    protected ${entity.className}DAO ${dao};
    
    /**
     * 新增 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  ${entity.comment}Id
     * 
     */
    public Object insert${entity.className}(${entity.className} ${alias}) {
        return ${dao}.insert${entity.className}(${alias});
    }
    
    /**
     * 更新 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  更新成功与否
     * 
     */
    public boolean update${entity.className}(${entity.className} ${alias}) {
        return ${dao}.update${entity.className}(${alias});
    }
    
   /**
     * 删除 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  删除成功与否
     * 
     */
    public boolean delete${entity.className}(${entity.className} ${alias}) {
        return ${dao}.delete${entity.className}(${alias});
    }
    
    
    /**
     * 读取 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  ${entity.comment}
     * 
     */
    public ${entity.className} load${entity.className}(${entity.className} ${alias}) {
        return ${dao}.load${entity.className}(${alias});
    }
    
    /**
     * 根据${entity.comment}主键读取 ${entity.comment}
     * 
     * @param id ${entity.comment}主键
     * @return  ${entity.comment}
     * 
     */
    public ${entity.className} load${entity.className}ById(String id) {
    	return ${dao}.load${entity.className}ById(id);
    }
    
    /**
     * 读取 ${entity.comment} 列表
     * 
     * @param condition 查询条件
     * @return  ${entity.comment}列表
     * 
     */
    public List<${entity.className}> query${entity.className}List(${entity.className} condition) {
        return ${dao}.query${entity.className}List(condition);
    }
    
    <#list entity.wrapperMethods as mt>
    /**
     * ${mt.comment}
     * 
     <#list mt.parameterTypes as pt>
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
    	<#if mt.methodType == 0 >
   		//TODO blank;
      	<#if mt.returnComment != "" >
        return  null;
        </#if>
   		<#elseif mt.methodType == 1 || mt.methodType == 2>
   		return ${dao}.${mt.simpleName}(${mt.paramterArgs});
   		</#if>
    }
    </#list>
}