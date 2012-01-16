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
import java.util.List;
import org.byteliberi.easydriver.generator.model.ClassModel;
import org.byteliberi.easydriver.generator.model.MetaEasyDriver;
import org.byteliberi.easydriver.generator.model.Visibility;
import org.byteliberi.easydriver.generator.model.methods.DeleteMethod;
import org.byteliberi.easydriver.generator.model.methods.InsertMethod;
import org.byteliberi.easydriver.generator.model.methods.SelectByPKMethod;
import org.byteliberi.easydriver.generator.model.methods.UpdateMethod;

public class ServiceGeneration implements GenerationAPI {
	private List<MetaEasyDriver> metaData;
	private String packageName;
	
	/**
	 * Creates a new instance of this class.
	 * @param con Database connection.
	 * @param packageName Name of the package.
	 * @param schemaName Name of the schema.
	 */
	public ServiceGeneration(final List<MetaEasyDriver> metaData, final String packageName) {	
		this.metaData = metaData;
		this.packageName = packageName;
	}
	
	@Override
	public void generate(final String dirPath) throws FileNotFoundException, UnsupportedEncodingException {
		for (MetaEasyDriver tableInfo : metaData) {
			final String tableName = tableInfo.getTableName();
						
			final String structureName = Utils.getCamelNameFirstCapital(tableName); 
			final String className = structureName + "Service";
			final PrintStream out = new PrintStream( Utils.getCamelNameFirstCapital(dirPath + className + ".java"), "UTF-8" );
			final String objectModelName = structureName + OBJECT_MODEL;
						
			final ClassModel classModel = new ClassModel(Visibility.PUBLIC,	className);
			classModel.setPackageName(this.packageName);
			classModel.addImport("java.sql.Connection");
			classModel.addImport("java.sql.SQLException");
			classModel.addImport("org.byteliberi.easydriver.*");
			classModel.addImport("org.byteliberi.easydriver.fields.*");
			classModel.addImport("org.byteliberi.easydriver.expressions.*");
			
				
			classModel.addMethod(new SelectByPKMethod(tableInfo.getPrimaryKey(), structureName, objectModelName));
			classModel.addMethod(new DeleteMethod(tableInfo.getPrimaryKey(), structureName));
			classModel.addMethod(new InsertMethod(tableInfo.getFields(), structureName, objectModelName, tableInfo));
			classModel.addMethod(new UpdateMethod(tableInfo.getFields(), tableInfo.getPrimaryKey(), structureName, objectModelName, tableInfo));
				
			classModel.write(out);
			out.flush();
			out.close();
		}	
	}
}
