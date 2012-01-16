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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.byteliberi.easydriver.ObjectFactory;

/**
 * This class is a mapper which reads some data from a Result Set and creates
 * zero or one value object: it tries to read one record in the result set, 
 * then creates a value object and fills it properties with the column of
 * the read result set.
 * <P>
 * This class should not be used directly by the user of this libray.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the value object that this class returns.
 */
class SingleRecordObjectMap<T> implements RecordObjectMap<T> {
	/**
	 * Value Object
	 */
	private T result = null;

	/**
	 * Creates a new instance of the value object, that will be stored inside the
	 * current instance, then reads a record from the result set and it fills the
	 * object parameters. 
	 */
	public void map(final ResultSet rs, final ObjectFactory<T> mapper) throws SQLException {
		result = mapper.map(rs);
	}

	/**
	 * Getter of the result.
	 * @return the found value object or null if no records have been found.
	 */
	public T getResult() {
		return this.result;
	}
}
