/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cip.common.demo.validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.comtop.cip.common.demo.validator.custom.Script;
import com.comtop.cip.common.demo.validator.custom.StringLength;
import com.comtop.cip.validator.javax.validation.Valid;
import com.comtop.cip.validator.javax.validation.constraints.AssertFalse;
import com.comtop.cip.validator.javax.validation.constraints.AssertTrue;
import com.comtop.cip.validator.javax.validation.constraints.DecimalMax;
import com.comtop.cip.validator.javax.validation.constraints.DecimalMin;
import com.comtop.cip.validator.javax.validation.constraints.Digits;
import com.comtop.cip.validator.javax.validation.constraints.Future;
import com.comtop.cip.validator.javax.validation.constraints.Max;
import com.comtop.cip.validator.javax.validation.constraints.Min;
import com.comtop.cip.validator.javax.validation.constraints.NotNull;
import com.comtop.cip.validator.javax.validation.constraints.Null;
import com.comtop.cip.validator.javax.validation.constraints.Past;
import com.comtop.cip.validator.javax.validation.constraints.Pattern;
import com.comtop.cip.validator.javax.validation.constraints.Size;
import com.comtop.cip.validator.org.hibernate.validator.constraints.Email;
import com.comtop.cip.validator.org.hibernate.validator.constraints.NotEmpty;

/**
 * 校验规则配置类，里面所有预制值校验都失败
 * 
 * @Null 验证对象是否为空
 * @NotNull 验证对象是否为非空
 * @AssertTrue 验证 Boolean 对象是否为 true
 * @AssertFalse 验证 Boolean 对象是否为 false
 * @Min 验证 Number 和 String 对象是否大等于指定的值
 * @Max 验证 Number 和 String 对象是否小等于指定的值
 * @DecimalMin 验证 Number 和 String 对象是否大等于指定的值，小数存在精度
 * @DecimalMax 验证 Number 和 String 对象是否小等于指定的值，小数存在精度
 * @Size 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内
 * @Digits 验证 Number 和 String 的构成是否合法
 * @Past 验证 Date 和 Calendar 对象是否在当前时间之前
 * @Future 验证 Date 和 Calendar 对象是否在当前时间之后
 * @Pattern 验证 String 对象是否符合正则表达式的规则
 * 
 * @author 郑重
 * @version jdk1.5
 * @version 2014-1-22 郑重
 */
public class DemoVO {
	/**
	 * 校验对象不允许为null
	 */
	@NotNull
	private String notNullValue = null;

	/**
	 * 校验对象必须为null
	 */
	@Null
	private String nullValue = "test";

	/**
	 * 校验Boolean，boolean类型是否为真
	 */
	@AssertTrue
	private boolean boolTrueValue = false;

	/**
	 * 校验Boolean，boolean类型是否为假
	 */
	@AssertFalse
	private boolean boolFalseValue = true;

	/**
	 * 校验BigDecimal,BigInteger,String,byte,short,int,long,double,float类型是为对应浮点型
	 * integer：整数位数 fraction：小数位数
	 */
	@Digits(integer = 2, fraction = 2)
	private double digitsValue = 11.111;

	/**
	 * 校验BigDecimal,BigInteger,String,byte,short,int,long,double,float类型是为对应浮点型
	 * integer：整数位数 fraction：小数位数
	 */
	@Digits(integer = 2, fraction = 2)
	private String digitsStringValue = "11.111";

	/**
	 * 校验java.util.Date,java.util.Calendar检查给定的日期是否比现在晚
	 */
	@Future
	private Date date1Value = new Date(System.currentTimeMillis() - 10000);

	/**
	 * 校验java.util.Date,java.util.Calendar检查给定的日期是否比现在早
	 */
	@Past
	private Date date2Value = new Date(System.currentTimeMillis() + 10000);

	/**
	 * 校验数字BigDecimal,BigInteger,byte,short,int,long,
	 * String检查该值是否小于或等于约束条件中指定的最大值， 用来做数字校验如果是字符串会转化为数字在判断
	 */
	@Max(5)
	private int maxValue = 6;

	/**
	 * 校验数字BigDecimal,BigInteger,byte,short,int,long,String检查该值是否小于或等于约束条件中值，
	 * 用来做数字校验如果是字符串会转化为数字在判断
	 */
	@Max(5)
	private String maxStringValue = "6";

	/**
	 * 校验数字BigDecimal,BigInteger,byte,short,int,long,String检查该值是否大于或等于约束条件中值，
	 * 用来做数字校验如果是字符串会转化为数字在判断
	 */
	@DecimalMin("2.14")
	private double decimalMinValue = 1.33;

	/**
	 * 校验数字BigDecimal,BigInteger,byte,short,int,long,String检查该值是否小于或等于约束条件中值，
	 * 用来做数字校验如果是字符串会转化为数字在判断
	 */
	@DecimalMax("2.14")
	private double decimalMaxValue = 11.33;

	/**
	 * 校验BigDecimal,BigInteger,byte,short,int,long,String检查该值是否大于或等于约束条件中值，
	 * 用来做数字校验如果是字符串会转化为数字在判断
	 */
	@Min(2)
	private int minValue = 1;

	/**
	 * 校验String是否满足正则表达式 regexp：正则表达式
	 */
	@Pattern(regexp = "^[\u4e00-\u9fa5]{0,}$", message = "只能输入中文")
	private String patternValue = "是大方的说11";

	/**
	 * 校验String,Collection,Map,arrays长度是否满足正min和max，中文和因为都只算一个字符 min： 最小值 （可选）
	 * max: 最大值（可选）
	 */
	@Size(min = 1, max = 1)
	private String sizeValue = "的1";

	/**
	 * 校验String,Collection,Map,arrays长度是否满足正min和max，中文和因为都只算一个字符 min： 最小值 （可选）
	 * max: 最大值（可选）
	 */
	@Size(min = 1, max = 1)
	private List<String> sizeListValue = new ArrayList<String>(5);

	/**
	 * 递归的对关联对象进行校验, 如果关联对象是个集合或者数组,那么对其中的元素进行递归校验,如果是一个map,则对其中的值部分进行校验
	 */
	@Valid
	private DemoSubVO demoSubVO = new DemoSubVO();

	/**
	 * 递归的对关联对象进行校验, 如果关联对象是个集合或者数组,那么对其中的元素进行递归校验,如果是一个map,则对其中的值部分进行校验
	 */
	@Valid
	private List<DemoSubVO> demoSubVOList = new ArrayList<DemoSubVO>() {
		{
			add(new DemoSubVO());
			add(new DemoSubVO());
		}
	};

	/**
	 * 校验String,Collection,Map,arrays等集合类型不为null或empty
	 */
	@NotEmpty
	private String emptyValue = "";

	/**
	 * 电子邮件信箱格式校验
	 */
	@Email
	private String emailValue = "sdfs";

	/**
	 * 自定义校验，校验字符长度，中文算两个字符
	 */
	@StringLength(min = 0, max = 5)
	private String stringLength = "测试五个字";

	/**
	 * 脚本校验 ，调用DAO查询用户名是否已经被注册
	 */
	@Script(script = "var userDAO=new com.comtop.cip.common.demo.validator.UserDAO();" + "var result=userDAO.isRegister(input);" + "return result;", message = "{value}用户名已经被注册")
	private String scriptValue = "zhengzhong";

	/**
	 * @return the notNullValue
	 */
	public String getNotNullValue() {
		return notNullValue;
	}

	/**
	 * @param notNullValue
	 *            the notNullValue to set
	 */
	public void setNotNullValue(String notNullValue) {
		this.notNullValue = notNullValue;
	}

	/**
	 * @return the nullValue
	 */
	public String getNullValue() {
		return nullValue;
	}

	/**
	 * @param nullValue
	 *            the nullValue to set
	 */
	public void setNullValue(String nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * @return the boolTrueValue
	 */
	public boolean isBoolTrueValue() {
		return boolTrueValue;
	}

	/**
	 * @param boolTrueValue
	 *            the boolTrueValue to set
	 */
	public void setBoolTrueValue(boolean boolTrueValue) {
		this.boolTrueValue = boolTrueValue;
	}

	/**
	 * @return the boolFalseValue
	 */
	public boolean isBoolFalseValue() {
		return boolFalseValue;
	}

	/**
	 * @param boolFalseValue
	 *            the boolFalseValue to set
	 */
	public void setBoolFalseValue(boolean boolFalseValue) {
		this.boolFalseValue = boolFalseValue;
	}

	/**
	 * @return the digitsValue
	 */
	public double getDigitsValue() {
		return digitsValue;
	}

	/**
	 * @param digitsValue
	 *            the digitsValue to set
	 */
	public void setDigitsValue(double digitsValue) {
		this.digitsValue = digitsValue;
	}

	/**
	 * @return the digitsStringValue
	 */
	public String getDigitsStringValue() {
		return digitsStringValue;
	}

	/**
	 * @param digitsStringValue
	 *            the digitsStringValue to set
	 */
	public void setDigitsStringValue(String digitsStringValue) {
		this.digitsStringValue = digitsStringValue;
	}

	/**
	 * @return the date1Value
	 */
	public Date getDate1Value() {
		return date1Value;
	}

	/**
	 * @param date1Value
	 *            the date1Value to set
	 */
	public void setDate1Value(Date date1Value) {
		this.date1Value = date1Value;
	}

	/**
	 * @return the date2Value
	 */
	public Date getDate2Value() {
		return date2Value;
	}

	/**
	 * @param date2Value
	 *            the date2Value to set
	 */
	public void setDate2Value(Date date2Value) {
		this.date2Value = date2Value;
	}

	/**
	 * @return the maxValue
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * @param maxValue
	 *            the maxValue to set
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * @return the maxStringValue
	 */
	public String getMaxStringValue() {
		return maxStringValue;
	}

	/**
	 * @param maxStringValue
	 *            the maxStringValue to set
	 */
	public void setMaxStringValue(String maxStringValue) {
		this.maxStringValue = maxStringValue;
	}

	/**
	 * @return the decimalMinValue
	 */
	public double getDecimalMinValue() {
		return decimalMinValue;
	}

	/**
	 * @param decimalMinValue
	 *            the decimalMinValue to set
	 */
	public void setDecimalMinValue(double decimalMinValue) {
		this.decimalMinValue = decimalMinValue;
	}

	/**
	 * @return the decimalMaxValue
	 */
	public double getDecimalMaxValue() {
		return decimalMaxValue;
	}

	/**
	 * @param decimalMaxValue
	 *            the decimalMaxValue to set
	 */
	public void setDecimalMaxValue(double decimalMaxValue) {
		this.decimalMaxValue = decimalMaxValue;
	}

	/**
	 * @return the minValue
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * @param minValue
	 *            the minValue to set
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	/**
	 * @return the patternValue
	 */
	public String getPatternValue() {
		return patternValue;
	}

	/**
	 * @param patternValue
	 *            the patternValue to set
	 */
	public void setPatternValue(String patternValue) {
		this.patternValue = patternValue;
	}

	/**
	 * @return the sizeValue
	 */
	public String getSizeValue() {
		return sizeValue;
	}

	/**
	 * @param sizeValue
	 *            the sizeValue to set
	 */
	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
	}

	/**
	 * @return the sizeListValue
	 */
	public List<String> getSizeListValue() {
		return sizeListValue;
	}

	/**
	 * @param sizeListValue
	 *            the sizeListValue to set
	 */
	public void setSizeListValue(List<String> sizeListValue) {
		this.sizeListValue = sizeListValue;
	}

	/**
	 * @return the demoSubVO
	 */
	public DemoSubVO getDemoSubVO() {
		return demoSubVO;
	}

	/**
	 * @param demoSubVO
	 *            the demoSubVO to set
	 */
	public void setDemoSubVO(DemoSubVO demoSubVO) {
		this.demoSubVO = demoSubVO;
	}

	/**
	 * @return the demoSubVOList
	 */
	public List<DemoSubVO> getDemoSubVOList() {
		return demoSubVOList;
	}

	/**
	 * @param demoSubVOList
	 *            the demoSubVOList to set
	 */
	public void setDemoSubVOList(List<DemoSubVO> demoSubVOList) {
		this.demoSubVOList = demoSubVOList;
	}

	/**
	 * @return the emptyValue
	 */
	public String getEmptyValue() {
		return emptyValue;
	}

	/**
	 * @param emptyValue
	 *            the emptyValue to set
	 */
	public void setEmptyValue(String emptyValue) {
		this.emptyValue = emptyValue;
	}

	/**
	 * @return the emailValue
	 */
	public String getEmailValue() {
		return emailValue;
	}

	/**
	 * @param emailValue
	 *            the emailValue to set
	 */
	public void setEmailValue(String emailValue) {
		this.emailValue = emailValue;
	}

	/**
	 * @return the stringLength
	 */
	public String getStringLength() {
		return stringLength;
	}

	/**
	 * @param stringLength
	 *            the stringLength to set
	 */
	public void setStringLength(String stringLength) {
		this.stringLength = stringLength;
	}

	/**
	 * @return the scriptValue
	 */
	public String getScriptValue() {
		return scriptValue;
	}

	/**
	 * @param scriptValue
	 *            the scriptValue to set
	 */
	public void setScriptValue(String scriptValue) {
		this.scriptValue = scriptValue;
	}
}
