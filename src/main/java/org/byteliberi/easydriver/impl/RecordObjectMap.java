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

import org.byteliberi.easydriver.ObjectFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface is implemented by the mappers that get a single value object
 * or a list.<P>
 * This should never be used directly by the API user.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the value objects that are created by this class.
 */
interface RecordObjectMap<T> {
	/**
	 * 
	 * This method reads the Result Set data and calls the passed object factory, in order
	 * to create a new instance of the Value Object or a list on Value Objects and appends
	 * them to the result list.
	 *
	 * @param rs Result Set where to read the data
	 * @param mapper Creates the value objects
	 * @throws SQLException A problem occurred with the query or the database. 
	 */
	public void map(ResultSet rs, ObjectFactory<T> mapper) throws SQLException;
}
