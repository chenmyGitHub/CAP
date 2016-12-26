
----------------------------------------------------- 整理人：杨赛-----------------------------------------------------------------------------
----------------------------------------------------- 执行人：杨赛-----------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-03-09 ------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------

--【补丁】2015-11-25_23-15_BPMS_V1.4.72_无_协作实现调用南网TBI接口_李欢.sql
begin 
 	 P_SOA_REG_SERVICE('colCommunicationLocalDelegate4TBI','','ws_tbi','','soa-service-register_20151122190649','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.delegate.ColCommunicationLocalDelegate4TBI');
 	 P_SOA_REG_Method('colCommunicationLocalDelegate4TBI.colInvoke','colInvoke','colInvoke','colCommunicationLocalDelegate4TBI','0','0','');
 	 p_soa_reg_Params('colCommunicationLocalDelegate4TBI.colInvoke_I0','colCommunicationLocalDelegate4TBI.colInvoke','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationLocalDelegate4TBI.colInvoke_I1','colCommunicationLocalDelegate4TBI.colInvoke','','1','1','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.delegate.ColParamVO','','');
 	 p_soa_reg_Params('colCommunicationLocalDelegate4TBI.colInvoke_O','colCommunicationLocalDelegate4TBI.colInvoke','','2','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.delegate.ColReturnVO','','');
end; 
/ 
begin 
 	 P_SOA_REG_WS('TBIColCommunicationLocalDelegate4TBIcolInvoke','ColCommunicationLocalDelegate4TBI_colInvoke','1','colCommunicationLocalDelegate4TBI.colInvoke','BPMS协作服务','http://ggfw.bpms.soa.csg.cn','','','','com.comtop.soa.tbi.service.colcommunicationlocaldelegate4tbi.colinvoke.ColCommunicationLocalDelegate4TBI','');
end; 
/ 
commit;

--【补丁】2015-12-04_17-20_BPMS_V1.4.76_无_删除性能差索引_翟羽佳.sql
DROP index TEMPTASK_INDEX7;

--【补丁】2015-12-04_17-30_BPMS_V1.4.77_无_实现协作通过代理方式访问_李欢
begin 
 	 P_SOA_REG_SERVICE('colCommunicationWSProxySoaClient','','','','soa-service-register_20151204145207','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.wsproxy.ColCommunicationWSProxySoaClient');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.readDirectoryChildElements','readDirectoryChildElements','readDirectoryChildElements','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDirectoryChildElements_I0','colCommunicationWSProxySoaClient.readDirectoryChildElements','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDirectoryChildElements_I1','colCommunicationWSProxySoaClient.readDirectoryChildElements','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDirectoryChildElements_O','colCommunicationWSProxySoaClient.readDirectoryChildElements','','2','0','java.util.List','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDirectoryChildElements_E0','colCommunicationWSProxySoaClient.readDirectoryChildElements','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.findColFileContent','findColFileContent','findColFileContent','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColFileContent_I0','colCommunicationWSProxySoaClient.findColFileContent','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColFileContent_I1','colCommunicationWSProxySoaClient.findColFileContent','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColFileContent_O','colCommunicationWSProxySoaClient.findColFileContent','','2','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColFileContent_E0','colCommunicationWSProxySoaClient.findColFileContent','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.queryReceLocalAllTrackData','queryReceLocalAllTrackData','queryReceLocalAllTrackData','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryReceLocalAllTrackData_I0','colCommunicationWSProxySoaClient.queryReceLocalAllTrackData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryReceLocalAllTrackData_O','colCommunicationWSProxySoaClient.queryReceLocalAllTrackData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryReceLocalAllTrackData_E0','colCommunicationWSProxySoaClient.queryReceLocalAllTrackData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.queryProcessDefFile','queryProcessDefFile','queryProcessDefFile','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryProcessDefFile_I0','colCommunicationWSProxySoaClient.queryProcessDefFile','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryProcessDefFile_I1','colCommunicationWSProxySoaClient.queryProcessDefFile','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryProcessDefFile_O','colCommunicationWSProxySoaClient.queryProcessDefFile','','2','0','java.lang.String','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.getLastProcessVersion','getLastProcessVersion','getLastProcessVersion','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.getLastProcessVersion_I0','colCommunicationWSProxySoaClient.getLastProcessVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.getLastProcessVersion_O','colCommunicationWSProxySoaClient.getLastProcessVersion','','2','0','java.lang.Integer','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.querySendLocalAllTrackData','querySendLocalAllTrackData','querySendLocalAllTrackData','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalAllTrackData_I0','colCommunicationWSProxySoaClient.querySendLocalAllTrackData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalAllTrackData_I1','colCommunicationWSProxySoaClient.querySendLocalAllTrackData','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalAllTrackData_O','colCommunicationWSProxySoaClient.querySendLocalAllTrackData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalAllTrackData_E0','colCommunicationWSProxySoaClient.querySendLocalAllTrackData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.saveCollabarationInfoByProcess','saveCollabarationInfoByProcess','saveCollabarationInfoByProcess','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.saveCollabarationInfoByProcess_I0','colCommunicationWSProxySoaClient.saveCollabarationInfoByProcess','','1','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.parse.definition.ColDefinition','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.saveCollabarationInfoByProcess_E0','colCommunicationWSProxySoaClient.saveCollabarationInfoByProcess','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.queryReceLocalTrackTableData','queryReceLocalTrackTableData','queryReceLocalTrackTableData','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryReceLocalTrackTableData_I0','colCommunicationWSProxySoaClient.queryReceLocalTrackTableData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryReceLocalTrackTableData_O','colCommunicationWSProxySoaClient.queryReceLocalTrackTableData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryReceLocalTrackTableData_E0','colCommunicationWSProxySoaClient.queryReceLocalTrackTableData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.uninstallColByIdAndVersion','uninstallColByIdAndVersion','uninstallColByIdAndVersion','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.uninstallColByIdAndVersion_I0','colCommunicationWSProxySoaClient.uninstallColByIdAndVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.uninstallColByIdAndVersion_I1','colCommunicationWSProxySoaClient.uninstallColByIdAndVersion','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.uninstallColByIdAndVersion_I2','colCommunicationWSProxySoaClient.uninstallColByIdAndVersion','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.uninstallColByIdAndVersion_E0','colCommunicationWSProxySoaClient.uninstallColByIdAndVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.querySendLocalTrackTableData','querySendLocalTrackTableData','querySendLocalTrackTableData','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalTrackTableData_I0','colCommunicationWSProxySoaClient.querySendLocalTrackTableData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalTrackTableData_I1','colCommunicationWSProxySoaClient.querySendLocalTrackTableData','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalTrackTableData_O','colCommunicationWSProxySoaClient.querySendLocalTrackTableData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querySendLocalTrackTableData_E0','colCommunicationWSProxySoaClient.querySendLocalTrackTableData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','readDeployInfoByDirectoryCode','readDeployInfoByDirectoryCode','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode_I0','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode_I1','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode_I2','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','','1','2','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode_I3','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','','1','3','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode_O','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','','2','0','java.util.List','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode_E0','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCode','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.readDefinitionFileContextByDeployeId','readDefinitionFileContextByDeployeId','readDefinitionFileContextByDeployeId','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDefinitionFileContextByDeployeId_I0','colCommunicationWSProxySoaClient.readDefinitionFileContextByDeployeId','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDefinitionFileContextByDeployeId_O','colCommunicationWSProxySoaClient.readDefinitionFileContextByDeployeId','','2','0','java.lang.String','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.readRootDirectory','readRootDirectory','readRootDirectory','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readRootDirectory_I0','colCommunicationWSProxySoaClient.readRootDirectory','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readRootDirectory_O','colCommunicationWSProxySoaClient.readRootDirectory','','2','0','java.util.List','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readRootDirectory_E0','colCommunicationWSProxySoaClient.readRootDirectory','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.saveCollabarationInfos','saveCollabarationInfos','saveCollabarationInfos','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.saveCollabarationInfos_I0','colCommunicationWSProxySoaClient.saveCollabarationInfos','','1','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.parse.definition.ColDefinition','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.saveCollabarationInfos_I1','colCommunicationWSProxySoaClient.saveCollabarationInfos','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.saveCollabarationInfos_E0','colCommunicationWSProxySoaClient.saveCollabarationInfos','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.findColDefinition','findColDefinition','findColDefinition','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColDefinition_I0','colCommunicationWSProxySoaClient.findColDefinition','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColDefinition_I1','colCommunicationWSProxySoaClient.findColDefinition','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColDefinition_O','colCommunicationWSProxySoaClient.findColDefinition','','2','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.parse.definition.ColDefinition','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.findColDefinition_E0','colCommunicationWSProxySoaClient.findColDefinition','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.messageReceive','messageReceive','messageReceive','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.messageReceive_I0','colCommunicationWSProxySoaClient.messageReceive','','1','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.messinteract.model.SendMessageVO','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.messageReceive_E0','colCommunicationWSProxySoaClient.messageReceive','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.querytLastProcessDefFile','querytLastProcessDefFile','querytLastProcessDefFile','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querytLastProcessDefFile_I0','colCommunicationWSProxySoaClient.querytLastProcessDefFile','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.querytLastProcessDefFile_O','colCommunicationWSProxySoaClient.querytLastProcessDefFile','','2','0','java.lang.String','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','readDeployInfoByDirectoryCodeWithPage','readDeployInfoByDirectoryCodeWithPage','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_I0','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_I1','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_I2','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','1','2','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_I3','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','1','3','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_I4','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','1','4','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_I5','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','1','5','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_O','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','2','0','com.comtop.bpms.base.BpmsPageBean','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage_E0','colCommunicationWSProxySoaClient.readDeployInfoByDirectoryCodeWithPage','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.receiveTestServiceConnection','receiveTestServiceConnection','receiveTestServiceConnection','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.receiveTestServiceConnection_O','colCommunicationWSProxySoaClient.receiveTestServiceConnection','','2','0','java.lang.String','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.getLastColVersion','getLastColVersion','getLastColVersion','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.getLastColVersion_I0','colCommunicationWSProxySoaClient.getLastColVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.getLastColVersion_O','colCommunicationWSProxySoaClient.getLastColVersion','','2','0','java.lang.Integer','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.getLastColVersion_E0','colCommunicationWSProxySoaClient.getLastColVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.recoverColByIdAndVersion','recoverColByIdAndVersion','recoverColByIdAndVersion','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.recoverColByIdAndVersion_I0','colCommunicationWSProxySoaClient.recoverColByIdAndVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.recoverColByIdAndVersion_I1','colCommunicationWSProxySoaClient.recoverColByIdAndVersion','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.recoverColByIdAndVersion_I2','colCommunicationWSProxySoaClient.recoverColByIdAndVersion','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.recoverColByIdAndVersion_E0','colCommunicationWSProxySoaClient.recoverColByIdAndVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient.queryContentByCondition','queryContentByCondition','queryContentByCondition','colCommunicationWSProxySoaClient','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_I0','colCommunicationWSProxySoaClient.queryContentByCondition','','1','0','com.comtop.bpms.common.model.DeployQueryConditionInfo','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_I1','colCommunicationWSProxySoaClient.queryContentByCondition','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_I2','colCommunicationWSProxySoaClient.queryContentByCondition','','1','2','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_I3','colCommunicationWSProxySoaClient.queryContentByCondition','','1','3','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_I4','colCommunicationWSProxySoaClient.queryContentByCondition','','1','4','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_I5','colCommunicationWSProxySoaClient.queryContentByCondition','','1','5','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_O','colCommunicationWSProxySoaClient.queryContentByCondition','','2','0','com.comtop.bpms.base.BpmsPageBean','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient.queryContentByCondition_E0','colCommunicationWSProxySoaClient.queryContentByCondition','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
end; 
/ 
--------准备注册：colCommunicationWSProxySoaClient$1服务-------
begin 
 	 P_SOA_REG_SERVICE('colCommunicationWSProxySoaClient$1','','','','soa-service-register_20151204145207','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.wsproxy.ColCommunicationWSProxySoaClient$1');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient$1.getType','getType','getType','colCommunicationWSProxySoaClient$1','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$1.getType_O','colCommunicationWSProxySoaClient$1.getType','','2','0','java.lang.reflect.Type','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient$1.compareTo','compareTo','compareTo','colCommunicationWSProxySoaClient$1','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$1.compareTo_I0','colCommunicationWSProxySoaClient$1.compareTo','','1','0','comtop.soa.com.fasterxml.jackson.core.type.TypeReference','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$1.compareTo_O','colCommunicationWSProxySoaClient$1.compareTo','','2','0','int','','');
end; 
/ 
--------准备注册：colCommunicationWSProxySoaClient$2服务-------
begin 
 	 P_SOA_REG_SERVICE('colCommunicationWSProxySoaClient$2','','','','soa-service-register_20151204145207','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.wsproxy.ColCommunicationWSProxySoaClient$2');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient$2.compareTo','compareTo','compareTo','colCommunicationWSProxySoaClient$2','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$2.compareTo_I0','colCommunicationWSProxySoaClient$2.compareTo','','1','0','comtop.soa.com.fasterxml.jackson.core.type.TypeReference','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$2.compareTo_O','colCommunicationWSProxySoaClient$2.compareTo','','2','0','int','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient$2.getType','getType','getType','colCommunicationWSProxySoaClient$2','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$2.getType_O','colCommunicationWSProxySoaClient$2.getType','','2','0','java.lang.reflect.Type','','');
end; 
/ 
--------准备注册：colCommunicationWSProxySoaServer服务-------
begin 
 	 P_SOA_REG_SERVICE('colCommunicationWSProxySoaServer','','','','soa-service-register_20151204145207','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.wsproxy.ColCommunicationWSProxySoaServer');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.queryReceLocalTrackTableData','queryReceLocalTrackTableData','queryReceLocalTrackTableData','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalTrackTableData_I0','colCommunicationWSProxySoaServer.queryReceLocalTrackTableData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalTrackTableData_I1','colCommunicationWSProxySoaServer.queryReceLocalTrackTableData','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalTrackTableData_O','colCommunicationWSProxySoaServer.queryReceLocalTrackTableData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalTrackTableData_E0','colCommunicationWSProxySoaServer.queryReceLocalTrackTableData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.messageReceive','messageReceive','messageReceive','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.messageReceive_I0','colCommunicationWSProxySoaServer.messageReceive','','1','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.messinteract.model.SendMessageVO','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.messageReceive_I1','colCommunicationWSProxySoaServer.messageReceive','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.messageReceive_E0','colCommunicationWSProxySoaServer.messageReceive','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','readDeployInfoByDirectoryCodeWithPage','readDeployInfoByDirectoryCodeWithPage','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I0','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I1','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I2','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','2','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I3','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','3','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I4','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','4','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I5','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','5','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_I6','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','1','6','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_O','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','2','0','com.comtop.bpms.base.BpmsPageBean','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage_E0','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCodeWithPage','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.uninstallColByIdAndVersion','uninstallColByIdAndVersion','uninstallColByIdAndVersion','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.uninstallColByIdAndVersion_I0','colCommunicationWSProxySoaServer.uninstallColByIdAndVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.uninstallColByIdAndVersion_I1','colCommunicationWSProxySoaServer.uninstallColByIdAndVersion','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.uninstallColByIdAndVersion_I2','colCommunicationWSProxySoaServer.uninstallColByIdAndVersion','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.uninstallColByIdAndVersion_I3','colCommunicationWSProxySoaServer.uninstallColByIdAndVersion','','1','3','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.uninstallColByIdAndVersion_E0','colCommunicationWSProxySoaServer.uninstallColByIdAndVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess','saveCollabarationInfoByProcess','saveCollabarationInfoByProcess','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess_I0','colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess','','1','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.parse.definition.ColDefinition','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess_I1','colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess_E0','colCommunicationWSProxySoaServer.saveCollabarationInfoByProcess','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.saveCollabarationInfos','saveCollabarationInfos','saveCollabarationInfos','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfos_I0','colCommunicationWSProxySoaServer.saveCollabarationInfos','','1','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.parse.definition.ColDefinition','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfos_I1','colCommunicationWSProxySoaServer.saveCollabarationInfos','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfos_I2','colCommunicationWSProxySoaServer.saveCollabarationInfos','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.saveCollabarationInfos_E0','colCommunicationWSProxySoaServer.saveCollabarationInfos','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.queryProcessDefFile','queryProcessDefFile','queryProcessDefFile','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryProcessDefFile_I0','colCommunicationWSProxySoaServer.queryProcessDefFile','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryProcessDefFile_I1','colCommunicationWSProxySoaServer.queryProcessDefFile','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryProcessDefFile_I2','colCommunicationWSProxySoaServer.queryProcessDefFile','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryProcessDefFile_O','colCommunicationWSProxySoaServer.queryProcessDefFile','','2','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryProcessDefFile_E0','colCommunicationWSProxySoaServer.queryProcessDefFile','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.queryContentByCondition','queryContentByCondition','queryContentByCondition','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I0','colCommunicationWSProxySoaServer.queryContentByCondition','','1','0','com.comtop.bpms.common.model.DeployQueryConditionInfo','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I1','colCommunicationWSProxySoaServer.queryContentByCondition','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I2','colCommunicationWSProxySoaServer.queryContentByCondition','','1','2','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I3','colCommunicationWSProxySoaServer.queryContentByCondition','','1','3','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I4','colCommunicationWSProxySoaServer.queryContentByCondition','','1','4','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I5','colCommunicationWSProxySoaServer.queryContentByCondition','','1','5','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_I6','colCommunicationWSProxySoaServer.queryContentByCondition','','1','6','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_O','colCommunicationWSProxySoaServer.queryContentByCondition','','2','0','com.comtop.bpms.base.BpmsPageBean','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryContentByCondition_E0','colCommunicationWSProxySoaServer.queryContentByCondition','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','readDeployInfoByDirectoryCode','readDeployInfoByDirectoryCode','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_I0','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_I1','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_I2','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','1','2','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_I3','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','1','3','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_I4','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','1','4','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_O','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','2','0','java.util.List','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode_E0','colCommunicationWSProxySoaServer.readDeployInfoByDirectoryCode','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.receiveTestServiceConnection','receiveTestServiceConnection','receiveTestServiceConnection','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.receiveTestServiceConnection_I0','colCommunicationWSProxySoaServer.receiveTestServiceConnection','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.receiveTestServiceConnection_O','colCommunicationWSProxySoaServer.receiveTestServiceConnection','','2','0','java.lang.String','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.findColDefinition','findColDefinition','findColDefinition','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColDefinition_I0','colCommunicationWSProxySoaServer.findColDefinition','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColDefinition_I1','colCommunicationWSProxySoaServer.findColDefinition','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColDefinition_I2','colCommunicationWSProxySoaServer.findColDefinition','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColDefinition_O','colCommunicationWSProxySoaServer.findColDefinition','','2','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.parse.definition.ColDefinition','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColDefinition_E0','colCommunicationWSProxySoaServer.findColDefinition','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.readDirectoryChildElements','readDirectoryChildElements','readDirectoryChildElements','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDirectoryChildElements_I0','colCommunicationWSProxySoaServer.readDirectoryChildElements','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDirectoryChildElements_I1','colCommunicationWSProxySoaServer.readDirectoryChildElements','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDirectoryChildElements_I2','colCommunicationWSProxySoaServer.readDirectoryChildElements','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDirectoryChildElements_O','colCommunicationWSProxySoaServer.readDirectoryChildElements','','2','0','java.util.List','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDirectoryChildElements_E0','colCommunicationWSProxySoaServer.readDirectoryChildElements','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.querytLastProcessDefFile','querytLastProcessDefFile','querytLastProcessDefFile','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querytLastProcessDefFile_I0','colCommunicationWSProxySoaServer.querytLastProcessDefFile','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querytLastProcessDefFile_I1','colCommunicationWSProxySoaServer.querytLastProcessDefFile','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querytLastProcessDefFile_O','colCommunicationWSProxySoaServer.querytLastProcessDefFile','','2','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querytLastProcessDefFile_E0','colCommunicationWSProxySoaServer.querytLastProcessDefFile','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.queryReceLocalAllTrackData','queryReceLocalAllTrackData','queryReceLocalAllTrackData','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalAllTrackData_I0','colCommunicationWSProxySoaServer.queryReceLocalAllTrackData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalAllTrackData_I1','colCommunicationWSProxySoaServer.queryReceLocalAllTrackData','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalAllTrackData_O','colCommunicationWSProxySoaServer.queryReceLocalAllTrackData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.queryReceLocalAllTrackData_E0','colCommunicationWSProxySoaServer.queryReceLocalAllTrackData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.getLastColVersion','getLastColVersion','getLastColVersion','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastColVersion_I0','colCommunicationWSProxySoaServer.getLastColVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastColVersion_I1','colCommunicationWSProxySoaServer.getLastColVersion','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastColVersion_O','colCommunicationWSProxySoaServer.getLastColVersion','','2','0','java.lang.Integer','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastColVersion_E0','colCommunicationWSProxySoaServer.getLastColVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.readRootDirectory','readRootDirectory','readRootDirectory','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readRootDirectory_I0','colCommunicationWSProxySoaServer.readRootDirectory','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readRootDirectory_I1','colCommunicationWSProxySoaServer.readRootDirectory','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readRootDirectory_O','colCommunicationWSProxySoaServer.readRootDirectory','','2','0','java.util.List','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readRootDirectory_E0','colCommunicationWSProxySoaServer.readRootDirectory','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.recoverColByIdAndVersion','recoverColByIdAndVersion','recoverColByIdAndVersion','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.recoverColByIdAndVersion_I0','colCommunicationWSProxySoaServer.recoverColByIdAndVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.recoverColByIdAndVersion_I1','colCommunicationWSProxySoaServer.recoverColByIdAndVersion','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.recoverColByIdAndVersion_I2','colCommunicationWSProxySoaServer.recoverColByIdAndVersion','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.recoverColByIdAndVersion_I3','colCommunicationWSProxySoaServer.recoverColByIdAndVersion','','1','3','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.recoverColByIdAndVersion_E0','colCommunicationWSProxySoaServer.recoverColByIdAndVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.getLastProcessVersion','getLastProcessVersion','getLastProcessVersion','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastProcessVersion_I0','colCommunicationWSProxySoaServer.getLastProcessVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastProcessVersion_I1','colCommunicationWSProxySoaServer.getLastProcessVersion','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastProcessVersion_O','colCommunicationWSProxySoaServer.getLastProcessVersion','','2','0','java.lang.Integer','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.getLastProcessVersion_E0','colCommunicationWSProxySoaServer.getLastProcessVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.querySendLocalTrackTableData','querySendLocalTrackTableData','querySendLocalTrackTableData','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalTrackTableData_I0','colCommunicationWSProxySoaServer.querySendLocalTrackTableData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalTrackTableData_I1','colCommunicationWSProxySoaServer.querySendLocalTrackTableData','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalTrackTableData_I2','colCommunicationWSProxySoaServer.querySendLocalTrackTableData','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalTrackTableData_O','colCommunicationWSProxySoaServer.querySendLocalTrackTableData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalTrackTableData_E0','colCommunicationWSProxySoaServer.querySendLocalTrackTableData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId','readDefinitionFileContextByDeployeId','readDefinitionFileContextByDeployeId','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId_I0','colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId_I1','colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId_O','colCommunicationWSProxySoaServer.readDefinitionFileContextByDeployeId','','2','0','java.lang.String','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.findColFileContent','findColFileContent','findColFileContent','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColFileContent_I0','colCommunicationWSProxySoaServer.findColFileContent','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColFileContent_I1','colCommunicationWSProxySoaServer.findColFileContent','','1','1','int','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColFileContent_I2','colCommunicationWSProxySoaServer.findColFileContent','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColFileContent_O','colCommunicationWSProxySoaServer.findColFileContent','','2','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.findColFileContent_E0','colCommunicationWSProxySoaServer.findColFileContent','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaServer.querySendLocalAllTrackData','querySendLocalAllTrackData','querySendLocalAllTrackData','colCommunicationWSProxySoaServer','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalAllTrackData_I0','colCommunicationWSProxySoaServer.querySendLocalAllTrackData','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalAllTrackData_I1','colCommunicationWSProxySoaServer.querySendLocalAllTrackData','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalAllTrackData_I2','colCommunicationWSProxySoaServer.querySendLocalAllTrackData','','1','2','java.lang.String','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalAllTrackData_O','colCommunicationWSProxySoaServer.querySendLocalAllTrackData','','2','0','com.comtop.bpms.common.model.NodeTrackInfo[]','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaServer.querySendLocalAllTrackData_E0','colCommunicationWSProxySoaServer.querySendLocalAllTrackData','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
end; 
/ 
--------准备注册：colCommunicationWSProxySoaClient$3服务-------
begin 
 	 P_SOA_REG_SERVICE('colCommunicationWSProxySoaClient$3','','','','soa-service-register_20151204145207','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.wsproxy.ColCommunicationWSProxySoaClient$3');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient$3.getType','getType','getType','colCommunicationWSProxySoaClient$3','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$3.getType_O','colCommunicationWSProxySoaClient$3.getType','','2','0','java.lang.reflect.Type','','');
 	 P_SOA_REG_Method('colCommunicationWSProxySoaClient$3.compareTo','compareTo','compareTo','colCommunicationWSProxySoaClient$3','0','0','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$3.compareTo_I0','colCommunicationWSProxySoaClient$3.compareTo','','1','0','comtop.soa.com.fasterxml.jackson.core.type.TypeReference','','');
 	 p_soa_reg_Params('colCommunicationWSProxySoaClient$3.compareTo_O','colCommunicationWSProxySoaClient$3.compareTo','','2','0','int','','');
end; 
/ 
commit;


--【补丁】2015-12-1_16-20_BPMS_V1.4.74_CR20151201118062 _解决查扩展属性性能慢及上报可能导致死锁的问题_李欢
create INDEX INDEX1_ATT_NODE_REL on BPMS_DEF_ATT_NODE_REL(PROCESS_ID,VERSION) nologging;
create INDEX INDEX2_ATT_NODE_REL on BPMS_DEF_ATT_NODE_REL(NODE_ID) nologging;
create INDEX INDEX3_ATT_NODE_REL on BPMS_DEF_ATT_NODE_REL(ATT_ID) nologging;

--【补丁】2015-12-10_17-30_BPMS_V1.4.78_无_将BPMS流程跟踪表默认表按照生产流程进行分区_李欢.sql

alter table BPMS_RU_NODE_TRACK
rename to BPMS_RU_NODE_TRACK_bak1210;
-- drop table BPMS_RU_NODE_TRACK_bak1210
alter table BPMS_RU_TRANS_TRACK
rename to BPMS_RU_TRANS_TRACK_bak1210;
-- drop table BPMS_RU_TRANS_TRACK_bak1210

--alter table BPMS_RU_NODE_TRACK_bak1210
--rename to BPMS_RU_NODE_TRACK;

--alter table BPMS_RU_TRANS_TRACK_bak1210
--rename to BPMS_RU_TRANS_TRACK;

--drop table BPMS_RU_NODE_TRACK;
create table BPMS_RU_NODE_TRACK
(
   NODE_TRACK_ID         VARCHAR2(40) not null,
  CUR_PROCESS_DEF_ID    VARCHAR2(40),
  CUR_PROCESS_ID        VARCHAR2(40),
  CUR_PROCESS_INS_ID    VARCHAR2(40),
  CUR_NODE_ID           VARCHAR2(40),
  CUR_NODE_NAME         VARCHAR2(100),
  CUR_NODE_TYPE         VARCHAR2(60),
  CUR_NODE_INS_ID       VARCHAR2(40),
  PARENT_PROCESS_ID     VARCHAR2(40),
  PARENT_PROCESS_INS_ID VARCHAR2(40),
  PARENT_NODE_INS_ID    VARCHAR2(40),
  MAIN_PROCESS_INS_ID   VARCHAR2(40),
  PRE_NODE_INS_ID       VARCHAR2(40),
  START_FLAG            VARCHAR2(10),
  CUR_NODE_INS_STATUS   NUMBER(2),
  OPERATE_TYPE          NUMBER(2),
  OPERATE_LEVEL         NUMBER(4),
  CREATE_TIME           TIMESTAMP(6),
  OVER_TIME             TIMESTAMP(6),
  NODE_NOTE             VARCHAR2(4000),
  GROUP_ID              VARCHAR2(40),
  GROUP_NAME            VARCHAR2(80),
  CUR_PROCESS_INS_TYPE  NUMBER(2),
  BOUNDARY_INS_ID       VARCHAR2(40),
  COMPLETE_TYPE         NUMBER(2) default -1
)
PARTITION BY LIST(CUR_PROCESS_ID)  (
                                     PARTITION
                                     P1
                                     VALUES('Pro3970854554520'),
                                     PARTITION
                                     P2
                                     VALUES('ProdPlanYearWorkFlow'),
                                     PARTITION
                                     P3
                                     VALUES('ProdPlanMonthWorkFlow'),
                                     PARTITION
                                     P4
                                     VALUES('DisElecturnoverProcess'),
                                     PARTITION
                                     P5
                                     VALUES('oneAssetProcess'),
                                     PARTITION
                                     P6
                                     VALUES('electurnoverManage'),
                                     PARTITION
                                     P7
                                     VALUES('assetchangeManage'),
                                     PARTITION
                                     P8
                                     VALUES('disAssetchangeManage'),
                                     PARTITION
                                     P9
                                     VALUES('TaskFormProjectWorkFlow'),
                                     PARTITION
                                     P10
                                     VALUES('TaskFormBureauProjectWorkFlow'),
                                     PARTITION
                                     P11
                                     VALUES('TaskFormTemplateWorkFlow'),
                                     PARTITION
                                     P12
                                     VALUES('TaskFormBureauTemplateWorkFlow'),
                                     PARTITION
                                     P13
                                     VALUES('TaskFormTeamTemplateWorkFlow'),
                                     PARTITION
                                     P14
                                     VALUES('AntiAccident'),
                                     PARTITION
                                     P15
                                     VALUES('ANTIACCIDENT_SEC'),
                                     PARTITION
                                     P16
                                     VALUES('AntiAccidentSuggest'),
                                     PARTITION
                                     P17
                                     VALUES('ReliabilityStopEventReportWorkFlow'),
                                     PARTITION
                                     P18
                                     VALUES('ReliabilityStopEventInformalWorkFlow'),
                                     PARTITION
                                     P19
                                     VALUES('ReliabilityBreakerCutoff'),
                                     PARTITION
                                     P20
                                     VALUES('ReliabilityBreakerOperational'),
                                     PARTITION
                                     P21
                                     VALUES('PRODUCTION_INDEX_PROCESS_PUBLIC'),
                                     PARTITION
                                     P22
                                     VALUES('OutagePlanYearWorkflow?'),
                                     PARTITION
                                     P23
                                     VALUES('OutagePlanMonthWorkflow?'),
                                     PARTITION
                                     P24
                                     VALUES('OutageApplyCommWorkflow?'),
                                     PARTITION
                                     P25
                                     VALUES('DEFECT_PROCESS'),
                                     PARTITION
                                     P26
                                     VALUES(default)
                                   );
-- Add comments to the table 
comment on table BPMS_RU_NODE_TRACK
  is '记录流程运行的节点跟踪信息';
-- Add comments to the columns 
comment on column BPMS_RU_NODE_TRACK.NODE_TRACK_ID
  is '节点跟踪ID，主键';
comment on column BPMS_RU_NODE_TRACK.CUR_PROCESS_DEF_ID
  is '当前流程定义ID';
comment on column BPMS_RU_NODE_TRACK.CUR_PROCESS_ID
  is '当前流程ID';
comment on column BPMS_RU_NODE_TRACK.CUR_PROCESS_INS_ID
  is '当前流程实例ID';
comment on column BPMS_RU_NODE_TRACK.CUR_NODE_ID
  is '当前节点ID';
comment on column BPMS_RU_NODE_TRACK.CUR_NODE_NAME
  is '当前节点名称';
comment on column BPMS_RU_NODE_TRACK.CUR_NODE_TYPE
  is '当前节点的类型，如用户任务、脚本任务、网关等';
comment on column BPMS_RU_NODE_TRACK.CUR_NODE_INS_ID
  is '当前节点实例ID';
comment on column BPMS_RU_NODE_TRACK.PARENT_PROCESS_ID
  is '(预留，暂未用)父流程的唯一ID';
comment on column BPMS_RU_NODE_TRACK.PARENT_PROCESS_INS_ID
  is '父流程实例ID';
comment on column BPMS_RU_NODE_TRACK.PARENT_NODE_INS_ID
  is '父节点实例ID';
comment on column BPMS_RU_NODE_TRACK.MAIN_PROCESS_INS_ID
  is '主流程实例ID';
comment on column BPMS_RU_NODE_TRACK.PRE_NODE_INS_ID
  is '前一节点实例ID';
comment on column BPMS_RU_NODE_TRACK.START_FLAG
  is '(预留，暂未用)是否为起始记录，每一次由上报开始的待办记录为起始记录，0表示非起始记录，1表示起始记录';
comment on column BPMS_RU_NODE_TRACK.CUR_NODE_INS_STATUS
  is '当前节点实例的运行状态，1表示待办，2处理中（多活动实例，预留），3为处理结束,4挂起，5为终止';
comment on column BPMS_RU_NODE_TRACK.OPERATE_TYPE
  is '当前跟踪轨迹的操作类型(产生的方式)，1启动，2发送，3回退，5终止，6挂起，7恢复';
comment on column BPMS_RU_NODE_TRACK.OPERATE_LEVEL
  is '当前节点实例所处的层级，按流程节点的执行顺序依次递加。';
comment on column BPMS_RU_NODE_TRACK.CREATE_TIME
  is '创建时间';
comment on column BPMS_RU_NODE_TRACK.OVER_TIME
  is '审批结束时间';
comment on column BPMS_RU_NODE_TRACK.NODE_NOTE
  is '(预留，暂未用)节点审批意见';
comment on column BPMS_RU_NODE_TRACK.GROUP_ID
  is '组ID';
comment on column BPMS_RU_NODE_TRACK.GROUP_NAME
  is '组名称';
comment on column BPMS_RU_NODE_TRACK.CUR_PROCESS_INS_TYPE
  is '类型 1 主流实例，2 子流程实例 ，3 执行实例';
comment on column BPMS_RU_NODE_TRACK.BOUNDARY_INS_ID
  is '边界流程实例ID，如果任务存在边界事件被触发，则任务上会有该值';
comment on column BPMS_RU_NODE_TRACK.COMPLETE_TYPE
  is '处理完成的类型(完成的方式)；1：启动、2：发送、3：回退、5：终止、6：恢复、7：挂起、9：协作补偿下发、10：跳转下发、-1：未处理';
-- Create/Recreate primary, unique and foreign key constraints 
alter table BPMS_RU_NODE_TRACK add primary key (NODE_TRACK_ID) using index nologging;
-- Create/Recreate indexes 
create index NODETRACK_C_INDEX1 on BPMS_RU_NODE_TRACK (MAIN_PROCESS_INS_ID) nologging;
create index NODETRACK_C_INDEX2 on BPMS_RU_NODE_TRACK (CUR_NODE_INS_ID) nologging;

-- Create table
--drop table BPMS_RU_TRANS_TRACK;
create table BPMS_RU_TRANS_TRACK
(
  TRANS_TRACK_ID       VARCHAR2(40) not null,
  NODE_INS_ID          VARCHAR2(40),
  ACTIVE_INS_ID        VARCHAR2(40),
  NODE_TRACK_ID        VARCHAR2(40),
  WORK_ID              VARCHAR2(40),
  TRANS_FLAG           NUMBER(2),
  ACTOR_ID             VARCHAR2(64),
  ACTOR_NAME           VARCHAR2(50),
  AUTHORIZER_ID        VARCHAR2(64),
  AUTHORIZER_NAME      VARCHAR2(64),
  CREATE_TIME          TIMESTAMP(6),
  HANDLE_TIME          TIMESTAMP(6),
  OVER_TIME            TIMESTAMP(6),
  MSG                  VARCHAR2(4000),
  MSG_TYPE             NUMBER(2),
  RESULT_FLAG          VARCHAR2(20),
  BACK_FLAG            NUMBER(2),
  CONDITION            VARCHAR2(100),
  CONDITION_VALUE      VARCHAR2(100),
  CONDITION_RESULT     NUMBER(2),
  RESSIGNM_PERSON_ID   VARCHAR2(64),
  RESSIGNM_PERSON_NAME VARCHAR2(64),
  COMPLETE_TYPE        NUMBER(2) default -1,
  REVOKE_FLAG          NUMBER(1) default 0,
  PRE_TRANS_TRACK_ID   VARCHAR2(40),
  PROCESS_ID            VARCHAR2(40),
  MAIN_PROCESS_INS_ID  VARCHAR2(40)
)
PARTITION BY LIST(PROCESS_ID)  (
                                  PARTITION
                                   P1
                                   VALUES('Pro3970854554520'),
                                   PARTITION
                                   P2
                                   VALUES('ProdPlanYearWorkFlow'),
                                   PARTITION
                                   P3
                                   VALUES('ProdPlanMonthWorkFlow'),
                                   PARTITION
                                   P4
                                   VALUES('DisElecturnoverProcess'),
                                   PARTITION
                                   P5
                                   VALUES('oneAssetProcess'),
                                   PARTITION
                                   P6
                                   VALUES('electurnoverManage'),
                                   PARTITION
                                   P7
                                   VALUES('assetchangeManage'),
                                   PARTITION
                                   P8
                                   VALUES('disAssetchangeManage'),
                                   PARTITION
                                   P9
                                   VALUES('TaskFormProjectWorkFlow'),
                                   PARTITION
                                   P10
                                   VALUES('TaskFormBureauProjectWorkFlow'),
                                   PARTITION
                                   P11
                                   VALUES('TaskFormTemplateWorkFlow'),
                                   PARTITION
                                   P12
                                   VALUES('TaskFormBureauTemplateWorkFlow'),
                                   PARTITION
                                   P13
                                   VALUES('TaskFormTeamTemplateWorkFlow'),
                                   PARTITION
                                   P14
                                   VALUES('AntiAccident'),
                                   PARTITION
                                   P15
                                   VALUES('ANTIACCIDENT_SEC'),
                                   PARTITION
                                   P16
                                   VALUES('AntiAccidentSuggest'),
                                   PARTITION
                                   P17
                                   VALUES('ReliabilityStopEventReportWorkFlow'),
                                   PARTITION
                                   P18
                                   VALUES('ReliabilityStopEventInformalWorkFlow'),
                                   PARTITION
                                   P19
                                   VALUES('ReliabilityBreakerCutoff'),
                                   PARTITION
                                   P20
                                   VALUES('ReliabilityBreakerOperational'),
                                   PARTITION
                                   P21
                                   VALUES('PRODUCTION_INDEX_PROCESS_PUBLIC'),
                                   PARTITION
                                   P22
                                   VALUES('OutagePlanYearWorkflow?'),
                                   PARTITION
                                   P23
                                   VALUES('OutagePlanMonthWorkflow?'),
                                   PARTITION
                                   P24
                                   VALUES('OutageApplyCommWorkflow?'),
                                   PARTITION
                                   P25
                                   VALUES('DEFECT_PROCESS'),
                                   PARTITION
                                   P26
                                   VALUES(default)
                                 );
-- Add comments to the table 
comment on table BPMS_RU_TRANS_TRACK
  is '用于记录节点上的处理信息，如果是人工节点就记录处理人、处理意见等。如果是条件节点就记录节点的条件值及结果等';
-- Add comments to the columns 
comment on column BPMS_RU_TRANS_TRACK.TRANS_TRACK_ID
  is '作为记录的主键';
comment on column BPMS_RU_TRANS_TRACK.NODE_INS_ID
  is '节点实例ID';
comment on column BPMS_RU_TRANS_TRACK.ACTIVE_INS_ID
  is '活动实例ID';
comment on column BPMS_RU_TRANS_TRACK.NODE_TRACK_ID
  is '节点跟踪ID';
comment on column BPMS_RU_TRANS_TRACK.WORK_ID
  is '工单编号';
comment on column BPMS_RU_TRANS_TRACK.TRANS_FLAG
  is '1为待办，2为处理中，3为处理结束，4为他人办理，5终止，6改派，7挂起';
comment on column BPMS_RU_TRANS_TRACK.ACTOR_ID
  is '处理人员编号';
comment on column BPMS_RU_TRANS_TRACK.ACTOR_NAME
  is '处理人姓名';
comment on column BPMS_RU_TRANS_TRACK.AUTHORIZER_ID
  is '委托人编号';
comment on column BPMS_RU_TRANS_TRACK.AUTHORIZER_NAME
  is '委托人姓名';
comment on column BPMS_RU_TRANS_TRACK.CREATE_TIME
  is '创建时间';
comment on column BPMS_RU_TRANS_TRACK.HANDLE_TIME
  is '开始办理时间';
comment on column BPMS_RU_TRANS_TRACK.OVER_TIME
  is '最后修改时间(办结时间)';
comment on column BPMS_RU_TRANS_TRACK.MSG
  is '填写意见';
comment on column BPMS_RU_TRANS_TRACK.MSG_TYPE
  is '记录意见类型';
comment on column BPMS_RU_TRANS_TRACK.RESULT_FLAG
  is '结论标志';
comment on column BPMS_RU_TRANS_TRACK.BACK_FLAG
  is '默认指定操作标志。0：默认，1：指定';
comment on column BPMS_RU_TRANS_TRACK.CONDITION
  is '条件（预留）';
comment on column BPMS_RU_TRANS_TRACK.CONDITION_VALUE
  is '条件值（预留）';
comment on column BPMS_RU_TRANS_TRACK.CONDITION_RESULT
  is '条件结果（预留）';
comment on column BPMS_RU_TRANS_TRACK.RESSIGNM_PERSON_ID
  is '改派人编号';
comment on column BPMS_RU_TRANS_TRACK.RESSIGNM_PERSON_NAME
  is '该派人姓名';
comment on column BPMS_RU_TRANS_TRACK.COMPLETE_TYPE
  is '处理完成的类型；1：启动、2：发送、3：回退、5：终止、6：恢复、7：挂起、8:改派、9：协作补偿下发、10：跳转下发、-1：未处理';
comment on column BPMS_RU_TRANS_TRACK.REVOKE_FLAG
  is '撤回标志，0：没有撤回，1：有过撤回操作，默认为0';
comment on column BPMS_RU_TRANS_TRACK.PRE_TRANS_TRACK_ID
  is '记录前一转发的处理跟踪id';
comment on column BPMS_RU_TRANS_TRACK.PROCESS_ID
  is '流程编号';
comment on column BPMS_RU_TRANS_TRACK.MAIN_PROCESS_INS_ID
  is '流程实例编号';

-- Create/Recreate primary, unique and foreign key constraints 
alter table BPMS_RU_TRANS_TRACK add constraint BPMS_RU_TRANS_TRACK_C1 primary key (TRANS_TRACK_ID)using index nologging;
-- Create/Recreate indexes 
create index TRANSTRACK_C_INDEX1 on BPMS_RU_TRANS_TRACK (NODE_INS_ID) nologging;
create index TRANSTRACK_C_INDEX2 on BPMS_RU_TRANS_TRACK (ACTOR_ID) nologging;
create index TRANSTRACK_C_INDEX3 on BPMS_RU_TRANS_TRACK (ACTIVE_INS_ID) nologging;
create index TRANSTRACK_C_INDEX4 on BPMS_RU_TRANS_TRACK (NODE_TRACK_ID) nologging;
create index TRANSTRACK_C_INDEX5 on BPMS_RU_TRANS_TRACK (MAIN_PROCESS_INS_ID) nologging;


--重新插入节点跟踪表的数据
alter table BPMS_RU_NODE_TRACK nologging;
insert into BPMS_RU_NODE_TRACK
  (NODE_TRACK_ID,
   CUR_PROCESS_DEF_ID,
   CUR_PROCESS_ID,
   CUR_PROCESS_INS_ID,
   CUR_NODE_ID,
   CUR_NODE_NAME,
   CUR_NODE_TYPE,
   CUR_NODE_INS_ID,
   PARENT_PROCESS_ID,
   PARENT_PROCESS_INS_ID,
   PARENT_NODE_INS_ID,
   MAIN_PROCESS_INS_ID,
   PRE_NODE_INS_ID,
   START_FLAG,
   CUR_NODE_INS_STATUS,
   OPERATE_TYPE,
   OPERATE_LEVEL,
   CREATE_TIME,
   OVER_TIME,
   NODE_NOTE,
   GROUP_ID,
   GROUP_NAME,
   CUR_PROCESS_INS_TYPE,
   BOUNDARY_INS_ID,
   COMPLETE_TYPE)
  select tt.NODE_TRACK_ID,
         tt.CUR_PROCESS_DEF_ID,
         tt.CUR_PROCESS_ID,
         tt.CUR_PROCESS_INS_ID,
         tt.CUR_NODE_ID,
         tt.CUR_NODE_NAME,
         tt.CUR_NODE_TYPE,
         tt.CUR_NODE_INS_ID,
         tt.PARENT_PROCESS_ID,
         tt.PARENT_PROCESS_INS_ID,
         tt.PARENT_NODE_INS_ID,
         tt.MAIN_PROCESS_INS_ID,
         tt.PRE_NODE_INS_ID,
         tt.START_FLAG,
         tt.CUR_NODE_INS_STATUS,
         tt.OPERATE_TYPE,
         tt.OPERATE_LEVEL,
         tt.CREATE_TIME,
         tt.OVER_TIME,
         tt.NODE_NOTE,
         tt.GROUP_ID,
         tt.GROUP_NAME,
         tt.CUR_PROCESS_INS_TYPE,
         tt.BOUNDARY_INS_ID,
         tt.COMPLETE_TYPE
    from BPMS_RU_NODE_TRACK_bak1210 tt;
alter table BPMS_RU_NODE_TRACK logging;

--重新插入处理跟踪表的数据
alter table BPMS_RU_TRANS_TRACK nologging;
insert into BPMS_RU_TRANS_TRACK
  (TRANS_TRACK_ID,
   NODE_INS_ID,
   ACTIVE_INS_ID,
   NODE_TRACK_ID,
   WORK_ID,
   TRANS_FLAG,
   ACTOR_ID,
   ACTOR_NAME,
   AUTHORIZER_ID,
   AUTHORIZER_NAME,
   CREATE_TIME,
   HANDLE_TIME,
   OVER_TIME,
   MSG,
   MSG_TYPE,
   RESULT_FLAG,
   BACK_FLAG,
   CONDITION,
   CONDITION_VALUE,
   CONDITION_RESULT,
   RESSIGNM_PERSON_ID,
   RESSIGNM_PERSON_NAME,
   COMPLETE_TYPE,
   REVOKE_FLAG,
   PRE_TRANS_TRACK_ID,
   PROCESS_ID,
   MAIN_PROCESS_INS_ID)
  select tt.TRANS_TRACK_ID,
         tt.NODE_INS_ID,
         tt.ACTIVE_INS_ID,
         tt.NODE_TRACK_ID,
         tt.WORK_ID,
         tt.TRANS_FLAG,
         tt.ACTOR_ID,
         tt.ACTOR_NAME,
         tt.AUTHORIZER_ID,
         tt.AUTHORIZER_NAME,
         tt.CREATE_TIME,
         tt.HANDLE_TIME,
         tt.OVER_TIME,
         tt.MSG,
         tt.MSG_TYPE,
         tt.RESULT_FLAG,
         tt.BACK_FLAG,
         tt.CONDITION,
         tt.CONDITION_VALUE,
         tt.CONDITION_RESULT,
         tt.RESSIGNM_PERSON_ID,
         tt.RESSIGNM_PERSON_NAME,
         tt.COMPLETE_TYPE,
         tt.REVOKE_FLAG,
         tt.PRE_TRANS_TRACK_ID,
         tt.PROCESS_ID,
         tt.MAIN_PROCESS_INS_ID
    from BPMS_RU_TRANS_TRACK_bak1210 tt;
alter table BPMS_RU_TRANS_TRACK logging;
commit;
/

--【补丁】2015-12-21_17-30_BPMS_V1.4.80_无_企信发代办消息_翟羽佳
create table bpms_todo_task_for_eim
(
todo_task_id varchar2(40),
MSG_CONTENT varchar2(2000),
TO_USER_ID varchar2(40),
PROCESS_ID varchar2(100)
);
comment on table bpms_todo_task_for_eim
  is '企信代办消息提醒表';
comment on column bpms_todo_task_for_eim.todo_task_id
  is '代办id';
comment on column bpms_todo_task_for_eim.MSG_CONTENT
  is '代办提醒消息体';
comment on column bpms_todo_task_for_eim.TO_USER_ID
  is '用户id';
comment on column bpms_todo_task_for_eim.PROCESS_ID
  is '流程ID';   
 
--插入节点上全局扩展属性
insert into bpms_def_att(ATT_ID,ATT_KEY,Att_Name,att_type,Default_Value,Att_Scope)
values ('BPMS_SYSTEM_ATT_ISSENDEIM','isSendEim','是发送企信提醒','Boolean','true','SYSTEM');

commit;

--【补丁】2015-12-30_16-00_BPMS_V1.4.82_无_给流程定义表和流程实例表添加时间戳字段_彭建华
alter table BPMS_DEF_DEPLOYE_FILE add create_time timestamp(6);
alter table BPMS_DEF_DEPLOYE_FILE add  modify_date timestamp(6);

alter table BPMS_DEF_WORKTRANSFER_LOG add  create_time timestamp(6);
alter table BPMS_DEF_WORKTRANSFER_LOG add  modify_date timestamp(6);

alter table BPMS_DEF_COL add  create_time timestamp(6);
alter table BPMS_DEF_COL add  modify_date timestamp(6);

alter table BPMS_DEF_PROCESS add  create_time timestamp(6);
alter table BPMS_DEF_PROCESS add  modify_date timestamp(6);

alter table BPMS_DEF_COL_FILECONTENT add  create_time timestamp(6);
alter table BPMS_DEF_COL_FILECONTENT add  modify_date timestamp(6); 

alter table BPMS_DEF_COL_PROCESS add  create_time timestamp(6);
alter table BPMS_DEF_COL_PROCESS add  modify_date timestamp(6);

alter table bpms_def_col_deployelog add  create_time timestamp(6);
alter table bpms_def_col_deployelog add  modify_date timestamp(6);

alter table bpms_def_deploye_log add  create_time timestamp(6);
alter table bpms_def_deploye_log add  modify_date timestamp(6);


alter table BPMS_DEF_ATT_NODE_REL add  create_time timestamp(6);
alter table BPMS_DEF_ATT_NODE_REL add  modify_date timestamp(6);

alter table BPMS_DEF_DEPLOY_COLCON_READ add  create_time timestamp(6);
alter table BPMS_DEF_DEPLOY_COLCON_READ add  modify_date timestamp(6);

alter table BPMS_DEF_NODE_POST add  create_time timestamp(6);
alter table BPMS_DEF_NODE_POST add  modify_date timestamp(6);

alter table BPMS_DEF_ATT add  create_time timestamp(6);
alter table BPMS_DEF_ATT add  modify_date timestamp(6);

alter table BPMS_DEF_DEPLOY_COL_READ add  create_time timestamp(6);
alter table BPMS_DEF_DEPLOY_COL_READ add  modify_date timestamp(6);

alter table BPMS_DEF_HUMAN_NODE add  create_time timestamp(6);
alter table BPMS_DEF_HUMAN_NODE add  modify_date timestamp(6);

alter table BPMS_DEF_ATT_ENUM add  create_time timestamp(6);
alter table BPMS_DEF_ATT_ENUM add  modify_date timestamp(6);

alter table BPMS_DEF_DEPLOY_COL_ASYN add  create_time timestamp(6);
alter table BPMS_DEF_DEPLOY_COL_ASYN add  modify_date timestamp(6);

alter table BPMS_DEF_DEPLOYE add  create_time timestamp(6);
alter table BPMS_DEF_DEPLOYE add  modify_date timestamp(6);


DECLARE
  CURSOR bpms_tableNameCursor
  IS
    SELECT table_name
    FROM user_tables t
    WHERE t.table_name LIKE upper('BPMS_RU_%');
  c_row bpms_tableNameCursor%rowtype;
  rowCount INT;
  exeSQL   VARCHAR2(1000);
BEGIN
  FOR c_row IN bpms_tableNameCursor
  LOOP
    SELECT COUNT(1)
    INTO rowCount
    FROM user_tab_columns t
    WHERE t.table_name = c_row.table_name
    AND t.column_name  =upper('create_time');
    IF rowCount        = 0 THEN
      exeSQL          := 'alter table '|| c_row.table_name ||' add(CREATE_TIME TIMESTAMP (6))';
      EXECUTE immediate exeSQL;
    END IF;
    SELECT COUNT(1)
    INTO rowCount
    FROM user_tab_columns t
    WHERE t.table_name = c_row.table_name
    AND t.column_name  =upper('modify_date');
    IF rowCount        = 0 THEN
      exeSQL          :='alter table '|| c_row.table_name ||' add(MODIFY_DATE TIMESTAMP (6))';
      EXECUTE immediate exeSQL;
    END IF;
  END LOOP;
  COMMIT;
END;
/


CREATE OR REPLACE PROCEDURE PRO_BPMS_INNER_OUT_SYNC(dblink varchar2,
split_code varchar2
) AUTHID CURRENT_USER is
  /******************************************************************************
     NAME:       PRO_BPMS_INNER_OUT_SYNC
     PURPOSE:    基建内外网的工作流同步存储过程

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-12-15    翟羽佳       Created this procedure.
  ******************************************************************************/
 tableName            varchar2(40);
 varSql               varchar2(1000);
 BEGIN
  --1、先删除临时表
  varSql:='delete from processins_temp@'||dblink;
  execute immediate varSql;
  commit;
  tableName:='bpms_ru_todo_task_'||split_code;
  --2、往临时表加入数据
  varSql:='insert into processins_temp@'||dblink||' 
  select distinct(main_process_ins_id) from '||tableName||'@'||dblink;
  execute immediate varSql;
  commit;
  --3、删除代办统计表
  varSql:=' delete from bpms_ru_todo_task_all al where al.activity_ins_id in
 (select activity_ins_id from '||tableName||' where main_process_ins_id in
  (select main_process_ins_id from processins_temp@'||dblink||'))';
  execute immediate varSql;
  commit;
  --4、删除代办表
  varSql:=' delete from '||tableName||' where main_process_ins_id in
  (select main_process_ins_id from processins_temp@'||dblink||')';
  execute immediate varSql;
  commit;
END PRO_BPMS_INNER_OUT_SYNC;
/

CREATE OR REPLACE PROCEDURE PRO_BPMS_INNER_OUT_SYNC_MAIN(dblink varchar2
) AUTHID CURRENT_USER is
  /******************************************************************************
     NAME:       PRO_BPMS_INNER_OUT_SYNC_MAIN
     PURPOSE:    基建内外网的工作流同步存储过程

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-12-15    翟羽佳       Created this procedure.
  ******************************************************************************/
 BEGIN
 PRO_BPMS_INNER_OUT_SYNC(dblink,'LCAM');
 PRO_BPMS_INNER_OUT_SYNC(dblink,'EPPD');
 PRO_BPMS_INNER_OUT_SYNC(dblink,'PS');
END PRO_BPMS_INNER_OUT_SYNC_MAIN;
/

--【补丁】2016-01-11_17-30_BPMS_V1.4.85_无_流程撤回、流程不能删除报错问题解决_彭建华
create or replace 
PROCEDURE PRO_RU_REMOVE_TRH(processId        varchar2,
                                              mainProcessInsId varchar2) AUTHID CURRENT_USER IS

  /******************************************************************************
     NAME:       PRO_RU_Remove_trh
     PURPOSE:  (根据流程ID、流程实例ID进行数据移植)数据删除/移植思路，将运行时的流程实例数据移植到回收数据区中

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2012-11-15          1. Created this procedure.

     NOTES:

     Automatically available Auto Replace Keywords:
        Object Name:     PRO_RU_Remove_trh 流程实例数据删除
        Sysdate:         2012-11-15
        Date and Time:   2012-11-15, 17:57:40, and 2012-11-15 17:57:40
        Username:         (set in TOAD Options, Procedure Editor)
        Table Name:       (set in the "New PL/SQL Object" dialog)

  ******************************************************************************/
  process_redef_table_name varchar2(10); --记录流程定义所对应的分表后缀名
  redef_table_num          number(1); ---是否存在分表
  exeSQL                   varchar2(3000); --要执行SQL语句

  BPMS_RU_CC_TASK_TABLE     VARCHAR2(100); --抄送任务表名
  BPMS_RU_DONE_TASK_TABLE   VARCHAR2(100); --已办表名
  BPMS_RU_NODE_TRACK_TABLE  VARCHAR2(100); --节点跟踪表名
  BPMS_RU_PROCESS_INS_TABLE VARCHAR2(100); --流程实例表名
  BPMS_RU_TODO_TASK_TABLE   VARCHAR2(100); --待办表名
  BPMS_RU_VAR_TABLE         VARCHAR2(100); --变量表名

  BPMS_RU_ACTIVE_INS_TABLE  VARCHAR2(100); --活动实例表名
  BPMS_RU_NODE_ORBIT_TABLE  VARCHAR2(100); --节点轨迹表名
  BPMS_RU_TRANS_TRACK_TABLE VARCHAR2(100); --处理跟踪表名


  BPMS_RU_NODE_INS_TABLE VARCHAR2(100); --节点实例表名


  BPMS_RU_ATTACHMENT_TABLE VARCHAR2(100); --审批附件基本信息表名
  BPMS_RU_REVOKE_LOG_TABLE VARCHAR2(100); --流程撤回日志记录表名
  BPMS_RU_TODO_TASK_ALL_TABLE VARCHAR2(100); --待办任务总表表名
  BPMS_RU_COMPEVENT_INS_TB VARCHAR2(100); --补偿事件实例表名
  BPMS_RU_COMPEVENT_SUS_TB VARCHAR2(100); --补偿事件订阅表名
  BPMS_RU_MESEVENT_SUS_TABLE VARCHAR2(100); --消息事件订阅表名
  BPMS_RU_MESEVENT_TRI_TABLE VARCHAR2(100); --消息事件触发表表名
  BPMS_RU_SIGEVENT_TRI_TABLE VARCHAR2(100); --信号事件实例表表名
  BPMS_RU_SIGNEVENT_SUS_TABLE VARCHAR2(100); --信号事件订阅表表名

BEGIN

  if processId is not null then
    if mainProcessInsId is not null then

      select count(1) into redef_table_num  from BPMS_CFG_REDEF_TABLE t1 where t1.PROCESS_ID = '' || processId || '';

      --step1:分析流程分表信息，准备源数据表名
      if redef_table_num > 0 then
        --当用户有对此流程定义分表时，加上分表后缀
        select t2.SUFFIX_FLAG into process_redef_table_name from BPMS_CFG_REDEF_TABLE t2 where t2.PROCESS_ID = '' || processId || '';

        BPMS_RU_CC_TASK_TABLE     := 'BPMS_RU_CC_TASK_' || process_redef_table_name;
        BPMS_RU_DONE_TASK_TABLE   := 'BPMS_RU_DONE_TASK_' || process_redef_table_name;
        BPMS_RU_NODE_TRACK_TABLE  := 'BPMS_RU_NODE_TRACK_' || process_redef_table_name;
        BPMS_RU_PROCESS_INS_TABLE := 'BPMS_RU_PROCESS_INS_' || process_redef_table_name;
        BPMS_RU_TODO_TASK_TABLE   := 'BPMS_RU_TODO_TASK_' || process_redef_table_name;
        BPMS_RU_VAR_TABLE         := 'BPMS_RU_VAR_' || process_redef_table_name;
        BPMS_RU_ACTIVE_INS_TABLE  := 'BPMS_RU_ACTIVE_INS_' || process_redef_table_name;
        BPMS_RU_NODE_ORBIT_TABLE  := 'BPMS_RU_NODE_ORBIT_' || process_redef_table_name;
        BPMS_RU_TRANS_TRACK_TABLE := 'BPMS_RU_TRANS_TRACK_' || process_redef_table_name;
        BPMS_RU_NODE_INS_TABLE    := 'BPMS_RU_NODE_INS_' || process_redef_table_name;
        BPMS_RU_ATTACHMENT_TABLE  := 'BPMS_RU_ATTACHMENT_' || process_redef_table_name;
        BPMS_RU_REVOKE_LOG_TABLE  := 'BPMS_RU_REVOKE_LOG_' || process_redef_table_name;
        BPMS_RU_TODO_TASK_ALL_TABLE:= 'BPMS_RU_TODO_TASK_ALL';
        BPMS_RU_COMPEVENT_INS_TB:='BPMS_RU_COMPEVENT_INS_'|| process_redef_table_name;
        BPMS_RU_COMPEVENT_SUS_TB:='BPMS_RU_COMPEVENT_SUS_'|| process_redef_table_name;
        BPMS_RU_MESEVENT_SUS_TABLE :='BPMS_RU_MESEVENT_SUS_'|| process_redef_table_name;
        BPMS_RU_MESEVENT_TRI_TABLE :='BPMS_RU_MESEVENT_TRI_'|| process_redef_table_name;
        BPMS_RU_SIGEVENT_TRI_TABLE :='BPMS_RU_SIGEVENT_TRI_'|| process_redef_table_name;
        BPMS_RU_SIGNEVENT_SUS_TABLE :='BPMS_RU_SIGNEVENT_SUS_'|| process_redef_table_name;
      else
        --不加后缀，直接查原表
        BPMS_RU_CC_TASK_TABLE     := 'BPMS_RU_CC_TASK';
        BPMS_RU_DONE_TASK_TABLE   := 'BPMS_RU_DONE_TASK';
        BPMS_RU_NODE_TRACK_TABLE  := 'BPMS_RU_NODE_TRACK';
        BPMS_RU_PROCESS_INS_TABLE := 'BPMS_RU_PROCESS_INS';
        BPMS_RU_TODO_TASK_TABLE   := 'BPMS_RU_TODO_TASK';
        BPMS_RU_VAR_TABLE         := 'BPMS_RU_VAR';
        BPMS_RU_ACTIVE_INS_TABLE  := 'BPMS_RU_ACTIVE_INS';
        BPMS_RU_NODE_ORBIT_TABLE  := 'BPMS_RU_NODE_ORBIT';
        BPMS_RU_TRANS_TRACK_TABLE := 'BPMS_RU_TRANS_TRACK';
        BPMS_RU_NODE_INS_TABLE    := 'BPMS_RU_NODE_INS';
        BPMS_RU_ATTACHMENT_TABLE  := 'BPMS_RU_ATTACHMENT';
        BPMS_RU_REVOKE_LOG_TABLE  := 'BPMS_RU_REVOKE_LOG';
        BPMS_RU_TODO_TASK_ALL_TABLE:= 'BPMS_RU_TODO_TASK_ALL';
        BPMS_RU_COMPEVENT_INS_TB:='BPMS_RU_COMPEVENT_INS';
        BPMS_RU_COMPEVENT_SUS_TB:='BPMS_RU_COMPEVENT_SUS';
        BPMS_RU_MESEVENT_SUS_TABLE :='BPMS_RU_MESEVENT_SUS';
        BPMS_RU_MESEVENT_TRI_TABLE :='BPMS_RU_MESEVENT_TRI';
        BPMS_RU_SIGEVENT_TRI_TABLE :='BPMS_RU_SIGEVENT_TRI';
        BPMS_RU_SIGNEVENT_SUS_TABLE :='BPMS_RU_SIGNEVENT_SUS';
      end if;

      --step2:拼装并插入实例数据

      --直接依赖流程实例表

      --备份流程抄送数据
      exeSQL := ' insert into BPMS_RU_CC_TASK_TRH select * from ' ||
                BPMS_RU_CC_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
           --备份已办数据
      --exeSQL := ' insert into BPMS_RU_DONE_TASK_TRH select * from ' ||
      --          BPMS_RU_DONE_TASK_TABLE ||
      --          ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      --execute immediate exeSQL;
      pro_deletetask(BPMS_RU_DONE_TASK_TABLE,'BPMS_RU_DONE_TASK_TRH',mainProcessInsId);
      --备份节点跟踪数据
      exeSQL := ' insert into BPMS_RU_NODE_TRACK_TRH select * from ' ||
                BPMS_RU_NODE_TRACK_TABLE ||
                ' k where k.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份流程实例数据
      exeSQL := 'insert into BPMS_RU_PROCESS_INS_TRH select * from ' ||
                BPMS_RU_PROCESS_INS_TABLE ||
                ' p where p.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份待办任务数据
     -- exeSQL := 'insert into BPMS_RU_TODO_TASK_TRH select * from ' ||
     --           BPMS_RU_TODO_TASK_TABLE ||
     --           ' t where t.main_process_ins_id =''' || mainProcessInsId || '''';
      --execute immediate exeSQL;
      pro_deletetask(BPMS_RU_TODO_TASK_TABLE,'BPMS_RU_TODO_TASK_TRH',mainProcessInsId);
      --备份变量数据
      exeSQL := 'insert into BPMS_RU_VAR_TRH select * from ' ||
                BPMS_RU_VAR_TABLE || ' v where v.main_process_ins_id =''' ||
                mainProcessInsId || '''';
      execute immediate exeSQL;


      --间接依赖实例数据表
      exeSQL := 'insert into BPMS_RU_ACTIVE_INS_TRH select * from ' ||
                BPMS_RU_ACTIVE_INS_TABLE ||
                ' a where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where a.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --备份节点轨迹表数据
      exeSQL := 'insert into BPMS_RU_NODE_ORBIT_TRH select * from ' ||
                BPMS_RU_NODE_ORBIT_TABLE ||
                ' o where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where o.cur_node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --备份跟踪明细数据
      exeSQL := 'insert into BPMS_RU_TRANS_TRACK_TRH(TRANS_TRACK_ID, NODE_INS_ID, ACTIVE_INS_ID, NODE_TRACK_ID, WORK_ID, TRANS_FLAG, ACTOR_ID,
                ACTOR_NAME, CREATE_TIME, HANDLE_TIME, OVER_TIME, MSG, MSG_TYPE, RESULT_FLAG, BACK_FLAG,
                CONDITION, CONDITION_VALUE, CONDITION_RESULT, RESSIGNM_PERSON_ID, RESSIGNM_PERSON_NAME,
                COMPLETE_TYPE, REVOKE_FLAG, AUTHORIZER_ID, AUTHORIZER_NAME, PRE_TRANS_TRACK_ID,
                PROCESS_ID, MAIN_PROCESS_INS_ID,MODIFY_DATE) select 
                TRANS_TRACK_ID, NODE_INS_ID, ACTIVE_INS_ID, NODE_TRACK_ID, WORK_ID, TRANS_FLAG, ACTOR_ID,
                ACTOR_NAME, CREATE_TIME, HANDLE_TIME, OVER_TIME, MSG, MSG_TYPE, RESULT_FLAG, BACK_FLAG,
                CONDITION, CONDITION_VALUE, CONDITION_RESULT, RESSIGNM_PERSON_ID, RESSIGNM_PERSON_NAME,
                COMPLETE_TYPE, REVOKE_FLAG, AUTHORIZER_ID, AUTHORIZER_NAME, PRE_TRANS_TRACK_ID,
                PROCESS_ID, MAIN_PROCESS_INS_ID,MODIFY_DATE from ' ||
                BPMS_RU_TRANS_TRACK_TABLE ||
                ' r where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where r.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --备份节点实例表数据
      exeSQL := 'insert into BPMS_RU_NODE_INS_TRH select * from ' ||
                BPMS_RU_NODE_INS_TABLE ||' n where n.main_process_ins_id = ''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份审批附件信息表数据
      exeSQL := 'insert into BPMS_RU_ATTACHMENT_TRH select * from ' ||
            BPMS_RU_ATTACHMENT_TABLE|| ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.act_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            '  c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --备份流程撤回日志记录表数据
      exeSQL := 'insert into BPMS_RU_REVOKE_LOG_TRH select * from ' ||
            BPMS_RU_REVOKE_LOG_TABLE|| ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            '  c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --备份待办任务总表数据
      exeSQL := 'insert into BPMS_RU_TODO_TASK_ALL_TRH select * from '||
            BPMS_RU_TODO_TASK_ALL_TABLE||' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            '  c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --备份:信号事件实例表数据
      exeSQL := 'insert into BPMS_RU_COMPEVENT_INS_TRH select * from '||
            BPMS_RU_COMPEVENT_INS_TB||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:补偿事件订阅表数据
      exeSQL := 'insert into BPMS_RU_COMPEVENT_SUS_TRH select * from '||
            BPMS_RU_COMPEVENT_SUS_TB||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:消息事件订阅表数据
      exeSQL := 'insert into BPMS_RU_MESEVENT_SUS_TRH select * from '||
            BPMS_RU_MESEVENT_SUS_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:消息事件触发表数据
      exeSQL := 'insert into BPMS_RU_MESEVENT_TRI_TRH select * from '||
            BPMS_RU_MESEVENT_TRI_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:信号事件实例表数据
      exeSQL := 'insert into BPMS_RU_SIGEVENT_TRI_TRH select * from '||
            BPMS_RU_SIGEVENT_TRI_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:信号事件订阅表数据
      exeSQL := 'insert into BPMS_RU_SIGNEVENT_SUS_TRH select * from '||
            BPMS_RU_SIGNEVENT_SUS_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;





      --step3: 拼装并删除实例数据

      --直接依赖流程实例表
      --删除:信号事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_SIGNEVENT_SUS_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除信号事件实例表数据
      exeSQL := 'delete from '||BPMS_RU_SIGEVENT_TRI_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除消息事件触发表数据
      exeSQL := 'delete from '||BPMS_RU_MESEVENT_TRI_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除消息事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_MESEVENT_SUS_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除补偿事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_COMPEVENT_SUS_TB||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除信号事件实例表数据
      exeSQL := 'delete from '|| BPMS_RU_COMPEVENT_INS_TB||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除待办任务总表数据
      exeSQL := 'delete from '||BPMS_RU_TODO_TASK_ALL_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --删除:流程撤回日志记录表数据
      exeSQL := 'delete from ' || BPMS_RU_REVOKE_LOG_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --删除：审批附件信息表数据
      exeSQL := 'delete from ' || BPMS_RU_ATTACHMENT_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.act_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;


      --删除抄送数据
      exeSQL := ' delete from ' ||
                BPMS_RU_CC_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除已办任务数据
      exeSQL := ' delete from ' ||
                BPMS_RU_DONE_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除节点跟踪数据
      exeSQL := ' delete from ' ||
                BPMS_RU_NODE_TRACK_TABLE ||
                ' k where k.main_process_ins_id =''' || mainProcessInsId || '''
                  and k.CUR_PROCESS_ID =''' || processId || '''';
      execute immediate exeSQL;
      --删除流程实例数据
      exeSQL := 'delete from ' ||
                BPMS_RU_PROCESS_INS_TABLE ||
                ' p where p.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除待办任务数据
      exeSQL := 'delete from ' ||
                BPMS_RU_TODO_TASK_TABLE ||
                ' t where t.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除变量数据
      exeSQL := 'delete from ' ||
                BPMS_RU_VAR_TABLE || ' v where v.main_process_ins_id =''' ||
                mainProcessInsId || '''';
      execute immediate exeSQL;

      --间接依赖实例数据表


      --删除活动实例数据
      exeSQL := ' delete from ' || BPMS_RU_ACTIVE_INS_TABLE ||
                ' a where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where a.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL; --执行删除活动实例表数据,



      --删除节点轨迹表数据
      exeSQL := 'delete from ' ||
                BPMS_RU_NODE_ORBIT_TABLE ||
                ' o where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where o.cur_node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --删除办理跟踪明细数据
      exeSQL := 'delete from ' ||
                BPMS_RU_TRANS_TRACK_TABLE ||
                ' r where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where r.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''' and r.PROCESS_ID =''' || processId || ''')';
      execute immediate exeSQL;

      --删除节点实例数据
      exeSQL := 'delete from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where n.main_process_ins_id = ''' || mainProcessInsId || '''';
      execute immediate exeSQL;

    end if;
  end if;
  --commit;
END PRO_RU_Remove_trh;
/

--【补丁】2016-01-19_16-00_BPMS_V1.4.88_无_网省协作撤回_翟羽佳
begin 
P_SOA_REG_SERVICE('userTaskServiceSoaDelegate','','app_service','','soa-service-register_20140928114542','com.comtop.bpms.delegate.soa.UserTaskServiceSoaDelegate');
P_SOA_REG_Method('userTaskServiceSoaDelegate.undoColWorkFlow','','undoColWorkFlow','userTaskServiceSoaDelegate','0','0','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_I0','userTaskServiceSoaDelegate.undoColWorkFlow','','1','0','com.comtop.bpms.common.model.WorkFlowParamVO','','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_I1','userTaskServiceSoaDelegate.undoColWorkFlow','','1','1','com.comtop.bpms.common.model.BpmsMap[]','','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_E0','userTaskServiceSoaDelegate.undoColWorkFlow','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
end; 
/ 

insert into bpms_def_att(ATT_ID,ATT_KEY,Att_Name,att_type,Default_Value,Att_Scope)
values ('BPMS_SYSTEM_ATT_ISCOLUNDO','isColUndo','是否协作撤回','Boolean','true','SYSTEM');

commit;

--【补丁】2016-01-29_16-00_BPMS_V1.4.91_协作撤回增加返回值_翟羽佳
begin 
P_SOA_REG_SERVICE('userTaskServiceSoaDelegate','','app_service','','soa-service-register_20140928114542','com.comtop.bpms.delegate.soa.UserTaskServiceSoaDelegate');
P_SOA_REG_Method('userTaskServiceSoaDelegate.undoColWorkFlow','','undoColWorkFlow','userTaskServiceSoaDelegate','0','0','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_I0','userTaskServiceSoaDelegate.undoColWorkFlow','','1','0','com.comtop.bpms.common.model.WorkFlowParamVO','','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_I1','userTaskServiceSoaDelegate.undoColWorkFlow','','1','1','com.comtop.bpms.common.model.BpmsMap[]','','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_O','userTaskServiceSoaDelegate.undoColWorkFlow','','2','0','com.comtop.bpms.common.model.ProcessInstanceInfo','','');
p_soa_reg_Params('userTaskServiceSoaDelegate.undoColWorkFlow_E0','userTaskServiceSoaDelegate.undoColWorkFlow','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
end; 
/ 


commit;

--【补丁】2016-02-01_16-30_BPMS_V1.4.91_无_增加防止重复提交表字段长度_彭建华
ALTER TABLE BPMS_PREVENT_REPEAT  
MODIFY (PREVENT_REPEAT_ID VARCHAR2(100 BYTE) );
COMMIT;
/

--【补丁】2016-1-4_16-00_BPMS_V1.4.84_无_IT监控流程启动实时指标_翟羽佳
CREATE OR REPLACE PROCEDURE PRO_BPMS_COUNT_OVER_TASK1(
statsZeroTime in varchar2,
statsLastTime in varchar2,
todo_task_count out number)
AUTHID CURRENT_USER is

  /******************************************************************************
     NAME:       PRO_BPMS_COUNT_OVER_TASK1
     PURPOSE:    bpms工作流统计某时间段的超时代办数量
     statsZeroTime 开始时间
     statsLastTime 结束时间

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-11-12    翟羽佳       Created this procedure.
  ******************************************************************************/
 --1、先统计分表
    taskCount number :=0;
    spliterCode varchar2(100);
    tasktable varchar2(100);
    varSql varchar2(1000);
    searchCursor SYS_REFCURSOR;
    mainCount number :=0;
    startTime Timestamp;
    endTime Timestamp;
    existCount number :=0;
    searchCount number:=0;
 cursor spliterCursor is
    select distinct(suffix_flag) from bpms_cfg_redef_table;
 BEGIN
  startTime:=to_date(statsZeroTime,'yyyy-mm-dd hh24:mi:ss');
  endTime:=to_date(statsLastTime,'yyyy-mm-dd hh24:mi:ss');
  FOR spliter in spliterCursor LOOP
     spliterCode:= spliter.suffix_flag;
     tasktable:='BPMS_RU_TODO_TASK_'||spliterCode;
     --判断分表是否存在
   select count(*) into existCount from user_tables w where w.TABLE_NAME =upper(tasktable);
   if existCount>0 then
      --查代办
     varSql :='select count(*) from '||tasktable||' where expiration<='''||endTime||''' and
     expiration>='''||startTime||'''';
     open searchCursor for varSql;
      loop
        FETCH searchCursor INTO searchCount;
        exit when searchCursor%notfound;
      end loop;
      close searchCursor;
      taskCount :=taskCount+searchCount;
   end if;
  end loop;
  --2、统计主表
  varSql :='select count(*) from BPMS_RU_TODO_TASK where expiration<='''||endTime||''' and
     expiration>='''||startTime||'''';
  open searchCursor for varSql;
  loop
        FETCH searchCursor INTO mainCount;

  exit when searchCursor%notfound;
  end loop;
   close searchCursor;
  taskCount:= taskCount+mainCount;
  todo_task_count:=taskCount;
END PRO_BPMS_COUNT_OVER_TASK1;
/
CREATE OR REPLACE PROCEDURE PRO_BPMS_COUNT_RUN_INS1(
statsZeroTime in varchar2,
statsLastTime in varchar2,
all_ins_count out number)
AUTHID CURRENT_USER is

  /******************************************************************************
     NAME:       PRO_BPMS_COUNT_RUN_INS1
     PURPOSE:    BPMS统计运行中的主流程实例数量
     statsZeroTime 开始时间
     statsLastTime 结束时间

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-11-12    翟羽佳       Created this procedure.
  ******************************************************************************/
 --1、先统计分表
    insCount number :=0;
    spliterCode varchar2(100);
    tableName varchar2(100);
    varSql varchar2(1000);
    searchCursor SYS_REFCURSOR;
    mainCount number :=0;
    startTime Timestamp;
    endTime Timestamp;
    existCount number :=0;
    searchCount number:=0;
 cursor spliterCursor is
    select distinct(suffix_flag) from bpms_cfg_redef_table;
 BEGIN
  startTime:=to_date(statsZeroTime,'yyyy-mm-dd hh24:mi:ss');
  endTime:=to_date(statsLastTime,'yyyy-mm-dd hh24:mi:ss');
  FOR spliter in spliterCursor LOOP
     spliterCode:= spliter.suffix_flag;
     tableName:='BPMS_RU_PROCESS_INS_'||spliterCode;
     --判断分表是否存在
   select count(*) into existCount from user_tables w where w.TABLE_NAME =upper(tableName);
   if existCount>0 then
      --查代办
     varSql :='select count(*) from '||tableName||' where type=1 and state = 1
     and create_time<='''||endTime||''' and
     create_time>='''||startTime||'''';
     open searchCursor for varSql;
      loop
        FETCH searchCursor INTO searchCount;
        exit when searchCursor%notfound;
      end loop;
      close searchCursor;
      insCount :=insCount+searchCount;
   end if;
  end loop;
  --2、统计主表
  varSql :='select count(*) from BPMS_RU_PROCESS_INS where
  type=1 and state = 1 and create_time<='''||endTime||''' and
     create_time>='''||startTime||'''';
  open searchCursor for varSql;
  loop
        FETCH searchCursor INTO mainCount;

  exit when searchCursor%notfound;
  end loop;
   close searchCursor;
  insCount:= insCount+mainCount;
  all_ins_count:=insCount;
END PRO_BPMS_COUNT_RUN_INS1;
/
CREATE OR REPLACE PROCEDURE PRO_BPMS_COUNT_TASK1(
statsZeroTime in varchar2,
statsLastTime in varchar2,
todo_task_count out number)
AUTHID CURRENT_USER is

  /******************************************************************************
     NAME:       PRO_BPMS_COUNT_TASK1
     PURPOSE:    bpms工作流统计某时间段的代办数量
     statsZeroTime 开始时间
     statsLastTime 结束时间

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-11-12    翟羽佳       Created this procedure.
  ******************************************************************************/
 --1、先统计分表
    taskCount number :=0;
    spliterCode varchar2(100);
    tasktable varchar2(100);
    varSql varchar2(1000);
    searchCursor SYS_REFCURSOR;
    mainCount number :=0;
    startTime Timestamp;
    endTime Timestamp;
    existCount number :=0;
    searchCount number:=0;
 cursor spliterCursor is
    select distinct(suffix_flag) from bpms_cfg_redef_table;
 BEGIN
  startTime:=to_date(statsZeroTime,'yyyy-mm-dd hh24:mi:ss');
  endTime:=to_date(statsLastTime,'yyyy-mm-dd hh24:mi:ss');
  FOR spliter in spliterCursor LOOP
     spliterCode:= spliter.suffix_flag;
     tasktable:='BPMS_RU_TODO_TASK_'||spliterCode;
     --判断分表是否存在
   select count(*) into existCount from user_tables w where w.TABLE_NAME =upper(tasktable);
   if existCount>0 then
      --查代办
     varSql :='select count(*) from '||tasktable||' where create_time<='''||endTime||''' and
     create_time>='''||startTime||'''';
     open searchCursor for varSql;
      loop
        FETCH searchCursor INTO searchCount;
        exit when searchCursor%notfound;
      end loop;
      close searchCursor;
      taskCount :=taskCount+searchCount;
   end if;
  end loop;
  --2、统计主表
  varSql :='select count(*) from BPMS_RU_TODO_TASK where create_time<='''||endTime||''' and
     create_time>='''||startTime||'''';
  open searchCursor for varSql;
  loop
        FETCH searchCursor INTO mainCount;

  exit when searchCursor%notfound;
  end loop;
   close searchCursor;
  taskCount:= taskCount+mainCount;
  todo_task_count:=taskCount;
END PRO_BPMS_COUNT_TASK1;
/
CREATE OR REPLACE PROCEDURE PRO_BPMS_COUNT_TODAY_INS_BYDIR(
statsZeroTime in varchar2,
statsLastTime in varchar2,
dirCode in varchar2,
all_ins_count out number)
AUTHID CURRENT_USER is

  /******************************************************************************
     NAME:       PRO_BPMS_COUNT_TODAY_INS_BYDIR
     PURPOSE:    bpms工作流根据目录结构获取当天的流程启动数
     statsZeroTime 开始时间
     statsLastTime 结束时间

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-11-12    翟羽佳       Created this procedure.
  ******************************************************************************/
 --1、先统计分表
    insCount number :=0;
    spliterCode varchar2(100);
    tableName varchar2(100);
    varSql varchar2(5000);
    searchCursor SYS_REFCURSOR;
    mainCount number :=0;
    startTime Timestamp;
    endTime Timestamp;
    existCount number :=0;
    searchCount number:=0;
 cursor spliterCursor is
    select distinct(suffix_flag) from bpms_cfg_redef_table;
 BEGIN
  startTime:=to_date(statsZeroTime,'yyyy-mm-dd hh24:mi:ss');
  endTime:=to_date(statsLastTime,'yyyy-mm-dd hh24:mi:ss');

  FOR spliter in spliterCursor LOOP
     spliterCode:= spliter.suffix_flag;
     tableName:='BPMS_RU_PROCESS_INS_'||spliterCode;
     --判断分表是否存在
   select count(*) into existCount from user_tables w where w.TABLE_NAME =upper(tableName);
   if existCount>0 then
      --查代办
     varSql :='select count(*) from '||tableName||' pi, (select p.process_id from
     bpms_def_process p where exists (select 1 from bpms_def_deploye dep where dep.deploy_dir_code in ('||
      dirCode||') and dep.state = 1 and p.deploye_id = dep.deploye_id) group by p.process_id) t where
      t.process_id = pi.process_id and pi.type = 1 and  pi.create_time<='''||endTime||''' and
     pi.create_time>='''||startTime||'''';
     open searchCursor for varSql;
      loop
        FETCH searchCursor INTO searchCount;
        exit when searchCursor%notfound;
      end loop;
      close searchCursor;
      insCount :=insCount+searchCount;
   end if;
  end loop;
  --2、统计主表
   varSql :='select count(*) from BPMS_RU_PROCESS_INS pi, (select p.process_id from
     bpms_def_process p where exists (select 1 from bpms_def_deploye dep where dep.deploy_dir_code in ('||
      dirCode||') and dep.state = 1 and p.deploye_id = dep.deploye_id) group by p.process_id) t where
      t.process_id = pi.process_id and pi.type = 1 and  pi.create_time<='''||endTime||''' and
     pi.create_time>='''||startTime||'''';
  open searchCursor for varSql;
  loop
        FETCH searchCursor INTO mainCount;

  exit when searchCursor%notfound;
  end loop;
   close searchCursor;
  insCount:= insCount+mainCount;
  all_ins_count:=insCount;
END PRO_BPMS_COUNT_TODAY_INS_BYDIR;
/


--【补丁】2015-10-10_23-00_SOA_V2.1.27_无_新增保存连通性测试的请求报文、报文关键字查询南网服务等_欧阳辉
-- Add/modify columns 
alter table SOA_WEBSERVICE add WS_REQ_MSG CLOB;
-- Add comments to the columns 
comment on column SOA_WEBSERVICE.WS_REQ_MSG
  is '请求报文，用于服务测试时的请求报文';
  
insert into soa_buss_system (SYS_CODE, SYS_NAME, TCPIP_URL, HTTP_URL, SUPPER_PROTOCOL, TBI_URL, USER_NAME, USER_PASSWORD)
values ('tbi_sys_common', 'tbi-client服务', '', '', 2, '', '', '');

--【补丁】2015-12-17_15-00_SOA_V2.1.28_无_报文异常格式处理等_欧阳辉
--1、字段改为备份字段
ALTER TABLE SOA_CALL_LOG RENAME COLUMN INPUT_ARGS TO INPUT_ARGS_BAK;
--2、新增原字段，字段类型修改为clob
ALTER TABLE SOA_CALL_LOG ADD INPUT_ARGS CLOB;
COMMENT ON COLUMN SOA_CALL_LOG.INPUT_ARGS IS '服务方法入参信息';
--3、迁移备份字段的数据到新字段
UPDATE SOA_CALL_LOG L SET L.INPUT_ARGS=(SELECT N.INPUT_ARGS_BAK FROM SOA_CALL_LOG N WHERE L.LOG_ID=N.LOG_ID AND N.INPUT_ARGS_BAK IS NOT NULL);
COMMIT;
--4、删除备份字段
declare  cnt number;cnt2 number;
begin 
   select count(0)into cnt from SOA_CALL_LOG where INPUT_ARGS IS NOT NULL;
   select count(0)into cnt2 from SOA_CALL_LOG where INPUT_ARGS_BAK IS NOT NULL;
   ---如果数量一致，则可以删除备份字段
   if cnt=cnt2 then
      execute immediate 'ALTER TABLE SOA_CALL_LOG DROP COLUMN INPUT_ARGS_BAK' ;
  end if;
end;
/

--【补丁】2016-01-12_18-00_TOP_V5.1.187_无_修改登录失败时记录日志方式_杜波
DELETE FROM TOP_LOG_USER_LOGIN_DETAIL DETAIL
 WHERE DETAIL.OPERATE_ID IN (SELECT OPERATE_ID
                               FROM TOP_LOG_USER_OPERATE T
                              WHERE T.USER_ID IS NULL);
DELETE FROM TOP_LOG_USER_OPERATE OPERATE WHERE OPERATE.USER_ID IS NULL;
COMMIT;


-- Create table
create table TOP_LOGIN_FAIL
(
  fail_id      VARCHAR2(32) not null,
  user_account VARCHAR2(100),
  remote_addr  VARCHAR2(100),
  remote_host  VARCHAR2(30),
  operate_time DATE,
  env_system   VARCHAR2(64),
  env_browser  VARCHAR2(64),
  fail_reason  VARCHAR2(2000)
);
-- Add comments to the table 
comment on table TOP_LOGIN_FAIL
  is '用户登录失败日志表';
-- Add comments to the columns 
comment on column TOP_LOGIN_FAIL.fail_id
  is '主键，UUID生成的主键';
comment on column TOP_LOGIN_FAIL.user_account
  is '用户账号';
comment on column TOP_LOGIN_FAIL.remote_addr
  is '用户使用的IP 地址';
comment on column TOP_LOGIN_FAIL.remote_host
  is '用户使用主机名';
comment on column TOP_LOGIN_FAIL.operate_time
  is '操作时间，精确到毫秒';
comment on column TOP_LOGIN_FAIL.env_system
  is '操作系统名';
comment on column TOP_LOGIN_FAIL.env_browser
  is '浏览器';
comment on column TOP_LOGIN_FAIL.fail_reason
  is '失败原因';


--【补丁】2016-01-19_18-00_TOP_V5.1.188_无_新增深圳局EIP取资产系统待办信息功能_杜波
begin 
 	 P_SOA_REG_WS('WebServiceTodoCount','QueryTodoCountByAccount','1','workbenchTodoFacade.queryTodoCountByAccount','','http://soa.csg.cn','','','','com.comtop.top.workbench.todo.adapter.WebServiceTodoCount','');
end; 
/ 
commit;

--【补丁】2016-01-31_18-00_TOP_V5.1.195_无_修改IT监控定时器条件_杜波
DELETE FROM TOP_COMPONENT_QUARTZ WHERE JOB_NAME = 'quartzGatherDayITMonitorDataFacade';

DELETE FROM TOP_COMPONENT_QUARTZ WHERE JOB_NAME = 'quartzGatherRealTimeITMonitorDataFacade';

UPDATE TOP_COMPONENT_QUARTZ SET JOB_STATE = 2 WHERE JOB_NAME = 'quartzUnionMonitorTaskFacade';

insert into top_component_quartz (JOB_ID, JOB_NAME, JOB_DESCRIBE, JOB_TRIGGER_NAME, JOB_CRON_EL, JOB_SYSTEM_ID, JOB_SYSTEM_NAME, JOB_NODE, JOB_STATE, JOB_DATA, UPDATE_TIME)
values ('12FD5C854A7A4C9D94A8ACB6105472DF', 'quartzGatherDayITMonitorDataFacade', 'IT集中监控日指标采集定时器', 'quartzGatherDayITMonitorDataFacadeTrigger', '0 0 0 * * ?', null, null, 1, 1, null, null);

insert into top_component_quartz (JOB_ID, JOB_NAME, JOB_DESCRIBE, JOB_TRIGGER_NAME, JOB_CRON_EL, JOB_SYSTEM_ID, JOB_SYSTEM_NAME, JOB_NODE, JOB_STATE, JOB_DATA, UPDATE_TIME)
values ('641CC18BE48F4928A3F1810A950E8921', 'quartzGatherRealTimeITMonitorDataFacade', 'IT集中监控实时指标采集定时器', 'quartzGatherRealTimeITMonitorDataFacadeTrigger', '0 10,25,40,55 * * * ?', null, null, 1, 1, null, null);

COMMIT;

-----------------------------------------------------  end  -----------------------------------------------------------------------------------

----------------------------------------------------- 整理人：杨赛-----------------------------------------------------------------------------
----------------------------------------------------- 执行人：杨赛-----------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-04-02 ------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------
--【补丁】2016-03-14_12-00_TOP_V5.1.203_无_调整首页CSS文件加载顺序及表字段大小_杜波.sql
alter table top_version_update_log modify(user_account varchar(100));

UPDATE WORKBENCH_PLATFORM_PORTLET a SET a.self_name = '系统资料' WHERE a.portlet_id IN (
       SELECT PORTLET_ID FROM Workbench_Portlet WHERE portlet_url LIKE '%/top/sys/tools/file/FileDeskView.jsp%' );
	   
--【补丁】2016-03-16_11-30_TOP_V5.1.204_无_系统插件功能改名为系统资料_石刚.sql
UPDATE Workbench_Portlet a SET a.portlet_name = '系统资料', a.portlet_describe = '系统资料' 
WHERE a.portlet_url LIKE '%/top/sys/tools/file/FileDeskView.jsp%';
COMMIT;

--【补丁】2016-03-22_11-30_TOP_V5.1.205_无_系统资料微件改名为资料及控件下载_石刚.sql
UPDATE WORKBENCH_PLATFORM_PORTLET a SET a.self_name = '资料及控件下载' WHERE a.portlet_id IN (
       SELECT PORTLET_ID FROM Workbench_Portlet WHERE portlet_url LIKE '%/top/sys/tools/file/FileDeskView.jsp%' );

UPDATE Workbench_Portlet a SET a.portlet_name = '资料及控件下载', a.portlet_describe = '资料及控件下载' 
WHERE a.portlet_url LIKE '%/top/sys/tools/file/FileDeskView.jsp%';
COMMIT;

--系统账号表字段长度更新
alter table TOP_VERSION_UPDATE_LOG modify user_account VARCHAR2(100);


--【补丁】2016-03-29_11-40_TOP_V5.1.207_无_基础平台提供权限标签功能增量_石刚.sql
UPDATE top_per_func a SET a.func_code = 'SYS_FOURA_LOG' WHERE a.func_url LIKE '%/log/FouraMessageContentList.jsp%';
commit;


DELETE FROM TOP_CFG_ATTRIBUTE_CONFIG  WHERE ATTRIBUTE_ID IN (SELECT A.ATTRIBUTE_ID FROM TOP_CFG_ATTRIBUTE A WHERE A.CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.access.verifySwitch'));

DELETE FROM TOP_CFG_ATTRIBUTE WHERE CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.access.verifySwitch');

DELETE FROM TOP_CFG_CONFIG  WHERE CONFIG_FULLCODE = 'ct.top.access.verifySwitch';

INSERT INTO TOP_CFG_CONFIG(CONFIG_ID,CONFIG_CLASSIFY_ID,CONFIG_FULLCODE,CONFIG_NAME,CONFIG_TYPE,CONFIG_DESCRIPTION,IS_VALID,SORT_NO,CLASSIFY_TYPE, CREATE_TIME,UPDATE_TIME,CREATOR_ID,MODIFIER_ID)VALUES(sys_guid(),(SELECT TC.CLASSIFY_ID FROM TOP_CLASSIFY TC WHERE TC.CLASSIFY_CODE = 'ct.top.system'),'ct.top.access.verifySwitch','权限标签验证开关','0','权限标签是否开启权限验证，off 关闭 on 开启，默认关闭',1,0,'UNI_CLASSIFY',SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE(ATTRIBUTE_ID,CONFIG_ID,ATTRIBUTE_CODE,ATTRIBUTE_NAME,CONFIG_TYPE,SORT_NO,CREATE_TIME,UPDATE_TIME,CREATOR_ID, MODIFIER_ID)VALUES(sys_guid(),(SELECT CONFIG_ID FROM TOP_CFG_CONFIG ITEM WHERE ITEM.CONFIG_FULLCODE='ct.top.access.verifySwitch'),'','','0',0,SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE_CONFIG(ATTRIBUTE_CONFIG_ID,ATTRIBUTE_ID,IS_DEFALUT_VALUE,ATTRIBUTE_VALUE,SORT_NO,CREATE_TIME,CREATOR_ID,UPDATE_TIME,MODIFIER_ID)VALUES(sys_guid(),(SELECT a.attribute_id FROM top_cfg_config c,top_cfg_attribute a WHERE a.config_id =c.config_id AND c.config_fullcode = 'ct.top.access.verifySwitch'),1,'off',0,SYSDATE,'SuperAdmin',SYSDATE,'SuperAdmin');

COMMIT;


--【补丁】2016-03-9_14-20_BPMS_V1.4.94_无_上报后撤回不能再上报问题_李欢.sql
create or replace 
PROCEDURE PRO_RU_REMOVE_TRH(processId        varchar2,
                                              mainProcessInsId varchar2) AUTHID CURRENT_USER IS

  /******************************************************************************
     NAME:       PRO_RU_Remove_trh
     PURPOSE:  (根据流程ID、流程实例ID进行数据移植)数据删除/移植思路，将运行时的流程实例数据移植到回收数据区中

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2012-11-15          1. Created this procedure.

     NOTES:

     Automatically available Auto Replace Keywords:
        Object Name:     PRO_RU_Remove_trh 流程实例数据删除
        Sysdate:         2012-11-15
        Date and Time:   2012-11-15, 17:57:40, and 2012-11-15 17:57:40
        Username:         (set in TOAD Options, Procedure Editor)
        Table Name:       (set in the "New PL/SQL Object" dialog)

  ******************************************************************************/
  process_redef_table_name varchar2(10); --记录流程定义所对应的分表后缀名
  redef_table_num          number(1); ---是否存在分表
  exeSQL                   varchar2(3000); --要执行SQL语句

  BPMS_RU_CC_TASK_TABLE     VARCHAR2(100); --抄送任务表名
  BPMS_RU_DONE_TASK_TABLE   VARCHAR2(100); --已办表名
  BPMS_RU_NODE_TRACK_TABLE  VARCHAR2(100); --节点跟踪表名
  BPMS_RU_PROCESS_INS_TABLE VARCHAR2(100); --流程实例表名
  BPMS_RU_TODO_TASK_TABLE   VARCHAR2(100); --待办表名
  BPMS_RU_VAR_TABLE         VARCHAR2(100); --变量表名

  BPMS_RU_ACTIVE_INS_TABLE  VARCHAR2(100); --活动实例表名
  BPMS_RU_NODE_ORBIT_TABLE  VARCHAR2(100); --节点轨迹表名
  BPMS_RU_TRANS_TRACK_TABLE VARCHAR2(100); --处理跟踪表名


  BPMS_RU_NODE_INS_TABLE VARCHAR2(100); --节点实例表名


  BPMS_RU_ATTACHMENT_TABLE VARCHAR2(100); --审批附件基本信息表名
  BPMS_RU_REVOKE_LOG_TABLE VARCHAR2(100); --流程撤回日志记录表名
  BPMS_RU_TODO_TASK_ALL_TABLE VARCHAR2(100); --待办任务总表表名
  BPMS_RU_COMPEVENT_INS_TB VARCHAR2(100); --补偿事件实例表名
  BPMS_RU_COMPEVENT_SUS_TB VARCHAR2(100); --补偿事件订阅表名
  BPMS_RU_MESEVENT_SUS_TABLE VARCHAR2(100); --消息事件订阅表名
  BPMS_RU_MESEVENT_TRI_TABLE VARCHAR2(100); --消息事件触发表表名
  BPMS_RU_SIGEVENT_TRI_TABLE VARCHAR2(100); --信号事件实例表表名
  BPMS_RU_SIGNEVENT_SUS_TABLE VARCHAR2(100); --信号事件订阅表表名

BEGIN

  if processId is not null then
    if mainProcessInsId is not null then

      select count(1) into redef_table_num  from BPMS_CFG_REDEF_TABLE t1 where t1.PROCESS_ID = '' || processId || '';

      --step1:分析流程分表信息，准备源数据表名
      if redef_table_num > 0 then
        --当用户有对此流程定义分表时，加上分表后缀
        select t2.SUFFIX_FLAG into process_redef_table_name from BPMS_CFG_REDEF_TABLE t2 where t2.PROCESS_ID = '' || processId || '';

        BPMS_RU_CC_TASK_TABLE     := 'BPMS_RU_CC_TASK_' || process_redef_table_name;
        BPMS_RU_DONE_TASK_TABLE   := 'BPMS_RU_DONE_TASK_' || process_redef_table_name;
        BPMS_RU_NODE_TRACK_TABLE  := 'BPMS_RU_NODE_TRACK_' || process_redef_table_name;
        BPMS_RU_PROCESS_INS_TABLE := 'BPMS_RU_PROCESS_INS_' || process_redef_table_name;
        BPMS_RU_TODO_TASK_TABLE   := 'BPMS_RU_TODO_TASK_' || process_redef_table_name;
        BPMS_RU_VAR_TABLE         := 'BPMS_RU_VAR_' || process_redef_table_name;
        BPMS_RU_ACTIVE_INS_TABLE  := 'BPMS_RU_ACTIVE_INS_' || process_redef_table_name;
        BPMS_RU_NODE_ORBIT_TABLE  := 'BPMS_RU_NODE_ORBIT_' || process_redef_table_name;
        BPMS_RU_TRANS_TRACK_TABLE := 'BPMS_RU_TRANS_TRACK_' || process_redef_table_name;
        BPMS_RU_NODE_INS_TABLE    := 'BPMS_RU_NODE_INS_' || process_redef_table_name;
        BPMS_RU_ATTACHMENT_TABLE  := 'BPMS_RU_ATTACHMENT_' || process_redef_table_name;
        BPMS_RU_REVOKE_LOG_TABLE  := 'BPMS_RU_REVOKE_LOG_' || process_redef_table_name;
        BPMS_RU_TODO_TASK_ALL_TABLE:= 'BPMS_RU_TODO_TASK_ALL';
        BPMS_RU_COMPEVENT_INS_TB:='BPMS_RU_COMPEVENT_INS_'|| process_redef_table_name;
        BPMS_RU_COMPEVENT_SUS_TB:='BPMS_RU_COMPEVENT_SUS_'|| process_redef_table_name;
        BPMS_RU_MESEVENT_SUS_TABLE :='BPMS_RU_MESEVENT_SUS_'|| process_redef_table_name;
        BPMS_RU_MESEVENT_TRI_TABLE :='BPMS_RU_MESEVENT_TRI_'|| process_redef_table_name;
        BPMS_RU_SIGEVENT_TRI_TABLE :='BPMS_RU_SIGEVENT_TRI_'|| process_redef_table_name;
        BPMS_RU_SIGNEVENT_SUS_TABLE :='BPMS_RU_SIGNEVENT_SUS_'|| process_redef_table_name;
      else
        --不加后缀，直接查原表
        BPMS_RU_CC_TASK_TABLE     := 'BPMS_RU_CC_TASK';
        BPMS_RU_DONE_TASK_TABLE   := 'BPMS_RU_DONE_TASK';
        BPMS_RU_NODE_TRACK_TABLE  := 'BPMS_RU_NODE_TRACK';
        BPMS_RU_PROCESS_INS_TABLE := 'BPMS_RU_PROCESS_INS';
        BPMS_RU_TODO_TASK_TABLE   := 'BPMS_RU_TODO_TASK';
        BPMS_RU_VAR_TABLE         := 'BPMS_RU_VAR';
        BPMS_RU_ACTIVE_INS_TABLE  := 'BPMS_RU_ACTIVE_INS';
        BPMS_RU_NODE_ORBIT_TABLE  := 'BPMS_RU_NODE_ORBIT';
        BPMS_RU_TRANS_TRACK_TABLE := 'BPMS_RU_TRANS_TRACK';
        BPMS_RU_NODE_INS_TABLE    := 'BPMS_RU_NODE_INS';
        BPMS_RU_ATTACHMENT_TABLE  := 'BPMS_RU_ATTACHMENT';
        BPMS_RU_REVOKE_LOG_TABLE  := 'BPMS_RU_REVOKE_LOG';
        BPMS_RU_TODO_TASK_ALL_TABLE:= 'BPMS_RU_TODO_TASK_ALL';
        BPMS_RU_COMPEVENT_INS_TB:='BPMS_RU_COMPEVENT_INS';
        BPMS_RU_COMPEVENT_SUS_TB:='BPMS_RU_COMPEVENT_SUS';
        BPMS_RU_MESEVENT_SUS_TABLE :='BPMS_RU_MESEVENT_SUS';
        BPMS_RU_MESEVENT_TRI_TABLE :='BPMS_RU_MESEVENT_TRI';
        BPMS_RU_SIGEVENT_TRI_TABLE :='BPMS_RU_SIGEVENT_TRI';
        BPMS_RU_SIGNEVENT_SUS_TABLE :='BPMS_RU_SIGNEVENT_SUS';
      end if;

      --step2:拼装并插入实例数据

      --直接依赖流程实例表

      --备份流程抄送数据
      exeSQL := ' insert into BPMS_RU_CC_TASK_TRH select * from ' ||
                BPMS_RU_CC_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
           --备份已办数据
      --exeSQL := ' insert into BPMS_RU_DONE_TASK_TRH select * from ' ||
      --          BPMS_RU_DONE_TASK_TABLE ||
      --          ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      --execute immediate exeSQL;
      pro_deletetask(BPMS_RU_DONE_TASK_TABLE,'BPMS_RU_DONE_TASK_TRH',mainProcessInsId);
      --备份节点跟踪数据
      exeSQL := ' insert into BPMS_RU_NODE_TRACK_TRH select * from ' ||
                BPMS_RU_NODE_TRACK_TABLE ||
                ' k where k.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份流程实例数据
      exeSQL := 'insert into BPMS_RU_PROCESS_INS_TRH select * from ' ||
                BPMS_RU_PROCESS_INS_TABLE ||
                ' p where p.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份待办任务数据
     -- exeSQL := 'insert into BPMS_RU_TODO_TASK_TRH select * from ' ||
     --           BPMS_RU_TODO_TASK_TABLE ||
     --           ' t where t.main_process_ins_id =''' || mainProcessInsId || '''';
      --execute immediate exeSQL;
      pro_deletetask(BPMS_RU_TODO_TASK_TABLE,'BPMS_RU_TODO_TASK_TRH',mainProcessInsId);
      --备份变量数据
      exeSQL := 'insert into BPMS_RU_VAR_TRH select * from ' ||
                BPMS_RU_VAR_TABLE || ' v where v.main_process_ins_id =''' ||
                mainProcessInsId || '''';
      execute immediate exeSQL;


      --间接依赖实例数据表
      exeSQL := 'insert into BPMS_RU_ACTIVE_INS_TRH select * from ' ||
                BPMS_RU_ACTIVE_INS_TABLE ||
                ' a where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where a.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --备份节点轨迹表数据
      exeSQL := 'insert into BPMS_RU_NODE_ORBIT_TRH select * from ' ||
                BPMS_RU_NODE_ORBIT_TABLE ||
                ' o where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where o.cur_node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --备份跟踪明细数据
      exeSQL := 'insert into BPMS_RU_TRANS_TRACK_TRH(TRANS_TRACK_ID, NODE_INS_ID, ACTIVE_INS_ID, NODE_TRACK_ID, WORK_ID, TRANS_FLAG, ACTOR_ID,
                ACTOR_NAME, CREATE_TIME, HANDLE_TIME, OVER_TIME, MSG, MSG_TYPE, RESULT_FLAG, BACK_FLAG,
                CONDITION, CONDITION_VALUE, CONDITION_RESULT, RESSIGNM_PERSON_ID, RESSIGNM_PERSON_NAME,
                COMPLETE_TYPE, REVOKE_FLAG, AUTHORIZER_ID, AUTHORIZER_NAME, PRE_TRANS_TRACK_ID,
                PROCESS_ID, MAIN_PROCESS_INS_ID,MODIFY_DATE) select 
                TRANS_TRACK_ID, NODE_INS_ID, ACTIVE_INS_ID, NODE_TRACK_ID, WORK_ID, TRANS_FLAG, ACTOR_ID,
                ACTOR_NAME, CREATE_TIME, HANDLE_TIME, OVER_TIME, MSG, MSG_TYPE, RESULT_FLAG, BACK_FLAG,
                CONDITION, CONDITION_VALUE, CONDITION_RESULT, RESSIGNM_PERSON_ID, RESSIGNM_PERSON_NAME,
                COMPLETE_TYPE, REVOKE_FLAG, AUTHORIZER_ID, AUTHORIZER_NAME, PRE_TRANS_TRACK_ID,
                PROCESS_ID, MAIN_PROCESS_INS_ID,MODIFY_DATE from ' ||
                BPMS_RU_TRANS_TRACK_TABLE ||
                ' r where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where r.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --备份节点实例表数据
      exeSQL := 'insert into BPMS_RU_NODE_INS_TRH select * from ' ||
                BPMS_RU_NODE_INS_TABLE ||' n where n.main_process_ins_id = ''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份审批附件信息表数据
      exeSQL := 'insert into BPMS_RU_ATTACHMENT_TRH select * from ' ||
            BPMS_RU_ATTACHMENT_TABLE|| ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.act_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            '  c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --备份流程撤回日志记录表数据
      exeSQL := 'insert into BPMS_RU_REVOKE_LOG_TRH select * from ' ||
            BPMS_RU_REVOKE_LOG_TABLE|| ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            '  c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --备份待办任务总表数据
      exeSQL := 'insert into BPMS_RU_TODO_TASK_ALL_TRH select * from '||
            BPMS_RU_TODO_TASK_ALL_TABLE||' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            '  c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --备份:信号事件实例表数据
      exeSQL := 'insert into BPMS_RU_COMPEVENT_INS_TRH select * from '||
            BPMS_RU_COMPEVENT_INS_TB||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:补偿事件订阅表数据
      exeSQL := 'insert into BPMS_RU_COMPEVENT_SUS_TRH select * from '||
            BPMS_RU_COMPEVENT_SUS_TB||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:消息事件订阅表数据
      exeSQL := 'insert into BPMS_RU_MESEVENT_SUS_TRH select * from '||
            BPMS_RU_MESEVENT_SUS_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:消息事件触发表数据
      exeSQL := 'insert into BPMS_RU_MESEVENT_TRI_TRH select * from '||
            BPMS_RU_MESEVENT_TRI_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:信号事件实例表数据
      exeSQL := 'insert into BPMS_RU_SIGEVENT_TRI_TRH select * from '||
            BPMS_RU_SIGEVENT_TRI_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --备份:信号事件订阅表数据
      exeSQL := 'insert into BPMS_RU_SIGNEVENT_SUS_TRH select * from '||
            BPMS_RU_SIGNEVENT_SUS_TABLE||' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;



       --删除:删除防止重复提交的表
      exeSQL := 'delete from bpms_prevent_repeat a where a.prevent_repeat_id like '''||processId || '%''';
      execute immediate exeSQL;

      --step3: 拼装并删除实例数据

      --直接依赖流程实例表
      --删除:信号事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_SIGNEVENT_SUS_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除信号事件实例表数据
      exeSQL := 'delete from '||BPMS_RU_SIGEVENT_TRI_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除消息事件触发表数据
      exeSQL := 'delete from '||BPMS_RU_MESEVENT_TRI_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除消息事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_MESEVENT_SUS_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除补偿事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_COMPEVENT_SUS_TB||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除信号事件实例表数据
      exeSQL := 'delete from '|| BPMS_RU_COMPEVENT_INS_TB||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除待办任务总表数据
      exeSQL := 'delete from '||BPMS_RU_TODO_TASK_ALL_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --删除:流程撤回日志记录表数据
      exeSQL := 'delete from ' || BPMS_RU_REVOKE_LOG_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --删除：审批附件信息表数据
      exeSQL := 'delete from ' || BPMS_RU_ATTACHMENT_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.act_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;


      --删除抄送数据
      exeSQL := ' delete from ' ||
                BPMS_RU_CC_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除已办任务数据
      exeSQL := ' delete from ' ||
                BPMS_RU_DONE_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除节点跟踪数据
      exeSQL := ' delete from ' ||
                BPMS_RU_NODE_TRACK_TABLE ||
                ' k where k.main_process_ins_id =''' || mainProcessInsId || '''
                  and k.CUR_PROCESS_ID =''' || processId || '''';
      execute immediate exeSQL;
      --删除流程实例数据
      exeSQL := 'delete from ' ||
                BPMS_RU_PROCESS_INS_TABLE ||
                ' p where p.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除待办任务数据
      exeSQL := 'delete from ' ||
                BPMS_RU_TODO_TASK_TABLE ||
                ' t where t.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除变量数据
      exeSQL := 'delete from ' ||
                BPMS_RU_VAR_TABLE || ' v where v.main_process_ins_id =''' ||
                mainProcessInsId || '''';
      execute immediate exeSQL;

      --间接依赖实例数据表


      --删除活动实例数据
      exeSQL := ' delete from ' || BPMS_RU_ACTIVE_INS_TABLE ||
                ' a where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where a.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL; --执行删除活动实例表数据,



      --删除节点轨迹表数据
      exeSQL := 'delete from ' ||
                BPMS_RU_NODE_ORBIT_TABLE ||
                ' o where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where o.cur_node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --删除办理跟踪明细数据
      exeSQL := 'delete from ' ||
                BPMS_RU_TRANS_TRACK_TABLE ||
                ' r where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where r.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''' and r.PROCESS_ID =''' || processId || ''')';
      execute immediate exeSQL;

      --删除节点实例数据
      exeSQL := 'delete from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where n.main_process_ins_id = ''' || mainProcessInsId || '''';
      execute immediate exeSQL;

    end if;
  end if;
  --commit;
END PRO_RU_Remove_trh;
/



CREATE OR REPLACE PROCEDURE BPMS_PRO_INSTANCE_DELETE(processId        varchar2,
                                              mainProcessInsId varchar2) AUTHID CURRENT_USER IS

  /******************************************************************************
     NAME:       BPMS_PRO_INSTANCE_DELETE
     PURPOSE:  (根据流程ID、流程实例ID进行数据删除)

     REVISIONS:
     Ver        Date        Author           Description
     ---------  ----------  ---------------  ------------------------------------
     1.0        2015-02-02          1. Created this procedure.

     NOTES:

     Automatically available Auto Replace Keywords:
        Object Name:     BPMS_PRO_INSTANCE_DELETE 流程实例数据删除
        Sysdate:         2015-02-02
        Date and Time:   2015-02-02, 14:43:40, and 2015-02-02 14:43:40
        Username:         (set in TOAD Options, Procedure Editor)
        Table Name:       (set in the "New PL/SQL Object" dialog)

  ******************************************************************************/
  process_redef_table_name varchar2(10); --记录流程定义所对应的分表后缀名
  redef_table_num          number(1); ---是否存在分表
  exeSQL                   varchar2(1000); --要执行SQL语句

  BPMS_RU_CC_TASK_TABLE     VARCHAR2(100); --抄送任务表名
  BPMS_RU_DONE_TASK_TABLE   VARCHAR2(100); --已办表名
  BPMS_RU_NODE_TRACK_TABLE  VARCHAR2(100); --节点跟踪表名
  BPMS_RU_PROCESS_INS_TABLE VARCHAR2(100); --流程实例表名
  BPMS_RU_TODO_TASK_TABLE   VARCHAR2(100); --待办表名
  BPMS_RU_VAR_TABLE         VARCHAR2(100); --变量表名

  BPMS_RU_ACTIVE_INS_TABLE  VARCHAR2(100); --活动实例表名
  BPMS_RU_NODE_ORBIT_TABLE  VARCHAR2(100); --节点轨迹表名
  BPMS_RU_TRANS_TRACK_TABLE VARCHAR2(100); --处理跟踪表名
  BPMS_RU_NODE_INS_TABLE VARCHAR2(100); --节点实例表名
  BPMS_RU_ATTACHMENT_TABLE VARCHAR2(100); --审批附件基本信息表名
  BPMS_RU_REVOKE_LOG_TABLE VARCHAR2(100); --流程撤回日志记录表名
  BPMS_RU_TODO_TASK_ALL_TABLE VARCHAR2(100); --待办任务总表表名
  BPMS_RU_COMPEVENT_INS_TB VARCHAR2(100); --补偿事件实例表名
  BPMS_RU_COMPEVENT_SUS_TB VARCHAR2(100); --补偿事件订阅表名
  BPMS_RU_MESEVENT_SUS_TABLE VARCHAR2(100); --消息事件订阅表名
  BPMS_RU_MESEVENT_TRI_TABLE VARCHAR2(100); --消息事件触发表表名
  BPMS_RU_SIGEVENT_TRI_TABLE VARCHAR2(100); --信号事件实例表表名
  BPMS_RU_SIGNEVENT_SUS_TABLE VARCHAR2(100); --信号事件订阅表表名

BEGIN

  if processId is not null then
    if mainProcessInsId is not null then

      select count(1) into redef_table_num  from BPMS_CFG_REDEF_TABLE t1 where t1.PROCESS_ID = '' || processId || '';

      --step1:分析流程分表信息，准备源数据表名
      if redef_table_num > 0 then
        --当用户有对此流程定义分表时，加上分表后缀
        select t2.SUFFIX_FLAG into process_redef_table_name from BPMS_CFG_REDEF_TABLE t2 where t2.PROCESS_ID = '' || processId || '';

        BPMS_RU_CC_TASK_TABLE     := 'BPMS_RU_CC_TASK_' || process_redef_table_name;
        BPMS_RU_DONE_TASK_TABLE   := 'BPMS_RU_DONE_TASK_' || process_redef_table_name;
        BPMS_RU_NODE_TRACK_TABLE  := 'BPMS_RU_NODE_TRACK_' || process_redef_table_name;
        BPMS_RU_PROCESS_INS_TABLE := 'BPMS_RU_PROCESS_INS_' || process_redef_table_name;
        BPMS_RU_TODO_TASK_TABLE   := 'BPMS_RU_TODO_TASK_' || process_redef_table_name;
        BPMS_RU_VAR_TABLE         := 'BPMS_RU_VAR_' || process_redef_table_name;
        BPMS_RU_ACTIVE_INS_TABLE  := 'BPMS_RU_ACTIVE_INS_' || process_redef_table_name;
        BPMS_RU_NODE_ORBIT_TABLE  := 'BPMS_RU_NODE_ORBIT_' || process_redef_table_name;
        BPMS_RU_TRANS_TRACK_TABLE := 'BPMS_RU_TRANS_TRACK_' || process_redef_table_name;
        BPMS_RU_NODE_INS_TABLE    := 'BPMS_RU_NODE_INS_' || process_redef_table_name;
        BPMS_RU_ATTACHMENT_TABLE  := 'BPMS_RU_ATTACHMENT_' || process_redef_table_name;
        BPMS_RU_REVOKE_LOG_TABLE  := 'BPMS_RU_REVOKE_LOG_' || process_redef_table_name;
        BPMS_RU_TODO_TASK_ALL_TABLE:= 'BPMS_RU_TODO_TASK_ALL';
        BPMS_RU_COMPEVENT_INS_TB:='BPMS_RU_COMPEVENT_INS_'|| process_redef_table_name;
        BPMS_RU_COMPEVENT_SUS_TB:='BPMS_RU_COMPEVENT_SUS_'|| process_redef_table_name;
        BPMS_RU_MESEVENT_SUS_TABLE :='BPMS_RU_MESEVENT_SUS_'|| process_redef_table_name;
        BPMS_RU_MESEVENT_TRI_TABLE :='BPMS_RU_MESEVENT_TRI_'|| process_redef_table_name;
        BPMS_RU_SIGEVENT_TRI_TABLE :='BPMS_RU_SIGEVENT_TRI_'|| process_redef_table_name;
        BPMS_RU_SIGNEVENT_SUS_TABLE :='BPMS_RU_SIGNEVENT_SUS_'|| process_redef_table_name;
      else
        --不加后缀，直接查原表
        BPMS_RU_CC_TASK_TABLE     := 'BPMS_RU_CC_TASK';
        BPMS_RU_DONE_TASK_TABLE   := 'BPMS_RU_DONE_TASK';
        BPMS_RU_NODE_TRACK_TABLE  := 'BPMS_RU_NODE_TRACK';
        BPMS_RU_PROCESS_INS_TABLE := 'BPMS_RU_PROCESS_INS';
        BPMS_RU_TODO_TASK_TABLE   := 'BPMS_RU_TODO_TASK';
        BPMS_RU_VAR_TABLE         := 'BPMS_RU_VAR';
        BPMS_RU_ACTIVE_INS_TABLE  := 'BPMS_RU_ACTIVE_INS';
        BPMS_RU_NODE_ORBIT_TABLE  := 'BPMS_RU_NODE_ORBIT';
        BPMS_RU_TRANS_TRACK_TABLE := 'BPMS_RU_TRANS_TRACK';
        BPMS_RU_NODE_INS_TABLE    := 'BPMS_RU_NODE_INS';
        BPMS_RU_ATTACHMENT_TABLE  := 'BPMS_RU_ATTACHMENT';
        BPMS_RU_REVOKE_LOG_TABLE  := 'BPMS_RU_REVOKE_LOG';
        BPMS_RU_TODO_TASK_ALL_TABLE:= 'BPMS_RU_TODO_TASK_ALL';
        BPMS_RU_COMPEVENT_INS_TB:='BPMS_RU_COMPEVENT_INS';
        BPMS_RU_COMPEVENT_SUS_TB:='BPMS_RU_COMPEVENT_SUS';
        BPMS_RU_MESEVENT_SUS_TABLE :='BPMS_RU_MESEVENT_SUS';
        BPMS_RU_MESEVENT_TRI_TABLE :='BPMS_RU_MESEVENT_TRI';
        BPMS_RU_SIGEVENT_TRI_TABLE :='BPMS_RU_SIGEVENT_TRI';
        BPMS_RU_SIGNEVENT_SUS_TABLE :='BPMS_RU_SIGNEVENT_SUS';
      end if;

      --删除:删除防止重复提交的表
      exeSQL := 'delete from bpms_prevent_repeat a where a.prevent_repeat_id like '''||processId || '%''';
      execute immediate exeSQL;
      --step2: 拼装并删除实例数据

      --删除:信号事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_SIGNEVENT_SUS_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除信号事件实例表数据
      exeSQL := 'delete from '||BPMS_RU_SIGEVENT_TRI_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除消息事件触发表数据
      exeSQL := 'delete from '||BPMS_RU_MESEVENT_TRI_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除消息事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_MESEVENT_SUS_TABLE||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除补偿事件订阅表数据
      exeSQL := 'delete from '|| BPMS_RU_COMPEVENT_SUS_TB||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除信号事件实例表数据
      exeSQL := 'delete from '|| BPMS_RU_COMPEVENT_INS_TB||
            ' a where a.main_process_ins_id ='''||mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除待办任务总表数据
      exeSQL := 'delete from '||BPMS_RU_TODO_TASK_ALL_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --删除:流程撤回日志记录表数据
      exeSQL := 'delete from ' || BPMS_RU_REVOKE_LOG_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.activity_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;
      --删除：审批附件信息表数据
      exeSQL := 'delete from ' || BPMS_RU_ATTACHMENT_TABLE||
            ' a where exists(select 1 from '||BPMS_RU_ACTIVE_INS_TABLE||
            ' b where a.act_ins_id = b.activity_ins_id and exists( select 1 from '||BPMS_RU_NODE_INS_TABLE||
            ' c where b.node_ins_id = c.node_ins_id and c.main_process_ins_id ='''||mainProcessInsId || '''))';
      execute immediate exeSQL;

      --删除抄送数据
      exeSQL := ' delete from ' ||
                BPMS_RU_CC_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除已办任务数据
      exeSQL := ' delete from ' ||
                BPMS_RU_DONE_TASK_TABLE ||
                ' c where c.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除节点跟踪数据
      exeSQL := ' delete from ' ||
                BPMS_RU_NODE_TRACK_TABLE ||
                ' k where k.main_process_ins_id =''' || mainProcessInsId || ''' 
                  and k.CUR_PROCESS_ID =''' || processId || '''';
      execute immediate exeSQL;
      --删除流程实例数据
      exeSQL := 'delete from ' ||
                BPMS_RU_PROCESS_INS_TABLE ||
                ' p where p.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除待办任务数据
      exeSQL := 'delete from ' ||
                BPMS_RU_TODO_TASK_TABLE ||
                ' t where t.main_process_ins_id =''' || mainProcessInsId || '''';
      execute immediate exeSQL;
      --删除变量数据
      exeSQL := 'delete from ' ||
                BPMS_RU_VAR_TABLE || ' v where v.main_process_ins_id =''' ||
                mainProcessInsId || '''';
      execute immediate exeSQL;

      --删除活动实例数据
      exeSQL := ' delete from ' || BPMS_RU_ACTIVE_INS_TABLE ||
                ' a where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where a.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL; --执行删除活动实例表数据,

      --删除节点轨迹表数据
      exeSQL := 'delete from ' ||
                BPMS_RU_NODE_ORBIT_TABLE ||
                ' o where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where o.cur_node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''')';
      execute immediate exeSQL;
      --删除办理跟踪明细数据
      exeSQL := 'delete from ' ||
                BPMS_RU_TRANS_TRACK_TABLE ||
                ' r where exists (select 1  from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where r.node_ins_id = n.node_ins_id and n.main_process_ins_id =''' ||
                mainProcessInsId || ''' and r.PROCESS_ID =''' || processId || ''')';
      execute immediate exeSQL;

      --删除节点实例数据
      exeSQL := 'delete from ' ||
                BPMS_RU_NODE_INS_TABLE ||
                ' n where n.main_process_ins_id = ''' || mainProcessInsId || '''';
      execute immediate exeSQL;

    end if;
  end if;
END BPMS_PRO_INSTANCE_DELETE;
/


--【补丁】2016-03-16_16-30_BPMS_V1.4.95_无_将BPMS流程已办表按照生产流程进行分区_翟羽佳.sql
-- 备份正式表

create table BPMS_RU_DONE_TASK_bak0316 as select * from BPMS_RU_DONE_TASK;
drop table BPMS_RU_DONE_TASK;


create table BPMS_RU_DONE_TASK
(
  DONE_TASK_ID        VARCHAR2(40) not null,
  MAIN_PROCESS_ID     VARCHAR2(40),
  CUR_PROCESS_ID      VARCHAR2(40),
  CUR_NODE_ID         VARCHAR2(40),
  CUR_NODE_NAME       VARCHAR2(60),
  MAIN_PROCESS_INS_ID VARCHAR2(40),
  CUR_PROCESS_INS_ID  VARCHAR2(40),
  CUR_NODE_INS_ID     VARCHAR2(40),
  ACTIVITY_INS_ID     VARCHAR2(40),
  STATUS              NUMBER(2),
  TRANS_ACTOR_ID      VARCHAR2(40),
  ACTOR_NAME          VARCHAR2(40),
  READ_FLAG           NUMBER(1),
  BACK_FLAG           NUMBER(2),
  EXPIRATION          TIMESTAMP(6),
  URGE_TIME           TIMESTAMP(6),
  URGE_INTERVAL       NUMBER(20),
  URGE_FLAG           NUMBER(1),
  NOTE                VARCHAR2(4000),
  NOTE_TYPE           NUMBER(2),
  CREATE_TIME         TIMESTAMP(6),
  VERSION             NUMBER(20),
  TASK_COMPLETE_TYPE  NUMBER(2) default -1,
  REVOKE_BACK_FLAG    NUMBER(1) default 1,
  MODIFY_DATE         TIMESTAMP(6) 
)
PARTITION BY LIST(CUR_PROCESS_ID)  (
                                     PARTITION
                                     P1
                                     VALUES('Pro3970854554520'),
                                     PARTITION
                                     P2
                                     VALUES('ProdPlanYearWorkFlow'),
                                     PARTITION
                                     P3
                                     VALUES('ProdPlanMonthWorkFlow'),
                                     PARTITION
                                     P4
                                     VALUES('DisElecturnoverProcess'),
                                     PARTITION
                                     P5
                                     VALUES('oneAssetProcess'),
                                     PARTITION
                                     P6
                                     VALUES('electurnoverManage'),
                                     PARTITION
                                     P7
                                     VALUES('assetchangeManage'),
                                     PARTITION
                                     P8
                                     VALUES('disAssetchangeManage'),
                                     PARTITION
                                     P9
                                     VALUES('TaskFormProjectWorkFlow'),
                                     PARTITION
                                     P10
                                     VALUES('TaskFormBureauProjectWorkFlow'),
                                     PARTITION
                                     P11
                                     VALUES('TaskFormTemplateWorkFlow'),
                                     PARTITION
                                     P12
                                     VALUES('TaskFormBureauTemplateWorkFlow'),
                                     PARTITION
                                     P13
                                     VALUES('TaskFormTeamTemplateWorkFlow'),
                                     PARTITION
                                     P14
                                     VALUES('AntiAccident'),
                                     PARTITION
                                     P15
                                     VALUES('ANTIACCIDENT_SEC'),
                                     PARTITION
                                     P16
                                     VALUES('AntiAccidentSuggest'),
                                     PARTITION
                                     P17
                                     VALUES('ReliabilityStopEventReportWorkFlow'),
                                     PARTITION
                                     P18
                                     VALUES('ReliabilityStopEventInformalWorkFlow'),
                                     PARTITION
                                     P19
                                     VALUES('ReliabilityBreakerCutoff'),
                                     PARTITION
                                     P20
                                     VALUES('ReliabilityBreakerOperational'),
                                     PARTITION
                                     P21
                                     VALUES('PRODUCTION_INDEX_PROCESS_PUBLIC'),
                                     PARTITION
                                     P22
                                     VALUES('OutagePlanYearWorkflow?'),
                                     PARTITION
                                     P23
                                     VALUES('OutagePlanMonthWorkflow?'),
                                     PARTITION
                                     P24
                                     VALUES('OutageApplyCommWorkflow?'),
                                     PARTITION
                                     P25
                                     VALUES('DEFECT_PROCESS'),
                                     PARTITION
                                     P26
                                     VALUES(default)
                                   );
-- Add comments to the table 
comment on table BPMS_RU_DONE_TASK
  is '存储已处理的任务信息';
comment on column BPMS_RU_DONE_TASK.DONE_TASK_ID
  is '记录主键';
comment on column BPMS_RU_DONE_TASK.MAIN_PROCESS_ID
  is '主流程编号';
comment on column BPMS_RU_DONE_TASK.CUR_PROCESS_ID
  is '当前流程的唯一标识';
comment on column BPMS_RU_DONE_TASK.CUR_NODE_ID
  is '当前节点定义的唯一标识';
comment on column BPMS_RU_DONE_TASK.CUR_NODE_NAME
  is '当前节点名称';
comment on column BPMS_RU_DONE_TASK.MAIN_PROCESS_INS_ID
  is '主流程实例的唯一标识';
comment on column BPMS_RU_DONE_TASK.CUR_PROCESS_INS_ID
  is '当前流程实例的唯一标识';
comment on column BPMS_RU_DONE_TASK.CUR_NODE_INS_ID
  is '当前节点实例的唯一标识';
comment on column BPMS_RU_DONE_TASK.ACTIVITY_INS_ID
  is '活动实例的唯一标识';
comment on column BPMS_RU_DONE_TASK.STATUS
  is '状态0未办;1正常已办;2他办；4被挂起；5已终止';
comment on column BPMS_RU_DONE_TASK.TRANS_ACTOR_ID
  is '处理人员id，待办情况下，表示待处理人员；已办情况下表示处理了本条消息的人员id。多个人的id以半角分号分隔';
comment on column BPMS_RU_DONE_TASK.ACTOR_NAME
  is '处理人姓名';
comment on column BPMS_RU_DONE_TASK.READ_FLAG
  is '已读标识，0为未读，1为已读';
comment on column BPMS_RU_DONE_TASK.BACK_FLAG
  is '回退标志。0：下发，1：回退, 2:协作回退';
comment on column BPMS_RU_DONE_TASK.EXPIRATION
  is '何时超时，值为准确的时间点';
comment on column BPMS_RU_DONE_TASK.URGE_TIME
  is '催办的时间，值为准确的时间点';
comment on column BPMS_RU_DONE_TASK.URGE_INTERVAL
  is '催办的间隔时间，以秒为单位';
comment on column BPMS_RU_DONE_TASK.URGE_FLAG
  is '是否催办，0为不催办，1为催办';
comment on column BPMS_RU_DONE_TASK.NOTE
  is '填写意见';
comment on column BPMS_RU_DONE_TASK.NOTE_TYPE
  is '记录意见类型';
comment on column BPMS_RU_DONE_TASK.CREATE_TIME
  is '创建时间';
comment on column BPMS_RU_DONE_TASK.VERSION
  is '流程版本号';
comment on column BPMS_RU_DONE_TASK.TASK_COMPLETE_TYPE 
  is '任务处理完成的类型；1：启动、2：发送、3：回退、5：终止、6：恢复、7：改派 8挂起、9：协作补偿下发、10：跳转下发、-1：未处理';
comment on column BPMS_RU_DONE_TASK.REVOKE_BACK_FLAG
  is '已办是否能撤回标志位。1：能撤回，2：不能撤回';
comment on column BPMS_RU_DONE_TASK.MODIFY_DATE
  is '修改时间';
  
--重新插入已办表的数据
alter table BPMS_RU_DONE_TASK nologging;
insert into BPMS_RU_DONE_TASK
  (DONE_TASK_ID,
   MAIN_PROCESS_ID,
   CUR_PROCESS_ID,
   CUR_NODE_ID,
   CUR_NODE_NAME,
   MAIN_PROCESS_INS_ID,
   CUR_PROCESS_INS_ID,
   CUR_NODE_INS_ID,
   ACTIVITY_INS_ID,
   STATUS,
   TRANS_ACTOR_ID,
   ACTOR_NAME,
   READ_FLAG,
   BACK_FLAG,
   EXPIRATION,
   URGE_TIME,
   URGE_INTERVAL,
   URGE_FLAG,
   NOTE,
   NOTE_TYPE,
   CREATE_TIME,
   VERSION,
   TASK_COMPLETE_TYPE,
   REVOKE_BACK_FLAG,
   MODIFY_DATE)
  select tt.DONE_TASK_ID,
         tt.MAIN_PROCESS_ID,
         tt.CUR_PROCESS_ID,
         tt.CUR_NODE_ID,
         tt.CUR_NODE_NAME,
         tt.MAIN_PROCESS_INS_ID,
         tt.CUR_PROCESS_INS_ID,
		 tt.CUR_NODE_INS_ID,
         tt.ACTIVITY_INS_ID,
         tt.STATUS,
         tt.TRANS_ACTOR_ID,
         tt.ACTOR_NAME,
         tt.READ_FLAG,
         tt.BACK_FLAG,
         tt.EXPIRATION,
         tt.URGE_TIME,
         tt.URGE_INTERVAL,
         tt.URGE_FLAG,
         tt.NOTE,
         tt.NOTE_TYPE,
         tt.CREATE_TIME,
         tt.VERSION,
         tt.TASK_COMPLETE_TYPE,
         tt.REVOKE_BACK_FLAG,
         tt.MODIFY_DATE 
    from BPMS_RU_DONE_TASK_bak0316 tt;
alter table BPMS_RU_DONE_TASK logging;

commit;

--加主键索引
alter table BPMS_RU_DONE_TASK
  add constraint BPMS_RU_DONE_TASK primary key (DONE_TASK_ID);
create index DoneTask_Index1 on BPMS_RU_DONE_TASK (MAIN_PROCESS_INS_ID);
create index DoneTask_Index2 on BPMS_RU_DONE_TASK (TRANS_ACTOR_ID);
create index DONETASK_INDEX3 on BPMS_RU_DONE_TASK (CUR_NODE_INS_ID);
create index DONETASK_INDEX4 on BPMS_RU_DONE_TASK (ACTIVITY_INS_ID);

--【补丁】2016-03-17_11-00_BPMS_V1.4.96_无_查询流程走向节点信息接口_翟羽佳.sql
begin 
P_SOA_REG_SERVICE('userTaskServiceSoaDelegate','','app_service','','soa-service-register_20140928114542','com.comtop.bpms.delegate.soa.UserTaskServiceSoaDelegate');
 P_SOA_REG_Method('userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','queryAllUserNodesByVersion','userTaskServiceSoaDelegate','0','0','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I0','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I1','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','1','int','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I2','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','2','com.comtop.bpms.common.model.BpmsMap[]','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_O','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','2','0','java.util.List','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_E0','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');
end; 
/ 
commit;

-----------------------------------------------------  end  -----------------------------------------------------------------------------------

----------------------------------------------------- 整理人：杨赛---------------------------------------------------------------------------------
----------------------------------------------------- 执行人：杨赛---------------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-04-19 --------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------

--【补丁】2016-04-14_11-30_TOP_V5.1.215_无_新用户功能介绍屏蔽开关_梁璇.sql
DELETE FROM TOP_CFG_ATTRIBUTE_CONFIG  WHERE ATTRIBUTE_ID IN (SELECT A.ATTRIBUTE_ID FROM TOP_CFG_ATTRIBUTE A WHERE A.CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.introduceSwitch'));

DELETE FROM TOP_CFG_ATTRIBUTE WHERE CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.introduceSwitch');

DELETE FROM TOP_CFG_CONFIG  WHERE CONFIG_FULLCODE = 'ct.top.system.introduceSwitch';

declare recordExistedCount number;
begin
      SELECT COUNT(1) into recordExistedCount FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='ct.top';
      if recordExistedCount  < 1 then
          execute immediate 
'INSERT INTO TOP_CLASSIFY(CLASSIFY_ID,PARENT_CLASSIFY_ID,PARENT_CLASSIFY_TYPE,CLASSIFY_NAME,NAME_FULL_PATH,CLASSIFY_CODE,SORT_NO,CLASSIFY_TYPE,ISFLAG,DESCR,CREATOR_ID,CREATE_TIME,MODIFIER_ID,UPDATE_TIME) VALUES(sys_guid(),(SELECT S.MODULE_ID FROM TOP_SYS_MODULE S WHERE S.MODULE_CODE = ''ct'' AND S.STATE = 1),''SYS_MODULE'',''基础平台'','' '',''ct.top'',0,1,1,'''',''SuperAdmin'',SYSDATE,''SuperAdmin'',SYSDATE)';
      end if;  
end;  
/

INSERT INTO TOP_CFG_CONFIG(CONFIG_ID,CONFIG_CLASSIFY_ID,CONFIG_FULLCODE,CONFIG_NAME,CONFIG_TYPE,CONFIG_DESCRIPTION,IS_VALID,SORT_NO,CLASSIFY_TYPE, CREATE_TIME,UPDATE_TIME,CREATOR_ID,MODIFIER_ID)VALUES(sys_guid(),(SELECT TC.CLASSIFY_ID FROM TOP_CLASSIFY TC WHERE TC.CLASSIFY_CODE = 'ct.top'),'ct.top.system.introduceSwitch','新用户功能介绍屏蔽开关','4','系统没有该开关配置或者开关设置的是开启（true）时，执行提示新版本的页面；
系统有开关，且开关配置的是关闭（false）的时候，不执行提示新版本页面逻辑。',1,0,'UNI_CLASSIFY',SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE(ATTRIBUTE_ID,CONFIG_ID,ATTRIBUTE_CODE,ATTRIBUTE_NAME,CONFIG_TYPE,SORT_NO,CREATE_TIME,UPDATE_TIME,CREATOR_ID, MODIFIER_ID)VALUES(sys_guid(),(SELECT CONFIG_ID FROM TOP_CFG_CONFIG ITEM WHERE ITEM.CONFIG_FULLCODE='ct.top.system.introduceSwitch'),'','','0',0,SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE_CONFIG(ATTRIBUTE_CONFIG_ID,ATTRIBUTE_ID,IS_DEFALUT_VALUE,ATTRIBUTE_VALUE,SORT_NO,CREATE_TIME,CREATOR_ID,UPDATE_TIME,MODIFIER_ID)VALUES(sys_guid(),(SELECT a.attribute_id FROM top_cfg_config c,top_cfg_attribute a WHERE a.config_id =c.config_id AND c.config_fullcode = 'ct.top.system.introduceSwitch'),1,'true',0,SYSDATE,'SuperAdmin',SYSDATE,'SuperAdmin');

COMMIT;

--【补丁】2016-04-16_10-20_BPMS_V1.4.100_无__新增soa接口_翟羽佳.sql
--第一步，建立子系统用户到平台用户的DBlink，注意：执行脚本之前请填充'基础平台用户名','基础平台密码','基础平台数据库ip','端口','实例名'信息
create public database link TO_LCAM_PUB connect to 基础平台用户名 identified by 基础平台用户密码
using '基础平台数据库ip:端口/实例名';

--第二步：新建DBlink的同义词
create or replace synonym BPMS_DEF_COL_FILECONTENT_link  for BPMS_DEF_COL_FILECONTENT@TO_LCAM_PUB;
create or replace synonym BPMS_DEF_DEPLOYE_FILE_link  for BPMS_DEF_DEPLOYE_FILE@TO_LCAM_PUB; 
create or replace synonym BPMS_DEF_DEPLOY_COLCON_READ_link  for BPMS_DEF_DEPLOY_COLCON_READ@TO_LCAM_PUB;

begin 
P_SOA_REG_SERVICE('userTaskServiceSoaDelegate','','app_service','','soa-service-register_20140928114542','com.comtop.bpms.delegate.soa.UserTaskServiceSoaDelegate');
 P_SOA_REG_Method('userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','queryAllUserNodesByVersion','userTaskServiceSoaDelegate','0','0','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I0','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','0','java.lang.String','','');
	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I0','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','1','java.lang.String','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I1','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','2','int','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_I2','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','1','3','com.comtop.bpms.common.model.BpmsMap[]','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_O','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','2','0','java.util.List','','');
 	 p_soa_reg_Params('userTaskServiceSoaDelegate.queryAllUserNodesByVersion_E0','userTaskServiceSoaDelegate.queryAllUserNodesByVersion','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');

P_SOA_REG_Method('userTaskServiceSoaDelegate.isMultiInsNode','','isMultiInsNode','userTaskServiceSoaDelegate','0','0','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_I0','userTaskServiceSoaDelegate.isMultiInsNode','','1','0','java.lang.String','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_I0','userTaskServiceSoaDelegate.isMultiInsNode','','1','1','java.lang.String','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_I1','userTaskServiceSoaDelegate.isMultiInsNode','','1','2','int','','');
  p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_O','userTaskServiceSoaDelegate.isMultiInsNode','','2','0','java.util.List','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_E0','userTaskServiceSoaDelegate.isMultiInsNode','','3','0','com.comtop.bpms.common.AbstractBpmsException','',''); 
	 
end; 
/ 
commit;
-----------------------------------------------------  end  -----------------------------------------------------------------------------------

----------------------------------------------------- 整理人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 执行人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-05-06 --------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------
--------准备注册：colUndoLocalDelegate4TBI服务-------
begin 
 	 P_SOA_REG_SERVICE('colUndoLocalDelegate4TBI','','ws_tbi','','soa-service-register_20160428145011','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.delegate.ColUndoLocalDelegate4TBI');
 	 P_SOA_REG_Method('colUndoLocalDelegate4TBI.colInvoke','colInvoke','colInvoke','colUndoLocalDelegate4TBI','0','0','');
 	 p_soa_reg_Params('colUndoLocalDelegate4TBI.colInvoke_I0','colUndoLocalDelegate4TBI.colInvoke','','1','0','java.lang.String','','');
 	 p_soa_reg_Params('colUndoLocalDelegate4TBI.colInvoke_I1','colUndoLocalDelegate4TBI.colInvoke','','1','1','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.delegate.UndoColParamVO','','');
 	 p_soa_reg_Params('colUndoLocalDelegate4TBI.colInvoke_O','colUndoLocalDelegate4TBI.colInvoke','','2','0','com.comtop.bpms.engine.bpmnimpl.advancecollaboration.delegate.UndoColReturnVO','','');
end; 
/ 
begin 
 	 P_SOA_REG_WS('TBIColUndoLocalDelegate4TBIcolInvoke','ColUndoLocalDelegate4TBI_colInvoke','1','colUndoLocalDelegate4TBI.colInvoke','','http://am.soa.csg.cn','','','','com.comtop.soa.tbi.service.colundolocaldelegate4tbi.colinvoke.ColUndoLocalDelegate4TBI','');
end; 
/ 
commit;

begin 
P_SOA_REG_SERVICE('userTaskServiceSoaDelegate','','app_service','','soa-service-register_20140928114542','com.comtop.bpms.delegate.soa.UserTaskServiceSoaDelegate');
P_SOA_REG_Method('userTaskServiceSoaDelegate.queryLastDoneTask','','queryLastDoneTask','userTaskServiceSoaDelegate','0','0','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.queryLastDoneTask_I0','userTaskServiceSoaDelegate.queryLastDoneTask','','1','0','java.lang.String','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.queryLastDoneTask_I0','userTaskServiceSoaDelegate.queryLastDoneTask','','1','1','java.lang.String','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.queryLastDoneTask_I1','userTaskServiceSoaDelegate.queryLastDoneTask','','1','2','java.lang.String','','');
  p_soa_reg_Params('userTaskServiceSoaDelegate.queryLastDoneTask_O','userTaskServiceSoaDelegate.queryLastDoneTask','','2','0','com.comtop.bpms.common.model.DoneTaskInfo','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.queryLastDoneTask_E0','userTaskServiceSoaDelegate.queryLastDoneTask','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');

P_SOA_REG_Method('userTaskServiceSoaDelegate.isMultiInsNode','','isMultiInsNode','userTaskServiceSoaDelegate','0','0','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_I0','userTaskServiceSoaDelegate.isMultiInsNode','','1','0','java.lang.String','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_I0','userTaskServiceSoaDelegate.isMultiInsNode','','1','1','java.lang.String','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_I1','userTaskServiceSoaDelegate.isMultiInsNode','','1','2','int','','');
  p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_O','userTaskServiceSoaDelegate.isMultiInsNode','','2','0','boolean','','');
 p_soa_reg_Params('userTaskServiceSoaDelegate.isMultiInsNode_E0','userTaskServiceSoaDelegate.isMultiInsNode','','3','0','com.comtop.bpms.common.AbstractBpmsException','','');  
	 
end; 
/ 
commit;
-----------------------------------------------------  end  -----------------------------------------------------------------------------------


----------------------------------------------------- 整理人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 执行人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-06-02--------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------
--【补丁】2016-05-11_12-00_SOA_V2.1.30_无_删除SOA_CALL_LOG表历史分区及修改SOA日志记录级别
-- 1、创建临时表，存放表分区名及分区时间
CREATE TABLE TEMP_PART_SOA_CALL_LOG 
AS
SELECT T.partition_name partition_name,t.Partition_Position Partition_Position,to_lob(t.high_value) high_value   FROM USER_TAB_PARTITIONS t where t.table_name='SOA_CALL_LOG';

--select t.partition_name from TEMP_PART_SOA_CALL_LOG t where to_char(substr(t.high_value, 12, 19)) <'2016-03-01';
-- 2、根据分区时间查询分区名，循环删除历史分区，保留2至3个月的数据
declare  part_name_v char;
begin
     declare cursor part_vo is  select t.* from TEMP_PART_SOA_CALL_LOG t where to_char(substr(t.high_value, 12, 19)) <to_char(add_months(sysdate, -2), 'yyyy-MM');
     begin
       FOR part_name_v in part_vo
       loop
        if part_name_v.Partition_Position!=1 then
        --dbms_output.put_line(part_name_v.partition_name);    
         execute immediate 'alter table SOA_CALL_LOG drop partition '||part_name_v.partition_name ||' update global indexes';
        end if;    
       end loop;
     end;
end;
/
commit;
--select t.partition_name,to_char(add_months(sysdate, -2), 'yyyy-MM'), to_char(substr(t.high_value, 12, 19)) high_value
--from TEMP_PART_SOA_CALL_LOG t where to_char(substr(t.high_value, 12, 19)) <to_char(add_months(sysdate, -2), 'yyyy-MM');
-- 3、删除临时表
DROP  TABLE TEMP_PART_SOA_CALL_LOG;

-- 4、查询分区
--select partition_name,PARTITION_POSITION from user_tab_partitions where table_name='SOA_CALL_LOG';


--【补丁】2016-05-12_15-15_BPMS_V1.4.112_无_解决网省协作跟踪表问题_翟羽佳
create index I_RU_TODO_TASK_ACTI on BPMS_RU_TODO_TASK(ACTIVITY_INS_ID) nologging;

--【补丁】2016-05-16_12-00_TOP_V5.1.229_无_优化心跳检测SQL语句和修改已用DB空间统计方式_杜波
create or replace procedure PRO_TOP_TABLESPACE_STATISTICS(gatherTime DATE) AUTHID CURRENT_USER IS
v_top_space NUMBER;
v_attachment_space NUMBER;
v_used_space VARCHAR2(100);

begin


      SELECT sum(TOTAL_SPACE - FREE_SPACE) USED_SPACE
           INTO v_top_space
      FROM (SELECT TABLESPACE_NAME, SUM(BYTES) / 1024 / 1024 TOTAL_SPACE
              FROM DBA_DATA_FILES
             GROUP BY TABLESPACE_NAME) T1,
           (SELECT TABLESPACE_NAME, SUM(BYTES) / 1024 / 1024 FREE_SPACE
              FROM DBA_FREE_SPACE
             GROUP BY TABLESPACE_NAME) T2
     WHERE T1.TABLESPACE_NAME = T2.TABLESPACE_NAME;
     SELECT  SUM(T.FILE_SIZE)/1024/1024 FILE_SIZE
     INTO v_attachment_space
          FROM TOP_ATTACHMENT T;

     v_used_space := v_top_space + v_attachment_space;



     INSERT INTO app_data_value(BUSINESS_ID,GATHER_TIME,district_code,code,value)
     VALUES('00200',gatherTime,'广东电网','11',v_used_space);
     COMMIT;


end PRO_TOP_TABLESPACE_STATISTICS;
/

--【补丁】2016-05-31_12-00_TOP_V5.1.234_无_修改IT集中监控统计DB空间存储过程_杜波
create or replace procedure PRO_TOP_TABLESPACE_STATISTICS(gatherTime DATE) AUTHID CURRENT_USER IS
v_top_space NUMBER;
v_attachment_space NUMBER;
v_used_space VARCHAR2(100);

begin


      SELECT sum(TOTAL_SPACE - FREE_SPACE) USED_SPACE
           INTO v_top_space
      FROM (SELECT TABLESPACE_NAME, SUM(BYTES) / 1024 / 1024 TOTAL_SPACE
              FROM DBA_DATA_FILES
             GROUP BY TABLESPACE_NAME) T1,
           (SELECT TABLESPACE_NAME, SUM(BYTES) / 1024 / 1024 FREE_SPACE
              FROM DBA_FREE_SPACE
             GROUP BY TABLESPACE_NAME) T2
     WHERE T1.TABLESPACE_NAME = T2.TABLESPACE_NAME;
     SELECT  SUM(T.FILE_SIZE)/1024/1024 FILE_SIZE
     INTO v_attachment_space
          FROM TOP_ATTACHMENT T;

     v_used_space := round(v_top_space + v_attachment_space);



     INSERT INTO app_data_value(BUSINESS_ID,GATHER_TIME,district_code,code,value)
     VALUES('00200',gatherTime,'广东电网','11',v_used_space);
     COMMIT;


end PRO_TOP_TABLESPACE_STATISTICS;
/
-----------------------------------------------------  end  -----------------------------------------------------------------------------------


----------------------------------------------------- 整理人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 执行人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-11-25--------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------
ALTER TABLE TOP_FILE ADD FILE_CLASSIFY VARCHAR2(60);
commit;

--更新系统应用图标地址
update top_per_func a set a.menu_icon_url = regexp_replace(a.menu_icon_url, '^/web/lcam/{1}','/web/top/appicon/lcam/')
       where a.menu_icon_url is not null and regexp_like(a.menu_icon_url, '^/web/lcam/{1}'); 

update top_per_func a set a.menu_icon_url = regexp_replace(a.menu_icon_url, '^/lcam/{1}','/top/appicon/lcam/')
       where a.menu_icon_url is not null and regexp_like(a.menu_icon_url, '^/lcam/{1}'); 

commit;

--更新系统应用图标地址
update top_per_func a set a.menu_icon_url = regexp_replace(a.menu_icon_url, '^/web/lcam/{1}','/top/appicon/lcam/')
       where a.menu_icon_url is not null and regexp_like(a.menu_icon_url, '^/web/lcam/{1}'); 

update top_per_func a set a.menu_icon_url = regexp_replace(a.menu_icon_url, '^/lcam/{1}','/top/appicon/lcam/')
       where a.menu_icon_url is not null and regexp_like(a.menu_icon_url, '^/lcam/{1}'); 

update top_per_func a set a.menu_icon_url = regexp_replace(a.menu_icon_url, '^/web/{1}','/')
       where a.menu_icon_url is not null and regexp_like(a.menu_icon_url, '^/web/{1}'); 

commit;



DELETE FROM TOP_CFG_ATTRIBUTE_CONFIG  WHERE ATTRIBUTE_ID IN (SELECT A.ATTRIBUTE_ID FROM TOP_CFG_ATTRIBUTE A WHERE A.CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.log.outputmode'));

DELETE FROM TOP_CFG_ATTRIBUTE WHERE CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.log.outputmode');

DELETE FROM TOP_CFG_CONFIG  WHERE CONFIG_FULLCODE = 'ct.top.system.log.outputmode';

declare recordExistedCount number;
begin
      SELECT COUNT(1) into recordExistedCount FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='ct.top';
      if recordExistedCount  < 1 then
          execute immediate 
'INSERT INTO TOP_CLASSIFY(CLASSIFY_ID,PARENT_CLASSIFY_ID,PARENT_CLASSIFY_TYPE,CLASSIFY_NAME,NAME_FULL_PATH,CLASSIFY_CODE,SORT_NO,CLASSIFY_TYPE,ISFLAG,DESCR,CREATOR_ID,CREATE_TIME,MODIFIER_ID,UPDATE_TIME) VALUES(sys_guid(),(SELECT S.MODULE_ID FROM TOP_SYS_MODULE S WHERE S.MODULE_CODE = ''ct'' AND S.STATE = 1),''SYS_MODULE'',''基础平台'','' '',''ct.top'',0,1,1,'''',''SuperAdmin'',SYSDATE,''SuperAdmin'',SYSDATE)';
      end if;  
end;  
/

declare recordExistedCount number;
begin
      SELECT COUNT(1) into recordExistedCount FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='ct.top.system';
      if recordExistedCount  < 1 then
          execute immediate 
'INSERT INTO TOP_CLASSIFY(CLASSIFY_ID,PARENT_CLASSIFY_ID,PARENT_CLASSIFY_TYPE,CLASSIFY_NAME,NAME_FULL_PATH,CLASSIFY_CODE,SORT_NO,CLASSIFY_TYPE,ISFLAG,DESCR,CREATOR_ID,CREATE_TIME,MODIFIER_ID,UPDATE_TIME) VALUES(sys_guid(),(SELECT C.CLASSIFY_ID FROM TOP_CLASSIFY C WHERE C.CLASSIFY_CODE = ''ct.top'' AND C.CLASSIFY_TYPE = 1),''UNI_CLASSIFY'',''系统管理'','' '',''ct.top.system'',0,1,1,'''',''SuperAdmin'',SYSDATE,'''',SYSDATE)';
      end if;  
end;  
/

INSERT INTO TOP_CFG_CONFIG(CONFIG_ID,CONFIG_CLASSIFY_ID,CONFIG_FULLCODE,CONFIG_NAME,CONFIG_TYPE,CONFIG_DESCRIPTION,IS_VALID,SORT_NO,CLASSIFY_TYPE, CREATE_TIME,UPDATE_TIME,CREATOR_ID,MODIFIER_ID)VALUES(sys_guid(),(SELECT TC.CLASSIFY_ID FROM TOP_CLASSIFY TC WHERE TC.CLASSIFY_CODE = 'ct.top.system'),'ct.top.system.log.outputmode','日志输出模式','1','0：正常模式；1：调试模式；',1,0,'UNI_CLASSIFY',SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE(ATTRIBUTE_ID,CONFIG_ID,ATTRIBUTE_CODE,ATTRIBUTE_NAME,CONFIG_TYPE,SORT_NO,CREATE_TIME,UPDATE_TIME,CREATOR_ID, MODIFIER_ID)VALUES(sys_guid(),(SELECT CONFIG_ID FROM TOP_CFG_CONFIG ITEM WHERE ITEM.CONFIG_FULLCODE='ct.top.system.log.outputmode'),'','','0',0,SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE_CONFIG(ATTRIBUTE_CONFIG_ID,ATTRIBUTE_ID,IS_DEFALUT_VALUE,ATTRIBUTE_VALUE,SORT_NO,CREATE_TIME,CREATOR_ID,UPDATE_TIME,MODIFIER_ID)VALUES(sys_guid(),(SELECT a.attribute_id FROM top_cfg_config c,top_cfg_attribute a WHERE a.config_id =c.config_id AND c.config_fullcode = 'ct.top.system.log.outputmode'),1,'1',0,SYSDATE,'SuperAdmin',SYSDATE,'SuperAdmin');

COMMIT;

alter table workbench_todo_statistics add update_time date;

create table top_per_subject_resource_0817 as select * from top_per_subject_resource;
delete from top_per_subject_resource where resource_type_code = 'OTHER';
INSERT INTO top_per_subject_resource(subject_resource_id, subject_id, subject_classify_code, resource_id, 
		       resource_type_code, creator_id, create_time)
with t_classify AS (
select c.classify_id from top_classify c
where c.isflag = 1
and c.classify_type = 2
start with c.parent_classify_id = '-1'
connect by prior c.classify_id = c.parent_classify_id)
select * from (select sys_guid(),g.admin_id, 'ADMIN', p.other_post_id, 'OTHER', 'SuperAdmin', SYSDATE from
      top_per_grade_admin g,top_post_other p 
      where p.classify_id in (select * from t_classify)
      and p.state = 1);
commit;

ALTER TABLE workbench_message DROP column update_time ;
ALTER TABLE workbench_message_rel DROP  column update_time ;
ALTER TABLE workbench_message_history DROP column update_time ;
ALTER TABLE workbench_message_rel_history DROP  column update_time ;
ALTER TABLE workbench_message ADD update_time DATE ;
ALTER TABLE workbench_message_rel ADD update_time DATE ;
ALTER TABLE workbench_message_history ADD update_time DATE;
ALTER TABLE workbench_message_rel_history ADD update_time DATE;



DELETE FROM TOP_CFG_ATTRIBUTE_CONFIG  WHERE ATTRIBUTE_ID IN (SELECT A.ATTRIBUTE_ID FROM TOP_CFG_ATTRIBUTE A WHERE A.CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.logoutPageClose'));

DELETE FROM TOP_CFG_ATTRIBUTE WHERE CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.logoutPageClose');

DELETE FROM TOP_CFG_CONFIG  WHERE CONFIG_FULLCODE = 'ct.top.system.logoutPageClose';

declare recordExistedCount number;
begin
      SELECT COUNT(1) into recordExistedCount FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='ct.top';
      if recordExistedCount  < 1 then
          execute immediate 
'INSERT INTO TOP_CLASSIFY(CLASSIFY_ID,PARENT_CLASSIFY_ID,PARENT_CLASSIFY_TYPE,CLASSIFY_NAME,NAME_FULL_PATH,CLASSIFY_CODE,SORT_NO,CLASSIFY_TYPE,ISFLAG,DESCR,CREATOR_ID,CREATE_TIME,MODIFIER_ID,UPDATE_TIME) VALUES(sys_guid(),(SELECT S.MODULE_ID FROM TOP_SYS_MODULE S WHERE S.MODULE_CODE = ''ct'' AND S.STATE = 1),''SYS_MODULE'',''基础平台'','' '',''ct.top'',0,1,1,'''',''SuperAdmin'',SYSDATE,''SuperAdmin'',SYSDATE)';
      end if;  
end;  
/

INSERT INTO TOP_CFG_CONFIG(CONFIG_ID,CONFIG_CLASSIFY_ID,CONFIG_FULLCODE,CONFIG_NAME,CONFIG_TYPE,CONFIG_DESCRIPTION,IS_VALID,SORT_NO,CLASSIFY_TYPE, CREATE_TIME,UPDATE_TIME,CREATOR_ID,MODIFIER_ID)VALUES(sys_guid(),(SELECT TC.CLASSIFY_ID FROM TOP_CLASSIFY TC WHERE TC.CLASSIFY_CODE = 'ct.top'),'ct.top.system.logoutPageClose','系统登出关闭页面开关','4','系统没有该开关配置或者开关设置的是关闭（false）时，点击登出，跳转到重新登录页面；
系统有开关，且开关配置的是开启（true）的时候，点击登出，关掉页面。',1,0,'UNI_CLASSIFY',SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE(ATTRIBUTE_ID,CONFIG_ID,ATTRIBUTE_CODE,ATTRIBUTE_NAME,CONFIG_TYPE,SORT_NO,CREATE_TIME,UPDATE_TIME,CREATOR_ID, MODIFIER_ID)VALUES(sys_guid(),(SELECT CONFIG_ID FROM TOP_CFG_CONFIG ITEM WHERE ITEM.CONFIG_FULLCODE='ct.top.system.logoutPageClose'),'','','0',0,SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE_CONFIG(ATTRIBUTE_CONFIG_ID,ATTRIBUTE_ID,IS_DEFALUT_VALUE,ATTRIBUTE_VALUE,SORT_NO,CREATE_TIME,CREATOR_ID,UPDATE_TIME,MODIFIER_ID)VALUES(sys_guid(),(SELECT a.attribute_id FROM top_cfg_config c,top_cfg_attribute a WHERE a.config_id =c.config_id AND c.config_fullcode = 'ct.top.system.logoutPageClose'),1,'false',0,SYSDATE,'SuperAdmin',SYSDATE,'SuperAdmin');

COMMIT;



DELETE FROM TOP_CFG_ATTRIBUTE_CONFIG  WHERE ATTRIBUTE_ID IN (SELECT A.ATTRIBUTE_ID FROM TOP_CFG_ATTRIBUTE A WHERE A.CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.logoutPageClose'));

DELETE FROM TOP_CFG_ATTRIBUTE WHERE CONFIG_ID IN (SELECT C.CONFIG_ID FROM TOP_CFG_CONFIG C WHERE C.CONFIG_FULLCODE = 'ct.top.system.logoutPageClose');

DELETE FROM TOP_CFG_CONFIG  WHERE CONFIG_FULLCODE = 'ct.top.system.logoutPageClose';

declare recordExistedCount number;
begin
      SELECT COUNT(1) into recordExistedCount FROM TOP_CLASSIFY T WHERE T.CLASSIFY_CODE ='ct.top';
      if recordExistedCount  < 1 then
          execute immediate 
'INSERT INTO TOP_CLASSIFY(CLASSIFY_ID,PARENT_CLASSIFY_ID,PARENT_CLASSIFY_TYPE,CLASSIFY_NAME,NAME_FULL_PATH,CLASSIFY_CODE,SORT_NO,CLASSIFY_TYPE,ISFLAG,DESCR,CREATOR_ID,CREATE_TIME,MODIFIER_ID,UPDATE_TIME) VALUES(sys_guid(),(SELECT S.MODULE_ID FROM TOP_SYS_MODULE S WHERE S.MODULE_CODE = ''ct'' AND S.STATE = 1),''SYS_MODULE'',''基础平台'','' '',''ct.top'',0,1,1,'''',''SuperAdmin'',SYSDATE,''SuperAdmin'',SYSDATE)';
      end if;  
end;  
/

INSERT INTO TOP_CFG_CONFIG(CONFIG_ID,CONFIG_CLASSIFY_ID,CONFIG_FULLCODE,CONFIG_NAME,CONFIG_TYPE,CONFIG_DESCRIPTION,IS_VALID,SORT_NO,CLASSIFY_TYPE, CREATE_TIME,UPDATE_TIME,CREATOR_ID,MODIFIER_ID)VALUES(sys_guid(),(SELECT TC.CLASSIFY_ID FROM TOP_CLASSIFY TC WHERE TC.CLASSIFY_CODE = 'ct.top'),'ct.top.system.logoutPageClose','系统登出关闭页面开关','4','系统没有该开关配置或者开关设置的是关闭（false）时，点击登出，跳转到重新登录页面；
系统有开关，且开关配置的是开启（true）的时候，点击登出，关掉页面。',1,0,'UNI_CLASSIFY',SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE(ATTRIBUTE_ID,CONFIG_ID,ATTRIBUTE_CODE,ATTRIBUTE_NAME,CONFIG_TYPE,SORT_NO,CREATE_TIME,UPDATE_TIME,CREATOR_ID, MODIFIER_ID)VALUES(sys_guid(),(SELECT CONFIG_ID FROM TOP_CFG_CONFIG ITEM WHERE ITEM.CONFIG_FULLCODE='ct.top.system.logoutPageClose'),'','','0',0,SYSDATE,SYSDATE,'SuperAdmin','SuperAdmin');

INSERT INTO TOP_CFG_ATTRIBUTE_CONFIG(ATTRIBUTE_CONFIG_ID,ATTRIBUTE_ID,IS_DEFALUT_VALUE,ATTRIBUTE_VALUE,SORT_NO,CREATE_TIME,CREATOR_ID,UPDATE_TIME,MODIFIER_ID)VALUES(sys_guid(),(SELECT a.attribute_id FROM top_cfg_config c,top_cfg_attribute a WHERE a.config_id =c.config_id AND c.config_fullcode = 'ct.top.system.logoutPageClose'),1,'false',0,SYSDATE,'SuperAdmin',SYSDATE,'SuperAdmin');

COMMIT;


create index index_top_log_fun_operate on top_log_fun_detail(operate_id);

create table cache_attribute_test1009(
       attribute_id varchar(32) primary key,
       attribute_key varchar(500),
       attribute_length number(8),
       update_time date
);

declare  
  cnt VARCHAR(10);
begin 
   select data_type into cnt From USER_TAB_COLUMNS  where table_name='SOA_CALL_LOG' AND column_name='INVOKE_RESULT';
   --0、检查该字段是否为CLOB类型
   if cnt<>'CLOB' then
				--1、修改正式字段名为临时字段名
         execute immediate 'alter table SOA_CALL_LOG rename column INVOKE_RESULT to INVOKE_RESULT_TEMP' ;
				--2、新增字段
         execute immediate 'alter table SOA_CALL_LOG add INVOKE_RESULT CLOB' ;
				 execute immediate  'comment on column SOA_CALL_LOG.INVOKE_RESULT  is' ||'''调用结果''';
        --3、将数据复制到新增的字段
         execute immediate 'update soa_call_log t set t.INVOKE_RESULT=t.INVOKE_RESULT_TEMP' ;
         execute immediate 'commit' ;
        --4、删除原来字符类型的字段
         execute immediate 'alter table SOA_CALL_LOG drop column INVOKE_RESULT_TEMP';
  end if;
end;
/


--新增名称为‘IND_BDP_PROCESS_ID’的PROCESS_ID列的Normal索引
declare
  isIndexAExist number;
  isIndexBExist number;
begin
    --判断索引是否存在  
  select count(1)
      into isIndexAExist
      from user_ind_columns t
     where t.table_name = upper('BPMS_DEF_PROCESS') and t.column_name = 'PROCESS_ID';
	 select count(1)
      into isIndexBExist
      from user_ind_columns t
     where t.table_name = upper('BPMS_DEF_PROCESS') and t.column_name = 'DEPLOYE_ID';
    if isIndexAExist = 0 then
      execute immediate 'Create Index IND_BDP_PROCESS_ID On BPMS_DEF_PROCESS(PROCESS_ID) nologging';
    end if;
    if isIndexBExist = 0 then
	execute immediate 'Create Index IND_BDP_DEPLOYE_ID On BPMS_DEF_PROCESS(DEPLOYE_ID) nologging';
	end if;
  commit;
end;
/

-----------------------------------------------------  end  -----------------------------------------------------------------------------------

----------------------------------------------------- 整理人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 执行人：凌晨---------------------------------------------------------------------------------
----------------------------------------------------- 日期：2016-12-14--------------------------------------------------------------------------
-----------------------------------------------------  start  ---------------------------------------------------------------------------------
---1.删除掉脚本注册记录，无需备份
drop table soa_register_log;
--2.建立新表
create table SOA_REGISTER_LOG
(
  METHOD_CODE   VARCHAR2(200) not null,
  SERVICE_CODE  VARCHAR2(100) not null,
  LOG_TIME      TIMESTAMP(6) not null,
  LOG_LEVEL     NUMBER(1),
  CLASS_NAME    VARCHAR2(40),
  METHOD_NAME   VARCHAR2(100),
  CODE_LINE     NUMBER(4),
  OVERHEAD_TIME NUMBER(4),
  LOG_CONTENT   VARCHAR2(2000)
)
;
comment on table SOA_REGISTER_LOG
  is '服务注册日志记录';
comment on column SOA_REGISTER_LOG.METHOD_CODE
  is '服务元素（方法）编号';
comment on column SOA_REGISTER_LOG.SERVICE_CODE
  is '服务对象编号';
comment on column SOA_REGISTER_LOG.LOG_TIME
  is '日志记录时间';
comment on column SOA_REGISTER_LOG.LOG_LEVEL
  is '日志级别';
comment on column SOA_REGISTER_LOG.CLASS_NAME
  is '类全名';
comment on column SOA_REGISTER_LOG.METHOD_NAME
  is '服务方法，如果是本地服务需要填写此项';
comment on column SOA_REGISTER_LOG.CODE_LINE
  is '代码行';
comment on column SOA_REGISTER_LOG.OVERHEAD_TIME
  is '消耗时间';
comment on column SOA_REGISTER_LOG.LOG_CONTENT
  is '日志内容';

-----------------------------------------------------  end  -----------------------------------------------------------------------------------