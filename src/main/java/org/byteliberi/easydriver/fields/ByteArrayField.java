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
package org.byteliberi.easydriver.fields;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.byteliberi.easydriver.DBTable;
import org.byteliberi.easydriver.TableField;

/**
 * This is a table field that matches the BYTE ARRAY database type.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class ByteArrayField extends TableField<byte[]> {
	/**
	 * This instance is used when the caller needs the map methods only.
	 */
	private final static ByteArrayField empty = new ByteArrayField();
	
	/**
	 * Creates a new instance of this class.
	 */
	private ByteArrayField() {
		super();
	}
	
	/**
	 * Returns a singleton empty instance.
	 * @return This instance is used when the caller needs the map methods only.
	 */
	public static ByteArrayField getEmpty() {
		return empty;
	}
	
	/**
	 * Creates a new instance of this class.
	 * @param name Field name.
	 * @param table This field belongs to the specified table.
	 */
	public ByteArrayField(final String name, final DBTable<?> table) {
		super(name, table);
	}
	
	/**
	 * Creates a new instance of this class.
	 * @param name Field name
	 * @param annullable This is true when this field accepts a null value	 
	 * @param table This field belongs to the specified table.
	 */
	public ByteArrayField(final String name, final boolean annullable, final DBTable<?> table) {
		super(name, annullable, table);
	}
	
	@Override
	public byte[] map(final ResultSet rs, final int index) throws SQLException {
		final byte[] res = rs.getBytes(index);
		if (rs.wasNull())
			return null;
		else
			return res;
	}

	@Override
	public void map(final PreparedStatement pstm, final int index, final byte[] value) throws SQLException {
		if (value == null) {
			if (isAnnullable())
				pstm.setNull(index, Types.ARRAY);
			else
				throw new SQLException("Attempt to set a null value to the field " + getCompleteName());
		}
		else
			pstm.setBytes(index, value);
	}
}
