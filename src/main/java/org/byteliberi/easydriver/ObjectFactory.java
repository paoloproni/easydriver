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

/**
 * An <code>ObjectFactory</code> is a factory which creates a new instance
 * of a value object and fills its properties with the values read from
 * a JDBC result set.
 * 
 * @param T Class of the newly created value object.
 */
public interface ObjectFactory<T> {

	/**
	 * Creates an object of class T and fills its properties,
	 * reading from the passed result set.
	 * 
	 * @param rs Result set which is created by the query that this
	 * factory belongs to.
	 * @return Value object
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
    public T map(ResultSet rs) throws SQLException;
}
