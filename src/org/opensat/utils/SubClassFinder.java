/*
 * OpenSAT, the Open Satisfiability platform
 * Copyright (C) 2002 Joao Marques Silva and Daniel Le Berre 
 * 
 * JSAT, the Java-based Satisfiability library
 * Copyright (C) 2001  Daniel Le Berre
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * SubClass.java
 *
 *
 * Created: Wed Jan 24 11:15:02 2001
 *
 */
package org.opensat.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * This utility class is looking for all the classes implementing or
 * inheriting from a given class in the package given in parameter
 * or in the currently loaded packages.
 *
 * @author <a href="mailto:daniel@satlive.org">Daniel Le Berre</a>
 * @version 1.1, 2002/11/30 21:27:48
 */
public class SubClassFinder {

	/**
	 * Display all the classes inheriting or implementing a given
	 * class in the currently loaded packages.
	 * @param tosubclassname the name of the class to inherit from
	 */
	public static List find(String tosubclassname)
		throws ClassNotFoundException {
		Class tosubclass = Class.forName(tosubclassname);
		Package[] pcks = Package.getPackages();
		List list = new ArrayList();

		for (int i = 0; i < pcks.length; i++) {
			list.addAll(find(pcks[i].getName(), tosubclass));
		}

		return list;
	}

	/**
	 * Display all the classes inheriting or implementing a given
	 * class in a given package.
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the name of the class to inherit from
	 */
	public static List find(String pckname, String tosubclassname)
		throws ClassNotFoundException {
		Class tosubclass = Class.forName(tosubclassname);
		return find(pckname, tosubclass);
	}

	/**
	 * Display all the classes inheriting or implementing a given
	 * class in a given package.
	 * @param pckgname the fully qualified name of the package
	 * @param tosubclass the Class object to inherit from
	 */
	public static List find(String pckgname, Class tosubclass)
		throws ClassNotFoundException {
		// Code from JWhich
		// ======
		// Translate the package name into an absolute path
		List list = new ArrayList();
		String name = new String(pckgname);
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		name = name.replace('.', '/');

		// Get a File object for the package
		URL url = tosubclass.getResource(name);
		if (url != null) {
			File directory = new File(url.getFile());

			// New code
			// ======
			if (directory.exists()) {
				// Get the list of the files contained in the package
				String[] files = directory.list();
				for (int i = 0; i < files.length; i++) {

					// we are only interested in .class files
					if (files[i].endsWith(".class")) {
						// removes the .class extension
						String classname =
							files[i].substring(0, files[i].length() - 6);
						try {
							// Try to create an instance of the object
							Object o =
								Class
									.forName(pckgname + "." + classname)
									.newInstance();
							if (tosubclass.isInstance(o)) {
								list.add(classname);
							}
						} catch (InstantiationException iex) {
							// We try to instanciate an interface
							// or an object that does not have a 
							// default constructor
						} catch (IllegalAccessException iaex) {
							// The class is not public
						}
					}
				}
			} else {
				// it may be a jar file:
				try {
					JarURLConnection conn =
						(JarURLConnection) url.openConnection();
					String starts = conn.getEntryName();
					JarFile jfile = conn.getJarFile();
					Enumeration e = jfile.entries();
					while (e.hasMoreElements()) {
						ZipEntry entry = (ZipEntry) e.nextElement();
						String entryname = entry.getName();
						if (entryname.startsWith(starts)
							&& (entryname.lastIndexOf('/') <= starts.length())
							&& entryname.endsWith(".class")) {
							String classname =
								entryname.substring(0, entryname.length() - 6);
							if (classname.startsWith("/"))
								classname = classname.substring(1);
							classname = classname.replace('/', '.');
							try {
								// Try to create an instance of the object
								Object o =
									Class.forName(classname).newInstance();
								if (tosubclass.isInstance(o)) {
									list.add(
										classname.substring(
											classname.lastIndexOf('.') + 1));
								}
							} catch (InstantiationException iex) {
								// We try to instanciate an interface
								// or an object that does not have a 
								// default constructor
							} catch (IllegalAccessException iaex) {
								// The class is not public
							}
						}
					}
				} catch (IOException ioex) {
					System.err.println(ioex);
				}
			}
		}
		return list;
	}
} // SubClass
