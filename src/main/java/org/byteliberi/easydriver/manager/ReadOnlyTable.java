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

import java.util.List;
import org.byteliberi.easydriver.DataLayerException;

/**
 * A readonly manager can read data, but it cannot not update,insert or delete.
 * @author Paolo Proni
 * 
 * @param K class of the primary key
 * @param T value object of the found records
 */
public interface ReadOnlyTable<K,T> {
    
    /**
     * Finds a record that matches the given key value
     * @return Object which maps to the found record or null if no record matches.
     * @throws DataLayerException A problem occurred with the persistence layer.
     */
    public T findById(K key) throws DataLayerException;
    
    /**
     * Reads all the records of the managed table or view.
     * Watch out as this operation could be slow.
     * @return Objects which map to the found records or empty list if no records
     * have been found.
     * @throws DataLayerException A problem occurred with the persistence layer.
     */
    public List<T> readAll() throws DataLayerException;
    
    /**
     * Reads all the records of the managed table or view and returns only the 
     * portion that starts for the offset index and ends at no more than
     * offset + pageSize index.
     * 
     * @param offset Number of row to skip
     * @param pageSize Max size of the page.
     * @return Objects which map to the found records or empty list if no records
     * have been found.
     * @throws DataLayerException A problem occurred with the persistence layer.
     */
    public List<T> readPage(int offset, int pageSize) throws DataLayerException;       
}
