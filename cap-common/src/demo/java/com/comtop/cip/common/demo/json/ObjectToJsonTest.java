/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comtop.cip.json.JSON;
import com.comtop.cip.json.serializer.SerializeConfig;
import com.comtop.cip.json.serializer.SerializerFeature;
import com.comtop.cip.json.serializer.SimpleDateFormatSerializer;

/**
 * JSON 测试VO，将对象转化为JSON
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-14 郑重
 */
public class ObjectToJsonTest {
    
    /**
     * 转换配置对象
     */
    private static SerializeConfig mapping = new SerializeConfig();
    
    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(Boolean.class, new CustomFormatSerializer("test"));
    }
    
    /**
     * 
     * 主函数
     * 
     * @param args 数组
     */
    public static void main(String[] args) {
        //toJSONString();
        toJSONStringByFormatConfig();
    }
    
    /**
     * 
     * 将对象转换为JSON字符串,如果第二个参数为true则对返回的JSON字符串进行格式美化
     * 美化前：
     * {"barAge":-1645399001,"barDate":1392367541225,"barName":"sss_0.7591177"}
     * 美化后：
     * {
     * "barAge":-1476773,
     * "barDate":1392367450677,
     * "barName":"sss_0.2380783"
     * }
     */
    public static void toJSONString() {
        String strJson = "";
        
        //单个对象转化为JSON
        BarVO objBarVO = new BarVO();
        strJson = JSON.toJSONString(objBarVO, true);
        System.out.println(strJson);
        
        //对象集合转化为JSON
        List<BarVO> barList = new ArrayList<BarVO>();
        barList.add(new BarVO());
        barList.add(new BarVO());
        strJson = JSON.toJSONString(barList, true);
        System.out.println(strJson);
        
        //HashMap转化为JSON
        Map<String, BarVO> map = new HashMap<String, BarVO>();
        map.put("a", new BarVO());
        map.put("b", new BarVO());
        map.put("c", new BarVO());
        strJson = JSON.toJSONString(map, true);
        System.out.println(strJson);
        
        //数组转化为JSON
        String[] arString = { "a", "b", "c" };
        strJson = JSON.toJSONString(arString, true);
        System.out.println(strJson);
        
        //对象数组转化为JSON
        BarVO[] arBar = { new BarVO(), new BarVO(), new BarVO() };
        strJson = JSON.toJSONString(arBar, true);
        System.out.println(strJson);
    }
    
    /**
     * 
     * 将对象转换为JSON字符串,如果第二个参数为Config用来确定特殊类型转换方式，例如日期
     */
    public static void toJSONStringByFormatConfig() {
        String strJson = "";
        //对象转化为JSON字符串
        BarVO objBarVO = new BarVO();
        strJson = JSON.toJSONString(objBarVO, mapping, SerializerFeature.PrettyFormat);
        System.out.println(strJson);
        
        FooVO objFooVO = new FooVO();
        strJson = JSON.toJSONString(objFooVO, mapping, SerializerFeature.PrettyFormat);
        System.out.println(strJson);
    }
}
