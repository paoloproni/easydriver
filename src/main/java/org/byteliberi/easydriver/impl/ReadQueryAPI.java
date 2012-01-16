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

import java.sql.SQLException;
import java.util.List;
import org.byteliberi.easydriver.TableField;

/**
 * This interface is implemented by the query which 
 * can return some value object, such as select, union etc...
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the value objects that are created by this class.
 */
public interface ReadQueryAPI<T> {

    /**
     * Getter of the fields which are part of the <code>SELECT</code> query.
     * @return Fields that appear in the select part of the query.
     */
    public TableField<?>[] getSelectFields();

    /**     
     * Get a single record, obtained by executing the query.
     * @return Single Value Object or null if no rows have been found.
     * @throws SQLException A problem occurred with the query or the database.
     */
    public T getSingleResult() throws SQLException;

    /**
     * Get a list of records, obtained by executing the query.
     * @return List of Value Object or an emtpy list if no rows have been found.
     * @throws SQLException A problem occurred with the query or the database.
     */
    public List<T> getResultList() throws SQLException;

    /**
     * Get a single record, obtained by executing the query, then closes
     * the prepared statement.
     * @return Single Value Object or null if no rows have been found.
     * @throws SQLException A problem occurred with the query or the database.
     */
    public T getSingleResultAndClose() throws SQLException;

    /**
     * Get a list of records, obtained by executing the query, then closes
     * the prepared statement.
     * @return List of Value Object or an emtpy list if no rows have been found.
     * @throws SQLException A problem occurred with the query or the database.
     */
    public List<T> getResultAndClose() throws SQLException;
}
