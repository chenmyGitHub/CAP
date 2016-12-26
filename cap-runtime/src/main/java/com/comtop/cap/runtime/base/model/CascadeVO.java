/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.util.List;

/**
 * 级联性VO对象
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年9月21日 龚斌
 */
public class CascadeVO {
    
    /**
     * 被关联实体名称(如A一对多关联B，则entityName为B)
     */
    private String entityName;
    
    /**
     * 源实体名称别名
     */
    private String sourceEntityAliasName;
    
    /**
     * 被关联实体名称别名(如A一对多关联B，则entityAliasName为B)
     */
    private String targetEntityAliasName;
    
    /**
     * 因关联关系产生的实体属性名
     */
    private String entityAttName;
    
    /**
     * 当前实体级联的VO集合
     */
    private List<CascadeVO> lstCascadeVO;
    
    //
    // /**
    // * 关联属性字段名（如A一对多关联B，且A中主键为id，则srcFiledName为id）
    // */
    // private String srcFiledName;
    //
    // /**
    // * 关联属性字段名（如A一对多关联B，且B中通过ProjectId字段记录A的主键，则filedName为ProjectId）
    // */
    // private String tarFiledName;
    //
    // /**
    // * 关联关系名（如A一对多关联B，该关联关系英文名为BRelationships）
    // */
    // private String relationShipName;
    
    /**
     * 构造函数
     */
    public CascadeVO() {
    }
    
    /**
     * 构造函数
     * 
     * @param entityName 实体名称
     * @param sourceEntityAliasName 源实体别名
     * @param targetEntityAliasName 目标实体别名
     * @param entityAttName 实体属性名称
     * @param lstCascadeVO 当前实体级联的VO集合
     */
    public CascadeVO(String entityName, String sourceEntityAliasName, String targetEntityAliasName,
        String entityAttName, List<CascadeVO> lstCascadeVO) {
        super();
        this.entityName = entityName;
        this.sourceEntityAliasName = sourceEntityAliasName;
        this.targetEntityAliasName = targetEntityAliasName;
        this.entityAttName = entityAttName;
        this.lstCascadeVO = lstCascadeVO;
    }
    
    /**
     * @return 获取 entityName属性值
     */
    public String getEntityName() {
        return entityName;
    }
    
    /**
     * @param entityName 设置 entityName 属性值为参数值 entityName
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
    
    /**
     * @return 获取 sourceEntityAliasName属性值
     */
    public String getSourceEntityAliasName() {
        return sourceEntityAliasName;
    }
    
    /**
     * @param sourceEntityAliasName 设置 sourceEntityAliasName 属性值为参数值 sourceEntityAliasName
     */
    public void setSourceEntityAliasName(String sourceEntityAliasName) {
        this.sourceEntityAliasName = sourceEntityAliasName;
    }
    
    /**
     * @return 获取 targetEntityAliasName属性值
     */
    public String getTargetEntityAliasName() {
        return targetEntityAliasName;
    }
    
    /**
     * @param targetEntityAliasName 设置 targetEntityAliasName 属性值为参数值 targetEntityAliasName
     */
    public void setTargetEntityAliasName(String targetEntityAliasName) {
        this.targetEntityAliasName = targetEntityAliasName;
    }
    
    /**
     * @return 获取 lstCascadeVO属性值
     */
    public List<CascadeVO> getLstCascadeVO() {
        return lstCascadeVO;
    }
    
    /**
     * @param lstCascadeVO 设置 lstCascadeVO 属性值为参数值 lstCascadeVO
     */
    public void setLstCascadeVO(List<CascadeVO> lstCascadeVO) {
        this.lstCascadeVO = lstCascadeVO;
    }
    
    /**
     * @return 获取 entityAttName属性值
     */
    public String getEntityAttName() {
        return entityAttName;
    }
    
    /**
     * @param entityAttName 设置 entityAttName 属性值为参数值 entityAttName
     */
    public void setEntityAttName(String entityAttName) {
        this.entityAttName = entityAttName;
    }
    
}
