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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is used to make a query that reads a single field only.
 * @author Paolo Proni 
 *
 * @param <T> Selected field class type.
 * @param <K> Primary key class type.
 */
public class SimpleSelect<T,K> {
	private TableField<T> selectField = null;	
	private PreparedStatement pstm = null;
	private TableField<K> primaryKey = null;
	
	@SuppressWarnings("unchecked")
	public SimpleSelect(final Connection con, final TableField<T> selectField, 
						final DBTable<?> table) throws SQLException {
		
		this.selectField = selectField;
		this.primaryKey = (TableField<K>) table.getPrimaryKey().getSingleField();
		
		final StringBuilder sb = new StringBuilder(100);
		sb.append("SELECT ").append(selectField.getCompleteName())
		  .append(" FROM ").append(table.getCompleteName())
		  .append(" WHERE ").append(this.primaryKey.getCompleteName()).append("=?");
		this.pstm = con.prepareStatement(sb.toString());
		
	}
	
	public T readSingleField(K pkValue) throws SQLException {
		primaryKey.map(this.pstm, 1, pkValue);
		final ResultSet rs = this.pstm.getResultSet();
		if (rs.next())
			return selectField.map(rs, 1);
		else
			return null;
	}
}
