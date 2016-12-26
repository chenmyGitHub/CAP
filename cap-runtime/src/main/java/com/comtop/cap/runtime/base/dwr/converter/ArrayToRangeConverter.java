/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.dwr.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.comtop.cap.runtime.base.model.Range;
import comtop.org.directwebremoting.ConversionException;
import comtop.org.directwebremoting.extend.ArrayOutboundVariable;
import comtop.org.directwebremoting.extend.ConvertUtil;
import comtop.org.directwebremoting.extend.Converter;
import comtop.org.directwebremoting.extend.ConverterManager;
import comtop.org.directwebremoting.extend.ErrorOutboundVariable;
import comtop.org.directwebremoting.extend.InboundContext;
import comtop.org.directwebremoting.extend.InboundVariable;
import comtop.org.directwebremoting.extend.OutboundContext;
import comtop.org.directwebremoting.extend.OutboundVariable;
import comtop.org.directwebremoting.extend.Property;
import comtop.org.directwebremoting.extend.ProtocolConstants;

/**
 * 数组转为范围
 * 
 * @author lizhiyong
 * @since jdk1.6
 * @version 2015年6月2日 lizhiyong
 */
public class ArrayToRangeConverter implements Converter {
    
    /*
     * (non-Javadoc)
     * 
     * @see org.directwebremoting.convert.BaseV20Converter#setConverterManager(org.directwebremoting.ConverterManager)
     */
    @Override
    public void setConverterManager(ConverterManager converterManager) {
        this.converterManager = converterManager;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.directwebremoting.Converter#convertInbound(java.lang.Class, org.directwebremoting.InboundVariable,
     * org.directwebremoting.InboundContext)
     */
    @Override
    public Object convertInbound(Class<?> paramType, InboundVariable data) throws ConversionException {
        if (data.isNull()) {
            return null;
        }
        String value = data.getValue();
        if (value == null || "".equals(value.trim())) {
            return null;
        }
        if (!value.startsWith(ProtocolConstants.INBOUND_ARRAY_START)
            || !value.endsWith(ProtocolConstants.INBOUND_ARRAY_END)) {
            throw new IllegalArgumentException("不支持的范围格式:" + value
                + "。范围须以‘,’分隔,且长度为2。格式为[,value]，[value,]，[value,value]");
        }
        value = value.substring(1, value.length() - 1);
        StringTokenizer st = new StringTokenizer(value, ProtocolConstants.INBOUND_ARRAY_SEPARATOR);
        int size = st.countTokens();
        if (size != 2) {
            throw new ConversionException(paramType, "不支持的范围格式:" + value
                + "。范围须以‘,’分隔,且长度为2。格式为[,value]，[value,]，[value,value]");
        }
        
        Property parent = data.getContext().getCurrentProperty();
        Property child = parent.createChild(0);
        child = converterManager.checkOverride(child);
        Class<?> componentType = child.getPropertyType();
        
        InboundContext incx = data.getContext();
        InboundVariable[] members = data.getMembers();
        try {
            Range<?> rangeModel = (Range<?>) paramType.newInstance();
            Method setStartMethod = paramType.getMethod("setStart", Object.class);
            Method setEndMethod = paramType.getMethod("setEnd", Object.class);
            
            if (members != null && members.length >= 1) {
                data.getContext().addConverted(data, paramType, rangeModel);
                Object startOutput = converterManager.convertInbound(componentType, members[0], data.getContext()
                    .getCurrentProperty());
                Object endOutput = converterManager.convertInbound(componentType, members[1], data.getContext()
                    .getCurrentProperty());
                setStartMethod.invoke(rangeModel, startOutput);
                setEndMethod.invoke(rangeModel, endOutput);
                return rangeModel;
            }
            
            Object[] outputs = new Object[size];
            for (int i = 0; i < size; i++) {
                String token = st.nextToken();
                String[] split = ConvertUtil.splitInbound(token);
                String splitType = split[ConvertUtil.INBOUND_INDEX_TYPE];
                String splitValue = split[ConvertUtil.INBOUND_INDEX_VALUE];
                InboundVariable nested = new InboundVariable(incx, null, splitType, splitValue);
                nested.dereference();
                Object output = converterManager.convertInbound(componentType, nested, data.getContext()
                    .getCurrentProperty());
                outputs[i] = output;
            }
            setStartMethod.invoke(rangeModel, outputs[0]);
            setEndMethod.invoke(rangeModel, outputs[1]);
            data.getContext().addConverted(data, paramType, rangeModel);
            return rangeModel;
        } catch (Exception e) {
            throw new ConversionException(paramType, "转换失败:" + value, e);
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.directwebremoting.Converter#convertOutbound(java.lang.Object, org.directwebremoting.OutboundContext)
     */
    @Override
    public OutboundVariable convertOutbound(Object data, OutboundContext outctx) throws ConversionException {
        
        ArrayOutboundVariable ov = new ArrayOutboundVariable(outctx);
        outctx.put(data, ov);
        // Convert all the data members
        Range<?> range = (Range<?>) data;
        int size = Array.getLength(range.getValues());
        List<OutboundVariable> ovs = new ArrayList<OutboundVariable>();
        for (int i = 0; i < size; i++) {
            OutboundVariable nested;
            try {
                nested = converterManager.convertOutbound(Array.get(data, i), outctx);
            } catch (Exception ex) {
                String errorMessage = "Conversion error for " + data.getClass().getName() + ".";
                log.warn(errorMessage, ex);
                
                nested = new ErrorOutboundVariable(errorMessage);
            }
            ovs.add(nested);
        }
        
        // Group the list of converted objects into this OutboundVariable
        ov.setChildren(ovs);
        
        return ov;
    }
    
    /**
     * The log stream
     */
    private static final Log log = LogFactory.getLog(ArrayToRangeConverter.class);
    
    /**
     * The converter manager to which we forward array members for conversion
     */
    private ConverterManager converterManager = null;
}
