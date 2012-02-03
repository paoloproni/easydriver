/*
 * EasyDriver is a library that let a programmer build queries easier
 * than using plain JDBC.
 * Copyright (C) 2012 Paolo Proni
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

/**
 * The databases have some proprietary syntax elements, for
 * instance PostgreSQL has the <b>ilike</b> operator.
 * 
 * @author Paolo Proni
 */
public interface CustomOperators {
   
    /**
     * Returns a special <b>like</p> operator which
     * does not make differences between capital and
     * not capital letters.
     * 
     * @return Case insensitive LIKE operator
     */
    public ExpressionAPI iLike();
    
    /**
     * Returns an operator that matches the similar
     * words, by fuzzy logic.
     * @return an operator that finds all the similar
     * occurrencies.
     */
    public ExpressionAPI similar();
}