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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.byteliberi.easydriver.ObjectFactory;

/**
 * This class is a mapper which reads some data from a Result Set and creates
 * zero, one or more value objects: for each found records in the result set,
 * this class creates one value object and fills its property values.
 * <P>
 * This class should not be used directly by the user of this libray.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the value object that this class returns.
 */
class MultipleRecordObjectMap<T> implements RecordObjectMap<T> {
	/**
	 * Result list, it is filled by the read data from the Result Set.
	 */
	private LinkedList<T> result = new LinkedList<T>();

	/**
	 * This method reads the Result Set data and calls the passed object factory, in order
	 * to create a new instance of the Value Object and appends it to the result list.
	 */
	@Override
	public void map(final ResultSet rs, final ObjectFactory<T> mapper) throws SQLException {
		result.add( mapper.map(rs) );
	}

	/**
	 * This method simply get the list of value objects that have been created
	 * by the {@link #map(ResultSet, ObjectFactory)} method.
	 * 
	 * @return Result list, it is filled by the read data from the Result Set.
	 * It can be an empy list, but it should never be null.
	 */
	public List<T> getResult() {
		return this.result;
	}
}
