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
 * Created on 2 d?c. 2002
 * 
 */
package org.opensat.localprocessor;

import org.opensat.TimeoutException;
import org.opensat.algs.DPLLStats;
import org.opensat.algs.SatSolverAdapter;
import org.opensat.data.ICNF;
import org.opensat.data.ILiteral;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class LookAhead implements ILocalProcessor {

	public LookAhead(
		SatSolverAdapter dpll,
		IPropagator engine,
		ILocalProcessor nextprocess) {
		this.dpll = dpll;
		this.nextprocess = nextprocess;
		this.engine = engine;

	}

	/**
	 * @see org.opensat.algs.ISatHeuristic#choose(ICNF)
	 */
	public boolean process(ICNF f) throws TimeoutException {
		boolean modified = false;
		ILiteral [] vars= f.getVocabulary().getVariables();
		int i=0;
		while (i<vars.length && !(f.hasNullClause())) {
			ILiteral l = vars[i++];
			if (l.isUnassigned()) {
				if (!engine.propagateTrial(f, this, l)) {
					dpll.fix(f, l.opposite(), engine.getReason());
					modified = true;
					((DPLLStats)(dpll.getStatistics())).newFailed();
					dpll.propagateUnitClauses(f);
					continue;
				}
				if (!engine.propagateTrial(f, this, l.opposite())) {
					dpll.fix(f, l, engine.getReason());
					modified = true;
					dpll.propagateUnitClauses(f);
					((DPLLStats)(dpll.getStatistics())).newFailed();
				}
			}
		}
		if (!modified && (nextprocess != null)) {
			return nextprocess.process(f);
		}
		return modified;
	}

	public void managePropagatedLiteral(ILiteral l) {
	}

	protected ILocalProcessor nextprocess;
	protected SatSolverAdapter dpll;
	protected IPropagator engine;
}
