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
import org.byteliberi.easydriver.expressions.ExpressionAPI;

/**
 * This interface is implemented by the queries which can have
 * a <code>WHERE</code> clause
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 */
public interface FilteredQueryAPI {
	public final static String WHERE = " WHERE ";
	
	/**
	 * Set a <code>WHERE</code> clause of the query, where the
	 * passed field has to be equal to a lately passed parameter
	 * @param field Field that will put in a <code>WHERE</code> clause
	 * such as <code>field name = ?</code>
	 */
	public void setWhere(TableField<?> field);

	/**
	 * Set a <code>WHERE</code> clause of the query.
	 * @param exp Single expression, it could be an <code>equals</code> or an <code>AND</code> etc...
	 */
	public void setWhere(ExpressionAPI exp);

	/**
	 * Set a complex <code>WHERE</code> clause of the query, made by
	 * several logical part, such as several MANY, OR etc...
	 * @param exp <code>WHERE</code> clause, made by a list of logical operators.
	 */
	public void setWhere(ExpressionAPI[] exp);
}

