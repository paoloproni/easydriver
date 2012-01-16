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
 * An <code>LeftJoin</code> is a join where a row in the main
 * table is extracted always and the row in the related
 * table is extracted if it matches, otherwise its fields
 * are set to null.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 * 
 * @param T type of the joined columns
 */
public class LeftJoin<T> extends Join<T> {
	private final static String JOIN_TYPE = "\nLEFT OUTER JOIN ";

	/**
	 * Creates a new instance of this class.
	 * @param relationship Relationship between the database tables.
	 */
	public LeftJoin(final Relationship<T> relationship) {
		super(relationship, JOIN_TYPE);
	}
}
