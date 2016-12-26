/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.json;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.comtop.cip.jodd.io.FileUtil;
import com.comtop.cip.json.JSON;
import com.comtop.cip.json.JSONArray;
import com.comtop.cip.json.JSONObject;
import com.comtop.cip.json.serializer.SerializeConfig;
import com.comtop.cip.json.serializer.SimpleDateFormatSerializer;

/**
 * JSON 测试VO,将JSON转化为对象
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-14 郑重
 */
public class JsonToObjectTest {
    
    /**
     * JSON测试字符串
     * {
     * "avBar":[{
     * "barAge":1020775935,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.5595958"
     * },{
     * "barAge":-1974116886,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.18443501"
     * }],
     * "avString":["aaa","bbb","ccc"],
     * "avboolean":[true,false,true,true],
     * "avint":[1,2,3,4],
     * "bar":{
     * "barAge":1943913160,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.49188656"
     * },
     * "dddd":"2014-02-17 17:30:55",
     * "listBar":[
     * {
     * "barAge":-1787192510,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.69199216"
     * },
     * {
     * "barAge":1481328536,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.6358857"
     * },
     * {
     * "barAge":-837627150,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.36370552"
     * }
     * ],
     * "listString":[
     * "listString1",
     * "listString2",
     * "listString3"
     * ],
     * "map":{
     * "x":"s11111x",
     * "y":"s22222y",
     * "z":"s33333z"
     * },
     * "v_Date":"2014-02-17 17:30:55",
     * "vboolean":false,
     * "vbyte":64,
     * "vchar":"x",
     * "vdouble":22.203,
     * "vfloat":12.1,
     * "vint":65535,
     * "vlong":9999999,
     * "vshort":128
     * }
     */
    private static String jsonData = "";
    
    /**
     * [{
     * "barAge":1020775935,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.5595958"
     * },{
     * "barAge":-1974116886,
     * "barDate":"2014-02-17 17:30:55",
     * "barName":"sss_0.18443501"
     * }]
     */
    private static String jsonArrayData = "";
    
    /**
     * 转换配置对象
     */
    private static SerializeConfig mapping = new SerializeConfig();
    
    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(Boolean.class, new CustomFormatSerializer("test"));
        try {
            jsonData = FileUtil
                .readString(new File(
                    "D:/CIP/08_SDE/CIPStudio/workspace/cip-common/src/demo/java/com/comtop/cip/common/demo/json/json_data.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            jsonArrayData = FileUtil
                .readString(new File(
                    "D:/CIP/08_SDE/CIPStudio/workspace/cip-common/src/demo/java/com/comtop/cip/common/demo/json/json_array_data.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 
     * 主函数
     * 
     * @param args 数组
     */
    public static void main(String[] args) {
        //parseObject();
        //parse();
        //parseBean();
        parseArray();
        parseArrayBean();
    }
    
    /**
     * 把JSON文本parse成JSONObject
     */
    public static void parseObject() {
        JSONObject objJSONObject = JSON.parseObject(jsonData);
        System.out.println(objJSONObject);
        
        Map objMap = JSON.parseObject(jsonData);
        System.out.println(objMap);
    }
    
    /**
     * 把JSON文本parse为JSONObject或者JSONArray
     */
    public static void parse() {
        JSONObject objJSONObject = (JSONObject) JSON.parse(jsonData);
        System.out.println(objJSONObject);
    }
    
    /**
     * 把JSON文本parse为JavaBean
     */
    public static void parseBean() {
        FooVO objFooVO = JSON.parseObject(jsonData, FooVO.class);
        System.out.println(objFooVO.getDddd());
    }
    
    /**
     * 把JSON文本parse成JSONArray
     */
    public static void parseArray() {
        JSONArray jsonArray = JSON.parseArray(jsonArrayData);
        System.out.println(jsonArray.get(0));
    }
    
    /**
     * 把JSON文本parse成JavaBean集合
     */
    public static void parseArrayBean() {
        List<BarVO> lstFooVO = JSON.parseArray(jsonArrayData, BarVO.class);
        System.out.println(lstFooVO.get(0));
    }
}
