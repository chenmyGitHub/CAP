/**
 * 
 */

package com.comtop.cap.runtime.base.appservice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.runtime.base.model.CapSoaParamExtendVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;

/**
 * @author luozhenming
 *
 */
@PetiteBean
public class CapSoaExtendAppService extends CapBaseAppService<CapSoaParamExtendVO> {
    
    /**
     * @param soaSid soa服务Id
     * @return 调用soa服务需要的扩展参数信息
     * 
     */
    public List<CapSoaParamExtendVO> querySoaExtendParamInfoList(String soaSid) {
        Map<String, String> objParams = new HashMap<String, String>();
        objParams.put("soaSid", soaSid);
        List<CapSoaParamExtendVO> lst = capBaseCommonDAO.queryList(
            "com.comtop.cap.runtime.base.model.querySoaExtendParamInfoList", objParams);
        return lst;
    }
    
    /**
     * @param entityName 实体名
     * @param pkgName 包路径
     * @param flag 流程化标识位
     */
    public void insertSoaExtendParamInfoList(String entityName, String pkgName, int flag) {
        Map<String, Object> objParam = new HashMap<String, Object>();
        objParam.put("entityName", entityName);
        objParam.put("pkgName", pkgName);
        objParam.put("proFlag", flag);
        capBaseCommonDAO.queryList("com.comtop.cap.runtime.base.model.callProToInsertSoaExtendParamInfoList", objParam);
    }
    
    /**
     * 注册实体方法的soa服务sql脚本
     * 
     * @param sqlContent 实体服务sql脚本
     */
    public void registerEntitySoaSql(String sqlContent) {
        List<String> lstProcedure = Arrays.asList(sqlContent.trim().split(";"));
        capBaseCommonDAO.execProcedureSqlList(lstProcedure);
    }
    
}
