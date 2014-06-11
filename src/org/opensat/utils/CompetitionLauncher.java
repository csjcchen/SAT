/*
 * The OpenSAT project
 * Copyright (c) 2002, Joao Marques-Silva and Daniel Le Berre
 * 
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
 * Created on 21 janv. 2003
 * 
 */
package org.opensat.utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.opensat.Default;
import org.opensat.ICertifiable;
import org.opensat.IFormula;
import org.opensat.IParser;
import org.opensat.ISolver;
import org.opensat.ParseFormatException;
import org.opensat.TimeoutException;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class CompetitionLauncher {

	public static final int SATISFIABLE = 10;
	public static final int UNSATISFIABLE = 20;
	public static final int UNKNOWN = 0;

	public static void main(String[] args) {
		ISolver solver = Default.solver();
		solver.setHeuristic(Default.heuristic());
		IFormula formula = Default.cnf();
		IParser parser = Default.parser();

		String timeoutstr = System.getProperty("TIMEOUT");
		if ((timeoutstr!=null)&&!timeoutstr.equals("")) {
			int timeout = Integer.parseInt(timeoutstr);
			solver.setTimeout(timeout);
		}

		System.out.println("c solving " + args[0]);
		try {
			parser.parseInstance(args[0], formula);
			if (solver.solve(formula)) {
				System.out.println("s SATISFIABLE");
				System.out.println("v " + ((ICertifiable)solver).getCertificate());
				System.exit(SATISFIABLE);
			} else {
				System.out.println("s UNSATISFIABLE");
				System.exit(UNSATISFIABLE);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + args[0]);
		} catch (IOException e) {
			System.err.println(e);
		} catch (ParseFormatException e) {
			System.err.println(e);
		} catch (TimeoutException e) {
		}
		System.out.println("s UNKNOWN");
		System.exit(UNKNOWN);
	}
}
