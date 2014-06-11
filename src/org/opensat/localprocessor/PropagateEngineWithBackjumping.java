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
 * Used when Backljumping is enable
 * 
 * @author audemard
 *
 */
public class PropagateEngineWithBackjumping implements IPropagator {

	private IClause workingReason;

	private boolean propagateTrialRec(ICNF f, LookAhead lp, ILiteral lit) {
		IClause reason = null;

		f.propagateAssignment(lit);
		lp.managePropagatedLiteral(lit);
		boolean res;
		if (f.hasNullClause()) {
			res = false;
			workingReason = f.getLastNullClause(lit.opposite());
		} else {
			try {
				if ((f.hasUnitClause())) {
					reason = f.nextUnitClause();
					ILiteral l = reason.firstUnassignedLiteral();
					res = propagateTrialRec(f, lp, l);
					if (!res) {
						if (workingReason.contains(l.opposite())) {
							workingReason = workingReason.resolveWith(reason);
						}
					}
				} else {
					res = true;
				}

			} catch (ContradictionFoundException e) {
				workingReason = f.getLastNullClause(lit.opposite());
				res = false;
			}
		}
		f.unpropagateAssignment(lit);
		return res;
	}
	
	/**
	 * @see org.opensat.algs.propagateTrial(ICNF,LookAhead,ILiteral)
	 */

	public boolean propagateTrial(ICNF f, LookAhead lp, ILiteral lit) {
		return propagateTrialRec(f, lp, lit);
	}

	/**
	 * @see org.opensat.algs.getReason()
	 */

	public IClause getReason() {

		return workingReason;
	}
}
