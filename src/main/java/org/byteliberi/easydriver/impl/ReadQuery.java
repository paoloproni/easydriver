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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.byteliberi.easydriver.ObjectFactory;
import org.byteliberi.easydriver.TableField;

/**
 * This class is used to reuse some common code to all the queries
 * that return some value objects.<P>
 * Please notice that this class does not implements ReadQueryAPI as
 * the caller only is resonably requested to implements such records,
 * while the reusable parts are in this class only.
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 *
 * @param <T> Class of the value objects that are created by this class.
 */
public class ReadQuery<T> {
    /**
    * Mapper reads some data from a Result Set and creates zero or one
    * value object: if it get one record, it creates one value object.
    */
    private SingleRecordObjectMap<T> singleRecordMapper = null;

    /**
    * Mapper which reads some data from a Result Set and creates
    * one or more value objects: for each found records in the result set,
    * this class creates one value object and fills its property values.
    */
    private MultipleRecordObjectMap<T> multipleRecordMapper = null;

    /**
    * Factory which creates a new instance
    * of a value object and fills its properties with the values read from
    * a JDBC result set.
    */
    private ObjectFactory<T> valueObjectFactory;

    /**    
    * Prepared Statement which is created from the generated query string.
    * The result set is created by this prepared statement and this prepared
    * statement is filled with the parameter values, passed by an <code>addParameter</code>
    * or <code>addNullParameter</code> methods.
    */
    private PreparedStatement pstm;

    /**
    * Fields that appear in the select part of the query.
    */
    private List<TableField<?>> selectFields;

    /**
    * Creates a new instance of this class.
    * @param valueObjectFactory actory which creates a new instance
    * of a value object and fills its properties with the values read from
    * a JDBC result set.
    */
    public ReadQuery(final ObjectFactory<T> valueObjectFactory) {
        this.valueObjectFactory = valueObjectFactory;
    }

    /**
    * Executes the query and for each read record, it calls the mapper,
    * which will be called later, to get one record or more.
    * @param recordMapper Mapper which read the records from a record set
    * and create the value objects, then it keeps the data, so it can
    * give back a single object or a list.
    * @throws SQLException A problem occurred with the database or the query.
    */
    private void fetchRecords(final RecordObjectMap<T> recordMapper) throws SQLException {
        ResultSet rs = null;
        try {
            // set the parameter values

            rs = this.pstm.executeQuery();

            while (rs.next()) {
                recordMapper.map(rs, this.valueObjectFactory);
            }
        }
        finally {
            if (rs != null)
                rs.close();
        }
    }

    /**
    * Get one value object or null.
    * @return Single Value Object or null if no records have been found
    * for the passed parameter.
    * @throws SQLException A problem occurred with the database or the query.
    */
    public synchronized T getSingleResult() throws SQLException {
        // Lazy constructor
        if (this.singleRecordMapper == null)
                this.singleRecordMapper = new SingleRecordObjectMap<T>();

        fetchRecords(this.singleRecordMapper);
        return this.singleRecordMapper.getResult();
    }

    /**
    * Get a list of value objects.
    * @return List of Value Object or an empty list of records.
    * @throws SQLException A problem occurred with the database or the query.
    */
    public synchronized List<T> getResultList() throws SQLException {
        // Lazy constructor
        if (this.multipleRecordMapper == null)
                this.multipleRecordMapper = new MultipleRecordObjectMap<T>();

        fetchRecords(this.multipleRecordMapper);
        return this.multipleRecordMapper.getResult();
    }

    /**
    * Getter of the Prepared Statement
    * @return Prepared Statement which generates the Result Set
    */
    public final PreparedStatement getPstm() {
        return pstm;
    }

    /**
    * Setter of the Prepared Statement
    * @param pstm  Prepared Statement which generates the Result Set
    */
    public final void setPstm(final PreparedStatement pstm) {
        this.pstm = pstm;
    }

    /**
    * Setter of the fields which are part of the <code>SELECT</code> query.
    * @param selectFields Fields that appear in the select part of the query.
    */
    public final void setSelectFields(final List<TableField<?>> selectFields) {
        this.selectFields = selectFields;
    }

    /**
    * Getter of the fields which are part of the <code>SELECT</code> query.
    * @return Fields that appear in the select part of the query.
    */
    public final List<TableField<?>> getSelectFields() {
        return this.selectFields;
    }
}
