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

import javax.swing.SwingUtilities;
import org.byteliberi.easydriver.generator.model.Preparation;

/**
 * This is the entry point of the program, it opens the window where
 * the user can input the parameters for the source generation.
 * 
 * @author Paolo Proni
 */
public class Main {
	
	/**
	 * Entry point method
	 * @param args no parameters are managed
	 */
	public static void main(String[] args) {
		// This property under Mac OS X puts a nice name in the upper bar.
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Easy Driver");
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				final MainFrame frame = new MainFrame();
				frame.setModel(new Preparation()); 
				frame.pack();
				frame.setVisible(true);
			}
		});

	}
}
