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

import org.byteliberi.easydriver.TableField;

/**
 * This class contains the field that take
 * part of a relationship between two tables.
 *  
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the fields
 */
public class RelatedFields<T> {
	/**
	 * Field in the main table
	 */
	private TableField<T> field;

	/**
	 * Field in the related table
	 */
	private TableField<T> relatedField;

	/**
	 * Creates a new instance of this class.
	 * @param mainField Field in the main table.
	 * @param relatedField Field in the related table.
	 */
	public RelatedFields(final TableField<T> mainField, final TableField<T> relatedField) {
		this.field = mainField;
		this.relatedField = relatedField;
	}

	/**
	 * Getter of the main field.
	 * @return main field.
	 */
	public final TableField<T> getField() {
		return field;
	}

	/**
	 * Setter of the main field.
	 * @param field main field.
	 */
	public final void setField(final TableField<T> field) {
		this.field = field;
	}

	/**
	 * Getter of the related field.
	 * @return Related field.
	 */
	public final TableField<T> getRelatedField() {
		return relatedField;
	}

	/**
	 * Setter of the related field.
	 * @param relatedField Related field
	 */
	public final void setRelatedField(final TableField<T> relatedField) {
		this.relatedField = relatedField;
	}
}
