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
import java.util.LinkedList;
import java.util.List;

import org.byteliberi.easydriver.generator.Utils;
import org.byteliberi.easydriver.generator.model.methods.GetMethodModel;
import org.byteliberi.easydriver.generator.model.methods.MethodModel;
import org.byteliberi.easydriver.generator.model.methods.SetMethodModel;

/**
 * This model represents a Java class file, it is used to
 * create and write to an Output Stream the generated class.
 * 
 * @author Paolo Proni
 */
public class ClassModel implements ConstructorGeneratorAPI {
	/**
	 * Public, protected, package or private
	 */
	private Visibility visibility;
	
	/**
	 * Name of the class
	 */
	private String className;
	
	/**
	 * This is a list of packages which has to be imported by the compiler.
	 */
	private LinkedList<String> imports = new LinkedList<String>();
	
	/**
	 * These are the instance variables.
	 */
	private LinkedList<PropertyModel> properties = new LinkedList<PropertyModel>();
	
	/**
	 * These are the methods (such as getters and setters)
	 */
	private LinkedList<MethodModel> methods = new LinkedList<MethodModel>();
	
	/**
	 * This is the name of the package
	 */
	private String packageName="";
	
	/**
	 * When this is true, this is an enumeration, when it is false, this is a normal class.
	 */
	private boolean enumeration = false;
	
	/**
	 * This is a list of enumeration tokens, if this model is about an enum.
	 */
	private String enumList = "";
	
	/**
	 * This is a comma separated list of interfaces that the new class should implement
	 */
	private String implementList = "";
	
	/**
	 * This is the creator of the constructor method, if null, an empty constructor will be made.
	 */
	private ConstructorGeneratorAPI constructorGeneratorAPI = null; 
	
	/**
	 * Creates a new instance of this class, the generated code is going to be about a class, not an enum.
	 * @param visibility public, protected, package or private
	 * @param className Name of the class
	 */
	public ClassModel(final Visibility visibility, 
					  final String className) {
		
		this(visibility, className, false);
	}
	
	/**
	 * Creates a new instance of this class
	 * @param visibility public, protected, package or private
	 * @param className Name of the class
	 * @param enumeration When this is true, this is an enumeration, when it is false, this is a normal class.
	 */
	public ClassModel(final Visibility visibility, 
					  final String className, 
					  final boolean enumeration) {
		
		this.visibility = visibility;
		this.className = className;
		this.enumeration = enumeration;
	}
	
	/**
	 * Setter of the class which creates the generator 
	 * @param constructorGeneratorAPI This is the creator of the constructor method, if null, an empty constructor will be made.
	 */
	public final void setConstructorGenerator(final ConstructorGeneratorAPI constructorGeneratorAPI) {
		this.constructorGeneratorAPI = constructorGeneratorAPI;
	}
	
	/**
	 * Setter of the token for an enum.
	 * @param enumList This is a list of enumeration tokens, if this model is about an enum.
	 */
	public final void setEnumList(final String enumList) {
		this.enumList = enumList;
	}
	
	/**
	 * Append an instance variable 
	 * @param prop Property made by a name and a data type.
	 */
	public final void addProperty(final PropertyModel prop) {		
		this.properties.add(prop);
	}
	
	/**
	 * Getter of the instance variables.
	 * @return Properties made by name and data type.
	 */
	public final List<PropertyModel> getProperties() {
		return this.properties;
	}
	
	/**
	 * Appends a method
	 * @param method instance method
	 */
	public final void addMethod(final MethodModel method) {
		this.methods.add(method);
	}
	
	/**
	 * Appends an import item.
	 * @param importName packages which has to be imported by the compiler.
	 */
	public final void addImport(final String importName) {
		this.imports.add(importName);
	}
	
	/**
	 * Setter of the package name
	 * @param packageName Name of the package the generate class will belong to.
	 */
	public final void setPackageName(final String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * This method creates a getter method for each property.
	 */
	public final void createGetters() {
		for (PropertyModel property : this.properties) {
			final StringBuilder sb = new StringBuilder();
			final String propName = property.getName();
			
			sb.append("get").append(Utils.getCamelNameFirstCapital(propName));		
						
			this.methods.add(new GetMethodModel(Visibility.PUBLIC, 
												property.getPropertyClass(),
												sb.toString(),
												property.getName()));
		}
	}
	
	/**
	 * This method creates a setter method for each property.
	 */
	public final void createSetters() {
		for (PropertyModel property : this.properties) {
			final StringBuilder sb = new StringBuilder();
			final String propName = property.getName();
			
			sb.append("set").append(Utils.getCamelNameFirstCapital(propName));
			
			this.methods.add(new SetMethodModel(Visibility.PUBLIC, 
												property.getPropertyClass(),
												sb.toString(),
												property.getName()));
		}
	}
		
	@Override
	public void write(final PrintStream out) {
		out.println("package " + this.packageName + ';');
		out.println();
		for (String importRow : this.imports) {
			out.println("import " + importRow + ';');			
		}
		out.println();
		out.print(this.visibility.getToken() + " ");
		final String classNameFC = Utils.getCamelNameFirstCapital(this.className);
		if (this.enumeration) {
			out.println("enum " + classNameFC + " {");
			out.println('\t' + this.enumList + ';');
		}
		else {
			out.print("class " + classNameFC);
			if ((this.implementList != null) && (this.implementList.length() > 0)) {
				out.print(" implements " + this.implementList);
			}
			out.println(" {");
		}
		out.println();
		for (PropertyModel property : this.properties) {
			property.write(out);			
		}
		out.println();
		if (this.constructorGeneratorAPI == null) {
			out.println("\tpublic " + classNameFC + "() {");
			out.println("\t}");
		}
		else	
			this.constructorGeneratorAPI.write(out);
		
		out.println();
		for (MethodModel method : this.methods) {
			method.write(out);
			out.println();
		}
		
		// end
		out.println("}");
	}

	public final void setImplementList(String implementList) {
		this.implementList = implementList;
	}

	public final String getImplementList() {
		return implementList;
	}
}
