<#include "/common/capCopyright.ftl">

package ${modelPackage}.action;

import ${modelPackage}.action.abs.Abstract${modelName}Action;
import com.comtop.cip.jodd.madvoc.meta.MadvocAction;
import comtop.org.directwebremoting.annotations.DwrProxy;

/**
 * ${cname}Action
 * 
 * @author ${author}
 * @since 1.0
 * @version ${.now?date} ${author}
 */
@DwrProxy
@MadvocAction
public class ${modelName}Action extends Abstract${modelName}Action {

	/**
	 * 初始化页面参数
	 */
	@Override
	public void initBussinessAttr() {
		//添加页面初始化时候输出参数值
	}
}