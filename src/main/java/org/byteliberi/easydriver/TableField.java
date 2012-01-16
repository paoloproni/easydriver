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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is a super class for every field in a table.<P>
 * This can be used in the queries to map the result set, the PreparedStatement
 * and to read the complete name of the field
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 * 
 * @param <T> Class type of the table field
 */
public abstract class TableField<T> implements Comparable<TableField<?>> {
	/**
	 * Name of the field
	 */
	private String name;
	
	/**
	 * Secondary name of the field
	 */
	private String alias;
	
	/**
	 * This is true when this field accepts a null value.	 
	 */
	private boolean annullable;
	
	/**
	 * This table field belongs to table, referred by this property
	 */
	private DBTable<?> table;
	
	/**
	 * Creates a new instance of this class
	 */
	public TableField() {
		this.name = "";
		this.annullable = true;
		this.alias="";
		this.table = null;
	}

	/**
	 * Creates a new instance of this class
	 * @param name Name of the field
	 * @param table This table field belongs to table, referred by this property
	 */
	public TableField(final String name, final DBTable<?> table) {
		this(name, true, table);
	}

	/**
	 * Crates a new instance of this class
	 * @param name Name of the field.
	 * @param annullable This is true when this field accepts a null value.	 
	 * @param table This table field belongs to table, referred by this parameter.
	 */
	public TableField(final String name, final boolean annullable, final DBTable<?> table) {
		this.name = name;
		this.alias = "";
		this.annullable = annullable;
		this.table = table;
		if (this.table != null)
			this.table.addField(this);
	}

	/**
	 * Getter of the alias
	 * @return Secondary name of the field
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Getter of the field name 
	 * @return Name of the field
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Getter of the complete name
	 * @return complete name
	 */
	public String getCompleteName() {
		String tableName;
		DBSchema schema;
		String schemaName="";
		if (this.table != null) {
			tableName = this.table.getTableName();
			schema = this.table.getSchema();
			if (schema != null)
				schemaName = schema.getName();
		}
		else {
			tableName = "";
			schema = null;			
		}
	
		final StringBuilder sb = new StringBuilder(schemaName.length() + tableName.length() + 1);
		if (schemaName.length() > 0)
			sb.append(schemaName).append('.');

		sb.append(tableName).append('.').append(this.name);
		return sb.toString();
	}

	/**
	 * Checks if this field accepts null value
	 * @return This is true when this field accepts a null value.
	 */
	public boolean isAnnullable() {
		return this.annullable;
	}

	/**
	 * Getter of the table
	 * @return This table field belongs to table, referred by this property
	 */
	public DBTable<?> getTable() {
		return this.table;
	}

	public int compareTo(TableField<?> obj) {
		if (this.name == null)
			return 1;
		else
			return this.name.compareTo(obj.getName());
	}
	
	/**
	 * Creates a new instance of a value object and it fills its properties
	 * with the read values from the Result Set.
	 * 
	 * @param rs Result Set which was created, executing this query.
	 * @param index 1 based index of the field.
	 * @return Value Object
	 * @throws SQLException A problem occurred with the query or the database...
	 */
	public abstract T map( ResultSet rs, int index) throws SQLException;
	 
	/**
	 * Fills the prepared statement, calling the proper field.
	 * 
	 * TODO: checks that the passed value can be null or not
	 * @param pstm Prepared Statement to fill with the value.
	 * @param index 1 based index of the field.
	 * @param value Value Object
	 * @throws SQLException A problem occurred with the query or the database...
	 */
	public abstract void map(PreparedStatement pstm, int index, T value) throws SQLException;
}
