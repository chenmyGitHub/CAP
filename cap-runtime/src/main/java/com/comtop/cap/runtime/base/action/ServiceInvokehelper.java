/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.model.CapSoaParamExtendVO;
import com.comtop.cap.runtime.base.model.SoaInvokeParam;
import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cip.jodd.introspector.ClassDescriptor;
import com.comtop.cip.jodd.introspector.ClassIntrospector;
import com.comtop.cip.jodd.introspector.FieldDescriptor;
import com.comtop.cip.jodd.io.FileUtil;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.JSON;
import com.comtop.cip.json.util.IOUtils;
import com.comtop.soa.annotation.SoaMethod;
import com.comtop.soa.base.exception.SoaRuntimeException;
import com.comtop.soa.bus.channels.local.CommonCallUtil;
import com.comtop.soa.bus.exception.BusException;
import com.comtop.soa.bus.exception.MethodException;
import com.comtop.soa.common.constant.SoaBaseConstant;
import com.comtop.soa.store.finder.IServiceSearcher;
import com.comtop.soa.store.finder.ServiceSearcherFactory;
import com.comtop.soa.store.smc.model.MethodVO;
import com.comtop.soa.store.smc.model.ServiceVO;

/**
 * CAP 调用facade中的方法
 * 
 * @author 罗珍明
 * @version jdk1.6
 * @version 2015-11-04 罗珍明
 */
public class ServiceInvokehelper {
	
	/** 日誌 */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInvokehelper.class);
	
	/***/
	private static final ServiceInvokehelper INVOKE_HELPER = new ServiceInvokehelper();
	
	/**开发模式*/
	private static final String RUNNING_MODEL_DEVELOP = "develop";
	
	/**产品发布模式*/
	private static final String RUNNING_MODEL_PRODUCT = "product";
	
	/***/
	private String runningModel = RUNNING_MODEL_PRODUCT;
	
	/**
	 * 
	 * @param model 运行模式
	 */
	protected void setRunnintModel(String model){
		this.runningModel = model;
	}
	
	/**
	 * @return 运行模式
	 * 
	 */
	protected String getRunnintModel(){
		return this.runningModel;
	}
	
	/**
	 * 返回当前是否为开发模式
	 * @return true 是
	 */
	public static boolean isDevelopRunningModel(){
		return RUNNING_MODEL_DEVELOP.equals(INVOKE_HELPER.getRunnintModel());
	}
	
	/**
	 * 设置为开发模式
	 */
	public static void setRunningModelToDevelopModel(){
		if(checkIsHotReloadConfig()){
			INVOKE_HELPER.setRunnintModel(RUNNING_MODEL_DEVELOP);
		}
	}
	
	/**
	 * 
	 * @return true 配置了jrebel热加载，false 没有配置
	 */
	private static boolean checkIsHotReloadConfig(){
		try {
			Thread.currentThread().getContextClassLoader().loadClass("com.zeroturnaround.javarebel.vr");
		} catch (ClassNotFoundException e) {
			LOGGER.debug("class not found", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 */
	public static void setRunningModelToProduct(){
		INVOKE_HELPER.setRunnintModel(RUNNING_MODEL_PRODUCT);
	}
	
    /**
     * 调用soa服务
     * 
     * @param invokeParam 服务调用参数
     * @param lstParamExtendVOs soa参数
     * @return 服务调用结果
     * @throws Exception 异常
     */
    public static Object invokeSoaMethod(SoaInvokeParam invokeParam, List<CapSoaParamExtendVO> lstParamExtendVOs) throws Exception {
        Object[] objParam = getSoaMethodParam(invokeParam.getParamJosn());
        return invokeSoaService(invokeParam, objParam);
    }
    
    /**
     * 
     * @param paramJosn 页面传入参数
     * @return Object[0] 参数类型 Object[1]参数值
     */
    private static Object[] getSoaMethodParam(String[] paramJosn) {
        
        int iParamValueCount = paramJosn.length;
        
        Object[] arrInovekParamValue = new Object[iParamValueCount];
        
        Object objParam;
        
        for (int i = 0; i < paramJosn.length; i++) {
            objParam = JSON.parse(paramJosn[i]);
            if (objParam instanceof String) {
                arrInovekParamValue[i] = objParam;
            } else {
                arrInovekParamValue[i] = paramJosn[i];
            }
        }
        return arrInovekParamValue;
    }
    
    /**
     * 调用本地服务
     * 
     * @param invokeParam 服务调用参数
     * @param lstParamExtendVOs 扩展参数信息
     * @return 对象
     * @throws Exception 异常
     */
	public static Object invokeLocalBaseMethod(SoaInvokeParam invokeParam,List<CapSoaParamExtendVO> lstParamExtendVOs) throws Exception {

		Object[][] param = getMethodParam(invokeParam.getParamJosn(),
				lstParamExtendVOs);

		String strSoaServiceId = invokeParam.getSoaServiceId();

		String[] str = strSoaServiceId.split("\\.");

		Object objBean = getMethodObject(str[0]);

		String aliasName = str[1];// 方法别名

		Method[] methods = objBean.getClass().getSuperclass().getMethods();
		for (Method method : methods) {
			if (!method.isAnnotationPresent(SoaMethod.class))
				continue;

			SoaMethod soaMethod = method.getAnnotation(SoaMethod.class);

			Class<?>[] paramsCls = (Class<?>[]) param[0];
			if (soaMethod != null && aliasName.equals(soaMethod.alias())
					&& Arrays.equals(method.getParameterTypes(), paramsCls)) {
				return method.invoke(objBean, param[1]);
			}
		}

		Method objMethod = getMethod(objBean, str[1], (Class<?>[]) param[0]);

		Object obj = objMethod.invoke(objBean, param[1]);

		return obj;
	}
    
    /**
     * 
     * @param objBean facade对象
     * @param methodName 方法名称
     * @param objParamTypeClass 方法参数类型
     * @return 方法
     * @throws NoSuchMethodException 方法不存在
     * @throws SecurityException 异常
     */
    private static Method getMethod(Object objBean, String methodName, Class<?>[] objParamTypeClass)
        throws SecurityException, NoSuchMethodException {
        return objBean.getClass().getMethod(methodName, objParamTypeClass);
    }
    
    /**
     * 获取对象
     * 
     * @param beanName facade在容器中的bean名称
     * @return facade对象
     */
    private static Object getMethodObject(String beanName) {
    	return BeanContextUtil.getBean(beanName);
    }
    
    /**
     * 
     * @param paramJosn 页面传入参数
     * @param lstParamExtendVOs 服务参数类型
     * @return Object[0] 参数类型 Object[1]参数值
     * @throws Exception 异常
     */
    private static Object[][] getMethodParam(String[] paramJosn, List<CapSoaParamExtendVO> lstParamExtendVOs)
        throws Exception {
        
        int iParamCount = lstParamExtendVOs.size();
        
        Object[] arrInovekParamValue = new Object[iParamCount];
        
        int iParamSeq = 0;
        Class<?>[] paramTypeClass = new Class<?>[iParamCount];
        Class<?> objInvokeParamClass;
        for (CapSoaParamExtendVO capSoaParamExtendVO : lstParamExtendVOs) {
            iParamSeq = capSoaParamExtendVO.getSeq();
            paramTypeClass[iParamSeq] = getClassForName(capSoaParamExtendVO.getSoaParamType());
            objInvokeParamClass = getClassForName(capSoaParamExtendVO.getParamTypeFullName());
            if (iParamSeq < paramJosn.length && paramJosn[iParamSeq] != null) {
                arrInovekParamValue[iParamSeq] = JSON.parseObject(paramJosn[iParamSeq], objInvokeParamClass);
                if (objInvokeParamClass.isArray()) {
                    // 此段逻辑是为了特殊处理CapBaseFacade中deleteList方法参数问题
                    arrInovekParamValue[iParamSeq] = Arrays.asList((Object[]) arrInovekParamValue[iParamSeq]);
                }
            }
        }
        
        Object[][] obj = new Object[2][iParamCount];
        obj[0] = paramTypeClass;
        obj[1] = arrInovekParamValue;
        return obj;
    }
    
    /**
     * 调用SOA的服务，并将soa返回的json字符创转换为json对象
     * 
     * @param soaParam soa服务id
     * @param invokeParam 调用服务的参数
     * @return 服务返回结果
     * @throws Exception 异常
     */
    private static Object invokeSoaService(SoaInvokeParam soaParam, Object[] invokeParam) throws Exception {
//        ICommonCall objICommonCall = CommonCallHelper.getService();
//        Object obj;
//        if (invokeParam.length == 1) {
//            // 此段逻辑是为了判断，当目标方法只有一个参数，且改参数类型为数组或集合类时，与可变参数的处理冲突。
//            obj = objICommonCall.call(serviceId, null, invokeParam[0]);
//        } else {
//            obj = objICommonCall.call(serviceId, null, invokeParam);
//        }
//    	return transSoaResultToJson(obj);
    	Object obj;
    	if (invokeParam.length == 1) {
          // 此段逻辑是为了判断，当目标方法只有一个参数，且改参数类型为数组或集合类时，与可变参数的处理冲突。
    		obj = callSoaMethod(soaParam, invokeParam[0]);
    	} else {
    		obj = callSoaMethod(soaParam, invokeParam);
    	}
        
        return obj;
    }
    
    
	/**
	 * @param soaParam 类名
	 * @param args 参数
	 * @return object
	 * @throws Exception 异常
	 */
    private static Object callSoaMethod(SoaInvokeParam soaParam,
			Object... args) throws Exception {
    	
    	String serviceId = soaParam.getSoaServiceId();
    	String[] arrStr = serviceId.split("\\.");
    	
    	String classAlias = arrStr[0];
    	
    	String methodAlias = arrStr[1];
    	
	   //** 服务信息查询 *//*
	   IServiceSearcher SERVICE_SEARCHER = ServiceSearcherFactory.getInstance();
	   ServiceVO objServiceVO = SERVICE_SEARCHER.getService(classAlias);
	   if (null == objServiceVO) {
		   throw new BusException("10100:未找到调用的服务类:" + classAlias);
	   }
	   // 获取注册的方法信息
	   MethodVO objMethodVO = objServiceVO.getMethodVO(methodAlias);
	   if (null == objMethodVO) {
		   throw new MethodException("10101:服务类找不到指定服务方法: " + classAlias + "." + methodAlias);
	   }
	   
	   // 转化方法参数值
	   Object[] objArr = CommonCallUtil.convertInputParams(args, objMethodVO);
	   //System.out.println( objBean.save3eeeInSelf((LzmProjectVO)objArr[0])); 
	   Class<?>[] paramTypeClass = getParamTypeClass(objMethodVO.getParameterTypes());
	   try {
		   
		 Class<?> serviceClass = getClassForName(objServiceVO.getServiceAddress());
		 
		 // 获取bean实例
         Object objBean = BeanContextUtil.getBean(serviceClass);
       
         //获取反射方法
         Method objMethod = getMethod(objBean, objMethodVO.getName(), paramTypeClass);
          
         // 获取方法访问对象
         //Object objResult = CommonCallUtil.invokeMethod(objBean, objMethod, objArr);
         Object objResult = objMethod.invoke(objBean, objArr);
         
         return objResult;
         
         // 如果结果中包含VO信息，转化结果为JSON数据
         //return (R) CommonCallUtil.convertOutputParam(objResult, objMethodVO.getGenericReturnType(), null);
     } catch (RuntimeException e) {
         // 异常信息中添加服务所在应用服务器的IP、端口信息，便于定位服务调用信息
         if (e instanceof MethodException) {
             throw new MethodException(e.getMessage() + "," + SoaBaseConstant.LOCAL_HOST, e.getCause());
         } else if (e instanceof BusException) {
             throw new BusException(e.getMessage() + "," + SoaBaseConstant.LOCAL_HOST, e.getCause());
         } else if (e instanceof SoaRuntimeException) {
             throw new SoaRuntimeException(e.getMessage() + "," + SoaBaseConstant.LOCAL_HOST, e.getCause());
         } else {
             throw new RuntimeException(e.getMessage() + "," + SoaBaseConstant.LOCAL_HOST, e.getCause());
         }
     }catch(InvocationTargetException ex){
    	 if(RUNNING_MODEL_DEVELOP.equals(INVOKE_HELPER.getRunnintModel())){
    		 //以下这段逻辑，只能在开发模式下运行，
    		 //开发模式开启是在/cap/bm/common/Taglibs.jsp 这个文件中设置的。
    		 //这个taglibs文件是在通过CAP的登陆界面/cap/ptc/login/jsp/CapLogin.jsp中引入的。
    		 if(ex.getCause()!=null && ex.getCause().getCause() instanceof java.lang.NoSuchMethodError){
    			 
    			 Object objBean = newFacadeInstance(soaParam.getFacadeSeviceFullName());
    			 //获取反射方法
    			 Method objMethod = getMethod(objBean,objMethodVO.getName(), paramTypeClass);
    			 
    			 return objMethod.invoke(objBean, objArr);
    		 }
    	 }
    	 throw new RuntimeException(ex.getMessage() + "," + SoaBaseConstant.LOCAL_HOST, ex.getCause());
     }
	}
    
   /**
	 * @param facadeSeviceFullName facade全路径
	 * @return facade的实例
	 * @throws Exception 异常
	 */
    private static Object newFacadeInstance(String facadeSeviceFullName) throws Exception {
		Class<?> objClass = getClassForName(facadeSeviceFullName);
		Object objResutl = objClass.newInstance();
		ClassDescriptor objClassDescriptor = ClassIntrospector.lookup(objClass);
        FieldDescriptor[] objFieldDescriptors = objClassDescriptor.getAllFieldDescriptors();
        for (FieldDescriptor objFieldDescriptor : objFieldDescriptors) {
            objFieldDescriptor.getField().setAccessible(true);
            PetiteInject objInject = objFieldDescriptor.getField().getAnnotation(PetiteInject.class);
            if (objInject != null) {
                try {
                    String strJoddBeanName = objInject.value();
                    if (StringUtil.isEmpty(strJoddBeanName)) {
                        strJoddBeanName = objFieldDescriptor.getField().getName();
                    }
                    objFieldDescriptor.getField().set(objResutl, BeanContextUtil.getBeanFromJoddContext(strJoddBeanName));
                } catch (IllegalArgumentException e) {
                	throw new Exception("class:" + facadeSeviceFullName + ";field:"
                        + objFieldDescriptor.getField().getName(), e);
                } catch (IllegalAccessException e) {
                	throw new Exception("class:" + facadeSeviceFullName + ";field:"
                        + objFieldDescriptor.getField().getName(), e);
                } catch (Exception e) {
                	throw new Exception("给"+facadeSeviceFullName+"的实例注入属性失败.", e);
                }
            }
        }
        return objResutl;
	}

    /**
     * 
     * @param parameterTypes xx
     * @return xx
     * @throws Exception 异常
     **/
    private static Class<?>[] getParamTypeClass(Class<?>[] parameterTypes) throws Exception {
    	Class<?>[] arrClass = new Class<?>[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			arrClass[i] = getClassForName(parameterTypes[i].getName());
		}
		return arrClass;
	}

//	/**
//     * @param obj soa返回结果
//     * @return json对象
//     */
//    private static Object transSoaResultToJson(Object obj) {
//        if (obj instanceof String) {
//            try {
//                return JSON.parse((String) obj);
//            } catch (Exception e) {
//                return obj;
//            }
//        }
//        return obj;
//    }
    
    /**
     * 
     * @param classFullName 参数类型类名全路径
     * @return class 参数类型
     * @throws Exception 异常
     */
    private static Class<?> getClassForName(String classFullName) throws Exception {
        try {
            return Class.forName(classFullName);
        } catch (ClassNotFoundException e) {
            throw new Exception("未找到类" + classFullName, e);
        }
    }
    
	/**
	 * 判断当前URL是否有效 5次连接后无效即为无效的连接
	 * 
	 * @param urlStr
	 *            url地址
	 * @return boolean
	 */
	@SuppressWarnings("resource")
	public static boolean isURLConnected(String urlStr) {

		int counts = 0;
		if (urlStr == null || urlStr.length() <= 0) {
			return false;
		}

		InputStream in = null;

		while (counts < 5) {
			try {
				URL url = new URL(urlStr);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				in = con.getInputStream();
				int state = con.getResponseCode();

				File file=new File("D:/temp/atm.js");
				if(!file.exists()){
					FileUtil.mkdir("D:/temp");
					file.createNewFile();
				}
				FileOutputStream fs = new FileOutputStream(file);
				
				int byteread = 0;
				byte[] buffer = new byte[1204];
				while ((byteread = in.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				
				//通过io流读取文件内容来判断文件是否是代理

				if (in != null && state == 200) {
					return true;
				}

				return false;

			} catch (Exception ex) {
				LOGGER.debug("exception", ex);
				counts++;
			} finally {
				IOUtils.close(in);
			}
		}
		return false;
	}
}
