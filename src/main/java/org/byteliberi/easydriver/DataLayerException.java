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

import java.sql.SQLException;

/**
 * This exception is risen when a problem with the database, or to the query
 * or generally to the application layer has happened
 *
 * @author Paolo Proni
 * @version 1.0
 * @author 1.0
 */
public class DataLayerException extends Exception {
	private static final long serialVersionUID = 3986345522569235022L;

	/**
	 * Empty constructor
	 */
	public DataLayerException() {
		super();
	}

	/**
	 * Creates a new instance of this class for the passed message
	 * @param msg Message describing what has happened
	 */
	public DataLayerException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new instance of this class for the passed exception
	 * @param ex Included exception
	 */
	public DataLayerException(SQLException ex) {
		super(ex);
	}
}

