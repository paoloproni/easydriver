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

import java.util.List;
import org.byteliberi.easydriver.DBTable;

/**
 * This interface is implemented by the classes which represent
 * the database relationships between the database tables.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 * 
 * @param T Class of the fields
 */
public interface Relationship<T> {

	/**
	 * Getter or the related table
	 * @return related table
	 */
	public DBTable<T> getRelatedTable();

	/**
	 * Getter of the related field
	 * @return fields in the related table
	 */
	public List<RelatedFields<T>> getRelationFields();
	
	/**
	 * Getter of the alias name of the related table,
	 * this is useful in the join part of a select query,
	 * where the same table name could be used more
	 * than once.
	 * @return
	 */
	public String getAlias();
}
