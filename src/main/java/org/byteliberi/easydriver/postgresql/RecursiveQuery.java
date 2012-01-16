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
package org.byteliberi.easydriver.postgresql;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.byteliberi.easydriver.DBTable;
import org.byteliberi.easydriver.ObjectFactory;
import org.byteliberi.easydriver.SelectQuery;
import org.byteliberi.easydriver.TableField;
import org.byteliberi.easydriver.expressions.ExpressionAPI;
import org.byteliberi.easydriver.impl.Query;
import org.byteliberi.easydriver.impl.ReadQuery;
import org.byteliberi.easydriver.impl.ReadQueryAPI;

/**
 * A <code>RecursiveQuery</code> is made by two united queries that starts
 * from a record and read the related records in the same table.<p>
 * This kind of query is used for example to look for the parts of a machine.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 * @param <T> Class of the value objects that are created by this class
 */
public class RecursiveQuery<T> extends Query implements ReadQueryAPI<T> {
	private static final String WITH_RECURSIVE = "WITH RECURSIVE ";
	private static final String AS = " AS ";
	
	/**
	 * Factory which creates a new instance
	 * of a value object and fills its properties with the values read from
	 * a JDBC result set.
	 */
	private ObjectFactory<T> valueObjectFactory;
	
	/**
	 * This class contains some reusable code for the queries that creates
	 * some value objects and return them to the user.
	 */
	private ReadQuery<T> readQuery = null;
	
	/**
	 * Name of the query, it is used in the header of the recursive query,
	 * then it is called by an upper level query.
	 */
	private String queryName;
	
	/**
	 * Name of the fields of the external recursive query
	 */
	private String[] queryFieldNames;
	
	/**
	 * Fields that appear in the select part of the query
	 */
	private TableField<?>[] selectFields;
	
	/**
	 * Main table to look for
	 */
	private DBTable<?> table;
	
	/**
	 * <code>Where</code> section of the initial query, that is the query
	 * which finds the first record. 
	 */
	private ExpressionAPI[] startCondition;
	
	/**
	 * <code>Where</code> section the recursive query, that is the query
	 * which fines the other record.
	 */
	private ExpressionAPI[] stopCondition;
	
	/**
	 * Fields that are in the <code>ORDER BY</code> section of the query. 
	 */
	private TableField<?>[] orderBy;
	
	// TODO: a constructor that accepts a SelectQuery as start point, but it must not contains a field named 'depth'
	
	/**
	 * Creates a new instance of this class
	 * @param queryName Name of the query, it is used in the header of the recursive query,
	 * then it is called by an upper level query.
	 * @param queryFieldNames Name of the fields of the external recursive query
	 * @param selectFields Fields that appear in the select part of the query
	 * @param table Main table to look for
	 * @param startCondition <code>Where</code> section of the initial query, that is the query
	 * which finds the first record.
	 * @param stopCondition <code>Where</code> section the recursive query, that is the query
	 * which fines the other record.
	 * @param orderBy Fields that are in the <code>ORDER BY</code> section of the query.
	 * @param valueObjectFactory Factory which creates a new instance
	 * of a value object and fills its properties with the values read from
	 * a JDBC result set.
	 */
	public RecursiveQuery(final String queryName,
						  final String[] queryFieldNames,
						  final TableField<?>[] selectFields,
						  final DBTable<?> table,
						  final ExpressionAPI[] startCondition,
						  final ExpressionAPI[] stopCondition,
						  final TableField<?>[] orderBy,
						  final ObjectFactory<T> valueObjectFactory) {
		
		this.queryName = queryName;
		this.queryFieldNames = queryFieldNames;
		this.selectFields = selectFields;
		this.table = table;
		this.startCondition = startCondition;
		this.stopCondition = stopCondition;
		this.orderBy = orderBy;
		this.valueObjectFactory = valueObjectFactory;
	}

	@Override
	protected String createQueryString() {
		final StringBuilder sbQuery = new StringBuilder(100);
		sbQuery.append(WITH_RECURSIVE).append(queryName).append('(');
				
		for (String queryFieldName : queryFieldNames) {
			sbQuery.append(queryFieldName).append(',');
		}
		sbQuery.append("depth) ").append(AS).append("(\n")
			   .append("SELECT ");
		for (TableField<?> tableField : selectFields)
			sbQuery.append(tableField.getCompleteName()).append(',');
		sbQuery.append("1");
		sbQuery.append("\nFROM ").append(table.getCompleteName());
		if (startCondition.length > 0) {
			sbQuery.append("\nWHERE ");
			for (ExpressionAPI startCond : this.startCondition)
				sbQuery.append(startCond.createString());
		}
		
		sbQuery.append("\nUNION ALL \nSELECT ");
		for (TableField<?> tableField : selectFields)
			sbQuery.append(tableField.getCompleteName()).append(',');
		
		sbQuery.append(queryName).append(".depth + 1\nFROM ")
			   .append(table.getCompleteName()).append(',').append(queryName).append('\n');
		
		if (stopCondition.length > 0) {
			sbQuery.append("WHERE ");
			for (ExpressionAPI stopCond : this.stopCondition)
				sbQuery.append(stopCond.createString());
		}
		sbQuery.append(")\n")
		 	   .append("SELECT ");
		for (String queryFieldName : queryFieldNames) {
			sbQuery.append(queryFieldName).append(',');
		}
		sbQuery.deleteCharAt(sbQuery.length() - 1);
		sbQuery.append(" FROM ").append(queryName);
		
		final int orderBySize = this.orderBy.length;
		if (orderBySize > 0) {
			sbQuery.append(" ORDER BY ");
			for (int i = 0; i < orderBySize; i++)
				sbQuery.append(this.orderBy[i].getCompleteName()).append(',');
			sbQuery.deleteCharAt(sbQuery.length() - 1);
		}
		final String queryStr = sbQuery.toString();
		Logger.getLogger(SelectQuery.class.getName()).info(queryStr);

		return queryStr;
	}

	@Override
	public TableField<?>[] getSelectFields() {
		return this.selectFields;
	}

	@Override
	public synchronized T getSingleResult() throws SQLException {
		if (this.readQuery == null)
			this.readQuery = new ReadQuery<T>(this.valueObjectFactory);

		this.readQuery.setPstm(this.pstm);        
		return this.readQuery.getSingleResult();
	}

	@Override
	public synchronized T getSingleResultAndClose() throws SQLException {
		final T result = getSingleResult();
		if (this.pstm != null)
			this.pstm.close();
		return result;
	}

	@Override
	public synchronized List<T> getResultList() throws SQLException {
		if (this.readQuery == null)
			this.readQuery = new ReadQuery<T>(this.valueObjectFactory);

		this.readQuery.setPstm(this.pstm);
		return this.readQuery.getResultList();
	}

	@Override
	public synchronized List<T> getResultAndClose() throws SQLException {
		final List<T> result = getResultList();
		if (this.pstm != null)
			this.pstm.close();

		return result;
	}
}
