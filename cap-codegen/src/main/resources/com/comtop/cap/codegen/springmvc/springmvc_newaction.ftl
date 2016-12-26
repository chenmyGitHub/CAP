<#include "/common/capCopyright.ftl">

package ${modelPackage}.action;

import org.springframework.stereotype.Controller;

import ${modelPackage}.action.abs.Abstract${modelName}Action;
import comtop.org.directwebremoting.annotations.DwrProxy;

/**
 * ${cname}Action
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 */
@DwrProxy
@Controller
public class ${modelName}Action extends Abstract${modelName}Action {

	/**
	 * 初始化页面参数
	 */
	@Override
	public void initBussinessAttr() {
		//添加页面初始化时候输出参数值
	}
}