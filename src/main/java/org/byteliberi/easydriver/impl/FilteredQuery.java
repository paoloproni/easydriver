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
package org.byteliberi.easydriver.impl;

import org.byteliberi.easydriver.TableField;
import org.byteliberi.easydriver.expressions.Equals;
import org.byteliberi.easydriver.ExpressionAPI;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to reuse some common code among the query types
 * that allow a <code>WHERE</code> clause.<P>
 * Please notice that this class does not implements FilteredQueryAPI,
 * it does need to
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 */
public class FilteredQuery implements FilteredQueryAPI {
	/**
	 * Expression used in a where clause
	 */
	private ExpressionAPI[] expressions;

	/**
	 * Set a <code>WHERE</code> clause of the query, where the
	 * passed field has to be equal to a lately passed parameter
	 * @param field Field that will put in a <code>WHERE</code> clause
	 * such as <code>field name = ?</code>
	 */
	public void setWhere(final TableField<?> field) {
		final Equals fieldEquals = new Equals(field);
		this.expressions = new ExpressionAPI[] {fieldEquals};
	}

	/**
	 * Set a <code>WHERE</code> clause of the query.
	 * @param exp Single expression, it could be an <code>equals</code> or an <code>AND</code> etc...
	 */
	public void setWhere(final ExpressionAPI exp) {
		setWhere(new ExpressionAPI[] {exp});
	}

	/**
	 * Set a complex <code>WHERE</code> clause of the query, made by
	 * several logical part, such as several MANY, OR etc...
	 * @param exp <code>WHERE</code> clause, made by a list of logical operators.
	 */
	public void setWhere(final ExpressionAPI[] exp) {
		this.expressions = exp;
	}

	/**
	 * Getter of the parameter list contained in the expressions. 
	 * @return Parameters that are contained in the expressions.
	 */
	public List<TableField<?>> findParameters() {
		final LinkedList<TableField<?>> found = new LinkedList<TableField<?>>();
		for (ExpressionAPI expression : this.expressions) {
			found.addAll(expression.getParameters());
		}
		return found;
	}

	/**
	 * Get the complex expression.
	 * @return Expression used in a where clause
	 */
	public ExpressionAPI[] getExpression() {
		return this.expressions;
	}
}
