
package com.comtop.cip.runtime.demo.defect.dao;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectSolution;

/**
 * 
 * Product defect solution DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@PetiteBean
public class ProdDefectSolutionDao extends CommonDAO<ProdDefectSolution> {
    
    @Override
    public Object insert(ProdDefectSolution prodDefectSolution, String... mappingTables) {
        Object result = super.insert(prodDefectSolution);
        return result;
    }
    
    @Override
    public boolean update(ProdDefectSolution model, String... mappingTables) {
        boolean result = super.update(model);
        return result;
    }
    
    @Override
    public boolean delete(ProdDefectSolution model, String... mappingTables) {
        boolean result = super.delete(model);
        return result;
    }
    
    @Override
    public ProdDefectSolution load(ProdDefectSolution prodDefectSolution, String... mappingTables) {
        ProdDefectSolution result = super.load(prodDefectSolution);
        return result;
    }
    
    /**
     * 
     * 测试缺陷类的相关方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        ProdDefectSolutionDao prodDefectSolutionDao = new ProdDefectSolutionDao();
        ProdDefectSolution prodDefectSolution = new ProdDefectSolution();
        prodDefectSolution.setId(1);
        prodDefectSolution.setDisposalDescr("disposal descr");
        prodDefectSolutionDao.insert(prodDefectSolution);
        
        prodDefectSolution = prodDefectSolutionDao.load(prodDefectSolution);
        System.out.println("prodDefectSolution.disposalDescr=" + prodDefectSolution.getDisposalDescr());
        
        prodDefectSolutionDao.delete(prodDefectSolution);
        System.out.println("success");
    }
}
