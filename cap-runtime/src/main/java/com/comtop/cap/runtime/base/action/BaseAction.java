/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cip.jodd.introspector.ClassDescriptor;
import com.comtop.cip.jodd.introspector.ClassIntrospector;
import com.comtop.cip.jodd.introspector.FieldDescriptor;
import com.comtop.cip.jodd.madvoc.ScopeType;
import com.comtop.cip.jodd.madvoc.meta.In;
import com.comtop.cip.jodd.madvoc.meta.InOut;
import com.comtop.cip.jodd.madvoc.meta.Out;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * CAP 代码生成Action基类
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2015-5-14 郑重
 */
public abstract class BaseAction extends CapRuntimeBaseAction {
    
    /**
     * 页面依赖的数据字典集合
     */
    @Out
    protected List<Map<String, Object>> dicDatas = new ArrayList<Map<String, Object>>();
    
    /****/
    private Map<String, FieldDescriptor> sessionAttrClassMap = new HashMap<String, FieldDescriptor>();
    
    /**
     * 
     */
    @InOut(REQUEST_PARAM_KEY_CLEAR_SESSION)
    protected boolean clearSessionAttr = true;
    
    /** 用于已办消息中心链接到编辑页面需要用到的扩展参数 **/
    @InOut
    protected String extendAttr;
    
    @Override
    protected boolean isclearSessionAttr() {
        return clearSessionAttr;
    }
    
    /**
     * 
     * @param code 数据字典编码
     * @param attrs 数据字典关联的属性
     */
    public void initDicDatas(List<String> code, List<List<String>> attrs) {
        dicDatas.addAll(queryDicDatas(code, attrs));
    }
    
    /**
     * 
     * @param code 枚举类
     * @param attrs 枚举类关联的属性
     */
    public void initEnumDatas(List<String> code,
    		List<List<String>> attrs) {
    	dicDatas.addAll(getEnumDatas(code, attrs));
    }
    
    /**
     * 
     */
    public BaseAction() {
        initSessionAttrClass();
    }
    
    /**
     * 初始化页面使用的数据字典集合
     */
    public void initDicDatas() {
        // 被子类覆盖使用
    }
    
    /**
     * 初始化页面使用的枚举集合
     */
    public void initEnumDatas() {
        // 被子类覆盖使用
    }
    
    @Override
    public void initPageAttr() {
        super.initPageAttr();
        initDicDatas();
        initEnumDatas();
    }
    
    /**
     * 初始化Session变量的类型信息
     */
    private void initSessionAttrClass() {
        ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(this.getClass());
        FieldDescriptor[] objFieldDescriptors = objClassDescriptor.getAllFieldDescriptors();
        for (FieldDescriptor objFieldDescriptor : objFieldDescriptors) {
            objFieldDescriptor.getField().setAccessible(true);
            In sessionInAttr = objFieldDescriptor.getField().getAnnotation(In.class);
            if (sessionInAttr != null && ScopeType.SESSION == sessionInAttr.scope()) {
                String strAttrName = sessionInAttr.value();
                if (StringUtil.isEmpty(strAttrName)) {
                    strAttrName = objFieldDescriptor.getField().getName();
                }
                sessionAttrClassMap.put(strAttrName, objFieldDescriptor);
                continue;
            }
            InOut sessionAttr = objFieldDescriptor.getField().getAnnotation(InOut.class);
            if (sessionAttr != null && ScopeType.SESSION == sessionAttr.scope()) {
                String strAttrName = sessionAttr.value();
                if (StringUtil.isEmpty(strAttrName)) {
                    strAttrName = objFieldDescriptor.getField().getName();
                }
                sessionAttrClassMap.put(strAttrName, objFieldDescriptor);
            }
        }
    }
    
    /**
     * 
     * @param attrName sessionAttr的key
     */
    @Override
    public void removeSessionAttribute(String attrName) {
        getRequest().getSession().removeAttribute(attrName);
        if (sessionAttrClassMap.get(attrName) == null) {
            return;
        }
        try {
            sessionAttrClassMap.get(attrName).getField().set(this, null);
        } catch (Exception e) {
            LOGGER.error("将session中的变量：" + attrName + "移除失败.", e);
        }
    }
    
    /**
     * 清空当前Action中定义的SessionAttribute参数
     * 
     * @return true删除成功
     */
    @Override
    public boolean clearCurrentActionSessionAttribute() {
        for (String strSessionAttrKey : sessionAttrClassMap.keySet()) {
            removeSessionAttribute(strSessionAttrKey);
        }
        return true;
    }
    
}
