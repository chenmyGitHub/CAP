<#include "/common/capCopyright.ftl">

package ${entity.packagePath}.model;

<#--引入父实体基类-->
import ${entity.parentEntity.modelPackage}.model.${entity.parentEntity.engName}VO;
<#if entity.classPattern != "abstract">
import comtop.org.directwebremoting.annotations.DataTransferObject;
</#if>
<#if entity.isEntityAlias()>
import com.comtop.cap.runtime.base.annotation.EntityAlias;
</#if>
<#if (entity.table)??>
import javax.persistence.Table;
</#if>
<#if (entity.voImports ? size > 0) >
<#list entity.voImports as imp>
import ${imp};
</#list>
</#if>


/**
 * ${entity.comment}
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 */
<#if (entity.table)??>
@Table(name = "${entity.table}")
</#if>
<#if entity.classPattern == "abstract">
public abstract class ${entity.className} extends ${entity.parentEntity.engName}VO {
<#else>
@DataTransferObject
<#if entity.isEntityAlias()>
@EntityAlias(value = "${entity.entity.aliasName}")
</#if>
public class ${entity.className} extends ${entity.parentEntity.engName}VO {
</#if>

    /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
	<#-- 实体基本属性 -->
	<#list entity.wrapperAttributes as attr>
    /** ${attr.comment} */
    <#list attr.annotations as annot>
    ${annot}
    </#list>
    ${attr.modifer} ${attr.type} ${attr.alias};
    
	</#list>
	
	<#-- 生成Get，Set方法 -->
	<#list entity.wrapperAttributes as attr>
	<#if attr.hasOverride>
		<#assign ant = "@Override">
	<#else>
		<#assign ant = "">
	</#if>
    /**
     * @return 获取 ${attr.comment} 属性值
     */
    <#if ant != "">
    ${ant}
    </#if>
    public ${attr.type} ${attr.getMethodName}() {
        return ${attr.alias};
    }
    	
    /**
     * @param ${attr.alias} 设置 ${attr.comment} 属性值为参数值 ${attr.alias}
     */
    <#if ant != "">
    ${ant}
    </#if>
    public void ${attr.setMethodName}(${attr.type} ${attr.alias}) {
        this.${attr.alias} = ${attr.alias};
    }
    
	</#list>
	 
	 <#if entity.primaryKeyName??>
    /**
     * 获取主键值
     * @return 主键值
     */
    @Override
    public String getPrimaryValue(){
    		return  this.${entity.primaryKeyName};
    }
    </#if>
}