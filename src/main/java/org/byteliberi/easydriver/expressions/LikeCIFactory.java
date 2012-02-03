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
package org.byteliberi.easydriver.expressions;

import org.byteliberi.easydriver.ExpressionAPI;
import org.byteliberi.easydriver.TableField;
import org.byteliberi.easydriver.postgresql.Ilike;

/**
 * This factory class creates a <code>like</code> operator
 * which is case insensitive.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class LikeCIFactory {
// TODO: the constructors should look for some property which describes the database in order to choose the proper implementation
	
	/**
	 * Creates a new instance of the special case insensitive
	 * <code>LIKE</code> operator.
	 * @param field Field name lays at the left of the operator, a <code>?</code>
	 * is put at its right.
	 * @return case insensitive like operator.
	 */
	public static DualOperator newLikeCI(TableField<?> field) {
		return new Ilike(field);
	}
	
	/**
	 * Creates a new instance of the special case insensitive
	 * <code>LIKE</code> operator.
	 * 
	 * @param field Field name lays at the left of the operator.
	 * @param right String which lays at the right of the operator.
	 * @return case insensitive like operator.
	 */
	public static DualOperator newLikeCI(TableField<?> field, String right) {
		return new Ilike(field, right);
    }

	/**
	 * Creates a new instance of the special case insensitive
	 * <code>LIKE</code> operator.
	 * 
	 * @param left left String which lays at the left of the operator.
	 * @param right right String which lays at the right of the operator.
	 * @return case insensitive like operator.
	 */
	public static DualOperator newLikeCI(String left, String right) {
		return new Ilike(left, right);
	}

	/**
	 * Creates a new instance of the special case insensitive
	 * <code>LIKE</code> operator.
	 * 
	 * @param left Expression which lays at the left of the operator.
	 * @param right Expression which lays at the right of the operator.
	 * @return case insensitive like operator.
	 */
	public static DualOperator newLikeCI(ExpressionAPI left, ExpressionAPI right) {
		return new Ilike(left, right);
	}
}
