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

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import org.byteliberi.easydriver.fields.BooleanField;
import org.byteliberi.easydriver.generator.FieldPropertyAssociation;
import org.byteliberi.easydriver.generator.Utils;

/**
 * Creates the structure classes 
 * @author Paolo Proni
 */
public class TableStructureConstructor implements ConstructorGeneratorAPI {	
	/**
	 * Table name
	 */
	private String table;
	
	/**
	 * List of sssociations of a column and the property name and type.
	 */
	private List<FieldPropertyAssociation> fieldAssoc;
	
	/**
	 * Collection where each element is a list of relationship fields,
	 * as a relationship can be made by more than one field.
	 */
	private Collection<List<RelationModel>> relationships;
	
	/**
	 * List of properties (name and type) which are the primary fields.
	 */
	private List<PropertyModel> primaryKeys;
	
	/**
	 * Constructor.
	 * @param tableName Name of the table
	 * @param fieldAssoc List of sssociations of a column and the property name and type.
	 * @param relationships Collection where each element is a list of relationship fields,
	 * as a relationship can be made by more than one field.
	 * @param primaryKeys List of properties (name and type) which are the primary fields.
	 */
	public TableStructureConstructor(final String tableName, 
									 final List<FieldPropertyAssociation> fieldAssoc, 
									 final Collection<List<RelationModel>> relationships,
									 final List<PropertyModel> primaryKeys) {
		
		this.table = tableName;
		this.fieldAssoc = fieldAssoc;
		this.relationships = relationships;
		this.primaryKeys = primaryKeys;
	}
	
	@Override
	public void write(final PrintStream out) {
		out.println("\tprivate " + Utils.getCamelNameFirstCapital( this.table ) + "() {");
		// Table
		out.println("\t\tthis.table = new DBTable(\"" + this.table + "\");");
		out.println();
		
		// Columns
		writeColumns(out);
		
		// Relations
		writeRelations(out);
		
		// Primary Keys
		writePrimaryKeys(out);
		
		out.println("\t}");
	}
	
	/**
	 * Writes the properties which match the database columns. 
	 * @param out Print Stream (a file, propably...)
	 */
	private void writeColumns(final PrintStream out) {
		for (FieldPropertyAssociation fp : this.fieldAssoc) {			
			final PropertyModel prop = fp.getProp();
			final String propertyClass = prop.getPropertyClass();
			if (BooleanField.class.getSimpleName().equals(propertyClass)) {
				out.println(MessageFormat.format(
						"\t\tthis.{0} = new {1}(\"{2}\", table);", 
						prop.getName(), prop.getPropertyClass(), 
						fp.getFieldName()));
			}
			else {
				out.println(MessageFormat.format(
					"\t\tthis.{0} = new {1}(\"{2}\", {3}, table);", 
					prop.getName(), prop.getPropertyClass(), 
					fp.getFieldName(), fp.isNullable()));
			}
		}
		out.println();
	}
	
	/**
	 * Writes the properties which match the foreign columns.
	 * @param out Print Stream (a file, propably...)
	 */
	private void writeRelations(final PrintStream out) {
		if (this.relationships != null) {
			for (List<RelationModel> relList : this.relationships) {
				if ((relList != null) && (relList.size() > 0)) {
					final RelationModel firstRel = relList.get(0);
					final String sideOne = Utils.getCamelNameFirstCapital( firstRel.getOneTable() );					
					final String prop = "fk" + sideOne;
					out.print(MessageFormat.format(
							"\t\tthis.{0} = new ManyToOne(table, {1}.INSTANCE.getTable(), new org.byteliberi.easydriver.impl.RelatedFields<?>[] '{' ", prop, sideOne));
					
					final StringBuilder sb = new StringBuilder();
					for (RelationModel rel : relList) {
						final String pkSideOne = Utils.getCamelNameFirstCapital(rel.getPkOneTable());						
						final String relField = Utils.getCamelName( rel.getRfManyTable() );
						sb.append( MessageFormat.format("new org.byteliberi.easydriver.impl.RelatedFields<{0}>(this.{1}, {2}.INSTANCE.get{3}()),",
											rel.getJavaType(), relField, sideOne, pkSideOne) );						
					}
					sb.deleteCharAt(sb.length() - 1);
					sb.append("});");
					out.println(sb.toString());
				}
			}
		}		
		out.println();
	}
	
	/**
	 * Writes the properties which match the foreign columns.
	 * @param out Print Stream (a file, propably...) 
	 */
	private void writePrimaryKeys(final PrintStream out) {
		if (this.primaryKeys != null) {
			if (this.primaryKeys.size() == 1) {
				final PropertyModel propModel =  this.primaryKeys.get(0);
				out.println(MessageFormat.format("\t\tthis.table.setPrimaryKey(new PrimaryKey<{0}>(this.{1}));",
												propModel.getPropertyClass(),
												Utils.getCamelName( propModel.getName() ) )); 
			}
			else {
				final StringBuilder sb = new StringBuilder();
				sb.append("\t\ttable.setPrimaryKey(new PrimaryKey<Void>(new TableField<?>[] {");
				for (PropertyModel propertyModel : this.primaryKeys) {
					sb.append(MessageFormat.format("this.{0},", Utils.getCamelName(propertyModel.getName())));
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("}));");
				out.println(sb.toString());
			}
		}
	}

	
}