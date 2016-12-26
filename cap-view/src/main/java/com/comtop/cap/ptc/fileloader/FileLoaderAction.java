/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comtop.cap.ptc.fileloader;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comtop.cap.component.loader.LoadFile;
import com.comtop.cap.component.loader.util.LoaderUtil;
import com.comtop.cip.json.JSON;

import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;
import comtop.org.directwebremoting.io.FileTransfer;

/**
 * 页面文件上传下载action
 * @author 杨赛
 */
@DwrProxy
@Controller
@RequestMapping("/FileLoaderAction")
public class FileLoaderAction
{
	/**
	 * 上传文件
	 * @param uploadFile 文件上传参数
	 * @return 文件上传信息
	 */
	@RemoteMethod
    public Object uploadFile(LoadFile uploadFile) {
        return JSON.toJSON(uploadFile);
    }
	
	/**
	 * 下载文件
	 * @param uploadKey	上传key
	 * @param uploadId	上传id
	 * @param fileName	文件名
	 * @return Object
	 * @throws UnsupportedEncodingException exception
	 */
	@RemoteMethod
	public Object downloadFile(String uploadKey, String uploadId, String fileName) throws UnsupportedEncodingException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		LoaderUtil.downLoad(buffer , uploadKey, uploadId, fileName);
		return new FileTransfer(new String(fileName.getBytes("GBK"),"iso8859-1"), "application/octet-stream", buffer.toByteArray());
	}
	
	/**
	 * 获取文件列表
	 * @param uploadKey	上传key
	 * @param uploadId	上传id
	 * @return 文件列表
	 */
	@RemoteMethod
	public Object getFileNames(String uploadKey, String uploadId) {
		if(StringUtils.isBlank(uploadId) || StringUtils.isBlank(uploadKey)) {
			return null;
		}
		return JSON.toJSON(LoaderUtil.getFileNames(uploadKey, uploadId));
	}
	
	/**
	 * 删除文件
	 * @param uploadKey	上传key
	 * @param uploadId	上传id
	 * @param fileName	文件名
	 * @return 删除信息  true 删除成功, false删除不成功 
	 */
	@RemoteMethod
	public Object deleteFile(String uploadKey, String uploadId, String fileName) {
		if(StringUtils.isBlank(uploadId) || StringUtils.isBlank(uploadKey)) {
			return false;
		}
		return LoaderUtil.delete(uploadKey, uploadId, fileName);
	}
	
}
