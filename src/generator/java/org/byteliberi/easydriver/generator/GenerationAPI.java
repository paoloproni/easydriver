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
import java.io.UnsupportedEncodingException;

/**
 * This interface is implemented by the class or enum generators
 * @author Paolo Proni
 */
public interface GenerationAPI {
	public final static String OBJECT_MODEL = "ObjectModel";
	
	/**
	 * This method generates the class.
	 * @param dirPath Directory where to write the file
	 * @throws UnsupportedEncodingException This platform cannot write UTF-8 file.
	 * @throws FileNotFoundException A problem occurred with the filesystem while
	 * creating the directories.
	 */	
	public void generate(final String dirPath) throws FileNotFoundException, UnsupportedEncodingException;
}
