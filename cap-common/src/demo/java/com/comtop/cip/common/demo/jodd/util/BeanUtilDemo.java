/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import java.util.ArrayList;
import java.util.List;

import com.comtop.cip.jodd.bean.BeanCopy;
import com.comtop.cip.jodd.bean.BeanUtil;

/**
 * 
 * jodd BeanUtil 使用例子
 * 
 * 可用于修改bean的某个属性、获取某个属性、复制bean等
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-19 柯尚福
 */
public class BeanUtilDemo {
    
    /**
     * 一个简单的java bean
     */
    class Person {
        
        /** 姓名 **/
        private String name;
        
        /** 年龄 **/
        private int age;
        
        /** 父亲 **/
        private Person father;
        
        /** 兄弟 **/
        public List<Person> brothers;
        
        /**
         * 
         * 构造函数
         * 
         */
        public Person() {
            
        }
        
        /**
         * 
         * 构造函数
         * 
         * @param name 姓名
         * @param age 年龄
         */
        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
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
        
        /**
         * @return 获取 age属性值
         */
        public int getAge() {
            return age;
        }
        
        /**
         * @param age 设置 age 属性值为参数值 age
         */
        public void setAge(int age) {
            this.age = age;
        }
        
        /**
         * @return 获取 brothers属性值
         */
        public List<Person> getBrothers() {
            return brothers;
        }
        
        /**
         * @param brothers 设置 brothers 属性值为参数值 brothers
         */
        public void setBrothers(List<Person> brothers) {
            this.brothers = brothers;
        }
        
        /**
         * @return 获取 father属性值
         */
        public Person getFather() {
            return father;
        }
        
        /**
         * @param father 设置 father 属性值为参数值 father
         */
        public void setFather(Person father) {
            this.father = father;
        }
        
    }
    
    /**
     * 设置bean的属性值
     */
    public void setProperties() {
        String nameString = "aaa";
        String age = "33";
        
        Person person = new Person();
        
        BeanUtil.setProperty(person, "name", nameString);
        BeanUtil.setProperty(person, "age", age);// 只支持基本数据类型自动转换
        
        System.out.println(person.getName());
        System.out.println(person.getAge());
    }
    
    /**
     * 获取bean的属性值
     */
    public void getProperties() {
        
        Person person = new Person("ksf", 28);
        Object name = BeanUtil.getProperty(person, "name");
        Object age = BeanUtil.getProperty(person, "age");
        
        System.out.println("name:" + name);
        System.out.println(" age:" + age);
    }
    
    /**
     * 设置bean的 嵌套的属性（引用的其它对象，包括list map都可以）
     */
    public void setNestedProperties() {
        
        Person person = new Person("ksf", 28);
        Person father = new Person("kyc", 68);
        person.setFather(father);
        
        List<BeanUtilDemo.Person> brothers = new ArrayList<BeanUtilDemo.Person>(3);
        brothers.add(new Person());
        person.setBrothers(brothers);
        
        BeanUtil.setProperty(person, "father.name", "krd");
        System.out.println(person.getFather().getName());
        
        BeanUtil.setProperty(person, "brothers[0].age", 123);
        System.out.println(person.getBrothers().get(0).getAge());
    }
    
    /**
     * 读取bean的 嵌套的属性（引用的其它对象，包括list map都可以）
     */
    public void getNestedProperties() {
        
        Person person = new Person("ksf", 28);
        Person father = new Person("kyc", 68);
        person.setFather(father);
        
        List<BeanUtilDemo.Person> brothers = new ArrayList<BeanUtilDemo.Person>(3);
        brothers.add(new Person("aaa", 25));
        person.setBrothers(brothers);
        
        System.out.println(BeanUtil.getProperty(person, "father.name"));
        System.out.println(BeanUtil.getProperty(person, "brothers[0].age"));
    }
    
    /**
     * bean 复制
     * 使用BeanCopy 类
     * 
     */
    public void copyBean() {
        
        Person person1 = new Person("ksf", 28);
        Person person2 = new Person();
        
        // BeanCopy需要实例化，可以对拷贝操作做一些配置,例如只拷贝某些属性
        BeanCopy beanCopy = new BeanCopy(person1, person2);
        beanCopy.copy(); // 开始拷贝
        
        System.out.println(person2.getName());
    }
    
    /**
     * 判断bean中是否存在某个属性
     * 
     */
    public void isExistProperty() {
        Person person = new Person("ksf", 28);
        boolean existName = BeanUtil.hasProperty(person, "name");
        
        System.out.println("是否存在name属性：" + existName);
    }
    
    /**
     * jodd BeanUtil 例子测试
     * 
     * @param args 参数
     */
    public static void main(String[] args) {
        BeanUtilDemo beanUtilDemo = new BeanUtilDemo();
        
        // 设置读取简单属性
        beanUtilDemo.setProperties();
        beanUtilDemo.getProperties();
        
        // 设置读取嵌套属性
        beanUtilDemo.setNestedProperties();
        beanUtilDemo.getNestedProperties();
        
        // bean 复制
        beanUtilDemo.copyBean();
        
        // bean中是否存在某个属性
        beanUtilDemo.isExistProperty();
        
    }
    
}
