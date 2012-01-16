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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.byteliberi.easydriver.generator.model.ClassModel;
import org.byteliberi.easydriver.generator.model.MetaEasyDriver;
import org.byteliberi.easydriver.generator.model.PrimaryKeyConstructor;
import org.byteliberi.easydriver.generator.model.PropertyModel;
import org.byteliberi.easydriver.generator.model.RelationModel;
import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * This class generates a Java class for each table or view,
 * it can be used as object model.<br/>
 * It is not strictly bound to the relational model, as a
 * property is created for the foreign keys and it does
 * not know what is the primary key.
 * 
 * @author Paolo Proni
 */
public class ObjectModelGeneration implements GenerationAPI {
	private List<MetaEasyDriver> metaData;
	private String packageName;
	
	/**
	 * Creates a new instance of this class
	 * @param con Open database connection.
	 * @param packageName Name of the package.
	 * @param schemaName Name of the schema.
	 */
	public ObjectModelGeneration(final List<MetaEasyDriver> metaData, final String packageName) {
		this.metaData = metaData;
		this.packageName = packageName;
	}
	
	/**
	 * For each foreign key, it creates a reference to the matching Java class.
	 * @param tableInfo Database metadata.
	 * @param classModel Model of the table that is going to be created.
	 * @return Map where for each item the key is the referred table name and the 
	 *  value is the referred column name.
	 */
	private Map<String, String> addExternalClasses(final MetaEasyDriver tableInfo, final ClassModel classModel) {
		final Map<String, String> fks = new HashMap<String, String>();
		Collection<List<RelationModel>> rmlc = tableInfo.getRelationships();
		if (rmlc != null) {
			for (List<RelationModel> rml : rmlc) {
				if (rml != null) {
					for (RelationModel rm : rml) {
						if (rm != null) {
							fks.put(Utils.getCamelName(rm.getRfManyTable()), rm.getOneTable());
						}
					}				
				}
			}
		}		
		return fks;
	}
		
	/**
	 * Creates the file content
	 * @param out Stream where to write the generated structure.
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	public void generate(final String dirPath) throws FileNotFoundException, UnsupportedEncodingException {
		for (MetaEasyDriver tableInfo : metaData) {
			final String tableName = tableInfo.getTableName();			
						
			final String structureName = Utils.getCamelNameFirstCapital(tableName); 
			final String className = structureName + "ObjectModel";
			final PrintStream out = new PrintStream( Utils.getCamelNameFirstCapital(dirPath + className + ".java"), "UTF-8" );
			Logger.getLogger(ObjectModelGeneration.class.getName()).info("Working on: " + tableName);
				
			final ClassModel classModel = new ClassModel(Visibility.PUBLIC,	className, false);
			classModel.setPackageName(this.packageName);
			
			// Let's look for the foreign keys and let's add the properties
			final Map<String, String> fks = addExternalClasses(tableInfo, classModel);
					
			// There can be more than one field for just one external table. 
			// Let's group by the Class name, saving just the first column name.
			final HashSet<String> usedClasses = new HashSet<String>();
			final Set<Entry<String,String>> fksEntries = fks.entrySet();
			for (Entry<String, String> fksEntry : fksEntries) {
				final String examinedClass = Utils.getCamelNameFirstCapital( fksEntry.getValue() ) + "ObjectModel";
				if (!usedClasses.contains(examinedClass)) {
					usedClasses.add(examinedClass);
					classModel.addProperty(new PropertyModel(Visibility.PRIVATE, examinedClass, fksEntry.getKey()));
				}
			}
			
			// Let's add the properties for the columns
			for (PropertyModel prop : tableInfo.getProperties()) {
				if (!fks.containsKey(prop.getName()))
					classModel.addProperty(prop);
			}
			
			final List<PropertyModel> primaryKeys = new LinkedList<PropertyModel>();
			final List<PropertyModel> presentProps = classModel.getProperties();
			for(PropertyModel primaryKey : tableInfo.getPrimaryKey()) {
				final String key = primaryKey.getName();
				for (PropertyModel presentProp : presentProps) {
					 if (presentProp.getName().equals(key)) {
						 primaryKeys.add(new PropertyModel(Visibility.PRIVATE, presentProp.getPropertyClass(), key));
					 }
				}
			}
			
			classModel.setConstructorGenerator(new PrimaryKeyConstructor(className, primaryKeys));
			
			// Getters
			classModel.createGetters();
			// Setters
			classModel.createSetters();
			
			classModel.write(out);
			out.flush();
			out.close();
		}				
	}		
}