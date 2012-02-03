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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.byteliberi.easydriver.expressions.StringExpression;

/**
 * This is a <b>Where</b> clause of a query
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 */
public class Where implements ExpressionAPI {
    /**
     * List of elements that cromposes the <b>where</b> clause.
     */
    private List<ExpressionAPI> whereClause = null;
    
    /**
     * List of parameters which are filled later, during the query
     * execution, by the proper values.
     */
    private List<TableField<?>> paramFields = Collections.<TableField<?>>emptyList();
        
//    private final static Logger logger = Logger.getLogger(Where.class.getName());    
    
    /**
     * Private constructor which creates a new instance of this class.
     * @param builder Helper class which build the internal
     * properties in a easy way.
     */
    private Where(WhereBuilder builder) {
        this.whereClause = builder.getWhereClause();
        this.paramFields = builder.getParamFields();
    }
    
    @Override
    public String createString() {
        final StringBuilder sb = new StringBuilder();
        for (ExpressionAPI exp : this.whereClause)
            sb.append(exp.createString());
        return sb.toString();
    }

    @Override
    public List<TableField<?>> getParameters() {
        return paramFields;
    }
    
    /**
     * This class implements the Builder pattern, in order to
     * create the <p>where</p> part of the query easily.
     */
    public class WhereBuilder {
        private final Logger logger = Logger.getLogger(WhereBuilder.class.getName());
        
        /**
         * Wrapper for the customer operators, which are going to be
         * filled later with the proper values, when executing the query,
         * knowing the database type.
         */
        private CustomOperators customOperators = null;
        
        /**
         * <p>Where</p> part of the query.
         */
        private List<ExpressionAPI> where = new LinkedList<ExpressionAPI>();
        
        /**
         * List of parameters which are filled later, during the query
         * execution, by the proper values.
         */
        private List<TableField<?>> paramFields = new LinkedList<TableField<?>>();
        
        /**
         * Creates a new instance of this class
         * @param customOperators Operators
         */
        public WhereBuilder(CustomOperators customOperators) {           
            this.customOperators = customOperators;            
        }
      
        /**
         * This is used by the Where class constructor, in order
         * to get the added components.
         * @return List of components of the <p>where</p> part
         * of the query.
         */
        public List<ExpressionAPI> getWhereClause() {
            return this.where;            
        }
        
        /**
         * Getter of the parameter fields
         * @return Parameter field list.
         */
        public List<TableField<?>> getParamFields() {
            return this.paramFields;
        }

        /**
         * Build the <p>where</p> part of the query.
         * @return Where part of the query.
         */
        public Where build() {
            return new Where(this);
        }
        
        /**
         * Appends the param part, that is a parameter for 
         * the type of the passed table field.
         * @param field This is used to get the type.
         * @return Builder of the where part.
         */
        public WhereBuilder param(TableField<?> field) {
            this.paramFields.add(field);
            return this;
        }

        /**
         * Appends the string, that is built from the passed expression.
         * @param exp Table Field or other expression to be appended.
         * @return Updated builder
         */
        public WhereBuilder eval(ExpressionAPI exp) {            
            this.where.add(exp);
            return this;
        }
        
        /**
         * Appends an <b>and</b> operator
         * @return Updated builder
         */
        public WhereBuilder and () {        
            this.where.add(new StringExpression(" and "));
            return this;
        }
        
        /**
         * Appends a <b>between</b> operator
         * @return Updated builder
         */
        public WhereBuilder between() {
            this.where.add(new StringExpression(" between "));
            return this;
        }
        
        /**
         * Appends a <b>&lt;&gt;</b> operator
         * @return Updated builder
         */
        public WhereBuilder different() {
            this.where.add(new StringExpression(" <> "));
            return this;
        }
        
        /**
         * Appends a <b>=</b> operator
         * @return Updated builder
         */
        public WhereBuilder eq() {
            this.where.add(new StringExpression(" = "));
            return this;
        }
        
        /**
         * Appends a <b>in</b> operator, followed by a right bracket.
         * @return Updated builder
         */
        public WhereBuilder in() {
            this.where.add(new StringExpression(" in ("));
            return this;
        }

        /**
         * Appends a left bracket 
         * @return Updated builder
         */
        public WhereBuilder lb () {
            this.where.add(new StringExpression(" ("));
            return this;
        }       
        
        /**
         * Appends a right bracket
         * @return Updated builder
         */
        public WhereBuilder rb() {
            this.where.add(new StringExpression(") "));
            return this;
        }
        
        /**
         * Appends a comma
         * @return Updated builder 
         */
        public WhereBuilder comma() {
            this.where.add(new StringExpression(","));
            return this;
        }
        
        /**
         * Appends a normal <b>like</b> operation
         * @return Updated builder  
         */
        public WhereBuilder like() {
            this.where.add(new StringExpression(" like "));            
            return this;
        }

        /**
         * Appends a <b>not</b> operator
         * 
         * @return Update builder
         */
        public WhereBuilder not() {
            this.where.add(new StringExpression(" not "));
            return this;
        }       

        /**
         * Appends an <b>or</b> operator
         * @return Updated builder
         */
        public WhereBuilder or() {
            this.where.add(new StringExpression(" or "));
            return this;
        }               
        
        /**
         * Appends an operator that does not make differences 
         * between capital and not capital characters.
         * The name of this method comes from the PostgreSQL
         * ilike clause, but in other databases it could be
         * different. 
         * In order to customize this operator, you should
         * pass an object of 
         * @return Updated builder  
         */
        public WhereBuilder iLike() {
            this.where.add( customOperators.iLike() );
            return this;
        }
        
        /**
         * Appends an operator which finds the similar record, that are records
         * that are not exactly equal but similar in fuzzy logic, for instance...
         * @return Update builder
         */
        public WhereBuilder similar() {
            this.where.add( customOperators.similar() );
            return this;
        }
    }
}
