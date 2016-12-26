/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.corm.resource.util.CollectionUtils;

/**
 * 自动编译工具类
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-2-2 李忠文
 */
public final class AutoCompileUtil {
    
    /** 日志 */
    private static Logger log = LoggerFactory.getLogger(AutoCompileUtil.class);
    
    /** 工具 */
    private static AutoCompileUtil instance;
    
    /** 依赖包 */
    private String classPath;
    
    /**
     * 构造函数
     */
    private AutoCompileUtil() {
        
    }
    
    /**
     * 获取自动编译工具实例
     * 
     * @return 自动编译工具实例
     */
    public static AutoCompileUtil getInstance() {
        if (instance == null) {
            synchronized (AutoCompileUtil.class) {
                if (instance == null) {
                    init();
                }
            }
        }
        return instance;
    }
    
    /**
     * 工具初始化
     */
    private static void init() {
        instance = new AutoCompileUtil();
        instance.classPath = "";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Set<String> set = new LinkedHashSet<String>();
        initAllClassPath(loader, set);
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> it = set.iterator(); it.hasNext();) {
            builder.append(it.next());
            builder.append(";");
        }
        instance.classPath = builder.toString();
        log.info("Current compile class path is {}", instance.classPath);
    }
    
    /***
     * 处理当前服务所有的classPath
     * 
     * @param loader 类加载器
     * 
     * @param set ,当前已有的classpath容器，LinkedHashSet<String>类型
     */
    private static void initAllClassPath(ClassLoader loader, Set<String> set) {
        // Set<String> set=new LinkedHashSet<String>();
        if (loader == null) {
            return;
        }
        
        URL[] urls = null;
        if (loader instanceof URLClassLoader) {
            urls = ((URLClassLoader) loader).getURLs();
        } else {
            urls = new URL[] {};
        }
        File jar = null;
        for (URL url : urls) {
            jar = new File(url.getFile());
            set.add(jar.getAbsolutePath().replace('\\', '/'));
        }
        initAllClassPath(loader.getParent(), set);
    }
    
    /**
     * 
     * 执行代码编译
     * 
     * @param sourcePath 源代码路径
     * 
     * @param targetPath 目标路径
     */
    public void compile(String sourcePath, String targetPath) {
        List<File> srcs = listSourceFiles(sourcePath);
        if (CollectionUtils.isEmpty(srcs)) {
            return;
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, Locale.CHINESE,
            Charset.forName("UTF-8"));
        Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjectsFromFiles(srcs);
        
        Iterable<String> options = Arrays.asList(new String[] { "-d", targetPath, "-g:lines,vars,source", "-classpath",
            classPath });
        
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        CompilationTask compilerTask = compiler.getTask(null, manager, diagnostics, options, null, compilationUnits);
        try {
            boolean status = compilerTask.call();
            if (!status) {// If compilation error occurs
                StringBuilder trace = new StringBuilder();
                /* Iterate through each compilation problem and print it */
                for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                    trace.append(String.format("Error on line %d in %s\n", diagnostic.getLineNumber(), diagnostic));
                }
                log.error(trace.toString());
            }
        } catch (Exception e) {
            log.error("auto compile source failed.", e);
        }
        try {
            manager.close();// Close the file manager
        } catch (IOException ex) {
            log.error("Failed to close the file manager", ex);
        }
    }
    
    /**
     * 获取完整的源代码路径
     * 
     * @param sourcePath 源代码路径
     * @return 完整的源代码路径
     */
    private List<File> listSourceFiles(String sourcePath) {
        File root = new File(sourcePath);
        if (!root.exists()) {
            return null;
        }
        List<File> files = new LinkedList<File>();
        if (root.isFile()) {
            files.add(root);
            return files;
        }
        listJavaFiles(root, files);
        return files;
    }
    
    /**
     * 遍历所有的包
     * 
     * @param root 根路径
     * @param srcs 源文件
     */
    private void listJavaFiles(File root, List<File> srcs) {
        File[] files = root.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                srcs.add(file);
            } else if (file.isDirectory()) {
                listJavaFiles(file, srcs);
            }
        }
    }
}
