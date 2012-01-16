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
package org.byteliberi.easydriver.generator.model.methods;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.byteliberi.easydriver.generator.FieldPropertyAssociation;
import org.byteliberi.easydriver.generator.Utils;
import org.byteliberi.easydriver.generator.model.MetaEasyDriver;
import org.byteliberi.easydriver.generator.model.RelationModel;
import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * Creates the insert method of a service class.
 * @author Paolo Proni
 */
public class InsertMethod extends MethodModel {
	private List<FieldPropertyAssociation> fields;
	private String tableName;
	private String objectModel;
	private MetaEasyDriver tableInfo;
	
	/**
	 * Creates a new instance of this class
	 * @param fields Fields which match the columns
	 * @param tableName Table name
	 * @param objectModel Object model which contains the values to be inserted.
	 * @param tableInfo meta data informations about a table.
	 */
	public InsertMethod(final List<FieldPropertyAssociation> fields, final String tableName, final String objectModel, final MetaEasyDriver tableInfo) {
		super(Visibility.PUBLIC, "int", "insert", "objectModel");
		this.fields = fields;
		this.tableName = tableName;
		this.objectModel = objectModel;
		this.tableInfo = tableInfo;
	}
		
	@Override
	public void write(PrintStream out) {
		StringBuilder sb = new StringBuilder();
		
		sb.append('\t').append(visibility.getToken()).append(" final ").append(returnClass).append(' ').append(methodName)
		  .append("(final Connection con, final ").append(objectModel).append(" model) throws SQLException {");
		out.println(sb.toString());
		
		out.println(MessageFormat.format("\t\tfinal {0} tableStruct = {0}.INSTANCE;", tableName));
		out.println("\t\tfinal InsertQuery query = tableStruct.getTable().createInsertQuery();");
		out.println("\t\tquery.prepareQuery(con);");			
		
		// Let's look for the foreign keys
		final Map<String, String> fks = tableInfo.getExternalClasses();					
		final HashSet<String> usedTableNames = new HashSet<String>();
		
		for (FieldPropertyAssociation field : fields) {
			final String fieldName = field.getFieldName();
			// Let's check if the field is simple or a reference to an external class
			if (fks.containsKey(fieldName)) {
				// It's a foreign key	
				final List<RelationModel> relModelList = this.tableInfo.findRelByFK(fieldName);				
				final RelationModel foundRelation = this.tableInfo.findRelationModelByFK(relModelList, fieldName);
				
				final String propertyClass = Utils.getCamelNameFirstCapital ( foundRelation.getOneTable() ) + "ObjectModel";				
				final String propertyName = Utils.getCamelName( foundRelation.getOneTable() );
								
				if (!usedTableNames.contains(propertyClass)) {
					usedTableNames.add(propertyClass);
					
					final String referredProperty = Utils.getCamelNameFirstCapital(foundRelation.getRfManyTable() );
					
					out.println(MessageFormat.format("\t\tfinal {0} {1} = model.get{2}();", propertyClass, propertyName, referredProperty));
					out.println(MessageFormat.format("\t\tif ( {0} == null ) '{'", propertyName));
					
					for (int i = 0; i < relModelList.size(); i++)
						out.println(MessageFormat.format("\t\t\tquery.addNullParameter({0}.getEmpty());", field.getProp().getPropertyClass()));
					
					out.println("\t\t}");
					out.println("\t\telse {");
					for (RelationModel rm : relModelList)
						out.println(MessageFormat.format("\t\t\tquery.addParameter(model.get{0}().get{1}());", 
										referredProperty, Utils.getCamelNameFirstCapital( rm.getPkOneTable() )));
					out.println("\t\t}");
				}
			}
			else // It's a simple field
				out.println(MessageFormat.format("\t\tquery.addParameter(model.get{0}());", Utils.getCamelNameFirstCapital( fieldName )));
		
		}	
		out.println("\t\tint count = query.execute();");
		out.println("\t\tquery.close();");
		out.println("\t\treturn count;");
		out.println("\t}");
	}

}
