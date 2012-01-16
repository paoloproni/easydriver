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

import java.util.LinkedHashSet;
import org.byteliberi.easydriver.impl.Relationship;

/**
 * This class represents a database table, it contains the fields as it
 * is in the database. It is used to get easily the object names and in
 * order to crete some common used queries.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 * @param P Class of the primary key (a composite primary key should
 * be represented by a unique object)
 */
public class DBTable<P> {
	/**
	 * Table name
	 */
	private String name;
	
	/**
	 * Database schema
	 */
	private DBSchema schema;

	/**
	 * Relationships with the other tables
	 */
	private Relationship<?>[] relationships;
	
	/**
	 * Primary key
	 */
	private PrimaryKey<P> primaryKey;
	
	/**
	 * Fields belonging to the table
	 */
	private LinkedHashSet<TableField<?>> dbFields;
		
	/**
	 * Creates a new instance of this class, for the given table name, without a schema.
	 * @param name Name of the database table
	 */
	public DBTable(String name) {
		this(name, new DBSchema(), new Relationship[0], null);
	}
		
	/**
	 * Creates a new instance of this class, with the passed parameters
	 * @param name Name of the table
	 * @param schema Database scheme
	 * @param relationships Relationship with the other tables
	 * @param pk Primary key
	 */
	public DBTable(String name, DBSchema schema,
				   Relationship<?>[] relationships,
				   PrimaryKey<P> pk) {

		this.name=name;
		this.schema=schema;
		this.relationships=relationships;
		this.primaryKey=pk;
		this.dbFields=new LinkedHashSet<TableField<?>>();
	}	
	
					
	/**
	 * Creates a query that can insert all the fields of this table
	 * @return insert query
	 */
	public final InsertQuery createInsertQuery() {
		return new InsertQuery(dbFields.toArray(new TableField<?>[dbFields.size()]), this);
	}
	
	/**
	 * Creates a query that can delete one or more records, belonging to this table,
	 * the <code>where</code> condition should be passed by the programmer.
	 * @return Delete query
	 */
	public final DeleteQuery createDeleteQuery() {
		return new DeleteQuery(this);
	}
	
	/**
	 * Creates a query than can update all the fields, the <code>where</code> condition should be passed by the programmer.
	 *  
	 * @return Update query
	 */
	public final UpdateQuery createUpdateQuery() {
		return new UpdateQuery(dbFields.toArray(new TableField<?>[dbFields.size()]), this);
	}

	/**
	 * Gets the complete name of a table, that is the schema name, if there is one, followed
	 * by the table name.
	 * @return complete table name.
	 */
	public String getCompleteName() {
		final StringBuilder sb = new StringBuilder();
		String schemaName = "";
		if (this.schema != null)
			schemaName = this.schema.getName();

		if (schemaName.length() > 0)
			sb.append(schemaName).append('.');

		sb.append(this.name);

		return sb.toString();
	}

	/**
	 * Getter of all the table fields
	 * @return Fiels which are in this table
	 */
	public final TableField<?>[] getFields() {
		return this.dbFields.toArray(new TableField<?>[this.dbFields.size()]);
	}

	/**
	 * Adds a field to the list of the fiels which are in this table
	 * @param field New field
	 */
	public final void addField(final TableField<?> field) {
		this.dbFields.add(field);
	}
	
	/**
	 * Setter of the primary key
	 * @param pk Primary key
	 */
	public final void setPrimaryKey(final PrimaryKey<P> pk) {
		this.primaryKey = pk;
	}

	/**
	 * Getter of the primary key
	 * @return Primary Key
	 */
	public final PrimaryKey<P> getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * Getter of the relationships
	 * @return Relationships to the other tables
	 */
	public Relationship<?>[] getRelationships() {
		return this.relationships;
	}
	
	/**
	 * Setter of the relationship
	 * @param rels Relationships to the other tables
	 */
	public void setRelationship(Relationship<?>[] rels) {
		this.relationships = rels;
	}

	/**
	 * Getter of the schema
	 * @return Schema which this table belongs to
	 */
	public DBSchema getSchema() {
		return this.schema;
	}

	/**
	 * Getter of the table name
	 * @return Table name
	 */
	public String getTableName() {
		return this.name;
	}
}