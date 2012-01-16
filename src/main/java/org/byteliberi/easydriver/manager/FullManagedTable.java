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
package org.byteliberi.easydriver.manager;

import org.byteliberi.easydriver.DataLayerException;

/**
 * A manager class can read, write, update and delete records in a table or in a
 * updatable view.
 * 
 * @author Paolo Proni
 * 
 * @param K class of the primary key
 * @param T value object of the found records
 */
public interface FullManagedTable<K, T> extends ReadOnlyTable<K, T> {
    
    /**
     * Inserts a new record
     * @param newRecord Object which maps a new record to be inserted
     * @throws DataLayerException A problem occurred with the persistence layer.
     */
    public void insert(T newRecord) throws DataLayerException;
    
    /**
     * Delete an existing record
     * @param key Primary key value of an existing record.
     * @throws DataLayerException A problem occurred with the persistence layer.
     */
    public void delete(K key) throws DataLayerException;
    
    /**
     * Update and existing record with the values of the passed object.
     * @param key Primary key value of the existing record.
     * @param record New values of a record to be updated
     * @throws DataLayerException A problem occurred with the persistence layer.
     */
    public void update(K key, T record) throws DataLayerException;
}
