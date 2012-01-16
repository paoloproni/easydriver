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
package org.byteliberi.easydriver.generator;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.byteliberi.easydriver.generator.model.ClassModel;
import org.byteliberi.easydriver.generator.model.FieldInit;
import org.byteliberi.easydriver.generator.model.MetaEasyDriver;
import org.byteliberi.easydriver.generator.model.RelationModel;
import org.byteliberi.easydriver.generator.model.Visibility;
import org.byteliberi.easydriver.generator.model.methods.MapRS;

/**
 * Create a class which implements ObjectFactory, in order to read
 * the values from a single result set, to create a new instance 
 * of an object model and to fill its properties.
 * 
 * @author Paolo Proni
 */
public class BeanFactoryGeneration implements GenerationAPI {
	private List<MetaEasyDriver> metaData;
	private String packageName;
	
	/**
	 * Creates a new instance of this class
	 * @param metaData Each item contains the database metadata for a table or a view.
	 * @param packageName Package the future class will belong to.
	 */
	public BeanFactoryGeneration(final List<MetaEasyDriver> metaData, final String packageName) {
		this.metaData = metaData;
		this.packageName = packageName;
	}
	
	@Override
	public void generate(final String dirPath) throws FileNotFoundException, UnsupportedEncodingException {
		for (MetaEasyDriver tableInfo : metaData) {
			final String tableName = tableInfo.getTableName();			
			
			final String structureName = Utils.getCamelNameFirstCapital(tableName);
			final String objectModelName = structureName + "ObjectModel";
			final String className = objectModelName + "Factory";
			final PrintStream out = new PrintStream( Utils.getCamelNameFirstCapital(dirPath + className + ".java"), "UTF-8" );
						
			Logger.getLogger(ObjectModelGeneration.class.getName()).info("Working on: " + tableName);

			final ClassModel classModel = new ClassModel(Visibility.PUBLIC,	className);
			classModel.setPackageName(this.packageName);				
			classModel.addImport("java.sql.ResultSet");
			classModel.addImport("java.sql.SQLException");
			classModel.addImport("org.byteliberi.easydriver.ObjectFactory");
			classModel.setImplementList(MessageFormat.format("ObjectFactory<{0}>", objectModelName));
			
			classModel.addMethod(new MapRS(objectModelName, structureName, findFieldInit(tableInfo)));
			
			classModel.write(out);
			out.flush();
			out.close();
		}
	}
		
	/**
	 * Searches for the list of relations where a column name belongs to.
	 * @param extRel Complete collections of all the relations
	 * @param column Column name in a database table.
	 * @return List of relation informations where there is the specified
	 * column as foreign key.
	 */
	private List<RelationModel> findRelByColumn(final Collection<List<RelationModel>> extRel, final String column) {
		List<RelationModel> found = null;
		for (List<RelationModel> rmList : extRel) {
			for (RelationModel rm : rmList) {
				if (rm.getRfManyTable().equals(column)) {
					found = rmList;
					break;
				}
			}	 
		}
		return found;
	}
	
	
	/**
	 * Finds the columns
	 * @param dbMeta Database metadata.
	 * @param schemaName Name of the database schema.
	 * @param tableName Name of the table.
	 * @return List of javized names for columns.
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	private List<FieldInit> findFieldInit(MetaEasyDriver tableInfo) {
		
		final Collection<List<RelationModel>> relations = tableInfo.getRelationships();
		final List<FieldPropertyAssociation> fields = tableInfo.getFields();
				
		final LinkedList<FieldInit> found = new LinkedList<FieldInit>();		
				
		final HashSet<String> usedExtTables = new HashSet<String>();
		for (FieldPropertyAssociation fieldProp : fields) {
			
			FieldInit fpa=null;
			final String columnName = fieldProp.getFieldName();
				
			final List<RelationModel> relationModelList = findRelByColumn(relations, columnName);
			if (relationModelList == null)
				fpa = new FieldInit( Utils.getCamelNameFirstCapital(columnName) );
			else {
				final RelationModel relationModel = relationModelList.iterator().next(); 
				final String referencedTable = relationModel.getOneTable() + "ObjectModel";
				final String localColumnName = relationModel.getRfManyTable();
				if (!usedExtTables.contains(referencedTable)) {
					final LinkedList<String> columns = new LinkedList<String>();
					for (RelationModel rm : relationModelList)
						columns.add( Utils.getCamelNameFirstCapital(  rm.getRfManyTable() ));					
					
					fpa = new FieldInit(columns, 
										Utils.getCamelNameFirstCapital(localColumnName), 
										Utils.getCamelNameFirstCapital(referencedTable));
					
					usedExtTables.add(referencedTable);
				}
			}
			if (fpa != null)
				found.add(fpa);
		}
		
		return found;
	}
}
