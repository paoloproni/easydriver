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
import java.util.List;

/**
 * This class contains the initialization values for a property bean.
 * @author Paolo Proni
 */
public class FieldInit {
	/**
	 * When it is true, the field is not an external class
	 */
	private boolean simpleField;
	
	/**
	 * When the field is simple, this is the field name, 
	 * with the first character as capital letter.
	 */
	private String fieldName;

	/**
	 * Then the field is not simple, this is the list of 
	 * constructor fields
	 */
	private List<String> fieldNames;
	
	/**
	 * Property name, that points to a local instance variable.
	 */
	private String propertyName;
	
	/**
	 * This is the name of an external Class, which matches
	 * a table that is connected by a foreign key.
	 */
	private String referredClassName;
	
	/**
	 * Creates a new instance for 
	 * @param fieldName Javized field name
	 */
	public FieldInit(final String fieldName) {
		this.fieldName = fieldName;
		this.simpleField = true;
	}
	
	/**
	 * Creates a new instance for this class, when the field matches an external class
	 * @param fieldNames List of javized field names
	 */
	public FieldInit(final List<String> fieldNames, final String propertyName, final String referredClassName) {
		this.fieldNames = fieldNames;
		this.propertyName = propertyName;
		this.referredClassName = referredClassName;
	}
	
	/**
	 * Checks if this is a simple field
	 * @return When it is true, the field is not an external class
	 */
	public boolean isSimpleField() {
		return this.simpleField;
	}
	
	/**
	 * Getter of the javized field name
	 * @return Name of the property
	 */
	public String getFieldName() {
		return this.fieldName;
	}
	
	/**
	 * Getter of the javized field names.
	 * @return Name of the properties to initialize the external field
	 */
	public List<String> getFieldNames() {
		return this.fieldNames;
	}
	
	/**
	 * Getter of the referred class name
	 * @return Name of the class with matches the table that is referenced by a foreign key.
	 */
	public String getReferredClassName() {
		return this.referredClassName;
	}
	
	/**
	 * Getter of the propery name
	 * @return Property name, that points to a local instance variable.
	 */
	public String getPropertyName() {
		return this.propertyName;
	}
}
