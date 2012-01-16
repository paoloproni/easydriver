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
package org.byteliberi.easydriver.generator.model;

/**
 * This is the model of a relation between two database tables.<p>
 * The relations is of ManyToOne type.
 *  
 * @author Paolo Proni
 */
public class RelationModel {
	/**
	 * Table name in the side <code>one</code> of the relation.
	 */
	private String oneTable;
	
	/**
	 * Primary key name of the table in the side <code>one</code> of the relation.
	 */
	private String pkOneTable;
	
	/**
	 * Table name in the side <code>many</code> of the relation.
	 */
	private String rfManyTable;
	
	/**
	 * Class name of the field, used in a template.
	 */
	private String javaType;
	
	/**
	 * Creates a new instance of this class.
	 */
	public RelationModel() {
		this.oneTable = "";
		this.pkOneTable = "";
		this.rfManyTable = "";
		this.setJavaType("");
	}
	
	/**
	 * Creates a new instance of this class
	 * @param oneTable Table name in the side <code>one</code> of the relation.
	 * @param pkOneTable Primary key name of the table in the side <code>one</code> of the relation.
	 * @param rfManyTable Table name in the side <code>many</code> of the relation.
	 * @param javaType Creates a new instance of this class.
	 */
	public RelationModel(final String oneTable, final String pkOneTable, final String rfManyTable, final String javaType) {
		this.oneTable = oneTable;
		this.pkOneTable = pkOneTable;
		this.rfManyTable = rfManyTable;
		this.setJavaType(javaType);
	}

	/**
	 * Getter of the table name which is in the side <code>one</code> of the relation.
	 * @return Table name in the side <code>one</code> of the relation.
	 */
	public final String getOneTable() {
		return oneTable;
	}

	/**
	 * Setter of the table name which is in the side <code>one</code> of the relation.
	 * @param oneTable Table name in the side <code>one</code> of the relation.
	 */
	public final void setOneTable(final String oneTable) {
		this.oneTable = oneTable;
	}

	/**
	 * Getter of the Primary key name of the table in the side <code>one</code> of the relation.
	 * @return Primary key name of the table in the side <code>one</code> of the relation.
	 */
	public final String getPkOneTable() {
		return pkOneTable;
	}

	/**
	 * Setter of the Primary key name of the table in the side <code>one</code> of the relation.
	 * @param pkOneTable Primary key name of the table in the side <code>one</code> of the relation.
	 */
	public final void setPkOneTable(final String pkOneTable) {
		this.pkOneTable = pkOneTable;
	}

	/**
	 * Getter of the table name in the side <code>many</code> of the relation.
	 * @return table name in the side <code>many</code> of the relation.
	 */
	public final String getRfManyTable() {
		return rfManyTable;
	}

	/**
	 * Setter of the table name in the side <code>many</code> of the relation.
	 * @param rfManyTable table name in the side <code>many</code> of the relation. 
	 */
	public final void setRfManyTable(final String rfManyTable) {
		this.rfManyTable = rfManyTable;
	}

	/**
	 * Setter of class name of the field, used in a template.
	 * @param javaType Class name of the field, used in a template.
	 */
	public final void setJavaType(final String javaType) {
		this.javaType = javaType;
	}

	/**
	 * Getter of the class name of the field, used in a template.
	 * @return Class name of the field, used in a template.
	 */
	public final String getJavaType() {
		return javaType;
	}	
}
