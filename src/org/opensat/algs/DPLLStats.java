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
 * Created on 8 janv. 2003
 * 
 */
package org.opensat.algs;

import org.opensat.IStatistics;

/**
 * Statistics returned by SAT solvers.
 * 
 * @author leberre
 *
 */
public class DPLLStats implements IStatistics {

    /**
     * Constructor for DPLLStats.
     */
    public DPLLStats() {
        super();
    }

    private int decisions;
    private int unitclauses;
    private int skippingLit;
    private int failedLit;
    private int pureLit;
    private int learnedclauses;
    private int restarts;

    public void newDecision() {
        decisions++;
    }

    public void newUnitClause() {
        unitclauses++;
    }

    public void newSkipping() {
        skippingLit++;
    }

    public void newFailed() {
        failedLit++;
    }

    public void newPure() {
        pureLit++;
    }

    public void newLearnedClause() {
        learnedclauses++;
    }

    public void newRestart() {
        restarts++;
    }

    public int getPure() {
        return pureLit;
    }

    public int getDecisions() {
        return decisions;
    }

    public int getFailed() {
        return failedLit;
    }

    public int getUnitclauses() {
        return unitclauses;
    }

    /**
     * Return the number of skipped literals
     * @return int
     */
    public int getSkipping() {
        return skippingLit;
    }

    /**
     * Return the number of learned clauses.
     */
    public void clear() {
        decisions = 0;
        unitclauses = 0;
        skippingLit = 0;
        failedLit = 0;
        pureLit = 0;
        learnedclauses = 0;
        restarts = 0;
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String st = "\nDEC: " + decisions + "\tUNIT: " + unitclauses;
        if (skippingLit > 0)
            st = st + "\tSkipping: " + skippingLit;
        if (failedLit > 0)
            st = st + "\tFailed: " + failedLit;
        if (pureLit > 0)
            st = st + "\tPure: " + pureLit;
        if (learnedclauses > 0)
            st = st + "\tLearned: " + learnedclauses;
        if (restarts > 0)
            st = st + "\tRestarts: " + restarts;

        return st;
    }

}
