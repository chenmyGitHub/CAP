
package com.comtop.cip.runtime.demo.company.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.runtime.demo.company.model.Address;
import com.comtop.cip.runtime.demo.company.model.Company;
import com.comtop.cip.runtime.demo.company.model.CompanyAndAddress;
import com.comtop.cip.runtime.demo.company.model.Staff;

/**
 * 
 * Company DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
@PetiteBean
public class CompanyDao extends CommonDAO<Company> {
    
    /**
     * Address DAO
     */
    @PetiteInject
    private AddressDao addressDao;
    
    /**
     * Staff DAO
     */
    @PetiteInject
    private StaffDao staffDao;
    
    @Override
    public Object insert(Company company, String... mappingTables) {
        Object id = super.insert(company);
        
        if (company.getAddress() != null) {
            company.getAddress().setCompanyId(company.getId());
            addressDao.insert(company.getAddress());
        }
        
        if (company.getStaffs() != null) {
            for (Staff modelC : company.getStaffs()) {
                modelC.setCompanyId(company.getId());
            }
            staffDao.insert(company.getStaffs());
        }
        
        return id;
    }
    
    @Override
    public boolean update(Company company, String... mappingTables) {
        boolean result = super.update(company);
        
        if (company.getAddress() != null) {
            company.getAddress().setCompanyId(company.getId());
            addressDao.update(company.getAddress());
        }
        
        if (company.getStaffs() != null) {
            for (Staff modelC : company.getStaffs()) {
                modelC.setCompanyId(company.getId());
            }
            staffDao.update(company.getStaffs());
        }
        
        return result;
    }
    
    @Override
    public boolean delete(Company company, String... mappingTables) {
        Company dbmodel = load(company);
        boolean result = super.delete(dbmodel);
        
        if (dbmodel.getAddress() != null) {
            addressDao.delete(dbmodel.getAddress());
        }
        
        if (dbmodel.getStaffs() != null) {
            staffDao.delete(dbmodel.getStaffs());
        }
        
        return result;
    }
    
    @Override
    public Company load(Company company, String... mappingTables) {
        Company result = super.load(company);
        
        Address modelb = (Address) super.selectOne("loadCompany_Address", result.getId());
        result.setAddress(modelb);
        
        @SuppressWarnings("unchecked")
        List<Staff> modelCs = super.queryList("loadCompany_Staffs", result.getId());
        result.setStaffs(modelCs);
        return company;
    }
    
    /**
     * 
     * 带参的静态查询示例
     * 
     * @param parameter1 参数1
     * @param parameter2 参数2
     * @return 查询结果
     */
    public List<?> queryStatic(String parameter1, Integer parameter2) {
        List<Object> params = new ArrayList<Object>();
        params.add(parameter1);
        params.add(parameter2);
        return super.queryList("queryStatic", params, 0, 0);
    }
    
    /**
     * 
     * 动态查询示例
     * 
     * @param company 查询参数
     * @param condition 查询条件
     * @return 查询结果
     */
    public List<?> queryDynamic(Company company, Condition condition) {
        return super.queryList("queryStatic", company, 0, 0);
    }
    
    /**
     * @param args 参数
     */
    public static void main(String[] args) {
        CompanyDao dao = new CompanyDao();
        dao.addressDao = new AddressDao();
        dao.staffDao = new StaffDao();
        
        Company model = new Company();
        
        model.setName("f1-a");
        model.setNo(2L);
        model.setActive(true);
        dao.merge(model);
        
        dao.delete(model);
        
        model = new Company();
        model.setName("f1-a");
        model.setNo(2L);
        model.setActive(true);
        Address modelB = new Address();
        model.setAddress(modelB);
        modelB.setDetail("f1-b");
        Staff modelC = new Staff();
        modelC.setName("f1-c-1");
        Staff modelC2 = new Staff();
        modelC2.setName("f1-c-2");
        List<Staff> modelCList = new ArrayList<Staff>();
        modelCList.add(modelC);
        modelCList.add(modelC2);
        model.setStaffs(modelCList);
        Object id = dao.merge(model);
        model.setId((String) id);
        dao.delete(model);
        
        CompanyAndAddressDao daoAB = new CompanyAndAddressDao();
        CompanyAndAddress modelab = new CompanyAndAddress();
        modelab.setId(String.valueOf(System.currentTimeMillis()));
        modelab.setName("ab-f1-a");
        modelab.setNo(2L);
        modelab.setActive(true);
        modelab.setDetail("ab-f1-b");
        daoAB.insert(modelab);
        
        daoAB.delete(modelab);
    }
}
