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
 * Created on 3 déc. 2002
 * 
 */
package org.opensat.localprocessor;

import org.opensat.data.ContradictionFoundException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * Implementation of a propagator algorithm
 * Used when chronological backtracking is enable
 * 
 * @author audemard
 *
 */
public class PropagateEngine implements IPropagator {

	/**
	 * @see org.opensat.algs.propagateTrial(ICNF,LookAhead,ILiteral)
	 */

	public boolean propagateTrial(ICNF f, LookAhead lp, ILiteral lit) {
		f.propagateAssignment(lit);
		//System.out.print(lit + " ");
		lp.managePropagatedLiteral(lit);
		boolean res;
		if (f.hasNullClause()) {
			res = false;
		} else {
			try {
				if (f.hasUnitClause()) {
					ILiteral l = f.nextUnitClause().firstUnassignedLiteral();
					res = propagateTrial(f, lp, l);
				} else {
					res = true;
				}

			} catch (ContradictionFoundException e) {
				res = false;
			}
		}
		f.unpropagateAssignment(lit);

		return res;
	}

	/**
	 * @see org.opensat.algs.getReason()
	 * When chronological backtracking is used : reason is not used
	 */

	public IClause getReason() {
		return null;
	}

}
