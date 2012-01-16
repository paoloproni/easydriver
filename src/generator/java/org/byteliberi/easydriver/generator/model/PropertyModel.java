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

import java.io.PrintStream;

/**
 * This model represents a Java property of a class.
 * 
 * @author Paolo Proni
 */
public class PropertyModel implements ConstructorGeneratorAPI {
	/**
	 * Public, protected, package or private.
	 */
	private Visibility visibility;
	
	/**
	 * Name of the property.
	 */
	private String propertyClass;
	
	/**
	 * Name of the property.
	 */
	private String name;
	
	/**
	 * When true, the generator should generate a default value
	 */
	private boolean initialized = false;
	
	/**
	 * This is the init value to be set, when the <code>initialized</code> property is true.
	 */
	private String initValue;
	
	/**
	 * Creates a new instance of this class.
	 * @param visibility Public, protected, package or private.
	 * @param propertyType This is the type of the property.
	 * @param propertyName This is the name of the property.
	 */
	public PropertyModel(final Visibility visibility,
						 final String propertyType, 
						 final String propertyName) {
		
		this.visibility = visibility;
		this.propertyClass = propertyType;
		this.name = propertyName;
	}
	
	/**
	 * Getter of the Class name of the property.
	 * @return Class name of the property.
	 */
	public final String getPropertyClass() {
		return this.propertyClass;
	}
	
	/**
	 * Getter of the property name.
	 * @return Property name.
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Setter of the property name
	 * @param name Property name
	 */
	public final void setName(final String name) {
		this.name = name;
	}
	
	/**
	 * Setter of the property class
	 * @param propertyClassName Name of the class.
	 */
	public final void setPropertyClass(final String propertyClassName) {
		this.propertyClass = propertyClassName;
	}
	
	@Override
	public void write(final PrintStream out) {
		final StringBuilder sb = new StringBuilder();
		sb.append('\t')
			.append(this.visibility.getToken()).append(' ')
			.append(this.propertyClass).append(' ')
			.append(this.name);
		
		if (this.initialized) {
			sb.append(" = ").append(this.initValue);
		}
		sb.append(';');
		out.println(sb.toString());
	}
}
