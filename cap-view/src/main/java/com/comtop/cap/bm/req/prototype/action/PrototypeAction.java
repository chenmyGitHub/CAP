
package com.comtop.cap.bm.req.prototype.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comtop.cap.bm.metadata.common.storage.exception.ValidateException;
import com.comtop.cap.bm.metadata.preferencesconfig.PreferenceConfigQueryUtil;
import com.comtop.cap.bm.req.prototype.design.facade.PrototypeFacade;
import com.comtop.cap.bm.req.prototype.design.facade.PrototypeVersionFacade;
import com.comtop.cap.bm.req.prototype.design.model.PrototypeVO;
import com.comtop.cap.codegen.generate.GenerateCode;
import com.comtop.cap.codegen.generate.PrototypeHTMLProcess;
import com.comtop.cap.component.loader.config.CapFileType;
import com.comtop.cap.component.loader.util.LoaderUtil;
import com.comtop.cap.doc.content.facade.DocChapterContentStructFacade;
import com.comtop.cip.graph.image.utils.GraphToImageUtil;
import com.comtop.top.component.common.systeminit.TopServletListener;
import com.comtop.top.core.jodd.AppContext;
import com.comtop.top.core.util.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 需求建模之界面原型生成html的入口类
 *
 * @author 凌晨
 * @since jdk1.6
 * @version 2016年10月31日 凌晨
 */
@DwrProxy
public class PrototypeAction {
    
    /** 日志 */
    private final static Logger LOG = LoggerFactory.getLogger(PrototypeAction.class);
    
    /**
     * 界面原型 facade
     */
    protected PrototypeFacade prototypeFacade = AppContext.getBean(PrototypeFacade.class);
    
    /**
     * DocChapterContentStructFacade
     */
    protected DocChapterContentStructFacade docChapterContentStructFacade = AppContext
        .getBean(DocChapterContentStructFacade.class);
    
    /** 版本管理接口 */
    private final PrototypeVersionFacade prototypeVersionFacade = AppContext.getBean(PrototypeVersionFacade.class);
    
    /**
     * 生成模块下所有代码
     * 
     * @param modelPackage 模块包路径
     */
    @RemoteMethod
    public void generateByPackageName(String modelPackage) {
        GenerateCode.generateByPackageName(modelPackage, new PrototypeHTMLProcess<PrototypeVO>());
    }
    
    /**
     * 根据界面原型Id生成代码
     * 
     * @param prototypeVO 界面原型对象
     * @return 界面原型对象
     * @throws ValidateException 元数据校验异常
     */
    @RemoteMethod
    public PrototypeVO generateById(PrototypeVO prototypeVO) throws ValidateException {
        // 先保存界面原型
        PrototypeVO objPrototypeVO = prototypeFacade.saveModel(prototypeVO);
        GenerateCode.generateByIdAndLayerName(objPrototypeVO.getModelPackage(), objPrototypeVO.getModelId(),
            new PrototypeHTMLProcess<PrototypeVO>());
        prototypeVersionFacade.updateCodeVersion(objPrototypeVO.getModelId(), null);
        return objPrototypeVO;
    }
    
    /**
     * 根据界面原型id集合生成代码
     * 
     * @param ids id集合
     * @param modelPackage 模块包路径
     * @throws ValidateException 验证异常
     */
    @RemoteMethod
    public void generateByIdList(List<String> ids, String modelPackage) throws ValidateException {
        GenerateCode.generateByIdListAndLayerName(modelPackage, ids, new PrototypeHTMLProcess<PrototypeVO>());
        prototypeVersionFacade.batchUpdateCodeVersion(ids);
    }
    
    /**
     * 执行生成图片名称
     * 
     * @param modelPackage 界面原型的包路径
     * @param models 界面原型ID集合
     * @param url 页面URL
     * @param imageName 图片名称
     * @param reqFunctionSubitemId 功能子项id
     * @throws JsonProcessingException json转换异常
     * @throws FileNotFoundException 文件找不到异常
     * @throws ValidateException 元数据校验异常
     */
    @RemoteMethod
    public void htmlToImage(String modelPackage, List<String> models, String url, String imageName,
        String reqFunctionSubitemId) throws JsonProcessingException, FileNotFoundException, ValidateException {
        // 从首选项中读取图片的保存路径
        String projectFilePath = PreferenceConfigQueryUtil.getCodePath();
        if (!projectFilePath.endsWith("/")) {
            projectFilePath += File.separator;
        }
        // 图片本地存储路径
        String localImagePath = projectFilePath + PreferenceConfigQueryUtil.getPrototypeImagePath();
        localImagePath += this.genImagePath(modelPackage);
        // 创建本地路径文件夹
        this.createFileDir(localImagePath);
        // 导出图片的日志路径
        String logdir = localImagePath;
        // 追加图片名称
        localImagePath += imageName;
        HttpServletRequest request = TopServletListener.getRequest();
        Cookie[] cookies = request.getCookies();
        String cookieString = null;
        if (cookies != null && cookies.length > 0) {
            ObjectMapper mapper = new ObjectMapper();
            cookieString = mapper.writeValueAsString(cookies);
            
            String baseUrl = "";
            if (StringUtil.isBlank(request.getContextPath())) {
                throw new RuntimeException("获取request.getContextPath()失败，导出界面原型图片失败");
            }
            Matcher matcher = Pattern.compile("(.*?[/]{2}.*?)" + request.getContextPath()).matcher(
                request.getRequestURL().toString());
            if (matcher.find()) {
                baseUrl = matcher.group(1) + url;
            } else {
                throw new RuntimeException("请求URL与contextPath无法匹配，导出界面原型图片失败");
            }
            
            GraphToImageUtil.screenshot(baseUrl, cookieString, 1920, 1080, localImagePath, "#pageRoot", logdir);
        } else {
            throw new RuntimeException("导出界面原型图片时，无法获取登录用户的cookie信息，可能是session已过期，请重新登录后再进行尝试");
        }
        
        // 图片服务器存储路径
        String serviceImagePath = this.genImagePath(modelPackage);
        this.uploadPrototypeImage2Service(localImagePath, serviceImagePath, imageName);
        docChapterContentStructFacade.saveReqFunctionPrototype(reqFunctionSubitemId);
        prototypeVersionFacade.batchUpdateImageVersion(models);
    }
    
    /**
     * 
     * 生成图片路径
     * 
     * @param modelPackage 界面原型modelPackage
     * @return 图片路径
     */
    private String genImagePath(String modelPackage) {
        return File.separator + CapFileType.REQ_PROTOTYPE_IMAGE_KEY + File.separator
            + modelPackage.replaceFirst("com[.]comtop[.]prototype[.]", "").replaceAll("[.]", "/") + File.separator;
    }
    
    /**
     * 创建目录路径
     * 
     * @param path 待创建的路径
     */
    private void createFileDir(String path) {
        File file = new File(path);
        this.mkDir(file);
    }
    
    /**
     * 递归创建目录
     *
     * @param file 文件
     */
    private void mkDir(File file) {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                mkDir(file.getParentFile());
            }
            boolean flag = file.mkdir();
            if (!flag) {
                throw new RuntimeException("创建目录" + file.toString() + "失败");
            }
        }
    }
    
    /**
     * 上传界面原型图片到服务器，如FTP服务器
     * 
     * @param localPath 图片的本地路径
     * @param servicePath 图片需要上传到服务器的路径
     * @param imageName 图片的文件名
     * @throws FileNotFoundException 文件找不到异常
     */
    private void uploadPrototypeImage2Service(String localPath, String servicePath, String imageName)
        throws FileNotFoundException {
        InputStream in = null;
        try {
            in = new FileInputStream(localPath);
            LoaderUtil.upLoad(in, servicePath, imageName);
        } catch (FileNotFoundException e) {
            LOG.error("导出界面原型图片，上传图片到服务器时失败。", e);
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error("上传图片到服务器后关闭I/O流失败", e);
                }
            }
        }
    }
    
    /**
     * 获取图片在服务器上访问地址的前缀，如http://10.10.5.223:8090/cap-ftp/
     *
     * @return 图片url访问前缀
     */
    @RemoteMethod
    public String getImageVisitPrefix() {
        return LoaderUtil.getVisitUrl();
    }
    
}
