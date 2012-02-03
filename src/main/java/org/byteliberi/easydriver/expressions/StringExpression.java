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
package org.byteliberi.easydriver.expressions;

import java.util.Collections;
import java.util.List;
import org.byteliberi.easydriver.ExpressionAPI;
import org.byteliberi.easydriver.TableField;

/**
 * This is a simple wrapper for a String which is in the
 * query <p>where</p> part of a query.
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class StringExpression implements ExpressionAPI { 
    private String exp = "";
    
    public StringExpression(final String exp) {
        this.exp = exp;
    }   

    @Override
    public String createString() {
        return this.exp;
    }

    @Override
    public List<TableField<?>> getParameters() {
        return Collections.<TableField<?>>emptyList();
    }    
}
