/**
 * 
 */

package com.comtop.cap.runtime.base.json;

import java.io.IOException;
import java.sql.Timestamp;

import comtop.soa.com.fasterxml.jackson.core.JsonParser;
import comtop.soa.com.fasterxml.jackson.core.JsonProcessingException;
import comtop.soa.com.fasterxml.jackson.databind.DeserializationContext;
import comtop.soa.com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer;

/**
 * 重写jackson的TimastampDeserializer
 *
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2015年11月25日 凌晨
 */
public class CapTimeStampDeserise extends TimestampDeserializer {
    
    /** 序列化版本号 */
    private static final long serialVersionUID = 1L;
    
    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
        JsonProcessingException {
        String strd = jp.getText();
        if (strd == null || strd.trim().length() == 0) {
            return null;
        }
        String strVal = jp.getText().trim();
        
        long times = DatePatterner.parse(strVal);
        if (times != -1L) {
            return new java.sql.Timestamp(times);
        }
        
        return super.deserialize(jp, ctxt);
    }
}
