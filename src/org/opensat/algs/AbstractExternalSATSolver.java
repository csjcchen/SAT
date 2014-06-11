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
 * Created on 2 févr. 2003
 * 
 */
package org.opensat.algs;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.opensat.IFormula;
import org.opensat.IHeuristic;
import org.opensat.ISolver;
import org.opensat.IStatistics;
import org.opensat.TimeoutException;
import org.opensat.data.ICNF;

/**
 * A wrapper for external SAT solvers.
 * 
 * @author roussel
 *
 */
public abstract class AbstractExternalSATSolver
    implements IExternalSATSolver, ISolver {

    protected Process proc; // the process that runs the SAT solver

    /*
     * @see org.opensat.algs.IExternalSATSolver#endOfFormula()
     */
    public void endOfFormula() {
        try {
            getOutputStream().close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * get the name of the program to use
     * 
     * @return String the name of the program to run with necessary 
     * arguments to read the formula on the standart input
     */
    protected abstract String getCmdLine();

    /*
     * @see org.opensat.algs.IExternalSATSolver#getOutputStream()
     */
    public OutputStream getOutputStream() {
        return proc.getOutputStream();
    }

    /*
     * @see org.opensat.ISolver#getStatistics()
     */
    public IStatistics getStatistics() {
        throw new UnsupportedOperationException();
    }

    /*
     * @see org.opensat.algs.IExternalSATSolver#isSatisfiable()
     */
    public boolean isSatisfiable() {
        return false;
    }

    /*
     * @see org.opensat.ISolver#setHeuristic(org.opensat.ISatHeuristic)
     */
    public void setHeuristic(IHeuristic h) {
        throw new UnsupportedOperationException();
    }

    /*
     * @see org.opensat.ISolver#setTimeout(int)
     */
    public void setTimeout(int seconds) {
        throw new UnsupportedOperationException();
    }

    /*
     * @see org.opensat.ISolver#solve(org.opensat.IFormula)
     */
    public boolean solve(IFormula formula) throws TimeoutException {
        startSolver();

        ICNF cnf = (ICNF) formula;

        cnf.display(new PrintWriter(getOutputStream()));

        return isSatisfiable();
    }

    /*
     * @see org.opensat.algs.IExternalSATSolver#startSolver()
     */
    public void startSolver() {
        try {
            proc = Runtime.getRuntime().exec(getCmdLine());
        } catch (IOException e) {
            throw new RuntimeException(
                "failed to run external solver : " + getCmdLine());
        }
    }
}
