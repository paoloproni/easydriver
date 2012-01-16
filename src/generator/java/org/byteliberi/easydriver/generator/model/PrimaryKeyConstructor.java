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
import java.util.List;

import org.byteliberi.easydriver.generator.Utils;

/**
 * This class creates two constructors: one has no parameters and the other
 * has the parameters that match the fields in the primary key.
 * 
 * @author Paolo Proni
 */
public class PrimaryKeyConstructor implements ConstructorGeneratorAPI {
	private final String className;
	private final List<PropertyModel> params;
	
	/**
	 * Creates a new instance of this class.
	 * @param className Name of the class.
	 * @param params Parameter which have to be passed to the constructor.
	 */
	public PrimaryKeyConstructor(final String className, final List<PropertyModel> params) {
		this.className = Utils.getCamelNameFirstCapital( className );
		this.params = params;
	}
	
	@Override
	public void write(final PrintStream out) {
		// Empty constructor
		out.println("\tpublic " + className + "() {");
		out.println("\t}");
		out.println();
		
		// Constructor for the primary key
		out.print("\tpublic " + className + "(");
		final StringBuilder sb = new StringBuilder();
		for (PropertyModel pm : this.params) {
			sb.append("final ").append(pm.getPropertyClass()).append(' ')
			  .append(Utils.getCamelName( pm.getName()) ).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") {");
		out.println(sb.toString());
		
		for (PropertyModel pm : this.params) {
			final String propName = Utils.getCamelName( pm.getName());
			out.println(MessageFormat.format("\t\tthis.{0} = {0};", propName));
		}
				
		out.println("\t}");
	}
}
