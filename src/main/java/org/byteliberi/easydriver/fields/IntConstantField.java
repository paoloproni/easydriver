/*
- * EasyDriver is a library that let a programmer build queries easier
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
package org.byteliberi.easydriver.fields;

import org.byteliberi.easydriver.DBTable;

/**
 * This is a constant field, used to specify some numerical
 * constants, such as 1 or some expression which returns a
 * numerical value, for instance: depth + 1
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class IntConstantField extends IntField {
	/**
	 * Field name
	 */
	private String name;
	
	/**
	 * Creates a new instance of this class.
	 * @param value Constant value.
	 * @param table This field belong to the specified table.
	 */
	public IntConstantField(final int value, final DBTable<?> table) {
		super(String.valueOf( value ), table);
		this.name = String.valueOf( value );
	}
	
	
	/**
	 * Creates a new instance of this class.
	 * @param value Constant expression.
	 * @param table This field belong to the specified table.
	 */
	public IntConstantField(final String value, final DBTable<?> table) {
		super(value, table);
		this.name = value;
	}
	
	/**
	 * Get the constant expression
	 * @return the constant expression
	 */
	@Override
	public String getCompleteName() {
		return this.name;
	}
	
	/**
	 * Get the constant expression
	 * @return the constant expression
	 */
	@Override
	public String getName() {
		return this.name;
	}	
}
