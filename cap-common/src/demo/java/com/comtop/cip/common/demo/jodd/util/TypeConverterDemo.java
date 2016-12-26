/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import com.comtop.cip.jodd.typeconverter.Convert;
import com.comtop.cip.jodd.typeconverter.TypeConverter;
import com.comtop.cip.jodd.typeconverter.TypeConverterManager;
import com.comtop.cip.jodd.typeconverter.impl.BooleanConverter;
import com.comtop.cip.jodd.typeconverter.impl.IntegerConverter;

/**
 * 
 * jodd 类型转换工具类 使用例子
 * 
 * jodd 对每个基本类型都提供了一个对应的类型转换器，如Integer对应的类型转换器是IntegerConverter
 * 
 * 所有类型转换器都在 TypeConverterManager类中进行管理
 * 
 * 可以自定义自己的类型转换器
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-19 柯尚福
 */
public class TypeConverterDemo {
    
    /**
     * 一个简单的pojo
     */
    class A {
        
        /** 属性 */
        private String name;
        
        /**
         * @return 获取 name属性值
         */
        public String getName() {
            return name;
        }
        
        /**
         * @param name 设置 name 属性值为参数值 name
         */
        public void setName(String name) {
            this.name = name;
        }
        
    }
    
    /**
     * 一个简单的pojo
     */
    class B {
        
        /** 属性 */
        private String name;
        
        /**
         * @return 获取 name属性值
         */
        public String getName() {
            return name;
        }
        
        /**
         * @param name 设置 name 属性值为参数值 name
         */
        public void setName(String name) {
            this.name = name;
        }
        
    }
    
    /**
     * 自定义类型转换器
     * 
     * 将类A转换为类B
     * 
     */
    class MyTypeConverter implements TypeConverter<B> {
        
        /**
         * 
         * @see com.comtop.cip.jodd.typeconverter.TypeConverter#convert(java.lang.Object)
         */
        @Override
        public B convert(Object value) {
            
            if (!(value instanceof A)) {
                return null;
            }
            A a = (A) value;
            B b = new B();
            b.setName("B:" + a.getName());
            return b;
        }
        
    }
    
    /**
     * 类型转换例子
     * 
     */
    public void convert() {
        
        Integer num = new IntegerConverter().convert("234");
        System.out.println(num.toString());
        
        Boolean bool1 = new BooleanConverter().convert(0);
        Boolean bool2 = new BooleanConverter().convert(1);
        System.out.println(bool1);
        System.out.println(bool2);
        
        // 另外一些转换方法：
        TypeConverter tc = TypeConverterManager.lookup(String.class);
        String a = (String) tc.convert(Integer.valueOf(123));
        System.out.println(a);
        
        Integer i = Convert.toInteger("173");
        System.out.println(i);
        
        Integer j = TypeConverterManager.convertType("173", Integer.class);
        System.out.println(j);
    }
    
    /**
     * 自定义类型转换器
     * 
     */
    public void customConvert() {
        
        A a = new A();
        a.setName("hello");
        B b = new MyTypeConverter().convert(a);
        System.out.println(b.getName());
        
        // 另一种用法：将自定义转换器注册到TypeConverterManager， 通过TypeConverterManager进行转换
        TypeConverterManager.register(B.class, new MyTypeConverter());
        B b2 = TypeConverterManager.convertType(a, B.class);
        System.out.println(b2.getName());
        
    }
    
    /**
     * jodd 类型转换器 例子测试
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        
        TypeConverterDemo demo = new TypeConverterDemo();
        // 常见各种类型转换
        demo.convert();
        
        // 自定义类型转换器
        demo.customConvert();
    }
}
