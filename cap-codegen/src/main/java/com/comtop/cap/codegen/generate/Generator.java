/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.generate;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cap.bm.metadata.entity.model.EntitySource;
import com.comtop.cap.bm.metadata.entity.model.EntityType;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.sysmodel.model.CapPackageVO;
import com.comtop.cap.bm.metadata.sysmodel.utils.CapSystemModelUtil;
import com.comtop.cap.codegen.config.LayerConfig;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * 代码生成器
 * 
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 * @version 2016年07月11日 许畅 修改
 */
public final class Generator extends AbstractGenerator<EntityVO> {
    
    /** 生成所有 */
    public static final int GEN_ALL = 0;
    
    /** 生成VO */
    public static final int GEN_VO = 1;
    
    /** 生成业务 */
    public static final int GEN_BIZ = 2;
    
    /** 生成SQL */
    public static final int GEN_SQL = 3;
    
    /** 代码生成所有层次 ：包括facade、service、vo及sql */
    public static final List<String> ALL_LAYERS_ID = new ArrayList<String>();
    
    /** 代码生成实体层次 ：包括vo及sql */
    public static final List<String> VO_LAYERS_ID = new ArrayList<String>();
    
    /** 代码生成数据层次 ：只包括vo */
    public static final List<String> DATA_LAYERS_ID = new ArrayList<String>();
    
    /** 代码生成业务层次 ：包括facade、service、sql */
    public static final List<String> BIZ_LAYERS_ID = new ArrayList<String>();
    
    /** 代码生成SQL层次 ：包括sql */
    public static final List<String> SQL_LAYERS_ID = new ArrayList<String>();
    
    static {
        SQL_LAYERS_ID.add("base_sql");
        SQL_LAYERS_ID.add("ext_sql");
        
        BIZ_LAYERS_ID.add("abs_facade");
        BIZ_LAYERS_ID.add("facade");
        BIZ_LAYERS_ID.add("abs_appservice");
        BIZ_LAYERS_ID.add("appservice");
        BIZ_LAYERS_ID.addAll(SQL_LAYERS_ID);
        
        DATA_LAYERS_ID.add("vo");
        
        VO_LAYERS_ID.addAll(DATA_LAYERS_ID);
        VO_LAYERS_ID.addAll(SQL_LAYERS_ID);
        
        ALL_LAYERS_ID.addAll(BIZ_LAYERS_ID);
        ALL_LAYERS_ID.addAll(VO_LAYERS_ID);
    }
    
    /** 生成代码范围 */
    private int genType;
    
    /**
     * @param genType the genType to set
     */
    public void setGenType(int genType) {
        this.genType = genType;
    }
    
    /**
     * 构造函数
     */
    public Generator() {
        super();
    }
    
    @Override
    public List<EntityVO> generateByPackageName(String packageName) {
        return null;
    }
    
    @Override
    public EntityVO generateById(String id) {
        return null;
    }
    
    @Override
    public List<EntityVO> generateByIdList(List ids) {
        return null;
    }
    
    @Override
    public List<String> getBuilderSourcePath() {
        List<String> lstPath = new ArrayList<String>();
        lstPath.add("");
        return lstPath;
    }
    
    @Override
    protected List<LayerConfig> filerDefultLayerConfig(List<LayerConfig> lstDefaultConfig) {
        List<String> lagers = null;
        // 生成代码
        switch (genType) {
            case GEN_ALL:
                lagers = Generator.ALL_LAYERS_ID;
                break;
            case GEN_VO:
                lagers = Generator.VO_LAYERS_ID;
                break;
            case GEN_BIZ:
                lagers = Generator.BIZ_LAYERS_ID;
                break;
            case GEN_SQL:
                lagers = Generator.SQL_LAYERS_ID;
                break;
            default:
                lagers = Generator.ALL_LAYERS_ID;
                break;
        }
        if (lagers == null) {
            return lstDefaultConfig;
        }
        List<LayerConfig> lstResult = new ArrayList<LayerConfig>();
        for (LayerConfig layerConfig : lstDefaultConfig) {
            if (lagers.contains(layerConfig.getId())) {
                lstResult.add(layerConfig);
            }
        }
        return lstResult;
    }
    
    @Override
    public boolean isGenerateCodeOnLayer(EntityVO t, LayerConfig layer) {
        // 已有实体不做处理，新元数据可通过entitySource处理，旧元数据仍然通过entityType来处理
        if (EntitySource.EXIST_ENTITY_INPUT.getValue().equals(t.getEntitySource()) || "exist_entity".equals(t.getEntityType())) {
            return false;
        }
        if (EntityType.DATA_ENTITY.getValue().equals(t.getEntityType())) {
            return Generator.DATA_LAYERS_ID.contains(layer.getId());
        }
        return true;
    }
    
    /**
     * 代码工程路径 <br>
     * 如果应用信息中java源代码路径为空则取首选项中项目工程路径 <br>
     * 应用信息有java源代码则取应用信息中值
     * 
     * @return java代码生成路径
     */
    @Override
    public String getProjectDir(EntityVO entityVO) {
        CapPackageVO capPackageVO = CapSystemModelUtil.queryCapPackageByEntity(entityVO);
        
        if (capPackageVO == null || StringUtil.isBlank(capPackageVO.getJavaCodePath())) {
            return super.getProjectDir(entityVO);
        }
        
        return capPackageVO.getJavaCodePath();
    }
    
    /**
     * 查询模块配置是否配置代码生成路径
     * 
     * @return 是否生成代码模块化
     */
    @Override
    public boolean isGenerateCodeModule(EntityVO entityVO) {
        CapPackageVO capPackageVO = CapSystemModelUtil.queryCapPackageByEntity(entityVO);
        
        if (capPackageVO == null || StringUtil.isBlank(capPackageVO.getJavaCodePath())) {
            return false;
        }
        
        return true;
    }
    
}
