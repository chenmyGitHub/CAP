/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.bm.webapp.systemmodel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import com.comtop.cip.jodd.petite.meta.PetiteInject;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cap.bm.webapp.systemmodel.facade.IMoveSystemModelFacade;
import com.comtop.top.core.util.constant.NumberConstant;
import com.comtop.top.core.util.tree.TreeTransformUtils;
import com.comtop.top.sys.module.facade.IModuleChangeValidate;
import com.comtop.top.sys.module.facade.IModuleFacade;
import com.comtop.top.sys.module.model.ModuleDTO;
import com.comtop.top.sys.module.model.ModuleVO;
import com.comtop.top.sys.module.util.ModuleTree;

import comtop.org.directwebremoting.annotations.DwrProxy;
import comtop.org.directwebremoting.annotations.RemoteMethod;

/**
 * 系统建模Action
 * 
 * @author 沈康
 * @since 1.0
 * @version 2014-10-15 沈康
 */

@DwrProxy
@MadvocAction
public class SystemModelAction {
    
    /** 系统模块 Facade */
    @PetiteInject
    protected IModuleFacade moduleFacade;
    
    /** 移动应用、目录、系统模块 */
    @PetiteInject
    protected IMoveSystemModelFacade moveSystemModelFacade;
    
    /**
     * 根据父节点查询子节点信息
     * 
     * @param moduleDTO
     *            模块VO,parentModuleId有值
     * @return ModelMap
     */
    @RemoteMethod
    public String queryChildrenModule(final ModuleDTO moduleDTO) {
        List<ModuleDTO> lstRootAndChildren = new ArrayList<ModuleDTO>();
        // 查询子节点
        List<ModuleDTO> lstModuleVO = null;
        // 查询父节点
        ModuleDTO objModuleDTO = null;
        
        String strJson = "";
        // 从根节点开始查询
        if (String.valueOf(NumberConstant.MINUS_ONE).equals(moduleDTO.getParentModuleId())) {
            List<ModuleDTO> lstTempModule = moduleFacade.queryChildrenModuleVOList(moduleDTO);
            if (lstTempModule != null && lstTempModule.size() > NumberConstant.ZERO) {
                objModuleDTO = lstTempModule.get(NumberConstant.ZERO);
                ModuleDTO objParentMenuVO = new ModuleDTO();
                objParentMenuVO.setParentModuleId(objModuleDTO.getModuleId());
                objParentMenuVO.setModuleType(moduleDTO.getModuleType());
                lstModuleVO = moduleFacade.queryChildrenModuleVOList(objParentMenuVO);
                lstRootAndChildren.add(objModuleDTO);
            }
        } else {
            objModuleDTO = moduleFacade.readModuleVO(moduleDTO.getParentModuleId());
            lstModuleVO = moduleFacade.queryChildrenModuleVOList(moduleDTO);
        }
        if (objModuleDTO != null && objModuleDTO.getModuleName() != null) {
            if (!String.valueOf(NumberConstant.MINUS_ONE).equals(moduleDTO.getParentModuleId())) {
                lstRootAndChildren.add(NumberConstant.ZERO, objModuleDTO);
            }
            if (lstModuleVO != null) {
                lstRootAndChildren.addAll(lstModuleVO);
            }
            
            // 返回树节点JSON
            strJson = TreeTransformUtils.listToTree(lstRootAndChildren, new ModuleTree());
        }
        return strJson;
    }
    
    /**
     * 取模块信息
     * 
     * @param moduleId
     *            模块id
     * @return 返回模块基础信息
     */
    @RemoteMethod
    public ModuleDTO getModuleInfo(String moduleId) {
        ModuleDTO objModuleDTO = moduleFacade.readModuleVO(moduleId);
        return objModuleDTO;
    }
    
    /**
     * 取模块信息
     * 
     * @return 返回模块tree数据
     */
    @RemoteMethod
    public String getAllModuleTree() {
        List<ModuleDTO> lstModuleDTO = moduleFacade.getAllModuleTree();
        // ModuleDTO rootModuleDto = null;
        // for(ModuleDTO dto : lstModuleDTO) {
        // if("-1".equals(dto.getParentModuleId())) {
        // rootModuleDto = dto;
        // break;
        // }
        // }
        // lstModuleDTO.remove(rootModuleDto);
        // lstModuleDTO.set(0, rootModuleDto);
//        return TreeTransformUtils.listToTree(lstModuleDTO, new ModuleTree());
        if ((lstModuleDTO == null) || (lstModuleDTO.isEmpty())) {
        	  return "{}";
        }
        
        ModuleTree moduleTree =  new ModuleTree();
        DefaultMutableTreeNode objTree = TreeTransformUtils.createTree(lstModuleDTO, moduleTree);
        setModuleDTOChildCount(objTree);
    	String strTree = TreeTransformUtils.subTreeString(objTree, moduleTree);
    	return strTree;
    }
    
    /**
     * 递归设置ModuleDTO的childCount
     * @param objTree objTree
     */
    private void setModuleDTOChildCount(DefaultMutableTreeNode objTree) {
    	((ModuleDTO)objTree.getUserObject()).setChildCount(objTree.getChildCount());
    	@SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> en = objTree.children();
    	while (en.hasMoreElements()) {
    		DefaultMutableTreeNode node = en.nextElement();
    		setModuleDTOChildCount(node);

		}
    }
    
    /**
     * 新增模块信息方法
     * 
     * @param moduleDTO
     *            模块基础信息
     * @return 新增成功后的模块id
     */
    @RemoteMethod
    public ModuleDTO insertModuleVO(ModuleDTO moduleDTO) {
        String strModuleId = moduleFacade.saveModule(moduleDTO);
        return moduleFacade.readModuleVO(strModuleId);
    }
    
    /**
     * 修改模块信息方法
     * 
     * @param moduleDTO
     *            模块基础信息
     * @return ModuleVO 模块信息
     */
    @RemoteMethod
    public ModuleDTO updateModuleVO(final ModuleDTO moduleDTO) {
        moduleFacade.updateModule(moduleDTO);
        return moduleFacade.readModuleVO(moduleDTO.getModuleId());
    }
    
    /**
     * 是否包含子节点
     * 
     * @param parentModuleId
     *            模块信息，关键信息moduleCode,moduleId
     * @return true表示存在，false表示不存在
     */
    @RemoteMethod
    public ModuleDTO hasSubModule(final String parentModuleId) {
        ModuleDTO objModuleDTO = moduleFacade.readModuleVO(parentModuleId);
        String objIsDelete = moduleFacade.querySubModuleCount(parentModuleId) > NumberConstant.ZERO ? "true" : "false";
        objModuleDTO.setIsDelete(objIsDelete);
        if (("true").equals(objIsDelete)) {
            return objModuleDTO;
        }
        Map<String, String> objOutPutMsg = new HashMap<String, String>();
        if (!IModuleChangeValidate.Validate.canDelete(objModuleDTO, objOutPutMsg)) {
            objModuleDTO.setIsDelete("true");
            for (String objDataKey : objOutPutMsg.keySet()) {
                objModuleDTO.setDeleteMessage(objOutPutMsg.get(objDataKey));
            }
            return objModuleDTO;
        }
        
        return objModuleDTO;
    }
    
    /**
     * 快速查询系统模块
     * 
     * @param keyword
     *            关键字
     * @return 系统模块集合
     */
    @RemoteMethod
    public List<ModuleDTO> fastQueryModule(String keyword) {
        return moduleFacade.fastQueryModule(keyword);
    }
    
    /**
     * 删除模块信息方法
     * 
     * @param moduleDTO
     *            模块基础信息
     */
    @RemoteMethod
    public void deleteModuleVO(final ModuleDTO moduleDTO) {
        List<ModuleDTO> lstModuleDTO = new ArrayList<ModuleDTO>();
        lstModuleDTO.add(moduleDTO);
        moduleFacade.deleteModule(lstModuleDTO);
    }
    
    /**
     * 名称是否存在
     * 
     * @param moduleDTO
     *            模块信息，关键信息moduleCode,moduleId
     * @return true表示存在，false表示不存在
     */
    @RemoteMethod
    public boolean isModuleNameExist(final ModuleDTO moduleDTO) {
        return moduleFacade.isModuleNameExist(moduleDTO);
    }
    
    /**
     * 编码是否存在
     * 
     * @param moduleDTO
     *            模块信息，关键信息moduleCode,moduleId
     * @return true表示存在，false表示不存在
     */
    @RemoteMethod
    public boolean isModuleCodeExist(final ModuleDTO moduleDTO) {
        return moduleFacade.isModuleCodeExist(moduleDTO);
    }
    
    /**
     * 系统模块排序时更新系统模块的sort值
     * 
     * @param type
     *            排序类型 top将sortNo+1,bottom， 将sortNo-1,up，
     *            将n和relativeNode的sortNo交换 , down，将n和relativeNode的sortNo交换
     * @param lstModuleDTO
     *            上下级关系
     * @return 返回结果
     */
    @RemoteMethod
    public List<ModuleDTO> updateModuleRelations(String type, List<ModuleDTO> lstModuleDTO) {
        ModuleDTO objSelectNode = moduleFacade.readModuleVO(lstModuleDTO.get(NumberConstant.ZERO).getModuleId());
        ModuleDTO objRelativeNode = moduleFacade.readModuleVO(lstModuleDTO.get(NumberConstant.ONE).getModuleId());
        List<ModuleDTO> lstUpdateVOs = new ArrayList<ModuleDTO>();
        int iTempSortNo = NumberConstant.ZERO;
        if ("top".equals(type)) {
            objSelectNode.setSortId(objRelativeNode.getSortId() - NumberConstant.ONE);
            lstUpdateVOs.add(objSelectNode);
        } else if ("bottom".equals(type)) {
            objSelectNode.setSortId(objRelativeNode.getSortId() + NumberConstant.ONE);
            lstUpdateVOs.add(objSelectNode);
        } else {
            iTempSortNo = objSelectNode.getSortId();
            objSelectNode.setSortId(objRelativeNode.getSortId());
            objRelativeNode.setSortId(iTempSortNo);
            lstUpdateVOs.add(objSelectNode);
            lstUpdateVOs.add(objRelativeNode);
        }
        moduleFacade.updateModuleRelations(lstUpdateVOs);
        return lstModuleDTO;
    }
    
    /**
     * FIXME 方法注释信息(此标记由Eclipse自动生成,请填写注释信息删除此标记)
     *
     * @param objMoveModel 应用、目录、系统VO
     * @param strMoveTargetId 移动目标ID
     */
    @RemoteMethod
    public void moveSystemModel(ModuleVO objMoveModel, String strMoveTargetId) {
        moveSystemModelFacade.moveSystemModel(objMoveModel, strMoveTargetId);
    }
    
    /**
     * 根据模块ID查询全路径
     *
     * @param moduleId 模块ID
     * @return 模块VO
     */
    @RemoteMethod
    public ModuleDTO queryConnectCodeByModuleId(String moduleId) {
        ModuleDTO objModuleDTO = moduleFacade.readModuleVO(moduleId);
        if (StringUtil.isBlank(objModuleDTO.getFullPath())) {
            ModuleDTO objFullModuleDTO = moduleFacade.queryConnectCodeByModuleId(moduleId);
            objModuleDTO.setFullPath(objFullModuleDTO.getFullPath());
        } else {
            objModuleDTO.setFullPath(objModuleDTO.getFullPath().substring(11));
        }
        return objModuleDTO;
    }
    
}
