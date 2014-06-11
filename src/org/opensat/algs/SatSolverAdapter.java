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

import java.util.Timer;
import java.util.TimerTask;

import org.opensat.ICertifiable;
import org.opensat.ICertificate;
import org.opensat.IFormula;
import org.opensat.IHeuristic;
import org.opensat.IStatistics;
import org.opensat.TimeoutException;
import org.opensat.data.ICNF;
import org.opensat.data.IClause;
import org.opensat.data.ILiteral;
import org.opensat.heuristics.TwoSidedJW;

/**
 * Basic services shared by most SAT solvers.
 * 
 * @author leberre
 *
 */
public abstract class SatSolverAdapter
    extends SolverAdapter
    implements ICertifiable {

    protected Certificate certificate;

    protected ISatHeuristic heuristic;

    protected DPLLStats statistics;

    public SatSolverAdapter() {
        this(new TwoSidedJW());
    }

    public SatSolverAdapter(ISatHeuristic h) {
        super();
        setHeuristic(h);
        statistics = new DPLLStats();
    }

    /**
     * Method fix.
     * Fix the literal l with the reason in the formula f
     * The reason is used only with backjumping algorithm
     * @param f
     * @param l
     * @param reason
     */
    abstract public void fix(ICNF f, ILiteral l, IClause reason);

    public ICertificate getCertificate() {
        return certificate;
    }

    /**
     * @see org.opensat.algs.ISolver#getStatistics()
     */
    public IStatistics getStatistics() {
        return statistics;
    }

    /**
     * The basic service available from a SAT solver:
     * if a given formula f satisfiable?
     * @param f a formula
     * @return true iff the formula is satisfiable.
     */
    protected boolean isSatisfiable(ICNF f) throws TimeoutException {
        stopASAP = false;
        statistics.clear();
        TimerTask stopMe = new TimerTask() {
            public void run() {
                stopASAP = true;
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(stopMe, timeout * 1000);

        heuristic.init(f);
        boolean sat = startSearch(f);

        timer.cancel();

        return sat;
    }

    /**
     * Method propagateUnitClauses.
     * Propagate unit clauses in the formula f
     * return false if a contradiction occured
     *  
     * @param f
     * @return boolean
     * @throws TimeoutException
     */
    abstract public boolean propagateUnitClauses(ICNF f)
        throws TimeoutException;

    public void setHeuristic(IHeuristic h) {
        if (h instanceof ISatHeuristic) {
            heuristic = (ISatHeuristic) h;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @see org.opensat.algs.ISolver#solve(java.lang.Object)
     */
    public boolean solve(IFormula formula) throws TimeoutException {
        if (formula instanceof ICNF) {
            return isSatisfiable((ICNF) formula);
        }
        throw new UnsupportedOperationException();

    }

    protected abstract boolean startSearch(ICNF f) throws TimeoutException;

}
