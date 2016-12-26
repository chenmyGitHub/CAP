
<#-- table increment SQL ,table change  -->
<#if (t.objResult.result == 1)>
	<#if t.isChangeDescription()>
/*=======================================================================*/
/* edit table description                                                */
/*=======================================================================*/
comment on TABLE ${t.objResult.targetTable.code} is <#if t.objResult.targetTable.existsDescription()>'${t.objResult.targetTable.description}'<#else>''</#if>;
	</#if>
<#-- index change -->
	<#if t.isChangeIndex()>
/*=======================================================================*/
/* edit table index                                                      */
/*=======================================================================*/
		<#list t.objResult.indexResults as idxR>
<#-- index not exists -->
			<#if (idxR.result == -2)>
				<#if idxR.srcIndex.isUniqueConstraint() || idxR.srcIndex.isPrimaryConstraint()>
alter table ${t.objResult.targetTable.code} drop constraint ${idxR.srcIndex.engName}; 
				<#else>
drop index ${idxR.srcIndex.engName};
				</#if>
<#-- add index  -->
			<#elseif (idxR.result == -1)>
				<#if idxR.targetIndex.isPrimaryConstraint()>
alter table ${t.objResult.targetTable.code} add Constraint ${idxR.targetIndex.engName} Primary Key (${idxR.strTargetIndexColumn});
				<#elseif idxR.targetIndex.isUniqueConstraint()>
alter table ${t.objResult.targetTable.code} add constraint ${idxR.targetIndex.engName} unique (${idxR.strTargetIndexColumn});
				<#else>
create index ${idxR.targetIndex.engName} on ${t.objResult.targetTable.code} (${idxR.strTargetIndexColumn});
			</#if>
<#-- modify index  -->
			<#elseif (idxR.result == 1)>
				<#if idxR.srcIndex.isPrimaryConstraint()>
alter table ${t.objResult.targetTable.code} drop constraint ${idxR.targetIndex.engName}; 
alter table ${t.objResult.targetTable.code} add Constraint ${idxR.targetIndex.engName} Primary Key (${idxR.strTargetIndexColumn});
				<#elseif idxR.srcIndex.isUniqueConstraint()>
alter table ${t.objResult.targetTable.code} drop constraint ${idxR.targetIndex.engName}; 
alter table ${t.objResult.targetTable.code} add constraint ${idxR.targetIndex.engName} unique (${idxR.strTargetIndexColumn});
				<#else>
drop index ${idxR.targetIndex.engName};
create index ${idxR.targetIndex.engName} on ${t.objResult.targetTable.code} (${idxR.strTargetIndexColumn});
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
alter table ${t.objResult.srcTable.code} drop column ${col.srcColumn.code};
<#-- add column -->
			<#elseif (col.result == -1)>
				<#if col.targetColumn.dataType== "TIMESTAMP">
alter table ${t.objResult.srcTable.code} add ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && (col.targetColumn.precision > 6 || col.targetColumn.precision < 6)) >(${col.targetColumn.precision?c})</#if><#if col.targetColumn.existsDefaultValue()> default ${col.targetColumn.defaultValue}<#else> default null</#if><#if !col.targetColumn.getCanBeNull() > not null<#else> null</#if>;
				<#elseif col.targetColumn.dataType== "NUMBER">
alter table ${t.objResult.srcTable.code} add ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length},${col.targetColumn.precision?c})<#else><#if (col.targetColumn.length ?? && (col.targetColumn.length > 22 || col.targetColumn.length < 22)) >(${col.targetColumn.length?c})</#if></#if><#if col.targetColumn.existsDefaultValue()> default ${col.targetColumn.defaultValue}<#else> default null</#if><#if !col.targetColumn.getCanBeNull() > not null<#else> null</#if>;
				<#elseif col.targetColumn.dataType == "CLOB" || col.targetColumn.dataType == "BLOB" >
alter table ${t.objResult.srcTable.code} add ${col.targetColumn.code} ${col.targetColumn.dataType} <#if col.targetColumn.existsDefaultValue()>default '${col.targetColumn.defaultValue}'<#else> default null</#if><#if !col.targetColumn.getCanBeNull() > not null<#else> null</#if>;
				<#else>
alter table ${t.objResult.srcTable.code} add ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if><#if col.targetColumn.existsDefaultValue()> default '${col.targetColumn.defaultValue}'<#else> default null</#if><#if !col.targetColumn.getCanBeNull() > not null<#else> null</#if>;
				</#if>
<#-- description change -->
comment on column ${t.objResult.srcTable.code}.${col.targetColumn.code} is <#if col.targetColumn.existsDescription()>'${col.targetColumn.description}'<#else>''</#if>;
<#-- alter column -->
			<#elseif (col.result == 1) >
				<#list col.lstResult as diff>
<#-- description change -->
					<#if diff == 12 >
comment on column ${t.objResult.srcTable.code}.${col.targetColumn.code} is <#if col.targetColumn.existsDescription()>'${col.targetColumn.description}'<#else>''</#if>;
<#-- dataType , precision or length change  -->
					<#elseif diff == 16>
						<#if col.targetColumn.dataType== "TIMESTAMP">
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && (col.targetColumn.precision > 6 || col.targetColumn.precision < 6)) >(${col.targetColumn.precision?c})</#if>;
						<#elseif col.targetColumn.dataType== "NUMBER">
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length},${col.targetColumn.precision?c})<#else><#if (col.targetColumn.length ?? && (col.targetColumn.length > 22 || col.targetColumn.length < 22)) >(${col.targetColumn.length?c})</#if></#if>;
						<#elseif col.targetColumn.dataType == "CLOB" || col.targetColumn.dataType == "BLOB" >
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} ${col.targetColumn.dataType};
						<#else>
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} ${col.targetColumn.dataType}<#if (col.targetColumn.precision ?? && col.targetColumn.precision>0)>(${col.targetColumn.length},${col.targetColumn.precision?c})<#else>(${col.targetColumn.length?c})</#if>;
						</#if>
<#-- canbeNull change  -->
					<#elseif diff == 18>
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} <#if !col.targetColumn.getCanBeNull() > not null<#else> null</#if>;
<#-- defaultValue change -->
					<#elseif diff == 19>
						<#if col.targetColumn.dataType== "NUMBER" || col.targetColumn.dataType== "TIMESTAMP">
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} <#if col.targetColumn.defaultValue?? >default ${col.targetColumn.defaultValue}<#else>default null</#if>;
						<#else>
alter table ${t.objResult.srcTable.code} MODIFY ${col.targetColumn.code} <#if col.targetColumn.defaultValue?? >default '${col.targetColumn.defaultValue}'<#else>default null</#if>;
						</#if>
					</#if>
				</#list>
			</#if>
		</#list>
	</#if>
</#if>

   