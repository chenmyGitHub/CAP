<#include "/copyright.ftl">

package ${entity.packagePath}.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import ${entity.packagePath}.facade.${entity.className}Facade;
import ${entity.packagePath}.model.${entity.className};

<#if (entity.otherImports ? size > 0) >
<#list entity.otherImports as imp>
import ${imp};
</#list>
</#if>

<#--关联属性(一对一或者多对一)-->
<#list entity.relatedTypes as relatedType>
import ${relatedType.pkg}.facade.${relatedType.simpleName}Facade;
import ${relatedType.pkg}.model.${relatedType.simpleName};
</#list>
/**
 * ${entity.comment}Action
 * 
 * 
 * @author CIP
 * @since 1.0
 * @version ${.now?date} CIP
 */
@PetiteBean
public class ${entity.className}Action {
	<#assign alias =  entity.className ? uncap_first>
   
    /** 注入Facade **/
    @PetiteInject
    private ${entity.className}Facade ${alias}Facade;
    
    <#--关联属性(一对一或者多对一)-->
	<#list entity.relatedTypes as relatedType>
    /** 注入${relatedType.simpleName}Facade **/
    @PetiteInject
    private ${relatedType.simpleName}Facade ${relatedType.simpleName ? uncap_first}Facade;
    </#list>
    
    /**
     * 新增${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void add${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        request.setAttribute("operate", "insert");
        setRelatedEntityToAttribute(request, response);
    }
    
    /**
     * 保存${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     * 
     */
    public void insert${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ${alias}.setId(null);
        ${alias}Facade.insert${entity.className}(${alias});
        refresh${entity.className}(${alias}, request, response);
    }
    
    /**
     * 刷新${entity.comment}列表页面
     * 
     * @param ${alias} 销售订单对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void refresh${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/sample/sample.dispatcher?target=${entity.entity.id}&operate=list")
            .forward(request, response);
    }
    
    /**
     * 查看${entity.comment}
     * 
     * @param ${alias} 销售订单对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void view${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ${entity.className} obj${entity.className} = this.load${entity.className}(${alias});
        request.setAttribute("bizVO", obj${entity.className});
        request.setAttribute("operate", "view");
        setRelatedEntityToAttribute(request, response);
    }
    
    /**
     * 修改${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void modify${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ${entity.className} obj${entity.className} = this.load${entity.className}(${alias});
        request.setAttribute("bizVO", obj${entity.className} );
        request.setAttribute("operate", "update");
        setRelatedEntityToAttribute(request, response);
    }
    
    /**
     * 刷新列表页面
     * 
     * @param ${alias} ${entity.comment}对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void list${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response)throws Exception {
        List<${entity.className}> lst${entity.className} = this.query${entity.className}List(null);
        request.setAttribute("bizVOs", lst${entity.className});
        request.getRequestDispatcher("/sample/pages/${entity.className}List.jsp").forward(request, response);
    }
    
    /**
     * 更新${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void update${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ${alias}Facade.update${entity.className}(${alias});
        this.refresh${entity.className}(${alias}, request, response);
    }
    
    /**
     * 删除${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    public void delete${entity.className}(${entity.className} ${alias}, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        ${alias}Facade.delete${entity.className}(${alias});
        this.refresh${entity.className}(${alias}, request, response);
    }
    
    /**
     * 读取${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return ${entity.comment}
     */
    public ${entity.className} load${entity.className}(${entity.className} ${alias}) {
        return ${alias}Facade.load${entity.className}(${alias});
    }
    
    /**
     * 读取${entity.comment} 列表
     * 
     * @param condition 查询条件
     * @return ${entity.comment}列表
     * 
     */
    public List<${entity.className}> query${entity.className}List(${entity.className} condition) {
        return ${alias}Facade.query${entity.className}List(condition);
    }
    
    /**
     * 查询管理属性并跳转到编辑页面
     * 
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    private void setRelatedEntityToAttribute(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        <#--关联属性(一对一或者多对一)-->
		<#list entity.relatedAttributes as relatedAttr>
		<#assign relatedType =  relatedAttr.type>
		<#assign relatedTypeAlias =  relatedType.simpleName ? uncap_first>
        List<${relatedType.simpleName}> lst${relatedType.simpleName}= ${relatedTypeAlias}Facade.query${relatedType.simpleName}List(null);
        request.setAttribute("${relatedTypeAlias}List", lst${relatedType.simpleName});
        </#list>
        request.getRequestDispatcher("/sample/pages/${entity.className}Edit.jsp").forward(request, response);
    }
    
}
