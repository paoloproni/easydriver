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
import java.util.List;

import org.byteliberi.easydriver.generator.Utils;
import org.byteliberi.easydriver.generator.model.PropertyModel;
import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * Creates the delete method in a service class.
 * @author Paolo Proni
 */
public class DeleteMethod extends MethodModel {
	/**
	 * Primary keys in the form of a model for Java properties.
	 */
	private List<PropertyModel> pks;
	
	/**
	 * Table name.
	 */
	private String tableName;
	
	/**
	 * Creates a new instance of this class.
	 * @param pks Primary keys in the form of a model for Java properties.
	 * @param tableName Table name.
	 */
	public DeleteMethod(final List<PropertyModel> pks, final String tableName) {
		super(Visibility.PUBLIC, "int", "deleteByPK", "pk");
		this.pks = pks;
		this.tableName = tableName;
	}

	@Override
	public void write(final PrintStream out) {
		StringBuilder sb = new StringBuilder();
		
		sb.append('\t').append(visibility.getToken()).append(" final ").append(returnClass).append(' ').append(methodName).append("(final Connection con, ");
		for (PropertyModel model : pks)
		  sb.append("final ").append(model.getPropertyClass()).append(' ').append(Utils.getCamelName( model.getName() )).append(',');
		
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") throws SQLException {");
		out.println(sb.toString());
		
		out.println(MessageFormat.format("\t\tfinal {0} tableStruct = {0}.INSTANCE;", tableName));
		out.println("\t\tfinal DeleteQuery query = tableStruct.getTable().createDeleteQuery();");
		out.print("\t\tquery.setWhere(");		
			
		sb = new StringBuilder();
		final int andCount = pks.size() - 1;
		if (andCount == 0) {
			sb.append("new Equals(").append(MessageFormat.format("tableStruct.get{0}()", Utils.getCamelNameFirstCapital( pks.get(0).getName() ))).append("));");
			out.println(sb.toString());
		}
		else {
			sb.append("new ExpressionAPI[] { ");
			int indexEqual = 0;
			for (int i = 0; i < andCount; i++) {
				sb.append("new And(");
				sb.append("new Equals(").append(MessageFormat.format("tableStruct.get{0}()", Utils.getCamelNameFirstCapital( pks.get(indexEqual++).getName() ))).append("),");
				sb.append("new Equals(").append(MessageFormat.format("tableStruct.get{0}()", Utils.getCamelNameFirstCapital( pks.get(indexEqual++).getName() ))).append(")");
				sb.append("),");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" });");
			out.println(sb.toString());
		}
		
		out.println("\t\tquery.prepareQuery(con);");
		
		for (PropertyModel prop : pks)
			out.println(MessageFormat.format("\t\tquery.addParameter({0});", prop.getName()));
		
		out.println("\t\tint count = query.execute();");
		out.println("\t\tquery.close();");
		out.println("\t\treturn count;");		
		out.println("\t}");
	}
}
