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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import org.byteliberi.easydriver.generator.model.DBParameterAPI;

/**
 * This is the form where the user set the connection parameters
 * and the destination point for the generated code.
 * 
 * @author Paolo Proni
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = -6446671350477388201L;
	
	/**
	 * Driver Label
	 */
	private JLabel lblDriver = null;
	
	/**
	 * Driver text field
	 */
	private JTextField txtDriver = null;
	
	/**
	 * Database connection label
	 */
	private JLabel lblCon = null;
	
	/**
	 * Database connection text field
	 */
	private JTextField txtCon = null;
	
	/**
	 * User name label
	 */
	private JLabel lblUsr = null;
	
	/**
	 * User name text field
	 */
	private JTextField txtUsr = null;
	
	/**
	 * Password label
	 */
	private JLabel lblPwd = null;
	
	/**
	 * Password text field
	 */
	private JPasswordField txtPwd = null;
	
	/**
	 * Package name label
	 */
	private JLabel lblPackage = null;
	
	/**
	 * Package text field
	 */
	private JTextField txtPackage = null;
	
	/**
	 * Destination directory label
	 */
	private JLabel lblDestDir = null;
	
	/**
	 * Destination directory text field
	 */
	private JTextField txtDestDir = null;
	
	/**
	 * This button opens the driverFileChooser
	 */
	private JButton btnChooseDriver = null;
	
	/**
	 * This button let choose a directory where to write the generated .java files.
	 */
	private JButton btnChooseDestination = null;
	
	/**
	 * This button starts the java source generation
	 */
	private JButton btnGenerate = null;
	
	/**
	 * This chooser let the user choose a file which contains the database driver
	 */
	private JFileChooser driverFileChooser = null;
	
	/**
	 * This chooser let the user choose a a directory where to write the generated .java files.
	 */
	private JFileChooser destinationFileChooser = null;
	
	/**
	 *  his object is going to receive the parameter values and start the generation.
	 */
	private DBParameterAPI model = null;
	
	/**
	 * Creates a new instance of this class, setting all the GUI elements and
	 * their callback actions.
	 */
	public MainFrame() {
		setTitle("Easy Driver Generation");
		
		final int numPairs = 6;		
				
		final JPanel panel = new JPanel(new SpringLayout());
		
		// Driver
		lblDriver = new JLabel("Driver:", JLabel.TRAILING);
		panel.add(lblDriver);
		txtDriver = new JTextField(40);
		txtDriver.setToolTipText("Example: /usr/shared/postgresql-8.4-701.jdbc4.jar");
		lblDriver.setLabelFor(txtDriver);
		
		this.driverFileChooser = new JFileChooser();
		this.driverFileChooser.setAcceptAllFileFilterUsed(false);
		this.driverFileChooser.addChoosableFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				return ".jar or .zip";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
			    }
				String ext = null;
		        String s = f.getName();
		        int i = s.lastIndexOf('.');

		        if (i > 0 &&  i < s.length() - 1) {
		            ext = s.substring(i+1).toLowerCase();
		        }
				if (ext == null)
					return false;
				
				return ".jar".equals(ext.toLowerCase()) || ".zip".equals(ext.toLowerCase());
			}
		});
		
		
		this.btnChooseDriver = new JButton("Browse");
		this.btnChooseDriver.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent event) {
				int returnVal = driverFileChooser.showDialog(MainFrame.this, "Choose the database driver");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File mapFile = driverFileChooser.getSelectedFile();
					txtDriver.setText(mapFile.getAbsolutePath());
				}
			}
		});
		
		final JPanel driverPanel = new JPanel(new BorderLayout());
		driverPanel.add(txtDriver, BorderLayout.CENTER);
		driverPanel.add(btnChooseDriver, BorderLayout.EAST);
		panel.add(driverPanel);
				
		// Connection
		lblCon = new JLabel("Connection:", JLabel.TRAILING);
		panel.add(lblCon);
		txtCon = new JTextField(30);
		txtCon.setText("jdbc:");
		txtCon.setToolTipText("Example: jdbc:postgresql://myserveraddress/mydatabasename");
		lblCon.setLabelFor(txtCon);
		panel.add(txtCon);
		
		// User
		lblUsr = new JLabel("User:", JLabel.TRAILING);
		panel.add(lblUsr);
		txtUsr = new JTextField(20);
		txtUsr.setToolTipText("Example: paolo");
		lblUsr.setLabelFor(txtUsr);
		panel.add(txtUsr);
		
		// Password
		lblPwd = new JLabel("Password:", JLabel.TRAILING);
		panel.add(lblPwd);
		txtPwd = new JPasswordField(20);
		txtPwd.setToolTipText("Example: secret123");
		lblPwd.setLabelFor(txtPwd);
		panel.add(txtPwd);
		
		// Package
		lblPackage = new JLabel("Package:", JLabel.TRAILING);
		panel.add(lblPackage);
		txtPackage = new JTextField(10);
		txtPackage.setToolTipText("Example: org.byteliberi.myapp.datalayer");
		lblPackage.setLabelFor(txtPackage);
		panel.add(txtPackage);
		
		// Destination Directory
		lblDestDir = new JLabel("Destination Dir.:", JLabel.TRAILING);
		panel.add(lblDestDir);
		txtDestDir = new JTextField(10);
		txtDestDir.setToolTipText("Example: /home/paolo/workspace/myproject/src/");
		lblDestDir.setLabelFor(txtDestDir);
		this.destinationFileChooser = new JFileChooser();
		this.destinationFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		this.btnChooseDestination = new JButton("Browse");
		this.btnChooseDestination.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent event) {
				int returnVal = destinationFileChooser.showDialog(MainFrame.this, "Choose the destination directory");
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					final File mapFile = destinationFileChooser.getSelectedFile();
					txtDestDir.setText(mapFile.getAbsolutePath());
				}
			}
		});
		
		final JPanel destinationPanel = new JPanel(new BorderLayout());
		destinationPanel.add(txtDestDir, BorderLayout.CENTER);
		destinationPanel.add(btnChooseDestination, BorderLayout.EAST);
		panel.add(destinationPanel);
		
//		panel.add(txtDestDir);
		
		// Generation Button
		this.btnGenerate = new JButton("Generate !");	
		this.btnGenerate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				final String txtDriverStr = txtDriver.getText();
				if ((txtDriverStr == null) || (txtDriverStr.length() == 0)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Driver path not inserted");
					txtDriver.requestFocusInWindow();
					return;
				}
				model.setDriver(txtDriver.getText());
				
				final String txtConStr = txtCon.getText(); 
				if ((txtConStr == null) || (txtConStr.length() == 0)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Connection address not inserted");
					txtCon.requestFocusInWindow();
					return;
				}
				model.setCon(txtConStr);
				
				final String txtUserStr = txtUsr.getText();
				if ((txtUserStr == null) || (txtUserStr.length() == 0)) {
					JOptionPane.showMessageDialog(MainFrame.this, "User name not inserted");
					txtUsr.requestFocusInWindow();
					return;
				}
				model.setUser(txtUsr.getText());
				
				final char[] txtPasswordChar = txtPwd.getPassword();
				model.setPassword(new String(txtPasswordChar));
				
				final String txtPackageStr = txtPackage.getText();
				if ((txtPackageStr == null) || (txtPackageStr.length() == 0)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Package name not inserted");
					txtPackage.requestFocusInWindow();
					return;
				}
				model.setPackage(txtPackage.getText());
				
				final String txtDestDirStr = txtDestDir.getText();
				if ((txtDestDirStr == null) || (txtDestDirStr.length() == 0)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Destination directory not inserted");
					txtDestDir.requestFocusInWindow();
					return;
				}					
				model.setDestinationDir(txtDestDirStr);
				
				try {
					model.generate();
					JOptionPane.showMessageDialog(MainFrame.this, "Generation completed !");
				}
				catch (MalformedURLException ex) {
					Logger.getAnonymousLogger().info(ex.getMessage());					
					JOptionPane.showMessageDialog(MainFrame.this, "Cannot load the JDBC driver");					
				}
				catch (SQLException ex) {
					String msg = ex.getMessage();
					if ((msg == null) || (msg.length() == 0))
						msg = "A problem occurred with the query or the database itself";
					Logger.getAnonymousLogger().info(msg);					
					JOptionPane.showMessageDialog(MainFrame.this, msg);
				}
				catch (GenerationException ex) {
					ex.getMessage();					
					JOptionPane.showMessageDialog(MainFrame.this, "A problem occurred during the code generation");
				}
				catch (FileNotFoundException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(MainFrame.this, "File not found");
				}
				catch (UnsupportedEncodingException ex) {
					Logger.getAnonymousLogger().info(ex.getMessage());
					JOptionPane.showMessageDialog(MainFrame.this, "The UTF-8 codec is not available");
				}
				catch (ClassNotFoundException ex) {
					JOptionPane.showMessageDialog(MainFrame.this, "Cannot load the JDBC driver");
					ex.printStackTrace();
				} catch (InstantiationException ex) {
					JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage());
					ex.printStackTrace();
				} catch (IllegalAccessException ex) {
					JOptionPane.showMessageDialog(MainFrame.this, ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
				
		SpringUtilities.makeCompactGrid(panel,
										numPairs, 2, // rows, cols
										6, 6,		 // initX, initY
										6, 6);		 // xPad, yPad
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		final BorderLayout borderLayout = new BorderLayout();
		final JPanel externalPanel = new JPanel(borderLayout);
		externalPanel.add(panel, BorderLayout.CENTER);
		
		final JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		northPanel.add(new JLabel("by Paolo Proni (http://www.byteliberi.org/)"));		
		externalPanel.add(northPanel, BorderLayout.NORTH);
		
		final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5 ,5));
		southPanel.add(this.btnGenerate);
		externalPanel.add(southPanel, BorderLayout.SOUTH);
		
		panel.setOpaque(true);
//		setContentPane(panel);		
		
		setContentPane(externalPanel);
	}	
	
	/**
	 * Setter of the model in the Model View Controller pattern
	 * @param param This object is going to receive the parameter values and start the generation.
	 */
	public final void setModel(final DBParameterAPI param) {
		this.model = param;
	}
}
