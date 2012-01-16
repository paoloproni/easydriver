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
package org.byteliberi.easydriver.fields.decorations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.byteliberi.easydriver.TableField;

/**
 * A <code>Decoration</code> is some text that add something to
 * a field name, such as a function name.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 *
 * @param <T> Type of the table field.
 */
public class Decoration<T> extends TableField<T> {
	/**
	 * Name of the function
	 */
	private String functionName;
	
	/**
	 * Field whose name we want to decorate
	 */
	private TableField<T> field;
	
	/**
	 * Creates a new instance of this class.
	 * @param field Field whose name we want to decorate.
	 * @param functionName Name of the function we want to add.
	 */
	public Decoration(final TableField<T> field, final String functionName) {
		this.field = field;
		this.functionName = functionName;
	}
	
	/**
	 * Creates a text with a function and the name.
	 * @return Function followed by the field name  
	 */
	@Override
	public String getName() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.functionName).append('(').append(this.field.getName()).append(')');
		return sb.toString();
	}
	
	/**
	 * Creates a text with a function and the name.
	 * @return Function followed by the field complete name 
	 */
	@Override
	public String getCompleteName() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.functionName).append('(').append(this.field.getCompleteName()).append(')');
		return sb.toString();
	}
	
	@Override
	public T map(final ResultSet rs, final int index) throws SQLException {
		return this.field.map(rs, index);
	}

	@Override
	public void map(PreparedStatement pstm, int index, T value) throws SQLException {
		this.field.map(pstm, index, value);
	}
}
