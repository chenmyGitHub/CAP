/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.codegen.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.entity.model.CascadeAttributeVO;
import com.comtop.cap.bm.metadata.entity.model.DBObject;
import com.comtop.cap.bm.metadata.entity.model.DBObjectParam;
import com.comtop.cap.bm.metadata.entity.model.DataTypeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.bm.metadata.entity.model.MethodOperateType;
import com.comtop.cap.bm.metadata.entity.model.MethodType;
import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.bm.metadata.entity.model.ParameterVO;
import com.comtop.cap.codegen.exception.CapCodegenException;
import com.comtop.cap.codegen.model.WrapperEntity.ImportNotifyArgs;
import com.comtop.cap.codegen.util.CapCodegenConstant;
import com.comtop.cap.codegen.util.CapCodegenUtils;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.corm.resource.util.CollectionUtils;

/**
 * 
 * 实体方法包装
 * 
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class WrapperMethod extends Observable {
    
    /** 实体方法 */
    private final MethodVO method;
    
    /** 实体方法 */
    private final String methodType;
    
    /** 导入类型观察者 */
    private final Observer observer;
    
    /** 级联代码字符串 */
    private final String cascadeCode;
    
    /** 方法名称 */
    private final String methodName;
    
    /** 返回值类型 */
    private final String returnType;
    
    /** 参数包装集合 */
    private List<WrapperParameter> parameterTypes;
    
    /** 数据库对象存储过程、函数参参数 */
    private List<DBObjectParam> dbObjectParameters;
    
    /** 异常包装集合 */
    private List<WrapperException> exceptionTypes;
    
    /** 参数与异常字符串 */
    private String paramAndExceptionName;
    
    /** 查询建模包装 */
    private final WrapperQueryModel queryModel;
    
    /** 方法所属实体 */
    private EntityVO belongEntity;
    
    /**
     * 构造函数
     * 
     * @param method 实体方法
     * @param entityAttributes 实体属性
     * @param observer 观察者对象
     * @param belongEntity 方法所属实体
     */
    public WrapperMethod(MethodVO method, List<WrapperAttribute> entityAttributes, Observer observer,
        EntityVO belongEntity) {
        super();
        this.belongEntity = belongEntity;
        this.method = method;
        this.methodType = this.method.getMethodType();
        this.observer = observer;
        this.addObserver(observer);
        this.returnType = wrapperReturnType();
        this.methodName = this.wrapperMethodName();
        this.cascadeCode = this.wrapperCascadeCode();
        this.queryModel = this.wrapperQueryModel();
        this.wrapperSQL();
        this.wrapperSoaMethod();
    }
    
    /**
     * 组装级联方法的代码字符串
     * 
     * @return 级联代码
     */
    private String wrapperCascadeCode() {
        if (MethodType.CASCADE.getValue().equals(methodType)) { // 级联方法
            StringBuffer bfCascadeCode = new StringBuffer();
            bfCascadeCode.append("  List<String> lstCascade = new ArrayList<String>();");
            bfCascadeCode.append("\n\t\t");
            List<CascadeAttributeVO> lstCascadeAttributeVO = this.method.getLstCascadeAttribute();
            if (lstCascadeAttributeVO != null && lstCascadeAttributeVO.size() > 0) {
                for (CascadeAttributeVO objCascadeAttributeVO : lstCascadeAttributeVO) {
                    bfCascadeCode.append("lstCascade.add(\"");
                    String strVO = covertVOToJsonStr(objCascadeAttributeVO);
                    bfCascadeCode.append(strVO);
                    bfCascadeCode.append("\" );");
                    bfCascadeCode.append("\n\t\t");
                }
            }
            if (this.method.getParameters() == null || this.method.getParameters().size() != 1) {
                throw new CapCodegenException("级联方法" + this.method.getEngName() + "参数设置不正确！");
            }
            String strOperateType = this.method.getMethodOperateType();
            String paramName = this.method.getParameters().get(0).getEngName();
            if (MethodOperateType.INSERT.getValue().equals(strOperateType)) { // 级联新增方法
                bfCascadeCode.append("return super.insertCascadeVO(" + paramName);
            } else if (MethodOperateType.QUERY.getValue().equals(strOperateType)) { // 级联查询方法
                bfCascadeCode.append("return (" + this.returnType + ") super.loadCascadeById(loadById(" + paramName
                    + ")");
            } else if (MethodOperateType.UPDATE.getValue().equals(strOperateType)) { // 级联更新方法
                bfCascadeCode.append("return super.updateCascadeVO(" + paramName);
            } else if (MethodOperateType.DELETE.getValue().equals(strOperateType)) { // 级联删除方法
                bfCascadeCode.append("return super.deleteCascadeList(" + paramName);
            } else if (MethodOperateType.SAVE.getValue().equals(strOperateType)) { // 级联保存方法
                bfCascadeCode.append("return super.saveCascadeVO(" + paramName);
            } else {
                throw new CapCodegenException("不支持的级联方法类型：" + strOperateType);
            }
            bfCascadeCode.append(", getCascadeVOList(lstCascade));");
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT, CapCodegenConstant.LIST_PATH, this));
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT,
                CapCodegenConstant.ARRAY_LIST_PATH, this));
            return bfCascadeCode.toString();
        }
        return null;
    }
    
    /**
     * 将对象转换为json串
     * 
     * @param objCascadeAttributeVO 级联对象
     * @return json串
     */
    private String covertVOToJsonStr(CascadeAttributeVO objCascadeAttributeVO) {
        if (objCascadeAttributeVO != null) {
            StringBuffer sbReturnStr = new StringBuffer();
            sbReturnStr.append("{\\\"entityName\\\":\\\"");
            String entityName = CapCodegenUtils.getEntityNameByEntityId(objCascadeAttributeVO.getGenerateCodeType());
            sbReturnStr.append(entityName);
            sbReturnStr.append("\\\", \\\"sourceEntityAliasName\\\":\\\"");
            String sourceEntityAliasName = "";
            if (belongEntity != null) {
                sourceEntityAliasName = StringUtil.isNotBlank(belongEntity.getAliasName()) ? belongEntity
                    .getAliasName() : CapCodegenUtils.getEntityNameByEntityId(belongEntity.getModelId());
            }
            sbReturnStr.append(sourceEntityAliasName);
            sbReturnStr.append("\\\", \\\"targetEntityAliasName\\\":\\\"");
            String entityAliasName = CapCodegenUtils.getEntityAliasNameByEntityId(objCascadeAttributeVO
                .getGenerateCodeType());
            sbReturnStr.append(entityAliasName);
            sbReturnStr.append("\\\", \\\"entityAttName\\\":\\\"");
            sbReturnStr.append(objCascadeAttributeVO.getName());
            sbReturnStr.append("\\\"");
            List<CascadeAttributeVO> lstSubCascadeAttributeVO = objCascadeAttributeVO.getLstCascadeAttribute();
            if (lstSubCascadeAttributeVO != null && lstSubCascadeAttributeVO.size() > 0) {
                sbReturnStr.append(", \\\"lstCascadeVO\\\":[");
                for (CascadeAttributeVO objSubVO : lstSubCascadeAttributeVO) {
                    sbReturnStr.append(covertVOToJsonStr(objSubVO));
                }
                sbReturnStr.append("]");
            }
            sbReturnStr.append("}");
            return sbReturnStr.toString();
        }
        return "";
    }
    
    /**
     * 包装SQL语句
     * 
     */
    private void wrapperSQL() {
        // 当方法类型不为 非自定义sql查询方法 并且不为存储过程调用方法时直接返回
        if (MethodType.USER_DEFINED_SQL.getValue().equals(methodType)
            || MethodType.QUERY_MODELING.getValue().equals(methodType)) {
            if (this.method.getParameters() != null && this.method.getParameters().size() > 1) { // 多于一个参数的自定义sql方法
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, CapCodegenConstant.MAP_PATH,
                    this));
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT,
                    CapCodegenConstant.HASHMAP_PATH, this));
            }
            if (this.method.isNeedPagination()) { // 分页方法
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT, CapCodegenConstant.MAP_PATH,
                    this));
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT,
                    CapCodegenConstant.HASHMAP_PATH, this));
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT, CapCodegenConstant.LIST_PATH,
                    this));
            }
        }
        // 存储过程调用、函数调用导入Map、HashMap包
        if (MethodType.PROCEDURE.getValue().equals(methodType) || MethodType.FUNCTION.getValue().equals(methodType)) {
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, CapCodegenConstant.MAP_PATH, this));
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, CapCodegenConstant.HASHMAP_PATH,
                this));
        }
    }
    
    /**
     * 包装soa方法导入包
     */
    private void wrapperSoaMethod() {
        // 如果存在blank空方法则导入soaMethod包
        if (MethodType.BLANK.getValue().equals(methodType)) {
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.IMP_FACADE_IMPORT,
                CapCodegenConstant.SOA_METHOD_PATH, this));
        }
        
        // 如果全部都是queryExtend查询重写方法则不需要导入soaMethod包
        boolean isAllQueryExtend = true;
        if (!MethodType.QUERY_EXTEND.getValue().equals(methodType)) {
            isAllQueryExtend = false;
        }
        if (!isAllQueryExtend) {
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT,
                CapCodegenConstant.SOA_METHOD_PATH, this));
        }
        
    }
    
    /**
     * 包装查询建模
     * 
     * @return 包装查询建模
     */
    private WrapperQueryModel wrapperQueryModel() {
        if (MethodType.QUERY_MODELING.getValue().equals(method.getMethodType())) {
            return new WrapperQueryModel(method, observer);
        }
        return null;
    }
    
    /**
     * @return 获取 method属性值
     */
    public MethodVO getMethod() {
        return method;
    }
    
    /**
     * @return 获取 数据对象的信息 用于存储过程
     */
    public String getProcedureMessage() {
        if (method.getDbObjectCalled() != null) {
            String procedureName = method.getDbObjectCalled().getObjectName();
            List<DBObjectParam> dBObjectParams = method.getDbObjectCalled().getLstParameters();
            if (dBObjectParams != null) {
                StringBuffer bff = new StringBuffer("");
                if (dBObjectParams.size() > 0) {
                    bff.append("(");
                    for (int i = 0; i < dBObjectParams.size(); i++) {
                        DBObjectParam dBObjectParam = dBObjectParams.get(i);
                        bff.append(getCallMessage(dBObjectParam));
                        if (i < dBObjectParams.size() - 1) {
                            bff.append(",");
                        } else {
                            bff.append(")");
                        }
                    }
                }
                return "{ call " + procedureName + bff.toString() + " }";
            }
            return "{ call " + procedureName + " }";
        }
        return "{ call }";
    }
    
    /**
     * @return 获取 数据对象的信息 用于函数调用
     */
    public String getFunctionMessage() {
        if (method.getDbObjectCalled() != null) {
            String functionName = method.getDbObjectCalled().getObjectName();
            List<DBObjectParam> dBObjectParams = method.getDbObjectCalled().getLstParameters();
            // 如果存在参数并且参数个数大于0
            if (dBObjectParams != null && dBObjectParams.size() > 0) {
                List<DBObjectParam> dBObjectINParams = new ArrayList<DBObjectParam>();
                List<DBObjectParam> dBObjectOUTParams = new ArrayList<DBObjectParam>();
                // 先区分IN 类型 和OUT类型参数
                for (int i = 0; i < dBObjectParams.size(); i++) {
                    DBObjectParam dBObjectParam = dBObjectParams.get(i);
                    if ("OUT".equals(CapCodegenUtils.getParamCategoryString(dBObjectParam.getParamCategory()))) {
                        dBObjectOUTParams.add(dBObjectParam);
                    } else {
                        dBObjectINParams.add(dBObjectParam);
                    }
                }
                // 如果有返回 ，格式如下：
                // {#{f_RETURN,mode=OUT,jdbcType=VARCHAR} = call
                // LC_TEST_FUN2(#{p_DELIMITER,mode=IN,jdbcType=VARCHAR},#{p_STR,mode=IN,jdbcType=VARCHAR}) }
                if (dBObjectOUTParams.size() > 0) {
                    // 先组装OUT类型参数，函数只能有一个返回值
                    StringBuffer bffOUT = new StringBuffer("");
                    bffOUT.append(getCallMessage(dBObjectOUTParams.get(0)));
                    // 再组装IN烈性参数
                    StringBuffer bffIN = new StringBuffer("");
                    if (dBObjectINParams.size() > 0) {
                        bffIN.append("(");
                        for (int i = 0; i < dBObjectINParams.size(); i++) {
                            DBObjectParam dBObjectParam = dBObjectINParams.get(i);
                            bffIN.append(getCallMessage(dBObjectParam));
                            if (i < dBObjectINParams.size() - 1) {
                                bffIN.append(",");
                            } else {
                                bffIN.append(")");
                            }
                        }
                    }
                    return "{" + bffOUT.toString() + " = call " + functionName + bffIN.toString() + "}";
                }
                // 如果没有返回值，则直接调用call 格式如下：
                // {call LC_TEST_FUN1(#{p_DELIMITER,mode=IN,jdbcType=VARCHAR},#{p_STR,mode=IN,jdbcType=VARCHAR},
                // #{f_RETURN,mode=OUT,jdbcType=VARCHAR})}
                StringBuffer bff = new StringBuffer("");
                if (dBObjectINParams.size() > 0) {
                    bff.append("(");
                    for (int i = 0; i < dBObjectINParams.size(); i++) {
                        DBObjectParam dBObjectParam = dBObjectINParams.get(i);
                        bff.append(getCallMessage(dBObjectParam));
                        if (i < dBObjectINParams.size() - 1) {
                            bff.append(",");
                        } else {
                            bff.append(")");
                        }
                    }
                }
                return "{call " + functionName + bff.toString() + " }";
            }
            // 如果没有参数，则返回函数名称
            return "{call " + functionName + " }";
        }
        return "";
    }
    
    /**
     * @param dBObjectParam 数据对象参数
     * @return 获取 方法的简单名称
     */
    private String getCallMessage(DBObjectParam dBObjectParam) {
        String callMessage = "#{" + CapCodegenUtils.firstLetterToLower(dBObjectParam.getParamName()) + ",mode="
            + CapCodegenUtils.getParamCategoryString(dBObjectParam.getParamCategory()) + ",jdbcType="
            + CapCodegenUtils.tranfJavaTypeToDatabaseType(dBObjectParam.getParamType()) + "}";
        return callMessage;
    }
    
    /**
     * @return 获取 方法的简单名称
     */
    public String getSingleMethodName() {
        return method.getEngName();
    }
    
    /**
     * 方法的简单名称(不含访问级别、返回值、参数和异常)
     * 
     * 
     * @return 方法的简单名称
     */
    public String getSimpleName() {
        return StringUtil.uncapitalize(this.method.getEngName());
    }
    
    /**
     * 包装此Method方法签名的字符串，包括访问级别、返回值和参数。
     * 
     * @return 方法名
     * 
     */
    public String wrapperMethodName() {
        return genMethodName(this.returnType);
    }
    
    /**
     * 生成方法名称
     * 
     * @param ret 返回值
     * @return 方法名称
     */
    private String genMethodName(String ret) {
        // 空格
        String strBlank = " ";
        StringBuilder objStrBuilder = new StringBuilder();
        String strAccess = getModifier();
        objStrBuilder.append(strAccess);
        
        objStrBuilder.append(strBlank);
        objStrBuilder.append(ret);
        
        objStrBuilder.append(strBlank);
        objStrBuilder.append(method.getEngName());
        this.paramAndExceptionName = genParamAndExceptionName();
        objStrBuilder.append(paramAndExceptionName);
        return objStrBuilder.toString();
    }
    
    /**
     * 返回方法参数和异常签名字符串
     * 
     * @return 签名字符串
     */
    private String genParamAndExceptionName() {
        StringBuilder objStrBuilder = new StringBuilder();
        objStrBuilder.append("(");
        // 包装参数
        if (this.parameterTypes == null) {
            wrapperParameterTypes();
        }
        for (int iIndex = 0; iIndex < this.parameterTypes.size(); iIndex++) {
            WrapperParameter objParam = this.parameterTypes.get(iIndex);
            objStrBuilder.append(objParam.getParameterType());
            objStrBuilder.append(" ");
            objStrBuilder.append(objParam.getAlias());
            if (iIndex < (this.parameterTypes.size() - 1)) {
                objStrBuilder.append(",");
            }
        }
        objStrBuilder.append(")");
        // 包装异常
        if (this.exceptionTypes == null) {
            wrapperExceptionTypes();
        }
        if (this.exceptionTypes.size() > 0) {
            objStrBuilder.append(" throws ");
            for (int iIndex = 0; iIndex < this.exceptionTypes.size(); iIndex++) {
                WrapperException objEx = this.exceptionTypes.get(iIndex);
                objStrBuilder.append(objEx.getExceptionType());
                if (iIndex < (this.exceptionTypes.size() - 1)) {
                    objStrBuilder.append(",");
                }
            }
        }
        return objStrBuilder.toString();
    }
    
    /**
     * 获取方法的 Java语言修饰符
     * 
     * @return Java语言修饰符
     */
    private String getModifier() {
        return method.getAccessLevel();
    }
    
    /**
     * 包装返回值类型并更新导入类
     * 
     * @return 返回值类型
     */
    private String wrapperReturnType() {
        DataTypeVO objReturnDataTypeVO = this.method.getReturnType();
        // 查询重写方法不生成代码
        if (MethodType.QUERY_EXTEND.getValue().equals(methodType)) {
            return objReturnDataTypeVO.readDataTypeName();
        }
        // 更新facade层的导入类
        List<String> lstClassName = objReturnDataTypeVO.readImportDateType();
        for (String strClassName : lstClassName) {
            this.setChanged();
            notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_FACADE_IMPORT, strClassName, this));
            if (MethodType.BLANK.getValue().equals(methodType)) { // 空方法
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.IMP_FACADE_IMPORT, strClassName, this));
            }
            if (MethodType.USER_DEFINED_SQL.getValue().equals(methodType)) { // 自定义sql方法
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, strClassName, this));
            }
            
            if (MethodType.QUERY_MODELING.getValue().equals(methodType)) {// 查询建模
                this.setChanged();
                notifyObservers(new ImportNotifyArgs(ImportNotifyArgs.ABS_SERVICE_IMPORT, strClassName, this));
            }
        }
        return objReturnDataTypeVO.readDataTypeName();
    }
    
    /**
     * 获取方法的正式返回类型
     * 
     * @return 方法的正式返回类型
     */
    public String getReturnType() {
        return this.returnType;
    }
    
    /**
     * 获取返回值注释
     * 
     * @return 返回值注释
     */
    public String getReturnComment() {
        if ("void".equals(this.returnType)) {
            return "";
        }
        return this.returnType;
    }
    
    /**
     * 包装方法参数
     */
    private void wrapperParameterTypes() {
        List<ParameterVO> lstParam = method.getParameters();
        this.parameterTypes = new ArrayList<WrapperParameter>();
        if (!CollectionUtils.isEmpty(lstParam)) {
            for (ParameterVO objParam : lstParam) {
                WrapperParameter objWrapperParam = new WrapperParameter(this.methodType, objParam, this.observer);
                this.parameterTypes.add(objWrapperParam);
            }
        }
    }
    
    /**
     * 获取方法参数封装
     * 
     * @return 方法参数封装
     */
    public List<WrapperParameter> getParameterTypes() {
        return this.parameterTypes == null ? new ArrayList<WrapperParameter>() : this.parameterTypes;
    }
    
    /**
     * 获取方法抛出的异常类型
     * 
     * @return 方法异常
     */
    public List<WrapperException> getExceptionTypes() {
        return this.exceptionTypes;
    }
    
    /**
     * 获取参数字符串
     * 
     * @return 参数字符串
     */
    public String getParamterArgs() {
        if (CollectionUtils.isEmpty(this.parameterTypes)) {
            return "";
        }
        String strArgs = "";
        for (int iIndex = 0; iIndex < this.parameterTypes.size(); iIndex++) {
            WrapperParameter objParam = this.parameterTypes.get(iIndex);
            if (iIndex > 0) {
                strArgs += ",";
            }
            strArgs += objParam.getAlias();
        }
        return strArgs;
    }
    
    /**
     * 包装方法异常
     * 
     */
    private void wrapperExceptionTypes() {
        List<ExceptionVO> lstEx = method.getExceptions();
        this.exceptionTypes = new ArrayList<WrapperException>();
        if (!CollectionUtils.isEmpty(lstEx)) {
            for (ExceptionVO objEx : lstEx) {
                WrapperException objWrapperEx = new WrapperException(objEx, this.observer, this.methodType);
                this.exceptionTypes.add(objWrapperEx);
            }
        }
    }
    
    /**
     * 方法名注释
     * 
     * @return 方法名注释
     */
    public String getComment() {
        return this.method.getChName();
    }
    
    /**
     * @return 获取 methodName属性值
     */
    public String getMethodName() {
        return this.methodName;
    }
    
    /**
     * @return 获取级联操作代码
     */
    public String getCascadeCode() {
        return this.cascadeCode;
    }
    
    /**
     * @return 获取 服务增强属性值
     */
    public String getServiceEx() {
        return this.method.getServiceEx();
    }
    
    /**
     * @return 获取方法类型属性值
     */
    public String getMethodType() {
        return methodType;
    }
    
    /**
     * @return 获取方法操作类型属性值
     */
    public String getMethodOperateType() {
        return this.method.getMethodOperateType();
    }
    
    /**
     * @return 获取querySQL属性值
     */
    public String getQuerySQL() {
        return this.method.getUserDefinedSQL().getQuerySQL();
    }
    
    /**
     * @return 获取sortSQL属性值
     */
    public String getSortSQL() {
        String sortSql = this.method.getUserDefinedSQL().getSortSQL();
        return StringUtil.isNotBlank(sortSql) ? sortSql.trim() : null;
    }
    
    /**
     * @return 获取sql返回值类型
     */
    public String getSqlReturnType() {
        return this.method.getUserDefinedSQL().getResultType();
    }
    
    /**
     * @return 获取sql参数类型
     */
    public String getSqlParamType() {
        String parameterType = this.method.getUserDefinedSQL().getParameterType();
        return parameterType == null ? null : parameterType.trim();
    }
    
    /**
     * @return 获取assoMethodName属性值
     */
    public String getAssoMethodName() {
        return this.method.getAssoMethodName();
    }
    
    /**
     * @return 获取bNeedPagination属性值
     */
    public boolean isNeedPagination() {
        return this.method.isNeedPagination();
    }
    
    /**
     * @return 获取参数和异常字符串属性值
     */
    public String getParamAndExceptionName() {
        return this.paramAndExceptionName;
    }
    
    /**
     * @return 获取 数据库对象参数
     */
    public List<DBObjectParam> getDbObjectParameters() {
        DBObject dbObject = this.method.getDbObjectCalled();
        if (dbObject == null) {
            return null;
        }
        dbObjectParameters = dbObject.getLstParameters();
        return dbObjectParameters;
    }
    
    /**
     * @return the queryModel
     */
    public WrapperQueryModel getQueryModel() {
        return queryModel;
    }
    
}
