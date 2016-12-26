/******************************************************************************
 * Copyright (C) 2016 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 *****************************************************************************/
package com.comtop.cap.codegen.model;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.comtop.cap.bm.metadata.common.storage.CacheOperator;
import com.comtop.cap.bm.metadata.entity.model.DataTypeVO;
import com.comtop.cap.bm.metadata.entity.model.EntityVO;
import com.comtop.cap.bm.metadata.entity.model.MethodVO;
import com.comtop.cap.bm.metadata.entity.model.ParameterVO;
import com.comtop.cap.bm.metadata.entity.model.TransferValPattern;
import com.comtop.cap.bm.metadata.entity.model.Wildcard;
import com.comtop.cap.bm.metadata.entity.model.query.QueryAttribute;
import com.comtop.cap.bm.metadata.entity.model.query.QueryModel;
import com.comtop.cap.bm.metadata.entity.model.query.Sort;
import com.comtop.cap.bm.metadata.entity.model.query.Subquery;
import com.comtop.cap.bm.metadata.entity.model.query.WhereCondition;
import com.comtop.cap.codegen.convert.DataBaseConvertFactory;
import com.comtop.cap.codegen.util.QueryModelUtil;
import com.comtop.cip.jodd.util.StringUtil;
import com.comtop.corm.resource.util.CollectionUtils;

/**
 * 查询建模包装类
 * 
 * @author 许畅
 * @since JDK1.6
 * @version 2016年8月2日 许畅 新建
 */
public final class WrapperQueryModel extends Observable {
	
	/** 空格 */
	private static final String BLANK="\t";

	/** 实体方法 */
	private final MethodVO method;

	/** 导入观察者 */
	private final Observer observer;

	/** 查询建模queryModel对象 */
	private final QueryModel queryModel;

	/** 查询建模select */
	private final String select;

	/** 查询建模from */
	private final String from;

	/** 查询建模where */
	private final String where;

	/** 查询建模orderBy */
	private final String orderBy;

	/** 查询建模groupBy */
	private final String groupBy;

	/** 查询建模完整 SQL */
	private final String sql;
	

	/**
	 * 构造方法
	 * 
	 * @param method
	 *            实体方法
	 * @param observer
	 *            导入观察者
	 */
	public WrapperQueryModel(MethodVO method, Observer observer) {
		super();
		this.observer = observer;
		this.addObserver(observer);
		this.method = method;
		this.queryModel = method.getQueryModel();
		this.select = wrapperSelect();
		this.from = wrapperFrom();
		this.where = wrapperWhere();
		this.orderBy = wrapperOrderBy();
		this.groupBy = wrapperGroupBy();
		this.sql = wrapperSQL();
	}
	
	/**
	 * 构造方法(用于子查询关联自身)
	 * 
	 * @param queryModel
	 *            查询建模
	 */
	public WrapperQueryModel(QueryModel queryModel) {
		super();
		this.observer = null;
		this.method = null;
		this.queryModel = queryModel;
		this.select = wrapperSelect();
		this.from = wrapperFrom();
		this.where = wrapperWhere();
		this.orderBy = wrapperOrderBy();
		this.groupBy = wrapperGroupBy();
		this.sql = wrapperSQL();
	}
	
	/**
	 * 包装select
	 * 
	 * @return 包装select
	 */
	private String wrapperSelect() {
		List<QueryAttribute> lst = queryModel.getSelect().getSelectAttributes();

		StringBuffer wrapperSelect = new StringBuffer();
		wrapperSelect.append(BLANK + "SELECT \n");
		if (CollectionUtils.isEmpty(lst)) {
			wrapperSelect.append(BLANK + "* \n");
		}

		for (int i = 0; i < lst.size(); i++) {
			// 分隔符
			String separator = QueryModelUtil.getCollectionSeparator(lst, i);

			QueryAttribute attribute = lst.get(i);
			if (StringUtil.isNotBlank(attribute.getSqlScript())) {
				// 形如: case t.flow_state when 1 then '进行中' when 2 then '已结束'
				// else '未发起' end state,
				wrapperSelect.append(BLANK + attribute.getSqlScript() + " "
						+ attribute.getColumnAlias() + separator + " \n");
			} else {
				// 形如:t1.fid id,
				wrapperSelect.append(BLANK + attribute.getTableAlias() + "."
						+ attribute.getColumnName() + " "
						+ attribute.getColumnAlias() + separator + " \n");
			}
		}

		return wrapperSelect.toString();
	}

	/**
	 * 包装from
	 * 
	 * @return 包装from
	 */
	private String wrapperFrom() {
		Subquery pTable = queryModel.getFrom().getPrimaryTable();
		List<Subquery> subQuerys = queryModel.getFrom().getSubquerys();

		StringBuffer wrapperFrom = new StringBuffer();
		wrapperFrom.append(BLANK+ "FROM \n");
		if (pTable.getRefQueryModel() == null) {
			wrapperFrom.append(BLANK + pTable.getSubTableName() + " "+ pTable.getSubTableAlias() + " \n");
		} else {
			// 特殊子查询关联自身
			QueryModel subQueryModel = pTable.getRefQueryModel();
			WrapperQueryModel wrapperQueryModel = new WrapperQueryModel(subQueryModel);
			// 连接类型
			wrapperFrom.append(BLANK + "( \n");
			// 子查询关联自身
			wrapperFrom.append(BLANK + wrapperQueryModel.getSql());
			wrapperFrom.append(BLANK + ") ");
			wrapperFrom.append(BLANK + pTable.getSubTableAlias() + " \n");
		}
		if (CollectionUtils.isEmpty(subQuerys)) {
			return wrapperFrom.toString();
		}

		for (Subquery subQuery : subQuerys) {
			// 是否子查询关联自身
			if (subQuery.getRefQueryModel() == null) {
				wrapperFrom.append(BLANK + subQuery.getJoinType() + " "
						+ subQuery.getSubTableName() + " "
						+ subQuery.getSubTableAlias() + " ON "
						+ subQuery.getOnLeft().getTableAlias() + "."
						+ subQuery.getOnLeft().getColumnName() + " = "
						+ subQuery.getOnRight().getTableAlias() + "."
						+ subQuery.getOnRight().getColumnName() + " \n");
			} else {
				// 特殊子查询关联自身
				QueryModel subQueryModel = subQuery.getRefQueryModel();
				WrapperQueryModel wrapperQueryModel = new WrapperQueryModel(subQueryModel);
				// 连接类型
				wrapperFrom.append(BLANK + subQuery.getJoinType() + " ( \n");
				// 子查询关联自身
				wrapperFrom.append(wrapperQueryModel.getSql());
				wrapperFrom.append(BLANK + ") \n");
				// ON条件
				wrapperFrom.append(BLANK + subQuery.getSubTableAlias() + " ON ");
				wrapperFrom.append(subQuery.getOnLeft().getTableAlias() + ".");
				wrapperFrom.append(subQuery.getOnLeft().getColumnName() + " = ");
				wrapperFrom.append(subQuery.getOnRight().getTableAlias() + ".");
				wrapperFrom.append(subQuery.getOnRight().getColumnName() + " \n");
			}
		}

		return wrapperFrom.toString();
	}

	/**
	 * 包装where
	 * 
	 * @return 包装where
	 */
	private String wrapperWhere() {
		// 是否引用公共查询条件
		boolean isRefCommonCondition = queryModel.getWhere().isRefCommonCondtion();
		List<WhereCondition> conditions = queryModel.getWhere().getWhereConditions();

		StringBuffer wrapperWhere = new StringBuffer();
		if (CollectionUtils.isEmpty(conditions)) {
			if (isRefCommonCondition) {
				wrapperWhere.append("  <trim prefix=\"WHERE\" prefixOverrides=\"AND|OR\"> \n");
				this.wrapperCommonQuery(wrapperWhere);
				wrapperWhere.append("  </trim> \n");
			}
		} else {
			wrapperWhere.append("  <trim prefix=\"WHERE\" prefixOverrides=\"AND|OR\"> \n");
			for (WhereCondition condition : conditions) {
				// 条件是否需要空判断
				if (condition.isEmptyCheck()) {
					if(Wildcard.IN.getValue().equals(condition.getWildcard()) || Wildcard.NOT_IN.getValue().equals(condition.getWildcard())){
						wrapperWhere.append(BLANK + "<if test = \""+condition.getValue()+" != null and " +condition.getValue()+".size > 0 \"> \n");
					}else{
						this.wrapperEmptyCheck(wrapperWhere, condition);
					}
					this.wrapperWhereCondition(wrapperWhere, condition);
					wrapperWhere.append(BLANK + "</if> \n");
				} else {
					this.wrapperWhereCondition(wrapperWhere, condition);
				}
			}

			if (isRefCommonCondition) {
				this.wrapperCommonQuery(wrapperWhere);
			}
			wrapperWhere.append("  </trim> \n");	
		}

		return wrapperWhere.toString();
	}
	
	/**
	 * 包装空判断条件
	 * 
	 * @param wrapperWhere Where条件
	 * @param condition WhereCondition
	 */
	private void wrapperEmptyCheck(StringBuffer wrapperWhere, WhereCondition condition) {
		if(TransferValPattern.CONSTANT.getValue().equals(condition.getTransferValPattern())){//常量传值方式
			//判断查询条件是否为时间等特殊类型
			if(QueryModelUtil.specialType(condition)){
				wrapperWhere.append(BLANK + "<if test = \"'"+condition.getValue()+"' != null \"> \n");
			}else{
				wrapperWhere.append(BLANK + "<if test = \"'"+condition.getValue()+"' != null and '"+condition.getValue()+"' != '' \"> \n");
			}
		}else{//方法参数传值方式
			if(QueryModelUtil.specialType(condition)){
				wrapperWhere.append(BLANK + "<if test = \""+condition.getValue()+" != null \"> \n");
			}else{
				wrapperWhere.append(BLANK + "<if test = \""+condition.getValue()+" != null and "+condition.getValue()+" != '' \"> \n");
			}
		}
	}

	/**
	 * 包装where条件
	 * 
	 * @param wrapperWhere
	 *            where条件
	 * @param condition
	 *            WhereCondition对象
	 */
	private void wrapperWhereCondition(StringBuffer wrapperWhere,
			WhereCondition condition) {
		// 是否自定义查询条件
		boolean isCustomCondition = this.isCustomWhereCondition(condition);
		//特殊符号转义符
		if (Wildcard.LESS.getValue().equals(condition.getWildcard())
				|| Wildcard.LESS_THAN.getValue().equals(condition.getWildcard())
				|| Wildcard.GREATER.getValue().equals(condition.getWildcard())
				|| Wildcard.GREATER_THAN.getValue().equals(condition.getWildcard())) {
			wrapperWhere.append(BLANK + "<![CDATA[ ");
		}
	
		// 操作符
		wrapperWhere.append(BLANK +" " + condition.getOperatorType() + " ");
		// 左括号
		wrapperWhere.append(condition.isHasLeftBracket() ? "( " : " ");
		
		if (isCustomCondition) {
			//自定义查询条件
			wrapperWhere.append(condition.getCustomCondition() + " ");
		} else {
			// 条件属性
			wrapperWhere.append(condition.getConditionAttribute().getTableAlias() + "."
							+ condition.getConditionAttribute().getColumnName()
							+ " ");
			// 通配符
			String wildcard = (Wildcard.ALL_LIKE.getValue().equals(condition.getWildcard())
					|| Wildcard.LEFT_LIKE.getValue().equals(condition.getWildcard()) 
					|| Wildcard.RIGHT_LIKE.getValue().equals(condition.getWildcard())) ? "LIKE"
					: condition.getWildcard();
			wrapperWhere.append(wildcard+" ");
			// 根据传值方式包装值
			if (TransferValPattern.CONSTANT.getValue().equals(condition.getTransferValPattern())) {
				// 常量方式
				if (Wildcard.ALL_LIKE.getValue().equals(condition.getWildcard())) {
					wrapperWhere.append("'%" + condition.getValue() + "%' ");
				} else if (Wildcard.LEFT_LIKE.getValue().equals(condition.getWildcard())) {
					wrapperWhere.append("'%" + condition.getValue() + "' ");
				} else if (Wildcard.RIGHT_LIKE.getValue().equals(condition.getWildcard())) {
					wrapperWhere.append("'" + condition.getValue() + "%' ");
				} else if(Wildcard.IN.getValue().equals(condition.getWildcard()) 
						|| Wildcard.NOT_IN.getValue().equals(condition.getWildcard())){
					wrapperWhere.append("(" + QueryModelUtil.convertToInString(condition.getValue()) + ") ");
				} else {
					wrapperWhere.append("'" + condition.getValue() + "' ");
				}
		
			} else {// 参数方式
				if (Wildcard.ALL_LIKE.getValue().equals(condition.getWildcard())
						|| Wildcard.LEFT_LIKE.getValue().equals(condition.getWildcard())
						|| Wildcard.RIGHT_LIKE.getValue().equals(condition.getWildcard())) {
					String expression= 
							DataBaseConvertFactory.getInstance().convert(condition.getWildcard());
					wrapperWhere.append(QueryModelUtil.parsingExpression(expression, condition.getValue()));
				} else if (Wildcard.IN.getValue().equals(condition.getWildcard()) || Wildcard.NOT_IN.getValue().equals(condition.getWildcard())) {
					// in条件sql模板
					String inTemplate = QueryModelUtil.getListTemplate();
					wrapperWhere.append(QueryModelUtil.parsingExpression(inTemplate, condition.getValue()));
				} else {
					wrapperWhere.append("#{" + condition.getValue() + "}" + " ");
				}
			}
		}
		// 右边括号
		wrapperWhere.append(condition.isHasRightBracket() ? ") \n" : " \n");
		//特殊符号转义符
		if (Wildcard.LESS.getValue().equals(condition.getWildcard())
				|| Wildcard.LESS_THAN.getValue().equals(condition.getWildcard())
				|| Wildcard.GREATER.getValue().equals(condition.getWildcard())
				|| Wildcard.GREATER_THAN.getValue().equals(condition.getWildcard())) {
			wrapperWhere.append("]]> \n");
		}
	}
	
	
	/**
	 * 是否为自定义条件
	 * 
	 * @param condition
	 *            WhereCondition对象
	 * @return 是否为自定义条件
	 */
	private boolean isCustomWhereCondition(WhereCondition condition) {
		return StringUtil.isNotBlank(condition.getCustomCondition()) ? true : false;
	}

	/**
	 * 包装通用查询条件
	 * 
	 * @param wrapperWhere
	 *            where语句
	 */
	private void wrapperCommonQuery(StringBuffer wrapperWhere) {
		String entityName = "";
		if (observer != null) {
			WrapperEntity entity = (WrapperEntity) observer;
			entityName = StringUtil.uncapitalize(entity.getEntity().getEngName()); 
		} else {
			String entityId = queryModel.getFrom().getPrimaryTable().getEntityId();
			EntityVO entity = (EntityVO) CacheOperator.readById(entityId);
			entityName = StringUtil.uncapitalize(entity.getEngName()); 
		}
		wrapperWhere.append(BLANK + "<include refid=\"" + entityName + "_default_query_condition\" /> \n");
	}

	/**
	 * 包装orderBy
	 * 
	 * @return 包装orderBy
	 */
	private String wrapperOrderBy() {
		String sortEnd = queryModel.getOrderBy().getSortEnd();
		List<Sort> sorts = queryModel.getOrderBy().getSorts();
		boolean dynamicOrder = queryModel.getOrderBy().isDynamicOrder();
		StringBuffer wrapperOrderBy = new StringBuffer();
		
		if(CollectionUtils.isEmpty(sorts)){
			if(dynamicOrder){
				String orderByTableAlias = this.getDynamicOrderByAlias();
				wrapperOrderBy.append(BLANK + "<if test=\"sortFieldName != null and sortFieldName != '' and sortType != null and sortType != ''\"> \n");
				wrapperOrderBy.append(BLANK + " ORDER BY "+ orderByTableAlias +".${sortFieldName} ${sortType} \n");
				wrapperOrderBy.append(BLANK + sortEnd + " \n");
				wrapperOrderBy.append(BLANK + "</if> \n");
			}
		}else{
			wrapperOrderBy.append(BLANK + "ORDER BY \n");
			for (int i = 0; i < sorts.size(); i++) {
				// 分隔符
				String separator = QueryModelUtil.getCollectionSeparator(sorts, i);
				// 排序属性
				QueryAttribute attr = sorts.get(i).getSortAttribute();
				wrapperOrderBy.append(BLANK + attr.getTableAlias() + "."+ attr.getColumnName() 
						+ " " + sorts.get(i).getSortType()
						+ separator+" \n");
			}
			if(dynamicOrder){
				String orderByTableAlias = this.getDynamicOrderByAlias();
				wrapperOrderBy.append(BLANK + "<if test=\"sortFieldName != null and sortFieldName != '' and sortType != null and sortType != ''\"> \n");
				wrapperOrderBy.append(BLANK + " ,"+ orderByTableAlias +".${sortFieldName} ${sortType} \n");
				wrapperOrderBy.append(BLANK + "</if> \n");
			}
			wrapperOrderBy.append(BLANK + sortEnd + " \n");
		}
		
		return wrapperOrderBy.toString();
	}

	/**
	 * 包装分组函数
	 * 
	 * @return 包装分组函数
	 */
	private String wrapperGroupBy() {
		List<QueryAttribute> lst = queryModel.getGroupBy().getGroupByAttributes();
		StringBuffer wrapperGroupBy = new StringBuffer();
		
		if (!CollectionUtils.isEmpty(lst)) {
			wrapperGroupBy.append(BLANK + "GROUP BY ");
			for (int i = 0; i < lst.size(); i++) {
				// 分隔符
				String separator = QueryModelUtil.getCollectionSeparator(lst, i);
				//分组属性
				QueryAttribute groupByAttribute = lst.get(i);
				wrapperGroupBy.append(groupByAttribute.getTableAlias() + ".");
				wrapperGroupBy.append(groupByAttribute.getColumnName() + separator + " \n");
			}
		}

		return wrapperGroupBy.toString();
	}

	/**
	 * 包装成完整的SQL
	 * 
	 * @return 包装查询建模SQL
	 */
	private String wrapperSQL() {
		if (this.method != null
				&& StringUtil.isNotEmpty(method.getAssoMethodName())) {
			return wrapperCountSQL();
		}
		StringBuffer wrapperSQL = new StringBuffer();
		wrapperSQL.append(select);
		wrapperSQL.append(from);
		wrapperSQL.append(where);
		wrapperSQL.append(groupBy);
		wrapperSQL.append(orderBy);
		return wrapperSQL.toString();
	}
	
	/**
	 * 包装查询数量SQL
	 * 
	 * @return 数量查询SQL
	 */
	private String wrapperCountSQL() {
		StringBuffer wrapperSQL = new StringBuffer();
		wrapperSQL.append("SELECT COUNT(1) FROM (");
		wrapperSQL.append(select);
		wrapperSQL.append(from);
		wrapperSQL.append(where);
		wrapperSQL.append(groupBy);
		wrapperSQL.append(orderBy);
		wrapperSQL.append(")");
		return wrapperSQL.toString();
	}
	
	/**
	 * 获取动态排序的表别名
	 * 
	 * @return 动态排序的表别名
	 */
	private String getDynamicOrderByAlias() {
		String primaryTableAlias = queryModel.getFrom().getPrimaryTable().getSubTableAlias();
		return queryModel.getOrderBy().getDynamicAttribute() == null ? primaryTableAlias
				: queryModel.getOrderBy().getDynamicAttribute().getTableAlias();
	}
	
	/**
	 * 查询建模SQL.xml的参数类型
	 * 
	 * @return 参数类型
	 */
	public String getParameterType() {
		List<ParameterVO> parameters = method.getParameters();
		if (CollectionUtils.isEmpty(parameters)) {
			return "";
		} else if (parameters.size() == 1) {
			DataTypeVO dataType = parameters.get(0).getDataType();
			return dataType.readDataTypeFullName();
		} else {// 多个参数 为java.util.Map
			return "java.util.Map";
		}
	}

	/**
	 * 查询建模SQL.xml的返回值类型
	 * 
	 * @return 返回值类型
	 */
	public String getResultType() {
		DataTypeVO dataType = method.getReturnType();
		if ("java.util.List".equals(dataType.getType())) {
			List<String> entityIds=dataType.readRelationEntityIds();
			if (CollectionUtils.isEmpty(entityIds)) {

				return dataType.readImportDateType().size() > 1 ? dataType
						.readImportDateType().get(1) : dataType
						.readImportDateType().get(0);
			}
			
			String entityId = entityIds.get(0);
			return QueryModelUtil.convertToEntityFullName(entityId);
		}
		return dataType.readDataTypeFullName();
	}

	/**
	 * @return the method 实体方法
	 */
	public MethodVO getMethod() {
		return method;
	}

	/**
	 * @return the sql 查询建模SQL
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @return the queryModel 查询建模对象
	 */
	public QueryModel getQueryModel() {
		return queryModel;
	}

	/**
	 * @return the select 查询建模select
	 */
	public String getSelect() {
		return select;
	}

	/**
	 * @return the from 查询建模from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @return the where 查询建模where
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * @return the orderBy 查询建模orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @return the groupBy 查询建模groupBy
	 */
	public String getGroupBy() {
		return groupBy;
	}

}
