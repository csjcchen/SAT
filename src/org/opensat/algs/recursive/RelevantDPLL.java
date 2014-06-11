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
 * Created on 24 nov. 2002
 * 
 */
package org.opensat.algs.recursive;
import java.util.Iterator;

import org.opensat.TimeoutException;
import org.opensat.data.ContradictionFoundException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.algs.DPLLStats;
import org.opensat.algs.ISatHeuristic;
/**
 * @author artois
 *
 * This is a recursive version of the basic DPLL
 * original algorithm.
 */
public class RelevantDPLL extends DPLL {

    public RelevantDPLL() {
        super();
    }

    public RelevantDPLL(ISatHeuristic h) {
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
            markFalsifiedClauses(f);
            return false;
        }
        try {
            boolean result;
            if (f.hasUnitClause()) {
                IClause cl = f.nextUnitClause();
                // literals in that clause are relevant since 
                // the clause would be falsified by propagating
                // lit.opposite().
                cl.setRelevance(true);
                ILiteral lit = cl.firstUnassignedLiteral();
                f.propagateAssignment(lit);
                heuristic.onSatisfyingLiteral(f,lit);
                statistics.newUnitClause();
                result = propagateUnitClauses(f);
                f.unpropagateAssignment(lit);
                heuristic.onBacktrackingLiteral(f,lit);
            } else {
                result = branch(f);
            }
            return result;
        } catch (ContradictionFoundException e) {
            markFalsifiedClauses(f);
            return false;
        }
    }

    /**
     * Mark all the literals appearing in falsified clauses as relevant.
     * @param f
     */
    private void markFalsifiedClauses(ICNF f) {
        Iterator it = f.getNullClauses();
        while (it.hasNext()) {
            ((IClause) it.next()).setRelevance(true);
        }
    }

    public boolean branch(ICNF f) throws TimeoutException {
        boolean result;
        statistics.newDecision();
        ILiteral lit = heuristic.choose(f);
        if (lit == null) {
            return true;
        }
        f.propagateAssignment(lit);
        heuristic.onSatisfyingLiteral(f,lit);
        lit.setRelevance(false);
        result = startSearch(f);
        f.unpropagateAssignment(lit);
        heuristic.onBacktrackingLiteral(f,lit);
        if (!result) {
            if (lit.isRelevant()) {
                f.propagateAssignment(lit.opposite());
                heuristic.onSatisfyingLiteral(f,lit.opposite());
                result = startSearch(f);
                f.unpropagateAssignment(lit.opposite());
                heuristic.onBacktrackingLiteral(f,lit.opposite());
            } else {
                ((DPLLStats) statistics).newSkipping();
            }
        }
        return result;
    }

}
