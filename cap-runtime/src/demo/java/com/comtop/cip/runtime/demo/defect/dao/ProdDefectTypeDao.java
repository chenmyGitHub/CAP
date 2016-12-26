
package com.comtop.cip.runtime.demo.defect.dao;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectType;

/**
 * 
 * Product Defect Type DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@PetiteBean
public class ProdDefectTypeDao extends CommonDAO<ProdDefectType> {
    
    @Override
    public Object insert(ProdDefectType prodDefectType, String... mappingTables) {
        Object id = super.insert(prodDefectType);
        return id;
    }
    
    @Override
    public boolean update(ProdDefectType prodDefectType, String... mappingTables) {
        boolean result = super.update(prodDefectType);
        return result;
    }
    
    @Override
    public boolean delete(ProdDefectType prodDefectType, String... mappingTables) {
        boolean result = super.delete(prodDefectType);
        return result;
    }
    
    @Override
    public ProdDefectType load(ProdDefectType prodDefectType, String... mappingTables) {
        ProdDefectType result = super.load(prodDefectType);
        return result;
    }
    
    /**
     * 
     * 测试缺陷类的相关方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        ProdDefectTypeDao prodDefectTypeDao = new ProdDefectTypeDao();
        ProdDefectType prodDefectType = new ProdDefectType();
        prodDefectType.setId(1);
        prodDefectType.setDefectTypeName("defect Type Name");
        prodDefectTypeDao.insert(prodDefectType);
        
        prodDefectType = prodDefectTypeDao.load(prodDefectType);
        System.out.println("prodDefectType.defectTypeName=" + prodDefectType.getDefectTypeName());
        
        prodDefectTypeDao.delete(prodDefectType);
        System.out.println("success");
    }
}
