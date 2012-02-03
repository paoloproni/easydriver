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
import org.byteliberi.easydriver.TableField;

/**
 * This is the mathematical operator <pre><></pre>
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class Different extends DualOperator {
    private final static String OPERATOR = " <> ";

    /**
     * Creates a new instance of this class.
     * @param field Field name lays at the left of the operator, a <pre>?</pre>
     * is put at its right.
     */
    public Different(final TableField<?> field) {
        super(field, OPERATOR);
    }

    /**
     * Creates a new instance of this class.
     * @param field Field name lays at the left of the operator.
     * @param right String which lays at the right of the operator.
     */
    public Different(final TableField<?> field, final String right) {
        super(field, OPERATOR, right);
    }

    /**
     * Creates a new instance of this class.
     * @param left String which lays at the left of the operator.
     * @param right String which lays at the right of the operator.
     */
    public Different(final String left, final String right) {
        super(left, OPERATOR, right);
    }

    /**
     * Creates a new instance of this class.
     * @param left Expression which lays at the left of the operator.
     * @param right Expression which lays at the right of the operator.
     */
    public Different(final ExpressionAPI left, final ExpressionAPI right) {
        super(left, OPERATOR, right);
    }
}
