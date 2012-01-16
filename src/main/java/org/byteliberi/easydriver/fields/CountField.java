/*
 * EasyDriver is a library that let a programmer build queries easier
 * easily than using plain JDBC.
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
package org.byteliberi.easydriver.fields;

/**
 * This is a calculated field, which counts the records.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class CountField extends IntField {
	private final static String COUNT = "COUNT(*)";
	
	private final static CountField instance = new CountField();
	
	/**
	 * Creates a new instance of this class.
	 */
	private CountField() {
		super(COUNT, false, null);
	}
	
	public static CountField getInstance() {
		return instance;
	}
	
	/**
	 * Get the <code>count(*)</code> string.
	 * @return count(*)
	 */
	public String getName() {
		return COUNT;
	}
	
	/**
	 * Get the <code>count(*)</code> string.
	 * @return count(*)
	 */
	public String getCompleteName() {
		return COUNT;
	}
	
	/**
	 * Get the fact that this calculated field is not annullable
	 * @return false
	 */
	public boolean isAnnullable() {
		return false;
	}
}
