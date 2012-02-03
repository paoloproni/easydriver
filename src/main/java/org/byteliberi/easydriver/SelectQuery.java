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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.byteliberi.easydriver.impl.*;
import org.byteliberi.easydriver.join.Join;
import org.byteliberi.easydriver.postgresql.CustomPGOperators;

/**
 * This is one of the most important classes, as it is the Select query.<p>
 * Instances of this class. 
 * 
 * @author Paolo Proni
 * @version 1.0
 * @since 1.0
 * @param <T> Class of the value objects that are created by this class.
 */
public class SelectQuery<T> extends Query implements ReadQueryAPI<T>, FilteredQueryAPI {

    private final static String SELECT = "SELECT ";
    private final static String AS_F = " as f";
    private final static String FROM = "\nFROM ";
    private final static String WHERE = "\nWHERE ";
    private final static String GROUP_BY = "\nGROUP BY ";
    private final static String HAVING = "\nHAVING ";   
    private final static String ORDER_BY = "\nORDER BY ";
    private final static String DISTINCT = " DISTINCT ";

    /**
     * Constant as it is returned by the database JDBC driver.
     */
    private final static String POSTGRESQL = "PostgreSQL";
    
    private final static Level LOG_LEVEL = Level.INFO;
    
    /**
     * Factory which creates a new instance
     * of a value object and fills its properties with the values read from
     * a JDBC result set.
     */
    private ObjectFactory<T> valueObjectFactory;
   
    /**
     * This class contains some reusable code for the queries that creates
     * some value objects and return them to the user.
     */
    private ReadQuery<T> readQuery = null;
    
    /**
     * This class contains some reusable code for the queries that have
     * a <code>WHERE</code> clause.
     */
    private FilteredQuery filteredQuery = null;

    /**
     * Fields that appear in the select part of the query.
     */
    protected List<TableField<?>> selectFields = new LinkedList<TableField<?>>();
    
    /**
     * The records of these tables are joined by a Cartesian product.
     */
    private DBTable<?>[] crossedTables;
    
    /**
     * These are the joins to some external tables.
     */
    private Join<?>[] joins;
    
    /**
     * Fields that are in the <code>ORDER BY</code> section of the query. 
     */
    private TableField<?>[] orderBy;
    
    /**
     * These expressions are in the <code>HAVING</code> section of the query.
     */
    private ExpressionAPI[] having;
    
    /**
     * These fields are in the <code>GROUP BY</code> section of the query.
     */
    private TableField<?>[] groupBy;    
    
    /**
     * If this is true, the select is going to contain the <pre>DISTINCT</pre>
     * keyword, in order to have no duplicate records.
     */
    private boolean distinct = false;   
    
    /**
     * Native or special part of the query are defined here. 
     */
    protected CustomOperators customOperators;
    
    /**
     * Creates a new instance of this class.
     * @param con Database connection 
     * @param table Main table read by this query
     * @param valueObjectFactory Factory which creates a new instance
     * of a value object and fills its properties with the values read from
     * a JDBC result set.
     */
    public SelectQuery( final Connection con,
                        final DBTable<?> table, 
                        final ObjectFactory<T> valueObjectFactory) {
        
        this(con, table.getFields(), 
             new DBTable[] { table }, new Join[0], 
             new ExpressionAPI[0],
             new TableField[0], 
             valueObjectFactory);
    }
    

    
    /**
     * Gets the metadata from the database connection.
     * @param con Database connection.
     * @return Contains the native part of a query, in order to use some specific
     * functionality for a certain database.
     */
    private void defineDBType(final Connection con) {        
        try {
            final DatabaseMetaData dbmd = con.getMetaData();
            final String dbName = dbmd.getDatabaseProductName();
            if (POSTGRESQL.equals(dbName)) {
               this.customOperators = new CustomPGOperators();
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SelectQuery.class.getName())
                  .log(Level.SEVERE, 
                  "Impossible to get the database name from the connection metadata.", ex);
        }        
    }
    
    /**
     * Creates a new instance of this class.
     * @param con Database connection.
     * @param selectFields Fields that appear in the select part of the query
     * @param table Main table read by this query
     * @param whereCondition <code>WHERE</code> section of this query
     * @param valueObjectFactory Factory which creates a new instance
     * of a value object and fills its properties with the values read from
     * a JDBC result set.
     */
    public SelectQuery( final Connection con,
                        final TableField<?>[] selectFields,
                        final DBTable<?> table,
                        final ExpressionAPI whereCondition,
                        final ObjectFactory<T> valueObjectFactory) {
    	
    	this(con, selectFields,
             new DBTable[] { table }, 
             new Join[0],
             new ExpressionAPI[] { whereCondition },
             new TableField<?>[0],
             valueObjectFactory);
    	
    }
    
    /**
     * Creates a new instance of this class.
     * @param con Database connection
     * @param selectFields Fields that appear in the select part of the query.
     * @param tables Tables read by this query.
     * @param joins These are the joins to some external tables.
     * @param whereCondition <code>WHERE</code> section of this query.
     * @param orderByFields Fields that are in the <code>ORDER BY</code> section of the query.
     * @param valueObjectFactory Factory which creates a new instance
     * of a value object and fills its properties with the values read from
     * a JDBC result set.
     */
    public SelectQuery(final Connection con,
                       final TableField<?>[] selectFields,
                       final DBTable<?>[] tables,
                       final Join<?>[] joins,
                       final ExpressionAPI[] whereCondition,
                       final TableField<?>[] orderByFields,
                       final ObjectFactory<T> valueObjectFactory) {

        this.selectFields.addAll(Arrays.asList(selectFields));

        if (tables.length == 1)
            this.table = tables[0];
        else
            this.crossedTables = tables;

        this.joins = joins;
        getFilteredQuery().setWhere(whereCondition);
        this.orderBy = orderByFields;

        this.valueObjectFactory = valueObjectFactory;
        
        defineDBType(con);
    }
    
    public CustomOperators getCustomOperators() {
        return this.customOperators;
    }
    
    public void setSelectFields(final TableField<?>[] selectFields) {
        this.selectFields = new LinkedList<TableField<?>>(Arrays.asList(selectFields));
    }
    
    public void setJoin(final Join<?>[] joins) {
        this.joins = joins;
    }
    
    public void setOrderBy(final TableField<?>[] orderByFields) {
        this.orderBy = orderByFields;
    }
    
    /**
     * Setter of the <code>HAVING</code> section.
     * @param havingFields These expressions are in the <code>HAVING</code> section of the query.
     */
    public final void setHaving(final ExpressionAPI[] havingFields) {
    	this.having = havingFields;
    }
    
    /**
     * Setter of the <code>GROUP BY</code> section.
     * @param groupByFields These fields are in the <code>GROUP BY</code> section of the query.
     */
    public final void setGroupBy(final TableField<?>[] groupByFields) {
    	this.groupBy = groupByFields;
    }

    /**
     * This method creates the <code>select</code> section of the query
     * @param sbQuery Query string to be completed
     */
    private void createSelect(final StringBuilder sbQuery) {
        sbQuery.append(SELECT);
        if (this.distinct)
                sbQuery.append(DISTINCT);

        int i = 0;
        for (TableField<?> field : selectFields) {
            i++;
            sbQuery.append(' ').append(field.getCompleteName())
                   .append(AS_F).append(i).append(',');
        }
        final int len = sbQuery.length();
        sbQuery.deleteCharAt(len - 1);		
    }

    /**
     * This method creates the <code>FROM</code> section of the query
     * @param sbQuery Query string to be completed
     */
    private void createFrom(final StringBuilder sbQuery) {
        sbQuery.append(FROM);
        // Lists all the fields to be read
        if ((this.crossedTables != null) && (this.crossedTables.length > 0)) {
            for (DBTable<?> t : this.crossedTables) {
                sbQuery.append(t.getCompleteName()).append(',');
            }
            sbQuery.deleteCharAt(sbQuery.length() - 1);
        }
        else
            sbQuery.append(this.table.getCompleteName());
	
    }

    /**
     * This method creates the <code>JOIN</code> section of the query
     * @param sbQuery Query string to be completed
     */
    private void createJoin(final StringBuilder sbQuery) {
        final int joinLen = this.joins.length;
        for (int i = 0; i < joinLen; i++) {
            final Join<?> join = this.joins[i];
            sbQuery.append(join.createQueryPart());
        }		
    }

    /**
     * This method creates the <code>WHERE</code> section of the query
     * @param sbQuery Query string to be completed.
     */
    private void createWhereClause(final StringBuilder sbQuery) {
    	final ExpressionAPI[] whereExp = this.filteredQuery.getExpression();
    	if ((whereExp != null) && (whereExp.length > 0)) {
            sbQuery.append(WHERE); 
            for (ExpressionAPI expressionAPI : this.filteredQuery.getExpression())
                sbQuery.append(expressionAPI.createString());
    	}
    }
    
    /**
     * This method creates the <code>GROUP BY</code> section of the query.
     * @param sbQuery Query string to be completed.
     */
    private void createGroupBy(final StringBuilder sbQuery) {
    	if ((this.groupBy != null) && (this.groupBy.length > 0)) {
            sbQuery.append(GROUP_BY);
            for (TableField<?> field : this.groupBy) {
                    sbQuery.append(field.getCompleteName()).append(',');
            }
            sbQuery.deleteCharAt(sbQuery.length() - 1);
    	}
    }

    /**
     * This method creates the <code>HAVING</code> section of the query.
     * @param sbQuery Query string to be completed.
     */
    private void createHaving(final StringBuilder sbQuery) {
    	if ((this.having != null) && (this.having.length > 0)) {
            sbQuery.append(HAVING);
            for (ExpressionAPI exp : this.having) {
                sbQuery.append(exp.createString());
            }
    	}
    }

    /**
     * This method creates the <code>ORDER BY</code> section of the query.
     * @param sbQuery Query string to be completed.
     */
    private void createOrderBy(final StringBuilder sbQuery) {
        if ((this.orderBy != null) && (this.orderBy.length > 0)) {
            sbQuery.append(ORDER_BY);
            for (TableField<?> field : this.orderBy) {
                sbQuery.append(field.getCompleteName()).append(',');
            }
            sbQuery.deleteCharAt(sbQuery.length() - 1);
        }
    }

    @Override
    public void setWhere(TableField<?> field) {
    	getFilteredQuery().setWhere(field);
    }

    @Override
    public void setWhere(ExpressionAPI exp) {
    	getFilteredQuery().setWhere(exp);
    }

    @Override
    public void setWhere(ExpressionAPI[] exp) {
    	getFilteredQuery().setWhere(exp);
    }

    /**
     * This method get the where part manager of the query.
     * @return This class contains some reusable code for the queries that have
     * a <code>WHERE</code> clause.
     */
    private synchronized FilteredQuery getFilteredQuery() {
    	// Lazy constructor
    	if (this.filteredQuery == null)
    		this.filteredQuery = new FilteredQuery();
    	return this.filteredQuery;
    }

    @Override
    public String createQueryString() {
        final StringBuilder sbQuery = new StringBuilder();
    	
        createSelect(sbQuery);
        createFrom(sbQuery);
        createJoin(sbQuery);
        createWhereClause(sbQuery);
        createGroupBy(sbQuery);
        createHaving(sbQuery);
        createOrderBy(sbQuery);

        final String queryStr = sbQuery.toString();
        final Logger logger = Logger.getLogger(SelectQuery.class.getName());
        logger.log(LOG_LEVEL, queryStr);

        return queryStr;
    }
    
    @Override
    public TableField<?>[] getSelectFields() {
    	return this.selectFields.toArray(new TableField<?>[this.selectFields.size()]);
    }

    @Override
    public synchronized T getSingleResult() throws SQLException {
    	if (this.readQuery == null)
            this.readQuery = new ReadQuery<T>(this.valueObjectFactory);

    	this.readQuery.setPstm(this.pstm);
    	return this.readQuery.getSingleResult();
    }

    @Override
    public synchronized T getSingleResultAndClose() throws SQLException {
    	final T result = getSingleResult();
    	if (this.pstm != null)
    		this.pstm.close();
    	return result;
    }
    
    @Override
    public synchronized List<T> getResultList() throws SQLException {
    	if (this.readQuery == null)
    		this.readQuery = new ReadQuery<T>(this.valueObjectFactory);

    	this.readQuery.setPstm(this.pstm);
    	return this.readQuery.getResultList();
    }

    @Override
    public synchronized List<T> getResultAndClose() throws SQLException {
    	final List<T> result = getResultList();
    	if (this.pstm != null)
    		this.pstm.close();

    	return result;
    }

    /**
     * Setter of the presence of a <pre>DISTINCT</pre>
     * keyword after the SELECT, in order to get no duplicated records.
     * @param distinct If true, a DISTINCT word is going to be
     * put just after the Select word.
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * Getter of the presence of a <pre>DISTINCT</pre>
     * keyword after the SELECT, in order to get no duplicated records.
     * @return If true, a DISTINCT word is going to be
     * put just after the Select word.
     */
    public boolean isDistinct() {
        return distinct;
    }
}