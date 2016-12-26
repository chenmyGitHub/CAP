
package com.comtop.cip.runtime.demo.company.dao;

import com.comtop.cip.common.orm.CommonDAO;
import com.comtop.cip.runtime.demo.company.model.CompanyAndAddress;

/**
 * 
 * CompanyAndAddress DAO
 * 
 * 
 * @author 冯展
 * @since 1.0
 * @version 2014-2-18 冯展
 */
public class CompanyAndAddressDao extends CommonDAO<CompanyAndAddress> {
    
    @Override
    public Object insert(CompanyAndAddress companyAndAddress, String... mappingTables) {
        Object id = super.insert(companyAndAddress, "company", "address");
        return id;
    }
    
    @Override
    public boolean update(CompanyAndAddress companyAndAddress, String... mappingTables) {
        boolean result = super.update(companyAndAddress, "company", "address");
        return result;
    }
    
    @Override
    public boolean delete(CompanyAndAddress companyAndAddress, String... mappingTables) {
        boolean result = super.delete(companyAndAddress, "company", "address");
        return result;
    }
}
