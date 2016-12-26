/**
 * 
 */

package com.comtop.cap.runtime.base.facade;

import java.util.List;

import com.comtop.cap.runtime.base.appservice.CapSoaExtendAppService;
import com.comtop.cap.runtime.base.model.CapSoaParamExtendVO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.soa.init.SOAManagerService;

/**
 * @author luozhenming
 *
 */
@PetiteBean
public class CapSoaExtendFacade {
    
    /**
	 * 
	 */
    @PetiteInject
    protected CapSoaExtendAppService capSoaExtendAppService;
    
    /**
     * @param soaSid soa服务Id
     * @return 调用soa服务需要的扩展参数信息
     * 
     */
    public List<CapSoaParamExtendVO> querySoaExtendParamInfoList(String soaSid) {
        return capSoaExtendAppService.querySoaExtendParamInfoList(soaSid);
    }
    
    // /**
    // * @param entityName 实体名
    // * @param pkgName 包路径
    // * @param flag 流程化标识位
    // */
    // public void insertSoaExtendParamInfoList(String entityName, String pkgName, int flag) {
    // capSoaExtendAppService.insertSoaExtendParamInfoList(entityName, pkgName, flag);
    // }
    
    /**
     * 注册实体方法的soa服务sql脚本
     * 
     * @param sqlContent 实体服务sql脚本
     */
    public void registerEntitySoaSql(String sqlContent) {
        capSoaExtendAppService.registerEntitySoaSql(sqlContent);
    }
    
    /**
     * 刷新soa服务
     * 
     * @param lstServiceCodes 服务编码集合
     */
    public void refreshSoaService(List<String> lstServiceCodes) {
        SOAManagerService objSOAManagerService = new SOAManagerService();
        objSOAManagerService.reloadLocalServices(lstServiceCodes);
    }
    
}
