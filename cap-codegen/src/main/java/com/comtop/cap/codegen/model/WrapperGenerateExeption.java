/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.codegen.generate.IWrapper;

/**
 * 新版页面建模JSP包装类
 *
 * @author 郑重
 * @version 2015-6-23 郑重
 */
public class WrapperGenerateExeption implements Observer, IWrapper<ExceptionVO> {
	
	@Override
	public Map<String, Object> wrapper(ExceptionVO ex) {
		WrapperException objWrapperException = new WrapperException(ex, this, null);
		Map<String, Object> objExParam = new HashMap<String, Object>();
		objExParam.put("exception", objWrapperException);
		return objExParam;
	}

	@Override
	public Map<String, Object> wrapper(ExceptionVO ex, String format) {
		return wrapper(ex);
	}

	@Override
	public void update(Observable o, Object arg) {
		//
	}
}
