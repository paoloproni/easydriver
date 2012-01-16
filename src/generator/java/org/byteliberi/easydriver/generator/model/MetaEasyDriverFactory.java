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

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.byteliberi.easydriver.generator.FieldPropertyAssociation;
import org.byteliberi.easydriver.generator.GenerationException;
import org.byteliberi.easydriver.generator.Utils;


/**
 * This factory class creates the directories and reads the metadata from the database.
 * @author Paolo Proni
 */
public class MetaEasyDriverFactory {
	private final static String PUBLIC = "public";
			
	/**
	 * Creates the directories which match the package name
	 * @param basePath Directory where to start to write down the other directories.
	 * @param packageName Package name that the generated class will belong to.
	 * @return String with the complete path for creating the source names.
	 * @throws GenerationException It was not possible to create the directories
	 */
	public static String createDir(final String basePath, final String packageName) throws GenerationException {
		final StringBuilder sb = new StringBuilder(basePath);
		if (sb.charAt(sb.length() - 1) != File.separatorChar)
			sb.append(File.separatorChar);
		sb.append(packageName.replace('.', File.separatorChar));
		
		String path = sb.toString();
		
		final File dir = new File(path);
		if (!dir.exists()) {
			final boolean ok = dir.mkdirs();
			if (!ok)
				throw new GenerationException("Not created");
		}
		
		if (path.charAt(path.length() - 1) != File.separatorChar)
			path = path + File.separatorChar;
		
		return path;
	}
	
	/**
	 * Reads the metadata from the database
	 * @param con Database connection
	 * @return List where every item contains the metadata about a single table or a view.
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	public static List<MetaEasyDriver> readMetaData(final Connection con) throws SQLException {
		final LinkedList<MetaEasyDriver> entities = new LinkedList<MetaEasyDriver>();
		final DatabaseMetaData dbMeta = con.getMetaData();
		ResultSet rsTables = null;
		try {
			rsTables = dbMeta.getTables(null, PUBLIC, null, new String[] {"TABLE","VIEW"});			
			while (rsTables.next()) {
				final String tableName = rsTables.getString(3);
				final MetaEasyDriver metaEasyDriver = new MetaEasyDriver(tableName);
				metaEasyDriver.setSchema(PUBLIC);
				
				final List<PropertyModel> props = findProperties(dbMeta, PUBLIC, tableName);				
				final HashMap<String, String> propMap = new HashMap<String, String>(props.size());
				for (PropertyModel prop : props) {
					propMap.put(prop.getName(), prop.getPropertyClass());
				}
				
				final List<FieldPropertyAssociation> fieldAssoc = Utils.findFields(dbMeta, PUBLIC, tableName);
				
				metaEasyDriver.setFields(fieldAssoc);						
				metaEasyDriver.setProperties(props);
								
				
				/* Let's look for the primary key fields. The metadata do not read easily the type of the columns, 
				   so we look for the already found fields ! */
				final List<PropertyModel> pks = findPrimaryKeys(dbMeta, PUBLIC, tableName);								
				for (PropertyModel pm : pks) {
					pm.setPropertyClass(propMap.get(Utils.getCamelName(pm.getName())));
				}
				metaEasyDriver.setPrimaryKey(pks);				
				metaEasyDriver.setRelationships(findRelations(dbMeta, PUBLIC, tableName));	
								
				entities.add(metaEasyDriver);
			}
		}
		finally {
			if (rsTables != null)
				rsTables.close();
		}
		return entities;
	}
	
	/**
	 * Finds the fields which compose a primary key.
	 * @param dbMeta Database metadata.
	 * @param schemaName Name of the schema.
	 * @param tableName Name of the table.
	 * @return List properties made by a name and a type.
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	private static List<PropertyModel> findPrimaryKeys(final DatabaseMetaData dbMeta,
														final String schemaName,
														final String tableName) throws SQLException {
		
		final LinkedList<PropertyModel> result = new LinkedList<PropertyModel>();
		ResultSet rs = null;
		try {
			rs = dbMeta.getPrimaryKeys(null, schemaName, tableName);
			while (rs.next()) {
				final String pkName = rs.getString(4);
				final PropertyModel prop = new PropertyModel(Visibility.PRIVATE, "", Utils.getCamelName( pkName ));
				
				result.add(prop);
			}
		}
		finally {
			if (rs != null)
				rs.close();
		}
		return result;
	}
	
	/**
	 * Read the fields of a table and creates the model for some Java properties
	 * which match those fields.
	 * 
	 * @param dbMeta Database meta data
	 * @param schemaName Name of the database schema.
	 * @param tableName Name of the table
	 * @return List of property which match the columns
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	private static List<PropertyModel> findProperties(final DatabaseMetaData dbMeta, 
		 											  final String schemaName,
		 											  final String tableName) throws SQLException {
		
		final LinkedList<PropertyModel> found = new LinkedList<PropertyModel>();
		ResultSet rs = null;
		try {
			rs = dbMeta.getColumns(null, schemaName, tableName, null);			
			while (rs.next()) {
				final String columnName = Utils.getCamelName( rs.getString(4) );
				final int columnType = rs.getInt(5);
				final String className = Utils.getJavaType(columnType);
				found.add(new PropertyModel(Visibility.PRIVATE, className, columnName));
			}
		}
		finally {
			if (rs != null)
				rs.close();
		}
		return found;	
	}
	
	
	
	/**
	 * This method returns the Java class name which best matches with given column.
	 * @param dbMeta Database metadata.
	 * @param schemaName Name of the schema.
	 * @param table Name of the table.
	 * @param column Column name
	 * @return Java class name which matches the column type
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	private static String getJavaTypeForColumn(final DatabaseMetaData dbMeta, final String schemaName, final String table, final String column) throws SQLException {
		String javaType="";
		ResultSet rs = null;
		try {
			rs = dbMeta.getColumns(null, schemaName, table, column);
			while (rs.next()) {
				javaType = Utils.getJavaType(rs.getInt(5));
			}
		}
		finally {
			if (rs != null)
				rs.close();
		}
		return javaType;
	}
	
	/**
	 * Creates a collection where each item is a list of relations with related tables and fields.
	 * @param dbMeta Database metadata.
	 * @param schemaName Name of the schema.
	 * @param table Name of the table.
	 * @return Each item is a relationship between two tables, but every relationship is made by
	 * on or more fields. Each field couple is an element of the list.
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	private static Collection<List<RelationModel>> findRelations(final DatabaseMetaData dbMeta, final String schemaName, final String table) throws SQLException {					
		final HashMap<String, List<RelationModel>> foundRel = new HashMap<String, List<RelationModel>>();
		
		ResultSet rs = null;
		try {
			rs = dbMeta.getImportedKeys(null, schemaName, table);
			
			while (rs.next()) {
				final String oneTable = rs.getString(3);
				final String pkOneTable = rs.getString(4);
//				final String manyTable = rs.getString(7);
				final String rfManyTable = rs.getString(8);
				
				List<RelationModel> rels;
				if (foundRel.containsKey(oneTable)) 
					rels = foundRel.get(oneTable);
				else {
					rels = new LinkedList<RelationModel>();
					foundRel.put(oneTable, rels);
				}
				
				rels.add(new RelationModel(oneTable, pkOneTable, rfManyTable, getJavaTypeForColumn(dbMeta, schemaName, oneTable, pkOneTable)));
			}
		}
		finally {
			if (rs != null)
				rs.close();
		}
		
		return foundRel.values();
	}
}
