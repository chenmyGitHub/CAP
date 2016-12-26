/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.enumeration.EnumReflectProvider;
import com.comtop.cap.runtime.base.exception.CapRuntimeException;
import com.comtop.cap.runtime.base.facade.CapSoaExtendFacade;
import com.comtop.cap.runtime.base.json.CapSqlDateDeserise;
import com.comtop.cap.runtime.base.json.CapTimeStampDeserise;
import com.comtop.cap.runtime.base.json.ExtSqlDateDeserializer;
import com.comtop.cap.runtime.base.json.ExtTimestampDeserializer;
import com.comtop.cap.runtime.base.model.CapDefaultTreeNode;
import com.comtop.cap.runtime.base.model.CapSoaParamExtendVO;
import com.comtop.cap.runtime.base.model.SoaInvokeParam;
import com.comtop.cip.json.JSON;
import com.comtop.cip.json.parser.ParserConfig;
import com.comtop.soa.common.util.SOAJsonUtil;
import com.comtop.top.cfg.facade.ConfigFacade;
import com.comtop.top.cfg.facade.IConfigFacade;
import com.comtop.top.component.app.session.HttpSessionUtil;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.tree.TreeTransformUtils;
import com.comtop.top.sys.client.facade.ClientAccessFacade;
import com.comtop.top.sys.usermanagement.user.model.UserDTO;
import comtop.org.directwebremoting.annotations.RemoteMethod;
import comtop.soa.com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * CAP 代码生成Action基类
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2015-5-14 郑重
 */
public abstract class CapRuntimeBaseAction {
    
    static {
        ParserConfig config = ParserConfig.getGlobalInstance();
        // 注册重写的FastJSON TimestampDeserializer
        config.putDeserializer(java.sql.Timestamp.class, ExtTimestampDeserializer.EXT_TIMESTAMP_INSTANCE);
        // 注册重写的FastJSON SqlDateDeserializer
        config.putDeserializer(java.sql.Date.class, ExtSqlDateDeserializer.EXT_SQLDATE_INSTANCE);
        
        // 给soa的jackson处理类，设置timestamp的格式化处理类
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Timestamp.class, new CapTimeStampDeserise());
        module.addDeserializer(Date.class, new CapSqlDateDeserise());
        SOAJsonUtil.getMapper().registerModule(module);
    }
    
    /** 日志 */
    protected final static Logger LOGGER = LoggerFactory.getLogger(CapRuntimeBaseAction.class);
    
    /**
     * ioc注入ClientAccessFacade
     */
    protected ClientAccessFacade clientAccessFacade = AppContext.getBean(ClientAccessFacade.class);
    
    /**
     * 数据字典facade
     */
    protected IConfigFacade configFacade = AppContext.getBean(ConfigFacade.class);
    
    /** soa服务参数扩展信息读取类 */
    private CapSoaExtendFacade capSoaExtendFacade = AppContext.getBean(CapSoaExtendFacade.class);
    
    /** 清除session的url参数key */
    public static final String REQUEST_PARAM_KEY_CLEAR_SESSION = "clearSession";
    
    /**
     * 校验权限
     * 
     * @param pageCode 页面编码
     * @param operationCode 操作编码
     * @return 验证成功返回 true,否则返回false
     */
    public boolean verify(String pageCode, String operationCode) {
        UserDTO objUserDTO = (UserDTO) HttpSessionUtil.getCurUserInfo();
        return clientAccessFacade.verifyUserOperation(objUserDTO.getUserId(), pageCode, operationCode);
    }
    
    /**
     * 初始化数据字典集合
     * 
     * @param code 数据字典code集合
     * @param attrs 数据字典code关联属性集合
     * @return 数据字典结果集
     */
    @SuppressWarnings({ "serial" })
    public List<Map<String, Object>>  queryDicDatas(List<String> code, List<List<String>> attrs) {
    	List<Map<String, Object>> lst = new ArrayList<Map<String,Object>>();
        Map<String, Map<String, Object>> codes = new HashMap<String, Map<String, Object>>();
        for (int i = 0; i < code.size(); i++) {
            final String strCode = code.get(i);
            final List<String> lstAttr = attrs.get(i);
            codes.put(strCode, new HashMap<String, Object>() {
                
                {
                    put("code", strCode);
                    put("attrs", lstAttr);
                }
            });
        }
        
        String[] strCodes = new String[codes.size()];
        strCodes = codes.keySet().toArray(strCodes);
        
        Map<String, List<Map<String, String>>> objMapDics = configFacade.getDicDataSet(strCodes);
        if (objMapDics != null) {
            Set<Map.Entry<String, List<Map<String, String>>>> ojbSet = objMapDics.entrySet();
            Iterator<Map.Entry<String, List<Map<String, String>>>> objIterator = ojbSet.iterator();
            while (objIterator.hasNext()) {
                Map.Entry<String, List<Map<String, String>>> entry = objIterator.next();
                String strCode = entry.getKey();
                List<Map<String, String>> lstDics = entry.getValue();
                Map<String, Object> objDic = codes.get(strCode);
                objDic.put("list", lstDics);
                lst.add(objDic);
            }
        }
        return lst;
    }
    
    /**
     * 初始化枚举集合
     * 
     * @param code 枚举类的全路径
     * @param attrs 关联枚举的实体属性集合
     * @return f安徽枚举数据结果集
     */
    // （不要试图将本方法与本类中的<code>initDicDatas(List<String> code, List<List<String>> attrs)</code>方法进行提炼重构，这样将会导致不能向前兼容）
    @SuppressWarnings("serial")
    public List<Map<String, Object>> getEnumDatas(List<String> code, List<List<String>> attrs) {
    	List<Map<String, Object>> lst = new ArrayList<Map<String,Object>>();
        Map<String, Map<String, Object>> codes = new HashMap<String, Map<String, Object>>();
        for (int i = 0; i < code.size(); i++) {
            final String strCode = code.get(i);
            final List<String> lstAttr = attrs.get(i);
            codes.put(strCode, new HashMap<String, Object>() {
                
                {
                    put("code", strCode);
                    put("attrs", lstAttr);
                }
            });
        }
        
        String[] strCodes = new String[codes.size()];
        strCodes = codes.keySet().toArray(strCodes);
        
        Map<String, List<Map<String, String>>> objMapDics = null;
        try {
            objMapDics = EnumReflectProvider.invoke(strCodes);
        } catch (ClassNotFoundException e) {
            LOGGER.error("从枚举类中获取数据字典失败", e);
        }
        if (objMapDics != null) {
            Set<Map.Entry<String, List<Map<String, String>>>> ojbSet = objMapDics.entrySet();
            Iterator<Map.Entry<String, List<Map<String, String>>>> objIterator = ojbSet.iterator();
            while (objIterator.hasNext()) {
                Map.Entry<String, List<Map<String, String>>> entry = objIterator.next();
                String strCode = entry.getKey();
                List<Map<String, String>> lstDics = entry.getValue();
                Map<String, Object> objDic = codes.get(strCode);
                objDic.put("list", lstDics);
                lst.add(objDic);
            }
        }
        return lst;
    }
    
    /**
     * 初始化页面参数
     */
    public abstract void initBussinessAttr();
    
    /**
     * 初始化页面参数-权限
     */
    public abstract void initVerifyAttr();
    
    /**
     * 初始化Session中的Attr
     */
    public void initSessionAttr() {
        if (isclearSessionAttr()) {
            clearCurrentActionSessionAttribute();
        }
    }
    
    /**
     * 是否清理session
     * 
     * @return 是否清除session
     * */
    protected boolean isclearSessionAttr() {
        return !"false".equals(getRequest().getParameter(REQUEST_PARAM_KEY_CLEAR_SESSION));
    }
    
    /**
     * 初始化页面参数
     */
    public void initPageAttr() {
        initBussinessAttr();
        initVerifyAttr();
        initSessionAttr();
    }
    
    /**
     * 通过调用此方法来调用后台的方法（走SOA）
     * 
     * @param invokeParam soa调用方法的信息
     * @return 调用soa方法得到的返回值。如果调用的方法为void返回值，此方法范围null
     */
    @RemoteMethod
    public Object dwrInvoke(SoaInvokeParam invokeParam) {
        RuntimeException e = null;
        if (invokeParam.getSoaServiceId() == null || invokeParam.getSoaServiceId().trim().length() == 0) {
            e = new CapRuntimeException("没有指定要调用的服务，invokeParam.soaServiceId为空");
            LOGGER.error("调用SOA发生了如下异常", e);
            throw e;
        }
        
        try {
            
            List<CapSoaParamExtendVO> lstParamExtendVOs = capSoaExtendFacade.querySoaExtendParamInfoList(invokeParam
                .getSoaServiceId());
            
            if (lstParamExtendVOs == null || lstParamExtendVOs.size() == 0) {
                return ServiceInvokehelper.invokeSoaMethod(invokeParam, lstParamExtendVOs);
            }
            
            if (invokeParam.getParamJosn() == null || invokeParam.getParamJosn().length == 0) {
                e = new CapRuntimeException("方法" + invokeParam.getSoaServiceId() + "需要" + lstParamExtendVOs.size()
                    + "个参数，实际传入：" + invokeParam.getParamJosn());
                LOGGER.error("调用SOA发生了如下异常", e);
                throw e;
            }
            
            return ServiceInvokehelper.invokeLocalBaseMethod(invokeParam, lstParamExtendVOs);
        } catch (Exception ex) {
            // 处理反射的invoke异常因为该异常会丢失详细堆栈信息(循环查找堆栈是否存在InvocationTargetException)
            while (ex instanceof InvocationTargetException) {
                Throwable throwable = ((InvocationTargetException) ex).getTargetException();
                if (throwable.getCause() == null) {
                    ex = (Exception) throwable;
                    break;
                }
                ex = (Exception) throwable.getCause();
            }
            
            LOGGER.error("调用" + invokeParam.getSoaServiceId() + "方法出错", ex);
            throw new CapRuntimeException(ex);
        }
    }
    
    /**
     * 根据查询出的数据源转换为json树对象
     * 
     * @author xu_chang
     * @param invokeParam 树参数
     * @return json树形结构
     */
    @RemoteMethod
    public String convertToTreeJSON(SoaInvokeParam invokeParam) {
        
        final String jsonParam = invokeParam.getParamJosn()[0];
        
        String[] treeDataParam = Arrays.copyOfRange(invokeParam.getParamJosn(), 1, invokeParam.getParamJosn().length);
        
        invokeParam.setParamJosn(treeDataParam);
        
        Object objResult = dwrInvoke(invokeParam);
        if (!(objResult instanceof List)) {
            throw new CapRuntimeException("the dataSource is not List");
        }
        
        List<Object> list = (List<Object>) objResult;
        
        CapDefaultTreeNode tree = new CapDefaultTreeNode();
        tree.setTreeParam(jsonParam);
        
        return TreeTransformUtils.listToTree(list, tree);
    }
    
    /**
     * 清空当前Action中定义的SessionAttribute参数
     * 
     * @return true删除成功
     */
    abstract public boolean clearCurrentActionSessionAttribute();
    
    /**
     * 
     * @param attrName sessionAttr的key
     */
    abstract public void removeSessionAttribute(String attrName);
    
    /**
     * 该方法，暂时没被用到
     * 
     * @param type 类型
     * @param attrValue json对象
     * @return 信息
     */
    protected Object getValue(Class<?> type, String attrValue) {
        if (type == null) {
            return attrValue;
        }
        if (type.isArray()) {
            ParameterizedType pt = (ParameterizedType) type.getGenericSuperclass();
            Class<?> clazz = (Class<?>) pt.getActualTypeArguments()[0];
            try {
                return JSON.parseArray(attrValue, clazz);
            } catch (Exception e) {
                LOGGER.debug("json字符串转换失败，将" + attrValue + "转换成：" + clazz + "数组", e);
                return attrValue;
            }
        }
        try {
            return JSON.parseObject(attrValue, type);
        } catch (Exception e) {
            LOGGER.debug("json字符串转换失败，将" + attrValue + "转换成：" + type + "", e);
            return attrValue;
        }
    }
    
    /**
     * 将变量放到session中
     * 
     * @param sessionAttrMap 放到session中变量 <key>变量名 <value> 变量值的json字符串
     * @return true 成果 false 失败
     */
    @RemoteMethod
    public boolean setAttributeToSession(Map<String, String> sessionAttrMap) {
        if (sessionAttrMap != null) {
            for (Entry<String, String> map : sessionAttrMap.entrySet()) {
                putAttrToSession(map.getKey(), JSON.parseObject(map.getValue()));
            }
        }
        return true;
    }
    
    /**
     * 将变量放入到session中
     * 
     * @param key 变量名称
     * @param Value 变量值
     */
    public void putAttrToSession(String key, Object Value) {
        HttpServletRequest request = TopServletListener.getRequest();
        request.getSession().setAttribute(key, Value);
    }
    
    /**
     * 
     * @return 返回request对象
     */
    public HttpServletRequest getRequest() {
        return TopServletListener.getRequest();
    }
}
