<#include "/copyright.ftl">

package ${entity.packagePath}.model;

<#if (entity.voImports ? size > 0) >
<#list entity.voImports as imp>
import ${imp};
</#list>
</#if>
import javax.persistence.Table;
import com.comtop.cap.runtime.base.model.BaseVO;

/**
 * ${entity.comment}
 * 
 * @author CIP
 * @since 1.0
 * @version ${.now?date} CIP
 */
@Table(name = "${entity.table}")
public class ${entity.className} extends BaseVO {

	 /** 序列化ID */
    private static final long serialVersionUID = 1L;
    
    <#-- 实体基本属性 -->
    <#list entity.wrapperAttributes as attr>
    /** ${attr.comment} */
    <#list attr.annotations as annot>
    ${annot}
    </#list>
    <#if attr.mutiple == 1 || attr.mutiple == 3>
    ${attr.modifer} ${attr.type} ${attr.alias} = new com.comtop.cip.sample.root.model.AutoArrayList<${attr.relatedEntity.englishName}>(${attr.relatedEntity.englishName}.class);
    
     /** ${attr.comment}Id集合 */
    ${attr.modifer} String [] ${attr.alias}IdList;
    <#else>
    ${attr.modifer} ${attr.type} ${attr.alias};
    </#if>
	 
	</#list>
	
	<#-- 生成Get，Set方法 -->
	<#list entity.wrapperAttributes as attr>
	<#if attr.mutiple == 1 || attr.mutiple == 3>
	<#assign relatedName="${attr.relatedEntity.englishName ? cap_first}">
	<#assign relatedIdList="${attr.alias}IdList">
	/**
     * @return 获取 ${attr.comment}属性值
     */
    public  ${attr.type} ${attr.getMethodName}() {
        return this.${attr.alias};
    }
    	
    /**
     * @param ${attr.alias} 设置 获取 ${attr.comment}属性值为参数值 ${attr.alias}
     */
    public void ${attr.setMethodName}(${attr.type} ${attr.alias}) {
        if (${attr.alias} != null && ${attr.alias}.size() > 0) {
            String[] strIds = new String[${attr.alias}.size()];
            for (int i = 0; i < ${attr.alias}.size(); i++) {
                ${relatedName} obj${relatedName} = ${attr.alias}.get(i);
                strIds[i] = obj${relatedName}.getId();
            }
            this.${relatedIdList} = strIds;
        }
        this.${attr.alias} = ${attr.alias};
    }
    
    /**
     * @return 获取 ${relatedIdList}属性值
     */
    public String[] get${relatedIdList ? cap_first}() {
        return  ${relatedIdList};
    }
    
    /**
     * @param  ${relatedIdList} 设置  ${relatedIdList} 属性值为参数值  ${relatedIdList}
     */
    public void set${relatedIdList ? cap_first}(String[]  ${relatedIdList}) {
        if ( ${relatedIdList} != null &&  ${relatedIdList}.length > 0) {
            List<${relatedName}> lst${relatedName}= new java.util.ArrayList<${relatedName}>();
            ${relatedName} obj${relatedName};
            for (String str${relatedName}Id : ${relatedIdList}) {
                obj${relatedName} = new ${relatedName}();
                obj${relatedName}.setId(str${relatedName}Id);
                lst${relatedName}.add(obj${relatedName});
            }
            this.${attr.alias} = lst${relatedName};
        }
        this.${relatedIdList} = ${relatedIdList};
    }
	
	<#else>
    /**
     * @return 获取 ${attr.comment}属性值
     */
    <#if (attr.alias == "id") >
    @Override
    </#if>
    public ${attr.type} ${attr.getMethodName}() {
        return ${attr.alias};
    }
    	
    /**
     * @param ${attr.alias} 设置 ${attr.comment}属性值为参数值 ${attr.alias}
     */
    <#if attr.alias == "id" >
    @Override
    </#if>
    public void ${attr.setMethodName}(${attr.type} ${attr.alias}) {
        this.${attr.alias} = ${attr.alias};
    }
    </#if>
	</#list>
}