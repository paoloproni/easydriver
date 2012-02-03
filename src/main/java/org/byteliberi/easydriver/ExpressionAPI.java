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

import java.util.List;

/**
 * An <code>Expression</code> is part of a <code>Where</code> clause,
 * it could be a logical or mathematical expression, such as a = ? or b = ?.
 *  
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public interface ExpressionAPI {
    /**
     * Creates the query string part which matches this expression.
     * @return part which can be inserted in a query string.
     */
    public String createString();

    /**
     * Get the parameter list.
     * @return Parameter list.
     */
    public List<TableField<?>> getParameters();
}
