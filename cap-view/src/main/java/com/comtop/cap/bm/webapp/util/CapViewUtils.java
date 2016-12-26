/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.database.dbobject.util.DBType;
import com.comtop.cap.bm.metadata.database.dbobject.util.DBTypeAdapter;
import com.comtop.cap.codegen.util.CapCodegenUtils;

/**
 * 
 * 代码生成工具类
 *
 * @author 龚斌
 * @since 1.0
 * @version 2015年10月19日 龚斌
 */
public final class CapViewUtils {
    
    /**
     * 构造函数
     */
    private CapViewUtils() {
        
    }
    
    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(CapViewUtils.class);
    
	/** SQL文件路径前缀 */
	private static final String SQL_FILE_PATH_PREFIX = "\\sql\\[1]default";

	/** 前缀业务名称 */
	private static final String PREFIX_BUSINESS = "\\[6]business";
    
    /**
     * 生成实体相关sql文件
     * 
     * @param projectPath 前端配置的项目路径
     * @param resourcePath 资源文件路径
     * @param pkgPath 包路径
     * @param fileName 文件名称
     * @param fileContent 文件内容
     * 
     */
    public static void writeResourceSqlFile(String projectPath, String resourcePath, String pkgPath, String fileName,
        String fileContent) {
        // 根据实体生成实体注册SOA的SQL脚本
        File objSqlFile = getFile(projectPath, resourcePath, pkgPath, fileName);
        writeFile(objSqlFile, fileContent);
    }
    
    /**
     * 生成文件名称
     * 
     * @param packageName
     *            包名
     * @param endPrefix
     *            后缀名
     * @return 文件名称
     */
    public static String getFileName(String packageName, String endPrefix) {
        if (StringUtils.isNotEmpty(packageName)) {
            if (packageName.indexOf(".") > -1) {
                String fileName = packageName.substring(packageName.lastIndexOf('.') + 1);
                // 进行字母的ascii编码前移实现首字母大写
                String value = fileName + endPrefix;
                char[] cs = value.toCharArray();
                cs[0] -= 32;
                return String.valueOf(cs);
            }
        }
        return "NewFile.sql";
    }
    
    /**
     * 根据实体VO获取模型文件
     * 
     * @param projectPath 前端配置的项目路径
     * @param resourcePath 资源文件路径
     * @param pkgPath 包路径
     * @param fileName 文件名称
     * @return 文件
     */
    public static File getFile(String projectPath, String resourcePath, String pkgPath, String fileName) {
        String strPkgPath = pkgPath.replace(".", "/");
        String strFilePath = getDirectory(projectPath, resourcePath, strPkgPath) + File.separator + File.separator
            + fileName;
        return new File(strFilePath);
    }
    
    /**
     * 获取目标文件的目录
     * 
     * 
     * @param codeDir 项目所在目录
     * @param resourceDir 资源文件路径
     * @param pkgFullPath 包路径
     * @return 目标文件所在目录
     */
    private static String getDirectory(final String codeDir, final String resourceDir, final String pkgFullPath) {
		DBType dbType = DBTypeAdapter.getDBType();
		String strPrjDir = CapCodegenUtils.fixPath(codeDir + SQL_FILE_PATH_PREFIX + "\\"+dbType.getValue() + PREFIX_BUSINESS, true);
		String strDirPath = strPrjDir;
		// strDirPath += CapCodegenUtils.fixPath(resourceDir, true);
		strDirPath += pkgFullPath;
		return strDirPath;
    }
    
    /**
     * 将字符串写入文件
     * 
     * @param dest 写入文件
     * @param file 写入文件内容
     */
    private static void writeFile(File dest, String file) {
        // 如果当前目录不存在则创建
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        BufferedWriter objWriter = null;
        try {
            if (!dest.canWrite()) {
                dest.setWritable(true);
            }
            objWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "utf-8"));
            objWriter.write(file);
            objWriter.flush();
            objWriter.close();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                if (objWriter != null) {
                    objWriter.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
