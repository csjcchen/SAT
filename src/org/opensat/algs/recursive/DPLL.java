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
 * Created on 24 nov. 2002
 * 
 */
package org.opensat.algs.recursive;
import org.opensat.TimeoutException;
import org.opensat.algs.Certificate;
import org.opensat.algs.ISatHeuristic;
import org.opensat.algs.SatSolverAdapter;
import org.opensat.data.ContradictionFoundException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;


/**
 * This is a recursive version of the basic DPLL
 * original algorithm.
 *
 * @author artois
 *
 */
public class DPLL extends SatSolverAdapter {

		public DPLL() {
		super();
	}

	public DPLL(ISatHeuristic h) {
		super(h);
	}

	/**
	 * Unit propagation.
	 * 
	 * @param f
	 * @return boolean
	 */
	public boolean propagateUnitClauses(ICNF f) throws TimeoutException {
		if (f.hasNullClause()) {
			return false;
		}
		try {
			boolean result;
			if (f.hasUnitClause()) {
				ILiteral lit = f.nextUnitClause().firstUnassignedLiteral();
				f.propagateAssignment(lit);
                heuristic.onSatisfyingLiteral(f,lit);
				statistics.newUnitClause();
				result = propagateUnitClauses(f);
				f.unpropagateAssignment(lit);
                heuristic.onBacktrackingLiteral(f,lit);
				if (result) {
					assert certificate != null;
					certificate.add(lit);
				}
			} else {
				result = branch(f);
			}
			return result;
		} catch (ContradictionFoundException e) {
			return false;
		}
	}

	public boolean branch(ICNF f) throws TimeoutException {
		boolean result;
		statistics.newDecision();
		ILiteral lit = heuristic.choose(f);
		// assert lit != null;
		if (lit==null) {
			assert f.isSatisfied();
			certificate = new Certificate(f.getVocabulary());
			return true;
		}
		fix(f,lit);
		result = startSearch(f);
		f.unpropagateAssignment(lit);

		if (result) {
			assert certificate != null;
			certificate.add(lit);
		} else {
			fix(f,lit.opposite());
			result = startSearch(f);
			f.unpropagateAssignment(lit.opposite());
			if (result) {
				assert certificate != null;
				certificate.add(lit.opposite());
			}
		}
		return result;
	}
	
	/**
	 * @see org.opensat.algs.SatSolverAdapter#fix(org.opensat.data.ICNF, org.opensat.data.ILiteral)
	 */
	public void fix(ICNF f, ILiteral l) {
		f.propagateAssignment(l);
	}
	
  
	protected boolean startSearch(ICNF f) throws TimeoutException {
		
		System.out.println("*************************");
		org.opensat.data.simple.CNFSimpleImplAltWL aw_f = (org.opensat.data.simple.CNFSimpleImplAltWL)f; 
		aw_f.showAssignments();
		
		if (stopASAP) {
			throw new TimeoutException();
		}
		return propagateUnitClauses(f);
	}
	/**
	 * @see org.opensat.algs.SatSolverAdapter#fix(org.opensat.data.ICNF, org.opensat.data.ILiteral, org.opensat.data.IClaus)
     * Her it is useless to have a reason
     */
	public void fix(ICNF f, ILiteral l, IClause reason) {
		fix(f,l);
	}

}
