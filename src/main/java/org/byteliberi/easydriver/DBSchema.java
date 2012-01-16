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

/**
 * A schema is an aggregation of tables and other database objects.
 * Some databases use the concept of schema as an alias for a db user.
 * Here is just in order to complete tables names for application
 * with more than one schema.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class DBSchema {
	/**
	 * Schema name
	 */
	private String name;

	/**
	 * Creates a new instance of this class, setting an empty schema name
	 */
	public DBSchema() {
		setName("");
	}

	/**
	 * Creates a new instance of this class
	 * @param schemaName Schema name
	 */
	public DBSchema(final String schemaName) {
		setName(schemaName);
	}

	/**
	 * Getter of the schema name
	 * @return Schema name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Setter of the schema name
	 * @param schemaName Schema name
	 */
	private void setName(final String schemaName) {
		if (schemaName == null)
			throw new IllegalArgumentException("The schema name can be emtpy, but not null");

		this.name = schemaName;
	}
}