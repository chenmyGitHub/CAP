
package com.comtop.cap.ueditor.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;

import com.comtop.ueditor.ConfigManager;
import com.comtop.ueditor.define.ActionType;
import com.comtop.ueditor.define.BaseState;
import com.comtop.ueditor.define.State;
import com.comtop.ueditor.upload.FileUploader;

/**
 * Ueditor请求响应Actiion类<br>
 * 目前主要用于响应editor附件上传
 * 
 * @author yangsai
 */
public class UeditorControlAction {
    
    /** http 请求 */
    private HttpServletRequest request = null;
    
    /** 网站根路径 */
    private String rootPath = null;
    
    /** 服务器上下文环境路径 */
    private String contextPath = null;
    
    /** 操作类型 */
    private String actionType = null;
    
    /** 操作类型 */
    private ConfigManager configManager = null;
    
    /**
     * 构造方法
     * 
     * @param request http 请求
     * @param rootPath 网站根路径
     */
    public UeditorControlAction(HttpServletRequest request, String rootPath) {
        this.request = request;
        this.rootPath = rootPath;
        this.actionType = request.getParameter("action");
        this.contextPath = request.getContextPath();
        this.configManager = ConfigManager.getInstance(this.rootPath, this.contextPath, request.getRequestURI());
    }
    
    /**
     * 执行对应操作
     * 
     * @return 结果
     */
    public String exec() {
        String callbackName = this.request.getParameter("callback");
        
        if (callbackName != null) {
            if (!validCallbackName(callbackName)) {
                return new BaseState(false, 401).toJSONString();
            }
            
            return callbackName + "(" + invoke() + ");";
        }
        
        return invoke();
    }
    
    /**
     * 调用对应的操作方法
     * 
     * @return 操作结果
     */
    private String invoke() {
        if (this.actionType == null) {
            return new BaseState(false, 101).toJSONString();
        }
        
        if ((this.configManager == null) || (!this.configManager.valid())) {
            return new BaseState(false, 102).toJSONString();
        }
        
        State state = null;
        
        // Map<String, Object> conf = null;
        ActionType objActionType = ActionType.valueOf(actionType.toUpperCase());
        switch (objActionType) {
            case CONFIG:
                return configManager.getAllConfig().toString();
            case UPLOADIMAGE:
                state = new FileUploader(configManager.getConfig(objActionType)).upload(request);
                break;
            case UPLOADSCRAWL:
                throw new NotImplementedException("涂鸦功能还未实现");
            case UPLOADVIDEO:
                state = new FileUploader(configManager.getConfig(objActionType)).upload(request);
                break;
            case UPLOADFILE:
                state = new FileUploader(configManager.getConfig(objActionType)).upload(request);
                break;
            case LISTIMAGE:
                // conf = configManager.getConfig(objActionType);
                // int start = getStartIndex();
                // state = new FileManager(conf).listFile(start);
                throw new NotImplementedException("图片列表功能还未实现");
            case CATCHIMAGE:
                // conf = this.configManager.getConfig(objActionType);
                // String[] list = request.getParameterValues((String) conf.get("fieldName"));
                // state = new ImageHunter(conf).capture(list);
                throw new NotImplementedException("网上链接获取图片还未实现");
            default:
                throw new NotImplementedException("非法的actionType");
        }
        
        return state.toJSONString();
    }
    
    /**
     * @return index
     */
    public int getStartIndex() {
        String start = this.request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * 验证js方法是否合法
     * 
     * @param name js方法名
     * @return true 合法 false 不合法
     */
    public boolean validCallbackName(String name) {
        return name.matches("^[a-zA-Z_]+[\\w0-9_]*$");
    }
}
