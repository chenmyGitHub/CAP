/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.comtop.bpms.common.model.NodeExtendAttributeInfo;
import comtop.org.directwebremoting.annotations.DataTransferObject;

/**
 * FIXME 类注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
 * 
 * 
 * @author 作者
 * @since 1.0
 * @version 2015-6-8 作者
 */
@DataTransferObject
public class WorkflowNodeConfigVO implements Serializable {
    
    /** FIXME */
    private static final long serialVersionUID = 6843375568393508009L;
    
    /** 常用的流程节点扩展属性 */
    public static final List<String> NODE_CONFIG_CONSTANTS = new ArrayList<String>();
    
    static {
        NODE_CONFIG_CONSTANTS.add("viewSelectDept");
        NODE_CONFIG_CONSTANTS.add("opinionDisplay");
        NODE_CONFIG_CONSTANTS.add("opinionRequired");
        NODE_CONFIG_CONSTANTS.add("limitOpinionValue");
        NODE_CONFIG_CONSTANTS.add("singleSelect");
        NODE_CONFIG_CONSTANTS.add("smsColHide");
        NODE_CONFIG_CONSTANTS.add("emailColHide");
        NODE_CONFIG_CONSTANTS.add("special");
        NODE_CONFIG_CONSTANTS.add("backSpecial");
        NODE_CONFIG_CONSTANTS.add("backOpinionRequired");
        NODE_CONFIG_CONSTANTS.add("chooseFromUserDeptComponet");
        
    }
    
    /** 是否显示部门选择组件 */
    private Boolean viewSelectDept;
    
    /** 是否显示结论选择和意见输入 */
    private boolean isOpinionDisplay = false;
    
    /** 回退操作时是否必须选择不同意, (决定是否限制结论值（如：回退时，是否必须是不同意，发送时必须是同意）限制为true，反之false) */
    private boolean isLimitOpinionValue = true;
    
    /** 意见是否必须有值,( 意见是否必填，必填为true，非必填为false) */
    private boolean isOpinionRequired = false;
    
    /** 多流程节点时是单选还是多选，单选为true，多选为false */
    private boolean isSingleSelect = true;
    
    /** 是否显示短信列 */
    private boolean isSmsColHide = true;
    
    /** 是否显示邮件列 */
    private boolean isEmailColHide = true;
    
    /** 是否指定操作 */
    private boolean isSpecial = false;
    
    /** 是否指定回退 */
    private boolean backSpecial = false;
    
    /** 回退意见是否必填 */
    private boolean backOpinionRequired = true;
    
    /** 是否调用部门人员选择组件进行人员选择 */
    private boolean isChooseFromUserDeptComponet = false;
    
    /** 节点上的扩展属性集合 */
    private Map<String, String> extendsAttrs;
    
    /**
     * @return 获取 extendsAttrs属性值
     */
    public Map<String, String> getExtendsAttrs() {
        return extendsAttrs;
    }
    
    /**
     * @param extendsAttrs 设置 extendsAttrs 属性值为参数值 extendsAttrs
     */
    public void setExtendsAttrs(Map<String, String> extendsAttrs) {
        this.extendsAttrs = extendsAttrs;
    }
    
    /**
     * @return 获取 backSpecial属性值
     */
    public boolean isBackSpecial() {
        return backSpecial;
    }
    
    /**
     * @param backSpecial 设置 backSpecial 属性值为参数值 backSpecial
     */
    public void setBackSpecial(boolean backSpecial) {
        this.backSpecial = backSpecial;
    }
    
    /**
     * @return 获取 backOpinionRequired属性值
     */
    public boolean isBackOpinionRequired() {
        return backOpinionRequired;
    }
    
    /**
     * @param backOpinionRequired 设置 backOpinionRequired 属性值为参数值 backOpinionRequired
     */
    public void setBackOpinionRequired(boolean backOpinionRequired) {
        this.backOpinionRequired = backOpinionRequired;
    }
    
    /**
     * @return 获取 isSpecial属性值
     */
    public boolean isSpecial() {
        return isSpecial;
    }
    
    /**
     * @param isSpecial 设置 isSpecial 属性值为参数值 isSpecial
     */
    public void setSpecial(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }
    
    /**
     * @return 获取 isChooseFromUserDeptComponet属性值
     */
    public boolean isChooseFromUserDeptComponet() {
        return isChooseFromUserDeptComponet;
    }
    
    /**
     * @param isChooseFromUserDeptComponet 设置 isChooseFromUserDeptComponet 属性值为参数值 isChooseFromUserDeptComponet
     */
    public void setChooseFromUserDeptComponet(boolean isChooseFromUserDeptComponet) {
        this.isChooseFromUserDeptComponet = isChooseFromUserDeptComponet;
    }
    
    /**
     * @param isOpinionDisplay 设置 isOpinionDisplay 属性值为参数值 isOpinionDisplay
     */
    public void setOpinionDisplay(boolean isOpinionDisplay) {
        this.isOpinionDisplay = isOpinionDisplay;
    }
    
    /**
     * @param isLimitOpinionValue 设置 isLimitOpinionValue 属性值为参数值 isLimitOpinionValue
     */
    public void setLimitOpinionValue(boolean isLimitOpinionValue) {
        this.isLimitOpinionValue = isLimitOpinionValue;
    }
    
    /**
     * @param isOpinionRequired 设置 isOpinionRequired 属性值为参数值 isOpinionRequired
     */
    public void setOpinionRequired(boolean isOpinionRequired) {
        this.isOpinionRequired = isOpinionRequired;
    }
    
    /**
     * @param isSingleSelect 设置 isSingleSelect 属性值为参数值 isSingleSelect
     */
    public void setSingleSelect(boolean isSingleSelect) {
        this.isSingleSelect = isSingleSelect;
    }
    
    /**
     * @param isSmsColHide 设置 isSmsColHide 属性值为参数值 isSmsColHide
     */
    public void setSmsColHide(boolean isSmsColHide) {
        this.isSmsColHide = isSmsColHide;
    }
    
    /**
     * @param isEmailColHide 设置 isEmailColHide 属性值为参数值 isEmailColHide
     */
    public void setEmailColHide(boolean isEmailColHide) {
        this.isEmailColHide = isEmailColHide;
    }
    
    /*** 节点扩展属性集合 */
    private NodeExtendAttributeInfo[] nodeExtendAttributeInfo;
    
    /**
     * @return 获取 nodeExtendAttributeInfo属性值
     */
    public NodeExtendAttributeInfo[] getNodeExtendAttributeInfo() {
        return nodeExtendAttributeInfo;
    }
    
    /**
     * @param nodeExtendAttributeInfo 设置 nodeExtendAttributeInfo 属性值为参数值 nodeExtendAttributeInfo
     */
    public void setNodeExtendAttributeInfo(NodeExtendAttributeInfo[] nodeExtendAttributeInfo) {
        this.nodeExtendAttributeInfo = nodeExtendAttributeInfo;
    }
    
    /**
     * @return 获取 isOpinionDisplay属性值
     */
    public boolean isOpinionDisplay() {
        return isOpinionDisplay;
    }
    
    /**
     * @return 获取 isLimitOpinionValue属性值
     */
    public boolean isLimitOpinionValue() {
        return isLimitOpinionValue;
    }
    
    /**
     * @return 获取 isOpinionRequired属性值
     */
    public boolean isOpinionRequired() {
        return isOpinionRequired;
    }
    
    /**
     * @return 获取 isSingleSelect属性值
     */
    public boolean isSingleSelect() {
        return isSingleSelect;
    }
    
    /**
     * @return 获取 isSmsColHide属性值
     */
    public boolean isSmsColHide() {
        return isSmsColHide;
    }
    
    /**
     * @return 获取 isEmailColHide属性值
     */
    public boolean isEmailColHide() {
        return isEmailColHide;
    }
    
    /**
     * @return the viewSelectDept
     */
    public Boolean getViewSelectDept() {
        return viewSelectDept;
    }
    
    /**
     * @param viewSelectDept the viewSelectDept to set
     */
    public void setViewSelectDept(Boolean viewSelectDept) {
        this.viewSelectDept = viewSelectDept;
    }
    
}
