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
package org.opensat.algs;

import java.util.Iterator;

import org.opensat.IFormula;
import org.opensat.IHeuristic;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;

/**
 * @author artois
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface ISatHeuristic extends IHeuristic {

    /**
     * This method is called before search in order to initialize the heuristic.
     * 
     * @param f the formula on which to choose a literal.
     */
    void init(IFormula f);

    /**
     * This method is called when a literal is satisfied during the search
     * (branching or unit propagation for instance).
     * 
     * @param f
     * @param lit the literal being satisfied.
     */
    void onSatisfyingLiteral(IFormula f, ILiteral lit);

    /**
     * This method is called when backtracking.
     * 
     * @param f
     * @param l the literal satisfied becoming unassigned.
     */
    void onBacktrackingLiteral(IFormula f, ILiteral l);

    /**
     * This method is called when a new clause is learned during search.
     * 
     * @param f
     * @param c
     */
    void onLearnedClause(IFormula f, IClause c);

    /**
     * Method onRestart.
     * @param f
     */
    void onRestart(IFormula f);

    /**
     * Choose a literal to pick up among the unassigned
     * literals of the formula.
     * 
     * @param f
     * @return ILiteral
     */
    ILiteral choose(IFormula f);

    /**
     * Pick up a literal among that iterator on literals.
     * 
     * @param it an iterator on ILiterals
     * @return ILiteral
     * @see #choose(ILiteral []) 
     */
    ILiteral choose(Iterator it);

    /**
     * Pick up a literal among the variables.
     * 
     * @param vars the variables used in the formula
     * @return the chosen literal
     * @see org.opensat.data.IVocabulary#getVariables()
     */
    ILiteral choose(ILiteral[] vars);

}
