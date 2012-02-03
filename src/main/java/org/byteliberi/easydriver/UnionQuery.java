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

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.byteliberi.easydriver.impl.Query;
import org.byteliberi.easydriver.impl.ReadQuery;
import org.byteliberi.easydriver.impl.ReadQueryAPI;

/**
 * This query get the joined result of two queries which extracts the
 * same list of fields, by type, order and number.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the value objects that are created by this class.
 */
public class UnionQuery<T> extends Query implements ReadQueryAPI<T> {
	protected final static String UNION = "\nUNION ";
	protected final static String ALL = "ALL ";
	
	/**
	 * Factory which creates a new instance
	 * of a value object and fills its properties with the values read from
	 * a JDBC result set.
	 */
	private ObjectFactory<T> valueObjectFactory;
	
	/**
	 * This class contains some reusable code for the queries that creates
	 * * some value objects and return them to the user.
	 */
	private ReadQuery<T> readQuery = null;
	
	/**
	 * This is the first query, which appears before the UNION clause.
	 */
	protected SelectQuery<T> firstQuery;
	
	/**
	 * This is the second query, which appears after the UNION clause.
	 */
	protected SelectQuery<T> secondQuery;
	
	/**
	 * When this is true, the query is created by UNION ALL, else it is
	 * created by UNION only.
	 */
	protected boolean unionAll = false;
	
	/**
	 * Creates a new instance of this class.
	 * @param firstQuery This is the first query, which appears before the UNION clause.
	 * @param secondQuery This is the second query, which appears after the UNION clause.
	 * @param unionAll When this is true, the query is created by UNION ALL, else it is
	 * created by UNION only.
	 * @param valueObjectFactory Factory which creates a new instance
	 * of a value object and fills its properties with the values read from
	 * a JDBC result set.
	 */
	public UnionQuery(final SelectQuery<T> firstQuery,
					  final SelectQuery<T> secondQuery,
					  final boolean unionAll,
					  final ObjectFactory<T> valueObjectFactory) {
		
		this.firstQuery = firstQuery;
		this.secondQuery = secondQuery;
		this.unionAll = unionAll;
		
		this.valueObjectFactory = valueObjectFactory;		
	}
	
	@Override
	protected String createQueryString() {
            final StringBuilder sbQuery = new StringBuilder(100);
            sbQuery.append('(').append(firstQuery.createQueryString()).append(')').append(UNION);
            if (unionAll)
                sbQuery.append(ALL);
            sbQuery.append('(').append(secondQuery.createQueryString()).append(')');	

            final String queryStr = sbQuery.toString();
            Logger.getLogger(SelectQuery.class.getName()).info(queryStr);

            return queryStr;
	}
	
	/**
	 * This method creates the <code>select</code> section of the query     
	 */
    @Override
    public TableField<?>[] getSelectFields() {
            return this.firstQuery.getSelectFields();
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
