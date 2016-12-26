<#include "/copyright.ftl">

package ${entity.packagePath}.dao;

<#if (entity.daoImports ? size > 0) >
<#list entity.daoImports as imp>
import ${imp};
</#list>
</#if>
import com.comtop.cip.jodd.jtx.JtxPropagationBehavior;
import com.comtop.cip.jodd.jtx.meta.Transaction;
import com.comtop.cip.runtime.base.dao.BaseDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
<#if (entity.cascadeOperators ? size > 0) >
import com.comtop.cip.jodd.petite.meta.PetiteInject;
</#if>

/**
 * ${entity.comment}DAO
 * 
 * @author CIP
 * @since 1.0
 * @version ${.now?date} CIP
 */
@PetiteBean
public class ${entity.className}DAO extends BaseDAO<${entity.className}> {
     <#--处理注入的一对一的DAO -->
     <#list entity.cascadeOperators as operator >
     /** 注入${operator.type}DAO **/
     @PetiteInject
     protected ${operator.type}DAO ${operator.name}DAO;

	 </#list>
	 
     <#assign alias="${entity.className ? uncap_first}">
     /**
     * 新增 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  ${entity.comment}Id
     * 
     */
    @Transaction(propagation = JtxPropagationBehavior.PROPAGATION_REQUIRED , readOnly = false)
    public Object insert${entity.className}(${entity.className} ${alias}) {
    	<#list entity.cascadeOOAttributes as cascadeOOAttr>
    	<#assign attr = cascadeOOAttr.attribute />
		<#--处理一对一 或者 多对一级联保存-->
		${cascadeOOAttr.operatorType} ${attr.alias} = ${alias}.${attr.getMethodName}();
		if(${attr.alias}!=null){
			Object obj${cascadeOOAttr.operatorType}Id = ${cascadeOOAttr.operatorName}DAO.insert${cascadeOOAttr.operatorType}(${attr.alias});
			${alias}.${attr.setMethodName}Id(obj${cascadeOOAttr.operatorType}Id.toString());
		}
		
    	</#list>
		Object result = insert(${alias});
		<#list entity.cascadeOMAttributes as  cascadeOMAttr>
		<#assign attr = cascadeOMAttr.attribute />
		<#assign oppositeAttr = cascadeOMAttr.oppositeAttribute />
		<#--处理一对多 -->
		List<${cascadeOMAttr.operatorType}> ${attr.alias} = ${alias}.${attr.getMethodName}();
		if(${attr.alias}!=null){
			for(${cascadeOMAttr.operatorType} ${cascadeOMAttr.operatorType ? uncap_first} : ${attr.alias}){
				${cascadeOMAttr.operatorType ? uncap_first}.${oppositeAttr.setMethodName}(result.toString());
				${cascadeOMAttr.operatorName}DAO.insert${cascadeOMAttr.operatorType}(${cascadeOMAttr.operatorType ? uncap_first});
			}
		}
		</#list>
		<#list entity.cascadeMMAttributes as cascadeMMAttr>
		<#assign attr = cascadeMMAttr.attribute />
		<#--处理多对多 -->
		List<${cascadeMMAttr.operatorType}> ${attr.alias} = ${alias}.${attr.getMethodName}();
		if(${cascadeMMAttr.attribute.alias} !=null){
			for(${cascadeMMAttr.operatorType} ${cascadeMMAttr.operatorType ? uncap_first} : ${attr.alias}){
				addRelated${cascadeMMAttr.operatorType}By${entity.className}(result.toString(), ${cascadeMMAttr.operatorType ? uncap_first}.getId());
			}
		}
		
    	</#list>
        return result;
    }
    
    /**
     * 更新 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  更新成功与否
     * 
     */
    @Transaction(propagation = JtxPropagationBehavior.PROPAGATION_REQUIRED , readOnly = false)
    public boolean update${entity.className}(${entity.className} ${alias}) {
        <#list entity.cascadeOOAttributes as  cascadeOOAttr>
        <#assign attr = cascadeOOAttr.attribute />
		<#--处理一对一 或者 多对一级联保存-->
		${cascadeOOAttr.operatorType} ${attr.alias} = ${alias}.${attr.getMethodName}();
		if(${attr.alias}!=null){
			${cascadeOOAttr.operatorName}DAO.update${cascadeOOAttr.operatorType}(${attr.alias});
		}
		
		</#list>
		<#list entity.cascadeOMAttributes as  cascadeOMAttr>
		<#assign attr = cascadeOMAttr.attribute />
		<#assign oppositeAttr = cascadeOMAttr.oppositeAttribute />
		<#--处理一对多 -->
		List<${cascadeOMAttr.operatorType}> ${attr.alias} = ${alias}.${attr.getMethodName}();
		delete${cascadeOMAttr.operatorType}ListBy${entity.className}Id(${alias}.getId());
		if(${attr.alias}!=null){
			for(${cascadeOMAttr.operatorType} ${cascadeOMAttr.operatorType ? uncap_first} : ${attr.alias}){
				${cascadeOMAttr.operatorType ? uncap_first}.${oppositeAttr.setMethodName}(${alias}.getId());
				${cascadeOMAttr.operatorName}DAO.insert${cascadeOMAttr.operatorType}(${cascadeOMAttr.operatorType ? uncap_first});
			}
		}
		
		</#list>
		<#list entity.cascadeMMAttributes as cascadeMMAttr>
		<#assign attr = cascadeMMAttr.attribute />
		<#--处理多对多 -->
		List<${cascadeMMAttr.operatorType}> ${attr.alias} = ${alias}.${attr.getMethodName}();
		deleteRelated${cascadeMMAttr.operatorType}By${entity.className}Id(${alias}.getId());
		if(${attr.alias} !=null){
			for(${cascadeMMAttr.operatorType} ${cascadeMMAttr.operatorType ? uncap_first} : ${attr.alias}){
				addRelated${cascadeMMAttr.operatorType}By${entity.className}(${alias}.getId(), ${cascadeMMAttr.operatorType ? uncap_first}.getId());
			}
		}
		
		</#list>
        return update(${alias});
    }
    
    /**
     * 删除 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  删除成功与否
     * 
     */
    @Transaction(propagation = JtxPropagationBehavior.PROPAGATION_REQUIRED , readOnly = false)
    public boolean delete${entity.className}(${entity.className} ${alias}) {
        <#list entity.cascadeOOAttributes as  cascadeOOAttr>
		<#assign attr= cascadeOOAttr.attribute>
		<#--处理一对一 或者 多对一级联保存-->
		${cascadeOOAttr.operatorType} ${attr.alias} = ${alias}.${attr.getMethodName}();
		if(${attr.alias}!=null){
			${cascadeOOAttr.operatorName}DAO.delete${cascadeOOAttr.operatorType}(${attr.alias});
		}
		
		</#list>
		<#list entity.cascadeOMAttributes as  cascadeOMAttr>
		<#--处理一对多 -->
		delete${cascadeOMAttr.operatorType}ListBy${entity.className}Id(${alias}.getId());
		
		</#list>
		<#list entity.cascadeMMAttributes as  cascadeMMAttr>
		<#--处理多对多 -->
		deleteRelated${cascadeMMAttr.operatorType}By${entity.className}Id(${alias}.getId());
		</#list>
        return delete(${alias});
    }
    
    
    /**
     * 读取 ${entity.comment}
     * 
     * @param ${alias} ${entity.comment}对象
     * @return  ${entity.comment}
     * 
     */
    public ${entity.className} load${entity.className}(${entity.className} ${alias}) {
        <#assign entityVar="obj${entity.className}">
    	${entity.className} ${entityVar} = load(${alias});
        return ${entityVar};
    }
    
    /**
     * 根据${entity.comment}主键读取 ${entity.comment}
     * 
     * @param id ${entity.comment}主键
     * @return  ${entity.comment}
     * 
     */
    public ${entity.className} load${entity.className}ById(String id) {
    	<#assign entityVar="obj${entity.className}">
    	${entity.className} ${entityVar} = new ${entity.className}();
    	${entityVar}.setId(id);
        return load${entity.className}(${entityVar});
    }
    
    /**
     * 读取 ${entity.comment} 列表
     * 
     * @param condition 查询条件
     * @return  ${entity.comment}列表
     * 
     */
    public List<${entity.className}> query${entity.className}List(${entity.className} condition) {
        return queryList("${entity.packagePath}.model.query${entity.className}List", condition);
    }
    
    <#list entity.cascadeOMMethods as  cascadeMethod>
	/**
     * 通过${alias}Id删除相关${cascadeMethod.comment}
     * 
     * @param ${alias}Id ${entity.comment}Id
     * 
     */
    public void delete${cascadeMethod.simpleName}ListBy${entity.className}Id(String ${alias}Id) {
       this.delete("${entity.packagePath}.model.delete${cascadeMethod.simpleName}ListBy${entity.className}Id", ${alias}Id);
    }
    	
	</#list>
    
    <#list entity.cascadeMMMethods as cascadeMethod>
    /**
     * 添加 ${cascadeMethod.comment}
     *
     * @param ${alias}Id ${entity.comment}主键
     * @param ${cascadeMethod.simpleName ? uncap_first}Id ${cascadeMethod.comment}主键
     */
    @Transaction(propagation = JtxPropagationBehavior.PROPAGATION_REQUIRED , readOnly = false)
    public void addRelated${cascadeMethod.simpleName}By${entity.className}(String ${alias}Id, String ${cascadeMethod.simpleName ? uncap_first}Id) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("relationId", UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
        params.put("${alias}Id", ${alias}Id);
        params.put("${cascadeMethod.simpleName ? uncap_first}Id", ${cascadeMethod.simpleName ? uncap_first}Id);
        this.insert("${entity.packagePath}.model.addRelated${cascadeMethod.simpleName}By${entity.className}", params);
    }
    
    /**
     * 删除  所有 ${cascadeMethod.comment}
     * @param ${alias}Id ${entity.comment}主键
     */
    @Transaction(propagation = JtxPropagationBehavior.PROPAGATION_REQUIRED , readOnly = false)
    public void deleteRelated${cascadeMethod.simpleName}By${entity.className}Id(String ${alias}Id) {
        this.delete("${entity.packagePath}.model.deleteRelated${cascadeMethod.simpleName}By${entity.className}Id", ${alias}Id);
    }
    
    /**
     * 通过${entity.comment}主键查询 所有 ${cascadeMethod.comment}
     * @param ${alias}Id ${entity.comment}主键
     * @return ${cascadeMethod.comment}集合
     */
    public List<${cascadeMethod.simpleName}> queryRelated${cascadeMethod.simpleName}By${entity.className}Id(String ${alias}Id) {
       return this.queryList("${entity.packagePath}.model.queryRelated${cascadeMethod.simpleName}By${entity.className}Id", ${alias}Id);
    }
    
    </#list>
    
    <#list entity.wrapperMethods as mt>
    <#if mt.methodType == 1 >
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
    	<#assign entityVar="obj${entity.className}">
    	${entity.className} ${entityVar} = this.load${entity.className}ById(${mt.paramterArgs});
    	if (${entityVar} != null) { //处理级联查询
    		<#list mt.cascadeQueryAttrs as queryAttr>
    		<#-- 处理一对一-->
    		<#if queryAttr.multiple == 0>
    		${queryAttr.operatorType} obj${queryAttr.operatorType} = null;
    		<#if queryAttr.attribute.relatedByOpposite>
    		obj${queryAttr.operatorType} = this.queryRelated${queryAttr.operatorType}By${entity.className}Id(${entityVar}.getId());
    		<#else>
    		obj${queryAttr.operatorType} = ${queryAttr.operatorName}DAO.load${queryAttr.operatorType}ById(${entityVar}.${queryAttr.attribute.getMethodName}Id());
    		</#if>
    		${entityVar}.${queryAttr.attribute.setMethodName}(obj${queryAttr.operatorType});
    		<#-- 处理一对多-->
    		<#elseif queryAttr.multiple == 1>
    		List<${queryAttr.operatorType}> lst${queryAttr.operatorType} = this.queryRelated${queryAttr.operatorType}By${entity.className}Id(${entityVar}.getId());
    		${entityVar}.${queryAttr.attribute.setMethodName}(lst${queryAttr.operatorType});
    		<#-- 处理多对一-->
    		<#elseif queryAttr.multiple == 2>
    		${queryAttr.operatorType} obj${queryAttr.operatorType} = ${queryAttr.operatorName}DAO.load${queryAttr.operatorType}ById(${entityVar}.${queryAttr.attribute.getMethodName}Id());
    		${entityVar}.${queryAttr.attribute.setMethodName}(obj${queryAttr.operatorType});
    		<#-- 处理多对多-->
    		<#elseif queryAttr.multiple == 3>
    		List<${queryAttr.operatorType}> lst${queryAttr.operatorType} = this.queryRelated${queryAttr.operatorType}By${entity.className}Id( ${entityVar}.getId());
    		${entityVar}.${queryAttr.attribute.setMethodName}(lst${queryAttr.operatorType});
    		</#if>
    		</#list>
    	}
    	return ${entityVar};
    }
    
    </#if>
    </#list>
    
    <#--处理级联查询产生的其他方法 -->
    <#list entity.cascadeQueryMethods as queryMethod>
    <#if queryMethod.mutiple ==0>
    <#assign returnType="${queryMethod.simpleName}">
    <#assign queryComment="${queryMethod.comment}">
    <#assign queryType="(${queryMethod.simpleName})this.selectOne">
    <#elseif queryMethod.mutiple ==1 >
    <#assign returnType="List<${queryMethod.simpleName}> ">
    <#assign queryComment="${queryMethod.comment}集合">
     <#assign queryType="this.queryList">
    </#if>
     /**
     * 通过${entity.comment}主键查询 所有 ${queryMethod.comment}
     * @param ${alias}Id ${entity.comment}主键
     * @return ${queryComment}
     */
    public ${returnType} queryRelated${queryMethod.simpleName}By${entity.className}Id(String ${alias}Id) {
       return ${queryType}("${entity.packagePath}.model.queryRelated${queryMethod.simpleName}By${entity.className}Id", ${alias}Id);
    }
    </#list>
    
    <#list entity.wrapperMethods as mt>
    <#if mt.methodType == 2 >
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
    	Map<String,String> params = new HashMap<String,String>();
        params.put("${mt.paramterArgs}", ${mt.paramterArgs});
        <#if mt.primitiveReturnType == 8 >
        return  this.queryList("${entity.packagePath}.model.${mt.simpleName}", params);
        <#elseif mt.primitiveReturnType == -1 >
        //TODO fixed;
        <#else>
        return (${mt.returnType})this.selectOne("${entity.packagePath}.model.${mt.simpleName}", params);
        </#if>
    }
    </#if>
    </#list>
}