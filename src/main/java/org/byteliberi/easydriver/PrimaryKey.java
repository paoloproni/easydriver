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

import org.byteliberi.easydriver.TableField;

/**
 * This class contains the field or the fields that are
 * part of the primary key of a table.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 * 
 * @param <T> Class of a single primary key
 */
public class PrimaryKey<T> {
	/**
	 * Single field of the primary key, if there is one only.
	 */
	private TableField<T> field;
	
	/**
	 * Fiels of the primary key, if there are many.
	 */
	private TableField<?>[] fields;
	
	/**
	 * Creates a new instance of this class
	 * @param field Single field of the primary key, if there is one only.	
	 */
	public PrimaryKey(TableField<T> field) {
		this.field = field;
		this.fields = new TableField<?>[] { field };
	}

	/**
	 * Creates new instance of this class. 
	 * @param fieldList Fiels of the primary key, if there are many.
	 */
	public PrimaryKey(TableField<?>[] fieldList) {
		this.field = null;
		this.fields = fieldList;
	}

	/**
	 * Getter of the primary key fields
	 * @return Fiels of the primary key, if there are many.
	 */
	public TableField<?>[] getFields() {
		return this.fields;
	}

	/**
	 * Getter of the single primary key field
	 * @return Single field of the primary key, if there is one only.
	 * @throws UnsupportedOperationException This exception is risen
	 * if this method is called and the primary key is made by one
	 * field only.
	 */
	public TableField<T> getSingleField() {
		if (field == null)
			throw new UnsupportedOperationException(
						"There is more than one field in the primary key");

		return this.field;
	}

	/**
	 * This method checks if in the primary key there is one field
	 * @return true if the primary key is made by one field only.
	 */
	public boolean isSingle() {
		return (this.fields == null) || (this.fields.length == 0);
	}
}

