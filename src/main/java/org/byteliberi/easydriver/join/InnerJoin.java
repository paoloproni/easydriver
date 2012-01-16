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
package org.byteliberi.easydriver.join;

import org.byteliberi.easydriver.impl.Relationship;

/**
 * An <code>InnerJoin</code> is a join where a row in the main
 * table is extracted only if there is a matching row in the
 * related table.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 */
public class InnerJoin<T> extends Join<T> {
	private final static String JOIN_TYPE = "\nINNER JOIN ";

	/**
	 * Creates a new instance of this class.
	 * @param relationship Relationship between the database tables.
	 */
	public InnerJoin(final Relationship<T> relationship) {
		super(relationship, JOIN_TYPE);
	}
}
