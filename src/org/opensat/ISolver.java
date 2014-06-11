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
 * Created on 28 janv. 2003
 * 
 */
package org.opensat;




/**
 * Basic services to be implemented by a solver.
 * 
 * @author leberre
 *
 */
public interface ISolver {

    /**
     * Method setTimeout.
     * @param seconds
     */
    void setTimeout(int seconds);
    
    
    /**
     * To set a different heuristic to the ISolver.
     * 
     * @param h
     */
    void setHeuristic(IHeuristic h);
    
    /**
     * A solver is aimed at solving a formula.
     *  
     * @param formula the formula to solve.
     * @return boolean true is the formula is solved positively (SAT for a SAT formula,
     * TRUE for a QBF, etc.), false when the formula is solved negatively (UNSAT for a SAT formula)..
     * @throws TimeoutException when the formula cannot be solved within the allowed time or resources.
     */
    boolean solve(IFormula formula) throws TimeoutException;
    
    
    /**
     * Return some statistics concerning the solver.
     * 
     * @return IStatistics
     */
    IStatistics getStatistics();

}
