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

import org.byteliberi.easydriver.expressions.ExpressionAPI;
import org.byteliberi.easydriver.impl.ExecutableQuery;
import org.byteliberi.easydriver.impl.FilteredQuery;
import org.byteliberi.easydriver.impl.FilteredQueryAPI;

/**
 * This class contains a query which deletes one or more records.
 *  
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class DeleteQuery extends ExecutableQuery implements FilteredQueryAPI {
	private final static Level LOG_LEVEL = Level.FINE;
	private final static String DELETE_FROM = "DELETE FROM ";
	
	/**
	 * This is used just to reuse the code among the query which can
	 * contain the where clause
	 */
	private FilteredQuery filteredQuery = null;
	
	/**
	 * Creates a new query for the passed table
	 * @param table Table where we can delete the records
	 */
	public DeleteQuery(final DBTable<?> table) {
		this.table = table;		
	}
	
	/**
	 * Creates the query string
	 */
	@Override
	protected String createQueryString() {
		final StringBuilder sbQuery = new StringBuilder(100);
		sbQuery.append(DELETE_FROM).append(this.table.getCompleteName());
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
	 * Getter of the query part which contains the where clause
	 * @return <code>Where</code> component
	 */
	private synchronized FilteredQuery getFilteredQuery() {
		// lazy constructor
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
