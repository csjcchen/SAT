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

import java.io.OutputStream;

/**
 * @author roussel
 *
 * this interface let us run an external SAT solver
 * 
 * 1) use startSolver() to launch the external program
 * 2) write to getOutputStream() the formula in DIMACS format
 * 3) call isSatifiable() to get the answer from the external SAT Solver
 * 
 */
public interface IExternalSATSolver {

	/**
	 * launch the external SAT solver
	 */
	void startSolver();
	
	/**
	 * get the stream to which we must write the formula
	 * 
	 * @return OutputStream to which the formula must be written in DIMACS format
	 */
	OutputStream getOutputStream();
	
	/**
	 * tell the external solver that it has got all clauses
	 */
	void endOfFormula();
	
	/**
	 * get the answer from the external SAT solver
	 * 
	 * @return boolean true iff the formula is satisfiable
	 */
	boolean isSatisfiable();
}
