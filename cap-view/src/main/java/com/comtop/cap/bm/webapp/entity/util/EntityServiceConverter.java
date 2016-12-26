/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.bm.webapp.entity.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cap.bm.metadata.entity.model.DataTypeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.ExceptionVO;
import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.bm.metadata.entity.model.ParameterVO;
import com.comtop.cap.bm.util.EntityOperateUtil;
import com.comtop.cip.common.util.CAPCollectionUtils;
import com.comtop.cip.common.util.CAPStringUtils;
import com.comtop.soa.store.smc.model.ServiceVO;

/**
 * 实体对象与soaService服务对象的转换器
 * 
 * @author 李小强
 * @since 1.0
 * @version 2016年5月26日 李小强
 */
public class EntityServiceConverter {

	/**容器Bean构造器实现类名 */
	public static final String BEAN_BUILDER_CLASS_NAME="com.comtop.cap.runtime.spring.SoaBeanBuilder4Cap";
	  
    /** 方法参数类型--入参 */
    public static final int PARAMETER_TYPE_IN = 1;
    
    /** 方法参数类型--出参 */
    public static final int PARAMETER_TYPE_OUT = 2;
    
    /** 方法参数类型--异常 */
    public static final int PARAMETER_TYPE_EXCEPTION = 3;
    
    /** 方法参数编号后缀--方法入参编号后缀 */
    public final static String METHOD_INPUT_PARAM_CODE_SUFFIX = "_I";
    
    /** 方法参数编号后缀--方法出参编号后缀 */
    public final static String METHOD_OUTPUT_PARAM_CODE_SUFFIX = "_O";
    
    /** 方法参数编号后缀--方法异常参编号后缀 */
    public final static String METHOD_EXCEPTION_PARAM_CODE_SUFFIX = "_E";
	/**
	 * 构造函数
	 */
	private EntityServiceConverter() {
	}


	/**
	 * serviceMap转换器，转换内部服务签名信息
	 * 
	 * @param entityVO
	 *            实体VO
	 * @return 转换后的serviceMap
	 */
	public static Map<String, ServiceVO> convertServiceMap(EntityVO entityVO) {
		Map<String, ServiceVO> serviceMap = new HashMap<String, ServiceVO>();
		if (null == entityVO) {
			return serviceMap;
		}
		ServiceVO serviceVo = new ServiceVO();
		entityVO2ServiceVO(entityVO, serviceVo);
		serviceMap.put(serviceVo.getCode(), serviceVo);
		return serviceMap;
	}
	/***
	 * 实体vo转serviceVO
	 * @param entityVO  实体vo
	 * @param serviceVo 服务VO
	 */
	private static void entityVO2ServiceVO(EntityVO entityVO,ServiceVO serviceVo){
		serviceVo.setBuilderClass(BEAN_BUILDER_CLASS_NAME);
		serviceVo.setCode(getServiceCode(entityVO)+"Facade");//实体别名+Facade
		serviceVo.setRegisterFileName("CAP_BM");
		serviceVo.setName(entityVO.getChName());
		serviceVo.setServiceAddress(entityVO.getModelPackage()+".facade."+entityVO.getEngName()+"Facade");
		List<MethodVO> methodlist=entityVO.getMethods();
		if(CAPCollectionUtils.isEmpty(methodlist)){
			return;
		}
		//设置 方法信息
		Map<String, com.comtop.soa.store.smc.model.MethodVO> mapMethodVO= new HashMap<String, com.comtop.soa.store.smc.model.MethodVO>(methodlist.size());
		for(MethodVO method:methodlist){
			com.comtop.soa.store.smc.model.MethodVO soaMethod = new com.comtop.soa.store.smc.model.MethodVO();
			method2SoaMethodVO(serviceVo.getCode(),method, soaMethod);
			mapMethodVO.put(soaMethod.getCode(), soaMethod);
		}
		serviceVo.setMapMethodVO(mapMethodVO);
	}
	
	/**
	 * 将CAP元数据Method转换为soaMethod
	 * @param serviceCode 服务类别名
	 * @param method  CAP元数据Method
	 * @param soaMethod 转换后的soaMethod
	 */
	private static void method2SoaMethodVO(String serviceCode,MethodVO method,com.comtop.soa.store.smc.model.MethodVO soaMethod){
		soaMethod.setAlias(getMethodAliasName(method));
		soaMethod.setCode(serviceCode+"."+soaMethod.getAlias());
		soaMethod.setName(method.getEngName());
		soaMethod.setServiceCode(serviceCode);
		soaMethod.setCnName(method.getChName());
		List<com.comtop.soa.store.smc.model.ParameterVO> soaParamList=new ArrayList<com.comtop.soa.store.smc.model.ParameterVO>();
		//设置参数信息--入参
		List<ParameterVO> inputParams=method.getParameters();
		if(CAPCollectionUtils.isNotEmpty(inputParams)){
			 //处理入参
			for(ParameterVO capinputParam:inputParams){
				addInputParam(soaMethod.getCode(),capinputParam, soaParamList);
			}
		}
		//设置参数信息--出参
		DataTypeVO returnVo= method.getReturnType();
		addOutputParam(soaMethod.getCode(), returnVo, soaParamList);
		 //设置参数信息--异常
		List<ExceptionVO> exceptions= method.getExceptions();
		if(CAPCollectionUtils.isNotEmpty(exceptions)){
			 //TODO 异常暂不处理
		}
	}
	
	/***
	 * 将数据的入参加入到SOA参数集合
	 * @param methodCode 方法编号
	 * @param capParam 元数据参数对象
	 * @param soaParamList SOA参数集合
	 */
	private static void addInputParam(String methodCode,ParameterVO capParam,List<com.comtop.soa.store.smc.model.ParameterVO> soaParamList){
		com.comtop.soa.store.smc.model.ParameterVO soaParam = new com.comtop.soa.store.smc.model.ParameterVO();
		soaParam.setType(PARAMETER_TYPE_IN);
		soaParam.setMethodCode(methodCode);
		soaParam.setName(capParam.getEngName());
		soaParam.setParamClass(capParam.getDataType().getValue());
		soaParam.setSeq(capParam.getSortNo());
		soaParam.setCode(getParamCode(methodCode,PARAMETER_TYPE_IN,soaParam.getSeq()));
		//处理参数明细 ,目前用不着，可以先不拼装--李小强 2016-5-26 17:39
		soaParamList.add(soaParam);
	}
	
	/***
	 * 将数据的出参加入到SOA参数集合
	 * @param methodCode 方法编号
	 * @param returnVo 元数据出参
	 * @param soaParamList SOA参数集合
	 */
	private static void addOutputParam(String methodCode,DataTypeVO returnVo,List<com.comtop.soa.store.smc.model.ParameterVO> soaParamList){
		com.comtop.soa.store.smc.model.ParameterVO soaParam = new com.comtop.soa.store.smc.model.ParameterVO();
		soaParam.setType(PARAMETER_TYPE_OUT);
		soaParam.setMethodCode(methodCode);
		soaParam.setParamClass(getOutParamClass(returnVo));
		soaParam.setSeq(0);
		soaParam.setCode(getParamCode(methodCode,PARAMETER_TYPE_IN,soaParam.getSeq()));
		//处理参数明细
		soaParamList.add(soaParam);
	}
	
	/**
	 * 获取方法的返回真实类型
	 * @param returnVo 返回对象
	 * @return 方法的返回真实类型
	 */
	private static String getOutParamClass(DataTypeVO returnVo){
		return EntityOperateUtil.convertToReturnVal(returnVo);
	} 
	
	
    /***
     * 获取参数编号，规则：方法编号（服务类别名.服务方法别名）_I|O|EXX;
     * 
     * @param methodCode 方法编号，服务类名.服务方法别名
     * @param paramType 参数类别 1：入参；2：出参；3：异常
     * @param seq 参数顺序
     * @return 参数编号,方法编号（服务类别名.服务方法别名_i|o|eXX;
     */
	private static String getParamCode(String methodCode, int paramType, int seq) {
        String strParaCode = methodCode;
        if (PARAMETER_TYPE_IN == paramType) {
            strParaCode += METHOD_INPUT_PARAM_CODE_SUFFIX + seq;
        } else if (PARAMETER_TYPE_OUT == paramType) {
            strParaCode += METHOD_OUTPUT_PARAM_CODE_SUFFIX;
        } else if (PARAMETER_TYPE_EXCEPTION == paramType) {
            strParaCode += METHOD_EXCEPTION_PARAM_CODE_SUFFIX + seq;
        }
        return strParaCode;
    }

	/**
	 * 获取方法别名 ,如果有别名就返回别名，如果没有则使用方法名
	 * @param method 方法对象
	 * @return 获取方法别名 ,如果有别名就返回别名，如果没有则使用方法名
	 */
	private static String getMethodAliasName(MethodVO method){
		String aliasName =method.getAliasName();
		if(CAPStringUtils.isBlank(aliasName)){
			return method.getEngName();
		}
		return aliasName;
	}
	
	/***
	 * 获取服务编号,如果有别名就返回别名，如果没有则使用实体英文名，首字母小写
	 * @param entityVO 实体vo
	 * @return 服务编号。如果有别名就返回别名，如果没有则使用实体英文名，首字母小写
	 */
	private static String getServiceCode(EntityVO entityVO){
		String aliasName =entityVO.getAliasName();
		if(CAPStringUtils.isBlank(aliasName)){
			return CAPStringUtils.uncapitalize(entityVO.getEngName());
		}
		return aliasName;
	}
}
