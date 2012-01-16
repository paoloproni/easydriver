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
 * This is a particular decoration, used to specify an inverse order
 * for an <code>ORDER BY</code> clause.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 *
 * @param <T> Type of the field that we want to put in the <code>ORDER BY</code> clause
 * with reverse order. 
 */
public class Desc<T> extends TableField<T> {
	private final static String DESC = " DESC ";
	
	/**
	 * Field that we want to put in the <code>ORDER BY</code> clause
	 * with reverse order.
	 */
	private TableField<T> field;	
	
	/**
	 * Creates a new instance of this class
	 * @param field Field that we want to put in the <code>ORDER BY</code>
	 * clause with reverse order.
	 */
	public Desc(final TableField<T> field) {
		this.field = field;
	}
	
	/**
	 * Get the complete name of the field, followed by a <code>DESC</code> clause.
	 * @return Complete name.
	 */
	@Override	
	public String getCompleteName() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.field.getCompleteName()).append(DESC);
		return sb.toString();
	}
	
	/**
	 * Get the name of the field, followed by a <code>DESC</code> clause.
	 * @return Name of the field to order. 
	 */
	@Override
	public String getName() {
		final StringBuilder sb = new StringBuilder();
		sb.append(this.field.getName()).append(DESC);
		return sb.toString();
	}

	@Override
	public T map(ResultSet rs, int index) throws SQLException {
		return this.field.map(rs, index);
	}

	@Override
	public void map(PreparedStatement pstm, int index, T value) throws SQLException {
		this.field.map(pstm, index, value);
	}
}