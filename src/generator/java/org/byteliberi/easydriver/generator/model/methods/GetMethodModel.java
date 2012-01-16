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

import org.byteliberi.easydriver.generator.model.Visibility;

/**
 * This class models a a getter method
 * @author Paolo Proni
 */
public class GetMethodModel extends MethodModel {
	
	/**
	 * Creates a new instance of this class
	 * @param visibility Public, protected, package or private.
	 * @param returnClass Type of the return value or void.
	 * @param methodName Name of the instance method.
	 * @param propName Name of the property.
	 */
	public GetMethodModel(final Visibility visibility, final String returnClass, 
						  final String methodName, final String propName) {
		super(visibility, returnClass, methodName, propName);
	}

	@Override
	public void write(PrintStream out) {
		StringBuilder sb = new StringBuilder();
		sb.append('\t').append(visibility.getToken()).append(' ')
		  .append("final ").append(returnClass).append(' ')
		  .append(methodName).append("() {");				  
		
		out.println(sb.toString());
	
		sb = new StringBuilder();
		sb.append("\t\treturn ").append(propName).append(';');
		out.println(sb.toString());
		
		out.println("\t}");
	}

}
