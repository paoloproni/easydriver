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
 * Creates the select method for a service. The created method will select
 * a single record by the primary key values.
 *  
 * @author Paolo Proni
 */
public class SelectByPKMethod extends MethodModel {
	private List<PropertyModel> pks;
	private String objectModel;
	private String structure;
	
	/**
	 * Creates a new instance of this class.
	 * @param pks Properties about the primary key fields.
	 * @param structure Table name for the structure.
	 * @param objectModel Object model name.
	 */
	public SelectByPKMethod(final List<PropertyModel> pks, final String structure, final String objectModel) {
		super(Visibility.PUBLIC, objectModel, "selectByPK", "pk");
		this.pks = pks;
		this.objectModel = objectModel;
		this.structure = structure;
	}
	
	@Override
	public void write(PrintStream out) {
		StringBuilder sb = new StringBuilder();
		
		sb.append('\t').append(visibility.getToken()).append(" final ").append(returnClass).append(' ').append(methodName).append("(final Connection con, ");
		for (PropertyModel model : pks)
		  sb.append("final ").append(model.getPropertyClass()).append(' ').append(Utils.getCamelName( model.getName() )).append(',');
		
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") throws SQLException {");
		out.println(sb.toString());
		out.println(MessageFormat.format("\t\tfinal {0} tableStruct = {0}.INSTANCE;", structure));
		out.println(MessageFormat.format("\t\tfinal SelectQuery<{0}> query = new SelectQuery<{0}>({1}.INSTANCE.getTable(), new {0}Factory());",
					objectModel, structure));
		
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
		
		out.println("\t\treturn query.getSingleResultAndClose();");
		
		out.println("\t}");
	}
}
