package org.byteliberi.easydriver.expressions;

import org.byteliberi.easydriver.SelectQuery;
import org.byteliberi.easydriver.TableField;

public class In<T> extends DualOperator {
	private final static String OPERATOR = " in (";
	private final static String NEGATED_OPERATOR = " not in ("; 
	
	public In(final TableField<T> field, final String valueList, final boolean negation) {
		super(field, negation ? NEGATED_OPERATOR : OPERATOR, valueList + ")");
	}
	
	public In(final TableField<T> field, final SelectQuery<T> subQuery) {
		super(field, OPERATOR, subQuery.createQueryString() + ") ");
	}
}
