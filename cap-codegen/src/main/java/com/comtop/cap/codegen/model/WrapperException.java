/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.bm.metadata.entity.model.MethodType;
import com.comtop.cap.codegen.model.WrapperEntity.ImportNotifyArgs;
import com.comtop.cap.ptc.login.utils.CapLoginUtil;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * 异常元数据包装类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月21日 龚斌
 */
public final class WrapperException extends Observable {
    
    /** 异常 */
    private final ExceptionVO ex;
    
    /**
     * 构造函数
     * 
     * @param ex 异常
     * @param observer 导入类观察者
     * @param methodType 异常所在方法类型
     */
    WrapperException(final ExceptionVO ex, final Observer observer, final String methodType) {
        super();
        this.ex = ex;
        this.addObserver(observer);
        String strExceptionFullName = ex.getModelPackage() + ".exception." + getExceptionType();
        this.setChanged();
        notifyObservers(this);
        
        this.setChanged();
        notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT, strExceptionFullName, this));
        if (MethodType.BLANK.getValue().equals(methodType)) { // 空方法
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.IMP_FACADE_IMPORT, strExceptionFullName, this));
        }
        if (MethodType.USER_DEFINED_SQL.getValue().equals(methodType) || MethodType.QUERY_MODELING.getValue().equals(methodType)) { // 自定义sql方法 ,查询建模
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, strExceptionFullName, this));
        }
    }
    
    /**
     * 获取异常类型
     * 
     * @return 异常类型
     */
    public String getExceptionType() {
        return StringUtil.capitalize(ex.getEngName());
    }
    
    /**
     * 获取异常所在包路径
     * 
     * 
     * @return 包路径
     */
    public String getPackagePath() {
        return ex.getModelPackage();
    }
    
    /**
     * 获取异常注释
     * 
     * 
     * @return 异常注释
     */
    public String getComment() {
        return ex.getChName();
    }
    
    /**
     * @return 获取异常作者名
     */
    public String getAuthor(){
    	//return ex.getCreaterName();
    	return CapLoginUtil.getCapCurrentUserSession().getBmEmployeeName();
    	//return SystemHelper.getCurUserInfo().getEmployeeName();
    }
    
    /**
     * 
     * @see java.lang.Object#hashCode()
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        final int iPrime = 31;
        int iResult = 1;
        iResult = iPrime * iResult + ((this.getExceptionType() == null) ? 0 : this.getExceptionType().hashCode());
        iResult = iPrime * iResult + ((this.getPackagePath() == null) ? 0 : this.getPackagePath().hashCode());
        return iResult;
    }
    
    /**
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WrapperException objOther = (WrapperException) obj;
        if (this.getExceptionType() == null) {
            if (objOther.getExceptionType() != null) {
                return false;
            }
        } else if (!this.getExceptionType().equals(objOther.getExceptionType())) {
            return false;
        }
        if (this.getPackagePath() == null) {
            if (objOther.getPackagePath() != null) {
                return false;
            }
        } else if (!this.getPackagePath().equals(objOther.getPackagePath())) {
            return false;
        }
        return true;
    }
    
}
