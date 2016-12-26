
package com.comtop.cip.runtime.demo.defect.dao;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectCause;

/**
 * 缺陷DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 作者
 */
@PetiteBean
public class ProdDefectCauseDao extends CommonDAO<ProdDefectCause> {
    
    @Override
    public Object insert(ProdDefectCause prodDefectCause, String... mappingTables) {
        Object id = super.insert(prodDefectCause);
        return id;
    }
    
    @Override
    public boolean update(ProdDefectCause model, String... mappingTables) {
        boolean result = super.update(model);
        return result;
    }
    
    @Override
    public boolean delete(ProdDefectCause model, String... mappingTables) {
        boolean result = super.delete(model);
        return result;
    }
    
    @Override
    public ProdDefectCause load(ProdDefectCause prodDefectCause, String... mappingTables) {
        ProdDefectCause result = super.load(prodDefectCause);
        return result;
    }
    
    /**
     * 
     * 测试缺陷类的相关方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        ProdDefectCauseDao prodDefectCauseDao = new ProdDefectCauseDao();
        ProdDefectCause ProdDefectCause = new ProdDefectCause();
        ProdDefectCause.setId(1);
        ProdDefectCause.setDefectCauseName("cause name");
        
        prodDefectCauseDao.insert(ProdDefectCause);
        
        ProdDefectCause = prodDefectCauseDao.load(ProdDefectCause);
        System.out.println("prodDefectCauseDao.defectCauseName=" + ProdDefectCause.getDefectCauseName());
        
        prodDefectCauseDao.delete(ProdDefectCause);
        System.out.println("success");
    }
}
