/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.codegen.convert;

import com.comtop.cap.bm.metadata.database.dbobject.util.DBType;
import com.comtop.cap.bm.metadata.database.dbobject.util.DBTypeAdapter;

/**
 * 数据库类型转换工厂
 * 
 * @author 许畅
 * @since JDK1.7
 * @version 2016年12月5日 许畅 新建
 */
public class DataBaseConvertFactory {

	/**
	 * 构造方法
	 */
	private DataBaseConvertFactory() {
		super();
	}

	/**
	 * 根据首选项配置获取对应的数据库类型实例
	 * 
	 * @return IDataBaseConvert
	 */
	public static IDataBaseConvert getInstance() {
		DBType dbType = DBTypeAdapter.getDBType();
		if (DBType.MYSQL.getValue().equals(dbType.getValue()))
			return MySQLConverter.getInstance();

		if (DBType.ORACLE.getValue().equals(dbType.getValue()))
			return OracleConverter.getInstance();

		return OracleConverter.getInstance();
	}

}
