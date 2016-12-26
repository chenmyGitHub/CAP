
<#-- table increment SQL  -->
<#if (t.objResult.result == 1)>
<#if t.isChangeDescription()>
/*=======================================================================*/
/* edit table description                                                */
/*=======================================================================*/
ALTER TABLE ${t.objResult.targetTable.code} COMMENT='${t.objResult.targetTable.description}';
</#if>
<#-- index change -->
<#if t.isChangeIndex()>
/*=======================================================================*/
/* edit table index                                                      */
/*=======================================================================*/
  <#list t.objResult.indexResults as idxR>
<#-- index not exists -->  
    <#if (idxR.result == -2)>
         <#if idxR.srcIndex.isPrimaryConstraint()>
ALTER TABLE ${t.objResult.targetTable.code} DROP PRIMARY KEY;
         <#else>
ALTER TABLE ${t.objResult.targetTable.code} DROP INDEX ${idxR.srcIndex.engName}; 
         </#if>
<#-- add index  -->         
    <#elseif (idxR.result == -1)>
          <#if idxR.targetIndex.isPrimaryConstraint()>
ALTER TABLE ${t.objResult.targetTable.code} ADD PRIMARY KEY (${idxR.strTargetIndexColumn});
          <#elseif idxR.targetIndex.isUniqueConstraint()>
ALTER TABLE ${t.objResult.targetTable.code} ADD UNIQUE INDEX ${idxR.targetIndex.engName} (${idxR.strTargetIndexColumn}) USING BTREE;
          <#else>
ALTER TABLE ${t.objResult.targetTable.code} ADD INDEX ${idxR.targetIndex.engName} (${idxR.strTargetIndexColumn}) USING HASH;
          </#if>
<#-- modify index  -->          
     <#elseif (idxR.result == 1)>
          <#if idxR.srcIndex.isPrimaryConstraint()>
ALTER TABLE ${t.objResult.targetTable.code} DROP PRIMARY KEY; 
ALTER TABLE ${t.objResult.targetTable.code} ADD PRIMARY KEY (${idxR.strTargetIndexColumn});
          <#elseif idxR.srcIndex.isUniqueConstraint()>
ALTER TABLE ${t.objResult.targetTable.code} DROP INDEX ${idxR.targetIndex.engName}; 
ALTER TABLE ${t.objResult.targetTable.code} ADD UNIQUE INDEX ${idxR.targetIndex.engName} (${idxR.strTargetIndexColumn}) USING BTREE;
          <#else>
ALTER TABLE ${t.objResult.targetTable.code} DROP INDEX ${idxR.targetIndex.engName};
ALTER TABLE ${t.objResult.targetTable.code} ADD INDEX ${idxR.targetIndex.engName} (${idxR.strTargetIndexColumn}) USING HASH;
          </#if>
      </#if>
  </#list>
</#if>
<#-- column change -->
<#if t.isChangeCloumn()>
/*=======================================================================*/
/* drop,add,alter table column                                           */
/*=======================================================================*/
 <#-- drop column -->
 <#list t.objResult.columnResults as col>
  <#if (col.result == -2)>
ALTER TABLE ${t.objResult.srcTable.code} DROP COLUMN ${col.srcColumn.code};
  <#-- add column -->
  <#elseif (col.result == -1)>
     <#if col.targetColumn.dataType=='TIMESTAMP'> 
ALTER TABLE ${t.objResult.srcTable.code} ADD COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if col.targetColumn.defaultValue?? && col.targetColumn.defaultValue!=""> DEFAULT CURRENT_TIMESTAMP <#else> NULL DEFAULT NULL </#if> COMMENT '${col.targetColumn.description}';
     <#elseif col.targetColumn.dataType=='NUMERIC' || col.targetColumn.dataType=='INT'>
ALTER TABLE ${t.objResult.srcTable.code} ADD COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if><#if col.targetColumn.defaultValue?? && col.targetColumn.defaultValue!=""> DEFAULT ${col.targetColumn.defaultValue} <#else> NULL DEFAULT NULL </#if> COMMENT '${col.targetColumn.description}';
     <#elseif col.targetColumn.dataType=='TEXT'>
ALTER TABLE ${t.objResult.srcTable.code} ADD COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if col.targetColumn.defaultValue?? && col.targetColumn.defaultValue!=""> DEFAULT '${col.targetColumn.defaultValue}' <#else> NULL DEFAULT NULL </#if> COMMENT '${col.targetColumn.description}';
     <#else>
ALTER TABLE ${t.objResult.srcTable.code} ADD COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if><#if col.targetColumn.defaultValue?? && col.targetColumn.defaultValue!=""> DEFAULT '${col.targetColumn.defaultValue}' <#else> NULL DEFAULT NULL </#if> COMMENT '${col.targetColumn.description}';
     </#if>
  <#-- alter column -->
  <#elseif (col.result == 1) >
     <#list col.lstResult as diff>
       <#-- description change 12 ; dataType , precision or length change 16 ; canbeNull change 18 ;defaultValue change 19 -->
       <#if diff == 12 || diff == 16 || diff == 18 || diff == 19>
           <#if col.targetColumn.getCanBeNull() && (!col.targetColumn.defaultValue?? || col.targetColumn.defaultValue=="")>
                       <#if col.targetColumn.dataType=='TIMESTAMP' || col.targetColumn.dataType=='TEXT'> 
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType} NULL DEFAULT NULL COMMENT '${col.targetColumn.description}';
                       <#else>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if> COMMENT '${col.targetColumn.description}';
                       </#if>
           <#elseif col.targetColumn.getCanBeNull() && (col.targetColumn.defaultValue?? && col.targetColumn.defaultValue!="")>
                       <#if col.targetColumn.dataType=='TIMESTAMP'> 
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType} NULL DEFAULT CURRENT_TIMESTAMP COMMENT '${col.targetColumn.description}';
                       <#elseif col.targetColumn.dataType=='NUMERIC' || col.targetColumn.dataType=='INT'>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if> DEFAULT ${col.targetColumn.defaultValue} COMMENT '${col.targetColumn.description}';
                       <#elseif col.targetColumn.dataType=='TEXT'>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType} DEFAULT '${col.targetColumn.defaultValue}' COMMENT '${col.targetColumn.description}';
                       <#else>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if> DEFAULT '${col.targetColumn.defaultValue}' COMMENT '${col.targetColumn.description}';
                       </#if>
           <#elseif !col.targetColumn.getCanBeNull() && (col.targetColumn.defaultValue?? && col.targetColumn.defaultValue!="")>
                      <#if col.targetColumn.dataType=='TIMESTAMP'> 
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType} NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '${col.targetColumn.description}';
                      <#elseif col.targetColumn.dataType=='NUMERIC' || col.targetColumn.dataType=='INT'>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if> NOT NULL DEFAULT ${col.targetColumn.defaultValue} COMMENT '${col.targetColumn.description}';
                      <#elseif col.targetColumn.dataType=='TEXT'>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType} NOT NULL DEFAULT '${col.targetColumn.defaultValue}' COMMENT '${col.targetColumn.description}';
                      <#else>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if> NOT NULL DEFAULT '${col.targetColumn.defaultValue}' COMMENT '${col.targetColumn.description}';
                      </#if>
           <#elseif !col.targetColumn.getCanBeNull() && (!col.targetColumn.defaultValue?? || col.targetColumn.defaultValue=="")>
                       <#if col.targetColumn.dataType=='TIMESTAMP' || col.targetColumn.dataType=='TEXT'> 
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType} NOT NULL COMMENT '${col.targetColumn.description}';
                       <#else>
ALTER TABLE ${t.objResult.srcTable.code} MODIFY COLUMN ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length?c},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if> NOT NULL COMMENT '${col.targetColumn.description}';
                       </#if>      
           </#if>
           <#break>
      </#if>
     </#list>
  </#if>
 </#list>
</#if>
</#if>

   