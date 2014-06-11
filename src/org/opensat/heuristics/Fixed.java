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
 * Created on 28 janv. 2003
 * 
 */
package org.opensat.heuristics;

import java.util.Iterator;

import org.opensat.IFormula;
import org.opensat.algs.ISatHeuristic;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * That heuristics returns the first unassigned literal of the iterator.
 * Very fast, but not really intelligent.
 * 
 * @author leberre
 *
 */
public class Fixed implements ISatHeuristic {

    /**
     * Constructor for Fixed.
     */
    public Fixed() {
        super();
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(ICNF)
     */
    public ILiteral choose(IFormula f) {
        return choose(((ICNF) f).getVocabulary().getVariables());
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(Iterator)
     */
    public ILiteral choose(Iterator it) {
        while (it.hasNext()) {
            ILiteral lit = (ILiteral) it.next();
            if (lit.isUnassigned()) {
                return lit;
            }
        }
        return null;
    }

    /**
     * @see org.opensat.ISatHeuristic#choose(org.opensat.data.ILiteral)
     */
    public ILiteral choose(ILiteral[] vars) {
        for (int i = 0; i < vars.length; i++) {
            if (vars[i].isUnassigned()) {
                return vars[i];
            }
        }
        return null;
    }

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
