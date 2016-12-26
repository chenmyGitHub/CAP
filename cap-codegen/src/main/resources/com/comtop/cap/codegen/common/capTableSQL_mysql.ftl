#drop table 
DROP TABLE IF EXISTS ${t.tableVO.code};

#Table: ${t.tableVO.code}                                    
CREATE TABLE ${t.tableVO.code}
(
   <#list t.tableVO.columns as col>
	   <#if col.dataType=="TIMESTAMP">
   ${col.code}    ${col.dataType} NULL DEFAULT NULL comment '${col.chName}',    
       <#elseif col.isPrimaryKEY && col.dataType=="INT">
   ${col.code}    ${col.dataType}<#if (col.precision ?? && col.precision>0)>(${col.length},${col.precision})<#else>(${col.length})</#if> NOT NULL auto_increment comment '${col.chName}',        
	   <#else>
   ${col.code}    ${col.dataType}<#if (col.precision ?? && col.precision>0)>(${col.length},${col.precision})<#else>(${col.length})</#if> comment '${col.chName}', 	   
	   </#if>
   </#list>
   PRIMARY KEY (${t.pk})
)  COMMENT '${t.tableVO.chName}';