/*
 * EasyDriver is a library that let a programmer build queries easier
 * than using plain JDBC.
 * Copyright (C) 2011 Paolo Proni
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.byteliberi.easydriver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.byteliberi.easydriver.expressions.Equals;
import org.byteliberi.easydriver.expressions.ExpressionAPI;
import org.byteliberi.easydriver.impl.ExecutableQuery;
import org.byteliberi.easydriver.impl.FilteredQuery;
import org.byteliberi.easydriver.impl.FilteredQueryAPI;

/**
 * This query modifies the values of the fields of a table in the database.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 */
public class UpdateQuery extends ExecutableQuery implements FilteredQueryAPI {
	private final static String UPDATE = "UPDATE ";
	private final static String SET = " SET ";
	private final static Level LOG_LEVEL = Level.FINE;	
		
	protected ExpressionAPI[] expressions = null;
	
	/**
	 * This class contains some reusable code for the queries that have
	 * a <code>WHERE</code> clause.
	 */
	private FilteredQuery filteredQuery = null;
	
	/**
	 * Creates a new instance of this class.
	 * @param updateFields Fields of the passed table, where the values are updated by this query.
	 * @param table 
	 */
	public UpdateQuery(final TableField<?>[] updateFields, final DBTable<?> table) {
		this.table = table;
		
		final int fieldLen = updateFields.length;
		this.expressions = new ExpressionAPI[fieldLen];
		for (int i = 0; i < fieldLen; i++)
			this.expressions[i] = new Equals( updateFields[i], false );		
	}
	
	public UpdateQuery(final ExpressionAPI[] expressions, final DBTable<?> table) {
		this.expressions = expressions;
		this.table = table;
	}
	
	@Override
	protected String createQueryString() {
		final StringBuilder sbQuery = new StringBuilder(100);
		sbQuery.append(UPDATE).append(this.table.getCompleteName()).append(SET);
		for (ExpressionAPI expression : this.expressions)
			sbQuery.append(expression.createString()).append(',');
						
		sbQuery.deleteCharAt(sbQuery.length() - 1);
		final ExpressionAPI[] whereClause = getFilteredQuery().getExpression();
		if (whereClause != null) {
			sbQuery.append(WHERE);
			for (ExpressionAPI expr : whereClause)
				sbQuery.append(expr.createString());
		}
		final String query = sbQuery.toString();
		final Logger logger = Logger.getLogger(InsertQuery.class.getName());
		logger.log(LOG_LEVEL, query);
		return query;
	}
	
	/**
	 * This method get the where part manager of the query.
	 * @return This class contains some reusable code for the queries that have
	 * a <code>WHERE</code> clause.
	 */
	private FilteredQuery getFilteredQuery() {
		// Lazy constructor		
		if (this.filteredQuery == null)
			this.filteredQuery = new FilteredQuery();
		
		return this.filteredQuery;
	}

	@Override
	public void setWhere(TableField<?> field) {
		getFilteredQuery().setWhere(field);
	}

	@Override
	public void setWhere(ExpressionAPI exp) {
		getFilteredQuery().setWhere(exp);
	}

	@Override
	public void setWhere(ExpressionAPI[] exp) {
		getFilteredQuery().setWhere(exp);
	}
}
