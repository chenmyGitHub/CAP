/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.comtop.cap.bm.metadata.common.storage.CacheOperator;
import com.comtop.cap.bm.metadata.entity.model.AttributeType;
import com.comtop.cap.bm.metadata.entity.model.DataTypeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityAttributeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityRelationshipVO;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.RelatioMultiple;
import com.comtop.cap.codegen.model.WrapperEntity.ImportNotifyArgs;
import com.comtop.cap.codegen.util.CapCodegenConstant;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * 
 * 实体属性包装类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class WrapperAttribute extends Observable {
    
    /** 流程关联属性 */
    private final static Set<String> PROCESS_ASS_ATT = new HashSet<String>(Arrays.asList("processInsId", "flowState"));
    
    /** 实体属性 */
    private final EntityAttributeVO attribute;
    
    /** 实体属性关联关系 */
    private final List<EntityRelationshipVO> lstRelation;
    
    /** 是否为覆盖父类属性 */
    private final boolean hasOverride;
    
    /** 属性类型 */
    private final String type;
    
    /** 字段注解 */
    private List<String> annotations;
    
    /** 默认级联查询表达式 */
    private String cascadeExpr;
    
    /** 是否存在查询表达式 */
    private boolean hasQueryExpr;
    
    /** 业务查询表达式 */
    private String queryExpr;
    
    /** 范围查询表达式1 */
    private String queryRangeExpr1;
    
    /** 范围查询表达式2 */
    private String queryRangeExpr2;
    
    /**
     * 
     * 构造函数
     * 
     * @param attribute 实体属性vo
     * @param lstRelation 实体属性关联关系
     * @param observer 观察者对象
     * @param bProFlag 是否关联流程标识位
     */
    public WrapperAttribute(EntityAttributeVO attribute, List<EntityRelationshipVO> lstRelation, Observer observer,
        boolean bProFlag) {
        super();
        if (observer != null) {
            this.addObserver(observer);
        }
        this.attribute = attribute;
        this.lstRelation = lstRelation;
        this.type = wrapperAttributeType();
        this.wrapperAnnotation();
        this.hasOverride = bProFlag && PROCESS_ASS_ATT.contains(this.attribute.getEngName());
        this.parserQueryExpr(observer);
    }
    
    /**
     * 包装属性类型
     * 
     * @return 属性类型
     */
    private String wrapperAttributeType() {
        DataTypeVO objAttDataType = this.attribute.getAttributeType();
        // 更新VO的导入类
        List<String> lstClassName = objAttDataType.readImportDateType();
        
        for (String strClassName : lstClassName) {
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.VO_IMPORT, strClassName, this));
        }
        return objAttDataType.readDataTypeName();
    }
    
    /**
     * 包装属性上的注解
     * 
     */
    private void wrapperAnnotation() {
        annotations = new ArrayList<String>();
        if (this.attribute.isPrimaryKey()) {
            annotations.add("@Id");
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.VO_IMPORT, CapCodegenConstant.PERSISTENCE_ID_PATH,
                this));
        }
        // 关联数据库字段的注解
        String strColumnAnnotaion = getColumnAnnotation();
        if (StringUtil.isNotBlank(strColumnAnnotaion)) {
            annotations.add(strColumnAnnotaion);
        }
        // 关联关系的注解
        String strAssociateAnnotation = getAssociateAnnotation();
        if (StringUtil.isNotBlank(strAssociateAnnotation)) {
            annotations.add(strAssociateAnnotation);
        }
        // 默认值的注解
        String strDefaultValueAnnotation = getDefaultValueAnnotation();
        if (StringUtil.isNotBlank(strDefaultValueAnnotation)) {
            annotations.add(strDefaultValueAnnotation);
        }
    }
    
    /**
     * 获得属性默认值的注解
     * 
     * @return 默认值注解
     */
    private String getDefaultValueAnnotation() {
        String _defaultValue = this.attribute.getDefaultValue();
        // 属性无默认值
        if (StringUtil.isBlank(_defaultValue)) {
            return null;
        }
        this.setChanged();
        notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.VO_IMPORT, CapCodegenConstant.DEFAULT_VALUE_ANNO_PATH,
            this));
        return "@DefaultValue(value=\"" + _defaultValue + "\")";
    }
    
    /**
     * 包装数据库字段注解
     * 
     * @return 字段注解
     */
    private String getColumnAnnotation() {
        if (StringUtil.isBlank(this.attribute.getDbFieldId())) { // 无绑定数据库字段
            return null;
        }
        StringBuilder objBuilder = new StringBuilder();
        objBuilder.append("@Column(name = \"");
        objBuilder.append(this.attribute.getDbFieldId());
        objBuilder.append("\"");
        String strLengthAnnotation = this.getLengthAnnotation();
        if (StringUtil.isNotBlank(strLengthAnnotation)) {
            objBuilder.append(strLengthAnnotation);
        }
        objBuilder.append(")");
        this.setChanged();
        notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.VO_IMPORT, CapCodegenConstant.PERSISTENCE_COLUMN_PATH,
            this));
        return objBuilder.toString();
    }
    
    /**
     * 包装关联字段注解
     * 
     * @return 字段注解
     */
    private String getAssociateAnnotation() {
        String relationId = this.attribute.getRelationId();
        if (StringUtil.isBlank(relationId)) { // 无关联关系
            return null;
        }
        // 遍历关联关系，找到产生当前属性的关联关系
        EntityRelationshipVO objAssRelation = null;
        for (EntityRelationshipVO objRelation : lstRelation) {
            if (relationId.equals(objRelation.getRelationId())) {
                objAssRelation = objRelation;
                break;
            }
        }
        if (objAssRelation == null) {
            return null;
        }
        String multiple = objAssRelation.getMultiple(); // 实体属性多重性
        String srcAssAttName = objAssRelation.getSourceField();
        String tgtAssAttName = objAssRelation.getTargetField();
        StringBuilder objBuilder = new StringBuilder();
        objBuilder.append("@AssociateAttribute(multiple = \"");
        objBuilder.append(multiple);
        objBuilder.append("\"");
        objBuilder.append(", associateFieldName = \"");
        if (RelatioMultiple.ONE_MANY.getValue().equals(multiple)) { // 1对多
            objBuilder.append(tgtAssAttName);
            objBuilder.append("\"");
        } else if (RelatioMultiple.ONE_ONE.getValue().equals(multiple)
            || RelatioMultiple.MANY_ONE.getValue().equals(multiple)) { // 1对1或或多对1
            objBuilder.append(srcAssAttName);
            objBuilder.append("\"");
        } else if (RelatioMultiple.MANY_MANY.getValue().equals(multiple)) { // 多对多
            objBuilder
                .append(objAssRelation.getAssociateSourceField() + ":" + objAssRelation.getAssociateTargetField());
            objBuilder.append("\"");
            objBuilder.append(", associateEntityAliasName = \"");
            EntityVO entity = (EntityVO) CacheOperator.readById(objAssRelation.getAssociateEntityId());
            String entityAliasName = StringUtil.isNotBlank(entity.getAliasName()) ? entity.getAliasName() : "";
            objBuilder.append(entityAliasName);
            objBuilder.append("\"");
            objBuilder.append(", associateEntityId = \"");
            objBuilder.append(objAssRelation.getAssociateEntityId());
            objBuilder.append("\"");
        } else {
            return null;
        }
        objBuilder.append(")");
        this.setChanged();
        notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.VO_IMPORT, CapCodegenConstant.ASSOCIATE_ATT_PATH, this));
        return objBuilder.toString();
    }
    
    /**
     * @return 获取hasOverride属性值
     */
    public boolean isHasOverride() {
        return this.hasOverride;
    }
    
    /**
     * 获取方法的 Java语言修饰符
     * 
     * @return 访问级别
     */
    public String getModifer() {
        return this.attribute.getAccessLevel();
    }
    
    /**
     * 获取实体属性类型
     * 
     * @return 实体属性类型
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * @return 获取参数别名
     */
    public String getAlias() {
        return StringUtil.uncapitalize(this.attribute.getEngName());
    }
    
    /**
     * 获取持久化注解
     * 
     * @return 持久化注解
     */
    public List<String> getAnnotations() {
        return this.annotations;
    }
    
    /**
     * 获取字段注释
     * 
     * @return 注释
     */
    public String getComment() {
        String strComment = StringUtil.isNotBlank(this.attribute.getChName()) ? this.attribute.getChName()
            : this.attribute.getDescription();
        return StringUtil.isBlank(strComment) ? this.attribute.getEngName() : strComment;
    }
    
    /**
     * 获取字段默认值
     * 
     * @return 默认值
     */
    public String getDefaultValue() {
        return this.attribute.getDefaultValue();
    }
    
    /**
     * @return 获取set方法名称
     */
    public String getSetMethodName() {
        return "set" + StringUtil.capitalize(this.attribute.getEngName());
    }
    
    /**
     * @return 获取get方法名称
     */
    public String getGetMethodName() {
        return "get" + StringUtil.capitalize(this.attribute.getEngName());
    }
    
    /**
     * @return 获取属性上长度和精度注解
     */
    private String getLengthAnnotation() {
        StringBuffer bfLength = new StringBuffer();
        int iLength = this.attribute.getAttributeLength();
        if (iLength != 0) {
            if ("String".equals(this.attribute.getAttributeType().getType())) { // 字符串类型 ，注解上用length来表示长度
                bfLength.append(",length=" + iLength);
            }
        }
        String precision = this.attribute.getPrecision();
        if (StringUtil.isNotBlank(precision) && StringUtil.isNotBlank(precision.trim())) {
            // 注解上用precision来表示长度
            bfLength.append(",precision=" + precision.trim());
            
        }
        return bfLength.toString();
    }
    
    /**
     * 解析查询表达式
     * 
     * @param observer 包装实体
     */
    private void parserQueryExpr(Observer observer) {
        String attName = this.attribute.getEngName(); // 属性名
        String attFullTypeName = this.attribute.getAttributeType().readDataTypeFullName();
        boolean isStringType = false;
        String strTagHead = "";
        if (AttributeType.STRING.getFullName().equals(attFullTypeName)) { // 字符串类型
            strTagHead = "<if test = \"" + attName + " != null and " + attName + " != \'\' \"> \n\t\t";
            isStringType = true;
        } else { // 非字符串类型
            strTagHead = "<if test = \"" + attName + " != null\"> \n\t\t";
        }
        // 初始化级联查询表达式
        if (StringUtil.isNotBlank(this.attribute.getDbFieldId())) {
            String dbFieldName = this.attribute.getDbFieldId(); // 属性对应的字段名
            this.cascadeExpr = strTagHead + "AND (T1." + dbFieldName + " = #{" + attName + "} )  \n \t</if>";
        }
        
        if (this.attribute.isQueryField()) { // 查询字段
            if ("11".equals(this.attribute.getQueryMatchRule())) { // 查询规则为“范围查询”
                parserRangeQueryExpr(isStringType);
            } else {
				if (StringUtil.isEmpty(this.attribute.getDbFieldId()))
					return;
            	
                // 初始化业务查询表达式
                String expression = this.attribute.getQueryExpr();
                if (expression != null && StringUtil.isNotBlank(expression.trim())) {
                    expression = expression.trim();
                    if (expression.indexOf(">") != -1 || expression.indexOf("<") != -1) {
                        expression = "<![CDATA[ " + expression + "]]> ";
                    }
                    this.queryExpr = strTagHead + "AND (" + expression + ")  \n \t</if>";
                }
            }
        }
    }
    
    /**
     * 解析范围查询的表达式
     * 
     * 
     * @param isStringType 属性是否是String类型
     */
    private void parserRangeQueryExpr(boolean isStringType) {
        String _rang_1 = this.attribute.getQueryRange_1();
        if (_rang_1 != null && StringUtil.isNotBlank(_rang_1.trim())) {
            this.queryRangeExpr1 = parserRangeQueryExpr(isStringType, _rang_1);
        }
        String _rang_2 = this.attribute.getQueryRange_2();
        if (_rang_2 != null && StringUtil.isNotBlank(_rang_2.trim())) {
            this.queryRangeExpr2 = parserRangeQueryExpr(isStringType, _rang_2);
        }
    }
    
    /**
     * 具体解析范围查询的表达式
     *
     * @param isStringType 当前属性是否是String类型
     * @param range 范围表达式字符串
     * @return 拼好的mybatis语法的sql
     */
    private String parserRangeQueryExpr(boolean isStringType, String range) {
        String strTagHead = "";
        if (StringUtil.isNotBlank(range.trim())) {
            String attributeName = matherAttributeName(range.trim());
            if (StringUtil.isNotBlank(attributeName)) {
                if (isStringType) {
                    strTagHead = "<if test = \"" + attributeName + " != null and " + attributeName
                        + "!= \'\' \"> \n\t\t";
                } else {
                    strTagHead = "<if test = \"" + attributeName + " != null\"> \n\t\t";
                }
                return strTagHead + "AND (<![CDATA[ " + range.trim() + "]]>)  \n \t</if>";
            }
        }
        return null;
    }
    
    /**
     * 从传过来的参数中匹配属性名称
     * 
     * @param range mybatis语法的sql语句
     * @return 匹配到的名称
     */
    private String matherAttributeName(String range) {
        if (StringUtil.isNotBlank(range)) {
            Pattern attrPattern = Pattern.compile("[#$][{](.+)[}]");
            Matcher matcher = attrPattern.matcher(range);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        }
        return null;
    }
    
    /**
     * @return 获取cascadeExpr级联查询表达式属性值
     */
    public String getCascadeExpr() {
        return cascadeExpr;
    }
    
    /**
     * @return 获取 hasQueryExpr属性值
     */
    public boolean isHasQueryExpr() {
        return hasQueryExpr;
    }
    
    /**
     * @return 获取queryExpr查询表达式属性值
     */
    public String getQueryExpr() {
        return queryExpr;
    }
    
    /**
     * @return 获取 queryRangeExpr1属性值
     */
    public String getQueryRangeExpr1() {
        return queryRangeExpr1;
    }
    
    /**
     * @return 获取 queryRangeExpr2属性值
     */
    public String getQueryRangeExpr2() {
        return queryRangeExpr2;
    }
}
