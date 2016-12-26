/******************************************************************************
* Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
* All Rights Reserved.
* 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
* 复制、修改或发布本软件.
*****************************************************************************/

package com.comtop.cip.common.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * @author  杨赛
 * @since   jdk1.6
 * @version 2016年12月1日 杨赛
 */
public class ImageIUtilTest {

	/**
	 * FIXME 
	 * @throws IOException IOException
	 */
	@Test
	public final void testGetImageSize() throws IOException {
		int[] size = ImageIUtil.getImageSize(this.getClass().getClassLoader().getResourceAsStream("testImage/MeetingEdit.png"));
		assertEquals(size[0], 1054);
		assertEquals(size[1], 599);

		size = ImageIUtil.getImageSize(this.getClass().getClassLoader().getResourceAsStream("testImage/MeetingList.png"));
		assertEquals(size[0], 1054);
		assertEquals(size[1], 114);

		size = ImageIUtil.getImageSize(this.getClass().getClassLoader().getResourceAsStream("testImage/MeetingTab.png"));
		assertEquals(size[0], 1054);
		assertEquals(size[1], 67);
	}

}
