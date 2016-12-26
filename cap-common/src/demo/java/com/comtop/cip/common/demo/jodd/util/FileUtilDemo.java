/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cip.common.demo.jodd.util;

import java.io.File;
import java.io.IOException;

import com.comtop.cip.jodd.io.FileUtil;
import com.comtop.cip.jodd.io.FileUtilParams;

/**
 * 
 * jodd 文件操作工具类 使用列子
 * 
 * 
 * @author 柯尚福
 * @since 1.0
 * @version 2014-2-18 柯尚福
 */
public class FileUtilDemo {
    
    /**
     * 写文件操作
     * 
     * @throws IOException ioe
     * 
     */
    public static void writeFile() throws IOException {
        File file = new File("D:\\test.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        
        // 写入字节数组 （注意write系列方法都为覆盖写入，会覆盖整个文件的内容）
        FileUtil.writeBytes(file, "hello".getBytes());
        
        // 也可以直接以文件名作为参数（下面的其它方法也都提供了以文件名作为参数的）
        FileUtil.writeBytes("D:\\test.txt", "hello".getBytes());
        
        // 写入字符串(前面写入的hello会被覆盖)
        FileUtil.writeString(file, "world");
        
        // 写入字符数组
        FileUtil.writeChars(file, new char[] { 'a', 'b', 'c' });
        
        // 通过追加的方式分别写入字节、字符串、
        FileUtil.appendBytes(file, " hi，\n".getBytes());
        FileUtil.appendString(file, "my name is ksf\n");
        FileUtil.appendString(file, "what is your name\n");
        
    }
    
    /**
     * 读文件操作
     * 
     * @throws IOException ioe
     * 
     */
    public static void readFile() throws IOException {
        File file = new File("D:\\test.txt");
        
        // 将文件读入到字节数组
        byte[] bytes = FileUtil.readBytes(file);
        System.out.println("文件内容：" + bytes);
        
        // 将文件内容读出到字符串对象，
        // 参数可以是file对象也可以直接用文件名字符串（下面的其它方法也都提供了以文件名作为参数的）
        String data = FileUtil.readString(file);
        data = FileUtil.readString("D:\\test.txt");
        System.out.println("文件内容：" + data);
        
        //
        data = FileUtil.readString("D:\\test.txt");
        
        // 将文件内容读出到字符数组
        char[] chars = FileUtil.readChars(file);
        System.out.println("文件内容：" + new String(chars));
        
        // 指定字符编码，按特定编码读取文件内容
        String data2 = FileUtil.readString(file, "UTF-8");
        System.out.println("文件内容(utf-8)：" + data2);
        // 按行读取文件内容
        String[] lines = FileUtil.readLines(file);
        for (String line : lines) {
            System.out.println(line);
        }
        
    }
    
    /**
     * 文件的删除、拷贝、移动等操作
     * 
     * @throws IOException ioe
     * 
     */
    public static void editFile() throws IOException {
        File file = new File("D:\\test.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        
        // 拷贝文件
        FileUtilParams params = new FileUtilParams(); // 参数
        params.setPreserveDate(true);// 创建时间是否和源文件一样
        params.setCreateDirs(true); // 当目标文件所在目录不存在时是否创建目录
        params.setEncoding("UTF-8");// 设置文件编码
        params.setOverwrite(true); // 是否覆盖已经存在的文件
        
        // 参数可以是file对象，也可以是文件名字符串（如下）
        FileUtil.copyFile(file, new File("D:\\test2.txt"), params);
        FileUtil.copyFile("D:\\test.txt", "D:\\test2.txt", params);
        
        // 拷贝目录
        FileUtil.copyDir(new File("D:\\test"), new File("D:\\test2"));
        FileUtil.copyDir("D:\\test", "D:\\test2");
        
        // 移动文件
        FileUtil.move(file, new File("E:\\test.txt"), params);
        FileUtil.move("D:\\test.txt", "E:\\test.txt", params);
        
        // 移动目录
        FileUtil.moveDir(new File("D:\\test"), new File("D:\\test2"));
        FileUtil.moveDir("D:\\test", "E:\\test");
        
        // 删除文件
        FileUtil.delete(file);
        FileUtil.delete("D:\\test.txt");
        
        // 删除目录
        FileUtil.deleteDir(new File("D:\\test"));
        FileUtil.deleteDir("D:\\test");
        
    }
    
    /**
     * jodd文件操作工具类使用例子测试
     * 
     * @param args 参数
     * @throws IOException ioe
     */
    public static void main(String[] args) throws IOException {
        
        FileUtilDemo.writeFile();
        FileUtilDemo.readFile();
        FileUtilDemo.editFile();
        
    }
    
}
