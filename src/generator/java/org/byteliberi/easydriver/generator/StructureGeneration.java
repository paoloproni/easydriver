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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.byteliberi.easydriver.DBTable;
import org.byteliberi.easydriver.ManyToOne;
import org.byteliberi.easydriver.generator.model.ClassModel;
import org.byteliberi.easydriver.generator.model.MetaEasyDriver;
import org.byteliberi.easydriver.generator.model.PropertyModel;
import org.byteliberi.easydriver.generator.model.RelationModel;
import org.byteliberi.easydriver.generator.model.TableStructureConstructor;
import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * This class generate the structure classes, where each class
 * represents a table or a view, with its fields, its foreign keys
 * and its primary keys.
 * 
 * @author Paolo Proni 
 */
public class StructureGeneration implements GenerationAPI {
	private List<MetaEasyDriver> metaData;
	private String packageName;
	
	/**
	 * Creates a new instance of this class.
	 * @param con Database connection.
	 * @param packageName Name of the package.
	 * @param schemaName Name of the schema.
	 */
	public StructureGeneration(final List<MetaEasyDriver> metaData, final String packageName) {	
		this.metaData = metaData;
		this.packageName = packageName;
	}
		
	/**
	 * This method generates the class
	 * @param dirPath Directory where to write the file
	 * @throws UnsupportedEncodingException This platform cannot write UTF-8 file.
	 * @throws FileNotFoundException 
	 */	
	public void generate(final String dirPath) throws FileNotFoundException, UnsupportedEncodingException {
		Logger logger = Logger.getLogger(ObjectModelGeneration.class.getName());
		for (MetaEasyDriver tableInfo : metaData) {
			final String tableName = tableInfo.getTableName();
			final PrintStream out = new PrintStream( dirPath + Utils.getCamelNameFirstCapital(tableName) + ".java", "UTF-8" );
			
			logger.info("Working on: " + tableName);

			final ClassModel classModel = new ClassModel(Visibility.PUBLIC,	tableName, true);
				
			classModel.setEnumList("INSTANCE");
			classModel.setPackageName(this.packageName);
			classModel.addImport("org.byteliberi.easydriver.*");	
			classModel.addImport("org.byteliberi.easydriver.fields.*");
			
			classModel.addProperty(new PropertyModel(Visibility.PRIVATE, DBTable.class.getSimpleName(), "table"));

			// Let's look for the fields				
			final List<FieldPropertyAssociation> fieldAssoc = tableInfo.getFields();
			final HashSet<String> usedFields = new HashSet<String>();
			
			for (FieldPropertyAssociation fa : fieldAssoc) {
				final String fieldName = fa.getFieldName();
				if (!usedFields.contains(fieldName)) {
					classModel.addProperty(fa.getProp());
					usedFields.add(fieldName);
				}
			}
				
			// Let's look for the relationships
			final Collection<List<RelationModel>> rels = tableInfo.getRelationships();
			// Now we add the properties for the relationships that we have found					
			
			final HashSet<String> ext = new HashSet<String>();
			if ((rels != null) && (rels.size() > 0)) {
				Iterator<List<RelationModel>> relsIterator = rels.iterator();
				while (relsIterator.hasNext()) {
					final List<RelationModel> relList = relsIterator.next();
					if (relList.size() > 0) {
						final String oneTable = relList.get(0).getOneTable();
						ext.add(oneTable);
					}
				}				
			}	
			
			for (String name : ext) {
				classModel.addProperty(new PropertyModel(Visibility.PRIVATE,
														 ManyToOne.class.getSimpleName(), 
														 "fk" + Utils.getCamelNameFirstCapital(name)));
			}
				
			classModel.setConstructorGenerator(new TableStructureConstructor(tableName, fieldAssoc, rels, tableInfo.getPrimaryKey()));

			classModel.createGetters();
			classModel.write(out);
			out.flush();
			out.close();
		}	
	}	
}
