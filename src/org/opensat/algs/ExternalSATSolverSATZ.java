/*
 * The OpenSAT project
 * Copyright (c) 2002-2003, Joao Marques-Silva and Daniel Le Berre
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
 * Created on 8 janv. 2003
 * 
 */
package org.opensat.algs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author roussel
 *
 * run SATZ
 */
public class ExternalSATSolverSATZ extends AbstractExternalSATSolver  {

	/**
	 * @see org.opensat.algs.IExternalSATSolver#isSatisfiable()
	 */
	public boolean isSatisfiable() {
		try {
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(proc.getInputStream()));

			String line = in.readLine();

			return line.indexOf("unsatisfiable") < 0;
		} catch (IOException e) {
			throw new RuntimeException("failed to run solver :"+getCmdLine());
		}
	}
	/**
	 * @see org.opensat.algs.AbstractExternalSATSolver#getCmdLine()
	 */
	protected String getCmdLine() {
		//return "satz /dev/stdin";
		return "tee /tmp/duc";
	}		
}
