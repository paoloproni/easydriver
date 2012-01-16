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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.byteliberi.easydriver.fields.BigDecimalField;
import org.byteliberi.easydriver.fields.BooleanField;
import org.byteliberi.easydriver.fields.ByteArrayField;
import org.byteliberi.easydriver.fields.CharField;
import org.byteliberi.easydriver.fields.DateField;
import org.byteliberi.easydriver.fields.DoubleField;
import org.byteliberi.easydriver.fields.IntField;
import org.byteliberi.easydriver.fields.TimestampField;
import org.byteliberi.easydriver.fields.UUIDField;
import org.byteliberi.easydriver.fields.VarcharField;
import org.byteliberi.easydriver.generator.model.MetaEasyDriverFactory;
import org.byteliberi.easydriver.generator.model.PropertyModel;
import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * This class contains some useful static methods.
 * @author Paolo Proni
 */
public class Utils {
	
	/**
	 * Takes a string with <code>_</code> and removes that, tranforming
	 * the following character in upper code.
	 * 
	 * @param dbStyleName String to be transformed.
	 * @return Camelized string.
	 */
	public static String getCamelName(final String dbStyleName) {
		boolean underscoreFound = false;
		final StringBuilder sb = new StringBuilder();
		final int len = dbStyleName.length();
		for (int i = 0; i < len; i++) {
			final char character = dbStyleName.charAt(i);
			if (character == '_') 
				underscoreFound = true;				
			else {
				if (underscoreFound) {
					underscoreFound = false;
					sb.append(String.valueOf(character).toUpperCase());
				}
				else
					sb.append(character);
			}			
		}
		
		return sb.toString();
	}
	
	/**
	 * Transform in upper case the first character of a String, than
	 * calls {@link #getCamelName(String)} 
	 * @param dbStyleName String to be transformed.
	 * @return Camelized string.
	 */
	public static String getCamelNameFirstCapital(final String dbStyleName) {
		final StringBuilder sb = new StringBuilder(dbStyleName.length());
		sb.append(dbStyleName.substring(0, 1).toUpperCase());
		sb.append(dbStyleName.substring(1));
		
		return getCamelName(sb.toString());		
	}
	
	/**
	 * Returns the Java class name which matches the SQL type
	 * @param columnType Type of the database column
	 * @return name of the java class
	 */
	public static String getJavaType(final int columnType) {
		String javaType="";
		switch (columnType) {
		case Types.NUMERIC: case Types.DECIMAL:
			javaType = java.math.BigDecimal.class.getName();
			break;
		case Types.BIT: case Types.BOOLEAN:
			javaType = Boolean.class.getSimpleName();					
			break;
		case Types.CHAR: case Types.VARCHAR:
			javaType = String.class.getSimpleName();
			break;
		case Types.DATE: case Types.TIMESTAMP:
			javaType = java.util.Date.class.getName();
			break;
		case Types.DOUBLE:
			javaType = Double.class.getSimpleName();
			break;
		case Types.INTEGER:
			javaType = Integer.class.getSimpleName();
			break;		
		case Types.BINARY:
			javaType = "byte[]";
			break;
		case Types.OTHER:  // TODO What if it is not UUID ?
			javaType = java.util.UUID.class.getName();
			break;
		default:
			javaType = Object.class.getSimpleName();
		}
		return javaType;
	}
	
	/**
	 * Finds the columns
	 * @param dbMeta Database metadata.
	 * @param schemaName Name of the database schema.
	 * @param tableName Name of the table.
	 * @return List of associations of column name and properties, made by a name and class type.
	 * @throws SQLException A problem occurred with the query or the database itself.
	 */
	public static List<FieldPropertyAssociation> findFields(final DatabaseMetaData dbMeta, 
													  		 final String schemaName,
													  		 final String tableName) throws SQLException {
		
		final LinkedList<FieldPropertyAssociation> found = new LinkedList<FieldPropertyAssociation>();
		ResultSet rs = null;
		try {
			rs = dbMeta.getColumns(null, schemaName, tableName, null);
			
			while (rs.next()) {
				final FieldPropertyAssociation fpa = new FieldPropertyAssociation();
				
				final String columnName = rs.getString(4);								
				fpa.setFieldName( columnName );
				final String propName = Utils.getCamelName(columnName);
				
				final int dataType = rs.getInt(5);
				fpa.setNullable( "YES".equals( rs.getString(18) ) );
				
				switch (dataType) {
				case Types.NUMERIC: case Types.DECIMAL:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, BigDecimalField.class.getSimpleName(), propName));
					break;
				case Types.BIT: case Types.BOOLEAN:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, BooleanField.class.getSimpleName(), propName));					
					break;
				case Types.CHAR:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, CharField.class.getSimpleName(), propName));
					break;
				case Types.DATE:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, DateField.class.getSimpleName(), propName));
					break;
				case Types.DOUBLE:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, DoubleField.class.getSimpleName(), propName));
					break;
				case Types.INTEGER:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, IntField.class.getSimpleName(), propName));
					break;
				case Types.TIMESTAMP:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, TimestampField.class.getSimpleName(), propName));
					break;
				case Types.BINARY:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, ByteArrayField.class.getSimpleName(), propName));
					break;
				case Types.OTHER:  // TODO What if it is not UUID ?
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, UUIDField.class.getSimpleName(), propName));
					break;
				case Types.VARCHAR:
					fpa.setProp(new PropertyModel(Visibility.PRIVATE, VarcharField.class.getSimpleName(), propName));
					break;
				default:
					Logger.getLogger(MetaEasyDriverFactory.class.getSimpleName()).severe(columnName + " " + rs.getString(6) + " " + dataType);
				}
			
				found.add(fpa);
			}
		}
		finally {
			if (rs != null)
				rs.close();
		}
		return found;
	}
}
