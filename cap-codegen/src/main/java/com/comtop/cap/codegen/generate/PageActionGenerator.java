/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.List;

import com.comtop.cap.bm.metadata.page.desinger.model.PageVO;

/**
 * 代码生成入口类
 *
 * @author 郑重
 * @version 2015-6-18 郑重
 */
public class PageActionGenerator {
    
    /**
     * 构造函数
     */
    private PageActionGenerator() {
        super();
    }
    
    /**
     * 生成当前包下layerName类型的代码
     * 
     * @param packageName 包名
     */
    public static void generateByPackageNameAndLayerName(String packageName) {
        GenerateCode.generateByPackageName(packageName, new NewPageProcess<PageVO>());
    }
    
    /**
     * 根据元数据ID生成layerName的代码
     * 
     * @param packageName 包名
     * @param id 元数据主键
     * @param genType 代码生成模式，0：生成所有代码;1：仅生成前端代码;
     */
    public static void generateByIdAndLayerName(String packageName, String id, String genType) {
        NewPageProcess<PageVO> objNewPageProcess = new NewPageProcess<PageVO>();
        int iGen = 0;
        if (genType != null) {
            iGen = Integer.parseInt(genType);
        }
        objNewPageProcess.setGenType(iGen);
        GenerateCode.generateByIdAndLayerName(packageName, id, objNewPageProcess);
    }
    
    /**
     * 根据元数据ID List生成layerName的代码
     * 
     * @param packageName 包名
     * @param ids 元数据主键集合
     */
    public static void generateByIdListAndLayerName(String packageName, List<String> ids) {
        GenerateCode.generateByIdListAndLayerName(packageName, ids, new NewPageProcess<PageVO>());
    }
}
