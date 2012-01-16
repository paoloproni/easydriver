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

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import org.byteliberi.easydriver.generator.GenerationException;

/**
 * This interface is the model for the user interface, such as a GUI, like MainFrame. 
 * @author Paolo Proni
 */
public interface DBParameterAPI {

	/**
	 * Setter of the database driver file.
	 * @param driver File name for the database driver file.
	 */
	public void setDriver(String driver);
	
	/**
	 * Setter of the database connection
	 * @param connection Database connection string.
	 */
	public void setCon(String connection);
	
	/**
	 * Setter of the user name
	 * @param userName User name
	 */
	public void setUser(String userName);
	
	/**
	 * Setter of the password
	 * @param password Password
	 */
	public void setPassword(String password);
	
	/**
	 * Setter of the package name
	 * @param packageName Package String which is going to be added at the top of 
	 */
	public void setPackage(String packageName);
	
	/**
	 * Setter of the destination directory
	 * @param destDir Directory where the package structure is going to be written
	 */
	public void setDestinationDir(String destDir);
	
	/**
	 * Generates the source files.<P/>
	 * For each table, it creates a structure file which has the javized table name,
	 * an object model, an object model factory and a service file with the basic operations.
	 * @throws MalformedURLException A problem occurred reading the driver file name as an url
	 * @throws SQLException The database rises this exception, where there are some troubles,
	 * such as 
	 * @throws GenerationException It was not possible to create the directories.
	 * @throws FileNotFoundException An I/O problem occurred
	 * @throws UnsupportedEncodingException UTF-8 is not available
	 * @throws ClassNotFoundException Database driver not found
	 * @throws InstantiationException A Problem occurred during the instantiation of the loaded driver
	 * @throws IllegalAccessException A Problem occurred during the instantiation of the loaded driver
	 */
	public void generate() throws MalformedURLException, SQLException, GenerationException, 
									FileNotFoundException, UnsupportedEncodingException, ClassNotFoundException, 
									InstantiationException, IllegalAccessException;
}
