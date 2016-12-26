/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.core;

import static com.comtop.cip.jodd.JoddCore.ioBufferSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.runtime.base.util.BeanContextUtil;
import com.comtop.cip.jodd.JoddCore;
import com.comtop.cip.jodd.io.StreamUtil;
import com.comtop.cip.jodd.util.StringUtil;

/**
 * Cip编译工具
 * 
 * 
 * @author 李忠文
 * @since 1.0
 * @version 2015-2-2 李忠文
 */
public final class CipCompileUtil {
    
    /** 日志 */
    private static Logger log = LoggerFactory.getLogger(AutoCompileUtil.class);
    
    /** 服务器上下文 */
    private ServletContext context;
    
    /** CIP编译工具类 */
    private static CipCompileUtil instance;
    
    /**
     * 构造函数
     */
    private CipCompileUtil() {
    }
    
    /**
     * 初始化
     * 
     * 
     * @param context 服务器上下文
     */
    public static void init(ServletContext context) {
        if (instance != null) {
            log.info("this cip compile util is inited.");
            return;
        }
        synchronized (CipCompileUtil.class) {
            if (instance == null) {
                instance = new CipCompileUtil();
                instance.context = context;
            }
        }
    }
    
    /**
     * 获取CIP编译工具实例
     * 
     * @return CIP编译工具
     * @throws Exception 未初始化异常
     */
    public static CipCompileUtil getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("this cip compile util isn't inited,please init it first.");
        }
        return instance;
    }
    
    /**
     * 编译
     * 
     * 
     * @param projectPath 项目路径
     * @param targetPath 类文件路径
     */
    public void compile(String projectPath, String targetPath) {
        if (StringUtil.isBlank(projectPath)) {
            throw new IllegalArgumentException("the project path can't be blank.");
        }
        
        String target = fixTargetPath(targetPath);
        if (StringUtil.isBlank(target)) {
            throw new RuntimeException("can't get the compile target path..");
        }
        // compile java class
        String temp = projectPath.replace('\\', '/');
        if (!temp.endsWith("/")) {
            temp += "/";
        }
        try {
            String srcPath = temp + "src/main/java";
            AutoCompileUtil.getInstance().compile(srcPath, target);
        } catch (Exception e) {
            log.error("compile java class failed.", e);
        }
        // copy resources files
        copyResources(temp, target);
        
        log.info("compile java class success.");
        
    }
    
    /**
     * 编译
     * 
     * 
     * @param projectPath 项目路径
     * @param packagePath 文件路径
     */
    public void compileModule(String projectPath, String packagePath) {
        if (StringUtil.isBlank(projectPath)) {
            throw new IllegalArgumentException("the project path can't be blank.");
        }
        
        String target = fixTargetPath(null);
        if (StringUtil.isBlank(target)) {
            throw new RuntimeException("can't get the compile target path..");
        }
        // compile java class
        String temp = projectPath.replace('\\', '/');
        if (!temp.endsWith("/")) {
            temp += "/";
        }
        try {
            String srcPath = temp + "src/main/java/" + packagePath.replace(".", "/");
            AutoCompileUtil.getInstance().compile(srcPath, target);
        } catch (Exception e) {
            log.error("compile java class failed.", e);
        }
        // copy resources files
        copyResources(temp, target);
        
        log.info("compile java class success.");
        
    }
    
    /**
     * 编译构建
     * 
     * @param projectPath 项目路径
     * @param packagePath 文件路径
     * @param isGenerateCodeModule 是否生成代码模块化
     */
    public void compileModule(String projectPath, String packagePath, boolean isGenerateCodeModule) {
        if (StringUtil.isBlank(projectPath)) {
            throw new IllegalArgumentException("the project path can't be blank.");
        }
        String target = "";
        if (isGenerateCodeModule) {
            target = projectPath + "/src/main/webapp/WEB-INF/classes";
        } else {
            target = fixTargetPath(null);
        }
        
        if (StringUtil.isBlank(target)) {
            throw new RuntimeException("can't get the compile target path..");
        }
        // compile java class
        String temp = projectPath.replace('\\', '/');
        if (!temp.endsWith("/")) {
            temp += "/";
        }
        try {
            String srcPath = temp + "src/main/java/" + packagePath.replace(".", "/");
            AutoCompileUtil.getInstance().compile(srcPath, target);
        } catch (Exception e) {
            log.error("compile java class failed.", e);
        }
        // copy resources files
        copyResources(temp, target);
        
        log.info("compile java class success.");
        
    }
    
    /**
     * 获取配置文件路径
     * 
     * 
     * @param projectPath 项目路径
     * @param targetPath 目标路径
     */
    private void copyResources(String projectPath, String targetPath) {
        String javaPath = projectPath + "src/main/java";
        File javaDir = new File(javaPath);
        if (!javaDir.exists()) {
            return;
        }
        copyResources(javaPath, javaDir, targetPath);
        String resPath = projectPath + "src/main/resources";
        File resDir = new File(resPath);
        if (!resDir.exists()) {
            return;
        }
        copyResources(resPath, resDir, targetPath);
    }
    
    /**
     * F获取配置文件路径
     * 
     * 
     * @param root 根路径
     * @param dir 目录
     * @param rootTarget 目标根路径
     */
    private void copyResources(String root, File dir, String rootTarget) {
        File files[] = dir.listFiles();
        if (files == null) {
            return;
        }
        int index = root.length();
        for (File src : files) {
            if (src.isFile() && !src.getName().endsWith(".java")) {
                copyFile(rootTarget, index, src);
            } else if (src.isDirectory()) {
                copyResources(root, src, rootTarget);
            }
        }
    }
    
    /**
     * 拷贝文件
     * 
     * 
     * @param rootTarget 目标根路径
     * @param index 源文件截取起始索引
     * @param src 源文件
     */
    private void copyFile(String rootTarget, int index, File src) {
        String filePath = src.getAbsolutePath().replace('\\', '/');
        String relativePath = filePath.substring(index);
        File dest = new File(rootTarget + relativePath);
        try {
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            doCopyFile(src, dest);
        } catch (IOException e) {
            log.error("copy file {} to {} failed!", filePath, dest.getAbsolutePath(), e);
        }
    }
    
    /**
     * 拷贝文件
     * 
     * 
     * @param src 源文件
     * @param dest 目标文件
     * @throws IOException IO异常
     */
    private static void doCopyFile(final File src, final File dest) throws IOException {
        
        // do copy file
        FileInputStream input = new FileInputStream(src);
        try {
            FileOutputStream output = new FileOutputStream(dest);
            try {
                
                byte[] buffer = new byte[JoddCore.ioBufferSize];
                int read;
                while ((read = input.read(buffer, 0, ioBufferSize)) >= 0) {
                    output.write(buffer, 0, read);
                    output.flush();
                }
                output.flush();
            } finally {
                StreamUtil.close(output);
            }
        } finally {
            StreamUtil.close(input);
        }
        
        // done
    }
    
    /**
     * 获取编译目标路径
     * 
     * 
     * @param targetPath 编译目标路径
     * @return 起始路径
     */
    private String fixTargetPath(String targetPath) {
        String target = targetPath;
        if (StringUtil.isBlank(target)) {
            try {
                // 因为runtime工程不能依赖metadata工程，所以暂时使用反射
                Class facadeClazz = Class
                    .forName("com.comtop.cap.bm.metadata.preferencesconfig.facade.PreferencesFacade");
                Method method = facadeClazz.getDeclaredMethod("getConfig", String.class);
                method.setAccessible(true);
                Object obj = BeanContextUtil.getBean("preferencesFacade");
                Object preferenceConfigVO = method.invoke(obj, "compileThermalPath");
                Class voClazz = Class.forName("com.comtop.cap.bm.metadata.preferencesconfig.model.PreferenceConfigVO");
                Method voMethod = voClazz.getDeclaredMethod("getConfigValue");
                voMethod.setAccessible(true);
                String path = (String) voMethod.invoke(preferenceConfigVO);
                
                String[] paths = null;
                
                if (StringUtil.isNotBlank(path) && path.split(";") != null) {
                    paths = path.split(";");
                }
                
                if (paths != null && paths.length > 0) {
                    target = paths[0];
                }
            } catch (Exception e) {
                log.error("load  thermal rootPath failed .", e);
            }
        }
        // re check
        if (StringUtil.isBlank(target)) {
            target = context.getRealPath("/WEB-INF/classes");
        }
        return target;
    }
}
