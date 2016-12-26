/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.comtop.cap.runtime.base.annotation.SpringRequestAttribute;
import com.comtop.cip.jodd.util.StringUtil;


/**
 * CAP 代码生成Action基类
 * 
 * @author 罗珍明
 * @version jdk1.5
 * @version 2015-5-14 罗珍明
 */
public abstract class SpringBaseAction extends CapRuntimeBaseAction {
    
    /****/
    private Map<String, Class<?>> sessionAttrClassMap = new HashMap<String, Class<?>>();
    
    /****/
    private Map<String, Method> requestAttrClassMap = new HashMap<String, Method>();
    
    /**
     * 
     */
    public SpringBaseAction() {
        initsessionAttrClassMap();
        initRequestAttrClassMap();
    }
    
    /**
     * @param sessionStatus 初始化页面
     */
    public void initPageAttr(SessionStatus sessionStatus) {
    	setDefaultUrlParamToAttribute();
    	setUrlParamToAttr();
        initBussinessAttr();
        initVerifyAttr();
        setDicData();
        initSessionAttr();
        if (isclearSessionAttr()) {
            sessionStatus.setComplete();
        }
        setDataStoreToAttr();
    }
    
    /***
     * 设置默认页面参数到request的Attribute中
     */
    protected abstract void setDefaultUrlParamToAttribute();
    
    /***
     * 设置参数到request的Attribute中
     * @param attrbuteName attr的key
     * @param attrValue attr的值
     */
    protected void setAttributeToRequest(String attrbuteName,Object attrValue){
    	getRequest().setAttribute(attrbuteName, attrValue);
    }
    
    /**
     * 
     * @param code 枚举类
     * @param attrs 枚举类关联的实体属性
     * @return 集合
     */
    protected List<Map<String, Object>> initEnumDatas(List<String> code, List<List<String>> attrs) {
    	return getEnumDatas(code, attrs);
    }
    
    /**
     * 
     * @param code 数据字典
     * @param attrs 数据字典关联的实体属性
     * @return 集合
     */
    public List<Map<String, Object>> initDicDatas(List<String> code, List<List<String>> attrs) {
    	return queryDicDatas(code, attrs);
    }
    
    /**
     * 设置数据字典数据
     */
    private void setDicData() {
        List<Map<String, Object>> dicDatas = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> lst = initDicDatas();
        if (lst != null) {
            dicDatas.addAll(lst);
        }
        lst = initEnumDatas();
        if (lst != null) {
            dicDatas.addAll(lst);
        }
        getRequest().setAttribute("dicDatas", dicDatas);
    }
    
    /**
     * @return 数据字典
     * 
     */
    public abstract List<Map<String, Object>> initDicDatas();
    
    /**
     * @return 数据字典
     * 
     */
    public abstract List<Map<String, Object>> initEnumDatas();
    
    /***
     * 获取url参数的值,获取实际请求url中的参数值 等同request.getParameter(paramName)
     * 
     * @param urlParamName url参数名称
     * @return url参数值
     */
    public String getUrlParamValue(String urlParamName) {
        return getRequest().getParameter(urlParamName);
    }
    
    /**
     * 
     * @param rightCode 权限编码
     * @param hasRight 是否具有改权限
     */
    protected void setRightToAttr(String rightCode, boolean hasRight) {
        getRequest().setAttribute(rightCode, hasRight);
    }
    
    /**
     * 将数据集放入到request的attr中
     */
    private void setDataStoreToAttr() {
        for (Entry<String, Method> entry : requestAttrClassMap.entrySet()) {
            Exception ex = null;
            try {
                Object objResult = entry.getValue().invoke(this, new Object[0]);
                getRequest().setAttribute(entry.getKey(), objResult);
            } catch (IllegalArgumentException e) {
                ex = e;
            } catch (IllegalAccessException e) {
                ex = e;
            } catch (InvocationTargetException e) {
                ex = e;
            } catch (Exception e) {
                ex = e;
            }
            if (ex != null) {
                LOGGER.error("the method '" + entry.getValue().getName()
                    + "' with Annotation SpringRequestAttribute invoke failed", ex);
            }
        }
    }
    
    /**
     * 将url中的paramcans 设置到request的attribute中
     */
    private void setUrlParamToAttr() {
        @SuppressWarnings("unchecked")
        Enumeration<String> enumParaName = getRequest().getParameterNames();
        while (enumParaName.hasMoreElements()) {
            String strParamName = enumParaName.nextElement();
            String strParamValue = getRequest().getParameter(strParamName);
            getRequest().setAttribute(strParamName, strParamValue);
        }
    }
    
    /**
     * 初始化request中需要存放的attr变量map
     */
    private void initRequestAttrClassMap() {
        Method[] arrMethod = getClass().getMethods();
        for (Method method : arrMethod) {
            SpringRequestAttribute obj = method.getAnnotation(SpringRequestAttribute.class);
            if (obj == null) {
                continue;
            }
            if (StringUtil.isBlank(obj.value())) {
                LOGGER.error("the Annotation SpringRequestAttribute of method '" + method.getName()
                    + "' missed require value");
                continue;
            }
            if (method.getParameterTypes().length > 0) {
                LOGGER.error("the method '" + method.getName()
                    + "' with Annotation SpringRequestAttribute must has no parameter");
                continue;
            }
            requestAttrClassMap.put(obj.value(), method);
        }
    }
    
    /**
     * 初始化session中要存放的attr的map
     */
    private void initsessionAttrClassMap() {
        SessionAttributes objSessionAttributes = getClass().getAnnotation(SessionAttributes.class);
        if (objSessionAttributes == null) {
            return;
        }
        Class<?>[] sessionAttType = objSessionAttributes.types();
        if (sessionAttType == null || sessionAttType.length == 0) {
            return;
        }
        String[] sessionKey = objSessionAttributes.value();
        for (int i = 0; i < sessionKey.length; i++) {
            sessionAttrClassMap.put(sessionKey[i], sessionAttType[i]);
        }
    }
    
    /**
     * 
     * @param attrName sessionAttr的key
     */
    @Override
    public void removeSessionAttribute(String attrName) {
        getRequest().getSession().removeAttribute(attrName);
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
