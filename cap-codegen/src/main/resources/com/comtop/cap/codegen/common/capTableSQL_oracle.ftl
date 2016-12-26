-- drop table ${t.tableVO.code} if is exists                    
declare 
      num   number; 
begin 
      select count(1) into num from user_tables where TABLE_NAME = '${t.tableVO.code}'; 
      if   num>0   then 
          execute immediate 'drop table ${t.tableVO.code}'; 
      end   if; 
end; 
/
-- Table: ${t.tableVO.code}                                     
create table ${t.tableVO.code} 
(
   <#list t.tableVO.columns as col>
        <#if col.getIsPrimaryKEY() >
        <#if col.dataType == "NUMBER" || col.dataType == "TIMESTAMP">
   ${col.code}    ${col.dataType}<#if (col.precision ?? && col.precision>0)>(${col.length},${col.precision?c})<#else>(${col.length?c})</#if><#if col.existsDefaultValue()> default ${col.defaultValue}</#if><#if !col.getCanBeNull() > not null</#if>,
        <#else>
   ${col.code}    ${col.dataType}<#if (col.precision ?? && col.precision>0)>(${col.length},${col.precision?c})<#else>(${col.length?c})</#if><#if col.existsDefaultValue()> default '${col.defaultValue}'</#if><#if !col.getCanBeNull() > not null</#if>,
        </#if>
        <#else>
        <#if col.dataType == "TIMESTAMP" >
   ${col.code}    ${col.dataType}<#if (col.precision>6 || col.precision<6) >(${col.precision?c})</#if><#if col.existsDefaultValue()> default ${col.defaultValue}</#if><#if !col.getCanBeNull() > not null</#if>,
   		<#elseif col.dataType == "NUMBER" >
   ${col.code}    ${col.dataType}<#if (col.precision ?? && col.precision>0)>(${col.length},${col.precision?c})<#else><#if (col.length>22 || col.length<22) >(${col.length?c})</#if></#if><#if col.existsDefaultValue()> default ${col.defaultValue}</#if><#if !col.getCanBeNull() > not null</#if>, 
   		<#elseif col.dataType == "CLOB" || col.dataType == "BLOB" >
   ${col.code}    ${col.dataType}<#if col.existsDefaultValue()> default '${col.defaultValue}'</#if><#if !col.getCanBeNull() > not null</#if>,		
        <#else>
   ${col.code}    ${col.dataType}<#if (col.precision ?? && col.precision>0)>(${col.length},${col.precision?c})<#else>(${col.length?c})</#if><#if col.existsDefaultValue()> default '${col.defaultValue}'</#if><#if !col.getCanBeNull() > not null</#if>,     
        </#if>
        </#if>
   </#list>
   <#if t.existsPrimarykey()>
   constraint PK_${t.tableVO.code} primary key (${t.pk})
   </#if>
);
<#if t.tableVO.existsDescription()>
comment on table ${t.tableVO.code} is '${t.tableVO.description}';
</#if>
<#list t.tableVO.columns as col>
<#if col.existsDescription()>
comment on column ${t.tableVO.code}.${col.code} is '${col.description}';	
</#if>
</#list>


<#list t.indexes as index>
-- Index: "${index.indexName}"                                 
create index ${index.indexName} on ${t.tableVO.code} (${index.columnName});
</#list>
   