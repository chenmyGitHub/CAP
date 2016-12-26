
package com.comtop.cip.runtime.demo.defect.dao;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectPhenomenon;

/**
 * 
 * Product defect phenomenon DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@PetiteBean
public class ProdDefectPhenomenonDao extends CommonDAO<ProdDefectPhenomenon> {
    
    @Override
    public Object insert(ProdDefectPhenomenon prodDefectPhenomenon, String... mappingTables) {
        Object id = super.insert(prodDefectPhenomenon);
        return id;
    }
    
    @Override
    public boolean update(ProdDefectPhenomenon model, String... mappingTables) {
        boolean result = super.update(model);
        return result;
    }
    
    @Override
    public boolean delete(ProdDefectPhenomenon model, String... mappingTables) {
        boolean result = super.delete(model);
        return result;
    }
    
    @Override
    public ProdDefectPhenomenon load(ProdDefectPhenomenon model, String... mappingTables) {
        ProdDefectPhenomenon result = super.load(model);
        return result;
    }
    
    /**
     * 
     * 测试缺陷类的相关方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        ProdDefectPhenomenonDao prodDefectPhenomenonDao = new ProdDefectPhenomenonDao();
        ProdDefectPhenomenon prodDefectPhenomenon = new ProdDefectPhenomenon();
        prodDefectPhenomenon.setId(1);
        prodDefectPhenomenon.setDefectDamageLevel("level");
        prodDefectPhenomenon.setDefectPhenomenonCode("code");
        prodDefectPhenomenonDao.insert(prodDefectPhenomenon);
        
        prodDefectPhenomenon = prodDefectPhenomenonDao.load(prodDefectPhenomenon);
        System.out.println("prodDefectPhenomenon.defectPhenomenonCode="
            + prodDefectPhenomenon.getDefectPhenomenonCode());
        
        prodDefectPhenomenonDao.delete(prodDefectPhenomenon);
        System.out.println("success");
    }
}
