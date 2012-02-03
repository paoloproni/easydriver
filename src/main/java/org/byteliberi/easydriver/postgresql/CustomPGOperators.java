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
package org.byteliberi.easydriver.postgresql;

import org.byteliberi.easydriver.CustomOperators;
import org.byteliberi.easydriver.ExpressionAPI;
import org.byteliberi.easydriver.expressions.StringExpression;

/**
 * Here are the custom operators for PostgreSQL
 * @author Paolo Proni
 */
public class CustomPGOperators implements CustomOperators {

    /**
     * Returns a case insensitive like operator.
     * @return the <p>ilike</p> operator
     */
    @Override
    public ExpressionAPI iLike() {
        return new StringExpression(" ilike ");
    }
    
    /**
     * Returns an operator that matches the similar strings,
     * as defined in the optional pg_trgm contrib package.
     * @return The %
     */
    @Override
    public ExpressionAPI similar() {
        return new StringExpression(" % ");
    }
}
