/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.entity.model.DataTypeVO;
import com.comtop.cap.bm.metadata.entity.model.MethodType;
import com.comtop.cap.bm.metadata.entity.model.ParameterVO;
import com.comtop.cap.codegen.model.WrapperEntity.ImportNotifyArgs;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * 方法参数封装
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class WrapperParameter extends Observable {
    
    /** 参数 */
    private final ParameterVO param;
    
    /** 参数类型 */
    private final String type;
    
    /** 参数所在方法类型 */
    private final String methodType;
    
    /** 参数类型 */
    private String fullClassName;
    
    /**
     * 构造函数
     * 
     * @param methodType 方法类型
     * @param param 参数
     * @param observer 导入类观察者
     */
    WrapperParameter(final String methodType, final ParameterVO param, final Observer observer) {
        super();
        this.methodType = methodType;
        this.param = param;
        this.addObserver(observer);
        this.type = wrapperParamterType();
    }
    
    /**
     * 包装参数类型
     * 
     * @return 参数类型
     * 
     */
    private String wrapperParamterType() {
        DataTypeVO objParamDataType = this.param.getDataType();
        
        //查询重写方法不生成代码
    	if(MethodType.QUERY_EXTEND.getValue().equals(methodType)){
    		return objParamDataType.readDataTypeName();
    	}
        
        this.fullClassName = objParamDataType.readDataTypeFullName();
        // 更新facade层的导入类
        List<String> lstClassName = objParamDataType.readImportDateType();
        for (String strClassName : lstClassName) {
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT, strClassName, this));
            if (MethodType.BLANK.getValue().equals(this.methodType)) { // 空方法，则实现类也要添加导入类
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.IMP_FACADE_IMPORT, strClassName, this));
            } else if (MethodType.USER_DEFINED_SQL.getValue().equals(this.methodType)) {
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, strClassName, this));
			} else if (MethodType.QUERY_MODELING.getValue().equals(this.methodType)) {
				this.setChanged();
				notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, strClassName, this));
			}
        }
        return objParamDataType.readDataTypeName();
    }
    
    /**
     * 获取方法参数类型
     * 
     * @return 方法参数类型
     */
    public String getParameterType() {
        return this.type;
    }
    
    /**
     * 获取方法参数类型
     * 
     * @return 方法参数类型
     */
    public String getFullClassName() {
        return this.fullClassName;
    }
    
    /**
     * 获取方法参数别名
     * 
     * @return 参数别名
     */
    public String getAlias() {
        return StringUtil.uncapitalize(param.getEngName());
    }
    
    /**
     * 获取方法参数注释
     * 
     * @return 参数注释
     */
    public String getComment() {
        return this.param.getChName();
    }
}
