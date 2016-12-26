/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cip.jodd.petite.PetiteContainer;

/**
 * Bean工具类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-1-27 李忠文
 */
public class AppBeanUtil {
    
	/** 日誌 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanUtil.class);
	
    /**
     * 通过类的类型获取bean实例
     * 
     * @param <T> 泛型
     * @param type bean的类型
     * @return bean instance
     */
    public static <T> T getBean(Class<T> type) {
        T ins = null;
//        try {
//            ins = AppContext.getBean(type);
//        } catch (Exception e) {
//            // do noting....
//        }
//        if (ins == null) {
            try {
                ins = com.comtop.top.core.jodd.AppContext.getBean(type);
            } catch (Exception e) {
            	LOGGER.debug("error", e);
                // do noting....
            }
//        }
        com.comtop.top.core.jodd.AppCore appCore = com.comtop.top.core.jodd.AppCore.getInstance();
        if (!appCore.isStarted()) {
            appCore.startJoddNoDB();
            appCore.startJoddDB();
        }
        PetiteContainer madpc = appCore.getPetite();
        if (ins == null && madpc != null) {
            String name = madpc.resolveBeanName(type);
            madpc.registerPetiteBean(type, name, null, null, false);
//            if (AppCore.getInstance().isStarted()) {
//                AppCore.getInstance().getPetite().registerPetiteBean(type, name, null, null, false);
//            }
        }
        
        return com.comtop.top.core.jodd.AppContext.getBean(type);
    }
}
