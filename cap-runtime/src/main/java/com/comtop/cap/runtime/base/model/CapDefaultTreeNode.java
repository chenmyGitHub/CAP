/**
 * 
 */
package com.comtop.cap.runtime.base.model;

import java.util.Map;

import com.comtop.cap.runtime.base.util.CapRuntimeUtils;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.cip.json.JSONObject;
import com.comtop.top.core.util.tree.AbstractTree;

/**
 * @author luozhenming
 *
 */
public class CapDefaultTreeNode extends AbstractTree<Object> {
	
	/***/
	private Map<String, Object> beanMap ;

	/***/
	private Object bean;
	
	/***/
	private JSONObject treeParam;
	
	/**
	 * 
	 * @param jsonParam xx
	 */
	public void setTreeParam(String jsonParam){
		this.treeParam = JSONObject.parseObject(jsonParam);
	}

	/**
	 * @param bean the bean to set
	 */
	public void setBean(Object bean) {
		if(bean.equals(this.bean)){
			return;
		}
		this.bean = bean;
		this.beanMap= CapRuntimeUtils.beanConvertToMap(this.bean);
	}

	/**
	 * 通过javabean的key获取当前bean的string值
	 * 
	 * @param key
	 *            javabean的key
	 * @return 获取javabean的值
	 */
	public String getString(String key) {
		return StringUtil.toSafeString(beanMap.get(key));
	}
	
	@Override
	public String getTitle(Object vo) {
		this.setBean(vo);
		return getString(treeParam.getString("title"));
	}

	@Override
	public String getKey(Object vo) {
		this.setBean(vo);
		return getString(treeParam.getString("key"));
	}

	@Override
	public String getParentKey(Object vo) {
		this.setBean(vo);
		return getString(treeParam.getString("parentKey"));
	}

	@Override
	public Boolean isLazy(Object vo) {
		return Boolean.valueOf(treeParam.getString("isLazy"));
	}

	@Override
	public String getIcon(Object vo) {
		if (StringUtil.isNotEmpty(treeParam.getString("icon"))) {
			return treeParam.getString("icon");
		}
		return super.getIcon(vo);
	}

	@Override
	public Boolean isExpand(Object vo) {
		if (StringUtil.isNotEmpty(treeParam.getString("isExpand"))) {
			return Boolean.valueOf(treeParam.getString("isExpand"));
		}
		return super.isExpand(vo);
	}
		
}
