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
package org.byteliberi.easydriver.generator.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.byteliberi.easydriver.generator.FieldPropertyAssociation;

/**
 * This class contains some meta informations about the tables.
 * Using an instance of this class it is not necessary to query
 * the database metadata for each class generation.
 * 
 * @author Paolo Proni
 */
public class MetaEasyDriver {
	/**
	 * This is the schema name
	 */
	private String schema;
	
	/**
	 * This is the table name
	 */
	private String tableName;
	
	/**
	 * This is a list of fields that are in the primary key
	 */
	private List<PropertyModel> primaryKey;
	
	/**
	 * Each item of the collection is a ManyToOne relationship,
	 * where the element of the list are couples of fields between
	 * which matches the foreign key of the 'many' side and
	 * the primary key of the 'one' side of the relationship.  
	 */
	private Collection<List<RelationModel>> relationships;
	
	/**
	 * These are the fields which belong to the table or the view.
	 */
	private List<FieldPropertyAssociation> fields;
	
	/**
	 * These are the property which match the columns in a table or in a view
	 */
	private List<PropertyModel> properties;
	
	/**
	 * Creates a new instance of this class.
	 */
	public MetaEasyDriver(final String tableName) {
		this.tableName = tableName;
	}
	
	public List<RelationModel> findRelByFK(final String foreignKey) {
		List<RelationModel> ret = new LinkedList<RelationModel>();
		boolean found = false;
		final Iterator<List<RelationModel>> extRelIterator = this.relationships.iterator();
		while (extRelIterator.hasNext() && !found) {
			final List<RelationModel> rmList = extRelIterator.next();
			final RelationModel rm = findRelationModelByFK(rmList, foreignKey);
			if (rm != null) {
				ret = rmList;
				found = true;
			}			
		}
		return ret;
	}
	
	public RelationModel findRelationModelByFK(final List<RelationModel> rmList, final String foreignKey) {
		boolean found = false;
		RelationModel ret = null;
		final Iterator<RelationModel> rmIterator = rmList.iterator();
		while (rmIterator.hasNext() && !found) {
			final RelationModel rm = rmIterator.next();
			if (rm.getRfManyTable().equals(foreignKey)) {
				ret = rm;
				found = true;
			}
		}
		return ret;
	}
	
	/**
	 * For each foreign key, it creates a reference to the matching Java class.
	 * @param tableInfo Database metadata.
	 * @param classModel Model of the table that is going to be created.
	 * @return Map where for each item the key is the referred table name and the 
	 *  value is the referred column name.
	 */
	public Map<String, String> getExternalClasses() {
		final Map<String, String> fks = new HashMap<String, String>();		
		if (relationships != null) {
			for (List<RelationModel> rml : relationships) {
				if (rml != null) {
					for (RelationModel rm : rml) {
						if (rm != null) {
							fks.put(rm.getRfManyTable(), rm.getOneTable());
						}
					}										
				}
			}
		}		
		return fks;
	}
	
	public final String getSchema() {
		return schema;
	}

	public final void setSchema(final String schema) {
		this.schema = schema;
	}

	public final String getTableName() {
		return tableName;
	}

	public final void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	public final List<PropertyModel> getPrimaryKey() {
		return primaryKey;
	}

	public final void setPrimaryKey(final List<PropertyModel> primaryKey) {
		this.primaryKey = primaryKey;
	}

	public final Collection<List<RelationModel>> getRelationships() {
		return relationships;
	}

	public final void setRelationships(final Collection<List<RelationModel>> relationships) {
		this.relationships = relationships;
	}

	public final List<FieldPropertyAssociation> getFields() {
		return fields;
	}

	public final void setFields(final List<FieldPropertyAssociation> fields) {
		this.fields = fields;
	}

	public final List<PropertyModel> getProperties() {
		return properties;
	}

	public final void setProperties(final List<PropertyModel> properties) {
		this.properties = properties;
	}
}
