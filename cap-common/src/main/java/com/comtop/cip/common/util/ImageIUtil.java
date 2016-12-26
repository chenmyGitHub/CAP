/******************************************************************************
* Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
* All Rights Reserved.
* 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
* 复制、修改或发布本软件.
*****************************************************************************/

package com.comtop.cip.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 图片操作工具类
 * 
 * @author 杨赛
 * @since jdk1.6
 * @version 2016年12月1日 杨赛
 */
public class ImageIUtil {

	/**
	 * 获取对应图片的宽高
	 * 
	 * @param imageFile
	 *            图片文件
	 * @return [width, height]
	 * @throws IOException
	 *             文件读取异常
	 */
	public static int[] getImageSize(File imageFile) throws IOException {
		BufferedImage bimg = ImageIO.read(imageFile);
		int[] size = new int[2];
		size[0] = bimg.getWidth();
		size[1] = bimg.getHeight();
		return size;
	}

	/**
	 * 获取对应图片的宽高
	 * @param inputStream 图片文件流
	 * @return	[width, height]
	 * @throws IOException 文件读取异常
	 */
	public static int[] getImageSize(InputStream inputStream) throws IOException {
		BufferedImage bimg = ImageIO.read(inputStream);
		int[] size = new int[2];
		size[0]  = bimg.getWidth();
		size[1] = bimg.getHeight();
		return size;
	}
	
	/**
	 * 获取对应图片的宽高
	 * 
	 * @param url url
	 *            图片文件流
	 * @return [width, height]
	 * @throws IOException
	 *             文件读取异常
	 */
	public static int[] getImageSize(URL url) throws IOException {
		BufferedImage bimg = ImageIO.read(url);
		int[] size = new int[2];
		size[0] = bimg.getWidth();
		size[1] = bimg.getHeight();
		return size;
	}
}
