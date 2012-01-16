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

import java.util.List;
import java.io.PrintStream;
import java.text.MessageFormat;

import org.byteliberi.easydriver.generator.model.FieldInit;
import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * Creates the method which read the values from a Result Set of an object model mapper.
 * 
 * @author Paolo Proni
 */
public class MapRS extends MethodModel {
	/**
	 * This is the name 
	 */
	private String structureClassName;
	
	/**
	 * Informations about the fields that will receive the data.
	 */
	private List<FieldInit> fields;	
	
	/**
	 * Creates a new instance of this class
	 * @param returnClass Class that will be returned by the generated method
	 * @param structureClassName Name of a structure.
	 * @param fields Informations about the fields that will receive the data.
	 */
	public MapRS(final String returnClass, final String structureClassName, 
				 final List<FieldInit> fields) {
		super(Visibility.PUBLIC, returnClass, "map", "ResultSet rs");
		this.structureClassName = structureClassName;
		this.fields = fields;
	}
	
	@Override
	public void write(final PrintStream out) {
		out.println("\t@Override");
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append(visibility.getToken()).append(' ')
			.append("final ").append(returnClass).append(' ')
			.append(methodName).append("(")
			.append("final ").append(propName)
			.append(") throws SQLException {");
		out.println(sb.toString());
		
		out.println(MessageFormat.format("\t\tfinal {0} table = {0}.INSTANCE;", structureClassName)); 
		out.println(MessageFormat.format("\t\tfinal {0} vo = new {0}();", returnClass));
		
		int i = 1;
		for (FieldInit field : this.fields) {
			if (field.isSimpleField())
				out.println(MessageFormat.format("\t\tvo.set{0}( table.get{1}().map(rs, {2}) );", field.getFieldName(), field.getFieldName(), i++));
			else {
				out.print(MessageFormat.format("\t\tvo.set{0}( new {1}(", field.getPropertyName(), field.getReferredClassName()));
				final List<String> fieldNames = field.getFieldNames();
				final StringBuilder sbFields = new StringBuilder();
				for (String fieldName : fieldNames) {
					sbFields.append(MessageFormat.format("table.get{0}().map(rs, {1})", fieldName, i++) ).append(',');
				}
				sbFields.deleteCharAt(sbFields.length() - 1);
				out.print(sbFields.toString());
				out.println("));");
			}
		}	

		out.println("\t\treturn vo;");
		out.println("\t}");
	}

}
