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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.byteliberi.easydriver.impl.ExecutableQuery;

/**
 * This class contains a query which inserts a record in a table.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class InsertQuery extends ExecutableQuery {
        private final static Level LOG_LEVEL = Level.FINE;
	private final static String INSERT = "INSERT INTO ";
	private final static String VALUES = " VALUES ";
	
	/**
	 * Fields which appears in the select part of the query
	 */
	protected TableField<?>[] fields = null;	
	
	/**
	 * Creates a new instance of this class 
	 * 
	 * @param selectFields Fields which appears in the select part of the query
	 * @param table This is the table where the newly created records are going
	 * to be inserted.
	 */
	public InsertQuery(final TableField<?>[] selectFields, final DBTable<?> table) {
		this.table = table;
		this.fields = selectFields;
	}
	
	@Override
	protected String createQueryString() {
		final StringBuilder sbQuery = new StringBuilder(100);
		sbQuery.append(INSERT).append(this.table.getCompleteName()).append(" (");
		for (TableField<?> tableField : fields)
			sbQuery.append(tableField.getName()).append(',');

		sbQuery.deleteCharAt(sbQuery.length() - 1);
		sbQuery.append(')').append(VALUES).append('(');
		final int fieldLen = this.fields.length;
		for (int i = 0; i < fieldLen; i++)			
			sbQuery.append("?,");
		sbQuery.deleteCharAt(sbQuery.length() - 1);
		sbQuery.append(')');
		
		final String query = sbQuery.toString();
		final Logger logger = Logger.getLogger(InsertQuery.class.getName());
		logger.log(LOG_LEVEL, query);
		return query;
	}
}
