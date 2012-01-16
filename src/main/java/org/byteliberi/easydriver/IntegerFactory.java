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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.byteliberi.easydriver.fields.IntField;

/**
 * This class is used when a query result is a simple, scalar, integer value
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class IntegerFactory implements ObjectFactory<Integer> {
	/**
	 * 1 based index of the column in the query
	 */
	private final int index;
	
	/**
	 * Creates a new instance of this class
	 */
	public IntegerFactory() {
		this.index = 1;
	}
	
	/**
	 * Creates a new instance of this class
	 * @param index 1 based index of the column in the query
	 */
	public IntegerFactory(final int index) {
		this.index = index;
	}

	@Override
	public Integer map(ResultSet rs) throws SQLException {
		return ( IntField.getEmpty() ).map(rs, this.index);		
	}

	/**
	 * Getter of the column index
	 * @return 1 based index of the column in the query
	 */
	public final int getIndex() {
		return index;
	}
}
