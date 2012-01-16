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
import java.util.List;

import org.byteliberi.easydriver.impl.RelatedFields;
import org.byteliberi.easydriver.impl.Relationship;

/**
 * This class is the relationship between a single record of
 * table A and only one record of table B.<P>
 * This kind of relation joins the primary keys. 
 * 
 * @author Paolo Proni
 * @since 1.0
 * @version 1.0
 * 
 * @param F foreign key type
 */
public class OneToOne<F> implements Relationship<F> {	
	private List<RelatedFields<F>> relatedFields;
    private DBTable<F> relatedTable;
    private String alias;
    
    /**
     * This constructor supposes that the tables have a simple primary key
     * made by a single field.
     * @param mainTable Table in the FROM part of the select query
     * @param relatedTable Table in the JOIN part of the select query
     */	
	public OneToOne(final DBTable<F> mainTable, final DBTable<F> relatedTable) {
		this.relatedTable = relatedTable;
		final TableField<F> fieldA = mainTable.getPrimaryKey().getSingleField();
		final TableField<F> fieldB = relatedTable.getPrimaryKey().getSingleField();
		
		this.relatedFields = new ArrayList<RelatedFields<F>>();
		this.relatedFields.add(new RelatedFields<F>(fieldA, fieldB));
	}
		
	@Override
	public DBTable<F> getRelatedTable() {
		return this.relatedTable;
	}

	@Override
	public List<RelatedFields<F>> getRelationFields() {
		return this.relatedFields;
	}
	
    public String getAlias() {
    	return this.alias;
    }
    
    public void setAlias(String alias) {
    	this.alias = alias;
    }
}
