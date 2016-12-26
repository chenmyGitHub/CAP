/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.convert;

import com.comtop.cap.bm.metadata.entity.model.Wildcard;

/**
 * 数据库类型转换接口
 * 
 * @author 许畅
 * @since JDK1.7
 * @version 2016年12月5日 许畅 新建
 */
public interface IDataBaseConvert {

	/**
	 * 转换为对应的数据库特殊语法
	 * 
	 * @param wildcard
	 *            通配符
	 * @return 对应的数据库特殊语法
	 */
	String convert(Wildcard wildcard);

	/**
	 * 转换为对应的数据库特殊语法
	 * 
	 * @param wildcard
	 *            通配符
	 * @return 对应的数据库特殊语法
	 */
	String convert(String wildcard);

}
