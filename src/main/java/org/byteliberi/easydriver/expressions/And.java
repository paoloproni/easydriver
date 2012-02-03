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
package org.byteliberi.easydriver.expressions;

import org.byteliberi.easydriver.ExpressionAPI;

/**
 * This is the logical operator <pre>AND</pre>
 *
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class And extends DualOperator {
    private final static String OPERATOR = " AND ";

    /**
     * Creates a new instance of this class.
     * @param left Left part of the expression.
     * @param right Right part of the expression.
     */
    public And(ExpressionAPI left, ExpressionAPI right) {
        super(left, OPERATOR, right);
        setParentheses(true);
    }
}