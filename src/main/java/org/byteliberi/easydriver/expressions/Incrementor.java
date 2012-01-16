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
package org.byteliberi.easydriver.expressions;

import org.byteliberi.easydriver.TableField;

/**
 * This expression increments the value of a field by 1 unit.
 * A typical use case is an update query, for a counter.
 * 
 * @author Paolo Proni
 */
public class Incrementor extends Equals {
	
	/**
	 * Creates a new instance of this class
	 * @param field Field which has to be incremented by one.
	 */
	public Incrementor(TableField<?> field) {
		super(field.getName(), field.getName() + "+1 ");
	}	
}
