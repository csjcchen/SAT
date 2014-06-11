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
 * Created on 1 d?c. 2002
 * 
 */
package org.opensat.heuristics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.opensat.IFormula;
import org.opensat.algs.ISatHeuristic;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * That heuristics picks randomly one unassigned positive literal from
 * the ICNF.
 * 
 * @author leberre
 *
 */
public class RandomChoice implements ISatHeuristic {

    /**
     * Constructor for RandomChoice.
     */
    public RandomChoice() {
        super();
    }

    /**
     * @see org.opensat.heuristics.ISatHeuristic#choose(ICNF)
     */
    public ILiteral choose(IFormula f) {
        return choose(((ICNF) f).getVocabulary().variableIterator());
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(java.util.Iterator)
     */
    public ILiteral choose(Iterator it) {
        unassignedLiterals.clear();
        while (it.hasNext()) {
            ILiteral lit = (ILiteral) it.next();
            if (lit.isUnassigned()) {
                unassignedLiterals.add(lit);
            }
        }

        if (unassignedLiterals.isEmpty()) {
            return null;
        } else {
            return (ILiteral) unassignedLiterals.get(
                rand.nextInt(unassignedLiterals.size()));
        }
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.data.ILiteral)
     */
    public ILiteral choose(ILiteral[] vars) {
        unassignedLiterals.clear();
        for (int i = 0; i < vars.length; i++) {
            if (vars[i].isUnassigned()) {
                unassignedLiterals.add(vars[i]);
            }
        }

        if (unassignedLiterals.isEmpty()) {
            return null;
        } else {
            return (ILiteral) unassignedLiterals.get(
                rand.nextInt(unassignedLiterals.size()));
        }
    }

    private List unassignedLiterals = new ArrayList();
    private Random rand = new Random();

    /**
     * @see org.opensat.ISatHeuristic#init(org.opensat.IFormula)
     */
    public void init(IFormula f) {
    }

    /**
     * @see org.opensat.ISatHeuristic#onBacktrackingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onBacktrackingLiteral(IFormula f, ILiteral l) {
    }

    /**
     * @see org.opensat.ISatHeuristic#onLearnedClause(org.opensat.IFormula, null)
     */
    public void onLearnedClause(IFormula f, IClause c) {
    }

    /**
     * @see org.opensat.ISatHeuristic#onSatisfyingLiteral(org.opensat.IFormula, org.opensat.data.ILiteral)
     */
    public void onSatisfyingLiteral(IFormula f, ILiteral lit) {
    }

    /**
     * @see org.opensat.ISatHeuristic#onRestart(IFormula)
     */
    public void onRestart(IFormula f) {
    }

}
