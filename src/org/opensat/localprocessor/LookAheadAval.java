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
 * Created on 2 d???c. 2002
 * 
 */
package org.opensat.localprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.opensat.TimeoutException;
import org.opensat.algs.DPLLStats;
import org.opensat.algs.SatSolverAdapter;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * Class responsability.
 * 
 * @author leberre
 *
 */
public class LookAheadAval extends LookAhead {

	private IPropagator engine;
	private List possibleLit;

	public LookAheadAval(
		SatSolverAdapter dpll,
		IPropagator engine,
		ILocalProcessor nextprocess) {
		super(dpll, engine, nextprocess);
		this.engine = engine;
		possibleLit = new ArrayList();
	}

	private void initPossible(ICNF f) {

		// mark literals in binary clauses relevant
		Iterator clauses = f.activeClauseIterator();
		while (clauses.hasNext()) {
			IClause clause = (IClause) clauses.next();
			if (clause.isBinary()) {
				clause.setRelevance(true);
			}
		}
		// pick up relevant literals
		possibleLit.clear();
		ILiteral [] vars = f.getVocabulary().getVariables();
		for(int i=0;i<vars.length;i++) {
			ILiteral lit = vars[i];
			if (lit.isUnassigned() && lit.isRelevant()) {
				possibleLit.add(lit);
				lit.setRelevance(false);
			}
		}
		// all the literals should be irrelevant now.
	}

	/**
	 * @see org.opensat.algs.ISatHeuristic#choose(ICNF)
	 */
	public boolean process(ICNF f) throws TimeoutException {
		boolean modified = false;
		// Init possibleLit
		initPossible(f);

		while(possibleLit.size()>0) {
			if ((!(f.hasNullClause()))) {
				ILiteral l = (ILiteral) possibleLit.get(0);
				if (l.isUnassigned()) {
					if (!engine.propagateTrial(f, this, l.opposite())) {
						dpll.fix(f, l, engine.getReason());
						modified = true;
						((DPLLStats)(dpll.getStatistics())).newFailed();
						dpll.propagateUnitClauses(f);
					}
				}
			}
		}
		
		if (!modified && (nextprocess != null)) {
			return nextprocess.process(f);
		}
		return modified;
	}

	public void managePropagatedLiteral(ILiteral l) {
		// Literal l is propagated 
		// Remove the opposite
		possibleLit.remove(l.opposite());
	}

}
