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
package org.byteliberi.easydriver.join;

import org.byteliberi.easydriver.DBTable;
import org.byteliberi.easydriver.TableField;
import org.byteliberi.easydriver.impl.RelatedFields;
import org.byteliberi.easydriver.impl.Relationship;

/**
 * A <code>Join</code> is a command which relates two table, based on
 * the equality of one or more fields.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 * 
 * @param T join type
 */
public class Join<T> {
	/**
	 * Specific join clause, such as Left join or Right join etc...
	 */
	private String joinType="";

	/**
	 * Database relationships between the database tables.
	 */
	private Relationship<T> relationship;

	protected final static String ON = " ON (";
	protected final static String AND = " AND ";

	/**
	 * Creates a new instance of this class.
	 * @param relationship Relationship between the database tables.
	 * @param joinType Specific join clause, such as Left join or Right join etc...
	 */
	public Join(final Relationship<T> relationship, final String joinType) {
		this.relationship = relationship;
		this.joinType = joinType;
	}

	/**
	 * Create a String which will be inserted in the query in the <code>FROM</code> clause
	 * after the table name.
	 * 
	 * @return Join section of the query for the included relationship.
	 */
	public String createQueryPart() {
		final StringBuilder sbQuery = new StringBuilder(100);
		final DBTable<T> refTable = this.relationship.getRelatedTable();
		sbQuery.append(joinType).append(refTable.getCompleteName());
		final String alias = this.relationship.getAlias();
		final boolean hasAlias = ((alias != null) && (alias.length() > 0)); 
		if (hasAlias)
			sbQuery.append(' ').append(alias);
		
		sbQuery.append(ON);			
		
		for (RelatedFields<?> relatedFields : relationship.getRelationFields()) {
			final TableField<?> localField = relatedFields.getField();
			final TableField<?> refField = relatedFields.getRelatedField();
			sbQuery.append(localField.getCompleteName()).append('=');
			if (hasAlias)
				sbQuery.append(alias).append('.').append(refField.getName());
			else
				sbQuery.append(refField.getCompleteName());				
			
			sbQuery.append(AND);
		}
		final int sbLen = sbQuery.length();
		sbQuery.delete(sbLen - AND.length(), sbLen);
		sbQuery.append(") ");
		return sbQuery.toString();
	}
}