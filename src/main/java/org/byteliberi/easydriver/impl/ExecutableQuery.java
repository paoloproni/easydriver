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

import java.sql.SQLException;

/**
 * This is the super class of the queries that can update, insert or delete data.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 */
public abstract class ExecutableQuery extends Query {

	/**
	 * This method executes the update, insert or delete.
	 * @return Number of rows affected by the execution of this query.
	 * @throws SQLException A problem occurred in the query or in the database.
	 */
	public int execute() throws SQLException {
		return pstm.executeUpdate();
	}

}
