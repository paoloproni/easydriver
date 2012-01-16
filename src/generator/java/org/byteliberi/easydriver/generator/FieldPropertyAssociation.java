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
package org.byteliberi.easydriver.generator;

import org.byteliberi.easydriver.generator.model.PropertyModel;

/**
 * This class associates a column name with a bean property.
 * @author Paolo Proni
 */
public class FieldPropertyAssociation {
	/**
	 * Property which is going to appear in the generated class.
	 */
	private PropertyModel prop;
	
	/**
	 * Name of the column.
	 */
	private String fieldName;
	
	/**
	 * If true, the column accepts a null value.
	 */
	private boolean annullable;

	/**
	 * Creates a new instance of this class
	 */
	public FieldPropertyAssociation() {		
	}
	
	/**
	 * Creates a new instance of this class
	 * @param prop Property which is going to appear in the generated class.
	 * @param fieldName Name of the column.
	 * @param annullable If this is true, the column accepts a null value.
	 */
	public FieldPropertyAssociation(final PropertyModel prop, final String fieldName, final boolean annullable) {
		this.prop = prop;
		this.fieldName = fieldName;
		this.annullable = annullable;
	}
	
	/**
	 * Sets if the columnn accepts a null value
	 * @param nullable If this is true, the column accepts a null value.
	 */
	public void setNullable(final boolean nullable) {
		this.annullable = nullable;
	}

	/**
	 * Checks if the column accepts a null value
	 * @return If this is true, the column accepts a null value.
	 */
	public boolean isNullable() {
		return this.annullable;
	}
	
	/**
	 * Gets the property
	 * @return Property which is going to appear in the generated class.
	 */
	public PropertyModel getProp() {
		return prop;
	}

	/**
	 * Sets the property
	 * @param prop Property which is going to appear in the generated class.
	 */
	public void setProp(PropertyModel prop) {
		this.prop = prop;
	}

	/**
	 * Gets the field name
	 * @return Name of the column.
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Sets the field name
	 * @param fieldName Name of the column.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
