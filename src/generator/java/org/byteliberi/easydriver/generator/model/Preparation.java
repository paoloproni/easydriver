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
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.byteliberi.easydriver.generator.BeanFactoryGeneration;
import org.byteliberi.easydriver.generator.GenerationException;
import org.byteliberi.easydriver.generator.ObjectModelGeneration;
import org.byteliberi.easydriver.generator.ServiceGeneration;
import org.byteliberi.easydriver.generator.StructureGeneration;

/**
 * Loads the database driver, instantiates it and starts the
 * creation of the source code.
 * 
 * @author Paolo Proni
 */
public class Preparation implements DBParameterAPI {
	private String connection;
	private String destDir;
	private String driver;
	private String packageName;
	private String password;
	private String userName;
	
	@Override
	public void generate() throws MalformedURLException, SQLException, GenerationException, FileNotFoundException, UnsupportedEncodingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Logger logger = Logger.getAnonymousLogger();
		logger.info("Generation button pressed");
		logger.info(connection);
		logger.info(destDir);
		logger.info(driver);
		logger.info(packageName);
		logger.info(password);
		logger.info(userName);
		
		URL u = new URL( "jar:file:" + driver + "!/");				
		String classname = "org.postgresql.Driver";
		URLClassLoader ucl = new URLClassLoader(new URL[] { u });
		Driver d = (Driver)Class.forName(classname, true, ucl).newInstance();
		DriverManager.registerDriver(new DriverShim(d));
		DriverManager.getConnection(connection, userName, password);
				
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(connection, userName, password);
			final List<MetaEasyDriver> metaData = MetaEasyDriverFactory.readMetaData(con);					
						
			final String baseDir = MetaEasyDriverFactory.createDir(destDir, packageName);
									 
			final StructureGeneration structure = new StructureGeneration(metaData, packageName);
			structure.generate(baseDir);
			
			final ObjectModelGeneration omGen = new ObjectModelGeneration(metaData, packageName);
			omGen.generate(baseDir);
			
			final BeanFactoryGeneration beanGen = new BeanFactoryGeneration(metaData, packageName);
			beanGen.generate(baseDir);
			
			final ServiceGeneration serviceGen = new ServiceGeneration(metaData, packageName);
			serviceGen.generate(baseDir);
		}
		finally {
			if (con != null)
				con.close();
		}
	}
		
	@Override
	public void setCon(String connection)  {
		this.connection = connection;
	}

	@Override
	public void setDestinationDir(String destDir) {
		this.destDir = destDir;
	}

	@Override
	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Override
	public void setPackage(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setUser(String userName) {
		this.userName = userName;
	}
}
