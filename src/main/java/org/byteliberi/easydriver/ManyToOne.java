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
package org.byteliberi.easydriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.byteliberi.easydriver.impl.RelatedFields;
import org.byteliberi.easydriver.impl.Relationship;

/**
 * This class is the relationship between many fields of
 * table A and only one record of table B.<P>
 * It is used usually for a table that contains a reference
 * to another table. 
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 * @param F foreign key type
 */
public class ManyToOne<F> implements Relationship<F> {
	/**
	 * Table with the <code>many</code> part of the relationship
	 */
    private DBTable<F> mainTable;
    
    /**
     * Table with the <code>one</code> part of the relationship
     */
    private DBTable<F> relatedTable;
    
    /**
     * Fields that take part in the relationship
     */
    private List<RelatedFields<F>> relationFields;
    
    private String alias;
    
    /**
     * Creates a new instance of this class.
     * @param mainTable Table with the <code>many</code> part of the relationship
     * @param relatedTable Table with the <code>one</code> part of the relationship
     * @param relField Field that take part in the relationship
     */
    public ManyToOne(final DBTable<F> mainTable,
                     final DBTable<F> relatedTable,
                     final RelatedFields<F> relField) {
    	
    	this.mainTable = mainTable;
        this.relatedTable = relatedTable;
        this.relationFields = new ArrayList<RelatedFields<F>>();
        this.relationFields.add(relField);
    }

    /**
     * Creates a new instance of this class.
     * @param mainTable Table with the <code>many</code> part of the relationship
     * @param relatedTable Table with the <code>one</code> part of the relationship
     * @param relFields Fields that take part in the relationship
     */
    public ManyToOne(final DBTable<F> mainTable,
                     final DBTable<F> relatedTable,
                     final RelatedFields<F>[] relFields) {

    		this(mainTable, relatedTable, new ArrayList<RelatedFields<F>>(Arrays.asList(relFields)));                
    }
    
    public ManyToOne(final DBTable<F> mainTable,
            		 	final DBTable<F> relatedTable,
            		 	final ArrayList<RelatedFields<F>> relFields) {
    	
    	this.mainTable = mainTable;
        this.relatedTable = relatedTable;
        this.relationFields = relFields;
    }


    /**
     * Getter of the main table
     * @return Table with the <code>many</code> part of the relationship
     */
    public final DBTable<F> getMainTable() {
        return mainTable;
    }

    /**
     * Setter of the main table 
     * @param mainTable Table with the <code>many</code> part of the relationship
     */
    public final void setMainTable(final DBTable<F> mainTable) {
        this.mainTable = mainTable;
    }

    /**
     * Getter of the related table
     * @return Table with the <code>one</code> part of the relationship
     */
    public final DBTable<F> getRelatedTable() {
        return relatedTable;
    }

    /**
     * Setter of the related table 
     * @param relatedTable Table with the <code>one</code> part of the relationship
     */
    public final void setRelatedTable(final DBTable<F> relatedTable) {
        this.relatedTable = relatedTable;
    }

    /**
     * Getter of the fields that take part in the relation
     * @return Fields that take part in the relationship
     */
    public final List<RelatedFields<F>> getRelationFields() {
        return relationFields;
    }

    /**
     * Setter of the fields that take part in the relation
     * @param relationFields Fields that take part in the relationship. An ArrayList
     * is preferred.
     */
    public final void setRelationFields(final List<RelatedFields<F>> relationFields) {
        this.relationFields = relationFields;
    }
    
    public String getAlias() {
    		return this.alias;
    }
    
    public void setAlias(String alias) {
    		this.alias = alias;
    }
}