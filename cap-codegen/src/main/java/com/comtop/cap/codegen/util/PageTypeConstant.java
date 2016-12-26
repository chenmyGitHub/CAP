/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.codegen.util;

/**
 * 录入页面类型常量
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年4月4日 许畅 新建
 */
public class PageTypeConstant {
	
	/**
     * 构造函数
     */
    private PageTypeConstant() {
    }
    
    /** 录入的模板或者普通页面，默认为1 */
    public final static int ENTRY_PAGE = 1;
    
    /** 录入的自定义页面， */
    public final static int CUSTOM_PAGE = 2;

}
