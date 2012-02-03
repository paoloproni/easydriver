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
import org.byteliberi.easydriver.fields.DoubleField;

/**
 * An <code>Avg</code> calculates the average of a column.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class Avg extends TableField<Double> {	
	private final static String TEXT = "AVG";
	
	/**
	 * Name of the field of which we want to calculate the average 
	 */
	private String name;
	
	/**
	 * Complete name of the field of which we want to calculate the average
	 */
	private String completeName;	
	
	/**
	 * Creates a new instance of this class.
	 * @param field Field to calculate te average
	 */
	public Avg(TableField<?> field) {		
		this.name = fillName( field.getName() );
		this.completeName = fillName( field.getCompleteName() );
	}
	
	// TODO create a constructor that accepts an ExpressionAPI
	
	/**
	 * Creates a string with the function name followed by
	 * the field name as a parameter.
	 * @return String with the function to be inserted in the
	 * query string.
	 */
	private String fillName(final String name) {
		final StringBuilder sb = new StringBuilder();
		sb.append(TEXT).append('(').append(name).append(')');
		return sb.toString();
	}
	
	@Override
	public String getCompleteName() {
		return this.completeName;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public Double map(ResultSet rs, int index) throws SQLException {
		return ( DoubleField.getEmpty() ).map(rs, index);		
	}

	@Override
	public void map(PreparedStatement pstm, int index, Double value) throws SQLException {
		// useless		
	}
}
