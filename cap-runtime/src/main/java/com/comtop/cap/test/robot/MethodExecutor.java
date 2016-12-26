/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.test.robot;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cap.test.robot.ResponseData.ResultType;
import com.comtop.top.core.util.JsonUtil;

/**
 * 方法执行器
 *
 * @author lizhongwen
 * @since jdk1.6
 * @version 2016年6月30日 lizhongwen
 */
public class MethodExecutor {
    
    /** 日志 */
    private final static Logger logger = LoggerFactory.getLogger(MethodExecutor.class);
    
    /**
     * @param data 请求数据
     * @return 执行结果
     */
    public static ResponseData execute(RequestData data) {
        ResponseData response = new ResponseData(ResultType.SUCCESS);
        if (!validateRequestData(data)) {
            response.setType(ResultType.FAIL);
            response.setMessage("数据格式不正确！");
            return response;
        }
        String clazzName = data.getClazz();
        List<String> paramTypes = data.getParams();
        String methodName = data.getMethodName();
        List<Object> datas = data.getDatas();
        Class<?>[] parameterTypes = null;
        Class<?> clazz = null;
        Method method = null;
        Object[] args = null;
        try {
            clazz = Class.forName(clazzName);
            if (paramTypes != null && !paramTypes.isEmpty()) {
                parameterTypes = new Class<?>[paramTypes.size()];
                for (int i = 0; i < paramTypes.size(); i++) {
                    parameterTypes[i] = Class.forName(paramTypes.get(i));
                }
            }
            method = handleMethod(clazz, methodName, parameterTypes);
        } catch (Exception e) {
            response.setType(ResultType.FAIL);
            response.setMessage("指定的API接口不存在！");
            response.setDetailMessage(e.getMessage());
            logger.error(e.getMessage(), e);
            return response;
        }
        Object instance = BeanContextUtil.getBean(clazz);
        if (instance == null || method == null) {
            response.setType(ResultType.FAIL);
            response.setMessage("无法从容器中获取到API接口实例！");
            return response;
        }
        try {
            args = convertDatas(datas, parameterTypes);
            Object result = method.invoke(instance, args);
            response.setData(result);
        } catch (Exception e) {
            response.setType(ResultType.FAIL);
            response.setMessage("API方法执行错误！");
            response.setDetailMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return response;
    }
    
    /**
     * @param datas 原始数据
     * @param parameterTypes 参数类型
     * @return 数据
     */
    private static Object[] convertDatas(List<Object> datas, Class<?>[] parameterTypes) {
        if (datas == null || datas.isEmpty()) {
            return null;
        }
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        if (datas.size() != parameterTypes.length) {
            throw new RuntimeException("参数个数不匹配！");
        }
        Object[] args = new Object[datas.size()];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Object data = datas.get(i);
            if (data instanceof String && String.class.equals(parameterType)) {
                args[i] = data;
            } else {
                String json = JsonUtil.objectToJson(data);
                args[i] = JsonUtil.jsonToObject(json, parameterType);
            }
        }
        return args;
    }
    
    /**
     * 获取方法
     * 
     * @param clazz 类
     * @param methodName 方法名
     * @param parameterTypes 参数名
     * @return 方法
     */
    public static Method handleMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        Method method = null;
        try {
            method = clazz.getMethod(methodName, parameterTypes);
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }
        Class<?> superClass = clazz.getSuperclass();
        while (method == null && superClass != null) {
            Method[] methos = clazz.getMethods();
            for (Method m : methos) {
                if (m.getName().equals(methodName)) {
                    Class<?>[] parameters = m.getParameterTypes();
                    if (parameters != null) {
                        if (parameterTypes == null || parameters.length != parameterTypes.length) {
                            continue;
                        }
                        boolean match = true;
                        for (int i = 0; i < parameters.length; i++) {
                            Class<?> p1 = parameters[i];
                            Class<?> p2 = parameterTypes[i];
                            match = (p1.isAssignableFrom(p2) || p1.equals(p2)) && match;
                        }
                        if (match) {
                            return m;
                        }
                    } else if (parameterTypes == null) {
                        return m;
                    }
                }
            }
            superClass = superClass.getSuperclass();
        }
        return method;
    }
    
    /**
     * 请求数据验证
     *
     * @param data 数据
     * @return 是否为合法数据
     */
    private static boolean validateRequestData(RequestData data) {
        return StringUtils.isNotBlank(data.getClazz()) && StringUtils.isNotBlank(data.getMethodName());
    }
}
