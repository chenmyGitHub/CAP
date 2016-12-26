/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.runtime.juel.ext;

import com.comtop.cap.runtime.component.facade.AutoGenNumberFacade;
import com.comtop.cap.runtime.juel.annotation.JuelExtMethod;
import com.comtop.cap.runtime.core.AppBeanUtil;

/**
 * juel扩展序列号管理工具类
 * @author 李小强
 * @since 1.0
 * @version 2016-11-9 李小强
 */
public class JuelSequenceUtils {

    /***
     * 获取序列
     * @param seqKey 序列号编码
     * @param suffixLenght 序列长度
     * @param firstNo 序列起始值
     * @param step 序列递增值
     * @return 序列编码 
     */
	@JuelExtMethod(localName="seq")
    public static String getSeq(String seqKey,int suffixLenght,int firstNo,int step){
    	AutoGenNumberFacade service = AppBeanUtil.getBean(AutoGenNumberFacade.class);
    	return service.getCodeSuffixBySeq(seqKey, suffixLenght, firstNo, step);
    }
}
