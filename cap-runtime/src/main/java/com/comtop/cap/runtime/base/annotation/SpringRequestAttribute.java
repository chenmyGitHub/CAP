/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/

package com.comtop.cap.runtime.base.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that indicates the session attributes that a specific handler
 * uses. This will typically list the names of model attributes which should be
 * transparently stored in the session or some conversational storage,
 * serving as form-backing beans. <b>Declared at the type level,</b> applying
 * to the model attributes that the annotated handler class operates on.
 *
 * <p><b>NOTE:</b> Session attributes as indicated using this annotation
 * correspond to a specific handler's model attributes, getting transparently
 * stored in a conversational session. Those attributes will be removed once
 * the handler indicates completion of its conversational session. Therefore,
 * use this facility for such conversational attributes which are supposed
 * to be stored in the session <i>temporarily</i> during the course of a
 * specific handler's conversation.
 *
 * <p>For permanent session attributes, e.g. a user authentication object,
 * use the traditional <code>session.setAttribute</code> method instead.
 * Alternatively, consider using the attribute management capabilities of the
 * generic {@link org.springframework.web.context.request.WebRequest} interface.
 *
 * <p><b>NOTE:</b> When using controller interfaces (e.g. for AOP proxying),
 * make sure to consistently put <i>all</i> your mapping annotations - such as
 * <code>@RequestMapping</code> and <code>@SessionAttributes</code> - on
 * the controller <i>interface</i> rather than on the implementation class.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * 注解用于处理在spring模式下，action传入页面的attribute属性
 * 
 * 
 * @author 罗珍明
 * @since jdk1.6
 * @version 2016年5月18日 罗珍明
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SpringRequestAttribute {

	/**
	 * The name of the model attribute to bind to.
	 * <p>The default model attribute name is inferred from the declared
	 * attribute type (i.e. the method parameter type or method return type),
	 * based on the non-qualified class name:
	 * e.g. "orderAddress" for class "mypackage.OrderAddress",
	 * or "orderAddressList" for "List&lt;mypackage.OrderAddress&gt;".
	 */
	String value();
}