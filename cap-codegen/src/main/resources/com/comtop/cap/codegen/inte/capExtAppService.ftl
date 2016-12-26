<#--文件头注释-->
<#include "/common/capCopyright.ftl">

package ${entity.packagePath}.appservice;

<#if entity.classPattern != "abstract">
import com.comtop.cip.jodd.petite.meta.PetiteBean;
</#if>
<#--引入流程相关的类-->
<#if entity.isProcessable()>
import java.util.List;
import com.comtop.bpms.common.model.ProcessInstanceInfo;
import com.comtop.cap.runtime.base.model.CapWorkflowParam;
</#if>
import ${entity.packagePath}.model.${entity.className};
import ${entity.packagePath}.appservice.abs.Abstract${entity.entityName}AppService;

<#if (entity.impServiceImports ? size > 0) >
<#list entity.impServiceImports as imp>
import ${imp};
</#list>
</#if>

/**
 * ${entity.comment}服务扩展类
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 * @param <T> 类泛型
 */
<#if entity.classPattern == "abstract">
public abstract class ${entity.entityName}AppService<T extends ${entity.className}> extends Abstract${entity.entityName}AppService<${entity.className}> {
<#else>
@PetiteBean(value="${entity.entity.aliasName}AppService")
public class ${entity.entityName}AppService<T extends ${entity.className}> extends Abstract${entity.entityName}AppService<${entity.className}> {
</#if>

	<#-- 实体绑定流程后，添加流程相关方法 -->
	<#if entity.isProcessable()>
    /**
     * 
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#batchEntryCallback(java.util.List,
     *      java.util.List, com.comtop.bpms.common.model.ProcessInstanceInfo[], int)
     */
    @Override
    protected void batchEntryCallback(List<${entity.className}> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        // TODO 自动生成方法存根注释，方法实现时请删除此注释
        super.batchEntryCallback(workRecords, workflowParamList, processInstanceInfos, targetFlowState);
    }
    
    /**
     * 
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#batchForeCallback(java.util.List,
     *      java.util.List, com.comtop.bpms.common.model.ProcessInstanceInfo[], int)
     */
    @Override
    protected void batchForeCallback(List<${entity.className}> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        // TODO 自动生成方法存根注释，方法实现时请删除此注释
        super.batchForeCallback(workRecords, workflowParamList, processInstanceInfos, targetFlowState);
    }
    
    /**
     * 
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#batchBackCallback(java.util.List,
     *      java.util.List, com.comtop.bpms.common.model.ProcessInstanceInfo[], int)
     */
    @Override
    protected void batchBackCallback(List<${entity.className}> workRecords, List<CapWorkflowParam> workflowParamList,
        ProcessInstanceInfo[] processInstanceInfos, int targetFlowState) {
        // TODO 自动生成方法存根注释，方法实现时请删除此注释
        super.batchBackCallback(workRecords, workflowParamList, processInstanceInfos, targetFlowState);
    }
    
    /**
     * 
     * @see com.comtop.cap.runtime.base.appservice.CapWorkflowAppService#undoCallback(com.comtop.cap.runtime.base.model.CapWorkflowVO,
     *      com.comtop.cap.runtime.base.model.CapWorkflowParam,com.comtop.bpms.common.model.ProcessInstanceInfo)
     */
    @Override
    protected void undoCallback(${entity.className} vo, CapWorkflowParam workflowParam,ProcessInstanceInfo processInstanceInfo) {
        // TODO 自动生成方法存根注释，方法实现时请删除此注释
        super.undoCallback(vo, workflowParam);
    }
    <#else>
   	 //TODO 请在此编写自己的业务代码
	</#if> 
}
