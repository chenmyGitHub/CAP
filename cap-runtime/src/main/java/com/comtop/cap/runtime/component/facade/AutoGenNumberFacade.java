/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.component.facade;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.component.appservice.BizSequenceAppService;
import com.comtop.cap.runtime.component.model.BizSequenceVO;
import com.comtop.cap.runtime.juel.JuelMethodExtContainer;
import com.comtop.cip.jodd.petite.meta.PetiteBean;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.runtime.core.AppBeanUtil;

import de.odysseus.el.util.SimpleContext;

/**
 * 业务序号
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-5-28 李忠文
 */
@PetiteBean
public class AutoGenNumberFacade {
    
	/** 日志对象 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoGenNumberFacade.class);
    
    /***
     * 
     * @param seqKey 序列号编码
     * @param suffixLenght 序列长度
     * @param firstNo 序列起始值
     * @param step 序列步长
     * @return 序列编码
     */
    public String getCodeSuffixBySeq(String seqKey,int suffixLenght,int firstNo,int step){
    	BizSequenceAppService objService = AppBeanUtil.getBean(BizSequenceAppService.class);
        if (objService == null) {
            throw new RuntimeException("没有找到BizSequenceAppService！");
        }
        int iLast = firstNo;
        BizSequenceVO objSeq = objService.loadBizSequenceByKey(seqKey);
        if (objSeq == null) {
            objSeq = new BizSequenceVO();
            objSeq.setKey(seqKey);
        } else {
            iLast = objSeq.getLast() + step;
        }
        objSeq.setLast(iLast);
        objService.saveBizSequenceByKey(objSeq);
        String strResult = String.valueOf(iLast).replace(",", "");
        int iRepeatTime = suffixLenght - strResult.length();
        if(iRepeatTime > 0){
        	String strFill = StringUtil.repeat("0", iRepeatTime);
        	return strFill+strResult;
        }
        return strResult;
    }
    
    
    /**
     * 生成编码
     * 
     * 
     * @param expression 表达式
     * @param param 参数
     * @return 编码
     */
    public String genNumber(final String expression, final Map<?, ?> param) {
    	ExpressionFactory factory = ExpressionFactory.newInstance();
		SimpleContext context = new SimpleContext();
		
		JuelMethodExtContainer.injectFunction2JuelContext(context);
		if(param != null) {
			for (Map.Entry<?, ?> objEntry : param.entrySet()) {
	            try {
	            	String strValue = URLDecoder.decode(String.valueOf(objEntry.getValue()),"UTF-8");
	            	context.setVariable(String.valueOf(objEntry.getKey()), factory.createValueExpression(strValue, String.class));
	            	context.setVariable("date", factory.createValueExpression(new Date(), Date.class));
	            } catch (UnsupportedEncodingException e) {
					LOGGER.error("根据utf-8解码出错:" + e);
			        throw new RuntimeException("根据utf-8解码出错！", e);
				}
	        }
		}
		ValueExpression exeExp = factory.createValueExpression(context, expression, String.class);
		return (String) exeExp.getValue(context);
    }
    
//
//    /**
//     * 生成编码
//     * 
//     * 
//     * @param expression 表达式
//     * @param param 参数
//     * @return 编码
//     */
//    public String genNumberFromFreemark(final String expression, final Map<?, ?> param) {
//        
//        Map<String, Object> objParams = new HashMap<String, Object>();
//        objParams.put("seq", new BizSequenceMethodModel());
//        objParams.put("date", new Date());
//        if (param != null && !param.isEmpty()) {
//            for (Map.Entry<?, ?> objEntry : param.entrySet()) {
//                try {
//					objParams.put(String.valueOf(objEntry.getKey()), URLDecoder.decode(String.valueOf(objEntry.getValue()),"UTF-8"));
//				} catch (UnsupportedEncodingException e) {
//					LOGGER.error("根据utf-8解码出错:" + e);
//			        throw new RuntimeException("根据utf-8解码出错！", e);
//				}
//            }
//        }
//        Configuration objConfig = new Configuration();
//        Template objTemplate;
//        StringWriter objWriter = null;
//        String strNumber = null;
//        try {
//            objWriter = new StringWriter();
//            objTemplate = new Template("gen_number", expression, objConfig);
//            objTemplate.process(objParams, objWriter);
//            strNumber = objWriter.getBuffer().toString();
//        } catch (TemplateException e) {
//        	LOGGER.error("根据规则生成编码出错:" + e);
//            throw new RuntimeException("根据规则生成编码出错！", e);
//        } catch (IOException e) {
//        	LOGGER.error("根据规则生成编码出错:" + e);
//            throw new RuntimeException("根据规则生成编码出错！", e);
//        } finally {
//            IOUtil.closeQuietly(objWriter);
//        }
//        return strNumber;
//    }
    
    /**
     * 生成编码
     * 
     * 
     * @param expression 表达式
     * @param param 参数
     * @param count 个数
     * @return 编码
     */
    public List<String> genNumber(final String expression, final Map<?, ?> param, final int count) {
        List<String> result = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            result.add(genNumber(expression, param));
        }
        return result;
    }
}
