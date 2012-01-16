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

import org.byteliberi.easydriver.join.Join;

/**
 * This class provides fields for the <code>SELECT</code> section
 * and the <code>FROM</code> section.<br/>
 * This is useful when some table fields are used in several
 * queries, where the other sections such as the <code>WHERE</code>
 * part only changes.<br/>
 * Another strategy for reusing a common query part is to create a
 * query without the where part, then you can subclass it, adding
 * the <code>WHERE</code> part in the subclasses, but using this
 * class, the programmer still can user the single inheritance for
 * something else.
 * 
 * @author Paolo Proni
 */
public class CommonSelectFrom {
	/**
	 * Fields which are in the select part of the query.
	 */
	private TableField<?>[] selectFields;
	
	/**
	 * Table for the <code>FROM</code> section.
	 */
	private DBTable<?> table;
	
	/**
	 * Joins to the external tables.
	 */
	private Join<?>[] joins;
	
	/**
	 * Creates a new instance of this class, setting an
	 * empty array for the relationships
	 * @param selectFields Fields which are in the select part of the query.
	 * @param table Table for the <code>FROM</code> section.
	 */
	public CommonSelectFrom(final TableField<?>[] selectFields, 
							final DBTable<?> table) {
		
		this(selectFields, table, new Join[0]);
	}
	
	/**
	 * Creates a new instance of this class.
	 * @param selectFields Fields which are in the select part of the query.
	 * @param table Table for the <code>FROM</code> section.
	 * @param joins Joins to the external tables.
	 */
	public CommonSelectFrom(final TableField<?>[] selectFields, 
							final DBTable<?> table,
							final Join<?>[] joins) {
		
		this.selectFields = selectFields;
		this.table = table;
		this.joins = joins;
	}
	
	/**
	 * Getter of the fields which are in the select part of the query.
	 * @return Fields which are in the select part of the query.
	 */
	public final TableField<?>[] getSelectFields() {
		return this.selectFields;
	}
	
	/**
	 * Getter of the name of the table for the <code>FROM</code> section.
	 * @return Name of the table for the <code>FROM</code> section.
	 */
	public final DBTable<?> getTable() {
		return this.table;
	}
	
	/**
	 * Getter of the joins to the external tables.
	 * @return Joins to the external tables.
	 */
	public final Join<?>[] getRelationship() {
		return this.joins;
	}
	
}
