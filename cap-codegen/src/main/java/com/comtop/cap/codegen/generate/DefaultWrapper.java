/******************************************************************************
 * Copyright (C) 2011 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.HashMap;
import java.util.Map;

import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.codegen.model.WrapperEntity;
import com.comtop.cap.ptc.login.model.CapLoginVO;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;

/**
 * 
 * 默认数据包装器
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class DefaultWrapper implements IWrapper<EntityVO> {
    
    /**
     * 包装实体，以便代码生成时使用
     * 
     * @see com.comtop.cap.codegen.generate.IWrapper#wrapper(java.lang.Object)
     */
    @Override
    public Map<String, Object> wrapper(final EntityVO entity) {
        WrapperEntity objEntity = new WrapperEntity(entity, "{0}VO");
        Map<String, Object> objParam = new HashMap<String, Object>(1000);
        objParam.put("entity", objEntity);
        objParam.put("exceptions", objEntity.getExceptions());
        this.wrapperJavadoc(objParam);
        return objParam;
    }
    
    /**
     * 包装实体，以便代码生成时使用
     * 
     * @see com.comtop.cap.codegen.generate.IWrapper#wrapper(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Map<String, Object> wrapper(final EntityVO entity, String format) {
        WrapperEntity objEntity = new WrapperEntity(entity, format);
        Map<String, Object> objParam = new HashMap<String, Object>(1000);
        objParam.put("entity", objEntity);
        objParam.put("exceptions", objEntity.getExceptions());
        this.wrapperJavadoc(objParam);
        return objParam;
    }
    
    /**
     * 注释信息
     *
     * @param objParam JSON对象
     */
    private void wrapperJavadoc(Map<String, Object> objParam) {
        CapLoginVO objCapLoginVO = CapLoginUtil.getCapCurrentUserSession();
        if (null == objCapLoginVO) {
            throw new RuntimeException("未能从Session中获取当前登录用户的信息，可能是Session已失效，请重新登录后，再尝试执行代码生成");
        }
        objParam.put("author", objCapLoginVO.getBmEmployeeName());
    }
}
