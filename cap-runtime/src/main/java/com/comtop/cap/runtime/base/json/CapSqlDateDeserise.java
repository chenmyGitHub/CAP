/**
 * 
 */

package com.comtop.cap.runtime.base.json;

import java.io.IOException;
import java.sql.Date;

import comtop.soa.com.fasterxml.jackson.core.JsonParser;
import comtop.soa.com.fasterxml.jackson.core.JsonProcessingException;
import comtop.soa.com.fasterxml.jackson.databind.DeserializationContext;

/**
 * 重写jackson的SqlDateDeserializer
 *
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2015年11月25日 凌晨
 */
public class CapSqlDateDeserise extends
    comtop.soa.com.fasterxml.jackson.databind.deser.std.DateDeserializers.SqlDateDeserializer {
    
    /** 序列化版本号 */
    private static final long serialVersionUID = 1L;
    
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String strd = jp.getText();
        if (strd == null || strd.trim().length() == 0) {
            return null;
        }
        String strVal = jp.getText().trim();
        
        long times = DatePatterner.parse(strVal);
        if (times != -1L) {
            return new Date(times);
        }
        
        return super.deserialize(jp, ctxt);
    }
}
