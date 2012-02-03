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

import java.util.LinkedList;
import java.util.List;

/**
 * This is the logical operator <pre>BETWEEN AND</bre>
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class Between implements ExpressionAPI {
    private final static String PARAM_PLACEHOLDER = "?";
    private final static String BETWEEN = " BETWEEN ";
    private final static String AND = " AND ";
    
    /**
     * This is the list of parameters which are going to receive the values later
     */
    private List<TableField<?>> parameters = new LinkedList<TableField<?>>();

    /**
     * What lays at the left of the word <pre>BETWEEN</pre>
     */
    private String field;
    
    /**
     * String version of the first parameter
     */
    private String first;
    
    /**
     * String version of the second parameter
     */
    private String second;

    /**
     * Creates a new instance of this class
     * @param field What lays at the left of the word <pre>BETWEEN</pre>
     * @param first String version of the first parameter,
     * it follows <pre>BETWEEN</pre> and stays before <pre>AND</pre>
     * @param second String version of the second parameter, 
     * it follows <pre>AND</pre>
     */
    public Between(final String field,
                   final String first,
                   final String second) {

        this.field = field;
        this.first = first;
        this.second = second;
    }

    /**
     * Creates a new instance of this class
     * @param field What lays at the left of the word <pre>BETWEEN</pre>
     * @param first First parameter, it follows <pre>BETWEEN</pre> and stays before <pre>AND</pre>
     * @param second Second parameter, it follows <pre>AND</pre>
     */
    public Between(final TableField<?> field,
                   final TableField<?> first,
                   final TableField<?> second) {

        this.field = field.getCompleteName();

        this.first = PARAM_PLACEHOLDER;
        this.parameters.add(first);

        this.second = PARAM_PLACEHOLDER;
        this.parameters.add(second);
    }

    @Override
    public String createString() {
        final StringBuilder sb = new StringBuilder(field.length() + first.length() + second.length()
                                     + BETWEEN.length() + AND.length());
        sb.append(field).append(BETWEEN).append(first).append(AND).append(second);
        return sb.toString();
    }

    @Override
    public List<TableField<?>> getParameters() {
        return this.parameters;
    }
    
    
}
