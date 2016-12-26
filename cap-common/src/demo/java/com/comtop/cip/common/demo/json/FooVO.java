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

/**
 * JSON 测试VO
 * 
 * @author 郑重
 * @since 1.0
 * @version 2014-2-14 郑重
 */
public class FooVO {
    
    /**
     * 字符处类型
     */
    private String vString = "vStringhehhehe";
    
    /**
     * 字符类型
     */
    private char vchar = 'x';
    
    /**
     * 
     */
    private byte vbyte = 64;
    
    /**
     * 
     */
    private short vshort = 128;
    
    /**
     * 
     */
    private int vint = 65535;
    
    /**
     * 
     */
    private long vlong = 9999999L;
    
    /**
     * 
     */
    private float vfloat = 12.1f;
    
    /**
     * 
     */
    private double vdouble = 22.203d;
    
    /**
     * 
     */
    private boolean vboolean = false;
    
    /**
     * 
     */
    private Date dddd = new Date();
    
    /**
     * 
     */
    private Date vDate = new Date();
    
    /**
     * 
     */
    private Date v_Date = new Date();
    
    /**
     * 
     */
    private Object vnull = null;
    
    /**
     * 
     */
    private String[] avString = { "aaa", "bbb", "ccc" };
    
    /**
     * 
     */
    private int[] avint = { 1, 2, 3, 4 };
    
    /**
     * 
     */
    private boolean[] avboolean = { true, false, true, true };
    
    /**
     * 
     */
    private List<String> listString = new ArrayList<String>();
    
    /**
     * 
     */
    private Map<String, String> map = new HashMap<String, String>();
    
    /**
     * 
     */
    private BarVO bar = new BarVO();
    
    /**
     * 
     */
    private BarVO[] avBar = { new BarVO(), new BarVO() };
    
    /**
     * 
     */
    private List<BarVO> listBar = new ArrayList<BarVO>();
    
    {
        listString.add("listString1");
        listString.add("listString2");
        listString.add("listString3");
        
        map.put("x", "s11111x");
        map.put("y", "s22222y");
        map.put("z", "s33333z");
        
        listBar.add(new BarVO());
        listBar.add(new BarVO());
        listBar.add(new BarVO());
    }
    
    /**
     * @return 获取 vString属性值
     */
    public String getvString() {
        return vString;
    }
    
    /**
     * @param vString 设置 vString 属性值为参数值 vString
     */
    public void setvString(String vString) {
        this.vString = vString;
    }
    
    /**
     * @return 获取 vchar属性值
     */
    public char getVchar() {
        return vchar;
    }
    
    /**
     * @param vchar 设置 vchar 属性值为参数值 vchar
     */
    public void setVchar(char vchar) {
        this.vchar = vchar;
    }
    
    /**
     * @return 获取 vbyte属性值
     */
    public byte getVbyte() {
        return vbyte;
    }
    
    /**
     * @param vbyte 设置 vbyte 属性值为参数值 vbyte
     */
    public void setVbyte(byte vbyte) {
        this.vbyte = vbyte;
    }
    
    /**
     * @return 获取 vshort属性值
     */
    public short getVshort() {
        return vshort;
    }
    
    /**
     * @param vshort 设置 vshort 属性值为参数值 vshort
     */
    public void setVshort(short vshort) {
        this.vshort = vshort;
    }
    
    /**
     * @return 获取 vint属性值
     */
    public int getVint() {
        return vint;
    }
    
    /**
     * @param vint 设置 vint 属性值为参数值 vint
     */
    public void setVint(int vint) {
        this.vint = vint;
    }
    
    /**
     * @return 获取 vlong属性值
     */
    public long getVlong() {
        return vlong;
    }
    
    /**
     * @param vlong 设置 vlong 属性值为参数值 vlong
     */
    public void setVlong(long vlong) {
        this.vlong = vlong;
    }
    
    /**
     * @return 获取 vfloat属性值
     */
    public float getVfloat() {
        return vfloat;
    }
    
    /**
     * @param vfloat 设置 vfloat 属性值为参数值 vfloat
     */
    public void setVfloat(float vfloat) {
        this.vfloat = vfloat;
    }
    
    /**
     * @return 获取 vdouble属性值
     */
    public double getVdouble() {
        return vdouble;
    }
    
    /**
     * @param vdouble 设置 vdouble 属性值为参数值 vdouble
     */
    public void setVdouble(double vdouble) {
        this.vdouble = vdouble;
    }
    
    /**
     * @return 获取 vboolean属性值
     */
    public boolean isVboolean() {
        return vboolean;
    }
    
    /**
     * @param vboolean 设置 vboolean 属性值为参数值 vboolean
     */
    public void setVboolean(boolean vboolean) {
        this.vboolean = vboolean;
    }
    
    /**
     * @return 获取 dddd属性值
     */
    public Date getDddd() {
        return dddd;
    }
    
    /**
     * @param dddd 设置 dddd 属性值为参数值 dddd
     */
    public void setDddd(Date dddd) {
        this.dddd = dddd;
    }
    
    /**
     * @return 获取 vDate属性值
     */
    public Date getvDate() {
        return vDate;
    }
    
    /**
     * @param vDate 设置 vDate 属性值为参数值 vDate
     */
    public void setvDate(Date vDate) {
        this.vDate = vDate;
    }
    
    /**
     * @return 获取 v_Date属性值
     */
    public Date getV_Date() {
        return v_Date;
    }
    
    /**
     * @param v_Date 设置 v_Date 属性值为参数值 v_Date
     */
    public void setV_Date(Date v_Date) {
        this.v_Date = v_Date;
    }
    
    /**
     * @return 获取 vnull属性值
     */
    public Object getVnull() {
        return vnull;
    }
    
    /**
     * @param vnull 设置 vnull 属性值为参数值 vnull
     */
    public void setVnull(Object vnull) {
        this.vnull = vnull;
    }
    
    /**
     * @return 获取 avString属性值
     */
    public String[] getAvString() {
        return avString;
    }
    
    /**
     * @param avString 设置 avString 属性值为参数值 avString
     */
    public void setAvString(String[] avString) {
        this.avString = avString;
    }
    
    /**
     * @return 获取 avint属性值
     */
    public int[] getAvint() {
        return avint;
    }
    
    /**
     * @param avint 设置 avint 属性值为参数值 avint
     */
    public void setAvint(int[] avint) {
        this.avint = avint;
    }
    
    /**
     * @return 获取 avboolean属性值
     */
    public boolean[] getAvboolean() {
        return avboolean;
    }
    
    /**
     * @param avboolean 设置 avboolean 属性值为参数值 avboolean
     */
    public void setAvboolean(boolean[] avboolean) {
        this.avboolean = avboolean;
    }
    
    /**
     * @return 获取 listString属性值
     */
    public List<String> getListString() {
        return listString;
    }
    
    /**
     * @param listString 设置 listString 属性值为参数值 listString
     */
    public void setListString(List<String> listString) {
        this.listString = listString;
    }
    
    /**
     * @return 获取 map属性值
     */
    public Map<String, String> getMap() {
        return map;
    }
    
    /**
     * @param map 设置 map 属性值为参数值 map
     */
    public void setMap(Map<String, String> map) {
        this.map = map;
    }
    
    /**
     * @return 获取 bar属性值
     */
    public BarVO getBar() {
        return bar;
    }
    
    /**
     * @param bar 设置 bar 属性值为参数值 bar
     */
    public void setBar(BarVO bar) {
        this.bar = bar;
    }
    
    /**
     * @return 获取 avBar属性值
     */
    public BarVO[] getAvBar() {
        return avBar;
    }
    
    /**
     * @param avBar 设置 avBar 属性值为参数值 avBar
     */
    public void setAvBar(BarVO[] avBar) {
        this.avBar = avBar;
    }
    
    /**
     * @return 获取 listBar属性值
     */
    public List<BarVO> getListBar() {
        return listBar;
    }
    
    /**
     * @param listBar 设置 listBar 属性值为参数值 listBar
     */
    public void setListBar(List<BarVO> listBar) {
        this.listBar = listBar;
    }
}
