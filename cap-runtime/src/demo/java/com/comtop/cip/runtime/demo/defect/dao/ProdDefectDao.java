
package com.comtop.cip.runtime.demo.defect.dao;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.runtime.demo.defect.model.ProdDefect;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectPhenomenon;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectSolution;
import com.comtop.cip.runtime.demo.defect.model.ProdDefectType;

/**
 * 
 * Product Defect DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@PetiteBean
public class ProdDefectDao extends CommonDAO<ProdDefect> {
    
    /**
     * product defect type DAO
     */
    @PetiteInject
    private ProdDefectTypeDao prodDefectTypeDao;
    
    /**
     * product defect phenomenon DAO
     */
    @PetiteInject
    private ProdDefectPhenomenonDao prodDefectPhenomenonDao;
    
    /**
     * product defect solution DAO
     */
    @PetiteInject
    private ProdDefectSolutionDao prodDefectSolutionDao;
    
    @Override
    public Object insert(ProdDefect prodDefect, String... mappingTables) {
        Object id = super.insert(prodDefect);
        
        // NOTICE: don't generate the sub model
        // see ModelADao.insert
        return id;
    }
    
    @Override
    public boolean update(ProdDefect prodDefect, String... mappingTables) {
        boolean result = super.update(prodDefect);
        
        // NOTICE: don't generate the sub model
        // see ModelADao.update
        return result;
    }
    
    @Override
    public boolean delete(ProdDefect prodDefect, String... mappingTables) {
        boolean result = super.delete(prodDefect);
        
        // NOTICE: don't generate the sub model
        // see ModelADao.delete
        return result;
    }
    
    @Override
    public ProdDefect load(ProdDefect model, String... mappingTables) {
        ProdDefect result = super.load(model);
        
        // NOTICE: new strategy, load the type according to model property
        // see ModelADao.load
        ProdDefectType prodDefectType = new ProdDefectType();
        prodDefectType.setId(result.getDefectTypeId());
        prodDefectType = prodDefectTypeDao.load(prodDefectType);
        result.setProdDefectType(prodDefectType);
        
        ProdDefectPhenomenon prodDefectPhenomenon = new ProdDefectPhenomenon();
        prodDefectPhenomenon.setId(result.getDefectPhenomenonId());
        prodDefectPhenomenon = prodDefectPhenomenonDao.load(prodDefectPhenomenon);
        result.setProdDefectPhenomenon(prodDefectPhenomenon);
        
        return result;
    }
    
    /**
     * 
     * 批量插入defect solution
     * 
     * @param lstDefectSolution solution列表
     * @return 成功的个数
     */
    // TODO: move this logic to prodDefectSolutionDao
    public int batchInsertDefectSolution(List<ProdDefectSolution> lstDefectSolution) {
        int result = prodDefectSolutionDao.insert(lstDefectSolution);
        return result;
    }
    
    /**
     * 
     * 批量删除defect
     * 
     * @param defectIds id 列表
     * @return 成功删除个数
     */
    public int removeDefect(String[] defectIds) {
        // UPDATE PROD_DEFECT PD SET PD.STATE = -2 WHERE PD.DEFECT_ID IN ( #defectIds )
        
        // TODO: conversion code
        List<ProdDefect> models = new ArrayList<ProdDefect>();
        for (String id : defectIds) {
            ProdDefect model = new ProdDefect();
            model.setId(new Integer(id));
            models.add(model);
        }
        
        models = load(models);
        
        // TODO: conversion code
        for (ProdDefect model : models) {
            model.setState(-2);
        }
        
        update(models);
        return models.size();
    }
    
    /**
     * 更新修复标志
     * 
     * @param repairFlag 标志
     * @param defectId defect id
     * @return 成功个数
     */
    public int updateDefectRepairFlag(int repairFlag, int defectId) {
        // TODO: conversion code
        ProdDefect model = new ProdDefect();
        model.setId(defectId);
        
        model = load(model);
        // model.setRepairFlag(repairFlag);
        boolean result = update(model);
        
        // TODO: conversion code
        return result ? 1 : 0;
    }
    
    /**
     * 批理修改defect info
     * 
     * @param models model列表
     * @return 成功个数
     */
    // USE update method
    public int batchUpdateDefectInfo(List<ProdDefect> models) {
        int result = update(models);
        return result;
    }
    
    /**
     * 
     * 测试缺陷类的相关方法
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        // add
        ProdDefectTypeDao prodDefectTypeDao = new ProdDefectTypeDao();
        ProdDefectDao prodDefectDao = new ProdDefectDao();
        ProdDefectType prodDefectType = new ProdDefectType();
        prodDefectType.setId(1);
        prodDefectType.setDefectTypeCode("code");
        prodDefectType.setDefectTypeName("defect type");
        prodDefectTypeDao.insert(prodDefectType);
        
        ProdDefectPhenomenonDao prodDefectPhenomenonDao = new ProdDefectPhenomenonDao();
        ProdDefectPhenomenon prodDefectPhenomenon = new ProdDefectPhenomenon();
        prodDefectPhenomenon.setId(1);
        prodDefectPhenomenon.setDefectDamageLevel("level");
        prodDefectPhenomenon.setDefectPhenomenonCode("code");
        prodDefectPhenomenonDao.insert(prodDefectPhenomenon);
        
        prodDefectDao.prodDefectPhenomenonDao = prodDefectPhenomenonDao;
        prodDefectDao.prodDefectTypeDao = prodDefectTypeDao;
        ProdDefect prodDefect = new ProdDefect();
        prodDefect.setId(1);
        prodDefect.setDefectPhenomenonId(1);
        prodDefect.setDefectTypeId(1);
        prodDefect.setDefectCode("aaa");
        prodDefectDao.insert(prodDefect);
        
        // load
        prodDefect = prodDefectDao.load(prodDefect);
        System.out.println("prodDefect.prodDefectType.id=" + prodDefect.getProdDefectType().getId());
        System.out.println("prodDefect.prodDefectPhenomenon.id=" + prodDefect.getProdDefectPhenomenon().getId());
        
        // delete
        prodDefectTypeDao.delete(prodDefectType);
        prodDefectPhenomenonDao.delete(prodDefectPhenomenon);
        prodDefectDao.delete(prodDefect);
        System.out.println("success");
    }
}
