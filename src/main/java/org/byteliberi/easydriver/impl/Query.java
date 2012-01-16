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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.byteliberi.easydriver.DBTable;
import org.byteliberi.easydriver.TableField;
import org.byteliberi.easydriver.fields.BigDecimalField;
import org.byteliberi.easydriver.fields.BooleanField;
import org.byteliberi.easydriver.fields.ByteArrayField;
import org.byteliberi.easydriver.fields.CharField;
import org.byteliberi.easydriver.fields.DateField;
import org.byteliberi.easydriver.fields.IntField;
import org.byteliberi.easydriver.fields.TimestampField;
import org.byteliberi.easydriver.fields.UUIDField;
import org.byteliberi.easydriver.fields.VarcharField;

/**
 * This is the super class of the all the queries.
 *  
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 */
public abstract class Query {
	/**
	 * Prepared Statement which is created from the generated query string.
	 * The result set is created by this prepared statement and this prepared
	 * statement is filled with the parameter values, passed by an <code>addParameter</code>
	 * or <code>addNullParameter</code> methods.
	 */
	protected PreparedStatement pstm = null;
	
	/**
	 * This is the list of parameters, contained in the <code>WHERE</code> expressions
	 */
	protected LinkedList<TableField<?>> parameterManagerList = new LinkedList<TableField<?>>();
    
	/**
	 * 1 based number of appended parameters
	 */
	protected AtomicInteger paramIndex = new AtomicInteger(0);
    
	/**
	 * Basic table treated by this query
	 */
	protected DBTable<?> table;

	/**
	 * Clears the parameter list and reset the index to 0.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void clearParameters() throws SQLException {
		this.parameterManagerList.clear();
		this.parameterManagerList = new LinkedList<TableField<?>>();
		
		this.pstm.clearParameters();
		
		this.paramIndex.set(0);
	}
		
	/**
	 * Appends a {@link java.math.BigDecimal} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final BigDecimal value) throws SQLException {
		final BigDecimalField field = BigDecimalField.getEmpty();
		this.parameterManagerList.add(field);
		
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.lang.Boolean} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final Boolean value) throws SQLException {
		final BooleanField field = BooleanField.getEmpty();
		this.parameterManagerList.add(field);
		
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.lang.String} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameterChar(final String value) throws SQLException {
		final CharField field = CharField.getEmpty();
		this.parameterManagerList.add(field);
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.util.Date} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final Date value) throws SQLException {
		final DateField field = DateField.getEmpty();
		this.parameterManagerList.add(field);
		
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.lang.Integer} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final Integer value) throws SQLException {
		final IntField field = IntField.getEmpty();
		this.parameterManagerList.add(field);
		
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.sql.Timestamp} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final Timestamp value) throws SQLException {
		final TimestampField field = TimestampField.getEmpty();
		this.parameterManagerList.add(field);
		
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.util.UUID} parameter to the internal prepared statement.
	 * @param value Value to be passed to the Prepared Statement after the other previously added values
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final UUID value) throws SQLException {
		final UUIDField field = UUIDField.getEmpty();
		this.parameterManagerList.add(field);
		
		field.map(pstm, this.paramIndex.addAndGet(1), value);
	}
	
	/**
	 * Appends a {@link java.lang.String} parameter to the internal prepared statement.
	 * @param parameter Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final String parameter) throws SQLException {
		final VarcharField field = VarcharField.getEmpty();
		this.parameterManagerList.add(field);
		field.map(pstm, this.paramIndex.addAndGet(1), parameter);
	}
	
	/**
	 * Appends a byte array to the internal prepared statement.
	 * @param parameter Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addParameter(final byte[] parameter) throws SQLException {
		final ByteArrayField field = ByteArrayField.getEmpty();
		this.parameterManagerList.add(field);
		field.map(pstm, this.paramIndex.addAndGet(1), parameter);
	}
	
	/**
	 * Appends a null parameter value to the internal prepared statement.
	 * @param parameter Value to be passed to the Prepared Statement after the other previously added values.
	 * @throws SQLException A problem occurred with the database.
	 */
	public synchronized void addNullParameter(final TableField<?> parameter) throws SQLException {
		this.parameterManagerList.add(parameter);
		parameter.map(pstm, this.paramIndex.addAndGet(1), null);
	}
	  
	/**
	 * Closes the Prepared Statement, so it can not be used again without creating a new one.
	 * @throws SQLException A problem occurred with the database.
	 */
	public void close() throws SQLException {		
		if (pstm != null)
			pstm.close();
	}

	/**
	 * Creates a Prepared Statement for the passed connection. 
	 * @param con Database connection.
	 * @throws SQLException A problem occurred with the database o the query.
	 */
	public synchronized void prepareQuery(final Connection con) throws SQLException {
		this.pstm = con.prepareStatement(createQueryString());
	}
	
	/**
	 * The subclasses create a query string by this method.
	 * @return Query string
	 */
	protected abstract String createQueryString();

	/**
	 * Getter of the connection from the internal Prepared Statement.
	 * @return Database connection, this can be null if the Prepared Statement
	 * has not been yet prepared, that is created.
	 * @throws SQLException A problem occurred with the database
	 */
	public synchronized final Connection getCon() throws SQLException {
		if (pstm == null)
			return null;
		else
			return pstm.getConnection();
	}

	/**
	 * Getter of the table
	 * @return database table
	 */
	public final DBTable<?> getTable() {
		return table;
	}

	/**
	 * Setter of the table
	 * @param table database table
	 */
	public final void setTable(final DBTable<?> table) {
		this.table = table;
	}

	/**
	 * Getter of the Prepared Statement.
	 * @return Prepared Statement, it can be null if the Prepared Statement
	 * has not been yet prepared, that is created.
	 */
	public final PreparedStatement getPstm() {
		return this.pstm;
	}
}